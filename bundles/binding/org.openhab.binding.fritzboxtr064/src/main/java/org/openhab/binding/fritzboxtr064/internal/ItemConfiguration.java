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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Configuration of an item from the binding config. Each configuration has an {@link #getItemCommand() item command}
 * which determines the value to read. For parametrisable commands the config may also have
 * {@link #getArgumentValues() values for the input arguments}.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.11.0
 */
public class ItemConfiguration {
    private static final String VALUE_SEPARATOR = ":";

    private final String _itemCommand;

    private final List<String> _argumentValues;

    /**
     * Parses a configuration string from the binding configuration.
     * The command and argument values must be separated by colons (':').
     *
     * @param itemConfig Configuration string from the binding configuration.
     * @return The parsed configuration.
     */
    public static ItemConfiguration parse(String itemConfig) {
        String[] requestParts = itemConfig.split(VALUE_SEPARATOR);

        String itemCommand = requestParts[0];
        List<String> _argumentValues;

        if (requestParts.length > 1) {
            _argumentValues = Arrays.asList(requestParts).subList(1, requestParts.length);
        } else {
            _argumentValues = emptyList();
        }

        return new ItemConfiguration(itemCommand, _argumentValues);
    }

    public ItemConfiguration(String _itemCommand, String... _argumentValues) {
        this(_itemCommand, Arrays.asList(_argumentValues));
    }

    public ItemConfiguration(String _itemCommand, List<String> _argumentValues) {
        this._itemCommand = _itemCommand;
        this._argumentValues = _argumentValues;
    }

    public String getItemCommand() {
        return _itemCommand;
    }

    public List<String> getArgumentValues() {
        return _argumentValues;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_itemCommand, _argumentValues);
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
        if (_argumentValues == null) {
            if (other._argumentValues != null) {
                return false;
            }
        } else if (!_argumentValues.equals(other._argumentValues)) {
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

        if (!_argumentValues.isEmpty()) {
            StringBuilder requestStringBuilder = new StringBuilder();
            for (String argumentValue : _argumentValues) {
                requestStringBuilder.append(VALUE_SEPARATOR).append(argumentValue);
            }

            requestString = requestStringBuilder.toString();
        } else {
            requestString = _itemCommand;
        }

        return requestString;
    }

}
