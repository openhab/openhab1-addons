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
    private String block = null; // Logo-style block name like Q10
    private String kind = null; // normalized memory type VB|VW|I|Q|M|AO|AQ|AM
    private int address = -1; // address in logo memory block
    private int bit = -1; // informational bit (-1 if not used)

    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    public PLCLogoMemoryConfig(String memory) throws BindingConfigParseException {
        String[] memparts = memory.split("\\.");
        block = memparts[0];
        if (Character.isDigit(block.charAt(1))) {
            kind = block.substring(0, 1).toUpperCase();
        } else if (Character.isDigit(block.charAt(2))) {
            kind = block.substring(0, 2).toUpperCase();
        } else if (Character.isDigit(block.charAt(3))) {
            kind = block.substring(0, 3).toUpperCase();
        } else {
            logger.error("Wrong block type detected: {}", kind);
            throw new BindingConfigParseException("Wrong block type detected: " + kind);
        }

        if (memparts.length > 1) {
            logger.debug("Memory map parts {} : {}", memparts[0], memparts[1]);
            bit = Integer.parseInt(memparts[1]);
            if ((bit < 0) || (bit > 7)) {
                logger.error("Invalid bit {} for block {} found", bit, block);
                throw new BindingConfigParseException("Invalid bit " + bit + " for block " + block + " found");
            }
        }
    }

    public String getBlockName() {
        return this.block;
    }

    public int getAddress(PLCLogoModel model) throws BindingConfigParseException {
        if (block.length() < 2) {
            return this.address;
        }

        if (this.address < 0) {
            int index = -1; // normalized index of element (starting from 0), for all but VB|VW
            if (Character.isDigit(block.charAt(1))) {
                index = Integer.parseInt(block.substring(1)) - 1;
            } else if (Character.isDigit(block.charAt(2))) {
                index = Integer.parseInt(block.substring(2)) - 1;
            } else if (Character.isDigit(block.charAt(3))) {
                index = Integer.parseInt(block.substring(3)) - 1;
            }

            if (index < 0) {
                logger.error("Wrong block type detected: {}", kind);
                throw new BindingConfigParseException("Wrong block type detected: " + kind);
            }

            if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
                if (kind.equals("I")) {
                    address = 923 + index / 8; // I starts at 923 for 3 bytes
                } else if (kind.equals("Q")) {
                    address = 942 + index / 8; // Q starts at 942 for 2 bytes
                } else if (kind.equals("M")) {
                    address = 948 + index / 8; // Markers starts at 948 for 2 bytes
                } else if (kind.equals("AI")) {
                    address = 926 + index * 2; // AI starts at 926 for 8 words
                } else if (kind.equals("AQ")) {
                    address = 944 + index * 2; // AQ starts at 944 for 2 words
                } else if (kind.equals("AM")) {
                    address = 952 + index * 2; // AM starts at 952 for 16 words
                } else if (kind.equals("VB") || kind.equals("VW")) {
                    int dot = block.indexOf(".", 2);
                    address = Integer.parseInt(block.substring(2, dot < 0 ? block.length() : dot));
                } else {
                    throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC");
                }
            } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
                if (kind.equals("I")) {
                    address = 1024 + index / 8; // I starts at 1024 for 8 bytes
                } else if (kind.equals("Q")) {
                    address = 1064 + index / 8; // Q starts at 1064 for 8 bytes
                } else if (kind.equals("M")) {
                    address = 1104 + index / 8; // Markers starts at 1104 for 14 bytes
                } else if (kind.equals("AI")) {
                    address = 1032 + index * 2; // AI starts at 1032 for 32 bytes -> 16 words
                } else if (kind.equals("AQ")) {
                    address = 1072 + index * 2; // AQ starts at 1072 for 32 bytes -> 16 words
                } else if (kind.equals("AM")) {
                    address = 1118 + index * 2; // Analog markers starts at 1118 for 128 bytes -> 64 words
                } else if (kind.equals("NI")) {
                    address = 1246 + index * 2; // Network inputs starts at 1246 for 16 bytes
                } else if (kind.equals("NAI")) {
                    address = 1262 + index * 2; // Network analog inputs starts at 1262 for 128 bytes -> 64 words
                } else if (kind.equals("NQ")) {
                    address = 1390 + index * 2; // Network outputs starts at 1390 for 16 bytes
                } else if (kind.equals("NAQ")) {
                    address = 1406 + index * 2; // Network analog inputs starts at 1406 for 64 bytes -> 32 words
                } else if (kind.equals("VB") || kind.equals("VW")) {
                    int dot = block.indexOf(".", 2);
                    address = Integer.parseInt(block.substring(2, dot < 0 ? block.length() : dot));
                } else {
                    throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC");
                }
            }
            logger.debug("Memory map for {} = {}", block, address + ((bit != -1) ? ("." + bit) : ""));
        }

        return this.address;
    }

    public int getBit(PLCLogoModel model) throws BindingConfigParseException {
        if (block.length() < 2) {
            return this.bit;
        }

        if ((this.bit < 0) && isDigital()) {
            int index = -1; // normalized index of element (starting from 0), for all but VB|VW
            if (Character.isDigit(block.charAt(1))) {
                index = Integer.parseInt(block.substring(1)) - 1;
            } else if (Character.isDigit(block.charAt(2))) {
                index = Integer.parseInt(block.substring(2)) - 1;
            } else if (Character.isDigit(block.charAt(3))) {
                index = Integer.parseInt(block.substring(3)) - 1;
            }

            if (index < 0) {
                logger.error("Wrong block type detected: {}", kind);
                throw new BindingConfigParseException("Wrong block type detected: " + kind);
            }

            bit = index % 8;
            if (kind.equals("VB") || kind.equals("VW")) {
                int dot = block.indexOf(".", 2);
                if (dot >= 0) {
                    bit = Integer.parseInt(block.substring(dot + 1, block.length()));
                }
            }

            if ((bit < 0) || (bit > 7)) {
                logger.error("Invalid bit {} for block {} found", bit, block);
                throw new BindingConfigParseException("Invalid bit " + bit + " for block " + block + " found");
            }
            logger.debug("Memory map for {} = {}.{}", block, address, bit);
        }

        return this.bit;
    }

    public boolean isInRange(PLCLogoModel model) {
        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            return (address > 849) && (address < 942);
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            return (address >= 0) && (address <= 1469);
        } else {
            return false;
        }
    }

    public boolean isDigital() {
        boolean result = kind.equals("I") || kind.equals("Q") || kind.equals("M");
        result = result || kind.equals("NI") || kind.equals("NQ");
        result = result || ((kind.equals("VB") || kind.equals("VW")) && (bit >= 0));
        return result;
    }

    public boolean isInput() {
        boolean result = kind.equals("I") || kind.equals("M") || kind.equals("AI");
        result = result || kind.equals("NI") || kind.equals("NAI");
        result = result || kind.equals("VB") || kind.equals("VW");
        return result;
    }

    public boolean isOutput() {
        boolean result = kind.equals("Q") || kind.equals("M") || kind.equals("AQ");
        result = result || kind.equals("NQ") || kind.equals("NAQ");
        result = result || kind.equals("VB") || kind.equals("VW");
        return result;
    }
}
