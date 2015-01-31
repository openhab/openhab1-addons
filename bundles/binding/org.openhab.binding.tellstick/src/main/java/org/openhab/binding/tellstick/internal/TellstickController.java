/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal;

import org.openhab.binding.tellstick.TellstickBindingConfig;
import org.openhab.binding.tellstick.TellstickValueSelector;
import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.openhab.binding.tellstick.internal.device.TellstickException;
import org.openhab.binding.tellstick.internal.device.iface.DeviceIntf;
import org.openhab.binding.tellstick.internal.device.iface.DimmableDeviceIntf;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the coordinator between openhab and telldus center, it is
 * responsible for all updates in both directions.
 * 
 * @since 1.5.0
 * @author jarlebh
 * 
 */
public class TellstickController {

	private static final Logger logger = LoggerFactory.getLogger(TellstickController.class);
	private long lastSend = 0;
	public static final long DEFAULT_INTERVAL_BETWEEN_SEND = 250;

	public void handleSendEvent(TellstickBindingConfig config, TellstickDevice dev, Command command)
			throws TellstickException {

		int resend = config.getResend();
		long resendInterval = config.getResendInterval();
		for (int i = 0; i < resend; i++) {
			checkLastAndWait(resendInterval);
			logger.info("Send " + command + " to " + dev + " time=" + i + " conf " + config);
			switch (config.getValueSelector()) {

			case COMMAND:
				if (command == OnOffType.ON) {
					if (config.getUsageSelector() == TellstickValueSelector.DIMMABLE) {
						turnOff(dev);
						checkLastAndWait(resendInterval);
					}
					turnOn(config, dev);
				} else if (command == OnOffType.OFF) {
					turnOff(dev);
				}
				break;
			case DIMMING_LEVEL:
				if (command == OnOffType.ON) {
					turnOn(config, dev);
				} else if (command == OnOffType.OFF) {
					turnOff(dev);
				} else if (command instanceof PercentType) {
					dim(dev, (PercentType) command);
				} else if (command instanceof IncreaseDecreaseType) {
					increaseDecrease(dev, ((IncreaseDecreaseType) command));
				}
				break;
			default:
				break;
			}
		}

	}

	private void increaseDecrease(TellstickDevice dev, IncreaseDecreaseType increaseDecreaseType)
			throws TellstickException {
		String strValue = dev.getData();
		double value = 0;
		if (strValue != null) {
			 value = Double.valueOf(strValue);
		}
		int percent = (int) Math.round((value / 255) * 100);
		if (IncreaseDecreaseType.INCREASE == increaseDecreaseType) {			
			percent = Math.min(percent + 10, 100);			
		} else if (IncreaseDecreaseType.DECREASE == increaseDecreaseType) {
			percent = Math.max(percent - 10, 0);				
		}
		
		dim(dev, new PercentType(percent));
	}

	private void dim(TellstickDevice dev, PercentType command) throws TellstickException {
		double value = command.doubleValue();
		
		// 0 means OFF and 100 means ON
		if(value == 0 && dev instanceof DeviceIntf) {
			((DeviceIntf) dev).off();
		} else if(value == 100 && dev instanceof DeviceIntf) {
			((DeviceIntf) dev).on();
		} else if (dev instanceof DimmableDeviceIntf) {
			long tdVal = Math.round((value / 100) * 255);
			((DimmableDeviceIntf) dev).dim((int) tdVal);
		} else {
			throw new RuntimeException("Cannot send DIM to " + dev);
		}
	}

	private void turnOff(TellstickDevice dev) throws TellstickException {
		if (dev instanceof DeviceIntf) {
			((DeviceIntf) dev).off();
		} else {
			throw new RuntimeException("Cannot send OFF to " + dev);
		}
	}

	private void turnOn(TellstickBindingConfig config, TellstickDevice dev) throws TellstickException {
		if (dev instanceof DeviceIntf) {
			((DeviceIntf) dev).on();
		} else {
			throw new RuntimeException("Cannot send ON to " + dev);
		}
	}

	private void checkLastAndWait(long resendInterval) {
		while ((System.currentTimeMillis() - lastSend) < resendInterval) {
			logger.info("Wait for " + resendInterval + " millisec");
			try {
				Thread.sleep(resendInterval);
			} catch (InterruptedException e) {
				logger.error("Failed to sleep", e);
			}
		}
		lastSend = System.currentTimeMillis();
	}

	public long getLastSend() {
		return lastSend;
	}

	public void setLastSend(long currentTimeMillis) {
		lastSend = currentTimeMillis;
	}
}
