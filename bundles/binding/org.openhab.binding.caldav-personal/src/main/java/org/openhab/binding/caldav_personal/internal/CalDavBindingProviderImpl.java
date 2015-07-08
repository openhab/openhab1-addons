/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_personal.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.openhab.binding.caldav_personal.CalDavBindingProvider;
import org.openhab.binding.caldav_personal.internal.CalDavConfig.Type;
import org.openhab.binding.caldav_personal.internal.CalDavConfig.Value;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * caldavPresence="calendar:my type:presence"
 * caldavPresence="calendar:my type:event eventNr:0 value:name"
 * caldavPresence="calendar:my type:active eventNr:0 value:name"
 * caldavPresence="calendar:my type:upcoming eventNr:0 value:place"
 * 
 * valid values: name, description, place, start, end
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavBindingProviderImpl extends AbstractGenericBindingProvider implements CalDavBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(CalDavBindingProviderImpl.class);
	
	private static final String REGEX_CALENDAR = "calendar:([A-Za-z-_]+(,[A-Za-z-_]+)*)";
	private static final String REGEX_TYPE = "type:([A-Za-z]+)";
	private static final String REGEX_EVENT_NR = "eventNr:([0-9]+)"; 
	private static final String REGEX_VALUE = "value:([A-Za-z]+)"; 
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "caldavPersonal";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, String and DateTimeItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig == null) {
			logger.debug("binding-configuration is currently not set for item: " + item.getName());
			return;
		}
		
		logger.trace("handling config: {}", bindingConfig);
		
		List<String> calendar = new ArrayList<String>();;
		String type = null;
		Type typeEnum = null;
		String eventNr = null;
		String value = null;
		Value valueEnum = null;
		
		String[] splits = bindingConfig.split(" ");
		for (String split : splits) {
			logger.trace("handling config part: {}", split);
			Matcher mCalendar = Pattern.compile(REGEX_CALENDAR).matcher(split);
			if (mCalendar.matches()) {
				for (String str : mCalendar.group(1).split(",")) {
					calendar.add(str);
				}
			}
			
			Matcher mType = Pattern.compile(REGEX_TYPE).matcher(split);
			if (mType.matches()) {
				type = mType.group(1);
			}
			
			Matcher mEventNr = Pattern.compile(REGEX_EVENT_NR).matcher(split);
			if (mEventNr.matches()) {
				eventNr = mEventNr.group(1);
			}
			
			Matcher mValue = Pattern.compile(REGEX_VALUE).matcher(split);
			if (mValue.matches()) {
				value = mValue.group(1);
			}
		}
		
		logger.trace("found values: calendar={}, type={}, eventNr={}, value={}", calendar, type, eventNr, value);
		
		if (calendar == null || calendar.size() == 0) {
			throw new BindingConfigParseException("missing attribute 'calendar'");
		}
		if (type == null) {
			throw new BindingConfigParseException("missing attribute 'type'");
		}
		
		try {
			typeEnum = CalDavConfig.Type.valueOf(type.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("type '" + type + "' not valid");
		}
		
		if (typeEnum != Type.PRESENCE) {
			if (eventNr == null) {
				throw new BindingConfigParseException("missing attribute 'eventNr'");
			}
			if (value == null) {
				throw new BindingConfigParseException("missing attribute 'value'");
			}
			if (!NumberUtils.isDigits(eventNr)) {
				throw new BindingConfigParseException("attribute 'eventNr' must be a valid integer");
			}
			try {
				valueEnum = CalDavConfig.Value.valueOf(value.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new BindingConfigParseException("value '" + type + "' not valid");
			}
		} else {
			if (eventNr != null) {
				throw new BindingConfigParseException("not required attribute 'eventNr'");
			}
			if (value != null) {
				throw new BindingConfigParseException("not required attribute 'value'");
			}
		}
		
		logger.debug("adding item: {}", item.getName());
		this.addBindingConfig(item, new CalDavConfig(
				calendar, typeEnum, NumberUtils.toInt(eventNr == null ? "0" : eventNr), valueEnum)
		);
	}
	
	@Override
	public CalDavConfig getConfig(String item) {
		return (CalDavConfig) this.bindingConfigs.get(item);
	}
}
