/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
	private String m_name;
	private boolean m_isStatus;
	
	private String m_dispatcher = null;
	private String m_pollHandler = null;
	private String m_defaultMsgHandler = null;
	private String m_defaultCmdHandler = null;
	private HashMap<Integer, String> m_messageHandlers = new HashMap<Integer, String>();
	private HashMap<Class<? extends Command>, String> m_commandHandlers = new HashMap<Class<? extends Command>, String>();
	
	public String getName() { return m_name; }
	public boolean isStatusFeature() { return m_isStatus; }
	public String getPollHandler() { return m_pollHandler; }
	public String getDispatcher() { return m_dispatcher; }
	public String getDefaultCommandHandler() { return m_defaultCmdHandler; }
	public String getDefaultMessageHandler() { return m_defaultMsgHandler; }
	
	/**
	 * Retrieves a hashmap of message command code to command handler name
	 * @return a Hashmap from Integer to String representing the command codes and the associated message handlers
	 */
	public HashMap<Integer, String> getMessageHandlers() { return m_messageHandlers; }
	/**
	 * Similar to getMessageHandlers(), but for command handlers
	 * Instead of Integers it uses the class of the Command as a key
	 * @see #getMessageHandlers()
	 * @return a HashMap from Command Classes to CommandHandler names
	 */
	public HashMap<Class<? extends Command>, String> getCommandHandlers() { return m_commandHandlers; }
	
	public void setName(String name) { m_name = name; }
	
	public void setStatusFeature(boolean status) { m_isStatus = status; }
	
	public void setMessageDispatcher(String dispatcher) { m_dispatcher = dispatcher; }
	public void setPollHandler(String poll) { m_pollHandler = poll; }
	public void setDefaultCommandHandler(String cmd) { m_defaultCmdHandler = cmd; }
	public void setDefaultMessageHandler(String msg) { m_defaultMsgHandler = msg; }
	/**
	 * Adds a message handler mapped from the command which this handler should be invoked for
	 * to the name of the handler to be created
	 */
	public void addMessageHandler(int cmd, String handler) {
		m_messageHandlers.put(cmd, handler);
	}
	/**
	 * Adds a command handler mapped from the command class which this handler should be invoke for
	 * to the name of the handler to be created
	 */
	public void addCommandHandler(Class<? extends Command> command, String handler) {
		m_commandHandlers.put(command, handler);
	}
	/**
	 * Builds the actual feature
	 * @return the feature which this template describes
	 */
	public DeviceFeature build() {
		DeviceFeature f = new DeviceFeature(m_name);
		f.setStatusFeature(m_isStatus);
		
		if (m_dispatcher != null) f.setMessageDispatcher((DeviceFeature.MessageDispatcher)
													DeviceFeature.s_makeHandler(m_dispatcher, f));
		if (m_pollHandler != null) f.setPollHandler((DeviceFeature.PollHandler)DeviceFeature.s_makeHandler(m_pollHandler, f));
		if (m_defaultCmdHandler != null) f.setDefaultCommandHandler((DeviceFeature.CommandHandler)
													DeviceFeature.s_makeHandler(m_defaultCmdHandler, f));
		if (m_defaultMsgHandler != null) f.setDefaultMsgHandler((DeviceFeature.MessageHandler)
													DeviceFeature.s_makeHandler(m_defaultMsgHandler, f));
		for (Entry<Integer, String> mH : m_messageHandlers.entrySet()) {
			f.addMessageHandler(mH.getKey(), (DeviceFeature.MessageHandler)DeviceFeature.s_makeHandler(mH.getValue(), f));
		}
		for (Entry<Class<? extends Command>, String> cH : m_commandHandlers.entrySet()) {
			f.addCommandHandler(cH.getKey(), (DeviceFeature.CommandHandler)DeviceFeature.s_makeHandler(cH.getValue(), f));
		}
		return f;
	}
	
	public String toString() {
		return getName() + "(" + isStatusFeature() + ")";
	}
}
