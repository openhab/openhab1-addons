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
public class S7OrderCode {

    public int V1;
    public int V2;
    public int V3;
    protected byte[] Buffer = new byte[1024];

    protected void Update(byte[] Src, int Pos, int Size)
    {
        System.arraycopy(Src, Pos, Buffer, 0, Size);
        V1 = (byte) Src[Size-3];
        V2 = (byte) Src[Size-2];
        V3 = (byte) Src[Size-1];
    }

    public String Code()
    {
        return S7.GetStringAt(Buffer, 2, 20);
    }
}
