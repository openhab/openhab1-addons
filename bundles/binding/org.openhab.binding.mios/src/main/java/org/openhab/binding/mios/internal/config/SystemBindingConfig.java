/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openhab.core.items.Item;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * A BindingConfig object targeted at MiOS System Attributes.
 * 
 * The system-specific form of a MiOS Binding is:<br>
 * <ul>
 * <li><nobr> <tt>mios="unit:<i>unitName</i>,system:/<i>attrName</i></tt>
 * </nobr>
 * </ul>
 * <p>
 * 
 * Example Item declarations:
 * <p>
 * <p>
 * <ul>
 * <li>
 * <code><nobr>Number   SystemDataVersion "System Data Version [%d]" (GSystem) {mios="unit:house,system:/DataVersion"}</nobr></code>
 * <li>
 * <code><nobr>DateTime SystemLoadTime "System Load Time [%1$ta, %1$tm/%1$te %1$tR]" &lt;calendar&gt; (GSystem) {mios="unit:house,system:/LoadTime"}</nobr></code>
 * <li>
 * <code><nobr>String   SystemLocalTime "System Local Time [%s]" (GSystem) {mios="unit:house,system:/LocalTime"}</nobr></code>
 * <li>
 * <code><nobr>DateTime SystemTimeStamp "System Time Stamp [%1$ta, %1$tm/%1$te %1$tR]" &lt;calendar&gt; (GSystem) {mios="unit:house,system:/TimeStamp"}</nobr></code>
 * <li>
 * <code><nobr>Number   SystemUserDataDataVersion "System User Data Version [%d]" (GSystem) {mios="unit:house,system:/UserData_DataVersion"}</nobr></code>
 * <li>
 * <code><nobr>Number   SystemZWaveStatus "System ZWave Status [%d]" (GSystem) {mios="unit:house,system:/ZWaveStatus"}</nobr></code>
 * <li>
 * <code><nobr>String   SystemZWaveStatusString "System ZWave Status String [%d]" (GSystem) {mios="unit:house,system:/ZWaveStatus,in:MAP(miosZWaveStatusIn.map)"}</nobr></code>
 * </ul>
 * <p>
 * 
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class SystemBindingConfig extends MiosBindingConfig {

	private static HashMap<String, ParameterDefaults> paramDefaults = new HashMap<String, ParameterDefaults>();
	private static String PARAM_DEFAULTS = "org/openhab/binding/mios/internal/config/SystemDefaults.properties";

	static {
		ClassLoader cl = SystemBindingConfig.class.getClassLoader();
		InputStream input;

		input = cl.getResourceAsStream(PARAM_DEFAULTS);

		try {
			Properties tmp = new Properties();
			tmp.load(input);

			for (Map.Entry<Object, Object> e : tmp.entrySet()) {
				paramDefaults.put((String) e.getKey(),
						ParameterDefaults.parse((String) e.getValue()));
			}

			logger.debug(
					"Successfully loaded System Parameter defaults from '{}', entries '{}'",
					PARAM_DEFAULTS, paramDefaults.size());
		} catch (Exception e) {
			// Pre-shipped with the Binding, so it should never error out.
			logger.error(
					"Failed to load System Parameter defaults file '{}', Exception",
					PARAM_DEFAULTS, e);
		}
	}

	private SystemBindingConfig(String context, String itemName,
			String unitName, String stuff, Class<? extends Item> itemType,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		super(context, itemName, unitName, 0, stuff, itemType, null,
				inTransform, outTransform);
	}

	/**
	 * Static constructor-method.
	 * 
	 * @return an initialized MiOS System Binding Configuration object.
	 */
	public static final MiosBindingConfig create(String context,
			String itemName, String unitName, String stuff,
			Class<? extends Item> itemType, String inTransform,
			String outTransform) throws BindingConfigParseException {
		ParameterDefaults pd = paramDefaults.get(stuff);
		if (pd != null) {
			logger.trace("System ParameterDefaults FOUND '{}' for '{}', '{}'",
					itemName, stuff, pd);
			if (inTransform == null) {
				inTransform = pd.getInTransform();
				logger.trace(
						"System ParameterDefaults '{}' defaulted in: to '{}'",
						itemName, inTransform);
			}
			if (outTransform == null) {
				outTransform = pd.getOutTransform();
				logger.trace(
						"System ParameterDefaults '{}' defaulted out: to '{}'",
						itemName, outTransform);
			}
		} else {
			logger.trace("System ParameterDefaults NOT FOUND '{}' for '{}'",
					itemName, stuff);
		}

		MiosBindingConfig c = new SystemBindingConfig(context, itemName,
				unitName, stuff, itemType, inTransform, outTransform);

		c.initialize();
		return c;
	}

	/**
	 * Returns the value "<code>system</code>".
	 * 
	 * @return the value "<code>system</code>"
	 */
	@Override
	public String getMiosType() {
		return "system";
	}

	/**
	 * This method throws a {@link TransformationException}, as MiOS System
	 * attributes don't support being called.
	 * 
	 * @throws TransformationException
	 */
	@Override
	public String transformCommand(Command command)
			throws TransformationException {
		throw new TransformationException(
				"System attributes don't support Command Transformations");
	}
}
