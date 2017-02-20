/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.ecosystem;

public interface TinkerforgeContext {
    /**
     * Clear the display of a Bricklet LCD 20x4.
     *
     * @param uid the uid of the Bricklet LCD 20x4
     * @return true on success otherwise false
     */
    public boolean tfClearLCD(String uid);

    /**
     * Drive a servo to a new postion.
     *
     * @param uid the uid of the Servo Brick
     * @param num the number of the Servo
     * @param position the new servo position
     * @param velocity the velocity of the servo action
     * @param acceleration the acceleration of the servo action
     * @return true on success otherwise false
     */
    public boolean tfServoSetposition(String uid, String num, String position, String velocity, String acceleration);

    /**
     * Set a new speed for the dc motor connected to a Brick DC.
     *
     * @param uid the uid of the Brick DC
     * @param speed the new speed of the dc motor
     * @param acceleration the acceleration
     * @param drivemode the drive mode has to be "break" or "coast"
     * @return true on success otherwise false
     */
    public boolean tfDCMotorSetspeed(String uid, Short speed, Integer acceleration, String drivemode);

    /**
     * Set a new speed for the dc motor connected to a Brick DC.
     *
     * @param uid the uid of the Brick DC
     * @param speed the new speed of the dc motor
     * @param acceleration the acceleration
     * @param drivemode the drive mode has to be "break" or "coast"
     * @return true on success otherwise false
     */
    public boolean tfDCMotorSetspeed(String uid, String speed, String acceleration, String drivemode);

    /**
     * Clear the counter of the Rotary Encoder.
     *
     * @param uid the uid of the Rotary Encoder Bricklet
     * @return true on success otherwise false
     */
    public boolean tfRotaryEncoderClear(String uid);

    /**
     * Set the currently measured weight as tare weight.
     *
     * @param uid the uid of the Load Cell Bricklet.
     * @return true on success otherwise false
     */
    public boolean tfLoadCellTare(String uid);

    /**
     * Clear the whole contents of the OLED.
     *
     * @param uid the uid of the OLED Bricklet
     * @return true on success otherwise false
     */
    public boolean tfOLEDClear(String uid);

    /**
     * Clear a OLED window.
     *
     * @param uid the uid of the OLED Bricklet
     * @param columnFrom the column to start at
     * @param columnTo the ending column
     * @param rowFrom the row to start at
     * @param rowTo the ending row
     * @return
     */
    public boolean tfOLEDClear(String uid, short columnFrom, short columnTo, short rowFrom, short rowTo);

    /**
     *
     * @param uid the uid of the OLED Bricklet
     * @param line the line to write to
     * @param position the insert position
     * @param text the text to write
     * @return
     */
    public boolean tfOLEDWriteLine(String uid, short line, short position, String text);

    public boolean tfOLEDSimpleGauge(String uid, int angle);

    public boolean tfOLEDSimpleGauge(String uid, Integer min, Integer max, Integer value);

}
