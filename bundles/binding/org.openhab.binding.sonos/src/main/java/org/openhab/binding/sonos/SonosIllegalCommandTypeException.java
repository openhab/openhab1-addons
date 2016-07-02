/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos;

public class SonosIllegalCommandTypeException extends Exception {

    private static final long serialVersionUID = 1107925527851348965L;

    public SonosIllegalCommandTypeException(String msg) {
        super(msg);
    }

    public SonosIllegalCommandTypeException(Throwable cause) {
        super(cause);
    }

    public SonosIllegalCommandTypeException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
