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
public class S7CpuInfo {

    private final int BufSize = 256;
    protected byte[] Buffer = new byte[BufSize];

    protected void Update(byte[] Src, int Pos)
    {
        System.arraycopy(Src, Pos, Buffer, 0, BufSize);
    }

    public String ModuleTypeName()
    {
        return S7.GetStringAt(Buffer,172,32);
    }
    public String SerialNumber()
    {
        return S7.GetStringAt(Buffer,138,24);
    }
    public String ASName()
    {
        return S7.GetStringAt(Buffer,2,24);
    }
    public String Copyright()
    {
        return S7.GetStringAt(Buffer,104,26);
    }
    public String ModuleName()
    {
        return S7.GetStringAt(Buffer,36,24);
    }
}
