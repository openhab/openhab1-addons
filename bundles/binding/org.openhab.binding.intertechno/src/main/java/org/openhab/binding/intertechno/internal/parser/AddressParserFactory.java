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
package org.openhab.binding.intertechno.internal.parser;

/**
 * Simple factory to create Intertechno parsers based on the type of the binding
 * config.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class AddressParserFactory {

    public static IntertechnoAddressParser getParser(String type) {

        if ("classic".equals(type)) {
            return new ClassicParser();
        } else if ("fls".equals(type)) {
            return new FLSParser();
        } else if ("rev".equals(type)) {
            return new REVParser();
        } else if ("raw".equals(type)) {
            return new RawParser();
        } else if ("v3".equals(type)) {
            return new V3Parser();
        }

        return null;

    }

}
