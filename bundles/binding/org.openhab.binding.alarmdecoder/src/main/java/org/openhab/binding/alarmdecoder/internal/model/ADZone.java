/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal.model;

/**
 * A class to hold our zone state
 *
 * @author Sean Mathews <coder@f34r.com>
 * @since 1.9.0
 *
 *        TODO: add timeouts
 */

public class ADZone {

    private int _id = 0;
    private boolean _state = false;

    /**
     * Constructor
     *
     * @param id - the zone id int type;
     */
    public ADZone(int id) {
        _id = id;
        _state = false;
    }

    /**
     * return zone state
     *
     * @return boolean - state of zone
     */
    public boolean getState() {
        return _state;
    }

    /**
     * set zone state
     *
     * @param state - new state
     */
    public void setState(boolean state) {
        _state = state;
    }

    /**
     * return zone ID
     *
     * @return int - state of zone
     */
    public int getID() {
        return _id;
    }

    /**
     * return true if this object equals another based upon ID
     *
     * @param Object to compare
     */
    @Override
    public boolean equals(Object object) {
        boolean equals = false;

        if (object != null && object instanceof ADZone) {
            equals = this._id == ((ADZone) object)._id;
        }

        return equals;
    }
}
