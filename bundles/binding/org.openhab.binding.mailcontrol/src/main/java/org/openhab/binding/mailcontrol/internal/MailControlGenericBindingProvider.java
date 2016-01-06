/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.internal;

import org.openhab.binding.mailcontrol.MailControlBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
public class MailControlGenericBindingProvider extends AbstractGenericBindingProvider implements MailControlBindingProvider {
    /**
     * {@inheritDoc}
     */
    public String getBindingType() {
        return "mailcontrol";
    }

    /**
     * Items of any type are allowed.
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
    }
}
