package org.openhab.binding.pcf8591.internal;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.gpio_raspberry.device.I2CDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCF8591Device extends I2CDevice<PCF8591Config, PCF8591ItemConfig> {
	private static final Logger LOG = LoggerFactory.getLogger(PCF8591Device.class);

	public PCF8591Device(PCF8591Config config) {
		super(config);
	}

	@Override
	public State communicate(Command command, PCF8591ItemConfig itemConfig, State state) {
		byte value = 0x00;
		if (command instanceof PercentType) {
			value = (byte) (((PercentType) command).intValue() * 255 / 100);
		} else if (command instanceof OnOffType) {
			if (((OnOffType) command) == OnOffType.ON) {
				value = (byte) 0xFF;
			} else if (((OnOffType) command) == OnOffType.OFF) {
				value = (byte) 0x00;
			} else {
				LOG.error("unhandled type: " + command);
			}
		} else if (command instanceof IncreaseDecreaseType) {
			switch (((IncreaseDecreaseType) command)) {
				case INCREASE:
//					if (state instanceof org.openhab.core.types.UnDefType) {
//						
//					}
					value += 5;
					break;
				case DECREASE:
					value -= 5;
					break;
				default:
					LOG.error("unhandled type: " + command);
					return null;
			}
		} else {
			LOG.warn("invalid command: " + command);
			return null;
		}
		LOG.debug("setting value: " + value);
		
		try {
			LOG.trace("active locks: " + LOCK.getHoldCount());
			if (LOCK.tryLock(TIMEOUT, TIMEOUT_UNIT)) {
				super.open("/dev/i2c-1");
				super.set((byte) 0x40, value);
				super.close();
			} else {
				LOG.error("timeout while waiting for I2C");
			}
		} catch (InterruptedException e) {
			LOG.warn("waiting for I2C has been interrupted", e);
		} finally {
			LOCK.unlock();
		}
		return null;
	}

}
