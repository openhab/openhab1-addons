/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal;

import java.util.EventObject;
import org.openhab.binding.dscalarm.internal.protocol.APIMessage;

/**
 * Event for Receiving API Messages.
 * @author Russell Stephens
 * @since 1.6.0
 */
public class DSCAlarmEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private APIMessage apiMessage;
	
	/**
	 * Constructor. Creates a new instance of the DSC Alarm event class.
	 * @param source
	 */
	public DSCAlarmEvent(Object source) {
		super(source);
	}

	/**
	 * Adds the the received API Message to the event.
	 * @param apiMessage
	 */
	public void dscAlarmEventMessage(APIMessage apiMessage) {
		this.apiMessage = apiMessage;
	}
	
	/**
	 * Returns the API Message event from the DSC Alarm System
	 * @return apiMessage
	 */
	public APIMessage getAPIMessage() {
		return apiMessage;
	}

}
