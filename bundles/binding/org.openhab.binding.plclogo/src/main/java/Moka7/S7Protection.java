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
