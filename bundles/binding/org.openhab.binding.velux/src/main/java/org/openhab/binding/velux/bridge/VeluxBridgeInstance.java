/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingProducts;
import org.openhab.binding.velux.things.VeluxExistingScenes;

/**
 * This interface is implemented by classes that deal with a specific Velux bridge and its configuration.
 * <P>
 * Communication
 * </P>
 * <UL>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BridgeAPI#getProducts getProducts}
 *  for handling of products managed by a specific Velux bridge,
 * and </LI>
 * <LI>{@link org.openhab.binding.velux.bridge.comm.BridgeAPI#getScenes getScenes} 
 * for handling of scenes managed by a specific Velux bridge.</LI>
 * </UL>
 *
 * <P>
 * Status
 * </P>
 * and, in addition, two methods for bridge-internal configuration retrieval:
 * <UL>
 * <LI>{@link existingProducts}
 *  for retrieving scene informations,</LI>
 * <LI>{@link existingScenes}
 *  for retrieving product informations.</LI>
 * </UL>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */

public interface VeluxBridgeInstance {

	/** Information retrieved by {@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding}
	 * and updated via {@link org.openhab.binding.velux.internal.VeluxBinding#updated} */

	/**
	 * Information retrieved by {@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding}
	 * and updated via {@link org.openhab.binding.velux.internal.VeluxBinding#updated}
	 *
	 * @return <b>response</b> of type VeluxBridgeConfiguration containing bridge communication parameters.
	 *         <P>
	 *         <B>null</B> in case of any error.
	 */
	public VeluxBridgeConfiguration veluxBridgeConfiguration();

	/**
	 * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetProducts#getProducts}
	 *
	 * @return <b>response</b> of type VeluxExistingProducts containing all registered products.
	 *         <P>
	 *         <B>null</B> in case of any error.
	 */
	public VeluxExistingProducts existingProducts();


	/**
	 * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeGetScenes#getScenes}
	 *
	 * @return <b>response</b> of type VeluxExistingScenes containing all registered scenes.
	 *         <P>
	 *         <B>null</B> in case of any error.
	 */
	public VeluxExistingScenes existingScenes();

}

/**
 * end-of-bridge/VeluxBridgeInstance.java
 */
