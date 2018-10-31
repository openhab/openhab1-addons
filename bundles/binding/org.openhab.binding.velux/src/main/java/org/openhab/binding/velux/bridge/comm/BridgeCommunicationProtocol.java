/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

/**
 * Protocol independent bridge communication supported by the Velux bridge.
 * <P>
 * Common Message semantic: Communication with the bridge and (optionally) storing returned information within the class itself.
 * <P>
 * As 2nd level interface it defines the methods to help in sending a query and
 * processing the received answer.
 * <P>
 * (Methods in this interface for the appropriate interaction:
 * <UL>
 * <LI>{@link name} to return the name of the interaction for human interface.</LI>
 * <LI>{@link isCommunicationSuccessful} to signal the success of the interaction (only available 
 * after storing the response).</LI>
* </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public interface BridgeCommunicationProtocol {

    /**
     * Returning a description of this communication pair.
     *
     * @return <b>name</b>
     *         as String describing the communication pair for human beings.
     */
    public String name();

    /**
     * Returning the communication status included within the response message.
     *
     * @return <b>boolean</b> whether the operation according to the request was successful.
     */
    public boolean isCommunicationSuccessful();


}

/**
 * end-of-bridge/comm/BridgeCommunicationProtocol.java
 */
