/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.io.IOException;
import java.util.HashMap;
import org.openhab.binding.insteonplm.internal.driver.ModemDBEntry;
import org.openhab.binding.insteonplm.internal.driver.Port;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Builds the modem database from incoming link record messages
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public class ModemDBBuilder implements MsgListener {
	private static final Logger logger = LoggerFactory.getLogger(ModemDBBuilder.class);
	
	Port m_port = null;
	
	public ModemDBBuilder(Port port) {
		m_port = port;
	}
	
	public void start() {
		m_port.addListener(this);
		logger.debug("querying port for first link record");
		try {
			m_port.writeMessage(Msg.s_makeMessage("GetFirstALLLinkRecord"));
		} catch (IOException e) {
			logger.error("cannot query for link messages", e);
		}
	}
	
	/**
	 * processes link record messages from the modem to build database
	 * and request more link records if not finished.
	 * {@inheritDoc}
	 */
	@Override
	public void msg(Msg msg, String fromPort) {
		if (msg.isPureNack()) return;
		try {
			if (msg.getByte("Cmd") == 0x69 ||
						msg.getByte("Cmd") == 0x6a) {
				// If the flag is "ACK/NACK", a record response
				// will follow, so we do nothing here.
				// If its "NACK", there are none
				if (msg.getByte("ACK/NACK") == 0x15) {
					logger.debug("got all link records.");
					done();
				}
			} else if (msg.getByte("Cmd") == 0x57) {
				// we got the link record response
				updateModemDB(msg);
				m_port.writeMessage(Msg.s_makeMessage("GetNextALLLinkRecord"));
			}
		} catch (FieldException e) {
			logger.debug("bad field handling link records {}", e);
		} catch (IOException e) {
			logger.debug("got IO exception handling link records {}", e);
		} catch (IllegalStateException e) {
			logger.debug("got exception requesting link records {}", e);
		}
	}
	
	private void done() {
		m_port.removeListener(this);
		m_port.modemDBComplete();
	}
	
	private void updateModemDB(Msg m) 	{
		try {
			HashMap<InsteonAddress, ModemDBEntry> dbes = m_port.getDriver().lockModemDBEntries();
			InsteonAddress linkAddr = m.getAddress("LinkAddr");
			ModemDBEntry dbe = dbes.get(linkAddr.toString());
			if (dbe == null) {
				dbe = new ModemDBEntry(linkAddr);
				dbes.put(linkAddr, dbe);
			}
			dbe.setPort(m_port);
			dbe.addLinkRecord(m);
		} catch (FieldException e) {
			logger.error("cannot access field:", e);
		} finally {
			m_port.getDriver().unlockModemDBEntries();
		}
	}
}
