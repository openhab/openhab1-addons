/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Dan Cunningham
 * @since 1.4.0
 */
public interface OmniLinkBindingProvider extends BindingProvider {

	public Item getItem(String itemName);
	
	public OmniLinkBindingConfig getOmniLinkBindingConfig(String itemName);
	
	
}
