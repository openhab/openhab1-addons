/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
