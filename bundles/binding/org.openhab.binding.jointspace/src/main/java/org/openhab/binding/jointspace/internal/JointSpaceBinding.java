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
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
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
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author David Lenz
 * @since 1.5.0
 */
public class JointSpaceBinding extends AbstractActiveBinding<JointSpaceBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(JointSpaceBinding.class);
	
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
	private String ip = "192.168.0.100";
	
	/**
	 * The port of the TV set, (optional, defaults to 1925)
	 */
	private String port = "1925";
	
	
	public JointSpaceBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "JointSpace Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 * 
	 * Calls @see updateItemState() for all items with a "POLL" command in the configuration 
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("Checking if host is available");
		
		boolean success = false;
		int timeout = 1000;
		try {
			success = Ping.checkVitality(ip, 0, timeout);
			if (success)
			{
				logger.debug("established connection [host '{}' port '{}' timeout '{}']", new Object[] {ip, 0, timeout});
			}
			else
			{
				logger.debug("couldn't establish network connection [host '{}' port '{}' timeout '{}']", new Object[] {ip, 0, timeout});
			}
		} 
		catch (SocketTimeoutException se) {
			logger.debug("timed out while connecting to host '{}' port '{}' timeout '{}'", new Object[] {ip, 0, timeout});
		}
		catch (IOException ioe) {
			logger.debug("couldn't establish network connection [host '{}' port '{}' timeout '{}']", new Object[] {ip, 0, timeout});
		}
		//if TV is not on, then we won't be able to poll it.
		if (!success)
		{
			return;
		}
		for (JointSpaceBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String tvcommand = provider.getTVCommand(itemName, "POLL");
				if (tvcommand != null)
				{
					updateItemState(itemName,tvcommand);
				}
			}
		}
	}
	
	/**
	 *  Parses an ambilight command and extracts the layers.
	 *  for example "ambilight[layer1[left]]" will return a list {"layer1","left"}
	 *  
	 * @param command ambilight command string. For example "ambilight[layer1].color
	 * @return a stringlist containing all the layers present in the command
	 */
	private String [] command2LayerString(String command)
	{
		String[] temp = command.split("\\.")[0].split("\\[");
		String [] layer = null;
		if (temp.length > 1)
		{
			layer = new String[temp.length-1];
			System.arraycopy(temp, 1, layer, 0, temp.length - 1);
			layer[layer.length-1] = layer[layer.length-1].replace(']', ' ').trim();
		}
		else
		{
			layer = null;
		}
		return layer;
	}
	
	/**
	 * Polls the TV for the values specified in @see tvCommand and posts state update for @see itemName
	 * Currently only the following commands are available
	 * - "ambilight[...]" returning a HSBType state for the given ambilight pixel specified in [...]
	 * - "volume" returning a DecimalType
	 * - "volume.mute" returning 'On' or 'Off' 
	 *  
	 * @param itemName
	 * @param tvCommand
	 */
	private void updateItemState(String itemName, String tvCommand)
	{
		if (tvCommand.contains("ambilight"))
		{
			String [] layer = command2LayerString(tvCommand);
			HSBType state = new HSBType(getAmbilightColor(ip+":"+port, layer));
			eventPublisher.postUpdate(itemName, state);
		}
		else if (tvCommand.contains("volume"))
		{
			if (tvCommand.contains("mute"))
			{
				eventPublisher.postUpdate(itemName, getTVVolume(ip + ":" + port).mute ? OnOffType.ON:OnOffType.OFF);
			}
			else
			{
				eventPublisher.postUpdate(itemName, new DecimalType(getTVVolume(ip + ":" + port).volume));
			}
		}
		else
		{
			logger.error("Could not parse item state\"" + tvCommand + "\" for polling");
			return;
		}
		
	}

	/**
	 * @{inheritDoc}
	 * 
	 * Processes the commands and maps them to jointspace commands
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.

		if (itemName != null) {
			JointSpaceBindingProvider provider = 
				findFirstMatchingBindingProvider(itemName, command.toString());

			if (provider == null) {
				logger.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}

			logger.debug(
					"Received command (item='{}', state='{}', class='{}')",
					new Object[] { itemName, command.toString(),
							command.getClass().toString() });
			
			String tmp = null;
			
			
			tmp = provider.getTVCommand(itemName, command.toString());
			
			if (tmp == null)
			{
				if (command instanceof HSBType)
				{
					tmp = provider.getTVCommand(itemName, "HSB");
				}
				else if (command instanceof DecimalType)
				{
					tmp = provider.getTVCommand(itemName, "DEC");
				}
				else
				{
					tmp = provider.getTVCommand(itemName, "*");
				}
			}
			
			
			if (tmp == null)
			{
				logger.warn("Unrecognized command \"" + command.toString() + "\"");
				return;
				
			}
			
			if (tmp.contains("key"))
			{
				String[] commandlist = tmp.split("\\.");
				if (commandlist.length != 2)
				{
					logger.warn("wrong number of arguments for key command \"" + tmp + "\". Should be key.X");
					return;
				}
				String key = commandlist[1];
				sendTVCommand(key, ip + ":" + port);
			}
			else if (tmp.contains("ambilight"))
			{
				String[] commandlist = tmp.split("\\.");
				String [] layer = command2LayerString(tmp);
				if (commandlist.length < 2)
				{
					logger.warn("wrong number of arguments for ambilight command \"" + tmp + "\". Should be at least ambilight.color, ambilight.mode.X, etc...");
					return;
				}
				if (commandlist[1].contains("color"))
				{
					setAmbilightColor(ip + ":" + port, command, layer);
				}
				else if (commandlist[1].contains("mode"))
				{
					if (commandlist.length != 3)
					{
						logger.warn("wrong number of arguments for ambilight.mode command \"" + tmp + "\". Should be ambilight.mode.internal, ambilight.mode.manual, ambilight.mode.expert");
						return;
					}
					setAmbilightMode(commandlist[2], ip+":"+port);
				}
			}
			else if (tmp.contains("volume"))
			{
				sendVolume(ip + ":" + port, command);
			}
			else
			{
				logger.warn("Unrecognized tv command \"" + tmp + "\". Only key.X or ambilight[].X is supported");
				return;
			}
		}
	}
	
	


	/**
	 * Gets the color for a specified ambilight pixel from the host and tries to parse the returned json value
	 * 
	 * @param host hostname including port to query the jointspace api.
	 * @param layers a list of layers to the requested pixel. For example [layer1[right[2]]]
	 * @return Color of the ambilight pixel, or NULL if value could not be retrieved
	 */
	private Color getAmbilightColor(String host, String[] layers)
	{
		//http://ip-address:1925/1/ambilight/cached
		Color retval = new Color(0, 0, 0);
		String url = "http://" + host + "/1/ambilight/processed";
		
		String ambilight_json = HttpUtil.executeUrl("GET", url, IOUtils.toInputStream(""), CONTENT_TYPE_JSON, 1000);
		if (ambilight_json != null)
		{
			logger.trace("TV returned for ambilight request: " + ambilight_json);
			try
			{
				Object obj=JSONValue.parse(ambilight_json);
				JSONObject array=(JSONObject)obj;
				for (String layer : layers)
				{
					array = (JSONObject) array.get((Object)layer.trim());
					if (array == null)
					{
						logger.warn("Could not find layer " + layer + " in the json string");
						return null;
					}
				}
				int r = 0, g =0, b=0;
				r = Integer.parseInt(array.get("r").toString());
				g = Integer.parseInt(array.get("g").toString());
				b = Integer.parseInt(array.get("b").toString());
				retval = new Color(r,g,b);
			}
			catch(Throwable t)
			{
				logger.warn("Could not parse JSON String for ambilight value. Error: " + t.toString());
			}
			
		}
		else
		{
			logger.debug("Could not get ambilight value from JointSpace Server \"" + host + "\"");
			return null;
		}
			
		return retval;
	}
	
	
	private void sendVolume(String host, Command command) {
		volumeConfig conf = getTVVolume(host);
		String url = "http://" + host + "/1/audio/volume";
		
		StringBuilder content = new StringBuilder();
		int newvalue = conf.volume;
		//ensure that we are in the valid range for this device
		
		
		if (command instanceof DecimalType)
		{
			newvalue = ((DecimalType)command).intValue();
		}
		else if (command instanceof IncreaseDecreaseType)
		{
			if ((IncreaseDecreaseType)command == IncreaseDecreaseType.INCREASE)
			{
				newvalue ++;
			}
			else
			{
				newvalue --;
			}
		}
		else
		{
			logger.warn("Unitl now only DecimalType and IncreaseDecreaseType commands are supported vor volume command");
			return;
		}
		
		newvalue = Math.min(newvalue, conf.max);
		newvalue = Math.max(newvalue, conf.min);
		content.append("{\"muted\":\"" + conf.mute + "\", \"current\":\""+newvalue+"\"}");
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000);
	}
	
	
	/**
	 * Sets the ambilight color specified in command (which must be an HSBType until now) for the pixel(s) specified with @see layers.
	 * 
	 * @param host
	 * @param command HSBType command to set the color
	 * @param layers pixel(s) to set the color for. null if all pixels should have the same value
	 */
	private void setAmbilightColor(String host, Command command, String[] layers) {
		
		
		if (!(command instanceof HSBType))
		{
			logger.warn("Until now only HSBType is allowed for ambilight commands");
			return;
		}
		
		HSBType hsbcommand = (HSBType) command;
		String url = "http://" + host + "/1/ambilight/cached";
		
		StringBuilder content = new StringBuilder();
		content.append("{");
		
		int count = 0;
		
		if (layers != null)
		{
			for(int i = 0; i < layers.length; i++)
			{
				content.append("\"" + layers[i] + "\":{");
				count++;
			}
		}
		
		int red = Math.round(hsbcommand.getRed().floatValue()*2.55f);
		int green = Math.round(hsbcommand.getGreen().floatValue()*2.55f);
		int blue =Math.round(hsbcommand.getBlue().floatValue()*2.55f);
		content.append("\"r\":" + red + ", \"g\":" + green + ", \"b\":"+blue);
		
		
		for(int i = 0; i < count; i++)
		{
			content.append("}");
		}
		
		content.append("}");

		logger.trace("Trying to post json for ambilight: " + content.toString());
		
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000); 
	}

	/**
	 * Sends a key to to the host. Possible values for keys can be found here:
	 * http://jointspace.sourceforge.net/projectdata/documentation/jasonApi/1/doc/API-Method-input-key-POST.html
	 * 
	 * @param key 
	 * @param host
	 */
	private void sendTVCommand(String key, String host) {
		
		String url = "http://" + host + "/1/input/key";
		
		StringBuilder content = new StringBuilder();
		content.append("{\"key\":\"" + key + "\"}");
		
        
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000); 
		
	}
	
	/**
	 * Helper function to increase (or decrease if @see inc is negative) the volume by a fixed amount.
	 * First queries the current volume, adds the value of @see inc and then sends this new value as a command
	 * 
	 * @param inc
	 * @param host
	 */
	private void addtoTVVolume(int inc, String host)
	{
		volumeConfig conf = getTVVolume(host);
		String url = "http://" + host + "/1/audio/volume";
		
		StringBuilder content = new StringBuilder();
		int newvalue = conf.volume + inc;
		//ensure that we are in the valid range for this device
		newvalue = Math.min(newvalue, conf.max);
		newvalue = Math.max(newvalue, conf.min);
		content.append("{\"muted\":\"" + conf.mute + "\", \"current\":\""+newvalue+"\"}");

		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000);
	}
	
	
	/**
	 * Function to query the TV Volume
	 * 
	 * @param host
	 * @return struct containing all given information about current volume settings (volume, mute, min, max) @see volumeConfig
	 */
	
	private volumeConfig getTVVolume(String host)
	{
		volumeConfig conf = new volumeConfig();
		String url = "http://" + host + "/1/audio/volume";
		String retval = HttpUtil.executeUrl("GET", url, IOUtils.toInputStream(""), CONTENT_TYPE_JSON, 1000);
		if (retval != null)
		{
			String[] fields = retval.split(",");
			if (fields.length < 4)
			{
				logger.warn("Could not interpret volume json return type");
				return conf;
			}
			try
			{
				conf.mute = Boolean.parseBoolean(fields[0].split(":")[1].trim());
				conf.volume = Integer.parseInt(fields[1].split(":")[1].trim());
				conf.min = Integer.parseInt(fields[2].split(":")[1].trim());
				conf.max = Integer.parseInt(fields[3].split(":")[1].replace('}', ' ').trim());
			}
			catch(NumberFormatException ex)
			{
				logger.warn("Exception while interpreting volume json return");
			}
		}
		return conf;
	}
	
	/**
	 * Sets the mode of the ambilight processing mode. 
	 * Manipulation the pixel values cannot be done in "internal" mode
	 * 
	 * For more details see: 
	 * http://jointspace.sourceforge.net/projectdata/documentation/jasonApi/1/doc/API-Method-ambilight-mode-POST.html
	 * 
	 * @param mode possible modes are: "internal", "manual", "expert". 
	 * @param host
	 */
	
	private void setAmbilightMode(String mode, String host)
	{
		String url = "http://" + host + "/1/ambilight/mode";
		
		StringBuilder content = new StringBuilder();
		content.append("{\"current\":\"" + mode + "\"}");
        
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000);
	}



	private JointSpaceBindingProvider findFirstMatchingBindingProvider(
			String itemName, String string) {
		
		for (JointSpaceBindingProvider provider : this.providers) {
			return provider;
		}
		
		return null;
	}
	


	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refreshinterval");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String ipString = (String) config.get("ip");
			if (StringUtils.isNotBlank(ipString))
			{
				ip = ipString;
			}
			String portString = (String) config.get("port");
			if (StringUtils.isNotBlank(portString))
			{
				port = portString;
			}
			
			// read further config parameters here ...

			setProperlyConfigured(true);
		}
	}
}
