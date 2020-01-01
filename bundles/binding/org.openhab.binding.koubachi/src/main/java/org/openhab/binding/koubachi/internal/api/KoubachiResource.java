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
package org.openhab.binding.koubachi.internal.api;

/**
 * The Superclass of all Koubachi resources. Holds common fields an methods.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public abstract class KoubachiResource {

    protected String id;

    public String getId() {
        return id;
    }

}
