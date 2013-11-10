package org.openhab.binding.zwave.internal.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class ZWaveConfiguration extends OpenHABConfigurationService {
	private static final Logger logger = LoggerFactory.getLogger(ZWaveConfiguration.class);

	private ZWaveController zController = null;
	private static final String FOLDER_NAME = "etc/zwave/config";

	public ZWaveConfiguration() {
		// this.zController = this.zController;
	}

	public ZWaveConfiguration(ZWaveController controller) {
		this.zController = controller;

		// Register the service
		FrameworkUtil.getBundle(getClass()).getBundleContext()
				.registerService(OpenHABConfigurationService.class.getName(), this, null);
	}

		return "zwave";
		return FrameworkUtil.getBundle(getClass()).getBundleContext().getBundle().getVersion().toString();

	@Override
	public List<OpenHABConfigurationRecord> getConfiguration(String domain) {
		if (zController == null)
			return null;

		List<OpenHABConfigurationRecord> records = new ArrayList<OpenHABConfigurationRecord>();
		OpenHABConfigurationRecord record;
		ZWaveNode node;

		if (domain.equals("status")) {
			// Return the z-wave status information

			return null;
		}
		if (domain.equals("nodes")) {
			// Return the list of nodes
			for (int nodeId = 0; nodeId < 256; nodeId++) {
				node = zController.getNode(nodeId);
				if (node == null)
					continue;

				ZWaveConfigManufacturer manufacturer = findManufacturer(node.getManufacturer());
				if (manufacturer == null)
					continue;

				ZWaveConfigProduct product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());
				if (product == null)
					continue;

				record = new OpenHABConfigurationRecord("nodes/" + "node" + nodeId, "node" + nodeId, "Node " + nodeId,
						true);
				record.value = product.name;
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

			ZWaveConfigManufacturer manufacturer = findManufacturer(node.getManufacturer());
			ZWaveConfigProduct product;
			if(manufacturer != null)
				product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());
			else
				product = null;

			// Process the request
			if (arg == null) {
				if (manufacturer == null) {
					record = new OpenHABConfigurationRecord("ManufacturerID", "Manufacturer ID", true);
					record.value = Integer.toString(node.getManufacturer());
					records.add(record);

					// Create a new product. This will cause the ID values to be
					// returned.
					product = new ZWaveConfigProduct();
				} else {
					record = new OpenHABConfigurationRecord("Manufacturer", "Manufacturer", true);
					record.value = manufacturer.name;
					records.add(record);

					if (product != null) {
						record = new OpenHABConfigurationRecord("Product", "Product", true);
						record.value = product.name;
						records.add(record);
					}
				}

				if (product.config == null) {
					record = new OpenHABConfigurationRecord("DeviceId", "Device ID", true);
					record.value = Integer.toString(node.getDeviceId());
					records.add(record);

					record = new OpenHABConfigurationRecord("DeviceType", "Device Type", true);
					record.value = Integer.toString(node.getDeviceType());
					records.add(record);
				} else {
					record = new OpenHABConfigurationRecord("nodes/" + "node" + nodeId + "/parameters", "parameters",
							"Configuration Parameters", true);
					record.addAction("Refresh", "Refresh");
					records.add(record);

					record = new OpenHABConfigurationRecord("nodes/" + "node" + nodeId + "/associations",
							"associations", "Association Groups", true);
					records.add(record);
				}
			} else if (arg.equals("parameters")) {
				product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());
				if (product != null) {
					List<ZWaveConfigValue> config = loadDeviceConfig(node, product.config);
					
					// Loop through the products and add to the records...
					for (ZWaveConfigValue value : config) {
						record = new OpenHABConfigurationRecord("configuration" + value.index, value.label, false);

						// Only provide a value if it's stored in the node
						// This is the only way we can be sure of its real value
						Integer val = node.configGetParameter(value.index);
						if (val != null)
							record.value = val.toString();

						// Add the data type
						if (value.type.equalsIgnoreCase("list")) {
							record.type = OpenHABConfigurationRecord.TYPE.LIST;

							for (ZWaveConfigItem item : value.Item)
								record.addValue(Integer.toString(item.value), item.label);
						} else if (value.type.equalsIgnoreCase("byte"))
							record.type = OpenHABConfigurationRecord.TYPE.BYTE;
						else
							record.type = OpenHABConfigurationRecord.TYPE.INT;

						records.add(record);
					}
				}

			} else if (arg.equals("associations")) {

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
	 * @param file
	 */
	private List<ZWaveConfigValue> loadDeviceConfig(ZWaveNode node, String file) {
		logger.debug("Loading ZWave produce information file");

		List<OpenHABConfigurationRecord> records = new ArrayList<OpenHABConfigurationRecord>();

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

	@Override
	String getCommonName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getDescription() {
		return "Provides interface to Z-Wave network";
	}

	@Override
	public void doAction(String domain, String name, String action) {
		if (domain.startsWith("nodes/node")) {
			String nodeNumber = domain.substring(10);
			int next = nodeNumber.indexOf('/');
			String arg = null;
			if (next != -1) {
				arg = nodeNumber.substring(next + 1);
				nodeNumber = nodeNumber.substring(0, next);
			}
			
			if(arg == null)
				return;
			
			int nodeId = Integer.parseInt(nodeNumber);

			if(arg.equals("parameters") && name.equals("parameters")) {
				if(action.equals("Refresh")) {
					ZWaveNode node = zController.getNode(nodeId);
					if (node == null)
						return;

					ZWaveConfigManufacturer manufacturer = findManufacturer(node.getManufacturer());
					if(manufacturer == null)
						return;

					ZWaveConfigProduct product = findProduct(manufacturer, node.getDeviceId(), node.getDeviceType());

					List<ZWaveConfigValue> config = loadDeviceConfig(node, product.config);

					// Request all parameters for this node
					for (ZWaveConfigValue value : config)
						node.configParameterReport(value.index);
				}
			}
		}		
	}
}
