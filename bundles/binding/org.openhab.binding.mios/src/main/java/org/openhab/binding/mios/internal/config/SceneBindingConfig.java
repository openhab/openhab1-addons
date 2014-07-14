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

	private SceneBindingConfig(String context, String unitName, int id,
			String stuff, Class<? extends Item> itemType, String outType,
			String outStuff, String inTransform, String outTransform) {
		super(context, unitName, id, stuff, itemType, outType, outStuff,
				inTransform, outTransform);
	}

	public static final MiosBindingConfig create(String context,
			String unitName, int id, String stuff,
			Class<? extends Item> itemType, String outType, String outStuff,
			String inTransform, String outTransform)
			throws BindingConfigParseException {
		MiosBindingConfig c = new SceneBindingConfig(context, unitName, id,
				stuff, itemType, outType, outStuff, inTransform, outTransform);

		c.initialize();
		return c;
	}

	@Override
	public String getType() {
		return "scene";
	}

	public String transformCommand(Command command)
			throws TransformationException {
		String key = command.toString();

		// TODO: Don't do this on the fly...
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
