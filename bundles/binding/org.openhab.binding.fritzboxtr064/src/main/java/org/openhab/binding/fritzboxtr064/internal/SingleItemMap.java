/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import static java.util.Collections.singleton;

import java.util.Objects;
import java.util.Set;

/**
 * An item map for SOAP services which only return a single item command value per response.
 * This item map is more flexible than the {@link MultiItemMap} as it supports an optional
 * {@link #getReadDataInName() input parameter}, a custom {@link #getSoapValueParser() value parser}
 * and {@link WritableItemMap writable commands}.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public class SingleItemMap extends AbstractItemMap implements ParametrizedItemMap, WritableItemMap {
    // common parameters
    private final String _itemCommand; // matches itemconfig

    // read specific
    private final String _readDataInName; // name of parameter to put in soap request to read value
    private String _readDataOutName; // name of parameter to extract from fbox soap response when reading value (is
                                     // parsed as value)

    // write specific
    private String _writeServiceCommand; // command to execute on fbox if value should be set
    private String _writeDataInName; // name of parameter which is put in soap request when setting an option on fbox
    private String _writeDataInNameAdditional; // additional Parameter to add to write request. e.g. id of TAM to set

    public SingleItemMap(String _itemCommand, String _readServiceCommand, String _serviceId, String _getDataInName1,
            String _getDataOutName1) {
        this(_itemCommand, _readServiceCommand, _serviceId, _getDataInName1, _getDataOutName1, new SoapValueParser());
    }

    public SingleItemMap(String _itemCommand, String _readServiceCommand, String _serviceId, String _getDataInName1,
            String _getDataOutName1, SoapValueParser _soapValueParser) {
        super(_readServiceCommand, _serviceId, _soapValueParser);
        this._itemCommand = _itemCommand;
        _readDataInName = _getDataInName1;
        _readDataOutName = _getDataOutName1;
    }

    @Override
    public String getReadDataInName() {
        return _readDataInName;
    }

    @Override
    public String getWriteDataInNameAdditional() {
        return _writeDataInNameAdditional;
    }

    public void setWriteDataInNameAdditional(String _writeDataInNameAdditional) {
        this._writeDataInNameAdditional = _writeDataInNameAdditional;
    }

    @Override
    public String getWriteServiceCommand() {
        return _writeServiceCommand;
    }

    public void setWriteServiceCommand(String _writeServiceCommand) {
        this._writeServiceCommand = _writeServiceCommand;
    }

    @Override
    public String getWriteDataInName() {
        return _writeDataInName;
    }

    public void setWriteDataInName(String _setDataInName) {
        this._writeDataInName = _setDataInName;
    }

    public String getItemCommand() {
        return _itemCommand;
    }

    @Override
    public Set<String> getItemCommands() {
        return singleton(_itemCommand);
    }

    public String getReadDataOutName() {
        return _readDataOutName;
    }

    @Override
    public String getReadDataOutName(String itemCommand) {
        if (!Objects.equals(itemCommand, _itemCommand)) {
            throw new IllegalArgumentException("unsupported itemCommand " + itemCommand);
        }
        return _readDataOutName;
    }
}
