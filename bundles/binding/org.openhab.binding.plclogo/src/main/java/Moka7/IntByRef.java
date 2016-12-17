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
 * Quick class to pass an integer by reference
 * @author Davide
 */

public class IntByRef {
   
    public IntByRef(int Val)
    {
        this.Value=Val;
    }
    public IntByRef()
    {
        this.Value=0;
    }
    public int Value;
}
