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

    public enum DmxRepeatMode {
        ALWAYS("always"),
        NEVER("never"),
        REDUCED("reduced");

        private String repeatMode;

        DmxRepeatMode(String repeatMode) {
            this.repeatMode = repeatMode;
        }

        @Override
        public String toString() {
            return this.repeatMode;
        }

        public static DmxRepeatMode fromString(String repeatMode) {
            if (repeatMode != null) {
                for (DmxRepeatMode mode : DmxRepeatMode.values()) {
                    if (repeatMode.equalsIgnoreCase(mode.repeatMode)) {
                        return mode;
                    }
                }
            }
            return null;
        }
    }

    private static Logger logger = LoggerFactory.getLogger(DmxTransmitter.class);

    private DmxUniverse universe = new DmxUniverse();

    private DmxService service;

    private boolean running;
    private DmxRepeatMode repeatMode = DmxRepeatMode.ALWAYS;

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
            DmxConnection conn = service.getConnection();
            if (conn != null) {
                if (universe.getBufferChanged()) {
                    logger.trace("DMX Buffer changed, also sending status updates");
                    conn.sendDmx(b);
                    universe.notifyStatusListeners();
                    lastTransmit = now;
                    packetRepeatCount = 0;
                } else if (repeatMode == DmxRepeatMode.ALWAYS) {
                    logger.trace("repeat mode always, sending DMX only");
                    conn.sendDmx(b);
                    lastTransmit = now;
                } else if ((repeatMode == DmxRepeatMode.REDUCED)
                        && ((packetRepeatCount < REPEAT_COUNT) || ((now - lastTransmit) > REPEAT_INTERVAL))) {
                    logger.trace("output needs refresh, sending DMX only");
                    conn.sendDmx(b);
                    if (packetRepeatCount < REPEAT_COUNT) {
                        packetRepeatCount++;
                    }
                    lastTransmit = now;
                } else {
                    logger.trace("DMX output suppressed");
                }
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
    public void setRepeatMode(DmxRepeatMode repeatMode) {
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
