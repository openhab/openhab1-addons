/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of a openHAB item that is binded to a Velux
 * KLF200 Gateway. It contains the following information:
 *
 * <ul>
 * <li><B>bindingItemType</B>
 * <P>
 * accessable via
 * {@link org.openhab.binding.velux.internal.VeluxBindingConfig#getBindingItemType
 * getBindingItemType} as representation of the Velux device is filed in the Velux bridge.</li>
 * <li><B>bindingConfig</B>
 * <P>
 * accessable via
 * {@link org.openhab.binding.velux.internal.VeluxBindingConfig#getBindingConfig getBindingConfig} containing the
 * device-specific binding configuration
 * as declared in the binding configuration (possibly adapted by preprocessing).</li>
 * </ul>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */

public class VeluxBindingConfig implements BindingConfig {

    private final Logger logger = LoggerFactory.getLogger(VeluxBindingConfig.class);

    /**
     * The binding type of the velux item described by type {@link org.openhab.binding.velux.internal.VeluxItemType
     * VeluxItemType}.
     */
    private VeluxItemType bindingItemType;

    /**
     * Device-specific binding configuration as declared in the binding configuration (possibly adapted by
     * preprocessing).
     */
    private String bindingConfig;

    /**
     * Constructor of the VeluxBindingConfig.
     *
     * @param bindingItemType
     *                            The Velux item type {@link org.openhab.binding.velux.internal.VeluxItemType
     *                            VeluxItemType}
     *                            which the Velux device is filed in the Velux bridge.
     * @param bindingConfig
     *                            The optional configuration type of the Velux binding.
     *
     * @throws BindingConfigParseException
     *                                         does not really occur.
     */
    public VeluxBindingConfig(VeluxItemType bindingItemType, String bindingConfig) throws BindingConfigParseException {
        logger.trace("VeluxBindingConfig(constructor:{},{}) called.", bindingItemType, bindingConfig);

        this.bindingItemType = bindingItemType;
        this.bindingConfig = bindingConfig;
    }

    /**
     * @return The Velux item type {@link org.openhab.binding.velux.internal.VeluxItemType
     *         VeluxItemType}.
     */
    public VeluxItemType getBindingItemType() {
        return this.bindingItemType;
    }

    /**
     * @return The binding configuration {@link String} that
     *         has been declared in the binding configuration,
     *         possibly adapted by preprocessing.
     */
    public String getBindingConfig() {
        return this.bindingConfig;
    }

}

/**
 * end-of-internal/VeluxBindingConfig.java
 */
