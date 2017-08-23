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
package eu.aleon.aleoncean.device;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class IllegalDeviceParameterException extends Exception {

    private static final long serialVersionUID = -7446981762480106959L;

    public IllegalDeviceParameterException() {
    }

    public IllegalDeviceParameterException(final String message) {
        super(message);
    }

    public IllegalDeviceParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IllegalDeviceParameterException(final Throwable cause) {
        super(cause);
    }

    public IllegalDeviceParameterException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
