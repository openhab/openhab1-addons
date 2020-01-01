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
package com.myhome.fcrisciani.datastructure.command;

/**
 * This class represent a Status/Request command in the OpenWebNet For further
 * details check the OpenWebNet reference
 *
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class StatusRequestCmd extends CommandOPEN {
    // ----- TYPES ----- //

    // ---- MEMBERS ---- //

    // ---- METHODS ---- //

    /**
     * Create a new instance of a StatusRequestCommand
     * 
     * @param commandString
     *            command as a string
     * @param who
     *            who field
     * @param where
     *            where field
     */
    public StatusRequestCmd(String commandString, String who, String where) {
        super(commandString, 1, who, where);
        ;
    }

    /**
     * Generate automatically a new Status Request Command passing basic
     * argument
     * 
     * @param who
     *            who field
     * @param where
     *            where field
     */
    public StatusRequestCmd(String who, String where) {
        super("*#" + who + "*" + where + "##", 1, who, where);
        ;
    }

}
