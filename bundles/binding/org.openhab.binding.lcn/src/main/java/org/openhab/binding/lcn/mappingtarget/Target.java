/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.mappingtarget;

import java.util.LinkedList;

import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.input.Input;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for all targets.
 * Targets are bound to openHAB item mappings ({@link LcnBindingConfig.Mapping}) and
 * represent LCN related actions and/or visualizations.
 * 
 * @author Tobias Jüttner
 */
public abstract class Target implements Input.VisualizationVisitor {
	
	/** Logger for this class. */
	protected static final Logger logger = LoggerFactory.getLogger(Target.class);
	
	/** Interface for parsers. */
	private interface Parser {
		
		/**
		 * Tries to parse the given input.
		 * 
		 * @param input the configuration input to parse from
		 * @return the parsed {@link Target} (never null)
		 */
		Target tryParseTarget(String input);
		
	}
	
	/** Registered parsers to create targets from text input. */
	private static final LinkedList<Parser> targetParsers = new LinkedList<Parser>(); 
	
	/** Initializes static data once this class is first used. */
	static  {
		// Register parsers
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return OutputVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return OutputDimAbs.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return OutputDimRel.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return OutputToggle.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return RelayVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return Relays.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return BinSensorVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return VarVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return VarAbs.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return VarRel.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return VarReset.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LedVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return Led.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LogicOpVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return SendKeys.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return SendKeysHitDeferred.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LockedKeyVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LockKeys.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LockKeysTemporary.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LockedRegulatorVisualize.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return LockRegulator.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return DynText.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return PlainPck.tryParseTarget(input); }
		});
		targetParsers.add(new Parser() {
			public Target tryParseTarget(String input) { return new Unknown(input); }  // Will "catch" everything
		});
	}
	
	/**
	 * Parses the given input into a target.
	 * Always returns an object.
	 * 
	 * @param input the target's text representation
	 * @return the parsed {@link Target} (never null)
	 */
	public static Target parse(String input) {
		for (Parser p : targetParsers) {
			Target command = p.tryParseTarget(input);
			if (command != null) {
				return command;
			}
		}
		throw new Error();
	}
	
	/**
	 * Sends the PCK command(s) to the given connection.
	 *  
	 * @param conn the target connection
	 * @param item the source item sending this command
	 * @param cmd the openHAB command to send
	 */
	public abstract void send(Connection conn, Item item, Command cmd);
	
	/**
	 * Registers required status values with the given connection.
	 * 
	 * @param conn the connection
	 */
	public abstract void register(Connection conn);
	
}
