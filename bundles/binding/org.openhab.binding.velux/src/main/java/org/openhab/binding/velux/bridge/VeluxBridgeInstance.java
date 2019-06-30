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
 * <LI>{@link org.openhab.binding.velux.bridge.common.BridgeAPI#getProducts getProducts}
 * for handling of products managed by a specific Velux bridge,</LI>
 * <LI>{@link org.openhab.binding.velux.bridge.common.BridgeAPI#getScenes getScenes}
 * for handling of scenes managed by a specific Velux bridge.</LI>
 * </UL>
 *
 * <P>
 * Status
 * </P>
 * two methods for bridge-internal configuration retrieval:
 * <UL>
 * <LI>{@link existingProducts}
 * for retrieving scene information,</LI>
 * <LI>{@link existingScenes}
 * for retrieving product information.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */

public interface VeluxBridgeInstance {

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
     * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeActuators#getProducts}
     *
     * @return VeluxExistingProducts containing all registered products, or <B>null</B> in case of any error.
     */
    public VeluxExistingProducts existingProducts();

    /**
     * Information retrieved by {@link org.openhab.binding.velux.bridge.VeluxBridgeScenes#getScenes}
     *
     * @return VeluxExistingScenes containing all registered scenes, or <B>null</B> in case of any error.
     */
    public VeluxExistingScenes existingScenes();

}
