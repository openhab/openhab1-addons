/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An item map for SOAP services which only return a single item command value per response.
 * This item map is more flexible than the {@link MultiItemMap} as it supports
 * a custom {@link #getSoapValueParser() value parser} and
 * {@link ParametrizedItemMap read} and {@link WritableItemMap write parameters}.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public class SingleItemMap extends AbstractItemMap implements ParametrizedItemMap, WritableItemMap {

    private static final Logger logger = LoggerFactory.getLogger(SingleItemMap.class);

    // common parameters
    private final String _itemCommand; // matches itemconfig

    private final String _itemArgumentName; // name of parameter to extract from fbox soap response when reading value
                                            // (is parsed as value)

    // write specific
    private String _writeServiceCommand; // command to execute on fbox if value should be set

    private final List<String> _configArgumentNames;

    public SingleItemMap(String _itemCommand, String _readServiceCommand, String _serviceId, String _configArgumentName,
            String _itemArgumentName) {
        this(_itemCommand, _readServiceCommand, _serviceId, _configArgumentName, _itemArgumentName,
                new SoapValueParser());
    }

    public SingleItemMap(String _itemCommand, String _readServiceCommand, String _serviceId, String _configArgumentName,
            String _itemArgumentName, SoapValueParser _soapValueParser) {
        super(_readServiceCommand, _serviceId, _soapValueParser);
        this._itemCommand = _itemCommand;
        this._configArgumentNames = _configArgumentName == null ? emptyList() : singletonList(_configArgumentName);
        this._itemArgumentName = _itemArgumentName;
    }

    @Override
    public String getWriteServiceCommand() {
        return _writeServiceCommand;
    }

    public void setWriteServiceCommand(String _writeServiceCommand) {
        this._writeServiceCommand = _writeServiceCommand;
    }

    public String getItemCommand() {
        return _itemCommand;
    }

    @Override
    public Set<String> getItemCommands() {
        return singleton(_itemCommand);
    }

    public String getItemArgumentName() {
        return _itemArgumentName;
    }

    @Override
    public String getItemArgumentName(String itemCommand) {
        if (!Objects.equals(itemCommand, _itemCommand)) {
            throw new IllegalArgumentException("unsupported itemCommand " + itemCommand);
        }
        return _itemArgumentName;
    }

    @Override
    public List<InputArgument> getConfigInputArguments(ItemConfiguration config) {
        List<String> parameterValues = config.getArgumentValues();
        if (_configArgumentNames.size() != parameterValues.size()) {
            logger.warn("item command {} requires {} parameters but {} parameters were configured",
                    config.getItemCommand(), _configArgumentNames.size(), parameterValues.size());
            return emptyList();
        }

        if (_configArgumentNames.size() == 0) {
            return emptyList();
        }

        List<InputArgument> inputValues = new ArrayList<>(_configArgumentNames.size());
        Iterator<String> names = _configArgumentNames.iterator();
        Iterator<String> values = parameterValues.iterator();
        while (names.hasNext()) {
            inputValues.add(new InputArgument(names.next(), values.next()));
        }

        return inputValues;
    }

    @Override
    public InputArgument getWriteInputArgument(Command cmd) {
        // convert String command into numeric
        assert cmd instanceof OnOffType : "unsupported Command type " + cmd.getClass().getName();
        String value = cmd.toString().equalsIgnoreCase("on") ? "1" : "0";
        return new InputArgument(_itemArgumentName, value);
    }

}
