/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freeswitch.internal;


/**
 * @author Dan Cunningham
 * @since 1.4.0
 */
public enum FreeswitchBindingType {
	ACTIVE("active"),
	MESSAGE_WAITING("message_waiting"),
	CMD_API("api");

	private String label;
	
	private FreeswitchBindingType(String label){
		this.label = label;
	}
	
	public static FreeswitchBindingType fromString(String bindingType) {

		if ("".equals(bindingType)) {
			return null;
		}

		for (FreeswitchBindingType type : FreeswitchBindingType.values()) {
			if (type.label.equals(bindingType)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid bindingType '" + bindingType + "'");
	}
}
