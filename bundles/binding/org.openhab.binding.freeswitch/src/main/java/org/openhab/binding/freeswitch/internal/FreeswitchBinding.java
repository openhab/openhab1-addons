/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freeswitch.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openhab.binding.freeswitch.internal.FreeswitchMessageHeader.*;

import org.openhab.binding.freeswitch.FreeswitchBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.library.tel.items.CallItem;
import org.openhab.library.tel.types.CallType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * FreeswitchBinding connects to a Freeswitch instance using a ESL Client
 * connection.  From this connection we listen for call life cycle events, message
 * waiting (MWI) events as well as send generic API commands.
 * @author Dan Cunningham
 * @since 1.4.0
 */
public class FreeswitchBinding extends AbstractBinding<FreeswitchBindingProvider> implements ManagedService, IEslEventListener {

	private static final Logger logger = 
			LoggerFactory.getLogger(FreeswitchBinding.class);
	
	private static int DEFAULT_PORT = 8021;
	
	/*
	 * How long we check to reconnect
	 */
	private long WATCHDOG_INTERVAL = 30000;
	

	//all calls are cached, we can lookup channles by thier UUID
	protected Map<String, Channel> eventCache;
	//map channels by UUID to one or more binding configs
	protected Map<String, LinkedList<FreeswitchBindingConfig>> itemMap;
	//Maps freeswitch accounts (vmail boxes) to MessageWaiting objects
	protected Map<String,MWIModel> mwiCache;

	private Client inboudClient;
	private String host;
	private String password;
	private int port;
	
	private WatchDog watchDog;
	
	public FreeswitchBinding() {
	}

	@Override
	public void activate() {
		logger.trace("activate() is called!");
	}

