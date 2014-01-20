/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.profiles;

import org.enocean.java.common.Parameter;
import org.enocean.java.common.ParameterAddress;
import org.enocean.java.common.values.ButtonState;
import org.enocean.java.common.values.Value;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge class to transform normal button behavior to a Dimmer "profile". A
 * RockerSwitch can control with this profile a light dimmer.
 * 
 * This profile increases the current light by 30% for each short press down. A
 * short press up switches off the light.
 * 
 * Not yet ready!
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class DimmerSteppingProfile extends BasicProfile {

    private static final Logger logger = LoggerFactory.getLogger(StandardProfile.class);

    private static final long SHORT_BUTTON_PRESS_TIME_RANGE = 300;

    long buttonOPressedTime = 0;
    long buttonIPressedTime = 0;

    private DimmerThread dimmerThread;

    public DimmerSteppingProfile(Item item, EventPublisher eventPublisher) {
        super(item, eventPublisher);
    }

    @Override
    public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
        ButtonState buttonState = (ButtonState) valueObject;
        Command command = null;
        if (buttonDownPressed(parameterAddress)) {
            switch (buttonState) {
            case PRESSED:
                startDimmerThread(IncreaseDecreaseType.INCREASE);
                buttonOPressedTime = System.currentTimeMillis();
                break;
            case RELEASED:
                stopDimmerThread();
                if (isLongOButtonReleased()) {
                    buttonOPressedTime = 0;
                } else {
                    command = OnOffType.ON;
                }
                break;
            }
        } else if (buttonUpPressed(parameterAddress)) {
            switch (buttonState) {
            case PRESSED:
                startDimmerThread(IncreaseDecreaseType.DECREASE);
                buttonIPressedTime = System.currentTimeMillis();
                break;
            case RELEASED:
                stopDimmerThread();
                if (isLongIButtonReleased()) {
                    buttonIPressedTime = 0;
                } else {
                    command = OnOffType.OFF;
                }
                break;
            }
        }
        postCommand(command);
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

    private boolean isLongIButtonReleased() {
        return (System.currentTimeMillis() - buttonIPressedTime) > SHORT_BUTTON_PRESS_TIME_RANGE;
    }

    private boolean isLongOButtonReleased() {
        return (System.currentTimeMillis() - buttonOPressedTime) > SHORT_BUTTON_PRESS_TIME_RANGE;
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
                try {
                    Thread.sleep(SLEEP_PERIOD_MS);
                } catch (InterruptedException e) {
                    logger.warn("DimmerThread got interrupted. This should not happen.", e);
                }
                if (!mayRun()) {
                    return;
                }
                logger.debug("Post new value {} for items {}", command, items);
                postCommand(command);
            }
        }

        private boolean mayRun() {
            return running && (currentLoop <= MAX_LOOPS);
        }

    }

}
