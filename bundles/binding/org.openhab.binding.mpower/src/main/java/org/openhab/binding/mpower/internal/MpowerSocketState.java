package org.openhab.binding.mpower.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Ubiquiti mPower strip binding. This transforms the raw mPower data into a
 * nice object
 * 
 * @author magcode
 */

public class MpowerSocketState {
	private int voltage;
	private long energy;
	private double power;
	private boolean on;
	private int socket;
	private String address;
	private static final Logger logger = LoggerFactory
			.getLogger(MpowerSocketState.class);

	public MpowerSocketState(String voltage, String power, String energy,
			String relayState, int socket, String address, double factor) {
		try {
			Double voltageAsDouble = Double.parseDouble(voltage);
			this.voltage = voltageAsDouble.intValue();
			Double powerRounded = Double.parseDouble(power);
			powerRounded = powerRounded * 10;
			powerRounded = (double) Math.round(powerRounded);
			powerRounded = powerRounded / 10;
			this.power = powerRounded;
			Double eneryAsDouble = Double.parseDouble(energy);
			eneryAsDouble = eneryAsDouble * factor;
			this.energy = eneryAsDouble.longValue();
			this.on = "1".equals(relayState) ? true : false;
		} catch (NumberFormatException nfe) {
			logger.error("Could not parse mPower response", nfe);
			logger.error("voltage", voltage);
			logger.error("power", power);
			logger.error("energy", energy);
			logger.error("relayState", relayState);
			logger.error("socket", socket);
			logger.error("address", address);
		}

		this.socket = socket;
		this.address = address;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
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
			// make volt a bit fuzzy. we don't care about changes by 5%
			int lower = givenState.getVoltage() - givenState.getVoltage() / 20;
			int higher = givenState.getVoltage() + givenState.getVoltage() / 20;
			boolean sameVolt = true;
			if (getVoltage() < lower || getVoltage() > higher) {
				sameVolt = false;
			}
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
