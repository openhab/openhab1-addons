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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.comfoair.datatypes.ComfoAirDataType;
import org.openhab.binding.comfoair.datatypes.DataTypeBoolean;
import org.openhab.binding.comfoair.datatypes.DataTypeMessage;
import org.openhab.binding.comfoair.datatypes.DataTypeNumber;
import org.openhab.binding.comfoair.datatypes.DataTypeTemperature;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public enum ComfoAirCommandType {

	ACTIVATE {
		{
			key = "activate";
			data_type = DataTypeBoolean.class;
			possible_values = new int[] { 0x03 };
			change_command = 0x9b;
			change_data_size = 1;
			change_data_pos = 0;
			read_command = 0x9c;
			read_reply_command = 0x9c;
			read_reply_data_pos = new int[] { 0 };
			read_reply_data_bits = 0x03;
		}
	},

	FAN_LEVEL {
		{
			key = "fan_level";
			data_type = DataTypeNumber.class;
			possible_values = new int[] { 0x01, 0x02, 0x03, 0x04 };
			change_command = 0x99;
			change_data_size = 1;
			change_data_pos = 0;
			change_affected = new String[] { "auto_mode", "incomming_fan",
					"outgoing_fan" };
			read_command = 0xcd;
			read_reply_command = 0xce;
			read_reply_data_pos = new int[] { 8 };
		}
	},

	INCOMMING_FAN {
		{
			key = "incomming_fan";
			data_type = DataTypeNumber.class;
			read_command = 0x0b;
			read_reply_command = 0x0c;
			read_reply_data_pos = new int[] { 0 };
		}
	},

	OUTGOING_FAN {
		{
			key = "outgoing_fan";
			data_type = DataTypeNumber.class;
			read_command = 0x0b;
			read_reply_command = 0x0c;
			read_reply_data_pos = new int[] { 1 };
		}
	},

	TARGET_TEMPERATUR {
		{
			key = "target_temperatur";
			data_type = DataTypeTemperature.class;
			change_command = 0xd3;
			change_data_size = 1;
			change_data_pos = 0;
			read_command = 0xd1;
			read_reply_command = 0xd2;
			read_reply_data_pos = new int[] { 0 };
		}
	},

	OUTDOOR_INCOMMING_TEMPERATUR {
		{
			key = "outdoor_incomming_temperatur";
			data_type = DataTypeTemperature.class;
			read_command = 0xd1;
			read_reply_command = 0xd2;
			read_reply_data_pos = new int[] { 1 };
		}
	},

	OUTDOOR_OUTGOING_TEMPERATUR {
		{
			key = "outdoor_outgoing_temperatur";
			data_type = DataTypeTemperature.class;
			read_command = 0xd1;
			read_reply_command = 0xd2;
			read_reply_data_pos = new int[] { 4 };
		}
	},

	INDOOR_INCOMMING_TEMPERATUR {
		{
			key = "indoor_incomming_temperatur";
			data_type = DataTypeTemperature.class;
			read_command = 0xd1;
			read_reply_command = 0xd2;
			read_reply_data_pos = new int[] { 2 };
		}
	},

	INDOOR_OUTGOING_TEMPERATUR {
		{
			key = "indoor_outgoing_temperatur";
			data_type = DataTypeTemperature.class;
			read_command = 0xd1;
			read_reply_command = 0xd2;
			read_reply_data_pos = new int[] { 3 };
		}
	},

	EWT_TEMPERATUR {
		{
			key = "ewt_temperatur";
			data_type = DataTypeTemperature.class;
			read_command = 0xd1;
			read_reply_command = 0xd2;
			read_reply_data_pos = new int[] { 6 };
		}
	},

	EWT_TEMPERATUR_HIGH {
		{
			key = "ewt_temperatur_high";
			data_type = DataTypeTemperature.class;
			read_command = 0xeb;
			read_reply_command = 0xec;
			read_reply_data_pos = new int[] { 0 };
		}
	},

	EWT_TEMPERATUR_LOW {
		{
			key = "ewt_temperatur_low";
			data_type = DataTypeTemperature.class;
			read_command = 0xeb;
			read_reply_command = 0xec;
			read_reply_data_pos = new int[] { 1 };
		}
	},

	EWT_SPEED {
		{
			key = "ewt_speed";
			data_type = DataTypeNumber.class;
			read_command = 0xeb;
			read_reply_command = 0xec;
			read_reply_data_pos = new int[] { 2 };
		}
	},

	BYPASS_MODE {
		{
			key = "bypass_mode";
			data_type = DataTypeBoolean.class;
			read_command = 0x37;
			read_reply_command = 0x3c;
			read_reply_data_pos = new int[] { 9 };
			read_reply_data_bits = 0x02;
		}
	},

	FILTER_RUNNING {
		{
			key = "filter_running";
			data_type = DataTypeNumber.class;
			read_command = 0xdd;
			read_reply_command = 0xde;
			read_reply_data_pos = new int[] { 15, 16 };
		}
	},

	FILTER_RESET {
		{
			key = "filter_reset";
			data_type = DataTypeBoolean.class;
			possible_values = new int[] { 0x01 };
			change_command = 0xdb;
			change_data_size = 4;
			change_data_pos = 3;
			change_affected = new String[] { "filter_error",
					"filter_error_intern", "filter_error_extern" };
		}
	},

	FILTER_ERROR {
		{
			key = "filter_error";
			data_type = DataTypeBoolean.class;
			read_command = 0xd9;
			read_reply_command = 0xda;
			read_reply_data_pos = new int[] { 8 };
			read_reply_data_bits = 0x01;
		}
	},

	FILTER_ERROR_INTERN {
		{
			key = "filter_error_intern";
			data_type = DataTypeBoolean.class;
			read_command = 0x37;
			read_reply_command = 0x3c;
			read_reply_data_pos = new int[] { 1 };
			read_reply_data_bits = 0x40;
		}
	},

	FILTER_ERROR_EXTERN {
		{
			key = "filter_error_extern";
			data_type = DataTypeBoolean.class;
			read_command = 0x37;
			read_reply_command = 0x3c;
			read_reply_data_pos = new int[] { 1 };
			read_reply_data_bits = 0x80;
		}
	},

	ERROR_RESET {
		{
			key = "error_reset";
			data_type = DataTypeBoolean.class;
			possible_values = new int[] { 0x01 };
			change_command = 0xdb;
			change_data_size = 4;
			change_data_pos = 0;
			change_affected = new String[] { "error_message" };
		}
	},

	ERROR_MESSAGE {
		{
			key = "error_message";
			data_type = DataTypeMessage.class;
			read_command = 0xd9;
			read_reply_command = 0xda;
			read_reply_data_pos = new int[] { 0, 1, 9, 13 };
		}
	};

	
	Logger logger = LoggerFactory.getLogger(ComfoAirCommandType.class);
	String key;
	Class<? extends ComfoAirDataType> data_type;

	/*
	 * Possible values
	 */
	int[] possible_values;

	/*
	 * Cmd code to swich properties on the comforair.
	 */
	int change_command;
	/*
	 * The size of the data block.
	 */
	int change_data_size;
	/*
	 * The byte inside the data block which holds the crucial value.
	 */
	int change_data_pos;
	/*
	 * Affected commands which should be refreshed after a successful change
	 * command call.
	 */
	String[] change_affected;

	/*
	 * Command for reading properties.
	 */
	int read_command;

	/*
	 * ACK Command which identifies the matching response.
	 */
	int read_reply_command;

	/*
	 * The byte position inside the response data.
	 */
	int[] read_reply_data_pos;

	/*
	 * Bit mask for boolean response properties to identify a true value.
	 */
	int read_reply_data_bits;

	/**
	 * @return command key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return data type for this command key
	 */
	public ComfoAirDataType getDataType() {
		try {
			return data_type.newInstance();
		} catch (Exception e) {
			logger.error("Creating new DataType went wrong", e);
		}
		return null;
	}

	/**
	 * @return possible byte values
	 */
	public int[] getPossibleValues() {
		return possible_values;
	}

	/**
	 * @return relevant byte position inside the response byte value array
	 */
	public int getChangeDataPos() {
		return change_data_pos;
	}

	/**
	 * @return generate a byte value sequence for the response stream
	 */
	public int[] getChangeDataTemplate() {
		int[] template = new int[change_data_size];
		for (int i = 0; i < template.length; i++) {
			template[i] = 0x00;
		}
		return template;
	}

	/**
	 * @return byte position inside the request byte value array
	 */
	public int[] getGetReplyDataPos() {
		return read_reply_data_pos;
	}

	/**
	 * @return bit mask for the response byte value
	 */
	public int getGetReplyDataBits() {
		return read_reply_data_bits;
	}

	/**
	 * Get a command to change properties on the comfoair.
	 * 
	 * @param key
	 *            command key
	 * @param value
	 *            new state
	 * @return initialized ComfoAirCommand
	 */
	public static ComfoAirCommand getChangeCommand(String key, State value) {
		ComfoAirCommandType commandType = ComfoAirCommandType.getCommandTypeByKey(key);
		ComfoAirDataType dataType = commandType.getDataType();
		int[] data = dataType.convertFromState(value, commandType);
		return new ComfoAirCommand(key, commandType.change_command, null, data);
	}

	/**
	 * Get all commands which should be refreshed after a successful change
	 * command.
	 * 
	 * @param key
	 *            command key
	 * @param usedKeys
	 * @return ComfoAirCommand's which should be updated after a modifying
	 *         ComfoAirCommand named by key
	 */
	public static Collection<ComfoAirCommand> getAffectedReadCommands(String key, Set<String> usedKeys) {

		Map<Integer, ComfoAirCommand> commands = new HashMap<Integer, ComfoAirCommand>();

		ComfoAirCommandType commandType = ComfoAirCommandType.getCommandTypeByKey(key);
		if (commandType.read_reply_command != 0) {
			Integer getCmd = commandType.read_command == 0 ? null : new Integer(commandType.read_command);
			Integer replyCmd = new Integer(commandType.read_reply_command);

			ComfoAirCommand command = new ComfoAirCommand(key, getCmd, replyCmd, null);
			commands.put(command.getReplyCmd(), command);
		}

		for (String affectedKey : commandType.change_affected) {
			// refresh affected event keys only when they are used
			if (!usedKeys.contains(affectedKey)) {
				continue;
			}

			ComfoAirCommandType affectedCommandType = ComfoAirCommandType.getCommandTypeByKey(affectedKey);

			Integer getCmd = affectedCommandType.read_command == 0 ? null : new Integer(affectedCommandType.read_command);
			Integer replyCmd = new Integer(affectedCommandType.read_reply_command);

			ComfoAirCommand command = commands.get(replyCmd);

			if (command == null) {
				command = new ComfoAirCommand(affectedKey, getCmd, replyCmd, null);
				commands.put(command.getReplyCmd(), command);
			} else {
				command.addKey(affectedKey);
			}
		}

		return commands.values();
	}

	/**
	 * Get all commands which receive informations to update items.
	 * 
	 * @return all ComfoAirCommand's identified by keys
	 */
	public static Collection<ComfoAirCommand> getReadCommandsByEventTypes( List<String> keys) {
		
		Map<Integer, ComfoAirCommand> commands = new HashMap<Integer, ComfoAirCommand>();
		for (ComfoAirCommandType entry : values()) {
			if (!keys.contains(entry.key)) {
				continue;
			}
			if (entry.read_reply_command == 0) {
				continue;
			}

			Integer getCmd = entry.read_command == 0 ? null : new Integer(entry.read_command);
			Integer replyCmd = new Integer(entry.read_reply_command);

			ComfoAirCommand command = commands.get(replyCmd);

			if (command == null) {
				command = new ComfoAirCommand(entry.key, getCmd, replyCmd, null);
				commands.put(command.getReplyCmd(), command);
			} else {
				command.addKey(entry.key);
			}
		}

		return commands.values();
	}

	/**
	 * Get commandtypes which matches the replyCmd.
	 * 
	 * @param replyCmd
	 *            reply command byte value
	 * @return ComfoAirCommandType identified by replyCmd
	 */
	public static List<ComfoAirCommandType> getCommandTypesByReplyCmd( int replyCmd) {
		List<ComfoAirCommandType> commands = new ArrayList<ComfoAirCommandType>();
		for (ComfoAirCommandType entry : values()) {
			if (entry.read_reply_command != replyCmd) {
				continue;
			}
			commands.add(entry);
		}
		return commands;
	}

	/**
	 * Get a specific command.
	 * 
	 * @param key
	 *            command key
	 * @return ComfoAirCommandType identified by key
	 */
	private static ComfoAirCommandType getCommandTypeByKey(String key) {
		for (ComfoAirCommandType entry : values()) {
			if (entry.key.equals(key)) {
				return entry;
			}
		}
		return null;
	}
	
}
