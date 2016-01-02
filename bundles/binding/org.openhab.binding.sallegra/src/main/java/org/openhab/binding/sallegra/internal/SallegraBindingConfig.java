/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sallegra.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * Wrapper to represent a Binding Item Configuration
 * 
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 * 
 */
public class SallegraBindingConfig implements BindingConfig {

	private String item;
	private String property;
	private String moduleName;
	private SallegraCommand cmdId;
	private String cmdValue;

	public SallegraBindingConfig(String moduleName, SallegraCommand cmdId, String cmdValue) {
		this.moduleName = moduleName;
		this.cmdId = cmdId;
		this.cmdValue = cmdValue;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String itemName) {
		this.item = itemName;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String Property) {
		this.property = Property;
	}

	public String getModuleName() {
		return moduleName;
	}

	public SallegraCommand getCmdId() {
		return cmdId;
	}

	public String getCmdValue() {
		return cmdValue;
	}
}
