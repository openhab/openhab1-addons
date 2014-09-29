/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence;

import org.openhab.core.items.Item;

/**
 * A persistence service which can be used to store data from openHAB.
 * This must not necessarily be a local database, a persistence service
 * can also be cloud-based or a simply data-export facility (e.g. 
 * for sending data to an IoT (Internet of Things) service. 
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public interface PersistenceService {
	
	/**
	 * Returns the name of this {@link PersistenceService}.
	 * This name is used to uniquely identify the {@link PersistenceService}. 
	 * 
	 * @return the name to uniquely identify the {@link PersistenceService}. 
	 */
	String getName();

	/**
	 * Stores the current value of the given item.
	 * <p>Implementors should keep in mind that all registered 
	 * {@link PersistenceService}s are called synchronously. Hence long running
	 * operations should be processed asynchronously. E.g. <code>store</code>
	 * adds things to a queue which is processed by some asynchronous workers
	 * (Quartz Job, Thread, etc.).</p>  
	 * 
	 * @param item the item which state should be persisted.
	 */
	void store(Item item);

	/**
	 * <p>Stores the current value of the given item under a specified alias.</p>
	 * <p>Implementors should keep in mind that all registered 
	 * {@link PersistenceService}s are called synchronously. Hence long running
	 * operations should be processed asynchronously. E.g. <code>store</code>
	 * adds things to a queue which is processed by some asynchronous workers
	 * (Quartz Job, Thread, etc.).</p>  
	 * 
	 * @param item the item which state should be persisted.
	 * @param alias the alias under which the item should be persisted.
	 */
	void store(Item item, String alias);
}
