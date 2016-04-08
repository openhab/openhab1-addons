/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

/**
 * Definition of the Homematic interfaces.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public enum HmInterface {
    RF,
    WIRED,
    HMIP,
    CUXD,
    TCL,
    HOMEGEAR,
    VIRTUALDEVICES;

    @Override
    public String toString() {
        switch (this) {
            case RF:
                return "BidCos-RF";
            case WIRED:
                return "BidCos-Wired";
            case HMIP:
                return "HmIP-RF";
            case CUXD:
                return "CUxD";
            case TCL:
                return "Tcl-Rega";
            case HOMEGEAR:
                return "Homegear";
            case VIRTUALDEVICES:
                return "VirtualDevices";
        }
        return "";
    }

    /**
     * Returns the Homematic server ports of the interfaces.
     */
    public int getPort() {
        switch (this) {
            case RF:
            case VIRTUALDEVICES:
            case HOMEGEAR:
                return 2001;
            case WIRED:
                return 2000;
            case HMIP:
                return 2010;
            case CUXD:
                return 8701;
            case TCL:
                return 8181;
        }
        return 0;
    }

    /**
     * Parses the string and returns the HmInterface object.
     */
    public static HmInterface parse(String interfaceType) {
        if (interfaceType == null) {
            return null;
        } else if (RF.toString().equals(interfaceType)) {
            return RF;
        } else if (VIRTUALDEVICES.toString().equals(interfaceType)) {
            return VIRTUALDEVICES;
        } else if (WIRED.toString().equals(interfaceType)) {
            return WIRED;
        } else if (HMIP.toString().equals(interfaceType)) {
            return HMIP;
        } else if (CUXD.toString().equals(interfaceType)) {
            return CUXD;
        } else if (HOMEGEAR.toString().equals(interfaceType)) {
            return HOMEGEAR;
        } else {
            return null;
        }
    }

}
