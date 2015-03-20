/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mochadx10.MochadX10BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format. It
 * registers as a {@link MochadX10BindingProvider} service as well.
 * </p>
 * 
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{mochadx10="a12"}</code> - Connects to X10 module with address 'a12' and use powerline (default) as transmission method.</li>
 * <li><code>{mochadx10="m3:rf"}</code> - Connects to X10 module with address 'm3' and use rf as transmission method.</li>
 * </ul>
 * @author Jack Sleuters
 * @since 1.7.0
 *
 */
public class MochadX10GenericBindingProvider extends AbstractGenericBindingProvider
		implements MochadX10BindingProvider {

	static final Logger logger = LoggerFactory.getLogger(MochadX10GenericBindingProvider.class);

	/**
	 * The regular expression that specifies an X10 address
	 */
	private static final Pattern X10_ADDRESS_PATTERN = Pattern.compile("[a-p]([1-9]|1[0-6])");
	
	@Override
	public String getBindingType() {
		return "mochadx10";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if ( !(item instanceof SwitchItem || 
			   item instanceof DimmerItem ||
			   item instanceof RollershutterItem )) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems, DimmerItems and RollershutterItems are allowed - please check your *.items configuration");
		}
	}

	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			if (bindingConfig != null) {
				String[] configParts = bindingConfig.split(":");
				String address = null;
				String transmitMethod = "pl"; // the default
				
				switch (configParts.length) {
					case 2:
						transmitMethod = configParts[1]; // intentional fall-through
					case 1: 
						address = configParts[0];
						break;
						
					default:
						logger.warn("bindingConfig should only contain x10 address and optional transmission method (item=" + item
								+ ") -> processing bindingConfig aborted!");
						return;				
				}
				
				validateAddress(address);
				validateTransmitMethod(transmitMethod);
				
				BindingConfig mochadX10BindingConfig = (BindingConfig) new MochadX10BindingConfig(item.getClass(), transmitMethod, address);
				addBindingConfig(item, mochadX10BindingConfig);
			} else {
				throw new BindingConfigParseException("No binding config specified (item=" + item
						+ ") -> processing bindingConfig aborted!");
			}
		} catch ( ArrayIndexOutOfBoundsException e ) {
			throw new BindingConfigParseException("bindingConfig is invalid (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * Check whether the specified transmit method is valid.
	 * 
	 * @param transmitMethod
	 * @throws BindingConfigParseException
	 */
	private void validateTransmitMethod(String transmitMethod) throws BindingConfigParseException {
		String lowerCaseTransmitMethod = transmitMethod.toLowerCase();
		if ( !(lowerCaseTransmitMethod.equals("pl") || lowerCaseTransmitMethod.equals("rf")) ) {
			throw new BindingConfigParseException("The specified transmit method '" + transmitMethod + "' is not a valid transmission method.");
		}
	}

	/**
	 * Check whether the specified address is a valid x10 address
	 * 
	 * @param address
	 * @throws BindingConfigParseException
	 */
	private void validateAddress(String address) throws BindingConfigParseException {
		Matcher matcher = X10_ADDRESS_PATTERN.matcher(address.toLowerCase());

		if ( !matcher.matches() ) {
			throw new BindingConfigParseException("The specified address '" + address + "' is not a valid X10 address.");
		}
	}

	@Override
	public MochadX10BindingConfig getItemConfig(String itemName) {
		return (MochadX10BindingConfig) bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			inBindings.add(itemName);
		}
		return inBindings;
	}
}
