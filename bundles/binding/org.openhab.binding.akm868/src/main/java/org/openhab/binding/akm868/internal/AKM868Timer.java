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
package org.openhab.binding.akm868.internal;

/**
 * This is self composed Timer
 *
 * @author Michael Heckmann
 * @since 1.8.0
 */
public class AKM868Timer {

    private long startTime;
    private int id;

    public AKM868Timer(int id) {
        this.id = id;
        startTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long duration() {
        long now = System.currentTimeMillis();
        return now - startTime;
    }

    public void restart() {
        startTime = System.currentTimeMillis();
    }

    public boolean hasTimedOut(long timeout) {
        long now = System.currentTimeMillis();
        long difference = now - startTime;
        if (difference > timeout) {
            return true;
        } else {
            return false;
        }
    }

}
