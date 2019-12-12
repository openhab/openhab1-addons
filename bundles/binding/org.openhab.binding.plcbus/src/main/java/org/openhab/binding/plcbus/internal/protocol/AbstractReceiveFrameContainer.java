/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.plcbus.internal.protocol;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Class for a ReceiveFrameContainer
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public abstract class AbstractReceiveFrameContainer implements IReceiveFrameContainer {

    private static Logger logger = LoggerFactory.getLogger(AbstractReceiveFrameContainer.class);

    protected List<ReceiveFrame> receiveFrames;

    /**
     * Constructor
     */
    public AbstractReceiveFrameContainer() {
        receiveFrames = new ArrayList<ReceiveFrame>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(IByteProvider byteProvider) {
        while (!isReceivingCompleted()) {
            parseFrame(byteProvider);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean isReceivingCompleted();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ReceiveFrame getAnswerFrame();

    private void parseFrame(IByteProvider byteProvider) {
        try {
            byte currentByte = byteProvider.getByte();

            if (currentByte == Frame.START_BYTE) {
                int length = byteProvider.getByte();

                if (length > 0) {
                    byte[] data = byteProvider.getBytes(length + 1);

                    ReceiveFrame frame = new ReceiveFrame();
                    frame.parse(data);

                    receiveFrames.add(frame);
                }
            }
        } catch (Exception e) {
            logger.warn("Error while parsing ReceiveFrame", e);
        }
    }

}
