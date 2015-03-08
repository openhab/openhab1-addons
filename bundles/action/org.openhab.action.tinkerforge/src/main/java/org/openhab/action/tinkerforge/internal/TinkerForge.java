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
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MServo;
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
  private static TinkerforgeContext context = TinkerforgeContext.getInstance();

  @ActionDoc(text = "clears a TinkerForge LCD", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
  public static boolean tfClearLCD(@ParamDoc(name = "uid", text = "the device uid") String uid) {
    if (context.getEcosystem() != null) {
      MBaseDevice mDevice = context.getEcosystem().getDevice(uid, null);
      if (mDevice instanceof MBrickletLCD20x4) {
        return ((MBrickletLCD20x4) mDevice).clear();
      } else {
        logger.error("no lcd device found with uid {}", uid);
        return false;
      }
    } else {
      logger.warn("ecosystem was null");
      return false;
    }
  }

  @ActionDoc(text = "sets the position of a TinkerForge servo", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
  public static boolean tfServoSetposition(@ParamDoc(name = "uid", text = "servo uid") String uid,
      @ParamDoc(name = "num", text = "servo number 0-6") String num,
      @ParamDoc(name = "position", text = "servo postion -9000 - 9000") String position,
      @ParamDoc(name = "velocity", text = "servo velocity") String velocity,
      @ParamDoc(name = "acceleration", text = "servo acceleration") String acceleration) {
    if (context.getEcosystem() != null) {
      MBaseDevice mDevice = context.getEcosystem().getDevice(uid, num);
      if (mDevice instanceof MServo) {
        logger.trace("servo setPoint action");
        return ((MServo) mDevice).setPoint(Short.parseShort(position), Integer.parseInt(velocity),
            Integer.parseInt(acceleration));
      } else {
        logger.trace("no servo device found with uid {}, num {}", uid, num);
        return false;
      }
    } else {
      logger.error("Action failed ecosystem is null");
    }
    return false;

  }

  @ActionDoc(text = "sets the speed of a TinkerForge DC motor", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
  public static boolean tfDCMotorSetspeed(
      @ParamDoc(name = "uid", text = "Brick DC uid") String uid,
      @ParamDoc(name = "speed", text = "speed -32767 - 32767") Short speed,
      @ParamDoc(name = "acceleration", text = "motor acceleration") Integer acceleration,
      @ParamDoc(name = "drivemode", text = "drive mode \"break\" or \"coast\"") String drivemode) {
    if (context.getEcosystem() != null) {
      MBaseDevice mDevice = context.getEcosystem().getDevice(uid, null);
      if (mDevice instanceof MBrickDC) {
        logger.trace("servo setPoint action");
        return ((MBrickDC) mDevice).setSpeed(speed, acceleration, drivemode);
      } else {
        logger.trace("no Brick DC device found with uid {}, num {}", uid);
        return false;
      }
    } else {
      logger.error("Action failed ecosystem is null");
    }
    return false;

  }

  @ActionDoc(text = "sets the speed of a TinkerForge DC motor", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
  public static boolean tfDCMotorSetspeed(
      @ParamDoc(name = "uid", text = "Brick DC uid") String uid,
      @ParamDoc(name = "speed", text = "speed -32767 - 32767") String speed,
      @ParamDoc(name = "acceleration", text = "motor acceleration") String acceleration,
      @ParamDoc(name = "drivemode", text = "drive mode \"break\" or \"coast\"") String drivemode) {
    if (context.getEcosystem() != null) {
      MBaseDevice mDevice = context.getEcosystem().getDevice(uid, null);
      if (mDevice instanceof MBrickDC) {
        logger.trace("servo setPoint action");
        return ((MBrickDC) mDevice).setSpeed(Short.parseShort(speed),
            Integer.parseInt(acceleration), drivemode);
      } else {
        logger.trace("no Brick DC device found with uid {}, num {}", uid);
        return false;
      }
    } else {
      logger.error("Action failed ecosystem is null");
    }
    return false;

  }
}
