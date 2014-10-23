/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.profiles;

import org.opencean.core.common.Parameter;
import org.opencean.core.common.ParameterAddress;
import org.opencean.core.common.values.ButtonState;
import org.opencean.core.common.values.Value;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge class to transform normal button behavior to a Dimmer "profile". A
 * RockerSwitch can control with this profile a light dimmer.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class DimmerProfile extends BasicProfile {

    private static final Logger logger = LoggerFactory.getLogger(StandardProfile.class);

    long buttonOPressedTime = 0;
    long buttonIPressedTime = 0;

    private DimmerThread dimmerThread;

    public DimmerProfile(Item item, EventPublisher eventPublisher) {
        super(item, eventPublisher);
    }

    @Override
    public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
        ButtonState buttonState = (ButtonState) valueObject;
        if (buttonUpPressed(parameterAddress)) {
            switch (buttonState) {
            case PRESSED:
                startDimmerThread(IncreaseDecreaseType.INCREASE);
                break;
            case RELEASED:
                stopDimmerThread();
                break;
            }
        } else if (buttonDownPressed(parameterAddress)) {
            switch (buttonState) {
            case PRESSED:
                startDimmerThread(IncreaseDecreaseType.DECREASE);
                break;
            case RELEASED:
                stopDimmerThread();
                break;
            }
        }
    }

    private void stopDimmerThread() {
        dimmerThread.stopRunning();
    }

    private void startDimmerThread(IncreaseDecreaseType type) {
        dimmerThread = new DimmerThread(type);
        dimmerThread.start();
    }

    private boolean buttonDownPressed(ParameterAddress parameterAddress) {
        return Parameter.I.name().equals(parameterAddress.getParameterId());
    }

    private boolean buttonUpPressed(ParameterAddress parameterAddress) {
        return Parameter.O.name().equals(parameterAddress.getParameterId());
    }

    private class DimmerThread extends Thread {

        private static final int MAX_LOOPS = 10;

        private static final long SLEEP_PERIOD_MS = 300;

        private IncreaseDecreaseType command;
        private boolean running = true;
        private int currentLoop = 0;

        public DimmerThread(IncreaseDecreaseType type) {
            this.command = type;
            setDaemon(true);
            setName("DimmerThread");
        }

        public void stopRunning() {
            running = false;
        }

        @Override
        public void run() {
            while (mayRun()) {
                logger.debug("Post new value {} for items {}", command, items);
                postCommand(command);
                try {
                    Thread.sleep(SLEEP_PERIOD_MS);
                } catch (InterruptedException e) {
                    logger.warn("DimmerThread got interrupted. This should not happen.", e);
                }
            }
        }

        private boolean mayRun() {
            return running && (currentLoop <= MAX_LOOPS);
        }

    }

}
