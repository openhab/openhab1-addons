/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.zwave.internal.ZWaveNetworkMonitor;
import org.openhab.binding.zwave.internal.protocol.ConfigurationParameter;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceType;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass.ZWaveAssociationEvent;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveBatteryCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveConfigurationCommandClass.ZWaveConfigurationParameterEvent;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveConfigurationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Z Wave configuration class Interfaces between the REST services using the
 * OpenHABConfigurationService interface. It uses the ZWave product database to
 * configure zwave devices.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class ZWaveConfiguration implements OpenHABConfigurationService, ZWaveEventListener {
	private static final Logger logger = LoggerFactory.getLogger(ZWaveConfiguration.class);

	private ZWaveController zController = null;
	private ZWaveNetworkMonitor networkMonitor = null;
	
	private boolean inclusion = false;
	private boolean exclusion = false;

	private Timer timer = new Timer();

	private TimerTask timerTask = null;
	
	private PendingConfiguration PendingCfg = new PendingConfiguration();
	
	public ZWaveConfiguration() {
	}

	/**
	 * Constructor for the configuration class. Sets the zwave controller
	 * which is required in order to allow the class to retrieve the configuration.
	 * @param controller The zWave controller
	 */
	public ZWaveConfiguration(ZWaveController controller, ZWaveNetworkMonitor monitor) {
		this.zController = controller;
		this.networkMonitor = monitor;

		// Register the service
		FrameworkUtil.getBundle(getClass()).getBundleContext()
				.registerService(OpenHABConfigurationService.class.getName(), this, null);
	}

	@Override
	public String getBundleName() {
		return "zwave";
	}

	@Override
	public String getVersion() {
		return FrameworkUtil.getBundle(getClass()).getBundleContext().getBundle().getVersion().toString();
	}

	@Override
	public List<OpenHABConfigurationRecord> getConfiguration(String domain) {
		// We only deal with top level domains here!
		if (domain.endsWith("/") == false) {
			logger.error("Malformed domain request in getConfiguration '{}'", domain);
			return null;
		}

		List<OpenHABConfigurationRecord> records = new ArrayList<OpenHABConfigurationRecord>();
		OpenHABConfigurationRecord record;
//		ZWaveNode node;

		if (domain.equals("status/")) {
			// Return the z-wave status information

			return null;
		}
		if (domain.startsWith("products/")) {
			ZWaveProductDatabase database = new ZWaveProductDatabase();

			String[] splitDomain = domain.split("/");

			switch (splitDomain.length) {
			case 1:
				// Just list the manufacturers
				for (ZWaveDbManufacturer manufacturer : database.GetManufacturers()) {
					record = new OpenHABConfigurationRecord(domain + manufacturer.Id.toString() + "/", manufacturer.Name);

					records.add(record);
				}
				break;
			case 2:
				// Get products list
				if (database.FindManufacturer(Integer.parseInt(splitDomain[1])) == false)
					break;

				record = new OpenHABConfigurationRecord(domain, "ManufacturerID", "Manufacturer ID", true);
				record.value = Integer.toHexString(database.getManufacturerId());
				records.add(record);
				
				for (ZWaveDbProduct product : database.GetProducts()) {
					record = new OpenHABConfigurationRecord(domain + product.Reference.get(0).Type + "/" + product.Reference.get(0).Id + "/", product.Model);
					record.value = database.getLabel(product.Label);
					records.add(record);
				}
				break;
			case 4:
				// Get product
				if (database.FindProduct(Integer.parseInt(splitDomain[1]), Integer.parseInt(splitDomain[2]), Integer.parseInt(splitDomain[3])) == false)
					break;

				if(database.doesProductImplementCommandClass(ZWaveCommandClass.CommandClass.CONFIGURATION.getKey()) == true) {
					record = new OpenHABConfigurationRecord(domain + "parameters/", "Configuration Parameters");
					records.add(record);
				}

				if(database.doesProductImplementCommandClass(ZWaveCommandClass.CommandClass.ASSOCIATION.getKey()) == true) {
					record = new OpenHABConfigurationRecord(domain + "associations/", "Association Groups");
					records.add(record);
				}

				record = new OpenHABConfigurationRecord(domain + "classes/", "Command Classes");
				records.add(record);
				break;
			case 5:
				// Get product
				if (database.FindProduct(Integer.parseInt(splitDomain[1]), Integer.parseInt(splitDomain[2]), Integer.parseInt(splitDomain[3])) == false)
					break;

				if (splitDomain[4].equals("parameters")) {
					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
					// Loop through the parameters and add to the records...
					for (ZWaveDbConfigurationParameter parameter : configList) {
						record = new OpenHABConfigurationRecord(domain, "configuration" + parameter.Index,
								database.getLabel(parameter.Label), true);

						if (parameter != null)
							record.value = parameter.Default;

						// Add the data type
						try {
							record.type = OpenHABConfigurationRecord.TYPE.valueOf(parameter.Type.toUpperCase());
						} catch(IllegalArgumentException e) {
							logger.error("Error with parameter type for {} - Set {} - assuming LONG", parameter.Label.toString(), parameter.Type);
							record.type = OpenHABConfigurationRecord.TYPE.LONG;							
						}

						if(parameter.Item != null) {
							for (ZWaveDbConfigurationListItem item : parameter.Item)
								record.addValue(Integer.toString(item.Value), database.getLabel(item.Label));
						}

						// Add the description
						record.description = database.getLabel(parameter.Help);

						records.add(record);
					}
				}
				if (splitDomain[4].equals("associations")) {
					List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();

					if (groupList != null) {
						// Loop through the associations and add to the
						// records...
						for (ZWaveDbAssociationGroup group : groupList) {
							record = new OpenHABConfigurationRecord(domain, "association" + group.Index,
									database.getLabel(group.Label), true);

							// Add the description
							record.description = database.getLabel(group.Help);

							records.add(record);
						}
					}
				}
				if (splitDomain[4].equals("classes")) {
					List<ZWaveDbCommandClass> classList = database.getProductCommandClasses();

					if (classList != null) {
						// Loop through the command classes and add to the
						// records...
						for (ZWaveDbCommandClass iClass : classList) {
							// Make sure the command class exists!
							if(ZWaveCommandClass.CommandClass.getCommandClass(iClass.Id) == null)
								continue;
							record = new OpenHABConfigurationRecord(domain, "class" + iClass.Id,
									ZWaveCommandClass.CommandClass.getCommandClass(iClass.Id).getLabel(), true);
							if(ZWaveCommandClass.CommandClass.getCommandClass(iClass.Id).getCommandClassClass() == null) {
								record.state = OpenHABConfigurationRecord.STATE.WARNING;
							}
							records.add(record);
						}
					}					
				}
				break;
			}
			return records;
		}

		// All domains after here must have an initialised ZWave network
		if (zController == null) {
			logger.error("Controller not initialised in call to getConfiguration");
			return null;
		}
		
		if (domain.equals("nodes/")) {
			ZWaveProductDatabase database = new ZWaveProductDatabase();
			// Return the list of nodes
			for(ZWaveNode node : zController.getNodes()) {
				if (node.getName() == null || node.getName().isEmpty()) {
					record = new OpenHABConfigurationRecord("nodes/" + "node" + node.getNodeId() + "/", "Node " + node.getNodeId());
				}
				else {
					record = new OpenHABConfigurationRecord("nodes/" + "node" + node.getNodeId() + "/", node.getName());
				}
				
				// If we can't find the product, then try and find just the
				// manufacturer
				if (node.getManufacturer() == Integer.MAX_VALUE) {
				} else if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
					if (database.FindManufacturer(node.getManufacturer()) == false) {
						record.value = "Manufacturer:" + node.getManufacturer() + " [ID:"
								+ Integer.toHexString(node.getDeviceId()) + ",Type:"
								+ Integer.toHexString(node.getDeviceType()) + "]";
					} else {
						record.value = database.getManufacturerName() + " [ID:"
								+ Integer.toHexString(node.getDeviceId()) + ",Type:"
								+ Integer.toHexString(node.getDeviceType()) + "]";
					}
					logger.debug("NODE {}: No database entry: {}", node.getNodeId(), record.value);
				} else {
					if (node.getLocation() == null || node.getLocation().isEmpty())
						record.value = database.getProductName();
					else
						record.value = database.getProductName() + ": " + node.getLocation();
				}

				// Set the state
				boolean canDelete = false;
				switch(node.getNodeStage()) {
				case DEAD:
					record.state = OpenHABConfigurationRecord.STATE.ERROR;
					canDelete = true;
					break;
				case DONE:
					Date lastDead = node.getDeadTime();
					Long timeSinceLastDead = Long.MAX_VALUE;
					if(lastDead != null) {
						timeSinceLastDead = System.currentTimeMillis() - lastDead.getTime();
					}
					if(node.getDeadCount() > 0 && timeSinceLastDead < 86400000)
						record.state = OpenHABConfigurationRecord.STATE.WARNING;
					else if(node.getSendCount() > 0 && (node.getRetryCount() * 100 / node.getSendCount()) > 5)
						record.state = OpenHABConfigurationRecord.STATE.WARNING;
					else
					record.state = OpenHABConfigurationRecord.STATE.OK;
					break;
				default:
					record.state = OpenHABConfigurationRecord.STATE.INITIALIZING;
					canDelete = true;
					break;
				}

				// Add the action buttons
				record.addAction("Heal", "Heal Node");
				
				// Add the delete button if the node is not "operational"
				if(canDelete) {
					record.addAction("Delete", "Delete Node");
				}
				records.add(record);

				// This needs to be removed - temporary only until it's added to initialisation code.
				record.addAction("Version", "Version Info");
			}
			return records;
		}
		if (domain.startsWith("nodes/node")) {
			String nodeNumber = domain.substring(10);
			int next = nodeNumber.indexOf('/');
			String arg = null;
			if (next != -1) {
				arg = nodeNumber.substring(next + 1);
				nodeNumber = nodeNumber.substring(0, next);
			}
			int nodeId = Integer.parseInt(nodeNumber);

			// Return the detailed configuration for this node
			ZWaveNode node = zController.getNode(nodeId);
			if (node == null)
				return null;

			// Open the product database
			ZWaveProductDatabase database = new ZWaveProductDatabase();

			// Process the request
			if (arg.equals("")) {
				record = new OpenHABConfigurationRecord(domain, "Name", "Name", false);
				record.value = node.getName();
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Location", "Location", false);
				record.value = node.getLocation();
				records.add(record);

				if (database.FindManufacturer(node.getManufacturer()) == false) {
					record = new OpenHABConfigurationRecord(domain, "ManufacturerID", "Manufacturer ID", true);
					record.value = Integer.toHexString(node.getManufacturer());
					records.add(record);
				} else {
					record = new OpenHABConfigurationRecord(domain, "Manufacturer", "Manufacturer", true);
					record.value = database.getManufacturerName();
					records.add(record);
				}

				if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
					record = new OpenHABConfigurationRecord(domain, "DeviceId", "Device ID", true);
					record.value = Integer.toHexString(node.getDeviceId());
					records.add(record);

					record = new OpenHABConfigurationRecord(domain, "DeviceType", "Device Type", true);
					record.value = Integer.toHexString(node.getDeviceType());
					records.add(record);
					
					record = new OpenHABConfigurationRecord(domain, "Version", "Version", true);
					record.value = Integer.toString(node.getVersion());
					records.add(record);
				} else {
					record = new OpenHABConfigurationRecord(domain, "Product", "Product", true);
					record.value = database.getProductName();
					records.add(record);

					// Add links to configuration if the node supports the various command classes
					if(database.doesProductImplementCommandClass(ZWaveCommandClass.CommandClass.CONFIGURATION.getKey()) == true) {
						record = new OpenHABConfigurationRecord(domain + "parameters/", "Configuration Parameters");
						record.addAction("Refresh", "Refresh");
						records.add(record);
					}

					if(database.doesProductImplementCommandClass(ZWaveCommandClass.CommandClass.ASSOCIATION.getKey()) == true) {
						record = new OpenHABConfigurationRecord(domain + "associations/", "Association Groups");
						record.addAction("Refresh", "Refresh");
						records.add(record);
					}

					if(database.doesProductImplementCommandClass(ZWaveCommandClass.CommandClass.WAKE_UP.getKey()) == true) {
						record = new OpenHABConfigurationRecord(domain + "wakeup/", "Wakeup Period");
						record.addAction("Refresh", "Refresh");
						records.add(record);
					}
				}

				// Is this a controller
				if(nodeId == zController.getOwnNodeId()) {
					record = new OpenHABConfigurationRecord(domain + "controller/", "Controller");
					records.add(record);
				}

				record = new OpenHABConfigurationRecord(domain + "neighbors/", "Neighbors");
				record.addAction("Refresh", "Refresh");
				records.add(record);
				
				record = new OpenHABConfigurationRecord(domain + "status/", "Status");
				records.add(record);
			} else if (arg.equals("status/")) {
				record = new OpenHABConfigurationRecord(domain, "LastUpdated", "Last Updated", true);
				record.value = node.getLastUpdated().toString();
				records.add(record);
				
				if(networkMonitor != null) {
					record = new OpenHABConfigurationRecord(domain, "LastHeal", "Heal Status", true);
					record.value = networkMonitor.getNodeState(nodeId);
					records.add(record);
				}

				record = new OpenHABConfigurationRecord(domain, "NodeStage", "Node Stage", true);
				record.value = node.getNodeStage().getLabel() + " @ " + node.getQueryStageTimeStamp().toString();
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Listening", "Listening", true);
				record.value = Boolean.toString(node.isListening());
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Routing", "Routing", true);
				record.value = Boolean.toString(node.isRouting());
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Packets", "Packet Statistics", true);
				record.value = node.getRetryCount() + " / " + node.getSendCount();
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Dead", "Dead", true);
				if(node.getDeadCount() == 0) {
					record.value = Boolean.toString(node.isDead());
				}
				else {
					record.value = Boolean.toString(node.isDead()) + " [" + node.getDeadCount() + " previous - last @ " + node.getDeadTime().toString() + "]";
				}
				records.add(record);
				
				record = new OpenHABConfigurationRecord(domain, "Power", "Power", true);
				ZWaveBatteryCommandClass batteryCommandClass = (ZWaveBatteryCommandClass) node
						.getCommandClass(CommandClass.BATTERY);
				if(batteryCommandClass != null) {
					record.value = "Battery";					
				}
				else {
					record.value = "Mains";
				}
				records.add(record);

				ZWaveVersionCommandClass versionCommandClass = (ZWaveVersionCommandClass) node
						.getCommandClass(CommandClass.VERSION);

				if (versionCommandClass != null) {
					record = new OpenHABConfigurationRecord(domain, "LibType", "Library Type", true);
					if(versionCommandClass.getLibraryType() == null)
						record.value = "Unknown";
					else
						record.value = versionCommandClass.getLibraryType().getLabel();
					records.add(record);

					record = new OpenHABConfigurationRecord(domain, "ProtocolVersion", "Protocol Version", true);
					if(versionCommandClass.getProtocolVersion() == null)
						record.value = "Unknown";
					else
						record.value = Double.toString(versionCommandClass.getProtocolVersion());
					records.add(record);

					record = new OpenHABConfigurationRecord(domain, "AppVersion", "Application Version", true);
					if(versionCommandClass.getApplicationVersion() == null)
						record.value = "Unknown";
					else
						record.value = Double.toString(versionCommandClass.getApplicationVersion());
					records.add(record);
				}
			} else if (arg.equals("parameters/")) {
				if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {
					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
					if(configList == null)
						return records;

					// Get the configuration command class for this node
					ZWaveConfigurationCommandClass configurationCommandClass = (ZWaveConfigurationCommandClass) node
							.getCommandClass(CommandClass.CONFIGURATION);

					if (configurationCommandClass == null) {
						logger.error("NODE {}: Error getting configurationCommandClass", nodeId);
						return null;
					}

					// Loop through the parameters and add to the records...
					for (ZWaveDbConfigurationParameter parameter : configList) {
						record = new OpenHABConfigurationRecord(domain, "configuration" + parameter.Index,
								parameter.Index + ": " + database.getLabel(parameter.Label), false);

						ConfigurationParameter configurationParameter = configurationCommandClass
								.getParameter(parameter.Index);

						// Only provide a value if it's stored in the node
						// This is the only way we can be sure of its real value
						if (configurationParameter != null)
							record.value = Integer.toString(configurationParameter.getValue());

						// If the value is in our PENDING list, then use that instead
						Integer pendingValue = PendingCfg.Get(ZWaveCommandClass.CommandClass.CONFIGURATION.getKey(), nodeId, parameter.Index);
						if(pendingValue != null) {
							record.value = Integer.toString(pendingValue);
							record.state = OpenHABConfigurationRecord.STATE.PENDING;
						}

						try {
							record.type = OpenHABConfigurationRecord.TYPE.valueOf(parameter.Type.toUpperCase());
						} catch(IllegalArgumentException e) {
							record.type = OpenHABConfigurationRecord.TYPE.LONG;							
						}

						if(parameter.Item != null) {
							for (ZWaveDbConfigurationListItem item : parameter.Item)
								record.addValue(Integer.toString(item.Value), database.getLabel(item.Label));
						}
						
						// Add any limits
						record.minimum = parameter.Minimum;
						record.maximum = parameter.Maximum;

						// Add the description
						record.description = database.getLabel(parameter.Help);

						records.add(record);
					}
				}
			} else if (arg.equals("associations/")) {
				if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {
					List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();

					if (groupList != null) {
						// Loop through the associations and add all groups to the
						// records...
						for (ZWaveDbAssociationGroup group : groupList) {
							// TODO: Controller reporting associations are set to read only
							record = new OpenHABConfigurationRecord(domain, "association" + group.Index + "/",
									database.getLabel(group.Label), group.SetToController);

							// Add the description
							record.description = database.getLabel(group.Help);
							
							// For the 'value', describe how many devices are set and the maximum allowed
							ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) node
									.getCommandClass(CommandClass.ASSOCIATION);
							int memberCnt = 0;
							List<Integer> members = associationCommandClass.getGroupMembers(group.Index);
							if(members != null)
								memberCnt = members.size();
							record.value = memberCnt + " of " + group.Maximum + " group members";

							// Add the action for refresh
							record.addAction("Refresh", "Refresh");

							records.add(record);
						}
					}
				}
			} else if (arg.startsWith("associations/association")) {
				if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {

					String groupString = arg.substring(24);
					int nextDelimiter = groupString.indexOf('/');
					// String arg = null;
					if (nextDelimiter != -1) {
						// arg = nodeNumber.substring(nextDelimiter + 1);
						groupString = groupString.substring(0, nextDelimiter);
					}
					int groupId = Integer.parseInt(groupString);

					// Get the requested group so we have access to the
					// attributes
					List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();
					if (groupList == null)
						return null;
					ZWaveDbAssociationGroup group = null;
					for (int cnt = 0; cnt < groupList.size(); cnt++) {
						if (groupList.get(cnt).Index == groupId) {
							group = groupList.get(cnt);
							break;
						}
					}

					// Return if the group wasn't found
					if (group == null)
						return null;

					// Get the group members
					ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) node
							.getCommandClass(CommandClass.ASSOCIATION);

					List<Integer> members = associationCommandClass.getGroupMembers(groupId);
					for(ZWaveNode nodeList : zController.getNodes()) {
						// Don't allow an association with itself
						if(nodeList.getNodeId() == node.getNodeId())
							continue;
						
						// Add the member
						if (nodeList.getName() == null || nodeList.getName().isEmpty())
							record = new OpenHABConfigurationRecord(domain, "node" + nodeList.getNodeId(), "Node " + nodeList.getNodeId(), false);
						else
							record = new OpenHABConfigurationRecord(domain, "node" + nodeList.getNodeId(), nodeList.getName(), false);

						record.type = OpenHABConfigurationRecord.TYPE.LIST;
						record.addValue("true", "Member");
						record.addValue("false", "Non-Member");

						if (members != null && members.contains(nodeList.getNodeId())) {
							record.value = "true";
						} else {
							record.value = "false";
						}

						// If the value is in our PENDING list, then use that instead
						Integer pendingValue = PendingCfg.Get(ZWaveCommandClass.CommandClass.ASSOCIATION.getKey(), nodeId, groupId, nodeList.getNodeId());
						if(pendingValue != null) {
							if(pendingValue == 1)
								record.value = "true";
							else
								record.value = "false";
							record.state = OpenHABConfigurationRecord.STATE.PENDING;
						}

						records.add(record);
					}
				}
			} else if (arg.equals("wakeup/")) {
				ZWaveWakeUpCommandClass wakeupCommandClass = (ZWaveWakeUpCommandClass) node
						.getCommandClass(CommandClass.WAKE_UP);

				if(wakeupCommandClass == null) {
					logger.error("NODE {}: Error getting wakeupCommandClass", nodeId);
					return null;
				}

				// Display the wakeup parameters.
				// Note that only the interval is writable.
				record = new OpenHABConfigurationRecord(domain, "Interval", "Wakeup Interval", false);
				record.minimum = wakeupCommandClass.getMinInterval();
				record.maximum = wakeupCommandClass.getMaxInterval();
				record.value = Integer.toString(wakeupCommandClass.getInterval());
				// If the value is in our PENDING list, then use that instead
				Integer pendingValue = PendingCfg.Get(ZWaveCommandClass.CommandClass.WAKE_UP.getKey(), nodeId);
				if(pendingValue != null) {
					record.value = Integer.toString(pendingValue);
					record.state = OpenHABConfigurationRecord.STATE.PENDING;
				}
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Minimum", "Minimum Interval", true);
				record.value = Integer.toString(wakeupCommandClass.getMinInterval());
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Maximum", "Maximum Interval", true);
				record.value = Integer.toString(wakeupCommandClass.getMaxInterval());
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Default", "Default Interval", true);
				record.value = Integer.toString(wakeupCommandClass.getDefaultInterval());
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "Step", "Interval Step", true);
				record.value = Integer.toString(wakeupCommandClass.getIntervalStep());
				records.add(record);
			} else if (arg.equals("neighbors/")) {
				// Check that we have the neighbor list for this node
				if(node.getNeighbors() == null)
					return null;

				for (Integer neighbor : node.getNeighbors()) {
					ZWaveNode nodeNeighbor = zController.getNode(neighbor);
					String neighborName;
					if (nodeNeighbor == null)
						neighborName = "Node " + neighbor + " (UNKNOWN)";
					else if (nodeNeighbor.getName() == null || nodeNeighbor.getName().isEmpty())
						neighborName = "Node " + neighbor;
					else
						neighborName = nodeNeighbor.getName();

					// Create the record
					record = new OpenHABConfigurationRecord(domain, "node" + neighbor, neighborName, false);
					record.readonly = true;

					// If this node isn't known, mark it as an error
					if(nodeNeighbor == null)
						record.state = OpenHABConfigurationRecord.STATE.ERROR;

					records.add(record);
				}
			} else if (arg.equals("controller/")) {
				// Create the record
				record = new OpenHABConfigurationRecord(domain, "Type", "Controller Type", true);
				record.type = OpenHABConfigurationRecord.TYPE.LIST;
				record.value = zController.getControllerType().getLabel();
				record.addValue(ZWaveDeviceType.PRIMARY.toString(), ZWaveDeviceType.PRIMARY.getLabel());
				record.addValue(ZWaveDeviceType.SECONDARY.toString(), ZWaveDeviceType.SECONDARY.getLabel());
				record.addValue(ZWaveDeviceType.SUC.toString(), ZWaveDeviceType.SUC.getLabel());

				// Set the read-only if this isn't a controller!
				switch(zController.getControllerType()) {
				case SUC:
				case PRIMARY:
				case SECONDARY:
					record.readonly = true;
					break;
				default:
					record.readonly = true;
					break;
				}
				records.add(record);

				record = new OpenHABConfigurationRecord(domain, "APIVersion", "API Version", true);
				record.value = zController.getSerialAPIVersion();
				records.add(record);
				
				record = new OpenHABConfigurationRecord(domain, "ZWaveVersion", "ZWave Version", true);
				record.value = zController.getZWaveVersion();
				records.add(record);
			}
			
			return records;
		}

		return null;
	}

	@Override
	public void setConfiguration(String domain, List<OpenHABConfigurationRecord> records) {

	}

	@Override
	public String getCommonName() {
		return "ZWave";
	}

	@Override
	public String getDescription() {
		return "Provides interface to Z-Wave network";
	}

	@Override
	public void doAction(String domain, String action) {
		logger.trace("doAction domain '{}' to '{}'", domain, action);

		// If the controller isn't ready, then ignore any requests
		if (zController.isConnected() == false) {
			logger.debug("Controller not ready - Ignoring request to '{}'", domain);
			return;
		}

		String[] splitDomain = domain.split("/");

		// There must be at least 2 components to the domain
		if (splitDomain.length < 2) {
			logger.error("Error malformed domain in doAction '{}'", domain);
			return;
		}

		if (splitDomain[0].equals("binding")) {
			if (splitDomain[1].equals("network")) {
				if (action.equals("Heal")) {
					if (networkMonitor != null)
						networkMonitor.rescheduleHeal();
				}
				if (inclusion == false && exclusion == false) {
					if (action.equals("Include")) {
						inclusion = true;
						zController.requestAddNodesStart();
						setInclusionTimer();
					}
					if (action.equals("Exclude")) {
						exclusion = true;
						zController.requestRemoveNodesStart();
						setInclusionTimer();
					}
				}
				else {
					logger.debug("Exclusion/Inclusion already in progress.");
				}
			}
		} else if (splitDomain[0].equals("nodes")) {
			int nodeId = Integer.parseInt(splitDomain[1].substring(4));

			// Get the node - if it exists
			ZWaveNode node = zController.getNode(nodeId);
			if (node == null) {
				logger.error("NODE {}: Error finding node in doAction", nodeId);
				return;
			}

			if (splitDomain.length == 2) {
				if (action.equals("Heal")) {
					logger.debug("NODE {}: Heal node", nodeId);

					if (networkMonitor != null)
						networkMonitor.healNode(nodeId);
				}

				if (action.equals("Save")) {
					logger.debug("NODE {}: Saving node", nodeId);

					// Write the node to disk
					ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
					nodeSerializer.SerializeNode(node);
				}

				if (action.equals("Delete")) {
					logger.debug("NODE {}: Delete node", nodeId);
					this.zController.requestRemoveFailedNode(nodeId);

					// Delete the XML file.
					// TODO: This should be possibly be done after registering
					// an event handler
					// Then we can delete this after the controller confirms the
					// removal.
					ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
					nodeSerializer.DeleteNode(nodeId);
				}

				// This is temporary
				// It should be in the startup code, but that needs refactoring
				if (action.equals("Version")) {
					logger.debug("NODE {}: Get node version", nodeId);
					ZWaveVersionCommandClass versionCommandClass = (ZWaveVersionCommandClass) node
							.getCommandClass(CommandClass.VERSION);

					if (versionCommandClass == null) {
						logger.error("NODE {}: Error getting versionCommandClass in doAction", nodeId);
						return;
					}

					// Request the version report for this node
					this.zController.sendData(versionCommandClass.getVersionMessage());
				}

				// Return here as afterwards we assume there are more elements
				// in the domain array
				return;
			}

			if (splitDomain[2].equals("parameters")) {
				ZWaveConfigurationCommandClass configurationCommandClass = (ZWaveConfigurationCommandClass) node
						.getCommandClass(CommandClass.CONFIGURATION);

				if (configurationCommandClass == null) {
					logger.error("NODE {}: Error getting configurationCommandClass in doAction", nodeId);
					return;
				}
				if (action.equals("Refresh")) {
					logger.debug("NODE {}: Refresh parameters", nodeId);

					ZWaveProductDatabase database = new ZWaveProductDatabase();
					if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
						logger.error("NODE {}: Error getting parameters - no database found", nodeId);
						return;
					}

					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();

					// Request all parameters for this node
					for (ZWaveDbConfigurationParameter parameter : configList)
						this.zController.sendData(configurationCommandClass.getConfigMessage(parameter.Index));
				}
			}

			if (splitDomain[2].equals("wakeup")) {
				if (action.equals("Refresh")) {
					logger.debug("NODE {}: Refresh wakeup capabilities", nodeId);

					ZWaveWakeUpCommandClass wakeupCommandClass = (ZWaveWakeUpCommandClass) node
							.getCommandClass(CommandClass.WAKE_UP);

					if (wakeupCommandClass == null) {
						logger.error("NODE {}: Error getting wakeupCommandClass in doAction", nodeId);
						return;
					}

					// Request the wakeup interval for this node
					this.zController.sendData(wakeupCommandClass.getIntervalMessage());

					// Request the wakeup parameters for this node
					this.zController.sendData(wakeupCommandClass.getIntervalCapabilitiesMessage());
				}
			}

			if (splitDomain[2].equals("neighbors")) {
				if (action.equals("Refresh")) {
					// this.zController.requestNodeNeighborUpdate(nodeId);
					this.zController.requestNodeRoutingInfo(nodeId);// .requestNodeNeighborUpdate(nodeId);
				}
			}

			if (splitDomain[2].equals("associations")) {
				if (action.equals("Refresh")) {
					logger.debug("NODE {}: Refresh associations", nodeId);

					ZWaveProductDatabase database = new ZWaveProductDatabase();
					if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
						logger.error("NODE {}: Error in doAction - no database found", nodeId);
						return;
					}

					ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) node
							.getCommandClass(CommandClass.ASSOCIATION);
					if (associationCommandClass == null) {
						logger.error("NODE {}: Error getting associationCommandClass in doAction", nodeId);
						return;
					}

					if (splitDomain.length == 3) {
						// Request all groups for this node
						List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();

						for (ZWaveDbAssociationGroup group : groupList)
							this.zController.sendData(associationCommandClass.getAssociationMessage(group.Index));
					} else if (splitDomain.length == 4) {
						// Request a single group
						int nodeArg = Integer.parseInt(splitDomain[3].substring(11));
						this.zController.sendData(associationCommandClass.getAssociationMessage(nodeArg));
					}
				}
			}
		}
	}

	@Override
	public void doSet(String domain, String value) {
		logger.debug("doSet domain '{}' to '{}'", domain, value);
		String[] splitDomain = domain.split("/");

		// If the controller isn't ready, then ignore any requests
		if(zController.isConnected() == false) {
			logger.debug("Controller not ready - Ignoring request to '{}'", domain);
			return;
		}

		// There must be at least 2 components to the domain
		if (splitDomain.length < 2) {
			logger.error("Error malformed domain in doSet '{}'", domain);
			return;
		}

		if (splitDomain[0].equals("nodes")) {
			int nodeId = Integer.parseInt(splitDomain[1].substring(4));

			ZWaveNode node = zController.getNode(nodeId);
			if (node == null) {
				logger.error("Error finding node in doSet '{}'", domain);
				return;
			}

			ZWaveProductDatabase database = new ZWaveProductDatabase();
			if (database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
				logger.error("NODE {}: Error in doSet - no database found", nodeId);
				return;
			}

			if (splitDomain.length == 3) {
				if (splitDomain[2].equals("Name"))
					node.setName(value);
				if (splitDomain[2].equals("Location"))
					node.setLocation(value);
			} else if (splitDomain.length == 4) {
				if (splitDomain[2].equals("parameters")) {
					ZWaveConfigurationCommandClass configurationCommandClass = (ZWaveConfigurationCommandClass) node
							.getCommandClass(CommandClass.CONFIGURATION);

					if (configurationCommandClass == null) {
						logger.error("NODE {}: Error getting configurationCommandClass in doSet", nodeId);
						return;
					}

					int paramIndex = Integer.parseInt(splitDomain[3].substring(13));
					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();

					// Get the size
					int size = 1;
					for (ZWaveDbConfigurationParameter parameter : configList) {
						if (parameter.Index == paramIndex) {
							size = parameter.Size;
							break;
						}
					}

					logger.debug("Set parameter index '{}' to '{}'", paramIndex, value);

					PendingCfg.Add(ZWaveCommandClass.CommandClass.CONFIGURATION.getKey(), nodeId, paramIndex, Integer.valueOf(value));

					ConfigurationParameter configurationParameter = new ConfigurationParameter(paramIndex,
							Integer.valueOf(value), size);
					// Set the parameter
					this.zController.sendData(configurationCommandClass.setConfigMessage(configurationParameter));
					// And request a read-back
					this.zController.sendData(configurationCommandClass.getConfigMessage(paramIndex));
				}
				if (splitDomain[2].equals("wakeup")) {
					ZWaveWakeUpCommandClass wakeupCommandClass = (ZWaveWakeUpCommandClass) node
							.getCommandClass(CommandClass.WAKE_UP);

					if (wakeupCommandClass == null) {
						logger.error("NODE {}: Error getting wakeupCommandClass in doSet", nodeId);
						return;
					}
					
					logger.debug("NODE {}: Set wakeup interval to '{}'", nodeId, value);

					// Add this as a pending transaction
					PendingCfg.Add(ZWaveCommandClass.CommandClass.WAKE_UP.getKey(), node.getNodeId(), Integer.parseInt(value));

					// Set the wake-up interval
					this.zController.sendData(wakeupCommandClass.setInterval(Integer.parseInt(value)));
					// And request a read-back
					this.zController.sendData(wakeupCommandClass.getIntervalMessage());
				}
				if (splitDomain[2].equals("controller")) {
					if(splitDomain[3].equals("Type")) {
						ZWaveDeviceType type = ZWaveDeviceType.fromString(value);
						logger.error("NODE {}: Setting controller type to {}", nodeId, type.toString());
//						ZW_EnableSUC and ZW_SetSUCNodeID
					}

				}
			} else if (splitDomain.length == 5) {
				if (splitDomain[2].equals("associations")) {
					ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) node
							.getCommandClass(CommandClass.ASSOCIATION);
					if (associationCommandClass == null) {
						logger.error("NODE {}: Error getting associationCommandClass in doSet", nodeId);
						return;
					}
					int assocId = Integer.parseInt(splitDomain[3].substring(11));
					int assocArg = Integer.parseInt(splitDomain[4].substring(4));

					if (value.equalsIgnoreCase("true")) {
						PendingCfg.Add(ZWaveCommandClass.CommandClass.ASSOCIATION.getKey(), nodeId, assocId, assocArg, 1);
						logger.debug("Add association index '{}' to '{}'", assocId, assocArg);
						this.zController.sendData(associationCommandClass.setAssociationMessage(assocId, assocArg));
					} else {
						PendingCfg.Add(ZWaveCommandClass.CommandClass.ASSOCIATION.getKey(), nodeId, assocId, assocArg, 0);
						logger.debug("Remove association index '{}' to '{}'", assocId, assocArg);
						this.zController.sendData(associationCommandClass.removeAssociationMessage(assocId, assocArg));
					}

					// Request an update to the group
					this.zController.sendData(associationCommandClass.getAssociationMessage(assocId));

					// When associations change, we should ensure routes are configured
					// So, let's start a network heal - just for this node right now
					if(networkMonitor != null)
						networkMonitor.healNode(nodeId);
				}
			}
		}
	}

	/**
	 * Handle the inclusion/exclusion event. This just notifies the GUI.
	 * @param event
	 */
	void handleInclusionEvent(ZWaveInclusionEvent event) {
		switch(event.getEvent()) {
		case IncludeStart:
			break;
		case IncludeSlaveFound:
			break;
		case IncludeControllerFound:
			break;
		case IncludeFail:
			break;
		case IncludeDone:
			break;
		case ExcludeStart:
			break;
		case ExcludeSlaveFound:
			break;
		case ExcludeControllerFound:
			break;
		case ExcludeFail:
			break;
		case ExcludeDone:
			break;
		}
	}

	/**
	 * Event handler method for incoming Z-Wave events.
	 * 
	 * @param event
	 *            the incoming Z-Wave event.
	 */
	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		if (event instanceof ZWaveConfigurationParameterEvent) {
			// Write the node to disk
			ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
			nodeSerializer.SerializeNode(zController.getNode(event.getNodeId()));

			// We've received an updated configuration parameter
			// See if this is something in our 'pending' list and remove it
			PendingCfg.Remove(ZWaveCommandClass.CommandClass.CONFIGURATION.getKey(), event.getNodeId(), ((ZWaveConfigurationParameterEvent) event).getParameter().getIndex());
			return;
		}

		if (event instanceof ZWaveAssociationEvent) {
			// Write the node to disk
			ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
			nodeSerializer.SerializeNode(zController.getNode(event.getNodeId()));

			// We've received an updated association group
			// See if this is something in our 'pending' list and remove it
			for(ZWaveNode node : zController.getNodes()) {
				PendingCfg.Remove(ZWaveCommandClass.CommandClass.ASSOCIATION.getKey(), event.getNodeId(), ((ZWaveAssociationEvent) event).getGroup(), node.getNodeId());
			}
			return;
		}

		if (event instanceof ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) {
			// We only care about the wake-up report
			if(((ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) event).getEvent() != ZWaveWakeUpCommandClass.WAKE_UP_INTERVAL_REPORT)
				return;

			// Write the node to disk
			ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
			nodeSerializer.SerializeNode(zController.getNode(event.getNodeId()));

			// Remove this node from the pending list
			PendingCfg.Remove(ZWaveCommandClass.CommandClass.WAKE_UP.getKey(), event.getNodeId());
			return;
		}

		if (event instanceof ZWaveInclusionEvent) {
			handleInclusionEvent((ZWaveInclusionEvent)event);
		}
	}

	// The following timer implements a re-triggerable timer to stop the inclusion
	// mode after 30 seconds.
	private class InclusionTimerTask extends TimerTask {
		ZWaveController zController;
//		boolean inclusion;

//		InclusionTimerTask(ZWaveController zController, boolean inclusion) {
		InclusionTimerTask(ZWaveController zController) {
			this.zController = zController;
//			this.inclusion = inclusion;
		}

		@Override
		public void run() {
			logger.debug("Ending inclusion mode.");
			if(inclusion)
				zController.requestAddNodesStop();
			else
				zController.requestRemoveNodesStop();
			
			inclusion = false;
			exclusion = false;
		}
	}
	
	public synchronized void setInclusionTimer() {
		// Stop any existing timer
		if(timerTask != null) {
			timerTask.cancel();
		}

		// Create the timer task
//		timerTask = new InclusionTimerTask(zController, inclusion);
		timerTask = new InclusionTimerTask(zController);

		// Start the timer
		timer.schedule(timerTask, 30000);
	}

	/**
	 * The PendingConfiguration class holds information on outstanding requests
	 * When the binding sends a configuration request to a device, we hold a copy
	 * of the new value here so that any requests for the current value can take account
	 * of the pending request.
	 * When the information is updated, we remove the request from the pending list. 
	 * @author Chris Jackson
	 *
	 */
	public class PendingConfiguration {
		public class Cfg {
			int key;
			int node;
			int parameter;
			int argument;
			int value;
		}

		List<Cfg> CfgList = new ArrayList<Cfg>();

		void Add(int key, int node, int value) {
			Add(key, node, 0, 0, value);
		}

		void Add(int key, int node, int parameter, int value) {
			Add(key, node, parameter, 0, value);
		}

		void Add(int key, int node, int parameter, int argument, int value) {
			for(Cfg i : CfgList) {
				if(i.key == key && i.node == node && i.argument == argument && i.parameter == parameter) {
					i.value = value;
					return;
				}
			}
			
			Cfg n = new Cfg();
			n.key = key;
			n.node = node;
			n.parameter = parameter;
			n.argument = argument;
			n.value = value;
			CfgList.add(n);
		}

		void Remove(int key, int node) {
			Remove(key, node, 0, 0);
		}

		void Remove(int key, int node, int parameter) {
			Remove(key, node, parameter, 0);
		}

		void Remove(int key, int node, int parameter, int argument) {
			for(Cfg i : CfgList) {
				if(i.key == key && i.node == node && i.argument == argument && i.parameter == parameter) {
					CfgList.remove(i);
					return;
				}
			}
		}

		Integer Get(int key, int node) {
			return Get(key, node, 0, 0);
		}

		Integer Get(int key, int node, int parameter) {
			return Get(key, node, parameter, 0);
		}

		Integer Get(int key, int node, int parameter, int argument) {
			for(Cfg i : CfgList) {
				if(i.key == key && i.node == node && i.argument == argument && i.parameter == parameter) {
					return i.value;
				}
			}
			return null;
		}
	}
}
