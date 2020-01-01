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
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;

/**
 * The {@link VeluxBridgeExecute} represents a complete set of transactions
 * for executing a scene defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeExecute#execute} for execution of a scene.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
@Deprecated
public class VeluxBridgeExecute {

    /**
     * Login into bridge, executes a scene and logout from bridge based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @param sceneNo Number of scene to be executed.
     * @return <b>success</b>
     *         of type boolean describing the overall result of this interaction.
     */
    public boolean execute(VeluxBridgeProvider bridge, int sceneNo) {
        return false;
    }
}
