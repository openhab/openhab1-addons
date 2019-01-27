/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
