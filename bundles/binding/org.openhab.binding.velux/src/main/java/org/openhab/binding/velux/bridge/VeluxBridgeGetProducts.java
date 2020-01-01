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
import org.openhab.binding.velux.things.VeluxExistingProducts;
import org.openhab.binding.velux.things.VeluxProduct;

/**
 * The {@link VeluxBridgeGetProducts} represents a complete set of transactions
 * for retrieving of any available products into a structure {@link VeluxExistingProducts}
 * defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeGetProducts#getProducts} for retrieval of information.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxProduct
 * @see VeluxExistingProducts
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
@Deprecated
public class VeluxBridgeGetProducts {

    /**
     * Login into bridge, retrieve all products and logout from bridge based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}. The results
     * are stored within a public structure {@link org.openhab.binding.velux.things.VeluxExistingProducts
     * VeluxExistingProducts}.
     *
     * @param bridge Initialized Velux bridge handler.
     * @return <b>success</b>
     *         of type boolean describing the overall result of this interaction.
     */

    public boolean getProducts(VeluxBridgeProvider bridge) {
        return false;
    }

}
