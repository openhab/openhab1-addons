/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openhab.binding.souliss.internal.network.udp.UDPSoulissDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class take service typicals from hash table and update it with new
 * values coming from the souliss network
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissTServiceUpdater {

	private static Logger logger = LoggerFactory
			.getLogger(UDPSoulissDecoder.class);

	/**
	 * Parse the hash table looking for the "virtual souliss' typical" idNodo
	 * and Souliss_TService_NODE_TIMESTAMP_VIRTUAL_SLOT and update its value
	 * 
	 * @param soulissTypicalsRecipients
	 * @param idNodo
	 * @param valueOf
	 */
	public static void updateHEALTY(SoulissTypicals soulissTypicalsRecipients,
			int idNodo, Short valueOf) {
		// Create and update "virtual souliss' typical"
		logger.debug("request to updateHEALTY. Node:  " + idNodo + "; Value: "
				+ valueOf);
		SoulissTServiceNODE_HEALTY VirtualTypical = (SoulissTServiceNODE_HEALTY) soulissTypicalsRecipients
				.getTypicalFromAddress(idNodo,
						Constants.Souliss_TService_NODE_HEALTY_VIRTUAL_SLOT,
						null);
		if (VirtualTypical != null) {
			VirtualTypical.setState(valueOf);
			logger.debug("updateHEALTY:  " + VirtualTypical.getName() + " ( "
					+ Short.valueOf(VirtualTypical.getType()) + ") = "
					+ valueOf);
		} else {
			logger.debug("Error for retrieving VirtualTypical from HashTable: "
					+ idNodo + ", "
					+ Constants.Souliss_TService_NODE_HEALTY_VIRTUAL_SLOT);
		}

	};

	/**
	 * Parse the hash table looking for the "virtual souliss' typical" idNodo
	 * and Souliss_TService_NODE_TIMESTAMP_VIRTUAL_SLOT and update its value
	 * 
	 * @param soulissTypicalsRecipients
	 * @param idNodo
	 */
	public static void updateTIMESTAMP(
			SoulissTypicals soulissTypicalsRecipients, int idNodo) {
		// Create and update "virtual souliss' typical"
		logger.debug("request to updateTIMESTAMP. Node:  " + idNodo);
		SoulissTServiceNODE_TIMESTAMP VirtualTypical = (SoulissTServiceNODE_TIMESTAMP) soulissTypicalsRecipients
				.getTypicalFromAddress(idNodo,
						Constants.Souliss_TService_NODE_TIMESTAMP_VIRTUAL_SLOT,
						null);

		if (VirtualTypical != null) {
			String tstamp = getTimestamp();
			VirtualTypical.setTIMESTAMP(tstamp);
			logger.debug("updateTIMESTAMP:  " + VirtualTypical.getName()
					+ " ( " + Short.valueOf(VirtualTypical.getType()) + ") = "
					+ tstamp);
		} else {
			logger.debug("Error for retrieving VirtualTypical from HashTable: "
					+ idNodo + ", "
					+ Constants.Souliss_TService_NODE_TIMESTAMP_VIRTUAL_SLOT);
		}

	}

	/**
	 * Create a time stamp as "yyyy-MM-dd'T'HH:mm:ssz"
	 * 
	 * @return String timestamp
	 */
	private static String getTimestamp() {
		// Pattern : yyyy-MM-dd'T'HH:mm:ssz
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSz");
		Date n = new Date();
		return sdf.format(n.getTime());
	}
}