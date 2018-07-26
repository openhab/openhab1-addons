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

/**
 * <B>Velux</B> product status representation.
 * <P>
 * Combined set of information which describes a current state of a single Velux product.
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
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

    public VeluxProductState(BCproductState productState) {
        this(new VeluxProductReference(productState), productState.getActuator(), productState.getStatus());
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
}

/**
 * end-of-VeluxProductState.java
 */
