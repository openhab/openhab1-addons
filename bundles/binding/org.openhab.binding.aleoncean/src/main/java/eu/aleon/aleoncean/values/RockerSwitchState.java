/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.values;

import java.util.List;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RockerSwitchState implements Cloneable {

    public Boolean buttonPressedAI;
    public Boolean buttonPressedAO;
    public Boolean buttonPressedBI;
    public Boolean buttonPressedBO;

    public RockerSwitchState() {
    }

    public RockerSwitchState(final Boolean pressed) {
        this(pressed, pressed, pressed, pressed);
    }

    public RockerSwitchState(final Boolean pressedAI,
                             final Boolean pressedAO,
                             final Boolean pressedBI,
                             final Boolean pressedBO) {
        this.buttonPressedAI = pressedAI;
        this.buttonPressedAO = pressedAO;
        this.buttonPressedBI = pressedBI;
        this.buttonPressedBO = pressedBO;
    }

    /**
     * Get the current state of a rocker switch button.
     *
     * @param button The button that state should be returned.
     * @return Return the last known button state (true = pressed, false = released, null = unknown).
     * @throws IllegalArgumentException This exception is thrown, if the value for {@code button} is not handled /
     *                                  unknown.
     */
    public Boolean get(final RockerSwitchButton button) {
        switch (button) {
            case AI:
                return buttonPressedAI;
            case AO:
                return buttonPressedAO;
            case BI:
                return buttonPressedBI;
            case BO:
                return buttonPressedBO;
            default:
                throw new IllegalArgumentException(String.format("Unhandled button: %s", button));
        }
    }

    public void set(final RockerSwitchButton button, final Boolean pressed) {
        switch (button) {
            case AI:
                buttonPressedAI = pressed;
                break;
            case AO:
                buttonPressedAO = pressed;
                break;
            case BI:
                buttonPressedBI = pressed;
                break;
            case BO:
                buttonPressedBO = pressed;
                break;
            default:
                break;
        }
    }

    @Override
    public RockerSwitchState clone() throws CloneNotSupportedException {

        try {
            final RockerSwitchState cloned = (RockerSwitchState) super.clone();
            return cloned;
        } catch (final CloneNotSupportedException ex) {
            final RockerSwitchState cloned = new RockerSwitchState();
            cloned.buttonPressedAI = this.buttonPressedAI;
            cloned.buttonPressedAO = this.buttonPressedAO;
            cloned.buttonPressedBI = this.buttonPressedBI;
            cloned.buttonPressedBO = this.buttonPressedBO;
            return cloned;
        }
    }

    @Override
    public String toString() {
        return "RockerSwitchState{" + "buttonPressedAI=" + buttonPressedAI + ", buttonPressedAO=" + buttonPressedAO + ", buttonPressedBI=" + buttonPressedBI + ", buttonPressedBO=" + buttonPressedBO + '}';
    }

    public static void getChanges(final RockerSwitchState oldState, final RockerSwitchState newState,
                                  final List<RockerSwitchButton> pressed,
                                  final List<RockerSwitchButton> released) {
        /* The new value should never change to null, if it differs before.
         * - old value could be null (only if old value is null, too), false, true
         * - new value could be null, false, true
         */
        if (newState.buttonPressedAI != null && !newState.buttonPressedAI.equals(oldState.buttonPressedAI)) {
            if (newState.buttonPressedAI) {
                pressed.add(RockerSwitchButton.AI);
            } else {
                released.add(RockerSwitchButton.AI);
            }
        }
        if (newState.buttonPressedAO != null && !newState.buttonPressedAO.equals(oldState.buttonPressedAO)) {
            if (newState.buttonPressedAO) {
                pressed.add(RockerSwitchButton.AO);
            } else {
                released.add(RockerSwitchButton.AO);
            }
        }
        if (newState.buttonPressedBI != null && !newState.buttonPressedBI.equals(oldState.buttonPressedBI)) {
            if (newState.buttonPressedBI) {
                pressed.add(RockerSwitchButton.BI);
            } else {
                released.add(RockerSwitchButton.BI);
            }
        }
        if (newState.buttonPressedBO != null && !newState.buttonPressedBO.equals(oldState.buttonPressedBO)) {
            if (newState.buttonPressedBO) {
                pressed.add(RockerSwitchButton.BO);
            } else {
                released.add(RockerSwitchButton.BO);
            }
        }
    }

}
