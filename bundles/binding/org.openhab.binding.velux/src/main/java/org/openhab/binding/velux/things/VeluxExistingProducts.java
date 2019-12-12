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
package org.openhab.binding.velux.things;

import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.velux.things.VeluxProduct.ProductBridgeIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combined set of product informations provided by the <B>Velux</B> bridge,
 * which can be used for later interactions.
 * <P>
 * The following class access methods exist:
 * <UL>
 * <LI>{@link VeluxExistingProducts#isRegistered} for querying existence of a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#register} for storing a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#update} for updating/storing of a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#get} for retrieval of a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#values} for retrieval of all {@link VeluxProduct}s,
 * <LI>{@link VeluxExistingProducts#getNoMembers} for retrieval of the number of all {@link VeluxProduct}s,
 * <LI>{@link VeluxExistingProducts#toString} for a descriptive string representation.
 * </UL>
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public class VeluxExistingProducts {
    private final Logger logger = LoggerFactory.getLogger(VeluxExistingProducts.class);

    // Type definitions, class-internal variables

    private static String outputSeparator = ",";
    private ConcurrentHashMap<String, VeluxProduct> existingProductsByUniqueIndex;
    private ConcurrentHashMap<Integer, String> bridgeIndexToSerialNumber;
    private ConcurrentHashMap<String, VeluxProduct> modifiedProductsByUniqueIndex;
    private int memberCount;

    /*
     * Value to flag any changes towards the getter.
     */
    private boolean dirty;

    // Constructor methods

    public VeluxExistingProducts() {
        logger.trace("VeluxExistingProducts() initializing.");
        existingProductsByUniqueIndex = new ConcurrentHashMap<String, VeluxProduct>();
        bridgeIndexToSerialNumber = new ConcurrentHashMap<Integer, String>();
        modifiedProductsByUniqueIndex = new ConcurrentHashMap<String, VeluxProduct>();
        memberCount = 0;
        dirty = true;
    }

    // Class access methods

    public boolean isRegistered(String productUniqueIndexOrSerialNumber) {
        logger.trace("isRegistered(String {}) returns {}.", productUniqueIndexOrSerialNumber,
                existingProductsByUniqueIndex.containsKey(productUniqueIndexOrSerialNumber) ? "true" : "false");
        return existingProductsByUniqueIndex.containsKey(productUniqueIndexOrSerialNumber);
    }

    public boolean isRegistered(VeluxProduct product) {
        logger.trace("isRegistered(VeluxProduct {}) called.", product.toString());
        if (product.isV2()) {
            return isRegistered(product.getSerialNumber());
        }
        return isRegistered(product.getProductUniqueIndex());
    }

    public boolean isRegistered(ProductBridgeIndex bridgeProductIndex) {
        logger.trace("isRegisteredProductBridgeIndex {}) called.", bridgeProductIndex.toString());
        if (!bridgeIndexToSerialNumber.containsKey(bridgeProductIndex.toInt())) {
            return false;
        }
        return isRegistered(bridgeIndexToSerialNumber.get(bridgeProductIndex.toInt()));
    }

    public boolean register(VeluxProduct newProduct) {
        logger.trace("register({}) called.", newProduct);
        if (isRegistered(newProduct)) {
            return false;
        }
        logger.trace("register() registering new product {}.", newProduct);

        String uniqueIndex = newProduct.isV2() ? newProduct.getSerialNumber() : newProduct.getProductUniqueIndex();
        logger.trace("register() registering by UniqueIndex {}", uniqueIndex);
        existingProductsByUniqueIndex.put(uniqueIndex, newProduct);

        logger.trace("register() registering by ProductBridgeIndex {}", newProduct.getBridgeProductIndex().toInt());
        bridgeIndexToSerialNumber.put(newProduct.getBridgeProductIndex().toInt(), newProduct.getSerialNumber());

        logger.trace("register() registering set of modifications by UniqueIndex {}", uniqueIndex);
        modifiedProductsByUniqueIndex.put(uniqueIndex, newProduct);

        memberCount++;
        dirty = true;
        return true;
    }

    public boolean update(ProductBridgeIndex bridgeProductIndex, int productState, int productPosition,
            int productTarget) {
        logger.debug("update(bridgeProductIndex={},productState={},productPosition={},productTarget={}) called.",
                bridgeProductIndex.toInt(), productState, productPosition, productTarget);
        if (!isRegistered(bridgeProductIndex)) {
            logger.warn("update() failed as actuator (with index {}) is not registered.", bridgeProductIndex.toInt());
            return false;
        }
        VeluxProduct thisProduct = this.get(bridgeProductIndex);
        if (thisProduct.setState(productState) || thisProduct.setCurrentPosition(productPosition)
                || thisProduct.setTarget(productTarget)) {
            dirty = true;

            String uniqueIndex = thisProduct.isV2() ? thisProduct.getSerialNumber()
                    : thisProduct.getProductUniqueIndex();
            logger.trace("update(): updating by UniqueIndex {}.", uniqueIndex);
            existingProductsByUniqueIndex.replace(uniqueIndex, thisProduct);
            modifiedProductsByUniqueIndex.put(uniqueIndex, thisProduct);
        }
        logger.trace("update() successfully finished (dirty={}).", dirty);
        return true;
    }

    public boolean update(VeluxProduct currentProduct) {
        logger.trace("update(currentProduct={}) called.", currentProduct);
        return update(currentProduct.getBridgeProductIndex(), currentProduct.getState(),
                currentProduct.getCurrentPosition(), currentProduct.getTarget());
    }

    public VeluxProduct get(String productUniqueIndexOrSerialNumber) {
        logger.trace("get({}) called.", productUniqueIndexOrSerialNumber);
        if (!isRegistered(productUniqueIndexOrSerialNumber)) {
            return null;
        }
        return existingProductsByUniqueIndex.get(productUniqueIndexOrSerialNumber);
    }

    public VeluxProduct get(ProductBridgeIndex bridgeProductIndex) {
        logger.trace("get({}) called.", bridgeProductIndex);
        if (!isRegistered(bridgeProductIndex)) {
            return null;
        }
        return existingProductsByUniqueIndex.get(bridgeIndexToSerialNumber.get(bridgeProductIndex.toInt()));
    }

    public VeluxProduct[] values() {
        return existingProductsByUniqueIndex.values().toArray(new VeluxProduct[0]);
    }

    public VeluxProduct[] valuesOfModified() {
        return modifiedProductsByUniqueIndex.values().toArray(new VeluxProduct[0]);
    }

    public int getNoMembers() {
        logger.trace("getNoMembers() returns {}.", memberCount);
        return memberCount;
    }

    public boolean isDirty() {
        logger.trace("isDirty() returns {}.", dirty);
        return dirty;
    }

    public void resetDirtyFlag() {
        logger.trace("resetDirtyFlag() called.");
        modifiedProductsByUniqueIndex = new ConcurrentHashMap<String, VeluxProduct>();
        dirty = false;
    }

    public String toString(boolean showSummary, String delimiter) {
        StringBuilder sb = new StringBuilder();

        if (showSummary) {
            sb.append(memberCount).append(" members: ");
        }
        for (VeluxProduct product : this.values()) {
            sb.append(product.toString()).append(delimiter);
        }
        if (sb.lastIndexOf(delimiter) > 0) {
            sb.deleteCharAt(sb.lastIndexOf(delimiter));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(true, outputSeparator);
    }

}
