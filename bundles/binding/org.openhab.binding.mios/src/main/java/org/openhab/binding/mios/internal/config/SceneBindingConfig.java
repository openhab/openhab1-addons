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
 * A BindingConfig object targeted at MiOS Scene Attributes.
 * 
 * The scene-specific form of a MiOS Binding is:<br>
 * <ul>
 * <li><nobr>
 * <tt>mios="unit:<i>unitName</i>,scene:<i>sceneId</i>/<i>attrName</i>,<i>optionalTransformations</i></tt>
 * </nobr>
 * </ul>
 * <p>
 * 
 * Example Item declarations:
 * <p>
 * <p>
 * <ul>
 * <li>
 * <code><nobr>String   SceneAlarmArrivingDisarmed "Alarm Arriving Disarmed Scene" <sofa> (GScene) {mios="unit:house,scene:2/status,command:,in:MAP(miosStatusIn.map)", autoupdate="false"}</nobr></code>
 * <li>
 * <code><nobr>Contact  SceneAlarmArrivingDisarmedActive "Active [%s]" <sofa> {mios="unit:house,scene:2/active,in:MAP(miosSceneActiveIn.map)"}</nobr></code>
 * </ul>
 * <p>
 * 
 * <b>Optional Transformations</b>
 * <p>
 * See the Optional Transformations section of the JavaDoc for
 * {@link DeviceBindingConfig} for a description of the main options here.
 * 
 * The one addition is that Scene Bindings also support:
 * <ul>
 * <li><code>command:</code> - map <i>any</i> openHAB Command sent to the Item,
 * and use it to trigger Scene execution.
 * <li><code>command:ON</code> - map <i>only</i> the <code>ON</code> Command
 * sent to the Item, and use it to trigger Scene execution ("<code>ON</code>"
 * can be any openHAB Command string)
 * </ul>
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class SceneBindingConfig extends MiosBindingConfig {

	private static HashMap<String, ParameterDefaults> paramDefaults = new HashMap<String, ParameterDefaults>();
	private static String PARAM_DEFAULTS = "org/openhab/binding/mios/internal/config/SceneDefaults.properties";

	static {
		ClassLoader cl = SceneBindingConfig.class.getClassLoader();
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
					"Successfully loaded Scene Parameter defaults from '{}', entries '{}'",
					PARAM_DEFAULTS, paramDefaults.size());
		} catch (Exception e) {
			// Pre-shipped with the Binding, so it should never error out.
			logger.error(
					"Failed to load Scene Parameter defaults file '{}', Exception",
					PARAM_DEFAULTS, e);
		}
	}

	private SceneBindingConfig(String context, String itemName,
			String unitName, int id, String stuff,
			Class<? extends Item> itemType, String commandTransform,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		super(context, itemName, unitName, id, stuff, itemType,
				commandTransform, inTransform, outTransform);
	}

	/**
	 * Static constructor-method.
	 * 
	 * @return an initialized MiOS Scene Binding Configuration object.
	 */
	public static final MiosBindingConfig create(String context,
			String itemName, String unitName, int id, String stuff,
			Class<? extends Item> itemType, String commandTransform,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		ParameterDefaults pd = paramDefaults.get(stuff);
		if (pd != null) {
			logger.trace("Scene ParameterDefaults FOUND '{}' for '{}', '{}'",
					itemName, stuff, pd);
			if (commandTransform == null) {
				commandTransform = pd.getCommandTransform();
				logger.trace(
						"Scene ParameterDefaults '{}' defaulted command: to '{}'",
						itemName, commandTransform);
			}
			if (inTransform == null) {
				inTransform = pd.getInTransform();
				logger.trace(
						"Scene ParameterDefaults '{}' defaulted in: to '{}'",
						itemName, inTransform);
			}
			if (outTransform == null) {
				outTransform = pd.getOutTransform();
				logger.trace(
						"Scene ParameterDefaults '{}' defaulted out: to '{}'",
						itemName, outTransform);
			}
		} else {
			logger.trace("Scene ParameterDefaults NOT FOUND '{}' for '{}'",
					itemName, stuff);
		}

		MiosBindingConfig c = new SceneBindingConfig(context, itemName,
				unitName, id, stuff, itemType, commandTransform, inTransform,
				outTransform);

		c.initialize();
		return c;
	}

	/**
	 * Returns the value "<code>scene</code>".
	 * 
	 * @return the value "<code>scene</code>"
	 */
	@Override
	public String getMiosType() {
		return "scene";
	}

	@Override
	public String transformCommand(Command command)
			throws TransformationException {
		String key = command.toString();

		String cThing = getCommandTransform();
		if (cThing == null) {
			return null;
		} else if ("".equals(cThing)) {
			return key;
		} else {
			String[] commands = cThing.split("\\|");

			for (int i = 0; i < cThing.length(); i++) {
				if (key.equals(commands[i])) {
					return key;
				}
			}
		}

		return null;
	}
}
