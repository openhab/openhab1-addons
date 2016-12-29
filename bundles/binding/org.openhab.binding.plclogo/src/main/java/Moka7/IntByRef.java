/**
 * Copyright (c) 2013-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package Moka7;

/**
 * Quick class to pass an integer by reference
 * @author Davide Nardella
 * @since 1.9.0
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
