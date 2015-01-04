package org.openhab.binding.mpower.internal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * Ubiquiti mPower strip binding This transforms the JSON data into a nice
 * object
 * 
 * @author magcode
 */

public class MpowerSocketState {
	private double voltage;
	private long energy;
	private double power;
	private boolean on;
	private int socket;
	private String address;

	public MpowerSocketState(String json, String address) {
		setAddress(address);
		JSONParser parser = new JSONParser();

		try {
			JSONObject root = (JSONObject) parser.parse(json);
			JSONArray sensors = (JSONArray) root.get("sensors");
			JSONObject oneSensor = (JSONObject) sensors.get(0);
			Object ob = oneSensor.get("voltage");
			if (ob instanceof Double) {
				Double val = (Double) ob;
				setVoltage(val.intValue());
			}
			ob = oneSensor.get("power");
			if (ob instanceof Double) {
				Double val = (double) Math.round((Double) ob * 10) / 10;

				setPower(val);
			}
			ob = oneSensor.get("port");
			if (ob instanceof Long) {
				Long sock = (Long) ob;
				setSocket(sock.intValue());
			}
			ob = oneSensor.get("energy");
			if (ob instanceof Double) {
				Double val = (Double) ob;
				setEnergy(val.longValue());
			}

			ob = oneSensor.get("output");
			if (ob instanceof Long) {
				Boolean on = (Long) ob == 1;
				setOn(on);
			}
		} catch (ParseException pe) {
			// TODO
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

	@Override
	public boolean equals(Object object) {
		if (object instanceof MpowerSocketState) {
			MpowerSocketState givenState = (MpowerSocketState) object;
			boolean sameVolt = givenState.getVoltage() == getVoltage();
			boolean samePower = givenState.getPower() == getPower();
			boolean sameEnergy = givenState.getEnergy() == getEnergy();
			boolean sameONOFFstate = givenState.isOn() == isOn();
			if (sameVolt && samePower && sameONOFFstate && sameEnergy) {
				return true;
			}
		}
		return false;
	}

	public long getEnergy() {
		return energy;
	}

	public void setEnergy(long energy) {
		this.energy = energy;
	}
}
