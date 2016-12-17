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
