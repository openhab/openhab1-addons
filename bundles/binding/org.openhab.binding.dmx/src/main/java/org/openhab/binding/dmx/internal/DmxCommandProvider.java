/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openhab.binding.dmx.DmxConnection;
import org.openhab.binding.dmx.DmxService;

/**
 * Command provider. Provides some basic DMX testing commands to the osgi
 * console.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxCommandProvider implements CommandProvider {

	private DmxService service;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("--- DMX Commands---\n");
		buffer.append("\tdmx status                       - Show connection status\n");
		buffer.append("\tdmx set <channel> <value>        - Set channel value\n");
		buffer.append("\tdmx get <channel>                - Get channel value\n");
		buffer.append("\tdmx loop <channel>               - Set channel in fading loop\n");
		buffer.append("\tdmx mirror <channel,channel,...> - Mirror the first channel on the other channels\n");

		return buffer.toString();
	}

	/**
	 * DMX command implementation.
	 * 
	 * @param intp
	 *            commandinterpreter
	 * 
	 * @return null
	 */
	public Object _dmx(CommandInterpreter intp) {

		try {

			String cmd = intp.nextArgument();

			if (cmd.equals("help")) {
				intp.println(getHelp());
				return null;
			}

			if (cmd.equals("status")) {
				DmxConnection conn = service.getConnection();
				if (conn == null) {
					intp.println("No dmx connection available.");
				} else if (conn.isClosed()) {
					intp.println("Not connected to "
							+ conn.getClass().getSimpleName() + ".");
				} else if (!conn.isClosed()) {
					intp.println("Connected to "
							+ conn.getClass().getSimpleName() + ".");
				}
				return null;
			}

			if (cmd.equals("mirror")) {
				String[] channels = intp.nextArgument().split(",");
				int sourceChannel = Integer.parseInt(channels[0]);
				for (int i = 1; i < channels.length; i++) {
					service.mirrorChannel(sourceChannel,
							Integer.parseInt(channels[i]), -1);
				}

				return null;
			}

			int channelId = Integer.parseInt(intp.nextArgument());

			if (cmd.equals("set")) {
				int value = Integer.parseInt(intp.nextArgument());
				service.setChannelValue(channelId, value);

			} else if (cmd.equals("get")) {

				int value = service.getChannelValue(channelId);
				intp.println("Channel " + channelId + " = " + value);

			} else if (cmd.equals("loop")) {
				service.fadeChannel(channelId, 15000, 255, 15000, true);
				service.fadeChannel(channelId, 15000, 0, 15000, false);
			} else {
				intp.println("Unrecognized command.");
			}

		} catch (Exception e) {
			intp.println("Unrecognized command.");
			intp.print(getHelp());
		}

		return null;
	}

	/**
	 * DmxService loaded via DS.
	 */
	public void setDmxService(DmxService dmxService) {
		this.service = dmxService;
	}

	/**
	 * DmxService unloaded via DS.
	 */
	public void unsetDmxService(DmxService dmxService) {
		this.service = null;
	}
}
