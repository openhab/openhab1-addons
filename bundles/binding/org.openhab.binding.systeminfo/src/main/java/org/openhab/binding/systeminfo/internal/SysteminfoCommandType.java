/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.systeminfo.internal;

import java.io.InvalidClassException;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;

/**
 * Represents all valid command types which could be processed by this binding.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public enum SysteminfoCommandType {

	LOAD_AVERAGE_1MIN ("LoadAverage1Min", NumberItem.class),
	LOAD_AVERAGE_5MIN ("LoadAverage5Min", NumberItem.class),
	LOAD_AVERAGE_15MIN ("LoadAverage15Min", NumberItem.class),

	CPU_COMBINED ("CpuCombined", NumberItem.class),
	CPU_USER ("CpuUser", NumberItem.class),
	CPU_SYSTEM ("CpuSystem", NumberItem.class),
	CPU_NICE ("CpuNice", NumberItem.class),
	CPU_WAIT ("CpuWait", NumberItem.class),
	
	UPTIME ("Uptime", NumberItem.class),
	UPTIME_FORMATTED ("UptimeFormatted", StringItem.class),

	MEM_FREE_PERCENT ("MemFreePercent", NumberItem.class),
	MEM_USED_PERCENT ("MemUsedPercent", NumberItem.class),

	MEM_FREE ("MemFree", NumberItem.class),
	MEM_USED ("MemUsed", NumberItem.class),
	MEM_ACTUAL_FREE ("MemActualFree", NumberItem.class),
	MEM_ACTUAL_USED ("MemActualUsed", NumberItem.class),
	MEM_TOTAL ("MemTotal", NumberItem.class),

	SWAP_FREE ("SwapFree", NumberItem.class),
	SWAP_TOTAL ("SwapTotal", NumberItem.class),
	SWAP_USED ("SwapUsed", NumberItem.class),
	SWAP_PAGE_IN ("SwapPageIn", NumberItem.class),
	SWAP_PAGE_OUT ("SwapPageOut", NumberItem.class),

	NET_TX_BYTES ("NetTxBytes", NumberItem.class),
	NET_RX_BYTES ("NetRxBytes", NumberItem.class),

	DISK_READS ("DiskReads", NumberItem.class),
	DISK_WRITES ("DiskWrites", NumberItem.class),
	DISK_READ_BYTES ("DiskReadBytes", NumberItem.class),
	DISK_WRITE_BYTES ("DiskWriteBytes", NumberItem.class),

	DIR_USAGE ("DirUsage", NumberItem.class),
	DIR_FILES ("DirFiles", NumberItem.class),

	PROCESS_REAL_MEM ("ProcessRealMem", NumberItem.class),
	PROCESS_VIRTUAL_MEM ("ProcessVirtualMem", NumberItem.class),
	
	PROCESS_CPU_PERCENT ("ProcessCpuPercent", NumberItem.class),
	PROCESS_CPU_SYSTEM ("ProcessCpuSystem", NumberItem.class),
	PROCESS_CPU_USER ("ProcessCpuUser", NumberItem.class),
	PROCESS_CPU_TOTAL ("ProcessCpuTotal", NumberItem.class),
	PROCESS_UPTIME ("ProcessUptime", NumberItem.class),
	PROCESS_UPTIME_FORMATTED ("ProcessUptimeFormatted", StringItem.class),
	;

	private final String text;
	private Class<? extends Item> itemClass;

	private SysteminfoCommandType(final String text, Class<? extends Item> itemClass) {
		this.text = text;
		this.itemClass = itemClass;
	}

	@Override
	public String toString() {
		return text;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * Procedure to validate command type string.
	 * 
	 * @param commandTypeText
	 *            command string e.g. T1
	 * @return true if item is valid.
	 * @throws IllegalArgumentException
	 *             Not valid command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static boolean validateBinding(String commandTypeText,
			Class<? extends Item> itemClass) throws IllegalArgumentException, InvalidClassException {

		for (SysteminfoCommandType c : SysteminfoCommandType.values()) {
			if (c.text.equals(commandTypeText)) {
				if (c.getItemClass().equals(itemClass)) {
					return true;
				} else {
					throw new InvalidClassException("Not valid class for command type");
				}
			}
		}

		throw new IllegalArgumentException("Not valid command type");
	}

	/**
	 * Procedure to convert command type string to command type class.
	 * 
	 * @param commandTypeText
	 *            command string e.g. LoadAverage1Min
	 * @return corresponding command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static SysteminfoCommandType getCommandType(String commandTypeText) throws IllegalArgumentException {

		for (SysteminfoCommandType c : SysteminfoCommandType.values()) {
			if (c.text.equals(commandTypeText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid command type");
	}

}
