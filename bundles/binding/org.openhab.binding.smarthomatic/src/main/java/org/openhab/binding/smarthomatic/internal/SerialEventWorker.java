/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smarthomatic.internal;

/**
 * Interface for serial events
 *
 * @author arohde
 * @since 1.9.0
 */
public interface SerialEventWorker {

    /**
     * fired is a event on the serial line has occurred
     * 
     * @param message
     */
    public void eventOccured(String message);

}
