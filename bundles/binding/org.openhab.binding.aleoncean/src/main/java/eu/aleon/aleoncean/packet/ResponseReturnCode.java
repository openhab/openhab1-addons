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
public class ResponseReturnCode {

    /**
     * The command is understood and triggered (OK).
     */
    public static final byte RET_OK = (byte) 0;

    /**
     * There is an error occurred.
     */
    public static final byte RET_ERROR = (byte) 1;

    /**
     * The functionality is not supported by that implementation.
     */
    public static final byte RET_NOT_SUPPORTED = (byte) 2;

    /**
     * There was a wrong parameter in the command.
     */
    public static final byte RET_WRONG_PARAM = (byte) 3;

    /**
     * The operation was denied.
     *
     * For example: memory access denied (code-protected).
     */
    public static final byte RET_OPERATION_DENIED = (byte) 4;

    /*
     * Return codes greater than 0x80 are used for commands with special return information, not commonly useable.
     */
    /**
     * The write/erase/verify process failed, the flash page seems to be corrupted.
     */
    public static final byte FLASH_HW_ERROR = (byte) 0x82;

    /**
     * Invalid base ID.
     */
    public static final byte BASEID_OUT_OF_RANGE = (byte) 0x90;

    /**
     * BaseID was changed 10 times, no more changes are allowed.
     */
    public static final byte BASEID_MAX_REACHED = (byte) 0x91;

    private ResponseReturnCode() {
    }

}
