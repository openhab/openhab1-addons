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
package org.openhab.binding.weather.internal.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Schedules a task for later execution with the possibility to cancel it.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class DelayedExecutor {
    private Timer timer;
    private TimerTask task;

    /**
     * Cancel the scheduled Task.
     */
    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Schedules a Task which starts after the specified delay.
     */
    public void schedule(TimerTask task, long delay) {
        this.task = task;
        timer = new Timer();
        timer.schedule(task, delay);
    }
}
