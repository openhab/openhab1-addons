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
package org.openhab.model.item.binding;

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
public class BindingConfigParseException extends Exception {

    private static final long serialVersionUID = 1434607160082879845L;

    public BindingConfigParseException(String msg) {
        super(msg);
    }

}
