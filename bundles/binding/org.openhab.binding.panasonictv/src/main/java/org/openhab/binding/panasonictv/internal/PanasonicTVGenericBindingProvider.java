/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panasonictv.internal;

import org.openhab.binding.panasonictv.PanasonicTVBindingConfig;
import org.openhab.binding.panasonictv.PanasonicTVBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * Valid configurations should look, for example, like this:<br />
 * 		<ul><li>In openhab.cfg: define TVs:</br 7>
 * 			<code>panasonictv:bedroom=192.168.1.100</code></li>
 * 		<li>In *.item file: define single items for each command:<br />
 * 			<code>Switch BR_setTVHDMI1 "Bedroom TV Input HDMI1" { panasonictv="bedroom:hdmi1" }</code><br />
 * 			<code>Switch BR_toggleMute "Bedroom TV Mute" { panasonictv="bedroom:mute" }</code></li></ul>	
 * 
 * @author André Heuer
 * @since 1.7.0
 */


/*			
  		Following commands are available (some commands may depend on TV model):
  		  "CH_DOWN"   => "Channel down",
		  "CH_UP"     => "Channel up",
		  "VOLUP"     => "Volume up",
		  "VOLDOWN"   => "Volume down",
		  "MUTE"      => "Mute",
		  "TV"        => "TV",
		  "CHG_INPUT" => "AV",
		  "RED"       => "Red",
		  "GREEN"     => "Green",
		  "YELLOW"    => "Yellow",
		  "BLUE"      => "Blue",
		  "VTOOLS"    => "VIERA tools",
		  "CANCEL"    => "Cancel / Exit",
		  "SUBMENU"   => "Option",
		  "RETURN"    => "Return",
		  "ENTER"     => "Control Center click / enter",
		  "RIGHT"     => "Control RIGHT",
		  "LEFT"      => "Control LEFT",
		  "UP"        => "Control UP",
		  "DOWN"      => "Control DOWN",
		  "3D"        => "3D button",
		  "SD_CARD"   => "SD-card",
		  "DISP_MODE" => "Display mode / Aspect ratio",
		  "MENU"      => "Menu",
		  "INTERNET"  => "VIERA connect",
		  "VIERA_LINK"=> "VIERA link",
		  "EPG"       => "Guide / EPG",
		  "TEXT"      => "Text / TTV",
		  "STTL"      => "STTL / Subtitles",
		  "INFO"      => "Info",
		  "INDEX"     => "TTV index",
		  "HOLD"      => "TTV hold / image freeze",
		  "R_TUNE"    => "Last view",
		  "POWER"     => "Power off",
		  "REW"       => "Rewind",
		  "PLAY"      => "Play",
		  "FF"        => "Fast forward",
		  "SKIP_PREV" => "Skip previous",
		  "PAUSE"     => "Pause",
		  "SKIP_NEXT" => "Skip next",
		  "STOP"      => "Stop",
		  "REC"       => "Record",
		  "D1"        => "Digit 1",
		  "D2"        => "Digit 2",
		  "D3"        => "Digit 3",
		  "D4"        => "Digit 4",
		  "D5"        => "Digit 5",
		  "D6"        => "Digit 6",
		  "D7"        => "Digit 7",
		  "D8"        => "Digit 8",
		  "D9"        => "Digit 9",
		  "D0"        => "Digit 0",
		  "P_NR"      => "P-NR (Noise reduction)",
		  "R_TUNE"    => "Seems to do the same as INFO",
		  "HDMI1"     => "Switch to HDMI input 1",
		  "HDMI2"     => "Switch to HDMI input 2",
		  "HDMI3"     => "Switch to HDMI input 3",
		  "HDMI4"     => "Switch to HDMI input 4",
*/

public class PanasonicTVGenericBindingProvider extends
		AbstractGenericBindingProvider implements PanasonicTVBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(PanasonicTVGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "panasonictv";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		String[] configs = bindingConfig.split(":");
		if (configs.length < 2) {
			logger.debug("Invalid item config for item '" + item.getName()
					+ "': " + bindingConfig);
			return;
		}

		logger.debug("Item '" + item.getName() + "' added for TV '"
				+ configs[0] + "' and command '" + configs[1] + "'");

		addBindingConfig(item, new PanasonicTVBindingConfig(item, configs[0],
				configs[1].toUpperCase()));
	}

	@Override
	public PanasonicTVBindingConfig getBindingConfigForItem(String item) {
		PanasonicTVBindingConfig config = (PanasonicTVBindingConfig)bindingConfigs.get(item);
		
		return config;
	}

}
