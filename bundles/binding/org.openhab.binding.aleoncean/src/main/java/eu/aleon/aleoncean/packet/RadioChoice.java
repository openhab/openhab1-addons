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
package eu.aleon.aleoncean.packet;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RadioChoice {

    /**
     * This is a not defined radio choice, we use it to signal an unset one.
     */
    public static final byte UNSET = (byte) 0x00;

    /**
     * Repeated switch communication.
     */
    public static final byte RORG_RPS = (byte) 0xF6;

    /**
     * 1 Byte communication.
     */
    public static final byte RORG_1BS = (byte) 0xD5;

    /**
     * 4 Byte communication.
     */
    public static final byte RORG_4BS = (byte) 0xA5;

    /**
     * Variable length data.
     */
    public static final byte RORG_VLD = (byte) 0xD2;

    /**
     * Manufacturer specific communication.
     */
    public static final byte RORG_MSC = (byte) 0xD1;

    /**
     * Addressing destination telegram.
     */
    public static final byte RORG_ADT = (byte) 0xA6;

    /**
     * Smart Ack learn request.
     */
    public static final byte RORG_SM_LRN_REQ = (byte) 0xC6;

    /**
     * Smart Ack learn answer.
     */
    public static final byte RORG_SM_LRN_ANS = (byte) 0xC7;

    /**
     * Smart Ack reclaim.
     */
    public static final byte RORG_SM_REC = (byte) 0xA7;

    /**
     * Remote management.
     */
    public static final byte RORG_SYS_EX = (byte) 0xC5;

    /**
     * Secure telegram.
     */
    public static final byte RORG_SEC = (byte) 0x30;

    /**
     * Secure telegram with R-ORG encapsulation.
     */
    public static final byte RORG_SEC_ENCAPS = (byte) 0x31;

    /**
     * UTE - Universal uni- and bidirectional teach-in.
     */
    public static final byte RORG_UTE = (byte) 0xD4;

    private RadioChoice() {
    }

}
