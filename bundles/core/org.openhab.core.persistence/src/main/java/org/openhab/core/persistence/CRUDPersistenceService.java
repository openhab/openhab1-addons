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

import org.openhab.core.types.State;

/**
 * A CRUD persistence service which can be used to store and retrieve data from
 * openHAB.
 * 
 * This class extends QueryablePersistenceService to add extra methods to the
 * persistence services.
 * 
 * The CRUDPersistenceService supports full Create Read Update and Delete
 * methods -: 
 * Create - store (from PersistenceService) 
 * Read - query (from QueryablePersistenceService)
 * Update - update
 * Delete - delete
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
public interface CRUDPersistenceService extends QueryablePersistenceService {

	/**
	 * Updates an existing entry in the {@link PersistenceService} for data with
	 * a given filter criteria
	 * 
	 * @param filter
	 *            the filter to apply to the query
	 * @param state
	 *            the updated state to record in the store
	 * @return true or false
	 */
	boolean update(FilterCriteria filter, State state);

	/**
	 * Updates an existing entry in the {@link PersistenceService} for data with
	 * a given filter criteria
	 * 
	 * @param filter
	 *            the filter to apply to the query
	 * @return true or false
	 */
	boolean delete(FilterCriteria filter);
	
	/**
	 * Gets the total record count for an item stored in a {@link PersistenceService} 
	 * 
	 * @param itemname
	 *            the item name 
	 * @return number of records in the store or -1 if the item was not found
	 */
	int getRecordCount(String itemname);

}
