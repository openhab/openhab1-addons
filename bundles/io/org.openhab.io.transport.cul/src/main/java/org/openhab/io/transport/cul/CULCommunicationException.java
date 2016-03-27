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
 * An exception which is thrown if communication with a culfw based device
 * causes an error.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULCommunicationException extends Exception {

    private static final long serialVersionUID = -1861588016496497682L;

    public CULCommunicationException() {
        super();
    }

    public CULCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CULCommunicationException(String message) {
        super(message);
    }

    public CULCommunicationException(Throwable cause) {
        super(cause);
    }

}
