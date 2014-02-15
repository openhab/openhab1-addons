/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vdr.internal;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.hampelratte.svdrp.Command;
import org.hampelratte.svdrp.Connection;
import org.hampelratte.svdrp.Response;
import org.hampelratte.svdrp.commands.LSTT;
import org.hampelratte.svdrp.parsers.TimerParser;
import org.hampelratte.svdrp.responses.highlevel.VDRTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class open a TCP/IP connection to the VDR (SVDRP), sends commands and wait for a response
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */

public class VDRConnection {

	private static Logger logger = LoggerFactory.getLogger(VDRConnection.class);
	
	private String mIp;
	private int mPort = 6419; // VDR > 1.7.15: 6419 otherwise 2001;

	private static int timeout = 500;

	public static String charset = "en_US.UTF-8";

	public VDRConnection(String pIP, int pPort) {
		mIp = pIP;
		mPort = pPort;
	}

	/**
	 * Sends a SVDRP command to VDR and returns a response object, which
	 * represents the vdr response
	 * 
	 * @param cmd
	 *            The SVDRP command to send
	 * @return The SVDRP response or null, if the Command couldn't be sent
	 */
	public Response send(final Command cmd) {
		Response res = null;
		Connection connection=null;
		try {
			logger.trace("New connection");
			connection = new Connection(mIp, mPort, timeout, charset);
			logger.debug("Try to send VDR command: {}", cmd.getCommand());

			res = connection.send(cmd);
			logger.debug("Recived Message from VDR: {}", res.getMessage());
		} catch (Exception e) {
			logger.error("Could not connect to VDR on {}: {}", mIp + ":" + mPort,
					e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					logger.error("Could not close connection to VDR on {}: {}", mIp + ":" + mPort,
							e);
				}			
			}
		}

		return res;
	}

	/**
	 * Check if recording is in process
	 * 
	 * @return true if recording is in process otherwise false
	 */
	public Boolean isRecording() {
		// get list of timers
		Response response = send(new LSTT());
		Boolean ret = Boolean.FALSE;
		if (response != null && response.getCode()==250 && response.getMessage() != null) {
			List<VDRTimer> timerList = TimerParser.parse(response.getMessage());
			if (timerList != null && !timerList.isEmpty()) {
				// check each timer until found a time which is active and state is recording
				// do not use vdrTimer.isRecording because we need to add
				// enough time (e.g. 6 Minutes) because of wakeup time
				for (VDRTimer vdrTimer : timerList) {
					// check if timer is active and state is recording
					if (vdrTimer.isActive()
							&& vdrTimer.hasState(VDRTimer.RECORDING)) {
						ret = Boolean.TRUE;
						break;
					} else {
						// check if timer starts before now (+6 minutes for wakeup) and if timer stops after now 
						boolean recording = false;
						Calendar startNow = Calendar.getInstance();
						Calendar endNow = Calendar.getInstance();
						startNow.add(Calendar.MINUTE, 6);
						if (startNow.after(vdrTimer.getStartTime())
								&& endNow.before(vdrTimer.getEndTime())) {
							recording = vdrTimer.hasState(VDRTimer.ACTIVE);
						}
						// if recording is true, check if timer is repeating timer
						// if repeating timer check if DaySet match
						if (recording) {
							if (!vdrTimer.isRepeating()) {
								ret = Boolean.TRUE;
								break;
							} else if (vdrTimer.isDaySet(Calendar.getInstance())) {
								ret = Boolean.TRUE;
								break;
							}
						}
					}
				}
			}
		}
		logger.trace("VDR (" + mIp + ") recording state: " + ret);
		return ret;
	}
}