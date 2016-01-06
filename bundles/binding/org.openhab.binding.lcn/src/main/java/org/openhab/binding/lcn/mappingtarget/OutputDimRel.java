/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.mappingtarget;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.PckGenerator;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.input.ModStatusBinSensors;
import org.openhab.binding.lcn.input.ModStatusKeyLocks;
import org.openhab.binding.lcn.input.ModStatusLedsAndLogicOps;
import org.openhab.binding.lcn.input.ModStatusOutput;
import org.openhab.binding.lcn.input.ModStatusRelays;
import org.openhab.binding.lcn.input.ModStatusVar;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;

/**
 * Changes the value of an output-port relatively.
 * 
 * @author Tobias Jüttner
 */
public class OutputDimRel extends TargetWithLcnAddr {
	
	/** Pattern to parse relative output-port commands. */ 
	private static final Pattern PATTERN_REL =
		Pattern.compile("(?<outputId>[1234])\\.(?<value>-?\\d+(,\\d+)?)%",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target output-port (0..3). */
	private final int outputId;
	
	/** The target value (-100..100). */
	private final double percent;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param outputId 0..3
	 * @param percent the relative value (-100..100)
	 */
	OutputDimRel(LcnAddr addr, int outputId, double percent) {
		super(addr);
		this.outputId = outputId;
		this.percent = percent;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link OutputDimRel} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			try  {
				Matcher matcher;				
				switch (header.getCmd().toUpperCase()) {
					case "REL":
					case "ADD":  // Same as "REL"
					case "SUB":  // "REL" inverted 
						if ((matcher = PATTERN_REL.matcher(header.getRestInput())).matches()) {
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							if (header.getCmd().equalsIgnoreCase("SUB")) {
								value = -value;
							}
							return new OutputDimRel(header.getAddr(), Integer.parseInt(matcher.group("outputId")) - 1, value);
						}
						break;
				}
			} catch (ParseException ex) { }
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		boolean ack = !item.getAcceptedDataTypes().contains(PercentType.class) && !this.addr.isGroup();  // No ack. for dimmers (produces many of them)
		conn.queue(this.addr, ack, PckGenerator.relOutput(this.outputId, this.percent));
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) { }
	
	/** {@inheritDoc} */
	@Override	
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationBinSensorsStatus(ModStatusBinSensors pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
