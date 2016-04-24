/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal;

import java.io.InvalidClassException;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid command types which could be processed by this
 * binding.
 * 
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public enum OppoBlurayPlayerCommandType {

	POWER ("Power", SwitchItem.class),
	PLAYBACK_STATUS ("PlayBackStatus", StringItem.class),
	VOLUME_LEVEL ("VolumeLevel", NumberItem.class),
	TRAY_POSITION ("TrayPosition", ContactItem.class),
	MUTE ("Mute", SwitchItem.class),
	VERBOSITY ("VerboseMode", StringItem.class),
	DISC_TYPE ("DiscType", StringItem.class),
	HDMI_RESOLUTION ("HdmiResolution", StringItem.class),
	INPUT_SOURCE ("Source", StringItem.class),
	;

	private final String text;
	private Class<? extends Item> itemClass;

	private OppoBlurayPlayerCommandType(final String text, Class<? extends Item> itemClass) {
		this.text = text;
		this.itemClass = itemClass;
	}

	@Override
	public String toString() {
		return text;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * Procedure to validate command type string.
	 * 
	 * @param commandTypeText
	 *            command string e.g. RawData, Command, Brightness
	 * @return true if item is valid.
	 * @throws IllegalArgumentException
	 *             Not valid command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static boolean validateBinding(String commandTypeText,
			Class<? extends Item> itemClass) throws IllegalArgumentException,
			InvalidClassException {

		for (OppoBlurayPlayerCommandType c : OppoBlurayPlayerCommandType.values()) {
			if (c.text.equals(commandTypeText)) {

				if (c.getItemClass().equals(itemClass)) {
					return true;
				} else {
					throw new InvalidClassException(
							"Not valid class for command type");
				}
			}
		}

		throw new IllegalArgumentException("Not valid command type");

	}

	/**
	 * Procedure to convert command type string to command type class.
	 * 
	 * @param commandTypeText
	 *            command string e.g. RawData, Command, Brightness
	 * @return corresponding command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static OppoBlurayPlayerCommandType getCommandType(String commandTypeText)
			throws IllegalArgumentException {

		for (OppoBlurayPlayerCommandType c : OppoBlurayPlayerCommandType.values()) {
			if (c.text.equals(commandTypeText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid command type");
	}

	public static void findStatusEvent(String command) {
		// TODO Auto-generated method stub
		
	}

}
