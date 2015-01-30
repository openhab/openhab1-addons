/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The battery of a MAX!Cube {@link Device}.
 * 
 * @author Dominic Lerbs
 * @since 1.7.0
 */
public class Battery {
	
	private static final Logger logger = LoggerFactory.getLogger(Battery.class);
	private Charge charge = Charge.UNKNOWN;
	private boolean chargeUpdated;

	public void setCharge(Charge charge) {
		chargeUpdated = (this.charge != charge);
		if (chargeUpdated){
			logger.info("Battery charge changed from " + this.charge + " to " + charge);
			this.charge = charge;
		}
	}

	public State getCharge() {
		return new StringType(charge.getText());
	}

	public boolean isChargeUpdated() {
		return chargeUpdated;
	}
	
	
	/** Charging state of the battery. */
	public enum Charge {
		UNKNOWN("n/a"), OK("ok"), LOW("low");

		private final String text;

		private Charge(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
}
