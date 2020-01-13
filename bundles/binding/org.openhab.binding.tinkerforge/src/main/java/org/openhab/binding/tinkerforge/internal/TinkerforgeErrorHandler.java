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
package org.openhab.binding.tinkerforge.internal;

import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.slf4j.Logger;

public class TinkerforgeErrorHandler {
    /**
     * @author Theo Weiss
     * @since 1.3.0
     */

    public static String TF_TIMEOUT_EXCEPTION = "Tinkerforge timeout occurred";
    public static String TF_NOT_CONNECTION_EXCEPTION = "Tinkerforge \"not connected\" occurred";
    public static final String TF_NOT_CRYPTO_EXCEPTION = "Tinkerforge authorization failed";;

    /**
     * Logs errors from devices using the device specific logger.
     * 
     * @param mBaseDevice The device to log errors for as {@code MBaseDevice}.
     * @param message The log message as {@code String}.
     * @param e The throwable to log as {@code Throwable}
     */
    public static void handleError(MBaseDevice mBaseDevice, String message, Throwable e) {
        mBaseDevice.getLogger().error("Tinkerforge Error: {} : {}", message, e.getMessage());
    }

    /**
     * Logs errors using the given logger.
     * 
     * @param logger The logger to use as {@code Logger}.
     * @param message The message to log as {@code String}.
     * @param e The throwable to log as {@code Throwable}
     */
    public static void handleError(Logger logger, String message, Throwable e) {
        logger.error("Tinkerforge Error: {} : {}", message, e.getMessage());
    }
}
