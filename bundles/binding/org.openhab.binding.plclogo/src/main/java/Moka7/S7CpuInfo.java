/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
