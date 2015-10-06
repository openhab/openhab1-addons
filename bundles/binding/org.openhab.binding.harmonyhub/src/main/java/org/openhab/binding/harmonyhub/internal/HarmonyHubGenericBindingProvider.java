/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.harmonyhub.internal;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.harmonyhub.HarmonyHubBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class HarmonyHubGenericBindingProvider extends AbstractGenericBindingProvider implements HarmonyHubBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(HarmonyHubGenericBindingProvider.class);

	/**
	 * this matches  <, * or >, optional qualifier, the type, optional param1 and optional param2  
	 */
	private Pattern bindingPattern = Pattern.compile("^(<|\\*|>)\\[((.+):)?(" + HarmonyHubBindingType.PatternString() + ")(:(.+))?\\]");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "harmonyhub";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only String and Number items are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		Matcher m = bindingPattern.matcher(bindingConfig);

		logger.debug("matching {} against {}", bindingConfig, bindingPattern);

		if (m.find()) {

			HarmonyHubBindingDirection direction = HarmonyHubBindingDirection.getHarmonyHubBindingDirection(m.group(1));
			String qualifier = m.group(3);
			HarmonyHubBindingType type = HarmonyHubBindingType.getHarmonyHubBindingType(m.group(4));

			String param1 = null;
			String param2 = null;
			
			if(m.group(6) != null){
				String [] params = m.group(6).split(":");
				if(params.length > 0){
					param1 = params[0];
				}
				if(params.length > 1){
					param2 = params[1];
				}
			}
			
			logger.debug("processBindingConfiguration parsed result q:{} t:{} p1:{} p2:{}", qualifier, type ,param1, param2);
			
			if(direction == null) {
				throw new BindingConfigParseException("Unknown direction " + m.group(1));
			}

			if(type == null) {
				throw new BindingConfigParseException("Unknown action " + m.group(3));
			}

			if(!direction.equals(type.getDirection()) && 
					type.getDirection() != HarmonyHubBindingDirection.BOTH){
				throw new BindingConfigParseException("wrong direction for action");
			}

			HarmonyHubBindingConfig config = new HarmonyHubBindingConfig(type,qualifier,type.getLabel(),param1,param2, item.getClass());

			addBindingConfig(item, config);		
			super.processBindingConfiguration(context, item, bindingConfig);
		} else {
			throw new BindingConfigParseException("Config string did not match pattern { harmonyhub=\"<binding>[ (qualifier:)<binding> ...]\" }");
		}
	}

	@Override
	public HarmonyHubBindingConfig getHarmonyHubBindingConfig(
			String itemName) {
		return ((HarmonyHubBindingConfig) this.bindingConfigs.get(itemName));
	}
}
