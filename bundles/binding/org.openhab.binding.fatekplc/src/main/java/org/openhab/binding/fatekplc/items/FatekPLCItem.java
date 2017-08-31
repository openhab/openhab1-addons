/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.FatekPLC;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default Fatek item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public abstract class FatekPLCItem implements BindingConfig {

	/**
	 * Supported Items Types by Fatek PLC
	 *
	 * @author Slawomir Jaranowski
	 */
	enum ItemsTypes {

		COLOR(ColorItem.class),
		CONTACT(ContactItem.class),
		DATETIME(DateTimeItem.class),
		DIMMER(DimmerItem.class),
		NUMBER(NumberItem.class),
		ROLLERSHUTTER(RollershutterItem.class),
		SWITCH(SwitchItem.class);

		private Class<? extends Item> aClass;

		private ItemsTypes(Class<? extends Item> aclass) {
			this.aClass = aclass;

		}

		public static ItemsTypes find(Class<? extends Item> aClass) {
			for (ItemsTypes v : values()) {
				if (v.aClass.equals(aClass)) {
					return v;
				}
			}
			return null;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(FatekPLCItem.class);

	private static final Pattern PARAM_CONFIG_PATTERN = Pattern.compile("(\\w+)='?(.+)'?");

	// fields
	private String slaveName;

	private String itemName;

	protected Reg reg1;

	/**
	 * How often refresh should occur.
	 */
	private int refreshStep;

	private int currentRefreshStep;

	/**
	 * Validates if the <code>item</code> is valid for this binding.
	 *
	 * @param item item to check
	 * @throws BindingConfigParseException
	 */
	public static void validateItemType(Item item) throws BindingConfigParseException {

		logger.debug("validateItemType: {}", item);

		if (ItemsTypes.find(item.getClass()) == null) {
			throw new BindingConfigParseException(String.format("Item %s class %s not supported by Fatek PLC",
					item.getName(), item.getClass().getName()));
		}
	}

	/**
	 * Create the corresponding Fatek item for given type.
	 * @param item OpenHab item
	 * @param bindingConfig config
	 * @return corresponding Fatek Item for given item type
	 * @throws BindingConfigParseException
	 */
	public static FatekPLCItem parseBindingConfiguration(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if (StringUtils.isBlank(bindingConfig)) {
			throw new BindingConfigParseException("bindconfig is empty");
		}

		List<String> confItems = new ArrayList<>(Arrays.asList(StringUtils.split(bindingConfig, ':')));

		if (confItems.size() < 2) {
			throw new BindingConfigParseException(String.format("Incorrect binding config: %s", bindingConfig));
		}


		FatekPLCItem config;
		switch (ItemsTypes.find(item.getClass())) {
		case COLOR:
			config = new FatekColorItem(item, confItems);
			break;
		case CONTACT:
			config = new FatekContactItem(item, confItems);
			break;
		case DATETIME:
			config = new FatekDateTimeItem(item, confItems);
			break;
		case DIMMER:
			config = new FatekDimmerItem(item, confItems);
			break;
		case NUMBER:
			config = new FatekNumberItem(item, confItems);
			break;
		case ROLLERSHUTTER:
			config = new FatekRollershutterItem(item, confItems);
			break;
		case SWITCH:
			config = new FatekSwitchItem(item, confItems);
			break;
		default:
			throw new BindingConfigParseException(String.format("Item %s class %s not supported by Fatek PLC",
					item.getName(), item.getClass().getName()));
		}

		return config;
	}

	public FatekPLCItem(Item item, List<String> confItems) throws BindingConfigParseException {

		itemName = item.getName();
		slaveName = confItems.remove(0);
		refreshStep = getParamsFromConfAsInt(confItems, "refreshStep", 1);

		currentRefreshStep = 1;
	}

	public String getSlaveName() {
		return slaveName;
	}

	public String getItemName() {
		return itemName;
	}

	/**
	 * Test if in current update cycle item should be refreshed.
	 *
	 * @return true if item should be refreshed.
	 */
	public boolean isToRefresh() {

		// update item on every refresh cycle
		if (refreshStep == 1) {
			return true;
		}

		if (currentRefreshStep > 0) {
			currentRefreshStep--;
			if (currentRefreshStep == 0 ) {
				currentRefreshStep = refreshStep;
				return true;
			}
		}

		return false;
	}

	public Collection<? extends Reg> getRegs() {

		return Arrays.asList(reg1);
	}

	public Reg getReg1() {
		return reg1;
	}

	/**
	 * Prepare standard toString for config.
	 *
	 * @return new pre filed StringBuilder.
	 */
	protected String toString(StringBuilder str) {

		StringBuilder sb = new StringBuilder();

		sb.append(getClass().getSimpleName()).append("[");
		sb.append("slave=").append(slaveName);
		sb.append(", name=").append(itemName);
		sb.append(", reg=").append(reg1);
		if (str != null) {
			sb.append(str);
		}

		if (refreshStep != 1) {
			sb.append(", refreshStep=").append(refreshStep);
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Looking for paramsName=value in config items list.
	 *
	 * @param confItems
	 * @param param
	 *            param name to look for value
	 * @return value or defaultValue if property not found
	 */
	protected String getParamsFromConf(List<String> confItems, String param, String defaultValue) {

		for (Iterator<String> iterator = confItems.iterator(); iterator.hasNext();) {

			String conf = iterator.next();
			Matcher matcher = PARAM_CONFIG_PATTERN.matcher(conf);
			if (matcher.find()) {
				if (StringUtils.equalsIgnoreCase(matcher.group(1), param)) {
					iterator.remove();
					return matcher.group(2);
				}
			}
		}

		return defaultValue;
	}

	protected int getParamsFromConfAsInt(List<String> confItems, String param, int defaultValue) throws BindingConfigParseException {

		String strParam = getParamsFromConf(confItems, param, String.valueOf(defaultValue));

		try {
			return Integer.parseInt(strParam);
		} catch  (NumberFormatException e) {
			throw new BindingConfigParseException(e.getMessage());
		}
	}

	public abstract State getState(Map<Reg, RegValue> response);

	public void command(FatekPLC fatekPLC, Command command) throws CommandException {
		throw new UnsupportedCommandException(this, command);
	}
}
