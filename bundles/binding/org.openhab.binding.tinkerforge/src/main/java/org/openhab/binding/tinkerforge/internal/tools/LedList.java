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
package org.openhab.binding.tinkerforge.internal.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LedList {
    // List of led Numbers
    private ArrayList<Integer> ledNumbers = new ArrayList<Integer>();
    // map: key is int of startled, value is range as short
    private Map<Integer, Short> ledRanges = new HashMap<Integer, Short>();
    // the led which is used to get the current color from the bricklet
    private Integer trackingled;

    public ArrayList<Integer> getLedNumbers() {
        return ledNumbers;
    }

    public void addLed(Integer led) {
        this.ledNumbers.add(led);
    }

    public Map<Integer, Short> getLedRanges() {
        return ledRanges;
    }

    public void addLedRange(Integer startLed, Short range) {
        ledRanges.put(startLed, range);
    }

    public Integer getTrackingled() {
        return trackingled;
    }

    public void setTrackingled(Integer trackingled) {
        this.trackingled = trackingled;
    }

    public boolean hasTrackingled() {
        return trackingled != null ? true : false;
    }

    public boolean hasLeds() {
        return !ledNumbers.isEmpty();
    }

    public boolean hasLedRanges() {
        return !ledRanges.isEmpty();
    }
}