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
public class S7CpInfo {

    public int MaxPduLength;
    public int MaxConnections;
    public int MaxMpiRate;
    public int MaxBusRate;

    protected void Update(byte[] Src, int Pos)
    {
        MaxPduLength = S7.GetShortAt(Src, 2);
        MaxConnections = S7.GetShortAt(Src, 4);
        MaxMpiRate = S7.GetDIntAt(Src, 6);
        MaxBusRate = S7.GetDIntAt(Src, 10);
    }
}
