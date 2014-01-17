/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc;

/**
 * A RuntimeException as wrapper for all checked Exceptions inside the binding.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HomematicBindingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HomematicBindingException(String message, Exception e) {
        super(message, e);
    }

    public HomematicBindingException(String message) {
        super(message);
    }

    public HomematicBindingException(Exception e) {
        super(e);
    }

}
