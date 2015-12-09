/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;
import java.util.Map.Entry;

import org.openhab.core.types.Command;

/**
 * A simple class which contains the basic info needed to create a device feature.
 * Here, all handlers are represented as strings. The actual device feature
 * is then instantiated from the template by calling the build() function.
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */
public class FeatureTemplate {
	private String	m_name				= null;
	private String	m_timeout			= null;
	private boolean m_isStatus			= false;
	private HandlerEntry	m_dispatcher		= null;
	private HandlerEntry	m_pollHandler		= null;
	private HandlerEntry	m_defaultMsgHandler	= null;
	private HandlerEntry	m_defaultCmdHandler	= null;
	private HashMap<Integer, HandlerEntry> m_messageHandlers =
				new HashMap<Integer, HandlerEntry>();
	private HashMap<Class<? extends Command>, HandlerEntry> m_commandHandlers =
				new HashMap<Class<? extends Command>, HandlerEntry>();
	
	// simple getters
	public String getName() { return m_name; }
	public String getTimeout() { return m_timeout; }
	public boolean isStatusFeature() { return m_isStatus; }
	public HandlerEntry getPollHandler() { return m_pollHandler; }
	public HandlerEntry getDispatcher() { return m_dispatcher; }
	public HandlerEntry getDefaultCommandHandler() { return m_defaultCmdHandler; }
	public HandlerEntry getDefaultMessageHandler() { return m_defaultMsgHandler; }
	
	/**
	 * Retrieves a hashmap of message command code to command handler name
	 * @return a Hashmap from Integer to String representing the command codes and the associated message handlers
	 */
	public HashMap<Integer, HandlerEntry> getMessageHandlers() { return m_messageHandlers; }
	/**
	 * Similar to getMessageHandlers(), but for command handlers
	 * Instead of Integers it uses the class of the Command as a key
	 * @see #getMessageHandlers()
	 * @return a HashMap from Command Classes to CommandHandler names
	 */
	public HashMap<Class<? extends Command>, HandlerEntry> getCommandHandlers() { return m_commandHandlers; }

	// simple setters
	public void setName(String name) { m_name = name; }
	public void setStatusFeature(boolean status) { m_isStatus = status; }
	public void setTimeout(String s) { m_timeout = s; }
	public void setMessageDispatcher(HandlerEntry he) { m_dispatcher = he; }
	public void setPollHandler(HandlerEntry he) { m_pollHandler = he; }
	public void setDefaultCommandHandler(HandlerEntry cmd) { m_defaultCmdHandler = cmd; }
	public void setDefaultMessageHandler(HandlerEntry he) {	m_defaultMsgHandler = he; }
	/**
	 * Adds a message handler mapped from the command which this handler should be invoked for
	 * to the name of the handler to be created
	 * @param cmd command to be mapped
	 * @param he  handler entry to map to
	 */
	public void addMessageHandler(int cmd, HandlerEntry he) {
		m_messageHandlers.put(cmd, he);
	}
	/**
	 * Adds a command handler mapped from the command class which this handler should be invoke for
	 * to the name of the handler to be created
	 */
	public void addCommandHandler(Class<? extends Command> command, HandlerEntry he) {
		m_commandHandlers.put(command, he);
	}
	/**
	 * Builds the actual feature
	 * @return the feature which this template describes
	 */
	public DeviceFeature build() {
		DeviceFeature f = new DeviceFeature(m_name);
		f.setStatusFeature(m_isStatus);
		f.setTimeout(m_timeout);
		if (m_dispatcher != null) f.setMessageDispatcher(MessageDispatcher.s_makeHandler(m_dispatcher.getName(),
								m_dispatcher.getParams(), f));
		if (m_pollHandler != null) f.setPollHandler(PollHandler.s_makeHandler(m_pollHandler, f));
		if (m_defaultCmdHandler != null) f.setDefaultCommandHandler(CommandHandler.s_makeHandler(m_defaultCmdHandler.getName(),
								m_defaultCmdHandler.getParams(), f));
		if (m_defaultMsgHandler != null) f.setDefaultMsgHandler(MessageHandler.s_makeHandler(m_defaultMsgHandler.getName(),
				m_defaultMsgHandler.getParams(), f));
		for (Entry<Integer, HandlerEntry> mH : m_messageHandlers.entrySet()) {
			f.addMessageHandler(mH.getKey(), MessageHandler.s_makeHandler(mH.getValue().getName(), mH.getValue().getParams(), f));
		}
		for (Entry<Class<? extends Command>, HandlerEntry> cH : m_commandHandlers.entrySet()) {
			f.addCommandHandler(cH.getKey(), CommandHandler.s_makeHandler(cH.getValue().getName(), cH.getValue().getParams(), f));
		}
		return f;
	}
	
	public String toString() {
		return getName() + "(" + isStatusFeature() + ")";
	}
}
