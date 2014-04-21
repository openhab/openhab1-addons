/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import org.openhab.binding.tellstick.internal.JNA;
import org.openhab.binding.tellstick.internal.JNA.DataType;
import org.openhab.binding.tellstick.internal.JNA.Method;
import org.openhab.binding.tellstick.internal.device.iface.DeviceChangeListener;
import org.openhab.binding.tellstick.internal.device.iface.SensorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Pointer;

/**
 * A callback listener to telldus that distributes to other listeners.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellsticEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(TellsticEventHandler.class);
	List<TellstickDevice> list;
	List<EventListener> changeListeners = new ArrayList<EventListener>();

	private int handleSensor;
	private int handleDeviceChange;
	private JNA.CLibrary.TDDeviceChangeEvent deviceChangeHandler;
	private int handleDeviceEvent;
	private JNA.CLibrary.TDDeviceEvent deviceEventHandler;
	private JNA.CLibrary.TDSensorEvent sensorEventHandler;

	public TellsticEventHandler(List<TellstickDevice> deviceList) {
		this.list = java.util.Collections.synchronizedList(deviceList);
		this.setupListeners();
	}

	public void setDeviceList(List<TellstickDevice> deviceList) {
		list = java.util.Collections.synchronizedList(deviceList);
	}

	public void addListener(EventListener listener) {
		this.changeListeners.add(listener);
	}

	public void removeListener(EventListener listener) {
		this.changeListeners.remove(listener);
	}

	public void remove() {
		JNA.CLibrary.INSTANCE.tdUnregisterCallback(handleDeviceEvent);
		JNA.CLibrary.INSTANCE.tdUnregisterCallback(handleDeviceChange);
		JNA.CLibrary.INSTANCE.tdUnregisterCallback(handleSensor);
		sensorEventHandler = null;
		deviceChangeHandler = null;
		deviceEventHandler = null;
	}

	public List<EventListener> getAllListeners() {
		return changeListeners;
	}

	private void notifyListeners(TellstickDevice ts, Method m, String dataStr) {
		for (EventListener changeListener : changeListeners) {
			if (changeListener instanceof DeviceChangeListener) {
				((DeviceChangeListener) changeListener).onRequest(new TellstickDeviceEvent(ts, m, dataStr));
			}
		}
	}

	private void notifySensorListeners(int deviceId, String protocol, String model, DataType type, String dataStr) {
		for (EventListener changeListener : changeListeners) {
			if (changeListener instanceof SensorListener) {
				((SensorListener) changeListener).onRequest(new TellstickSensorEvent(deviceId, dataStr, type, protocol,
						model));
			}
		}
	}

	public void setupListeners() {
		deviceEventHandler = new JNA.CLibrary.TDDeviceEvent() {
			@Override
			public void invoke(int deviceId, int method, Pointer data, int callbackId, Pointer context)
					throws SupportedMethodsException {

				try {
					TellstickDevice ts = TellstickDevice.getDevice(deviceId);

					int idx = Collections.binarySearch(list, ts);
					if (idx > -1) {
						list.set(idx, ts);
					}
					Method m = Method.getMethodById(method);
					String dataStr = null;
					if (m == Method.DIM) {
						dataStr = data.getString(0);
					}
					notifyListeners(ts, m, dataStr);
					// The device is not supported.
				} catch (Exception e) {
					logger.error("Failed in TDDeviceEvent", e);
				}

			}

		};

		handleDeviceEvent = JNA.CLibrary.INSTANCE.tdRegisterDeviceEvent(deviceEventHandler, null);

		deviceChangeHandler = new JNA.CLibrary.TDDeviceChangeEvent() {
			@Override
			public void invoke(int deviceId, int method, int changeType, int callbackId, Pointer context)
					throws SupportedMethodsException {
				try {

					TellstickDevice ts = null;

					if (method == JNA.CLibrary.TELLSTICK_DEVICE_CHANGED
							|| method == JNA.CLibrary.TELLSTICK_DEVICE_STATE_CHANGED) {
						ts = TellstickDevice.getDevice(deviceId);
						int idx = Collections.binarySearch(list, ts);
						if (idx > -1) {
							list.set(idx, ts);
						}
					}

					if (method == JNA.CLibrary.TELLSTICK_DEVICE_ADDED) {
						ts = TellstickDevice.getDevice(deviceId);
						list.add(ts);
					}
					if (method == JNA.CLibrary.TELLSTICK_DEVICE_ADDED) {
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getId() == deviceId) {
								ts = list.remove(i);
								return;
							}
						}
					}

					Method m = Method.getMethodById(method);
					notifyListeners(ts, m, null);
					// The device is not supported.
				} catch (Exception e) {
					logger.error("Failed in TDDeviceChangeEvent", e);

				}
			}
		};
		handleDeviceChange = JNA.CLibrary.INSTANCE.tdRegisterDeviceChangeEvent(deviceChangeHandler, null);
		sensorEventHandler = new JNA.CLibrary.TDSensorEvent() {
			@Override
			public void invoke(String protocol, String model, int deviceId, int dataType, Pointer value, int timeStamp,
					int callbackId, Pointer context) throws SupportedMethodsException {
				try {
					DataType m = DataType.getDataTypeId(dataType);
					notifySensorListeners(deviceId, protocol, model, m, value.getString(0));
				} catch (Exception e) {
					logger.error("Failed in TDSensorEvent", e);
				}
			}
		};
		handleSensor = JNA.CLibrary.INSTANCE.tdRegisterSensorEvent(sensorEventHandler, null);

	}

}
