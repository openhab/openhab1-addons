/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.GetProduct;
import org.openhab.binding.velux.bridge.comm.GetProducts;
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

	private final int max_nodeId=6;

	/**
	 * Login into bridge, retrieve all products and logout from bridge based
	 * on a well-prepared environment of a {@link VeluxBridgeProvider}. The results
	 * are stored within a public structure {@link org.openhab.binding.velux.things.VeluxExistingProducts
	 * VeluxExistingProducts}.
	 *
	 * @param bridge Initialized Velux bridge (communication) handler.
	 * @param thisKLF Initialized Velux bridge instance.
	 * @return <b>success</b>
	 *         of type boolean describing the overall result of this interaction.
	 */

	public boolean getProducts(VeluxBridge bridge, VeluxBridgeInstance thisKLF) {
		logger.trace("getProducts() called.");

		GetProducts bcp = bridge.bridgeAPI().getProducts();
		if (! bridge.bridgeInstance.veluxBridgeConfiguration().bulkRetrieval || (bcp == null)) {
			logger.trace("getProducts() working on step-by-step retrieval.");
			//ToDo: check why there are any responses back in the buffer. And eliminate the following Logout.
			//bridge.bridgeLogout();
			GetProduct bcp2 = bridge.bridgeAPI().getProduct();
			for(int nodeId = 0; nodeId < max_nodeId; nodeId++) {
				logger.trace("getProducts() {}.", new String(new char[80]).replace('\0', '*'));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {
					logger.trace("io() wait interrupted.");
				}
				logger.trace("getProducts() working on product {}.", nodeId);
				
				//ToDo: is this really necessary: ask Velux engineering
				//bridge.bridgeLogout();

				bcp2.setProductId(nodeId);
				if (bridge.bridgeCommunicate(bcp2)) {
					VeluxProduct veluxProduct = bcp2.getProduct();
					if (bcp2.isCommunicationSuccessful()) {
						logger.trace("getProducts() found product {}.", veluxProduct);
						if (!thisKLF.existingProducts().isRegistered(veluxProduct)) {
							thisKLF.existingProducts().register(veluxProduct);
						}
					}
				}
			}
			logger.debug("getProducts() finally has found products {}.", thisKLF.existingProducts());
			return true;
		} else {
			logger.trace("getProducts() working on bulk retrieval.");
			if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
				for (VeluxProduct product : bcp.getProducts()) {
					logger.trace("getProducts() found product {} (type {}).", product.getName(), product.getTypeId());

					VeluxProduct veluxProduct = product.clone();
					logger.trace("getProducts() storing product {}.", veluxProduct);
					if (!thisKLF.existingProducts().isRegistered(veluxProduct)) {
						thisKLF.existingProducts().register(veluxProduct);
					}
				}
				logger.debug("getProducts() finally has found products {}.", thisKLF.existingProducts());
				return true;
			} else {
				logger.trace("getProducts() finished with failure.");
				return false;
			}
		}
	}

}
/**
 * end-of-bridge/VeluxBridgeGetProducts.java
 */
