/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.entity.Event;
import org.openhab.binding.digitalstrom.internal.client.entity.EventItem;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONEventImpl implements Event{
	
	private List<EventItem>	eventItemList;

	public JSONEventImpl(JSONArray array) {
		this.eventItemList = new LinkedList<EventItem>();
		
		for (int i=0; i<array.size();i++) {
			if (array.get(i) instanceof JSONObject) {
				this.eventItemList.add(new JSONEventItemImpl((JSONObject)array.get(i)));
			}
		}
	}

	@Override
	public List<EventItem> getEventItems() {
		return eventItemList;
	}

}
