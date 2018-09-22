/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import org.openhab.binding.velux.bridge.comm.BCgetScenes.BCproductState;
import org.openhab.binding.velux.things.VeluxProduct.ProductName;
import org.openhab.binding.velux.things.VeluxProduct.ProductTypeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product reference representation.
 * <P>
 * Combined set of information with reference towards a single Velux product.
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxProductReference {
    private final Logger logger = LoggerFactory.getLogger(VeluxProductReference.class);

    // Class internal

    private ProductName name;
    private ProductTypeId typeId;

    // Constructor

    public VeluxProductReference(BCproductState productState) {
        this.name = new ProductName(productState.getName());
        this.typeId = ProductTypeId.get(productState.getTypeId());
        if (this.typeId == null) {
            logger.warn(
                    "Please report this to maintainer: VeluxProductReference({}) has found an unregistered ProductTypeId.",
                    productState.getTypeId());
        }
    }

    public VeluxProductReference(VeluxProduct product) {
        this.name = product.getName();
        this.typeId = product.getTypeId();
    }

    // Class access methods

    public ProductName getName() {
        return this.name;
    }

    public ProductTypeId getTypeId() {
        return this.typeId;
    }

    @Override
    public String toString() {
        return String.format("Prod.ref. \"%s\"/%s", this.name, this.typeId);
    }

    // Class helper methods

    public String getProductUniqueIndex() {
        return this.name.toString().concat("#").concat(this.typeId.toString());
    }

}

/**
 * end-of-VeluxProductReference.java
 */
