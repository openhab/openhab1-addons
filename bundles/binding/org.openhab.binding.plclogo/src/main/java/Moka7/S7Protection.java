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

// See §33.19 of "System Software for S7-300/400 System and Standard Functions"
public class S7Protection {
   public int sch_schal;
   public int sch_par;
   public int sch_rel;
   public int bart_sch;
   public int anl_sch;
   protected void Update(byte[] Src)
   {
       sch_schal = S7.GetWordAt(Src,2);
       sch_par   = S7.GetWordAt(Src,4);
       sch_rel   = S7.GetWordAt(Src,6);
       bart_sch  = S7.GetWordAt(Src,8);
       anl_sch   = S7.GetWordAt(Src,10);
   }
}
