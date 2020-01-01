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
package org.openhab.binding.davis.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.davis.datatypes.DavisCommandType;

/**
 * Class to encapsulate all data which is needed to send a cmd to davis
 *
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DavisCommand {

    private List<String> keys;
    private String requestCmd;

    /**
     * @param key
     *            command key
     * @param getCmd
     *            command type
     */
    public DavisCommand(String key, DavisCommandType getCmd) {
        this.keys = new ArrayList<String>();
        this.keys.add(key);
        this.requestCmd = getCmd.getCommand();
    }

    /**
     * @param key
     *            additional command key
     */
    public void addKey(String key) {
        keys.add(key);
    }

    /**
     * @return command keys
     */
    public List<String> getKeys() {
        return keys;
    }

    /**
     * @return command byte value
     */
    public String getRequestCmd() {
        return requestCmd;
    }

}
