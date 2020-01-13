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
package org.openhab.binding.velux.things;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.binding.velux.bridge.comm.BCgetScenes;
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
 * @since 1.13.0
 */
public class VeluxProductReference {
    private final Logger logger = LoggerFactory.getLogger(VeluxProductReference.class);

    // Class internal

    private VeluxProductName name;
    private VeluxProductType typeId;

    // Constructor

    public VeluxProductReference(VeluxProduct product) {
        this.name = product.getProductName();
        this.typeId = product.getProductType();
    }

    public VeluxProductReference(VeluxProductName name, int type) {
        this.name = name;
        this.typeId = VeluxProductType.get(type);
        if (this.typeId == null) {
            logger.warn(
                    "Please report this to maintainer of the {} binding: VeluxProductReference({}) has found an unregistered ProductTypeId.",
                    VeluxBindingConstants.BINDING_ID, type);
        }
    }

    // Class access methods

    public VeluxProductName getProductName() {
        return this.name;
    }

    public VeluxProductType getProductType() {
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

    @Deprecated
    public VeluxProductReference(BCgetScenes.BCproductState productState) {
    }

    @Deprecated
    public VeluxProduct.ProductName getName() {
        return null;
    }

    @Deprecated
    public VeluxProduct.ProductTypeId getTypeId() {
        return null;
    }

}
