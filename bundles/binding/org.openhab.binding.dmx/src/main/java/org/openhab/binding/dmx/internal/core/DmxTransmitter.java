/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.util.TimerTask;

import org.openhab.binding.dmx.DmxConnection;
import org.openhab.binding.dmx.DmxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DmxTransmitter, which is responsible for continuously sending all value
 * changes to the DMX connection.
 *
 * This transmitter should always run in a separate thread to allow for smooth
 * transmissions.
 *
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public final class DmxTransmitter extends TimerTask {

    /* REPEAT_INTERVAL is 750 ms (results in 800-1000ms) repetition time */
    private static final int REPEAT_INTERVAL = 750;
    private static final int REPEAT_COUNT = 3;

    public static final int REPEAT_MODE_ALWAYS = 0;
    public static final int REPEAT_MODE_NEVER = 1;
    public static final int REPEAT_MODE_REDUCED = 2;

    private static Logger logger = LoggerFactory.getLogger(DmxTransmitter.class);

    private DmxUniverse universe = new DmxUniverse();

    private DmxService service;

    private boolean running;
    private int repeatMode = 0;

    private boolean suspended;

    private long lastTransmit = 0;
    private int packetRepeatCount = 0;

    /**
     * Default constructor.
     */
    public DmxTransmitter(DmxService service) {
        this.service = service;
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void run() {

        if (suspended) {
            return;
        }

        running = true;
        try {
            long now = System.currentTimeMillis();
            byte[] b = universe.calculateBuffer();
            if (universe.getBufferChanged() || (repeatMode == REPEAT_MODE_ALWAYS)) {
                logger.trace("DMX Buffer changed or repeat mode always");
                DmxConnection conn = service.getConnection();
                if (conn != null) {
                    conn.sendDmx(b);
                    universe.notifyStatusListeners();
                }
                lastTransmit = now;
                packetRepeatCount = 0;
            } else if ((repeatMode == REPEAT_MODE_REDUCED)
                    && ((packetRepeatCount < REPEAT_COUNT) || ((now - lastTransmit) > REPEAT_INTERVAL))) {
                logger.trace("DMX Buffer needs refresh, sending");
                DmxConnection conn = service.getConnection();
                if (conn != null) {
                    conn.sendDmx(b);
                    universe.notifyStatusListeners();
                }
                if (packetRepeatCount < REPEAT_COUNT) {
                    packetRepeatCount++;
                }
                lastTransmit = now;
            } else {
                logger.trace("DMX output suppressed");
            }
        } catch (Exception e) {
            logger.error("Error sending dmx values.", e);
        } finally {
            running = false;
        }
    }

    /**
     * @return true if the transmitter is calculating values and transmitting
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Suspend/resume transmitting.
     *
     * @param suspend
     *            true to suspend
     */
    public void setSuspend(boolean suspend) {
        this.suspended = suspend;
    }

    /**
     * change transmitter refresh cycle
     *
     * @param refreshInterval
     *            interval in ms (if output did not change)
     */
    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    /**
     * Get the DMX channel in the current universe.
     *
     * @param channel
     *            number
     * @return DMX channel
     */
    public DmxChannel getChannel(int channel) {
        return universe.getChannel(channel);
    }

    /**
     * @return DMX universe
     */
    public DmxUniverse getUniverse() {
        return universe;
    }
}
