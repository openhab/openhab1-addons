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
import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.PckGenerator;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ModInfo;
import org.openhab.binding.lcn.input.ModStatusBinSensors;
import org.openhab.binding.lcn.input.ModStatusKeyLocks;
import org.openhab.binding.lcn.input.ModStatusLedsAndLogicOps;
import org.openhab.binding.lcn.input.ModStatusOutput;
import org.openhab.binding.lcn.input.ModStatusRelays;
import org.openhab.binding.lcn.input.ModStatusVar;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Controls output-ports.
 * Can also visualize single output-ports.
 * 
 * @author Tobias Jüttner
 */
public class OutputDimAbs extends TargetWithLcnAddr {
	
	/** Pattern to parse ON and OFF commands (shortcuts for DIM 0 and DIM 100). */
	private static final Pattern PATTERN_ONOFF =
		Pattern.compile("(?<outputId>[1234]|(ALL))(\\.(?<ramp>\\d+(,\\d+)?(?<timeUnit>.+)))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);	
	
	/** Pattern to parse DIM commands. */
	private static final Pattern PATTERN_DIM =
		Pattern.compile("(?<outputId>[1234]|(ALL))\\.(?<value>\\d+(,\\d+)?)%(\\.(?<ramp>\\d+(,\\d+)?(?<timeUnit>.+)))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse DIM commands with i%. */
	private static final Pattern PATTERN_DIMI =
		Pattern.compile("(?<outputId>[1234]|(ALL))\\.%i(\\.(?<ramp>\\d+(,\\d+)?(?<timeUnit>.+)))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target output-port (0..3) or -1 (all, no visualization). */
	private final int outputId;
	
	/** The target value (0..100) or -1 if %i will be used. */
	private final double percent;
	
	/** The ramp to use. */
	private final int rampMSec;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param outputId 0..3
	 * @param percent the absolute value (0..100) or -1 if %i will be used
	 * @param rampMSec the ramp in milliseconds
	 */
	OutputDimAbs(LcnAddr addr, int outputId, double percent, int rampMSec) {
		super(addr);
		this.outputId = outputId;
		this.percent = percent;
		this.rampMSec = rampMSec;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link OutputDimAbs} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			try  {
				Matcher matcher;				
				switch (header.getCmd().toUpperCase()) {
					case "ON": case "OFF":
						if ((matcher = PATTERN_ONOFF.matcher(header.getRestInput())).matches()) {
							int outputId = matcher.group("outputId").equalsIgnoreCase("ALL") ? -1 :
								Integer.parseInt(matcher.group("outputId")) - 1;
							double value = header.getCmd().equalsIgnoreCase("ON") ? 100 : 0;
							double ramp = 0;
							if (matcher.group("ramp") != null) {  // Optional
								if (LcnDefs.TimeUnit.parse(matcher.group("timeUnit")) != LcnDefs.TimeUnit.SECONDS) {
									throw new IllegalArgumentException();
								}
								ramp = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("ramp")).doubleValue();
							}
							return new OutputDimAbs(header.getAddr(), outputId, value, (int)(ramp * 1000));
						}
						break;
					case "DIM":
						if ((matcher = PATTERN_DIM.matcher(header.getRestInput())).matches()) {
							int outputId = matcher.group("outputId").equalsIgnoreCase("ALL") ? -1 :
								Integer.parseInt(matcher.group("outputId")) - 1;
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							double ramp = 0;
							if (matcher.group("ramp") != null) {  // Optional
								if (LcnDefs.TimeUnit.parse(matcher.group("timeUnit")) != LcnDefs.TimeUnit.SECONDS) {
									throw new IllegalArgumentException();
								}
								ramp = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("ramp")).doubleValue();
							}
							return new OutputDimAbs(header.getAddr(), outputId, value, (int)(ramp * 1000));
						}
						else if ((matcher = PATTERN_DIMI.matcher(header.getRestInput())).matches()) {
							int outputId = matcher.group("outputId").equalsIgnoreCase("ALL") ? -1 :
								Integer.parseInt(matcher.group("outputId")) - 1;
							double ramp = 0;
							if (matcher.group("ramp") != null) {  // Optional
								if (LcnDefs.TimeUnit.parse(matcher.group("timeUnit")) != LcnDefs.TimeUnit.SECONDS) {
									throw new IllegalArgumentException();
								}
								ramp = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("ramp")).doubleValue();
							}
							return new OutputDimAbs(header.getAddr(), outputId, -1, (int)(ramp * 1000));
						}
						break;
				}
			} catch (ParseException ex) {
			} catch (IllegalArgumentException ex) { }
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		double value = this.percent;
		if (value == -1 && cmd instanceof PercentType) {
			value = ((PercentType)cmd).doubleValue();
		}
		if (value >= 0 && value <= 100) {
			if (this.outputId == -1) {  // All
				boolean is1805 = false;  // Default
				if (!this.addr.isGroup()) {
					ModInfo info = conn.getModInfo((LcnAddrMod)this.addr);
					if (info != null) {
						is1805 = info.getSwAge() >= 0x180501;
					}
				}
				conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.dimAllOutputs(value, LcnDefs.timeToRampValue(this.rampMSec), is1805));
			}
			else {  // Single
				conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.dimOutput(this.outputId, value, LcnDefs.timeToRampValue(this.rampMSec)));
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		if (!this.addr.isGroup()) {
			long currTime = System.nanoTime();
			ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
			if (this.outputId == -1) {  // All
				if (!info.requestSwAge.isActive()) {
					info.requestSwAge.nextRequestIn(0, currTime);  // Firmware version is required
				}
			}
			else {  // Single: Allow visualization
				if (!info.requestStatusOutputs.get(this.outputId).isActive()) {
					info.requestStatusOutputs.get(this.outputId).nextRequestIn(0, currTime);
				}
			}
		}
	}
	
	/**
	 * Visualization for {@link PercentType} and {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		// We are actually not meant to visualize anything.
		// But (just in case) someone is really lazy in doing the item-definitions, we try to be helpful by implementing some ON/OFF and DIM logic.
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && pchkInput.getOutputId() == this.outputId) {
			if (this.percent == -1 && item.getAcceptedDataTypes().contains(PercentType.class)) {
				eventPublisher.postUpdate(item.getName(), new PercentType((int)Math.round(pchkInput.getPercent())));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(OnOffType.class)) { 
				State reportedState = pchkInput.getPercent() != 0 ? OnOffType.ON : OnOffType.OFF;
				// Only update if the state we are bound to is equal to the reported one
				if (cmd == reportedState) {
					eventPublisher.postUpdate(item.getName(), reportedState);
					return true;
				}
			}			
		}
		return false;
	}
	
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
