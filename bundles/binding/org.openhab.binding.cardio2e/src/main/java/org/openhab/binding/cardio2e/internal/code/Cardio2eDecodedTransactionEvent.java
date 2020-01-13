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
package org.openhab.binding.cardio2e.internal.code;

import java.util.EventObject;

/**
 * Cardio2e Decoded Transaction Event class.
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public class Cardio2eDecodedTransactionEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private Cardio2eTransaction decodedTransaction = null;

	public Cardio2eDecodedTransactionEvent(Object source,
			Cardio2eTransaction decodedTransaction) {
		super(source);
		this.decodedTransaction = decodedTransaction;
	}

	public Cardio2eTransaction getDecodedTransaction() {
		return decodedTransaction;
	}
}