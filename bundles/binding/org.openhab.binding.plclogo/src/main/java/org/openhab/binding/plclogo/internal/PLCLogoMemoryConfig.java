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
    private String kind; // normalized memory type VB|VW|I|Q|M|AO|AQ|AM
    private int address; // address in logo memory block
    private int bit; // informational bit (-1 if not used)
    private PLCLogoModel model = PLCLogoModel.LOGO_MODEL_0BA7;

    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    public PLCLogoMemoryConfig(String memory) throws BindingConfigParseException {
        this.bit = -1;

        String[] memparts = memory.split("\\.");
        location = memparts[0];
        if (memparts.length > 1) {
            logger.debug("Memory map parts {} : {}", memparts[0], memparts[1]);
            bit = Integer.parseInt(memparts[1]);
            if (bit > 7) {
                throw new BindingConfigParseException("Invalid bit in " + memory + " - bit should be 0-7");
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
        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            return (address > 849) && (address < 942);
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            return (address >= 0) && (address <= 1469);
        } else {
            return false;
        }
    }

    public void setModel(PLCLogoModel logoModel) {
        model = logoModel;
    }

    // bit location[0] and real mem loc[1]
    private void parseAddress(String memloc) throws BindingConfigParseException {
        if (memloc.length() < 2) {
            return;
        }

        int idx; // normalized index of element (starting from 0), for all but VB|VW
        if (Character.isDigit(memloc.charAt(1))) {
            kind = memloc.substring(0, 1).toUpperCase();
            idx = Integer.parseInt(memloc.substring(1)) - 1;
        } else if (Character.isDigit(memloc.charAt(2))) {
            kind = memloc.substring(0, 2).toUpperCase();
            idx = Integer.parseInt(memloc.substring(2)) - 1;
        } else if (Character.isDigit(memloc.charAt(3))) {
            kind = memloc.substring(0, 3).toUpperCase();
            idx = Integer.parseInt(memloc.substring(3)) - 1;
        } else {
            logger.error("Wrong block type detected: {}", kind);
            throw new BindingConfigParseException("Wrong block type detected: " + kind);
        }

        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            if (kind.equals("I")) {
                address = 923 + idx / 8; // I starts at 923 for 3 bytes
                bit = idx % 8;
            } else if (kind.equals("Q")) {
                address = 942 + idx / 8; // Q starts at 942 for 2 bytes
                bit = idx % 8;
            } else if (kind.equals("M")) {
                address = 948 + idx / 8; // Markers starts at 948 for 2 bytes
                bit = idx % 8;
            } else if (kind.equals("AI")) {
                address = 926 + idx * 2; // AI starts at 926 for 8 words
            } else if (kind.equals("AQ")) {
                address = 944 + idx * 2; // AQ starts at 944 for 2 words
            } else if (kind.equals("AM")) {
                address = 952 + idx * 2; // AM starts at 952 for 16 words
            } else if (kind.equals("VB") || kind.equals("VW")) {
                int dot = memloc.indexOf(".", 2);
                address = Integer.parseInt(memloc.substring(2, dot < 0 ? memloc.length() : dot));
                if (dot >= 0) {
                    bit = Integer.parseInt(memloc.substring(dot + 1, memloc.length()));
                }
            } else {
                throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC");
            }
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            if (kind.equals("I")) {
                address = 1024 + idx / 8; // I starts at 1024 for 8 bytes
                bit = idx % 8;
            } else if (kind.equals("Q")) {
                address = 1064 + idx / 8; // Q starts at 1064 for 8 bytes
                bit = idx % 8;
            } else if (kind.equals("M")) {
                address = 1104 + idx / 8; // Markers starts at 1104 for 14 bytes
                bit = idx % 8;
            } else if (kind.equals("AI")) {
                address = 1032 + idx * 2; // AI starts at 1032 for 32 bytes -> 16 words
            } else if (kind.equals("AQ")) {
                address = 1072 + idx * 2; // AQ starts at 1072 for 32 bytes -> 16 words
            } else if (kind.equals("AM")) {
                address = 1118 + idx * 2; // Analog markers starts at 1118 for 128 bytes -> 64 words
            } else if (kind.equals("NI")) {
                address = 1246 + idx * 2; // Network inputs starts at 1246 for 16 bytes
                bit = idx % 8;
            } else if (kind.equals("NAI")) {
                address = 1262 + idx * 2; // Network analog inputs starts at 1262 for 128 bytes -> 64 words
            } else if (kind.equals("NQ")) {
                address = 1390 + idx * 2; // Network outputs starts at 1390 for 16 bytes
                bit = idx % 8;
            } else if (kind.equals("NAQ")) {
                address = 1406 + idx * 2; // Network analog inputs starts at 1406 for 64 bytes -> 32 words
            } else if (kind.equals("VB") || kind.equals("VW")) {
                int dot = memloc.indexOf(".", 2);
                address = Integer.parseInt(memloc.substring(2, dot < 0 ? memloc.length() : dot));
                if (dot >= 0) {
                    bit = Integer.parseInt(memloc.substring(dot + 1, memloc.length()));
                }
            } else {
                throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC");
            }
        }
        logger.debug("Memory map for {} = {}", memloc, address + ((bit != -1) ? ("." + bit) : ""));
        return;
    }

    public boolean isDigital() {
        boolean result = kind.equals("I") || kind.equals("Q") || kind.equals("M");
        result = result || kind.equals("NI") || kind.equals("NQ");
        result = result || ((kind.equals("VB") || kind.equals("VW")) && (getBit() >= 0));
        return result;
    }

    public boolean isInput() {
        boolean result = kind.equals("I") || kind.equals("AI");
        result = result || kind.equals("NI") || kind.equals("NAI");
        result = result || kind.equals("VB") || kind.equals("VW");
        return result;
    }

}
