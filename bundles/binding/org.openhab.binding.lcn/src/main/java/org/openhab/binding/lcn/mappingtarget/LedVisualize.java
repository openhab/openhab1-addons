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
 * Visualizes an LED status.
 * 
 * @author Tobias Jüttner
 */
public class LedVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse LED visualizations. */ 
	private static final Pattern PATTERN_LED =
		Pattern.compile("(?<ledId>\\d+)(\\.(?<text0>.*?))?(\\.(?<text1>.*?))?(\\.(?<text2>.*?))?(\\.(?<text3>.*?))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0..11 */
	private final int ledId;
	
	/** 0 = off, 1 = on, 2 = blink, 3 = flicker */
	private final String[] stateTexts;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param ledId 0..11
	 * @param stateTexts the 4 state texts for off(0), on(1), blink(2), flicker(3)
	 */
	LedVisualize(LcnAddrMod addr, int ledId, String[] stateTexts) {
		super(addr);
		this.ledId = ledId;
		this.stateTexts = stateTexts;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link LedVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "LED_STATE":
					if ((matcher = PATTERN_LED.matcher(header.getRestInput())).matches()) {
						int ledId = Integer.parseInt(matcher.group("ledId")) - 1;
						if (ledId < 12) {
							String[] stateTexts = new String[] {
								"off", "on", "blink", "flicker"  // Defaults
							};
							for (int i = 0; i < 4; ++i) {
								String s = matcher.group(String.format("text%d", i));
								if (s != null) {
									stateTexts[i] = s;
								}
							}
							return new LedVisualize((LcnAddrMod)header.getAddr(), ledId, stateTexts);
						}
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
				switch (pchkInput.getLedState(this.ledId)) {
					case OFF: n = 0; break;
					case ON: n = 1; break;
					case BLINK: n = 2; break;
					case FLICKER: n = 3; break;
					default: throw new Error();
				}
				eventPublisher.postUpdate(item.getName(), new DecimalType(n));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(StringType.class)) {
				String s;
				switch (pchkInput.getLedState(this.ledId)) {
					case OFF: s = this.stateTexts[0]; break;
					case ON: s = this.stateTexts[1]; break;
					case BLINK: s = this.stateTexts[2]; break;
					case FLICKER: s = this.stateTexts[3]; break;
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
