/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.util.regex.Pattern;

import org.openhab.core.binding.BindingConfig;

/**
 * Configuration for a single property of a receiver. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonBindingConfig implements BindingConfig {
	
	// Examples of a Zone command: Z2ZV, Z3TUNER
	private static final Pattern ZONE_COMMAND_PATTERN = Pattern.compile("^(Z[0-9]{1})([A-Z])+$");
	
	private String instance;
	
	private String itemName;
	
	private String property;

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * See if this is a property that responds to on/off. For now, these properties are either 
	 * 2 chars long (PW, ZM, Z2) or are a zone property and 4 chars long (Z2MU)    
	 * 
	 * @return true if this is an on/off property 
	 */
	public boolean isOnOffProperty() {
		return property.length() == 2 || (isZoneCommand() && property.length() == 4);
	}
	
	public boolean isZoneCommand() {
		return (ZONE_COMMAND_PATTERN.matcher(property).matches());
	}

	/**
	 * Some properties need a translation from the property name in openHAB to the 
	 * actual command we need to send to the receiver.    
	 * 
	 * @return The actual command that can be sent to the receiver. 
	 */
	public String getActualProperty() {
		if (isZoneCommand()) {
			if (getZoneCommand().equals(DenonProperty.ZONE_VOLUME.getCode())) {
				return getZone();
			}
		}
		
		return getProperty();
	}
	
	private String getZone() {
		if (isZoneCommand()) {
			return property.substring(0, 2);
		}
		
		return null;
	}
	
	private String getZoneCommand() {
		if (isZoneCommand()) {
			return property.substring(2, property.length());
		}
		
		return null;
	}
}
