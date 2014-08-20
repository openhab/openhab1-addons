/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Dictionary;

import org.openhab.binding.benqprojector.BenqProjectorBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * This binding allows interaction with BenQ Projectors supporting and RS232 interface
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class BenqProjectorBinding extends AbstractActiveBinding<BenqProjectorBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(BenqProjectorBinding.class);

	/**
	 * Flag to indicate network vs. serial mode. Default to network
	 * at the moment as this is the only mode implemented
	 */
	private boolean networkMode = true;
	
	/**
	 * Network host to use
	 */
	private String networkHost = "";
	
	/**
	 * Network port to use
	 */
	private int networkPort = 0;
	
	private Socket projectorSocket = null;
	private PrintWriter projectorWriter = null;
	private BufferedReader projectorReader = null;
	
	/** 
	 * the refresh interval which is used to poll values from the BenqProjector
	 * server (optional, defaults to 60000ms)
	 * 
	 */
	private long refreshInterval = 60000;
	
	/**
	 * Min & Max volume limits
	 */
	private final int MAX_VOLUME = 10;
	private final int MIN_VOLUME = 0;
	
	/**
	 * Set socket timeout time in milliseconds
	 */
	private final int SOCKET_TIMEOUT_MS = 5000;
	
	public BenqProjectorBinding() {
	}
		
	
	public void activate() {		
	}
	
	public void deactivate() {		
		try {
			this.projectorReader.close();
			this.projectorReader = null;		
			this.projectorWriter.close();
			this.projectorReader = null;
			this.projectorSocket.close();
		} catch (IOException e) {
			logger.error("Trying close socket, reader or writer resulted in IO exception: "+e.getMessage());
		}
		this.projectorSocket = null;
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
		return "BenqProjector Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		for (BenqProjectorBindingProvider binding : super.providers)
		{
			for (String itemName : binding.getItemNames())
			{				
				logger.debug("Polling projector status for "+itemName);
				BenqProjectorBindingConfig cfg = binding.getConfigForItemName(itemName);
				State s = queryProjector(cfg);
				if (!(s instanceof UnDefType))
				{
					eventPublisher.postUpdate(itemName, s);
					logger.debug(itemName+" status is "+s);
				} else {
					logger.debug(itemName+" not updated as result was undefined");
				}
				
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (BenqProjectorBindingProvider binding : super.providers)
		{	
			if (binding.providesBindingFor(itemName))
			{
				logger.debug("Process command "+command+" for "+itemName);
				BenqProjectorBindingConfig cfg = binding.getConfigForItemName(itemName);
				sendCommandToProjector(cfg, command);
			}
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) 
		{
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) 
			{
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			String modeString = (String) config.get("mode");
			if (StringUtils.isNotBlank(modeString)) 
			{
				if (modeString.equalsIgnoreCase("serial"))
				{
					networkMode=false;
				}
			}
			
			String deviceIdString = (String) config.get("deviceId");
			if (StringUtils.isNotBlank(deviceIdString))
			{
				String[] deviceIdParts = deviceIdString.split(":");
				if (deviceIdParts.length == 2)
				{
					this.networkHost = deviceIdParts[0];
					this.networkPort = Integer.parseInt(deviceIdParts[1]);
				}
				setProperlyConfigured(true);
				setupConnection();
			}				
		}		
	}
	
	private boolean setupConnection()
	{		
		boolean setupOK = false;
		if (this.projectorSocket == null && this.networkMode)
		{
			logger.debug("Running connection setup");
			try
			{
				logger.debug("Setting up socket connection to "+this.networkHost+":"+this.networkPort);
				this.projectorSocket = new Socket(this.networkHost, this.networkPort);
				this.projectorSocket.setSoTimeout(SOCKET_TIMEOUT_MS);
				logger.debug("Setup reader/writer");
				this.projectorReader = new BufferedReader(new InputStreamReader(this.projectorSocket.getInputStream()));
				this.projectorWriter = new PrintWriter( this.projectorSocket.getOutputStream(), true );
				setupOK = true;
			}
			catch (UnknownHostException e)
			{
				logger.error("Unable to find host: "+this.networkHost);
			} catch (IOException e) {
				logger.error("IO Exception: "+e.getMessage());
			}
			logger.debug("Network connection setup successfully!");
		}
		else if (this.networkMode == false)
		{
			logger.error("Non-network mode not implemented yet!");
		} else
		{
			logger.debug("Socket is already setup");
		}
		return setupOK;
	}
	
	/**
	 * Run query on the projector
	 * @param cfg Configuration of item to run query on
	 */
	private State queryProjector(BenqProjectorBindingConfig cfg)
	{
		String resp = sendCommandExpectResponse(cfg.mode.getItemModeCommandQueryString());
		return cfg.mode.parseResponse(resp);		
	}
	
	private void sendCommandToProjector(BenqProjectorBindingConfig cfg, Command c)
	{
		Boolean cmdSent = false;
		switch (cfg.mode)
		{
		case POWER:
		case MUTE:
			if (c instanceof OnOffType)
			{
				if ((OnOffType)c == OnOffType.ON)
				{
					sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString("ON"));
					cmdSent = true;
				}
				else if ((OnOffType)c == OnOffType.OFF)
				{
					sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString("OFF"));
					cmdSent = true;
				}
			}
			break;
		case VOLUME:
			if (c instanceof DecimalType)
			{
				/* get current volume */
				State currentVolState = queryProjector(cfg);
				int currentVol = ((DecimalType)currentVolState).intValue();
				
				int volLevel = ((DecimalType)c).intValue();
				if (volLevel > this.MAX_VOLUME)
				{
					volLevel = this.MAX_VOLUME;
				}
				else if (volLevel < this.MIN_VOLUME)
				{
					volLevel = this.MIN_VOLUME;
				}
				
				if (currentVol == volLevel) cmdSent = true;
				
				while (currentVol != volLevel)
				{
					if (currentVol < volLevel)
					{
						sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString("+"));
						currentVol++;
						cmdSent = true;
					}
					else
					{
						sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString("-"));						
						currentVol--;
						cmdSent = true;
					}
				}					
			} else if (c instanceof IncreaseDecreaseType)
			{
				if ((IncreaseDecreaseType)c == IncreaseDecreaseType.INCREASE)
				{
					sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString("+"));
					cmdSent = true;
				}
				else if ((IncreaseDecreaseType)c == IncreaseDecreaseType.DECREASE)
				{
					sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString("-"));
					cmdSent = true;
				}
			}
			break;
		case LAMP_HOURS:
			logger.warn("Cannot send command to set lamp hours - not a valid operation!");			
			break;
		case SOURCE_NUMBER:
			if (c instanceof DecimalType)
			{
				DecimalType sourceIdx = (DecimalType)c;
				String cmd = BenqProjectorSourceMapping.getStringFromMapping(sourceIdx.intValue());
				if (cmd.isEmpty() == false)
				{
					sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString(cmd));
					cmdSent = true;
				}
			}
			break;
		case SOURCE_STRING:
			if (c instanceof StringType)
			{
				StringType sourceStr = (StringType)c;
				int mappingIdx = BenqProjectorSourceMapping.getMappingFromString(sourceStr.toString());
				if (mappingIdx != -1) // double check this is a valid mapping
				{
					sendCommandExpectResponse(cfg.mode.getItemModeCommandSetString(sourceStr.toString()));
					cmdSent = true;
				}
			}
			break;
		default:
			logger.error("Unexpected Item Mode!");
			break;
		}
				
		if (cmdSent == false)
		{
			logger.error("Unable to convert item command to projector state: Command="+c);
		}
	}
	
	private String sendCommandExpectResponse(String cmd)
	{	
		String respStr="";
		String tmp;
		if (this.projectorWriter != null)
		{
			this.projectorWriter.printf("%s", cmd);
			logger.debug("Sent command '"+cmd.replace("\r", "")+"'");
			try {
				tmp = this.projectorReader.readLine();		
				while (tmp != null)
				{					
					if (tmp.startsWith("*")==true && tmp.endsWith("#"))
					{						
						/* got response */
						logger.debug("Response: '"+tmp+"'");
						respStr = tmp;
						break;
					}
					tmp = this.projectorReader.readLine();
				}
			} catch (SocketTimeoutException e) {
				logger.warn("Timed out reading response from projector");
			} catch (IOException e) {
				logger.error("IO Exception while reading response from projector: "+e.getMessage());
			}
			
		} else {
			logger.debug("Not sending command to projector as connection is not configured yet.");
		}
		return respStr;
	}
}
