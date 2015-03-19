package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a Factory, which creates specialized OneWirePropertyBindingConfigs
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireDevicePropertyBindingConfigFactory {

	private static final Logger logger = LoggerFactory.getLogger(OneWireDevicePropertyBindingConfigFactory.class);

	/**
	 * @param pvItem
	 * @param pvBindingConfig
	 * @return a new BindingConfig, corresponding to the given <code><pvItem/code> and <code><pvBindingConfig/code>
	 * @throws BindingConfigParseException
	 */
	public static AbstractOneWireDevicePropertyBindingConfig createOneWireDeviceProperty(Item pvItem, String pvBindingConfig) throws BindingConfigParseException {
		logger.debug("createOneWireDeviceProperty: " + pvItem.getName() + "bindingConfig:" + pvBindingConfig);

		AbstractOneWireDevicePropertyBindingConfig lvNewBindingConfig = null;
		if (OneWireDevicePropertySwitchMinMaxNumberWarningBindingConfig.isBindingConfigToCreate(pvItem, pvBindingConfig)) {
			lvNewBindingConfig = new OneWireDevicePropertySwitchMinMaxNumberWarningBindingConfig(pvBindingConfig);
		} else if (pvItem instanceof NumberItem) {
			lvNewBindingConfig = new OneWireDevicePropertyNumberBindingConfig(pvBindingConfig);
		} else if (pvItem instanceof ContactItem) {
			lvNewBindingConfig = new OneWireDevicePropertyContactBindingConfig(pvBindingConfig);
		} else if (pvItem instanceof SwitchItem) {
			lvNewBindingConfig = new OneWireDevicePropertySwitchBindingConfig(pvBindingConfig);
		} else if (pvItem instanceof StringItem) {
			lvNewBindingConfig = new OneWireDevicePropertyStringBindingConfig(pvBindingConfig);
		} else {
			throw new UnsupportedOperationException("the item-type " + pvItem.getClass() + " cannot be a onewire device");
		}

		logger.debug("created newBindingConfig: " + lvNewBindingConfig.toString());

		return lvNewBindingConfig;
	}

	/**
	 * 
	 * @param pvItem
	 * @param pvBindingConfig
	 * @return is the given Item a valid one, which can be used for a OneWireBinding?
	 * @throws BindingConfigParseException
	 */
	public static boolean isValidItemType(Item pvItem, String pvBindingConfig) throws BindingConfigParseException {
		boolean lvIsValidItem = ((pvItem instanceof NumberItem) || (pvItem instanceof ContactItem) || (pvItem instanceof SwitchItem) || (pvItem instanceof StringItem));
		
		if (!lvIsValidItem) {
			logger.error("Item "+pvItem.getName()+" of type "+pvItem.getClass().getSimpleName()+" with configuration "+pvBindingConfig+" is not a valid onewire ItemType!");
		}
		
		return lvIsValidItem;
	}

}
