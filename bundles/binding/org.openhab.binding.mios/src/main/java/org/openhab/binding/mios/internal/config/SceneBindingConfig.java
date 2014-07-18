/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal.config;

import org.openhab.core.items.Item;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class SceneBindingConfig extends MiosBindingConfig {

	private SceneBindingConfig(String context, String itemName,
			String unitName, int id, String stuff,
			Class<? extends Item> itemType, String commandThing,
			String inTransform, String outTransform) {
		super(context, itemName, unitName, id, stuff, itemType, commandThing,
				inTransform, outTransform);
	}

	public static final MiosBindingConfig create(String context,
			String itemName, String unitName, int id, String stuff,
			Class<? extends Item> itemType, String commandThing,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		MiosBindingConfig c = new SceneBindingConfig(context, itemName,
				unitName, id, stuff, itemType, commandThing, inTransform,
				outTransform);

		c.initialize();
		return c;
	}

	@Override
	public String getType() {
		return "scene";
	}

	@Override
	public String transformCommand(Command command)
			throws TransformationException {
		String key = command.toString();

		String cThing = getCommandThing();
		if (cThing == null || "".equals(cThing)) {
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
