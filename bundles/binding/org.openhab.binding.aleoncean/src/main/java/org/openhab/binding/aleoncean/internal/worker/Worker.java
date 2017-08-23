/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package org.openhab.binding.aleoncean.internal.worker;

import java.util.concurrent.TimeUnit;
import org.openhab.binding.aleoncean.internal.devices.DeviceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.ESP3Packet;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.packet.ResponseReturnCode;
import eu.aleon.aleoncean.packet.commoncommand.CoRdIdBase;
import eu.aleon.aleoncean.packet.commoncommand.CoRdVersionPacket;
import eu.aleon.aleoncean.packet.commoncommand.CoWrIdBase;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;
import eu.aleon.aleoncean.packet.response.commoncommand.CoRdIdBaseResponseOk;
import eu.aleon.aleoncean.packet.response.commoncommand.CoRdVersionResponseOk;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.rxtx.USB300;
import eu.aleon.aleoncean.util.ThreadUtil;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class Worker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    private final WorkerQueue workerQueue;
    private final ESP3Connector connector;

    private final Thread workerThread;

    private final ESP3Reader reader;
    private final Thread readerThread;

    private final EnOceanId baseId = EnOceanId.getBroadcast();
    private final EnOceanId chipId = EnOceanId.getBroadcast();

    private final DeviceContainer devices;

    private volatile boolean running;

    public Worker() {
        this.workerQueue = new WorkerQueue();
        this.connector = new USB300();

        this.workerThread = new Thread(null, this, String.format("%s: %s", this.getClass().getCanonicalName(),
                "worker thread"));

        this.reader = new ESP3Reader(connector, workerQueue);
        this.readerThread = new Thread(null, reader, String.format("%s: %s", this.getClass().getCanonicalName(),
                "reader thread"));

        this.devices = new DeviceContainer(connector);
    }

    /**
     * Start the worker.
     *
     * After the worker is started, several things have to be done (by the caller).
     * - Set the current event publisher (using WorkerItemSetEventPublisher).
     * - Set a binding changed notification for all items (using severals WorkerItemBindingChanged).
     *
     * @return Return true if the worker could be started. If something went wrong false is returned.
     */
    public synchronized boolean start() {
        LOGGER.debug("worker: start");

        running = true;
        devices.start();
        workerThread.start();

        LOGGER.debug("worker: started");
        return true;
    }

    public synchronized void stop() {
        LOGGER.debug("worker: stop");

        final WorkerReply reply = addAndWaitForReply(new WorkerItemShutdown(), 10, TimeUnit.SECONDS);
        LOGGER.debug("{}", reply);

        ThreadUtil.join(workerThread);
        LOGGER.debug("worker: stopped");
    }

    public synchronized void add(final WorkerItem workerItem) {
        if (running) {
            workerQueue.add(workerItem);
        } else {
            LOGGER.warn("Will not add worker item ({}) because worker not running.", workerItem);
        }
    }

    public synchronized WorkerReply addAndWaitForReply(final WorkerItem workerItem, final long timeout,
            final TimeUnit unit) {
        if (running) {
            workerQueue.add(workerItem);
            return workerItem.waitForReply(timeout, unit);
        } else {
            return new WorkerReply(WorkerReplyCode.NOT_RUNNING);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Take the next item from the worker queue.
                final WorkerItem workerItem = workerQueue.take();
                LOGGER.trace("Received a worker item: {}", workerItem);

                // Inspect received item.
                if (workerItem instanceof WorkerItemShutdown) {
                    handleWorkerItemShutdown((WorkerItemShutdown) workerItem);
                } else if (workerItem instanceof WorkerItemESP3Port) {
                    handleWorkerItemESP3Port((WorkerItemESP3Port) workerItem);
                } else if (workerItem instanceof WorkerItemSetBaseId) {
                    handleWorkerItemSetBaseId((WorkerItemSetBaseId) workerItem);
                } else if (workerItem instanceof WorkerItemPacket) {
                    handleWorkerItemPacket((WorkerItemPacket) workerItem);
                } else if (workerItem instanceof WorkerItemReceivedCommand) {
                    handleWorkerItemReceivedCommand((WorkerItemReceivedCommand) workerItem);
                } else if (workerItem instanceof WorkerItemReceivedState) {
                    handleWorkerItemReceivedState((WorkerItemReceivedState) workerItem);
                } else if (workerItem instanceof WorkerItemSetEventPublisher) {
                    handleWorkerItemSetEventPublisher((WorkerItemSetEventPublisher) workerItem);
                } else if (workerItem instanceof WorkerItemBindingChanged) {
                    handleWorkerItemBindingChanged((WorkerItemBindingChanged) workerItem);
                } else {
                    handleWorkerItem(workerItem);
                }

            } catch (final InterruptedException ex) {
                // There is nothing to do, but writing a log message.
                LOGGER.debug("Received an exception while taking element from worker queue.", ex);
            }
        }
    }

    private void handleWorkerItem(final WorkerItem workerItem) {
        LOGGER.debug("Received unhandled item. {}", workerItem);
        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemShutdown(final WorkerItemShutdown workerItem) {
        LOGGER.debug("Received shutdown item: {}", workerItem);

        running = false;

        stopESP3();
        devices.stop();

        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemESP3Port(final WorkerItemESP3Port workerItem) {
        LOGGER.debug("Received ESP3 port: {}", workerItem.getESP3Port());

        stopESP3();
        if (startESP3(workerItem.getESP3Port())) {
            workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
        } else {
            workerItem.reply(new WorkerReply(WorkerReplyCode.FAILED));
        }
    }

    private void handleWorkerItemSetBaseId(final WorkerItemSetBaseId workerItem) {
        LOGGER.debug("Receives set base id: {}", workerItem.getBaseId());

        if (workerItem.getBaseId().equals(baseId)) {
            LOGGER.debug("The correct base ID is already set. Skip change request.");
            return;
        }

        // Write the new base ID.
        reqCoWrIdBase(workerItem.getBaseId());

        // Read the base ID back.
        reqCoRdIdBase();

        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemPacket(final WorkerItemPacket workerItem) {
        final ESP3Packet packet = workerItem.getPacket();

        LOGGER.debug("Received new ESP3 packet: {}", packet);

        if (packet instanceof RadioPacket) {
            devices.handleIncomingRadioPacket((RadioPacket) packet);
        }

        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemReceivedCommand(final WorkerItemReceivedCommand workerItem) {
        LOGGER.debug("Received command: {}, {}", workerItem.getItemName(), workerItem.getCommand());

        devices.handleReceivedCommand(workerItem.getItemName(), workerItem.getCommand());
        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemReceivedState(final WorkerItemReceivedState workerItem) {
        LOGGER.debug("Received state: {}, {}", workerItem.getItemName(), workerItem.getState());

        devices.handleReceivedState(workerItem.getItemName(), workerItem.getState());
        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemSetEventPublisher(final WorkerItemSetEventPublisher workerItem) {
        LOGGER.debug("Set event publisher: {}", workerItem.getEventPublisher());

        devices.setEventPublisher(workerItem.isSet() ? workerItem.getEventPublisher() : null);
        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private void handleWorkerItemBindingChanged(final WorkerItemBindingChanged workerItem) {
        LOGGER.debug("Binding changed for item: {}", workerItem.getItemName());

        devices.itemChanged(workerItem.getItemName(), workerItem.getConfig());
        workerItem.reply(new WorkerReply(WorkerReplyCode.OK));
    }

    private boolean startESP3(final String device) {
        if (!connector.connect(device)) {
            LOGGER.warn("Cannot connect to device '{}'", device);
            return false;
        }

        readerThread.start();

        // Fetch the chip ID.
        reqCoRdVersion();

        // Fetch the base ID.
        reqCoRdIdBase();

        return true;
    }

    private void stopESP3() {
        LOGGER.debug("Do a disconnect...");
        connector.disconnect();
        LOGGER.debug("Disconnect returned.");

        LOGGER.debug("Wait for reader thread...");
        ThreadUtil.join(readerThread);
        LOGGER.debug("Wait for reader thread returned.");
    }

    private void reqCoRdVersion() {
        final CoRdVersionPacket packet = new CoRdVersionPacket();
        final ResponsePacket responsePacket = connector.write(packet);
        if (responsePacket == null) {
            LOGGER.warn("Timeout on request versions (chip ID).");
            return;
        }

        try {
            final Response responseGeneric = packet.inspectResponsePacket(responsePacket);
            if (responseGeneric instanceof CoRdVersionResponseOk) {
                final CoRdVersionResponseOk response = (CoRdVersionResponseOk) responseGeneric;
                chipId.fill(response.getChipId());
                LOGGER.info("Chip ID: {}", response.getChipId());
            }
        } catch (final UnknownResponseException ex) {
            LOGGER.warn("Unknown response recived.", ex);
        }
    }

    private void reqCoRdIdBase() {
        final CoRdIdBase packet = new CoRdIdBase();
        final ResponsePacket responsePacket = connector.write(packet);
        if (responsePacket == null) {
            LOGGER.warn("Timeout on request base ID.");
            return;
        }

        try {
            final Response responseGeneric = packet.inspectResponsePacket(responsePacket);
            if (responseGeneric instanceof CoRdIdBaseResponseOk) {
                final CoRdIdBaseResponseOk response = (CoRdIdBaseResponseOk) responseGeneric;
                baseId.fill(response.getBaseId());
                LOGGER.info("Base ID: {}, remaining write cycles: {}", response.getBaseId(),
                        response.getRemainingWriteCycles());
            }
        } catch (final UnknownResponseException ex) {
            LOGGER.warn("Unknown response recived.", ex);
        }
    }

    private void reqCoWrIdBase(final EnOceanId baseId) {
        final CoWrIdBase packet = new CoWrIdBase();
        packet.setBaseId(baseId);

        LOGGER.debug("Try to change base ID to: {}", baseId);
        final ResponsePacket responsePacket = connector.write(packet);
        if (responsePacket == null) {
            LOGGER.warn("Timeout on set base ID request.");
            return;
        }

        try {
            /* final Response responseGeneric = */
            packet.inspectResponsePacket(responsePacket);
            if (responsePacket.getReturnCode() == ResponseReturnCode.RET_OK) {
                LOGGER.debug("Base ID change request succeeded.");
            } else {
                LOGGER.warn("Something went wrong on base ID writing {}.", responsePacket.getReturnCode());
            }
        } catch (final UnknownResponseException ex) {
            LOGGER.warn("Unknown response recived.", ex);
        }
    }
}
