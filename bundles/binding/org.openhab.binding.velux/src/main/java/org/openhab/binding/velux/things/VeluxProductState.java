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

import org.openhab.binding.velux.bridge.comm.BCgetScenes;

/**
 * <B>Velux</B> product status representation.
 * <P>
 * Combined set of information which describes a current state of a single Velux product.
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
 * @since 1.13.0
 */
public class VeluxProductState {

    // Type definitions

    public class ProductState {

        private int state;

        public int getState() {
            return state;
        }

        ProductState(int state) {
            this.state = state;
        }
    }

    // Class internal

    private VeluxProductReference productReference;
    private int actuator;
    private ProductState state;

    // Constructor

    public VeluxProductState(VeluxProductReference productReference, int actuator, int state) {
        this.productReference = productReference;
        this.actuator = actuator;
        this.state = new ProductState(state);
    }

    // Class access methods

    public VeluxProductReference getProductReference() {
        return this.productReference;
    }

    public int getActuator() {
        return this.actuator;
    }

    public ProductState getState() {
        return this.state;
    }

    public int getStateAsInt() {
        return this.state.getState();
    }

    @Override
    public String toString() {
        return String.format("State (%s, actuator %d, value %d)", this.productReference, this.actuator, this.state);
    }

    @Deprecated
    public VeluxProductState(BCgetScenes.BCproductState productState) {
    }
}
