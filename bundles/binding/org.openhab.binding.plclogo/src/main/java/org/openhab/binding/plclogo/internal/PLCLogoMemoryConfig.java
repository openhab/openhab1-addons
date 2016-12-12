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
import java.util.Map;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.binding.plclogo.internal.PLCLogoBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLCLogoMemoryConfig {
    private String location;         // Logo-style memory location like Q10
    private PLCLogoBlock.Kind kind;  // normalized memory type VB|VW|I|Q|M|AO|AQ|AM
    private int idx;                 // index of specified memory type
    private int bit;                 // informational bit (-1 if not used)
    private PLCLogoBlock block;
    private PLCLogoModel model;

    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    private static final PLCLogoBlock PLCLogoBlock0BA7[] = {
              new PLCLogoBlock(PLCLogoBlock.Kind.I,    923), // I starts at 923 for 3 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.Q,    942), // Q starts at 942 for 2 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.M,    948), // M starts at 948 for 2 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.AI,   926), // AI starts at 926 for 8 words
              new PLCLogoBlock(PLCLogoBlock.Kind.AQ,   944), // AQ starts at 944 for 2 words
              new PLCLogoBlock(PLCLogoBlock.Kind.AM,   952), // AM starts at 952 for 16 words

              new PLCLogoBlock(PLCLogoBlock.Kind.VB,   0),
              new PLCLogoBlock(PLCLogoBlock.Kind.VW,   0)
    };

    private static final PLCLogoBlock PLCLogoBlock0BA8[] = {
              new PLCLogoBlock(PLCLogoBlock.Kind.I,   1024), // I starts at 1024 for 8 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.Q,   1064), // Q starts at 1064 for 8 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.M,   1104), // M starts at 1104 for 14 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.AI,  1032), // AI starts at 1032 for 32 bytes -> 16 words
              new PLCLogoBlock(PLCLogoBlock.Kind.AQ,  1072), // AQ starts at 1072 for 32 bytes -> 16 words
              new PLCLogoBlock(PLCLogoBlock.Kind.AM,  1118), // Analog markers starts at 1118 for 128 bytes -> 64 words
              new PLCLogoBlock(PLCLogoBlock.Kind.NI,  1246), // Network inputs starts at 1246 for 16 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.NAI, 1262), // Network analog inputs starts at 1262 for 128 bytes -> 64 words
              new PLCLogoBlock(PLCLogoBlock.Kind.NQ,  1390), // Network outputs starts at 1390 for 16 bytes
              new PLCLogoBlock(PLCLogoBlock.Kind.NAQ, 1406), // Network analog inputs starts at 1406 for 64 bytes -> 32 words

              new PLCLogoBlock(PLCLogoBlock.Kind.VB, 0),
              new PLCLogoBlock(PLCLogoBlock.Kind.VW, 0)
    };

    final static HashMap<PLCLogoBlock.Kind, PLCLogoBlock> PLCLogoBlock0BA7Map = makeBlocksMap(PLCLogoBlock0BA7);
    final static HashMap<PLCLogoBlock.Kind, PLCLogoBlock> PLCLogoBlock0BA8Map = makeBlocksMap(PLCLogoBlock0BA8);


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

        if (location.length() < 2) {
            throw new BindingConfigParseException("Wrong block definition for " + location);
        }

        if (Character.isDigit(location.charAt(1))) {
            this.kind = PLCLogoBlock.Kind.valueOf(location.substring(0, 1).toUpperCase());
            this.idx = Integer.parseInt(location.substring(1)) - 1;
        } else if (Character.isDigit(location.charAt(2))) {
            this.kind = PLCLogoBlock.Kind.valueOf(location.substring(0, 2).toUpperCase());
            this.idx = Integer.parseInt(location.substring(2)) - 1;
        } else if (Character.isDigit(location.charAt(3))) {
            this.kind = PLCLogoBlock.Kind.valueOf(location.substring(0, 3).toUpperCase());
            this.idx = Integer.parseInt(location.substring(3)) - 1;
        } else {
            logger.error("Wrong block type detected: {}", kind);
            throw new BindingConfigParseException("Wrong block type detected: " + kind);
        }

        if (kind == PLCLogoBlock.Kind.VW || kind == PLCLogoBlock.Kind.VB) {
            int dot = location.indexOf(".", 2);
            this.idx = Integer.parseInt(location.substring(2, dot < 0 ? location.length() : dot));
            if (dot >= 0)
                  this.bit = Integer.parseInt(location.substring(dot + 1, location.length()));
        }
    }

    private static HashMap<PLCLogoBlock.Kind, PLCLogoBlock> makeBlocksMap(PLCLogoBlock blocks[])
    {
        HashMap<PLCLogoBlock.Kind, PLCLogoBlock> bmap = new HashMap<PLCLogoBlock.Kind, PLCLogoBlock>();

        for (PLCLogoBlock b : blocks) {
            bmap.put(b.getKind(), b);
        }

        return bmap;
    }

    public String getLocation() {
        return this.location;
    }

    public PLCLogoBlock.Kind getKind() {
        return this.kind;
    }

    public PLCLogoBlock getBlock()
    {
        return block;
    }

    public int getAddress() {
        return block.getAddress(idx);
    }

    public int getBit() {
        return block.getBit(idx, bit);
    }

    public boolean isInRange() {
        int address = getAddress();
        if (model == PLCLogoModel.LOGO_MODEL_0BA7) {
            return (address > 849) && (address < 942);
        } else if (model == PLCLogoModel.LOGO_MODEL_0BA8) {
            return (address >= 0) && (address <= 1469);
        } else {
            return false;
        }
    }

    public void setModel(PLCLogoModel logoModel) throws BindingConfigParseException {
        model = logoModel;
        HashMap<PLCLogoBlock.Kind, PLCLogoBlock> bmap =
                (model == PLCLogoModel.LOGO_MODEL_0BA7) ? PLCLogoBlock0BA7Map : PLCLogoBlock0BA8Map;

        if (model == PLCLogoModel.LOGO_MODEL_0BA7)
            bmap = PLCLogoBlock0BA7Map;
        else
        if (model == PLCLogoModel.LOGO_MODEL_0BA8)
            bmap = PLCLogoBlock0BA8Map;
        else
            throw new BindingConfigParseException("Wrong Model " + logoModel);

        PLCLogoBlock b = bmap.get(kind);

        if (b == null)
            throw new BindingConfigParseException("Logo memory " + kind + " is not supported on PLC " + model);

        block = b;
    }

    public boolean isDigital() {
        return block.isBitwise() || (block.isGeneral() && (getBit() >= 0));
    }

    public boolean isInput() {
        return block.isInput() || block.isGeneral();
    }

}
