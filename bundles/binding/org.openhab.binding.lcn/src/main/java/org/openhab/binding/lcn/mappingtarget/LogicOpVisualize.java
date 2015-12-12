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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

/**
 * Visualizes a logic-operation status.
 * 
 * @author Tobias Jüttner
 */
public class LogicOpVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse logic-operation visualizations. */ 
	private static final Pattern PATTERN_LOGICOP =
		Pattern.compile("(?<logicOpId>[1234])(\\.(?<text0>.*?))?(\\.(?<text1>.*?))?(\\.(?<text2>.*?))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0..3 */
	private final int logicOpId;
	
	/** 0 = not, 1 = or, 2 = and */
	private final String[] stateTexts;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param logicOpId 0..3
	 * @param stateTexts the 3 state texts for not(0), or(1), and(2)
	 */
	LogicOpVisualize(LcnAddrMod addr, int logicOpId, String[] stateTexts) {
		super(addr);
		this.logicOpId = logicOpId;
		this.stateTexts = stateTexts;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link LogicOpVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "LOGICOP_STATE":
					if ((matcher = PATTERN_LOGICOP.matcher(header.getRestInput())).matches()) {
						String[] stateTexts = new String[] {
							"not", "or", "and"
						};
						for (int i = 0; i < 3; ++i) {
							String s = matcher.group(String.format("text%d", i));
							if (s != null) {
								stateTexts[i] = s;
							}
						}
						return new LogicOpVisualize((LcnAddrMod)header.getAddr(), Integer.parseInt(matcher.group("logicOpId")) - 1, stateTexts);
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
		if (!info.requestStatusLedsAndLogicOps.isActive()) {
			info.requestStatusLedsAndLogicOps.nextRequestIn(0, System.nanoTime());
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
	 * Visualization for {@link DecimalType} and {@link StringType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		if (pchkInput.getLogicalSourceAddr().equals(this.addr)) {
			if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				int n;
				switch (pchkInput.getLogicOpState(this.logicOpId)) {
					case NOT: n = 0; break;
					case OR: n = 1; break;
					case AND: n = 2; break;
					default: throw new Error();
				}
				eventPublisher.postUpdate(item.getName(), new DecimalType(n));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(StringType.class)) {
				String s;
				switch (pchkInput.getLogicOpState(this.logicOpId)) {
					case NOT: s = this.stateTexts[0]; break;
					case OR: s = this.stateTexts[1]; break;
					case AND: s = this.stateTexts[2]; break;
					default: throw new Error();
				}
				eventPublisher.postUpdate(item.getName(), new StringType(s));
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
