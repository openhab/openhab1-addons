/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.nikobus.internal;

import org.apache.commons.lang.StringUtils;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openhab.binding.nikobus.internal.core.NikobusCommand;
import org.openhab.binding.nikobus.internal.util.SwitchModuleAnalyzer;

/**
 * Command provider. Provides the ability to analyze Nikobus modules from the
 * OSGI command line.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusCommandProvider implements CommandProvider {

	private NikobusBinding binding;

	/**
	 * {@inheritDoc}
	 * 
	 * Display available nikobus commands.
	 */
	@Override
	public String getHelp() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("--- Nikobus Commands---\n");
		appendCommand(buffer, "nikobus status", "Show connection status");
		appendCommand(buffer, "nikobus connect", "Connect to nikobus");

		appendCommand(buffer, "nikobus count '<moduleAddress>' <group>", "Show cached command count for <group> of module");
		appendCommand(buffer, "nikobus analyze '<moduleAddress>' <group>", "Brute force test checksum combinations for <group> of module");
		appendCommand(buffer, "nikobus verify '<moduleAddress>' <group>", "Test all commands in the cache for <group> of module");

		appendCommand(buffer, "nikobus send '<command>'", "Send command to nikobus");
		appendCommand(buffer, "nikobus help", "Print this text");
		return buffer.toString();
	}

	/**
	 * Add a right padded command to the provided builder.
	 * 
	 * @param builder
	 * @param command
	 * @param description
	 */
	private void appendCommand(StringBuilder builder, String command, String description) {
		builder.append("\t");
		builder.append(StringUtils.rightPad(command, 43));
		builder.append(" - ");
		builder.append(description);
		builder.append("\n");
	}

	/**
	 * Nikobus command implementation.
	 * 
	 * @param intp
	 *            commandInterpreter
	 * 
	 * @return null
	 */
	public Object _nikobus(CommandInterpreter intp) {
		try {
			String cmd = intp.nextArgument();

			if (cmd.equals("help")) {
				intp.println(getHelp());
				return null;
			}

			if (cmd.equals("connect")) {
				binding.connect();
				return null;
			}

			if (cmd.equals("send")) {
				String data = intp.nextArgument();
				if (data != null && data.length() > 0) {
					binding.sendCommand(new NikobusCommand(data));
				} else {
					intp.println("Missing command argument. Enclose command in single quotes. E.g. nikobus send '#N0D4CE6'");
					intp.print(getHelp());
				}
				return null;
			}

			if (cmd.equals("status")) {
				intp.println(binding.getConnectionStatus());
				intp.println("Command Cache Location: "
						+ binding.getCache().getPath());
				return null;
			}

			String address = intp.nextArgument();
			if (address == null || address.length() != 4) {
				intp.println(getHelp());
				return null;
			} else {
				address = address.toUpperCase();
			}

			String group = intp.nextArgument();
			int groupNum = 1;
			if (group != null && !(group.equals("1") || group.equals("2"))) {
				intp.println("Invalid group specified. Use group 1 or 2.");
			} else if (group != null) {
				groupNum = Integer.parseInt(group);
			}

			SwitchModuleAnalyzer analyzer = new SwitchModuleAnalyzer(groupNum);
			analyzer.setBinding(binding);
			intp.println("Starting analyzer for module " + address + ", group "
					+ groupNum);

			if (cmd.equals("analyze")) {
				analyzer.analyze(address);
			}

			if (cmd.equals("count")) {
				analyzer.count(address);
			}

			if (cmd.equals("verify")) {
				analyzer.verify(address);
			}

			return null;

		} catch (Exception e) {
			intp.print(getHelp());
		}

		return null;
	}

	/**
	 * Setter for DS.
	 * 
	 * @param binding
	 */
	public void setBinding(NikobusBinding binding) {
		this.binding = binding;
	}

	/**
	 * Unsetter for DS.
	 * 
	 * @param binding
	 */
	public void unsetBinding(NikobusBinding binding) {
		this.binding = null;
	}
	
}
