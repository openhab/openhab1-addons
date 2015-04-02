/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import org.openhab.core.binding.BindingConfig;

/**  
 * Config for a single Plex client property  
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class PlexBindingConfig implements BindingConfig {
	
	private String itemName;

	private String machineIdentifier;
	
	private String property;
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMachineIdentifier() {
		return machineIdentifier;
	}

	public void setMachineIdentifier(String machineIdentifier) {
		this.machineIdentifier = machineIdentifier;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	public boolean isReadOnly() {
		return !property.contains("/");
	}
}
