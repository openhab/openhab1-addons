/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * The whole set of data being read from the Libelium eHealth sensor kit.
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public class EHealthDatagram {
	
	private Map<EHealthSensorPropertyName, Number> rawData;	
	
	public EHealthDatagram(int airFlow, float temperature, float skinConductance, float skinResistance, float skinConductanceVoltage, int bpm, int oxygenSaturation, int bodyPosition, float ecg, int emg, int glucose) {
		this.rawData = new HashMap<EHealthSensorPropertyName, Number>();
		
		this.rawData.put(EHealthSensorPropertyName.AIR_FLOW, airFlow);
		this.rawData.put(EHealthSensorPropertyName.TEMPERATURE, temperature);

		this.rawData.put(EHealthSensorPropertyName.SKIN_CONDUCTANCE, skinConductance);
		this.rawData.put(EHealthSensorPropertyName.SKIN_RESISTANCE, skinResistance);
		this.rawData.put(EHealthSensorPropertyName.SKIN_CONDUCTANCE_VOLTAGE, skinConductanceVoltage);
		
		this.rawData.put(EHealthSensorPropertyName.BPM, bpm);
		this.rawData.put(EHealthSensorPropertyName.OXYGEN_SATURATION, oxygenSaturation);
		
		this.rawData.put(EHealthSensorPropertyName.BODY_POSITION, bodyPosition);
		
		this.rawData.put(EHealthSensorPropertyName.ECG, ecg);
		this.rawData.put(EHealthSensorPropertyName.EMG, emg);
		
		this.rawData.put(EHealthSensorPropertyName.GLUCOSE, glucose);
	}
	
	
	public void addRawDate(EHealthSensorPropertyName propertyName, Number value) {
		this.rawData.put(propertyName, value);
	}
	
	public Map<EHealthSensorPropertyName, Number> getRawData() {
		return rawData;
	}
	
	public int getAirFlow() {
		return (Integer) this.rawData.get(EHealthSensorPropertyName.AIR_FLOW);
	}

	public float getTemperature() {
		return (Float) this.rawData.get(EHealthSensorPropertyName.TEMPERATURE);
	}

	public float getSkinConductance() {
		return (Float) this.rawData.get(EHealthSensorPropertyName.SKIN_CONDUCTANCE);
	}

	public float getSkinResistance() {
		return (Float) this.rawData.get(EHealthSensorPropertyName.SKIN_RESISTANCE);
	}

	public float getSkinConductanceVoltage() {
		return (Float) this.rawData.get(EHealthSensorPropertyName.SKIN_CONDUCTANCE_VOLTAGE);
	}

	public int getBpm() {
		return (Integer) this.rawData.get(EHealthSensorPropertyName.BPM);
	}

	public int getOxygenSaturation() {
		return (Integer) this.rawData.get(EHealthSensorPropertyName.OXYGEN_SATURATION);
	}

	public int getBodyPosition() {
		return (Integer) this.rawData.get(EHealthSensorPropertyName.BODY_POSITION);
	}

	public float getEcg() {
		return (Float) this.rawData.get(EHealthSensorPropertyName.ECG);
	}

	public int getEmg() {
		return (Integer) this.rawData.get(EHealthSensorPropertyName.EMG);
	}
	
	public int getGlucose() {
		return (Integer) this.rawData.get(EHealthSensorPropertyName.GLUCOSE);
	}
	
	
	/**
	 * Indicates if the value identified by {@code propertyName} has changed in comparison to
	 * the {@code other} datagram or not.
	 * 
	 * @param other the other EHealthDatagram to compare the specific value
	 * @param propertyName the name of the property to compare the value of
	 * 
	 * @return {@code true} if either {@code other} or {@code propertyName} is Null or the
	 * value is not equal and {@code false} in all other cases.
	 */
	public boolean hasChanged(EHealthDatagram other, EHealthSensorPropertyName propertyName) {
		if (other == null || propertyName == null) {
			return true;
		}
		
		Number thisValue = getRawData().get(propertyName);
		Number thatValue = other.getRawData().get(propertyName);
		
		return !thisValue.equals(thatValue);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EHealthDatagram [");
		for (Entry<EHealthSensorPropertyName, Number> entry : rawData.entrySet()) {
			sb.append(entry.getKey().getPropertyName()).append("=").append(entry.getValue()).append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
	
	
}
