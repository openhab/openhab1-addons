/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.tinkerforge.internal;

import org.openhab.binding.tinkerforge.ecosystem.TinkerforgeContext;
import org.openhab.binding.tinkerforge.ecosystem.TinkerforgeContextImpl;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the methods that are made available in scripts and rules for TinkerForge.
 *
 * @author Theo Weiss
 * @since 1.7.0
 */
public class TinkerForge {

    private static final Logger logger = LoggerFactory.getLogger(TinkerForge.class);
    private static TinkerforgeContext context = TinkerforgeContextImpl.getInstance();

    @ActionDoc(text = "clears a TinkerForge LCD", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfClearLCD(@ParamDoc(name = "uid", text = "the device uid") String uid) {
        logger.trace("clear lcd action");
        return context.tfClearLCD(uid);
    }

    @ActionDoc(text = "sets the position of a TinkerForge servo", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfServoSetposition(@ParamDoc(name = "uid", text = "servo uid") String uid,
            @ParamDoc(name = "num", text = "servo number 0-6") String num,
            @ParamDoc(name = "position", text = "servo postion -9000 - 9000") String position,
            @ParamDoc(name = "velocity", text = "servo velocity") String velocity,
            @ParamDoc(name = "acceleration", text = "servo acceleration") String acceleration) {
        logger.trace("servo setPoint (from string args) action");
        return context.tfServoSetposition(uid, num, position, velocity, acceleration);
    }

    @ActionDoc(text = "sets the speed of a TinkerForge DC motor", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfDCMotorSetspeed(@ParamDoc(name = "uid", text = "Brick DC uid") String uid,
            @ParamDoc(name = "speed", text = "speed -32767 - 32767") Short speed,
            @ParamDoc(name = "acceleration", text = "motor acceleration") Integer acceleration,
            @ParamDoc(name = "drivemode", text = "drive mode \"break\" or \"coast\"") String drivemode) {
        logger.trace("dc motor setPoint action");
        return context.tfDCMotorSetspeed(uid, speed, acceleration, drivemode);
    }

    @ActionDoc(text = "sets the speed of a TinkerForge DC motor", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfDCMotorSetspeed(@ParamDoc(name = "uid", text = "Brick DC uid") String uid,
            @ParamDoc(name = "speed", text = "speed -32767 - 32767") String speed,
            @ParamDoc(name = "acceleration", text = "motor acceleration") String acceleration,
            @ParamDoc(name = "drivemode", text = "drive mode \"break\" or \"coast\"") String drivemode) {
        logger.trace("dc motor setPoint (from string args) action");
        return context.tfDCMotorSetspeed(uid, speed, acceleration, drivemode);
    }

    @ActionDoc(text = "clears the counter of the rotary encoder", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfRotaryEncoderClear(
            @ParamDoc(name = "uid", text = "Bricklet Rotary Encoder uid") String uid) {
        logger.trace("rotary encoder clear action");
        return context.tfRotaryEncoderClear(uid);
    }

    @ActionDoc(text = "Sets the currently measured weight as tare weight.", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfLoadCellTare(@ParamDoc(name = "uid", text = "Bricklet Load Cell uid") String uid) {
        logger.trace("Load Cell tare action");
        return context.tfLoadCellTare(uid);
    }

    @ActionDoc(text = "Clear the whole contents of the OLED", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfOLEDClear(@ParamDoc(name = "uid", text = "Bricklet OLED uid") String uid) {
        return context.tfOLEDClear(uid);
    }

    @ActionDoc(text = "Clear an OLED window", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfOLEDClear(@ParamDoc(name = "uid", text = "Bricklet OLED uid") String uid,
            @ParamDoc(name = "columnFrom", text = "the column to start at") short columnFrom,
            @ParamDoc(name = "columnTo", text = "the column to start at") short columnTo,
            @ParamDoc(name = "rowFrom", text = "the row to start at") short rowFrom,
            @ParamDoc(name = "rowTo", text = "the ending row") short rowTo) {
        return context.tfOLEDClear(uid, columnFrom, columnTo, rowFrom, rowTo);
    }

    @ActionDoc(text = "Write a line to an OLED Bricklet. The 64x48 Oled has 6 Lines (0-5) and 14 Columns (0 - 13). The 128x64 Oled has 8 Lines (0 - 7) and 26 Columns (0 - 25).", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfOLEDWriteLine(@ParamDoc(name = "uid", text = "Bricklet OLED uid") String uid,
            @ParamDoc(name = "line", text = "the line to write to") Integer line,
            @ParamDoc(name = "position", text = "the insert position") Integer position,
            @ParamDoc(name = "text", text = "the text to write") String text) {
        return context.tfOLEDWriteLine(uid, line.shortValue(), position.shortValue(), text);
    }

    @ActionDoc(text = "Write a line to an OLED Bricklet. The 64x48 Oled has 6 Lines (0-5) and 14 Columns (0 - 13). The 128x64 Oled has 8 Lines (0 - 7) and 26 Columns (0 - 25).", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfOLEDSimpleGauge(@ParamDoc(name = "uid", text = "Bricklet OLED uid") String uid,
            @ParamDoc(name = "angle", text = "the angel") Integer angle) {
        return context.tfOLEDSimpleGauge(uid, angle);
    }

    @ActionDoc(text = "Write a line to an OLED Bricklet. The 64x48 Oled has 6 Lines (0-5) and 14 Columns (0 - 13). The 128x64 Oled has 8 Lines (0 - 7) and 26 Columns (0 - 25).", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean tfOLEDSimpleGauge(@ParamDoc(name = "uid", text = "Bricklet OLED uid") String uid,
            @ParamDoc(name = "angle", text = "the angel") Integer min,
            @ParamDoc(name = "angle", text = "the angel") Integer max,
            @ParamDoc(name = "angle", text = "the angel") Integer value) {
        return context.tfOLEDSimpleGauge(uid, min, max, value);
    }
}
