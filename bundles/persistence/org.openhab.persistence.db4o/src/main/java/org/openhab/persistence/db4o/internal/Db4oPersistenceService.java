/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.persistence.db4o.internal;

import java.util.Date;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ext.Db4oException;

/**
 * This is a {@link PersistenceService} implementation using the db4o database.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class Db4oPersistenceService implements PersistenceService {

	private static final Logger logger = LoggerFactory.getLogger(Db4oPersistenceService.class);
	
	private static final String SERVICE_NAME = "db4o";
	private static final String DB_FILE_NAME = "etc/store.db4o";

	private static ObjectContainer db;
	
	public String getName() {
		return SERVICE_NAME;
	}

	public void activate() {
	    db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);
	    ItemState.configure(db.ext().configure());
	}
	
	public void deactivate() {
		if(db!=null) {
			db.close();
			db = null;
		}
	}

	public void store(Item item) {
		store(item, null);
	}

	public void store(Item item, String alias) {
		if(alias==null) alias = item.getName();
		
		ItemState itemState = new ItemState();
		itemState.setItemName(alias);
		itemState.setState(item.getState());
		itemState.setTimeStamp(new Date().getTime());
		try {
			db.store(itemState);	
			db.commit();
			logger.debug("Stored item state: " + itemState.toString());
		} catch(Db4oException e) {
			db.rollback();
			logger.warn("Error storing state for item '{}' as '{}': {}", new String[] { item.getName(), alias, e.getMessage() });
		}
	}
		
}
