/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.nest.NestBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class NestGenericBindingProvider extends AbstractGenericBindingProvider implements NestBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(NestGenericBindingProvider.class);
	private static final Pattern ID_REG_EXP = Pattern.compile(".*id=([^,]*).*");
	private static final Pattern TYPE_REG_EXP = Pattern.compile(".*type=([^,]*).*");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "nest";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		try{
			String id = null;
			NestType type = null;
	
			Matcher idMatcher = ID_REG_EXP.matcher(bindingConfig);
			if(idMatcher.matches()){
				id = idMatcher.group(1);
			}
	
			Matcher typeMatcher = TYPE_REG_EXP.matcher(bindingConfig);
			if(typeMatcher.matches()){
				type = NestType.valueOf(typeMatcher.group(1));
			}
			
			NestBindingConfig config = new NestBindingConfig(id, type);
			
			logger.info("ConfigString[{}] ItemName[{}] Id[{}] Type[{}]", bindingConfig, item.getName(), id, type);
			addBindingConfig(item, config);			
		}
		catch(Exception e){
			throw new BindingConfigParseException("Error parsing binding for Context["+ context + "] Item[" + item + "] BindingConfig[" + bindingConfig + "] ErrorMessage: " + e.getMessage());
		}
	
	}
	
	@Override
	public List<String> getItemNameFromNestId(String nestId) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			NestBindingConfig itemConfig = (NestBindingConfig) bindingConfigs.get(itemName);
			if(nestId != null && nestId.equals(itemConfig.getNestId())){
				bindings.add(itemName);
			}
		}
		return bindings;	
	}
	

	@Override
	public List<String> getItemNamesForType(NestType nestType) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			NestBindingConfig itemConfig = (NestBindingConfig) bindingConfigs.get(itemName);
			if(nestType != null && nestType.equals(itemConfig.getType())){
				bindings.add(itemName);
			}
		}
		return bindings;	
	}
	
	@Override
	public NestType getTypeForItemName(String itemName) {
		NestBindingConfig config = (NestBindingConfig) bindingConfigs.get(itemName);
		if(config != null){
			return config.getType();
		}
		return null;
	}

	@Override
	public String getIdForItemName(String itemName) {
		NestBindingConfig config = (NestBindingConfig) bindingConfigs.get(itemName);
		if(config != null){
			return config.getNestId();
		}
		return null;
	}
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Neil Renaud
	 * @since 1.7.0
	 */
	class NestBindingConfig implements BindingConfig {
		
		private final String id;
		private final NestType type;

		public NestBindingConfig(String id, NestType type) {
			this.id = id;
			this.type = type;
		}

		public NestType getType() {
			return type;
		}

		public String getNestId() {
			return id;
		}
	}
	
	
}
