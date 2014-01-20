/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.library.types;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

import org.openhab.core.types.Command;
import org.openhab.core.types.ComplexType;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 * The HSBType is a complex type with constituents for hue, saturation and
 * brightness and can be used for color items.
 * 
 * @author Kai Kreuzer
 * @since 1.2.0
 * 
 */
public class HSBType extends PercentType implements ComplexType, State, Command {

	private static final long serialVersionUID = 322902950356613226L;

	// constants for the constituents
	static final public String KEY_HUE = "h";
	static final public String KEY_SATURATION = "s";
	static final public String KEY_BRIGHTNESS = "b";

	// constants for colors
	static final public HSBType BLACK = new HSBType(Color.BLACK);
	static final public HSBType WHITE = new HSBType(Color.WHITE);
	static final public HSBType RED = new HSBType(Color.RED);
	static final public HSBType GREEN = new HSBType(Color.GREEN);
	static final public HSBType BLUE = new HSBType(Color.BLUE);

	protected BigDecimal hue;
	protected BigDecimal saturation;

	// the inherited field "value" of the parent DecimalType corresponds to the
	// "brightness"

	public HSBType(Color color) {
		if (color != null) {
			float[] hsbValues = Color.RGBtoHSB(color.getRed(),
					color.getGreen(), color.getBlue(), null);
			this.hue = BigDecimal.valueOf(hsbValues[0] * 360);
			this.saturation = BigDecimal.valueOf(hsbValues[1] * 100);
			this.value = BigDecimal.valueOf(hsbValues[2] * 100);
		} else {
			throw new IllegalArgumentException(
					"Constructor argument must not be null");
		}
	}

	public HSBType(DecimalType h, PercentType s, PercentType b) {
		this.hue = h.toBigDecimal();
		this.saturation = s.toBigDecimal();
		this.value = b.toBigDecimal();
	}

	public HSBType(String value) {
		if (value != null) {
			String[] constituents = value.split(",");
			if (constituents.length == 3) {
				this.hue = new BigDecimal(constituents[0]);
				this.saturation = new BigDecimal(constituents[1]);
				this.value = new BigDecimal(constituents[2]);
			} else {
				throw new IllegalArgumentException(value
						+ " is not a valid HSBType syntax");
			}
		} else {
			throw new IllegalArgumentException(
					"Constructor argument must not be null");
		}
	}

	public static HSBType valueOf(String value) {
		return new HSBType(value);
	}

	@Override
	public SortedMap<String, PrimitiveType> getConstituents() {
		TreeMap<String, PrimitiveType> map = new TreeMap<String, PrimitiveType>();
		map.put(KEY_HUE, getHue());
		map.put(KEY_SATURATION, getSaturation());
		map.put(KEY_BRIGHTNESS, getBrightness());
		return map;
	}

	public DecimalType getHue() {
		return new DecimalType(hue);
	}

	public PercentType getSaturation() {
		return new PercentType(saturation);
	}

	public PercentType getBrightness() {
		return new PercentType(value);
	}

	public PercentType getRed() {
		return byteToPercentType(toColor().getRed());
	}

	public PercentType getGreen() {
		return byteToPercentType(toColor().getGreen());
	}

	public PercentType getBlue() {
		return byteToPercentType(toColor().getBlue());
	}

	private PercentType byteToPercentType(int byteValue) {
		BigDecimal percentValue = new BigDecimal(byteValue).multiply(
				BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(255), 2,
				BigDecimal.ROUND_HALF_UP);
		return new PercentType(percentValue);
	}

	public Color toColor() {
		return Color.getHSBColor(hue.floatValue() / 360,
				saturation.floatValue() / 100, value.floatValue() / 100);
	}

	public String toString() {
		return getHue() + "," + getSaturation() + "," + getBrightness();
	}

	@Override
	public int hashCode() {
		int tmp = 10000 * (getHue() == null ? 0 : getHue().hashCode());
		tmp += 100 * (getSaturation() == null ? 0 : getSaturation().hashCode());
		tmp += (getBrightness() == null ? 0 : getBrightness().hashCode());
		return tmp;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HSBType))
			return false;
		HSBType other = (HSBType) obj;
		if ((getHue() != null && other.getHue() == null)
				|| (getHue() == null && other.getHue() != null)
				|| (getSaturation() != null && other.getSaturation() == null)
				|| (getSaturation() == null && other.getSaturation() != null)
				|| (getBrightness() != null && other.getBrightness() == null)
				|| (getBrightness() == null && other.getBrightness() != null)) {
			return false;
		}
		if (!getHue().equals(other.getHue())
				|| !getSaturation().equals(other.getSaturation())
				|| !getBrightness().equals(other.getBrightness())) {
			return false;
		}
		return true;
	}
}
