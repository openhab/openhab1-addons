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

import org.joda.time.DateTime;
import org.openhab.binding.plugwise.internal.Energy;

/**
 * Message containing real-time energy consumption
 * Not all parts of this kind of Message are already reverse engineered
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerInformationResponseMessage extends Message {
	
	private Energy oneSecond;
	private Energy eightSecond;
	private Energy allSeconds;
	@SuppressWarnings("unused")
	private int unknown1;
	@SuppressWarnings("unused")
	private int unknown2;
	@SuppressWarnings("unused")
	private int unknown3;

	public PowerInformationResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.POWER_INFORMATION_RESPONSE;
	}

	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{4})(\\w{4})(\\w{8})(\\w{4})(\\w{4})(\\w{4})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()) {
			MAC = matcher.group(1);
			oneSecond = new Energy(DateTime.now(), Integer.parseInt(matcher.group(2), 16), 1);
			eightSecond = new Energy(DateTime.now(), Integer.parseInt(matcher.group(3), 16), 8);
			allSeconds = new Energy(DateTime.now(), Integer.parseInt(matcher.group(4), 16), 0);
			unknown1 = Integer.parseInt(matcher.group(5), 16);
			unknown2 = Integer.parseInt(matcher.group(6), 16);
			unknown3 = Integer.parseInt(matcher.group(7), 16);

		}
		else {
			logger.debug("Plugwise protocol PowerInformationResponseMessage error: {} does not match", payLoad);
		}
	}

	public Energy getOneSecond() {
		return oneSecond;
	}

	public Energy getEightSecond() {
		return eightSecond;
	}

	public Energy getAllSeconds() {
		return allSeconds;
	}
}


