/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upb;

import org.openhab.binding.upb.internal.UPBBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Interface for the {@link BindingProvider} for the UPB binding.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public interface UPBBindingProvider extends BindingProvider {

    /**
     * Gets the configuration of an item.
     *
     * @param itemName
     *            the name of the item.
     * @return the {@link UPBBindingConfig} for the given item or null if one
     *         does not exist.
     */
    UPBBindingConfig getConfig(String itemName);
}
