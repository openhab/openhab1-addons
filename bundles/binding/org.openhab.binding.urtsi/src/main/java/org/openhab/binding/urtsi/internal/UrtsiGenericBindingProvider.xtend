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

	static val Pattern CONFIG_BINDING_PATTERN = Pattern::compile("(.*?):([0-9]*)((:)?([0-9])?)")

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
			bombOut(bindingConfig);
		}
		matcher.reset
		if (matcher.find) {
			var address = 1;
			var channel = 1;
			if (matcher.group(5) != null) { // both address and channel are specified
				channel = Integer::valueOf(matcher.group(5));
				if (matcher.group(2) == null) {
				   bombOut(bindingConfig);
				}
				address = Integer::valueOf(matcher.group(2));
			} else { // just channel specified
				if (matcher.group(2) == null) {
				   bombOut(bindingConfig);
				}
				channel = Integer::valueOf(matcher.group(2));
			}
			val urtsiConfig = new UrtsiItemConfiguration(matcher.group(1), channel, address);
			addBindingConfig(item, urtsiConfig)
		} else {
			bombOut(bindingConfig);
		}

	}
	/**
	* Shorthand for throwing lenghty exception
	*/
	def private void bombOut(String config) throws BindingConfigParseException {
		throw new BindingConfigParseException("bindingConfig '" + config +
			"' doesn't contain a valid Urtsii-binding-configuration. A valid configuration is matched by the RegExp '" +
			CONFIG_BINDING_PATTERN.pattern() + "'");		
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
	 * Returns the urtsi device address which is associated to the given item.
	 */
	override getAddress(String itemName) {
		itemName.itemConfiguration?.address
	}
	
	/**
	 * Returns the item configuration for the given item.
	 */
	def private getItemConfiguration(String itemName) {
		bindingConfigs.get(itemName) as UrtsiItemConfiguration
	}
	
}