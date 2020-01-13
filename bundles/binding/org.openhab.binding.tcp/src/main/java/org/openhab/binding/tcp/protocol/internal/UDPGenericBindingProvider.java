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
package org.openhab.binding.tcp.protocol.internal;

import org.openhab.binding.tcp.protocol.UDPBindingProvider;

/**
 *
 * udp=">[ON:192.168.0.1:3000:some text], >[OFF:192.168.0.1:3000:some other command]"
 * udp="<[192.168.0.1:3000]" - for String, Number,... Items
 *
 * @author Karel Goderis
 * @since 1.1.0
 *
 */

public class UDPGenericBindingProvider extends ProtocolGenericBindingProvider implements UDPBindingProvider {

    @Override
    public String getBindingType() {
        return "udp";
    }
}
