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
package org.openhab.binding.urtsi.internal

import java.util.regex.Pattern
import org.openhab.binding.urtsi.UrtsiBindingProvider
import org.openhab.core.items.Item
import org.openhab.core.library.items.RollershutterItem
import org.openhab.model.item.binding.AbstractGenericBindingProvider
import org.openhab.model.item.binding.BindingConfigParseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.openhab.binding.urtsi.internal.UrtsiGenericBindingProvider.*

/**
 * The binding provider is responsible for defining some metadata of the binding and for providing a parser for the binding configuration.
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
class UrtsiGenericBindingProvider extends AbstractGenericBindingProvider implements UrtsiBindingProvider {
	
	
	static val Logger logger = LoggerFactory::getLogger(typeof(UrtsiGenericBindingProvider))

	static val Pattern CONFIG_BINDING_PATTERN = Pattern::compile("(.*?):([0-9]*)")

	override getBindingType() {
		"urtsi"
	}
	
	/**
	 * The methods checks if the item which uses the urtsi binding is a rollershutter item.
	 */
	override validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		switch item {
			RollershutterItem : {}
			default : 
				throw new BindingConfigParseException("item '" + item.name
					+ "' is of type '" + item.getClass().simpleName
					+ "', only RollershutterItems are allowed - please check your *.items configuration")
		}
	}
	
	/**
	 * The Urtsi binding doesn't use auto-Update as we first check if the command is executed successfully.
	 * @see UrtsiBinding#internalReceiveCommand(String, org.openhab.core.types.Command)
	 */
	override autoUpdate(String itemName) {
		if (itemName.providesBindingFor) {
			false
		} else {
			null
		}
	}
	
	/**
	 * This method parses the binding configuration string and transforms it to an {@link UrtsiItemConfiguration}.
	 */
	override processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig)
		if (bindingConfig != null) {
			parseAndAddBindingConfig(item, bindingConfig)
		} else {
			logger.warn(getBindingType()+" bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!")
		}
		
	}
	
	/**
	 * This method parses the binding configuration string and transforms it to an {@link UrtsiItemConfiguration}.
	 */
	def protected void parseAndAddBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		val matcher = CONFIG_BINDING_PATTERN.matcher(bindingConfig)
		
		if (!matcher.matches) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid Urtsii-binding-configuration. A valid configuration is matched by the RegExp '" + CONFIG_BINDING_PATTERN.pattern() + "'")
		}
		matcher.reset
		if (matcher.find) {
			val urtsiConfig = new UrtsiItemConfiguration(matcher.group(1), Integer::valueOf(matcher.group(2)))
			addBindingConfig(item, urtsiConfig)

		} else {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid Urtsii-binding-configuration. A valid configuration is matched by the RegExp '" + CONFIG_BINDING_PATTERN.pattern() + "'")
		}

	}
	
	/**
	 * Returns the device id which is associated to the given item.
	 */
	override getDeviceId(String itemName) {
		itemName.itemConfiguration?.deviceId
	}
	

	/**
	 * Returns the channel which is associated to the given item.
	 */
	override getChannel(String itemName) {
		itemName.itemConfiguration?.channel
	}
	
	/**
	 * Returns the item configuration for the given item.
	 */
	def private getItemConfiguration(String itemName) {
		bindingConfigs.get(itemName) as UrtsiItemConfiguration
	}
	
}