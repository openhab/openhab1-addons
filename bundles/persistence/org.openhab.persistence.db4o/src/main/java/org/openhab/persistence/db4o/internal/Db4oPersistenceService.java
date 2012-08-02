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

import java.io.File;
import java.util.Collections;
import java.util.Date;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oException;
import com.db4o.query.Query;


/**
 * This is a {@link PersistenceService} implementation using the db4o database.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class Db4oPersistenceService implements QueryablePersistenceService {

	private static final Logger logger = LoggerFactory.getLogger(Db4oPersistenceService.class);
	
	private static final String SERVICE_NAME = "db4o";
	private static final String DB_FOLDER_NAME = "etc/db4o";
	private static final String DB_FILE_NAME = "store.db4o";

	private static ObjectContainer db;
	
	
	public String getName() {
		return SERVICE_NAME;
	}
	
	public void activate() {
		File folder = new File(DB_FOLDER_NAME);
		if(!folder.exists()) {
			folder.mkdir();
		}
	    openDbFile();
	    Db4oItem.configure(db.ext().configure());
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
		
		Db4oItem historicItem = new Db4oItem();
		historicItem.setName(alias);
		historicItem.setState(item.getState());
		historicItem.setTimestamp(new Date());
		try {
			db.store(historicItem);	
			db.commit();
			logger.debug("Stored item state '{}' -> '{}'", new String[] {historicItem.getName(), historicItem.getState().toString() } );
		} catch(Db4oException e) {
			db.rollback();
			logger.warn("Error storing state for item '{}' as '{}': {}", new String[] { item.getName(), alias, e.getMessage() });
		}
	}

	public Iterable<HistoricItem> query(FilterCriteria filter) {
		Query query = queryWithReconnect();
		
		if (query != null) {
			query.constrain(Db4oItem.class);
			
			if (filter==null) {
				filter = new FilterCriteria();
			}
			if (filter.getBeginDate()!=null) {
				query.descend("timestamp").constrain(filter.getBeginDate()).greater().equal();
			}
			if (filter.getEndDate()!=null) {
				query.descend("timestamp").constrain(filter.getEndDate()).smaller().equal();
			}
			if (filter.getItemName()!=null) {
				query.descend("name").constrain(filter.getItemName()).equal();
			}
			if (filter.getState()!=null && filter.getOperator()!=null) {
				switch(filter.getOperator()) {
					case EQ : query.descend("state").constrain(filter.getState()).equal(); break;
					case GT : query.descend("state").constrain(filter.getState()).greater(); break;
					case LT : query.descend("state").constrain(filter.getState()).smaller(); break;
					case NEQ : query.descend("state").constrain(filter.getState()).equal().not(); break;
					case GTE : query.descend("state").constrain(filter.getState()).greater().equal(); break;
					case LTE : query.descend("state").constrain(filter.getState()).smaller().equal(); break;
				}
			}
			
			query.descend("timestamp").orderDescending();
			ObjectSet<HistoricItem> results = query.execute();
	
			int startIndex = filter.getPageNumber() * filter.getPageSize();
			if (startIndex < results.size()) {
				int endIndex = startIndex + filter.getPageSize();
				if(endIndex > results.size()) {
					endIndex = results.size();
				}
				return results.subList(startIndex, endIndex);
			}
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Creates a new Query and returns it. In case the Database is closed for
	 * some reason we'll try to reopen it again and try to create a query a
	 * second time. If that fails too <code>null</code> is returned. 
	 * 
	 * @return a Query-Object or <code>null</code> if there are errors or the
	 * Database couldn't be opened again.
	 */
	private Query queryWithReconnect() {
		Query query = null;
		try {
			query = db.query();
		} catch (DatabaseClosedException dce) {
			logger.debug("Database '{}' is closed, we'll try to reopen it again ...");
			openDbFile();
			query = db.query();
		}
		return query;
	}

	private void openDbFile() {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FOLDER_NAME + File.separator + DB_FILE_NAME);
	}
	

}
