/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.intertechno.CULIntertechnoBindingProvider;
import org.openhab.binding.intertechno.IntertechnoBindingConfig;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULLifecycleListener;
import org.openhab.io.transport.cul.CULLifecycleManager;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the communication with Intertechno devices via CUL devices.
 * Currently it is only possible to send commands.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULIntertechnoBinding extends AbstractBinding<CULIntertechnoBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(CULIntertechnoBinding.class);

    /**
     * How often should the command be repeated? See
     * <a href="http://culfw.de/commandref.html">Culfw Command Ref</a> for more
     * details.
     */
    private final static String KEY_REPETITIONS = "repetitions";
    /**
     * How long should one pulse be? See
     * <a href="http://culfw.de/commandref.html">Culfw Command Ref</a> for more
     * details.
     */
    private final static String KEY_WAVE_LENGTH = "wavelength";

    private final CULLifecycleManager culHandlerLifecycle;

    private Integer repetitions;
    private Integer wavelength;

    public CULIntertechnoBinding() {
        culHandlerLifecycle = new CULLifecycleManager(CULMode.SLOW_RF, new CULLifecycleListener() {

            @Override
            public void open(CULHandler cul) throws CULCommunicationException {
                if (wavelength != null) {
                    cul.send("it" + wavelength);
                }
                if (repetitions != null) {
                    cul.send("isr" + repetitions);
                }
            }

            @Override
            public void close(CULHandler cul) {
            }
        });
    }

    @Override
    public void activate() {
        culHandlerLifecycle.open();
    }

    @Override
    public void deactivate() {
        culHandlerLifecycle.close();
    }

    /**
     *
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        IntertechnoBindingConfig config = null;
        for (CULIntertechnoBindingProvider provider : providers) {
            config = provider.getConfigForItemName(itemName);
            if (config != null) {
                break;
            }
        }
        if (config != null && culHandlerLifecycle.isCulReady() && command instanceof OnOffType) {
            OnOffType type = (OnOffType) command;
            String commandValue = null;
            switch (type) {
                case ON:
                    commandValue = config.getCommandON();
                    break;
                case OFF:
                    commandValue = config.getCommandOFF();
                    break;
            }
            if (commandValue != null) {
                try {
                    culHandlerLifecycle.getCul().send("is" + commandValue);
                } catch (CULCommunicationException e) {
                    logger.error("Can't write to CUL", e);
                }
            } else {
                logger.warn("Can't determine value to send for command {}", command);
            }
        }
    }

    protected void addBindingProvider(CULIntertechnoBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(CULIntertechnoBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            Integer parsedRepetitions = parseOptionalNumericParameter(KEY_REPETITIONS, config);
            if (parsedRepetitions != null) {
                this.repetitions = parsedRepetitions;
            }

            Integer parsedWavelength = parseOptionalNumericParameter(KEY_WAVE_LENGTH, config);
            if (parsedWavelength != null) {
                this.wavelength = parsedWavelength;
            }

            culHandlerLifecycle.config(config);
        }
    }

    private Integer parseOptionalNumericParameter(String key, Dictionary<String, ?> config)
            throws ConfigurationException {
        String valueString = (String) config.get(key);
        int value = 0;
        if (!StringUtils.isEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
                return value;
            } catch (NumberFormatException e) {
                throw new ConfigurationException(key, "Can't parse number");
            }
        }
        return null;
    }

}
