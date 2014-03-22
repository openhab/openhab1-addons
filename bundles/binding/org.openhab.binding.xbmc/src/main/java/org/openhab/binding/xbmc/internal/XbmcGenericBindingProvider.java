/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.xbmc.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.xbmc.XbmcBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author tlan
 * @since 1.3.0
 */
public class XbmcGenericBindingProvider extends AbstractGenericBindingProvider implements XbmcBindingProvider {

	private static final Pattern CONFIG_PATTERN = Pattern.compile(".\\[(.*)\\|(.*)\\]");
	
	public String getBindingType() {
		return "xbmc";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem) && !(item instanceof SwitchItem)){
			throw new BindingConfigParseException( "item '"+item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', but only String or Switch items are allowed.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig.startsWith("<")){
			XbmcBindingConfig config = parseIncomingBindingConfig(item, bindingConfig);		
			addBindingConfig(item, config);
		} else if (bindingConfig.startsWith(">")) {
			XbmcBindingConfig config = parseOutgoingBindingConfig(item, bindingConfig);		
			addBindingConfig(item, config);
		} else {
			throw new BindingConfigParseException("Item '"+item.getName()+"' does not start with < or >.");			
		}
	}
	
	private XbmcBindingConfig parseIncomingBindingConfig( Item item, String bindingConfig) throws BindingConfigParseException{
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if( !matcher.matches())
			throw new BindingConfigParseException("Config for item '"+item.getName()+"' could not be parsed.");

		String xbmcInstance = matcher.group(1);
		String property = matcher.group(2);
		
		return new XbmcBindingConfig(xbmcInstance, property, true);
	}
	
	private XbmcBindingConfig parseOutgoingBindingConfig( Item item, String bindingConfig) throws BindingConfigParseException{
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if( !matcher.matches())
			throw new BindingConfigParseException("Config for item '"+item.getName()+"' could not be parsed.");

		String xbmcInstance = matcher.group(1);
		String property = matcher.group(2);
		
		return new XbmcBindingConfig(xbmcInstance, property, false);
	}
	
	@Override
	public String getXbmcInstance(String itemname) {
		XbmcBindingConfig bindingConfig = (XbmcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig.getXbmcInstance();
	}
	
	@Override
	public String getProperty(String itemname) {
		XbmcBindingConfig bindingConfig = (XbmcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig.getProperty();
	}
	
	@Override
	public boolean isInBound(String itemname) {
		XbmcBindingConfig bindingConfig = (XbmcBindingConfig) bindingConfigs.get(itemname);
		return bindingConfig.isInBound();
	}

	class XbmcBindingConfig implements BindingConfig {
		
		private String xbmcInstance;
		private String property;
		private boolean inBound;
		
		public XbmcBindingConfig(String xbmcInstance, String property, boolean inBound) {
			this.xbmcInstance = xbmcInstance;
			this.property = property;
			this.inBound = inBound;
		}
		
		public String getXbmcInstance() {
			return xbmcInstance;
		}
		
		public String getProperty() {
			return property;
		}
		
		public boolean isInBound() {
			return inBound;
		}
	}
}
