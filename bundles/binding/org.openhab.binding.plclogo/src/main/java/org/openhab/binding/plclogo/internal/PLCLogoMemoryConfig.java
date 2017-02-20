/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import java.util.HashMap;

import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Memory configruration for PLCLogo item binding
 * One memory configuration may be assigned for input and another for output for same item.
 *
 * @author Vladimir Grebenschikov
 * @author Alexander Falkenstern
 * @since 1.9.0
 */

public class PLCLogoMemoryConfig {
    private String block = null; // Logo-style block name like Q10
    private PLCLogoBlock.Kind kind = null; // normalized memory type VB|VW|I|Q|M|AO|AQ|AM

    private int address = -1; // address in logo memory block
    private int bit = -1; // informational bit (-1 if not used)
    private int index = -1; // normalized index of element (starting from 0)

    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    // @formatter:off
    private static final PLCLogoBlock PLCLogoBlock0BA7[] = {
            new PLCLogoBlock(PLCLogoBlock.Kind.VB,  0),
            new PLCLogoBlock(PLCLogoBlock.Kind.VW,  0),
            new PLCLogoBlock(PLCLogoBlock.Kind.I,   923), // I starts at 923 for 3 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.Q,   942), // Q starts at 942 for 2 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.M,   948), // M starts at 948 for 2 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.AI,  926), // AI starts at 926 for 8 words
            new PLCLogoBlock(PLCLogoBlock.Kind.AQ,  944), // AQ starts at 944 for 2 words
            new PLCLogoBlock(PLCLogoBlock.Kind.AM,  952)  // AM starts at 952 for 16 words

    };

    private static final PLCLogoBlock PLCLogoBlock0BA8[] = {
            new PLCLogoBlock(PLCLogoBlock.Kind.VB,  0),
            new PLCLogoBlock(PLCLogoBlock.Kind.VW,  0),
            new PLCLogoBlock(PLCLogoBlock.Kind.I,   1024), // I starts at 1024 for 8 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.Q,   1064), // Q starts at 1064 for 8 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.M,   1104), // M starts at 1104 for 14 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.AI,  1032), // AI starts at 1032 for 32 bytes -> 16 words
            new PLCLogoBlock(PLCLogoBlock.Kind.AQ,  1072), // AQ starts at 1072 for 32 bytes -> 16 words
            new PLCLogoBlock(PLCLogoBlock.Kind.AM,  1118), // Analog markers starts at 1118 for 128 bytes(64 words)
            new PLCLogoBlock(PLCLogoBlock.Kind.NI,  1246), // Network inputs starts at 1246 for 16 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.NAI, 1262), // Network analog inputs starts at 1262 for 128 bytes(64 words)
            new PLCLogoBlock(PLCLogoBlock.Kind.NQ,  1390), // Network outputs starts at 1390 for 16 bytes
            new PLCLogoBlock(PLCLogoBlock.Kind.NAQ, 1406)  // Network analog inputs starts at 1406 for 64 bytes(32 words)
    };
    // @formatter:on

    final static HashMap<PLCLogoBlock.Kind, PLCLogoBlock> PLCLogoBlock0BA7Map = makeBlocksMap(PLCLogoBlock0BA7);
    final static HashMap<PLCLogoBlock.Kind, PLCLogoBlock> PLCLogoBlock0BA8Map = makeBlocksMap(PLCLogoBlock0BA8);

    private static HashMap<PLCLogoBlock.Kind, PLCLogoBlock> makeBlocksMap(PLCLogoBlock blocks[]) {
        HashMap<PLCLogoBlock.Kind, PLCLogoBlock> bmap = new HashMap<PLCLogoBlock.Kind, PLCLogoBlock>();

        for (PLCLogoBlock b : blocks) {
            bmap.put(b.getKind(), b);
        }

        return bmap;
    }

    public PLCLogoMemoryConfig(String memory) throws BindingConfigParseException {
        String[] memparts = memory.split("\\.");

        block = memparts[0];
        if (block.length() < 2) {
            logger.error("Wrong block definition for {}", block);
            throw new BindingConfigParseException("Wrong block definition for " + block);
        }

        if (Character.isDigit(block.charAt(1))) {
            kind = PLCLogoBlock.Kind.valueOf(block.substring(0, 1).toUpperCase());
            index = Integer.parseInt(block.substring(1));
        } else if (Character.isDigit(block.charAt(2))) {
            kind = PLCLogoBlock.Kind.valueOf(block.substring(0, 2).toUpperCase());
            index = Integer.parseInt(block.substring(2));
        } else if (Character.isDigit(block.charAt(3))) {
            kind = PLCLogoBlock.Kind.valueOf(block.substring(0, 3).toUpperCase());
            index = Integer.parseInt(block.substring(3));
        } else {
            logger.error("Wrong block type detected: {}", block);
            throw new BindingConfigParseException("Wrong block type detected: " + block);
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

    public PLCLogoBlock.Kind getKind() {
        return this.kind;
    }

    public PLCLogoBlock getBlock(PLCLogoModel model) throws BindingConfigParseException {
        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            return PLCLogoBlock0BA7Map.get(kind);
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            return PLCLogoBlock0BA8Map.get(kind);
        } else {
            throw new BindingConfigParseException("Wrong model " + model);
        }
    }

    public int getAddress(PLCLogoModel model) throws BindingConfigParseException {
        // First time this function called, address is less 0 -> Calculate it
        if (address < 0) {
            address = getBlock(model).getAddress(index);
            logger.debug("Address of {} = {}", block, address + ((bit != -1) ? ("." + bit) : ""));
        }

        return address;
    }

    public int getBit(PLCLogoModel model) throws BindingConfigParseException {
        // First time this function called, bit is less 0 excepting VB/VW blocks -> Calculate it
        if ((bit < 0) && PLCLogoBlock.isBitwise(kind)) {
            bit = getBlock(model).getBit(index);

            if ((bit < 0) || (bit > 7)) {
                logger.error("Invalid bit {} for block {} found", bit, block);
                throw new BindingConfigParseException("Invalid bit " + bit + " for block " + block + " found");
            }
            logger.debug("Address of {} = {}.{}", block, address, bit);
        }

        return bit;
    }

    public boolean isInRange(PLCLogoModel model) {
        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            return (address >= 0) && (address < 984);
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            return (address >= 0) && (address <= 1469);
        } else {
            return false;
        }
    }

    public boolean isDigital() {
        return PLCLogoBlock.isBitwise(kind) || (PLCLogoBlock.isGeneral(kind) && (bit >= 0));
    }

    public boolean isInput() {
        return PLCLogoBlock.isInput(kind) || PLCLogoBlock.isGeneral(kind);
    }

    public boolean isOutput() {
        return !PLCLogoBlock.isInput(kind) || PLCLogoBlock.isGeneral(kind);
    }
}
