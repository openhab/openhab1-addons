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
package com.myhome.fcrisciani.exception;

/**
 * This class represents a specific open command exception
 *
 * @author Flavio Crisciani
 * @serial 1.0
 * @since 1.7.0
 */
public class MalformedCommandOPEN extends Exception {

    // ----- TYPES ----- //

    // ---- MEMBERS ---- //
    private static final long serialVersionUID = -4446224937634207778L;

    String commandNotRecognised = null;

    // ---- METHODS ---- //

    public MalformedCommandOPEN(String commandNotRecognised) {
        super();
        this.commandNotRecognised = commandNotRecognised;
    }

    @Override
    public String getLocalizedMessage() {
        StringBuilder builder = new StringBuilder();

        builder.append("Command OPEN: ").append(commandNotRecognised).append(" has not a valid format");

        return builder.toString();
    }
}
