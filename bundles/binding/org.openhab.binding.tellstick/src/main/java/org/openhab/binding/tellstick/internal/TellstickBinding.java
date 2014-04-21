/**
 * Copyright (c) 2013-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.tellstick.TellstickBindingConfig;
import org.openhab.binding.tellstick.TellstickBindingProvider;
import org.openhab.binding.tellstick.TellstickValueSelector;
import org.openhab.binding.tellstick.internal.JNA.DataType;
import org.openhab.binding.tellstick.internal.JNA.Method;
import org.openhab.binding.tellstick.internal.device.SupportedMethodsException;
import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.openhab.binding.tellstick.internal.device.TellstickDeviceEvent;
import org.openhab.binding.tellstick.internal.device.TellstickSensorEvent;
import org.openhab.binding.tellstick.internal.device.iface.DeviceChangeListener;
import org.openhab.binding.tellstick.internal.device.iface.SensorListener;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class coordinates between the events in openHAB and the Tellstick
 * device. It uses a JNA bridge to talk to the C api of the tellstick.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickBinding extends AbstractActiveBinding<TellstickBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(TellstickBinding.class);
	/**
	 * Default update interval from tellstick
	 */
	private static final int UPDATE_FROM_TELLSTICK_INTERVAL = 600000;

	/**
	 * Max time without receiving any events.
	 */
	private static final int MAX_IDLE_BEFORE_RESTART = 600000;

	private int restartTimeout;

	private int updateFromTellstickInterval;

	private long lastRefresh = 0;

	/**
	 * the refresh interval which is used to poll values from the Tellstick
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private TellstickController controller = new TellstickController();

	public TellstickBinding() {
	}

	public void activate() {
		logger.info("Activate " + Thread.currentThread());
	}

	public void deactivate() {
		logger.info("Deactivate " + this);
		try {
			deRegisterListeners();
		} catch (Exception e) {
			logger.error("Failed to deactivate", e);
		}
	}

	private void registerListeners() {
		for (TellstickBindingProvider prov : providers) {
			prov.addListener(new TellstickDeviceEventHandler());
			prov.addListener(new TellstickSensorEventHandler());
		}
	}

	private void deRegisterListeners() throws SupportedMethodsException {
		for (TellstickBindingProvider prov : providers) {
			prov.removeTellstickListener();
		}
	}

	private void resetTelldusProvider() throws SupportedMethodsException {
		for (TellstickBindingProvider prov : providers) {
			prov.resetTellstickListener();
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called! for " + itemName + " with " + command);
		TellstickBindingConfig config = findTellstickBindingConfig(itemName);
		if (config != null) {
			try {
				TellstickDevice dev = findDevice(config);
				controller.handleSendEvent(config, dev, command);
			} catch (Exception e) {
				logger.error("Failed to send msg to " + config, e);
			}
		}
	}

	private TellstickDevice findDevice(TellstickBindingConfig config) {
		TellstickDevice dev = null;
		for (TellstickBindingProvider prov : providers) {
			dev = ((TellstickBindingProvider) prov).getDevice(config.getItemName());
			if (dev != null) {
				break;
			}
		}
		return dev;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		this.updateFromTellstickInterval = UPDATE_FROM_TELLSTICK_INTERVAL;
		this.restartTimeout = MAX_IDLE_BEFORE_RESTART;

		logger.info("Called with config " + config);
		if (config != null) {
			String maxIdle = (String) config.get("max_idle");
			String updateInterval = (String) config.get("update_interval");
			if (maxIdle != null) {
				this.restartTimeout = Integer.valueOf(maxIdle);
			}
			if (updateInterval != null) {
				this.updateFromTellstickInterval = Integer.valueOf(updateInterval);
			}
		}
		resetTellstick();
		setProperlyConfigured(true);
	}

	private TellstickBindingConfig findTellstickBindingConfig(int itemId, TellstickValueSelector valueSel) {

		TellstickBindingConfig matchingConfig = null;
		for (TellstickBindingProvider provider : this.providers) {
			TellstickBindingConfig config = provider.getTellstickBindingConfig(itemId, valueSel);
			if (config != null) {
				matchingConfig = config;
				break;
			}
		}
		return matchingConfig;
	}

	private TellstickBindingConfig findTellstickBindingConfig(String itemName) {
		TellstickBindingConfig matchingConfig = null;
		for (TellstickBindingProvider provider : this.providers) {
			TellstickBindingConfig config = provider.getTellstickBindingConfig(itemName);
			if (config != null) {
				matchingConfig = config;
				break;
			}
		}
		return matchingConfig;
	}

	class TellstickDeviceEventHandler implements DeviceChangeListener {
		@Override
		public void onRequest(TellstickDeviceEvent newDevices) {
			handleDeviceEvent(newDevices);
		}
	}

	private void handleDeviceEvent(TellstickDeviceEvent event) {
		TellstickDevice device = event.getDevice();
		controller.setLastSend(System.currentTimeMillis());
		logger.debug("Got deviceEvent for " + device + " name:" + device + " method" + event.getMethod());
		if (device != null) {
			State cmd = resolveCommand(event.getMethod(), event.getData());
			TellstickBindingConfig conf = findTellstickBindingConfig(device.getId(), null);
			if (conf != null) {
				sendToOpenHab(conf.getItemName(), cmd);
			} else {
				logger.info("Could not find config for " + device);
			}
		}
	}

	private State resolveCommand(Method method, String data) {
		State cmd = null;
		if (method == Method.TURNON) {
			cmd = OnOffType.ON;
		} else if (method == Method.TURNOFF) {
			cmd = OnOffType.OFF;
		} else if (method == Method.DIM) {
			double value = ((Double.valueOf(data)) / 255);
			cmd = new PercentType((int) (value * 100));
		}
		return cmd;
	}

	private void sendToOpenHab(String itemName, State cmd) {
		eventPublisher.postUpdate(itemName, cmd);
	}

	class TellstickSensorEventHandler implements SensorListener {
		private Map<DataType, String> prevMessages = new HashMap<DataType, String>();

		private void handleSensorEvent(TellstickSensorEvent event, TellstickBindingConfig device) {

			double dValue = Double.valueOf(event.getData());
			TellstickValueSelector selector = device.getUsageSelector();
			if (selector == null) {
				selector = device.getValueSelector();
			}
			State cmd = getCommand(event, dValue, selector);
			sendToOpenHab(device.getItemName(), cmd);
		}

		private State getCommand(TellstickSensorEvent event, double dValue, TellstickValueSelector selector) {
			State cmd = null;
			if (event.getDataType() == DataType.TEMPERATURE) {
				switch (selector) {
				case MOTION:
					cmd = OnOffType.ON;
					break;
				default:
					cmd = new DecimalType(dValue);
				}
			} else if (event.getDataType() == DataType.HUMIDITY) {
				switch (selector) {
				case BATTERY_LEVEL:
					cmd = new DecimalType(dValue);
					break;
				case HUMIDITY:
				default:
					double val = Math.min(100, dValue);
					cmd = new PercentType((int) val);

				}
			}
			return cmd;
		}

		private TellstickValueSelector getSensorBindingType(DataType dataType) {
			TellstickValueSelector result = null;
			switch (dataType) {
			case TEMPERATURE:
				result = TellstickValueSelector.TEMPERATURE;
				break;
			case HUMIDITY:
				result = TellstickValueSelector.HUMIDITY;
				break;
			default:
				logger.warn("Sensor of type " + dataType + " not supported");
			}
			return result;
		}

		@Override
		public void onRequest(TellstickSensorEvent sensorEvent) {
			controller.setLastSend(System.currentTimeMillis());

			String thisMsg = sensorEvent.getProtocol() + sensorEvent.getModel() + sensorEvent.getSensorId()
					+ sensorEvent.getData();
			String prevMessage = prevMessages.get(sensorEvent.getDataType());
			if (!thisMsg.equals(prevMessage)) {
				prevMessages.put(sensorEvent.getDataType(), thisMsg);
				TellstickBindingConfig device = findTellstickBindingConfig(sensorEvent.getSensorId(),
						getSensorBindingType(sensorEvent.getDataType()));
				logger.debug("Got sensorEvent for " + sensorEvent.getSensorId() + " name:" + device + " value:"
						+ sensorEvent.getData());
				if (device != null) {
					handleSensorEvent(sensorEvent, device);
				}
			} else {
				logger.debug("Ignored message for " + sensorEvent.getSensorId() + " value:" + sensorEvent.getData());
			}
		}

	}

	@Override
	protected void execute() {
		long lastSend = controller.getLastSend();
		logger.trace("Check thread current idle ms " + (System.currentTimeMillis() - lastSend));
		if ((System.currentTimeMillis() - lastSend) > restartTimeout) {
			// RE-INIT
			resetTellstick();

		}
		if ((System.currentTimeMillis() - lastRefresh) > updateFromTellstickInterval) {
			refreshFromTellstick();
		}
	}

	private void refreshFromTellstick() {
		logger.trace("Update with telldus state");
		for (TellstickBindingProvider prov : providers) {
			for (String name : prov.getItemNames()) {
				TellstickDevice dev = prov.getDevice(name);
				if (dev != null) {
					Method method = Method.getMethodById(dev.getStatus());
					sendToOpenHab(name, resolveCommand(method, dev.getData()));
				}

			}
		}
		lastRefresh = System.currentTimeMillis();
	}

	private void resetTellstick() {
		logger.warn("Will do a reinit of listeners, no message received for " + restartTimeout / 1000 + " seconds");
		try {
			deRegisterListeners();
			logger.info("Listeners removed");
			resetTelldusProvider();
			logger.info("Telldus reset");
			registerListeners();
			logger.info("Listeners restarted");
		} catch (Exception e) {
			logger.error("Failed to reset listener", e);
		}
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Telldus Sync Service";
	}
}
