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
        if (RockerSwitch.BUTTON_O.equals(parameterAddress.getParameterId())) {
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
        } else if (RockerSwitch.BUTTON_I.equals(parameterAddress.getParameterId())) {
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
