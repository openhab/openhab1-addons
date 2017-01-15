/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.binding.onewire.internal.deviceproperties.modifier.OneWireTypeModifier;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class which defines a 1-Wire Device Property which can be written
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 */
public abstract class AbstractOneWireDevicePropertyWritableBindingConfig
        extends AbstractOneWireDevicePropertyBindingConfig {

    private static final Logger logger = LoggerFactory
            .getLogger(AbstractOneWireDevicePropertyWritableBindingConfig.class);

    public AbstractOneWireDevicePropertyWritableBindingConfig(String pvBindingConfig)
            throws BindingConfigParseException {
        super(pvBindingConfig);
    }

    /**
     * @param pvType
     * @return the converted given openHab Type <code>pvType</code> into a modified String to write to the 1-Wire device
     *         property
     */
    public String convertTypeToString(Type pvType) {
        for (OneWireTypeModifier lvTypeModifier : getTypeModifieryList()) {
            logger.debug("type of {} before modifier:{} type={}", getDevicePropertyPath(),
                    lvTypeModifier.getModifierName(), pvType);
            pvType = lvTypeModifier.modify4Write(pvType);
            logger.debug("type of {} after modifier:{} type={}", getDevicePropertyPath(),
                    lvTypeModifier.getModifierName(), pvType);
        }

        return convertTypeToUnmodifiedString(pvType);
    }

    /**
     * Abstract method, which must be implemented by specialized Classes
     *
     * @param pvType
     * @return the converted given openHab Type <code>pvType</code> into a unmodified String
     */
    abstract protected String convertTypeToUnmodifiedString(Type pvType);

}
