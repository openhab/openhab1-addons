/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClassInitialization;
import org.openhab.binding.zwave.internal.commandclass.ZWaveManufacturerSpecificCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveNoOperationCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Z-Wave node class. Represents a node in the Z-Wave network.
 * 
 * @author Brian Crosby
 * @since 1.3.0
 */
public class ZWaveNode {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveNode.class);

	private final ZWaveDeviceClass deviceClass;
	private final ZWaveController controller;

	private int homeId;
	private int nodeId;
	private int version;
	
	private String name;
	private String location;
	
	private int manufacturer;
	private int deviceId;
	private int deviceType;
	
	private boolean listening;			// i.e. sleeping
	private boolean routing;
	
	private Map<CommandClass, ZWaveCommandClass> supportedCommandClasses = new HashMap<CommandClass, ZWaveCommandClass>();
	private Date lastUpdated; 
	private Date queryStageTimeStamp;
	private NodeStage nodeStage;
	
	private int resendCount = 0;
	private int queriesPending = -1;
	private boolean initializationComplete = false;
	
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
		this.nodeStage = NodeStage.NODEBUILDINFO_EMPTYNODE;
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
	 * Gets whether the node is sleeping or dead.
	 * @return
	 */
	public boolean isSleepingOrDead(){
		if(this.nodeStage == ZWaveNode.NodeStage.NODEBUILDINFO_DEAD)
			return true;
		else
			return false;
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
		if (++resendCount >= 3)
			this.nodeStage = NodeStage.NODEBUILDINFO_DEAD;
		this.lastUpdated = Calendar.getInstance().getTime();
	}

	/**
	 * Resets the resend counter and possibly resets the
	 * node stage to DONE when previous initialization was
	 * complete.
	 */
	public void resetResendCount() {
		this.resendCount = 0;
		if (this.initializationComplete)
			this.nodeStage = NodeStage.NODEBUILDINFO_DONE;
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
	 * TODO: Handle the rest of the node stages 
	 */
	public void advanceNodeStage() {
		this.setQueryStageTimeStamp(Calendar.getInstance().getTime());
		switch (this.nodeStage) {
			case NODEBUILDINFO_EMPTYNODE:
				try {
					this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_PROTOINFO);
					this.controller.identifyNode(this.nodeId);
				} catch (SerialInterfaceException e) {
					logger.error("Got error: {}, while identifying node {}", e.getLocalizedMessage(), this.nodeId);
				}
				break;
			case NODEBUILDINFO_PROTOINFO:
				if (nodeId != this.controller.getOwnNodeId())
				{
					ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass)supportedCommandClasses.get(CommandClass.NO_OPERATION);
					if (zwaveCommandClass == null)
						break;
					
					this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_PING);
					this.controller.sendData(zwaveCommandClass.getNoOperationMessage());
				} else
				{
					this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_DONE); // nothing more to do for this node.
				}
				break;
			case NODEBUILDINFO_PING:
			case NODEBUILDINFO_WAKEUP:
				this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_DETAILS);
				this.controller.requestNodeInfo(nodeId);
				break;
			case NODEBUILDINFO_DETAILS:
				// try and get the manufacturerSpecific command class.
				ZWaveManufacturerSpecificCommandClass manufacturerSpecific = (ZWaveManufacturerSpecificCommandClass)this.getCommandClass(CommandClass.MANUFACTURER_SPECIFIC);
				
				if (manufacturerSpecific != null) {
					// if this node implements the Manufacturer Specific command class, we use it to get manufacturer info.
					this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_MANSPEC01);
					this.controller.sendData(manufacturerSpecific.getManufacturerSpecificMessage());
					break;
				}
				
				logger.warn("Node {} does not support MANUFACTURER_SPECIFIC, proceeding to version node stage.", this.getNodeId());
			case NODEBUILDINFO_MANSPEC01:
				this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_VERSION); // nothing more to do for this node.
				// try and get the version command class.
				ZWaveVersionCommandClass version = (ZWaveVersionCommandClass)this.getCommandClass(CommandClass.VERSION);
				
				boolean checkVersionCalled = false;
				for (ZWaveCommandClass zwaveCommandClass : this.getCommandClasses()) {
					if (version != null && zwaveCommandClass.getMaxVersion() > 1) {
						version.checkVersion(zwaveCommandClass); // check version for this command class.
						checkVersionCalled = true;				
					} else
						zwaveCommandClass.setVersion(1);  
				}
				
				if (checkVersionCalled) // wait for another call of advanceNodeStage before continuing.
					break;
			case NODEBUILDINFO_VERSION:
				this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_INSTANCES); // nothing more to do for this node.
				// try and get the multi instance / channel command class.
				ZWaveMultiInstanceCommandClass multiInstance = (ZWaveMultiInstanceCommandClass)this.getCommandClass(CommandClass.MULTI_INSTANCE);
				
				if (multiInstance != null) {
					multiInstance.initEndpoints();
					break;
				} 
					
				logger.trace("Node {} does not support MULTI_INSTANCE, proceeding to static node stage.", this.getNodeId());
			case NODEBUILDINFO_INSTANCES:
				this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_STATIC); 
				
				if (queriesPending == -1) {
					queriesPending = 0;
					for (ZWaveCommandClass zwaveCommandClass : this.getCommandClasses()) {
						logger.trace("Inspecting command class {}", zwaveCommandClass.getCommandClass().getLabel());
						if (zwaveCommandClass instanceof ZWaveCommandClassInitialization) {
							logger.debug("Found initializable command class {}", zwaveCommandClass.getCommandClass().getLabel());
							ZWaveCommandClassInitialization zcci = (ZWaveCommandClassInitialization)zwaveCommandClass;
							int instances = zwaveCommandClass.getInstances();
							if (instances == 0)
							{
								Collection<SerialMessage> initqueries = zcci.initialize();
								for (SerialMessage serialMessage : initqueries) {
									this.controller.sendData(serialMessage);
									queriesPending++;
								}
							} else {
								for (int i = 1; i <= instances; i++) {
									Collection<SerialMessage> initqueries = zcci.initialize();
									for (SerialMessage serialMessage : initqueries) {
										this.controller.sendData(this.encapsulate(serialMessage, zwaveCommandClass, i));
										queriesPending++;
									}
								}
							}
						} else if (zwaveCommandClass instanceof ZWaveMultiInstanceCommandClass) {
							ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass)zwaveCommandClass;
							for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
								for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
									logger.trace("Inspecting command class {} for endpoint {}", endpointCommandClass.getCommandClass().getLabel(), endpoint.getEndpointId());
									if (endpointCommandClass instanceof ZWaveCommandClassInitialization) {
										logger.debug("Found initializable command class {}", endpointCommandClass.getCommandClass().getLabel());
										ZWaveCommandClassInitialization zcci2 = (ZWaveCommandClassInitialization)endpointCommandClass;
										Collection<SerialMessage> initqueries = zcci2.initialize();
										for (SerialMessage serialMessage : initqueries) {
											this.controller.sendData(this.encapsulate(serialMessage, endpointCommandClass, endpoint.getEndpointId()));
											queriesPending++;
										}
									}
								}
							}
						}
					}
				}
				if (queriesPending-- > 0) // there is still something to be initialized.
					break;
				
				logger.trace("Done getting static values, proceeding to done node stage.", this.getNodeId());
			case NODEBUILDINFO_STATIC:
				this.setNodeStage(ZWaveNode.NodeStage.NODEBUILDINFO_DONE); // nothing more to do for this node.
				initializationComplete = true;
				
				if (this.isListening())
					return;
				
				ZWaveWakeUpCommandClass wakeup = (ZWaveWakeUpCommandClass)this.getCommandClass(CommandClass.WAKE_UP);
				
				if (wakeup == null)
					return;

				logger.debug("Node {} is a battery operated device. Tell it to go to sleep.", this.getNodeId());
				this.controller.sendData(wakeup.getNoMoreInformationMessage());
				break;
			case NODEBUILDINFO_DONE:
			case NODEBUILDINFO_DEAD:
				break;
			default:
				logger.error("Unknown node state {} encountered on Node {}", this.nodeStage.getLabel(), this.getNodeId());
		}
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
			logger.debug("Encapsulating message for node {}, instance / endpoint {}", this.getNodeId(), endpointId);
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
			logger.warn("Encapsulating message for node {}, instance / endpoint {} failed, will discard message.", this.getNodeId(), endpointId);
			return null;
		}
		
		return serialMessage;
	}
	
	
	/**
	 * Node Stage Enumeration. Represents the state the node is in.
	 * 
	 * @author Brian Crosby
	 * @since 1.3.0
	 */
	public enum NodeStage {
		NODEBUILDINFO_EMPTYNODE(0, "Empty New Node"),
		NODEBUILDINFO_PROTOINFO(1, "Protocol Information"),
		NODEBUILDINFO_PING(2, "Ping Node"),
		NODEBUILDINFO_WAKEUP(3, "Wake Up"),
		NODEBUILDINFO_DETAILS(4, "Node Information"),
		NODEBUILDINFO_MANSPEC01(5, "Manufacture Name and Product Identification"),
		NODEBUILDINFO_MANSPEC02(6, "Manufacture Name and Product Identification"),
		NODEBUILDINFO_VERSION(7, "Node Version"),
		NODEBUILDINFO_INSTANCES(8, "Command Class Instances"),
		NODEBUILDINFO_STATIC(9, "Static Information"),
		NODEBUILDINFO_PROBE01(10, "Ping Node"),
		NODEBUILDINFO_ASSOCIATIONS(11, "Association Information"),
		NODEBUILDINFO_NEIGHBORS(12, "Node Neighbor Information"),
		NODEBUILDINFO_SESSION(13, "Infrequently Changed Information"),
		NODEBUILDINFO_DYNAMIC(14, "Frequently Changed Information"),
		NODEBUILDINFO_CONFIG(15, "Parameter Information"),
		NODEBUILDINFO_DONE(16, "Node Complete"),
		NODEBUILDINFO_INIT(17, "Node Not Started"),
		NODEBUILDINFO_DEAD(18, "Node Dead or Sleeping");
		
		private int stage;
		private String label;
		
		private NodeStage (int s, String l) {
			stage = s;
			label = l;
		}
		
		/**
		 * Get the stage protocol number.
		 * @return number
		 */
		public int getStage() {
			return this.stage;
		}
		
		/**
		 * Get the stage label
		 * @return label
		 */
		public String getLabel() {
			return this.label;
		}
	}
	
}
