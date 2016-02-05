/*
 * Copyright 2014 Nikolay A. Viguro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhab.binding.noolite.internal.noolite4j;

import java.nio.ByteBuffer;

import org.openhab.binding.noolite.internal.noolite4j.watchers.BatteryState;
import org.openhab.binding.noolite.internal.noolite4j.watchers.CommandType;
import org.openhab.binding.noolite.internal.noolite4j.watchers.DataFormat;
import org.openhab.binding.noolite.internal.noolite4j.watchers.Notification;
import org.openhab.binding.noolite.internal.noolite4j.watchers.SensorType;
import org.openhab.binding.noolite.internal.noolite4j.watchers.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.Context;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

/**
 * This class is responsible for RX dongle operations.
 * 
 * @author Nikolay Viguro
 * @since 1.0.0
 */

public class RX2164 {

	private static final long READ_UPDATE_DELAY_MS = 200L;
	private static final short VENDOR_ID = 5824; // 0x16c0;
	private static final short PRODUCT_ID = 1500; // 0x05dc;
	private final Context context = new Context();
	private Watcher watcher = null;
	private byte availableChannels = 64;
	private boolean shutdown = false;
	private DeviceHandle handle;
	private boolean pause = false;
	private static final Logger logger = LoggerFactory.getLogger(RX2164.class);

	public void addWatcher(Watcher watcher) {
		this.watcher = watcher;
	}

	public void open() throws LibUsbException {

		logger.debug("Opening RX2164");

		// �?нициализируем контекст
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) {
			try {
				throw new LibUsbException("Failed to itialize libusb", result);
			} catch (LibUsbException e) {
				logger.debug("Failed to itialize libusb: ", result);
				e.printStackTrace();
			}
		}

		handle = LibUsb.openDeviceWithVidPid(context, VENDOR_ID, PRODUCT_ID);

		if (handle == null) {
			logger.debug("Device RX2164 not found!");
			return;
		}

		if (LibUsb.kernelDriverActive(handle, 0) == 1) {
			LibUsb.detachKernelDriver(handle, 0);
		}

		int ret = LibUsb.setConfiguration(handle, 1);

		if (ret != LibUsb.SUCCESS) {
			logger.debug("Error configuring RX2164");
			LibUsb.close(handle);
			if (ret == LibUsb.ERROR_BUSY) {
				logger.debug("Device RX2164 busy");
			}
			return;
		}

