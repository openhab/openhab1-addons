/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.legrange.panstamp.DeviceStateStore;
import me.legrange.panstamp.Register;

/**
 * This panStamp device store implementation is a simple product code lookup.
 * 
 * @author Gideon le Grange
 * @since 1.8.0
 */
class ConfigDeviceStore implements DeviceStateStore {

	private static final Logger logger = LoggerFactory.getLogger(ConfigDeviceStore.class);
	private final Map<Integer, byte[]> map = new HashMap<Integer, byte[]>();

	ConfigDeviceStore() {
	}

	/**
	 * Add the product code for a device.
	 * 
	 * @param address
	 *            Device address.
	 * @param manId
	 *            Manufacturer ID for device.
	 * @param prodId
	 *            Product ID for device.
	 */
	void addProductCode(int address, int manId, int prodId) {
		byte val[] = new byte[8];
		val[0] = (byte) (manId >> 24);
		val[1] = (byte) (manId >> 16);
		val[2] = (byte) (manId >> 8);
		val[3] = (byte) (manId);
		val[4] = (byte) (prodId >> 24);
		val[5] = (byte) (prodId >> 16);
		val[6] = (byte) (prodId >> 8);
		val[7] = (byte) (prodId);
		if (map.containsKey(address)) {
			byte oldVal[] = map.get(address);
			if (Arrays.equals(oldVal, val)) {
				logger.warn("Product code for device {} re-assigned from {}/{} to {}/{}.", address,
						bytesToInt(oldVal, 0, 4), bytesToInt(oldVal, 4, 4), manId, prodId);
			}
		}
		map.put(address, val);
	}

	@Override
	public byte[] getRegisterValue(Register reg) {
		return map.get(reg.getDevice().getAddress());
	}

	@Override
	public boolean hasRegisterValue(Register reg) {
		return (reg.getId() == 0) && map.get(reg.getDevice().getAddress()) != null;
	}

	@Override
	public void setRegisterValue(Register reg, byte[] val) {
		if (reg.getId() == 0) {
			map.put(reg.getDevice().getAddress(), val);
		}
	}

	private int bytesToInt(byte bytes[], int idx, int len) {
		int val = 0;
		for (int i = 0; i < len; ++len) {
			val = val << 8;
			val = val | bytes[idx + i];
		}
		return val;
	}

	
}
