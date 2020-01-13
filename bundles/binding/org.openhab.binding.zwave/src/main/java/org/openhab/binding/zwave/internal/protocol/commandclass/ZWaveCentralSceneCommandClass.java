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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Handles the clock command class.
 *
 * @author Chris Jackson, Robert Savage
 */
@XStreamAlias("centralSceneCommandClass")
public class ZWaveCentralSceneCommandClass extends ZWaveCommandClass
        implements ZWaveGetCommands, ZWaveCommandClassInitialization {

    @XStreamOmitField
    private static final Logger logger = LoggerFactory.getLogger(ZWaveCentralSceneCommandClass.class);

    private static final int SCENE_GET = 1;
    private static final int SCENE_REPORT = 2;
    private static final int SCENE_SET = 3;

    @XStreamOmitField
    private boolean initialiseDone = false;

    private int sceneCount = 0;

    /**
     * Creates a new instance of the ZWaveCentralSceneCommandClass class.
     *
     * @param node
     *            the node this command class belongs to
     * @param controller
     *            the controller to use
     * @param endpoint
     *            the endpoint this Command class belongs to
     */
    public ZWaveCentralSceneCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return CommandClass.CENTRAL_SCENE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
        logger.debug("NODE {}: Received central scene command (v{})", this.getNode().getNodeId(), this.getVersion());
        int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
            case SCENE_SET:
                // offset+1 is an incrementing number
                int sceneId = serialMessage.getMessagePayloadByte(offset + 3);
                int key = serialMessage.getMessagePayloadByte(offset + 2);
                logger.debug("NODE {}: Received scene {} ; key {}", this.getNode().getNodeId(), sceneId, key);

                // Received Central Scene Commands. They are triggered by pushing a
                // button on a controller supporting the CENTRAL_SCENE command class.
                // Command class values in data holder: 'key' and 'scene'
                // - ’scene’: indicates the current activated scene/button.
                // - ’key’: indicates the current key/button state.
                //
                // Examples 'key' values:
                // 0x00 = Key pressed,
                // 0x01 = Key released,
                // 0x02 = Key held down,
                // 0x03 = Double Tap, (Homeseer HS-WD100, HS-WS100)
                // 0x04 = Triple Tap, (Homeseer HS-WD100, HS-WS100)
                Properties properties = new Properties();
                properties.put("key", key);
                properties.put("scene", sceneId);

                // forward command class event value
                ZWaveCommandClassValueEvent zEvent = new ZWaveCommandClassValueEvent(this.getNode().getNodeId(),
                        endpoint, this.getCommandClass(), properties);
                this.getController().notifyEventListeners(zEvent);

                break;
            case SCENE_REPORT:
                sceneCount = serialMessage.getMessagePayloadByte(offset + 1);
                logger.debug("NODE {}: Supports {} scenes", this.getNode().getNodeId(), sceneCount);
                initialiseDone = true;
                break;
            default:
                logger.warn(String.format("NODE %d: Unsupported Command %d for command class %s (0x%02X).",
                        this.getNode().getNodeId(), command, this.getCommandClass().getLabel(),
                        this.getCommandClass().getKey()));
        }
    }

    @Override
    public SerialMessage getValueMessage() {
        logger.debug("NODE {}: Creating new message for application command SCENE_GET", this.getNode().getNodeId());
        SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) getCommandClass().getKey(),
                (byte) SCENE_GET };
        result.setMessagePayload(newPayload);
        return result;
    }

    @Override
    public Collection<SerialMessage> initialize(boolean refresh) {
        ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();

        if (initialiseDone == false) {
            result.add(getValueMessage());
        }

        return result;
    }

}
