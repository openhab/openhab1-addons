/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

/***
 * Abstract base implementation of an {@link ItemMap}.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public abstract class AbstractItemMap implements ItemMap {
    // common parameters
    private String _serviceId; // SOAP service ID

    // read specific
    private final String _readServiceCommand; // command to execute on fbox if value should be read

    private final SoapValueParser _soapValueParser;

    public AbstractItemMap(String _readServiceCommand, String _serviceId, SoapValueParser _soapValueParser) {
        this._readServiceCommand = _readServiceCommand;
        this._serviceId = _serviceId;
        this._soapValueParser = _soapValueParser;
    }

    @Override
    public String getServiceId() {
        return _serviceId;
    }

    public void setServiceId(String _serviceId) {
        this._serviceId = _serviceId;
    }

    @Override
    public String getReadServiceCommand() {
        return _readServiceCommand;
    }

    @Override
    public SoapValueParser getSoapValueParser() {
        return _soapValueParser;
    }
}
