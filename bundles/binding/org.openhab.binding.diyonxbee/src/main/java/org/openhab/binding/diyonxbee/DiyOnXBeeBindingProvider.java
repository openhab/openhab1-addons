/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.diyonxbee;

import java.util.List;

import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.State;

/**
 * @author Jürgen Richtsfeld
 * @since 1.8
 */
public interface DiyOnXBeeBindingProvider extends AutoUpdateBindingProvider {

	String getId(String itemName);
	String getRemote(String itemName);
	boolean isSensor(String itemName);
	List<Class<? extends State>> getAvailableItemTypes(String itemName);
}
