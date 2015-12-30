/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mios.internal.MiosActivator;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MiOS specific abstract class for the openHAB {@link BindingConfig}.
 * <p>
 * 
 * Sub-classes of this class represent the various "types" of data that can be bound within a MiOS System.
 * <p>
 * These include the following objects: <br>
 * <ul>
 * <li>System - {@link SystemBindingConfig}
 * <li>Scene - {@link SceneBindingConfig}
 * <li>Device - {@link DeviceBindingConfig}
 * </ul>
 * <p>
 * 
 * The general form of a MiOS Binding is:
 * <p>
 * <ul>
 * <li>
 * <tt>mios="unit:<i>unitName</i>,<i>miosThing</i>{,command:<i>commandTransform</i>}{,in:<i>inTransform</i>}{,out:<i>outTransform</i>}"</tt>
 * </ul>
 * <p>
 * 
 * Each sub-class has a specific format for the <i>miosThing</i>, and examples are outlined in those sub-classes, in
 * addition to the <code>README.md</code> file shipped with the binding.
 * 
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public abstract class MiosBindingConfig implements BindingConfig {
	private static Pattern TRANSFORM_PATTERN = Pattern.compile("(?<transform>[a-zA-Z]+)[(]{1}?(?<param>.*)[)]{1}");

	protected static final Logger logger = LoggerFactory.getLogger(MiosBindingConfig.class);

	private int miosId;
	private String context;
	private String itemName;
	private String unitName;
	private String miosStuff;
	private String cachedProperty;
	private String cachedBinding;
	private Class<? extends Item> itemType;
	private String commandTransform;
	private String inTransform;
	private String outTransform;

	private String inTransformName = null;
	private String inTransformParam = null;

	private String outTransformName = null;
	private String outTransformParam = null;

	private TransformationService inTransformationService;
	private TransformationService outTransformationService;

	protected MiosBindingConfig(String context, String itemName, String unitName, int miosId, String miosStuff,
			Class<? extends Item> itemType, String commandTransform, String inTransform, String outTransform)
			throws BindingConfigParseException {
		// Crude parameter validations
		assert (miosId >= 0);

		this.context = context;
		this.itemName = itemName;
		this.unitName = unitName;
		this.miosId = miosId;
		this.miosStuff = miosStuff;
		this.itemType = itemType;
		this.commandTransform = commandTransform;
		this.inTransform = inTransform;
		this.outTransform = outTransform;

		Matcher m;
		if (inTransform != null) {
			m = TRANSFORM_PATTERN.matcher(inTransform);

			if (m.matches()) {
				this.inTransformName = m.group("transform");
				this.inTransformParam = m.group("param");

				logger.trace("start: IN '{}', '{}'", this.inTransformName, this.inTransformParam);
			} else {
				logger.warn("Unable to parse (in) Transformation string '{}', ignored.", inTransform);
			}
		}

		if (outTransform != null) {
			m = TRANSFORM_PATTERN.matcher(outTransform);

			if (m.matches()) {
				this.outTransformName = m.group("transform");
				this.outTransformParam = m.group("param");

				logger.trace("start: OUT '{}', '{}'", this.outTransformName, this.outTransformParam);
			} else {
				logger.warn("Unable to parse (out) Transformation string '{}', ignored.", outTransform);
			}
		}
	}

	/**
	 * The name of the openHAB context.
	 * 
	 * @return the name of the openHAB context this Binding Configuration is associated with.
	 */
	public String getContext() {
		return this.context;
	}

	/**
	 * The name of the openHAB Item.
	 * 
	 * @return the name of the Item this Binding Configuration is associated with.
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * The name of the MiOS Unit.
	 * <p>
	 * 
	 * @return the name of the MiOS Unit this Binding Configuration is associated with.
	 */
	public String getUnitName() {
		return unitName;
	};

	/**
	 * The type of MiOS object.
	 * <p>
	 * 
	 * The MiOS Binding supports binding to Device, Scene, and System objects within a MiOS System. This method returns
	 * which type of MiOS object the Binding Configuration represents, using the same format/value used in the Binding
	 * Configuration entry.
	 * <p>
	 * The value returned is dependent upon the specific sub-class of MiOSBindingConfig materialized from the Item
	 * configuration information.
	 * 
	 * @return the type of MiOS object.
	 */
	public abstract String getMiosType();

	/**
	 * The id of the MiOS object.
	 * <p>
	 * 
	 * Each object within a MiOS Unit has a type-specific Id. If the type of object doesn't have an Id, then the value
	 * will be 0.
	 * 
	 * @return the Id of the MiOS object, or 0 if the type doesn't allocate Id's.
	 */
	public int getMiosId() {
		return miosId;
	};

	/**
	 * A type-specific Name/Reference representing a named subcomponent of the MiOS object.
	 * <p>
	 * 
	 * Each object within a MiOS Unit has a type-specific Name/Reference string. This value works in conjunction with
	 * the MiOS Id, to further define the data to be returned. One "id" in a MiOS system may have multiple attributes
	 * associated with it, and this string is used to disambiguate those attributes.
	 * 
	 * @return the Name/Reference of the MiOS object represented by this Binding Configuration.
	 */
	public String getMiosStuff() {
		return miosStuff;
	}

	/**
	 * Convert to a Property string.
	 * <p>
	 * 
	 * This form includes the MiOS Device locator information (Id, Type, and Stuff) only.
	 * <p>
	 * It contains only enough information to identify the target component within the MiOS Unit.
	 * 
	 * @return the Binding Configuration as a string, suitable for use within openHAB Configuration files.
	 */
	public String toProperty() {
		return cachedProperty;
	}

	/**
	 * Convert to a full Binding string.
	 * <p>
	 * 
	 * This form includes the MiOS Device locator information, from <code>toProperty()</code>, in addition to any
	 * transformations ( <code>in:</code>, <code>out:</code>, <code>command:</code>) specified in the original Binding.
	 */
	public String toBinding() {
		return cachedBinding;
	}

	protected Class<? extends Item> getItemType() {
		return itemType;
	}

	private String getInTransform() {
		return inTransform;
	}

	private String getInTransformName() {
		return inTransformName;
	}

	private String getInTransformParam() {
		return inTransformParam;
	}

	private String getOutTransform() {
		return outTransform;
	}

	private String getOutTransformName() {
		return outTransformName;
	}

	private String getOutTransformParam() {
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

		inTransformationService = TransformationHelper.getTransformationService(context, name);

		if (inTransformationService == null) {

			logger.warn("Transformation Service (in) '{}' not found for declaration '{}'.", name, getInTransform());
		}

		return inTransformationService;
	}

	/**
	 * Returns a {@link State} which is inherited from the {@link Item}s accepted DataTypes. The call is delegated to
	 * the {@link TypeParser}. If <code>item</code> is <code>null</code> the {@link StringType} is used.
	 * <p>
	 * 
	 * Code borrowed from {@link HttpBinding}.
	 * 
	 * @param itemType
	 * @param value
	 * 
	 * @return a {@link State} which type is inherited by the {@link TypeParser} or a {@link StringType} if
	 *         <code>item</code> is <code>null</code>
	 */
	private State createState(String value) {
		Class<? extends Item> itemType = getItemType();
		State result;
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				//
				// For things like Weather Items when they're bound to
				// NumberItems.
				//
				// eg. Heat Index
				if ("".equals(value)) {
					result = UnDefType.NULL;
				} else {
					result = DecimalType.valueOf(value);
				}
			} else if (itemType.isAssignableFrom(ContactItem.class)) {
				result = OpenClosedType.valueOf(value);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				result = OnOffType.valueOf(value);
			} else if (itemType.isAssignableFrom(DimmerItem.class)) {
				result = PercentType.valueOf(value);
			} else if (itemType.isAssignableFrom(RollershutterItem.class)) {
				result = PercentType.valueOf(value);
			} else if (itemType.isAssignableFrom(DateTimeItem.class)) {
				try {
					//
					// If we're presented with the empty string, then consider
					// it to be the Undefined NULL value.
					//
					// This has been observed during Full-updates from a MiOS
					// unit that has Leviton Scene Controllers present
					// (LastUpdated).
					//
					if ("".equals(value)) {
						result = UnDefType.NULL;
					} else {
						//
						// See if it "looks" like an Epoch-style date. MiOS
						// Units return these as String/Integer versions of the
						// date and they need to be converted.
						//
						// This logic really belongs inside the OH 1.x core
						// class DateTimeType, but that's closed to changes...
						// doing it here also avoids the thread-safety issues
						// present in the DateTimeType class.
						//
						long l = Long.parseLong(value) * 1000;
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(l);
						result = new DateTimeType(c);
					}
				} catch (NumberFormatException nfe) {
					result = DateTimeType.valueOf(value);
				}
			} else if (itemType.isAssignableFrom(ColorItem.class)) {
				result = HSBType.valueOf(value);
			} else {
				result = StringType.valueOf(value);
			}

			logger.trace("createState: Converted '{}' to '{}', bound to '{}'", new Object[] { value,
					result.getClass().getName(), itemType });

			return result;
		} catch (Exception e) {
			logger.debug("Couldn't create state of type '{}' for value '{}'", itemType, value);
			return StringType.valueOf(value);
		}
	}

	/**
	 * Transform data from a MiOS Unit into a form suitable for use in an openHAB Item.
	 * <p>
	 * 
	 * Data received from a MiOS unit is in a number of different formats (String, Boolean, DataTime, etc). These values
	 * may need to be transformed from their original format prior to being pushed into the corresponding openHAB Item.
	 * <p>
	 * This method is used internally within the MiOS Binding to perform that transformation.
	 * <p>
	 * This method is responsible for transforming the inbound value from the MiOS Unit, into the form required by the
	 * openHAB Item.
	 * <p>
	 * metadata supplied by the user, via the <code>in:</code> parameter, in the Binding Configuration is used to define
	 * the transformation that must be performed.
	 * <p>
	 * If the <code>in:</code> parameter is missing, then no transformation will occur, and the source-value will be
	 * returned (as a <code>StringType</code>).
	 * <p>
	 * If the <code>in:</code> parameter is present, then it's value is used to determine which openHAB
	 * TransformationService should be used to transform the value.
	 * 
	 * @return the transformed value, or the input (<code>value</code>) if no transformation has been specified in the
	 *         Binding Configuration.
	 * 
	 * @throws TransformationException
	 *             if the underlying Transformation fails in any manner.
	 */
	public State transformIn(State value) throws TransformationException {
		TransformationService ts = getInTransformationService();

		if (ts != null) {
			return createState(ts.transform(getInTransformParam(), value.toString()));
		} else {
			if (value instanceof StringType) {
				value = createState(value.toString());

				logger.trace("transformIn: Converted value '{}' from StringType to more scoped type '{}'", value,
						value.getClass());
			}

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

		outTransformationService = TransformationHelper.getTransformationService(context, name);

		if (outTransformationService == null) {

			logger.warn("Transformation Service (out) '{}' not found for declaration '{}'.", name, getOutTransform());
		}

		return outTransformationService;
	}

	/**
	 * Transform data in an openHAB Item into a form suitable for use in calls made to a MiOS Unit.
	 * <p>
	 * 
	 * In order to calls a MiOS Unit, we may need to transform an Item's current value from it's openHAB State to a form
	 * suitable for transmission to remote MiOS Unit.
	 * <p>
	 * This method is responsible for transforming an Item's State value, using metadata supplied by the user, via the
	 * <code>out:</code> parameter, in the Binding Configuration.
	 * <p>
	 * If the <code>out:</code> parameter is missing, then no transformation will occur, and the source-value will be
	 * returned.
	 * <p>
	 * If the <code>out:</code> parameter is present, then it's value is used to determine which openHAB
	 * TransformationService should be used to transform the value.
	 * 
	 * @return the transformed value, or the input (<code>value</code>) if no transformation has been specified in the
	 *         Binding Configuration.
	 * 
	 * @throws TransformationException
	 *             if the underlying Transformation fails in any manner.
	 */
	public State transformOut(State value) throws TransformationException {
		TransformationService ts = getOutTransformationService();

		if (ts != null) {
			return createState(ts.transform(getOutTransformParam(), value.toString()));
		} else {
			return value;
		}
	}

	/**
	 * Transform an openHAB {@link Command} into a call suitable for invoking a service on a MiOS Unit.
	 * <p>
	 * 
	 * In order to call a MiOS Unit, we need the openHAB {@link Command} in the format they understand. This method is
	 * responsible for transforming the Command, using the metadata supplied by the user in the <code>command:</code>
	 * parameter of the Binding Configuration. Internally, this method uses that metadata to perform a transformation,
	 * using the openHAB Transformation service, to perform the mapping.
	 * <p>
	 * If the <code>command:</code> parameter is missing, then no commands can be delivered to the MiOS Unit, and
	 * <code>null</code> is returned.
	 * <p>
	 * If the <code>command:</code> parameter is present, then the associated openHAB Transformation Service is invoked
	 * to perform the required mapping, and the transformed value is returned.
	 * 
	 * @return the transformed value, or null if no transformation has been specified in the Binding Configuration.
	 * 
	 * @throws TransformationException
	 *             if the underlying Transformation fails in any manner.
	 */
	public abstract String transformCommand(Command command) throws TransformationException;

	/**
	 * Implementation method to be overridden by sub-classes when they have a different "format" of output for
	 * <code>toProperty()</code>.
	 * <p>
	 */
	protected void initialize() {
		StringBuilder result = new StringBuilder(100);
		String tmp;
		int i = getMiosId();
		String id = ((i == 0) ? "" : String.valueOf(i));
		String stuff = getMiosStuff();
		String unit = getUnitName();

		// Normalize the Default MiOS Unit name, so it doesn't appear in the
		// property String.
		unit = (unit == null) ? "" : "unit:" + unit + ',';

		if (stuff != null) {
			result.append(unit).append(getMiosType()).append(':').append(id).append('/').append(stuff);
		} else {
			result.append(unit).append(getMiosType()).append(':').append(id);
		}

		cachedProperty = result.toString();

		tmp = getCommandTransform();
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

	/**
	 * Return the Command Transformation binding parameter.
	 * <p>
	 * 
	 * If the Binding configuration has the <code>command:</code> parameter specified, then return that value. Otherwise
	 * return <code>null</code>.
	 * 
	 * @return the value of the <code>command:</code> Binding configuration parameter, or <code>null</code> if not
	 *         specified.
	 */
	protected String getCommandTransform() {

		return commandTransform;
	};

	/**
	 * Return the full Binding String.
	 */
	public String toString() {
		return toBinding();
	}

	/**
	 * Validate the Item's data type against the <code>BindingConfig</code>.
	 * <p>
	 * 
	 * If this validation fails, then a <code>BindingConfigParseException</code> is thrown. By default, this method will
	 * succeed if the Item is a <code>StringItem</code>, <code>NumberItem</code>, <code>SwitchItem</code> ,
	 * <code>DimmerItem</code>, <code>ContactItem</code>, <code>DataTimeItem</code>.
	 * <p>
	 * Subclasses can augment this behavior or replace it, so that Bindings can reconfigure their support for Item
	 * Types.
	 * 
	 * @param item
	 *            the Item that requiring validation.
	 * @exception BindingConfigParseException
	 *                when the validation of the <code>BindingConfig</code> fails for the Item.
	 */
	public void validateItemType(Item item) throws BindingConfigParseException {
		Class<? extends Item> t = getItemType();

		if (!((t == StringItem.class) || (t == SwitchItem.class) || (t == DimmerItem.class) || (t == NumberItem.class)
				|| (t == ContactItem.class) || (t == DateTimeItem.class) || (t == RollershutterItem.class))) {

			throw new BindingConfigParseException(
					String.format(
							"Item %s is of type %s, but only String, Switch, Dimmer, Number, Contact, DataTime, and Rollershutter Items are allowed.",
							item.getName(), item.getClass().getSimpleName()));

		}
	}
}
