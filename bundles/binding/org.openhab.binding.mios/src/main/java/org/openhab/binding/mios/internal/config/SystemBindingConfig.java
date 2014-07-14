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
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class SystemBindingConfig extends MiosBindingConfig {

	private SystemBindingConfig(String context, String unitName, String stuff,
			Class<? extends Item> itemType, String inTransform,
			String outTransform) {
		super(context, unitName, 0, stuff, itemType, null, null, inTransform,
				outTransform);
	}

	public static final MiosBindingConfig create(String context, String unitName,
			String stuff, Class<? extends Item> itemType, String inTransform,
			String outTransform) throws BindingConfigParseException {
		MiosBindingConfig c = new SystemBindingConfig(context, unitName,
				stuff, itemType, inTransform, outTransform);
		
		c.initialize();
		return c;
	}

	@Override
	public String getType() {
		return "system";
	}
}
