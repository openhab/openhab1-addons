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
package org.openhab.io.rest.internal.broadcaster;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

import org.atmosphere.cpr.BroadcasterLifeCyclePolicyListener;
import org.atmosphere.jersey.JerseyBroadcaster;
import org.openhab.io.rest.internal.listeners.ResourceStateChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Oliver Mazur
 *
 */
public class GeneralBroadcaster extends JerseyBroadcaster {
	
	private static final Logger logger = LoggerFactory.getLogger(GeneralBroadcaster.class);
	protected Collection<ResourceStateChangeListener> listeners = Collections.newSetFromMap(new WeakHashMap<ResourceStateChangeListener, Boolean>());
	
	public GeneralBroadcaster(String id, org.atmosphere.cpr.AtmosphereConfig config) {
		super(id, config);
		this.addBroadcasterLifeCyclePolicyListener(new BroadcasterLifeCyclePolicyListener() {
			
			@Override
			public void onIdle() {
				logger.debug("broadcaster '{}' is idle", this.toString());
			}
			
			@Override
			public void onEmpty() {
				logger.debug("broadcaster '{}' is empty", this.toString());
			/*	for (ResourceStateChangeListener l : listeners){
					l.unregisterItems();
					listeners.remove(l);

				} */
				
			}
			
			@Override
			public void onDestroy() {
				logger.debug("broadcaster '{}' destroyed", this.toString());
				for (ResourceStateChangeListener l : listeners){
					l.unregisterItems();
					listeners.remove(l);
				}
			}
		});
	}
	
	public void addStateChangeListener(final ResourceStateChangeListener listener){
		synchronized (listeners) {
			if(listeners.isEmpty()){
				listener.setBroadcaster(this);
				listener.registerItems();
				listeners.add(listener);	
			}
		}

	}


}
