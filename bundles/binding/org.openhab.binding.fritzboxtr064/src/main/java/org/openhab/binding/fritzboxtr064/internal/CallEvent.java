/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 
 * Call Events received from fbox.
 * Objects of this class represent parsed lines from fbox
 * call events
 * 
 *  12.10.15 20:24:22;RING;0;01715478654;6547841;SIP2;
 *  12.10.15 20:24:24;DISCONNECT;0;0;
 * 
 * @author gitbock
 * @since 1.8.0
 *
 */
public class CallEvent {
	private String _timestamp;
	private String _callType;
	private String _id;
	private String _externalNo;
	private String _internalNo;
	private String _connectionType;
	private String _raw;
	private String _line;
	
	//default logger
	private static final Logger logger =  LoggerFactory.getLogger(FritzboxTr064Binding.class);
	
	public CallEvent(String rawEvent) {
		this._raw = rawEvent;

	}
	
	
	public String getLine() {
		return _line;
	}


	public void setLine(String _line) {
		this._line = _line;
	}


	public String getTimestamp() {
		return _timestamp;
	}
	public void setTimestamp(String _timestamp) {
		this._timestamp = _timestamp;
	}
	public String getCallType() {
		return _callType;
	}
	public void setCallType(String _callType) {
		this._callType = _callType;
	}
	public String getId() {
		return _id;
	}
	public void setId(String _id) {
		this._id = _id;
	}
	public String getExternalNo() {
		return _externalNo;
	}
	public void setExternalNo(String _externalNo) {
		this._externalNo = _externalNo;
	}
	public String getInternalNo() {
		return _internalNo;
	}
	public void setInternalNo(String _internalNo) {
		this._internalNo = _internalNo;
	}
	public String getConnectionType() {
		return _connectionType;
	}
	public void setConnectionType(String _connectionType) {
		this._connectionType = _connectionType;
	}
	public String getRaw() {
		return _raw;
	}
	public void setRaw(String _raw) {
		this._raw = _raw;
	}
	
	
	/***
	 * parses the raw event string/line from fbox into fields of object
	 * @return true if parsing was successful
	 */
	public boolean parseRawEvent() {
		if(_raw == null){ //check if we have something to parse
			logger.error("Cannot parse call event. No input provided!");
			return false;
		}
		String[] fields = _raw.split(";");
		if(fields.length < 4){
			logger.error("Cannot parse call event. Unexpected line received {}",_raw);
			return false;
		}
		this._timestamp = fields[0];
		this._callType = fields[1];
		this._id = fields[2];
		if (this._callType.equals("RING")) {
			this._externalNo = fields[3];
			this._internalNo = fields[4];
			this._connectionType = fields[5];
		} else if (this._callType.equals("CONNECT")) {
			this._line = fields[3];
			this._externalNo = fields[4];
		} else if (this._callType.equals("CALL")) {
			this._line = fields[3];
			this._internalNo = fields[4];
			this._externalNo = fields[5];
			this._connectionType = fields[6];
		}
		logger.debug("Successfully parsed Call Event: {}",this.toString());
		return true;
	}


	@Override
	public String toString() {
		return "CallEvent [_timestamp=" + _timestamp + ", _callType="
				+ _callType + ", _id=" + _id + ", _externalNo=" + _externalNo
				+ ", _internalNo=" + _internalNo + ", _connectionType="
				+ _connectionType + ", _line=" + _line + "]";
	}
	
	
	
	

	
}
