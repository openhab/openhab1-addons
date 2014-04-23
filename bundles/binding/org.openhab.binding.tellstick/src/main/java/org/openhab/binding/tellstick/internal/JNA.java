/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal;

import org.openhab.binding.tellstick.internal.device.SupportedMethodsException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * This Class is the heart of TellstickDUO Java version.
 * 
 * Every important bit is happening in this class.
 * 
 * This Class uses JNA to map TelldusCore.dll/so to the class so we can use
 * methods in the Library file.
 * 
 * Methods is generated using the PHP script (partly) in the
 * dev/tools/telldus-core-parser.php
 * 
 * 
 * The idea is to create wrappers around this class, in a OOP way.
 * 
 * @see pkj.no.tellstick.device
 * 
 * 
 * @author peec
 * 
 */
public class JNA {
	public enum Method {
		TURNON(CLibrary.TELLSTICK_TURNON), TURNOFF(CLibrary.TELLSTICK_TURNOFF), DIM(CLibrary.TELLSTICK_DIM), LEARN(
				CLibrary.TELLSTICK_LEARN), DEVICE_ADDED(CLibrary.TELLSTICK_DEVICE_ADDED), DEVICE_CHANGED(
				CLibrary.TELLSTICK_DEVICE_CHANGED), DEVICE_REMOVED(CLibrary.TELLSTICK_DEVICE_REMOVED);

		private int nativeInt;

		Method(int nativeMethod) {
			this.nativeInt = nativeMethod;
		}

		public static Method getMethodById(int nativeId) {
			for (Method m : Method.values()) {
				if (m.nativeInt == nativeId) {
					return m;
				}
			}
			throw new RuntimeException("Method not found for " + nativeId);
		}

	}

	public enum DataType {
		HUMIDITY(CLibrary.TELLSTICK_HUMIDITY), TEMPERATURE(CLibrary.TELLSTICK_TEMPERATURE);

		private int nativeInt;

		DataType(int nativeMethod) {
			this.nativeInt = nativeMethod;
		}

		public static DataType getDataTypeId(int nativeId) {
			for (DataType m : DataType.values()) {
				if (m.nativeInt == nativeId) {
					return m;
				}
			}
			throw new RuntimeException("DataType not found for " + nativeId);
		}

	}

	public static String library = null;

	/**
	 * Interface that extends the library.
	 * 
	 * @author peec
	 * 
	 * 
	 */
	public interface CLibrary extends Library {

		CLibrary INSTANCE = (CLibrary) Native.loadLibrary((library), CLibrary.class);

		/**
		 * 
		 * 
		 * Lets start with the callbacks. Must use StdCallCallback . Not
		 * Callback, it will crash.
		 * 
		 * @todo ABSOLUTELY NEEDS TESTING
		 */

		// typedef void (WINAPI *TDDeviceEvent)(int deviceId, int method, const
		// char *data, int callbackId, void *context);
		interface TDDeviceEvent extends StdCallCallback {
			void invoke(int deviceId, int method, Pointer data, int callbackId, Pointer context)
					throws SupportedMethodsException;
		}

		// TELLSTICK_API int WINAPI tdRegisterDeviceEvent( TDDeviceEvent
		// eventFunction, void *context );
		public int tdRegisterDeviceEvent(TDDeviceEvent eventFunction, Pointer context);

		interface TDSensorEvent extends StdCallCallback {
			void invoke(String protocol, String model, int deviceId, int dataType, Pointer value, int timeStamp,
					int callbackId, Pointer context) throws SupportedMethodsException;
		}

		public int tdRegisterSensorEvent(TDSensorEvent eventFunction, Pointer context);

		// typedef void (WINAPI *TDDeviceChangeEvent)(int deviceId, int
		// changeEvent, int changeType, int callbackId, void *context);
		interface TDDeviceChangeEvent extends StdCallCallback {
			void invoke(int deviceId, int changeEvent, int changeType, int callbackId, Pointer context)
					throws SupportedMethodsException;
		}

		// TELLSTICK_API int WINAPI tdRegisterDeviceChangeEvent(
		// TDDeviceChangeEvent eventFunction, void *context);
		public int tdRegisterDeviceChangeEvent(TDDeviceChangeEvent eventFunction, Pointer context);

