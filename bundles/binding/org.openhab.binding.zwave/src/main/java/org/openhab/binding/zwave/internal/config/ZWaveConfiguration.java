package org.openhab.binding.zwave.internal.config;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class ZWaveConfiguration implements OpenHABConfigurationService {
	private static final Logger logger = LoggerFactory.getLogger(ZWaveConfiguration.class);

	private ZWaveController zController = null;

	public ZWaveConfiguration() {
		// this.zController = this.zController;
	}

	public ZWaveConfiguration(ZWaveController controller) {
		this.zController = controller;

		// Register the service
		FrameworkUtil.getBundle(getClass()).getBundleContext()
				.registerService(OpenHABConfigurationService.class.getName(), this, null);
	}

	public String getBundleName() {
		return "zwave";
	}

	public String getVersion() {
		return FrameworkUtil.getBundle(getClass()).getBundleContext().getBundle().getVersion().toString();
	}

	@Override
	public List<OpenHABConfigurationRecord> getConfiguration(String domain) {
		if (zController == null)
			return null;

		// We only deal with top level domains here!
		if (domain.endsWith("/") == false)
			return null;

		List<OpenHABConfigurationRecord> records = new ArrayList<OpenHABConfigurationRecord>();
		OpenHABConfigurationRecord record;
		ZWaveNode node;

		if (domain.equals("status/")) {
			// Return the z-wave status information

			return null;
		}
		if (domain.equals("nodes/")) {
			ZWaveProductDatabase database = new ZWaveProductDatabase();
			// Return the list of nodes
			for (int nodeId = 0; nodeId < 256; nodeId++) {
				node = zController.getNode(nodeId);
				if (node == null)
					continue;

				if(node.getManufacturer() == 0)
					continue;

				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
					if(database.FindManufacturer(node.getManufacturer()) == false) {
						record = new OpenHABConfigurationRecord("nodes/" + "node" + nodeId + "/", "Node " + nodeId);
						record.value = "Manufacturer:"+ node.getManufacturer() + " [ID:" + Integer.toHexString(node.getDeviceId()) + ",Type:" + Integer.toHexString(node.getDeviceType()) + "]";
						record.addAction("Save", "Save Node");
						records.add(record);
						continue;
					}

					record = new OpenHABConfigurationRecord("nodes/" + "node" + nodeId + "/", "Node " + nodeId);
					record.value = database.getManufacturerName() + " [ID:" + Integer.toHexString(node.getDeviceId()) + ",Type:" + Integer.toHexString(node.getDeviceType()) + "]";
					record.addAction("Save", "Save Node");
					records.add(record);
					continue;
				}

				record = new OpenHABConfigurationRecord("nodes/" + "node" + nodeId + "/", "Node " + nodeId);
				record.value = database.getProductName();
				record.addAction("Save", "Save Node");
				records.add(record);
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
			node = zController.getNode(nodeId);
			if (node == null)
				return null;

			ZWaveProductDatabase database = new ZWaveProductDatabase();

			// Process the request
			if (arg.equals("")) {
				if(database.FindManufacturer(node.getManufacturer()) == false) {
					record = new OpenHABConfigurationRecord(domain, "ManufacturerID", "Manufacturer ID", true);
					record.value = Integer.toString(node.getManufacturer());
					records.add(record);
				}
				else {
					record = new OpenHABConfigurationRecord(domain, "Manufacturer", "Manufacturer", true);
					record.value = database.getManufacturerName();
					records.add(record);				
				}

				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false) {
					record = new OpenHABConfigurationRecord(domain, "DeviceId", "Device ID", true);
					record.value = Integer.toString(node.getDeviceId());
					records.add(record);

					record = new OpenHABConfigurationRecord(domain, "DeviceType", "Device Type", true);
					record.value = Integer.toString(node.getDeviceType());
					records.add(record);
				}
				else {
					record = new OpenHABConfigurationRecord(domain, "Product", "Product", true);
					record.value = database.getProductName();
					records.add(record);

					record = new OpenHABConfigurationRecord(domain + "parameters/", "Configuration Parameters");
					record.addAction("Refresh", "Refresh");
					records.add(record);

					record = new OpenHABConfigurationRecord(domain + "associations/", "Association Groups");
					record.addAction("Refresh", "Refresh");
					records.add(record);
				}
			} else if (arg.equals("parameters/")) {
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {
					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();

					// Loop through the parameters and add to the records...
					for (ZWaveDbConfigurationParameter parameter : configList) {
						record = new OpenHABConfigurationRecord(domain, "configuration" + parameter.Index, 
								database.getLabel(parameter.Label), false);

						// Only provide a value if it's stored in the node
						// This is the only way we can be sure of its real value
						Integer val = node.configGetParameter(parameter.Index);
						if (val != null)
							record.value = val.toString();

						// Add the data type
						if (parameter.Type.equalsIgnoreCase("list")) {
							record.type = OpenHABConfigurationRecord.TYPE.LIST;

							for (ZWaveDbConfigurationListItem item : parameter.Item)
								record.addValue(Integer.toString(item.Value), database.getLabel(item.Label));
						} else if (parameter.Type.equalsIgnoreCase("byte"))
							record.type = OpenHABConfigurationRecord.TYPE.BYTE;
						else
							record.type = OpenHABConfigurationRecord.TYPE.INT;
						
						// Add the description
						record.description = database.getLabel(parameter.Help);

						records.add(record);
					}
				}
			} else if (arg.equals("associations/")) {
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {
					List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();

					if(groupList != null) {
						// Loop through the associations and add to the records...
						for (ZWaveDbAssociationGroup group : groupList) {
							// Controller reporting associations are set to read only
							record = new OpenHABConfigurationRecord(domain, "association" + group.Index + "/", database.getLabel(group.Label),
										group.SetToController);

							// Add the description
							record.description = database.getLabel(group.Help);

							// Add the action for refresh
							record.addAction("Refresh", "Refresh");
							
							records.add(record);
						}
					}
				}
			} else if (arg.startsWith("associations/association")) {
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {

				String groupString = arg.substring(24);
				int nextDelimiter = groupString.indexOf('/');
//				String arg = null;
				if (nextDelimiter != -1) {
//					arg = nodeNumber.substring(nextDelimiter + 1);
					groupString = groupString.substring(0, nextDelimiter);
				}
				int groupId = Integer.parseInt(groupString);

				// Get the requested group so we have access to the attributes
				List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();
				if(groupList == null)
					return null;
				ZWaveDbAssociationGroup group = null;
				for (int cnt = 0; cnt < groupList.size(); cnt++) {
					if(groupList.get(cnt).Index == groupId) {
						group = groupList.get(cnt);
						break;
					}
				}

				if(group == null)
					return null;

				// Get the group members
				List<Integer> members = node.configAssociationGetMembers(groupId);

				for (int id = 0; id < 256; id++) {
					node = zController.getNode(id);
					if (node == null)
						continue;

					record = new OpenHABConfigurationRecord(domain, "node" + id, "Node " + id, false);
//							group.SetToController);

					record.type = OpenHABConfigurationRecord.TYPE.LIST;
					record.addValue("true", "Member");
					record.addValue("false", "Non-Member");

					if(members != null && members.contains(id)) {
						record.value = "true";
					}
					else {
						record.value = "false";
					}

					records.add(record);
				}
				}
			}
			
			return records;
		}

		return null;
	}

	@Override
	public void setConfiguration(String domain, List<OpenHABConfigurationRecord> records) {
		// Sanity check
		if (domain == null)
			return;

		// Process the domain
		if (domain.isEmpty()) {
			// Empty domain means bundle configuration
			return;
		}

		// Only process configuration for nodes for now
		if (domain.startsWith("node/") == false)
			return;

		// Find the node
		ZWaveNode node = zController.getNode(5);

		if (node == null)
			return;

		int parameter = 1;
		int value = 1;
		int size = 1;

		// Send the request
		node.configParameterSet(parameter, value, size);
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
		String[] splitDomain = domain.split("/");

		// There must be at least 2 components to the domain
		if (splitDomain.length < 2)
			return;

		if (splitDomain[0].equals("nodes")) {
			int nodeId = Integer.parseInt(splitDomain[1].substring(4));

			// Get the node - if it exists
			ZWaveNode node = zController.getNode(nodeId);
			if (node == null)
				return;

			if (splitDomain.length == 2) {
				if (action.equals("Save")) {
					// Write the node to disk
					ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
					nodeSerializer.SerializeNode(node);
				}
				
				// Return here as afterwards we assume there are more elements in the domain array
				return;
			}

			if (splitDomain[2].equals("parameters")) {
				if (action.equals("Refresh")) {
					ZWaveProductDatabase database = new ZWaveProductDatabase();
					if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false)
						return;

					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();

					// Request all parameters for this node
					for (ZWaveDbConfigurationParameter parameter : configList)
						node.configParameterReport(parameter.Index);
				}
			}

			if (splitDomain[2].equals("associations")) {
				if (action.equals("Refresh")) {
					ZWaveProductDatabase database = new ZWaveProductDatabase();
					if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false)
						return;

					if(splitDomain.length == 3) {
						List<ZWaveDbAssociationGroup> groupList= database.getProductAssociationGroups();

						// Request all parameters for this node
						for (ZWaveDbAssociationGroup group : groupList)
							node.configAssociationReport(group.Index);
					}
					else if(splitDomain.length == 4) {
						int nodeArg = Integer.parseInt(splitDomain[3].substring(11));
						node.configAssociationReport(nodeArg);
					}
				}
			}
		}
	}

	@Override
	public void doSet(String domain, String value) {
		final Pattern ACTION_PATTERN = Pattern.compile("nodes/node([0-9]+)/([0-9.a-zA-Z]+)/([.a-zA-Z]+)([0-9]+)(.+)");

		Matcher matcher = ACTION_PATTERN.matcher(domain);

		// If no matched for the input, try the version with the transformation
		// string
		if (!matcher.matches())
			return;

		int nodeId = Integer.parseInt(matcher.group(1));
		String type = matcher.group(2).toString();
		String arg = matcher.group(3).toString();
		int id = Integer.parseInt(matcher.group(4));

		if (type.equals("parameters")) {
			ZWaveNode node = zController.getNode(nodeId);
			if (node == null)
				return;

			ZWaveProductDatabase database = new ZWaveProductDatabase();
			if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false)
				return;

			List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();

			// Get the size
			int size = 1;
			for (ZWaveDbConfigurationParameter parameter : configList) {
				if (parameter.Index == id) {
					size = parameter.Size;
					break;
				}
			}

			node.configParameterSet(id, Integer.parseInt(value), size);
		}
		if (type.equals("associations")) {
			ZWaveNode node = zController.getNode(nodeId);
			if (node == null)
				return;

			ZWaveProductDatabase database = new ZWaveProductDatabase();
			if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) == false)
				return;

			int nodeArg = Integer.parseInt(matcher.group(5).substring(5));

			if(value.equalsIgnoreCase("true"))
				node.configAssociationAdd(id, nodeArg);
			else
				node.configAssociationRemove(id, nodeArg);
		}
	}
}
