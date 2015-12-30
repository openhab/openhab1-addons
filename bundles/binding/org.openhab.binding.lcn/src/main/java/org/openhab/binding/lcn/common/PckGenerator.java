/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.common;

/**
 * Helpers to generate LCN-PCK commands.
 * <p>
 * LCN-PCK is the command-syntax used by LCN-PCHK to send and receive LCN commands.
 * 
 * @author Tobias Jüttner
 */
public final class PckGenerator {
	
	/** Terminates a PCK command. */
	public static final String TERMINATION = "\n";
	
	/**
	 * Generates a keep-alive.
	 * LCN-PCHK will close the connection if it does not receive any commands from
	 * an open {@link Connection} for a specific period (10 minutes by default).
	 * 
	 * @param counter the current ping's id (optional, but "best practice"). Should start with 1
	 * @return the PCK command as text
	 */
	public static String ping(int counter) {
		return String.format("^ping%d", counter);
	}
	
	/**
	 * Generates a PCK command that will set the LCN-PCHK connection's operation mode.
	 * This influences how output-port commands and status are interpreted and must be
	 * in sync with the LCN bus.
	 * 
	 * @param dimMode see {@link LcnDefs.OutputPortDimMode} 
	 * @param statusMode see {@link LcnDefs.OutputPortStatusMode}
	 * @return the PCK command as text
	 */
	public static String setOperationMode(LcnDefs.OutputPortDimMode dimMode, LcnDefs.OutputPortStatusMode statusMode) {
		return "!OM" + (dimMode == LcnDefs.OutputPortDimMode.STEPS200 ? "1" : "0") +
			(statusMode == LcnDefs.OutputPortStatusMode.PERCENT ? "P" : "N");
	}
	
	/**
	 * Generates a PCK address header.
	 * Used for commands to LCN modules and groups.
	 * 
	 * @param addr the target's address (module or group)
	 * @param localSegId the local segment id where the physical bus connection is located
	 * @param wantsAck true to claim an acknowledge / receipt from the target
	 * @return the PCK address header as text
	 */
	public static String generateAddressHeader(LcnAddr addr, int localSegId, boolean wantsAck) {
		return String.format(">%s%03d%03d%s", addr.isGroup() ? "G" : "M",
			addr.getPhysicalSegId(localSegId), addr.getId(),
			wantsAck ? "!" : ".");
	}
	
	/**
	 * Generates a scan-command for LCN segment-couplers.
	 * Used to detect the local segment (where the physical bus connection is located).
	 * 
	 * @return the PCK command (without address header) as text
	 */
	public static String segmentCouplerScan() {
		return "SK";
	}
	
	/**
	 * Generates a firmware/serial-number request.
	 * 
	 * @return the PCK command (without address header) as text
	 */
	public static String requestSn() {
		return "SN";
	}
	
	/**
	 * Generates an output-port status request.
	 * 
	 * @param outputId 0..3
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String requestOutputStatus(int outputId) throws IllegalArgumentException {
		if (outputId < 0 || outputId > 3) {
			throw new IllegalArgumentException();
		}
		return String.format("SMA%d", outputId + 1);
	}
	
	/**
	 * Generates a dim command for a single output-port.
	 * 
	 * @param outputId 0..3
	 * @param percent 0..100
	 * @param ramp use {@link LcnDefs#timeToRampValue(int)}
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String dimOutput(int outputId, double percent, int ramp) throws IllegalArgumentException {
		if (outputId < 0 || outputId > 3) {
			throw new IllegalArgumentException();
		}
		int n = (int)Math.round(percent * 2);
		if ((n % 2) == 0) {  // Use the percent command (supported by all LCN-PCHK versions)
			return String.format("A%dDI%03d%03d", outputId + 1, n / 2, ramp);
		}
		else {  // We have a ".5" value. Use the native command (supported since LCN-PCHK 2.3)
			return String.format("O%dDI%03d%03d", outputId + 1, n, ramp);
		}
	}
	
	/**
	 * Generates a dim command for all output-ports.
	 * 
	 * @param percent 0..100
	 * @param ramp use {@link LcnDefs#timeToRampValue(int)} (might be ignored in some cases)
	 * @param is1805 true if the target module's firmware is 180501 or newer
	 * @return the PCK command (without address header) as text
	 */
	public static String dimAllOutputs(double percent, int ramp, boolean is1805) {
		int n = (int)Math.round(percent * 2);
		if (is1805) {
			return String.format("OY%03d%03d%03d%03d%03d", n, n, n, n, ramp);  // Supported since LCN-PCHK 2.61
		}
		if (n == 0) {  // All off
			return String.format("AA%03d", ramp);
		}
		else if (n == 200) {  // All on
			return String.format("AE%03d", ramp);
		}
		// This is our worst-case: No high-res, no ramp
		return String.format("AH%03d", n / 2);
	}
	
