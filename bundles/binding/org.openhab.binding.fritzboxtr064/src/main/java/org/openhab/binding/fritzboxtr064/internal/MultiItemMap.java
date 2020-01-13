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
package org.openhab.binding.fritzboxtr064.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * {@link ItemMap} for a FritzBox SOAP service which returns the values of multiple items in
 * a single service response. The value of each {@link #getItemCommands() configured command} is
 * read from one {@link #getItemArgumentName(String)} XML element in the service response by
 * a {@link SoapValueParser}.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public class MultiItemMap extends AbstractItemMap {

    private Set<String> _itemCommands;
    private Function<String, String> _itemCommandToDataOutName;

    public MultiItemMap(Collection<String> _itemCommands, String _readServiceCommand, String _serviceId,
            Function<String, String> _itemCommandToDataOutName) {
        super(_readServiceCommand, _serviceId, new SoapValueParser());
        this._itemCommands = new HashSet<String>(_itemCommands);
        this._itemCommandToDataOutName = _itemCommandToDataOutName;
    }

    @Override
    public Set<String> getItemCommands() {
        return _itemCommands;
    }

    @Override
    public String getItemArgumentName(String itemCommand) {
        if (!_itemCommands.contains(itemCommand)) {
            throw new IllegalArgumentException("unsupported itemCommand " + itemCommand);
        }
        return _itemCommandToDataOutName.apply(itemCommand);
    }

}
