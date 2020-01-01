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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the UserCode command class.
 * @author Dave Badia
 * @since TODO: DB set version once done
 */
@XStreamAlias("userCodeCommandClass")
public class ZWaveUserCodeCommandClass extends ZWaveCommandClass implements ZWaveCommandClassDynamicState {
    private static final Logger logger = LoggerFactory.getLogger(ZWaveUserCodeCommandClass.class);
    public static final int USER_CODE_MIN_LENGTH = 4;
    public static final int USER_CODE_MAX_LENGTH = 10;

    private static final int USER_CODE_SET = 0x01;
    private static final int USER_CODE_GET = 0x02;
    private static final int USER_CODE_REPORT = 0x03;

    /**
     * Request the number of user codes that can be stored
     */
    private static final int USER_NUMBER_GET = 0x04;
    private static final int USER_NUMBER_REPORT = 0x05;

    private static final int UNKNOWN = -1;

    /**
     * The total number of users that the device supports as
     * determined by {@link #USER_NUMBER_REPORT}
     */
    private int numberOfUsersSupported = UNKNOWN;

    private List<UserCode> userCodeList = new ArrayList<UserCode>();

    /**
     * Creates a new instance of the ZWaveDoorLockCommandClass class.
     * @param node the node this command class belongs to
     * @param controller the controller to use
     * @param endpoint the endpoint this Command class belongs to
     */
    public ZWaveUserCodeCommandClass(ZWaveNode node,
            ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return CommandClass.USER_CODE;
    }

