/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul;

/**
 * An exception which represents error while opening/connecting to the culfw
 * based device.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULDeviceException extends Exception {

    private static final long serialVersionUID = 4834148919102194993L;

    public CULDeviceException() {
        super();
    }

    public CULDeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CULDeviceException(String message) {
        super(message);
    }

    public CULDeviceException(Throwable cause) {
        super(cause);
    }

}
