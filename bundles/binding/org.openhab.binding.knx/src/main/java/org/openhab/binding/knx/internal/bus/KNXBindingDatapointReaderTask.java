/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 *
 */
package org.openhab.binding.knx.internal.bus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.openhab.binding.knx.internal.connection.KNXConnection;
import org.openhab.binding.knx.internal.connection.KNXConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.exception.KNXInvalidResponseException;
import tuwien.auto.calimero.exception.KNXTimeoutException;
import tuwien.auto.calimero.link.KNXLinkClosedException;
import tuwien.auto.calimero.process.ProcessCommunicator;

/**
 * @author vdaube
 *
 */
public class KNXBindingDatapointReaderTask extends Thread implements KNXConnectionListener {

    private final BlockingQueue<Datapoint> readQueue;
    private final Map<Datapoint, Integer> dpReadRetries = new HashMap<Datapoint, Integer>();
    private final static Logger sLogger = LoggerFactory.getLogger(KNXBindingDatapointReaderTask.class);
    private boolean mKNXConnected = true;

    public KNXBindingDatapointReaderTask(BlockingQueue<Datapoint> queue) {
        super("KNXBinding/DatapointReaderTask");
        setDaemon(true);
        this.readQueue = queue;
        KNXConnection.addConnectionListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        Datapoint dp = null;
        try {
            while (true) {
                sLogger.debug("Autorefresh: Waiting for new item in reader queue");
                dp = readQueue.take();
                sLogger.debug("Autorefresh: got new item {} in reader queue", dp.getName());

                if (mKNXConnected) {
                    sLogger.debug("Autorefresh: Trying to read from KNX bus: {}", dp);
                    readFromKNXBus(dp);

                    long readingPause = KNXConnection.getReadingPause();
                    if (readingPause > 0) {
                        try {
                            sLogger.debug(
                                    "Autorefresh: DatapointReaderTask Waiting {} msecs to prevent KNX bus overload",
                                    readingPause);
                            Thread.sleep(readingPause);
                        } catch (InterruptedException e) {
                            sLogger.debug("Autorefresh: DatapointReaderTask KNX reading pause has been interrupted: {}",
                                    e.getMessage());
                        }
                    }
                } else {
                    sLogger.debug("Autorefresh: Not connected. Skipping bus read.");
                }
            }
        } catch (InterruptedException ex) {
            sLogger.debug("Autorefresh: DatapointReaderTask wait on blockingqueue interrupted: {}", ex.getMessage());
        }
        sLogger.debug("Autorefresh: DatapointReaderTask interrupted.");
    }

    private void readFromKNXBus(Datapoint datapoint) throws InterruptedException {
        try {
            ProcessCommunicator pc = KNXConnection.getCommunicator();
            if (pc != null) {
                sLogger.debug("Autorefresh: Sending read request to KNX for item '{}' DPT '{}'", datapoint.getName(),
                        datapoint.getDPT());
                pc.read(datapoint);
            } else {
                sLogger.debug(
                        "Autorefresh: Couldn't sent read request to KNX for item '{}'. Connection to KNX bus not (yet) established.",
                        datapoint.getName());
            }
        } catch (KNXFormatException e) {
            sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}: invalid format",
                    datapoint.getName(), e.getMessage());
        } catch (KNXInvalidResponseException e) {
            sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}: invalid response",
                    datapoint.getName(), e.getMessage());
        } catch (KNXTimeoutException e) {
            sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}: timeout", datapoint.getName(),
                    e.getMessage());
            addToReadQueue(datapoint);
        } catch (KNXLinkClosedException e) {
            sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}: link closed",
                    datapoint.getName(), e.getMessage());
        } catch (KNXException e) {
            sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}", datapoint.getName(),
                    e.getMessage());
        } catch (KNXIllegalArgumentException e) {
            sLogger.warn("Autorefresh: Error sending KNX read request for '{}': {}", datapoint.getName(),
                    e.getMessage());
        }

    }

    /**
     * Adds or re-adds a datapoint to readQueue,
     * if datapoint is known in dpReadRetries, retries will be reduced.
     * Otherwise it will be registered in dpReadRetries with retriesLimit.
     *
     * @param datapoint
     */
    private void addToReadQueue(Datapoint datapoint) throws InterruptedException {
        Integer r = dpReadRetries.remove(datapoint);
        int retries = (r != null ? r : KNXConnection.getReadRetriesLimit()) - 1;
        if (retries >= 0) {
            sLogger.warn("Autorefresh: Remaining retries for address '{}' = '{}'",
                    datapoint.getMainAddress().toString(), retries);
            readQueue.put(datapoint);
            dpReadRetries.put(datapoint, retries);
        } else if (retries == -1) {
            sLogger.warn("Autorefresh: Give up, could not read address '{}' after '{}' retries.",
                    datapoint.getMainAddress().toString(), KNXConnection.getReadRetriesLimit());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.knx.internal.connection.KNXConnectionListener#connectionEstablished()
     */
    @Override
    public void connectionEstablished() {
        mKNXConnected = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.knx.internal.connection.KNXConnectionListener#connectionLost()
     */
    @Override
    public synchronized void connectionLost() {
        mKNXConnected = false;
        readQueue.clear();
    }

}
