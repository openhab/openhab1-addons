/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.tinkerforge.internal;

import org.openhab.binding.tinkerforge.ecosystem.TinkerforgeContext;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
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
    if (!TinkerForgeActionService.isProperlyConfigured) {
      logger.debug("TinkerForge action is not yet configured - execution aborted!");
      return false;
    }
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
      @ParamDoc(name = "uid", text = "servo number 0-6") String num,
      @ParamDoc(name = "uid", text = "servo postion -9000 - 9000") String position,
      @ParamDoc(name = "uid", text = "servo velocity") String velocity,
      @ParamDoc(name = "uid", text = "servo acceleration") String acceleration) {
    if (!TinkerForgeActionService.isProperlyConfigured) {
      logger.debug("TinkerForge action is not yet configured - execution aborted!");
      return false;
    }
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
}
