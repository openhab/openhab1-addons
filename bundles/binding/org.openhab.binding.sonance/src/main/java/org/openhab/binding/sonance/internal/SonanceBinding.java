/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonance.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.sonance.SonanceBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Laurens Van Acker
 * @since 1.8.0
 */
public class SonanceBinding extends AbstractActiveBinding<SonanceBindingProvider> {
	private Map<String, Socket> socketCache = new HashMap<String, Socket>();
	private Map<String, DataOutputStream> outputStreamCache = new HashMap<String, DataOutputStream>();
	private Map<String, BufferedReader> bufferedReaderCache = new HashMap<String, BufferedReader>();
	private Map<String, ReentrantLock> volumeLocks = new HashMap<String, ReentrantLock>();
	
	private static final Logger logger = 
		LoggerFactory.getLogger(SonanceBinding.class);

	/** 
	 * the refresh interval which is used to poll values from the Sonance
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
		
	public SonanceBinding() {
	}
	
	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null
			
		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString))
			refreshInterval = Long.parseLong(refreshIntervalString);

		setProperlyConfigured(true);
	}
	
	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString))
			refreshInterval = Long.parseLong(refreshIntervalString);
	}
	
	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
     * <li> 1 – The component was disabled
     * <li> 2 – A reference became unsatisfied
     * <li> 3 – A configuration was changed
     * <li> 4 – A configuration was deleted
     * <li> 5 – The component was disposed
     * <li> 6 – The bundle was stopped
     * </ul>
	 */
	public void deactivate(final int reason) {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
		socketCache = null;
		outputStreamCache = null;
		bufferedReaderCache = null;
	}

	
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Sonance Refresh Service";
	}
	
	@Override
	protected void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing Sonance binding configuration => refresh cycle aborted!");
			return;
		}
	
		logger.info("Refreshing all items");

		for (SonanceBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				try {
					String group = ((SonanceBindingProvider)provider).getGroup(itemName);
					String ip = ((SonanceBindingProvider)provider).getIP(itemName);
					int port = ((SonanceBindingProvider)provider).getPort(itemName);
	
					String key = ip + ":" + port;
	
					if (!socketCache.containsKey(key)) {
						socketCache.put(key, new Socket(ip, port));
						outputStreamCache.put(key, new DataOutputStream(socketCache.get(key).getOutputStream()));
						bufferedReaderCache.put(key, new BufferedReader(new InputStreamReader(socketCache.get(key).getInputStream())));
		            	logger.info("New socket created");
					}		
					
			        if (((SonanceBindingProvider)provider).isMute(itemName))
				        sendMuteCommand(itemName, "FF550212" + group, outputStreamCache.get(key), bufferedReaderCache.get(key));
			        else if (((SonanceBindingProvider)provider).isVolume(itemName))
				        sendVolumeCommand(itemName, "FF550210" + group, outputStreamCache.get(key), bufferedReaderCache.get(key));
				} catch (UnknownHostException e) {
					logger.error("UnknownHostException occured.");
				} catch (IOException e) {
					logger.error("IOException occured");
				}			        
			}
		}		
	}

	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		
		for (Map.Entry<String, Socket> entry : socketCache.entrySet())
			try {
				entry.getValue().close();
			} catch (IOException e) {
				logger.error("Can't close a socket when binding changed.");
			}
			
		// Cleanup all sockets
		socketCache.clear();
		outputStreamCache.clear();
		bufferedReaderCache.clear();
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.info("Command received ({}, {})", itemName, command);
		
		SonanceBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String group = ((SonanceBindingProvider)provider).getGroup(itemName);
		String ip = ((SonanceBindingProvider)provider).getIP(itemName);
		int port = ((SonanceBindingProvider)provider).getPort(itemName);
		
        Socket s = null;
		try {
			s = new Socket(ip, port);
	        DataOutputStream outToServer  = new DataOutputStream(s.getOutputStream());
	        BufferedReader i   			  = new BufferedReader(new InputStreamReader(s.getInputStream()));

	        if (((SonanceBindingProvider)provider).isMute(itemName))
	        	   switch (command.toString()) {
		               case "OFF":
		            	   sendMuteCommand(itemName, "FF550207" + group, outToServer, i); // Mute on
		                        break;
		               case "ON":
		            	   sendMuteCommand(itemName, "FF550208" + group, outToServer, i); // Mute off
		                        break;
		               default:
		            	   logger.error("I don't know what to do with this command");
		                        break;
	        	   }
	        else if (((SonanceBindingProvider)provider).isVolume(itemName))
	        	if (command.toString().equals("UP"))
	        			sendVolumeCommand(itemName, "FF550204" + group, outToServer, i);
	        	else if (command.toString().equals("DOWN"))
	        			sendVolumeCommand(itemName, "FF550205" + group, outToServer, i);	        	
	        	else {
	        		try { 
	        			setVolumeCommand(itemName, group, Integer.parseInt(command.toString()), outToServer, i, ip + ":" + port);
	        		} catch(NumberFormatException nfe) {  
	        			logger.error("I don't know what to do with this command");  
	        		}  	        			
	        		
	        	}
            s.close();            
		} catch (IOException e) {
			logger.error("IO Exception");
		} finally {
				try {
					if (s!=null)
						s.close();
				} catch (IOException e) {}
		}
	}
	
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.info("Update received ({},{})", itemName, newState);
	
		SonanceBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String group = ((SonanceBindingProvider)provider).getGroup(itemName);
		String ip = ((SonanceBindingProvider)provider).getIP(itemName);
		int port = ((SonanceBindingProvider)provider).getPort(itemName);

		ip = null;
		group = null; // cleanup
		
        Socket s = null;
		try {
			s = new Socket(ip, port);
	        DataOutputStream outToServer  = new DataOutputStream(s.getOutputStream());
	        BufferedReader i   			  = new BufferedReader(new InputStreamReader(s.getInputStream()));

	        if (((SonanceBindingProvider)provider).isMute(itemName))
	        	   if (newState.equals(OnOffType.OFF))
	        		   sendMuteCommand(itemName, "FF550207" + group, outToServer, i); // Mute on
	        	   else if (newState.equals(OnOffType.ON))
	        		   sendMuteCommand(itemName, "FF550208" + group, outToServer, i); // Mute off
	        	   else
		            	logger.error("I don't know what to do with this command");
	        else if (((SonanceBindingProvider)provider).isVolume(itemName))
	        	if (newState.equals(IncreaseDecreaseType.INCREASE))
	        			sendVolumeCommand(itemName, "FF550204" + group, outToServer, i);
	        	else if (newState.equals(IncreaseDecreaseType.DECREASE))
	        			sendVolumeCommand(itemName, "FF550205" + group, outToServer, i);	        	
	        	else
	        			logger.error("I don't know what to do with this command: " + newState);
            s.close();            
		} catch (IOException e) {
			logger.error("IO Exception");
		} finally {
				try {
					if (s!=null)
						s.close();
				} catch (IOException e) {}
		}		
	}

	private void sendVolumeCommand(String itemName, String command, DataOutputStream outToServer, BufferedReader i) throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters
		
        outToServer.write(hexStringToByteArray(command));
        i.read(cbuf, 0, 50);

        Pattern p = Pattern.compile(".*Vol=(-?\\d{1,2}).*");
        Matcher m = p.matcher(new String(cbuf)); 

        if (m.find())
        	eventPublisher.postUpdate(itemName, new DecimalType(m.group(1)));
        else
        	logger.error("Error sending command, received this: " + new String(cbuf));        
	}

	private void sendMuteCommand(String itemName, String command, DataOutputStream outToServer, BufferedReader i) throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters

    	//logger.debug("Sending command {}", command);
        outToServer.write(hexStringToByteArray(command));
        i.read(cbuf, 0, 50);

        String result = new String(cbuf);
        
        if (result.contains("Mute=on") || result.contains("MuteOn"))
    		eventPublisher.postUpdate(itemName, OnOffType.OFF);
        else if (result.contains("Mute=off") || result.contains("MuteOff"))
    		eventPublisher.postUpdate(itemName, OnOffType.ON);        	
        else
        	logger.error("Error sending command, received this: " + new String(cbuf));
	}
	
	private void setVolumeCommand(String itemName, String group, int targetVolume, DataOutputStream outToServer, BufferedReader i, String endpoint) throws IOException {
        char[] cbuf = new char[50]; // Response is always 50 characters

        // Now lock this part, so we don't end up with two functions going up and now all the time
        String lockKey = endpoint + ":" + group;
        if (!volumeLocks.containsKey(lockKey))
        	volumeLocks.put(lockKey, new ReentrantLock()); // We can keep this one when we finished with it, speed before memory usage
        volumeLocks.get(lockKey).lock();
        
        try {
	        outToServer.write(hexStringToByteArray("FF550210" + group));       
	        i.read(cbuf, 0, 50);
	        
	        Pattern p = Pattern.compile(".*Vol=(-?\\d{1,2}).*");
	        Matcher m = p.matcher(new String(cbuf));
	
	        if (m.find()) {
	        	double currentVolume = Integer.parseInt(m.group(1));
	        	eventPublisher.postUpdate(itemName, new DecimalType(currentVolume));
	        	int step = 0; // We should be able to reach every volume in less 29 steps
	        	while (currentVolume != targetVolume && step++<=28) {
	        		logger.debug("Current volume: {}, target volume: {}, step: {})", currentVolume, targetVolume, step);
	        		if (currentVolume+3 <= targetVolume)
	        			outToServer.write(hexStringToByteArray("FF55020E" + group)); // Volume UP +3
	        		if (currentVolume-3 >= targetVolume)
	        			outToServer.write(hexStringToByteArray("FF55020F" + group)); // Volume Down -3
	        		else if (currentVolume < targetVolume)
	        			outToServer.write(hexStringToByteArray("FF550204" + group)); // Volume UP
	        		else
	        			outToServer.write(hexStringToByteArray("FF550205" + group)); // Volume Down
	        		i.read(cbuf, 0, 50);
	        		m = p.matcher(new String(cbuf));
	                if (m.find()) {
	                	currentVolume = Integer.parseInt(m.group(1));
	                	logger.info("Setting volume, current volume : " + currentVolume);
	                	eventPublisher.postUpdate(itemName, new DecimalType(currentVolume));
	                } else
	                	logger.error("Error sending command, received this: " + new String(cbuf));        		
	        	}
	        } else
	        	logger.error("Error sending command, received this: " + new String(cbuf));
    	} finally {
    		volumeLocks.get(lockKey).unlock();
    	}
	}
	
	protected SonanceBindingProvider findFirstMatchingBindingProvider(String itemName) {
		SonanceBindingProvider firstMatchingProvider = null;
		for (SonanceBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}
	
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        return data;
    }
}