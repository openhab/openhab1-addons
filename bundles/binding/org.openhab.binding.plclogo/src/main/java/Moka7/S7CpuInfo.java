/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * Moka7 librray is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Library or Lesser General Public License
 * version 3.0 (LGPLv3) as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */

package Moka7;

/**
 *
 * @author Davide
 */
public class S7CpuInfo {

    private final int BufSize = 256;
    protected byte[] Buffer = new byte[BufSize];

    protected void Update(byte[] Src, int Pos) {
        System.arraycopy(Src, Pos, Buffer, 0, BufSize);
    }

    public String ModuleTypeName() {
        return S7.GetStringAt(Buffer, 172, 32);
    }

    public String SerialNumber() {
        return S7.GetStringAt(Buffer, 138, 24);
    }

    public String ASName() {
        return S7.GetStringAt(Buffer, 2, 24);
    }

    public String Copyright() {
        return S7.GetStringAt(Buffer, 104, 26);
    }

    public String ModuleName() {
        return S7.GetStringAt(Buffer, 36, 24);
    }
}
