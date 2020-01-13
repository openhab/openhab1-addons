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
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.Arrays;
import java.util.List;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeStageAdvancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used only by {@link ZWaveSecurityCommandClassWithInitialization} during device inclusion.
 *
 * During device inclusion, the security registration process between us and the node
 * has multiple stages in order to share our network key with the device.  This class
 * is used to track our current state in that process and determine next steps.
 *
 * The specific commands to be exchanged are:
 * {@value #INIT_COMMAND_ORDER_LIST}
 *
 * @author Dave Badia
 * @since TODO
 */
class ZWaveSecureInclusionStateTracker {
    private static final Logger logger = LoggerFactory.getLogger(ZWaveSecureInclusionStateTracker.class);


    /**
     * During node inclusion <b>only</b>, this is the order in which commands should be sent and received.
     * Commands absent from this list (for example {@link #SECURITY_MESSAGE_ENCAP}) can be sent/received at any time
     */
    private final List<Byte> INIT_COMMAND_ORDER_LIST =
            Arrays.asList(new Byte[]{
                    ZWaveSecurityCommandClass.SECURITY_SCHEME_GET,
                    ZWaveSecurityCommandClass.SECURITY_SCHEME_REPORT,
                    ZWaveSecurityCommandClass.SECURITY_NETWORK_KEY_SET,
                    ZWaveSecurityCommandClass.SECURITY_NETWORK_KEY_VERIFY,
                    ZWaveSecurityCommandClass.SECURITY_COMMANDS_SUPPORTED_GET,
                    ZWaveSecurityCommandClass.SECURITY_COMMANDS_SUPPORTED_REPORT,
            });

    private static final boolean HALT_ON_IMPROPER_ORDER = true;

    private byte currentStep = INIT_COMMAND_ORDER_LIST.get(0);

    /**
     * After we send a non-nonce security message, we can wait up to 10 seconds
     * for a reply.  Then we must exit the inclusion process
     */
    private static final int WAIT_TIME_MILLIS = 10000;

    /**
     * The next {@link SerialMessage} that will be given to {@link ZWaveNodeStageAdvancer}
     * when it calls {@link ZWaveSecurityCommandClass#initialize(boolean)}
     */
    private SerialMessage nextRequestMessage = null;

    /**
     * Lock object that will be used for synchronization
     */
    private final Object nextMessageLock = new Object();

    private String errorState = null;

    private long waitForReplyTimeout = 0;

    private final ZWaveNode node;

    ZWaveSecureInclusionStateTracker(ZWaveNode node) {
        this.node = node;
    }

    /**
     * Since these operations are security sensitive we must ensure they are
     * executing in the proper sequence
     * @param newStep the state we are about to enter
     * @return true if the new command was in an acceptable order, false
     * if it was not.  if false is returned, the response should <b>not</b>
     * be sent.
     */
    synchronized boolean verifyAndAdvanceState(Byte newStep) {
        logger.debug("NODE {}: ZWaveSecurityCommandClass in verifyAndAdvanceState with newstep={}, currentstep={}",
                node.getNodeId(), ZWaveSecurityCommandClass.commandToString(newStep), ZWaveSecurityCommandClass.commandToString(currentStep));
        if(!INIT_COMMAND_ORDER_LIST.contains(newStep)) {
            // Commands absent from EXPECTED_COMMAND_ORDER_LIST are always ok
            return true;
        }
        // Going back to the first step (zero index) is always OK // TODO: DB is it really?
        if(INIT_COMMAND_ORDER_LIST.indexOf(newStep) > 0) {
            // We have to verify where we are at
            int currentIndex = INIT_COMMAND_ORDER_LIST.indexOf(currentStep);
            int newIndex = INIT_COMMAND_ORDER_LIST.indexOf(newStep);
            // Accept one message back or the same message(device resending last reply) in addition to the normal one message ahead
            if(newIndex != currentIndex && newIndex - currentIndex > 1) {
                if(HALT_ON_IMPROPER_ORDER) {
                    setErrorState(String.format("NODE %s: Commands received out of order, aborting current=%s, new=%s",
                            node.getNodeId(), ZWaveSecurityCommandClass.commandToString(currentStep), ZWaveSecurityCommandClass.commandToString(newStep)));
                    return false;
                } else {
                    logger.warn("NODE {}: Commands received out of order (warning only, continuing) current={}, new={}",
                            node.getNodeId(), ZWaveSecurityCommandClass.commandToString(currentStep), ZWaveSecurityCommandClass.commandToString(newStep));
                    // fall through below
                }
            }
        }
        currentStep = newStep;
        return true;
    }


    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    public void resetWaitForReplyTimeout() {
        waitForReplyTimeout = System.currentTimeMillis() + WAIT_TIME_MILLIS;
    }

    void setNextRequest(SerialMessage message) {
        logger.debug("NODE {}: in InclusionStateTracker.setNextRequest() (current={}) with {}", node.getNodeId(), (nextRequestMessage != null), message);
        if(nextRequestMessage != null) {
            logger.warn("NODE {}: in InclusionStateTracker.setNextRequest() overriding old message which was never sent of {}", node.getNodeId(), message);
        }
        verifyAndAdvanceState((byte) (message.getMessagePayloadByte(3) & 0xff));
        synchronized(nextMessageLock) {
            nextRequestMessage = message;
            nextMessageLock.notify();
        }
    }

    /**
     * Gets the next message to be sent during the inclusion flow.
     * Each message can only get retrieved once
     * @return the next message or null if there was none
     */
    SerialMessage getNextRequest() {
        synchronized(nextMessageLock) {
            logger.debug("NODE {}: in InclusionStateTracker.getNextRequest() time left for reply: {}ms, returning {}", node.getNodeId(),
                    (System.currentTimeMillis() - waitForReplyTimeout), nextRequestMessage);
            if(System.currentTimeMillis() > waitForReplyTimeout) {
                // waited too long for a reply, secure inclusion failed
                setErrorState(WAIT_TIME_MILLIS+"ms passed since last request was sent, secure inclusion failed.");
                return null;
            }
            if(nextRequestMessage != null) {
                SerialMessage message = nextRequestMessage;
                resetWaitForReplyTimeout();
                nextRequestMessage = null;
                return message;
            }
            return null;
        }
    }

    public byte getCurrentStep() {
        return currentStep;
    }

    public String getErrorState() {
        return errorState;
    }
}