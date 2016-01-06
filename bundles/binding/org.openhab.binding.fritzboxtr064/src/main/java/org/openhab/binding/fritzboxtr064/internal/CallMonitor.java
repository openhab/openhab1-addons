/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.fritzboxtr064.FritzboxTr064BindingProvider;
import org.openhab.binding.fritzboxtr064.internal.FritzboxTr064GenericBindingProvider.FritzboxTr064BindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.library.tel.types.CallType;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Wrapper class which handles all data/comm. when call monitoing is used
 * Thread control class
 * @author gitbock
 * @since 1.8.0
 */
public class CallMonitor extends Thread{
	
	//port number to connect at fbox
	private final int _DEFAULT_MONITOR_PORT = 1012;
	
	// Event Publisher from parent Generic Binding
	// to be able to pass item updates within this class
	protected EventPublisher _eventPublisher;
	
	// Default openhab Logger
	protected final static Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);
	
	// Main Monitor Thread receiving fbox messages
	protected CallMonitorThread _monitorThread;
	
	//ip and port to connect
	protected String _ip;
	protected int _port;
	
	//Phonebook Manager to resolve phone numbers in names
	protected PhonebookManager _pbm;
	
	//Providers to be able to extract all required items
	private Collection<FritzboxTr064BindingProvider> _providers;
	
	protected static CallMonitor _instance;
	
	/***
	 * 
	 * @param url from openhab.cfg to connect to fbox
	 * @param ep eventPublisher to pass updates to items
	 * @param providers all items relevant for this binding
	 */
	public CallMonitor(String url, EventPublisher ep, Collection<FritzboxTr064BindingProvider> providers, PhonebookManager pbm ){
		this._eventPublisher = ep;
		this._ip = parseIpFromUrl(url);
		this._port = _DEFAULT_MONITOR_PORT;
		this._providers = providers;
		this._pbm = pbm;
		_instance = this;
	}
	
	
	/***
	 * In Main Config only the TR064 URL is provided. Need IP for Socket connection.
	 * Parses the IP from URL String
	 * @param url String
	 * @return IP address from url
	 */
	private String parseIpFromUrl(String url) {
		String ip = "";
		Pattern pat = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
		
		Matcher m = pat.matcher(url);
		if(m.find()){
			ip = m.group(2);
		}
		else{
			logger.error("Cannot get IP from FritzBox URL:  {}",url);
		}
		return ip;
	}


	
	
	/***
	 * reset the connection to fbox periodically
	 */
	public void setupReconnectJob(){
		try {
			//String cronPattern = "0 0 0 * * ?"; //every day
			//String cronPattern = "0 * * * * ?"; //every minute
			String cronPattern = "0 0 0/2 * * ?"; //every 2 hrs
			Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
                    
            JobKey jobKey = jobKey("Reconnect", "FritzBox");
            TriggerKey triggerKey = triggerKey("Reconnect", "FritzBox");
            
            if (sched.checkExists(jobKey)) {
                logger.debug("reconnection job already exists");
            } else {
                CronScheduleBuilder scheduleBuilder = 
                		CronScheduleBuilder.cronSchedule(cronPattern);
                
                JobDetail job = newJob(ReconnectJob.class).withIdentity(jobKey).build();

                CronTrigger trigger = newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();

                sched.scheduleJob(job, trigger);
                logger.debug("Scheduled reconnection job to FritzBox: {}",cronPattern);
            }
		} catch (SchedulerException e) {
			logger.warn("Could not create daily reconnection job", e);
		}
	}
	
	/***
	 * cancel the reconnect job
	 */
	public void shutdownReconnectJob() {
		Scheduler sched = null;
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
			JobKey jobKey = jobKey("Reconnect", "FritzBox");
		    if (sched.checkExists(jobKey)) {
		        logger.debug("Found reconnection job. Shutting down...");
		        sched.deleteJob(jobKey);
		    }
		
		} catch (SchedulerException e) {
			logger.warn("Error shutting down reconnect job: {}",e.getLocalizedMessage());
		}
	}

	/**
	 * A quartz scheduler job to simply do a reconnection to the FritzBox.
	 */
	public static class ReconnectJob implements Job {
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			Logger logger = LoggerFactory.getLogger(FritzboxTr064Binding.class);
			logger.info("Fritzbox Reconnect Job executed");
			_instance.stopThread();
			
			// create a new thread for listening to the FritzBox
			_instance._monitorThread = _instance.new CallMonitorThread();
			
			//Wait before reconnect
			try {
				sleep(5000L);
			} catch (InterruptedException e) {
				
			}
			logger.debug("Reconnect Job starts new monitor Thread");
			_instance._monitorThread.start();
			
		}
	}
	
	
	/***
	 * thread for setting up socket to fbox, listening for messages, parsing them
	 * and updating items. Most of this code is from Kai Kreuzers original 
	 * fritzbox binding!
	 * 
	 * @author gitbock
	 *
	 */
	public class CallMonitorThread extends Thread{
		
		/***
		 * Devnote: 
		 * Objects need to be set here, not in parent class!
		 * Otherwise compiler can see them, but at runtime wrong values are given(?) 
		 */
		
		//Socket to connect
		private Socket _socket; 
		
		//Thread control flag
		private boolean _interrupted = false;
		
		//time to wait before reconnecting
		private long _reconnectTime = 60000L;
		
		public CallMonitorThread() {
			
		}
		
		@Override
		public void run() {
			logger.debug("Callmonitor Thread [{}] is interrupted: {}", Thread.currentThread().getId(),_interrupted);
			while (!_interrupted) {
				if (_ip != null) {
					BufferedReader reader = null;
					try {
						logger.info("Callmonitor Thread [{}] attempting connection to FritzBox on {}:{}..",Thread.currentThread().getId(),_ip, _port);
						_socket = new Socket(_ip, _port);
						reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
						// reset the retry interval
						_reconnectTime = 60000L;
					} catch (Exception e) {
						logger.warn("Error attempting to connect to FritzBox. Retrying in {}s", _reconnectTime / 1000L, e);
						try {
							Thread.sleep(_reconnectTime);
						} catch (InterruptedException ex) {
							_interrupted = true;
						}
						// wait another more minute the next time
						_reconnectTime += 60000L;
					}
					if (reader != null) {
						logger.info("Connected to FritzBox on {}:{}", _ip, _port);
						while (!_interrupted) {
							try {
								String line = reader.readLine();
								if (line != null) {
									logger.debug("Received raw call string from fbox: {}",line);
									CallEvent ce = new CallEvent(line);
									if(ce.parseRawEvent()){
										handleCallEvent(ce);
									}
									else{
										logger.error("Call Event could not be parsed!");
									}
									try {
										// wait a moment, so that rules can be
										// processed
										// see
										// http://knx-user-forum.de/openhab/25024-bug-im-fritzbox-binding.html
										sleep(100L);
									} catch (InterruptedException e) {
									}
								}
							} catch (IOException e) {
								if (_interrupted) {
									logger.info("Lost connection to Fritzbox because of interrupt");
								} else {
									logger.error("Lost connection to FritzBox", e);
								}
								break;
							}
							finally{
								// allow a few seconds until reconnect.
								// needed for interrupt state to settle?
								try {
									sleep(5000L);
								} catch (InterruptedException e) {
									
								}
							}
						}
					}
				}
			}
		}
		
		/**
		 * Handle call event and update item as required
		 * 
		 * @param ce call event to process
		 */
		private void handleCallEvent(CallEvent ce) {
			
			// Always try to resolve number to name. If not wanted return number instead later
			// pbm can be null, if no item wanted resolving!
			String callerName = "";
			if(_pbm != null){
				//resolving caller name if external number is present in call event
				if(ce.getExternalNo() == null || ce.getExternalNo().isEmpty()){
					 logger.debug("no external number provided by fbox. Will not resolve name");
				}
				else{
					logger.debug("resolving name for number {}", ce.getExternalNo());
					callerName = _pbm.getNameFromNumber(ce.getExternalNo(), 7);
					if(callerName == null){
						callerName = "Name not found for "+ce.getExternalNo(); //if no match was found, reset to number
					}
					else{
						logger.debug("external number resolved to: {}",callerName);
					}
				}
			}
			
			
			//cycle through all items
			logger.debug("Searching item to pass call event: {}",ce.getCallType());
			for (FritzboxTr064BindingProvider provider : _providers) { 
				for(String itemName : provider.getItemNames() ){ //check each item relevant for this binding		
					FritzboxTr064BindingConfig conf = provider.getBindingConfigByItemName(itemName); //config object for item
					Class<? extends Item> itemType = conf.getItemType(); //which type is this item?
					org.openhab.core.types.State state = null;
					String configString = conf.getConfigString();
					String externalInfo = null; //either name or number as requested by item
					//number name resolving wanted?
					if(configString.startsWith("callmonitor") && configString.contains("resolveName")){
						logger.debug("name resolving requested in item {}. Setting external no. to",itemName,callerName);
						externalInfo = callerName;
					}
					else{
						logger.debug("NO name resolving requested in item {}. Setting external no. to",itemName,callerName);
						externalInfo = ce.getExternalNo();
					}
					if (ce.getCallType().equals("DISCONNECT")) {
						//1.12.05⌴12:00:10;DISCONNECT;0;5;
						//reset states of callmonitor items to 0 for ALL items regardless of type
						if(configString.startsWith("callmonitor_ringing") || configString.startsWith("callmonitor_active") || configString.startsWith("callmonitor_outgoing")){
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.OFF : CallType.EMPTY;
						}
					}
					else if (ce.getCallType().equals("RING")) { //first event when call is incoming
						//1.12.05⌴12:00:15;RING;0;5551234;5556789;SIP0;
						if(configString.startsWith("callmonitor_ringing")){
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON : new CallType(ce.getInternalNo(), externalInfo );
						}
					}
					else if (ce.getCallType().equals("CONNECT")){ //when call is answered/running
						//1.12.05⌴12:00:05;CONNECT;0;0;0180537489269;
						if(configString.startsWith("callmonitor_active")){ // only for "active" items
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON : new CallType(externalInfo, ce.getInternalNo());
						}
					}
					else if (ce.getCallType().equals("CALL")){ //outgoing call
						//1.12.05⌴12:00:00;CALL;0;0;5557890;0180537489269;ISDN;
						if(configString.startsWith("callmonitor_outgoing")){
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON : new CallType(externalInfo, ce.getInternalNo() );
						}
					}
					
					if (state != null) {
						logger.debug("Dispatching call type {} to item {} as {}", ce.getCallType(),itemName,state.toString());
						_eventPublisher.postUpdate(itemName, state);
					}
					else{
						logger.debug("Could not determine state for item {}. Not relevant!", itemName);
					}
				}
			}
		}

		
		/**
		 * Close socket and stop running thread
		 */
		public void interrupt() {
			_interrupted = true;
			if (_socket != null) {
				try {
					
					_socket.close();
					logger.debug("Socket to FritzBox closed");
				} catch (IOException e) {
					logger.warn("Existing connection to FritzBox cannot be closed", e);
				}
			}
			else{
				logger.debug("Socket to FritzBox not open. Not closing.");
			}
		}

	}
	

	public void stopThread() {
		logger.debug("Stopping monitor Thread...");
		if (_monitorThread != null) {
			_monitorThread.interrupt();
			_monitorThread = null;
		}
		
	}


	public void startThread() {
		logger.debug("Starting monitor Thread...");
		if (_monitorThread != null) {
			logger.warn("Old monitor Thread was still running");
			// let's end the old thread
			_monitorThread.interrupt();
			_monitorThread = null;
		}
		// create a new thread for listening to the FritzBox
		_monitorThread = new CallMonitorThread();
		_monitorThread.start();
		
	}
	
}
