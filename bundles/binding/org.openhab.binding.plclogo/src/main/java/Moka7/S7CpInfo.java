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
