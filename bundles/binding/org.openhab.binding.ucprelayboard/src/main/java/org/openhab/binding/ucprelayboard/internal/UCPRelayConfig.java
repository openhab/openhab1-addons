/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

/**
 * Configuration of a Switch
 * 
 * @author Robert Michalak
 */
package org.openhab.binding.ucprelayboard.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.library.types.OnOffType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author Robert Michalak
 * @since 1.8.0
 */
public class UCPRelayConfig implements BindingConfig {
	private String boardName;
	private int relayNumber;
	private boolean inverted = false;
	
	public UCPRelayConfig(String boardName, int relayNumber) {
		this(boardName, relayNumber, false);
	}
	
	public UCPRelayConfig(String boardName, int relayNumber, boolean inverted) {
		this.boardName = boardName;
		this.relayNumber = relayNumber;
		this.inverted = inverted;
	}

	public String getBoardName() {
		return boardName;
	}
		
	public int getRelayNumber() {
		return relayNumber;
	}
		
	public OnOffType translateCommand(OnOffType command) {
		if (inverted) {
			return OnOffType.values()[1 - command.ordinal()];
		} else {
			return command;
		}
	}
	
	public static UCPRelayConfig fromString(final String config) throws BindingConfigParseException {
		String boardName = null;
		int relayNumber = -1;
		boolean inverted = false;
		final String[] params = StringUtils.split(config, ";");
		for (String param : params) {
			final String[] nameValue = StringUtils.split(param, "=");
			if (nameValue.length != 2) {
				continue;
			}
			if ("board".equals(nameValue[0])) {
				boardName = nameValue[1];
			} else if ("relay".equals(nameValue[0])) {
				try {
					relayNumber = Integer.parseInt(nameValue[1]);
				} catch (NumberFormatException e) {
					throw new BindingConfigParseException("Wrong item configuration. " + e.getMessage());
				}
			} else if ("inverted".equals(nameValue[0])) {
				inverted = Boolean.valueOf(nameValue[1]);
			}
		}
		if (boardName == null && relayNumber < 0) {
			throw new BindingConfigParseException("Wrong item configuration. missing 'board' or 'relay' values");
		}
		return new UCPRelayConfig(boardName, relayNumber, inverted);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boardName == null) ? 0 : boardName.hashCode());
		result = prime * result + relayNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UCPRelayConfig other = (UCPRelayConfig) obj;
		if (boardName == null) {
			if (other.boardName != null)
				return false;
		} else if (!boardName.equals(other.boardName))
			return false;
		if (relayNumber != other.relayNumber)
			return false;
		return true;
	}
	
	
}
