/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
