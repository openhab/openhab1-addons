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

import org.openhab.binding.freeswitch.FreeswitchBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.library.tel.items.CallItem;
import org.openhab.library.tel.types.CallType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Dan Cunningham
 * @since 1.4.0
 */
public class FreeswitchBinding extends AbstractActiveBinding<FreeswitchBindingProvider> implements ManagedService, IEslEventListener {

	private static final Logger logger = 
			LoggerFactory.getLogger(FreeswitchBinding.class);

	protected static String CH_CREATE = "CHANNEL_CREATE";
	protected static String CH_DESTROY = "CHANNEL_DESTROY";
	protected static String MESSAGE_WAITING = "MESSAGE_WAITING";
	protected static String FS_UUID = "Channel-Call-UUID";
	protected static String FS_CID_NAME = "Caller-Caller-ID-Name";
	protected static String FS_CID_NUMBER = "Caller-Caller-ID-Number";
	protected static String FS_DEST_NUMBER = "Caller-Destination-Number";
	protected static String FS_ORIG_NUMBER = "Caller-ANI";
	protected static String FS_MWI_WAITING = "MWI-Messages-Waiting";
	protected static String FS_MWI_ACCOUNT = "MWI-Message-Account";
	protected static String FS_MWI_MESSAGE = "MWI-Voice-Message";

	protected Map<String, Channel> eventCache;
	protected Map<String, LinkedList<FreeswitchBindingConfig>> itemMap;
	protected Map<String,MWIModel> mwiCache;

	private Client inboudClient;
	private String host;
	private String password;
	private int port;

	private long refreshInterval = 30000;

	public FreeswitchBinding() {
	}

	@Override
	public void activate() {
		logger.debug("activate() is called!");
	}

