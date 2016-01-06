
/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;


/***
 * Each TR064 service object represents a service offered by fritzbox
 * can be read from tr64desc.xml on fbox
 * @author gitbock
 * @since 1.8.0
 */
public class Tr064Service {
	
	//Service Parameters
	private String _serviceType;
	private String _serviceId;
	private String _controlUrl;
	private String _eventSubUrl;
	private String _scpdurl; 
	
	public Tr064Service() {
		
	}

	public String getServiceType() {
		return _serviceType;
	}

	public void setServiceType(String _serviceType) {
		this._serviceType = _serviceType;
	}

	public String getServiceId() {
		return _serviceId;
	}

	public void setServiceId(String _serviceId) {
		this._serviceId = _serviceId;
	}

	public String getControlUrl() {
		return _controlUrl;
	}

	public void setControlUrl(String _controlUrl) {
		this._controlUrl = _controlUrl;
	}

	public String getEventSubUrl() {
		return _eventSubUrl;
	}

	public void setEventSubUrl(String _eventSubUrl) {
		this._eventSubUrl = _eventSubUrl;
	}

	public String getScpdurl() {
		return _scpdurl;
	}

	public void setScpdurl(String _scpdurl) {
		this._scpdurl = _scpdurl;
	}

	

	@Override
	public String toString() {
		return "Tr064Service [_serviceType=" + _serviceType + ", _serviceId="
				+ _serviceId + ", _controlUrl=" + _controlUrl
				+ ", _eventSubUrl=" + _eventSubUrl + ", _scpdurl=" + _scpdurl+"]";
	}

	
	
	
	
	

}
