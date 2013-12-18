/**Copyright (c) 2010-${year}, openHAB.org and others.
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
**/

package org.openhab.binding.wmbus.internal;

import java.util.ArrayList;

/**
 * This class represents a data block given from a WMBus device.
 * 
 * @author Christoph Parnitzke
 *
 * @since 1.3.0
 */
public class DataBlock
{
    public int length;              // length in bytes
    public DataType datTyp;         // Coding type of data records
    public FunctionValue Func;      // Type of data: min, max, error, instant
    public int LSBstorageNum;       // LSB bit of the storage number
    public ArrayList<Byte> DIFE;         // Data Information Field Extension
    public byte VIF;                // Value Information Field
    public ArrayList<Byte> VIFE;         // Value Information Field Extension
    public ArrayList<Byte> value;        // Information

    @SuppressWarnings("unused")
	public DataBlock()
    {
		int length;
        this.datTyp = DataType.ND;
        this.Func = FunctionValue.Instant;
        this.LSBstorageNum = 0;
        this.value = new ArrayList<Byte>();
        this.DIFE = new ArrayList<Byte>();
        this.VIFE = new ArrayList<Byte>();
    }
}