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
 * This command represent an OpenWebNet dimension response See OpenWebNet manual
 * for further documentation
 *
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class DimensionResponseCmd extends CommandOPEN {

    // ----- TYPES ----- //

    // ---- MEMBERS ---- //

    int dimension = -1; // Dimension type (see documentation for possible
                        // values)
    String[] values = null; // List of values in the message

    // ---- METHODS ---- //
    /**
     * Create a new Dimension response instance
     * 
     * @param commandString
     *            command in string format
     * @param who
     *            who field
     * @param where
     *            where field
     * @param dimension
     *            dimension type
     * @param values
     *            list of values of the specified dimension
     */
    public DimensionResponseCmd(String commandString, String who, String where, String dimension, String[] values) {
        super(commandString, 4, who, where);
        this.dimension = Integer.parseInt(dimension);
        this.values = values;
    }

    /**
     * Get dimension type
     * 
     * @return dimension type
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Get list of values
     * 
     * @return
     */
    public String[] getValues() {
        return values;
    }

}
