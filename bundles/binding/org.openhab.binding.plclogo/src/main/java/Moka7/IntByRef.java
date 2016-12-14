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

/**
 * Quick class to pass an integer by reference
 *
 * @author Davide
 */

public class IntByRef {

    public IntByRef(int Val) {
        this.Value = Val;
    }

    public IntByRef() {
        this.Value = 0;
    }

    public int Value;
}
