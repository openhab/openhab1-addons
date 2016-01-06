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

/**
 * Changes the state of an LED.
 * 
 * @author Tobias Jüttner
 */
public class Led extends TargetWithLcnAddr {
	
	/** Pattern to parse LED commands. */ 
	private static final Pattern PATTERN_LED =
		Pattern.compile("(?<ledId>\\d+)\\.(?<action>(OFF)|(ON)|(BLINK)|(FLICKER))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0..11 */
	private final int ledId;
	
	/** Target LED state. */
	private final LcnDefs.LedStatus state;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param ledId 0..11
	 * @param state the state to set
	 */
	Led(LcnAddr addr, int ledId, LcnDefs.LedStatus state) {
		super(addr);
		this.ledId = ledId;
		this.state = state;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link Led} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "LED":
					if ((matcher = PATTERN_LED.matcher(header.getRestInput())).matches()) {
						int ledId = Integer.parseInt(matcher.group("ledId")) - 1;
						if (ledId < 12) {
							LcnDefs.LedStatus state;
							switch (matcher.group("action").toUpperCase()){
								case "OFF": state = LcnDefs.LedStatus.OFF; break;
								case "ON": state = LcnDefs.LedStatus.ON; break;
								case "BLINK": state = LcnDefs.LedStatus.BLINK; break;
								case "FLICKER": state = LcnDefs.LedStatus.FLICKER; break;
								default: throw new Error();
							}
							return new Led(header.getAddr(), ledId, state);
						}
					}
					break;
			}
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.controlLed(this.ledId, this.state));
		// Force status update (status is polled and should be updated now)
		if (!this.addr.isGroup()) {
			ModInfo info = conn.getModInfo((LcnAddrMod)this.addr);
			if (info != null) {
				info.requestStatusLedsAndLogicOps.nextRequestIn(ModInfo.STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC, System.nanoTime());
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		if (!this.addr.isGroup()) {
			ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
			if (!info.requestStatusLedsAndLogicOps.isActive()) {
				info.requestStatusLedsAndLogicOps.nextRequestIn(0, System.nanoTime());
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
	
	/**
	 * Visualization for {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		// We are actually not meant to visualize anything.
		// But (just in case) someone is really lazy in doing the item-definitions, we try to be helpful by implementing some ON/OFF logic.
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && item.getAcceptedDataTypes().contains(OnOffType.class)) {
			// Only update if the state we are bound to is equal to the reported one
			if (this.state == pchkInput.getLedState(this.ledId)) {
				eventPublisher.postUpdate(item.getName(), cmd == OnOffType.ON ? OnOffType.ON : OnOffType.OFF);
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
