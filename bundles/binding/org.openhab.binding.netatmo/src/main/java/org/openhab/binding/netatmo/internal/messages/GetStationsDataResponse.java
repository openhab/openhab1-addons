/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Java Bean to represent a JSON response to a <code>getstationsdata</code> API
 * method call.
 * <p>
 * Sample response:
 *
 * <pre>
 * {
 *   "body": {
 *     "devices": [
 *       {
 *         "_id": "70:ee:50:00:00:14",
 *         "co2_calibrating": false,
 *         "firmware": 91,
 *         "last_status_store": 1441872001,
 *         "last_upgrade": 1440507643,
 *         "module_name": "ind",
 *         "modules": [
 *           {
 *             "_id": "05:00:00:00:00:14",
 *             "module_name": "Pluie",
 *             "type": "NAModule3",
 *             "firmware": 91,
 *             "last_message": 1437990885,
 *             "last_seen": 1437990885,
 *             "rf_status": 40,
 *             "battery_vp": 4103,
 *             "dashboard_data": {
 *               "time_utc": 1437990885,
 *               "Rain": 0.101
 *             },
 *             "data_type": [
 *               "Rain"
 *             ]
 *           },
 *         ],
 *         "place": {
 *           "altitude": 30.478512648583,
 *           "city": "Saint-Denis",
 *           "country": "FR",
 *           "improveLocProposed": true,
 *           "location": [
 *             2.384033203125,
 *             48.936934954094
 *           ],
 *           "timezone": "Europe/Paris"
 *         },
 *         "station_name": "Station",
 *         "type": "NAMain",
 *         "wifi_status": 109,
 *         "dashboard_data": {
 *           "AbsolutePressure": 929.4,
 *           "time_utc": 1441872001,
 *           "Noise": 110,
 *           "Temperature": 3.2,
 *           "Humidity": 60,
 *           "Pressure": 929.4,
 *           "CO2": 4852,
 *           "date_max_temp": 1441850941,
 *           "date_min_temp": 1441862941,
 *           "min_temp": -39.5,
 *           "max_temp": 79.5
 *         },
 *         "data_type": [
 *           "Temperature",
 *           "CO2",
 *           "Humidity",
 *           "Noise",
 *           "Pressure"
 *         ]
 *       }
 *     ],
 *     "user": {
 *       "mail": "toto@netatmo.com",
 *       "administrative": {
 *         "reg_locale": "en-US",
 *         "lang": "en-US",
 *         "unit": 1,
 *         "windunit": 1,
 *         "pressureunit": 1,
 *         "feel_like_algo": 1
 *       }
 *     }
 *   },
 *   "status": "ok",
 *   "time_exec": 0.36015892028809,
 *   "time_server": 1441872018
 * }
 * </pre>
 *
 * @author Rob Nielsen
 * @since 1.8.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetStationsDataResponse extends AbstractResponse {

	/**
	 * <code>type</code> constant of the main indoor station.
	 */
	@SuppressWarnings("unused")
	private static final String TYPE_MAIN = "NAMain";

	/**
	 * <code>type</code> constant of the outdoor module
	 */
	private static final String TYPE_MODULE_1 = "NAModule1";
	
	/**
	 * <code>type</code> constant of the wind gauge module
	 */
	private static final String TYPE_MODULE_2 = "NAModule2";

	/**
	 * <code>type</code> constant of the rain gauge module
	 */
	private static final String TYPE_MODULE_3 = "NAModule3";

	/**
	 * <code>type</code> constant of the additional indoor module
	 */
	@SuppressWarnings("unused")
	private static final String TYPE_MODULE_4 = "NAModule4";

	/**
	 * <code>type</code> constant of the thermostat relay/plug
	 */
	@SuppressWarnings("unused")
	private static final String TYPE_PLUG = "NAPlug";

	/**
	 * <code>type</code> constant of the thermostat module
	 */
	@SuppressWarnings("unused")
	private static final String TYPE_THERM_1 = "NATherm1";

	/**
	 * <code>wifi_status</code> threshold constant: bad signal
	 */
	private static final int WIFI_STATUS_THRESHOLD_0 = 86;

	/**
	 * <code>wifi_status</code> threshold constant: middle quality signal
	 */
	private static final int WIFI_STATUS_THRESHOLD_1 = 71;

	/**
	 * <code>wifi_status</code> threshold constant: good signal
	 */
	private static final int WIFI_STATUS_THRESHOLD_2 = 56;

	/**
	 * <code>rf_status</code> threshold constant: low signal
	 */
	private static final int RF_STATUS_THRESHOLD_0 = 90;

	/**
	 * <code>rf_status</code> threshold constant: medium signal
	 */
	private static final int RF_STATUS_THRESHOLD_1 = 80;

	/**
	 * <code>rf_status</code> threshold constant: high signal
	 */
	private static final int RF_STATUS_THRESHOLD_2 = 70;

	/**
	 * <code>rf_status</code> threshold constant: full signal
	 */
	private static final int RF_STATUS_THRESHOLD_3 = 60;

	/**
	 * <code>battery_vp</code> threshold for type NAModule1: full
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_0 = 5500;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule1: high
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_1 = 5000;
	/**
	 * <code>battery_vp</code> threshold for type NAModule1: medium
	 */
	
	private static final int BATTERY_MODULE_1_THRESHOLD_2 = 4500;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule1: low, otherwise
	 * verylow
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_3 = 4000;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule2: full
	 */
	private static final int BATTERY_MODULE_2_THRESHOLD_0 = 5590;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule2: high
	 */
	private static final int BATTERY_MODULE_2_THRESHOLD_1 = 5180;
	/**
	 * <code>battery_vp</code> threshold for type NAModule2: medium
	 */
	
	private static final int BATTERY_MODULE_2_THRESHOLD_2 = 4770;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule2: low, otherwise
	 * verylow
	 */
	private static final int BATTERY_MODULE_2_THRESHOLD_3 = 4360;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule3: full
	 */
	private static final int BATTERY_MODULE_3_THRESHOLD_0 = 5500;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule3: high
	 */
	private static final int BATTERY_MODULE_3_THRESHOLD_1 = 5000;
	/**
	 * <code>battery_vp</code> threshold for type NAModule3: medium
	 */
	
	private static final int BATTERY_MODULE_3_THRESHOLD_2 = 4500;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule3: low, otherwise
	 * verylow
	 */
	private static final int BATTERY_MODULE_3_THRESHOLD_3 = 4000;

	/**
	 * <code>battery_vp</code> threshold for type NAModule4: full
	 */
	private static final int BATTERY_MODULE_4_THRESHOLD_0 = 5640;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule4: high
	 */
	private static final int BATTERY_MODULE_4_THRESHOLD_1 = 5280;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule4: medium
	 */
	private static final int BATTERY_MODULE_4_THRESHOLD_2 = 4920;
	
	/**
	 * <code>battery_vp</code> threshold for type NAModule4: low, otherwise
	 * verylow
	 */
	private static final int BATTERY_MODULE_4_THRESHOLD_3 = 4560;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Body extends AbstractMessagePart {
		private List<Device> devices;

		@JsonProperty("devices")
		public List<Device> getDevices() {
			return this.devices;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("devices", this.devices);

			return builder.toString();
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Device extends AbstractMessagePart {
		private String id;
		private Boolean co2Calibrating;
		private Integer firmware;
		private Date lastStatusStore;
		private Date lastUpgrade;
		private String moduleName;
		private List<Module> modules;
		private Place place;
		private String stationName;
		private String type;
		private Integer wifiStatus;
		private List<String> measurements;

		/**
		 * "_id": "70:ee:50:00:00:14"
		 */
		@JsonProperty("_id")
		public String getId() {
			return this.id;
		}
		
		/**
		 * "co2_calibrating": false
		 */
		@JsonProperty("co2_calibrating")
		public Boolean isCo2Calibrating() {
			return this.co2Calibrating;
		}

		/**
		 * "firmware": 91
		 */
		@JsonProperty("firmware")
		public Integer getFirmware() {
			return this.firmware;
		}
		
		/**
		 * "last_status_store": 1441872001
		 */
		@JsonProperty("last_status_store")
		public Date getLastStatusStore() {
			return this.lastStatusStore;
		}
		
		/**
		 * "last_upgrade": 1440507643
		 */
		@JsonProperty("last_upgrade")
		public Date getLastUpgrade() {
			return this.lastUpgrade;
		}
		
		/**
		 * "module_name": "ind"
		 */
		@JsonProperty("module_name")
		public String getModuleName() {
			return this.moduleName;
		}
		
		@JsonProperty("modules")
		public List<Module> getModules() {
			return this.modules;
		}
		
		/**
		 * <pre>
		 * "place": {
		 *   "altitude": 30.478512648583,
		 *   "city": "Saint-Denis",
		 *   "country": "FR",
		 *   "improveLocProposed": true,
		 *   "location": [
		 *     2.384033203125,
		 *     48.936934954094
		 *   ],
		 *   "timezone": "Europe/Paris"
		 * }
		 * </pre>
		 */
		@JsonProperty("place")
		public Place getPlace() {
			return this.place;
		}
		
		/**
		 * "station_name": "Station"
		 */
		@JsonProperty("station_name")
		public String getStationName() {
			return this.stationName;
		}
		
		/**
		 * "type": "NAMain"
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}
		
		/**
		 * "wifi_status"
		 */
		@JsonProperty("wifi_status")
		public Integer getWifiStatus() {
			return this.wifiStatus;
		}
		
		/**
		 * "data_type": [
		 *   "Temperature",
		 *   "CO2",
		 *   "Humidity",
		 *   "Noise",
		 *   "Pressure"
		 * ]
		 */
		@JsonProperty("data_type")
		public List<String> getMeasurements() {
			return this.measurements;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("id", this.id);
			builder.append("co2Calibrating", this.co2Calibrating);
			builder.append("firmware", this.firmware);
			builder.append("lastStatusStore", this.lastStatusStore);
			builder.append("lastUpgrade", this.lastUpgrade);
			builder.append("moduleName", this.moduleName);
			builder.append("modules", this.modules);
			builder.append("place", this.place);
			builder.append("stationName", this.stationName);
			builder.append("type", this.type);
			builder.append("wifiStatus", this.wifiStatus);
			builder.append("measurements", this.measurements);

			return builder.toString();
		}

		public int getWifiLevel() {
			int level = this.wifiStatus.intValue();
			int result = 3;
			if (level < WIFI_STATUS_THRESHOLD_2)
				result = 2;
			else if (level < WIFI_STATUS_THRESHOLD_1)
				result = 1;
			else if (level < WIFI_STATUS_THRESHOLD_0)
				result = 0;

			return result;
		}

		public Double getAltitude() {
			return this.place.altitude;
		}

		public Double getLatitude() {
			return this.place.location.get(1);
		}

		public Double getLongitude() {
			return this.place.location.get(0);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Module extends AbstractMessagePart {
		private String id;
		private String moduleName;
		private String type;
		private Integer firmware;
		private Date lastMessage;
		private Date lastSeen;
		private Integer rfStatus;
		private Integer batteryVp;
		private List<String> measurements;
		
		/**
		 * "_id": "02:00:00:00:00:14"
		 */
		@JsonProperty("_id")
		public String getId() {
			return this.id;
		}
		
		/**
		 * "module_name": "out"
		 */
		@JsonProperty("module_name")
		public String getModuleName() {
			return this.moduleName;
		}
		
		/**
		 * "type": "NAModule1"
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}
		
		/**
		 * "firmware": 91
		 */
		@JsonProperty("firmware")
		public Integer getFirmware() {
			return this.firmware;
		}
		
		/**
		 * "last_message": 1441872001
		 */
		@JsonProperty("last_message")
		public Date getLastMessage() {
			return this.lastMessage;
		}
		
		/**
		 * "last_seen": 1441868401
		 */
		@JsonProperty("last_seen")
		public Date getLastSeen() {
			return this.lastSeen;
		}
		
		/**
		 * "rf_status": 143
		 */
		@JsonProperty("rf_status")
		public Integer getRfStatus() {
			return this.rfStatus;
		}
		
		/**
		 * "battery_vp": 31188
		 */
		@JsonProperty("battery_vp")
		public Integer getBatteryVp() {
			return this.batteryVp;
		}
		
		/**
		 * "data_type": [
		 *   "Temperature",
		 *   "CO2",
		 *   "Humidity",
		 *   "Noise",
		 *   "Pressure"
		 * ]
		 */
		@JsonProperty("data_type")
		public List<String> getMeasurements() {
			return this.measurements;
		}

		public int getRfLevel() {
			int level = this.rfStatus.intValue();
			int result = 4; // not found

			if (level < RF_STATUS_THRESHOLD_3)
				result = 3;
			else if (level < RF_STATUS_THRESHOLD_2)
				result = 2;
			else if (level < RF_STATUS_THRESHOLD_1)
				result = 1;
			else if (level < RF_STATUS_THRESHOLD_0)
				result = 0;

			return result;
		}

		public Double getBatteryLevel() {
			int value;
			int minima;
			int spread;
			int threshold0;	
			int threshold1;
			int threshold2;
			int threshold3;
			if (this.type.equalsIgnoreCase(TYPE_MODULE_1)) {
				threshold0 = BATTERY_MODULE_1_THRESHOLD_0;	
				threshold1 = BATTERY_MODULE_1_THRESHOLD_1;
				threshold2 = BATTERY_MODULE_1_THRESHOLD_2;
				threshold3 = BATTERY_MODULE_1_THRESHOLD_3;
			} else if (this.type.equalsIgnoreCase(TYPE_MODULE_2)) {
				threshold0 = BATTERY_MODULE_2_THRESHOLD_0;	
				threshold1 = BATTERY_MODULE_2_THRESHOLD_1;
				threshold2 = BATTERY_MODULE_2_THRESHOLD_2;
				threshold3 = BATTERY_MODULE_2_THRESHOLD_3;
			} else if (this.type.equalsIgnoreCase(TYPE_MODULE_3)) {
				threshold0 = BATTERY_MODULE_3_THRESHOLD_0;	
				threshold1 = BATTERY_MODULE_3_THRESHOLD_1;
				threshold2 = BATTERY_MODULE_3_THRESHOLD_2;
				threshold3 = BATTERY_MODULE_3_THRESHOLD_3;
			} else {
				threshold0 = BATTERY_MODULE_4_THRESHOLD_0;	
				threshold1 = BATTERY_MODULE_4_THRESHOLD_1;
				threshold2 = BATTERY_MODULE_4_THRESHOLD_2;
				threshold3 = BATTERY_MODULE_4_THRESHOLD_3;
			}
			
			value = Math.min(this.batteryVp, threshold0);
			minima = threshold3 + threshold2 - threshold1;
			spread = threshold0 - minima;
			
			double percent = 100 * (value - minima) / spread;
			return new Double(percent);
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("id", this.id);
			builder.append("moduleName", this.moduleName);
			builder.append("type", this.type);
			builder.append("firmware", this.firmware);
			builder.append("lastMessage", this.lastMessage);
			builder.append("lastSeen", this.lastSeen);
			builder.append("rfStatus", this.rfStatus);
			builder.append("batteryVp", this.batteryVp);
			builder.append("dataTypes", this.measurements);

			return builder.toString();
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Place extends AbstractMessagePart {
		private Double altitude;
		private String city;
		private String country;
		private Boolean improveLocProposed;
		private List<Double> location;
		private String timezone;
		
		/**
		 * "altitude": 30.478512648583
		 */
		@JsonProperty("altitude")
		public Double getAltitude() {
			return this.altitude;
		}
		
		/**
		 * "city": "Saint-Denis"
		 */
		@JsonProperty("city")
		public String getCity() {
			return this.city;
		}
		
		/**
		 * "country": "FR"
		 */
		@JsonProperty("country")
		public String getCountry() {
			return this.country;
		}
		
		/**
		 * "improveLocProposed": true
		 */
		@JsonProperty("improveLocProposed")
		public Boolean isImproveLocProposed() {
			return this.improveLocProposed;
		}
		
		/**
		 * <pre>
		 * "location": [
		 *   2.384033203125,
         *   48.936934954094
		 * ]
		 * </pre>
		 */
		@JsonProperty("location")
		public List<Double> getlocation() {
			return this.location;
		}

		/**
		 * "timezone": "Europe/Paris"
		 */
		@JsonProperty("timezone")
		public String getTimezone() {
			return this.timezone;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("altitude", this.altitude);
			builder.append("city", this.city);
			builder.append("country", this.country);
			builder.append("improveLocProposed", this.improveLocProposed);
			builder.append("location", this.location);
			builder.append("timezone", this.timezone);

			return builder.toString();
		}
	}

	private String status;

	private Body body;

	private Double executionTime;

	@JsonProperty("body")
	public Body getBody() {
		return this.body;
	}

	public List<Device> getDevices() {
		return this.body.devices;
	}

	/**
	 * "time_exec": 0.019799947738647
	 */
	@JsonProperty("time_exec")
	public Double getExecutionTime() {
		return this.executionTime;
	}

	@JsonProperty("status")
	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("status", this.status);
		builder.append("body", this.body);
		builder.append("executionTime", this.executionTime);

		return builder.toString();
	}
}
