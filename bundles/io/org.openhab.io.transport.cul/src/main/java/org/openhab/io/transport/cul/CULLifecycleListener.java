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
 * Listener for CULHandler lifecycle events.
 *
 * @author Patrick Ruckstuhl
 * @since 1.9.0
 */
public interface CULLifecycleListener {
    /**
     * Called after a cul handler is opened.
     *
     * @param cul the handler
     * @throws CULCommunicationException if there is an issue with the communication
     */
    public void open(CULHandler cul) throws CULCommunicationException;

    /**
     * Called before a cul handler is closed.
     * 
     * @param cul the handler
     */
    public void close(CULHandler cul);
}
