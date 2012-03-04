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

import java.util.List;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oException;
import com.db4o.query.Query;

/**
 * This is a {@link PersistenceService} implementation using the db4o database.
 * 
 * @author Kai Kreuzer
 * @author GPM
 * @since 1.0.0
 *
 */
public class Db4oPersistenceService implements PersistenceService {

	private static final Logger logger = LoggerFactory.getLogger(Db4oPersistenceService.class);
	
	private static final String SERVICE_NAME = "db4o";

	private static final String DB_FILE_NAME = "etc/store.db4o";

	private static ObjectContainer database = null;
		
	public String getName() {
		return SERVICE_NAME;
	}

	public void store(Item item) {
		Event event = new Event();
		event.setItemName(item.getName());
		event.setState(item.getState().toString());
		addEvent(event);
	}

	protected synchronized ObjectContainer getDatabase() throws Db4oException {
		if(null==database){
						
		    database = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE_NAME);
		    	 
		    database.ext().configure().objectClass(Event.class).objectField("itemName").indexed(true);
		    database.ext().configure().objectClass(Event.class).objectField("timeMillis").indexed(true);

		    database.ext().configure().objectClass(Event.class).cascadeOnUpdate(false);
		    database.ext().configure().objectClass(Event.class).cascadeOnDelete(true);
		    
		}
		return database;		
	}
	
	protected void close() {
		if(null!=database){
			database.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected synchronized void addEvent(Event e) {
		try {
			getDatabase().store(e);	
			getDatabase().commit();
			logger.debug("Stored event: " + e.toString());
		} catch(Db4oException dbe) {
			getDatabase().rollback();
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	protected synchronized Event updateEvent(Event searchEvent, Event updatedEvent) {
		
		List<Event> queryEvents = this.queryEvents(searchEvent);
		if(queryEvents.size()==1) {
			Event eEvent = (Event) queryEvents.get(0);
		    eEvent.setItemName(updatedEvent.getItemName());
		    eEvent.setState(updatedEvent.getState());
		    eEvent.setTimeMillis(updatedEvent.getTimeMillis());
		    try {
		    	getDatabase().store(eEvent);
		    	getDatabase().commit();
		    	logger.info("Updated event: " + eEvent.toString());
		   		return eEvent;
		   	} catch(Db4oException dbe) {
				getDatabase().rollback();
				return null;
			}		    
		} else {
			logger.error("Unable to update event, too much results.");
			return null;
		}   
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected synchronized void deleteEvent(Event e) {	
		try {
			List<Event> queryEvents = this.queryEvents(e);
			for(Event de : queryEvents) {
				getDatabase().delete(de);
				logger.debug("Deleted event: " + de.toString());
			}
		    getDatabase().commit();
		} catch(Db4oException dbe) {
			getDatabase().rollback();
		}
	}
	

	protected List<Event> queryEvents(Event e) {	
		try {
			Query query = getDatabase().query();
			query.constrain(Event.class);
			
			//constraints
			if(null!=e) {
				if(null!=e.getItemName() && !e.getItemName().trim().isEmpty()) {
					query.descend("itemName").constrain(e.getItemName()).equal();
				}
				if(null!=e.getState() && !e.getState().trim().isEmpty()) {
					query.descend("state").constrain(e.getState()).equal();
				}
				if(null!=e.getTimeMillis()) {
					query.descend("timeMillis").constrain(e.getTimeMillis().longValue()).greater();
				}
			}		
			query.descend("timeMillis").orderAscending();
			List<Event> result = query.execute();
			
			return result;
		} catch(Db4oException dbe) {
			getDatabase().rollback();
			return null;
		}
	}
	
	protected List<Object> queryEventsPaginated(Event e, int start, int limit) {
		try {
			Query query = getDatabase().query();
			query.constrain(Event.class);
			
			//constraints
			if(null!=e){
				if(null!=e.getItemName() && !e.getItemName().trim().isEmpty()) {
					query.descend("itemName").constrain(e.getItemName()).equal();
				}
				if(null!=e.getState() && !e.getState().trim().isEmpty()) {
					query.descend("state").constrain(e.getState()).equal();
				}
				if(null!=e.getTimeMillis()) {
					query.descend("timeMillis").constrain(e.getTimeMillis().longValue()).equal();
				}
			}		
			query.descend("timeMillis").orderAscending();
			ObjectSet<Object> result = query.execute();
			
			List<Object> resultPaginated = PagingUtility.paging(result,start, limit);
			
			return resultPaginated;
		} catch(Db4oException dbe) {
			getDatabase().rollback();
			return null;
		}
	}
	
}
