/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.jpa.internal;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.types.UnDefType;
import org.openhab.persistence.jpa.internal.model.JpaPersistentItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /** 
	 JPA based implementation of QueryablePersistenceService.
	 
 * @author Manfred Bergmann
 * @since 1.6.0
 */
public class JpaPersistenceService implements QueryablePersistenceService {
	private static final Logger logger = LoggerFactory.getLogger(JpaPersistenceService.class);

	protected ItemRegistry itemRegistry;
	
	private EntityManagerFactory emf = null;
	
	/**
	 * lazy loading because update() is called after activate()
	 * @return
	 */
	protected EntityManagerFactory getEntityManagerFactory() {
		if(emf == null) {
			emf = newEntityManagerFactory();
		}
		return emf;
	}
	
	public void activate() {
		logger.debug("Activating jpa binding...");		
		logger.debug("Activating jpa binding...done");
	}

	/**
	 * Closes the EntityPersistenceFactory
	 */
	public void deactivate() {
		logger.debug("Deactivating jpa binding...");
		closeEntityManagerFactory();
		logger.debug("Deactivating jpa binding...done");
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
		logger.debug("Storing item: " + item.getName());
		
		if (item.getState() instanceof UnDefType) {
			logger.debug("This item is of undefined type. Cannot persist it!");
			return;
		}
		
		if(!JpaConfiguration.isInitialized) {
			logger.warn("Trying to create EntityManagerFactory but we don't have configuration yet!");
			return;
		}
		
		// determine item name to be stored
		String name = (alias != null) ? alias : item.getName();
		
		JpaPersistentItem pItem = new JpaPersistentItem();
		try {
			String newValue = StateHelper.toString(item.getState());
			pItem.setValue(newValue);
			logger.debug("Stored new value: {}", newValue);
		} catch (Exception e1) {
			logger.error("Error on converting state value to string: {}", e1.getMessage());
			return;
		}
		pItem.setName(name);
		pItem.setRealName(item.getName());
		pItem.setTimestamp(new Date());

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			logger.debug("Persisting item...");
			// In RESOURCE_LOCAL calls to EntityManager require a begin/commit			
			em.getTransaction().begin();
			em.persist(pItem);
			em.getTransaction().commit();
			logger.debug("Persisting item...done");
		} catch (Exception e) {
			logger.error("Error on persisting item! Rolling back!");
			logger.error(e.getMessage(), e);
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		
		logger.debug("Storing item...done");
	}

	@Override
	public Iterable<HistoricItem> query(FilterCriteria filter) {
		logger.debug("Querying for historic item: {}", filter.getItemName());
		
		if(!JpaConfiguration.isInitialized) {
			logger.warn("Trying to create EntityManagerFactory but we don't have configuration yet!");
			return Collections.emptyList();
		}

		String itemName = filter.getItemName();
		Item item = getItemFromRegistry(itemName);
		
		String sortOrder;
		if(filter.getOrdering() == Ordering.ASCENDING) sortOrder = "ASC";
		else sortOrder = "DESC";

		boolean hasBeginDate = false;
		boolean hasEndDate = false;
		String queryString = "SELECT n FROM " + JpaPersistentItem.class.getSimpleName() + " n WHERE n.realName = :itemName";
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

		EntityManager em = getEntityManagerFactory().createEntityManager();
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
			if(historicList != null) {
				logger.debug(String.format("Convert to HistoricItem: %d", historicList.size()));
			}
            
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
	
	/**
	 * Creates a new EntityManagerFactory with properties read from openhab.cfg via JpaConfiguration.
	 * @return initialized EntityManagerFactory
	 */
	protected EntityManagerFactory newEntityManagerFactory() {
		logger.debug("Creating EntityManagerFactory...");
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.url", JpaConfiguration.dbConnectionUrl);
		properties.put("javax.persistence.jdbc.driver", JpaConfiguration.dbDriverClass);
		if(JpaConfiguration.dbUserName != null) {
		    properties.put("javax.persistence.jdbc.user", JpaConfiguration.dbUserName);
		}
		if(JpaConfiguration.dbPassword != null) {
		    properties.put("javax.persistence.jdbc.password", JpaConfiguration.dbPassword);
		}
		if(JpaConfiguration.dbUserName != null && JpaConfiguration.dbPassword == null) {
			logger.warn("JPA persistence - it is recommended to use a password to protect data store");
		}
		if(JpaConfiguration.dbSyncMapping != null && !StringUtils.isBlank(JpaConfiguration.dbSyncMapping)) {
			logger.warn("You are settings openjpa.jdbc.SynchronizeMappings, I hope you know what you're doing!");
		    properties.put("openjpa.jdbc.SynchronizeMappings", JpaConfiguration.dbSyncMapping);
		}
		
		EntityManagerFactory fac = Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
		logger.debug("Creating EntityManagerFactory...done");

		return fac;
	}
	
	/**
	 * Closes EntityManagerFactory
	 */
	protected void closeEntityManagerFactory() {
		if(emf != null) {
			emf.close();
			emf = null;
		}
		logger.debug("Closing down entity objects...done");
	}
	
	/**
	 * Checks if EntityManagerFactory is open
	 * @return true when open, false otherwise
	 */
	protected boolean isEntityManagerFactoryOpen() {
		return emf != null && emf.isOpen();
	}

	/**
	 * Return the persistence unit as in persistence.xml file.
	 * @return the persistence unit name
	 */
	protected String getPersistenceUnitName() {
		return "default";
	}

	/**
	 * Retrieves the item for the given name from the item registry
	 * @param itemName
	 * @return
	 */
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
