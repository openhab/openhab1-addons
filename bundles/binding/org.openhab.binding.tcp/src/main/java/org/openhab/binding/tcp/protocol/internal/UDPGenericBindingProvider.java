/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
