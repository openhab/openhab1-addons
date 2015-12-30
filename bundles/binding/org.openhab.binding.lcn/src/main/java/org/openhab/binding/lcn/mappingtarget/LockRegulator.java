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
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Locks / unlocks a regulator.
 * Can also visualize the lock-state. 
 * 
 * @author Tobias Jüttner
 */
public class LockRegulator extends TargetWithLcnAddr {
	
	/** Pattern to parse regulator-lock/unlock commands. */ 
	private static final Pattern PATTERN_REGULATOR_LOCK_UNLOCK =
		Pattern.compile("(?<regId>[12])",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0..1 */
	private final int regId;
	
	/** Target lock state. */
	private final boolean state;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param regId 0..1
	 * @param state the lock state
	 */
	LockRegulator(LcnAddr addr, int regId, boolean state) {
		super(addr);
		this.regId = regId;
		this.state = state;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link LockRegulator} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "LOCK_REGULATOR":
				case "UNLOCK_REGULATOR":
					if ((matcher = PATTERN_REGULATOR_LOCK_UNLOCK.matcher(header.getRestInput())).matches()) {
						return new LockRegulator(header.getAddr(), Integer.parseInt(matcher.group("regId")) - 1,
							header.getCmd().equalsIgnoreCase("LOCK_REGULATOR"));
					}
					break;
			}
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.lockRegulator(this.regId, this.state));
		// Force a status update
		if (!this.addr.isGroup() && !this.state) {
			LcnDefs.Var var = this.regId == 0 ? LcnDefs.Var.R1VARSETPOINT : LcnDefs.Var.R2VARSETPOINT;
			ModInfo info = conn.getModInfo((LcnAddrMod)this.addr);
			if (info != null && LcnDefs.Var.shouldPollStatusAfterRegulatorLock(info.getSwAge(), this.state) && info.requestStatusVars.containsKey(var)) {
				info.requestStatusVars.get(var).nextRequestIn(ModInfo.STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC, System.nanoTime());
			}
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
			LcnDefs.Var var = this.regId == 0 ? LcnDefs.Var.R1VARSETPOINT : LcnDefs.Var.R2VARSETPOINT;
			if (info.requestStatusVars.containsKey(var) && !info.requestStatusVars.get(var).isActive()) {
				info.requestStatusVars.get(var).nextRequestIn(0, currTime);
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
	
	/**
	 * Visualization for {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		// We are actually not meant to visualize anything.
		// But (just in case) someone is really lazy in doing the item-definitions, we try to be helpful by implementing some ON/OFF logic.
		LcnDefs.Var var = this.regId == 0 ? LcnDefs.Var.R1VARSETPOINT : LcnDefs.Var.R2VARSETPOINT;
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && pchkInput.getVar() == var &&
			item.getAcceptedDataTypes().contains(OnOffType.class)) {
			State reportedState = pchkInput.getValue().isLockedRegulator() ? OnOffType.ON : OnOffType.OFF;
			// Only update if the state we are bound to is equal to the reported one
			if (cmd == reportedState) {
				eventPublisher.postUpdate(item.getName(), reportedState);
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
