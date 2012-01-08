/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.asterisk.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.asterisk.AsteriskBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Asterisk binding information from it. It registers as a 
 * {@link AsteriskBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ asterisk="active" }</code> - receives status updates on active calls</li>
 * </ul>
 * These binding configurations can be used on either SwitchItems or CallItems. 
 * For SwitchItems, it will obviously receive ON at the beginning and OFF at the
 * end. CallItems will be filled with origination and the destination number.
 * </p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class AsteriskGenericBindingProvider extends AbstractGenericBindingProvider implements AsteriskBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(AsteriskGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "asterisk";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			AsteriskBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}
	
	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an
	 * appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException if bindingConfig is no valid binding type
	 */
	protected AsteriskBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		try {
			return new AsteriskBindingConfig(AsteriskBindingTypes.fromString(bindingConfig));
		}
		catch (IllegalArgumentException iae) {
			throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
		}
	}

	public AsteriskBindingTypes getType(String itemName) {
		AsteriskBindingConfig config = (AsteriskBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.type : null;
	}

	public String[] getItemNamesForType(AsteriskBindingTypes type) {
		Set<String> itemNames = new HashSet<String>();
		for(Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			AsteriskBindingConfig fbConfig = (AsteriskBindingConfig) entry.getValue();
			if(fbConfig.type.equals(type)) {
				itemNames.add(entry.getKey());
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}

	
	static class AsteriskBindingConfig implements BindingConfig {
		
		final AsteriskBindingTypes type;
		
		public AsteriskBindingConfig(AsteriskBindingTypes type) {
			this.type = type;
		}
		
	}

}
