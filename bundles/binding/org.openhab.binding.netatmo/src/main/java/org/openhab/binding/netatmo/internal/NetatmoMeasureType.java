package org.openhab.binding.netatmo.internal;
import org.apache.commons.lang.StringUtils;
public enum NetatmoMeasureType {
	CO2("co2"),
	TEMPERATURE("temperature"),
	HUMIDITY("humidity"),
	NOISE("noise"),
	PRESSURE("pressure"),
	WIFISTATUS("wifistatus"),
	ALTITUDE("altitude"),
	LATITUDE("latitude"),
	LONGITUDE("longitude"),
	RFSTATUS("rfstatus"),
	BATTERYVP("batteryvp");
	
	String measure;

	private NetatmoMeasureType(String measure) {
		this.measure = measure;
	}

	public String getMeasure() {
		return measure;
	}
	
	public static NetatmoMeasureType fromString(String measure) {
		if (!StringUtils.isEmpty(measure)) {
			for (NetatmoMeasureType measureType : NetatmoMeasureType.values()) {
				if (measureType.toString().equalsIgnoreCase(measure)) {
					return measureType;
				}
			}
		}
		throw new IllegalArgumentException("Invalid measure: " + measure);
	}
}