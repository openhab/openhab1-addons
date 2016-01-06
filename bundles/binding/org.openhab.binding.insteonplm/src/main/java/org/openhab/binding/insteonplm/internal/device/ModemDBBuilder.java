/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.openhab.binding.insteonplm.internal.driver.ModemDBEntry;
import org.openhab.binding.insteonplm.internal.driver.Port;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.openhab.binding.insteonplm.internal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Builds the modem database from incoming link record messages
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public class ModemDBBuilder implements MsgListener, Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ModemDBBuilder.class);
	private boolean	m_isComplete 	= false;
	private Port	m_port 			= null;
	private	Thread	m_writeThread	= null;
	private int		m_timeoutMillis = 120000;

	public ModemDBBuilder(Port port) {
		m_port = port;
	}
	
	public void setRetryTimeout(int timeout) {
		m_timeoutMillis = timeout;
	}

	public void start() {
		m_port.addListener(this);
		m_writeThread	= new Thread(this);
		m_writeThread.setName("DBBuilder");
		m_writeThread.start();
		logger.debug("querying port for first link record");
	}

	public void startDownload() {
		logger.trace("starting modem database download");
		m_port.clearModemDB();
		getFirstLinkRecord();
	}
	
	public synchronized boolean isComplete() { return (m_isComplete); }


	@Override
	public void run() {
		logger.trace("starting modem db builder thread");
		while (!isComplete()) {
			startDownload();
			try {
				Thread.sleep(m_timeoutMillis); // wait for download to complete
			} catch (InterruptedException e) {
				logger.warn("modem db builder thread interrupted");
				break;
			}
			if (!isComplete()) {
				logger.warn("modem database download unsuccessful, restarting!");
			}
		}
		logger.trace("exiting modem db builder thread");
	}

	private void getFirstLinkRecord() {
		try {
			m_port.writeMessage(Msg.s_makeMessage("GetFirstALLLinkRecord"));
		} catch (IOException e) {
			logger.error("error sending link record query ", e);
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
				updateModemDB(msg.getAddress("LinkAddr"), m_port, msg);
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
	
	private synchronized void done() {
		m_isComplete = true;
		logModemDB();
		m_port.removeListener(this);
		m_port.modemDBComplete();
	}
	
	private void logModemDB() {
		try {
			logger.debug("MDB ------- start of modem link records ------------------");
			HashMap<InsteonAddress, ModemDBEntry> dbes = m_port.getDriver().lockModemDBEntries();
			for (Entry<InsteonAddress, ModemDBEntry> db : dbes.entrySet()) {
				ArrayList<Msg> lrs = db.getValue().getLinkRecords();
				for (Msg m: lrs) {
					int recordFlags = m.getByte("RecordFlags") & 0xff;
					String ms = ((recordFlags & (0x1 << 6)) != 0) ? "CTRL" : "RESP";
					logger.debug("MDB {}: {} group: {} data1: {} data2: {} data3: {}",
								db.getKey(), ms, toHex(m.getByte("ALLLinkGroup")),
									toHex(m.getByte("LinkData1")), toHex(m.getByte("LinkData2")),
											toHex(m.getByte("LinkData2")));
				}
				logger.debug("MDB -----");
			}
			logger.debug("MDB ---------------- end of modem link records -----------");
		} catch (FieldException e) {
			logger.error("cannot access field:", e);
		} finally {
			m_port.getDriver().unlockModemDBEntries();
		}
	}
	
	public static String toHex(byte b) {
		return Utils.getHexString(b);
	}
	
	public void updateModemDB(InsteonAddress linkAddr, Port port, Msg m) 	{
		HashMap<InsteonAddress, ModemDBEntry> dbes = port.getDriver().lockModemDBEntries();
		ModemDBEntry dbe = dbes.get(linkAddr);
		if (dbe == null) {
			dbe = new ModemDBEntry(linkAddr);
			dbes.put(linkAddr, dbe);
		}
		dbe.setPort(port);
		if (m != null) {
			dbe.addLinkRecord(m);
			try {
				byte group =  m.getByte("ALLLinkGroup");
				int recordFlags = m.getByte("RecordFlags") & 0xff;
				if ((recordFlags & (0x1 << 6)) != 0) {
					dbe.addControls(group);
				} else {
					dbe.addRespondsTo(group);
				}
			} catch (FieldException e) {
				logger.error("cannot access field:", e);
			}
		}
		port.getDriver().unlockModemDBEntries();
	}
}
