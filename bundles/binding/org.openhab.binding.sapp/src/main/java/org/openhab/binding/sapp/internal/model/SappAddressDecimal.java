/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;

import org.openhab.binding.sapp.internal.configs.SappBindingConfigUtils;

/**
 * Decimal Address model
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappAddressDecimal extends SappAddress {

	private int originalMinScale;
	private int originalMaxScale;
	private int minScale;
	private int maxScale;

	/**
	 * Constructor
	 */
	public SappAddressDecimal(String pnmasId, SappAddressType addressType, int address, String subAddress, int minScale, int maxScale) {
		super(pnmasId, addressType, address, subAddress);

		setOriginalScale(subAddress);
		if (minScale != maxScale) { // check against bad parameters, division by zero
			this.minScale = minScale;
			this.maxScale = maxScale;
		} else {
			this.minScale = originalMinScale;
			this.maxScale = originalMaxScale;
		}
	}

	/**
	 * Constructor
	 */
	public SappAddressDecimal(String pnmasId, SappAddressType addressType, int address, String subAddress) {
		super(pnmasId, addressType, address, subAddress);

		setOriginalScale(subAddress);
		this.minScale = originalMinScale;
		this.maxScale = originalMaxScale;
	}

	/**
	 * minScale getter
	 */
	public int getMinScale() {
		return minScale;
	}

	/**
	 * maxScale getter
	 */
	public int getMaxScale() {
		return maxScale;
	}

	/**
	 * originalMinScale getter
	 */
	public int getOriginalMinScale() {
		return originalMinScale;
	}

	/**
	 * originalMaxScale getter
	 */
	public int getOriginalMaxScale() {
		return originalMaxScale;
	}

	private void setOriginalScale(String subAddress) {

		if (subAddress.equals(SappBindingConfigUtils.WORD_MASK_U) || subAddress.equals(SappBindingConfigUtils.WORD_MASK_S)) {
			originalMinScale = 0;
			originalMaxScale = 0xFFFF;
		} else if (subAddress.equals(SappBindingConfigUtils.LOW_MASK_U) || subAddress.equals(SappBindingConfigUtils.LOW_MASK_S)) {
			originalMinScale = 0;
			originalMaxScale = 0x00FF;
		} else if (subAddress.equals(SappBindingConfigUtils.HIGH_MASK_U) || subAddress.equals(SappBindingConfigUtils.HIGH_MASK_S)) {
			originalMinScale = 0;
			originalMaxScale = 0x00FF;
		} else {
			originalMinScale = 0;
			originalMaxScale = 0x0001;
		}
	}

	/**
	 * returns the scaled value with respect to the original scale
	 */
	public double scaledValue(int value, String subAddress) {
		double toScaleValue;

		if (subAddress.equals(SappBindingConfigUtils.WORD_MASK_S) && value > 0x7FFF) {
			toScaleValue = value - 0xFFFF - 1;
		} else if (subAddress.equals(SappBindingConfigUtils.LOW_MASK_S) && value > 0x7F) {
			toScaleValue = value - 0xFF - 1;
		} else if (subAddress.equals(SappBindingConfigUtils.HIGH_MASK_S) && value > 0x7F) {
			toScaleValue = value - 0xFF - 1;
		} else {
			toScaleValue = value;
		}

		return (((double) (toScaleValue - originalMinScale)) * ((double) (maxScale - minScale)) / ((double) (originalMaxScale - originalMinScale))) + ((double) minScale);
	}

	/**
	 * converts a scaled value back to the original scale
	 */
	public int backScaledValue(double value) {
		return (int) Math.round((((double) (value - minScale)) * ((double) (originalMaxScale - originalMinScale)) / ((double) (maxScale - minScale))) + ((double) originalMinScale));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), minScale, maxScale);
	}
}
