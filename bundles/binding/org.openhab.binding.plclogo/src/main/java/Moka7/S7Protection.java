/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * Moka7 librray is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Library or Lesser General Public License
 * version 3.0 (LGPLv3) as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */

package Moka7;

// See §33.19 of "System Software for S7-300/400 System and Standard Functions"
public class S7Protection {
    public int sch_schal;
    public int sch_par;
    public int sch_rel;
    public int bart_sch;
    public int anl_sch;

    protected void Update(byte[] Src) {
        sch_schal = S7.GetWordAt(Src, 2);
        sch_par = S7.GetWordAt(Src, 4);
        sch_rel = S7.GetWordAt(Src, 6);
        bart_sch = S7.GetWordAt(Src, 8);
        anl_sch = S7.GetWordAt(Src, 10);
    }
}
