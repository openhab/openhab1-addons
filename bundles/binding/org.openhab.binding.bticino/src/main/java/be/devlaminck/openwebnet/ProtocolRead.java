package be.devlaminck.openwebnet;

/**
 * ProtocolRead for OpenWebNet - OpenHab binding Based on code from Mauro Cicolella
 * (as part of the FREEDOMOTIC framework)
 * (https://github.com/freedomotic/freedomotic/tree/master/plugins/devices/openwebnet) 
 * and on code of Flavio Fcrisciani released as EPL
 * (https://github.com/fcrisciani/java-myhome-library)
 * 
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
 * 
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.5.0
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolRead
{
	private String m_message = "";

	private static final Logger logger = LoggerFactory
			.getLogger(ProtocolRead.class);

	private Map<String, String> m_properties = new HashMap<String, String>();

	public ProtocolRead(String p_message)
	{
		m_message = p_message;
		logger.info("Instance created for message [" + p_message + "]");
	}

	public void addProperty(String p_key, String p_value)
	{
		// TODO Auto-generated method stub
		logger.info("addProperty Key : " + p_key + ", Value : " + p_value);
		m_properties.put(p_key, p_value);
	}

	public String getProperty(String p_key)
	{
		return (m_properties.get(p_key));
	}

	@Override
	public String toString()
	{
		return ("ProtocolRead, Message[" + m_message + "]");
	}
}
