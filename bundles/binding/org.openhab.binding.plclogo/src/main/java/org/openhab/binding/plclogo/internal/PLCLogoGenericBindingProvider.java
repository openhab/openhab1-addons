/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.binding.plclogo.PLCLogoBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.*;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author g8kmh
 * @since 1.5.0
 */
public class PLCLogoGenericBindingProvider extends AbstractGenericBindingProvider implements PLCLogoBindingProvider {
	private static final Logger logger = 
			LoggerFactory.getLogger(PLCLogoBinding.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "plclogo";
	}
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Item getItemType(String itemName) {
		PLCLogoBindingConfig config = (PLCLogoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getItemType() : null;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// @TODO may add additional checking based on the memloc
		if (!(item instanceof SwitchItem || item instanceof ContactItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch - Contact Items & Number are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		boolean invert;
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] parts = parseConfigString(bindingConfig);
	
		PLCLogoBindingConfig config = new PLCLogoBindingConfig(item.getName(), item, parts[0], parts[1], parts[2], parts[3].equalsIgnoreCase("YES")? true: false, Integer.parseInt(parts[4]));
		
		addBindingConfig(item, config);		
	}
	
	private String[] parseConfigString(String bindingConfig) throws BindingConfigParseException {
		// the config string has the format
		//
		//  instancename:memloc.bit [activelow:yes|no]
		//
		String shouldBe = "should be controllername:memloc[.bit] [activelow:yes|no]";
		String controller, memory, bit ="" , invert = "NO";
		int deltaValue = 0;
		String[] segments = bindingConfig.split(" ");
		if (segments.length > 2)
			throw new BindingConfigParseException("invalid item format: " + bindingConfig + ", " + shouldBe);
		String[] dev = segments[0].split(":");
		if (dev.length == 2){
			controller = dev[0];
			String[] membit = dev[1].split("\\.");
			memory = membit[0];
			if (membit.length == 2){
				bit = (membit[1]);
				if (Integer.parseInt(bit) > 7){
					throw new BindingConfigParseException("bit cannot be greater than 7: " + bindingConfig + ", " + shouldBe);
				}
			}
		} else {
			throw new BindingConfigParseException("invalid item name/memory format: " + bindingConfig + ", " + shouldBe);
		}
		
		// check for invert or analogdelta
		if (segments.length == 2){
			logger.debug("Addtional binding config " + segments[1]);
			String[] inversion = segments[1].split("=");
			if ((inversion.length != 2)){
				throw new BindingConfigParseException("invalid second parameter: " + bindingConfig + ", " + shouldBe);
			}
			if ((inversion[0].compareToIgnoreCase("activelow") == 0)){
				if (inversion[1].compareToIgnoreCase("yes") == 0){
				invert = "YES";
				}
			}
			if (inversion[0].compareToIgnoreCase("analogdelta") == 0){
				deltaValue = Integer.parseInt(inversion[1]);
				logger.debug("Setting analogDelta " + deltaValue);
			}
		}
				
		String [] retval = {controller, memory, bit, invert, Integer.toString(deltaValue)};
		
		return retval;
	}

	@Override
	public PLCLogoBindingConfig getBindingConfig(String itemName) {
		return (PLCLogoBindingConfig) this.bindingConfigs.get(itemName);

	}

	
	
}
