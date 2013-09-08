/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.enocean.internal.profiles;

import org.enocean.java.common.ParameterAddress;
import org.enocean.java.common.values.ButtonState;
import org.enocean.java.common.values.Value;
import org.enocean.java.eep.RockerSwitch;
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
        return RockerSwitch.BUTTON_I.equals(parameterAddress.getParameterId());
    }

    private boolean buttonUpPressed(ParameterAddress parameterAddress) {
        return RockerSwitch.BUTTON_O.equals(parameterAddress.getParameterId());
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
