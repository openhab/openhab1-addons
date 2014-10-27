/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeStatusEvent;
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
	private final ZWaveController controller;
	@XStreamOmitField
	private final ZWaveNodeStageAdvancer nodeStageAdvancer;

	@XStreamOmitField
	private int homeId;
	@XStreamOmitField
	private int nodeId;
	private int version;
	
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
	
	private Map<CommandClass, ZWaveCommandClass> supportedCommandClasses = new HashMap<CommandClass, ZWaveCommandClass>();
	private List<Integer> nodeNeighbors = new ArrayList<Integer>();
	private Date lastUpdated; 
	private Date queryStageTimeStamp;
	private volatile NodeStage nodeStage;

	@XStreamOmitField
	private int resendCount = 0;

	@XStreamOmitField
	private int sendCount = 0;
	@XStreamOmitField
	private int deadCount = 0;
	@XStreamOmitField
	private Date deadTime;	
	@XStreamOmitField
	private int retryCount = 0;

	// TODO: Implement ZWaveNodeValue for Nodes that store multiple values.
	
	/**
	 * Constructor. Creates a new instance of the ZWaveNode class.
	 * @param homeId the home ID to use.
	 * @param nodeId the node ID to use.
	 */
	public ZWaveNode(int homeId, int nodeId, ZWaveController controller) {
		this.homeId = homeId;
		this.nodeId = nodeId;
		this.controller = controller;
		this.nodeStageAdvancer = new ZWaveNodeStageAdvancer(this, controller);
		this.nodeStage = NodeStage.EMPTYNODE;
		this.deviceClass = new ZWaveDeviceClass(Basic.NOT_KNOWN, Generic.NOT_KNOWN, Specific.NOT_USED);
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
	}
	
	/**
	 * Gets whether the node is dead.
	 * @return
	 */
	public boolean isDead(){
		if(this.nodeStage == NodeStage.DEAD)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets whether the node is failed by controller.
	 * @return
	 */
	public boolean isFailed(){
		if(this.nodeStage == NodeStage.FAILED)
			return true;
		else
			return false;
	}
	
	/**
	 * Sets the node to be 'undead'.
	 * @return
	 */
	public void setAlive(){
		if(this.nodeStageAdvancer.isInitializationComplete()) {
			logger.debug("NODE {}: Node is now ALIVE", this.nodeId);
			this.nodeStage = NodeStage.DONE;
		}
		else {
			this.nodeStage = NodeStage.DYNAMIC;
			this.nodeStageAdvancer.advanceNodeStage(NodeStage.DONE);
		}

		// Reset the resend counter and remember when we last updated
		this.resendCount = 0;
		this.lastUpdated = Calendar.getInstance().getTime();

		// Alert anyone who wants to know...
		ZWaveEvent zEvent = new ZWaveNodeStatusEvent(this.getNodeId(), ZWaveNodeStatusEvent.State.Alive);
		controller.notifyEventListeners(zEvent);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the node name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		this.lastUpdated = Calendar.getInstance().getTime();
	}

	/**
	 * Gets the node location.
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the node location.
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
	}

	/**
	 * Get the date/time the node was last updated.
	 * @return the lastUpdated
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Gets the node stage.
	 * @return the nodeStage
	 */
	public NodeStage getNodeStage() {
		return nodeStage;
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
	public void setNodeStage(NodeStage nodeStage) {
		this.nodeStage = nodeStage;
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
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
		this.lastUpdated = Calendar.getInstance().getTime();
	}

	/**
	 * Gets the time stamp the node was last queried.
	 * @return the queryStageTimeStamp
	 */
	public Date getQueryStageTimeStamp() {
		return queryStageTimeStamp;
	}

	/**
	 * Sets the time stamp the node was last queried.
	 * @param queryStageTimeStamp the queryStageTimeStamp to set
	 */
	public void setQueryStageTimeStamp(Date queryStageTimeStamp) {
		this.queryStageTimeStamp = queryStageTimeStamp;
		this.lastUpdated = Calendar.getInstance().getTime();
	}

	/**
	 * Increments the resend counter.
	 * On three increments the node stage is set to DEAD and no
	 * more messages will be sent.
	 */
	public void incrementResendCount() {
		if (++resendCount >= 3) {
			this.nodeStage = NodeStage.DEAD;
			this.deadCount++;
			this.deadTime = Calendar.getInstance().getTime();
			this.queryStageTimeStamp = Calendar.getInstance().getTime();
			logger.debug("NODE {}: Retry count exceeded. Node is DEAD.", this.nodeId);

			if(nodeStageAdvancer.isInitializationComplete() == true) {
				ZWaveEvent zEvent = new ZWaveNodeStatusEvent(this.getNodeId(), ZWaveNodeStatusEvent.State.Dead);
				controller.notifyEventListeners(zEvent);
			}
			else {
				logger.debug("NODE {}: Initialisation incomplete, not signalling DEAD node.", this.nodeId);				
			}
		}
		this.retryCount++;
		this.lastUpdated = Calendar.getInstance().getTime();
	}

	/**
	 * Resets the resend counter and possibly resets the
	 * node stage to DONE when previous initialization was
	 * complete.
	 * Note that if the node is DEAD, then the nodeStage stays DEAD
	 */
	public void resetResendCount() {
		this.resendCount = 0;
		if (this.nodeStageAdvancer.isInitializationComplete() && this.nodeStage != NodeStage.DEAD)
			this.nodeStage = NodeStage.DONE;
		this.lastUpdated = Calendar.getInstance().getTime();
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
			
			if (commandClass instanceof ZWaveEventListener)
				this.controller.addEventListener((ZWaveEventListener)commandClass);
			
			this.lastUpdated = Calendar.getInstance().getTime();
		}
	}
	
	/**
	 * Resolves a command class for this node. First endpoint is checked. 
	 * If endpoint == 1 or (endpoint != 1 and version of the multi instance 
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
		if (commandClass == null)
			return null;
		
		ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass)supportedCommandClasses.get(CommandClass.MULTI_INSTANCE);
		
		if (multiInstanceCommandClass != null && multiInstanceCommandClass.getVersion() == 2) {
			ZWaveEndpoint endpoint = multiInstanceCommandClass.getEndpoint(endpointId);
			
			if (endpoint != null) { 
				ZWaveCommandClass result = endpoint.getCommandClass(commandClass);
				if (result != null)
					return result;
			} 
		}
		
		ZWaveCommandClass result = getCommandClass(commandClass);
		
		if (result == null)
			return result;
		
		if (multiInstanceCommandClass != null && multiInstanceCommandClass.getVersion() == 1 &&
				result.getInstances() >= endpointId)
			return result;
		
		return endpointId == 1 ? result : null;
	}
	
	/**
	 * Advances the initialization stage for this node. 
	 * Every node follows a certain path through it's 
	 * initialization phase. These stages are visited one by
	 * one to finally end up with a completely built node structure
	 * through querying the controller / node.
	 */
	public void advanceNodeStage(NodeStage targetStage) {
		// call the advanceNodeStage method on the advancer.
		this.nodeStageAdvancer.advanceNodeStage(targetStage);
	}
	
	/**
	 * Restores a node from an XML file using the @ ZWaveNodeSerializer} class.
	 * 
	 * @return true if succeeded, false otherwise.
	 */
	public boolean restoreFromConfig() {
		return this.nodeStageAdvancer.restoreFromConfig();
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
		
		if (serialMessage == null)
			return null;
		
		// no encapsulation necessary.
		if (endpointId == 1 && commandClass.getInstances() == 1 && commandClass.getEndpoint() == null)
			return serialMessage;
		
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

		if (endpointId != 1) {
			logger.warn("NODE {}:Encapsulating message, instance / endpoint {} failed, will discard message.", this.getNodeId(), endpointId);
			return null;
		}
		
		return serialMessage;
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
	 * Increments the sent packet counter
	 * This is simply used for statistical purposes to assess the health
	 * of a node.
	 */
	public void incrementSendCount() {
		sendCount++;
	}
	
	/**
	 * Gets the number of packets sent to the node
	 * @return send count
	 */
	public int getSendCount() {
		return sendCount;
	}
}
