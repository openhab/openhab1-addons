/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.ItemRegistry;

/**
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public interface MailControlBindingProvider extends BindingProvider {
    /**
     * Gets the ItemRegistry (catalog) used by this BindingProvider.
     * 
     * The {@code ItemRegistry} is injected into the {@code MailControlBindingProvider}
     * through OSGi configuration. This method provider read-only access to the
     * {@code ItemRegistry}, which is the catalog of Items in use by this
     * system.
     * 
     * @return the ItemRegistry associated with this BindingProvider.
     */
    public ItemRegistry getItemRegistry();
}
