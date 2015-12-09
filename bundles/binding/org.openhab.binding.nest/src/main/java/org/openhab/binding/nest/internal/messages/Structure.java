/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * The Structure Java Bean represents a Nest structure.
 * 
 * @see <a href="https://developer.nest.com/documentation/api-reference">API Reference</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Structure extends AbstractMessagePart implements DataModelElement {

	/**
	 * Describes the Structure state; see the Away Guide for more information.
	 * 
	 * @see <a href="https://developer.nest.com/documentation/cloud/structure-guide">Structure state</a>
	 * @see <a href="https://developer.nest.com/documentation/cloud/away-guide">Away Guide</a>
	 * @see <a href="https://developer.nest.com/documentation/cloud/api-overview#away">API Overview</a>
	 */
	public static enum AwayState {
		HOME("home"), AWAY("away"), AUTO_AWAY("auto-away"), UNKNOWN("unknown");

		private final String state;

		private AwayState(String state) {
			this.state = state;
		}

		@JsonValue
		public String value() {
			return state;
		}

		@JsonCreator
		public static AwayState forValue(String v) {
			for (AwayState s : AwayState.values()) {
				if (s.state.equals(v)) {
					return s;
				}
			}
			throw new IllegalArgumentException("Invalid state: " + v);
		}

		@Override
		public String toString() {
			return this.state;
		}
	}

	public static class ETA {
		String trip_id; // Unique identifier for this ETA instance
		Date estimated_arrival_window_begin; // Beginning time of the estimated arrival window
		Date estimated_arrival_window_end; // End time of the estimated arrival window

		/**
		 * @return the trip_id
		 */
		@JsonProperty("trip_id")
		public String getTrip_id() {
			return this.trip_id;
		}

		/**
		 * @param trip_id
		 *            the trip_id to set
		 */
		@JsonProperty("trip_id")
		public void setTrip_id(String trip_id) {
			this.trip_id = trip_id;
		}

		/**
		 * @return the estimated_arrival_window_begin
		 */
		@JsonProperty("estimated_arrival_window_begin")
		public Date getEstimated_arrival_window_begin() {
			return this.estimated_arrival_window_begin;
		}

		/**
		 * @param estimated_arrival_window_begin
		 *            the estimated_arrival_window_begin to set
		 */
		@JsonProperty("estimated_arrival_window_begin")
		public void setEstimated_arrival_window_begin(Date estimated_arrival_window_begin) {
			this.estimated_arrival_window_begin = estimated_arrival_window_begin;
		}

		/**
		 * @return the estimated_arrival_window_end
		 */
		@JsonProperty("estimated_arrival_window_end")
		public Date getEstimated_arrival_window_end() {
			return this.estimated_arrival_window_end;
		}

		/**
		 * @param estimated_arrival_window_end
		 *            the estimated_arrival_window_end to set
		 */
		@JsonProperty("estimated_arrival_window_end")
		public void setEstimated_arrival_window_end(Date estimated_arrival_window_end) {
			this.estimated_arrival_window_end = estimated_arrival_window_end;
		}
	}

	private String structure_id;
	@JsonProperty("thermostats")
	private List<String> thermostat_id_list;
	private Map<String, Thermostat> thermostats_by_name;
	@JsonProperty("smoke_co_alarms")
	private List<String> smoke_co_alarm_id_list;
	private Map<String, SmokeCOAlarm> smoke_co_alarms_by_name;
	@JsonProperty("cameras")
	private List<String> camera_id_list;
	private Map<String, Camera> cameras_by_name;
	private AwayState away;
	private String name;
	private String country_code;
	private String postal_code;
	private Date peak_period_start_time;
	private Date peak_period_end_time;
	private String time_zone;
	private ETA eta;

	public Structure(@JsonProperty("structure_id") String structure_id) {
		this.structure_id = structure_id;
	}

	/**
	 * @return the structure_id
	 */
	@JsonProperty("structure_id")
	public String getStructure_id() {
		return this.structure_id;
	}

	/**
	 * @return the thermostats
	 */
	@JsonProperty("thermostats")
	public List<String> getThermostat_id_list() {
		return this.thermostat_id_list;
	}

	public Map<String, Thermostat> getThermostats() {
		return this.thermostats_by_name;
	}

	public void setThermostats_by_name(Map<String, Thermostat> thermostats_by_name) {
		this.thermostats_by_name = thermostats_by_name;
	}

	/**
	 * @return the smoke_co_alarms
	 */
	@JsonProperty("smoke_co_alarms")
	public List<String> getSmoke_co_alarm_id_list() {
		return this.smoke_co_alarm_id_list;
	}

	public Map<String, SmokeCOAlarm> getSmoke_co_alarms() {
		return this.smoke_co_alarms_by_name;
	}

	public void setSmoke_co_alarms_by_name(Map<String, SmokeCOAlarm> smoke_co_alarms_by_name) {
		this.smoke_co_alarms_by_name = smoke_co_alarms_by_name;
	}

	/**
	 * @return the cameras
	 */
	@JsonProperty("cameras")
	public List<String> getCamera_id_list() {
		return this.camera_id_list;
	}

	public Map<String, Camera> getCameras() {
		return this.cameras_by_name;
	}

	public void setCameras_by_name(Map<String, Camera> cameras_by_name) {
		this.cameras_by_name = cameras_by_name;
	}

	/**
	 * @return the away state
	 */
	@JsonProperty("away")
	public AwayState getAway() {
		return this.away;
	}

	/**
	 * @param away
	 *            the away to set
	 */
	@JsonProperty("away")
	public void setAway(AwayState away) {
		this.away = away;
	}

	/**
	 * @return the name
	 */
	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	/**
	 * @return the country_code
	 */
	@JsonProperty("country_code")
	public String getCountry_code() {
		return this.country_code;
	}

	/**
	 * @return the postal_code
	 */
	@JsonProperty("postal_code")
	public String getPostal_code() {
		return this.postal_code;
	}

	/**
	 * @return the peak_period_start_time
	 */
	@JsonProperty("peak_period_start_time")
	public Date getPeak_period_start_time() {
		return this.peak_period_start_time;
	}

	/**
	 * @return the peak_period_end_time
	 */
	@JsonProperty("peak_period_end_time")
	public Date getPeak_period_end_time() {
		return this.peak_period_end_time;
	}

	/**
	 * @return the time_zone
	 */
	@JsonProperty("time_zone")
	public String getTime_zone() {
		return this.time_zone;
	}

	/**
	 * @return the eta
	 */
	@JsonProperty("eta")
	public ETA getEta() {
		return this.eta;
	}

	/**
	 * Set the eta.
	 */
	@JsonProperty("eta")
	public void setEta(ETA eta) {
		this.eta = eta;
	}

	/**
	 * This method creates maps to device objects, using the list of device IDs that were unmarshalled from JSON.
	 */
	@Override
	public void sync(DataModel dataModel) {
		// Build named-based maps from ID-based maps
		this.thermostats_by_name = new HashMap<String, Thermostat>();
		if (this.thermostat_id_list != null) {
			for (String id : this.thermostat_id_list) {
				Thermostat th = dataModel.getDevices().getThermostats_by_id().get(id);
				if (th != null) {
					this.thermostats_by_name.put(th.getName(), th);
				}
			}
		}
		this.smoke_co_alarms_by_name = new HashMap<String, SmokeCOAlarm>();
		if (this.smoke_co_alarm_id_list != null) {
			for (String id : this.smoke_co_alarm_id_list) {
				SmokeCOAlarm sm = dataModel.getDevices().getSmoke_co_alarms_by_id().get(id);
				if (sm != null) {
					this.smoke_co_alarms_by_name.put(sm.getName(), sm);
				}
			}
		}
		this.cameras_by_name = new HashMap<String, Camera>();
		if (this.camera_id_list != null) {
			for (String id : this.camera_id_list) {
				Camera cam = dataModel.getDevices().getCameras_by_id().get(id);
				if (cam != null) {
					this.cameras_by_name.put(cam.getName(), cam);
				}
			}
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("structure_id", this.structure_id);
		builder.append("thermostats", this.thermostat_id_list);
		builder.append("smoke_co_alarms", this.smoke_co_alarm_id_list);
		builder.append("cameras", this.camera_id_list);
		builder.append("away", this.away);
		builder.append("name", this.name);
		builder.append("country_code", this.country_code);
		builder.append("postal_code", this.postal_code);
		builder.append("peak_period_start_time", this.peak_period_start_time);
		builder.append("peak_period_end_time", this.peak_period_end_time);
		builder.append("time_zone", this.time_zone);
		builder.append("eta", this.eta);

		return builder.toString();
	}
}