		LibUsb.claimInterface(handle, 0);
	}

	public void close() {
		logger.debug("Closing RX2164");
		shutdown = true;

		if (handle != null)
			LibUsb.close(handle);

		LibUsb.exit(context);
	}

	public void start() {
		logger.debug("Starting incoming monitor for RX2164");

		new Thread(new Runnable() {
			@Override
			public void run() {

				int togl;

				int tmpTogl = -10000;

				ByteBuffer buf = ByteBuffer.allocateDirect(8);

				while (!shutdown) {

					if (!pause) {
						LibUsb.controlTransfer(handle,
								(byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE | LibUsb.ENDPOINT_IN),
								(byte) 0x9, (short) 0x300, (short) 0, buf, 100L);
					}

					togl = buf.get(0) & 63;

					if (togl != tmpTogl) {

						Notification notification = new Notification();

						notification.setBuffer(buf);

						byte channel = (byte) (buf.get(1) + 1);
						byte action = buf.get(2);
						byte dataFormat = buf.get(3);

						logger.debug("Incoming message for RX2164");
						logger.debug("TOGL value: " + togl);
						logger.debug("Command: " + CommandType.getValue(action).name());
						logger.debug("Channel: " + channel);

						if (dataFormat == DataFormat.NO_DATA.ordinal()) {
							logger.debug("Count of data: no");
							notification.setDataFormat(DataFormat.NO_DATA);
						} else if (dataFormat == DataFormat.ONE_BYTE.ordinal()) {
							logger.debug("Count of data: 1 byte");
							notification.setDataFormat(DataFormat.ONE_BYTE);
						} else if (dataFormat == DataFormat.TWO_BYTE.ordinal()) {
							logger.debug("Count of data: 2 bytes");
							notification.setDataFormat(DataFormat.TWO_BYTE);
						} else if (dataFormat == DataFormat.FOUR_BYTE.ordinal()) {
							logger.debug("Count of data: 4 bytes");
							notification.setDataFormat(DataFormat.FOUR_BYTE);
						} else if (dataFormat == DataFormat.LED.ordinal()) {
							logger.debug("Count of data: LED format");
							notification.setDataFormat(DataFormat.LED);
						} else {
							logger.debug("Count of data: unknown type");
							notification.setDataFormat(DataFormat.NO_DATA);
						}

						notification.setChannel(channel);

						switch (CommandType.getValue(action)) {
						case TURN_ON:
							notification.setType(CommandType.TURN_ON);
							watcher.onNotification(notification);
							break;
						case TURN_OFF:
							notification.setType(CommandType.TURN_OFF);
							watcher.onNotification(notification);
							break;
						case SET_LEVEL:
							notification.setType(CommandType.SET_LEVEL);
							notification.addData("level", buf.get(4));
							logger.debug("Device level: " + buf.get(4));
							watcher.onNotification(notification);
							break;
						case SWITCH:
							notification.setType(CommandType.SWITCH);
							watcher.onNotification(notification);
							break;
						case SLOW_TURN_ON:
							notification.setType(CommandType.SLOW_TURN_ON);
							watcher.onNotification(notification);
							break;
						case SLOW_TURN_OFF:
							notification.setType(CommandType.SLOW_TURN_OFF);
							watcher.onNotification(notification);
							break;
						case STOP_DIM_BRIGHT:
							notification.setType(CommandType.STOP_DIM_BRIGHT);
							watcher.onNotification(notification);
							break;
						case REVERT_SLOW_TURN:
							notification.setType(CommandType.REVERT_SLOW_TURN);
							watcher.onNotification(notification);
							break;
						case RUN_SCENE:
							notification.setType(CommandType.RUN_SCENE);
							watcher.onNotification(notification);
							break;
						case RECORD_SCENE:
							notification.setType(CommandType.RECORD_SCENE);
							watcher.onNotification(notification);
							break;
						case BIND:
							notification.setType(CommandType.BIND);

							if (notification.getDataFormat() == DataFormat.ONE_BYTE) {
								notification.addData("sensortype", SensorType.values()[(buf.get(4) & 0xff)]);
							}

							watcher.onNotification(notification);
							break;
						case UNBIND:
							notification.setType(CommandType.UNBIND);
							watcher.onNotification(notification);
							break;
						case SLOW_RGB_CHANGE:
							notification.setType(CommandType.SLOW_RGB_CHANGE);
							watcher.onNotification(notification);
							break;
						case SWITCH_COLOR:
							notification.setType(CommandType.SWITCH_COLOR);
							watcher.onNotification(notification);
							break;
						case SWITCH_MODE:
							notification.setType(CommandType.SWITCH_MODE);
							watcher.onNotification(notification);
							break;
						case SWITCH_SPEED_MODE:
							notification.setType(CommandType.SWITCH_SPEED_MODE);
							watcher.onNotification(notification);
							break;
						case BATTERY_LOW:
							notification.setType(CommandType.BATTERY_LOW);
							watcher.onNotification(notification);
							break;
						case TEMP_HUMI:
							notification.setType(CommandType.TEMP_HUMI);

							int intTemp = ((buf.get(5) & 0x0f) << 8) + (buf.get(4) & 0xff);

							if (intTemp >= 0x800) {
								intTemp = intTemp - 0x1000;
							}
							double temp = (double) intTemp / 10;

							notification.addData("battery", BatteryState.values()[(buf.get(5) >> 7) & 1]);

							notification.addData("temp", temp);

							notification.addData("sensortype", SensorType.values()[((buf.get(5) >> 4) & 7)]);

							notification.addData("humi", buf.get(6) & 0xff);

							notification.addData("analog", buf.get(7) & 0xff);

							watcher.onNotification(notification);
							break;

						default:
							logger.debug("Unknown command: " + action);
						}
					}

					try {
						Thread.sleep(READ_UPDATE_DELAY_MS);
					} catch (InterruptedException e) {
						logger.error("Error: " + e.getMessage());
						e.printStackTrace();
					}

					tmpTogl = togl;
					buf.clear();
				}
			}
		}).start();
	}

	public void bindChannel(byte channel) {
		if (channel > availableChannels - 1) {
			logger.error("Error! Channel count is less than your value!");
			return;
		}

		ByteBuffer buf = ByteBuffer.allocateDirect(8);
		buf.put((byte) 1);
		buf.put((byte) (channel - 1));

		logger.debug("Buffer Content: " + buf.get(0) + " " + buf.get(1) + " " + buf.get(2) + " " + buf.get(3) + " "
				+ buf.get(4) + " " + buf.get(5) + " " + buf.get(6) + " " + buf.get(7));

		pause = true;
		LibUsb.controlTransfer(handle, (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE), (byte) 0x9,
				(short) 0x300, (short) 0, buf, 100L);
		pause = false;
	}

	public void unbindChannel(byte channel) {
		if (channel - 1 > availableChannels - 1) {
			logger.error("Error! Channel count is less than your value!");
			return;
		}

		ByteBuffer buf = ByteBuffer.allocateDirect(8);
		buf.put((byte) 3);
		buf.put((byte) (channel - 1));

		pause = true;
		LibUsb.controlTransfer(handle, (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE), (byte) 0x9,
				(short) 0x300, (short) 0, buf, 100L);
		pause = false;
	}

	public void unbindAllChannels() {
		ByteBuffer buf = ByteBuffer.allocateDirect(8);
		buf.put((byte) 4);

		pause = true;
		LibUsb.controlTransfer(handle, (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE), (byte) 0x9,
				(short) 0x300, (short) 0, buf, 100);
		pause = false;
	}
}
