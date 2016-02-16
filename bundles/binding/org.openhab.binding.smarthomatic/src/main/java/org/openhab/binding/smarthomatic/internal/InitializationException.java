/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smarthomatic.internal;

/**
 * these exception is thrown if problems with the initialization occurs
 *
 * @author mcjobo
 * @since 1.9.0
 */
public class InitializationException extends Exception {

    private static final long serialVersionUID = 3215690312124168361L;

    /**
     * constructor for simple string exceptions
     * 
     * @param msg
     */
    public InitializationException(String msg) {
        super(msg);
    }

    /**
     * constructor for wrapping other exceptions
     * 
     * @param cause
     */
    public InitializationException(Throwable cause) {
        super(cause);
    }

    /**
     * constructor for individual problems + the wrapped exception
     * 
     * @param msg
     * @param cause
     */
    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