	@Override
	public void deactivate() {
		logger.trace("deactivate() is called!");
		stopWatchdog();
		disconnect();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.trace("Received command for item '{}' with command '{}'",itemName, command);

		for (FreeswitchBindingProvider provider : providers) {
			FreeswitchBindingConfig config = provider.getFreeswitchBindingConfig(itemName);
			switch(config.getType()){
			case CMD_API:{
				if (!(command instanceof StringType)){
					logger.warn("could not process command '{}' for item '{}': command is not a StringType", command, itemName);
					return;
				}

				String str = ((StringType) command).toString().toLowerCase();
				String response = executeApiCommand(str);
				eventPublisher.postUpdate(itemName, new StringType(response));
			}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.trace("updated() is called!");
		if (config != null) {
			
			startWatchdog();
			
			port = DEFAULT_PORT;
			host = (String) config.get("host");
			password = (String) config.get("password");
			
			String portString = (String) config.get("port"); 
			
			if(StringUtils.isNotBlank(portString))
				port = Integer.parseInt(portString);

			eventCache = new LinkedHashMap<String, Channel>();
			mwiCache = new HashMap<String, FreeswitchBinding.MWIModel>();
			itemMap = new LinkedHashMap<String, LinkedList<FreeswitchBindingConfig>>();

			try {
				connect();
			} catch (InboundConnectionFailure e) {
				logger.error("Could not connect to freeswitch server",e);
				//clean up 
				disconnect();
			}
		} else {
			//if we no longer have a config, make sure we are not connected and
			//that our watchdog thread is not running.
			stopWatchdog();
			disconnect();
		}
	}


	@Override
	public void eventReceived( EslEvent event) {
		logger.debug("Recieved ESLEvent {}", event.getEventName());
		logger.trace(printEvent(event));
		if(CHANNEl_CREATE.matches(event.getEventName())){
			handleNewCallEvent(event);
		}
		else if(CHANNEL_DESTROY.matches(event.getEventName())){
			handleHangupCallEvent(event);
		}
		else if(MESSAGE_WAITING.matches(event.getEventName())){
			handleMessageWaiting(event);
		}
	}

	@Override
	public void backgroundJobResultReceived(EslEvent arg0) {
	}
	
	/**
	 * Starts our watchdog thread to reconnect
	 */
	private void startWatchdog(){
		//start our watch dog if we have been configued at least
		//once, we will stop when the binding is unloaded
		if(watchDog == null || !watchDog.isRunning()){
			watchDog = new WatchDog();
			watchDog.start();
		}
	}
	
	/**
	 * stops our watchdog thread;
	 */
	private void stopWatchdog(){
		if(watchDog != null)
			watchDog.stopRunning();
	}

	/**
	 * Connect inbound client to freeswitch
	 * @throws InboundConnectionFailure
	 */
	private void connect() throws InboundConnectionFailure {

		disconnect();

		logger.debug("Connecting to {} on port {} with pass {}", 
				host, port, password);
		
		inboudClient = new Client();
		inboudClient.connect(host, port,password, 10);
		inboudClient.addEventListener(this);
		inboudClient.setEventSubscriptions("plain",String.format("%s %s %s", 
				CHANNEl_CREATE, 
				CHANNEL_DESTROY, 
				MESSAGE_WAITING));
		
		logger.debug(String.format("Connected"));
		
		initMessageItems();
	}

	/**
	 * disconnect inbound client from freeswitch
	 */
	private void disconnect() {
		if(inboudClient != null){
			try {
				inboudClient.close();
			} catch (Exception ignored) {
			} finally{
				inboudClient = null;
			}
		}
	}

	/**
	 * Handle Answer or Media (ringing) events and add an entry to our cache
	 * @param event
	 */
	private void handleNewCallEvent(EslEvent event) {

		String uuid = getHeader(event, UUID);
		logger.debug("Adding Call with uuid " + uuid);
		
		Channel channel = new Channel(event);
		//we should not get duplicate events, but lets be safe
		if(eventCache.containsKey(uuid))
			return;
		
		eventCache.put(uuid, channel);
		itemMap.put(uuid, new LinkedList<FreeswitchBindingConfig>());
		
		CallType call = channel.getCall();
		
		logger.debug("new call to : {} from : {}", 
				call.getDestNum(), call.getOrigNum());

		for (FreeswitchBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				FreeswitchBindingConfig config = provider
						.getFreeswitchBindingConfig(itemName);
				if (config.getType() == FreeswitchBindingType.ACTIVE) {
					/*
					 * Add the item if it is filtered and matches or if it is
					 * un-filtered and inbound
					 */
					if ((config.filtered() && matchCall(channel,config.getArgument()))
							|| (!config.filtered() && isInboundCall(channel))) {
						itemMap.get(uuid).add(config);
						newCallItemUpdate(config, channel);
					}
				}
			}
		}

	}
	
	/**
	 * MatchCall will attempt to match all the filters in a given filterString
	 * against the headers in a Channel.  If all filters are satisfied
	 * (matched) then we return true, if any filter fails we will stop
	 * processing and return false.
	 * @param channel
	 * @param filterString
	 * @return true if all filters match, false if any one does not.
	 */
	private boolean matchCall(Channel channel, String filterString){
		logger.debug("Trying to match filter string {}", filterString);
		
		//split our filter string rule pairs
		String[] filters = filterString.split(",");
		
		//out return value
		boolean matched = true;
		
		//for each filter try and match any channel headers
		for(String filter : filters){
			
			//break filter into header key and value
			String [] args = filter.split(":");
			
			//check that we have a key and value, and that neither is blank/null
			if(args.length == 2 && StringUtils.isNotBlank(args[0]) && StringUtils.isNotBlank(args[1])){
				String eventHeader = channel.getEventHeader(args[0]);
				try {
					//is the header blank/null or does the filter value not match the header value
					if(StringUtils.isBlank(eventHeader) || !args[1].equals(URLDecoder.decode(eventHeader, "UTF-8"))){
						//this item is filtered, but this call does not match
						matched = false;
					}
				} catch (UnsupportedEncodingException e) {
					logger.warn("Could not decode event header {}", eventHeader );
					matched = false;
				}
			} else {
				logger.warn("The filter string {} does not look valid, not updating item", filter);
				matched = false;
			}
			/*
			 * we have failed one of the filters, stop processing 
			 */
			if(!matched)
				break;
		}
		return matched;
	}

	/**
	 * Check if this channel is an inbound call
	 * @param channel
	 * @return true if the channel is inbound
	 */
	private boolean isInboundCall(Channel channel){
		String direction = channel.getEventHeader(CALL_DIRECTION);
		return StringUtils.isNotBlank(direction) && "inbound".equals(direction);
	}
	/**
	 * Handle channel destroy events and remove entries from our cache
	 * @param event
	 */
	private void handleHangupCallEvent(EslEvent event) {
		String uuid = getHeader(event, UUID);
		logger.debug("Removing Call with uuid " + uuid);
		
		eventCache.remove(uuid);
		
		LinkedList<FreeswitchBindingConfig> configs = 
				itemMap.remove(getHeader(event, UUID));
		
		if( configs != null ){
			for(FreeswitchBindingConfig config : configs){
				endCallItemUpdate(config);
			}
		}
	}

	/**
	 * Update items for new calls
	 * @param config
	 * @param channel
	 */
	private void newCallItemUpdate(FreeswitchBindingConfig config, Channel channel){

		if (config.getItemType().isAssignableFrom(SwitchItem.class)) {

			eventPublisher.postUpdate(config.getItemName(), OnOffType.ON);
		}
		else if (config.getItemType().isAssignableFrom(CallItem.class)) {

			eventPublisher.postUpdate(config.getItemName(), channel.getCall());
		}
		else if (config.getItemType().isAssignableFrom(StringItem.class)) {

			eventPublisher.postUpdate(config.getItemName(), 
					new StringType(String.format("%s : %s",
							channel.getEventHeader(CID_NAME),
							channel.getEventHeader(CID_NUMBER))));
		}
		else {
			logger.warn("handleHangupCall - postUpdate for itemType '{}' is undefined", config.getItemName());
		}
	}
	
	/**
	 * update items on call end
	 * @param config
	 */
	private void endCallItemUpdate(FreeswitchBindingConfig config){
		
		OnOffType activeState =  OnOffType.OFF;;
		CallType callType = (CallType)CallType.EMPTY;
		StringType callerId = StringType.EMPTY;

		/*
		 * A channel has ended that has this item associated with it
		 * We still need to check if this item is associated with other
		 * channels.
		 * We are going to iterate backwards to get the last added channel;
		 */
		ListIterator<String> it =
			    new ArrayList<String>(itemMap.keySet()).listIterator(itemMap.size());
		
		//if we get a match we will stop processing
		boolean match = false;
		while (it.hasPrevious()) {
			String uuid = it.previous();
			for(FreeswitchBindingConfig c : itemMap.get(uuid)){
				if(c.getItemName().equals(config.getItemName())){
					Channel channel = eventCache.get(uuid);
					activeState = OnOffType.ON;
					callType = channel.getCall();
					callerId = new StringType(String.format("%s : %s",
							channel.getEventHeader(CID_NAME),
							channel.getEventHeader(CID_NUMBER)));
					match = true;
					break;
				}
			}
			if(match)
				break;
		}
		if (config.getItemType().isAssignableFrom(SwitchItem.class)) {

			eventPublisher.postUpdate(config.getItemName(), activeState);
		}
		else if (config.getItemType().isAssignableFrom(CallItem.class)) {

			eventPublisher.postUpdate(config.getItemName(), callType);
		}
		else if (config.getItemType().isAssignableFrom(StringItem.class)) {

			eventPublisher.postUpdate(config.getItemName(), callerId);
		}
		else {
			logger.warn("handleHangupCall - postUpdate for itemType '{}' is undefined", config.getItemName());
		}
	}


	/**
	 * Handle message waiting indicator events (MWI)
	 * 
	 * A MWI looks has the following format
	 * 
	 * MWI-Messages-Waiting: yes
	 * MWI-Message-Account: jonas@gauffin.com
	 * MWI-Voice-Message: 2/1 (1/1)
	 * 
	 * The voice message line format translates to:
	 * total_new_messages / total_saved_messages (total_new_urgent_messages / total_saved_urgent_messages)
	 * 
	 * @param event to parse
	 */
	private void handleMessageWaiting(EslEvent event) {

		
		logger.debug("MWI event\\n {}", event.toString());
		
		for(String key : event.getEventHeaders().keySet()){
			logger.debug("MWI Message header {} : {}", 
					key,event.getEventHeaders().get(key));
		}
		
		String account = null;
		
		try {
			account = URLDecoder.decode(getHeader(event, MWI_ACCOUNT), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not decode account for event {} : {}", event, e);
			return;
		}

		boolean waiting = "yes".equalsIgnoreCase(getHeader(event, MWI_WAITING));

		String messagesString = getHeader(event, MWI_MESSAGE);

		logger.debug("Message header: {}", messagesString);

		if(StringUtils.isBlank(messagesString)){
			logger.debug("message is not for us.");
			return;
		}

		Pattern pattern = Pattern.compile("([0-9]+)/([0-9]+)\\s\\([0-9]+\\/[0-9]+\\)");

		Matcher matcher = pattern.matcher(messagesString);

		int messages = 0;

		if(matcher.matches()){
			logger.debug("trying to parse message number {} ", matcher.group(1));
			try {
				messages = Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				logger.warn("Could not parse message number from message {} : {}", messagesString, e);
			}
		}

		logger.debug("Updating MWI to {} VMs", messages);

		mwiCache.put(account, new MWIModel(waiting, messages));
		updateMessageWaitingItems();
	}

	/**
	 * update items for message waiting types for all providers
	 */
	private void updateMessageWaitingItems(){
		for (FreeswitchBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				FreeswitchBindingConfig config = provider.getFreeswitchBindingConfig(itemName);
				if(config.getType() == FreeswitchBindingType.MESSAGE_WAITING){
					updateMessageWaitingItem(config);
				}
			}
		}
	}

	/**
	 * update items for message waiting types
	 * @param itemName
	 * @param config
	 */
	private void updateMessageWaitingItem(FreeswitchBindingConfig config) {

		MWIModel model = mwiCache.get(config.getArgument());
		/*
		 * see if this is for us
		 */
		if(model == null)
			return;

		if (config.getItemType().isAssignableFrom(SwitchItem.class)) {
			eventPublisher.postUpdate(config.getItemName(), model.mwi ? OnOffType.ON : OnOffType.OFF);
		}
		else if (config.getItemType().isAssignableFrom(NumberItem.class)) {
			eventPublisher.postUpdate(config.getItemName(), new DecimalType(model.messages));
		}
		else {
			logger.warn("handle call for item type '{}' is undefined", config.getItemName());
		}
	}

	/**
	 * query freeswitch for the message count for VM accounts.  This should
	 * be done every time we connect to the system.
	 */
	private void initMessageItems(){
		mwiCache.clear();
		for (FreeswitchBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				FreeswitchBindingConfig config = provider.getFreeswitchBindingConfig(itemName);
				if(config.getType() == FreeswitchBindingType.MESSAGE_WAITING){
					String account = config.getArgument();
					if(!mwiCache.containsKey(account) &&  clientValid()){
						EslMessage msg = inboudClient.sendSyncApiCommand("vm_boxcount", account);
						if(msg.getBodyLines().size() == 1){
							try {
								int messages = Integer.parseInt(msg.getBodyLines().get(0));
								mwiCache.put(account, new MWIModel(messages > 0, messages));
								updateMessageWaitingItem(config);
							} catch (Exception e) {
								logger.error("Could not parse messages", e);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Execute a api command and return the body as a 
	 * single comma delimited String 
	 * @param command
	 * @return Each line of the response will be appended to the string, 
	 * delimited by a comma
	 */
	public String executeApiCommand(String command){
		
		logger.debug("Trying to execute API command {}", command);
		
		if(!clientValid() && StringUtils.isBlank(command)){
			logger.error("Bad command {}", command);
			return null;
		}
		
		String [] args = command.split(" ", 1);
		
		/*
		 * if we do not have 2 args then this is not valid
		 */
		if(args.length == 0){
			logger.error("Command did not contain a valid command string {}");
			return null;
		}
		
		EslMessage msg = inboudClient.sendSyncApiCommand(args[0], 
				args.length > 1 ? args[1] : "");
		
		List<String> bodyLines = msg.getBodyLines();
		
		StringBuilder builder = new StringBuilder();
		for(String line : bodyLines){
			if(builder.length() > 0)
				builder.append(",");
			builder.append(line);
		}
		return builder.toString();
	}
	/**
	 * Get a header from a esl event object
	 * @param event
	 * @param name
	 * @return
	 */
	private static String getHeader(EslEvent event, FreeswitchMessageHeader name){
		return getHeader(event, name.toString());
	}
	
	private static String getHeader(EslEvent event, String name){
		return event.getEventHeaders().get(name);
	}

	private boolean clientValid(){
		return inboudClient != null && inboudClient.canSend();
	}
	
	private String printEvent(EslEvent event){
		Map<String, String> headers = event.getEventHeaders();
		StringBuilder sb = new StringBuilder();
		for(String key : headers.keySet()){
			sb.append('\t').append(key).append(" = ").append(headers.get(key)).append('\n');
		}
		return sb.toString();
	}

	private class MWIModel {
		protected boolean mwi = false;
		protected int messages = 0;
		public MWIModel(boolean mwi, int messages) {
			super();
			this.mwi = mwi;
			this.messages = messages;
		}

	}

	private class Channel {
		protected EslEvent event;
		public Channel(EslEvent newChannelEvent) {
			super();
			this.event = newChannelEvent;
		}

		public CallType getCall(){
			String dest = getEventHeader(DEST_NUMBER);
			String orig = getEventHeader(ORIG_NUMBER);
			if(StringUtils.isBlank(dest))
				dest = "unknown";
			if(StringUtils.isBlank(orig))
				orig = "unknown";
			return new CallType(orig,dest);
		}

		public String getEventHeader(FreeswitchMessageHeader header){
			return getEventHeader(header.toString());
		}
		public String getEventHeader(String header){
			return getHeader(event, header);
		}
	}
	
	/**
	 * The Freeswitch ESL library we are using does not tell us when
	 * a connection dies, we need to poll and reconnect, which is what the
	 * WatchDog class does.
	 * @author daniel
	 *
	 */
	private class WatchDog extends Thread{
		private boolean running;
		private Object lock = new Object();
		
		public WatchDog(){
			super("Freeswitch WatchDog");
			running = true;
		}
		
		@Override
		public void run(){
			/*
			 * Check that our client is connected, try reconnecting if not
			 */
			
			while(running){
				if(!clientValid()){
					try {
						logger.warn("Client is not connected, reconnecting");
						connect();
					} catch (InboundConnectionFailure e) {
						logger.error("Could not connect to freeswitch server",e);
					}
				}
				synchronized (lock) {
					try {
						lock.wait(WATCHDOG_INTERVAL);
					} catch (InterruptedException ignored) {
					}
				}
			}
		}
		
		/**
		 * Stops the watchdog from running
		 */
		public void stopRunning(){
			this.running = false;
			lock.notifyAll();
		}
		
		public boolean isRunning(){
			return running;
		}
	}
}
