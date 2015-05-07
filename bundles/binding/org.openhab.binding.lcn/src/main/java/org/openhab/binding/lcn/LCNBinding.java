/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.lcn.internal.LCNActivator;
import org.openhab.binding.lcn.logic.LCNParser;
import org.openhab.binding.lcn.logic.LCNParserException;
import org.openhab.binding.lcn.logic.LCNUtil;
import org.openhab.binding.lcn.logic.data.LCNChannel;
import org.openhab.binding.lcn.logic.data.LCNChannelList;
import org.openhab.binding.lcn.logic.data.LCNInputModule;
import org.openhab.binding.lcn.logic.data.LCNInputModule.EngineStatus;
import org.openhab.binding.lcn.logic.data.LCNInputModule.ModuleType;
import org.openhab.binding.lcn.logic.data.LCNInputModule.Threshold;
import org.openhab.binding.lcn.logic.data.LCNItemBinding;
import org.openhab.binding.lcn.logic.data.LCNOutputElement;
import org.openhab.binding.lcn.logic.data.LCNSyntax;
import org.openhab.binding.lcn.logic.data.LCNSyntax.DataType;
import org.openhab.binding.lcn.logic.data.LCNSyntax.VarModifier;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles all necessary traffic from and to LCNModules.
 * It implements the actual Binding which is later used via openHAB.
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNBinding<P extends LCNBindingProvider> extends AbstractActiveBinding<P> implements ManagedService {

	/**Charset for the LCN-module.*/
	private static final String CHARSET = "UTF-8";
	/**The System encoding to read certain information.*/
	private static final String SYSTEM_CHARSET = System.getProperty("file.encoding");
	/**Suffix for all commands that are being sent.*/
	private static final String SUFFIX = "\r\n";
	/**Cron String for reconnects.*/
	private static final String RECONNECT_TIMER = "0 0 0 * * ?";
	/**The readbuffers size.*/
	private static final int READ_BUFFER = 1024;
	/**Time before a reconnect shall commence.*/
	private static final int RECONNECT_INTERVAL = 5;
	/**Pattern to identify mappings in item bindings.*/
	private static final Pattern MAPPING_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");
	/**The logger handles output.*/
	private static final Logger logger = LoggerFactory.getLogger(LCNBinding.class);
	/**Refresh Interval for the LCNBinding.*/
	private long refreshInterval = 100;
	/**Timeout for data requests.*/
	private long dataRequestTimeout = 2000;
	/**Timer for pings in seconds.*/
	private int pingTimer = 600;
//	/**The maximum write operations that be be commenced per cycle, 0 = unlimited.*/
//	private int writeLimit = 0;
	
	/**HashMap for 'old' modules, for which data has to be requested on a regular basis.*/
	private LCNRequestList requestModules = new LCNRequestList();
	/**Selector used to handle channels.*/
	private Selector selector;
	/**List of OutputELements(commands) that have to be send.*/
	private  List<LCNOutputElement> outputList =  Collections.synchronizedList(new ArrayList<LCNOutputElement>());
	/**HashMap for credentials needed to connect to the LCN bus.*/
	private HashMap<String, Credentials> credentialMap = new HashMap<String, Credentials>();
	/**List of LCNChannels of the binding.*/
	private LCNChannelList lcnChannels = new LCNChannelList();
	/**Counter for data requests.*/
	private long dataCounter = 1000;
	/**Last time that a request was sent.*/
	private long lastRequestTime = 0;
	/**Last time that a firmware was requested*/
	private long lastFirmwareTime = 0;
	/**Minimum time that a request job may take, can not be lower than 5000 (in ms) = 5 seconds.*/
	private int minRequestTime = 5000;
	/**Maximum time that a request job may take, can be no higher than 600000 (in ms) = 10 minutes.*/
	private int maxRequestTime = 600000;
	/**A list with firmwares for the used modules, useful for some commands that depend on different firmwares.*/
	private Map<String, String> firmwares = new HashMap<String, String>();
	
	/**
	 * Represents a single username-password combination.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public static class Credentials {
		
		/**The username of a connection.*/
		public String username;
		/**The password of a connection.*/
		public String password;
		/**The IP of the credentials.*/
		public String ip;
		/**The port of the credentials.*/
		public String port;
		/**The state of a connection.*/
		public boolean isConnected = false;
		/**The home segment id.*/
		public int homeSegment = 0;
		/**Flag that is to be set for the 'new' dim mode aka 0-200.*/
		public DimMode newDimMode = DimMode.UNKNOWN;
		
		public static enum DimMode {
			DEFAULT, ADVANCED, UNKNOWN;
		}
		
	}
	
	/**
	 * A simple Job that will Ping the PCK software every now and again, to make sure, that channels are not closed.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public static class PingJob implements Job {
		
		public void execute(JobExecutionContext context) throws JobExecutionException {
			
			JobDataMap map = context.getJobDetail().getJobDataMap();
			
			@SuppressWarnings("rawtypes")
			LCNBinding parent = (LCNBinding) map.get("Parent");
			LCNChannel chan = (LCNChannel) map.get("Channel");
			
			parent.quickInternalCommand(LCNSyntax.Command.RAW + "." + "^ping1", chan, null, true);
			
		}	
		
	}
	
	/**
	 * A simple Job that will request data for modules, which do not support auto-updates.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public static class RequestJob implements Job {
		
		/**
		 * Returns an apropriate JobKey for the Module.
		 * @param mod The LCNInputModule
		 * @return The JobKey as String
		 */
		public static String getJobKey(LCNInputModule mod) {
			
			String result = "";
			result = mod.id + mod.LcnShort;
			return result;
			
		}
		
		/**
		 * Returns the job group for requests.
		 * @return The name of the job group.
		 */
		public static String getJobGroup() {
			return "LCNRequest";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			
			JobDataMap map = context.getJobDetail().getJobDataMap();
			
			@SuppressWarnings("rawtypes")
			LCNBinding parent = (LCNBinding) map.get("Parent");
			LCNInputModule mod = (LCNInputModule) map.get("Module");
			
			synchronized(parent.requestModules) {
			
				int homeSegment = ((Credentials) parent.credentialMap.get(mod.id)).homeSegment;
				
				//Wait until the job gets an open spot, since there can only be one active request at a time.
				while (parent.requestModules.locked || null == parent.firmwares.get(LCNInputModule.generateKey(mod, homeSegment))) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.debug("Problem with sleeping RequestJob: {}", e.getMessage());
					}
				}				
			
				//update global request status.
				parent.requestModules.locked = true;
				parent.lastRequestTime = System.currentTimeMillis();
				parent.requestModules.remove(mod, homeSegment);
				parent.requestModules.add(0, mod);
			
				for (Object oProvider : parent.providers) {
				
					LCNBindingProvider provider = (LCNBindingProvider) oProvider;
					
					for (String name : provider.getItemNames()) {
						
						LCNInputModule pMod = provider.getFirstModule(name);
						
						if (null != pMod) {
							
							if (null != pMod && pMod.equals(mod, true, homeSegment) && pMod.outlet == mod.outlet && null != provider.getItemNamesByModule(mod, homeSegment) && !provider.getItemNamesByModule(mod, homeSegment).isEmpty()) {
								
								LCNChannel chan = parent.lcnChannels.get(provider.getAddress(provider.getItemNamesByModule(mod, homeSegment).get(0), 
										provider.getOpenHabCommands(provider.getItemNamesByModule(mod, homeSegment).get(0)).get(0), parent.credentialMap));
								
								if ((null != parent.credentialMap.get(mod.id) || ((Credentials) parent.credentialMap.get(mod.id)).isConnected)) {
									
									try {
									
										if (mod.type == LCNInputModule.ModuleType.THRESHOLD) {
											parent.quickInternalCommand(LCNParser.moduleToThresholdRequest(mod, (String) (parent.firmwares.get(LCNInputModule.generateKey(mod, homeSegment))), homeSegment), chan, LCNInputModule.generateKey(mod, homeSegment), false);
											logger.debug("Job sends request for job: {}", context.getJobDetail().getKey());
										} else if (mod.type == LCNInputModule.ModuleType.DATA) {
											parent.quickInternalCommand(LCNParser.moduleToDataRequest(parent.requestModules.get(0), homeSegment), chan, LCNInputModule.generateKey(parent.requestModules.get(0), homeSegment), false);
											logger.debug("Job sends request for job: {}", context.getJobDetail().getKey());
										} else if (mod.type == LCNInputModule.ModuleType.INFO && null != mod.LcnShort) {
											parent.quickInternalCommand(LCNSyntax.Command.RAW + "." + LCNParser.parse(mod.LcnShort, ((Credentials) parent.credentialMap.get(mod.id)).homeSegment), chan, LCNInputModule.generateKey(parent.requestModules.get(0), homeSegment), false);
											logger.debug("Job sends request for job: {}", context.getJobDetail().getKey());
										} else if (mod.type == LCNInputModule.ModuleType.OEM || mod.type == LCNInputModule.ModuleType.LED || mod.type == LCNInputModule.ModuleType.ENGINE_REPORT) {
											parent.quickInternalCommand(LCNSyntax.Command.RAW + "." + LCNParser.parse(mod.LcnShort, ((Credentials) parent.credentialMap.get(mod.id)).homeSegment), chan, LCNInputModule.generateKey(parent.requestModules.get(0), homeSegment), false);
											logger.debug("Job sends request for job: {}", context.getJobDetail().getKey());
										} 											
									
									} catch (LCNParserException exc) {
										
										logger.debug("Unable to parse a request for: {}", mod.LcnShort);
										
									} 
									
								}
								
								break;
								
							}
						
						}
						
					}
				
				}
				
				//update timer again(!), to have accurate timeouts.
				parent.lastRequestTime = System.currentTimeMillis();
			
			}	
			
		}
		
	}
	
	/**
	 * Used to reliably gather firmware information.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 *
	 */
	public static class FirmwareJob implements Job {

		/**This integer defines the maximum amount of calls this job accepts before selfdestruction.
		 * This is uesd in order to prevent constant request 'spam' if a module is not responding.*/
		private static final int CALL_LIMIT = 10;
		
		/**This counter will be incremented every time the job is (effectively) fired.*/
		private int callCounter = 0;
		
		/**
		 * Returns an apropriate JobKey for the Module.
		 * @param mod The LCNInputModule
		 * @param homeSegment The homeSegment for the LCNBus.
		 * @return The JobKey as String
		 */
		public static String getJobKey(LCNInputModule mod, int homeSegment) {
			
			int seg = mod.segment;
			if (seg == homeSegment) {
				seg = 0;
			}
			
			String result = "";
			result = mod.id + seg + mod.module;
			return result;
			
		}
		
		/**
		 * Returns the job group for requests.
		 * @return The name of the job group.
		 */
		public static String getJobGroup() {
			return "LCNFirmware";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			
			JobDataMap map = context.getJobDetail().getJobDataMap();
			
			@SuppressWarnings("rawtypes")
			LCNBinding parent = (LCNBinding) map.get("Parent");
			LCNInputModule mod = (LCNInputModule) map.get("Module");
			LCNChannel chan = (LCNChannel) map.get("Channel");
			
			int homeSegment = ((Credentials) parent.credentialMap.get(mod.id)).homeSegment;
			
			while (System.currentTimeMillis() - parent.lastFirmwareTime < 1000) {
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.debug("Problem with sleeping FirmwareJob: {}", e.getMessage());
				}
				
			}
			
			parent.lastFirmwareTime = System.currentTimeMillis();
			
			if (null != parent.firmwares.get(LCNInputModule.generateKey(mod, homeSegment)) || this.callCounter > CALL_LIMIT) {
				
				if (this.callCounter > CALL_LIMIT && null == parent.firmwares.get(LCNInputModule.generateKey(mod, homeSegment))) {
					
					logger.warn("Reading firmware for {} | {} failed, module is not responding!", mod.segment, mod.module);
					
				} else {
					
					logger.debug("Reading firmware for {} | {} was successful!", mod.segment, mod.module);
					
				}
				
				Scheduler sched;
				
				try {
					sched = StdSchedulerFactory.getDefaultScheduler();
					sched.deleteJob(JobKey.jobKey(FirmwareJob.getJobKey(mod, homeSegment), FirmwareJob.getJobGroup()));
				} catch (SchedulerException e) {
					logger.debug("Problems with the FirmwareJobs...");
				}
				
			} else {
				
				parent.quickInternalCommand(LCNParser.moduleToSerialRequest(mod, homeSegment), chan, LCNInputModule.generateKey(mod, homeSegment), true);
				logger.debug("Requesting firmware for {} | {}....", mod.segment, mod.module);
				this.callCounter++;
			
			}
			
			parent.lastFirmwareTime = System.currentTimeMillis();
			
		}
		
	}
	
	/**
	 * A simple QuartzJob to reconnect channels.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 *
	 */
	public static class ReconnectChannelJob implements Job {

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			
			logger.debug("Attempting to reconnect a channel...");
			
			JobDataMap map = context.getJobDetail().getJobDataMap();
			LCNBinding binding = (LCNBinding) map.get("Binding");
			LCNChannel chan = (LCNChannel) map.get("Channel");

			if(chan.isReconnecting) {

				logger.debug("...isReconnecting...");
				
				if(null != chan.remote && !chan.channel.isOpen()) {

					logger.debug("...waiting...");
					
					SelectionKey sKey = chan.channel.keyFor(binding.selector);
					
					if(sKey != null) {		
						logger.debug("...deleting old key...");
						sKey.cancel();						
					}

					try {
						
						logger.debug("...resetting channel...");
						chan.channel.close();
						chan.channel = SocketChannel.open();
						chan.buffer = null;
						chan.channel.configureBlocking(false);
						
					} catch (IOException e) {
						
						logger.error("An exception occurred while closing a channel: {}",e.getMessage());
						
					}

					synchronized(binding.selector) {
						
						binding.selector.wakeup();
						
						try {
							
							if(chan.channel != null) {	
								logger.debug("...registering channel...");
								chan.channel.register(binding.selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT));								
							}
							
						} catch (ClosedChannelException exc) {
							
							logger.error("Unable to register channel! ({})", chan.remote.getHostString());
							
						}
						
					}

					try {
						
						if(null != chan.channel) {
							
							chan.channel.connect(chan.remote);
							logger.info("Attempting to reconnect the channel for {}",chan.remote);
							
						}
						
					} catch (Exception e) {
						
						logger.error("An exception occurred while connecting a channel: {}",e.getMessage());
						
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Called whenever the configuration of the LCNBinding is updated.<br>
	 * @param config The updated configurations.
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		
		if (config != null) {
			
			logger.info("Loading LCN config...");
			
			//default values
			this.credentialMap.clear();
			this.pingTimer = 600;
			this.dataRequestTimeout = 2000;
			this.refreshInterval = 100;
			
			String id = null;
			String ip = null;
			String username = null;
			String password = null;
			String mode = null;
			int counter = 0;
			
			while (counter == 0 || (StringUtils.isNotBlank(id) || StringUtils.isNotBlank(ip) || StringUtils.isNotBlank(username) || StringUtils.isNotBlank(password))) {
				
				if (counter != 0) {
					
					Credentials cred = new Credentials();
					cred.username = username;
					cred.password = password;
					
					if (!ip.contains(":")) {
						cred.port = "4114";
						cred.ip = ip;
					} else {
						cred.port = ip.split(":")[1];
						cred.ip = ip.split(":")[0];
					}
					
					try {
						InetAddress adr = InetAddress.getByName(cred.ip);
						cred.ip = adr.getHostAddress();
					} catch (UnknownHostException e) {
						logger.warn("Unable to read address: " + ip);
					}
					
					if (StringUtils.isNotBlank(mode)) { 
						if (mode.equalsIgnoreCase("advanced")) {
							cred.newDimMode = Credentials.DimMode.ADVANCED;
						} else if (mode.equalsIgnoreCase("default")) {
							cred.newDimMode = Credentials.DimMode.DEFAULT;
						} else {
							cred.newDimMode = Credentials.DimMode.UNKNOWN;
						}
					} else {
						cred.newDimMode = Credentials.DimMode.UNKNOWN;
					}
					
					this.credentialMap.put(id, cred);
					
				}
				
				counter++;
				id = (String) config.get("id" + counter);
				ip = (String) config.get("address" + counter);
				username = (String) config.get("username" + counter);
				password = (String) config.get("password" + counter);
				mode = (String) config.get("mode" + counter);
				
				checkCredentialError(id, ip, username, password, counter);
				
			}
			
			String ping = (String) config.get("ping");
			if (StringUtils.isNotBlank(ping)) {
				
				try {
					int iPing = Integer.valueOf(ping);
					this.pingTimer = iPing;
				} catch (NumberFormatException exc) {
					logger.warn("Invalid declaration of the 'ping' parameter in the config!");
				}
				
			}
			
			String requestTimeout = (String) config.get("timeout");
			
			if (StringUtils.isNotBlank(requestTimeout)) {
				
				try {
					this.dataRequestTimeout = Integer.parseInt(requestTimeout);
				} catch (NumberFormatException exc) {
					logger.warn("Invalid declaration of the 'timeout' parameter in the config!");
				}
				
			}
			
			String refreshIntervall = (String) config.get("refresh");
			
			if (StringUtils.isNotBlank(refreshIntervall)) {
				
				try {
					this.refreshInterval = Integer.parseInt(refreshIntervall);
				} catch (NumberFormatException exc) {
					logger.warn("Invalid declaration of the 'refresh' parameter in the config!");
				}
				
			}
			
			setProperlyConfigured(true);
			
		}
		
	}
	
	/**
	 * Checks the credentials for errors (i.e. if some of them are missing)
	 * If there are missing credentials, print an error stating which of them are missing.
	 * @param id The id that is to be checked.
	 * @param ip The address that is to be checked.
	 * @param username The username that is to be checked.
	 * @param password The password that is to be checked.
	 */
	private void checkCredentialError(String id, String ip, String username, String password, int counter) {
		
		if (StringUtils.isBlank(id)
				&& (StringUtils.isNotBlank(ip) || StringUtils.isNotBlank(username) || StringUtils.isNotBlank(password))) {
			
			logger.error("Credentials #{} are missing an ID!", counter);
			
		}
		
		if (StringUtils.isBlank(ip)
				&& (StringUtils.isNotBlank(id) || StringUtils.isNotBlank(username) || StringUtils.isNotBlank(password))) {
			
			logger.error("Credentials #{} are missing an address!", counter);
			
		}
		
		if (StringUtils.isBlank(username)
				&& (StringUtils.isNotBlank(id) || StringUtils.isNotBlank(ip) || StringUtils.isNotBlank(password))) {
			
			logger.error("Credentials #{} are missing a username!", counter);
			
		}
		
		if (StringUtils.isBlank(password)
				&& (StringUtils.isNotBlank(id) || StringUtils.isNotBlank(ip) || StringUtils.isNotBlank(username))) {
			
			logger.error("Credentials #{} are missing a password!", counter);
			
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		
		LCNBindingProvider provider = getProvider(itemName);

		if (null == provider) {
			
			logger.warn("Item {} does not have a LCNBindingProvider!", itemName);
			
		} else if (null != command) {
	
			List<Command> commands = provider.getCommands(itemName, command);

			if (commands.isEmpty()) {
				
				logger.debug("No LCN-Command for openHAB Command '{}' found.", command);
				
			}
			
			for(Command cmd : commands) {

				LCNChannel chan = lcnChannels.get(provider.getAddress(itemName, cmd, credentialMap));

				if (null != chan && null != chan.channel) {

					internalReceiveChanneledCommand(itemName, cmd, chan, command.toString());

				} else {
					
					logger.error("Can not issue command ({}) for item ({})", command, itemName);
					
				}
				
			}  
		
		} 
		
	}
	
	/**
	 * Sends a command to the LCN bus (via the normal output writer list though).<br>
	 * This is exclusively being used for item bound commands, as mappings will get resolved!
	 * @param itemName The name of the item.
	 * @param cmd The openHAB command.
	 * @param chan The channel to write to.
	 * @param sCmd The actual command as String.
	 */
	private void internalReceiveChanneledCommand(String itemName, Command cmd, LCNChannel chan, String sCmd) {
		
		LCNBindingProvider provider = getProvider(itemName);
				
		if(null != cmd) {		
			
			String lcnCommand = parseLCNShort(provider.getOpenHabCmd(itemName, cmd), sCmd) + SUFFIX;
			
			if (lcnCommand.startsWith("SIMULATE:")) {
				
				String sim = lcnCommand.substring(9).trim();
				logger.debug("Recieved simulated input: {}", sim);
				parseInput(ByteBuffer.wrap(sim.getBytes()), chan);
				
			} else {
			
				ByteBuffer outputBuffer = null;
				
				try {
					
					outputBuffer = ByteBuffer.allocate(lcnCommand.getBytes(CHARSET).length);
					outputBuffer.put(lcnCommand.getBytes(CHARSET));
					int homeSegment = this.credentialMap.get(provider.getModule(itemName, cmd).id).homeSegment;
					send(outputBuffer, chan, LCNInputModule.generateKey(provider.getModule(itemName, cmd), homeSegment), false);
					
				} catch (UnsupportedEncodingException exc) {
					
					logger.warn("Unexpected encoding error!");
					
				}
			
			}

		}

	}
	
	/**
	 * Directly sends a command to the LCN bus (via the normal output writer list though).
	 * @param cmd The command.
	 * @param lCNChannel The channel to write to.
	 * @param key The key to the LCN Module to check back on firmware.
	 * @param priority True if the command has to be send right away (i.e. it is inserted as first element in the writer list).
	 */
	private void quickInternalCommand(String cmd, LCNChannel lCNChannel, String key, boolean priority) {
		
		if (cmd != null && !cmd.isEmpty()) {
			
			cmd = cmd + SUFFIX;
			
			ByteBuffer outputBuffer = null;
			
			try {
				
				outputBuffer = ByteBuffer.allocate(cmd.getBytes(CHARSET).length);
				outputBuffer.put(cmd.getBytes(CHARSET));
				send(outputBuffer, lCNChannel, key, priority);
				
			} catch (UnsupportedEncodingException exc) {
				
				logger.warn("Unexpected encoding error!");
				
			}
			
		}
		
	}
	
	/**
	 * Directly sends a command to the LCN bus (via the normal output writer list though).
	 * @param theBuffer The buffer with the content (the command).
	 * @param theChannel The channel to write on.
	 * @param key The key to the LCN Module to check back on firmware.
	 * @param priority True if the command has to be send right away (i.e. it is inserted as first element in the writer list).
	 */
	private void send(ByteBuffer theBuffer, LCNChannel theChannel, String key, boolean priority) {

		SocketChannel theSocketChannel = theChannel.channel;

		if(theBuffer != null) {
			
			if(theSocketChannel.isConnected()) {
				
				if (priority) {
					
					outputList.add(0, new LCNOutputElement(theChannel, theBuffer, key));
					
				} else {
					
					outputList.add(new LCNOutputElement(theChannel, theBuffer, key));
					
				}
				
			} else {
				
				logger.debug("Channel died!");
				
				reconnect(theSocketChannel, theChannel, true);
				outputList.add(new LCNOutputElement(theChannel, theBuffer, key));
				
			}
			
		}
			
	}
	
	/**
	 * If the binding is activated, open the Selector as well.
	 */
	@Override
	public void activate() {

		try {
			selector = Selector.open();
		} catch (IOException e) {
			logger.error("Unable to open the Selector!");
		}	
		
	}

	/**
	 * If the binding is deactivated, close the Selector as well.
	 */
	@Override
	public void deactivate() {

		try {
			selector.close();
		} catch (IOException e) {
			logger.error("Unable to close the Selector!");
		}

	}
	
	/**
	 * Parses input from the LCN bus into usable information for the openHAB system. 
	 */
	private void parseInput(ByteBuffer inputBuffer, LCNChannel chan){
		
		String input = "";
		String[] allInput = null;
		
		try {
			
			allInput = (new String(inputBuffer.array(), SYSTEM_CHARSET).split("\0")[0]).split("\n");
			input = allInput[0];
			logger.debug("Channel: " + chan.remote.getHostString() + " received input: " + input);
			
		} catch (UnsupportedEncodingException e) {
			
			logger.warn("Unable to read input for channel: {}", chan.remote.getHostString());
			
		}
		
		//Automatically send user name and password if asked for by the system.
		//Added a priority tag, to ensure that the credentials are send right away.
		if (input.contains("Username")) {		
			
			Credentials cred = null;
			
			for (String key : credentialMap.keySet()) {				
				
				if (credentialMap.get(key).ip.equals(chan.remote.getHostString()) && credentialMap.get(key).port.equals("" + chan.remote.getPort())) {
					cred = credentialMap.get(key);
					break;
				}
				
			}
			
			if (null != cred) {
				quickInternalCommand(LCNSyntax.Command.RAW + "." + cred.username, chan, null, true);
				logger.debug("Sending username: {}", cred.username);
			} else {
				logger.warn("No username found for IP: " + chan.remote.getHostString());
			}		
			
		}
		
		if (input.contains("Password")) {	
			
			Credentials cred = null;
			
			for (String key : credentialMap.keySet()) {
				
				if (credentialMap.get(key).ip.equals(chan.remote.getHostString()) && credentialMap.get(key).port.equals("" + chan.remote.getPort())) {
					cred = credentialMap.get(key);
					break;
				}
				
			}
			
			if (null != cred) {
				quickInternalCommand(LCNSyntax.Command.RAW + "." + cred.password, chan, null, true);
				logger.debug("Sending password: {}", cred.password);
			} else {
				logger.warn("No password found for IP: " + chan.remote.getHostString());
			}
			
		}
		
		//Order information about available segment couplers, right after a connection has been established.
		if (input.contains("connected")) {
			
			Credentials cred = this.credentialMap.get(getId(getCredential(chan.remote.getHostString())));
			if (null != cred) {
				cred.isConnected = true;
			}
			
			quickInternalCommand(LCNSyntax.Command.SK.toString(), chan, null, false);
			if (cred.newDimMode == Credentials.DimMode.ADVANCED) {
				quickInternalCommand(LCNSyntax.Command.NMODE.toString(), chan, null, false);
			} else if (cred.newDimMode == Credentials.DimMode.DEFAULT) {
				quickInternalCommand(LCNSyntax.Command.OMODE.toString(), chan, null, false);
			}
			
		} else if (input.contains("disconnected")) {
			
			Credentials cred = this.credentialMap.get(getId(getCredential(chan.remote.getHostString())));
			if (null != cred) {
				cred.isConnected = false;
			}
			
		}
		
		LCNInputModule lcnMod = LCNParser.parseInput(input, getId(getCredential(chan.remote.getHostString())));
		processModule(lcnMod, input, chan);
		
		//process the otherUpdates AFTER we have processed the first one, to ensure processing in correct (received) order.
		if (null != allInput) {
			
			for (int i = 1; i < allInput.length; i++) {
				parseInput(ByteBuffer.wrap(allInput[i].getBytes()), chan);
			}
			
		}	
		
	}
	
	/**
	 * Processes a single LCNInputModule with its input.
	 * @param lcnMod A LCNInputModule.
	 * @param input The input as String.
	 */
	private void processModule(LCNInputModule lcnMod, String input, LCNChannel chan) {
		
		if (null != lcnMod) {
				
			if (lcnMod.firmware != null) {
				
				int homeSegment = this.credentialMap.get(lcnMod.id).homeSegment;
				
				//add the firmware-mod-combination to a special registry...
				this.firmwares.put(LCNInputModule.generateKey(lcnMod, homeSegment), lcnMod.firmware);
				
				for (LCNBindingProvider prov : this.providers) {
					
					for (String name : prov.getItemNames()) {
						
						LCNInputModule pMod = prov.getFirstModule(name);
						
						if (null != pMod) {
							
							homeSegment = this.credentialMap.get(pMod.id).homeSegment;
							
							if (((prov.getFirstModule(name).type == LCNInputModule.ModuleType.THRESHOLD && prov.getFirstModule(name).thresholds != null) || prov.getFirstModule(name).type == LCNInputModule.ModuleType.INFO 
									|| prov.getFirstModule(name).type == LCNInputModule.ModuleType.OEM || prov.getFirstModule(name).type == LCNInputModule.ModuleType.LED || prov.getFirstModule(name).type == LCNInputModule.ModuleType.ENGINE_REPORT) 
									&& !this.requestModules.contains(prov.getFirstModule(name), homeSegment)) {
								this.requestModules.add(prov.getFirstModule(name));
								startRequest(prov.getFirstModule(name));
								
							}
						
						}
						
					}
					
				}
				
				//identify "old" modules, which need requests to be kept up to date.
				if ((LCNUtil.compareHexDate(lcnMod.firmware, "140C0E") < 0)) {
			
					for (LCNBindingProvider prov : this.providers) {
						
						for (String name : prov.getItemNames()) {
							
							for (LCNInputModule pMod : prov.getModules(name)) {
							
								if (null != pMod) {
									
									homeSegment = this.credentialMap.get(pMod.id).homeSegment;
									
									if (lcnMod.equals(pMod, true, homeSegment) && pMod.type == ModuleType.DATA && pMod.datatype != DataType.NONE && !this.requestModules.contains(pMod, homeSegment)) {
										this.requestModules.add(pMod);
										startRequest(pMod);
									}
									
									if (pMod.sceneData != null && !this.requestModules.contains(pMod, homeSegment)) {
										this.requestModules.add(pMod);
										startRequest(pMod);
										
									}
								}
							
							}
								
						}
					}
					
				}
			
			} else if (lcnMod.isHomeSegment) {
				
				credentialMap.get(getId(getCredential(chan.remote.getHostString()))).homeSegment = lcnMod.segment;
				
			}
		
			int homeSegment = credentialMap.get(lcnMod.id).homeSegment;
			
			LCNBindingProvider provider = this.getProvider(lcnMod, homeSegment);
			
			if (lcnMod.type == LCNInputModule.ModuleType.DATA && provider == null && this.requestModules.locked && !this.requestModules.isEmpty() 
					&& lcnMod.segment == this.requestModules.get(0).segment && lcnMod.module == this.requestModules.get(0).module) {
				LCNInputModule mod = this.requestModules.get(0);
				
				for (LCNBindingProvider provi : this.providers) {
					for (String name : provi.getItemNames()) {
						
						if (null != provi.getFirstModule(name) && provi.getFirstModule(name).equals(mod, homeSegment)) {
							mod = provi.getFirstModule(name);
							mod.value = lcnMod.value;
							lcnMod = mod;
							provider = provi;
						}
						
					}
				}
		
			}	
			
			//The input is represented as a LCNModule (even though it isn't). This allows us to match the input against all Modules listed in our configuration.
			if (null != lcnMod && null != provider) {
				
				try {
					
					List<String> items = provider.getItemNamesByModule(lcnMod, homeSegment);
					
					boolean jobImproved = false;
					
					logger.debug(input + " ===> " + items.size());

					String stateName = null;
					State newState = null;
					
					if (lcnMod.type == LCNInputModule.ModuleType.DATA) {
						
						List<String> realItems = provider.getItemNamesByModule(lcnMod, homeSegment);
						
						if (!jobImproved && !requestModules.isEmpty() && requestModules.get(0).equals(lcnMod, homeSegment)) {
							improveSchedule(requestModules.get(0), lcnMod);
							this.requestModules.locked = false;
							jobImproved = true;
						}
						
						for (String item : realItems) {
							
							for (LCNInputModule realMod : provider.getModules(item)) {
								
								stateName = "";
								
								if (realMod.datatype == LCNSyntax.DataType.LS && null != lcnMod.sceneData) {
									if (realMod.LcnShort.startsWith(LCNSyntax.Command.SZR.getValue())){
										realMod.adoptValue(lcnMod);
										
										if (realMod.dataId <= 0 || realMod.dataId > lcnMod.sceneData.ramp.length) {
											stateName =  realMod.sceneData.toString();
										} else {
											
											if (null != realMod.sValue) {
												
												if (realMod.sValue.equals("RAMP")) {
													stateName = String.valueOf(lcnMod.sceneData.ramp[realMod.dataId - 1]);
												} else {
													stateName = String.valueOf(lcnMod.sceneData.value[realMod.dataId - 1]);
												}
												
											} else {
												
												stateName = "Value: " + String.valueOf(lcnMod.sceneData.value[realMod.dataId - 1])
														+ " Ramp: " + String.valueOf(lcnMod.sceneData.ramp[realMod.dataId - 1]);
												
											}
											
										}
										
									} else {
										stateName = null;
									}
								} else if (realMod.datatype == LCNSyntax.DataType.S) {
									long value = lcnMod.value;
									if(value != 0xffff && (value & 0x8000) != 0) {
										  value &= 0x7fff; //Kill lock-bit
									}
									double value2 = LCNParser.parseWithModFromLCN(value, realMod.modifier);
									stateName = checkSpecialValues(value, value2);
									if (realMod.modifier == VarModifier.NONE && stateName.contains(".")) {
										stateName = stateName.split("\\.")[0];
									}
								} else {
									double value = LCNParser.parseWithModFromLCN(lcnMod.value, realMod.modifier);
									stateName = "" +  checkSpecialValues(lcnMod.value, value); //("" + lcnMod.value);
								}
								
								if (realMod.modifier == VarModifier.LUX || realMod.modifier == VarModifier.LUX_T) {
									stateName = stateName + " (" + lcnMod.value + ")";
								}
								
								List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(item, provider.getOpenHabCmd(item, realMod.LcnShort));						
								newState = getState(stateTypeList, stateName);
								
								if (null != newState && null != stateName) {
									eventPublisher.postUpdate(item, newState);	
									break;
								}
							
							}
		
						}
						
					} else {
					
						for (int i = 0; i < items.size(); i++) {
							
							for (LCNInputModule realMod : provider.getModules(items.get(i))) {
								
								stateName = "";
								
								//For switchable items, give the ON command the upper hand.
								LCNItemBinding bind = provider.getItemBinding(items.get(i), OnOffType.ON);
								
								if (null != bind) {
									realMod = LCNParser.reverseParse(bind.getLcnshort(), bind.getID()) ;
								}
															
								if (lcnMod.type == LCNInputModule.ModuleType.BINARY) {
									
									stateName = "true";
									
									for (int j = 0; j < realMod.bools.length; j++) {
										
										if (realMod.bools[j] && !lcnMod.bools[j]) {
											stateName = "false";
										}
										
									}
									
								} else if (lcnMod.type == LCNInputModule.ModuleType.RELAY) {
									
									stateName = "true";
									
									for (int j = 0; j < realMod.bools.length; j++) {
										
										if (realMod.bools[j] && !lcnMod.bools[j]) {
											stateName = "false";
										}
										
									}
									
								} else if (lcnMod.type == LCNInputModule.ModuleType.SERIAL) {
	
									if (realMod.type == LCNInputModule.ModuleType.SERIAL && null != lcnMod.serial) {
										stateName = lcnMod.serial;
									} else if (realMod.type == LCNInputModule.ModuleType.FIRMWARE && null != lcnMod.firmware) {
										stateName = lcnMod.firmware;
									} else {
										stateName = "";
									}	
			
								} else if (lcnMod.type == LCNInputModule.ModuleType.THRESHOLD && null != lcnMod.thresholds && null != realMod.thresholds && lcnMod.dataId == realMod.dataId) {
									
									if (!jobImproved && !requestModules.isEmpty() && requestModules.get(0).equals(lcnMod, homeSegment)) {
										improveSchedule(requestModules.get(0), lcnMod);
										this.requestModules.locked = false;
										jobImproved = true;
									}
									
									if (null != realMod.sValue && realMod.sValue.equals("HYSTERESIS")) {
										stateName = "";
										for (Threshold thres : lcnMod.thresholds) {
											if (thres.isHysteresis) {
												stateName = String.valueOf(thres.value);
												break;
											}
										}									
									} else if (realMod.dataId == 0 && realMod.value == 0) {
										stateName = "";
										for (Threshold thres : lcnMod.thresholds) {
											stateName += thres.toString() + " ";
										}
									} else {
										Threshold thres = lcnMod.getThreshold(realMod.dataId, (int)realMod.value);
										if (null != thres) {
											stateName = String.valueOf(LCNParser.parseWithModFromLCN(thres.value, realMod.modifier));
										} else {
											stateName = "Threshold does not exist!";
										}
									}
								
								} else if (lcnMod.type == LCNInputModule.ModuleType.INFO && null != lcnMod.sValue) {
									
									if (!jobImproved && !requestModules.isEmpty() && requestModules.get(0).equals(lcnMod, homeSegment)) {
										improveSchedule(requestModules.get(0), lcnMod);
										this.requestModules.locked = false;
										jobImproved = true;
									}
									stateName = lcnMod.sValue.replaceAll("ÿ", ""); //.replaceAll("[^ -~]", ""); //would remove non ascii characters...
									
								} else if (lcnMod.type == LCNInputModule.ModuleType.OEM && null != lcnMod.sValue) {
									
									if (!jobImproved && !requestModules.isEmpty() && requestModules.get(0).equals(lcnMod, homeSegment)) {
										improveSchedule(requestModules.get(0), lcnMod);
										this.requestModules.locked = false;
										jobImproved = true;
									}
									stateName = lcnMod.sValue.replaceAll("ÿ", ""); //.replaceAll("[^ -~]", ""); //would remove non ascii characters...
									
								} else if (lcnMod.type == LCNInputModule.ModuleType.LED && null != lcnMod.led) {
									
									if (!jobImproved && !requestModules.isEmpty() && requestModules.get(0).equals(lcnMod, homeSegment)) {
										improveSchedule(requestModules.get(0), lcnMod);
										this.requestModules.locked = false;
										jobImproved = true;
									}
									
									if (realMod.dataId != 0 && null != lcnMod.led.states.get(realMod.dataId - 1)) {
										stateName = lcnMod.led.states.get(realMod.dataId - 1).toString();
									} else {
										stateName = lcnMod.led.toString();
									}
									
								} else if (lcnMod.type == LCNInputModule.ModuleType.ENGINE_REPORT) {
									
									stateName = "";
									if (realMod.sValue == null) {
										for (EngineStatus eng : lcnMod.engines) {
											stateName += eng.toString();
											if (!eng.equals(lcnMod.engines.get(lcnMod.engines.size() - 1))) {
												stateName += " | ";
											}
										}
									} else if (null != lcnMod.getEngine(realMod.dataId)) {
										
										switch (LCNSyntax.EngineReport.valueOf(realMod.sValue)) {
										
											case POSITION:
												stateName = String.valueOf(lcnMod.getEngine(realMod.dataId).position);
												break;
												
											case LIMIT:
												stateName = String.valueOf(lcnMod.getEngine(realMod.dataId).limit);;
												break;
												
											case STEP_OUT:
												stateName = String.valueOf(lcnMod.getEngine(realMod.dataId).stepOut);;
												break;
												
											case STEP_IN:
												stateName = String.valueOf(lcnMod.getEngine(realMod.dataId).stepIn);;
												break;
										
											default:
												stateName = "ERROR";
												break;
										}
										
										if (stateName.equals("-1")) {
											
											stateName = "Unknown";
											
										}
										
									} else {
										
										stateName = "Error";
										
									}
									
								} else if (lcnMod.type != ModuleType.THRESHOLD){
									
									stateName = "OFF";
									
									if (lcnMod.status > 0) {
										stateName = "ON";
									}
									
									if (provider.getItem(items.get(i)) instanceof org.openhab.core.library.items.DimmerItem) {
										stateName = String.valueOf(lcnMod.status);
									}
									
								}
								
								List<Class<? extends State>> stateTypeList = provider.getAcceptedDataTypes(items.get(i), provider.getOpenHabCommands(items.get(i)).get(0));
								newState = getState(stateTypeList, stateName);
								
								if (null == newState && lcnMod.type == LCNInputModule.ModuleType.BINARY) {
									if (stateName.equals("true")) {
										stateName = "CLOSED";
									} else {
										stateName = "OPEN";
									}
									newState = getState(stateTypeList, stateName);
								} else if (null == newState && lcnMod.type == LCNInputModule.ModuleType.RELAY) {
									if (stateName.equals("true")) {
										stateName = "ON";
									} else {
										stateName = "OFF";
									}
									newState = getState(stateTypeList, stateName);
								} else if (null == newState && lcnMod.type == LCNInputModule.ModuleType.LED) {
									try {
										if (lcnMod.led.states.get(LCNParser.getLEDNumber(realMod.LcnShort)) == LCNInputModule.LEDInfo.State.E) {
											stateName = "ON";
										} else {
											stateName = "OFF";
										}
										newState = getState(stateTypeList, stateName);
									} catch (Exception exc) {
										if (logger.isDebugEnabled()) {
											exc.printStackTrace();
										}
									}
								}
								
								if (null == newState && lcnMod.type != ModuleType.THRESHOLD) {
									newState = getState(stateTypeList, "" + lcnMod.status);
								}
								
								if (null != newState) {
									eventPublisher.postUpdate(items.get(i), newState);
									break;
								} else {
									//logger.debug("Unable to update state of item {}", items.get(i));
								}
							
							}
							
						}
					
					}
					
				} catch (Exception e) {
		
					logger.debug("Input: '{}' could not be parsed!", input);
					e.printStackTrace();
					
				}
						
			} else {
				
				logger.debug("Input: '{}' can not be translated to a module!", input);
		
			}
			
		}
		
	}

	/**
	 * Checks native var-values for special meanings (like no value, undefined and defective...)
	 * @param value the <b>native</b> value
	 * @param transformedValue the value that will be set if there was no special value found.
	 * @return the transformedValue if no special value was found, a String with an explaination otherwise.
	 */
	private String checkSpecialValues(long value, double transformedValue) {

		String result = null;
		
		if(value == 0xffff) {  // No value
		    result = "---";
		} else if((value & 0xff00) == 0x8100) { // Undefined
			result = "---";
		} else if((value & 0xff00) == 0x7f00) { // Defective
			result = "--- (!!!)";
		}
		
		if (null == result) {
			result = "" + transformedValue;
		}
		
		return result;
		
	}
	
	/**
	 * Returns an appropriate state for the given String under the given list of states.
	 * @param stateList A list of possible states.
	 * @param state The value for the state.
	 * @return A legal state, StringType if no other state could be found.
	 */
	private State getState(List<Class<? extends State>> stateList, String state) {
		
		State result = null;
		
		if (null != stateList) {
			
			result = TypeParser.parseState(stateList, state);
			
		} else {
			
			result = StringType.valueOf(state);
			
		}
		
		return result;
		
	}
	
	/**
	 * Returns an appropriate provider for the given item name.
	 * @param itemName The name of the item in question.
	 * @return A LCNBindingProvider for the item, can be NULL.
	 */
	private LCNBindingProvider getProvider(String itemName) {
		
		LCNBindingProvider result = null;
		
		for (LCNBindingProvider provider : this.providers) {
			
			List<InetSocketAddress> list = provider.getInetSocketAddresses(itemName, credentialMap);
			
			if (null != list && !list.isEmpty()) {
				
				result = provider;
				break;
			}
			
			
		}
		
		return result;
		
	}
	
	/**
	 * Returns an appropriate provider for the given LCNInputModule.
	 * @param module The LCNInputModule in question.
	 * @return A LCNBindingProvider for the module, can be NULL.
	 */
	private LCNBindingProvider getProvider(LCNInputModule module, int homeSegment) {
		
		LCNBindingProvider result = null;
		
		for (LCNBindingProvider provider : this.providers) {
		
			for (String name : provider.getItemNames()) {
				
				if (module.equals(provider.getFirstModule(name), homeSegment)) {
					
					result = provider;
					break;
					
				}
				
			}
			
		}
		
		return result;
		
	}

	/**
	 * Parses:<br>
	 * LCNShort(with variables) to LCNShort(without variables)<br>
	 * or<br>
	 * A mapping to LCNShort.<br>
	 * @param lcnShort The lcnShort form OR a mapping.
	 * @param openHABcmd The command send by openHAB.
	 * @return String with the result, can be NULL!
	 */
	public static String parseLCNShort(String lcnShort, String openHABcmd) {
		
		String result = null;
			
		Matcher matcher = MAPPING_PATTERN.matcher(lcnShort);

		if (!matcher.matches()) {
			
			result = lcnShort;
			
		} else {
			
			matcher.reset();

			matcher.find();			
			String s1 = matcher.group(1);
			String s2 = matcher.group(2);

			TransformationService transformationService = TransformationHelper.getTransformationService(LCNActivator.getContext(), s1);
			if (transformationService != null) {
				
				try {
					
					result = transformationService.transform(s2, openHABcmd);
					
				} catch (TransformationException e) {
					
					result = lcnShort;

				}
				
			} else {
				
				result = lcnShort;
				
			}
			
		}
		
		if (null != openHABcmd && result.contains("%i")) {
			
			if (openHABcmd.contains("_")) {
				
				String[] sub = openHABcmd.split("_");
				int counter = 0;
				
				while (result.indexOf("%i") != -1) {
					if (counter > sub.length) {
						counter = 0;
					}
					result = result.replaceFirst("%i", sub[counter]);
					counter++;
				}
				
			} else {
				
				result = result.replace("%i", openHABcmd);
			
			}
			
		}

		return result;
	}
	
	/**
	 * Process all providers of the binding.
	 */
	private void processProviders() {
		
		//if one provider was updated, we need to reset ALL providers since openHAB will just throw away all our precious data.
		//And since we can't access the itemRegistry to check which items need to be reevaluated (mainly the 'new' VAR items as well as Firmware and SN), 
		//we have to reset all providers to ensure that data is read as soon as possible.
		boolean hasToReset = false;
		for (LCNBindingProvider provider : providers) {
			
			if (provider.wasUpdated) {
				provider.wasUpdated = false;
				hasToReset = true;
			}
			
		}
		
		if (hasToReset) {
			logger.info("Rebooting the LCN-Binding...");
			for (LCNBindingProvider provider : providers) {
				logger.debug("          Rebooting a provider");
				provider.setUninitialized();	
				this.requestModules.clear();
				this.firmwares.clear();
				clearRequests();
			}
		}
		
		for (LCNBindingProvider provider : providers) {
			
			for (String itemName : provider.getItemNames()) {
				
				for(Command aCommand : (provider).getOpenHabCommands(itemName)) {

					InetSocketAddress address = provider.getAddress(itemName, aCommand, credentialMap);

					if (null == address) {
						
						if (this.isProperlyConfigured()) {
							logger.error("Unable to resolve address of item: {}", itemName);
							provider.removeItem(itemName);
						}
						
					} else if(null == lcnChannels.get(address)) {

						LCNChannel chan = new LCNChannel(address , null, false, null);

						lcnChannels.add(chan);

						if(null == chan.channel) {
	
							if(null != lcnChannels.get(address)) {
								
								chan.channel = lcnChannels.get(address).channel;
								
							}

							synchronized (this) {

								if(null == chan.channel) {

									SocketChannel newSocketChannel = null;
									
									try {
										
										newSocketChannel = SocketChannel.open();
										newSocketChannel.socket().setKeepAlive(true);
										newSocketChannel.configureBlocking(false);
										
									} catch (IOException exc) {
										
										logger.warn("Unable to set up a new SocketChannel! ({})", exc.getMessage());
										
									}

									synchronized(selector) {
										
										selector.wakeup();
										
										try {
											
											newSocketChannel.register(selector, (SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT));
											
										} catch (ClosedChannelException exc) {
											
											logger.warn("Unable to register a new SocketChannel! ({})", exc.getMessage());
											
										}
										
									}

									chan.channel = newSocketChannel;

									try {
										
										logger.info("Connecting channel {} ", chan);
										newSocketChannel.connect(address);
										
									} catch (IOException exc) {
										
										logger.error("Unable to connect channel! ({})", exc.getMessage());
										
									}
									
								}
								
							}
							
						} 
						
					}
					
				}
				
			}
			
			initializeProvider(provider);
			
		}
		
	}
	
	/**
	 * Initialize items of the give provider, which were not initialized yet.
	 * @param provider
	 */
	private void initializeProvider(LCNBindingProvider provider) {
		
		//Initialize Items that have not been initialized!
		if (provider.hasUninitialized()) {
						
			List<String> uninitialized = provider.getUninitialized();
			
			for (LCNInputModule mod : provider.getUninitializedModules(uninitialized)) {
							
				if (null != mod && null != this.credentialMap.get(mod.id)) {
				
					//if homesegment == 0, then the couplers were not registered yet, thus we dont know the homeSegment and could not safely determine whether the module was already initialized...
					if (this.credentialMap.get(mod.id).isConnected && this.credentialMap.get(mod.id).homeSegment != 0) {
						
						boolean wasInitialized = false;
						int homeSegment = this.credentialMap.get(mod.id).homeSegment;
						
						for (LCNBindingProvider prov : providers) {
								
							if (prov.hasInitialized(mod, homeSegment)) {
								
								wasInitialized = true;
								break;
								
							}
							
						}
						
						LCNChannel chan = this.lcnChannels.get(provider.getAddress(provider.getItemNamesByModule(mod, homeSegment).get(0), 
								provider.getOpenHabCommands(provider.getItemNamesByModule(mod, homeSegment).get(0)).get(0), credentialMap));
							
						if (!this.requestModules.contains(mod, homeSegment)) { //!wasInitialized &&
							this.quickInternalCommand(LCNParser.moduleToNewDataRequest(mod, homeSegment), chan, LCNInputModule.generateKey(mod, homeSegment), false);
						}
						
						if (!wasInitialized && mod.module != 0) {
							logger.debug("Initializing...@ID: {}, @MOD: {}, @SEG: {} ", mod.id, mod.module, mod.segment);
							this.quickInternalCommand(LCNParser.moduleToStatusRequest(mod, homeSegment), chan, LCNInputModule.generateKey(mod, homeSegment), false);
							startFirmwareJob(mod, chan);
							//this.quickInternalCommand(LCNParser.moduleToSerialRequest(mod), chan, LCNInputModule.generateKey(mod), false);
							if (pingTimer > 0) {
								startPing(chan, pingTimer);
							}
						}
						
						provider.setInitialized(mod, homeSegment);
						
					}
				
				} else {
					
					logger.warn("Undefined identifier {}", mod.id);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Deletes all existing requests, in order to re-setup new request jobs.
	 */
	private void clearRequests() {
		
		Scheduler scheduler = null;
		
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException exc) {
			logger.error("Unable to receive the default Quartz Scheduler: {}", exc.getMessage());
		}
		
		if (null != scheduler) {
			
			try {
				
				Set<JobKey> keys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(RequestJob.getJobGroup()));
				
				for (JobKey key : keys) {
					
					try {
					
					scheduler.deleteJob(key);
					
					} catch (SchedulerException exc) {
						//Minor problem
						logger.debug("Unable to delete Job: " + exc.getMessage());
					}
					
				}
				
				keys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(FirmwareJob.getJobGroup()));
				
				for (JobKey key : keys) {
					
					try {
					
					scheduler.deleteJob(key);
					
					} catch (SchedulerException exc) {
						//Minor problem
						logger.debug("Unable to delete Job: " + exc.getMessage());
					}
					
				}
				
			} catch (SchedulerException exc) {
				//Major failure
				logger.debug("Unable to retrieve Jobs: " + exc.getMessage());
			}
			
		} else {
			logger.debug("Unable to retrieve scheduler!");
		}
		
	}
	
	/**
	 * Connects a SocketChannel.
	 * @param socketChan The SocketChannel to connect.
	 */
	private void connectChannel(SocketChannel socketChan) {
		
		LCNChannel chan = lcnChannels.get(socketChan);
		lcnChannels.setAllReconnecting(socketChan, false);		
		
		boolean connected = false;
		boolean failed = false;
		
		try {
			
			connected = socketChan.finishConnect();
			
		} catch (IOException exc) {
			
			logger.warn("Unable to connect channel {}", chan.remote.getHostString());
			failed=true;
			
		} catch (NoConnectionPendingException exc) {
			
			logger.warn("Unable to connect channel {}", chan.remote.getHostString());
			failed=true;
			
		}

		if(failed) {

			reconnect(socketChan, chan, true);

		} else {

			if(connected) {		
				
				logger.info("Channel {} was connected!", chan.remote.getHostString());
				reconnect(socketChan, chan, false);
				
			}
			
		}
		
	}
	
	/**
	 * Reads input from a SocketChannel.
	 * @param socketChan The SocketChannel that is to be read.
	 */
	private void readChannel(SocketChannel socketChan) {
		
		LCNChannel chan = lcnChannels.get(socketChan);
		ByteBuffer buff = ByteBuffer.allocate(READ_BUFFER);
		int read = 0;
		boolean failed = false;

		try {
			
			read = socketChan.read(buff);
			
		} catch (NotYetConnectedException exc) {
			
			if(!socketChan.isConnectionPending()) {
				
				logger.warn("The channel {} is not scheduled to connect!", chan.remote.getHostString());
				failed=true;
				
			} else {
				
				logger.warn("The channel {} is not connected yet!", chan.remote.getHostString());
				
			}
			
		} catch (IOException exc) {
			
			logger.warn("The channel {} suffered from an IOException!", chan.remote.getHostString());
			failed=true;
			
		}

		if(-1 == read) {
			
			try {
				
				socketChan.close();
				
			} catch (IOException e) {
				
				logger.warn("Unable to close channel {}!",chan.remote.getHostString());
				
			}
			
			failed = true;
			
			logger.debug("Channel {} was closed by peer!", chan.remote.getHostString());
			
		}

		if(failed) {

			reconnect(socketChan, chan, true);	

		} else {

			List<LCNChannel> channels = new ArrayList<LCNChannel>();

			channels = lcnChannels.getAll(socketChan);


			if(!channels.isEmpty()) {
				
				buff.flip();

				List<SocketAddress> list = new ArrayList<SocketAddress>();
				
				for(LCNChannel lcnChan : channels) {
					
					SocketAddress address = null;
					
					try {
						
						address = lcnChan.channel.getLocalAddress();
						
					} catch (IOException exc) {
						
						logger.warn("Unable to read address of channel: {}", lcnChan);
						logger.debug(exc.getMessage());
						
					}
						
					if (null != address && !list.contains(address)) {
						
						list.add(address);
						
						if(null != chan && null != buff && 0 != buff.limit()) {			
							
							parseInput(buff, lcnChan);
							
						}
						
					}
					
				}

			} 
			
		}	
		
	}
	
	/**
	 * Writes any output for the given socketChan.
	 * @param socketChan The SocketChannel that is to be written on.
	 */
	private void writeChannel(SocketChannel socketChan) {
		
		LCNChannel chan = lcnChannels.get(socketChan);
		LCNOutputElement output = null;		
		
		for (LCNOutputElement elem : outputList) {
			
			if (elem.lCNChannel.channel.equals(socketChan)) {
				output = elem;
				break;
			}
			
		}

		if(null != output && null != output.buffer) {

			boolean failed=false;
			
			output.buffer.rewind();
			String out = new String(output.buffer.array());

			LCNInputModule outputMod = LCNParser.reverseParse(new String(output.buffer.array()), getId(getCredential(socketChannelToIp(socketChan))));
			
			
			if (null != outputMod) {
				
				int homeSegment = this.credentialMap.get(outputMod.id).homeSegment;
				
				for (int k = 0; k < this.requestModules.size(); k++) {
						
					LCNInputModule mod = this.requestModules.get(k);
					
					if (mod.equals(outputMod, homeSegment) || (mod.equals(outputMod,  true, homeSegment) && mod.type == ModuleType.DATA && outputMod.datatype == DataType.NONE 
							&& (out.startsWith(LCNSyntax.Command.ZA.toString()) || out.startsWith(LCNSyntax.Command.ZS.toString())))) {
						
						if ((((mod.type == LCNInputModule.ModuleType.DATA && mod.datatype != DataType.NONE) || (mod.type == ModuleType.THRESHOLD) || mod.type == ModuleType.LED || mod.datatype == LCNSyntax.DataType.LS 
								|| (outputMod.datatype == LCNSyntax.DataType.NONE && outputMod.type == ModuleType.DATA)) && (this.requestModules.get(k).type != LCNInputModule.ModuleType.DATAO || mod.datatype == LCNSyntax.DataType.LS))) {
						
							try {
								
								String request = LCNParser.parse(this.requestModules.get(k).LcnShort, ((Credentials) credentialMap.get(getId(getCredential(chan.remote.getHostString())))).homeSegment, 
										firmwares.get(LCNInputModule.generateKey(this.requestModules.get(k), homeSegment)));
								
								if (!out.equals(request)) {
									//quickInternalCommand(request, output.lCNChannel, output.moduleKey, true);
									fireJob(mod);
								}
								
							} catch (LCNParserException e) {
								//e.printStackTrace();
							}
						
						}
						
					}
					
				}
				
			}
			
			if (!failed) {
				
				String outputString = new String(output.buffer.array());
				outputString = outputString.replaceAll("[\n\r]", "");
				
				try {
					
					output.buffer = LCNParser.quickParse(output.buffer, ((Credentials) credentialMap.get(getId(getCredential(chan.remote.getHostString())))).homeSegment, firmwares.get(output.moduleKey));

					if (output.buffer.hasRemaining()) {
						logger.debug("Output at '{}' with buffer '{}'", output.lCNChannel.remote.getHostString(), new String(output.buffer.array()).replaceAll("[\n\r]", ""));
					}
					
					try {
					
						while (output.buffer.hasRemaining()) {
							
							socketChan.write(output.buffer);
							
						}
						
					} catch (NotYetConnectedException exc) {
						
						logger.warn("Writing to channel {} failed!", chan.remote);
						failed = true;
						
					} catch (IOException exc) {
						
						logger.warn("Writing to channel {} failed!", chan.remote);
						failed = true;
						
					}				
					
				} catch (LCNParserException exc) {
					
					logger.error("Unable to parse command '{}'!", outputString);
					logger.error(exc.getMessage());
					
				}
				
			}			

			if(failed) {

				reconnect(socketChan, output.lCNChannel, true);

			} else {
				
				outputList.remove(output);

			}
		}
		
	}
	
	/**
	 * Will instantly trigger a job for a given LCNInputModule.
	 * @param mod The LCNInputModule of the job that shall be fired.
	 */
	private void fireJob(LCNInputModule mod) {
		
		Scheduler sched;
		
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
			sched.triggerJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup()));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Improves the Schedule for a RequestJob concerning a certain LCNInputModule.
	 * @param mod The original mod.
	 * @param inputMod The input mod.
	 */
	private void improveSchedule(LCNInputModule mod, LCNInputModule inputMod) {
		
		Scheduler sched;
		boolean same = false;
		
		logger.debug("Attempting to improve job for {}", mod.LcnShort);
		
		if (null != inputMod) {
			same = LCNInputModule.sameValue(mod, inputMod);
		}
		
		if (same || null == inputMod) {

			try {
		
				logger.debug("No change detected!");
				sched = StdSchedulerFactory.getDefaultScheduler();
				
				Trigger trig = sched.getTriggersOfJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup())).get(0);
				
				if (null != trig.getPreviousFireTime()) {
					
					long diff = (trig.getNextFireTime().getTime() - trig.getPreviousFireTime().getTime());
					sched.deleteJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup()));

					int newTime = (int) (diff * 2);
					if (newTime > this.maxRequestTime) {
						newTime = this.maxRequestTime;
					} else if (newTime < this.minRequestTime) {
						newTime = this.minRequestTime;
					}
					startRequest(mod, (newTime / 1000), false);
					
				} else {
					
					sched.deleteJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup()));
					startRequest(mod, 30, false);
					
				}
					
			} catch (SchedulerException | NullPointerException e) {
				logger.debug("Unable to improve Schedule: {}", e.getMessage());
				if (logger.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
			
		} else {

			logger.debug("Change detected!");
			mod.adoptValue(inputMod);
			
			try {
				
				sched = StdSchedulerFactory.getDefaultScheduler();
				
				Trigger trig = sched.getTriggersOfJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup())).get(0);
				
				if (null != trig.getPreviousFireTime()) {
					
					long diff = (trig.getNextFireTime().getTime() - trig.getPreviousFireTime().getTime());
					
					sched.deleteJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup()));
	
					int newTime = (int) (diff / 2);
					if (newTime > this.maxRequestTime) {
						newTime = this.maxRequestTime;
					} else if (newTime < this.minRequestTime) {
						newTime = this.minRequestTime;
					}
					startRequest(mod, (newTime / 1000), false);
				
				} else {
					
					sched.deleteJob(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup()));
					startRequest(mod, 30, false);
					
				}
					
			} catch (SchedulerException | NullPointerException e) {
				logger.debug("Unable to improve Schedule: {}", e.getMessage());
				if (logger.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	/**
	 * Starts a ping job to keep channels opened.
	 * @param chan
	 */
	private void startPing(LCNChannel chan, int time) {
		
		Scheduler scheduler = null;
		Trigger trigger = null;
		
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException exc) {
			logger.error("Unable to receive the default Quartz Scheduler: {}", exc.getMessage());
		}
		
		JobDataMap map = new JobDataMap();
		map.put("Channel", chan);
		map.put("Parent", this);
		
		JobDetail job = newJob(PingJob.class)
				.withIdentity(chan.toString(), "PingJob")
				.usingJobData(map)
				.build();
			
		trigger = newTrigger()
				.withIdentity(chan.toString(), "PingJob")
				.withSchedule(org.quartz.SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(time)
				.repeatForever())
				.build();
		
		try {

			boolean exists = scheduler.checkExists(JobKey.jobKey(chan.toString(), "PingJob"));
			if (!exists) {
				scheduler.scheduleJob(job, trigger);
			}
			
		} catch (SchedulerException exc) {
			
			logger.error("Unable to schedule the job: {}", job.getDescription());
			exc.printStackTrace();
			
		}
		
	}
	
	/**
	 * Starts a recurring request.
	 * @param mod The LCNInputModule that needs the recurring requests.
	 */
	private void startRequest(LCNInputModule mod) {
		startRequest(mod, 30, true);
	}
	
	/**
	 * Starts a recurring request.
	 * @param mod The LCNInputModule that needs the recurring requests.
	 */
	private void startRequest(LCNInputModule mod, int time, boolean startNow) {
		
		Scheduler scheduler = null;
		Trigger trigger = null;
		
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException exc) {
			logger.error("Unable to receive the default Quartz Scheduler: {}", exc.getMessage());
		}
		
		JobDataMap map = new JobDataMap();
		map.put("Module", mod);
		map.put("Parent", this);
		
		JobDetail job = newJob(RequestJob.class)
				.withIdentity(RequestJob.getJobKey(mod), RequestJob.getJobGroup())
				.usingJobData(map)
				.build();
		
		if (startNow) {
			
			trigger = newTrigger()
					.withIdentity(RequestJob.getJobKey(mod), RequestJob.getJobGroup())
					.startNow()
					.withSchedule(org.quartz.SimpleScheduleBuilder.simpleSchedule()
				            .withIntervalInSeconds(time)
				            .repeatForever()
				            .withMisfireHandlingInstructionNowWithExistingCount())
					.build();
		
		} else {
			
			trigger = newTrigger()
					.withIdentity(RequestJob.getJobKey(mod), RequestJob.getJobGroup())
					.startAt(new Date((System.currentTimeMillis()) + (time * 1000)))
					.withSchedule(org.quartz.SimpleScheduleBuilder.simpleSchedule()
				            .withIntervalInSeconds(time)
				            .repeatForever()
				            .withMisfireHandlingInstructionNowWithExistingCount())
					.build();
			
		}
		
		logger.debug("Setting up job: {}, fireing every {} seconds!", RequestJob.getJobKey(mod), time);
		
		boolean exists = false;
		
		try {
			exists = scheduler.checkExists(JobKey.jobKey(RequestJob.getJobKey(mod), RequestJob.getJobGroup()));
			if (exists) {
				logger.debug("Duplicate RequestJob: {}, {}", RequestJob.getJobKey(mod), RequestJob.getJobGroup());
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		if (null != job && null != trigger && !exists) {
			
			try {

				scheduler.scheduleJob(job, trigger);
				if (startNow) {
					scheduler.triggerJob(job.getKey());
				}
				
			} catch (SchedulerException exc) {
				
				logger.error("Unable to schedule the job: {}", job.getDescription());
				exc.printStackTrace();
				
			}
			
		} else {
			logger.debug("Unable to schedule the job, invalid... job {}, trigger {}, exists {}", job == null, trigger == null, exists);
		}
		
	}
	
	/**
	 * Sets up a FirmwareJob to secure the retrieval of the firmware of the targeted module.
	 * @param mod The target module.
	 * @param chan The LCNChannel to write to.
	 */
	private void startFirmwareJob(LCNInputModule mod, LCNChannel chan) {
		
		Scheduler scheduler = null;
		Trigger trigger = null;
		int time = 10;
		
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException exc) {
			logger.error("Unable to receive the default Quartz Scheduler: {}", exc.getMessage());
		}
		
		int homeSegment = ((Credentials) this.credentialMap.get(mod.id)).homeSegment;
		
		JobDataMap map = new JobDataMap();
		map.put("Module", mod);
		map.put("Parent", this);
		map.put("Channel", chan);
		
		JobDetail job = newJob(FirmwareJob.class)
				.withIdentity(FirmwareJob.getJobKey(mod, homeSegment), FirmwareJob.getJobGroup())
				.usingJobData(map)
				.build();
			
		trigger = newTrigger()
			.withIdentity(FirmwareJob.getJobKey(mod, homeSegment), FirmwareJob.getJobGroup())
			.startNow()
			.withSchedule(org.quartz.SimpleScheduleBuilder.simpleSchedule()
			    .withIntervalInSeconds(time)
				.repeatForever()
				.withMisfireHandlingInstructionNowWithExistingCount())
			.build();
		
		boolean exists = false;
		
		logger.debug("Setting up firmware job: {}, fireing every {} seconds!", RequestJob.getJobKey(mod), time);
		
		try {
			exists = scheduler.checkExists(JobKey.jobKey(FirmwareJob.getJobKey(mod, homeSegment), FirmwareJob.getJobGroup()));
			if (exists) {
				logger.debug("Duplicate FirmwareJob: {}, {}", FirmwareJob.getJobKey(mod, homeSegment), FirmwareJob.getJobGroup());
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		if (null != job && null != trigger && !exists) {
			
			try {

				scheduler.scheduleJob(job, trigger);
				
			} catch (SchedulerException exc) {
				
				logger.error("Unable to schedule the job: {}", job.getDescription());
				exc.printStackTrace();
				
			}
			
		} else {
			logger.debug("Unable to schedule the job, invalid... job {}, trigger {}, exists {}", job == null, trigger == null, exists);
		}
		
	}
	
	/**
	 * Reconnects a channel in case of an error.
	 * @param socketChan The SocketChannel to write.
	 * @param output The LCNChannel of the output.
	 * @param setReconnecting True if the channel is to be set as reconnecting, false otherwise.
	 */
	private void reconnect(SocketChannel socketChan, LCNChannel output, boolean setReconnecting) {
		
		logger.debug("Setting up a reconnect job...");
		Scheduler scheduler = null;
		Trigger trigger = null;
		
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException exc) {
			logger.error("Unable to receive the default Quartz Scheduler: {}", exc.getMessage());
		}

		JobDataMap map = new JobDataMap();
		map.put("Channel", output);
		map.put("Binding", this);

		JobDetail job = newJob(ReconnectChannelJob.class)
				.withIdentity(Integer.toHexString(hashCode()) +"-Reconnect-"+Long.toString(System.currentTimeMillis()), "LCNBinding")
				.usingJobData(map)
				.build();

		if (setReconnecting) {
			
			trigger = newTrigger()
					.withIdentity(Integer.toHexString(hashCode()) +"-Reconnect-"+Long.toString(System.currentTimeMillis()), "LCNBinding")
					.startAt(futureDate(RECONNECT_INTERVAL, IntervalUnit.SECOND))         
					.build();
		
		} else {
			
			trigger = newTrigger()
					.withIdentity(Integer.toHexString(hashCode()) +"-Reconnect-"+Long.toString(System.currentTimeMillis()), this.toString())
					.withSchedule(org.quartz.CronScheduleBuilder.cronSchedule(RECONNECT_TIMER))    
					.startNow()
					.build();
			
		}
				
		if(null != job && null != trigger) {
			
			if(setReconnecting && !output.isReconnecting) {
				
				//lcnChannels.setAllReconnecting(socketChan, true);
				lcnChannels.setAllReconnecting(output, true);
				
				try {
					
					logger.debug("Setting up a reconnect job: {}", job.getKey());
					scheduler.scheduleJob(job, trigger);
					
				} catch (SchedulerException exc) {
					
					logger.error("Unable to schedule the job: {}", job.getKey());
					
				}
				
			} else {
				
				try {
					
					logger.debug("Setting up a (not reconnecting) reconnect job: {}", job.getDescription());
					scheduler.scheduleJob(job, trigger);
					
				} catch (SchedulerException e) {

					logger.error("Unable to schedule the job: {}", job.getDescription());
					
				}	
				
			}
			
		} else {
			
			logger.error("Unable to set up a reconnect job for: {}", job.getDescription());
			
		}
		
	}
	
	/**
	 * Processes LCN-binding relevant data.<br>
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		
		if ((System.currentTimeMillis() - this.lastRequestTime) > dataRequestTimeout && this.requestModules.locked) {
			this.requestModules.locked = false;
			this.lastRequestTime = System.currentTimeMillis();
			if (!this.requestModules.isEmpty()) {
				improveSchedule(this.requestModules.get(0), null);
			}
		}
		
		this.dataCounter += refreshInterval;
		
		processProviders();

		synchronized(selector) {
			
			try {
				selector.selectNow();
			} catch (IOException e) {
				logger.error("Selection failure: " + e.getMessage());
			}
			
		}
		
		Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
		
		while (iter.hasNext()) {
			
			SelectionKey key = (SelectionKey) iter.next();
			iter.remove();

			if (key.isValid()) {

				SocketChannel socketChan = (SocketChannel) key.channel();
						
				if(key.isConnectable()) {
					
					connectChannel(socketChan);

				} else if (key.isReadable()) {

					readChannel(socketChan);

				} else if (key.isWritable()) {
					
					writeChannel(socketChan);
					break;
					
				}
				
			}
			
		}
		
	}

	/**
	 * Returns the first (and hopefully only!) credential for the given IP.
	 * @param ip The IP as String
	 * @return The fitting credential or NULL if none was found.
	 */
	private Credentials getCredential(String ip) {
		
		for (String key : credentialMap.keySet()) {
			
			Credentials cred = credentialMap.get(key);
			if (cred.ip.equals(ip)) {
				return cred;
			}
			
		}
		
		logger.debug("Unknown address: {}", ip);
		return null;
		
	}
	
	/**
	 * Returns the first key that corresponds to the given credentials.
	 * @param cred The credentials (must be registered in the credentialMap).
	 * @return The key as String
	 */
	private String getId(Credentials cred) {
		
		String result = "";
		if (null != cred) {
			for (String key : credentialMap.keySet()) {
				
				if (cred.equals(credentialMap.get(key))) {
					result = key;
					break;
				}
				
			}
		}
		return result;
		
	}
	
	/**
	 * Returns the IP of a SocketChannel (without the leading '/' and port)
	 * @param chan the SocketChannel.
	 * @return IP as String.
	 */
	private String socketChannelToIp(SocketChannel chan) {
		String ip = null;
		
		try {
			ip = chan.getRemoteAddress().toString().substring(1).split(":")[0];
		} catch (Exception e) {
			ip = null;
			logger.debug("Unable to parse SocketChannel to IP!");
		}
				
		return ip;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "LCN Refresh Service";
	}
	
	/**
	 * Will reset all data which is connected to the given provider.
	 * This is useful when a provider is updated and its items need to be refreshed.
	 * @param provider
	 */
	@SuppressWarnings("unused")
	private void reset(LCNBindingProvider provider) {
		provider.wasUpdated = false;
		provider.setUninitialized();	
		this.requestModules.clear(provider, this.firmwares, this.credentialMap);
		clearRequests();
	}
	
}
