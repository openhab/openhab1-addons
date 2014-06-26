/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jpa.internal;

import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.types.UnDefType;
import org.openhab.persistence.jpa.internal.model.JpaPersistentItem;
import org.openhab.persistence.jpa.internal.model.JpaPersistentOtherItem;
import org.openhab.persistence.jpa.internal.model.JpaPersistentStringItem;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /** 
 * @author Manfred Bergmann
 * @since 1.6.0
 */
public class JpaPersistenceService implements QueryablePersistenceService, ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(JpaPersistenceService.class);

	private boolean initialized = false;
	protected ItemRegistry itemRegistry;

	private static final String CFG_CONNECTION_URL = "url";
	private static final String CFG_DRIVER_CLASS = "driver";
	private static final String CFG_USERNAME = "user";
	private static final String CFG_PASSWORD = "password";
	
	private String dbConnectionUrl = "";
	private String dbDriverClass = "";
	private String dbUserName = "";
	private String dbPassword = "";
	
	private EntityManagerFactory emf;
	
	public void activate() {
		
	}

	public void deactivate() {
		logger.debug("JPA persistence bundle stopping.");
		closeEntityManagerFactory();
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	@Override
	public String getName() {
		return "jpa";
	}

	@Override
	public void store(Item item) {
		store(item, null);
	}

	@Override
	public void store(Item item, String alias) {
		if (item.getState() instanceof UnDefType) {
			return;
		}

		if (initialized == false) {
			logger.warn("Jpa not initialized");
			return;
		}

		if (!isEntityManagerFactoryOpen()) {
			try {
				initializeEntityManagerFactory();				
			} catch (Exception e) {
				logger.error("Error while initializing database connection!");
				logger.error(e.getMessage());
				return;			
			}
		}

		// determine item name to be stored
		String name = (alias != null) ? alias : item.getName();
		
		JpaPersistentItem pItem;
		if(StateHelper.isOtherStateType(item.getState())) {
			pItem = new JpaPersistentOtherItem();
			try {
				((JpaPersistentOtherItem)pItem).setValue(StateHelper.toOther(item.getState()));
			} catch (Exception e) {
				logger.error("Error on converting item value type: " + item.getState() + " to string!");
				logger.error(e.getMessage());
				return;
			}
		} else {
			pItem = new JpaPersistentStringItem();
			((JpaPersistentStringItem)pItem).setValue(StateHelper.toString(item.getState()));
		}
		pItem.setName(name);
		pItem.setRealName(item.getName());
		pItem.setTimestamp(new Date());

		EntityManager em = emf.createEntityManager();
		try {
			logger.debug("Persisting item...");
			// In RESOURCE_LOCAL calls to EntityManager require a begin/commit			
			em.getTransaction().begin();
			em.persist(pItem);
			em.getTransaction().commit();
			logger.debug("Persisting item...done");
		} catch (Exception e) {
			logger.error("Error on persisting item! Rolling back!");
			logger.error(e.getMessage());
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		
	}

	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		logger.debug("querying for historic item: " + filter.getItemName());
		
		if (!initialized) {
			return Collections.emptyList();			
		}

		if (!isEntityManagerFactoryOpen()) {
			try {
				initializeEntityManagerFactory();				
			} catch (Exception e) {
				logger.error("Error while initializing database connection!");
				logger.error(e.getMessage());
				return Collections.emptyList();			
			}
		}

		String itemName = filter.getItemName();
		Item item = getItemFromRegistry(itemName);
		
		String entityName;
		if(StateHelper.isOtherStateType(item.getState())) {
			entityName = JpaPersistentOtherItem.class.getSimpleName();
		} else {
			entityName = JpaPersistentStringItem.class.getSimpleName();
		}

		String sortOrder;
		if(filter.getOrdering() == Ordering.ASCENDING) sortOrder = "ASC";
		else sortOrder = "DESC";

		boolean hasBeginDate = false;
		boolean hasEndDate = false;
		String queryString = "SELECT n FROM " + entityName + " n WHERE n.realName = :itemName";
		if(filter.getBeginDate() != null) {
			queryString += " AND n.timestamp >= :beginDate";
			hasBeginDate = true;
		}
		if(filter.getEndDate() != null) {
			 queryString += " AND n.timestamp <= :endDate";
			 hasEndDate = true;
		}
		queryString += " ORDER BY n.timestamp " + sortOrder;
		
		logger.debug("The query: " + queryString);

		EntityManager em = emf.createEntityManager();
		try {
			// In RESOURCE_LOCAL calls to EntityManager require a begin/commit
			em.getTransaction().begin();
			
			logger.debug("Creating query...");
			Query query = em.createQuery(queryString);
			query.setParameter("itemName", item.getName());
			if(hasBeginDate) query.setParameter("beginDate", filter.getBeginDate());
			if(hasEndDate) query.setParameter("endDate", filter.getEndDate());
			
			query.setFirstResult(filter.getPageNumber() * filter.getPageSize());
			query.setMaxResults(filter.getPageSize());
			logger.debug("Creating query...done");

			logger.debug("Retrieving result list...");
			@SuppressWarnings("unchecked")
			List<JpaPersistentItem> result = (List<JpaPersistentItem>)query.getResultList();
			logger.debug("Retrieving result list...done");
			
			List<HistoricItem> historicList = JpaHistoricItem.fromResultList(result, item);
			em.getTransaction().commit();
			
			return historicList;
			
		} catch (Exception e) {
			logger.error("Error on querying database!");
			logger.error(e.getMessage(), e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		
		return Collections.emptyList();			
	}
	
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		logger.debug("Update config...");
		
		if(properties == null) {
			logger.error("Got a null properties object!");
			return;
		}
		
		updateConfigSettings(properties);

		// re-init connection
		closeEntityManagerFactory();
		try {
			initializeEntityManagerFactory();			
			initialized = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Reads config entries and sets the instance fields
	 * @param properties
	 * @throws ConfigurationException
	 */
	private void updateConfigSettings(Dictionary<String, ?> properties) throws ConfigurationException {
		String param = (String)properties.get(CFG_CONNECTION_URL);
		logger.debug("url: " + param);
		if(param == null) {
			logger.error("Connection url is required in openhab.cfg!");
			throw new ConfigurationException(CFG_CONNECTION_URL, "Connection url is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			logger.error("Empty connection url in openhab.cfg!");
			throw new ConfigurationException(CFG_CONNECTION_URL, "Empty connection url in openhab.cfg!");
		}
		dbConnectionUrl = (String)param;

		param = (String)properties.get(CFG_DRIVER_CLASS);
		logger.debug("driver: " + param);
		if(param == null) {
			throw new ConfigurationException(CFG_DRIVER_CLASS, "Driver class is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			throw new ConfigurationException(CFG_DRIVER_CLASS, "Empty driver class in openhab.cfg!");
		}
		dbDriverClass = (String)param;
		
		if(properties.get(CFG_USERNAME) == null) {
			logger.info(CFG_USERNAME + " was not specified!");
		}
		dbUserName = (String)properties.get(CFG_USERNAME);
		
		if(properties.get(CFG_PASSWORD) == null) {
			logger.info(CFG_PASSWORD + " was not specified!");
		}
		dbPassword = (String)properties.get(CFG_PASSWORD);		
	}

	protected EntityManagerFactory newEntityManagerFactory() {
		logger.debug("Creating EntityManagerFactory...");
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.url", dbConnectionUrl);
		properties.put("javax.persistence.jdbc.driver", dbDriverClass);
		properties.put("javax.persistence.jdbc.user", dbUserName);
		properties.put("javax.persistence.jdbc.password", dbPassword);
		
		EntityManagerFactory fac = Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
		logger.debug("Creating EntityManagerFactory...done");

		return fac;
	}
	
	protected void initializeEntityManagerFactory() {
		logger.debug("Initializing EntityManagerFactory...");
		emf = newEntityManagerFactory();
		logger.debug("Initializing EntityManagerFactory...done");
	}
	
	protected void closeEntityManagerFactory() {
		if(emf != null) {
			emf.close();
		}
		logger.debug("Closing down entity objects...done");
	}
	
	protected boolean isEntityManagerFactoryOpen() {
		return emf != null && emf.isOpen();
	}

	protected String getPersistenceUnitName() {
		return "default";
	}

	private Item getItemFromRegistry(String itemName) {
		Item item = null;
		try {
			if (itemRegistry != null) {
				item = itemRegistry.getItem(itemName);
			}
		} catch (ItemNotFoundException e1) {
			logger.error("Unable to get item type for {}", itemName);
			// Set type to null - data will be returned as StringType
			item = null;
		}
		return item;
	}

}