		// typedef void (WINAPI *TDRawDeviceEvent)(const char *data, int
		// controllerId, int callbackId, void *context);
		interface TDRawDeviceEvent extends StdCallCallback {
			void invoke(String data, int controllerId, int callbackId, Pointer context)
					throws SupportedMethodsException;
		}

		// TELLSTICK_API int WINAPI tdRegisterRawDeviceEvent( TDRawDeviceEvent
		// eventFunction, void *context );
		public int tdRegisterRawDeviceEvent(TDRawDeviceEvent eventFunction, Pointer context);

		/**
		 * 
		 * Now all the other methods in the tellduscore lib.
		 * 
		 * Notice: Most of these are implemented in the Tellstick Device and
		 * other types of Device objects.
		 */

		// TELLSTICK_API void WINAPI tdInit(void);
		public void tdInit();

		// TELLSTICK_API int WINAPI tdUnregisterCallback( int callbackId );
		public int tdUnregisterCallback(int callbackId);

		// TELLSTICK_API void WINAPI tdClose(void);
		public void tdClose();

		// TELLSTICK_API void WINAPI tdReleaseString(char *string);
		public void tdReleaseString(Pointer string);

		// TELLSTICK_API int WINAPI tdTurnOn(int intDeviceId);
		public int tdTurnOn(int intDeviceId);

		// TELLSTICK_API int WINAPI tdTurnOff(int intDeviceId);
		public int tdTurnOff(int intDeviceId);

		// TELLSTICK_API int WINAPI tdBell(int intDeviceId);
		public int tdBell(int intDeviceId);

		// TELLSTICK_API int WINAPI tdDim(int intDeviceId, unsigned char level);
		public int tdDim(int intDeviceId, int level);

		// TELLSTICK_API int WINAPI tdExecute(int intDeviceId);
		public int tdExecute(int inDeviceId);

		// TELLSTICK_API int WINAPI tdUp(int intDeviceId);
		public int tdUp(int intDeviceId);

		// TELLSTICK_API int WINAPI tdDown(int intDeviceId);
		public int tdDown(int intDeviceId);

		// TELLSTICK_API int WINAPI tdStop(int intDeviceId);
		public int tdStop(int intDeviceId);

		// TELLSTICK_API int WINAPI tdLearn(int intDeviceId);
		public int tdLearn(int intDeviceId);

		// TELLSTICK_API int WINAPI tdMethods(int id, int methodsSupported);
		public int tdMethods(int id, int methodsSupported);

		// TELLSTICK_API int WINAPI tdLastSentCommand( int intDeviceId, int
		// methodsSupported );
		public int tdLastSentCommand(int intDeviceId, int methodsSupported);

		public Pointer tdLastSentValue(int intDeviceId);

		// TELLSTICK_API int WINAPI tdGetNumberOfDevices();
		public int tdGetNumberOfDevices();

		// TELLSTICK_API int WINAPI tdGetDeviceId(int intDeviceIndex);
		public int tdGetDeviceId(int intDeviceIndex);

		// TELLSTICK_API int WINAPI tdGetDeviceType(int intDeviceId);
		public int tdGetDeviceType(int intDeviceId);

		// TELLSTICK_API char * WINAPI tdGetErrorString(int intErrorNo);
		public Pointer tdGetErrorString(int intErrorNo);

		// TELLSTICK_API char * WINAPI tdGetName(int intDeviceId);
		public Pointer tdGetName(int intDeviceId);

		// TELLSTICK_API bool WINAPI tdSetName(int intDeviceId, const char*
		// chNewName);
		public boolean tdSetName(int intDeviceId, String chNewName);

		// TELLSTICK_API char * WINAPI tdGetProtocol(int intDeviceId);
		public Pointer tdGetProtocol(int intDeviceId);

		// TELLSTICK_API bool WINAPI tdSetProtocol(int intDeviceId, const char*
		// strProtocol);
		public boolean tdSetProtocol(int intDeviceId, String strProtocol);

		// TELLSTICK_API char * WINAPI tdGetModel(int intDeviceId);
		public Pointer tdGetModel(int intDeviceId);

		// TELLSTICK_API bool WINAPI tdSetModel(int intDeviceId, const char
		// *intModel);
		public boolean tdSetModel(int intDeviceId, String intModel);

		// TELLSTICK_API char * WINAPI tdGetDeviceParameter(int intDeviceId,
		// const char *strName, const char *defaultValue);
		public Pointer tdGetDeviceParameter(int intDeviceId, String strName, String defaultValue);

