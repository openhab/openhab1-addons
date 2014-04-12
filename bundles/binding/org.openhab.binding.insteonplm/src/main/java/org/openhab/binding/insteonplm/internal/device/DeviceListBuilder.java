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

import org.openhab.binding.insteonplm.internal.device.InsteonDevice.InitStatus;
import org.openhab.binding.insteonplm.internal.driver.Port;
import org.openhab.binding.insteonplm.internal.message.FieldException;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Builds a list of devices from modem link records
 * (category, sub category, version) 
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public class DeviceListBuilder implements MsgListener {
	private static final Logger logger = LoggerFactory.getLogger(DeviceListBuilder.class);
	
	Port					m_port = null;
	
	public DeviceListBuilder(Port port) {
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
	 * processes link record messages from the modem to build device list,
	 * and request more link records if not finished.
	 * @see org.openhab.binding.insteonplm.internal.message.MsgListener#msg(org.openhab.binding.insteonplm.internal.message.Msg, java.lang.String)
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
				updateDeviceList(msg);
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
		m_port.deviceListComplete();
	}
	
	private void updateDeviceList(Msg m) 	{
		try {
			InsteonAddress linkAddr = m.getAddress("LinkAddr");
			int devCat	= m.getByte("LinkData1") & 0xff;
			int subCat	= m.getByte("LinkData2") & 0xff;
			int vers	= m.getByte("LinkData3") & 0xff;
			/*
			if (devCat == 0 && subCat == 0 && vers == 0) {
				// got this kind of bogus link record.
				// not sure what to do with it, just ignore
				// the record
				return;
			}
			*/
			HashMap<InsteonAddress, InsteonDevice> devices = m_port.getDeviceList();
			synchronized(devices) {
				InsteonDevice dev = devices.get(linkAddr);
				if (dev == null) {
					dev = InsteonDevice.s_makeDevice(linkAddr, m_port.getDriver());
					dev.setInitStatus(InitStatus.INITIALIZED);
					devices.put(linkAddr,  dev);
				}
				dev.addPort(m_port.getDeviceName());
				dev.addLinkRecord(m);
				DeviceDescriptor desc = DeviceDescriptor.s_getDeviceDescriptor(devCat, subCat, 0x00);
				desc.setVersion(vers);
				dev.addDescriptor(desc);
				dev.instantiateFeatures();
			}
		} catch (FieldException e) {
			logger.error("cannot access field:", e);
		}
	}
}
