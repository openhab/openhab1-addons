/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
 * @author Andre Heuer
 * @since 1.7.0
 */


//		  "NRC_CH_DOWN-ONOFF"   => "Channel down",
//		  "NRC_CH_UP-ONOFF"     => "Channel up",
//		  "NRC_VOLUP-ONOFF"     => "Volume up",
//		  "NRC_VOLDOWN-ONOFF"   => "Volume down",
//		  "NRC_MUTE-ONOFF"      => "Mute",
//		  "NRC_TV-ONOFF"        => "TV",
//		  "NRC_CHG_INPUT-ONOFF" => "AV",
//		  "NRC_RED-ONOFF"       => "Red",
//		  "NRC_GREEN-ONOFF"     => "Green",
//		  "NRC_YELLOW-ONOFF"    => "Yellow",
//		  "NRC_BLUE-ONOFF"      => "Blue",
//		  "NRC_VTOOLS-ONOFF"    => "VIERA tools",
//		  "NRC_CANCEL-ONOFF"    => "Cancel / Exit",
//		  "NRC_SUBMENU-ONOFF"   => "Option",
//		  "NRC_RETURN-ONOFF"    => "Return",
//		  "NRC_ENTER-ONOFF"     => "Control Center click / enter",
//		  "NRC_RIGHT-ONOFF"     => "Control RIGHT",
//		  "NRC_LEFT-ONOFF"      => "Control LEFT",
//		  "NRC_UP-ONOFF"        => "Control UP",
//		  "NRC_DOWN-ONOFF"      => "Control DOWN",
//		  "NRC_3D-ONOFF"        => "3D button",
//		  "NRC_SD_CARD-ONOFF"   => "SD-card",
//		  "NRC_DISP_MODE-ONOFF" => "Display mode / Aspect ratio",
//		  "NRC_MENU-ONOFF"      => "Menu",
//		  "NRC_INTERNET-ONOFF"  => "VIERA connect",
//		  "NRC_VIERA_LINK-ONOFF"=> "VIERA link",
//		  "NRC_EPG-ONOFF"       => "Guide / EPG",
//		  "NRC_TEXT-ONOFF"      => "Text / TTV",
//		  "NRC_STTL-ONOFF"      => "STTL / Subtitles",
//		  "NRC_INFO-ONOFF"      => "Info",
//		  "NRC_INDEX-ONOFF"     => "TTV index",
//		  "NRC_HOLD-ONOFF"      => "TTV hold / image freeze",
//		  "NRC_R_TUNE-ONOFF"    => "Last view",
//		  "NRC_POWER-ONOFF"     => "Power off",
//		  "NRC_REW-ONOFF"       => "Rewind",
//		  "NRC_PLAY-ONOFF"      => "Play",
//		  "NRC_FF-ONOFF"        => "Fast forward",
//		  "NRC_SKIP_PREV-ONOFF" => "Skip previous",
//		  "NRC_PAUSE-ONOFF"     => "Pause",
//		  "NRC_SKIP_NEXT-ONOFF" => "Skip next",
//		  "NRC_STOP-ONOFF"      => "Stop",
//		  "NRC_REC-ONOFF"       => "Record",
//		  "NRC_D1-ONOFF"        => "Digit 1",
//		  "NRC_D2-ONOFF"        => "Digit 2",
//		  "NRC_D3-ONOFF"        => "Digit 3",
//		  "NRC_D4-ONOFF"        => "Digit 4",
//		  "NRC_D5-ONOFF"        => "Digit 5",
//		  "NRC_D6-ONOFF"        => "Digit 6",
//		  "NRC_D7-ONOFF"        => "Digit 7",
//		  "NRC_D8-ONOFF"        => "Digit 8",
//		  "NRC_D9-ONOFF"        => "Digit 9",
//		  "NRC_D0-ONOFF"        => "Digit 0",
//		  "NRC_P_NR-ONOFF"      => "P-NR (Noise reduction)",
//		  "NRC_R_TUNE-ONOFF"    => "Seems to do the same as INFO",
//		  "NRC_HDMI1"           => "Switch to HDMI input 1",
//		  "NRC_HDMI2"           => "Switch to HDMI input 2",
//		  "NRC_HDMI3"           => "Switch to HDMI input 3",
//		  "NRC_HDMI4"           => "Switch to HDMI input 4",



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
	 * 
	 * Das ist das parsen der .items-Datei
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		// System.out.println("process Binding config: " + context + ", item: "
		// + item + ", Binding config: " + bindingConfig);

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
		// TODO Auto-generated method stub
		
		PanasonicTVBindingConfig config = (PanasonicTVBindingConfig)bindingConfigs.get(item);
		
		return config;
	}

}
