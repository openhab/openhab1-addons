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

/**
 * This class is responsible for PC dongle operations.
 * 
 * @author  Nikolay Viguro
 * @since 1.0.0
 */

import org.openhab.binding.noolite.internal.noolite4j.watchers.CommandType;
import org.openhab.binding.noolite.internal.noolite4j.watchers.DataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usb4java.Context;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import java.nio.ByteBuffer;

public class PC11xx {

	private static final short VENDOR_ID = 5824; // 0x16c0;
	private static final short PRODUCT_ID = 1503; // 0x05df;
	private static final Logger logger = LoggerFactory.getLogger(PC11xx.class);
	private final Context context = new Context();
	protected byte availableChannels = 8;
	private byte sendRepeat = 2;
	private final ByteBuffer buf = ByteBuffer.allocateDirect(8);

	public void open() throws LibUsbException {

		logger.debug("Initializing PC11xx");

		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) {
			try {
				throw new LibUsbException("Failed to itialize libusb", result);
			} catch (LibUsbException e) {
				logger.debug("Failed to itialize libusb: ", result);
				e.printStackTrace();
			}
		}
	}

	public void close() {
		logger.debug("Closing PC11xx");
		LibUsb.exit(context);
	}

	public boolean turnOn(byte channel) {
		if (channel - 1 >= availableChannels - 1) {
			logger.error("Total channels: " + availableChannels);
			return false;
		}
		logger.debug("Turning on channel {}", (channel));
		channel -= 1;

		buf.position(1);
		buf.put((byte) CommandType.TURN_ON.getCode());
		buf.position(4);
		buf.put(channel);

		writeToHID(buf);

		return true;
	}

	public boolean turnOff(byte channel) {
		if (channel - 1 >= availableChannels - 1) {
			logger.error("Total channels: " + availableChannels);
			return false;
		}
		logger.debug("Turn off channel {}", channel);
		channel -= 1;
		buf.position(1);
		buf.put((byte) CommandType.TURN_OFF.getCode());
		buf.position(4);
		buf.put(channel);

		writeToHID(buf);

		return true;
	}

	public boolean setLevel(byte channel, byte level) {
		if (channel - 1 >= availableChannels - 1) {
			logger.error("Total channels: " + availableChannels);
			return false;
		}

		channel -= 1;

		buf.position(1);
		buf.put((byte) CommandType.SET_LEVEL.getCode());
		buf.put((byte) 1);

		if (level > 99) {
			logger.debug("Turn on channel " + (channel));
			level = (byte) 155;
		} else if (level < 0) {
			logger.debug("Turn off channel " + (channel));
			level = 0;
		} else {
			logger.debug("setting level {} on channel {}", level, (channel));
		}

		buf.position(5);
		buf.put(level);

		buf.position(4);
		buf.put(channel);

		writeToHID(buf);

		return true;
	}

	public boolean setLevelRGB(byte channel, byte R, byte G, byte B) {
		if (channel - 1 >= availableChannels - 1) {
			logger.error("Total channels: " + availableChannels);
			return false;
		}

		logger.debug("Set bright on every channel of RGB-controller on channel {}", channel);

		channel -= 1;

		buf.position(1);
		buf.put((byte) CommandType.SET_LEVEL.getCode());
		buf.put((byte) DataFormat.FOUR_BYTE.ordinal());
		buf.position(4);
		buf.put(channel);
		buf.put(R);
		buf.put(G);
		buf.put(B);

		writeToHID(buf);

		return true;
	}

	public boolean bindChannel(byte channel) {
		if (channel - 1 >= availableChannels - 1) {
			logger.error("Total channels: " + availableChannels);
			return false;
		}

		buf.position(1);
		buf.put((byte) CommandType.BIND.getCode());
		buf.position(4);
		buf.put((byte) (channel - 1));

		logger.debug("Binding channel " + channel);

		writeToHID(buf);
		return true;
	}

	public boolean unbindChannel(byte channel) {
		if (channel - 1 >= availableChannels - 1) {
			logger.error("Total channels: " + availableChannels);
			return false;
		}

		buf.position(1);
		buf.put((byte) CommandType.UNBIND.getCode());
		buf.position(4);
		buf.put((byte) (channel - 1));

		logger.debug("Unbinding channel " + channel);

		writeToHID(buf);
		return true;
	}

	private void writeToHID(ByteBuffer command) {
		DeviceHandle handle = LibUsb.openDeviceWithVidPid(context, VENDOR_ID, PRODUCT_ID);

		if (handle == null) {
			logger.error("Device PC11XX not found!");
			return;
		}

		if (LibUsb.kernelDriverActive(handle, 0) == 1) {
			LibUsb.detachKernelDriver(handle, 0);
		}

		int ret = LibUsb.setConfiguration(handle, 1);

		if (ret != LibUsb.SUCCESS) {
			logger.error("Error configuring PC11XX");
			LibUsb.close(handle);
			if (ret == LibUsb.ERROR_BUSY) {
				logger.error(" PC11XX is busy");
			}
			return;
		}

		LibUsb.claimInterface(handle, 0);

		buf.position(0);
		buf.put((byte) (((sendRepeat & 0x3) << 6) + 0x30));

		logger.debug(
				"PC11XX buffer: " + command.get(0) + " " + command.get(1) + " " + command.get(2) + " " + command.get(3)
						+ " " + command.get(4) + " " + command.get(5) + " " + command.get(6) + " " + command.get(7));

		LibUsb.controlTransfer(handle, (byte) (LibUsb.REQUEST_TYPE_CLASS | LibUsb.RECIPIENT_INTERFACE), (byte) 0x9,
				(short) 0x300, (short) 0, command, 100L);

		LibUsb.attachKernelDriver(handle, 0);
		LibUsb.close(handle);

		buf.clear();
	}
}