		// TELLSTICK_API bool WINAPI tdSetDeviceParameter(int intDeviceId, const
		// char *strName, const char* strValue);
		public boolean tdSetDeviceParameter(int intDeviceId, String strName, String strValue);

		// TELLSTICK_API int WINAPI tdAddDevice();
		public int tdAddDevice();

		// TELLSTICK_API bool WINAPI tdRemoveDevice(int intDeviceId);
		public boolean tdRemoveDevice(int intDeviceId);

		// TELLSTICK_API int WINAPI tdSendRawCommand(const char *command, int
		// reserved);
		public int tdSendRawCommand(String command, int reserved);

		// TELLSTICK_API void WINAPI tdConnectTellStickController(int vid, int
		// pid, const char *serial);
		public void tdConnectTellStickController(int vid, int pid, String serial);

		// TELLSTICK_API void WINAPI tdDisconnectTellStickController(int vid,
		// int pid, const char *serial);
		public void tdDisconnectTellStickController(int vid, int pid, String serial);

		// define TELLSTICK_TURNON 1
		public final int TELLSTICK_TURNON = 1;
		// define TELLSTICK_TURNOFF 2
		public final int TELLSTICK_TURNOFF = 2;
		// define TELLSTICK_BELL 4
		public final int TELLSTICK_BELL = 4;
		// define TELLSTICK_TOGGLE 8
		public final int TELLSTICK_TOGGLE = 8;
		// define TELLSTICK_DIM 16
		public final int TELLSTICK_DIM = 16;
		// define TELLSTICK_LEARN 32
		public final int TELLSTICK_LEARN = 32;
		// define TELLSTICK_EXECUTE 64
		public final int TELLSTICK_EXECUTE = 64;
		// define TELLSTICK_UP 128
		public final int TELLSTICK_UP = 128;
		// define TELLSTICK_DOWN 256
		public final int TELLSTICK_DOWN = 256;
		// define TELLSTICK_STOP 512
		public final int TELLSTICK_STOP = 512;

		// define TELLSTICK_TYPE_DEVICE 1
		public final int TELLSTICK_TYPE_DEVICE = 1;
		// define TELLSTICK_TYPE_GROUP 2
		public final int TELLSTICK_TYPE_GROUP = 2;
		// define TELLSTICK_TYPE_SCENE 3
		public final int TELLSTICK_TYPE_SCENE = 3;

		// define TELLSTICK_DEVICE_ADDED 1
		public final int TELLSTICK_DEVICE_ADDED = 1;
		// define TELLSTICK_DEVICE_CHANGED 2
		public final int TELLSTICK_DEVICE_CHANGED = 2;
		// define TELLSTICK_DEVICE_REMOVED 3
		public final int TELLSTICK_DEVICE_REMOVED = 3;
		// define TELLSTICK_DEVICE_STATE_CHANGED 4
		public final int TELLSTICK_DEVICE_STATE_CHANGED = 4;

		// define TELLSTICK_CHANGE_NAME 1
		public final int TELLSTICK_CHANGE_NAME = 1;
		// define TELLSTICK_CHANGE_PROTOCOL 2
		public final int TELLSTICK_CHANGE_PROTOCOL = 2;
		// define TELLSTICK_CHANGE_MODEL 3
		public final int TELLSTICK_CHANGE_MODEL = 3;
		// define TELLSTICK_CHANGE_METHOD 4
		public final int TELLSTICK_CHANGE_METHOD = 4;

		public final int TELLSTICK_TEMPERATURE = 1;
		public final int TELLSTICK_HUMIDITY = 2;
		public final int TELLSTICK_RAINRATE = 4;
		public final int TELLSTICK_RAINTOTAL = 8;
		public final int TELLSTICK_WINDDIRECTION = 16;
		public final int TELLSTICK_WINDAVERAGE = 32;
		public final int TELLSTICK_WINDGUST = 64;
	}

	static {
		if (Platform.isWindows()) {
			JNA.library = "TelldusCore";
		} else {
			JNA.library = "telldus-core";
		}

	}

	public static String getPointerValue(Pointer point) {
		String val = point.getString(0);
		JNA.CLibrary.INSTANCE.tdReleaseString(point);
		return val;
	}
}