	@Override
	public void deactivate() {
		logger.debug("deactivate() is called!");
		disconnect();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called!");

		for (FreeswitchBindingProvider provider : providers) {
			FreeswitchBindingConfig config = provider.getFreeswitchBindingConfig(itemName);
			switch(config.getType()){
			case CMD_API:{
				if (!(command instanceof StringType)){
					logger.error("API commands must be a StringType");
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
	protected void execute() {
		/*
		 * Check that our client is connected, try reconnecting if not
		 */
		if(!clientValid()){
			try {
				connect();
			} catch (InboundConnectionFailure e) {
				logger.error(String.format("Could not connect to freeswitch server: %s", e.getMessage()));
			}
		}
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
		return "Freeswitch Binding";
	}


	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("updated() is called!");
		if (config != null) {

			port = 8021;
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
				logger.error(String.format("Could not connect to freeswitch server: %s", e.getMessage()),e);
			}
		}
	}


	@Override
	public void eventReceived( EslEvent event) {
		//logger.debug(String.format("Recieved ESLEvent %s", event.getEventName()));
		if(event.getEventName().equals(CH_CREATE)){
			handleNewCallEvent(event);
		}
		else if(event.getEventName().equals(CH_DESTROY)){
			handleHangupCallEvent(event);
		}
		else if(event.getEventName().equals(MESSAGE_WAITING)){
			handleMessageWaiting(event);
		}
	}

	@Override
	public void backgroundJobResultReceived(EslEvent arg0) {
	}

	/**
	 * Connect inbound client to freeswwitch
	 * @throws InboundConnectionFailure
	 */
	private void connect() throws InboundConnectionFailure {

		disconnect();

		logger.debug(String.format("Connecting to %s on port %d with pass %s", 
				host, port, password));
		
		inboudClient = new Client();
		inboudClient.connect(host, port,password, 10);
		inboudClient.addEventListener(this);
		inboudClient.setEventSubscriptions("plain",String.format("%s %s %s", 
				CH_CREATE, CH_DESTROY, MESSAGE_WAITING));
		
		logger.debug(String.format("Connected"));
		
		initMessageItems();
	}

	/**
	 * disconnect inbound client from freeswitch
	 */
	private void disconnect() {
		if(inboudClient != null){
			inboudClient.close();
			inboudClient = null;
		}
	}

	/**
	 * Handle Answer or Media (ringing) events and add an entry to our cache
	 * @param event
	 */
	private void handleNewCallEvent(EslEvent event) {

		String uuid = getHeader(event, FS_UUID);
		Channel channel = new Channel(event);
		
		eventCache.put(uuid, channel);
		itemMap.put(uuid, new LinkedList<FreeswitchBindingConfig>());
		
		CallType call = channel.getCall();
		
		logger.debug(String.format("Adding new call to : %s from:%s", 
				call.getDestNum(), call.getOrigNum()));
		logger.debug(event.getEventHeaders().toString());

		for (FreeswitchBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				FreeswitchBindingConfig config = provider.getFreeswitchBindingConfig(itemName);
				if(config.getType() == FreeswitchBindingType.ACTIVE){
					/*
					 * Check if this is a filtered item
					 */
					if(config.filtered()){
						logger.debug(String.format("Trying to match filter string %s",config.getArgument()));
						String[] filters = config.getArgument().split(",");
						boolean matched = true;
						for(String filterString : filters){
							String [] args = filterString.split(":");
							if(args.length == 2 && StringUtils.isNotBlank(args[0]) && StringUtils.isNotBlank(args[1])){
								String value = channel.getEventHeader(args[0]);
								try {
									//is the header blank/null or does the filter value not match the header value
									if(StringUtils.isBlank(value) || !args[1].equals(URLDecoder.decode(value, "UTF-8"))){
										//this item is filtered, but this call does not match
										matched = false;
									}
								} catch (UnsupportedEncodingException e) {
									logger.warn("Could not decode header", e);
									matched = false;
								}
							} else {
								logger.warn("The filter string does not look valid, not updating item");
								matched = false;
							}
							/*
							 * if no match then do not continue matching
							 */
							if(!matched)
								break;
						}
						/*
						 * we found a channel that matches our  filter string
						 */
						if(matched){
							itemMap.get(uuid).add(config);
							newCallItemUpdate(config, channel);
						}
					} else {
						/*
						 * This is not filtered, if its an inbound call then add it
						 */
						String direction = channel.getEventHeader("Call-Direction");
						if(StringUtils.isNotBlank(direction) && "inbound".equals(direction)){
							itemMap.get(uuid).add(config);
							newCallItemUpdate(config, channel);
						}
					}
				}
			}
		}

	}

	/**
	 * Handle channel destroy events and remove entries from our cache
	 * @param event
	 */
	private void handleHangupCallEvent(EslEvent event) {
		logger.debug("Removing Call");
		logger.debug(event.toString());
		
		eventCache.remove(getHeader(event, FS_UUID));
		
		LinkedList<FreeswitchBindingConfig> configs = itemMap.remove(getHeader(event, FS_UUID));
		
		if( configs != null ){
			for(FreeswitchBindingConfig config : configs){
				endCallItemUpdate(config);
			}
		}
	}

	/**
	 *  Update items for new calls
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
							channel.getEventHeader(FS_CID_NAME),
							channel.getEventHeader(FS_CID_NUMBER))));
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
							channel.getEventHeader(FS_CID_NAME),
							channel.getEventHeader(FS_CID_NUMBER)));
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
	 * @param event
	 */
	private void handleMessageWaiting(EslEvent event) {

		String account = null;
		try {
			account = URLDecoder.decode(getHeader(event, FS_MWI_ACCOUNT), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not decode account", e);
			return;
		}

		boolean waiting = "yes".equalsIgnoreCase(getHeader(event, FS_MWI_WAITING));

		//MWI-Messages-Waiting: yes
		//MWI-Message-Account: jonas@gauffin.com
		//MWI-Voice-Message: 2/1 (1/1)
		//total_new_messages / total_saved_messages (total_new_urgent_messages / total_saved_urgent_messages)

		logger.debug(String.format("MWI event\\n %s", event.toString()));

		String messagesString = getHeader(event, FS_MWI_MESSAGE);

		logger.debug(String.format("Message header: %s", messagesString));

		if(StringUtils.isBlank(messagesString)){
			logger.warn("Invalid message string, aborting");
			return;
		}

		Pattern pattern = Pattern.compile("([0-9]+)/([0-9]+)\\s\\([0-9]+\\/[0-9]+\\)");

		Matcher matcher = pattern.matcher(messagesString);

		int messages = 0;

		if(matcher.matches()){
			logger.debug("trying to parse message number");
			try {
				messages = Integer.parseInt(matcher.group(1));
			} catch (Exception e) {
				logger.warn("Could not parse message number", e);
			}
		}

		logger.debug(String.format("Updating MWI to %d VMs", messages));

		mwiCache.put(account, new MWIModel(waiting, messages));
		updateMessageWaitingItems();
	}

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
		
		logger.debug(String.format("Trying to execute API command %s", command));
		
		if(!clientValid() && StringUtils.isBlank(command)){
			logger.error(String.format("Bad command %s", command));
			return null;
		}
		
		String [] args = command.split(" ", 1);
		
		/*
		 * if we do not have 2 args then this is not valid
		 */
		if(args.length == 0){
			logger.error(String.format("Command did not contain a valid command string %s", command));
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
	private static String getHeader(EslEvent event, String name){
		return event.getEventHeaders().get(name);
	}

	private boolean clientValid(){
		return inboudClient != null && inboudClient.canSend();
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
			String dest = getEventHeader(FS_DEST_NUMBER);
			String orig = getEventHeader(FS_ORIG_NUMBER);
			if(StringUtils.isBlank(dest))
				dest = "unknown";
			if(StringUtils.isBlank(orig))
				orig = "unknown";
			return new CallType(getEventHeader(FS_ORIG_NUMBER), 
					getEventHeader(FS_DEST_NUMBER));
		}

		public String getEventHeader(String name){
			return getHeader(event, name);
		}
	}
}
