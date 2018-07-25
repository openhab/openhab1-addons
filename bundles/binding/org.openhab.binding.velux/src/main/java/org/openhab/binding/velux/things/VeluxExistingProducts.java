/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import java.util.concurrent.ConcurrentHashMap;

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
 * <LI>{@link VeluxExistingProducts#get} for retrieval of a {@link VeluxProduct},
 * <LI>{@link VeluxExistingProducts#values} for retrieval of all {@link VeluxProduct}s,
 * <LI>{@link VeluxExistingProducts#getNoMembers} for retrieval of the number of all {@link VeluxProduct}s,
 * <LI>{@link VeluxExistingProducts#toString} for a descriptive string representation.
 * </UL>
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxExistingProducts {
    private final Logger logger = LoggerFactory.getLogger(VeluxExistingProducts.class);

    // Type definitions

    private ConcurrentHashMap<String, VeluxProduct> existingProductsByUniqueIndex;
    private int memberCount;

    public VeluxExistingProducts() {
        logger.trace("VeluxExistingProducts() initializing.");
        existingProductsByUniqueIndex = new ConcurrentHashMap<String, VeluxProduct>();
        memberCount = 0;
    }

    // Class access methods

    public boolean isRegistered(String productUniqueIndex) {
        logger.trace("isRegistered({}) returns {}.", productUniqueIndex,
                existingProductsByUniqueIndex.containsKey(productUniqueIndex) ? "true" : "false");
        return existingProductsByUniqueIndex.containsKey(productUniqueIndex);
    }

    public boolean isRegistered(VeluxProduct product) {
        return isRegistered(product.getProductUniqueIndex());
    }

    public boolean register(VeluxProduct newProduct) {
        logger.trace("register({}) called.", newProduct);
        if (isRegistered(newProduct)) {
            return false;
        }
        logger.trace("register() registering new product {}.", newProduct);
        existingProductsByUniqueIndex.put(newProduct.getProductUniqueIndex(), newProduct);
        memberCount++;
        return true;
    }

    public VeluxProduct get(String productUniqueIndex) {
        logger.trace("get({}) called.", productUniqueIndex);
        if (!isRegistered(productUniqueIndex)) {
            return null;
        }
        return existingProductsByUniqueIndex.get(productUniqueIndex);
    }

    public VeluxProduct[] values() {
        return existingProductsByUniqueIndex.values().toArray(new VeluxProduct[0]);
    }

    public int getNoMembers() {
        logger.trace("getNoMembers() returns {}.", memberCount);
        return memberCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(memberCount).append(" members: ");
        for (VeluxProduct product : this.values()) {
            sb.append(product.toString()).append(",");
        }
        if (sb.lastIndexOf(",") > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }
}

/**
 * end-of-VeluxExistingProducts.java
 */
