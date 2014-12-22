package org.openhab.io.gpio_raspberry.device;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.openhab.io.gpio_raspberry.internal.Device;
import org.openhab.io.gpio_raspberry.item.GpioI2CItemConfig;

import com.pi4j.jni.I2C;


public abstract class I2CDevice<DC extends I2CConfig, IC extends GpioI2CItemConfig> extends Device<DC, IC> {
	protected static final ReentrantLock LOCK = new ReentrantLock(true);
	protected static final int TIMEOUT = 10;
	protected static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
	
	private int handle;
	
	public I2CDevice() {
		super();
	}

	public I2CDevice(DC config) {
		super(config);
	}

	public boolean set(byte register, byte value) {
		int res = I2C.i2cWriteByte(this.handle, this.config.getAddress(), register, value);
		if (res < 0) {
			return false;
		}
		return true;
	}
	
	protected void open(String bus) {
		this.handle = I2C.i2cOpen(bus);
	}
	
	protected void close() {
		I2C.i2cClose(handle);
	}
}
