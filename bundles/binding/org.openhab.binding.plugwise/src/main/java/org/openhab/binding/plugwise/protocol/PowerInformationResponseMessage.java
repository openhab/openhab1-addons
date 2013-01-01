/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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


