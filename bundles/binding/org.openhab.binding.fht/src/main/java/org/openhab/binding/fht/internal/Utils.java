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
package org.openhab.binding.fht.internal;

/**
 * Some utils to make communcation with the CUL easier.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class Utils {

    /**
     * Convert a decimal string to its hexadecimal representation
     * 
     * @param in
     * @return
     */
    public static String convertDecimalStringToHexString(String in) {
        int integer = Integer.parseInt(in);
        String hexString = Integer.toHexString(integer);
        if (hexString.length() == 1) {
            hexString = '0' + hexString;
        }
        return hexString;
    }

}
