package org.openhab.binding.lcn.mappingtarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.PckGenerator;
import org.openhab.binding.lcn.connection.Connection;
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
 * Sends keys deferred / delayed.
 * Supports multiple tables at once.
 * Only {@link LcnDefs.SendKeyCommand#HIT} is supported. 
 * 
 * @author Tobias Jüttner
 */
public class SendKeysHitDeferred extends TargetWithLcnAddr {
	
	/** Pattern to parse send-keys deferred commands. */ 
	private static final Pattern PATTERN_SENDKEYS_HIT_DEFERRED =
		Pattern.compile("(?<keys>([ABCD][12345678])*)(\\.HIT)?\\.(?<time>\\d+)(?<timeUnit>.+)",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Send-flags for the 4x8 keys. */
	private final boolean[][] keys;
	
	/** Delay time or 0. */
	private final int time;

	/** The delay time's unit. */
	private final LcnDefs.TimeUnit timeUnit; 
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param keys the 4x8 send-flags (true = send)
	 * @param time the delay time
	 * @param timeUnit the delay time's unit
	 */
	SendKeysHitDeferred(LcnAddr addr, boolean[][] keys, int time, LcnDefs.TimeUnit timeUnit) {
		super(addr);
		this.keys = keys;
		this.time = time;
		this.timeUnit = timeUnit;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link SendKeysHitDeferred} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			try {
				Matcher matcher;
				switch (header.getCmd().toUpperCase()) {
					case "KEYS":
						if ((matcher = PATTERN_SENDKEYS_HIT_DEFERRED.matcher(header.getRestInput())).matches()) {
							boolean[][] keys = new boolean[][] {
								new boolean[8], new boolean[8], new boolean[8], new boolean[8]
							};
							String s = matcher.group("keys");
							for (int i = 0; i < s.length(); i += 2) {
								String key = s.substring(i, i + 2).toUpperCase();
								int tableId;
								switch (key.charAt(0)){
									case 'A': tableId = 0; break;
									case 'B': tableId = 1; break;
									case 'C': tableId = 2; break;
									case 'D': tableId = 3; break;
									default: throw new Error();
								}
								int keyId = Integer.parseInt(key.substring(1, 2)) - 1;
								keys[tableId][keyId] = true;
							}
							LcnDefs.TimeUnit timeUnit = LcnDefs.TimeUnit.parse(matcher.group("timeUnit")); 
							return new SendKeysHitDeferred(header.getAddr(), keys, Integer.parseInt(matcher.group("time")), timeUnit);
						}
						break;
				}
			} catch (IllegalArgumentException ex) { }
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		for (int tableId = 0; tableId < 4; ++tableId) {
			for (int keyId = 0; keyId < 8; ++keyId) {
				if (this.keys[tableId][keyId]) {  // Send if at least one key should be sent
					conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.sendKeysHitDefered(tableId, this.time, this.timeUnit, this.keys[tableId]));
					break;
				}
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) { }
	
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
