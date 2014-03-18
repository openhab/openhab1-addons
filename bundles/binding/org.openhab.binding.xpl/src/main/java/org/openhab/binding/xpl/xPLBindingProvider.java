/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xpl;

import java.util.List;
import org.cdp1802.xpl.xPL_MessageI;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author clinique
 * @since 1.5.0
 */
public interface xPLBindingProvider extends BindingProvider {
	public xPLBindingConfig getConfig(String itemName);
	public List<String> hasMessage(xPL_MessageI theMessage);
	public Item getItem(String itemName);
}
