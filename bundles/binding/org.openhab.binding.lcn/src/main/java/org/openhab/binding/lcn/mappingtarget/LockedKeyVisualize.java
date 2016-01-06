package org.openhab.binding.lcn.mappingtarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddrMod;
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
 * Visualized the lock-state of a key.
 * 
 * @author Tobias Jüttner
 */
public class LockedKeyVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse key-lock visualizations. */
	private static final Pattern PATTERN_LOCK_STATE =
		Pattern.compile("(?<table>[ABCD])(?<keyId>[12345678])",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target's key table: 0(A)..3(D) */
	private final int tableId;
	
	/** The target key (0..7). */
	private final int keyId;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param tableId 0..3
	 * @param keyId 0..7
	 */
	LockedKeyVisualize(LcnAddrMod addr, int tableId, int keyId) {
		super(addr);
		this.tableId = tableId;
		this.keyId = keyId;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link LockedKeyVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;				
			switch (header.getCmd().toUpperCase()) {
				case "LOCK_STATE":
					if ((matcher = PATTERN_LOCK_STATE.matcher(header.getRestInput())).matches()) {
						int tableId;
						switch (matcher.group("table").toUpperCase()) {
							case "A": tableId = 0; break;
							case "B": tableId = 1; break;
							case "C": tableId = 2; break;
							case "D": tableId = 3; break;
							default: throw new Error();
						}
						return new LockedKeyVisualize((LcnAddrMod)header.getAddr(), tableId, Integer.parseInt(matcher.group("keyId")) - 1);
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
		ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
		if (!info.requestStatusLockedKeys.isActive()) {
			info.requestStatusLockedKeys.nextRequestIn(0, System.nanoTime());
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
	 * Visualization for {@link OpenClosedType} and {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		if (pchkInput.getLogicalSourceAddr().equals(this.addr)) {
			if (item.getAcceptedDataTypes().contains(OpenClosedType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getState(this.tableId, this.keyId) ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getState(this.tableId, this.keyId) ? OnOffType.ON : OnOffType.OFF);
				return true;
			}
		}
		return false;
	}
	
}
