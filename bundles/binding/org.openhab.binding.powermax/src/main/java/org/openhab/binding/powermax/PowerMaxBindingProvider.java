/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface for the powermax binding
 *
 * @author lolodomo
 * @since 1.9.0
 */
public interface PowerMaxBindingProvider extends BindingProvider {

    /**
     * Returns the binding configuration for the specified item name
     *
     * @param itemName: name of the item
     * @return the binding configuration.
     */
    public PowerMaxBindingConfig getConfig(String itemName);

}
