/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	 * @param telegram
	 */
	public void onTelegramReceived(EBusTelegram telegram);
	
}
