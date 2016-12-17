/*=============================================================================|
|  PROJECT Moka7                                                         1.0.2 |
|==============================================================================|
|  Copyright (c) 2013-2016 by Davide Nardella                                  |
|  All rights reserved.                                                        |
|==============================================================================|
|  This program and the accompanying materials                                 |
|  are made available under the terms of the Eclipse Public License v1.0       |
|  which accompanies this distribution, and is available at                    |
|  http://www.eclipse.org/legal/epl-v10.html                                   |
|                                                                              |
|  SNAP7 is distributed in the hope that it will be useful,                    |
|  but WITHOUT ANY WARRANTY; without even the implied warranty of              |
|  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE whatever license you    |
|  decide to adopt.                                                            |
|                                                                              |
|=============================================================================*/
package Moka7;

/**
 *
 * @author Davide
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
