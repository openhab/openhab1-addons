package org.openhab.binding.lcn.mappingtarget;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

/**
 * Sets the value of a variable absolute.
 * 
 * @author Tobias Jüttner
 */
public class VarAbs extends TargetWithLcnAddr {
	
	/** Pattern to parse variable commands. */
	private static final Pattern PATTERN_VAR_ABS =
		Pattern.compile("(?<varId>\\d+)\\.(?<value>-?\\d+(,\\d+)?)(?<modifier>.+)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse variable commands with %i. */
	private static final Pattern PATTERN_VAR_ABSI =
		Pattern.compile("(?<varId>\\d+)\\.%i(?<modifier>.+?)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse set-point variable commands. */
	private static final Pattern PATTERN_SETPOINT_ABS =
		Pattern.compile("(?<regId>[12])\\.(?<value>-?\\d+(,\\d+)?)(?<modifier>.+)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse set-point variable commands with %i. */
	private static final Pattern PATTERN_SETPOINT_ABSI =
		Pattern.compile("(?<regId>[12])\\.%i(?<modifier>.+?)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target LCN variable. */
	private final LcnDefs.Var var;
	
	/** The variable's unit. */
	private final LcnDefs.VarUnit unit;
	
	/** The absolute value to set or null if %i is used. */
	private final LcnDefs.VarValue value;
	
	/** Forces the old commands before 170206. Required if target is an LCN group. */
	private final boolean forceOld;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param var the target LCN variable
	 * @param unit the target variable's unit
	 * @param value the value or null if %i is used
	 * @param forceOld true to force old command before 170206 (only required if target is an LCN group)
	 */
	VarAbs(LcnAddr addr, LcnDefs.Var var, LcnDefs.VarUnit unit, LcnDefs.VarValue value, boolean forceOld) {
		super(addr);
		this.var = var;
		this.unit = unit;
		this.value = value;
		this.forceOld = forceOld;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link VarAbs} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			try  {
				Matcher matcher;				
				switch (header.getCmd().toUpperCase()) {
					case "VAR":
					case "VAR_OLD":
						if ((matcher = PATTERN_VAR_ABS.matcher(header.getRestInput())).matches()) {
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarAbs(header.getAddr(), LcnDefs.Var.varIdToVar(Integer.parseInt(matcher.group("varId")) - 1), unit,
								LcnDefs.VarValue.fromVarUnit(value, unit, true), header.getCmd().toUpperCase().endsWith("_OLD"));
						}
						else if ((matcher = PATTERN_VAR_ABSI.matcher(header.getRestInput())).matches()) {
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarAbs(header.getAddr(), LcnDefs.Var.varIdToVar(Integer.parseInt(matcher.group("varId")) - 1), unit,
								null, header.getCmd().toUpperCase().endsWith("_OLD"));
						}
						break;
					case "SETPOINT":
						if ((matcher = PATTERN_SETPOINT_ABS.matcher(header.getRestInput())).matches()) {
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarAbs(header.getAddr(), LcnDefs.Var.setPointIdToVar(Integer.parseInt(matcher.group("regId")) - 1), unit,
								LcnDefs.VarValue.fromVarUnit(value, unit, true), false);
						}
						else if ((matcher = PATTERN_SETPOINT_ABSI.matcher(header.getRestInput())).matches()) {
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarAbs(header.getAddr(), LcnDefs.Var.setPointIdToVar(Integer.parseInt(matcher.group("regId")) - 1), unit,
								null, false);
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
		LcnDefs.VarValue value = this.value;
		if (value == null && cmd instanceof DecimalType) {
			value = LcnDefs.VarValue.fromVarUnit(((DecimalType)cmd).doubleValue(), this.unit, true);
		}
		if (value != null) {
			try {
				boolean is2013 = !this.forceOld;
				ModInfo info = null;
				if (!this.addr.isGroup()) {
					info = conn.getModInfo((LcnAddrMod)this.addr);
					if (info != null) {
						is2013 = info.getSwAge() >= 0x170206;
					}
				}
				if (LcnDefs.Var.toVarId(this.var) != -1) {
					// Absolute commands for variables are not supported.
					// We fake the missing command by using reset and relative commands.
					conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.varReset(this.var, is2013));
					conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.varRel(this.var, LcnDefs.RelVarRef.CURRENT, value.toNative(), is2013));
				}
				else {
					conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.varAbs(this.var, value.toNative()));
				}
				// Force a status update
				if (info != null && LcnDefs.Var.shouldPollStatusAfterCommand(this.var, is2013) && info.requestStatusVars.containsKey(this.var)) {
					info.requestStatusVars.get(this.var).nextRequestIn(ModInfo.STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC, System.nanoTime());
				}
			}
			catch (IllegalArgumentException ex) {
				logger.warn(String.format("Variable of type %s does not support \"set absolute\" commands.", this.var));
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
	
	/**
	 * Visualization for {@link StringType} and {@link DecimalType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		// We are actually not meant to visualize anything.
		// But (just in case) someone is really lazy in doing the item-definitions, we try to be helpful by showing the current value.
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && pchkInput.getVar() == this.var) {
			if (item.getAcceptedDataTypes().contains(StringType.class)) {
				String valueStr = pchkInput.getValue().toVarUnitString(this.unit, LcnDefs.Var.isLockableRegulatorSource(this.var), LcnDefs.Var.useLcnSpecialValues(this.var));
				eventPublisher.postUpdate(item.getName(), new StringType(valueStr));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				eventPublisher.postUpdate(item.getName(), new DecimalType(pchkInput.getValue().toVarUnit(this.unit, LcnDefs.Var.isLockableRegulatorSource(this.var))));
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
