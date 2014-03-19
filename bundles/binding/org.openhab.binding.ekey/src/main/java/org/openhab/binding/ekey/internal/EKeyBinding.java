/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekey.internal;

import java.util.Dictionary;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.openhab.binding.ekey.EKeyBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fhooe.mc.schlgtwt.parser.MultiPacket;
import at.fhooe.mc.schlgtwt.parser.UniformPacket;

/**
 * This binding will start a thread that listens to incoming UDP traffic to
 * receive packet from the eKey-UDP-Converter. It is an unidirectional
 * conversation between the converter and the binding. No data is sent. This
 * binding supports the following protocol-modes:
 * <ul>
 * <li><b>RARE</b> This is set by default on the ekey-UDP-converter.<br>
 * It provides the following information:
 * <ul>
 * <li><b>Action</b>  - open or deny</li>
 * <li><b>TerminalID</b>  - final 8 digits of serial</li>
 * <li><b>RelayID</b>  - ID of Relay that switched</li>
 * <li><b>UserID</b>  - ID of User according to Controller</li>
 * <li><b>FingerID</b> - which finger was used</li>  
 * </ul>
 * </li>
 * <li><b>HOME</b> This provides about the same information as the RARE type. <br>
 * But in contrast this is sent as a String message over UDP and not as byte message
 * <ul>
 * <li><b>Action</b>  - open/deny or digital input</li>
 * <li><b>TerminalID</b>  - full serial of the terminal used</li>
 * <li><b>RelayID</b>  - ID of Relay that switched</li>
 * <li><b>UserID</b>  - ID of User according to controller</li>
 * <li><b>FingerID</b> - which finger was used</li> 
 * </ul>
 * </li>
 * <li><b>MULTI</b> This is the most powerful mode. It provides the most information.<br>
 * But it is only available on eKey-Multi devices
 * <ul>
 * <li><b>Action</b>  - open/deny (various forms of denial)</li>
 * <li><b>TerminalID</b>  - full serial of the terminal used</li>
 * <li><b>TerminalName</b>  - short name for the terminal according to the controller</li>
 * <li><b>KeyID</b>  - ID of the Key that was specified for that finger on the controller</li>
 * <li><b>UserID</b>  - ID of User according to Controller</li>
 * <li><b>UserName</b>  - short name for the user according to the controller</li>
 * <li><b>UserStatus</b>  - Status of the user (active/inactive)</li>
 * <li><b>FingerID</b> - which finger was used</li> 
 * <li><b>InputID</b> - ID of the digital input that was triggered</li> 
 * <li>but no RelayID</li> 
 * </ul>
 * </li>
 * </ul>
 * You can decide between those modes depending on your hardware. Use the software that is delivered with the
 * UDP-converter to change the mode.
 * 
 * @author Paul Schlagitweit
 * @since 1.5.0
 */
public class EKeyBinding extends AbstractBinding<EKeyBindingProvider> implements
		ManagedService, IEKeyListener {

	private static final Logger logger = LoggerFactory
			.getLogger(EKeyBinding.class);

	private static final String[] MODES = { "RARE", "HOME", "MULTI" };
	// pattern for checking ip addresses
	private static final String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	// members
	private int port = 0;
	private String ip = null;
	private String deliminator = null;
	private int mode = UniformPacket.tRARE;
	private EKeyPacketReceiver packetlistener;

	public EKeyBinding() {

		packetlistener = new EKeyPacketReceiver(this);
	}

	public void activate() {
	}

	public void deactivate() {
		logger.debug("Stoppig eKey listener...");
		if (packetlistener != null)
			packetlistener.stopListener();
	}
	
	

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// get ip from config file
			ip = (String) config.get("ip");

			if (!Pattern.compile(IP_PATTERN).matcher(ip).matches()){ // check valid ip
				logger.warn("eKey:ip No ip specified. It is recommended to specify a static ip");
				ip = null;
			}

			// get port from config file
			String portstring = (String) config.get("port");
			if (StringUtils.isNotBlank(portstring)) {
				port = Integer.parseInt(portstring);
			} else
				throw new ConfigurationException(
						"eKey:port",
						"No port specified."
								+ " Please specify a port as you did during the UDP-Converter configuration");

			// get mode from config file
			String modestring = (String) config.get("mode");
			if (StringUtils.isNotBlank(modestring)) {
				modestring = modestring.toUpperCase().trim();

				if (modestring.equals(MODES[0]))
					mode = UniformPacket.tRARE;
				else if (modestring.equals(MODES[1]))
					mode = UniformPacket.tHOME;
				else if (modestring.equals(MODES[2]))
					mode = UniformPacket.tMULTI;
				else
					throw new ConfigurationException(
							"eKey:mode",
							"Unknown mode '"
									+ modestring
									+ "'. Valid values are RARE, MULTI or HOME.");

			} else { // no mode specified -> use default
				logger.warn("eKey:mode was not declared in the config file. So mode RARE is used as default!");
				mode = UniformPacket.tRARE;
			}

			// get deliminator from the config file
			String delstring = (String) config.get("delimiter");
			if (StringUtils.isBlank(delstring)) { 
				// a blank (" ") will be definded as default deliminator
				deliminator = " ";
			} else {
				deliminator = delstring;
			}

			// make sure that there is no listener running
			packetlistener.stopListener();
			// send the parsed information to the listener
			packetlistener.initializeReceiver(mode, ip, port, deliminator);
			// start the listener
			new Thread(packetlistener).start();

		}
	}

	@Override
	public void publishUpdate(UniformPacket ekeyRecord) {

		for (EKeyBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {

				// get the interesting value from the parsed ekey packet
				Object value = EKeyBindingConfig.getValueOfInterest(ekeyRecord,
						provider.getItemInterest(itemName));

				if (value == null) // ekey packet doesn't provide the value that
									// is wanted
					logger.error("The interest '"
							+ provider.getItemInterest(itemName).toString()
							+ "' that you specified for the item '"
							+ itemName
							+ "' is not provided by the packet mode "
							+ "which is defined in the openhab.cfg.\nPlease change the mode on your eKey controller"
							+ "and your config file or avoid using this type of interest");

				// check what type of item wants to know the value
				Class<? extends Item> itemType = provider.getItemType(itemName);
				if (itemType.isAssignableFrom(NumberItem.class)) { // NumberItem

					if (value instanceof Integer)
						eventPublisher.postUpdate(itemName,
								DecimalType.valueOf(value.toString()));
					else
						logger.error("Your NumberItem cannot take values of type String!");

				} else // StringItem
				if (itemType.isAssignableFrom(StringItem.class)) { 
					// allow both - return of number or integer
					if (value instanceof String)
						eventPublisher.postUpdate(itemName,
								StringType.valueOf((String) value));
					else if (value instanceof Integer)
						eventPublisher.postUpdate(itemName,
								StringType.valueOf(value.toString()));
				}

			}
		}

		if (ekeyRecord != null)
			logger.info("new packet arrived: " + ekeyRecord.toString());

	}
	


}
