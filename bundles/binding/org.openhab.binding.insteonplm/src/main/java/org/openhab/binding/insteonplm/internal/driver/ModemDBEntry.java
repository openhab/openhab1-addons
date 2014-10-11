/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import java.util.ArrayList;
import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.message.Msg;

/*
 * The ModemDBEntry class holds a modem device type record
 * an xml file.
 *
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public class ModemDBEntry {
	private InsteonAddress	m_address = null;
	private Port			m_port = null;
	private	ArrayList<Msg>	m_linkRecords = new ArrayList<Msg>();
	
	public ModemDBEntry(InsteonAddress aAddr) {	m_address = aAddr; }
	public ArrayList<Msg> getLinkRecords() { return m_linkRecords; }
	public void addLinkRecord(Msg m) { m_linkRecords.add(m); }
	public void setPort(Port p) { m_port = p; }
	public Port getPort() { return m_port; }
	
	public String toString() {
		String s = "addr:" + m_address + "|link_recors";
		for (Msg m : m_linkRecords) {
			s += ":(" + m + ")";
		}
		return s;
	}
	
}
