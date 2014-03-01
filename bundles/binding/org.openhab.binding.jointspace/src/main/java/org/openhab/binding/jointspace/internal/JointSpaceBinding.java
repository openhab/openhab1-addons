/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jointspace.internal;

import java.util.Dictionary;

import org.openhab.binding.jointspace.JointSpaceBindingProvider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.net.http.HttpUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author „Lenzebo“
 * @since 1.5.0
 */
public class JointSpaceBinding extends AbstractActiveBinding<JointSpaceBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(JointSpaceBinding.class);
	
	/** Constant which represents the content type <code>application/json</code> */
	public final static String CONTENT_TYPE_JSON = "application/json";


	
	/** 
	 * the refresh interval which is used to poll values from the JointSpace
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
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
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
	}

	/**
	 * @{inheritDoc}
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
			
			String tmp = provider.getTVCommand(itemName, command.toString());
			
			if (tmp == null)
			{
				if (command instanceof HSBType)
				{
					tmp = provider.getTVCommand(itemName, "HSB");
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
				sendTVCommand(key, "192.168.0.100:1925");
			}
			else if (tmp.contains("ambilight"))
			{
				setAmbilightColor("192.168.0.100:1925", command, null);
			}
			else
			{
				logger.warn("Unrecognized tv command \"" + tmp + "\". Only key.X or ambilight[].X is supported");
				return;
			}
		}


	}
	
	private void setAmbilightColor(String host, Command command, String[] layers) {
		
		
		if (!(command instanceof HSBType))
		{
			logger.warn("Until now only HSBType is allowed for ambilight commands");
			
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
			}
			count++;
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

		
		HttpUtil.executeUrl("POST", url, IOUtils.toInputStream(content.toString()), CONTENT_TYPE_JSON, 1000); 
	}


	private void sendTVCommand(String key, String host) {
		
		String url = "http://" + host + "/1/input/key";
		
		StringBuilder content = new StringBuilder();
		content.append("{\"key\":\"" + key + "\"}");
		
        
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
		logger.debug("internalReceiveCommand() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			// read further config parameters here ...

			setProperlyConfigured(true);
		}
	}
}
