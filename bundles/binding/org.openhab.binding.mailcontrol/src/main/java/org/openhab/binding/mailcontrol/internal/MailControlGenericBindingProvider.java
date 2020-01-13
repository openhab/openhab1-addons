/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
public class MailControlGenericBindingProvider extends AbstractGenericBindingProvider
        implements MailControlBindingProvider {
    /**
     * {@inheritDoc}
     */
    @Override
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
