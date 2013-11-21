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
 * ZWave Configuration interface class
 * This provides an interface to the REST system to allow manual configuration 
 * of the ZWave network.
 * It uses a configuration database to read device data and support the configuration
 * interface.
 *  
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class ZWaveConfiguration implements OpenHABConfigurationService {
	private static final Logger logger = LoggerFactory.getLogger(ZWaveConfiguration.class);

	private ZWaveController zController = null;

	public ZWaveConfiguration() {
	}

	public ZWaveConfiguration(ZWaveController controller) {
		this.zController = controller;

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
			for (int nodeId = 2; nodeId < 256; nodeId++) {
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
<<<<<<< HEAD
				product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());
				if (product != null) {
					List<ZWaveConfigValue> config = loadDeviceConfig(node, product.config);
=======
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {
					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
>>>>>>> e8aced2... ZWave Configuration updates - new database format and classes

					// Loop through the products and add to the records...
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

						records.add(record);
					}
				}

			} else if (arg.equals("associations/")) {
<<<<<<< HEAD

=======
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId()) != false) {
					List<ZWaveDbAssociationGroup> groupList = database.getProductAssociationGroups();

					if(groupList != null) {
						// Loop through the products and add to the records...
						for (ZWaveDbAssociationGroup group : groupList) {
							record = new OpenHABConfigurationRecord(domain, "association" + group.Index, database.getLabel(group.Label),
									false);
							records.add(record);
						}
					}
				}
>>>>>>> e8aced2... ZWave Configuration updates - new database format and classes
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

<<<<<<< HEAD
	/**
	 * 
	 * @return
	 */
	private ZWaveConfigManufacturer findManufacturer(int manufacturer) {
		logger.debug("Loading ZWave product database.");

		ZWaveConfigManufacturerList products = null;
		FileInputStream fin;
		try {
			fin = new FileInputStream(FOLDER_NAME + "/manufacturer_specific.xml");

			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("ManufacturerSpecificData", ZWaveConfigManufacturerList.class);
			xstream.alias("Manufacturer", ZWaveConfigManufacturer.class);
			xstream.alias("Product", ZWaveConfigProduct.class);
			xstream.processAnnotations(ZWaveConfigManufacturerList.class);

			products = (ZWaveConfigManufacturerList) xstream.fromXML(fin);

			fin.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		if (products == null)
			return null;

		// Product list is loaded, now try and find our product
		for (ZWaveConfigManufacturer m : products.Manufacturer) {
			if (Long.parseLong(m.id, 16) == manufacturer)
				return m;
		}

		// Not found!!
		return null;
	}

	private ZWaveConfigProduct findProduct(ZWaveConfigManufacturer manufacturer, int productId, int typeId) {
		for (ZWaveConfigProduct p : manufacturer.Product) {
			if (Long.parseLong(p.id, 16) == productId && Long.parseLong(p.type, 16) == typeId) {
				return p;
			}
		}

		return null;
	}

	/**
	 * 
	 * @param node
	 * @param file
	 */
	private List<ZWaveConfigValue> loadDeviceConfig(ZWaveNode node, String file) {
		logger.debug("Loading ZWave produce information file");

		// List<OpenHABConfigurationRecord> records = new
		// ArrayList<OpenHABConfigurationRecord>();

		ZWaveConfigProductList products = null;
		FileInputStream fin;
		try {
			fin = new FileInputStream(FOLDER_NAME + "/" + file);

			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("Product", ZWaveConfigProductList.class);
			xstream.alias("CommandClass", ZWaveConfigCommandClass.class);
			xstream.alias("Value", ZWaveConfigValue.class);
			xstream.alias("Item", ZWaveConfigItem.class);
			xstream.alias("Associations", ZWaveConfigAssociation.class);
			xstream.alias("Group", ZWaveConfigGroup.class);
			xstream.processAnnotations(ZWaveConfigProductList.class);

			products = (ZWaveConfigProductList) xstream.fromXML(fin);

			fin.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return products.CommandClass.get(0).Value;
	}
=======
>>>>>>> e8aced2... ZWave Configuration updates - new database format and classes

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

<<<<<<< HEAD
					ZWaveConfigProduct product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());

					List<ZWaveConfigValue> config = loadDeviceConfig(node, product.config);
=======
					List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
>>>>>>> e8aced2... ZWave Configuration updates - new database format and classes

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

<<<<<<< HEAD
					ZWaveConfigProduct product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());

					List<ZWaveConfigValue> config = loadDeviceConfig(node, product.config);

					// Request all parameters for this node
//					for (ZWaveConfigValue value : config)
//						node.configParameterReport(value.index);
=======
					List<ZWaveDbAssociationGroup> groupList= database.getProductAssociationGroups();

					// Request all parameters for this node
					for (ZWaveDbAssociationGroup group : groupList)
						node.configAssociationReport(group.Index);
>>>>>>> e8aced2... ZWave Configuration updates - new database format and classes
				}
			}
		}
	}

	@Override
	public void doSet(String domain, String value) {
		final Pattern ACTION_PATTERN = Pattern.compile("nodes/node([0-9]+)/([0-9.a-zA-Z]+)/([.a-zA-Z]+)([0-9]+)");

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

<<<<<<< HEAD
			ZWaveConfigProduct product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());

			List<ZWaveConfigValue> config = loadDeviceConfig(node, product.config);
=======
			List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
>>>>>>> e8aced2... ZWave Configuration updates - new database format and classes

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
	}
}
