package de.akuz.cul;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.akuz.cul.internal.CULHandlerInternal;
import de.akuz.cul.internal.CULSerialHandlerImpl;

/**
 * This class handles all CULHandler. You can only obtain CULHandlers via this
 * manager.
 * 
 * @author Till Klocke
 * 
 */
public class CULManager {

	private final static Logger logger = LoggerFactory
			.getLogger(CULManager.class);

	private static Map<String, CULHandler> openDevices = new HashMap<String, CULHandler>();

	/**
	 * Get CULHandler for the given device in the given mode. The same
	 * CULHandler can be returned multiple times if you ask multiple times for
	 * the same device in the same mode. It is not possible to obtain a
	 * CULHandler of an already openend device for another RF mode.
	 * 
	 * @param deviceName
	 *            String representing the device. Currently only serial ports
	 *            are supported.
	 * @param mode
	 *            The RF mode for which the device will be configured.
	 * @return A CULHandler to communicate with the culfw based device.
	 * @throws CULDeviceException
	 */
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

	/**
	 * Return a CULHandler to the manager. The CULHandler will only be closed if
	 * there aren't any listeners registered with it. So it is save to call this
	 * methods as soon as you don't need the CULHandler any more.
	 * 
	 * @param handler
	 */
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
