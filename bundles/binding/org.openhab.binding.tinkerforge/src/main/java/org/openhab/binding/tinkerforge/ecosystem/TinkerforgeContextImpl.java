/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.ecosystem;

import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.LoadCellWeight;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickDC;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MServo;
import org.openhab.binding.tinkerforge.internal.model.RotaryEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinkerforgeContextImpl implements TinkerforgeContext {

  private static TinkerforgeContextImpl instance;
  private Ecosystem ecosystem;
  private Logger logger = LoggerFactory.getLogger(TinkerforgeContextImpl.class);

  private TinkerforgeContextImpl() {}

  public static TinkerforgeContext getInstance() {
    if (instance == null) {
      instance = new TinkerforgeContextImpl();
    }
    return instance;
  }

  public Ecosystem getEcosystem() {
    return ecosystem;
  }

  public void setEcosystem(Ecosystem ecosystem) {
    this.ecosystem = ecosystem;
  }

  @Override
  public boolean tfClearLCD(String uid) {
    if (ecosystem == null) {
      logger.error("tfClearLCD action failed ecosystem is null");
      return false;
    }
    MBaseDevice mDevice = ecosystem.getDevice(uid, null);
    if (mDevice instanceof MBrickletLCD20x4) {
      return ((MBrickletLCD20x4) mDevice).clear();
    } else {
      logger.error("no lcd device found with uid {}", uid);
      return false;
    }
  }

  @Override
  public boolean tfServoSetposition(String uid, String num, String position, String velocity,
      String acceleration) {
    if (ecosystem == null) {
      logger.error("tfServoSetposition action failed ecosystem is null");
      return false;
    }
    MBaseDevice mDevice = ecosystem.getDevice(uid, num);
    if (mDevice instanceof MServo) {
      logger.trace("servo setPoint action");
      return ((MServo) mDevice).setPoint(Short.parseShort(position), Integer.parseInt(velocity),
          Integer.parseInt(acceleration));
    } else {
      logger.trace("no servo device found with uid {}, num {}", uid, num);
      return false;
    }
  }

  @Override
  public boolean tfDCMotorSetspeed(String uid, Short speed, Integer acceleration, String drivemode) {
    if (ecosystem == null) {
      logger.error("tfDCMotorSetspeed action failed ecosystem is null");
      return false;
    }
    MBaseDevice mDevice = ecosystem.getDevice(uid, null);
    if (mDevice instanceof MBrickDC) {
      logger.trace("dc speed setPoint action");
      return ((MBrickDC) mDevice).setSpeed(speed, acceleration, drivemode);
    } else {
      logger.trace("no Brick DC device found for uid {}", uid);
      return false;
    }
  }

  @Override
  public boolean tfDCMotorSetspeed(String uid, String speed, String acceleration, String drivemode) {
    if (ecosystem == null) {
      logger.error("tfDCMotorSetspeed action failed ecosystem is null");
      return false;
    }
    MBaseDevice mDevice = ecosystem.getDevice(uid, null);
    if (mDevice instanceof MBrickDC) {
      logger.trace("dc speed setPoint action");
      return ((MBrickDC) mDevice).setSpeed(Short.parseShort(speed), Integer.parseInt(acceleration),
          drivemode);
    } else {
      logger.trace("no Brick DC device found for uid {}", uid);
      return false;
    }
  }

  @Override
  public boolean tfRotaryEncoderClear(String uid) {
    if (ecosystem == null) {
      logger.error("tfRotaryEncoderClear action failed ecosystem is null");
      return false;
    }
    MBaseDevice mDevice = ecosystem.getDevice(uid, "encoder");
    if (mDevice instanceof RotaryEncoder) {
      logger.trace("rotary encoder clear action");
      ((RotaryEncoder) mDevice).clear();
      return true;
    } else {
      logger.error("no Rotary Ecoder found for uid {}", uid);
      return false;
    }
  }

  @Override
  public boolean tfLoadCellTare(String uid) {
    if (ecosystem == null) {
      logger.error("tfLoadCellTare action failed ecosystem is null");
      return false;
    }
    MBaseDevice mDevice = ecosystem.getDevice(uid, "weight");
    if (mDevice instanceof LoadCellWeight) {
      logger.trace("load cell tare action");
      ((LoadCellWeight) mDevice).tare();
      return true;
    } else {
      logger.error("no Load Cell Bricklet found for uid {}", uid);
      return false;
    }
  }

}
