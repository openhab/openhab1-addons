/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.openwebif.internal.impl.model;

/**
 * Definition of all MessageTypes.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public enum MessageType {
	WARNING, INFO, ERROR;

	/**
	 * Returns the id to send to the receiver.
	 */
	public String getId() {
		switch (this) {
		case WARNING:
			return "1";
		case INFO:
			return "2";
		case ERROR:
			return "3";
		}
		return null;
	}
}