	/**
	 * Generates a command to change the value of an output-port.
	 * 
	 * @param outputId 0..3
	 * @param percent -100..100
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String relOutput(int outputId, double percent) throws IllegalArgumentException {
		if (outputId < 0 || outputId > 3) {
			throw new IllegalArgumentException();
		}
		int n = (int)Math.round(percent * 2);
		if ((n % 2) == 0) {  // Use the percent command (supported by all LCN-PCHK versions)
			return String.format("A%d%s%03d", outputId + 1, percent >= 0 ? "AD" : "SB", Math.abs(n / 2));
		}
		else {  // We have a ".5" value. Use the native command (supported since LCN-PCHK 2.3)
			return String.format("O%d%s%03d", outputId + 1, percent >= 0 ? "AD" : "SB", Math.abs(n));
		}
	}
	
	/**
	 * Generates a command that toggles a single output-port (on->off, off->on).
	 * 
	 * @param outputId 0..3
	 * @param ramp see {@link LcnDefs#timeToRampValue(int)}
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String toggleOutput(int outputId, int ramp) throws IllegalArgumentException {
		if (outputId < 0 || outputId > 3) {
			throw new IllegalArgumentException();
		}
		return String.format("A%dTA%03d", outputId + 1, ramp);
	}
	
	/**
	 * Generates a command that toggles all output-ports (on->off, off->on).
	 * 
	 * @param ramp see {@link LcnDefs#timeToRampValue(int)}
	 * @return the PCK command (without address header) as text
	 */
	public static String toggleAllOutputs(int ramp) {
		return String.format("AU%03d", ramp);
	}
	
	/**
	 * Generates a relays-status request.
	 * 
	 * @return the PCK command (without address header) as text
	 */
	public static String requestRelaysStatus() {
		return "SMR";
	}
	
	/**
	 * Generates a command to control relays.
	 * 
	 * @param states the 8 modifiers for the relay states
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range 
	 */
	public static String controlRelays(LcnDefs.RelayStateModifier[] states) throws IllegalArgumentException {
		if (states.length != 8) {
			throw new IllegalArgumentException();
		}
		String ret = "R8";
		for (int i = 0; i < 8; ++i) {
			switch (states[i]) {
				case ON: ret += "1"; break;
				case OFF: ret += "0"; break;
				case TOGGLE: ret += "U"; break;
				case NOCHANGE: ret += "-"; break;
				default: throw new Error();
			}
		}
		return ret;
	}
	
	/**
	 * Generates a binary-sensors status request.
	 * 
	 * @return the PCK command (without address header) as text
	 */
	public static String requestBinSensorsStatus() {
		return "SMB";
	}
	
	/**
	 * Generates a command that sets a variable absolute.
	 * 
	 * @param var the target variable to set
	 * @param value the absolute value to set
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException
	 */
	public static String varAbs(LcnDefs.Var var, int value) throws IllegalArgumentException {
		int id = LcnDefs.Var.toSetPointId(var);
		if (id != -1) {
			// Set absolute (not in PCK yet)
            int b1 = id << 6;  // 01000000
            b1 |= 0x20;  // xx10xxxx (set absolute)
            value -= 1000;  // Offset
            b1 |= (value >> 8) & 0x0f;  // xxxx1111
            int b2 = value & 0xff;
            return String.format("X2%03d%03d%03d", 30, b1, b2);
		}
		// Setting variables and thresholds absolute not implemented in LCN firmware yet
		throw new IllegalArgumentException();
	}
	
