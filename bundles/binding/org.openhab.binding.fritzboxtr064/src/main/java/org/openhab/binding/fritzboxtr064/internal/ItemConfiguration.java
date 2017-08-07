/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Configuration of an item from the binding config. Each configuration has an {@link #getItemCommand() item command}
 * which determines the value to read. For parametrisable commands the config may also have
 * a {@link #getDataInValue() data in value} and {@link #getAdditionalParameters() additional parameters}.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public class ItemConfiguration {
    private static final String PARAM_SEPARATOR = ":";

    private final String _itemCommand;

    private final Optional<String> _dataInValue;

    private final List<String> _additionalParameters;

    /**
     * Parses a configuration string from the binding configuration.
     * The command and parameters must be separated by colons (':').
     *
     * @param itemConfig Configuration string from the binding configuration.
     * @return The parsed configuration.
     */
    public static ItemConfiguration parse(String itemConfig) {
        String[] requestParts = itemConfig.split(PARAM_SEPARATOR);

        String itemCommand = requestParts[0];
        Optional<String> dataInValue;
        List<String> _additionalParameters;

        if (requestParts.length >= 2) {
            dataInValue = Optional.of(requestParts[1]);
        } else {
            dataInValue = Optional.empty();
        }

        if (requestParts.length > 2) {
            _additionalParameters = new ArrayList<>(requestParts.length - 2);
            for (int i = 2; i < requestParts.length; i++) {
                _additionalParameters.add(requestParts[i]);
            }
        } else {
            _additionalParameters = emptyList();
        }

        return new ItemConfiguration(itemCommand, dataInValue, _additionalParameters);
    }

    public ItemConfiguration(String _itemCommand) {
        this(_itemCommand, Optional.empty(), emptyList());
    }

    public ItemConfiguration(String _itemCommand, String _dataInValue) {
        this(_itemCommand, Optional.of(_dataInValue), emptyList());
    }

    public ItemConfiguration(String _itemCommand, Optional<String> _dataInValue, List<String> _additionalParameters) {
        this._itemCommand = _itemCommand;
        this._dataInValue = _dataInValue;
        this._additionalParameters = _additionalParameters;
    }

    public String getItemCommand() {
        return _itemCommand;
    }

    public Optional<String> getDataInValue() {
        return _dataInValue;
    }

    public List<String> getAdditionalParameters() {
        return _additionalParameters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_itemCommand, _dataInValue, _additionalParameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ItemConfiguration other = (ItemConfiguration) obj;
        if (_additionalParameters == null) {
            if (other._additionalParameters != null) {
                return false;
            }
        } else if (!_additionalParameters.equals(other._additionalParameters)) {
            return false;
        }
        if (_dataInValue == null) {
            if (other._dataInValue != null) {
                return false;
            }
        } else if (!_dataInValue.equals(other._dataInValue)) {
            return false;
        }
        if (_itemCommand == null) {
            if (other._itemCommand != null) {
                return false;
            }
        } else if (!_itemCommand.equals(other._itemCommand)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String requestString;

        if (_dataInValue.isPresent() || !_additionalParameters.isEmpty()) {
            StringBuilder requestStringBuilder = new StringBuilder();
            requestStringBuilder.append(_itemCommand).append(PARAM_SEPARATOR).append(_dataInValue.orElse(""));
            for (String additionalParameter : _additionalParameters) {
                requestStringBuilder.append(PARAM_SEPARATOR).append(additionalParameter);
            }

            requestString = requestStringBuilder.toString();
        } else {
            requestString = _itemCommand;
        }

        return requestString;
    }

}
