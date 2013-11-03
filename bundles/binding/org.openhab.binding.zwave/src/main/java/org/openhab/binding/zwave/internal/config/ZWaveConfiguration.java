package org.openhab.binding.zwave.internal.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveConfigurationCommandClass;
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
public class ZWaveConfiguration  {
	private static final Logger logger = LoggerFactory.getLogger(OpenHABConfiguration.class);

	private volatile ZWaveController zController;


	public ZWaveConfiguration(ZWaveController controller) {
		this.zController = controller;
	}

	public List<OpenHABConfigurationRecord> getConfiguration(String domain) {
		List<OpenHABConfigurationRecord> records = new ArrayList<OpenHABConfigurationRecord>();
		OpenHABConfigurationRecord record;
		ZWaveNode node;

		if(domain.equals("status")) {
			// Return the z-wave status information
		}
		if(domain.equals("nodes")) {
			// Return the list of nodes
			for(int nodeId = 0; nodeId < 256; nodeId++) {
				node = zController.getNode(nodeId);
				if(node != null) {
					record = new OpenHABConfigurationRecord("DeviceId", "Device ID", true);
					record.value = Integer.toString(node.getDeviceId());
					records.add(record);
					

					ZWaveConfigManufacturer manufacturer = findManufacturer(node.getManufacturer());
					if(manufacturer == null)
						continue;

					record = new OpenHABConfigurationRecord("Manufacturer", "Manufacturer", true);
					record.value = manufacturer.name;
					records.add(record);

					ZWaveConfigProduct product = findProduct(manufacturer, node.getDeviceId());
					if(product != null)
						continue;

					record = new OpenHABConfigurationRecord("Product", "Product", true);
					record.value = product.name;
					records.add(record);
					
					records.addAll(loadDeviceConfig(product.config));
					
				}
			}
		}

		return records;
	}


	/**
	 * 
	 * @return
	 */
	private ZWaveConfigManufacturer findManufacturer(int manufacturer) {
		logger.debug("Loading ZWave product database.");
		
		URL url = OpenHABConfiguration.class.getResource("resources/");
		if (url == null) {
			logger.debug("ZWave product database not found.");
			return null;
		}

		ZWaveConfigManufacturerList products = null;
		FileInputStream fin;
		try {
			fin = new FileInputStream(url+"manufacturer_specific.xml");

			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("ManufacturerSpecificData", ZWaveConfigManufacturerList.class);
			xstream.processAnnotations(ZWaveConfigManufacturerList.class);

			products = (ZWaveConfigManufacturerList) xstream.fromXML(fin);

			fin.close();

//			return null;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		if(products == null)
			return null;

		// Product list is loaded, now try and find our product
		for(ZWaveConfigManufacturer m : products.manufacturers) {
			if(m.id == manufacturer) 
				return m;
		}
		
		// Not found!!
		return null;
	}

	private ZWaveConfigProduct findProduct(ZWaveConfigManufacturer manufacturer, int productId) {
		for(ZWaveConfigProduct p : manufacturer.product) {
			if(p.id == productId) {
				return p;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param file
	 */
	private List<OpenHABConfigurationRecord> loadDeviceConfig(String file) {
		List<OpenHABConfigurationRecord> records = new ArrayList<OpenHABConfigurationRecord>();
		
		return records;
	}
}
