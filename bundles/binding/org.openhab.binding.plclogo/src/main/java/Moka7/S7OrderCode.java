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
public class S7OrderCode {

    public int V1;
    public int V2;
    public int V3;
    protected byte[] Buffer = new byte[1024];

    protected void Update(byte[] Src, int Pos, int Size) {
        System.arraycopy(Src, Pos, Buffer, 0, Size);
        V1 = (byte) Src[Size - 3];
        V2 = (byte) Src[Size - 2];
        V3 = (byte) Src[Size - 1];
    }

    public String Code() {
        return S7.GetStringAt(Buffer, 2, 20);
    }
}
