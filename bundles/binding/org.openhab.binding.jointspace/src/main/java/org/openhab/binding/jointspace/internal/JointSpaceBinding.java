/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jointspace.internal;

import java.awt.Color;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Dictionary;

import org.openhab.binding.jointspace.JointSpaceBindingProvider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

import org.openhab.core.library.types.OnOffType;
import org.openhab.io.net.actions.Ping;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Binding to handle communication with jointSPACE device. For items that are
 * configured for polling, the jointspace server is polled every
 * {@code refreshInterval} ms
 * 
 * @author David Lenz
 * @since 1.5.0
 */
public class JointSpaceBinding extends AbstractActiveBinding<JointSpaceBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(JointSpaceBinding.class);

	/** Constant which represents the content type <code>application/json</code> */
	public final static String CONTENT_TYPE_JSON = "application/json";

	public final static String PREFIX_HSB_TYPE = "HSB";
	public final static String PREFIX_DECIMAL_TYPE = "DEC";

	/**
	 * the refresh interval which is used to poll values from the JointSpace
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * The ip of the TV set
	 */
	private String ip;

	/**
	 * The port of the TV set, (optional, defaults to 1925)
	 */
	private String port = "1925";

	public JointSpaceBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "JointSpace Refresh Service";
	}

	/**
	 * @{inheritDoc
	 * 
	 *              Calls @see updateItemState() for all items with a "POLL"
	 *              command in the configuration
	 */
	@Override
	protected void execute() {
		logger.debug("Checking if host is available");

		boolean success = false;
		int timeout = 5000;
		try {
			success = Ping.checkVitality(ip, 0, timeout);
			if (success) {
				logger.debug("established connection [host '{}' port '{}' timeout '{}']",
						new Object[] { ip, 0, timeout });

				// After connection is possible, do the actual polling
				for (JointSpaceBindingProvider provider : providers) {
					for (String itemName : provider.getItemNames()) {
						String tvcommand = provider.getTVCommand(itemName, "POLL");
						if (tvcommand != null) {
							updateItemState(itemName, tvcommand);
						}
					}
				}

			} else {
				logger.debug("couldn't establish network connection [host '{}' port '{}' timeout '{}']", new Object[] {
						ip, 0, timeout });
			}
		} catch (SocketTimeoutException se) {
			logger.debug("timed out while connecting to host '{}' port '{}' timeout '{}'", new Object[] { ip, 0,
					timeout });
		} catch (IOException ioe) {
			logger.debug("couldn't establish network connection [host '{}' port '{}' timeout '{}']", new Object[] { ip,
					0, timeout });
		}
	}

	/**
	 * Parses an ambilight command and extracts the layers. for example
	 * "ambilight[layer1[left]]" will return a list {"layer1","left"}
	 * 
	 * @param command
	 *            ambilight command string. For example "ambilight[layer1].color
	 * @return a stringlist containing all the layers present in the command
	 */
	private String[] command2LayerString(String command) {
		String[] temp = command.split("\\.")[0].split("\\[");
		String[] layer = null;
		if (temp.length > 1) {
			layer = new String[temp.length - 1];
			System.arraycopy(temp, 1, layer, 0, temp.length - 1);
			layer[layer.length - 1] = layer[layer.length - 1].replace(']', ' ').trim();
		} else {
			layer = null;
		}
		return layer;
	}

	/**
	 * Polls the TV for the values specified in @see tvCommand and posts state
	 * update for @see itemName Currently only the following commands are
	 * available - "ambilight[...]" returning a HSBType state for the given
	 * ambilight pixel specified in [...] - "volume" returning a DecimalType -
	 * "volume.mute" returning 'On' or 'Off' - "source" returning a String with
	 * selected source (e.g. "hdmi1", "tv", etc)
	 * 
	 * @param itemName
	 * @param tvCommand
	 */
	private void updateItemState(String itemName, String tvCommand) {
		if (tvCommand.contains("ambilight")) {
			String[] layer = command2LayerString(tvCommand);
			HSBType state = new HSBType(getAmbilightColor(ip + ":" + port, layer));
			eventPublisher.postUpdate(itemName, state);
		} else if (tvCommand.contains("volume")) {
			if (tvCommand.contains("mute")) {
				eventPublisher.postUpdate(itemName, getTVVolume(ip + ":" + port).mute ? OnOffType.ON : OnOffType.OFF);
			} else {
				eventPublisher.postUpdate(itemName, new DecimalType(getTVVolume(ip + ":" + port).volume));
			}
		} else if (tvCommand.contains("source")) {
			eventPublisher.postUpdate(itemName, new StringType(getSource(ip + ":" + port)));
		} else {
			logger.error("Could not parse item state\"" + tvCommand + "\" for polling");
			return;
		}

	}

	/**
	 * @{inheritDoc Processes the commands and maps them to jointspace commands
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null && !this.providers.isEmpty()) {
			JointSpaceBindingProvider provider = this.providers.iterator().next();

			if (provider == null) {
				logger.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}

			logger.debug("Received command (item='{}', state='{}', class='{}')",
					new Object[] { itemName, command.toString(), command.getClass().toString() });

			String tvCommandString = null;

			// first check if we can translate the command directly
			tvCommandString = provider.getTVCommand(itemName, command.toString());

			// if not try some special notations
			if (tvCommandString == null) {
				if (command instanceof HSBType) {
					tvCommandString = provider.getTVCommand(itemName, "HSB");
				} else if (command instanceof DecimalType) {
					tvCommandString = provider.getTVCommand(itemName, "DEC");
				}
				if (tvCommandString == null) {
					tvCommandString = provider.getTVCommand(itemName, "*");
				}
			}

			if (tvCommandString == null) {
				logger.warn("Unrecognized command \"{}\"", command.toString());
				return;

			}

			if (tvCommandString.contains("key")) {
				logger.debug("Found a Key command: " + tvCommandString);
				String[] commandlist = tvCommandString.split("\\.");
				if (commandlist.length != 2) {
					logger.warn("wrong number of arguments for key command \"{}\". Should be key.X", tvCommandString);
					return;
				}
				String key = commandlist[1];
				sendTVCommand(key, ip + ":" + port);
			} else if (tvCommandString.contains("ambilight")) {
				logger.debug("Found an ambilight command: {}", tvCommandString);
				String[] commandlist = tvCommandString.split("\\.");
				String[] layer = command2LayerString(tvCommandString);
				if (commandlist.length < 2) {
					logger.warn(
							"wrong number of arguments for ambilight command \"{}\". Should be at least ambilight.color, ambilight.mode.X, etc...",
							tvCommandString);
					return;
				}
				if (commandlist[1].contains("color")) {
					setAmbilightColor(ip + ":" + port, command, layer);
				} else if (commandlist[1].contains("mode")) {
					if (commandlist.length != 3) {
						logger.warn(
								"wrong number of arguments for ambilight.mode command \"{}\". Should be ambilight.mode.internal, ambilight.mode.manual, ambilight.mode.expert",
								tvCommandString);
						return;
					}
					setAmbilightMode(commandlist[2], ip + ":" + port);
				}
			} else if (tvCommandString.contains("volume")) {
				logger.debug("Found a Volume command: {}", tvCommandString);
				sendVolume(ip + ":" + port, command);
			} else if (tvCommandString.contains("source")) {
				logger.debug("Found a Source command: {}", tvCommandString);
				String[] commandlist = tvCommandString.split("\\.");
				if (commandlist.length < 2) {
					logger.warn("wrong number of arguments for source command \"{}\". Should be at least mode.X...",
							tvCommandString);
					return;
				}
				sendSource(ip + ":" + port, commandlist[1]);

			} else {
				logger.warn("Unrecognized tv command \"{}\". Only key.X or ambilight[].X is supported", tvCommandString);
				return;
			}
		}
	}

	/**
	 * Gets the color for a specified ambilight pixel from the host and tries to
	 * parse the returned json value
	 * 
	 * @param host
	 *            hostname including port to query the jointspace api.
	 * @param layers
	 *            a list of layers to the requested pixel. For example
	 *            [layer1[right[2]]]
	 * @return Color of the ambilight pixel, or NULL if value could not be
	 *         retrieved
	 */
	private Color getAmbilightColor(String host, String[] layers) {

		logger.debug("Getting ambilight color for host {} for layers {}", host, layers);
		Color retval = new Color(0, 0, 0);
		String url = "http://" + host + "/1/ambilight/processed";

		String ambilight_json = HttpUtil.executeUrl("GET", url, IOUtils.toInputStream(""), CONTENT_TYPE_JSON, 1000);
		if (ambilight_json != null) {
			logger.trace("TV returned for ambilight request: {}", ambilight_json);
			try {
				Object obj = JSONValue.parse(ambilight_json);
				JSONObject array = (JSONObject) obj;
				for (String layer : layers) {
					array = (JSONObject) array.get((Object) layer.trim());
					if (array == null) {
						logger.warn("Could not find layer {} in the json string", layer);
						return null;
					}
				}
				int r = 0, g = 0, b = 0;
				r = Integer.parseInt(array.get("r").toString());
				g = Integer.parseInt(array.get("g").toString());
				b = Integer.parseInt(array.get("b").toString());
				retval = new Color(r, g, b);
			} catch (Throwable t) {
				logger.warn("Could not parse JSON String for ambilight value. Error: {}", t.toString());
			}

		} else {
			logger.debug("Could not get ambilight value from JointSpace Server \"{}\"", host);
			return null;
		}

		return retval;
	}

	/**
	 * Polls the source from the tv and returns it as a string
	 * 
	 * @param host
	 * @return a string containig the "source" returned by the TV, or null if
	 *         unsuccesfull
	 */
	private String getSource(String host) {
		String url = "http://" + host + "/1/sources/current";
		String source_json = HttpUtil.executeUrl("GET", url, IOUtils.toInputStream(""), CONTENT_TYPE_JSON, 1000);
		logger.debug("Getting source for host {}", host);
		if (source_json != null) {
			logger.trace("TV returned for source request: {}", source_json);
			try {
				Object obj = JSONValue.parse(source_json);
				JSONObject array = (JSONObject) obj;
				return array.get("id").toString();
			} catch (Throwable t) {
				logger.warn("Could not parse JSON String for source. Error: {}", t.toString());
			}

		} else {
			logger.debug("Could not get source from JointSpace Server \"{}\"", host);
		}
		return null;
	}

	/**
	 * Sets the current source at the TV
	 * 
	 * @param host
	 * @param source
	 *            string identifying the desired source. valid values are
	 *            "hdmi1", "tv", etc. (@see
	 *            http://jointspace.sourceforge.net/projectdata
	 *            /documentation/jasonApi/1/doc/API-Method-sources-GET.html)
	 */

	private void sendSource(String host, String source) {

		String url = "http://" + host + "/1/sources/current";

		String content = "{\"id\":\"" + source + "\"}";
		logger.debug("Switching source of host {} to {}", host, source);
		logger.trace(content.toString());
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content), CONTENT_TYPE_JSON, 1000);
	}

	/**
	 * Sends a volume command to the TV, depending on the command received For
	 * commands of type DecimalType, the value for volume will be set directly
	 * (mute will not be affected) For commands of type IncreaseDecreaseType,
	 * the current value (polled from TV( for volume will be
	 * incremented/decremented
	 * 
	 * @param host
	 * @param command
	 */
	private void sendVolume(String host, Command command) {

		logger.debug("Sending volume to host {} for command {}", host, command.toString());
		volumeConfig conf = getTVVolume(host);
		String url = "http://" + host + "/1/audio/volume";

		int newvalue = conf.volume;

		if (command instanceof DecimalType) {
			logger.debug("Setting volume to decimal type");
			newvalue = ((DecimalType) command).intValue();
		} else if (command instanceof IncreaseDecreaseType) {
			if ((IncreaseDecreaseType) command == IncreaseDecreaseType.INCREASE) {
				logger.debug("Increased volume");
				newvalue++;
			} else {
				logger.debug("Decreased volume");
				newvalue--;
			}
		} else {
			logger.warn("Unitl now only DecimalType and IncreaseDecreaseType commands are supported vor volume command");
			return;
		}
		// ensure that we are in the valid range for this device
		newvalue = Math.min(newvalue, conf.max);
		newvalue = Math.max(newvalue, conf.min);
		String content = "{\"muted\":\"" + conf.mute + "\", \"current\":\"" + newvalue + "\"}";
		logger.trace(content);
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content), CONTENT_TYPE_JSON, 1000);

	}

	/**
	 * Sets the ambilight color specified in command (which must be an HSBType
	 * until now) for the pixel(s) specified with @see layers.
	 * 
	 * @param host
	 * @param command
	 *            HSBType command to set the color
	 * @param layers
	 *            pixel(s) to set the color for. null if all pixels should have
	 *            the same value
	 */
	private void setAmbilightColor(String host, Command command, String[] layers) {

		if (!(command instanceof HSBType)) {
			logger.warn("Until now only HSBType is allowed for ambilight commands");
			return;
		}

		logger.debug("Setting Ambilight color for host {} and layer {} to {}", host, layers, command.toString());

		HSBType hsbcommand = (HSBType) command;
		String url = "http://" + host + "/1/ambilight/cached";

		StringBuilder content = new StringBuilder();
		content.append("{");

		int count = 0;

		if (layers != null) {
			for (int i = 0; i < layers.length; i++) {
				content.append("\"" + layers[i] + "\":{");
				count++;
			}
		}

		int red = Math.round(hsbcommand.getRed().floatValue() * 2.55f);
		int green = Math.round(hsbcommand.getGreen().floatValue() * 2.55f);
		int blue = Math.round(hsbcommand.getBlue().floatValue() * 2.55f);
		content.append("\"r\":" + red + ", \"g\":" + green + ", \"b\":" + blue);

		for (int i = 0; i < count; i++) {
			content.append("}");
		}

		content.append("}");

		logger.trace("Trying to post json for ambilight: {}", content.toString());

		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000);
	}

	/**
	 * Sends a key to to the host. Possible values for keys can be found here:
	 * http
	 * ://jointspace.sourceforge.net/projectdata/documentation/jasonApi/1/doc
	 * /API-Method-input-key-POST.html
	 * 
	 * @param key
	 * @param host
	 */
	private void sendTVCommand(String key, String host) {

		logger.debug("Sending Key {} to {}", key, host);
		String url = "http://" + host + "/1/input/key";

		String content = "{\"key\":\"" + key + "\"}";

		logger.trace(content);

		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content), CONTENT_TYPE_JSON, 1000);

	}

	/**
	 * Function to query the TV Volume
	 * 
	 * @param host
	 * @return struct containing all given information about current volume
	 *         settings (volume, mute, min, max) @see volumeConfig
	 */

	private volumeConfig getTVVolume(String host) {
		volumeConfig conf = new volumeConfig();
		String url = "http://" + host + "/1/audio/volume";
		String volume_json = HttpUtil.executeUrl("GET", url, IOUtils.toInputStream(""), CONTENT_TYPE_JSON, 1000);
		if (volume_json != null) {
			try {
				Object obj = JSONValue.parse(volume_json);
				JSONObject array = (JSONObject) obj;

				conf.mute = Boolean.parseBoolean(array.get("muted").toString());
				conf.volume = Integer.parseInt(array.get("current").toString());
				conf.min = Integer.parseInt(array.get("min").toString());
				conf.max = Integer.parseInt(array.get("max").toString());
			} catch (NumberFormatException ex) {
				logger.warn("Exception while interpreting volume json return");
			} catch (Throwable t) {
				logger.warn("Could not parse JSON String for volume value. Error: {}", t.toString());
			}

		}
		return conf;
	}

	/**
	 * Sets the mode of the ambilight processing mode. Manipulation the pixel
	 * values cannot be done in "internal" mode
	 * 
	 * For more details see:
	 * http://jointspace.sourceforge.net/projectdata/documentation
	 * /jasonApi/1/doc/API-Method-ambilight-mode-POST.html
	 * 
	 * @param mode
	 *            possible modes are: "internal", "manual", "expert".
	 * @param host
	 */

	private void setAmbilightMode(String mode, String host) {
		String url = "http://" + host + "/1/ambilight/mode";

		String content = "{\"current\":\"" + mode + "\"}";

		logger.trace(content);
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content), CONTENT_TYPE_JSON, 1000);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refreshinterval");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String ipString = (String) config.get("ip");
			if (StringUtils.isNotBlank(ipString)) {
				ip = ipString;
				setProperlyConfigured(true);
			}
			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString)) {
				port = portString;
			}

		}
	}
}
