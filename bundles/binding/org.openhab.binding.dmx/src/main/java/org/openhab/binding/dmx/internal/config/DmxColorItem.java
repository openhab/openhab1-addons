/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.config;

import java.awt.Color;

import org.openhab.binding.dmx.DmxBindingProvider;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.internal.core.DmxUtil;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * DMX item configuration for openHab Color Items.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxColorItem extends DmxDimmerItem {

	private static int BRIGHTNESS_STEP_SIZE = 5;

	private HSBType hsbState;

	/**
	 * Create new color item using a given configuration string.
	 * 
	 * @param itemName
	 *            name of the item
	 * @param configString
	 *            configuration string
	 * @param dmxBindingProvider
	 *            binding provider which created the item
	 * @throws BindingConfigParseException
	 *             if configuration string could not be parsed.
	 */
	public DmxColorItem(String itemName, String configString,
			DmxBindingProvider dmxBindingProvider)
			throws BindingConfigParseException {
		super(itemName, configString, dmxBindingProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processCommand(DmxService service, Command command) {

		// process HSB command
		if (command instanceof HSBType) {
			HSBType hsbValue = (HSBType) command;
			setHSBValue(service, hsbValue);
			return;
		}

		// process increase/decrease
		if (command instanceof IncreaseDecreaseType
				&& !isRedefinedByCustomCommand(command)
				&& !service.hasChannelActions(channels[0])) {

			// rather than doing a linear fade on all channels, we fade only the
			// V part of HSV to maintain the color during the fade

			HSBType hsb = hsbState;
			int brightness = 0;
			IncreaseDecreaseType t = (IncreaseDecreaseType) command;
			if (IncreaseDecreaseType.INCREASE.equals(t)) {

				if (hsb == null) {
					hsb = new HSBType(Color.WHITE);
				}
				for (int ch : channels) {
					service.enableChannel(ch);
				}

				brightness = hsb.getBrightness().intValue();
				brightness += BRIGHTNESS_STEP_SIZE;
				if (brightness > 100) {
					brightness = 100;
				}

			} else {

				if (hsb == null) {
					hsb = new HSBType(Color.BLACK);
				}
				brightness = hsb.getBrightness().intValue();
				brightness -= BRIGHTNESS_STEP_SIZE;
				if (brightness <= 0) {
					brightness = 0;
				}
			}

			HSBType newHsb = new HSBType(hsb.getHue(), hsb.getSaturation(),
					new PercentType(brightness));
			setHSBValue(service, newHsb);
			return;
		}

		// process percent command
		if (command instanceof PercentType
				&& !isRedefinedByCustomCommand(command)
				&& !service.hasChannelActions(channels[0])) {
			PercentType t = (PercentType) command;

			HSBType hsb = hsbState;
			if (hsb == null) {
				hsb = new HSBType(Color.WHITE);
			}

			HSBType newHsb = new HSBType(hsb.getHue(), hsb.getSaturation(), t);
			setHSBValue(service, newHsb);
			return;
		}

		// process on/off, increase/decrease, percent type
		super.processCommand(service, command);
	}

	private void setHSBValue(DmxService service, HSBType hsbValue) {

		hsbState = hsbValue;

		int valueLength = isRgbw() ? 4 : 3;
		int[] values = new int[valueLength];
		values[0] = DmxUtil.getByteFromPercentType(hsbValue.getRed());
		values[1] = DmxUtil.getByteFromPercentType(hsbValue.getGreen());
		values[2] = DmxUtil.getByteFromPercentType(hsbValue.getBlue());
		if (isRgbw()) {
			values[3] = DmxUtil.calculateWhite(values[0], values[1], values[3]);
		}

		int j = 0;
		for (int c : channels) {
			service.setChannelValue(c, values[j++]);
			if (j == values.length) {
				j = 0;
			}
		}
	}

	/**
	 * Check if this item represents RGB or RGBW.
	 * 
	 * @return true if color item is RGBW.
	 */
	private boolean isRgbw() {
		return channels.length % 4 == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStatusListener() {
		if (updateDelay > MIN_UPDATE_DELAY
				&& (channels.length == 3 || channels.length == 4)) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processStatusUpdate(int[] channelValues) {

		if (channelValues.length != 3 && channelValues.length != 4) {
			return;
		}

		HSBType cmd = new HSBType(
			new Color(channelValues[0], channelValues[1], channelValues[2]));
		publishState(cmd);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFootPrint() {
		if (channels == null) {
			// default footprint
			return 3;
		}
		// support RGB & RGBW
		return isRgbw() ? 4 : 3;
	}
}
