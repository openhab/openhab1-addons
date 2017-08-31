/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModbusBindingProvider provides binding for Openhab Items
 * There are two ways to bind an item to modbus coils/registers
 *
 * 1) single coil/register per item
 * Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:5"}
 *
 * This binds MySwitch to modbus slave defined as "slave1" in openhab.config reading/writing to the coil 5
 *
 * 2) separate coils/registers for reading and writing
 * Switch MySwitch "My Modbus Switch" (ALL) {modbus="slave1:<6:>7"}
 *
 * In this case coil 6 is used as status coil (readonly) and commands are put to coil 7 by setting coil 7 to true.
 * You hardware should then set coil 7 back to false to allow further commands processing.
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusGenericBindingProvider extends AbstractGenericBindingProvider implements ModbusBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(ModbusGenericBindingProvider.class);
    static final String BINDING_TYPE = "modbus";

    /**
     * {@inheritDoc}
     */
    /*
     * (non-Javadoc)
     *
     * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
     */
    @Override
    public String getBindingType() {
        return BINDING_TYPE;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (item.getClass() == SwitchItem.class) {
            return;
        } else if (item.getClass() == ContactItem.class) {
            return;
        } else if (item.getClass() == NumberItem.class) {
            return;
        } else {
            logger.debug(
                    "Item '{}' is of type '{}'. Please make sure that transformation is "
                            + "in place to convert the polled data to a format understood "
                            + "by the item. Furthermore, make sure that commands are converted to "
                            + "DecimalType or any command accepted by Switch, Contact or Number items.",
                    item.getName(), item.getClass().getSimpleName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig != null) {
            ModbusBindingConfig config = parseBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else {
            logger.warn("bindingConfig is NULL (item={}) -> processing bindingConfig aborted!", item);
        }
    }

    /**
     * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
     *
     * @param item
     * @param bindingConfig
     *
     * @throws BindingConfigParseException if bindingConfig is no valid binding type
     */
    protected ModbusBindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        return new ModbusBindingConfig(item, bindingConfig);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.modbus.tcp.master.ModbusBindingProvider#getConfig(java.lang.String)
     */
    @Override
    public ModbusBindingConfig getConfig(String name) {
        return (ModbusBindingConfig) bindingConfigs.get(name);
    }

    @Override
    public void removeConfigurations(String context) {
        super.removeConfigurations(context);
    }

}
