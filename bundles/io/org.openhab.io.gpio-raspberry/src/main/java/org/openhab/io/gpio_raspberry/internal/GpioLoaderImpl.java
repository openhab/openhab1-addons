package org.openhab.io.gpio_raspberry.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.gpio_raspberry.GpioException;
import org.openhab.io.gpio_raspberry.GpioLoader;
import org.openhab.io.gpio_raspberry.device.I2CConfig;
import org.openhab.io.gpio_raspberry.device.I2CDevice;
import org.openhab.io.gpio_raspberry.item.GpioI2CItemConfig;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class GpioLoaderImpl extends AbstractActiveService implements ManagedService, GpioLoader {
	
	private Map<String, I2CDevice<I2CConfig, GpioI2CItemConfig>> deviceI2CList = new HashMap<String, I2CDevice<I2CConfig, GpioI2CItemConfig>>();

	/* (non-Javadoc)
	 * @see org.openhab.io.gpio_raspberry.GpioLoader#createDevice(DC, java.lang.Class)
	 */
	public <DC extends I2CConfig, IC extends GpioI2CItemConfig> I2CDevice<DC, IC> createI2CDevice(DC config, Class<? extends I2CDevice<DC, IC>> deviceType) throws GpioException {
		if (config instanceof I2CConfig) {
			if (deviceI2CList.containsKey(config.getId())) {
				return (I2CDevice<DC, IC>) deviceI2CList.get(config.getId());
			} else {
				try {
					Constructor<?> constructor = deviceType.getConstructor(config.getClass());
					I2CDevice device = (I2CDevice) constructor.newInstance(config);
					this.deviceI2CList.put(config.getId(), device);
					return device;
				} catch (InstantiationException e) {
					throw new GpioException("cannot create device", e);
				} catch (IllegalAccessException e) {
					throw new GpioException("cannot create device", e);
				} catch (IllegalArgumentException e) {
					throw new GpioException("cannot create device", e);
				} catch (InvocationTargetException e) {
					throw new GpioException("cannot create device", e);
				} catch (NoSuchMethodException e) {
					throw new GpioException("cannot create device", e);
				} catch (SecurityException e) {
					throw new GpioException("cannot create device", e);
				}
				
			}
		} else {
			throw new GpioException("unexpected config type");
		}
		
	}
	
	

	public void updated(Dictionary<String, ?> properties)
			throws ConfigurationException {
		if (properties != null) {
			System.out.println(properties);
		}
	}



	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected long getRefreshInterval() {
		return 0;
	}



	@Override
	protected String getName() {
		return "gpio-raspberry";
	}

}
