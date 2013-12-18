/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

/**
 * This class represents the whole Frame send by a WMBus device.
 * 
 * @author Christoph Parnitzke
 *
 * @since 1.3.0
 */
public class Frame
{
    public byte[] Length;
    public byte[] CField;
    public byte[] MField;
    public byte[] AField;
    public byte[] CRC1;
    public byte[] CIField;
    public byte[] ACC;                  // Access number
    public byte[] Status;               // Information about meter reception level
    public byte[] Signature;
    public byte[] AES;

    public byte[] Data;
    public byte[] CRC2;

    public Frame()
    {
        this.Length = new byte[1];
        this.CField = new byte[1];
        this.MField = new byte[2];
        this.AField = new byte[6];
        this.CRC1 = new byte[2];
        this.CIField = new byte[1];
        this.ACC = new byte[1];
        this.Status = new byte[1];
        this.Signature = new byte[2];
        this.AES = new byte[2];

        //this.Data = new byte[15];
        //this.CRC2 = new byte[2];
    }
}
