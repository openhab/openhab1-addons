package org.openhab.binding.lcn.mappingtarget;

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
import org.openhab.core.types.Command;

/**
 * Resets a variable to 0.
 * 
 * @author Tobias Jüttner
 */
public class VarReset extends TargetWithLcnAddr {
	
	/** Pattern to parse variable reset commands. */
	private static final Pattern PATTERN_VAR_RESET =
		Pattern.compile("(?<varId>\\d+)",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse set-point reset commands. */
	private static final Pattern PATTERN_SETPOINT_RESET =
		Pattern.compile("(?<regId>[12])",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target LCN variable. */
	private final LcnDefs.Var var;
	
	/** Forces the old commands before 170206. Required if target is an LCN group. */
	private final boolean forceOld;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param targetLcnType the reference-point
	 */
	VarReset(LcnAddr addr, LcnDefs.Var var, boolean forceOld) {
		super(addr);
		this.var = var;
		this.forceOld = forceOld;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link VarReset} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			Matcher matcher;				
			switch (header.getCmd().toUpperCase()) {
				case "VAR_RESET":
				case "VAR_RESET_OLD":
					if ((matcher = PATTERN_VAR_RESET.matcher(header.getRestInput())).matches()) {
						return new VarReset(header.getAddr(), LcnDefs.Var.varIdToVar(Integer.parseInt(matcher.group("varId")) - 1),
							header.getCmd().toUpperCase().endsWith("_OLD"));
					}
					break;
				case "SETPOINT_RESET":
					if ((matcher = PATTERN_SETPOINT_RESET.matcher(header.getRestInput())).matches()) {
						return new VarReset(header.getAddr(), LcnDefs.Var.setPointIdToVar(Integer.parseInt(matcher.group("regId")) - 1),
							header.getCmd().toUpperCase().endsWith("_OLD"));
					}
					break;
			}
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		try {
			boolean is2013 = !this.forceOld;
			ModInfo info = null;
			if (!this.addr.isGroup()) {
				info = conn.getModInfo((LcnAddrMod)this.addr);
				if (info != null) {
					is2013 = info.getSwAge() >= 0x170206;
				}
			}
			conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.varReset(this.var, is2013));
			// Force a status update
			if (info != null && LcnDefs.Var.shouldPollStatusAfterCommand(this.var, is2013) && info.requestStatusVars.containsKey(this.var)) {
				info.requestStatusVars.get(this.var).nextRequestIn(ModInfo.STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC, System.nanoTime());
			}
		}
		catch (IllegalArgumentException ex) {
			logger.warn(String.format("Variable of type %s does not support \"reset to 0\" commands.", this.var));
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		if (!this.addr.isGroup()) {
			long currTime = System.nanoTime();
			ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
			if (!info.requestSwAge.isActive()) {
				info.requestSwAge.nextRequestIn(0, currTime);  // Firmware version is required
			}
			if (info.requestStatusVars.containsKey(this.var) && !info.requestStatusVars.get(this.var).isActive()) {
				info.requestStatusVars.get(this.var).nextRequestIn(0, currTime);
			}
		}
	}
	
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
