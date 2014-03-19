/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.config;

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommand;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Nikobus button. Represents one button area on a physical push button.
 * 
 * E.g. The Nikobus push button 05-064-01 has four different areas to press: A,
 * B, C & D. The NikobusButton class represents a only one area for the entire
 * button, which means that you will need 4 NikobusButton classes to control A,
 * B, C & D on this one physical device.
 * 
 * Since this class only represents a single area, the address used in this
 * class represents the button address + the selected area and as such does not
 * match the button address as you can find it in the Nikobus software.
 * 
 * To find the correct address of a button area to use, run OpenHab in debug
 * mode and write down the addresses which are printed to the console when you
 * press a button.
 * 
 * This class has been tested with the 05-06x-xx button series.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class Button extends AbstractNikobusItemConfig {

	protected static Logger log = LoggerFactory.getLogger(Button.class);

	public static final String END_OF_TRANSMISSION = "#E1";

	public enum PressType {
		SHORT, LONG
	}

	private PressType type = PressType.SHORT;

	private String[] impactedModules;

	/**
	 * Create a new button from the provided binding configuration. A button can
	 * be configured to receive short or long button presses (1 sec). When a
	 * button receives a long press, it does not also receive a short press.
	 * 
	 * Example button configurations are:
	 * 
	 * <pre>
	 * {@code
	 * 		#N4635E5 
	 * 		#N4635E5:LONG
	 * 		#N46345E[4856-1] 
	 * 		#N46345E:LONG[4856-1] 
	 * 		#N46345E:LONG[4856-1,4856-2]
	 * }
	 * </pre>
	 * 
	 * @param config
	 *            item configuration.
	 */
	public Button(String name, String config) {
		super(name, extractAddress(config));

		if (config.indexOf("LONG") != -1) {
			type = PressType.LONG;
		}

		if (config.indexOf("[") != -1) {
			// parse module groups
			String modulesConfig = config.substring(config.indexOf("[") + 1,
					config.lastIndexOf(']'));
			impactedModules = modulesConfig.split(",");
		}
	}

	/**
	 * Extract the button address part from the configuration string.
	 * 
	 * The address for a button is the full button signature including the area
	 * of the button which is pressed, e.g. #N98450
	 * 
	 * @param config
	 *            full configuration string, e.g. #N98450[1234-1]
	 * @return address part, e.g #N98450
	 */
	private static String extractAddress(String config) {

		if (config.indexOf(':') != -1) {
			return config.substring(0, config.indexOf(":"));
		}

		if (config.indexOf("[") != -1) {
			return config.substring(0, config.indexOf("["));
		}
		return config;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Commands will be processed only if they match the correct press type for
	 * the button. Every received command will trigger a status refresh request
	 * for any modules linked to the button.
	 */
	@Override
	public void processNikobusCommand(NikobusCommand command, NikobusBinding binding) {

		if (!command.getCommand().equals(getAddress())) {
			// not this this button
			return;
		}

		// the button was pressed, so let's refresh the status..
		if (command.getRepeats() <= NikobusCommand.MAX_REPEAT) {
			notifyModulesChanged(command.getRepeats() == NikobusCommand.MAX_REPEAT, binding);
		}

		if (command.getRepeats() >= NikobusCommand.MAX_REPEAT
				&& type == PressType.SHORT) {
			// we received a long button press
			// but we only care for short ones..
			return;
		} else if (command.getRepeats() < NikobusCommand.MAX_REPEAT
				&& type == PressType.LONG) {
			// we received a short button press
			// but we want a long one..
			return;
		}

		log.trace("Processing command {}", command.getCommand());

		// the button press received matches the expected one,
		// so we broadcast an ON state change
		binding.postUpdate(getName(), OnOffType.ON);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * When an ON command is received, a simulated button press is sent to the
	 * nikobus and a status refresh request is triggered for any modules linked
	 * to the button.
	 */
	@Override
	public void processCommand(Command command, NikobusBinding binding) throws Exception {

		log.trace("Processing command {}", command.toString());

		if (command == OnOffType.ON && getAddress().length() == 8) {
			// Whenever the button receives an ON command,
			// we send a simulated button press to the Nikobus
			int times = (type == PressType.LONG) ? NikobusCommand.MAX_REPEAT
					: 1;
			binding.sendCommand(new NikobusCommand(getAddress(), times));
			binding.sendCommand(new NikobusCommand(END_OF_TRANSMISSION));
		}

		notifyModulesChanged(false, binding);
	}

	/**
	 * Notify the binding provider that the state of the modules linked to this
	 * button is no longer accurate,
	 * 
	 * Refresh the status of any module whose state was impacted by the button
	 * press.
	 * 
	 * @param delayedSend
	 *            true if we should wait for an empty bus
	 */
	private void notifyModulesChanged(boolean delayedSend, NikobusBinding binding) {

		// refresh status of modules affected by button press
		if (impactedModules != null) {
			for (String moduleAddress : impactedModules) {
				binding.scheduleStatusUpdateRequest(moduleAddress, delayedSend);
			}
		}
	}

	/**
	 * @return whether this button is for short or long button presses.
	 */
	public PressType getType() {
		return type;
	}

}
