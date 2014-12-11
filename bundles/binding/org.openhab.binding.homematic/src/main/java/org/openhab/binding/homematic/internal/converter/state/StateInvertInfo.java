/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.apache.commons.lang.math.NumberUtils;
import org.openhab.binding.homematic.internal.model.HmDatapoint;

/**
 * Holds device specific infos for state invertion.
 * 
 * @author Gerhard Riegler
 * @since 1.5.1
 */
public class StateInvertInfo {
	private String deviceType;
	private int minChannel;
	private int maxChannel;

	/**
	 * Creates a StateInvertInfo with the specified deviceType.
	 */
	public StateInvertInfo(String deviceType) {
		this(deviceType, -1, -1);
	}

	/**
	 * Creates a StateInvertInfo with the specified deviceType and a channel
	 * range.
	 */
	public StateInvertInfo(String deviceType, int minChannel, int maxChannel) {
		this.deviceType = deviceType;
		this.minChannel = minChannel;
		this.maxChannel = maxChannel;
	}

	/**
	 * Validates if the state of a datapoint must be inverted.
	 */
	public boolean isToInvert(HmDatapoint dp) {
		String dpDeviceType = dp.getChannel().getDevice().getType().toUpperCase();
		if (minChannel != -1) {
			int dpChannel = NumberUtils.toInt(dp.getChannel().getNumber());
			return dpDeviceType.startsWith(deviceType) && dpChannel >= minChannel && dpChannel <= maxChannel;
		} else {
			return dpDeviceType.startsWith(deviceType);
		}
	}

}
