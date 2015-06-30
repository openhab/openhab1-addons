/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * openHAB binding config for a single device in pilight
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightBindingConfig implements BindingConfig {
	
		private String itemName; 
		
		private Class<? extends Item> itemType;
	
		private String instance;
		
		private String device;
		
		private String property; 
		
		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public Class<? extends Item> getItemType() {
			return itemType;
		}
		
		public void setItemType(Class<? extends Item> itemType) {
			this.itemType = itemType;
		}
		
		public String getInstance() {

			return instance;
		}

		public void setInstance(String instance) {
			this.instance = instance;
		}

		public String getDevice() {
			return device;
		}
		
		public void setDevice(String device) {
			this.device = device;
		}
		
		public String getProperty() {
			return property;
		}
		
		public void setProperty(String value) {
			this.property = value;
		}
		
}
