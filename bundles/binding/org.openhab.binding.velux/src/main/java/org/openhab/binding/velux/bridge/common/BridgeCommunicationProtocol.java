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
 * Protocol independent bridge communication supported by the Velux bridge.
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class
 * itself.
 * <P>
 * As 2nd level interface it defines the methods to help in sending a query and
 * processing the received answer.
 * <P>
 * Methods in this interface for the appropriate interaction:
 * <UL>
 * <LI>{@link name} to return the name of the interaction for human interface.</LI>
 * <LI>{@link isCommunicationSuccessful} to signal the success of the interaction (only available
 * after storing the response).</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public interface BridgeCommunicationProtocol {

    /**
     * Returns the name of this communication pair.
     *
     * @return name of the communication pair for human beings.
     */
    public String name();

    /**
     * Returns the communication status included within the response message.
     *
     * @return true if the communication was successful, and false otherwise.
     */
    public boolean isCommunicationSuccessful();

}
