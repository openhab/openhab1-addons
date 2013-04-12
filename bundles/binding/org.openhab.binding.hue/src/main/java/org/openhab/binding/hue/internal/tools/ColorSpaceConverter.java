/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.hue.internal.tools;

import java.awt.Color;

/**
 * This helper converts colors from RGB format to HSB format and back.
 * 
 * @author Roman Hartmann
 * @since 1.2.0
 */
public class ColorSpaceConverter {

	/**
	 * Converts a color in the HSB format into the same color in the RGB format.
	 * 
	 * @param hue
	 *            Hue as angle/360 [0.0 - 1.0]
	 * @param saturation
	 *            Saturation [0.0 - 1.0]
	 * @param brightness
	 *            Brightness [0.0 - 1.0]
	 * @return The RGB values for the color.
	 */
	public int[] hsbToRgb(double hue, double saturation, double brightness) {
		int rgb = Color.HSBtoRGB((float) hue, (float) saturation,
				(float) brightness);
		int[] result = new int[3];
		result[0] = (rgb >> 16) & 0xff;
		result[1] = (rgb >> 8) & 0xff;
		result[2] = (rgb >> 0) & 0xff;
		return result;
	}

	/**
	 * Converts a color in the RGB format into the same color in the HSB format.
	 * 
	 * @param red
	 *            Red [0 - 255]
	 * @param green
	 *            Green [0 - 255]
	 * @param blue
	 *            Blue [0 - 255]
	 * @return The HSB values for the color. Hue as angle/360 [0.0 - 1.0],
	 *         Saturation [0.0 - 1.0], Brightness [0.0 - 1.0]
	 */
	public double[] rgbToHsb(int red, int green, int blue) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(red, green, blue, hsb);
		double[] result = new double[3];
		result[0] = hsb[0];
		result[1] = hsb[1];
		result[2] = hsb[2];
		return result;
	}

	/**
	 * Converts a color in the RGB format into the same color in the HSB format.
	 * 
	 * @param rgbValues
	 *            Red [0 - 255], Green [0 - 255], Blue [0 - 255]
	 * @return The HSB values for the color. Hue as angle/360 [0.0 - 1.0],
	 *         Saturation [0.0 - 1.0], Brightness [0.0 - 1.0]
	 */
	public double[] rgbToHsb(int[] rgbValues) {
		return rgbToHsb(rgbValues[0], rgbValues[1], rgbValues[2]);
	}

}