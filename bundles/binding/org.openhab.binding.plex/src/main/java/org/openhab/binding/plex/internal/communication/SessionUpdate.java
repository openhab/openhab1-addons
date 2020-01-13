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
package org.openhab.binding.plex.internal.communication;

/**
 * Interface for objects containing updated information for a Plex media playing session.
 *
 * @author Jeroen Idserda
 * @since 1.9.0
 */
public interface SessionUpdate {

    public String getKey();

    public String getState();

    public String getSessionKey();

    public Integer getViewOffset();

}
