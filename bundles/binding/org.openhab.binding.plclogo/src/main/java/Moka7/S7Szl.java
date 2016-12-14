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
public class S7Szl {

    public int LENTHDR;
    public int N_DR;
    public int DataSize;
    public byte Data[];

    public S7Szl(int BufferSize) {
        Data = new byte[BufferSize];
    }

    protected void Copy(byte[] Src, int SrcPos, int DestPos, int Size) {
        System.arraycopy(Src, SrcPos, Data, DestPos, Size);
    }
}
