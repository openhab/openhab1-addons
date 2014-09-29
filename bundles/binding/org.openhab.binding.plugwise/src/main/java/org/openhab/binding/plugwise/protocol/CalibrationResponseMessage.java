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

/**
 * Circle Calibration response
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class CalibrationResponseMessage extends Message {
	
	private float gaina;
	private float gainb;
	private float offtot;
	private float offruis;

	public float getGaina() {
		return gaina;
	}


	public float getGainb() {
		return gainb;
	}


	public float getOfftot() {
		return offtot;
	}


	public float getOffruis() {
		return offruis;
	}


	public CalibrationResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.DEVICE_CALIBRATION_RESPONSE;
	}


	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{8})(\\w{8})(\\w{8})(\\w{8})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()) {
			MAC = matcher.group(1);
						
			gaina = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(2), 16)  ));			
			gainb = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(3), 16)  ));			
			offtot = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(4), 16) ));
			offruis = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(5), 16)  ));			
		}
		else {
			logger.debug("Plugwise protocol RoleCallResponseMessage error: {} does not match", payLoad);
		}
	}

}
