/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.listeners;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openhab.core.items.Item;
import org.openhab.io.rest.RESTApplication;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the {@link ResourceStateChangeListener} implementation for item REST requests
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class ItemsStateChangeListener extends ResourceStateChangeListener {

	static final Logger logger = LoggerFactory.getLogger(ItemsStateChangeListener.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getResponseObject(HttpServletRequest request) {	
		return getItemAndState(this.lastChange);
	}
	
	public static String getItemAndState(final Item item) {
		if (item != null) {
			return item.getName() + ":" + item.getState().toString();
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getSingleResponseObject(Item item, HttpServletRequest request) {
		return getResponseObject(request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<String> getRelevantItemNames(String pathInfo) {       
		ItemUIRegistry registry = RESTApplication.getItemUIRegistry();
        final Set<String> result = new HashSet<String>();
        for (Item item : registry.getItems()) {
        	result.add(item.getName());
        }
		return result;
	}

}
