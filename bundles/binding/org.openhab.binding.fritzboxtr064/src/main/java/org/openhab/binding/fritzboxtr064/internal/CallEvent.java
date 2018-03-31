/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Call Events received from fbox.
 * Objects of this class represent parsed lines from fbox
 * call events
 *
 * 12.10.15 20:24:22;RING;0;01715478654;6547841;SIP2;
 * 12.10.15 20:24:24;DISCONNECT;0;0;
 *
 * @author gitbock
 * @since 1.8.0
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

    // default logger
    private static final Logger logger = LoggerFactory.getLogger(CallEvent.class);

    public CallEvent(String rawEvent) {
        _raw = rawEvent;
    }

    public String getLine() {
        return _line;
    }

    public void setLine(String line) {
        _line = line;
    }

    public String getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(String timestamp) {
        this._timestamp = timestamp;
    }

    public String getCallType() {
        return _callType;
    }

    public void setCallType(String callType) {
        _callType = callType;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getExternalNo() {
        return _externalNo;
    }

    public void setExternalNo(String externalNo) {
        _externalNo = externalNo;
    }

    public String getInternalNo() {
        return _internalNo;
    }

    public void setInternalNo(String internalNo) {
        _internalNo = internalNo;
    }

    public String getConnectionType() {
        return _connectionType;
    }

    public void setConnectionType(String connectionType) {
        _connectionType = connectionType;
    }

    public String getRaw() {
        return _raw;
    }

    public void setRaw(String raw) {
        _raw = raw;
    }

    /**
     * parses the raw event string/line from fbox into fields of object
     *
     * @return true if parsing was successful
     */
    public boolean parseRawEvent() {
        if (_raw == null) { // check if we have something to parse
            logger.warn("Cannot parse call event. No input provided!");
            return false;
        }

        String[] fields = _raw.split(";");
        if (fields.length < 4) {
            logger.warn("Cannot parse call event. Unexpected line received: {}", _raw);
            return false;
        }

        _timestamp = fields[0];
        _callType = fields[1];
        _id = fields[2];

        if (_callType.equals("RING")) {
            _externalNo = fields[3];
            _internalNo = fields[4];
            _connectionType = fields[5];
        } else if (_callType.equals("CONNECT")) {
            _line = fields[3];
            if (fields.length > 4) {
                _externalNo = fields[4];
            } else {
                _externalNo = "Unknown";
            }
        } else if (_callType.equals("CALL")) {
            _line = fields[3];
            _internalNo = fields[4];
            _externalNo = fields[5];
            _connectionType = fields[6];
        }

        logger.debug("Successfully parsed Call Event: {}", this);
        return true;
    }

    @Override
    public String toString() {
        return "CallEvent [_timestamp=" + _timestamp + ", _callType=" + _callType + ", _id=" + _id + ", _externalNo="
                + _externalNo + ", _internalNo=" + _internalNo + ", _connectionType=" + _connectionType + ", _line="
                + _line + "]";
    }
}
