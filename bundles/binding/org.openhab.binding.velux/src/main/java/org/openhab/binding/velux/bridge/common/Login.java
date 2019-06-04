/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.velux.bridge.common;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Communication to authenticate itself, resulting in a return of current bridge state.
 * <P>
 * In addition to the common methods defined by {@link BridgeCommunicationProtocol}
 * each protocol-specific implementation has to provide the following methods:
 * <UL>
 * <LI>{@link #setPassword} for defining the intended authentication value.
 * </UL>
 *
 * @see BridgeCommunicationProtocol
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public abstract class Login implements BridgeCommunicationProtocol {

    /**
     * Sets the intended password string to be used for authentication
     *
     * @param thisPassword Password passed as String.
     */
    public void setPassword(String thisPassword) {
    }

    /**
     * Returns the authentication information optionally to be used for later following
     * messages.
     *
     * @return <b>authentication token</b> as String which can be used for next operations.
     */
    public String getAuthToken() {
        return "";
    }

}
