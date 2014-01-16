/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.plugwise.internal.Energy;

/**
 * Response to a PowerBufferRequest
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerBufferResponseMessage extends Message {
	
	Energy[] datapoints;
	private int logAddress;

	public PowerBufferResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.POWER_BUFFER_RESPONSE;
	}


	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	public Energy getEnergy(int i) {
		return datapoints[i];
	}


	public Energy[] getDatapoints() {
		return datapoints;
	}


	public int getLogAddress() {
		return logAddress;
	}


	@Override
	protected void parsePayLoad() {
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{8})(\\w{8})(\\w{8})(\\w{8})(\\w{8})(\\w{8})(\\w{8})(\\w{8})(\\w{8})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()) {
			MAC = matcher.group(1);
			datapoints = new Energy[4];

			if(!matcher.group(2).equals("FFFFFFFF")){
				datapoints[0] = new Energy(matcher.group(2), Long.parseLong(matcher.group(3), 16), 3600);
			}
			else {
				datapoints[0] = null;
			}
			if(!matcher.group(4).equals("FFFFFFFF")){
				datapoints[1] = new Energy(matcher.group(2), Long.parseLong(matcher.group(5), 16), 3600);
			}
			else {
				datapoints[1] = null;
			}
			if(!matcher.group(6).equals("FFFFFFFF")){
				datapoints[2] = new Energy(matcher.group(2), Long.parseLong(matcher.group(7), 16), 3600);
			}
			else {
				datapoints[2] = null;
			}
			if(!matcher.group(8).equals("FFFFFFFF")){
				datapoints[3] = new Energy(matcher.group(2), Long.parseLong(matcher.group(9), 16), 3600);
			}
			else {
				datapoints[3] = null;
			}

			logAddress = (Integer.parseInt(matcher.group(10), 16) - 278528) / 8;	
		}
		else {
			logger.debug("Plugwise protocol PowerBufferResponseMessage error: {} does not match", payLoad);
		}
	}

}
