/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.p1telegram;

/**
 * P1TelegramParserState stores the state of the P1TelegramParser The state
 * consists of the following attributes:
 * <ul>
 * <li>receivedData (all parsed data of a single P1 telegram)
 * <li>header (the header string of a single P1 telegram)
 * <li>obisId (the last OBIS Identifier seen or parsing)
 * <li>obisValue (the last OBIS value seen or parsing)
 * <li>crcValue (the last crc value seen or parsing)
 * <li>state (state in the P1 telegram)
 * </ul>
 * 
 * @author mvolaart
 * @since 1.7.0
 */
class P1TelegramParserState {
	static enum State {
		// Wait for the '/' character
		WAIT_FOR_START,
		// '/' character seen
		STARTED,
		// Parsing data after the '/' character
		HEADER,
		// Waiting for the header to end with a CR & LF
		CRLF,
		// Handling OBIS Identifier
		DATA_OBIS_ID,
		// OBIS value start seen '('
		DATA_OBIS_VALUE_START,
		// Parsing OBIS value
		DATA_OBIS_VALUE,
		// OBIS value end seen ')'
		DATA_OBIS_VALUE_END,
		// Parsing CRC value following '!'
		CRC_VALUE
	};

	/* internal state variables */
	private String receivedData = "";
	private String header = "";
	private String obisId = "";
	private String obisValue = "";
	private String crcValue = "";
	private State state = State.WAIT_FOR_START;

	/**
	 * Creates a new P1TelegramParser object
	 */
	P1TelegramParserState() {
	}

	/**
	 * Stores a single character
	 * 
	 * @param c
	 *            the character to store
	 */
	void handleCharacter(char c) {

		switch (state) {
		case WAIT_FOR_START:
			// ignore the data
			break;
		case STARTED:
			header += c;
			receivedData += c;
			break;
		case HEADER:
			header += c;
			receivedData += c;
			break;
		case CRLF:
			receivedData += c;
			break;
		case DATA_OBIS_ID:
			obisId += c;
			receivedData += c;
			break;
		case DATA_OBIS_VALUE_START:
			receivedData += c;
			break;
		case DATA_OBIS_VALUE:
			obisValue += c;
			receivedData += c;
			break;
		case DATA_OBIS_VALUE_END:
			receivedData += c;
			break;
		case CRC_VALUE:
			crcValue += c;
			// CRC data is not part of received data
			break;
		default:
			break;
		}
	}

	/**
	 * Clear internal state
	 */
	private void clearInternalData() {
		header = "";
		obisId = "";
		obisValue = "";
		receivedData = "";
		crcValue = "";
	}

	/**
	 * Clears the received OBIS Identifer
	 */
	private void clearObisId() {
		obisId = "";
	}

	/**
	 * Clears the current OBIS value
	 */
	private void clearObisValue() {
		obisValue = "";
	}

	/**
	 * @return the state
	 */
	State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	void setState(State newState) {
		switch (newState) {
		case WAIT_FOR_START:
			clearInternalData();
			break;
		case DATA_OBIS_ID:
			clearObisId();
			clearObisValue();
			break;
		case DATA_OBIS_VALUE_START:
			break;
		case DATA_OBIS_VALUE:
			clearObisValue();
			break;
		default:
			break;
		}

		state = newState;
	}

	/**
	 * @return the meterId
	 */
	String getHeader() {
		return header;
	}

	/**
	 * @return the obisId
	 */
	String getObisId() {
		return obisId;
	}

	/**
	 * @return the obisValue
	 */
	String getObisValue() {
		return obisValue;
	}

	/**
	 * @return the crcValue
	 */
	String getCrcValue() {
		return crcValue;
	}

	/**
	 * @return the receivedData
	 */
	String getReceivedData() {
		return receivedData;
	}

	@Override
	public String toString() {
		return state + ", header = " + header + ", obisId = " + obisId
				+ ", obisValue = " + obisValue;
	}
}
