/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.config;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Creates proper binding configuration class for given configuration string.
 *
 * @author Krzysztof Goworek
 * @since 1.8.0
 */
public class SatelBindingConfigFactory {

    private final List<Class<? extends SatelBindingConfig>> bindingConfigurationClasses = new LinkedList<Class<? extends SatelBindingConfig>>();

    /**
     * Constructs factory object. Adds all known types of configuration classes
     * to internal registry.
     */
    public SatelBindingConfigFactory() {
        bindingConfigurationClasses.add(IntegraStateBindingConfig.class);
        bindingConfigurationClasses.add(IntegraStatusBindingConfig.class);
        bindingConfigurationClasses.add(ConnectionStatusBindingConfig.class);
    }

    /**
     * Creates binding configuration class basing on given configuration string.
     *
     * @param bindingConfig
     *            configuration string
     * @return an instance of {@link SatelBindingConfig}
     * @throws BindingConfigParseException
     *             on any parsing error
     */
    public SatelBindingConfig createBindingConfig(String bindingConfig) throws BindingConfigParseException {
        try {
            for (Class<? extends SatelBindingConfig> c : bindingConfigurationClasses) {
                Method parseConfigMethod = c.getMethod("parseConfig", String.class);
                SatelBindingConfig bc = (SatelBindingConfig) parseConfigMethod.invoke(null, bindingConfig);
                if (bc != null) {
                    return bc;
                }
            }
            // no more options, throw parse exception
        } catch (Exception e) {
            // throw parse exception in case of any error
        }

        throw new BindingConfigParseException(String.format("Invalid binding configuration: %s", bindingConfig));
    }
}
