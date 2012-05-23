/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.vdr.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hampelratte.svdrp.commands.CHAN;
import org.hampelratte.svdrp.commands.HITK;
import org.hampelratte.svdrp.commands.MESG;
import org.hampelratte.svdrp.commands.VOLU;
import org.openhab.binding.vdr.VDRBindingProvider;
import org.openhab.binding.vdr.VDRCommandType;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The VDR binding connects to a VDR on the svdr port. The following features
 * are implemented
 * 
 * - Show message on OSD - power off - channel up / down - volume up / down - is
 * recoding
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */

public class VDRBinding extends
		AbstractEventSubscriberBinding<VDRBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(VDRBinding.class);

	/** RegEx to validate a vdr config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_VDR_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port)$");

	protected Map<String, VDRConfig> vdrConfigCache = new HashMap<String, VDRConfig>();

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * @{inheritDoc
	 */

	public void internalReceiveCommand(String itemName, Command command) {

		VDRBindingProvider provider = findFirstMatchingBindingProvider(itemName);

		if (provider == null) {
			logger.warn(
					"cannot find matching binding provider [itemName={}, command={}]",
					itemName, command);
			return;
		}

		List<String> vdrCommands = provider.getVDRCommand(itemName);
		List<String> ids = provider.getVDRId(itemName);

		if (ids != null) {
			int i = 0;
			for (String vdrId : ids) {
				String vdrCommand = vdrCommands.get(i);
				VDRCommandType vdrCmdType = VDRCommandType.create(vdrCommand,
						command);
				if (vdrCmdType != null) {
					executeCommand(vdrId, vdrCmdType, command);
				} else {
					logger.error(
							"wrong command type for binding [vdrId={}, command={}]",
							itemName, command);
				}
				i++;
			}
		}

	}

	private void executeCommand(String vdrId, VDRCommandType vdrCommandType,
			Command command) {
		VDRConnection connection = getVDRConnection(vdrId);

		if (connection != null && vdrCommandType != null) {
			switch (vdrCommandType) {
			case MESSAGE:
				connection.send(new MESG(command.toString()));
				break;
			case POWEROFF:
				connection.send(new HITK("Power"));
				break;
			case CHANNEL_UP:
				connection.send(new CHAN("+"));
				break;
			case CHANNEL_DOWN:
				connection.send(new CHAN("-"));
				break;
			case CHANNEL:
				connection.send(new CHAN (command.toString()));
				break;
			case VOLUME_UP:
				connection.send(new VOLU("+"));
				break;
			case VOLUME_DOWN:
				connection.send(new VOLU("-"));
				break;
			case VOLUME:
				connection.send(new VOLU(command.toString()));
				break;
			}
		}
	}

	private VDRConnection getVDRConnection(String vdrId) {
		VDRConfig vdrConfig = vdrConfigCache.get(vdrId);
		if (vdrConfig != null) {
			return vdrConfig.getVDRConnection();
		}
		return null;
	}

	/**
	 * Find the first matching {@link VDRBindingProvider} according to
	 * <code>itemName</code>
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private VDRBindingProvider findFirstMatchingBindingProvider(String itemName) {
		VDRBindingProvider firstMatchingProvider = null;
		for (VDRBindingProvider provider : this.providers) {

			List<String> vdrIds = provider.getVDRId(itemName);
			if (vdrIds != null && vdrIds.size() > 0) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}

	/**
	 * Find the first matching {@link VDRBindingProvider} according to
	 * <code>itemName</code> and <code>command</code>.
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	protected VDRBindingProvider findFirstMatchingBindingProviderByVDRId(
			String vdrId) {
		VDRBindingProvider firstMatchingProvider = null;
		for (VDRBindingProvider provider : this.providers) {

			String bindingItemName = provider.getBindingItemName(vdrId,
					VDRCommandType.MESSAGE);
			if (bindingItemName != null) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if (config != null) {

			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_VDR_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					logger.debug("given vdr-config-key '"
							+ key
							+ "' does not follow the expected pattern '<vdrId>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String playerId = matcher.group(1);

				VDRConfig playerConfig = vdrConfigCache.get(playerId);
				if (playerConfig == null) {
					playerConfig = new VDRConfig(playerId);
					vdrConfigCache.put(playerId, playerConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					playerConfig.host = value;
				} else if ("port".equals(configKey)) {
					playerConfig.port = Integer.valueOf(value);
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey + "' is unknown");
				}

			}
		}
	}

	/**
	 * Internal data structure which carries the connection details of one VDR
	 * (there could be several)
	 * 
	 * @author Wolfgang Willinghoefer
	 */
	static class VDRConfig {

		String host;
		int port;
		VDRConnection vdrConnection = null;
		String vdrId;

		public VDRConfig(String pVDRId) {
			vdrId = pVDRId;
		}

		@Override
		public String toString() {
			return "VDR [id=" + vdrId + ", host=" + host + ", port=" + port
					+ "]";
		}

		VDRConnection getVDRConnection() {
			if (vdrConnection == null) {
				vdrConnection = new VDRConnection(host, port);
			}
			return vdrConnection;
		}
	}
}