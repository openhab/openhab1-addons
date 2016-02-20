/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

/**
 * Exception type used when a login attempt fails against the MyQ API.
 *
 * @author Dan Cunningham
 *
 */
public class InvalidLoginException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidLoginException(String message) {
        super(message);
    }
}
