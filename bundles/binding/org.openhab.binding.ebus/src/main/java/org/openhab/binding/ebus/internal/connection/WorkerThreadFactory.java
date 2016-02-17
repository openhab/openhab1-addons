/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import java.util.concurrent.ThreadFactory;

/**
 * Simple thread factory which allows to use given prefix for created threads.
 *
 * @author Łukasz Dywicki <luke@code-house.org>
 */
public class WorkerThreadFactory implements ThreadFactory {

    private int counter = 0;
    private String prefix = "";

    public WorkerThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, prefix + "-" + counter++);
    }
}
