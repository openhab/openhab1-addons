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

/**
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserData4BSTeachInVariant3 extends UserData4BSTeachIn {

    private static final int EEP_RESULT_DB = 0;
    private static final int EEP_RESULT_BIT = 6;

    private static final int LRN_RESULT_DB = 0;
    private static final int LRN_RESULT_BIT = 5;

    private static final int LRN_STATUS_DB = 0;
    private static final int LRN_STATUS_BIT = 4;

    public UserData4BSTeachInVariant3() {
        super();
    }

    public UserData4BSTeachInVariant3(final byte[] data) {
        super(data);
    }

    public EEPResult getEEPResult() {
        if (getDataBit(EEP_RESULT_DB, EEP_RESULT_BIT) == 0) {
            return EEPResult.EEP_NOT_SUPPORTED;
        } else {
            return EEPResult.EEP_SUPPORTED;
        }
    }

    public void setEEPResult(final EEPResult eepResult) {
        switch (eepResult) {
            case EEP_NOT_SUPPORTED:
                setDataBit(EEP_RESULT_DB, EEP_RESULT_BIT, 0);
                break;
            case EEP_SUPPORTED:
                setDataBit(EEP_RESULT_DB, EEP_RESULT_BIT, 1);
                break;
            default:
                throw new IllegalArgumentException("Case not handled: " + eepResult);
        }
    }

    public LearnResult getLearnResult() {
        if (getDataBit(LRN_RESULT_DB, LRN_RESULT_BIT) == 0) {
            return LearnResult.SENDER_ID_DELETED_OR_NOT_STORED;
        } else {
            return LearnResult.SENDER_ID_STORED;
        }
    }

    public void setLearnResult(final LearnResult learnResult) {
        switch (learnResult) {
            case SENDER_ID_DELETED_OR_NOT_STORED:
                setDataBit(LRN_RESULT_DB, LRN_RESULT_BIT, 0);
                break;
            case SENDER_ID_STORED:
                setDataBit(LRN_RESULT_DB, LRN_RESULT_BIT, 1);
                break;
            default:
                throw new IllegalArgumentException("Case not handled: " + learnResult);
        }
    }

    public LearnStatus getLearnStatus() {
        if (getDataBit(LRN_STATUS_DB, LRN_STATUS_BIT) == 0) {
            return LearnStatus.QUERY;
        } else {
            return LearnStatus.RESPONSE;
        }
    }

    public void setLearnStatus(final LearnStatus learnStatus) {
        switch (learnStatus) {
            case QUERY:
                setDataBit(LRN_STATUS_DB, LRN_STATUS_BIT, 0);
                break;
            case RESPONSE:
                setDataBit(LRN_STATUS_DB, LRN_STATUS_BIT, 1);
                break;
            default:
                throw new IllegalArgumentException("Case not handled: " + learnStatus);
        }
    }

    @Override
    public String toString() {
        return String.format("UserData4BSTeachInVariant3{%s, eepResult=%s, learnResult=%s, learnStatus=%s}",
                             super.toString(),
                             getEEPResult(),
                             getLearnResult(),
                             getLearnStatus());
    }

}
