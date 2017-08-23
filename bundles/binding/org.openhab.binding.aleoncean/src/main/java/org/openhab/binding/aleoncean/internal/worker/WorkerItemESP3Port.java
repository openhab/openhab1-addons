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
package org.openhab.binding.aleoncean.internal.worker;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class WorkerItemESP3Port extends WorkerItem {

    private final String esp3Port;

    public WorkerItemESP3Port(final String esp3Port) {
        this.esp3Port = esp3Port;
    }

    public String getESP3Port() {
        return esp3Port;
    }

}
