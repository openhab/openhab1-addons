/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 *
 */
package org.openhab.binding.onewire.internal.scheduler;

import java.util.concurrent.BlockingQueue;

import org.openhab.binding.onewire.internal.listener.OneWireDevicePropertyWantsUpdateListener;
import org.openhab.binding.onewire.internal.listener.OneWireDevicePropertyWantsUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the class to read items from OneWireBus
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public class OneWireUpdateTask extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(OneWireUpdateTask.class);

    private final BlockingQueue<String> ivUpdateQueue;

    private OneWireDevicePropertyWantsUpdateListener ivWantsUpdateListener;

    public OneWireUpdateTask(BlockingQueue<String> pvQueue,
            OneWireDevicePropertyWantsUpdateListener pvWantsUpdateListener) {
        super("OneWireBinding/ReaderTask");
        setDaemon(true);
        this.ivUpdateQueue = pvQueue;
        this.ivWantsUpdateListener = pvWantsUpdateListener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        String lvItemName = null;
        try {
            while (true) {
                logger.debug("Autorefresh: Waiting for new item in update queue");
                lvItemName = ivUpdateQueue.take();
                logger.debug("Autorefresh: got new item {} in update queue", lvItemName);

                if (lvItemName != null) {
                    logger.debug("Autorefresh: Trying to update Item: {}", lvItemName);
                    ivWantsUpdateListener
                            .devicePropertyWantsUpdate(new OneWireDevicePropertyWantsUpdateEvent(this, lvItemName));
                }
            }
        } catch (InterruptedException ex) {
            logger.debug("Autorefresh: OneWireUpdateTask wait on blockingqueue interrupted: {}", ex.getMessage());
        }
        logger.debug("Autorefresh: OneWireUpdateTask interrupted.");
    }

    /**
     * @return
     */
    public OneWireDevicePropertyWantsUpdateListener getIvWantsUpdateListener() {
        return ivWantsUpdateListener;
    }
}
