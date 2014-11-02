package org.openhab.binding.fritzaha.internal.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * @author robert
 *
 */
@XmlRootElement(name = "temperature")
@XmlType(propOrder = { "celsius", "offset"})
public class TemperatureModel {
	private BigDecimal celsius;
	private BigDecimal offset;
	
	public BigDecimal getCelsius() {
		return celsius;
	}
	public void setCelsius(BigDecimal celsius) {
		this.celsius = celsius;
	}
	public BigDecimal getOffset() {
		return offset;
	}
	public void setOffset(BigDecimal offset) {
		this.offset = offset;
	}
	public String toString() {
		StringBuilder out = new StringBuilder("temperature");
		out.append("[celsius=").append(this.getCelsius()).append(',');
		out.append("offset=").append(this.getOffset()).append(']');
		return out.toString();
	}
}
