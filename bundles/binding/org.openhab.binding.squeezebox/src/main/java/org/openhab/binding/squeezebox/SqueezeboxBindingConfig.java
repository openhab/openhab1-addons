package org.openhab.binding.squeezebox;

import org.openhab.binding.squeezebox.internal.CommandType;
import org.openhab.core.binding.BindingConfig;

public class SqueezeboxBindingConfig implements BindingConfig {
	private final String playerId;
	private final CommandType commandType;
	private final String extra;
	
	public SqueezeboxBindingConfig(String playerId, CommandType commandType, String extra) {
		this.playerId = playerId;
		this.commandType = commandType;
		this.extra = extra;
	}
	
	public String getPlayerId() {
		return playerId;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}
	
	public String getExtra() {
		return extra;
	}
}
