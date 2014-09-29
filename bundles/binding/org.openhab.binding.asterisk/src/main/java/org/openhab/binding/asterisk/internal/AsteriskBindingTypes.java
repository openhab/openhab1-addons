/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.asterisk.internal;


/**
 * Enumerates the various BindingTypes which are allowed for the Asterisk binding
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public enum AsteriskBindingTypes {
		
	/** binds active (i.e. connected) calls to an item */
	ACTIVE {
		{
			name = "active";
		}
	};
	
	String name;
	
	public static AsteriskBindingTypes fromString(String bindingType) {

		if ("".equals(bindingType)) {
			return null;
		}

		for (AsteriskBindingTypes type : AsteriskBindingTypes.values()) {
			if (type.name.equals(bindingType)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid bindingType '" + bindingType + "'");
	}
	
}
