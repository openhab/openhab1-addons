/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

/**
 * PLCLogo Block represented by this class, as well as address/bit calculations
 *
 * @author Vladimir Grebenschikov
 * @author Alexander Falkenstern
 * @since 1.9.0
 */
public class PLCLogoBlock {

    public enum Kind {
        I, // Digital input
        Q, // Digital output
        M, // Digital flag
        AI, // Analog input
        AQ, // Analog output
        AM, // Analog flag
        NI, // Digital network input
        NAI, // Analog network input
        NQ, // Digital network output
        NAQ, // Analog network output
        VB, // Virtual memory, access to byte
        VW // Virtual memory, access to word
    };

    private final Kind kind;
    private final int base;

    PLCLogoBlock(Kind kind, int base) {
        this.kind = kind;
        this.base = base;
    };

    public Kind getKind() {
        return kind;
    }

    static public boolean isBitwise(Kind kind) {
        switch (kind) {
            case I:
            case Q:
            case M:
            case NI:
            case NQ: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    static public boolean isAnalog(Kind kind) {
        switch (kind) {
            case AI:
            case AQ:
            case AM:
            case NAI:
            case NAQ: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    static public boolean isGeneral(Kind kind) {
        return (kind == Kind.VB) || (kind == Kind.VW);
    }

    static public boolean isInput(Kind kind) {
        switch (kind) {
            case I:
            case AI:
            case NI:
            case NAI: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int getAddress(int index) {
        // Since counting of all block except VB and VW starts with 1
        // need to subtract 1 before address calculation
        if (isBitwise(kind)) {
            return base + (index - 1) / 8;
        } else if (isAnalog(kind)) {
            return base + (index - 1) * 2;
        } else {
            return index;
        }
    }

    public int getBit(int index) {
        // Since counting of all block except VB and VW starts with 1
        // need to subtract 1 before address calculation
        return isBitwise(kind) ? (index - 1) % 8 : -1;
    }

};
