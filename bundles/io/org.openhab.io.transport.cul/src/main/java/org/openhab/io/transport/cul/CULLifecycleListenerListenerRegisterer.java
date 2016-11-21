/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul;

public class CULLifecycleListenerListenerRegisterer implements CULLifecycleListener {

    private CULListener listener;

    public CULLifecycleListenerListenerRegisterer(CULListener listener) {
        this.listener = listener;
    }

    @Override
    public void open(CULHandler cul) throws CULCommunicationException {
        cul.registerListener(listener);

    }

    @Override
    public void close(CULHandler cul) {
        cul.unregisterListener(listener);
    }

}
