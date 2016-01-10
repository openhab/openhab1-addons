/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.zwave.internal.HexToIntegerConverter;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveNodeNamingCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeStatusEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeInitStage;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeStageAdvancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Z-Wave node class. Represents a node in the Z-Wave network.
 * @author Brian Crosby
 * @author Chris Jackson
 * @since 1.3.0
 */
@XStreamAlias("node")
public class ZWaveNode {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveNode.class);

	private final ZWaveDeviceClass deviceClass;
	@XStreamOmitField
	private ZWaveController controller;
	@XStreamOmitField
	private ZWaveNodeStageAdvancer nodeStageAdvancer;
	@XStreamOmitField
	private ZWaveNodeState nodeState;

	@XStreamConverter(HexToIntegerConverter.class)
	private int homeId = Integer.MAX_VALUE;
	private int nodeId = Integer.MAX_VALUE;
	private int version = Integer.MAX_VALUE;
	
	private String name;
	private String location;

	@XStreamConverter(HexToIntegerConverter.class)
	private int manufacturer = Integer.MAX_VALUE;
	@XStreamConverter(HexToIntegerConverter.class)
	private int deviceId = Integer.MAX_VALUE;
	@XStreamConverter(HexToIntegerConverter.class)
	private int deviceType = Integer.MAX_VALUE;
	
	private boolean listening;			 // i.e. sleeping
	private boolean frequentlyListening; 
	private boolean routing;
	private String healState;
    @SuppressWarnings("unused")
    private boolean security;
    @SuppressWarnings("unused")
    private boolean beaming;
    @SuppressWarnings("unused")
    private int maxBaudRate;

    // Keep the NIF - just used for information and debug in the XML
    @SuppressWarnings("unused")
    private List<Integer> nodeInformationFrame = null;
	
	private Map<CommandClass, ZWaveCommandClass> supportedCommandClasses = new HashMap<CommandClass, ZWaveCommandClass>();
	private List<Integer> nodeNeighbors = new ArrayList<Integer>();
	private Date lastSent = null;
	private Date lastReceived = null;

	@XStreamOmitField
	private boolean applicationUpdateReceived = false;

	@XStreamOmitField
	private int resendCount = 0;

	@XStreamOmitField
	private int receiveCount = 0;
	@XStreamOmitField
	private int sendCount = 0;
	@XStreamOmitField
	private int deadCount = 0;
	@XStreamOmitField
	private Date deadTime;	
	@XStreamOmitField
	private int retryCount = 0;

	/**
	 * Constructor. Creates a new instance of the ZWaveNode class.
	 * @param homeId the home ID to use.
	 * @param nodeId the node ID to use.
	 * @param controller the wave controller instance
	 */
	public ZWaveNode(int homeId, int nodeId, ZWaveController controller) {
		nodeState = ZWaveNodeState.ALIVE;
		this.homeId = homeId;
		this.nodeId = nodeId;
		this.controller = controller;
		this.nodeStageAdvancer = new ZWaveNodeStageAdvancer(this, controller);
		this.deviceClass = new ZWaveDeviceClass(Basic.NOT_KNOWN, Generic.NOT_KNOWN, Specific.NOT_USED);
	}

	/**
	 * Configures the node after it's been restored from file.
	 * NOTE: XStream doesn't run any default constructor. So, any initialisation
	 * made in a constructor, or statically, won't be performed!!!
	 * Set defaults here if it's important!!!
	 * @param controller the wave controller instance
	 */
	public void setRestoredFromConfigfile(ZWaveController controller) {
		nodeState = ZWaveNodeState.ALIVE;

		this.controller = controller;

		// Create the initialisation advancer and tell it we've loaded from file
		this.nodeStageAdvancer = new ZWaveNodeStageAdvancer(this, controller);
		this.nodeStageAdvancer.setRestoredFromConfigfile();
		nodeStageAdvancer.setCurrentStage(ZWaveNodeInitStage.EMPTYNODE);
	}

	/**
	 * Gets the node ID.
	 * @return the node id
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Gets whether the node is listening.
	 * @return boolean indicating whether the node is listening or not.
	 */
	public boolean isListening() {
		return listening;
	}
	
	/**
	 * Sets whether the node is listening.
	 * @param listening
	 */
	public void setListening(boolean listening) {
		this.listening = listening;
	}

	/**
	 * Gets whether the node is frequently listening.
	 * Frequently listening is responding to a beam signal. Apart from
	 * increased latency, nothing else is noticeable from the serial api
	 * side.
	 * @return boolean indicating whether the node is frequently
	 * listening or not.
	 */
	public boolean isFrequentlyListening() {
		return frequentlyListening;
	}
	
	/**
	 * Sets whether the node is frequently listening.
	 * Frequently listening is responding to a beam signal. Apart from
	 * increased latency, nothing else is noticeable from the serial api
	 * side.
	 * @param frequentlyListening indicating whether the node is frequently
	 * listening or not.
	 */
	public void setFrequentlyListening(boolean frequentlyListening) {
		this.frequentlyListening = frequentlyListening;
	}
	
	/**
	 * Gets the Heal State of the node.
	 * @return String indicating the node Heal State.
	 */
	public String getHealState() {
		return healState;
	}
	
	/**
	 * Sets the Heal State of the node.
	 * @param healState
	 */
	public void setHealState(String healState) {
		this.healState = healState;
	}

	/**
	 * Gets whether the node is dead.
	 * @return
	 */
	public boolean isDead() {
		if(nodeState == ZWaveNodeState.DEAD || nodeState == ZWaveNodeState.FAILED) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Sets the node to be 'undead'.
	 */
	public void setNodeState(ZWaveNodeState state) {
		// Make sure we only handle real state changes
		if(state == nodeState) {
			return;
		}

		switch(state) {
		case ALIVE:
			logger.debug("NODE {}: Node has risen from the DEAD. Init stage is {}:{}.", nodeId,
					this.getNodeInitializationStage().toString());			
	
			// Reset the resend counter
			this.resendCount = 0;
			break;

		case DEAD:
			// If the node is failed, then we don't allow transitions to DEAD
			// The only valid state change from FAILED is to ALIVE
			if(nodeState == ZWaveNodeState.FAILED) {
				return;
			}
		case FAILED:
			this.deadCount++;
			this.deadTime = Calendar.getInstance().getTime();
			logger.debug("NODE {}: Node is DEAD.", this.nodeId);
			break;
		}

		// Don't alert state changes while we're still initialising
		if(nodeStageAdvancer.isInitializationComplete() == true) {
			ZWaveEvent zEvent = new ZWaveNodeStatusEvent(this.getNodeId(), ZWaveNodeState.DEAD);
			controller.notifyEventListeners(zEvent);
		}
		else {
			logger.debug("NODE {}: Initialisation incomplete, not signalling state change.", this.nodeId);				
		}

		nodeState = state;
	}
	
	/**
	 * Gets the home ID
	 * @return the homeId
	 */
	public Integer getHomeId() {
		return homeId;
	}

	/**
	 * Gets the node name.
	 * If Node Naming Command Class is supported get name from device,
	 * else return the name stored in binding
	 * @return the name
	 */
	public String getName() {
		ZWaveNodeNamingCommandClass commandClass = (ZWaveNodeNamingCommandClass) getCommandClass(CommandClass.NODE_NAMING);
				
		if( commandClass == null ) {
			return this.name;
		}
		
		return commandClass.getName();
	}

	/**
	 * Sets the node name.
	 * If Node Naming Command Class is supported set name in the device,
	 * else set it in locally in the binding
	 * @param name the name to set
	 */
	public void setName(String name) {
		ZWaveNodeNamingCommandClass commandClass = (ZWaveNodeNamingCommandClass) getCommandClass(CommandClass.NODE_NAMING);
		
		if( commandClass == null ) {
			this.name = name;
			return;
		}

		SerialMessage m = commandClass.setNameMessage(name);
		this.controller.sendData(m);
	}

	/**
	 * Gets the node location.
	 * If Node Naming Command Class is supported get location from device, 
	 * else return the location stored in binding
	 * @return the location
	 */
	public String getLocation() {
		ZWaveNodeNamingCommandClass commandClass = (ZWaveNodeNamingCommandClass) getCommandClass(CommandClass.NODE_NAMING);
		
		if( commandClass == null ) {
			return this.location;
		}
		
		return commandClass.getLocation();
	}

	/**
	 * Sets the node location.
	 * If Node Naming Command Class is supported set location in the device,
	 * else set it in locally in the binding 
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		ZWaveNodeNamingCommandClass commandClass = (ZWaveNodeNamingCommandClass) getCommandClass(CommandClass.NODE_NAMING);
		
		if( commandClass == null ) {
			this.location = location;
			return;
		}

		SerialMessage m = commandClass.setLocationMessage(location);
		this.controller.sendData(m);
	}

	/**
	 * Gets the manufacturer of the node.
	 * @return the manufacturer
	 */
	public int getManufacturer() {
		return manufacturer;
	}

	/**
	 * Sets the manufacturer of the node.
	 * @param tempMan the manufacturer to set
	 */
	public void setManufacturer(int tempMan) {
		this.manufacturer = tempMan;
	}

	/**
	 * Gets the device id of the node.
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id of the node.
	 * @param tempDeviceId the device to set
	 */
	public void setDeviceId(int tempDeviceId) {
		this.deviceId = tempDeviceId;
	}

	/**
	 * Gets the device type of the node.
	 * @return the deviceType
	 */
	public int getDeviceType() {
		return deviceType;
	}

	/**
	 * Sets the device type of the node.
	 * @param tempDeviceType the deviceType to set
	 */
	public void setDeviceType(int tempDeviceType) {
		this.deviceType = tempDeviceType;
	}

	/**
	 * Get the date/time the node was last updated (ie a frame was received from it).
	 * @return the lastUpdated time
	 */
	public Date getLastReceived() {
		return lastReceived;
	}

	/**
	 * Get the date/time we last sent a frame to the node.
	 * @return the lastSent
	 */
	public Date getLastSent() {
		return lastSent;
	}

	/**
	 * Gets the node state.
	 * @return the nodeState
	 */
	public ZWaveNodeState getNodeState() {
		return this.nodeState;
	}

	/**
	 * Gets the node stage.
	 * @return the nodeStage
	 */
	public ZWaveNodeInitStage getNodeInitializationStage() {
		return this.nodeStageAdvancer.getCurrentStage();
	}

	/**
	 * Gets the initialization state
	 * @return true if initialization has been completed
	 */
	public boolean isInitializationComplete() {
		return this.nodeStageAdvancer.isInitializationComplete();
	}
	

	/**
	 * Sets the node stage.
	 * @param nodeStage the nodeStage to set
	 */
	public void setNodeStage(ZWaveNodeInitStage nodeStage) {
		nodeStageAdvancer.setCurrentStage(nodeStage);
	}

	/**
	 * Gets the node version
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the node version.
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}


	/**
	 * Gets the node application firmware version
	 * @return the version
	 */
	public String getApplicationVersion() {
		ZWaveVersionCommandClass versionCmdClass = (ZWaveVersionCommandClass) this.getCommandClass(CommandClass.VERSION);
		if(versionCmdClass == null) {
			return "0.0";
		}

		String appVersion = versionCmdClass.getApplicationVersion();
		if(appVersion == null) {
			logger.trace("NODE {}: App version requested but version is unknown", this.getNodeId());
			return "0.0";
		}
		
		return appVersion;
	}

	/**
	 * Gets whether the node is routing messages.
	 * @return the routing
	 */
	public boolean isRouting() {
		return routing;
	}

	/**
	 * Sets whether the node is routing messages.
	 * @param routing the routing to set
	 */
	public void setRouting(boolean routing) {
		this.routing = routing;
	}

	/**
	 * Gets the time stamp the node was last queried.
	 * @return the queryStageTimeStamp
	 */
	public Date getQueryStageTimeStamp() {
		return this.nodeStageAdvancer.getQueryStageTimeStamp();
	}

	/**
	 * Increments the resend counter.
	 * On three increments the node stage is set to DEAD and no
	 * more messages will be sent.
	 * This is only used for SendData messages.
	 */
	public void incrementResendCount() {
		if (++resendCount >= 3) {
			setNodeState(ZWaveNodeState.DEAD);
		}
		this.retryCount++;
	}

	/**
	 * Resets the resend counter and possibly resets the
	 * node stage to DONE when previous initialization was
	 * complete.
	 * Note that if the node is DEAD, then the nodeStage stays DEAD
	 */
	public void resetResendCount() {
		this.resendCount = 0;
		if (this.nodeStageAdvancer.isInitializationComplete() && this.isDead() == false) {
			nodeStageAdvancer.setCurrentStage(ZWaveNodeInitStage.DONE);
		}
	}

	/**
	 * Returns the device class of the node.
	 * @return the deviceClass
	 */
	public ZWaveDeviceClass getDeviceClass() {
		return deviceClass;
	}

	/**
	 * Returns the Command classes this node implements.
	 * @return the command classes.
	 */
	public Collection<ZWaveCommandClass> getCommandClasses() {
		return supportedCommandClasses.values();
	}
	
	/**
	 * Returns a commandClass object this node implements.
	 * Returns null if command class is not supported by this node.
	 * @param commandClass The command class to get.
	 * @return the command class.
	 */
	public ZWaveCommandClass getCommandClass(CommandClass commandClass)
	{
		return supportedCommandClasses.get(commandClass);
	}
	
	/**
	 * Returns whether a node supports this command class.
	 * @param commandClass the command class to check
	 * @return true if the command class is supported, false otherwise.
	 */
	public boolean supportsCommandClass(CommandClass commandClass) {
		return supportedCommandClasses.containsKey(commandClass);
	}
	
	/**
	 * Adds a command class to the list of supported command classes by this node.
	 * Does nothing if command class is already added.
	 * @param commandClass the command class instance to add.
	 */
	public void addCommandClass(ZWaveCommandClass commandClass)
	{
		CommandClass key = commandClass.getCommandClass();
		
		if (!supportedCommandClasses.containsKey(key)) {
			logger.debug("NODE {}: Adding command class {} to the list of supported command classes.", nodeId, commandClass.getCommandClass().getLabel());
			supportedCommandClasses.put(key, commandClass);
			
			if (commandClass instanceof ZWaveEventListener) {
				this.controller.addEventListener((ZWaveEventListener)commandClass);
			}
		}
	}
	
	/**
	 * Removes a command class from the node.
	 * This is used to remove classes that a node may report it supports
	 * but it doesn't respond to.
	 * @param commandClass The command class key
	 */
	public void removeCommandClass(CommandClass commandClass) {
		supportedCommandClasses.remove(commandClass);
	}

	/**
	 * Resolves a command class for this node. First endpoint is checked. 
	 * If endpoint == 0 or (endpoint != 1 and version of the multi instance 
	 * command == 1) then return a supported command class on the node itself. 
	 * If endpoint != 1 and version of the multi instance command == 2 then
	 * first try command classes of endpoints. If not found the return a  
	 * supported command class on the node itself.
	 * Returns null if a command class is not found.
	 * @param commandClass The command class to resolve.
	 * @param endpointId the endpoint / instance to resolve this command class for.
	 * @return the command class.
	 */
	public ZWaveCommandClass resolveCommandClass(CommandClass commandClass, int endpointId)
	{
		if (commandClass == null) {
			return null;
		}
		
		if (endpointId == 0) {
			return getCommandClass(commandClass);
		}
		
		ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) supportedCommandClasses.get(CommandClass.MULTI_INSTANCE);
		if (multiInstanceCommandClass == null) {
			return null;	
		}
		else if (multiInstanceCommandClass.getVersion() == 2) {
			ZWaveEndpoint endpoint = multiInstanceCommandClass.getEndpoint(endpointId);
			
			if (endpoint != null) { 
				ZWaveCommandClass result = endpoint.getCommandClass(commandClass);
				if (result != null) {
					return result;
				}
			}
		}
		else if (multiInstanceCommandClass.getVersion() == 1) {
			ZWaveCommandClass result = getCommandClass(commandClass);
			if (result != null && endpointId <= result.getInstances()) {
				return result;
			}
		} else {
			logger.warn("NODE {}: Unsupported multi instance command version: {}.", nodeId, multiInstanceCommandClass.getVersion());
		}
		
		return null;
	}

	/**
	 * Initialise the node
	 */
	public void initialiseNode() {
		this.nodeStageAdvancer.startInitialisation();		
	}

	/**
	 * Encapsulates a serial message for sending to a 
	 * multi-instance instance/ multi-channel endpoint on
	 * a node.
	 * @param serialMessage the serial message to encapsulate
	 * @param commandClass the command class used to generate the message.
	 * @param endpointId the instance / endpoint to encapsulate the message for
	 * @param node the destination node.
	 * @return SerialMessage on success, null on failure.
	 */
	public SerialMessage encapsulate(SerialMessage serialMessage, 
			ZWaveCommandClass commandClass, int endpointId) {
		ZWaveMultiInstanceCommandClass multiInstanceCommandClass;
		
		if (serialMessage == null) {
			return null;
		}
		
		// no encapsulation necessary.
		if (endpointId == 0) {
			return serialMessage;
		}
		
		multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass)this.getCommandClass(CommandClass.MULTI_INSTANCE);
		
		if (multiInstanceCommandClass != null) {
			logger.debug("NODE {}: Encapsulating message, instance / endpoint {}", this.getNodeId(), endpointId);
			switch (multiInstanceCommandClass.getVersion()) {
				case 2:
					if (commandClass.getEndpoint() != null) {
						serialMessage = multiInstanceCommandClass.getMultiChannelEncapMessage(serialMessage, commandClass.getEndpoint());
						return serialMessage;
					}
					break;
				case 1:
				default:
					if (commandClass.getInstances() >= endpointId) {
						serialMessage = multiInstanceCommandClass.getMultiInstanceEncapMessage(serialMessage, endpointId);
						return serialMessage;
					}
					break;
			}
		}

		logger.warn("NODE {}: Encapsulating message, instance / endpoint {} failed, will discard message.", this.getNodeId(), endpointId);
		return null;
	}

	/**
	 * Return a list with the nodes neighbors
	 * @return list of node IDs
	 */
	public List<Integer> getNeighbors() {
		return nodeNeighbors;
	}
	
	/**
	 * Clear the neighbor list
	 */
	public void clearNeighbors() {
		nodeNeighbors.clear();
	}

	/**
	 * Updates a nodes routing information
	 * Generation of routes uses associations
	 * @param nodeId
	 */
	public ArrayList<Integer> getRoutingList() {
		logger.debug("NODE {}: Update return routes", nodeId);

		// Create a list of nodes this device is configured to talk to
    	ArrayList<Integer> routedNodes = new ArrayList<Integer>();

    	// Only update routes if this is a routing node
    	if(isRouting() == false) {
    		logger.debug("NODE {}: Node is not a routing node. No routes can be set.", nodeId);
    		return null;
    	}

    	// Get the number of association groups reported by this node
		ZWaveAssociationCommandClass associationCmdClass = (ZWaveAssociationCommandClass) getCommandClass(CommandClass.ASSOCIATION);
		if(associationCmdClass == null) {
    		logger.debug("NODE {}: Node has no association class. No routes can be set.", nodeId);
    		return null;
    	}
		
		int groups = associationCmdClass.getGroupCount();
		if(groups != 0) {
			// Loop through each association group and add the node ID to the list
			for(int group = 1; group <= groups; group++) {
				for(Integer associationNodeId : associationCmdClass.getGroupMembers(group)) {
					routedNodes.add(associationNodeId);
				}
			}
		}

		// Add the wakeup destination node to the list for battery devices
		ZWaveWakeUpCommandClass wakeupCmdClass = (ZWaveWakeUpCommandClass) getCommandClass(CommandClass.WAKE_UP);
		if(wakeupCmdClass != null) {
			Integer wakeupNodeId = wakeupCmdClass.getTargetNodeId();
			routedNodes.add(wakeupNodeId);
		}

		// Are there any nodes to which we need to set routes?
		if(routedNodes.size() == 0) {
    		logger.debug("NODE {}: No return routes required.", nodeId);
    		return null;
		}
		
		return routedNodes;
	}
	
	/**
	 * Add a node ID to the neighbor list
	 * @param nodeId the node to add
	 */
	public void addNeighbor(Integer nodeId) {
		nodeNeighbors.add(nodeId);
	}

	/**
	 * Gets the number of times the node has been determined as DEAD
	 * @return dead count
	 */
	public int getDeadCount() {
		return deadCount;
	}
	
	/**
	 * Gets the number of times the node has been determined as DEAD
	 * @return dead count
	 */
	public Date getDeadTime() {
		return deadTime;
	}
	
	/**
	 * Gets the number of packets that have been resent to the node
	 * @return retry count
	 */
	public int getRetryCount() {
		return retryCount;
	}

	/**
	 * Increments the sent packet counter and records the last sent time
	 * This is simply used for statistical purposes to assess the health
	 * of a node.
	 */
	public void incrementSendCount() {
		sendCount++;
		this.lastSent = Calendar.getInstance().getTime();
	}
	
	/**
	 * Increments the received packet counter and records the last received time
	 * This is simply used for statistical purposes to assess the health
	 * of a node.
	 */
	public void incrementReceiveCount() {
		receiveCount++;
		this.lastReceived = Calendar.getInstance().getTime();
	}
	
	/**
	 * Gets the number of packets sent to the node
	 * @return send count
	 */
	public int getSendCount() {
		return sendCount;
	}

	/**
	 * Gets the applicationUpdateReceived flag.
	 * This is set to indicate that we have received the required information from the device
	 * @return true if information received
	 */
	public boolean getApplicationUpdateReceived() {
		return applicationUpdateReceived;
	}

	/**
	 * Sets the applicationUpdateReceived flag.
	 * This is set to indicate that we have received the required information from the device
	 * @param received true if received
	 */
	public void setApplicationUpdateReceived(boolean received) {
		applicationUpdateReceived = received;
	}
	
    public void updateNIF(List<Integer> nif) {
        nodeInformationFrame = nif;
    }
    
    public void setSecurity(boolean security) {
        this.security = security;
    }

    public void setBeaming(boolean beaming) {
        this.beaming = beaming;
    }

    public void setMaxBaud(int maxBaudRate) {
        this.maxBaudRate = maxBaudRate;
    }
}
