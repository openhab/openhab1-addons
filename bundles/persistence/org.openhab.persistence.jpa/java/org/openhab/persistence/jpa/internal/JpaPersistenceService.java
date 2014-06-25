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
import java.util.Enumeration;
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

	private final String DB_CONNECTION_URL = "url";
	private final String DB_DRIVER_CLASS = "driver";
	private final String DB_USERNAME = "user";
	private final String DB_PASSWORD = "password";
	
	private String dbConnectionUrl = "";
	private String dbDriverClass = "";
	private String dbUserName = "";
	private String dbPassword = "";
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public void activate() {
		
	}

	public void deactivate() {
		logger.debug("JPA persistence bundle stopping.");
		closeDbConnection();
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

		if (!isDbConnectionOpen()) {
			try {
				initializeDbConnection();				
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

		try {
			logger.debug("Persisting item...");
			beginTransaction();
			em.persist(pItem);
			commitTransaction();
			logger.debug("Persisting item...done");
		} catch (Exception e) {
			logger.error("Error on persisting item! Rolling back!");
			logger.error(e.getMessage());
			rollbackTransaction();
		}
		
	}

	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		logger.debug("querying for historic item: " + filter.getItemName());
		
		if (!initialized) {
			return Collections.emptyList();			
		}

		if (!isDbConnectionOpen()) {
			try {
				initializeDbConnection();				
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

		try {
			Query query = em.createQuery(queryString);
			query.setParameter("itemName", item.getName());
			if(hasBeginDate) query.setParameter("beginDate", filter.getBeginDate());
			if(hasEndDate) query.setParameter("endDate", filter.getEndDate());
			
			query.setFirstResult(filter.getPageNumber() * filter.getPageSize());
			query.setMaxResults(filter.getPageSize());

			beginTransaction();
			@SuppressWarnings("unchecked")
			List<JpaPersistentItem> result = (List<JpaPersistentItem>)query.getResultList();
			commitTransaction();
			
			return JpaHistoricItem.fromResultList(result, item);
			
		} catch (Exception e) {
			logger.error("Error on querying database!");
			logger.error(e.getMessage());
			rollbackTransaction();
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
		
		Enumeration keys = properties.keys();
		while(keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) properties.get(key);
			logger.debug("key=" + key + ", value=" + value);
		}
		
		String param = (String)properties.get(DB_CONNECTION_URL);
		logger.debug("url: " + param);
		if(param == null) {
			logger.error("Connection url is required in openhab.cfg!");
			throw new ConfigurationException(DB_CONNECTION_URL, "Connection url is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			logger.error("Empty connection url in openhab.cfg!");
			throw new ConfigurationException(DB_CONNECTION_URL, "Empty connection url in openhab.cfg!");
		}
		dbConnectionUrl = (String)param;

		param = (String)properties.get(DB_DRIVER_CLASS);
		logger.debug("driver: " + param);
		if(param == null) {
			throw new ConfigurationException(DB_DRIVER_CLASS, "Driver class is required in openhab.cfg!");
		}
		if(StringUtils.isBlank(param)) {
			throw new ConfigurationException(DB_DRIVER_CLASS, "Empty driver class in openhab.cfg!");
		}
		dbDriverClass = (String)param;
		
		if(properties.get(DB_USERNAME) == null) {
			logger.info(DB_USERNAME + " was not specified!");
		}
		dbUserName = (String)properties.get(DB_USERNAME);
		
		if(properties.get(DB_PASSWORD) == null) {
			logger.info(DB_PASSWORD + " was not specified!");
		}
		dbPassword = (String)properties.get(DB_PASSWORD);

		// re-init connection
		closeDbConnection();
		try {
			initializeDbConnection();			
			initialized = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
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
	
	protected EntityManager newPersistenceManager() {
		logger.debug("Creating EntityManager...");
		EntityManager tempEm = emf.createEntityManager();
		logger.debug("Creating EntityManager...done");
		return tempEm;
	}
	
	protected void initializeDbConnection() {
		logger.debug("Opening db connection/creating PersistenceEntity objects...");
		emf = newEntityManagerFactory();
		em = newPersistenceManager();		
		logger.debug("Opening db connection/creating PersistenceEntity objects...done");
	}
	
	protected String getPersistenceUnitName() {
		return "default";
	}

	protected void closeDbConnection() {
		logger.debug("Closing down entity objects...");
		if(em != null) {
			em.close();
		}
		if(emf != null) {
			emf.close();
		}
		logger.debug("Closing down entity objects...done");
	}
	
	protected boolean isDbConnectionOpen() {
		return em != null && em.isOpen();
	}

	private void beginTransaction() {
		if(!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
	}
	
	private void commitTransaction() {
		em.getTransaction().commit();
	}
	
	private void rollbackTransaction() {
		if(em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
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
