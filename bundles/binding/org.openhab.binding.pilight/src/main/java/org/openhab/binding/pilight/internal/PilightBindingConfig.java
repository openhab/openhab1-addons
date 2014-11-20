/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * openHAB binding config for a single device in pilight
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightBindingConfig implements BindingConfig {
	
		private String itemName;
		
		private String instance;
		
		private String location;
		
		private String device;
		
		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getInstance() {

			return instance;
		}

		public void setInstance(String instance) {
			this.instance = instance;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getDevice() {
			return device;
		}
		
		public void setDevice(String device) {
			this.device = device;
		}
}
