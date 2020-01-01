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
package org.openhab.binding.gc100ir.internal.response;

/**
 * An interface class defining commands from a GC100IR device.
 *
 * @author Stephen Liang
 * @since 1.9.0
 *
 */
public interface GC100IRCommand {
    /**
     * Gets the command code associated with this command. Example codes are completeir, unknowncommand, sendir.
     *
     * @return A GC100IRCommandCode, such as completeir.
     */
    public GC100IRCommandCode getCommandCode();
}
