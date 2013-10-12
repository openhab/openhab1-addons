/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

/**
 * This interface defines interface which every message class should implement.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public interface RFXComMessageInterface {

	/**
	 * Procedure for present class information in string format. Used for
	 * logging purposes.
	 * 
	 */
	String toString();

	/**
	 * Procedure for encode raw data.
	 * 
	 * @param data
	 *            Raw data.
	 */
	void encodeMessage(byte[] data);

	/**
	 * Procedure for decode object to raw data.
	 * 
	 * @return raw data.
	 */
	byte[] decodeMessage();
}
