/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.control;

import org.openhab.binding.onewire.internal.OneWireBindingConfig;
import org.openhab.binding.onewire.internal.OneWireBinding;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class which can control functions of the onewire-binding, like clearing the ItemStateCache
 * 
 * Basic Configuration for an OneWire Control Binding: 
 * <code>
 * 	onewire="control=<i>controlname</i>"
 * </code>
 * 
 * Example: <code>
 * 	onewire="control=CLEAR_CACHE"
 * </code>
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 */
public abstract class AbstractOneWireControlBindingConfig implements
		OneWireBindingConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractOneWireControlBindingConfig.class);

	/**
	 * control Name like <code>CLEAR_CASH</code>
	 */
	private String ivControlName;

	public AbstractOneWireControlBindingConfig(String pvBindingConfig)
			throws BindingConfigParseException {
		super();
		parseBindingConfig(pvBindingConfig);
	}

	private void parseBindingConfig(String pvBindingConfig)
			throws BindingConfigParseException {
		String[] pvConfigParts = pvBindingConfig.trim().split(";");

		for (String pvConfigPart : pvConfigParts) {
			parseControl(pvConfigPart);
		}

		// control must be filled
		if (this.ivControlName == null) {
			logger.error("control is not set in config!");
			throw new BindingConfigParseException(
					"Onewire binding control configuration must contain at least the 'control' name");
		}
	}

	private void parseControl(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "control=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty
					.length());
			this.setControlName(lvConfigValue);
		}
	}

	public String getControlName() {
		return ivControlName;
	}

	public void setControlName(String pvControlName) {
		this.ivControlName = pvControlName;
	}

	/**
	 * Abstract method, which must be implemented by specialized Classes
	 * 
	 * @param pvOneWireBinding
	 * @param pvCommand
	 * return new Type for item
	 */
	abstract public void executeControl(OneWireBinding pvOneWireBinding,
			Command pvCommand);

	@Override
	public String toString() {
		return "AbstractOneWireControlBindingConfig [ivControlName="
				+ ivControlName + "]";
	}

}
