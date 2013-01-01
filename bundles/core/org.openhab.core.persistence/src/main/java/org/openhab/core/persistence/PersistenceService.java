/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
