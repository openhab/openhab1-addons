/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import fr.zapi.Zibase;

/**
 * This Class handle variables stored in Zibase (32 volatiles + 32 non volatiles)
 *
 * @author Julien Tiphaine
 * @since 1.7.0
 *
 */
public class ZibaseBindingConfigVariable extends ZibaseBindingConfig {

    /**
     * {@inheritDoc}
     */
    public ZibaseBindingConfigVariable(String[] configParameters) {
        super(configParameters);
    }

    /**
     * Retrieve variable value as stored in zibase's memory
     */
    @Override
    public State getOpenhabStateFromZibaseValue(Zibase zibase, String zbResponseStr) {
        long variableValue = zibase.getVariable(Integer.parseInt(this.getId()));
        return new DecimalType(variableValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCommand(Zibase zibase, Command command, int dim) {
        int valueToSet = Integer.parseInt(command.toString());

        if (valueToSet >= -32768 && valueToSet <= 32768) {
            zibase.setVariable(Integer.parseInt(this.getId()), valueToSet);
        } else {
            logger.error("Out of range value {} for variable {}", valueToSet, this.getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isItemConfigValid() {
        logger.info("Checking config for variable {}", this.getId());

        int variableNumber = Integer.parseInt(this.getId());

        // The Zibase has 32 hardware pubic variable, from 0 to 31
        if (variableNumber >= 0 && variableNumber <= 31) {
            return true;
        } else {
            logger.error("bad variable number : {}", this.getId());
            return false;

        }
    }
}
