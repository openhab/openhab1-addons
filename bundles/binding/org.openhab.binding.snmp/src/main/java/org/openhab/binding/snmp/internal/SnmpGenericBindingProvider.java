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
package org.openhab.binding.snmp.internal;

import org.openhab.binding.snmp.SnmpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides SNMP binding information from it. It registers as a 
 * {@link SnmpBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ snmp="1.3.6.1.4.1.318.2.3.3.0" }</code> - receives status updates for the given OID prefix</li>
 * 	<li><code>{ snmp="1.3.6.1.4" }</code> - receives status updates for the given OID prefix</li>
 * </ul>
 * </p>
 * 
 * The given config strings are only valid for {@link StringItem}s.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class SnmpGenericBindingProvider extends AbstractGenericBindingProvider implements SnmpBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(SnmpGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "snmp";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			SnmpBindingConfig config = parseBindingConfig(item, bindingConfig);
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
	protected SnmpBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		SnmpBindingConfig config = new SnmpBindingConfig();
		config.oid = new OID(bindingConfig.trim());
		return config;
	}

	/**
	 * @{inheritDoc}
	 */
	public OID getOID(String itemName) {
		SnmpBindingConfig config = (SnmpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.oid : new OID("");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Iterable<String> getItemNames() {
		return bindingConfigs.keySet();
	}	
	
	
	static class SnmpBindingConfig implements BindingConfig {
		OID oid;
	}
	

}
