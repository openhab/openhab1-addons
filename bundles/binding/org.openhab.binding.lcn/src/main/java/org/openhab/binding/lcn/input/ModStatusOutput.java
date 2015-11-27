/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.input;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;

import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.PckParser;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ModInfo;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Status of an output-port received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModStatusOutput extends Mod {
	
	/** The output-port number (0..3). */
	private final int outputId;
	
	/** The output-port value in percent. */
	private final double percent;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param outputId 0..3
	 * @param percent the value in percent
	 */
	ModStatusOutput(LcnAddrMod physicalSourceAddr, int outputId, double percent) {
		super(physicalSourceAddr);
		this.outputId = outputId;
		this.percent = percent;
	}
	
	/**
	 * Gets the output-port id.
	 * 
	 * @return 0..3
	 */
	public int getOutputId() {
		return this.outputId;
	}
	
	/**
	 * Gets the value in percent.
	 * 
	 * @return the value
	 */
	public double getPercent() {
		return this.percent;
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModStatusOutput} (might be empty, but not null}
	 */	
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		Matcher matcher;
		if ((matcher = PckParser.PATTERN_STATUS_OUTPUT_PERCENT.matcher(input)).matches()) {
			ret.add(new ModStatusOutput(
				new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
				Integer.parseInt(matcher.group("outputId")) - 1,
				(double)Integer.parseInt(matcher.group("percent"))));
		}
		else if ((matcher = PckParser.PATTERN_STATUS_OUTPUT_NATIVE.matcher(input)).matches()) {
			ret.add(new ModStatusOutput(
				new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
				Integer.parseInt(matcher.group("outputId")) - 1,
				(double)Integer.parseInt(matcher.group("value")) / 2));
		}
		return ret;
	}
	
	/**
	 * Notifies the connection about the received output-port status. 
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		ModInfo info = conn.getModInfo(this.logicalSourceAddr);
		if (info != null) {
			info.requestStatusOutputs.get(this.outputId).onResponseReceived();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		return handler.visualizationHandleOutputStatus(this, cmd, item, eventPublisher);
	}
	
}
