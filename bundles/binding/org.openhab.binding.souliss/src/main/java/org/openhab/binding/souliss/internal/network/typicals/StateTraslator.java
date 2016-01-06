/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The methods of this class takes key from files
 * commands_OHtoSOULISS.properties
 * commands_to_states.properties
 * itemsType_SOULISS.properties
 * states_SOULISStoOH.properties
 * typicals_value_bytes.properties
 * 
 * and translate it values suitables
 * for openhab or Souliss
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class StateTraslator {
	static Properties propCommands = new Properties();
	static Properties propStates = new Properties();
	static Properties propTypes = new Properties();
	static Properties propCommands_to_states = new Properties();

	private static Logger logger = LoggerFactory
			.getLogger(StateTraslator.class);

	/**
	 * Take string key and translate it to SOULISS Typical Code
	 * 
	 * @param String
	 *            sTypeString
	 * @return short
	 */
	public static short stringToSOULISSTypicalCode(String sTypeString) {
		String sRes = null;
		sRes = propTypes.getProperty(sTypeString);
		logger.debug("translate types: {} -> {}", sTypeString , sRes);
		return Short.decode(sRes.trim());
	}

	/**
	 * Take integer typical value and openhab string command and translate it to
	 * Souliss command code
	 * 
	 * @param int typ
	 * @param String
	 *            sCommand
	 * @return short
	 */
	public static Short commandsOHtoSOULISS(int typ, String sCommand) {
		String sRes = null;
		sRes = propCommands.getProperty("0x"
				+ Short.decode(Integer.toHexString(typ))
				+ Constants.CONF_FIELD_DELIMITER + sCommand);
		logger.debug("translate commands: {} -> {}", sCommand, sRes);
		if (sRes != null)
			return Short.decode(sRes.trim());
		else
			return null;
	}

	/**
	 * Translate Souliss states in Openhab states
	 * 
	 * @param String
	 *            sOHType
	 * @param short type
	 * @param short state
	 * @return String
	 */
	public static String statesSoulissToOH(String sOHType, short s, short f) {
		String s1 = Integer.toHexString(s);
		s1 = s1.length() < 2 ? "0x0" + s1.toUpperCase() : "0x"
				+ s1.toUpperCase();

		String s2 = Integer.toHexString(f);
		s2 = s2.length() < 2 ? "0x0" + s2.toUpperCase() : "0x"
				+ s2.toUpperCase();

		String sRes = null;
		String sVal = sOHType + Constants.CONF_FIELD_DELIMITER + s1
				+ Constants.CONF_FIELD_DELIMITER + s2;
		sRes = propStates.getProperty(sVal);
		logger.debug("translate states: {} -> {}", sVal, sRes);
		return sRes;
	}
	
	
	/**
	 * Translate Souliss command to expected states
	 * 
	 * @param String
	 *            sOHType
	 * @param short type
	 * @param short state
	 * @return String
	 */
	public static String translateCommandsToExpectedStates(short soulissType, short s) {
		String s2 = Integer.toHexString(s);
		s2 = s2.length() < 2 ? "0x0" + s2.toUpperCase() : "0x"
				+ s2.toUpperCase();
		
		String s1 = Integer.toHexString(soulissType);
		s1 = s1.length() < 2 ? "0x0" + s1.toUpperCase() : "0x"
				+ s1.toUpperCase();
		
		String sRes = null;
		String sVal = s1.trim() + Constants.CONF_FIELD_DELIMITER + s2.trim();
		sRes = propCommands_to_states.getProperty(sVal.trim());
		return sRes;
	}

	/**
	 * Load commands file
	 * 
	 * @param InputStream
	 *            is
	 */
	public static void loadCommands(InputStream is) {
		try {
			propCommands.load(is);
			logger.info("ok");
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * Load states file
	 * 
	 * @param InputStream
	 *            is
	 */
	public static void loadStates(InputStream is) {
		try {
			propStates.load(is);
			logger.info("ok");
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**
	 * Load commands items type
	 * 
	 * @param InputStream
	 *            is
	 */
	public static void loadItemsType(InputStream is) {
		try {
			propTypes.load(is);
			logger.info("ok");
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public static void loadCommands_to_states(InputStream is) {
		try {
			propCommands_to_states.load(is);
			logger.info("ok");
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

}
