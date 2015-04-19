
/***
 * Each TR064 service object represents a service offered by fritzbox
 * can be read from tr64desc.xml on fbox
 */
package org.openhab.binding.fritzboxtr064.internal;

public class Tr064Service {
	
	//Service Parameters
	private String _serviceType;
	private String _serviceId;
	private String _controlUrl;
	private String _eventSubUrl;
	private String _scpdurl; 
	
	public Tr064Service() {
		
	}

	public String get_serviceType() {
		return _serviceType;
	}

	public void set_serviceType(String _serviceType) {
		this._serviceType = _serviceType;
	}

	public String get_serviceId() {
		return _serviceId;
	}

	public void set_serviceId(String _serviceId) {
		this._serviceId = _serviceId;
	}

	public String get_controlUrl() {
		return _controlUrl;
	}

	public void set_controlUrl(String _controlUrl) {
		this._controlUrl = _controlUrl;
	}

	public String get_eventSubUrl() {
		return _eventSubUrl;
	}

	public void set_eventSubUrl(String _eventSubUrl) {
		this._eventSubUrl = _eventSubUrl;
	}

	public String get_scpdurl() {
		return _scpdurl;
	}

	public void set_scpdurl(String _scpdurl) {
		this._scpdurl = _scpdurl;
	}

	

	@Override
	public String toString() {
		return "Tr064Service [_serviceType=" + _serviceType + ", _serviceId="
				+ _serviceId + ", _controlUrl=" + _controlUrl
				+ ", _eventSubUrl=" + _eventSubUrl + ", _scpdurl=" + _scpdurl+"]";
	}

	
	
	
	
	

}
