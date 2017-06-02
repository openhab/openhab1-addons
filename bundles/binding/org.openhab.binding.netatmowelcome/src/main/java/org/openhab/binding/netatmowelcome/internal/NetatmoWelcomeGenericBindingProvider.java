/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmowelcome.internal;

import org.openhab.binding.netatmowelcome.NetatmoWelcomeBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * Valid bindings for a home are:
 * <ul>
 * <li>
 * <code>{ netatmowelcome="&lt;home_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#Name" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#PlaceCountry" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#PlaceTimezone" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 *
 * Valid bindings for a person are:
 * <ul>
 * <li>
 * <code>{ netatmowelcome="&lt;home_id&gt;#&lt;person_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#Pseudo" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#LastSeen" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#OutOfSight" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#FaceId" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#FaceKey" }</code></li>
 * </ul>
 * </li> </ul>
 *
 * Valid bindings for unknown persons are:
 * <ul>
 * <li>
 * <code>{ netatmowelcome="&lt;home_id&gt;#&lt;UNKNOWN&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#UNKNOWN#HomeCount" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#UNKNOWN#AwayCount" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#UNKNOWN#LastSeenList" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#UNKNOWN#OutOfSightList" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#UNKNOWN#FaceIdList" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#UNKNOWN#FaceKeyList" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 * Valid bindings for a camera are:
 * <ul>
 * <li>
 * <code>{ netatmowelcome="&lt;home_id&gt;#&lt;camera_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#Status" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#SdStatus" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#AlimStatus" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#Name" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 * Valid bindings for a event is -> NOT IMPLEMENTED NOW: TODO
 * <ul>
 * <li>
 * <code>{ netatmowelcome="&lt;home_id&gt;#&lt;event_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#Status" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#SdStatus" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#AlimStatus" }</code></li>
 * <li><code>{ netatmowelcome="1234567890abcdefghijklmn#00:00:00:00:00:00#Name" }</code></li>
 * </ul>
 * </li> </ul>
 *  *
 * @author Ing. Peter Weiss
 * @since 1.8.0
 *  
 */
public class NetatmoWelcomeGenericBindingProvider extends AbstractGenericBindingProvider implements NetatmoWelcomeBindingProvider {

	private static Logger logger = LoggerFactory
			.getLogger(NetatmoWelcomeGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "netatmowelcome";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		/*
		if (!(item instanceof NumberItem || item instanceof DateTimeItem
				|| item instanceof LocationItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems, DateTimeItems, StringItems and LocationItems are allowed - please check your *.items configuration");
		}
		*/
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		logger.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		final NetatmoWelcomeBindingConfig config = new NetatmoWelcomeBindingConfig();

		final String[] configParts = bindingConfig.split("#");
		switch (configParts.length) {
		case 2:
			config.homeId=configParts[0];
			config.attribute=configParts[1];
			break;
		case 3:
			config.homeId=configParts[0];
			config.attribute=configParts[2];
			
			//Check Format (Mac Adress with : is a camera)
			String sTmp=configParts[1];
			final String[] sItem = sTmp.split(":");
			if (sItem.length == 6)
				config.cameraId = sTmp;
			else
				config.personId = sTmp;		
			break;
		default:
			throw new BindingConfigParseException(
					"A Netatmo welcome binding configuration must consist of two, three or four parts - please verify your *.items file");
		}

		logger.debug("Adding binding: {}", config);		
		addBindingConfig(item, config);		
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
 * @author Ing. Peter Weiss
 * @since 1.8.0
 * 	 
 */
	class NetatmoWelcomeBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		public String homeId;
		public String personId;
		public String cameraId;
		public String attribute;
		
		@Override
		public String toString() {
			return "NetatmoWelcomeBindingConfig [homeId=" + this.homeId
					+ " personId=" + this.personId
					+ " cameraId=" + this.cameraId
					+ " attribute=" + this.attribute
					+ "]";
		}
	}


	@Override
	public String getUserid(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getHomeId(String itemName) {
		final NetatmoWelcomeBindingConfig config = (NetatmoWelcomeBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.homeId : null;
	}

	@Override
	public String getPersonId(String itemName) {
		final NetatmoWelcomeBindingConfig config = (NetatmoWelcomeBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.personId : null;
	}
	
	@Override
	public String getAttribute(String itemName) {
		final NetatmoWelcomeBindingConfig config = (NetatmoWelcomeBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.attribute : null;
	}

	@Override
	public String getCameraId(String itemName) {
		final NetatmoWelcomeBindingConfig config = (NetatmoWelcomeBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.cameraId : null;
	}
}
