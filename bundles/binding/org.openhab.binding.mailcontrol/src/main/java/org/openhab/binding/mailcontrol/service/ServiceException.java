/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mailcontrol.service;

/**
 * 
 * @author Andrey.Pereverzin
 * @since 1.7.0
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {
    public ServiceException(String reason) {
        super(reason);
    }

    public ServiceException(Throwable ex) {
        super(ex);
    }
}
