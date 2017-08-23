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

import org.openhab.binding.aleoncean.internal.AleonceanBindingConfig;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class WorkerItemBindingChanged extends WorkerItem {

    private final String itemName;
    private final AleonceanBindingConfig config;

    public WorkerItemBindingChanged(final String itemName, final AleonceanBindingConfig config) {
        this.itemName = itemName;
        this.config = config;
    }

    public String getItemName() {
        return itemName;
    }

    public AleonceanBindingConfig getConfig() {
        return config;
    }

}
