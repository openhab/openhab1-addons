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

import eu.aleon.aleoncean.values.LearnType4BS;

/**
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserData4BSTeachInVariant1 extends UserData4BSTeachIn {

    public UserData4BSTeachInVariant1() {
        super();
        setLearnType(LearnType4BS.WITHOUT_EEP_NUM_WITHOUT_MANU_ID);
    }

    public UserData4BSTeachInVariant1(final byte[] data) {
        super(data);
    }

}
