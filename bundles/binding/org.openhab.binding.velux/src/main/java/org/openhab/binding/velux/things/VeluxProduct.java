/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.velux.bridge.comm.BCgetProducts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product representation.
 * <P>
 * Combined set of information describing a single Velux product.
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxProduct {
    private final Logger logger = LoggerFactory.getLogger(VeluxProduct.class);

    // Type definitions

    public static class ProductName {
        private String name;

        @Override
        public String toString() {
            return name;
        }

        ProductName(String name) {
            this.name = name;
        }
    }

    public enum ProductTypeId {
        ROLLER_SHUTTER(2, 0),
        OTHER(3, 0),
        WINDOW_OPENER(4, 1),
        SOLAR_SLIDER(10, 2),
        UNDEFTYPE(-1);

        // Class internal

        private int typeId;
        private int subtype;

        // Reverse-lookup map for getting a ProductTypeId from an TypeId
        private static final Map<Integer, ProductTypeId> lookupTypeId2Enum = new HashMap<Integer, ProductTypeId>();

        static {
            for (ProductTypeId typeId : ProductTypeId.values()) {
                lookupTypeId2Enum.put(typeId.getTypeId(), typeId);
            }
        }

        // Constructor

        ProductTypeId(int typeId) {
            this.typeId = typeId;
            this.subtype = 0;
        }

        ProductTypeId(int typeId, int subTypeId) {
            this.typeId = typeId;
            this.subtype = subTypeId;
        }

        ProductTypeId(String categoryString) {
            try {
                this.typeId = ProductTypeId.valueOf(categoryString).getTypeId();
                this.subtype = ProductTypeId.valueOf(categoryString).getSubtype();
            } catch (IllegalArgumentException e) {
                try {
                    this.typeId = ProductTypeId.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
                            .getTypeId();
                    this.subtype = ProductTypeId.valueOf(categoryString.replaceAll("\\p{C}", "_").toUpperCase())
                            .getSubtype();
                } catch (IllegalArgumentException e2) {
                    this.typeId = -1;
                }
            }
        }

        // Class access methods

        public int getTypeId() {
            return typeId;
        }

        public int getSubtype() {
            return subtype;
        }

        public static ProductTypeId get(int typeId) {
            return lookupTypeId2Enum.get(typeId);
        }

    }

    public static class ProductBridgeIndex {
        private int id;

        int toInt() {
            return id;
        }

        ProductBridgeIndex(int id) {
            this.id = id;
        }
    }

    // Class internal

    private ProductName name;
    private ProductTypeId typeId;
    private ProductBridgeIndex bridgeProductIndex;

    // Constructor

    public VeluxProduct(VeluxProduct product) {
        this.name = product.getName();
        this.bridgeProductIndex = product.getBridgeProductIndex();
        this.typeId = product.getTypeId();
    }

    public VeluxProduct(BCgetProducts.BCproduct productDescr) {
        this.name = new ProductName(productDescr.getName());
        this.bridgeProductIndex = new ProductBridgeIndex(productDescr.getId());
        String productTypeId = productDescr.getCategory().replaceAll("\\s", "_").toUpperCase();

        try {
            this.typeId = ProductTypeId.valueOf(productTypeId);
        } catch (IllegalArgumentException iae) {
            logger.warn(
                    "Please report this to maintainer: VeluxProduct({},{}) has found an unregistered ProductTypeId.",
                    productTypeId, productDescr.getTypeId());
        }
        if (this.getTypeId().getTypeId() != productDescr.getTypeId()) {
            logger.warn(
                    "Please report this to maintainer: VeluxProduct({},{}) has found two different typeIds which differ ({} vs. {}.",
                    productTypeId, productDescr.getTypeId(), this.getTypeId().getTypeId(), productDescr.getTypeId());
        }
    }

    // Class access methods

    public ProductName getName() {
        return this.name;
    }

    public ProductTypeId getTypeId() {
        return this.typeId;
    }

    public ProductBridgeIndex getBridgeProductIndex() {
        return this.bridgeProductIndex;
    }

    @Override
    public String toString() {
        return String.format("Product \"%s\"/%s (bridgeIndex %d)", this.name, this.typeId,
                this.bridgeProductIndex.toInt());
    }

    // Class helper methods

    public String getProductUniqueIndex() {
        return this.name.toString().concat("#").concat(this.typeId.toString());
    }

}

/**
 * end-of-VeluxProduct.java
 */
