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
 * Locks keys of a table.
 * Can also visualize single key-states.
 * 
 * @author Tobias Jüttner
 */
public class LockKeys extends TargetWithLcnAddr {
	
	/** Pattern to parse key-lock commands. */ 
	private static final Pattern PATTERN_LOCK_KEYS =
		Pattern.compile("(?<table>[ABCD])\\.(?<states>[10T-]{8})",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0(A)..3(D) */
	private final int tableId;
	
	/** Target key-lock states. */
	private final LcnDefs.KeyLockStateModifier[] states;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param tableId 0..3
	 * @param states the 8 key-lock modifiers
	 */
	LockKeys(LcnAddr addr, int tableId, LcnDefs.KeyLockStateModifier[] states) {
		super(addr);
		this.tableId = tableId;
		this.states = states;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link LockKeys} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "LOCK":
					if ((matcher = PATTERN_LOCK_KEYS.matcher(header.getRestInput())).matches()) {
						LcnDefs.KeyLockStateModifier[] states = new LcnDefs.KeyLockStateModifier[8];
						int tableId;
						switch (matcher.group("table").toUpperCase()){
							case "A": tableId = 0; break;
							case "B": tableId = 1; break;
							case "C": tableId = 2; break;
							case "D": tableId = 3; break;
							default: throw new Error();
						}
						for (int i = 0; i < 8; ++i) {
							switch (matcher.group("states").toUpperCase().charAt(i)) {
								case '1': states[i] = LcnDefs.KeyLockStateModifier.ON; break;
								case '0': states[i] = LcnDefs.KeyLockStateModifier.OFF; break;
								case 'T': states[i] = LcnDefs.KeyLockStateModifier.TOGGLE; break;
								case '-': states[i] = LcnDefs.KeyLockStateModifier.NOCHANGE; break;
								default: throw new Error();
							}
						}
						return new LockKeys(header.getAddr(), tableId, states);
					}
					break;
			}
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.lockKeys(this.tableId, this.states));
		// Force status update (status is polled and should be updated now)
		if (!this.addr.isGroup()) {
			ModInfo info = conn.getModInfo((LcnAddrMod)this.addr);
			if (info != null) {
				info.requestStatusLockedKeys.nextRequestIn(ModInfo.STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC, System.nanoTime());
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		if (!this.addr.isGroup()) {
			ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
			if (!info.requestStatusLockedKeys.isActive()) {
				info.requestStatusLockedKeys.nextRequestIn(0, System.nanoTime());
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
	
	/**
	 * Visualization of {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		// We are actually not meant to visualize anything.
		// But (just in case) someone is really lazy in doing the item-definitions, we try to be helpful by implementing some ON/OFF logic.
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && item.getAcceptedDataTypes().contains(OnOffType.class)) {
			for (int i = 0; i < 8; ++i) {
				if (this.states[i] == LcnDefs.KeyLockStateModifier.ON || this.states[i] == LcnDefs.KeyLockStateModifier.OFF) {
					State reportedState = pchkInput.getState(this.tableId, i) ? OnOffType.ON : OnOffType.OFF;
					// Only update if the state we are bound to is equal to the reported one.
					if ((this.states[i] == LcnDefs.KeyLockStateModifier.ON ? OnOffType.ON : OnOffType.OFF) == reportedState) {
						eventPublisher.postUpdate(item.getName(), reportedState);
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
	
}
