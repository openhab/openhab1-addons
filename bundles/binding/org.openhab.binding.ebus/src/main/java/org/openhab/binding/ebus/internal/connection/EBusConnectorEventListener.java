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
package org.openhab.binding.ebus.internal.connection;

import org.openhab.binding.ebus.internal.EBusTelegram;

/**
 * This listener is called if the connector received a valid eBus telegram.
 *
 * @author Christian Sowada
 * @since 1.7.0
 */
public interface EBusConnectorEventListener {

    /**
     * A new valid telegram has been received.
     * 
     * @param telegram
     */
    public void onTelegramReceived(EBusTelegram telegram);

}
