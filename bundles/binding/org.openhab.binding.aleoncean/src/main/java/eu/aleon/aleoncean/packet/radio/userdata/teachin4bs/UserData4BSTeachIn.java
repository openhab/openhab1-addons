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
package eu.aleon.aleoncean.packet.radio.userdata.teachin4bs;

import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;
import eu.aleon.aleoncean.values.LearnType4BS;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class UserData4BSTeachIn extends UserData4BS {

    public static final int FUNC_MIN = 0x00;
    public static final int FUNC_MAX = 0x3F;
    public static final int TYPE_MIN = 0x00;
    public static final int TYPE_MAX = 0x7F;
    public static final int MANUFACTURER_ID_MIN = 0x0000;
    public static final int MANUFACTURER_ID_MAX = 0x07FF;

    private static final int LEARN_TYPE_DB = 0;
    private static final int LEARN_TYPE_BIT = 7;

    public UserData4BSTeachIn() {
        super();
        setTeachIn(true);
    }

    public UserData4BSTeachIn(final byte[] data) {
        super(data);
    }

    public byte getFunc() {
        return (byte) getDataRange(3, 7, 3, 2);
    }

    public void setFunc(final byte func) {
        setDataRange(func, 3, 7, 3, 2);
    }

    public byte getType() {
        return (byte) getDataRange(3, 1, 2, 3);
    }

    public void setType(final byte type) {
        setDataRange(type, 3, 1, 2, 3);
    }

    public short getManufacturerId() {
        return (short) getDataRange(2, 2, 1, 0);
    }

    public void setManufacturerId(final short manufacturerId) {
        setDataRange(manufacturerId, 2, 2, 1, 0);
    }

    public LearnType4BS getLearnType() {
        if (getDataBit(LEARN_TYPE_DB, LEARN_TYPE_BIT) == 0) {
            return LearnType4BS.WITHOUT_EEP_NUM_WITHOUT_MANU_ID;
        } else {
            return LearnType4BS.WITH_EEP_NUM_WITH_MANU_ID;
        }
    }

    public void setLearnType(final LearnType4BS learnType) {
        switch (learnType) {
            case WITHOUT_EEP_NUM_WITHOUT_MANU_ID:
                setDataBit(LEARN_TYPE_DB, LEARN_TYPE_BIT, 0);
                break;
            case WITH_EEP_NUM_WITH_MANU_ID:
                setDataBit(LEARN_TYPE_DB, LEARN_TYPE_BIT, 1);
                break;
            default:
                throw new IllegalArgumentException("Case not handled: " + learnType);
        }
    }

    @Override
    public String toString() {
        return String.format("UserData4BSTeachIn{%s, func=0x%02X, type=0x%02X, manufacturerId=0x%04X, learnType=%s}",
                             super.toString(),
                             getFunc(),
                             getType(),
                             getManufacturerId(),
                             getLearnType());
    }

}
