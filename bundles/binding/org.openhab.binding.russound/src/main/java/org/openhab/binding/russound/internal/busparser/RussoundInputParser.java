package org.openhab.binding.russound.internal.busparser;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openhab.binding.russound.connection.InputParser;
import org.openhab.binding.russound.internal.AudioZone;
import org.openhab.binding.russound.internal.ZoneAddress;
import org.openhab.binding.russound.internal.ZoneListener;
import org.openhab.binding.russound.internal.ZoneStack;
import org.openhab.binding.russound.utils.StringHexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RussoundInputParser implements InputParser {

	private static final Logger logger = LoggerFactory
			.getLogger(RussoundInputParser.class);
	private List<BusParser> mBusParsers = new ArrayList<BusParser>();
	private List<ZoneListener> mZoneListeners = new ArrayList<ZoneListener>();
	private Map<ZoneAddress, AudioZone> mZones = new HashMap<ZoneAddress, AudioZone>();
	// let's keep a mru stack so when connection supervisor checks, it can query
	// a recently accessed zone
	private ZoneStack mZoneAddressStack = null;

	private byte[] partialBytes;

	public RussoundInputParser(ZoneStack zoneAddressStack) {
		mZoneAddressStack = zoneAddressStack;
		mBusParsers.add(new VolumeChangeBusParser());
		mBusParsers.add(new ZoneInfoBusParser());
		mBusParsers.add(new RussoundPowerBusParser());
		mBusParsers.add(new RussoundSourceBusParser());
	}

	/**
	 * Add event listener, which will be invoked when status upadte is received
	 * from receiver.
	 **/
	public synchronized void addPropertyChangeListener(ZoneListener listener) {
		mZoneListeners.add(listener);
	}

	/**
	 * Remove event listener.
	 **/
	public synchronized void removeEventListener(ZoneListener listener) {
		mZoneListeners.remove(listener);
	}

	private byte[] concat(byte[] a, byte[] b) {
		int aLen = a.length;
		int bLen = b.length;
		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public void parse(byte[] bytes) {
		logger.trace("parsing: " + StringHexUtils.byteArrayToHex(bytes));
		// might be losing linefeeds, lets watch for f7

		byte[] trimmedData;
		// should add partial bytes from previous if we have them
		if (partialBytes != null && partialBytes.length > 0)
			trimmedData = concat(partialBytes, bytes);
		else
			trimmedData = bytes;

		List<byte[]> byteArrays = new ArrayList<byte[]>();
		int lastStart = 0;
		for (int i = 0; i < trimmedData.length; i++) {
			if (trimmedData[i] == (byte) 0xf7) {
				// logger.debug("found terminator at index: " + i);

				byte[] aNewArray = Arrays.copyOfRange(trimmedData, lastStart,
						i + 1);
				byteArrays.add(aNewArray);
				lastStart = i + 1;
			}
		}
		logger.trace("last start: " + lastStart);
		partialBytes = Arrays.copyOfRange(trimmedData, lastStart,
				trimmedData.length);
		logger.trace("partial bytes: "
				+ StringHexUtils.byteArrayToHex(partialBytes));
		for (byte[] someBytes : byteArrays) {
			int bytesLen = someBytes.length;
			logger.trace("elements in byte array: " + bytesLen);
			if (someBytes[bytesLen - 1] == (byte) 0xf7) { // end-flag
				logger.debug("Russound message: "
						+ StringHexUtils.byteArrayToHex(someBytes));
				Iterator<BusParser> iter = mBusParsers.iterator();
				boolean wasMatch = false;
				while (iter.hasNext()) {
					BusParser converter = iter.next();
					ZoneAddress addy = converter.matches(someBytes);
					if (addy != null) {
						wasMatch = true;
						AudioZone zone = mZones.get(addy);
						if (zone == null) {
							logger.info("Bus Parser matched, but Zone not stored in map, creating entry: "
									+ StringHexUtils.byteArrayToHex(someBytes));
							zone = new AudioZone(addy);
							zone.addListener(new ZoneListener() {
								public void onPropertyChange(
										PropertyChangeEvent event,
										ZoneAddress address) {
									for (ZoneListener listener : mZoneListeners) {
										listener.onPropertyChange(event,
												address);
									}

								}
							});
							mZones.put(addy, zone);

						}
						converter.parse(someBytes, zone);
						if (!(converter instanceof ZoneInfoBusParser))
							mZoneAddressStack.push(addy);
					}
				}
				if (!wasMatch)
					logger.trace("Unhandled message on bus: "
							+ StringHexUtils.byteArrayToHex(someBytes));
			}
		}
	}

}
