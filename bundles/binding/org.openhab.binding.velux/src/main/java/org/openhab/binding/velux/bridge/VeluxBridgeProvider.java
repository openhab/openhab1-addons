/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol;
import org.openhab.binding.velux.bridge.comm.BridgeAPI;

/**
 * This interface is implemented by classes that provide general communication with the Velux bridge.
 * <P>
 * Communication
 * </P>
 * <UL>
 * <LI>{@link VeluxBridgeProvider#bridgeCommunicate} for generic communication,</LI>
 * <LI>{@link VeluxBridgeProvider#bridgeAPI} for common access to all interaction/communication methode.</LI>
 * </UL>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */
public interface VeluxBridgeProvider {

	/**
	 * Initializes a client/server communication towards <b>Velux</b> veluxBridge
	 * based on the Basic I/O interface {@link VeluxBridge} and parameters
	 * passed as arguments (see below) and provided by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
	 * This method automatically decides to invoke a login communication before the
	 * intended request if there has not been an authentication before.
	 *
	 * @param communication Structure of interface type {@link BridgeCommunicationProtocol} describing the intended
	 *                          communication, that is request and response interactions as well as appropriate URL definition.
	 * @return <b>success</b> of type boolean which signals the success of the communication.
	 */

	public boolean bridgeCommunicate(BridgeCommunicationProtocol communication);

	
	/**
	 * Returns the class {@link BridgeAPI} which summarizes all interfacing methods.
	 * 
	 * @return <b>BridgeAPI</b>
	 *         containing all API methods.
	 */	
	public BridgeAPI bridgeAPI();
}

/**
 * end-of-bridge/VeluxBridgeProvider.java
 */
