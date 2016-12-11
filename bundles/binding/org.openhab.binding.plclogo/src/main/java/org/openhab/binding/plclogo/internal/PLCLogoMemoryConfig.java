/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLCLogoMemoryConfig {
    private String location; // Logo-style memory location like Q10
    private String kind; // normalizaed memory type VB|VW|I|Q|M|AO|AQ|AM
    private int address; // address in logo memory block
    private int bit; // informational bit (-1 if not used)
    private PLCLogoModel model = PLCLogoModel.LOGO_MODEL_0BA7;

    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    public PLCLogoMemoryConfig(String mem) throws BindingConfigParseException {
        String[] memparts = mem.split("\\.");
        location = memparts[0];
        bit = -1;
        if (memparts.length > 1) {
            logger.debug("Memory map parts " + memparts[0] + " : " + memparts[1]);
            bit = Integer.parseInt(memparts[1]);
            if (bit > 7) {
                throw new BindingConfigParseException("Invalid bit in " + mem + " - bit should be 0-7");
            }
        }

        parseAddress(location);
    }

    public String getLocation() {
        return this.location;
    }

    public String getKind() {
        return this.kind;
    }

    public int getAddress() {
        return this.address;
    }

    public int getBit() {
        return this.bit;
    }

    public boolean isInRange() {
        return address > 849 && address < 942; // should be 0-1469 for BA08
    }

    public void setModel(PLCLogoModel logoModel) {
        model = logoModel;
    }

    // bit location[0] and real mem loc[1]
    private void parseAddress(String memloc) throws BindingConfigParseException {
        // I , Q and M have bit values derived: I1 is equivalent to VB923.0
        // TODO Add some validation to input parameters!

        if (memloc.length() < 2) {
            return;
        }

        int idx; // normalized index of element (starting from 0), for all but VB|VW
        if (Character.isDigit(memloc.charAt(1))) {
            kind = memloc.substring(0, 1).toUpperCase();
            idx = Integer.parseInt(memloc.substring(1)) - 1;
        } else {
            kind = memloc.substring(0, 2).toUpperCase();
            idx = Integer.parseInt(memloc.substring(2)) - 1;
        }

        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            if (kind.equals("VB") || kind.equals("VW")) {
                address = Integer.parseInt(memloc.substring(2));
            } else if (kind.equals("I")) {
                // I starts at 923 for three bytes
                address = 923 + idx / 8;
                bit = idx % 8;
            } else if (kind.equals("Q")) {
                // Q starts at 942 for two bytes
                address = 942 + idx / 8;
                bit = idx % 8;
            } else if (kind.equals("M")) {
                // Markers starts at 948 for two bytes
                address = 948 + idx / 8;
                bit = idx % 8;
            } else if (kind.equals("AI")) {
                // AI starts at 926 for 8 words
                address = 926 + idx * 2;
            } else if (kind.equals("AQ")) {
                // AQ starts at 944 for 2 words
                address = 944 + idx * 2;
            } else if (kind.equals("AM")) {
                // AM starts at 952 for 16 words
                address = 952 + idx * 2;
            } else {
                throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC");
            }
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            if (kind.equals("VB") || kind.equals("VW")) {
                address = Integer.parseInt(memloc.substring(2));
            } else if (kind.equals("I")) {
                // I starts at 923 for three bytes
                address = 1024 + idx / 8;
                bit = idx % 8;
            } else if (kind.equals("Q")) {
                // Q starts at 942 for two bytes
                address = 1064 + idx / 8;
                bit = idx % 8;
            } else if (kind.equals("M")) {
                // Markers starts at 948 for two bytes
                address = 1104 + idx / 8;
                bit = idx % 8;
            } else if (kind.equals("AI")) {
                // AI starts at 926 for 8 words
                address = 1032 + idx * 2;
            } else if (kind.equals("AQ")) {
                // AQ starts at 944 for 2 words
                address = 1072 + idx * 2;
            } else if (kind.equals("AM")) {
                // AM starts at 952 for 16 words
                address = 952 + idx * 2;
            } else if (kind.equals("NI")) {
                // AM starts at 952 for 16 words
                address = 1246 + idx * 2;
            } else if (kind.equals("NAI")) {
                // AM starts at 952 for 16 words
                address = 1246 + idx * 2;
            } else if (kind.equals("NQ")) {
                // AM starts at 952 for 16 words
                address = 1390 + idx * 2;
            } else if (kind.equals("NAQ")) {
                // AM starts at 952 for 16 words
                address = 1406 + idx * 2;
            } else {
                throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC");
            }
        }

        logger.debug("Memory map for " + memloc + " = " + address + ((bit != -1) ? ("." + bit) : ""));
        return;
    }

    public boolean isDigital() {
        return kind.equals("I") || kind.equals("Q") || kind.equals("M") || kind.equals("NI") || kind.equals("NQ")
                || ((kind.equals("VB") || kind.equals("VW")) && (getBit() >= 0));
    }

    public boolean isInput() {
        return kind.equals("I") || kind.equals("AI") || kind.equals("NI") || kind.equals("NAI") || kind.equals("VB")
                || kind.equals("VW");
    }

}
