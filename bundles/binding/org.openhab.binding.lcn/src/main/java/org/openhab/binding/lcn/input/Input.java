/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.input;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.openhab.binding.lcn.connection.Connection;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Parent class for all input data read from LCN-PCHK.
 * 
 * @author Tobias Jüttner
 */
public abstract class Input {
	
	/** Visitor pattern: Implemented by all concrete types related to visualization. */
	public interface VisualizationVisitor {
		
		/**
		 * Called to check for visualization of output-port status input.
		 * 
		 * @param input the input
		 * @param cmd the current openHAB command to check for visualization
		 * @param item the current openHAB item to check for visualization
		 * @param eventPublisher the {@link EventPublisher} that will will receive updates
		 * @return true to indicate visualization was successful
		 */
		boolean visualizationHandleOutputStatus(ModStatusOutput input, Command cmd, Item item, EventPublisher eventPublisher);

		/**
		 * Called to check for visualization of relays status input.
		 * 
		 * @param input the input
		 * @param cmd the current openHAB command to check for visualization
		 * @param item the current openHAB item to check for visualization
		 * @param eventPublisher the {@link EventPublisher} that will will receive updates
		 * @return true to indicate visualization was successful
		 */
		boolean visualizationHandleRelaysStatus(ModStatusRelays input, Command cmd, Item item, EventPublisher eventPublisher);

		/**
		 * Called to check for visualization of binary-sensors status input.
		 * 
		 * @param input the input
		 * @param cmd the current openHAB command to check for visualization
		 * @param item the current openHAB item to check for visualization
		 * @param eventPublisher the {@link EventPublisher} that will will receive updates
		 * @return true to indicate visualization was successful
		 */
		boolean visualizationBinSensorsStatus(ModStatusBinSensors input, Command cmd, Item item, EventPublisher eventPublisher);
		
		/**
		 * Called to check for visualization of variable status input.
		 * 
		 * @param input the input
		 * @param cmd the current openHAB command to check for visualization
		 * @param item the current openHAB item to check for visualization
		 * @param eventPublisher the {@link EventPublisher} that will will receive updates
		 * @return true to indicate visualization was successful
		 */
		boolean visualizationVarStatus(ModStatusVar input, Command cmd, Item item, EventPublisher eventPublisher);
		
		/**
		 * Called to check for visualization of LEDs and logic-operations status input.
		 * 
		 * @param input the input
		 * @param cmd the current openHAB command to check for visualization
		 * @param item the current openHAB item to check for visualization
		 * @param eventPublisher the {@link EventPublisher} that will will receive updates
		 * @return true to indicate visualization was successful
		 */
		boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps input, Command cmd, Item item, EventPublisher eventPublisher);
		
		/**
		 * Called to check for visualization of key-locks status input.
		 * 
		 * @param input the input
		 * @param cmd the current openHAB command to check for visualization
		 * @param item the current openHAB item to check for visualization
		 * @param eventPublisher the {@link EventPublisher} that will will receive updates
		 * @return true to indicate visualization was successful
		 */
		boolean visualizationKeyLocksStatus(ModStatusKeyLocks input, Command cmd, Item item, EventPublisher eventPublisher);
		
	}
	
	/** Interface for parsers. */
	private interface Parser {
		
		/**
		 * Tries to parse the given input text.
		 * Will return a list of parsed {@link Input}s. The list might be empty (but not null).
		 * 
		 * @param input the input data received from LCN-PCHK
		 * @return the parsed {@link Input}s (never null)
		 */
		Collection<Input> tryParseInput(String input);
		
	}
	
	/**
	 * Registered parsers.
	 * Will be used to create {@link Input} objects from read input.
	 */
	private static final LinkedList<Parser> inputParsers = new LinkedList<Parser>(); 
	
	/** Initializes static data once this class is first used. */
	static  {
		// Register parsers
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return AuthUsername.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return AuthPassword.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return AuthOk.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return LcnConnState.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModAck.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModSk.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModSn.tryParseInput(input); }
		});		
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModStatusOutput.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModStatusRelays.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModStatusBinSensors.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModStatusVar.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModStatusLedsAndLogicOps.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return ModStatusKeyLocks.tryParseInput(input); }
		});
		inputParsers.add(new Parser() {
			public Collection<Input> tryParseInput(String input) { return Arrays.asList((Input)new Unknown(input)); }  // Will "catch" everything
		});
	}
	
	/**
	 * Parses the given input (received from LCN-PCHK).
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link Input} list. Will always contain at least one element
	 */
	public static Collection<Input> parse(String input) {
		for (Parser p : inputParsers) {
			Collection<Input> ret = p.tryParseInput(input);
			if (!ret.isEmpty()) {
				return ret;
			}
		}
		throw new Error();
	}
	
	/**
	 * Applies data to the given connection.
	 *  
	 * @param conn the connection which belongs to this {@link Input}
	 */
	public abstract void process(Connection conn);
	
	/**
	 * Visitor pattern.
	 * Will call the concrete type's {@link VisualizationVisitor} method to visualize the given item.
	 *  
	 * @param handler the visitor handler
	 * @param conn the connection which belongs to this {@link Input}
	 * @param cmd the current openHAB command to check for visualization
	 * @param item the current openHAB item to check for visualization
	 * @param eventPublisher the {@link EventPublisher} that will will receive updates
	 * @return true to indicate visualization was successful
	 */
	public abstract boolean tryVisualization(VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher);
	
}
