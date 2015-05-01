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
import org.openhab.binding.mochadx10.commands.MochadX10Address;
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
 * <li><code>{mochadx10="h8:dim"}</code> - Connects to X10 module with address 'h8' and use the 'dim' command as dim method.</li>
 * </ul>
 * @author Jack Sleuters
 * @since 1.7.0
 *
 */
public class MochadX10GenericBindingProvider extends AbstractGenericBindingProvider
		implements MochadX10BindingProvider {

	static final Logger logger = LoggerFactory.getLogger(MochadX10GenericBindingProvider.class);

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
		String bindingConfigLc = bindingConfig.toLowerCase();
		
		super.processBindingConfiguration(context, item, bindingConfigLc);

		if (bindingConfigLc != null) {
			String address 		  = getAddress(bindingConfigLc);
			String transmitMethod = getTransmitMethod(bindingConfigLc);
			String dimMethod      = getDimMethod(bindingConfigLc);
			
			if (address != null) {
				if (transmitMethod == null) transmitMethod = "pl";
				if (dimMethod == null) dimMethod = "xdim";

				BindingConfig mochadX10BindingConfig = (BindingConfig) new MochadX10BindingConfig(item.getName(), item.getClass(), transmitMethod, dimMethod, address);

				addBindingConfig(item, mochadX10BindingConfig);
			}
			else {
				throw new BindingConfigParseException("No address specified, bindingConfig is invalid (item=" + item
						+ ") -> processing bindingConfig aborted!");
			}
		} else {
			throw new BindingConfigParseException("No binding config specified (item=" + item
					+ ") -> processing bindingConfig aborted!");
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

	/**
	 * Retrieve the X10 address from a bindingConfig string
	 * 
	 * @param bindingConfig
	 * @return  if an X10 address is found, a string representing the address, otherwise null
	 */
	private String getAddress(String bindingConfig) {
		String[] strings = bindingConfig.split(":");
		
		for (String s: strings) {
			Matcher matcher = MochadX10Address.X10_ADDRESS_PATTERN.matcher(s);
			if (matcher.matches()) {
				return s;
			}
		}
		
		return null;
	}

	/**
	 * Retrieve the transmitMethod from a bindingConfig string
	 * 
	 * @param bindingConfig
	 * @return  if a transmitMethod is found, a string representing the transmitMethod, otherwise null
	 */
	private String getTransmitMethod(String bindingConfig) {
		String[] strings = bindingConfig.split(":");
		
		Pattern tmPattern = Pattern.compile("(pl)|(rf)");
		for (String s: strings) {
			Matcher matcher = tmPattern.matcher(s);
			if (matcher.matches()) {
				return s;
			}
		}
		
		return null;
	}
	
	/**
	 * Retrieve the dim method from a bindingConfig string
	 * 
	 * @param bindingConfig
	 * @return  if a dim method is found, a string representing the dim method, otherwise null
	 */
	private String getDimMethod(String msg) {
		String[] strings = msg.split(":");
		
		Pattern dimPattern = Pattern.compile("(dim)|(xdim)");
		for (String s: strings) {
			Matcher matcher = dimPattern.matcher(s);
			if (matcher.matches()) {
				return s;
			}
		}
		
		return null;
	}
}
