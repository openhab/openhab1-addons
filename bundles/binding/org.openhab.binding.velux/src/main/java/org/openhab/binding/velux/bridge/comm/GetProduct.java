/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

import org.openhab.binding.velux.things.VeluxProduct;

/**
 * <B>Common bridge communication message scheme supported by the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * Message semantic will be defined by the implementations according to the different comm paths.
 * <P>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public abstract class GetProduct implements BridgeCommunicationProtocol  {
   
	/**
	 *<B>Methods in addition to interface {@link BridgeCommunicationProtocol}.</B><P>
	 *
	 * Set the intended node identifier to be queried
	 *
	 * @param nodeId		Gateway internal node identifier (zero to 199)
	 */
	public void setProductId(int nodeId) {
	}
	
	/**
	 *<B>Methods in addition to interface {@link BridgeCommunicationProtocol}.</B><P>
	 *
	 * Returning the communication status included within the response message.
	 *
	 * @return <b>veluxProduct</b> as VeluxProduct.
	 */
	abstract public VeluxProduct getProduct() ;

}
/**
 * end-of-bridge/comm/GetProduct.java
 */
