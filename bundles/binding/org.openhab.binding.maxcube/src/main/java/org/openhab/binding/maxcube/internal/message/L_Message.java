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
package org.openhab.binding.maxcube.internal.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.MaxTokenizer;
import org.slf4j.Logger;

/**
 * The L message contains real time information about all MAX! devices.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public final class L_Message extends Message {

	private List<Device> devices = new ArrayList<Device>();

	public L_Message(String raw) {
		super(raw);
	}

	public Collection<? extends Device> getDevices(List<Configuration> configurations) {
		
		byte[] decodedRawMessage = Base64.decodeBase64(getPayload().getBytes());

		MaxTokenizer tokenizer = new MaxTokenizer(decodedRawMessage);

		while (tokenizer.hasMoreElements()) {
			byte[] token = tokenizer.nextElement();

			devices.add(Device.create(token, configurations));
		}
		
		return devices;
	}
	
	@Override
	public void debug(Logger logger) {
		logger.debug("=== L_Message === ");
	}

	@Override
	public MessageType getType() {
		return MessageType.L;
	}
}