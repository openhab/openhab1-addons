/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.koubachi.internal.api;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a plant in the Koubachi domain.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plant extends KoubachiResource {
	
	String name;
	String location;
	
	Date lastFertilizerAt;
	Date nextFertilizerAt;
	Date lastMistAt;
	Date nextMistAt;
	Date lastWaterAt;
	Date nextWaterAt;
	
	String vdmWaterInstruction;
	BigDecimal vdmWaterLevel;
	String vdmMistInstruction;
	BigDecimal vdmMistLevel;
	String vdmFertilizerInstruction;
	BigDecimal vdmFertilizerLevel;
	String vdmTemperatureHint;
	String vdmTemperatureInstruction;
	BigDecimal vdmTemperatureLevel;
	String vdmLightHint;
	String vdmLightInstruction;
	BigDecimal vdmLightLevel;
	
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}
	
	
	@JsonProperty("last_fertilizer_at")
	public Date getLastFertilizerAt() {
		return lastFertilizerAt;
	}
	
	@JsonProperty("next_fertilizer_at")
	public Date getNextFertilizerAt() {
		return nextFertilizerAt;
	}
	
	@JsonProperty("last_mist_at")
	public Date getLastMistAt() {
		return lastMistAt;
	}
	
	@JsonProperty("next_mist_at")
	public Date getNextMistAt() {
		return nextMistAt;
	}
	
	@JsonProperty("last_water_at")
	public Date getLastWaterAt() {
		return lastWaterAt;
	}
	
	@JsonProperty("next_water_at")
	public Date getNextWaterAt() {
		return nextWaterAt;
	}
	
	
	@JsonProperty("vdm_water_instruction")
	public String getVdmWaterInstruction() {
		return vdmWaterInstruction;
	}
	
	@JsonProperty("vdm_water_level")
	public BigDecimal getVdmWaterLevel() {
		return vdmWaterLevel;
	}
	
	@JsonProperty("vdm_mist_instruction")
	public String getVdmMistInstruction() {
		return vdmMistInstruction;
	}

	@JsonProperty("vdm_mist_level")
	public BigDecimal getVdmMistLevel() {
		return vdmMistLevel;
	}

	@JsonProperty("vdm_fertilizer_instruction")
	public String getVdmFertilizerInstruction() {
		return vdmFertilizerInstruction;
	}
	
	@JsonProperty("vdm_fertilizer_level")
	public BigDecimal getVdmFertilizerLevel() {
		return vdmFertilizerLevel;
	}
	
	@JsonProperty("vdm_temperature_hint")
	public String getVdmTemperatureHint() {
		return vdmTemperatureHint;
	}
	
	@JsonProperty("vdm_temperature_instruction")
	public String getVdmTemperatureInstruction() {
		return vdmTemperatureInstruction;
	}

	@JsonProperty("vdm_temperature_level")
	public BigDecimal getVdmTemperatureLevel() {
		return vdmTemperatureLevel;
	}
	
	@JsonProperty("vdm_light_hint")
	public String getVdmLightHint() {
		return vdmLightHint;
	}
	
	@JsonProperty("vdm_light_instruction")
	public String getVdmLightInstruction() {
		return vdmLightInstruction;
	}
	
	@JsonProperty("vdm_light_level")
	public BigDecimal getVdmLightLevel() {
		return vdmLightLevel;
	}

	@Override
	public String toString() {
		return "Plant [name=" + name + ", location=" + location
				+ ", lastFertilizerAt=" + lastFertilizerAt
				+ ", nextFertilizerAt=" + nextFertilizerAt + ", lastMistAt="
				+ lastMistAt + ", nextMistAt=" + nextMistAt + ", lastWaterAt="
				+ lastWaterAt + ", nextWaterAt=" + nextWaterAt
				+ ", vdmWaterInstruction=" + vdmWaterInstruction
				+ ", vdmWaterLevel=" + vdmWaterLevel + ", vdmMistInstruction="
				+ vdmMistInstruction + ", vdmMistLevel=" + vdmMistLevel
				+ ", vdmFertilizerInstruction=" + vdmFertilizerInstruction
				+ ", vdmFertilizerLevel=" + vdmFertilizerLevel
				+ ", vdmTemperatureHint=" + vdmTemperatureHint
				+ ", vdmTemperatureInstruction=" + vdmTemperatureInstruction
				+ ", vdmTemperatureLevel=" + vdmTemperatureLevel
				+ ", vdmLightHint=" + vdmLightHint + ", vdmLightInstruction="
				+ vdmLightInstruction + ", vdmLightLevel=" + vdmLightLevel
				+ "]";
	}
	
}
