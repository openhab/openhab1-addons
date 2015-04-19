package org.openhab.binding.fritzboxtr064.internal;

/***
 * Represents a item mapping. An item mapping is a collection of all parameters 
 * based on the config string of the item. config string is like the key of an item mapping
 * Item Mappings must be created manually to support desired fbox tr064 functions.
 * 
 * @author gitbock
 *
 */
public class ItemMap{
	//common parameters 
	private String _itemCommand;     // matches itemconfig
	private String _serviceId;       // SOAP service ID
	
	//read specific 
	private String _readServiceCommand; // command to execute on fbox if value should be read
	private String _readDataInName;		//name of parameter to put in soap request to read value
	private String _readDataOutName;		//name of parameter to extract from fbox soap response when reading value (is parsed as value)
	private SoapValueParser _soapValueParser; 		//handler to use for parsing soapresponse
	
	// write specific
	private String _writeServiceCommand; // command to execute on fbox if value should be set
	
	
	public String get_writeServiceCommand() {
		return _writeServiceCommand;
	}
	public void set_writeServiceCommand(String _writeServiceCommand) {
		this._writeServiceCommand = _writeServiceCommand;
	}
	private String _writeDataInName;		//name of parameter which is put in soap request when setting an option on fbox
	
	
	public String get_writeDataInName() {
		return _writeDataInName;
	}
	public void set_writeDataInName(String _setDataInName) {
		this._writeDataInName = _setDataInName;
	}
	
	public SoapValueParser get_soapValueParser() {
		return _soapValueParser;
	}
	public void set_soapValueParser(SoapValueParser _svp) {
		this._soapValueParser = _svp;
	}
	
	public ItemMap(String _itemCommand, String _getServiceCommand,
			String _serviceId, String _getDataInName1, String _getDataOutName1) {
		this._itemCommand = _itemCommand;
		this._readServiceCommand = _getServiceCommand;
		this._serviceId = _serviceId;
		this._readDataInName = _getDataInName1;
		this._readDataOutName = _getDataOutName1;
	}
	public String get_itemCommand() {
		return _itemCommand;
	}
	public void set_itemCommand(String _itemCommand) {
		this._itemCommand = _itemCommand;
	}
	public String get_readServiceCommand() {
		return _readServiceCommand;
	}
	public void set_readServiceCommand(String _serviceCommand) {
		this._readServiceCommand = _serviceCommand;
	}
	public String get_serviceId() {
		return _serviceId;
	}
	public void set_serviceId(String _serviceId) {
		this._serviceId = _serviceId;
	}
	public String get_readDataInName() {
		return _readDataInName;
	}
	public void set_readDataInName1(String _dataInName1) {
		this._readDataInName = _dataInName1;
	}
	public String get_readDataOutName() {
		return _readDataOutName;
	}
	public void set_readDataOutName(String _dataOutName1) {
		this._readDataOutName = _dataOutName1;
	}
	@Override
	public String toString() {
		return "ItemMap [_itemCommand=" + _itemCommand + ", _serviceId="
				+ _serviceId + ", _readServiceCommand=" + _readServiceCommand
				+ ", _readDataInName=" + _readDataInName
				+ ", _readDataOutName=" + _readDataOutName + ", _svp=" + _soapValueParser
				+ ", _writeDataInName=" + _writeDataInName + "]";
	}
	
	
	
	
}
