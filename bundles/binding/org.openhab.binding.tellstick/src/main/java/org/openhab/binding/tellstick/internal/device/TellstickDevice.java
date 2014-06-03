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

import org.openhab.binding.tellstick.internal.JNA;

/**
 * Any tellstick device.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickDevice implements Comparable<TellstickDevice> {

	/**
	 * Holds the device ID.
	 */
	protected int deviceId;

	/**
	 * Holds the device Name.
	 */
	protected String name;

	/**
	 * Holds the device model
	 */
	protected String model;

	/**
	 * Holds the device protocol.
	 */
	protected String protocol;

	/**
	 * Holds the current Device Status.
	 */
	protected int status;

	/**
	 * Holds the device type.
	 */
	protected int deviceType;

	/**
	 * Holds the last send data
	 */
	protected String data;
	/**
	 * Holds all the supported methods! Must be set with setSupportedMethods.
	 */
	static protected int supportedMethods = -1;

	/**
	 * Tellstick success flag from C Lib.
	 */
	static final public int TELLSTICK_SUCCESS = 0;

	/**
	 * This is a must to set for all applications that is getting developed! If
	 * not set SupportedMethodsException will get thrown whenever you try to get
	 * / create devices. Usage: // Set supported methods for this app.
	 * TellstickDevice.setSupportedMethods( JNA.CLibrary.TELLSTICK_BELL |
	 * JNA.CLibrary.TELLSTICK_TURNOFF | JNA.CLibrary.TELLSTICK_TURNON |
	 * JNA.CLibrary.TELLSTICK_DIM | JNA.CLibrary.TELLSTICK_LEARN |
	 * JNA.CLibrary.TELLSTICK_EXECUTE | JNA.CLibrary.TELLSTICK_STOP );
	 * 
	 * @param supportedM
	 */
	static public void setSupportedMethods(int supportedM) {
		supportedMethods = supportedM;
	}

	/**
	 * Checks if supported methods is set. This is required for this lib to
	 * work.
	 * 
	 * @throws SupportedMethodsException
	 */
	static public void supportedMethodsCheck() throws SupportedMethodsException {
		if (supportedMethods == -1)
			throw new SupportedMethodsException(
					"You must set supported methods with TellstickDevice.setSupportedMethods( ... ) to define what methods your application supports.");
	}

	/**
	 * Constuctor of the device.
	 * 
	 * @param deviceId
	 * @throws SupportedMethodsException
	 */
	public TellstickDevice(int deviceId) throws SupportedMethodsException {
		supportedMethodsCheck();
		this.deviceId = deviceId;

		// Get name
		this.name = JNA.getPointerValue(JNA.CLibrary.INSTANCE.tdGetName(deviceId));
		// JNA.CLibrary.INSTANCE.tdReleaseString(name);

		// Get model
		this.model = JNA.getPointerValue(JNA.CLibrary.INSTANCE.tdGetModel(deviceId));
		// JNA.CLibrary.INSTANCE.tdReleaseString(model);

		// Get protocol
		this.protocol = JNA.getPointerValue(JNA.CLibrary.INSTANCE.tdGetProtocol(deviceId));
		// JNA.CLibrary.INSTANCE.tdReleaseString(protocol);

		// Get last status ( EMULATED 2 way communication ) Works with TS DUO
		this.status = JNA.CLibrary.INSTANCE.tdLastSentCommand(deviceId, getSupportedMethods());

		if (this.status == JNA.CLibrary.TELLSTICK_DIM) {
			this.data = JNA.getPointerValue(JNA.CLibrary.INSTANCE.tdLastSentValue(deviceId));
		}
		// Get the device type.
		this.deviceType = JNA.CLibrary.INSTANCE.tdGetDeviceType(deviceId);

	}

	/**
	 * Creates a new device. Can throw exception if failure to create new
	 * device.
	 * 
	 */
	public TellstickDevice(String name, String protocol, String model) throws Exception {
		supportedMethodsCheck();
		int id = JNA.CLibrary.INSTANCE.tdAddDevice();
		if (id < 0) {
			throw new Exception("Unknown error creating a new device.");
		}
		this.deviceId = id;

		if (!setName(name)) {
			remove();
			throw new Exception("Error creating device, could not set name.");
		}

		if (!setProtocol(protocol)) {
			remove();
			throw new Exception("Error creating device, could not set protocol.");
		}

		if (!setModel(model)) {
			remove();
			throw new Exception("Error creating device, could not set protocol.");
		}

	}

	/**
	 * Gets parameter from device.
	 * 
	 * @param attribute
	 * @param defaultVal
	 */
	public String getParameter(String attribute, String defaultVal) {
		return JNA.getPointerValue(JNA.CLibrary.INSTANCE.tdGetDeviceParameter(getId(), attribute, defaultVal));
	}

	/**
	 * Sets parameter to device.
	 * 
	 * @param attribute
	 * @param value
	 */
	public boolean setParameter(String attribute, String value) {
		return JNA.CLibrary.INSTANCE.tdSetDeviceParameter(getId(), attribute, value);
	}

	/**
	 * Returns The right object for this device. Can be: - GroupDevice ( Can
	 * have methods from bell, dim and on/off) - SceneDevice ( Contains execute
	 * ) - BellDevice - DimmableDevice - Device
	 * 
	 * @param TS
	 * @param deviceId
	 *            TellstickDevice , Either BellDevice, DimmableDevice or Device.
	 * @throws SupportedMethodsException
	 *             Exception is thrown if accepted methods is not defined.
	 * @throws DeviceNotSupportedException
	 *             Exception is thrown if application does not support this
	 *             device type.
	 */
	static public TellstickDevice getDevice(int deviceId) throws SupportedMethodsException, DeviceNotSupportedException {
		supportedMethodsCheck();
		int methods = JNA.CLibrary.INSTANCE.tdMethods(deviceId, getSupportedMethods());

		// Is this device a group or scene ? In this case, we must return either
		// GroupDevice or SceneDevice.
		int type = JNA.CLibrary.INSTANCE.tdGetDeviceType(deviceId);
		if (type == JNA.CLibrary.TELLSTICK_TYPE_GROUP) {
			return new GroupDevice(deviceId);
		}

		// Now single action based.
		if (type == JNA.CLibrary.TELLSTICK_TYPE_SCENE) {
			return new SceneDevice(deviceId);
		} else if ((methods & JNA.CLibrary.TELLSTICK_BELL) > 0) {
			return new BellDevice(deviceId);
		} else if ((methods & JNA.CLibrary.TELLSTICK_DIM) > 0) {
			return new DimmableDevice(deviceId);
		} else if ((methods & JNA.CLibrary.TELLSTICK_UP) > 0 && (methods & JNA.CLibrary.TELLSTICK_DOWN) > 0
				&& (methods & JNA.CLibrary.TELLSTICK_STOP) > 0) {
			return new UpDownDevice(deviceId);
		} else if ((methods & JNA.CLibrary.TELLSTICK_TURNON) > 0 && (methods & JNA.CLibrary.TELLSTICK_TURNOFF) > 0) {
			return new Device(deviceId);
		} else if ((methods & JNA.CLibrary.TELLSTICK_EXECUTE) > 0) {
			return new SceneDevice(deviceId);
		}
		throw new DeviceNotSupportedException("The device properties seems not to be supported by this application");
	}

	/**
	 * Gets a list of all the devices. Created using ArrayList.
	 * 
	 * @throws SupportedMethodsException
	 * @throws DeviceNotSupportedException
	 */
	static public ArrayList<TellstickDevice> getDevices() throws SupportedMethodsException {
		int nbrDevices = JNA.CLibrary.INSTANCE.tdGetNumberOfDevices();
		ArrayList<TellstickDevice> devices = new ArrayList<TellstickDevice>();
		for (int i = 0; i < nbrDevices; i++) {
			try {
				TellstickDevice dev = TellstickDevice.getDevice(JNA.CLibrary.INSTANCE.tdGetDeviceId(i));
				devices.add(dev);
			} catch (DeviceNotSupportedException e) {
				// Device not supported. Do not add to list.
			}
		}
		return devices;
	}

	/**
	 * Gets all devices with a filter of instances. Usage:
	 * ArrayList<TellstickDevice> devices2 =
	 * TellstickDevice.getDevices(Device.class, DimmableDevice.class,
	 * ...etc...);
	 * 
	 * @param type
	 * 
	 * @throws SupportedMethodsException
	 */
	static public ArrayList<TellstickDevice> getDevices(Class<? extends TellstickDevice>... types)
			throws SupportedMethodsException {
		return filterDevices(getDevices(), types);
	}

	/**
	 * Filters devices into a new list. Usage: ArrayList<TellstickDevice>
	 * devices2 = TellstickDevice.getDevices(devices, Device.class,
	 * DimmableDevice.class, ...etc...);
	 * 
	 * @param allDevices
	 *            ArrayList of devices for input.
	 * @param type
	 *            The types , eg DimmableDevice.class , Device.class,
	 *            BellDevice.class, GroupDevice.class ... etc ..
	 * 
	 * @throws SupportedMethodsException
	 */
	static public ArrayList<TellstickDevice> filterDevices(ArrayList<TellstickDevice> allDevices,
			Class<? extends TellstickDevice>... types) throws SupportedMethodsException {

		ArrayList<TellstickDevice> newList = new ArrayList<TellstickDevice>();

		for (TellstickDevice dev : allDevices) {
			boolean added = false;
			for (Class<? extends TellstickDevice> t : types) {
				if (!added && t.isInstance(dev)) {
					newList.add(dev);
					added = true;
				}
			}
		}

		return newList;
	}

	/**
	 * Gets a list of TellstickDevice with a supplied list of device Ids in
	 * int..
	 * 
	 * @param deviceIds
	 * 
	 * @throws SupportedMethodsException
	 * @throws DeviceNotSupportedException
	 */
	static public ArrayList<TellstickDevice> getDevices(int... deviceIds) throws SupportedMethodsException {
		ArrayList<TellstickDevice> devices = getDevices();
		ArrayList<TellstickDevice> retDevices = new ArrayList<TellstickDevice>();
		for (TellstickDevice td : devices) {
			for (int devid : deviceIds) {
				if (td.getId() == devid) {
					retDevices.add(td);
				}
			}
		}
		return devices;
	}

	/**
	 * Gets a list of TellstickDevice with a supplied list of device Ids in
	 * String.
	 * 
	 * @param deviceIds
	 * 
	 * @throws SupportedMethodsException
	 * @throws DeviceNotSupportedException
	 */
	static public ArrayList<TellstickDevice> getDevices(String... deviceIds) throws SupportedMethodsException {
		int[] parsedIds = new int[deviceIds.length];

		int a = 0;
		for (String i : deviceIds) {
			int parsedI = Integer.parseInt(i.trim());
			if (parsedI > 0) {
				parsedIds[a] = parsedI;
				a++;
			}
		}
		return getDevices(parsedIds);
	}

	/**
	 * Gets the ID of the device.
	 */
	public int getId() {
		return deviceId;
	}

	/**
	 * Gets the name of the device.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the device type. Can return JNA.CLibrary.TELLSTICK_TYPE_GROUP
	 * JNA.CLibrary.TELLSTICK_TYPE_SCENE JNA.CLibrary.TELLSTICK_TYPE_DEVICE
	 */
	public int getDeviceType() {
		return deviceType;
	}

	/**
	 * Gets the status of the last action """ CURRENT STATE """ that this device
	 * has. Must be in supported methods.
	 * 
	 * Available statuses: See setSupportedMethods( ... ), this depends on what
	 * the application supports!
	 * 
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Gets the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Sets the protocol of the device.
	 * 
	 * @param name
	 */
	public boolean setProtocol(String protocol) {
		this.protocol = protocol;
		return JNA.CLibrary.INSTANCE.tdSetProtocol(getId(), protocol);
	}

	/**
	 * Gets the model name of the device.
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Removes this device totally and deletes it. Note that the object will
	 * still be there in JAVA while in runtime.
	 */
	public boolean remove() {
		return JNA.CLibrary.INSTANCE.tdRemoveDevice(getId());
	}

	/**
	 * Tries to learn the device. Note, must have flag in methods:
	 * JNA.CLibrary.INSTANCE.TELLSTICK_LEARN
	 * 
	 * @throws TellstickException
	 */
	public void learn() throws TellstickException {
		int status = JNA.CLibrary.INSTANCE.tdLearn(getId());
		if (status != TELLSTICK_SUCCESS)
			throw new TellstickException(this, status);
	}

	/**
	 * Returns true if this device is learnable.
	 */
	public boolean isLearnable() {
		int methods = JNA.CLibrary.INSTANCE.tdMethods(deviceId, JNA.CLibrary.TELLSTICK_LEARN);
		return (JNA.CLibrary.TELLSTICK_LEARN & methods) > 0;
	}

	/**
	 * Sets the name of the device.
	 * 
	 * @param name
	 */
	public boolean setName(String name) {
		this.name = name;
		return JNA.CLibrary.INSTANCE.tdSetName(getId(), name);
	}

	/**
	 * Sets the name of the model of the device. WARNING; Careful using this!
	 * 
	 * @param model
	 */
	public boolean setModel(String model) {
		this.model = model;
		return JNA.CLibrary.INSTANCE.tdSetModel(getId(), model);
	}

	/**
	 * Gets the type of the device. Override this when creating new devices.
	 */
	public String getType() {
		return "Unknown";
	}

	/**
	 * These are supported by the API. Returns all the action methods that are
	 * implemented in tellstick. Check like: int methods =
	 * JNA.CLibrary.INSTANCE.tdMethods(deviceId, allMethods()); if ((methods &
	 * JNA.CLibrary.TELLSTICK_BELL) > 0) // IT HAS BELL ACTION
	 */
	public static int getSupportedMethods() {
		return supportedMethods;
	}

	/**
	 * Compares to tellstick devices. They are equal if they have the same id.
	 */
	@Override
	public int compareTo(TellstickDevice dev) {
		return this.getId() - dev.getId();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TellstickDevice [deviceId=" + deviceId + ", name=" + name + ", status=" + status + ", deviceType="
				+ deviceType + ", data=" + data + "]";
	}

}
