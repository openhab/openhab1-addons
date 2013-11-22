package org.openhab.binding.zwave.internal.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.osgi.framework.FrameworkUtil;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ZWaveProductDatabase {
	ZWaveDbRoot database = null;
	Languages language = Languages.ENGLISH;
	
	ZWaveDbManufacturer selManufacturer = null;
	ZWaveDbProduct selProduct = null;
	
	ZWaveDbProductFile productFile = null;

	public ZWaveProductDatabase() {
		loadDatabase();
	}

	public ZWaveProductDatabase(Languages Language) {
		language = Language;
		loadDatabase();
	}

	public ZWaveProductDatabase(String Language) {
		language = Languages.fromString(Language);
		loadDatabase();
	}

	private void loadDatabase() {
		URL entry = FrameworkUtil.getBundle(ZWaveProductDatabase.class).getEntry("database/products.xml");
		if (entry == null) {
			// throw new
			// RenderException("Cannot find a snippet for element type ''");
		}

		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("Manufacturers", ZWaveDbRoot.class);
		xstream.alias("Manufacturer", ZWaveDbManufacturer.class);
		xstream.alias("Product", ZWaveDbProduct.class);
		xstream.alias("Reference", ZWaveDbProductReference.class);

		xstream.processAnnotations(ZWaveDbRoot.class);
		// xstream.processAnnotations(ZWaveConfigManufacturer.class);
		// xstream.processAnnotations(ZWaveConfigProduct.class);

		try {
			// this.Manufacturer = (ZWaveDbManufacturer)
			InputStream x = entry.openStream();
			database = (ZWaveDbRoot) xstream.fromXML(x);
			if (database == null)
				return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ZWaveDbProductFile LoadProductFile() {
		// If the file is already loaded, then just return the class
		if(productFile != null)
			return productFile;

		// Have we selected a product?
		if(selProduct == null)
			return null;
		
		URL entry = FrameworkUtil.getBundle(ZWaveProductDatabase.class).getEntry("database/"+selProduct.ConfigFile);
		if (entry == null) {
			// throw new
			// RenderException("Cannot find a snippet for element type ''");
		}

		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("Product", ZWaveDbProductFile.class);
		xstream.alias("Configuration", ZWaveDbProductFile.ZWaveDbConfiguration.class);
		xstream.alias("Parameter", ZWaveDbConfigurationParameter.class);
		xstream.alias("Item", ZWaveDbConfigurationListItem.class);
		xstream.alias("Associations", ZWaveDbProductFile.ZWaveDbAssociation.class);
		xstream.alias("Group", ZWaveDbAssociationGroup.class);

		xstream.processAnnotations(ZWaveDbProductFile.class);
		// xstream.processAnnotations(ZWaveConfigManufacturer.class);
		// xstream.processAnnotations(ZWaveConfigProduct.class);

		try {
			// this.Manufacturer = (ZWaveDbManufacturer)
			InputStream x = entry.openStream();
			productFile = (ZWaveDbProductFile) xstream.fromXML(x);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productFile;
	}

	public boolean FindManufacturer(int manufacturerId) {
		selManufacturer = null;
		selProduct = null;
		productFile = null;
		
		for(ZWaveDbManufacturer manufacturer : database.Manufacturer) {
			if(manufacturer.Id == manufacturerId) {
				selManufacturer = manufacturer;
				return true;
			}
		}
		return false;
	}

	public boolean FindProduct(int manufacturerId, int productType, int productId) {
		if(FindManufacturer(manufacturerId) == false)
			return false;

		return FindProduct(productType, productId);
	}

	public boolean FindProduct(int productType, int productId) {
		if(selManufacturer == null)
			return false;

		for(ZWaveDbProduct product : selManufacturer.Product) {
			for(ZWaveDbProductReference reference : product.Reference) {
				if(reference.Type == productType && reference.Id == productId) {
					selProduct = product;
					return true;
				}
			}
		}
		return false;
	}

	public String getManufacturerName() {
		if(selManufacturer == null)
			return "";
		else
			return selManufacturer.Name;
	}

	public String getProductName() {
		if(selProduct == null)
			return "";
		else
			return selProduct.Model + " " + getLabel(selProduct.Label);
	}

	public String getProductModel() {
		if(selProduct == null)
			return null;

		return selProduct.Model;
	}

	public Integer getProductEndpoints() {
		if(selProduct == null)
			return null;

		return 0;
	}

	public List<ZWaveDbConfigurationParameter> getProductConfigParameters() {
		if(LoadProductFile() == null)
			return null;

		return productFile.getConfiguration();
	}

	public List<ZWaveDbAssociationGroup> getProductAssociationGroups() {
		if(LoadProductFile() == null)
			return null;

		return productFile.getAssociations();
	}

	/**
	 * Prints a hex string 4 digits long
	 * @param value The integer value to print
	 * @return the hexadecimal string
	 */
	private String hexString(Integer value) {
		String hex = new String();
		if(value < 0x0fff)
			hex += "0";
		if(value < 0x00ff)
			hex += "0";
		if(value < 0x000f)
			hex += "0";
		hex += Integer.toHexString(value);
		return hex;
	}

	private class ZWaveDbRoot {
		@XStreamImplicit
		List<ZWaveDbManufacturer> Manufacturer;
	}

	public String getLabel(List<ZWaveDbLabel> labelList) {
		if(labelList == null)
			return null;

		for(ZWaveDbLabel label : labelList) {
			if(label.Language == null)
				return label.Label;

			if(label.Language.equals(language.toString()))
				return label.Label;
		}
		return null;
	}

	public enum Languages {
		ENGLISH("en"), GERMAN("de");

		private String value;

		private Languages(String value) {
			this.value = value;
		}

		public static Languages fromString(String text) {
			if (text != null) {
				for (Languages c : Languages.values()) {
					if (text.equalsIgnoreCase(c.value)) {
						return c;
					}
				}
			}
			return ENGLISH;
		}

		public String toString() {
			return this.value;
		}
	}
}
