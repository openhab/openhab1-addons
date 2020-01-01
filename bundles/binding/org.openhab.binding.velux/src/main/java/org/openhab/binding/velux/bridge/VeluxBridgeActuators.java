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

import org.openhab.binding.velux.VeluxBindingProvider;
import org.openhab.binding.velux.bridge.common.GetProduct;
import org.openhab.binding.velux.bridge.common.GetProducts;
import org.openhab.binding.velux.internal.VeluxBindingConfig;
import org.openhab.binding.velux.internal.VeluxItemType;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.binding.velux.things.VeluxExistingProducts;
import org.openhab.binding.velux.things.VeluxKLFAPI;
import org.openhab.binding.velux.things.VeluxProduct;
import org.openhab.binding.velux.things.VeluxProductPosition;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeActuators} represents a complete set of transactions
 * for retrieving of any available products into a structure {@link #channel}
 * defined on the <B>Velux</B> bridge.
 * <P>
 * It provides the methods:
 * <UL>
 * <LI>{@link #getProducts} for retrieval of information from the bridge.
 * <LI>{@link #getChannel} for accessing the retrieved information.
 * <LI>{@link #autoRefresh} for retrieval of information in case of an
 * empty list of actuators.
 * <LI>{@link #updateOH} for updating the corresponding openHAB items via
 * the EventBus {@link EventPublisher}.
 * </UL>
 * Any parameters are controlled by {@link VeluxBridgeConfiguration}.
 *
 * @see VeluxProduct
 * @see VeluxExistingProducts
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBridgeActuators {
    private final Logger logger = LoggerFactory.getLogger(VeluxBridgeActuators.class);

    // Type definitions, class-internal variables

    /**
     * Actuator information consisting of:
     * <ul>
     * <li>isRetrieved (boolean),
     * <li>existingProducts ({@link VeluxExistingProducts}).
     * </ul>
     */
    public class Channel {
        public boolean isRetrieved = false;
        public VeluxExistingProducts existingProducts = new VeluxExistingProducts();
    }

    private Channel channel = null;

    // Constructor methods

    /**
     * Constructor.
     * <P>
     * Initializes the internal data structure {@link #channel} of Velux actuators/products,
     * which is publicly accessible via the method {@link #getChannel()}.
     */
    public VeluxBridgeActuators() {
        logger.trace("VeluxBridgeActuators(constructor) called.");
        channel = new Channel();
    }

    // Class access methods

    /**
     * Provide access to the internal structure of actuators/products.
     *
     * @return a channel describing the overall actuator situation.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Login into bridge, retrieve all products and logout from bridge based
     * on a well-prepared environment of a {@link VeluxBridgeProvider}. The results
     * are stored within {@link org.openhab.binding.velux.things.VeluxExistingProducts
     * VeluxExistingProducts}.
     *
     * @param bridge Initialized Velux bridge (communication) handler.
     * @return true if successful, and false otherwise.
     */
    public boolean getProducts(VeluxBridge bridge) {
        logger.trace("getProducts() called.");

        GetProducts bcp = bridge.bridgeAPI().getProducts();
        if (!bridge.bridgeInstance.veluxBridgeConfiguration().isBulkRetrievalEnabled || (bcp == null)) {
            logger.trace("getProducts() working on step-by-step retrieval.");
            GetProduct bcpSbS = bridge.bridgeAPI().getProduct();
            for (int nodeId = 0; nodeId < VeluxKLFAPI.KLF_SYSTEMTABLE_MAX; nodeId++) {
                logger.trace("getProducts() {}.", new String(new char[80]).replace('\0', '*'));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    logger.trace("io() wait interrupted.");
                }
                logger.trace("getProducts() working on product {}.", nodeId);
                bcpSbS.setProductId(nodeId);
                if ((bridge.bridgeCommunicate(bcpSbS)) && (bcpSbS.isCommunicationSuccessful())) {
                    VeluxProduct veluxProduct = bcpSbS.getProduct();
                    if (bcpSbS.isCommunicationSuccessful()) {
                        logger.debug("getProducts() found product {}.", veluxProduct);
                        if (!channel.existingProducts.isRegistered(veluxProduct)) {
                            channel.existingProducts.register(veluxProduct);
                        }
                    }
                }
            }
            logger.debug("getProducts() finally has found products {}.", channel.existingProducts);
            return true;
        } else {
            logger.trace("getProducts() working on bulk retrieval.");
            if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
                for (VeluxProduct product : bcp.getProducts()) {
                    logger.trace("getProducts() found product {} (type {}).", product.getProductName(),
                            product.getProductType());
                    if (!channel.existingProducts.isRegistered(product)) {
                        logger.debug("getProducts() storing new product {}.", product);
                        channel.existingProducts.register(product);
                    } else {
                        logger.debug("getProducts() storing updates for product {}.", product);
                        channel.existingProducts.update(product);
                    }

                }
                logger.debug("getProducts() finally has found products {}.", channel.existingProducts);
                return true;
            } else {
                logger.trace("getProducts() finished with failure.");
                return false;
            }
        }
    }

    /**
     * In case of an empty list of recognized products, the method will
     * initiate a product retrieval using {@link #getProducts(VeluxBridge)}.
     *
     * @param bridge Initialized Velux bridge (communication) handler.
     * @return true if at least one product was found, and false otherwise.
     */
    public boolean autoRefresh(VeluxBridge bridge) {
        logger.trace("autoRefresh(): is about to fetch existing products.");
        getProducts(bridge);
        return (channel.existingProducts.getNoMembers() > 0);
    }

    /**
     * In case of recognized changes in the real world, the method will
     * update the corresponding states via openHAB event bus.
     *
     * @param provider provides access to the defined items.
     * @param eventPublisher provides access to the openHAB event bus methods.
     * @return true if the update of the openHAB item succeeded, and false otherwise.
     *
     */
    public boolean updateOH(VeluxBindingProvider provider, EventPublisher eventPublisher) {
        logger.trace("updateOH() called.");
        if (!channel.existingProducts.isDirty()) {
            logger.trace("updateOH() finished.");
            return true;
        }
        logger.trace("updateOH(): existingProducts have changed.");
        for (VeluxProduct product : channel.existingProducts.valuesOfModified()) {
            logger.trace("updateOH(): actuator {} has changed values.", product.getProductName().toString());
            for (String thisItemName : provider.getInBindingItemNames()) {
                VeluxBindingConfig thisItemConfig = provider.getConfigForItemName(thisItemName);
                if (thisItemConfig.getBindingItemType() != VeluxItemType.ACTUATOR_SERIAL) {
                    continue;
                }
                if (product.getSerialNumber().equals(thisItemConfig.getBindingConfig())) {
                    logger.trace("updateOH(): product {}/{} used within item {}.", product.getProductName(),
                            product.getSerialNumber(), thisItemName);
                    try {
                        PercentType positionAsPercent = new VeluxProductPosition(product.getCurrentPosition())
                                .getPositionAsPercentType();
                        if (positionAsPercent != null) {
                            logger.debug("updateOH(): updating item {} to position {}%.", thisItemName,
                                    positionAsPercent);
                            eventPublisher.postUpdate(thisItemName, positionAsPercent);
                        } else {
                            logger.trace("updateOH(): update of item {} to position {} skipped.", thisItemName,
                                    positionAsPercent);
                        }
                    } catch (Exception e) {
                        logger.warn("updateOH(): getProducts() exception: {}.", e.getMessage());
                    }
                }
            }
        }
        channel.existingProducts.resetDirtyFlag();
        logger.trace("updateOH() finished.");
        return true;
    }

}
