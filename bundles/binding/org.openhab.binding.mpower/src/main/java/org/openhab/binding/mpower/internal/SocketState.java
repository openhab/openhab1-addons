package org.openhab.binding.mpower.internal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This transforms the JSON data into a nice object
 * 
 * @author magcode
 *
 */
public class SocketState {
	private double voltage;
	private double power;
	private boolean on;
	private int socket;
	private String address;

	public SocketState(String json, String address) {
		setAddress(address);
		JSONParser parser = new JSONParser();

		try {
			JSONObject root = (JSONObject) parser.parse(json);
			JSONArray sensors = (JSONArray) root.get("sensors");
			JSONObject oneSensor = (JSONObject) sensors.get(0);
			Object ob = oneSensor.get("voltage");
			if (ob instanceof Double) {
				setVoltage((Double) ob);
			}
			ob = oneSensor.get("power");
			if (ob instanceof Double) {
				setPower((Double) ob);
			}
			ob = oneSensor.get("port");
			if (ob instanceof Long) {
				Long sock = (Long) ob;
				setSocket(sock.intValue());
			}

		} catch (ParseException pe) {
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		}
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getSocket() {
		return socket;
	}

	public void setSocket(int socket) {
		this.socket = socket;
	}
}
