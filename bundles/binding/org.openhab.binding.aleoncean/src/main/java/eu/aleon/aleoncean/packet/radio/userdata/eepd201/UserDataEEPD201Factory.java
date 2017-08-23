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
package eu.aleon.aleoncean.packet.radio.userdata.eepd201;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201Factory {

    public static UserDataEEPD201 createFromUserDataRaw(final byte[] raw) {
        final UserDataEEPD201 userData;

        switch (UserDataEEPD201.getCmd(raw)) {
            case UserDataEEPD201CMD01.CMD:
                userData = new UserDataEEPD201CMD01(raw);
                break;
            case UserDataEEPD201CMD02.CMD:
                userData = new UserDataEEPD201CMD02(raw);
                break;
            case UserDataEEPD201CMD03.CMD:
                userData = new UserDataEEPD201CMD03(raw);
                break;
            case UserDataEEPD201CMD04.CMD:
                userData = new UserDataEEPD201CMD04(raw);
                break;
            case UserDataEEPD201CMD05.CMD:
                userData = new UserDataEEPD201CMD05(raw);
                break;
            case UserDataEEPD201CMD06.CMD:
                userData = new UserDataEEPD201CMD06(raw);
                break;
            case UserDataEEPD201CMD07.CMD:
                userData = new UserDataEEPD201CMD07(raw);
                break;
            default:
                userData = new UserDataEEPD201((byte) 0x00, raw);
                break;
        }

        return userData;
    }

    private UserDataEEPD201Factory() {
    }

}
