/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
