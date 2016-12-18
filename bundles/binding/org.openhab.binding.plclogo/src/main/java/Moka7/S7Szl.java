/**
 * Copyright (c) 2013-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package Moka7;

/**
 *
 * @author Davide Nardella
 * @since 1.9.0
 */
public class S7Szl {

    public int LENTHDR;
    public int N_DR;
    public int DataSize;
    public byte Data[];

    public S7Szl(int BufferSize)
    {
        Data = new byte[BufferSize];
    }
    protected void Copy(byte[] Src, int SrcPos, int DestPos, int Size)
    {
        System.arraycopy(Src, SrcPos, Data, DestPos, Size);
    }
}
