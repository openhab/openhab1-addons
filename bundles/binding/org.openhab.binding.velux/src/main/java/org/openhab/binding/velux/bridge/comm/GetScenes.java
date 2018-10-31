/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge.comm;

import org.openhab.binding.velux.things.VeluxScene;

/**
 * <B>Common bridge communication message scheme supported by the </B><I>Velux</I><B> bridge.</B>
 * <P>
 * Message semantic will be defined by the implementations according to the different comm paths.
 * <P>
 *
 * @author Guenther Schreiner - Initial contribution.
 */
public abstract class GetScenes implements BridgeCommunicationProtocol  {
   
	/**
	 * <B>Additional information retrieval.</B>
	 *
	 * @return <b>arrayOfScenes</b> as Array of VeluxScene describing the current scene configuration of the bridge.
	 */
	abstract public VeluxScene[] getScenes() ;

}
/**
 * end-of-bridge/comm/GetScenes.java
 */
