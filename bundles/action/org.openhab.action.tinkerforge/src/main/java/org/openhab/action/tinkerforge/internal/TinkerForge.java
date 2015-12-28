/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
  public static boolean tfDCMotorSetspeed(
      @ParamDoc(name = "uid", text = "Brick DC uid") String uid,
      @ParamDoc(name = "speed", text = "speed -32767 - 32767") Short speed,
      @ParamDoc(name = "acceleration", text = "motor acceleration") Integer acceleration,
      @ParamDoc(name = "drivemode", text = "drive mode \"break\" or \"coast\"") String drivemode) {
    logger.trace("dc motor setPoint action");
    return context.tfDCMotorSetspeed(uid, speed, acceleration, drivemode);
  }

  @ActionDoc(text = "sets the speed of a TinkerForge DC motor", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
  public static boolean tfDCMotorSetspeed(
      @ParamDoc(name = "uid", text = "Brick DC uid") String uid,
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

}
