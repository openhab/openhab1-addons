package org.openhab.binding.lcn.mappingtarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.LcnDefs;
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
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;

/**
 * Visualizes the lock-state of a regulator.
 * 
 * @author Tobias Jüttner
 */
public class LockedRegulatorVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse regulator-lock visualizations. */
	private static final Pattern PATTERN_LOCK_STATE =
		Pattern.compile("(?<regId>[12])",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0..1 */
	private final int regId;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param regId 0..1
	 */
	LockedRegulatorVisualize(LcnAddrMod addr, int regId) {
		super(addr);
		this.regId = regId;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link LockedRegulatorVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;				
			switch (header.getCmd().toUpperCase()) {
				case "REGULATOR_LOCK_STATE":
					if ((matcher = PATTERN_LOCK_STATE.matcher(header.getRestInput())).matches()) {
						return new LockedRegulatorVisualize((LcnAddrMod)header.getAddr(), Integer.parseInt(matcher.group("regId")) - 1);
					}
					break;
			}
		}
		return null;
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) { }
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		long currTime = System.nanoTime();
		ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
		if (!info.requestSwAge.isActive()) {
			info.requestSwAge.nextRequestIn(0, currTime);  // Firmware version is required
		}
		LcnDefs.Var var = this.regId == 0 ? LcnDefs.Var.R1VARSETPOINT : LcnDefs.Var.R2VARSETPOINT;
		if (info.requestStatusVars.containsKey(var) && !info.requestStatusVars.get(var).isActive()) {
			info.requestStatusVars.get(var).nextRequestIn(0, currTime);
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
	
	/**
	 * Visualization for {@link OpenClosedType} and {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		LcnDefs.Var var = this.regId == 0 ? LcnDefs.Var.R1VARSETPOINT : LcnDefs.Var.R2VARSETPOINT;
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && pchkInput.getVar() == var) {
			if (item.getAcceptedDataTypes().contains(OpenClosedType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getValue().isLockedRegulator() ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getValue().isLockedRegulator() ? OnOffType.ON : OnOffType.OFF);
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
