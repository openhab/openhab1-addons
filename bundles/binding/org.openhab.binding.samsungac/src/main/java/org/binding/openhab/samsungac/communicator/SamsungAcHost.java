package org.binding.openhab.samsungac.communicator;

public class SamsungAcHost {

	private AirConditioner airConditioner;
	
	private String ipAddress = "127.0.0.1";
	private String macAddress;
	private String token;
	
	public void connect() {
		airConditioner = new AirConditioner(ipAddress, macAddress, token);
		airConditioner.login();
	}
	
	public boolean isConnected() {
		return airConditioner != null && airConditioner.isConnected();
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public AirConditioner getAirConditioner() {
		return airConditioner;
	}
	public void setAirConditioner(AirConditioner airConditioner) {
		this.airConditioner = airConditioner;
	}
	
	
}
