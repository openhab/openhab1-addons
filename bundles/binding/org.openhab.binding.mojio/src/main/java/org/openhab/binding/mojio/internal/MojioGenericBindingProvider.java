/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.internal;

import org.openhab.binding.mojio.MojioBindingProvider;
import org.openhab.binding.mojio.messages.VehicleLocation;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Vladimir Pavluk
 * @since 1.0
 */
public class MojioGenericBindingProvider extends AbstractGenericBindingProvider implements MojioBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(MojioGenericBindingProvider.class);

   @Override
  public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
  }

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "mojio";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		MojioBindingConfig config = new MojioBindingConfig();
		
		//parse bindingconfig here ...
		// 352648063154983/LastLocation/Lat
		// 352648063154983/LastLocation/Lng
		// 352648063154983/FuelLevel
    String[] configParts = bindingConfig.split(";");
    String[] path = configParts[0].split("/");

    logger.debug("Configured new item binding as " + bindingConfig);

    config.Imei = path[0];
    config.boundPath = new String[path.length - 1];
    config.conversionRate = 1;
    config.itemType = item.getClass();
    System.arraycopy(path, 1, config.boundPath, 0, config.boundPath.length);

    if(configParts.length > 1) {
      String rate = configParts[1];
      rate = rate.replace("unitconvert=", "");
      config.conversionRate = Double.parseDouble(rate);
    }

		addBindingConfig(item, config);		
	}
	
  @Override
  public String getMojioIMEI(String itemName) {
    MojioBindingConfig config = (MojioBindingConfig) this.bindingConfigs.get(itemName);
    return config.Imei;
  }

  @Override
  public String[] getValuePath(String itemName) {
    MojioBindingConfig config = (MojioBindingConfig) this.bindingConfigs.get(itemName);
    return config.boundPath;
  }

  @Override
  public double getItemRate(String itemName) {
    MojioBindingConfig config = (MojioBindingConfig) this.bindingConfigs.get(itemName);
    return config.conversionRate;
  }

  @Override
  public Class getItemType(String itemName) {
    MojioBindingConfig config = (MojioBindingConfig) this.bindingConfigs.get(itemName);
    return config.itemType;
  }
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Vladimir Pavluk
	 * @since 1.0
	 */
	class MojioBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
    String Imei; // Mojio IMEI
    String[] boundPath; // JSON path
    double conversionRate;
    Class itemType;
	}
	
}
