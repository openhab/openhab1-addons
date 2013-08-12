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
package org.openhab.binding.comfoair.handling;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to encapsulate all data which is needed to send a cmd to comfoair
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class ComfoAirCommand {

	private List<String> keys;
	private Integer requestCmd;
	private Integer replyCmd;
	private int[] requestData;

	/**
	 * @param key
	 *            command key
	 * @param requestCmd
	 *            command as byte value
	 * @param replyCmd
	 *            reply command as byte value
	 * @param requestData
	 *            request byte values
	 */
	public ComfoAirCommand(String key, Integer requestCmd, Integer replyCmd, int[] requestData) {
		this.keys = new ArrayList<String>();
		this.keys.add(key);
		this.requestCmd = requestCmd;
		this.requestData = requestData;
		this.replyCmd = replyCmd;
	}

	/**
	 * @param key
	 *            additional command key
	 */
	public void addKey(String key) {
		keys.add(key);
	}

	/**
	 * @return command keys
	 */
	public List<String> getKeys() {
		return keys;
	}

	/**
	 * @return command byte value
	 */
	public Integer getRequestCmd() {
		return requestCmd;
	}

	/**
	 * @return request data as byte values
	 */
	public int[] getRequestData() {
		return requestData;
	}

	/**
	 * @return acknowledge cmd byte value
	 */
	public Integer getReplyCmd() {
		return replyCmd;
	}
}
