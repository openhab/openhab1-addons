/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.rxtx;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class ReaderShutdownException extends Exception {

    private static final long serialVersionUID = -1405905679676209283L;

    public ReaderShutdownException() {
        super();
    }

    public ReaderShutdownException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ReaderShutdownException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderShutdownException(String message) {
        super(message);
    }

    public ReaderShutdownException(Throwable cause) {
        super(cause);
    }

}
