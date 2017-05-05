/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.followmemusic;

import org.openhab.binding.followmemusic.internal.FollowMeMusicGenericBindingProvider.FollowMeMusicBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author FollowMe
 * @since 0.1.0.dev
 */
public interface FollowMeMusicBindingProvider extends BindingProvider {
	
	/**
	 * Returns Modbus item configuration
	 * 
	 * @param itemName item name
	 * @return Modbus item configuration
	 */
	FollowMeMusicBindingConfig getConfig(String itemName);
	
}
