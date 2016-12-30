/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Lehane Kellett
 * @since 1.9.0
 */
public interface PLCLogoBindingProvider extends BindingProvider {
    public PLCLogoBindingConfig getBindingConfig(String itemName);

    /**
     * Returns the item identified by {@code itemName}
     *
     * @param itemName the name of the item to find
     * @return item identified by {@code itemName}
     */
    public Item getItem(String itemName);

}
