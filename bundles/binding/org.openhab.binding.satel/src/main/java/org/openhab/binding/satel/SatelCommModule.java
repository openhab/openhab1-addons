/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel;

import org.openhab.binding.satel.command.SatelCommand;

/**
 * This interface exposes functions that help sending commands to Satel communication module (INT-RS, ETHM-1).
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public interface SatelCommModule {

    /**
     * Returns connection status to the module.
     *
     * @return <code>true</code> if connection is established
     */
    boolean isConnected();

    /**
     * Returns firmware version of connected module.
     *
     * @return version string
     */
    String getFirmwareVersion();

    /**
     * Sends command to the module.
     *
     * @param command command to send
     * @return <code>true</code> if command has been successfully sent and handled
     */
    boolean sendCommand(SatelCommand command);

    /**
     * Returns character encoding for all texts returned by the module.
     *
     * @return character encoding for texts
     */
    String getTextEncoding();

    /**
     * Overrides user code configured in settings.
     *
     * @param userCode user code to set
     */
    void setUserCode(String userCode);

    /**
     * Reverts user code to the value configured in settings.
     */
    void resetUserCode();

}
