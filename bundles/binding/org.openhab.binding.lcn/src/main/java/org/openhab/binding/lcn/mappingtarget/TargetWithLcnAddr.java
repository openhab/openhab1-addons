/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.mappingtarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnAddrGrp;
import org.openhab.binding.lcn.common.LcnAddrMod;

/**
 * Parent class for all targets targeting an LCN address. 
 * 
 * @author Tobias Jüttner
 */
public abstract class TargetWithLcnAddr extends Target {
	
	/** The target LCN address (module or group). */
	protected final LcnAddr addr;
	
	/**
	 * Constructor with target address.
	 * 
	 * @param addr the target address (module or group)
	 */
	protected TargetWithLcnAddr(LcnAddr addr) {
		this.addr = addr;
	}
	
	/**
	 * Gets the target LCN address (module or group). 
	 * 
	 * @return the address
	 */
	public LcnAddr getAddr() {
		return this.addr;
	}
	
	/**
	 * Parsed (common) target header.
	 * Returned by {@link Target#commonParseCmdAndAddress(String, boolean)}. 
	 */
	protected static class CmdAndAddressRet {
		
		private static final Pattern PATTERN =
			Pattern.compile("(?<cmd>.*?)\\.S?(?<segId>\\d+)\\.(?<type>M|G)?(?<id>\\d+)\\.?(?<rest>.*)",
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

		/** Rest input to be parsed. */
		private final String restInput;
		
		/** The parsed LCN command identifier. */
		private final String cmd;
		
		/** The parsed LCN address. */
		private final LcnAddr addr;
		
		/**
		 * Constructor.
		 * 
		 * @param restInput the rest input to be parsed
		 * @param cmd the parsed LCN command identifier
		 * @param addr the parsed {@link LcnAddr}
		 */
		private CmdAndAddressRet(String restInput, String cmd, LcnAddr addr) {
			this.restInput = restInput;
			this.cmd = cmd;
			this.addr = addr;
		}
		
		/**
		 * Parses the given text for an LCN command definition followed by an LCN address definition.
		 * <p>
		 * Examples:<ul>
		 * <li>- "ON.11.23" => ON, segment 11, module 23
		 * <li>- "ON.S11.M23" => ON, segment 11, module 23
		 * <li>- "OFF.11.G3" => OFF, segment 11, group 3</ul>
		 * 
		 * @param input the text to parse
		 * @param allowGroup false to disallow group addresses (will cause null to be returned)
		 * @return the parsed data or null
		 */
		static CmdAndAddressRet parse(String input, boolean allowGroup) {
			Matcher matcher;
			if ((matcher = PATTERN.matcher(input)).matches()) {
				boolean isGroup = matcher.group("type") != null && matcher.group("type").equalsIgnoreCase("G");
				if (isGroup && !allowGroup) {
					logger.error(String.format("Binding does not allow LCN group addresses: %s", input));
					return null;
				}
				return new CmdAndAddressRet(matcher.group("rest"), matcher.group("cmd"), isGroup ?
					new LcnAddrGrp(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("id"))) :
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("id"))));
			}
			return null;
		}		
		
		/**
		 * Retrieves the rest input to be parsed.
		 * 
		 * @return the rest input
		 */
		String getRestInput() {
			return this.restInput;
		}
		
		/**
		 * Retrieves the parsed LCN command identifier.
		 * 
		 * @return the LCN command identifier
		 */
		String getCmd() {
			return this.cmd;
		}
		
		/**
		 * Retrieves the parsed LCN address.
		 * 
		 * @return the parsed {@link LcnAddr}
		 */
		LcnAddr getAddr() {
			return this.addr;
		}
		
	}
	
}
