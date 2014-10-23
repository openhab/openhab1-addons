/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.samsungac.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.samsungac.SamsungAcBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public class SamsungAcGenericBindingProvider extends
		AbstractGenericBindingProvider implements SamsungAcBindingProvider {

	static final Logger logger = 
		LoggerFactory.getLogger(SamsungAcGenericBindingProvider.class);

	private static final Pattern CONFIG_PATTERN = Pattern.compile(".\\[(.*)\\|(.*)\\]");
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "samsungac";
	}

	/**
	 * @{inheritDoc
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem) && !(item instanceof SwitchItem) &&
				!(item instanceof DimmerItem) && !(item instanceof NumberItem)){
			throw new BindingConfigParseException( "item '"+item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', but only String, Number, Switch or Dimmer items are allowed.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig.startsWith("<")){
			SamsungAcBindingConfig config = parseIncomingBindingConfig(item, bindingConfig);		
			addBindingConfig(item, config);
		} else if (bindingConfig.startsWith(">")) {
			SamsungAcBindingConfig config = parseOutgoingBindingConfig(item, bindingConfig);		
			addBindingConfig(item, config);
		} else if (bindingConfig.startsWith("=")) {
			SamsungAcBindingConfig config = parseBidirectionalBindingConfig(item, bindingConfig);		
			addBindingConfig(item, config);
		} else {
			throw new BindingConfigParseException("Item '"+item.getName()+"' does not start with <, > or =. it was:" + bindingConfig);			
		}
		
	}
	
	private SamsungAcBindingConfig parseBidirectionalBindingConfig( Item item, String bindingConfig) throws BindingConfigParseException{
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if( !matcher.matches())
			throw new BindingConfigParseException("Config for item '"+item.getName()+"' could not be parsed.");

		String acInstance = matcher.group(1);
		String property = matcher.group(2);
		
		return new SamsungAcBindingConfig(acInstance, property, true, true);
	}

	private SamsungAcBindingConfig parseIncomingBindingConfig( Item item, String bindingConfig) throws BindingConfigParseException{
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if( !matcher.matches())
			throw new BindingConfigParseException("Config for item '"+item.getName()+"' could not be parsed.");

		String acInstance = matcher.group(1);
		String property = matcher.group(2);
		
		return new SamsungAcBindingConfig(acInstance, property, true, false);
	}
	
	private SamsungAcBindingConfig parseOutgoingBindingConfig( Item item, String bindingConfig) throws BindingConfigParseException{
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if( !matcher.matches())
			throw new BindingConfigParseException("Config for item '"+item.getName()+"' could not be parsed.");

		String acInstance = matcher.group(1);
		String property = matcher.group(2);
		
		return new SamsungAcBindingConfig(acInstance, property, false, true);
	}
	
	class SamsungAcBindingConfig implements BindingConfig {
		private String acInstance;
		private String property;
		private boolean inBound = false;
		private boolean outBound = false;
		
		public SamsungAcBindingConfig(String acInstance, String property, boolean inBound) {
			this.acInstance = acInstance;
			this.property = property;
			this.inBound = inBound;
		}

		public SamsungAcBindingConfig(String acInstance, String property, boolean inBound, boolean outBound) {
			this.acInstance = acInstance;
			this.property = property;
			this.inBound = inBound;
			this.outBound = outBound;
		}

		public String getSamsungAcInstance() {
			return acInstance;
		}
		
		public String getProperty() {
			return property;
		}
		
		public boolean isInBound() {
			return inBound;
		}

		public boolean isOutBound() {
			return outBound;
		}
	}

//	@Override
	public String getAirConditionerInstance(String itemname) {
		SamsungAcBindingConfig bindingConfig = (SamsungAcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig.getSamsungAcInstance();
	}

//	@Override
	public String getProperty(String itemname) {
		SamsungAcBindingConfig bindingConfig = (SamsungAcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig.getProperty();
	}

//	@Override
	public boolean isInBound(String itemname) {
		SamsungAcBindingConfig bindingConfig = (SamsungAcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig != null ? bindingConfig.isInBound(): false;
	}

//	@Override
	public boolean isOutBound(String itemname) {
		SamsungAcBindingConfig bindingConfig = (SamsungAcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig != null ? bindingConfig.isOutBound(): false;
	}

}
