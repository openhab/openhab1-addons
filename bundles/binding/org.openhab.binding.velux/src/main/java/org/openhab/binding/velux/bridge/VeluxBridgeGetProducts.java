/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.BCgetProducts;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingProducts;
import org.openhab.binding.velux.things.VeluxProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 */
public class VeluxBridgeGetProducts {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeGetProducts.class);

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
        logger.trace("getProducts() called.");

        BCgetProducts.Response response = bridge.bridgeCommunicate(new BCgetProducts());
        if (response != null) {
            for (BCgetProducts.BCproduct product : response.getDevices()) {
                logger.trace("getProducts() found product {} (type {}).", product.getName(), product.getCategory());

                VeluxProduct veluxProduct = new VeluxProduct(product);
                logger.trace("getProducts() storing product {}.", veluxProduct);
                if (!bridge.getExistingsProducts().isRegistered(veluxProduct)) {
                    bridge.getExistingsProducts().register(veluxProduct);
                }
            }
            logger.debug("getProducts() finally has found products {}.", bridge.getExistingsProducts());
            return true;
        } else {
            logger.trace("getProducts() finished with failure.");
            return false;
        }
    }

}
/**
 * end-of-bridge/VeluxBridgeGetProducts.java
 */
