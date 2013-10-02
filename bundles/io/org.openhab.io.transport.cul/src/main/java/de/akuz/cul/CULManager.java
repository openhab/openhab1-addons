package de.akuz.cul;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.internal.CULHandlerInternal;
import de.akuz.cul.internal.CULSerialHandlerImpl;

public class CULManager {

	private final static Logger logger = LoggerFactory
			.getLogger(CULManager.class);

	private static Map<String, CULHandler> openDevices = new HashMap<String, CULHandler>();

	public static CULHandler getOpenCULHandler(String deviceName, CULMode mode)
			throws CULDeviceException {
		logger.debug("Trying to open device " + deviceName + " in mode "
				+ mode.toString());
		synchronized (openDevices) {

			if (openDevices.containsKey(deviceName)) {
				CULHandler handler = openDevices.get(deviceName);
				if (handler.getCULMode() == mode) {
					return handler;
				} else {
					throw new CULDeviceException("The device " + deviceName
							+ " is already open in mode " + mode.toString());
				}
			}
			CULHandler handler = createNewHandler(deviceName, mode);
			openDevices.put(deviceName, handler);
			return handler;
		}
	}

	public static void close(CULHandler handler) {
		synchronized (openDevices) {

			if (handler instanceof CULHandlerInternal) {
				CULHandlerInternal internalHandler = (CULHandlerInternal) handler;
				if (!internalHandler.hasListeners()) {
					openDevices.remove(handler);
					internalHandler.close();
				} else {
					logger.warn("Can't close device because it still has listeners");
				}
			}
		}
	}

	private static CULHandler createNewHandler(String deviceName, CULMode mode)
			throws CULDeviceException {
		CULSerialHandlerImpl handler = new CULSerialHandlerImpl(deviceName,
				mode);
		handler.open();
		if (mode == CULMode.ASK_SIN) {
			try {
				handler.sendRaw("Ar\r\n");
			} catch (CULCommunicationException e) {
				throw new CULDeviceException(
						"Can't set AskSin receiption mode", e);
			}
		}

		return handler;
	}

}
