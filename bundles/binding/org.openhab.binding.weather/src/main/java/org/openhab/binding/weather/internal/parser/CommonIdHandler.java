/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.weather.internal.model.Condition;
import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.model.Weather;
import org.openhab.binding.weather.internal.model.common.CommonId;
import org.openhab.binding.weather.internal.model.common.CommonIdList;
import org.openhab.binding.weather.internal.model.common.CommonIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles all the weather ids and icons of the differnet providers and map them
 * to a common weather id.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class CommonIdHandler {
	private static final Logger logger = LoggerFactory.getLogger(CommonIdHandler.class);
	private static CommonIdHandler instance;

	private Map<ProviderName, Map<String, CommonId>> providerCommonIds = new HashMap<ProviderName, Map<String, CommonId>>();

	private CommonIdHandler() {
	}

	/**
	 * Returns the singleton instance of the CommonIdHandler.
	 */
	public static CommonIdHandler getInstance() {
		if (instance == null) {
			instance = new CommonIdHandler();
		}
		return instance;
	}

	/**
	 * Load predefined common id mappings from an XML file.
	 */
	public void loadMapping() throws Exception {
		Unmarshaller um = JAXBContext.newInstance(CommonIdList.class).createUnmarshaller();
		InputStream stream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("weather/common-id-mappings.xml");
		CommonIdList mappings = (CommonIdList) um.unmarshal(stream);

		for (CommonId commonId : mappings.getCommonIds()) {
			for (CommonIdProvider commonIdProvider : commonId.getProviders()) {
				Map<String, CommonId> commonIds = providerCommonIds.get(commonIdProvider.getName());
				if (commonIds == null) {
					commonIds = new HashMap<String, CommonId>();
					providerCommonIds.put(commonIdProvider.getName(), commonIds);
				}

				addCommonId(commonIdProvider.getIds(), "id", commonIdProvider, commonIds, commonId);
				addCommonId(commonIdProvider.getIcons(), "icon", commonIdProvider, commonIds, commonId);
			}
		}
	}

	private void addCommonId(String[] keys, String keyType, CommonIdProvider providerMapping,
			Map<String, CommonId> commonIds, CommonId commonCondition) {
		if (keys != null) {
			for (String key : keys) {
				key = StringUtils.trim(key);
				if (commonIds.containsKey(key)) {
					throw new IllegalArgumentException("CommonId for provider " + providerMapping.getName() + " with "
							+ keyType + " " + key + " already exists");
				}
				commonIds.put(key, commonCondition);
			}
		}
	}

	/**
	 * Sets the common condition id into the weather object.
	 */
	public void setCommonId(Weather weather) {
		Map<String, CommonId> commonIds = providerCommonIds.get(weather.getProvider());
		if (commonIds == null) {
			throw new RuntimeException("No common ids for provider " + weather.getProvider() + " declared");
		}

		Condition cond = weather.getCondition();

		CommonId cid = commonIds.get(cond.getId());
		if (cid == null) {
			cid = commonIds.get(cond.getIcon());
		}

		if (cid != null) {
			cond.setCommonId(cid.getId());
		} else {
			ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
					.append("provider", weather.getProvider()).append("id", cond.getId())
					.append("icon", cond.getIcon());
			logger.warn("CommonId not found: {}", tsb.toString());
		}
	}
}
