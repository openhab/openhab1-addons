/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

import org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol;

/**
 * Specific bridge communication message supported by the Velux bridge.
 * <P>
 * Message semantic: Communication to authenticate itself, resulting in a return of current bridge state.
 * <P>
 *
 * It defines informations how to send query and receive answer through the
 * {@link org.openhab.binding.velux.bridge.VeluxBridgeProvider VeluxBridgeProvider}
 * as described by the {@link org.openhab.binding.velux.bridge.comm.BridgeCommunicationProtocol
 * BridgeCommunicationProtocol}.
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public abstract class Login implements BridgeCommunicationProtocol {

	/**
	 *<B>Methods in addition to interface {@link BridgeCommunicationProtocol}.</B><P>
	 *
	 * Returning the communication status included within the response message.
	 *
	 * @param thisPassword		Password passed as String.
	 */
	public void setPassword(String thisPassword) {
	}
	
	/**
	 *<B>Methods in addition to interface {@link BridgeCommunicationProtocol}.</B><P>
	 *
	 * Returning the authentication information optionally to be used for later following 
	 * messages.
	 *
     * @return <b>authentication token</b> as String which can be used for next operations.
     */
    public String getAuthToken() {
		return "";
    }

}
/**
 * end-of-bridge/comm/Login.java
 */
