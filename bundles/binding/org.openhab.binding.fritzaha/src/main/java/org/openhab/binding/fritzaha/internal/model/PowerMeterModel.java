package org.openhab.binding.fritzaha.internal.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 * @author robert
 *
 */
@XmlRootElement(name = "powermeter")
@XmlType(propOrder = { "power", "energy"})
public class PowerMeterModel {
	private BigDecimal power;
	private BigDecimal energy;

	public BigDecimal getPower() {
		return power;
	}
	public void setPower(BigDecimal power) {
		this.power = power;
	}
	public BigDecimal getEnergy() {
		return energy;
	}
	public void setEnergy(BigDecimal energy) {
		this.energy = energy;
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder("powermeter");
		out.append("[power=").append(this.getPower()).append(',');
		out.append("energy=").append(this.getEnergy()).append(']');
		return out.toString();
	}
}
