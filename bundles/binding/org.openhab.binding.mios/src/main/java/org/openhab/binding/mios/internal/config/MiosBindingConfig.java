/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mios.internal.MiosActivator;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public abstract class MiosBindingConfig implements BindingConfig {
	private static Pattern TRANSFORM_PATTERN = Pattern
			.compile("(?<transform>.+)[(]{1}?(?<param>.*)[)]{1}");

	// private static Pattern ACTION_PATTERN = Pattern
	// .compile("(?<actionType>(update|command):" + "(?<service>[^/]+)"
	// + "(/(?<stuff>[^/])+[(]{1}(?<param>.*)[)]{1})?");

	protected static final Logger logger = LoggerFactory
			.getLogger(MiosBindingConfig.class);

	private int id;
	private String context;
	private String itemName;
	private String unitName;
	private String inStuff;
	private String cachedProperty;
	private String cachedBinding;
	private Class<? extends Item> itemType;
	private String commandThing;
	private String inTransform;
	private String outTransform;

	private String inTransformName = null;
	private String inTransformParam = null;

	private String outTransformName = null;
	private String outTransformParam = null;

	private TransformationService inTransformationService;
	private TransformationService outTransformationService;

	public MiosBindingConfig(String context, String itemName, String unitName,
			int id, String stuff, Class<? extends Item> itemType,
			String commandThing, String inTransform,
			String outTransform) {
		// Crude parameter validations
		assert (id >= 0);

		this.context = context;
		this.itemName = itemName;
		this.unitName = unitName;
		this.id = id;
		this.inStuff = stuff;
		this.itemType = itemType;
		this.commandThing = commandThing;
		this.inTransform = inTransform;
		this.outTransform = outTransform;

		Matcher m;
		if (inTransform != null) {
			m = TRANSFORM_PATTERN.matcher(inTransform);

			if (m.matches()) {
				this.inTransformName = m.group("transform");
				this.inTransformParam = m.group("param");

				logger.trace("start: IN '{}', '{}'", this.inTransformName,
						this.inTransformParam);
			} else {
				logger.warn(
						"Unable to parse (in) Transformation string '{}', ignored.",
						inTransform);
			}
		}

		if (outTransform != null) {
			m = TRANSFORM_PATTERN.matcher(outTransform);

			if (m.matches()) {
				this.outTransformName = m.group("transform");
				this.outTransformParam = m.group("param");

				logger.trace("start: OUT '{}', '{}'", this.outTransformName,
						this.outTransformParam);
			} else {
				logger.warn(
						"Unable to parse (out) Transformation string '{}', ignored.",
						outTransform);
			}
		}
	}

	public String getContext() {
		return this.context;
	}

	public String getItemName() {
		return this.itemName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#getUnitName()
	 */
	public String getUnitName() {
		return unitName;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#getType()
	 */
	public abstract String getType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#getId()
	 */
	public int getId() {
		return id;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#getStuff()
	 */
	public String getStuff() {
		return inStuff;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#toProperty()
	 */
	public String toProperty() {
		return cachedProperty;
	}

	public String toBinding() {
		return cachedBinding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#getItemType()
	 */
	public Class<? extends Item> getItemType() {
		return itemType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mios.MiosBindingConfig#supportsInbound()
	 */
	public boolean supportsInbound() {
		// All of the current Configs support inbound data
		return true;
	}

	public boolean supportsCommands() {
		return (getCommandThing() != null);
	}

	public String getInTransform() {
		return inTransform;
	}

	public String getInTransformName() {
		return inTransformName;
	}

	public String getInTransformParam() {
		return inTransformParam;
	}

	public String getOutTransform() {
		return outTransform;
	}

	public String getOutTransformName() {
		return outTransformName;
	}

	public String getOutTransformParam() {
		return outTransformParam;
	}

	private TransformationService getInTransformationService() {
		String name = getInTransformName();

		if (name == null || name.equals("")) {
			return null;
		}

		if (inTransformationService != null) {
			return inTransformationService;
		}

		BundleContext context = MiosActivator.getContext();

		inTransformationService = TransformationHelper
				.getTransformationService(context, name);

		if (inTransformationService == null) {

			logger.warn(
					"Transformation Service (in) '{}' not found for declaration '{}'.",
					name, getInTransform());
		}

		return inTransformationService;
	}

	public boolean supportsTransformIn() {
		return getInTransformationService() != null;
	}

	public String transformIn(String value) throws TransformationException {
		TransformationService ts = getInTransformationService();

		if (ts != null) {
			return ts.transform(getInTransformParam(), value);
		} else {
			return value;
		}
	}

	private TransformationService getOutTransformationService() {
		String name = getOutTransformName();

		if (name == null || name.equals("")) {
			return null;
		}

		if (outTransformationService != null) {
			return outTransformationService;
		}

		BundleContext context = MiosActivator.getContext();

		outTransformationService = TransformationHelper
				.getTransformationService(context, name);

		if (outTransformationService == null) {

			logger.warn(
					"Transformation Service (out) '{}' not found for declaration '{}'.",
					name, getOutTransform());
		}

		return outTransformationService;
	}

	public boolean supportsTransformOut() {
		return getOutTransformationService() != null;
	}

	public String transformOut(String value) throws TransformationException {
		TransformationService ts = getOutTransformationService();

		if (ts != null) {
			return ts.transform(getOutTransformParam(), value);
		} else {
			return value;
		}
	}

	public String transformUpdate(String value) throws TransformationException {
		return transformOut(value);
	}

	public abstract String transformCommand(Command command)
			throws TransformationException;

	// public abstract TransformationService getOutTransformationService();

	/**
	 * Implementation class intended to be overridden by sub-classes when they
	 * have a different "format" of output for toProperty()
	 */
	protected void initialize() {
		StringBuilder result = new StringBuilder(100);
		String tmp;
		int i = getId();
		String id = ((i == 0) ? "" : String.valueOf(i));
		String stuff = getStuff();
		String unit = getUnitName();

		// Normalize the Default MiOS Unit name, so it doesn't appear in the
		// property String.
		unit = (unit == null) ? "" : "unit:" + unit + ',';

		if (stuff != null) {
			result.append(unit).append(getType()).append(':').append(id)
					.append('/').append(stuff);
		} else {
			result.append(unit).append(getType()).append(':').append(id);
		}

		cachedProperty = result.toString();

		tmp = getCommandThing();
		if (tmp != null) {
			result.append(",command:").append(tmp);
		}

		tmp = getInTransform();
		if (tmp != null) {
			result.append(",in:").append(tmp);
		}

		tmp = getOutTransform();
		if (tmp != null) {
			result.append(",out:").append(tmp);
		}

		cachedBinding = result.toString();
	};

	public String getCommandThing() {
		return commandThing;
	};

	/**
	 * So we can use it in logger.*() calls with deferred evaluation.
	 * */
	public String toString() {
		return toBinding();
	}

	/**
	 * Validate the Item's data type against the BindingConfig.
	 * 
	 * If this validation fails, then a BindingConfigParseException is thrown.
	 * By default, this method will succeed if the Item is a StringItem,
	 * NumberItem, SwitchItem, DimmerItem, ContactItem.
	 * 
	 * Subclasses can augment this behavior or replace it, so that Bindings can
	 * reconfigure their support for Item Types.
	 * 
	 * @param item
	 *            the Item that requiring validation.
	 * @exception BindingConfigParseException
	 * */
	public void validateItemType(Item item) throws BindingConfigParseException {
		if (!(item instanceof StringItem) && !(item instanceof SwitchItem)
				&& !(item instanceof DimmerItem)
				&& !(item instanceof NumberItem)
				&& !(item instanceof ContactItem)
				&& !(item instanceof DateTimeItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', but only String, Switch, Dimmer and Number items are allowed.");
		}
	}
}
