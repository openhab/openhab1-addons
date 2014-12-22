package org.openhab.io.gpio_raspberry.device;

import java.util.HashMap;
import java.util.Map;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.gpio_raspberry.internal.Device;
import org.openhab.io.gpio_raspberry.item.GpioIOItemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class IODevice extends Device<IOConfig, GpioIOItemConfig> {
	private static final Logger LOG = LoggerFactory.getLogger(IODevice.class);
	
	private Map<Byte, GpioPinDigitalOutput> pinMap = new HashMap<Byte, GpioPinDigitalOutput>();

	public IODevice() {
		super();
	}

	@Override
	public State communicate(Command command, GpioIOItemConfig itemConfig, State state) {
		LOG.debug("communicate with gpio device");
		IOConfig ioBindingConfig = (IOConfig) config;
		
		GpioController controller = GpioFactory.getInstance();
		LOG.debug("GPIO controller created");
		
		if (ioBindingConfig.in) {
			LOG.error("not implemented");
			throw new IllegalStateException("currently not implemented");
		} else {
			LOG.debug("setting output pin...");
			GpioPinDigitalOutput pin = null;
			if (this.pinMap.containsKey(itemConfig.port)) {
				pin = pinMap.get(itemConfig.port);
			} else {
				final Pin raspiPin = this.getRaspiPin(itemConfig.port);
				LOG.debug("resolved to raspi-pin: " + raspiPin);
				pin = controller.provisionDigitalOutputPin(raspiPin);
				pinMap.put(itemConfig.port, pin);
			}
			
			
			try {
				if (command instanceof OnOffType) {
					boolean on = false;
					if (((OnOffType) command) == OnOffType.ON) {
						on = true;
					} else if (((OnOffType) command) == OnOffType.OFF) {
						on = false;
					} else {
						throw new IllegalStateException("not implemented");
					}
					if (ioBindingConfig.activeLow) {
						on = !on;
					}
					LOG.debug("set pin '" + config + "' to: " + on);
					pin.setState(on);
				} else {
					throw new IllegalStateException("unsupported command: " + command);
				}
			} finally {
				pin.unexport();
			}
			return null;
		}
	}
	
	private Pin getRaspiPin(Byte port) {
		if (port == null) {
			throw new IllegalStateException("port is not set");
		}
		LOG.debug("resolving port '" + port + "' to raspi pin");
		switch (port) {
		case 0:
			return RaspiPin.GPIO_00;
		case 1:
			return RaspiPin.GPIO_01;
		case 2:
			return RaspiPin.GPIO_02;
		case 3:
			return RaspiPin.GPIO_03;
		case 4:
			return RaspiPin.GPIO_04;
		case 5:
			return RaspiPin.GPIO_05;
		case 6:
			return RaspiPin.GPIO_06;
		case 7:
			return RaspiPin.GPIO_07;
		case 8:
			return RaspiPin.GPIO_08;
		case 9:
			return RaspiPin.GPIO_09;
		case 10:
			return RaspiPin.GPIO_10;
		case 11:
			return RaspiPin.GPIO_11;
		case 12:
			return RaspiPin.GPIO_12;
		case 13:
			return RaspiPin.GPIO_13;
		case 14:
			return RaspiPin.GPIO_14;
		case 15:
			return RaspiPin.GPIO_15;
		case 16:
			return RaspiPin.GPIO_16;
		case 17:
			return RaspiPin.GPIO_17;
		case 18:
			return RaspiPin.GPIO_18;
		case 19:
			return RaspiPin.GPIO_19;
		case 20:
			return RaspiPin.GPIO_20;
		default:
			throw new IllegalStateException("not a valid port: " + port);
		}
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof IODevice)) {
			return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
