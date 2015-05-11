/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.types.State;

/**
 * Typical D99 Service Typical TIMESTAMP - LAST NODE UPGRADE
 * 
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissTServiceNODE_TIMESTAMP extends SoulissGenericTypical {
	// Parameters sSoulissNode, iSlot, Type, State are stored in the class

	// private static final short SERVICE_SLOT=998;
	private static String timestamp;

	public SoulissTServiceNODE_TIMESTAMP(String sSoulissNodeIPAddressOnLAN,
			int iIDNodo, int iSlot, String sOHType) {
		super();
		this.setSlot(iSlot);
		this.setSoulissNodeID(iIDNodo);
		this.setType(Constants.Souliss_TService_NODE_TIMESTAMP);
		this.setNote(sOHType);
	}

	/**
	 * Log the timestamp
	 * 
	 * @return String timestamp
	 */
	public String getTIMESTAMP() {
		return timestamp;
	}

	/**
	 * Set timestamp
	 * 
	 * @param string
	 */
	public void setTIMESTAMP(String string) {
		timestamp = string;
		setUpdatedTrue();
	}

	/**
	 * Return as openHAB type DateTimeType
	 */
	@Override
	public State getOHState() {
		if (timestamp!=null) 
		return DateTimeType.valueOf(timestamp);
		else 
			return null;

	}
}
