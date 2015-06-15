/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Paolo Denti
 * @since 1.0.0
 */
public class SappGenericBindingProvider extends AbstractGenericBindingProvider implements SappBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(SappGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "sapp";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug(String.format("validating item '%s' against config '%s'", item, bindingConfig));
		// TODO
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			addBindingConfig(item, new SappBindingConfig(bindingConfig));
		} else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Paolo Denti
	 * @since 1.0.0
	 * 
	 *        valid format: <id>:[A/I/O/V]:[1-2500]:[1-16,L,H,*]
	 */
	static class SappBindingConfig implements BindingConfig {
		private static final Map<String, Integer> validAddresses = new HashMap<String, Integer>() {
			private static final long serialVersionUID = 592091386684476669L;
			{
				put("A", new Integer(250));
				put("I", new Integer(255));
				put("O", new Integer(255));
				put("V", new Integer(2500));
			}
		};
		private static final String[] validSubAddresses;

		static {
			validSubAddresses = new String[19];
			for (int i = 0; i < 16; i++) {
				validSubAddresses[i] = String.valueOf(i);
			}
			validSubAddresses[16] = "L";
			validSubAddresses[17] = "H";
			validSubAddresses[18] = "*";
		}

		private String pnmasId;
		private String addressType;
		private int address;
		private String subAddress;

		public SappBindingConfig(String bindingConfig) throws BindingConfigParseException {

			// check syntax
			String[] bindingConfigParts = bindingConfig.split(":");
			if (bindingConfigParts.length != 4) {
				throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
			}

			// pnmasId
			this.pnmasId = bindingConfigParts[0];
			if (this.pnmasId.length() == 0) {
				throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
			}

			// addressType
			this.addressType = bindingConfigParts[1].toUpperCase();
			if (!validAddresses.keySet().contains(this.addressType)) {
				throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
			}

			// address
			try {
				this.address = Integer.parseInt(bindingConfigParts[2]);
				if (this.address <= 0 || this.address > validAddresses.get(this.addressType).intValue()) {
					throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
				}
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
			}

			this.subAddress = bindingConfigParts[3].toUpperCase();
			if (!ArrayUtils.contains(validSubAddresses, this.subAddress)) {
				throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
			}
		}
	}
}
