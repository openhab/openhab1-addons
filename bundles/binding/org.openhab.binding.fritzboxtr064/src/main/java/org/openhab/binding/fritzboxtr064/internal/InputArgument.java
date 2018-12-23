/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

/**
 * Input argument for a TR064 SOAP call. Input arguments are added to the SOAP request of both reading
 * and writing values.
 *
 * @author Michael Koch <tensberg@gmx.net>
 * @since 1.12.0
 */
public class InputArgument {
    private final String name;
    private final String value;

    InputArgument(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
