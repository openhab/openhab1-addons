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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.openhab.binding.zwave.internal.protocol.SecurityEncapsulatedSerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveSecureNonceTracker.Nonce;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent.Type;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.openhab.binding.zwave.internal.protocol.serialmessage.ApplicationCommandMessageClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * For code readability and maintainability, the logic for the secure command class is split into 2 classes: this one
 * and {@link ZWaveSecurityCommandClassWithInitialization}. {@link ZWaveSecurityCommandClassWithInitialization}
 * will always be created, as this class is abstract
 *
 * @see {@link ZWaveSecurityCommandClassWithInitialization}
 * @author Dave Badia
 * @since TODO
 */
@XStreamAlias("securityCommandClass")
public abstract class ZWaveSecurityCommandClass extends ZWaveCommandClass {
    private static final Logger logger = LoggerFactory.getLogger(ZWaveSecurityCommandClass.class);

    /**
     * Per the z-wave spec, this is the AES key used to derive {@link #encryptKey} from {@link #networkKey}
     */
    private static final byte[] DERIVE_ENCRYPT_KEY = { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
            (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
            (byte) 0xAA, (byte) 0xAA, (byte) 0xAA };
    /**
     * Per the z-wave spec, this is the AES key used to derive {@link #authKey} from {@link #networkKey}
     */
    private static final byte[] DERIVE_AUTH_KEY = { 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55, 0x55,
            0x55, 0x55, 0x55, 0x55, 0x55 };
    private static final String AES = "AES";
    private static final int MAC_LENGTH = 8;
    private static final int IV_LENGTH = 16;
    static final int HALF_OF_IV = IV_LENGTH / 2;
    /**
     * Marks the end of the list of supported command classes. The remaining classes are those that can be controlled by
     * the device. These classes are created without values. Messages received cause notification events instead.
     */
    public static final byte COMMAND_CLASS_MARK = (byte) 0xef;

    /**
     * Request which commands the device supports using
     * security encapsulation (encryption)
     */
    static final byte SECURITY_COMMANDS_SUPPORTED_GET = 0x02;
    /**
     * Response from the device which indicates which commands
     * the device supports using security encapsulation (encryption)
     */
    static final byte SECURITY_COMMANDS_SUPPORTED_REPORT = 0x03;
    /**
     * Request which security initialization schemes the
     * device supports
     */
    static final byte SECURITY_SCHEME_GET = 0x04;
    /**
     * Response from the device of which security initialization
     * schemes the device supports
     */
    static final byte SECURITY_SCHEME_REPORT = 0x05;
    /**
     * The controller is sending the device the network key to
     * be used for all secure transmissions
     */
    static final byte SECURITY_NETWORK_KEY_SET = 0x06;
    /**
     * Response from the device after getting SECURITY_NETWORK_KEY_SET
     * that was encapsulated using the new key
     */
    static final byte SECURITY_NETWORK_KEY_VERIFY = 0x07;
    /**
     * Not supported since we are always the master
     */
    private static final byte SECURITY_SCHEME_INHERIT = 0x08;
    /**
     * Request to generate a nonce to be used in message encapsulation
     */
    public static final byte SECURITY_NONCE_GET = 0x40;
    /**
     * Response with the generated nonce to be used in message encapsulation
     */
    public static final byte SECURITY_NONCE_REPORT = (byte) 0x80;
    /**
     * Indicates this message has been encapsulated and must be decrypted
     * to reveal the actual message
     * public so {@link ApplicationCommandMessageClass} can check for this and invoke
     * {@link #decryptMessage(byte[], int)} as needed
     */
    public static final byte SECURITY_MESSAGE_ENCAP = (byte) 0x81;
    /**
     * Indicates this message has been encapsulated and must be decrypted
     * to reveal the actual message and that there are more messages to
     * send so another nonce is needed.
     * public so {@link ApplicationCommandMessageClass} can check for this and invoke
     * {@link #decryptMessage(byte[], int)} as needed
     */
    public static final byte SECURITY_MESSAGE_ENCAP_NONCE_GET = (byte) 0xc1;

    private static final Map<Byte, String> COMMAND_LOOKUP_TABLE = new ConcurrentHashMap<Byte, String>();

    private static final List<Byte> REQUIRED_ENCAPSULATION_LIST = Arrays
            .asList(new Byte[] { SECURITY_NETWORK_KEY_SET, SECURITY_NETWORK_KEY_VERIFY, SECURITY_SCHEME_INHERIT,
                    SECURITY_COMMANDS_SUPPORTED_GET, SECURITY_COMMANDS_SUPPORTED_REPORT });

    /**
     * Should be set to true to ensure all incoming security encapsulated messages adhere to
     * zwave security mac standards
     *
     * Package-protected visible for test case use
     */
    static boolean DROP_PACKETS_ON_MAC_FAILURE = true;

    /**
     * Should be set to true unless we find a good reason not to since we
     * also have {@link #disableEncapNonceGet} which will disable the
     * use of {@link #SECURITY_MESSAGE_ENCAP_NONCE_GET} on a per device basis
     * if it's not working
     *
     * OZW code comments say that {@link #SECURITY_MESSAGE_ENCAP_NONCE_GET}
     * doesn't work so keep a flag to enable/disable.
     *
     * Package-protected visible for test case use
     *
     * @see {@link #disableEncapNonceGet}
     *      TODO: DB NONE, NORMAL, AGGRESSIVE, VERY_AGRESSIVE
     */
    private static final boolean USE_SECURITY_MESSAGE_ENCAP_NONCE_GET = true;

    /**
     * OZW code sets different transmit option flags for some security
     * messages.
     *
     * Package-protected visible for test case use
     */
    static final boolean OVERRIDE_DEFAULT_TRANSMIT_OPTIONS = true;

    /**
     * Should be set to false as sending outside of the inclusion flow has been know to cause issues
     * with the lock becoming unresponsive per OZW team
     *
     * When false, SECURITY_COMMANDS_SUPPORTED_GET is only sent during secure inclusion.
     * When true, SECURITY_COMMANDS_SUPPORTED_GET is sent every time OpenHAB starts up.
     *
     * Package-protected visible for test case use
     */
    protected static final boolean SEND_SECURITY_COMMANDS_SUPPORTED_GET_ON_STARTUP = false;

    /**
     * Security messages are time sensitive so mark them as high priority
     */
    public static final SerialMessagePriority SECURITY_MESSAGE_PRIORITY = SerialMessagePriority.High;

    /**
     * Header is made up of 10 bytes:
     * command class byte
     * message type byte
     * 8 bytes for the device's nonce
     */
    private static final int ENCAPSULATED_HEADER_LENGTH = 10;

    /**
     * Footer consists of the nonce ID (1 byte) and the MAC (8 bytes)
     */
    private static final int ENCAPSULATED_FOOTER_LENGTH = 9;

    /**
     * Security encapsulated messages have much higher overhead than normal messages so we must use care to ensure we do
     * not bombard devices with messages or they can stop responding. To avoid bombardment, a similar message check is
     * performed so that if someone hits lock, then unlock, only the unlock command is sent. We only apply this logic to
     * specific command classes, as specified in this list
     */
    private static final List<CommandClass> SIMILAR_FRAME_COMMAND_CLASS_LIST = Arrays
            .asList(new CommandClass[] { CommandClass.DOOR_LOCK, CommandClass.BATTERY });

    /**
     * Queue of {@link ZWaveSecurityPayloadFrame} that are waiting for nonces
     * so they can be encapsulated and set.
     *
     * Note the reference is ConcurrentLinkedQueue and not Queue. This is done
     * as a safety check we have to instantiate this in multiple places. This
     * will ensure we always create it as the correct type.
     */
    @XStreamOmitField
    private ConcurrentLinkedQueue<ZWaveSecurityPayloadFrame> payloadEncapsulationQueue = new ConcurrentLinkedQueue<ZWaveSecurityPayloadFrame>();

    /**
     * The network key as configured in the openhab.cfg -> zwave:networkey
     */
    @XStreamOmitField
    protected static SecretKey realNetworkKey;

    /**
     * The network key currently in use. My be {@link #realNetworkKey} or a scheme network key
     */
    @XStreamOmitField
    private SecretKey networkKey;

    /**
     * The encryption key currently in use which is derived from {@link #networkKey}
     */
    @XStreamOmitField
    private SecretKey encryptKey;

    /**
     * The auth key currently in use which is derived from {@link #networkKey}
     */
    @XStreamOmitField
    private SecretKey authKey;

    /**
     * The error that occurred when trying to load the encryption key from openhab.cfg -> zwave:networkey
     * Will be null if the load succeeded
     */
    @XStreamOmitField
    protected static Exception keyException;

    /**
     * Flag so we understand that we received the {@link #SECURITY_COMMANDS_SUPPORTED_REPORT} reply
     * This occurs during inclusion and optionally during normal startup depending on the
     * {@value #SEND_SECURITY_COMMANDS_SUPPORTED_GET_ON_STARTUP} flag
     */
    @XStreamOmitField
    protected boolean receivedSecurityCommandsSupportedReport = false;

    /**
     * The last time we sent a security message to the node
     */
    @XStreamOmitField
    protected long lastSentMessageTimestamp = 0;

    /**
     * The last time we received any security message from the node
     */
    @XStreamOmitField
    protected long lastReceivedMessageTimestamp = 0;

    /**
     * The last time we received a {@link #SECURITY_NONCE_GET} message from the node
     */
    @XStreamOmitField
    private long lastNonceGetReceivedAt = 0L;

    @XStreamOmitField
    private volatile SecurityEncapsulatedSerialMessage lastEncapsulatedRequstMessage = null;

    @XStreamOmitField
    protected ZWaveSecureNonceTracker nonceGeneration = new ZWaveSecureNonceTracker(getNode());

    @XStreamOmitField
    private Object threadLock = new Object();

    @XStreamOmitField
    private ZWaveSecurityEncapsulationThread encapsulationThread;

    @XStreamOmitField
    private long lastDeviceNonceReceivedAt = 0L;

    // TODO: DB serialize
    /**
     * Flag to disable the use of {@link #SECURITY_MESSAGE_ENCAP_NONCE_GET}
     * on a per device basis if it's not working
     */
    private boolean disableEncapNonceGet = false;

    static {
        // Initialize the COMMAND_LOOKUP_TABLE
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_COMMANDS_SUPPORTED_GET), "SECURITY_COMMANDS_SUPPORTED_GET");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_COMMANDS_SUPPORTED_REPORT),
                "SECURITY_COMMANDS_SUPPORTED_REPORT");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_SCHEME_GET), "SECURITY_SCHEME_GET");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_SCHEME_REPORT), "SECURITY_SCHEME_REPORT");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_NETWORK_KEY_SET), "SECURITY_NETWORK_KEY_SET");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_NETWORK_KEY_VERIFY), "SECURITY_NETWORK_KEY_VERIFY");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_SCHEME_INHERIT), "SECURITY_SCHEME_INHERIT");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_NONCE_GET), "SECURITY_NONCE_GET");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_NONCE_REPORT), "SECURITY_NONCE_REPORT");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_MESSAGE_ENCAP), "SECURITY_MESSAGE_ENCAP");
        COMMAND_LOOKUP_TABLE.put(Byte.valueOf(SECURITY_MESSAGE_ENCAP_NONCE_GET), "SECURITY_MESSAGE_ENCAP_NONCE_GET");
    }

    abstract boolean checkRealNetworkKeyLoaded();

    protected void transmitMessage(SerialMessage serialMessage) {
        // Normal (non-inclusion mode) so give the message to the controller to be transmitted
        this.getController().sendData(serialMessage);
    }

    /**
     * Creates a new instance of the ZWaveSecurityCommandClass class. This is package
     * protected as {@link ZWaveSecurityCommandClassWithInitialization} will typically be invoked
     *
     * @param node
     *            the node this command class belongs to
     * @param controller
     *            the controller to use
     * @param endpoint
     *            the endpoint this Command class belongs to
     */
    protected ZWaveSecurityCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
        super(node, controller, endpoint);
        if (!checkRealNetworkKeyLoaded()) {
            throw new IllegalStateException(
                    "NODE " + getNode().getNodeId() + ": node wants to use security but key is not set");
        }
        setupNetworkKey(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandClass getCommandClass() {
        return getSecurityCommandClass();
    }

    protected static CommandClass getSecurityCommandClass() {
        return CommandClass.SECURITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxVersion() {
        return 1;
    }

    /**
     * The Security command class is unique in that only some commands require encryption
     * (for all others, the security encapsulation requirement applies to the entire command class.)
     */
    public static boolean doesCommandRequireSecurityEncapsulation(Byte commandByte) {
        return REQUIRED_ENCAPSULATION_LIST.contains(commandByte);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {

        byte command = (byte) serialMessage.getMessagePayloadByte(offset);
        traceHex("payload bytes for incoming security message", serialMessage.getMessagePayload());
        lastReceivedMessageTimestamp = System.currentTimeMillis();
        switch (command) {

            case SECURITY_NONCE_GET:
                // the Device wants to send us a Encrypted Packet, so we need to generate a nonce and send it
                lastNonceGetReceivedAt = System.currentTimeMillis();
                sendNonceReport();
                break;

            case SECURITY_NONCE_REPORT:
                lastDeviceNonceReceivedAt = System.currentTimeMillis();
                // we received a NONCE from a device in response to our SECURITY_NONCE_GET or
                // SECURITY_MESSAGE_ENCAP_NONCE_GET
                // Nonce is messageBuf without the first offset +1 bytes
                byte[] messageBuf = serialMessage.getMessagePayload();
                int startAt = offset + 1;
                int copyCount = messageBuf.length - startAt;
                byte[] nonceBytes = new byte[copyCount];
                System.arraycopy(messageBuf, startAt, nonceBytes, 0, copyCount);
                debugHex("Received device nonce", nonceBytes);
                nonceGeneration.receivedNonceFromDevice(nonceBytes);
                // Notify the ZWaveSecurityEncapsulationThread since we received a nonce
                notifyEncapsulationThread();
                return;

            case SECURITY_MESSAGE_ENCAP: // SECURITY_MESSAGE_ENCAP should be trapped and handled in {@link
                                         // ApplicationCommandMessageClass}
            case SECURITY_MESSAGE_ENCAP_NONCE_GET: // SECURITY_MESSAGE_ENCAP_NONCE_GET should be trapped and handled in
                                                   // {@link ApplicationCommandMessageClass}
            case SECURITY_COMMANDS_SUPPORTED_REPORT:// Handled by ZWaveSecurityCommandClassInitialization
            case SECURITY_SCHEME_REPORT: // Handled by ZWaveSecurityCommandClassInitialization and is only received
                                         // during secure inclusion
            case SECURITY_NETWORK_KEY_VERIFY: // Handled by ZWaveSecurityCommandClassInitialization and is only received
                                              // during secure inclusion
            case SECURITY_NETWORK_KEY_SET: // Should NEVER be received since we are the controller
            case SECURITY_SCHEME_INHERIT: // Should NEVER be received as this is only used in a controller replication
                                          // environment (unsupported).
                logger.info("NODE {}: Received {} from node but we shouldn't have gotten it.",
                        this.getNode().getNodeId(), commandToString(command));
                return;
            default:
                logger.warn(String.format(
                        "NODE %s: Unsupported Command 0x%02X for command class %s (0x%02X) for message %s.",
                        this.getNode().getNodeId(), command, this.getCommandClass().getLabel(),
                        this.getCommandClass().getKey(), serialMessage));
        }
    }

    protected void processSecurityCommandsSupportedReport(SerialMessage serialMessage, int offset) {
        // This can be received during device inclusion or outside of it depending on
        // SEND_SECURITY_COMMANDS_SUPPORTED_GET_ON_STARTUP
        byte[] messagePayload = serialMessage.getMessagePayload();
        int ourOffset = offset + 1;
        int size = messagePayload.length - ourOffset;
        byte[] secureClassBytes = new byte[size];
        System.arraycopy(messagePayload, ourOffset, secureClassBytes, 0, size);
        traceHex("Supported Security Classes", secureClassBytes);
        getNode().setSecuredClasses(secureClassBytes);
        receivedSecurityCommandsSupportedReport = true;
    }

    public void sendNonceReport() {
        SerialMessage nonceReportMessage = nonceGeneration.generateAndBuildNonceReport();
        if (nonceReportMessage == null) {
            logger.error("NODE {}: generateAndBuildNonceReport returned null", this.getNode().getNodeId());
        } else {
            transmitMessage(nonceReportMessage);
        }
    }

    /**
     * Decrypts a security encapsulated message from the Z-Wave network. Ideally this would return
     * a {@link SerialMessage} but we don't have enough data to do so. So we just return the
     * decrypted payload bytes
     *
     * @param offset the offset at which the command byte exists
     * @param endpoint
     * @param messagePayload
     * @return the decrypted payload bytes. 0=command class, 1=command, 2+=payload
     */
    public byte[] decryptMessage(byte[] data, int offset) {
        if (!checkRealNetworkKeyLoaded()) {
            return null;
        }
        traceHex("in decryptMessage starting at offset, buffer is", data, offset);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        // check for minimum size here so we can ignore the return value of bais.read() below
        int minimumSize = offset + ENCAPSULATED_HEADER_LENGTH + ENCAPSULATED_FOOTER_LENGTH;
        if (data.length < minimumSize) {
            logger.error("NODE {}: Dropping security encapsulated packet which is too small:  min={}, actual={}",
                    this.getNode().getNodeId(), minimumSize, data.length);
            return null;
        }
        try {
            // advance to the command byte
            bais.read(new byte[offset]);
            byte command = (byte) bais.read();
            byte[] initializationVector = new byte[IV_LENGTH];
            // the next 8 bytes of packet are the nonce generated by the device for the IV
            bais.read(initializationVector, 0, HALF_OF_IV);
            traceHex("device nonce", initializationVector, 0, HALF_OF_IV);
            int ciphertextSize = data.length - offset - ENCAPSULATED_HEADER_LENGTH - ENCAPSULATED_FOOTER_LENGTH + 1;
            // Next are the ciphertext bytes
            byte[] ciphertextBytes = new byte[ciphertextSize];
            bais.read(ciphertextBytes);
            logger.trace("NODE {}: Encrypted Packet Sizes: total={}, encrypted={}", this.getNode().getNodeId(),
                    data.length, ciphertextSize);
            traceHex("ciphertextBytes", ciphertextBytes);
            // We stored the nonce that we sent to the device, retrieve it by the id so we can use it in the IV
            byte nonceId = (byte) bais.read();
            Nonce nonceWeSentToDevice = nonceGeneration.getNonceWeGeneratedById(nonceId);
            if (nonceWeSentToDevice == null) { // probably expired
                // Error message logged in ZWaveSecureNonceTracker, just return
                return null;
            }
            System.arraycopy(nonceWeSentToDevice.getNonceBytes(), 0, initializationVector, HALF_OF_IV, HALF_OF_IV);
            traceHex("IV", initializationVector);

            byte[] macFromPacket = new byte[MAC_LENGTH];
            bais.read(macFromPacket);
            Cipher cipher = Cipher.getInstance("AES/OFB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, encryptKey, new IvParameterSpec(initializationVector));
            byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
            traceHex("plaintextBytes", plaintextBytes);

            byte driverNodeId = (byte) this.getController().getOwnNodeId();
            byte[] mac = generateMAC(command, ciphertextBytes, (byte) this.getNode().getNodeId(), driverNodeId,
                    initializationVector);
            if (Arrays.equals(mac, macFromPacket)) {
                logger.trace("NODE {}: MAC Authentication of packet verified OK", this.getNode().getNodeId());
            } else {
                logger.error("NODE {}: MAC Authentication of packet failed. dropping", this.getNode().getNodeId());
                traceHex("full packet", data);
                traceHex("package mac", macFromPacket);
                traceHex("our mac", mac);
                if (DROP_PACKETS_ON_MAC_FAILURE) {
                    return null;
                } else {
                    logger.error("NODE {}: Just kidding, ignored failed MAC Authentication of packet",
                            this.getNode().getNodeId());
                }
            }
            byte sequenceDataByte = plaintextBytes[0];
            if (sequenceDataByte != ZWaveSecurityPayloadFrame.SEQUENCE_BYTE_FOR_SINGLE_FRAME_MESSAGE) {
                // This is a multi frame message which is not yet supported
                logger.error(
                        "NODE {}: Received multi frmae message which is not supported.  Please post this to the OpenHab"
                                + "mailing list so it can be fixed!  bytes=",
                        this.getNode().getNodeId(), SerialMessage.bb2hex(plaintextBytes));
                return null;
            }
            // so we know if we got something that's not supported
            logger.debug("NODE {}: decrypted bytes {}", getNode().getNodeId(), SerialMessage.bb2hex(plaintextBytes));

            if (lastEncapsulatedRequstMessage != null) {
                lastEncapsulatedRequstMessage.securityReponseReceived(plaintextBytes);
            }
            notifyEncapsulationThread();
            return plaintextBytes;
        } catch (Exception e) {
            logger.error("NODE {}: Error decrypting packet", getNode().getNodeId(), e);
            return null;
        }
    }

    /**
     * Queues the given message for security encapsulation and transmission.
     *
     * Note that, per the z-wave spec, we don't just encrypt the message and send it. We need to first request a nonce
     * from the node, wait for that response, then encrypt and send. Therefore this message will be split into one or
     * more security frames, placed into a queue until the next nonce is received. Only then will it be encrypted and
     * sent.
     *
     * @param message
     *            the unencrypted message to be transmitted
     */
    public void queueMessageForEncapsulationAndTransmission(SerialMessage serialMessage) {
        checkInit();
        if (serialMessage.getMessageBuffer().length < 7) {
            logger.error("NODE {}: Message too short for encapsulation, dropping message {}",
                    this.getController().getNode(serialMessage.getMessageNode()).getNodeId(), serialMessage);
            return;
        }

        if (serialMessage.getMessageClass() != SerialMessageClass.SendData) {
            logger.error(String.format("NODE %d: Invalid message class %s (0x%02X) for sendData for message %s",
                    getNode().getNodeId(), serialMessage.getMessageClass().getLabel(),
                    serialMessage.getMessageClass().getKey(), serialMessage.toString()));
        }

        List<ZWaveSecurityPayloadFrame> securityPayloadFrameList = ZWaveSecurityPayloadFrame
                .convertToSecurityPayload(getNode(), serialMessage);
        logger.debug("NODE {}: Converted serial message {} to securityPayload(s): {}", getNode().getNodeId(),
                serialMessage, securityPayloadFrameList);

        if (!payloadEncapsulationQueue.isEmpty()) {
            // Clean up expired items and check for duplicate requests. This is necessary as
            // bombarding a device with messages will typically cause issues and it will stop
            // responding (seen during testing with Kwikset locks)
            Iterator<ZWaveSecurityPayloadFrame> iter = payloadEncapsulationQueue.iterator();
            while (iter.hasNext()) {
                ZWaveSecurityPayloadFrame aFrameFromQueue = iter.next();
                boolean shouldRemove = false;
                byte[] newMessageTwoBytes = new byte[2];
                System.arraycopy(securityPayloadFrameList.get(0).getMessageBytes(), 0, newMessageTwoBytes, 0, 2);
                CommandClass newMessageCommandClass = CommandClass.getCommandClass(newMessageTwoBytes[0] & 0xff);
                // Expired frame check
                if (System.currentTimeMillis() > aFrameFromQueue.getExpirationTime()) {
                    shouldRemove = true;
                    logger.warn("NODE {}: Expired from payloadEncapsulationQueue: {}", getNode().getNodeId(),
                            aFrameFromQueue);
                } else if (SIMILAR_FRAME_COMMAND_CLASS_LIST.contains(newMessageCommandClass)) {
                    // Duplicate message check - if the queue already contains a message like this one, replace it
                    // Compare the first 2 bytes (command class and operation) to do so
                    byte[] aFrameFromQueueTwoBytes = new byte[2];
                    System.arraycopy(aFrameFromQueue.getMessageBytes(), 0, aFrameFromQueueTwoBytes, 0, 2);
                    shouldRemove = Arrays.equals(newMessageTwoBytes, aFrameFromQueueTwoBytes);
                    logger.debug("NODE {}: payloadEncapsulationQueue simliar frame check, shouldRemove={}: {} vs {}",
                            getNode().getNodeId(), shouldRemove,
                            SerialMessage.bb2hex(aFrameFromQueue.getMessageBytes()),
                            SerialMessage.bb2hex(securityPayloadFrameList.get(0).getMessageBytes()));
                }
                if (shouldRemove) {
                    removeFromEncapsulationQueue(aFrameFromQueue, iter, "Newer request received");
                }
            }
        }
        // Finally, since we've cleanup duplicates and removed old entries, we can add the new frame{s} to our queue
        payloadEncapsulationQueue.addAll(securityPayloadFrameList);
        // Wake up the {@link ZWaveSecurityEncapsulationThread} so it can do what it needs to
        notifyEncapsulationThread();
    }

    /**
     * Deletes the give frame using iter.remove(). Will automatically delete subsequent frames as needed
     * for multi-part messages
     *
     * @param frameToRemove the frame to remove, this is used only to check for multiple parts
     * @param iter the iterator which <b>must</b> point to frameToRemove
     * @param reason the reason it is being removed, will be put in the logs
     */
    private void removeFromEncapsulationQueue(ZWaveSecurityPayloadFrame frameToRemove,
            Iterator<ZWaveSecurityPayloadFrame> iter, String reason) {
        logger.info("NODE {}: {} removing from payloadEncapsulationQueue: {}", getNode().getNodeId(), reason,
                frameToRemove);
        boolean hasMultipleParts = frameToRemove.getTotalParts() > 0;
        iter.remove();

        if (hasMultipleParts) {
            if (!iter.hasNext()) {
                logger.warn("NODE {}: security payload frame was marked as having 2 parts, "
                        + "but only found 1 in payloadEncapsulationQueue: {}", this.getNode().getNodeId(), frameToRemove);
            } else {
                ZWaveSecurityPayloadFrame secondFrame = iter.next(); // Go to the 2nd part
                logger.info("NODE {}: Removing 2nd part from payloadEncapsulationQueue: {}", getNode().getNodeId(),
                        secondFrame);
                iter.remove();
            }
        }
    }

    /**
     * Gets the next message from {@link #payloadEncapsulationQueue}, encapsulates (encrypts and MACs) it, then
     * transmits
     * Invoked by {@link ZWaveSecurityEncapsulationThread}. This method must only be called by
     * {@link ZWaveSecurityEncapsulationThread}
     */
    protected void sendNextMessageUsingDeviceNonce() {
        checkInit();
        if (!checkRealNetworkKeyLoaded()) {
            return;
        }
        if (encryptKey == null) {
            // when loaded from xml, encrypt key will be null so we load it here
            setupNetworkKey(false);
        }

        if (payloadEncapsulationQueue.isEmpty()) {
            logger.warn("NODE {}: payloadQueue was empty, returning", this.getNode().getNodeId());
            return;
        }

        Nonce deviceNonce = nonceGeneration.getUseableDeviceNonce();
        if (deviceNonce == null) {
            SerialMessage nonceGetMessage = nonceGeneration.buildNonceGetIfNeeded();
            if (nonceGetMessage == null) {
                // Nothing to do, we are already waiting for a nonce from the device
            } else {
                transmitMessage(nonceGetMessage);
            }
            return;
        }

        // Fetch the next payload from the queue and encapsulate it
        ZWaveSecurityPayloadFrame securityPayload = payloadEncapsulationQueue.poll();
        if (securityPayload == null) {
            logger.warn("NODE {}: payloadQueue was empty, returning", this.getNode().getNodeId());
            return;
        }

        // Encapsulate the message fragment
        traceHex("SecurityPayloadBytes", securityPayload.getMessageBytes());
        // Note that we set the expected reply to that of the original message, as it can vary
        SecurityEncapsulatedSerialMessage message = new SecurityEncapsulatedSerialMessage(SerialMessageClass.SendData,
                SerialMessageType.Request, securityPayload.getOriginalMessage());
        message.setDeviceNonceId(deviceNonce.getNonceBytes()[0]);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write((byte) this.getNode().getNodeId());
        baos.write(securityPayload.getLength() + 20);
        baos.write(this.getCommandClass().getKey());
        byte commandByte = SECURITY_MESSAGE_ENCAP;
        if (USE_SECURITY_MESSAGE_ENCAP_NONCE_GET && !disableEncapNonceGet) {
            boolean useNonceGetMessage = false;
            if (payloadEncapsulationQueue.size() > 0) {
                useNonceGetMessage = true;
                logger.debug("NODE {}: using SECURITY_MESSAGE_ENCAP_NONCE_GET with queue size of {}",
                        this.getNode().getNodeId(), payloadEncapsulationQueue.size());
            } else if (false) { // Check for messages that we know will have a follow-up request that is secure TODO: DB
                                // change flag to AGGRESSIVE, etc? or just remove..
                useNonceGetMessage = bytesAreEqual(securityPayload.getMessageBytes()[0],
                        ZWaveCommandClass.CommandClass.DOOR_LOCK.getKey())
                        && bytesAreEqual(securityPayload.getMessageBytes()[1], ZWaveDoorLockCommandClass.DOORLOCK_SET);
                if (useNonceGetMessage) {
                    logger.debug(
                            "NODE {}: using SECURITY_MESSAGE_ENCAP_NONCE_GET since there will be a followup command",
                            this.getNode().getNodeId());
                }
            }
            if (useNonceGetMessage) {
                commandByte = SECURITY_MESSAGE_ENCAP_NONCE_GET;
                nonceGeneration.sendingEncapNonceGet(message);
            }
        }
        logger.trace("NODE {}: Used nonce to form {} ({}).", this.getNode().getNodeId(), commandToString(commandByte),
                securityPayload.getLogMessage());
        baos.write(commandByte);
        // create the iv
        byte[] initializationVector = new byte[16];
        // Generate a new nonce and fill the first half of the IV buffer with it
        byte[] nonceBytes = nonceGeneration.generateNonceForEncapsulationMessage();
        System.arraycopy(nonceBytes, 0, initializationVector, 0, HALF_OF_IV);
        // the 2nd half of the IV is the nonce provided by the device
        System.arraycopy(deviceNonce.getNonceBytes(), 0, initializationVector, HALF_OF_IV, HALF_OF_IV);

        try {
            // Append the first 8 bytes of the IV (our nonce) to the message
            baos.write(initializationVector, 0, HALF_OF_IV);

            int totalParts = securityPayload.getTotalParts();
            if (totalParts < 1 || totalParts > 2) {
                logger.error("NODE {}: securityPayload had invalid number of parts: {}   Send aborted.",
                        this.getNode().getNodeId(), totalParts);
                return;
            }
            // at most, the payload will be securityPayload length + 1 byte for the sequence byte
            byte[] plaintextMessageBytes = new byte[1 + securityPayload.getLength()];
            plaintextMessageBytes[0] = securityPayload.getSequenceByte();
            System.arraycopy(securityPayload.getMessageBytes(), 0, plaintextMessageBytes, 1,
                    securityPayload.getLength());
            // Append the message payload after encrypting it with AES-OFB
            traceHex("Input frame for encryption:", plaintextMessageBytes);
            traceHex("IV:", initializationVector);

            // This will use hardware AES acceleration when possible (default in JDK 8)
            Cipher encryptCipher = Cipher.getInstance("AES/OFB/NoPadding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, encryptKey, new IvParameterSpec(initializationVector));
            byte[] ciphertextBytes = encryptCipher.doFinal(plaintextMessageBytes);
            traceHex("Encrypted Output", ciphertextBytes);
            baos.write(ciphertextBytes);
            // Append the nonce identifier which is the first byte of the device nonce
            baos.write(deviceNonce.getNonceBytes()[0]);
            int commandClassByteOffset = 2;
            int toMacLength = baos.toByteArray().length - commandClassByteOffset; // Start at command class byte
            byte[] toMac = new byte[toMacLength];
            System.arraycopy(baos.toByteArray(), commandClassByteOffset, toMac, 0, toMacLength);
            // Generate the MAC
            byte sendingNode = (byte) this.getController().getOwnNodeId();
            byte[] mac = generateMAC(commandByte, ciphertextBytes, sendingNode, (byte) getNode().getNodeId(),
                    initializationVector);
            traceHex("Auth mac", mac);
            baos.write(mac);
            byte[] payload = baos.toByteArray();
            debugHex(
                    String.format("Outgoing encrypted message (device nonce=%02X): ", initializationVector[HALF_OF_IV]),
                    payload);
            message.setMessagePayload(payload);
            message.setSecurityPayload(securityPayload);
            lastEncapsulatedRequstMessage = message;
            transmitMessage(message);
        } catch (GeneralSecurityException e) {
            logger.error("NODE {}: Error in sendNextMessageWithNonce, message not sent", e);
        } catch (IOException e) {
            logger.error("NODE {}: Error in sendNextMessageWithNonce, message not sent", e);
        }
    }

    /**
     * Checks the fields which are marked with XStreamOmitField as they will be null
     * upon deserialization from a file
     */
    protected synchronized void checkInit() {
        if (nonceGeneration == null) {
            nonceGeneration = new ZWaveSecureNonceTracker(getNode());
        }
        if (threadLock == null) {
            threadLock = new Object();
        }
        if (payloadEncapsulationQueue == null) {
            payloadEncapsulationQueue = new ConcurrentLinkedQueue<ZWaveSecurityPayloadFrame>();
        }
    }

    public void startSecurityEncapsulationThread() {
        if (encapsulationThread == null) {
            encapsulationThread = new ZWaveSecurityEncapsulationThread();
            encapsulationThread.start();
        }
    }

    // package visible for junit
    void setupNetworkKey(boolean useSchemeZero) {
        logger.info("NODE {}: setupNetworkKey useSchemeZero={}", this.getNode().getNodeId(), useSchemeZero);
        if (useSchemeZero) {
            logger.info("NODE {}: Using Scheme0 Network Key for Key Exchange since we are in inclusion mode.)",
                    this.getNode().getNodeId());
            // Scheme0 network key is a key of all zeros
            networkKey = new SecretKeySpec(new byte[16], AES);
        } else {
            if (!checkRealNetworkKeyLoaded()) {
                return; // Nothing we can do
            }
            // Use the real key
            logger.trace("NODE {}: Using Real Network Key.", this.getNode().getNodeId());
            networkKey = realNetworkKey;
        }

        try {
            // Derived the message encryption key from the network key
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, networkKey);
            encryptKey = new SecretKeySpec(cipher.doFinal(DERIVE_ENCRYPT_KEY), AES);

            // Derived the message auth key from the network key
            cipher.init(Cipher.ENCRYPT_MODE, networkKey);
            authKey = new SecretKeySpec(cipher.doFinal(DERIVE_AUTH_KEY), AES);
        } catch (GeneralSecurityException e) {
            logger.error("NODE " + this.getNode().getNodeId() + ": Error building derived keys", e);
            keyException = e;
        }
    }

    /**
     * @return true if we are in the process of adding this node, ie the controller
     *         and device are performing a secure pair
     */
    protected boolean wasThisNodeJustIncluded() {
        ZWaveInclusionEvent lastInclusionEvent = getNode().getController().getLastIncludeSlaveFoundEvent();
        boolean result = false;
        if (lastInclusionEvent != null && lastInclusionEvent.getEvent() == Type.IncludeSlaveFound
                && getNode().getNodeId() == lastInclusionEvent.getNodeId()) {
            // Check that this node was included very recently
            long twoMinutesAgoMs = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2);
            result = lastInclusionEvent.getIncludedAt().getTime() > twoMinutesAgoMs;
        }
        logger.trace("NODE {}: lastInclusionEvent={} returning={}", this.getNode().getNodeId(), lastInclusionEvent,
                result);
        return result;
    }

    /**
     * Generate the MAC (message authentication code) from a security-encrypted message
     *
     * @throws GeneralSecurityException
     */
    byte[] generateMAC(byte commandClass, byte[] ciphertext, byte sendingNode, byte receivingNode, byte[] iv)
            throws GeneralSecurityException {
        traceHex("generateMAC ciphertext", ciphertext);
        traceHex("generateMAC iv", iv);
        // Build a buffer containing a 4-byte header and the encrypted message data, padded with zeros to a 16-byte
        // boundary.
        int bufferSize = ciphertext.length + 4; // +4 to account for the header
        byte[] buffer = new byte[bufferSize];
        byte[] tempAuth = new byte[16];

        buffer[0] = commandClass;
        buffer[1] = sendingNode;
        buffer[2] = receivingNode;
        buffer[3] = (byte) ciphertext.length;
        System.arraycopy(ciphertext, 0, buffer, 4, ciphertext.length);
        traceHex("generateMAC NetworkKey", networkKey.getEncoded());
        traceHex("generateMAC Raw Auth (minus IV)", buffer);

        // Encrypt the IV with ECB
        Cipher encryptCipher = Cipher.getInstance("AES/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, authKey);
        tempAuth = encryptCipher.doFinal(iv);
        traceHex("generateMAC tmp1", tempAuth);
        // our temporary holding
        byte[] encpck = new byte[16];
        int block = 0;

        // now xor the buffer with our encrypted IV
        for (int i = 0; i < bufferSize; i++) {
            encpck[block] = buffer[i];
            block++;
            // if we hit a blocksize, then xor and encrypt
            if (block == 16) {
                for (int j = 0; j < 16; j++) {
                    // here we do our xor
                    tempAuth[j] = (byte) (encpck[j] ^ tempAuth[j]);
                    encpck[j] = 0;
                }
                // reset encpck for good measure
                Arrays.fill(encpck, (byte) 0);
                // reset our block counter back to 0
                block = 0;

                encryptCipher.init(Cipher.ENCRYPT_MODE, authKey);
                tempAuth = encryptCipher.doFinal(tempAuth);
            }
        }

        // any left over data that isn't a full block size
        if (block > 0) {
            for (int i = 0; i < 16; i++) {
                // encpck from block to 16 is already guaranteed to be 0 so its safe to xor it with out tempAuth
                tempAuth[i] = (byte) (encpck[i] ^ tempAuth[i]);
            }

            encryptCipher.init(Cipher.ENCRYPT_MODE, authKey);
            tempAuth = encryptCipher.doFinal(tempAuth);
        }
        // we only care about the first 8 bytes of tempAuth as the mac
        traceHex("generateMAC Computed Auth", tempAuth);
        byte[] mac = new byte[8];
        System.arraycopy(tempAuth, 0, mac, 0, 8);
        return mac;
    }

    /**
     * Complex as in hard to understand what's going on
     *
     * @deprecated use {@link #generateMAC(byte, byte[], byte, byte, byte[]) instead
     */
    @Deprecated
    byte[] generateMACComplex(byte[] data, int length, byte sendingNode, byte receivingNode, byte[] iv)
            throws GeneralSecurityException {
        traceHex("data", data);
        traceHex("iv", iv);
        // Build a buffer containing a 4-byte header and the encrypted message data, padded with zeros to a 16-byte
        // boundary.
        byte[] buffer = new byte[256];
        byte[] tempAuth = new byte[16];

        buffer[0] = data[0]; // Security command class command
        buffer[1] = sendingNode;
        buffer[2] = receivingNode;
        byte copyLength = (byte) (length - 19); // Subtract 19 to account for the 9 security command class bytes that
        // come before and after the encrypted data
        buffer[3] = copyLength;
        System.arraycopy(data, 9, buffer, 4, copyLength); // Copy the cipher bytes over

        int bufferSize = copyLength + 4; // +4 to account for the header above
        traceHex("Raw Auth (minus IV)", buffer);

        // Encrypt the IV with ECB
        Cipher encryptCipher = Cipher.getInstance("AES/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, authKey);
        tempAuth = encryptCipher.doFinal(iv);
        // our temporary holding
        byte[] encpck = new byte[16];
        int block = 0;

        // now xor the buffer with our encrypted IV
        for (int i = 0; i < bufferSize; i++) {
            encpck[block] = buffer[i];
            block++;
            // if we hit a blocksize, then encrypt
            if (block == 16) {
                for (int j = 0; j < 16; j++) {
                    // here we do our xor
                    tempAuth[j] = (byte) (encpck[j] ^ tempAuth[j]);
                    encpck[j] = 0;
                }
                // reset encpck for good measure
                Arrays.fill(encpck, (byte) 0);
                // reset our block counter back to 0
                block = 0;

                encryptCipher.init(Cipher.ENCRYPT_MODE, authKey);
                tempAuth = encryptCipher.doFinal(tempAuth);
                traceHex("BAD tmp2", tempAuth);
            }
        }
        // any left over data that isn't a full block size
        if (block > 0) {
            for (int i = 0; i < 16; i++) {
                // encpck from block to 16 is already guaranteed to be 0 so its safe to xor it with out tmpmac
                tempAuth[i] = (byte) (encpck[i] ^ tempAuth[i]);
            }
            encryptCipher.init(Cipher.ENCRYPT_MODE, authKey);
            tempAuth = encryptCipher.doFinal(tempAuth);
        }
        /* we only care about the first 8 bytes of tmpauth as the mac */
        traceHex("Computed Auth", tempAuth);
        byte[] mac = new byte[8];
        System.arraycopy(tempAuth, 0, mac, 0, 8);
        return mac;
    }

    /**
     * Utility method to do unsigned byte comparison. This is necessary since in java all primitives are signed but
     * zwave we often represent values in hex (which is unsigned).
     *
     * @param aByte
     *            a byte
     * @param anotherByte
     *            an int
     * @return true if they are equal
     */
    public static boolean bytesAreEqual(byte aByte, int anotherByte) {
        return aByte == ((byte) (anotherByte & 0xff));
    }

    /**
     * Used to set the security key from the config file
     *
     * @param hexString a comma separated hex string, for example: (please DO NOT use this as your key!)
     *            0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10
     */
    public static void setRealNetworkKey(String hexString) {
        try {
            byte[] keyBytes = hexStringToByteArray(hexString);
            ZWaveSecurityCommandClass.realNetworkKey = new SecretKeySpec(keyBytes, "AES");
            logger.info("Update networkKey");
            ZWaveSecurityCommandClass.keyException = null; // we have a valid key
        } catch (IllegalArgumentException e) {
            logger.error("Error parsing zwave:networkKey", e);
            ZWaveSecurityCommandClass.keyException = e;
        }
    }

    protected void notifyEncapsulationThread() {
        long start = System.currentTimeMillis();
        synchronized (threadLock) {
            threadLock.notify();
        }
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed > 500) {
            logger.warn("NODE {}: Took {}ms to get threadLock for notify", getNode().getNodeId(), elapsed);
        }
    }

    public static String commandToString(int command) {
        Byte theByte = Byte.valueOf((byte) (command & 0xff));
        String result = COMMAND_LOOKUP_TABLE.get(theByte);
        if (result == null) {
            return "unknown";
        }
        return result;
    }

    /**
     * Utility method to dump a byte array as hex. Will only print the data if debug
     * mode is debug logging is actually enabled. We don't use {@link SerialMessage#bb2hex(byte[])}
     * because we need our debug format to match that of OZW
     *
     * @param description
     *            a human readable description of the data being logged
     * @param bytes
     *            the bytes to convert to hex and log
     * @param offset
     *            where to start from; zero means log the full byte array
     */
    private void traceHex(String description, byte[] bytesParam, int offset, int length) {
        if (!logger.isTraceEnabled()) {
            return;
        }
        byte[] bytes = bytesParam;
        if (length < bytes.length) {
            bytes = new byte[length];
            System.arraycopy(bytesParam, offset, bytes, 0, length);
        }
        logger.trace("NODE {}: {}={}", getNode().getNodeId(), description, SerialMessage.bb2hex(bytes));
    }

    private void traceHex(String description, byte[] bytes, int offset) {
        traceHex(description, bytes, offset, bytes.length - offset);
    }

    private void debugHex(String description, byte[] bytes, int offset, int length) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        StringBuilder buf = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            buf.append(String.format("%02x ", (bytes[i] & 0xff)));
        }
        String byteString = buf.toString().toUpperCase();
        logger.debug("NODE {}: {}={}", getNode().getNodeId(), description, byteString);
    }

    private void debugHex(String description, byte[] bytes) {
        int offset = 0;
        debugHex(description, bytes, offset, bytes.length - offset);
    }

    /**
     * Utility method to dump a byte array as hex. Will only print the data if debug mode is debug logging is actually
     * enabled
     *
     * @param description
     *            a human readable description of the data being logged
     * @param bytes
     *            the bytes to convert to hex and log
     */
    protected void traceHex(String description, byte[] messagePayload) {
        traceHex(description, messagePayload, 0, messagePayload.length);
    }

    public static byte[] hexStringToByteArray(String hexStringParam) {
        String hexString = hexStringParam.replace("0x", "");
        hexString = hexString.replace(",", "");
        hexString = hexString.replace(" ", "");
        // from https://stackoverflow.com/questions/23354999/hex-string-to-byte-array-conversion
        if ((hexString.length() % 2) != 0) {
            throw new IllegalArgumentException("Input string must contain an even number of characters");
        }

        byte result[] = new byte[hexString.length() / 2];
        char enc[] = hexString.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            StringBuilder curr = new StringBuilder(2);
            curr.append(enc[i]).append(enc[i + 1]);
            result[i / 2] = (byte) Integer.parseInt(curr.toString(), 16);
        }
        return result;
    }

    public static boolean isSecurityNonceReportMessage(SerialMessage serialMessage) {
        if (serialMessage == null) {
            return false;
        }
        if (serialMessage.getMessagePayload() == null || serialMessage.getMessagePayload().length < 3) {
            return false;
        }
        byte[] payloadBytes = serialMessage.getMessagePayload();
        boolean result = (payloadBytes[2] == ((byte) CommandClass.SECURITY.getKey())
                && payloadBytes[3] == SECURITY_NONCE_REPORT);
        if (result) {
            logger.trace("NODE {}: found  Security NonceReportMessage={} payloadBytes={}",
                    serialMessage.getMessageNode(), result, SerialMessage.bb2hex(payloadBytes));
        }
        return result;
    }

    /**
     * Security encapsulation thread. This waits for 1) a device nonce to arrive
     * and 2) the last transaction to be completed. It will then use the device
     * nonce to to security encapsulate the next message in {@link ZWaveSecurityCommandClass#payloadEncapsulationQueue}
     * and give it to the controller for sending
     *
     * @author Dave Badia
     * @since TODO
     */
    private class ZWaveSecurityEncapsulationThread extends Thread {
        /**
         * The default time we will wait to receive a response (or if no response will be sent at all)
         */
        private static final long DEFAULT_WAIT_FOR_RESPONSE = 10000;
        /**
         * If we get a {@link ZWaveSecurityCommandClass#SECURITY_NONCE_GET}, then we know the node
         * is going to send us a security encapsulated response message. Wait additional time to receive that
         */
        private static final long NONCE_GET_ADDON = 20000;

        private ZWaveSecurityEncapsulationThread() {
            super("ZWaveSecurityEncapsulationThreadForNode" + getNode().getNodeId());
        }

        @Override
        public void run() {
            logger.debug("NODE {}: Starting Z-Wave thread: security encapsulation", getNode().getNodeId());
            while (true) {
                try {
                    boolean transmitNext = lastEncapsulatedRequstMessage == null;
                    if (!transmitNext && lastEncapsulatedRequstMessage.hasBeenTransmitted()) {
                        // Recompute the timeout each time
                        long timeOutAt = lastEncapsulatedRequstMessage.getTransmittedAt() + DEFAULT_WAIT_FOR_RESPONSE;
                        boolean expectingResponseMessage = lastNonceGetReceivedAt > lastEncapsulatedRequstMessage
                                .getTransmittedAt();
                        if (expectingResponseMessage) {
                            timeOutAt += NONCE_GET_ADDON;
                        }
                        // See if we have reached the timeout yet
                        if (System.currentTimeMillis() > timeOutAt) {
                            if (expectingResponseMessage) {
                                logger.error("NODE {}: Timed out waiting on response for encapsulated message {}",
                                        getNode().getNodeId(), lastEncapsulatedRequstMessage);
                            } else {
                                logger.debug("NODE {}: no response expected for security transaction {}",
                                        getNode().getNodeId(), lastEncapsulatedRequstMessage);
                            }
                            // SECURITY_MESSAGE_ENCAP_NONCE_GET doesn't always work. If it's not working with a device,
                            // we disable it. Check here to see if it's working
                            logger.debug("NODE {}: TODO DB: remove checking for  SECURITY_MESSAGE_ENCAP_NONCE_GET",
                                    getNode().getNodeId());
                            if (bytesAreEqual(lastEncapsulatedRequstMessage.getMessagePayload()[3],
                                    SECURITY_MESSAGE_ENCAP_NONCE_GET)) {
                                logger.debug("NODE {}: SECURITY_MESSAGE_ENCAP_NONCE_GET are equal",
                                        getNode().getNodeId());
                                if (lastDeviceNonceReceivedAt < lastEncapsulatedRequstMessage.getTransmittedAt()) {
                                    // The last NONCE_REPORT was received before we sent
                                    // SECURITY_MESSAGE_ENCAP_NONCE_GET
                                    // so SECURITY_MESSAGE_ENCAP_NONCE_GET isn't working, disable it
                                    disableEncapNonceGet = true;
                                    logger.error("NODE {}: SECURITY_MESSAGE_ENCAP_NONCE_GET disabled",
                                            getNode().getNodeId());
                                    // Save the setting so we remember
                                    new ZWaveNodeSerializer().SerializeNode(getNode());
                                }
                            }
                            lastEncapsulatedRequstMessage = null;
                            transmitNext = true;
                        }
                    }

                    if (transmitNext && !payloadEncapsulationQueue.isEmpty()) {
                        sendNextMessageUsingDeviceNonce();
                    }

                    synchronized (threadLock) {
                        threadLock.wait(1000);
                    }
                } catch (InterruptedException e) {
                    continue;
                } catch (Exception e) {
                    logger.error("NODE {}: Exception during Z-Wave thread: security encapsulation",
                            getNode().getNodeId(), e);
                }
            }
        }
    }
}
