/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.io.IOException;

/**
 * Throw if the data we are parsing in not what we are expecting for input.
 *
 * @author Dan Cunningham
 *
 */
public class InvalidDataException extends IOException {

    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }
}
