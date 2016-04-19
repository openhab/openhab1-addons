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
 * @since 1.6.0
 */

public class ADZone {

    private int _id = 0;
    private double _ts = 0;
    private boolean _state = false;

    /**
     * Constructor
     *
     * @param id - the zone id int type;
     */
    public ADZone(int id) {
        _id = id;
        _state = false;
        _ts = System.nanoTime();
    }

    public void updateTime() {
        _ts = System.nanoTime();
    }

    /**
     * return diff of zone time and provided time in seconds.
     * time is stored in nanoseconds
     *
     * @param ts
     * @return double the time difference
     */
    public double timeDiff(double ts) {
        return (ts - _ts) / 1000000000;
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
     * return diff of zone time and provided time in seconds.
     * time is stored in nanoseconds
     *
     * @param Object to search
     * @return double the time difference
     */
    @Override
    public boolean equals(Object object) {
        boolean equals = false;

        if (object != null && object instanceof ADZone)
        {
            equals = this._id == ((ADZone) object)._id;
        }

        return equals;
    }
}