    @Override
    public Collection<SerialMessage> getDynamicValues(boolean refresh) {
        logger.debug("NODE {}: User Code initialize starting, refresh={}", getNode().getNodeId());
        Collection<SerialMessage> result = new ArrayList<SerialMessage>();
        if(refresh == true) {
            if(numberOfUsersSupported == ZWaveUserCodeCommandClass.UNKNOWN) {
                // Request it and wait for response
                logger.debug("NODE {}: numberOfUsersSupported=-1, refreshing}", getNode().getNodeId());
                SerialMessage message = buildGetMessage(ZWaveUserCodeCommandClass.USER_NUMBER_GET);
                result.add(message);
                // when we receive USER_NUMBER_REPORT it will kickoff syncUserCodes();
            } else if(numberOfUsersSupported == 0) {
                logger.error("NODE {}: door lock does not support any user codes, no codes set", getNode().getNodeId());
            } else {
                syncUserCodes();
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage,
            int offset, int endpoint) {
        logger.debug(String.format("NODE %d: Received UserCode Request", this.getNode().getNodeId()));
        final int command = serialMessage.getMessagePayloadByte(offset);
        switch (command) {
        case USER_NUMBER_REPORT:
            numberOfUsersSupported = serialMessage.getMessagePayloadByte(offset + 1);
            logger.info(String.format("NODE %d: ZWaveUserCodeCommandClass numberOfUsersSupported=%d", getNode().getNodeId(), numberOfUsersSupported));
            syncUserCodes();
            break;
        case USER_CODE_REPORT:
            logger.info(String.format("NODE %d: USER_CODE_REPORT %s", getNode().getNodeId(), SerialMessage.bb2hex(serialMessage.getMessagePayload())));
            // TODO: confirm the data was set properly

            break;
        case USER_CODE_GET:
        case USER_CODE_SET:
        case USER_NUMBER_GET:
        default:
            logger.warn(String.format("NODE %d: Unsupported Command 0x%02X for command class %s (0x%02X): %s", getNode().getNodeId(),
                    command, this.getCommandClass().getLabel(), this.getCommandClass().getKey(), SerialMessage.bb2hex(serialMessage.getMessagePayload())));
            break;
        }
    }

    public SerialMessage buildGetMessage(int type, Integer... argBytes) {
        if(type != USER_NUMBER_GET && type != USER_CODE_GET) {
            logger.error("NODE {}: Called buildGetMessage with invalid type of {}", this.getNode().getNodeId(), (0xff & type));
            return null;
        }
        logger.debug("NODE {}: Creating new message for application command USER_CODE {}", this.getNode().getNodeId(), (0xff & type));
        SerialMessage message = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write((byte) this.getNode().getNodeId());
        baos.write(2 + argBytes.length);
        baos.write((byte) getCommandClass().getKey());
        baos.write((byte) (type & 0xff));
        for(Integer aArgByte : argBytes) {
            baos.write((byte) (0xff & aArgByte));
        }
        message.setMessagePayload(baos.toByteArray());
        return message;
    }

    private void syncUserCodes() {
        // Sanity check
        if(numberOfUsersSupported == 0) {
            logger.error("NODE {}: does not support any user codes; no codes set", getNode().getNodeId());
            return;
        }
        if(userCodeList.size() > numberOfUsersSupported) {
            logger.error("NODE {}: too many user codes given, device only supports {}; no codes set", getNode().getNodeId(), numberOfUsersSupported);
            return;
        }
        for (int i = 0; i < userCodeList.size(); i++) {
            UserCode userCode = userCodeList.get(i);
            // Take all codes from the serialize xml and send them to the lock
            String code = userCode.code;
            // zeros means delete the code
            boolean codeIsZeros = Integer.parseInt(code) == 0; // no NumberFormatException since we called userCodeIsValid
            if(codeIsZeros) {
                code = ""; // send no code since we will set UserIdStatusType.AVAILBLE
            }
            if(codeIsZeros || userCodeIsValid(userCode, getNode().getNodeId())) {
                logger.debug("NODE {}: {} user code for {}", this.getNode().getNodeId(), codeIsZeros ? "Removing" : "Sending", userCode.friendlyName);
                SerialMessage message = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
                        SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Get);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write((byte) this.getNode().getNodeId());
                baos.write(4 + code.length());
                baos.write((byte) getCommandClass().getKey());
                baos.write(USER_CODE_SET);
                baos.write(i + 1); // identifier, must be 1 or higher
                if(codeIsZeros) {
                    baos.write(UserIdStatusType.AVAILBLE.key); // status
                } else {
                    baos.write(UserIdStatusType.OCCUPIED.key); // status
                    try {
                        for(Byte aCodeDigit : code.getBytes("UTF-8")) {
                            baos.write(aCodeDigit);
                        }
                    } catch (UnsupportedEncodingException e) {
                        logger.error("Got UnsupportedEncodingException", e);
                    }
                }
                message.setMessagePayload(baos.toByteArray());
                getNode().getController().enqueue(message);
            }
        }
    }

    public static boolean userCodeIsValid(UserCode userCode, Integer nodeId) {
        // We ignore userCode.name since it is just a friendly name
        // Check length of userCode.code
        if(userCode.code.length() < USER_CODE_MIN_LENGTH || userCode.code.length() > USER_CODE_MAX_LENGTH) {
            logger.error("NODE {}: Ignoring user code {}: was {} digits but must be between {} and {}", nodeId,
                    userCode.code, userCode.code.length(), USER_CODE_MIN_LENGTH, USER_CODE_MAX_LENGTH);
            return false;
        }
        // Check that userCode.code is numeric
        for(char c : userCode.code.toCharArray()) {
            if(!Character.isDigit(c)) {
                logger.error("NODE {}: Ignoring user code {}: found non-digit of '{}' in code", nodeId, userCode.code, c);
                return false;
            }
        }
        return true;
    }

    @XStreamAlias("userCode")
    public static class UserCode {
        /**
         * {@link #friendlyName} is never sent to the device
         */
        private final String friendlyName;
        private final String code;
        public UserCode(String friendlyName, String code) {
            this.friendlyName = friendlyName;
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public String getFriendlyName() {
            return friendlyName;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("UserCode [user=").append(friendlyName).append(", code=")
                    .append(code).append("]");
            return builder.toString();
        }
    }

    /**
     * Z-Wave UserIDStatus enumeration. The user ID status type indicates
     * the state of the user ID.
     *
     * @see {@link ZWaveUserCodeCommandClass#USER_CODE_GET}
     * @see {@link ZWaveUserCodeCommandClass#USER_CODE_SET}
     */
    @XStreamAlias("userIdStatusType")
    static enum UserIdStatusType {
        AVAILBLE(0x00, "Available (not set)"),
        OCCUPIED(0x01, "Occupied"),
        RESERVED_BY_ADMINISTRATOR(0x02, "Reserved by administrator"),
        STATUS_NOT_AVAILABLE(0x11, "Status not available"),
        ;
        /**
         * A mapping between the integer code and its corresponding door
         * lock state type to facilitate lookup by code.
         */
        private static Map<Integer, UserIdStatusType> codeToUserIdStatusTypeMapping;

        private int key;
        private String label;

        private static void initMapping() {
            codeToUserIdStatusTypeMapping = new ConcurrentHashMap<Integer, UserIdStatusType>();
            for (UserIdStatusType d : values()) {
                codeToUserIdStatusTypeMapping.put(d.key, d);
            }
        }

        /**
         * Lookup function based on the user id status type code.
         * Returns null if the code does not exist.
         * @param i the code to lookup
         * @return enumeration value of the user id status type.
         */
        public static UserIdStatusType getDoorLockStateType(int i) {
            if (codeToUserIdStatusTypeMapping == null) {
                initMapping();
            }
            return codeToUserIdStatusTypeMapping.get(i);
        }

        UserIdStatusType(int key, String label) {
            this.key = key;
            this.label = label;
        }

        /**
         * @return the key
         */
        public int getKey() {
            return key;
        }

        /**
         * @return the label
         */
        public String getLabel() {
            return label;
        }
    }
}
