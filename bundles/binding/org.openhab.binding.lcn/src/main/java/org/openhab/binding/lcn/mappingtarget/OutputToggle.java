package org.openhab.binding.lcn.mappingtarget;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
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
 * Toggle's output-ports (on->off, off->on).
 * 
 * @author Tobias Jüttner
 */
public class OutputToggle extends TargetWithLcnAddr {
	
	/** Pattern to parse toggle commands. */
	private static final Pattern PATTERN_TOGGLE =
		Pattern.compile("(?<outputId>[1234]|(ALL))(?<ramp>\\.\\d+(,\\d+)?(?<timeUnit>.+))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target output-port (0..3) or -1 (all). */
	private final int outputId;
	
	/** The ramp to use. */
	private final int rampMSec;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param outputId 0..3
	 * @param rampMSec the ramp in milliseconds
	 */
	OutputToggle(LcnAddr addr, int outputId, int rampMSec) {
		super(addr);
		this.outputId = outputId;
		this.rampMSec = rampMSec;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link OutputToggle} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			try  {
				Matcher matcher;				
				switch (header.getCmd().toUpperCase()) {
					case "TOGGLE":
						if ((matcher = PATTERN_TOGGLE.matcher(header.getRestInput())).matches()) {
							int outputId = matcher.group("outputId").equalsIgnoreCase("ALL") ? -1 :
								Integer.parseInt(matcher.group("outputId")) - 1;
							double ramp = 0;
							if (matcher.group("ramp") != null) {  // Optional
								if (LcnDefs.TimeUnit.parse(matcher.group("timeUnit")) != LcnDefs.TimeUnit.SECONDS) {
									throw new IllegalArgumentException();
								}
								ramp = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("ramp")).doubleValue();
							}
							return new OutputToggle(header.getAddr(), outputId, (int)(ramp * 1000));
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
		if (this.outputId == -1) {  // All
			conn.queue(this.addr, true, PckGenerator.toggleAllOutputs(LcnDefs.timeToRampValue(this.rampMSec)));
		}
		else {  // Single
			conn.queue(this.addr, true, PckGenerator.toggleOutput(this.outputId, LcnDefs.timeToRampValue(this.rampMSec)));
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
