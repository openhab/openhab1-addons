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
import org.openhab.binding.velux.things.VeluxExistingScenes;
import org.openhab.binding.velux.things.VeluxScene;

/**
 * The {@link VeluxBridgeGetScenes} represents a complete set of transactions
 * for retrieving of any available scenes into a structure {@link VeluxExistingScenes}
 * defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeGetScenes#getScenes} for retrieval of information.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxScene
 * @see VeluxExistingScenes
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
@Deprecated
public class VeluxBridgeGetScenes {

    @Deprecated
    public boolean getScenes(VeluxBridgeProvider bridge) {
        return false;
    }

    @Deprecated
    public VeluxBridgeGetScenes() {
    }

}
