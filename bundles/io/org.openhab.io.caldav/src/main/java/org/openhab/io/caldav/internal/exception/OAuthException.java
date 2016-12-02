/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.caldav.internal.exception;

/**
 * Exception, which will be thrown in any OAuth error.
 *
 * @author Robert Delbrück
 * @since 1.9.0
 *
 */
public class OAuthException extends Exception {
    private static final long serialVersionUID = 1L;

    private int statusCode;

    public OAuthException(int statusCode) {
        super();
        this.statusCode = statusCode;
    }
}
