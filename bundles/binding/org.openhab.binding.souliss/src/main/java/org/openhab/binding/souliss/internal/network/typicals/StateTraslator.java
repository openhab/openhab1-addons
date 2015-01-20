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
 * The method of this class takes key and translate it  
 * @author Antonino-Fazio
 */
public class StateTraslator {
	static Properties propCommands=new Properties();
	static Properties propStates=new Properties();
	static Properties propTypes=new Properties();
	
	
	private static Logger LOGGER = LoggerFactory.getLogger(StateTraslator.class);
	
	
	public static short stringToSOULISSTypicalCode(String sTypeString) {
		String sRes=null;
		sRes=propTypes.getProperty(sTypeString);
		LOGGER.debug("translate types: " + sTypeString +" -> " + sRes);
  		return  Short.decode(sRes);
	}
	
	public static Short commandsOHtoSOULISS(int typ, String sCommand) {
		String sRes=null;
		sRes=propCommands.getProperty("0x"+Short.decode(Integer.toHexString(typ))+Constants.CONF_FIELD_DELIMITER+sCommand);
		LOGGER.debug("translate commands: " + sCommand +" -> " + sRes);
		if(sRes!=null)
		return Short.decode(sRes);
		else return null;
	}

	public static String statesSoulissToOH(String sOHType, short s, short f) {
		String s1=Integer.toHexString(s); 
		s1= s1.length() < 2 ? "0x0" + s1.toUpperCase() : "0x"+s1.toUpperCase();
		
		String s2=Integer.toHexString(f);
		s2= s2.length() < 2 ? "0x0" + s2.toUpperCase(): "0x"+s2.toUpperCase();
		
		
		String sRes=null;
		String sVal=sOHType+Constants.CONF_FIELD_DELIMITER+s1+Constants.CONF_FIELD_DELIMITER+s2;
		sRes=propStates.getProperty(sVal);
		LOGGER.debug("translate states: " + sVal +" -> " + sRes);
		return sRes;
	}
	
	public static void loadCommands(InputStream is) {
		try {
			propCommands.load(is);
			LOGGER.info("ok");
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}
	
	public static void loadStates(InputStream is) {
		try {
			propStates.load(is);
			LOGGER.info("ok");
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	public static void loadItemsType(InputStream is) {
		try {
			propTypes.load(is);
			LOGGER.info("ok");
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}
}
