/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.entity.impl;

import org.json.simple.JSONObject;
import org.openhab.binding.digitalstrom.internal.client.constants.JSONApiResponseKeysEnum;
import org.openhab.binding.digitalstrom.internal.client.entity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 	Alexander Betker
 * @since 1.3.0
 */
public class JSONGroupImpl implements Group {
	
	private static final Logger logger = LoggerFactory.getLogger(JSONGroupImpl.class);
	
	private String name	= "";	// it is possible, that a group has no name!
	private short groupId = 0;
	
	
	public JSONGroupImpl(JSONObject object) {
		if (object.get(JSONApiResponseKeysEnum.GROUP_NAME.getKey()) != null) {
			name = object.get(JSONApiResponseKeysEnum.GROUP_NAME.getKey()).toString();
		}
		
		if (object.get(JSONApiResponseKeysEnum.GROUP_ID.getKey()) != null) {
			try {
				this.groupId = Short.parseShort(object.get(JSONApiResponseKeysEnum.GROUP_ID.getKey()).toString());
			}
			catch (java.lang.NumberFormatException e) {
				logger.error("NumberFormatException by getting groupID: "+object.get(JSONApiResponseKeysEnum.GROUP_ID.getKey()).toString());
			}
		}

	}

	@Override
	public short getGroupID() {
		return groupId;
	}

	@Override
	public String getGroupName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Group) {
			Group group = (Group)obj;
			return group.getGroupID() == this.getGroupID();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "name: "+this.name+", "+"groupId: "+this.groupId;
	}

}