	/**
	 * Generates a command that resets a variable to 0.
	 * 
	 * @param var the target variable to set 0
	 * @param is2013 the target module's firmware version is 170101 or newer
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if command is not supported
	 */
	public static String varReset(LcnDefs.Var var, boolean is2013) throws IllegalArgumentException {
		int id = LcnDefs.Var.toVarId(var);
		if (id != -1) {
			if (is2013) {
				return String.format("Z-%03d%04d", id + 1, 4090);
			}
			else {
				if (id == 0) {
					return "ZS30000";
				}
				else {
					throw new IllegalArgumentException();
				}
			}
		}
		id = LcnDefs.Var.toSetPointId(var);
		if (id != -1) {
			// Set absolute = 0 (not in PCK yet)
            int b1 = id << 6;  // 01000000
            b1 |= 0x20;  // xx10xxxx (set absolute)
            int b2 = 0;
            return String.format("X2%03d%03d%03d", 30, b1, b2);
		}
		// Reset for threshold not implemented in LCN firmware yet
		throw new IllegalArgumentException();
	}
	
	/**
	 * Generates a command to change the value of a variable.
	 * 
	 * @param var the target variable to change
	 * @param type the reference-point
	 * @param value the native LCN value to add/subtract (can be negative)
	 * @param is2013 the target module's firmware version is 170101 or newer
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if command is not supported
	 */
	public static String varRel(LcnDefs.Var var, LcnDefs.RelVarRef type, int value, boolean is2013) throws IllegalArgumentException {
		int id = LcnDefs.Var.toVarId(var);
		if (id != -1) {
			if (id == 0) {  // Old command for variable 1 / T-var (compatible with all modules)
				return String.format("Z%s%d",
					value >= 0 ? "A" : "S", Math.abs(value));
			}
			else {  // New command for variable 1-12 (compatible with all modules, since LCN-PCHK 2.8)
				return String.format("Z%s%03d%d",
					value >= 0 ? "+" : "-", id + 1, Math.abs(value));
			}
		}
		id = LcnDefs.Var.toSetPointId(var);
		if (id != -1) {
			return String.format("RE%sS%s%s%d",
				id == 0 ? "A" : "B", type == LcnDefs.RelVarRef.CURRENT ? "A" : "P",
				value >= 0 ? "+" : "-", Math.abs(value));
		}
		int registerId = LcnDefs.Var.toThrsRegisterId(var);
		id = LcnDefs.Var.toThrsId(var);
		if (registerId != -1 && id != -1) {
			if (is2013) {  // New command for registers 1-4 (since 170206, LCN-PCHK 2.8)
				return String.format("SS%s%04d%sR%d%d",
					type == LcnDefs.RelVarRef.CURRENT ? "R" : "E", Math.abs(value), value >= 0 ? "A" : "S",
					registerId + 1, id + 1);
			}
			else if (registerId == 0) {  // Old command for register 1 (before 170206)
				return String.format("SS%s%04d%s%s%s%s%s%s",
					type == LcnDefs.RelVarRef.CURRENT ? "R" : "E", Math.abs(value), value >= 0 ? "A" : "S",
					id == 0 ? "1" : "0", id == 1 ? "1" : "0", id == 2 ? "1" : "0", id == 3 ? "1" : "0", id == 4 ? "1" : "0");
			}
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Generates a variable value request.
	 * 
	 * @param var the variable to request
	 * @param swAge the target module's firmware version
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if command is not supported
	 */
	public static String requestVarStatus(LcnDefs.Var var, int swAge) throws IllegalArgumentException {
		if (swAge >= 0x170206) {
			int id = LcnDefs.Var.toVarId(var);
			if (id != -1) {
				return String.format("MWT%03d", id + 1);
			}
			id = LcnDefs.Var.toSetPointId(var);
			if (id != -1) {
				return String.format("MWS%03d", id + 1);
			}
			id = LcnDefs.Var.toThrsRegisterId(var);
			if (id != -1) {
				return String.format("SE%03d", id + 1);  // Whole register
			}
			id = LcnDefs.Var.toS0Id(var);
			if (id != -1) {
				return String.format("MWC%03d", id + 1);
			}
			throw new IllegalArgumentException();
		}
		else {
			switch (var) {
				case VAR1ORTVAR: return "MWV"; 
				case VAR2ORR1VAR: return "MWTA";
				case VAR3ORR2VAR: return "MWTB";
				case R1VARSETPOINT: return "MWSA";
				case R2VARSETPOINT: return "MWSB";
				case THRS1: case THRS2: case THRS3: case THRS4: case THRS5:
					return "SL1";  // Whole register
				default:
					throw new IllegalArgumentException();
			}
		}
	}
	
	/**
	 * Generates a request for LED and logic-operations states. 
	 * 
	 * @return the PCK command (without address header) as text
	 */
	public static String requestLedsAndLogicOpsStatus() {
		return "SMT";
	}
	
	/**
	 * Generates a command to the set the state of a single LED.
	 * 
	 * @param ledId 0..11
	 * @param state the state to set
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String controlLed(int ledId, LcnDefs.LedStatus state) throws IllegalArgumentException {
		if (ledId < 0 || ledId > 11) {
			throw new IllegalArgumentException();
		}
		return String.format("LA%03d%s", ledId + 1,
			state == LcnDefs.LedStatus.OFF ? "A" : state == LcnDefs.LedStatus.ON ? "E" :
			state == LcnDefs.LedStatus.BLINK ? "B" : "F"); 
	}
	
	/**
	 * Generates a command to send LCN keys.
	 * 
	 * @param cmds the 4 concrete commands to send for the tables (A-D) 
	 * @param keys the tables' 8 key-states (true means "send")
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String sendKeys(LcnDefs.SendKeyCommand[] cmds, boolean[] keys) throws IllegalArgumentException {
		if (cmds.length != 4 || keys.length != 8) {
			throw new IllegalArgumentException();
		}
		String ret = "TS";
		for (int i = 0; i < 4; ++i) {
			switch (cmds[i]) {
				case HIT: ret += "K"; break; 
				case MAKE: ret += "L"; break;
				case BREAK: ret += "O"; break;
				case DONTSEND:
					// By skipping table D (if it is not used), we use the old command
					// for table A-C which is compatible with older LCN modules 
					if (i < 3) {
						ret += "-";
					}
					break;
				default:
					throw new Error();
			}
		}
		for (int i = 0; i < 8; ++i) {
			ret += keys[i] ? "1" : "0";
		}
		return ret;
	}
	
	/**
	 * Generates a command to send LCN keys deferred / delayed.
	 * 
	 * @param tableId 0(A)..3(D)
	 * @param time the delay time
	 * @param timeUnit the time unit
	 * @param keys the key-states (true means "send")
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String sendKeysHitDefered(int tableId, int time, LcnDefs.TimeUnit timeUnit, boolean[] keys) throws IllegalArgumentException {
		if (tableId < 0 || tableId > 3 || keys.length != 8) {
			throw new IllegalArgumentException();
		}
		String ret = "TV";
		switch (tableId) {
			case 0: ret += "A"; break;
			case 1: ret += "B"; break;
			case 2: ret += "C"; break;
			case 3: ret += "D"; break;
			default:
				throw new IllegalArgumentException();
		}
		ret += String.format("%03d", time);
		switch (timeUnit) {
			case SECONDS:
				if (time < 1 || time > 60) {
					throw new IllegalArgumentException();
				}
				ret += "S";
				break;
			case MINUTES:
				if (time < 1 || time > 90) {
					throw new IllegalArgumentException();
				}
				ret += "M";
				break;
			case HOURS:
				if (time < 1 || time > 50) {
					throw new IllegalArgumentException();
				}
				ret += "H";
				break;
			case DAYS:
				if (time < 1 || time > 45) {
					throw new IllegalArgumentException();
				}
				ret += "D";
				break;
			default:
				throw new Error();
		}
		for (int i = 0; i < 8; ++i) {
			ret += keys[i] ? "1" : "0";
		}
		return ret;
	}
	
	/**
	 * Generates a request for key-lock states.
	 * Always requests table A-D. Supported since LCN-PCHK 2.8.
	 * @return the PCK command (without address header) as text
	 */
	public static String requestKeyLocksStatus() {
		return "STX";
	}
	
	/**
	 * Generates a command to lock keys. 
	 * 
	 * @param tableId 0(A)..3(D)
	 * @param states the 8 key-lock modifiers
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String lockKeys(int tableId, LcnDefs.KeyLockStateModifier[] states) throws IllegalArgumentException {
		if (tableId < 0 || tableId > 3 || states.length != 8) {
			throw new IllegalArgumentException();
		}
		String ret = String.format("TX%s", tableId == 0 ? "A" : tableId == 1 ? "B" : tableId == 2 ? "C" : "D");
		for (int i = 0; i < 8; ++i) {
			switch (states[i]) {
				case ON: ret += "1"; break;
				case OFF: ret += "0"; break;
				case TOGGLE: ret += "U"; break;
				case NOCHANGE: ret += "-"; break;
				default: throw new Error();
			}
		}
		return ret;
	}
	
	/**
	 * Generates a command to lock keys for table A temporary.
	 * There is no hardware-support for locking tables B-D.
	 * 
	 * @param time the lock time
	 * @param timeUnit the time unit
	 * @param keys the 8 key-lock states (true means lock)
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String lockKeyTabATemporary(int time, LcnDefs.TimeUnit timeUnit, boolean[] keys) throws IllegalArgumentException {
		if (keys.length != 8) {
			throw new IllegalArgumentException();
		}
		String ret = String.format("TXZA%03d", time);
		switch (timeUnit) {
			case SECONDS:
				if (time < 1 || time > 60) {
					throw new IllegalArgumentException();
				}
				ret += "S";
				break;
			case MINUTES:
				if (time < 1 || time > 90) {
					throw new IllegalArgumentException();
				}
				ret += "M";
				break;
			case HOURS:
				if (time < 1 || time > 50) {
					throw new IllegalArgumentException();
				}
				ret += "H";
				break;
			case DAYS:
				if (time < 1 || time > 45) {
					throw new IllegalArgumentException();
				}
				ret += "D";
				break;
			default:
				throw new Error();
		}
		for (int i = 0; i < 8; ++i) {
			ret += keys[i] ? "1" : "0";
		}
		return ret;
	}
	
	/**
	 * Generates the command header / start for sending dynamic texts.
	 * Used by LCN-GTxD periphery (supports 4 text rows).
	 * To complete the command, the text to send must be appended (UTF-8 encoding).
	 * Texts are split up into up to 5 parts with 12 "UTF-8 bytes" each.
	 * 
	 * @param row 0..3
	 * @param part 0..4
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String dynTextHeader(int row, int part) throws IllegalArgumentException {
		if (row < 0 || row > 3 || part < 0 || part > 4) {
			throw new IllegalArgumentException();
		}
		return String.format("GTDT%d%d", row + 1, part + 1);
	}
	
	/**
	 * Generates a command to lock a regulator.
	 * 
	 * @param regId 0..1
	 * @param state the lock state
	 * @return the PCK command (without address header) as text
	 * @throws IllegalArgumentException if out of range
	 */
	public static String lockRegulator(int regId, boolean state) throws IllegalArgumentException {
		if (regId < 0 || regId > 1){
			throw new IllegalArgumentException();
		}
		return String.format("RE%sX%s", regId == 0 ? "A" : "B", state ? "S" : "A");
	}
	
}
