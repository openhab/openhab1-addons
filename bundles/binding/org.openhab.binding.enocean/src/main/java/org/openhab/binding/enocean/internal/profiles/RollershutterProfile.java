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
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge class to transform normal button behavior to a RollerShutter
 * "profile". A RockerSwitch can control with this profile a roller shutter.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 */
public class RollershutterProfile extends BasicProfile {

    private static final Logger logger = LoggerFactory.getLogger(StandardProfile.class);

    private static final long SHORT_BUTTON_PRESS_TIME_RANGE = 300;

    /*
     * If a short button press is followed by a another button press in this
     * 10sec range a STOP is issued
     */
    private static final long STOP_BUTTON_PRESS_TIME_RANGE = 10000;

    long buttonOPressedTime = 0;
    long buttonIPressedTime = 0;
    long lastButtonShortPressTime = 0;

    boolean belongsToLastShortButtonPress = false;

    public RollershutterProfile(Item item, EventPublisher eventPublisher) {
        super(item, eventPublisher);
    }

    @Override
    public void valueChanged(ParameterAddress parameterAddress, Value valueObject) {
        ButtonState buttonState = (ButtonState) valueObject;
        Command command = null;
        if (Parameter.O.name().equals(parameterAddress.getParameterId())) {
            switch (buttonState) {
            case PRESSED:
                if (belongsToLastShortButtonPress()) {
                    command = StopMoveType.STOP;
                    lastButtonShortPressTime = 0;
                    belongsToLastShortButtonPress = true;
                    buttonOPressedTime = System.currentTimeMillis();
                } else {
                    command = UpDownType.UP;
                    buttonOPressedTime = System.currentTimeMillis();
                }
                break;
            case RELEASED:
                if (isLongOButtonReleased()) {
                    command = StopMoveType.STOP;
                    buttonOPressedTime = 0;
                } else if (belongsToLastShortButtonPress) {
                    lastButtonShortPressTime = 0;
                    belongsToLastShortButtonPress = false;
                } else {
                    lastButtonShortPressTime = System.currentTimeMillis();
                }
                break;
            }
        } else if (Parameter.I.name().equals(parameterAddress.getParameterId())) {
            switch (buttonState) {
            case PRESSED:
                if (belongsToLastShortButtonPress()) {
                    command = StopMoveType.STOP;
                    lastButtonShortPressTime = 0;
                    belongsToLastShortButtonPress = true;
                    buttonIPressedTime = System.currentTimeMillis();
                } else {
                    command = UpDownType.DOWN;
                    buttonIPressedTime = System.currentTimeMillis();
                }
                break;
            case RELEASED:
                if (isLongIButtonReleased()) {
                    command = StopMoveType.STOP;
                    buttonIPressedTime = 0;
                } else if (belongsToLastShortButtonPress) {
                    lastButtonShortPressTime = 0;
                    belongsToLastShortButtonPress = false;
                } else {
                    lastButtonShortPressTime = System.currentTimeMillis();
                }
                break;
            }
        }
        logger.debug("Received new value {} for items {}", command, items);
        postCommand(command);
    }

    private boolean belongsToLastShortButtonPress() {
        return (System.currentTimeMillis() - lastButtonShortPressTime) < STOP_BUTTON_PRESS_TIME_RANGE;
    }

    private boolean isLongIButtonReleased() {
        return (System.currentTimeMillis() - buttonIPressedTime) > SHORT_BUTTON_PRESS_TIME_RANGE;
    }

    private boolean isLongOButtonReleased() {
        return (System.currentTimeMillis() - buttonOPressedTime) > SHORT_BUTTON_PRESS_TIME_RANGE;
    }

}
