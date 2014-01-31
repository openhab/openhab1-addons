/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Implements the top level functions for the XML product database This class
 * includes helper functions to manipulate the database and facilitate access to
 * the database.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class ZWaveProductDatabase {
	private static final Logger logger = LoggerFactory.getLogger(ZWaveProductDatabase.class);

	ZWaveDbRoot database = null;
	Languages language = Languages.ENGLISH;

	ZWaveDbManufacturer selManufacturer = null;
	ZWaveDbProduct selProduct = null;

	ZWaveDbProductFile productFile = null;

	public ZWaveProductDatabase() {
		loadDatabase();
	}

	/**
	 * Constructor for the product database
	 * 
	 * @param Language
	 *            defines the language in which all labels will be returned
	 */
	public ZWaveProductDatabase(Languages Language) {
		language = Language;
		loadDatabase();
	}

	/**
	 * Constructor for the product database
	 * 
	 * @param Language
	 *            defines the language in which all labels will be returned
	 */
	public ZWaveProductDatabase(String Language) {
		language = Languages.fromString(Language);
		loadDatabase();
	}

	private void loadDatabase() {
		URL entry = FrameworkUtil.getBundle(ZWaveProductDatabase.class).getEntry("database/products.xml");
		if (entry == null) {
			database = null;
			logger.error("Unable to load ZWave product database!");
			return;
		}

		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("Manufacturers", ZWaveDbRoot.class);
		xstream.alias("Manufacturer", ZWaveDbManufacturer.class);
		xstream.alias("Product", ZWaveDbProduct.class);
		xstream.alias("Reference", ZWaveDbProductReference.class);

		xstream.processAnnotations(ZWaveDbRoot.class);

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
		if (productFile != null)
			return productFile;

		// Have we selected a product?
		if (selProduct == null)
			return null;

		URL entry = FrameworkUtil.getBundle(ZWaveProductDatabase.class).getEntry("database/" + selProduct.ConfigFile);
		if (entry == null) {
			database = null;
			logger.error("Unable to load ZWave product file: '{}'", selProduct.ConfigFile);
			return null;
		}

		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("Product", ZWaveDbProductFile.class);
		xstream.alias("Configuration", ZWaveDbProductFile.ZWaveDbConfiguration.class);
		xstream.alias("Parameter", ZWaveDbConfigurationParameter.class);
		xstream.alias("Item", ZWaveDbConfigurationListItem.class);
		xstream.alias("Associations", ZWaveDbProductFile.ZWaveDbAssociation.class);
		xstream.alias("Group", ZWaveDbAssociationGroup.class);
		xstream.alias("CommandClass", ZWaveDbProductFile.ZWaveDbCommandClassList.class);
		xstream.alias("Class", ZWaveDbCommandClass.class);

		xstream.processAnnotations(ZWaveDbProductFile.class);

		try {
			// this.Manufacturer = (ZWaveDbManufacturer)
			InputStream x = entry.openStream();
			productFile = (ZWaveDbProductFile) xstream.fromXML(x);
		} catch (IOException e) {
			logger.error("Unable to load ZWave product file '{}' : {}", selProduct.ConfigFile, e.toString());
		}

		return productFile;
	}

	public List<ZWaveDbManufacturer> GetManufacturers() {
		return database.Manufacturer;
	}

	public List<ZWaveDbProduct> GetProducts() {
		if (selManufacturer == null)
			return null;

		return selManufacturer.Product;
	}

	/**
	 * Finds the manufacturer in the database.
	 * 
	 * @param manufacturerId
	 * @return true if the manufacturer was found
	 */
	public boolean FindManufacturer(int manufacturerId) {
		if (database == null)
			return false;

		selManufacturer = null;
		selProduct = null;
		productFile = null;

		for (ZWaveDbManufacturer manufacturer : database.Manufacturer) {
			if (manufacturer.Id == manufacturerId) {
				selManufacturer = manufacturer;
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds a product in the database
	 * 
	 * @param manufacturerId
	 *            The manufacturer ID
	 * @param productType
	 *            The product type
	 * @param productId
	 *            The product ID
	 * @return true if the product was found
	 */
	public boolean FindProduct(int manufacturerId, int productType, int productId) {
		if (FindManufacturer(manufacturerId) == false)
			return false;

		return FindProduct(productType, productId);
	}

	/**
	 * Finds a product in the database. FindManufacturer must be called before
	 * this function.
	 * 
	 * @param productType
	 *            The product type
	 * @param productId
	 *            The product ID
	 * @return true if the product was found
	 */
	public boolean FindProduct(int productType, int productId) {
		if (selManufacturer == null)
			return false;

		for (ZWaveDbProduct product : selManufacturer.Product) {
			for (ZWaveDbProductReference reference : product.Reference) {
				if (reference.Type == productType && reference.Id == productId) {
					selProduct = product;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the manufacturer name. FindManufacturer or FindProduct must be
	 * called before this method.
	 * 
	 * @return String with the manufacturer name, or null if not found.
	 */
	public String getManufacturerName() {
		if (selManufacturer == null)
			return "";
		else
			return selManufacturer.Name;
	}

	/**
	 * Returns the manufacturer ID. FindManufacturer or FindProduct must be
	 * called before this method.
	 * 
	 * @return Integer with the manufacturer ID, or null if not found.
	 */
	public Integer getManufacturerId() {
		if (selManufacturer == null)
			return null;
		else
			return selManufacturer.Id;
	}

	/**
	 * Returns the product name. FindProduct must be called before this method.
	 * 
	 * @return String with the product name, or null if not found.
	 */
	public String getProductName() {
		if (selProduct == null)
			return "";
		else
			return selProduct.Model + " " + getLabel(selProduct.Label);
	}

	/**
	 * Returns the product model. FindProduct must be called before this method.
	 * 
	 * @return String with the product model, or null if not found.
	 */
	public String getProductModel() {
		if (selProduct == null)
			return null;

		return selProduct.Model;
	}

	/**
	 * Returns the number of endpoints from the database. FindProduct must be
	 * called before this method.
	 * 
	 * @return number of endpoints
	 */
	public Integer getProductEndpoints() {
		if (selProduct == null)
			return null;

		return 0;
	}

	/**
	 * Checks if a specific command class is implemented by the device.
	 * FindProduct must be called before this method.
	 * 
	 * @param classNumber
	 *            the class number to check
	 * @return true if the class is supported
	 */
	public boolean doesProductImplementCommandClass(Integer classNumber) {
		if (LoadProductFile() == null)
			return false;

		if(productFile.CommandClasses == null || productFile.CommandClasses.Class == null)
			return false;

		for(ZWaveDbCommandClass iClass : productFile.CommandClasses.Class) {
			if(iClass.Id.equals(classNumber))
				return true;
		}

		return false;
	}

	/**
	 * Returns the command classes implemented by the device.
	 * FindProduct must be called before this method.
	 * 
	 * @return true if the class is supported
	 */
	public List<ZWaveDbCommandClass> getProductCommandClasses() {
		if (LoadProductFile() == null)
			return null;

		if(productFile.CommandClasses == null)
			return null;
		
		return productFile.CommandClasses.Class;
	}

	/**
	 * Returns the configuration parameters list. FindProduct must be called
	 * before this method.
	 * 
	 * @return List of configuration parameters
	 */
	public List<ZWaveDbConfigurationParameter> getProductConfigParameters() {
		if (LoadProductFile() == null)
			return null;

		return productFile.getConfiguration();
	}

	/**
	 * Returns the associations list. FindProduct must be called before this
	 * method.
	 * 
	 * @return List of association groups
	 */
	public List<ZWaveDbAssociationGroup> getProductAssociationGroups() {
		if (LoadProductFile() == null)
			return null;

		return productFile.getAssociations();
	}

	private class ZWaveDbRoot {
		@XStreamImplicit
		List<ZWaveDbManufacturer> Manufacturer;
	}

	/**
	 * Helper function to find the label associated with the specified database
	 * language If no language is defined, or if the label cant be found in the
	 * specified language the english label will be returned.
	 * 
	 * @param labelList
	 *            A List defining the label
	 * @return String of the respective language
	 */
	public String getLabel(List<ZWaveDbLabel> labelList) {
		if (labelList == null)
			return null;

		for (ZWaveDbLabel label : labelList) {
			if (label.Language == null)
				return label.Label;

			if (label.Language.equals(language.toString()))
				return label.Label;
		}
		return null;
	}

	/**
	 * enum defining the languages used for the multilingual labels in the
	 * product database
	 * 
	 */
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
