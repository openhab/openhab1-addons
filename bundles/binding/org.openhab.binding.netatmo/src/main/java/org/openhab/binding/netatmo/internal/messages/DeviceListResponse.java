/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
 * Java Bean to represent a JSON response to a <code>devicelist</code> API
 * method call.
 * <p>
 * Sample response:
 * 
 * <pre>
 * {
 *   "status":  "ok",
 *   "body": {
 *     "devices": [
 *       {
 *         "_id": "f0:4d:a2:ee:bc:49",
 *         "firmware":  1,
 *         "ip":  "127.0.0.1",
 *         "last_fw_update":  1347008293,
 *         "last_radio_store":  1325675936,
 *         "last_status_store":  1347624601,
 *         "last_upgrade":  1347455989,
 *                         "module_name":  "Inside",
 *         "modules":  [
 *           "02:00:00:ee:bc:49"
 *         ],
 *         "place":  {
 *           "altitude":  33,
 *            "country":  "FR",
 *            "location":  [
 *             2.35222,
 *              48.85661 
 *           ],
 *            "timezone":  "Europe/Paris",
 *            "trust_location":  true 
 *         },
 *          "public_ext_data":  true,
 *         "station_name":  "LA",
 *          "type":  "NAMain",
 *          "user_owner":  [
 *           "4fe091cdc56a6eb606000118"
 *         ]
 *       }
 *     ],
 *     "modules":  [
 *       {
 *         "_id":  "02:00:00:ee:bc:49",
 *          "firmware":  4,
 *         "main_device":  "f0:4d:a2:ee:bc:49",
 *          "module_name":  "Outside",
 *         "public_ext_data":  true,
 *         "rf_status":  161,
 *         "type":  "NAModule1"
 *       }
 *     ]
 *   },
 *   "time_exec":  0.019799947738647
 * }
 * </pre>
 * 
 * @author Andreas Brenk
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceListResponse extends AbstractResponse {

	// battery_vp, rf_status and wifi_status:
	// http://forum.netatmo.com/viewtopic.php?f=5&t=2290&sid=bb1c0f95abcf3198908829eb89adf1a1

	/**
	 * <code>type</code> constant of the main indoor station.
	 */
	private static final String TYPE_MAIN = "NAMain";

	/**
	 * <code>type</code> constant of the outdoor module
	 */
	private static final String TYPE_MODULE_1 = "NAModule1";

	/**
	 * <code>type</code> constant of the rain gauge module
	 */
	private static final String TYPE_MODULE_3 = "NAModule3";

	/**
	 * <code>type</code> constant of the additional indoor module
	 */
	private static final String TYPE_MODULE_4 = "NAModule4";

	/**
	 * <code>type</code> constant of the thermostat relay/plug
	 */
	private static final String TYPE_PLUG = "NAPlug";

	/**
	 * <code>type</code> constant of the thermostat module
	 */
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
	 * <code>battery_vp</code> threshold for type NAModule4: full
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_0 = 5500;
	/**
	 * <code>battery_vp</code> threshold for type NAModule4: high
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_1 = 5000;
	/**
	 * <code>battery_vp</code> threshold for type NAModule4: medium
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_2 = 4500;
	/**
	 * <code>battery_vp</code> threshold for type NAModule4: low, otherwise
	 * verylow
	 */
	private static final int BATTERY_MODULE_1_THRESHOLD_3 = 4000;

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

		private List<Module> modules;

		@JsonProperty("devices")
		public List<Device> getDevices() {
			return this.devices;
		}

		@JsonProperty("modules")
		public List<Module> getModules() {
			return this.modules;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("devices", this.devices);
			builder.append("modules", this.modules);

			return builder.toString();
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Device extends AbstractMessagePart {

		private String id;
		private Integer firmware;
		private String ip;
		private Date lastFirmwareUpdate;
		private Date lastRadioStore;
		private Date lastStatusStore;
		private Date lastUpgrade;
		private String moduleName;
		private List<String> modules;
		private Place place;
		private Boolean publicData;
		private String stationName;
		private String type;
		private List<String> owner;
		private List<String> measurements;

		/**
		 * "firmware": 1
		 */
		@JsonProperty("firmware")
		public Integer getFirmware() {
			return this.firmware;
		}

		/**
		 * "_id": "f0:4d:a2:ee:bc:49"
		 */
		@JsonProperty("_id")
		public String getId() {
			return this.id;
		}

		/**
		 * "ip": "127.0.0.1"
		 */
		@JsonProperty("ip")
		public String getIp() {
			return this.ip;
		}

		/**
		 * "last_fw_update": 1347008293
		 */
		@JsonProperty("last_fw_update")
		public Date getLastFirmwareUpdate() {
			return this.lastFirmwareUpdate;
		}

		/**
		 * "last_radio_store": 1325675936
		 */
		@JsonProperty("last_radio_store")
		public Date getLastRadioStore() {
			return this.lastRadioStore;
		}

		/**
		 * "last_status_store": 1347624601
		 */
		@JsonProperty("last_status_store")
		public Date getLastStatusStore() {
			return this.lastStatusStore;
		}

		/**
		 * "last_upgrade": 1347455989
		 */
		@JsonProperty("last_upgrade")
		public Date getLastUpgrade() {
			return this.lastUpgrade;
		}

		/**
		 * "data_type":["Temperature","Co2","Humidity","Noise","Pressure"]
		 */
		@JsonProperty("data_type")
		public List<String> getMeasurements() {
			return this.measurements;
		}

		/**
		 * "module_name": "Inside"
		 */
		@JsonProperty("module_name")
		public String getModuleName() {
			return this.moduleName;
		}

		/**
		 * "modules": [ "02:00:00:ee:bc:49" ]
		 */
		@JsonProperty("modules")
		public List<String> getModules() {
			return this.modules;
		}

		/**
		 * "user_owner": [ "4fe091cdc56a6eb606000118" ]
		 */
		@JsonProperty("user_owner")
		public List<String> getOwner() {
			return this.owner;
		}

		/**
		 * <pre>
		 * "place": {
		 *   "altitude":  33,
		 * 	 "country":  "FR",
		 * 	 "location":  [
		 * 	   2.35222,
		 * 	   48.85661 
		 * 	 ],
		 * 	 "timezone":  "Europe/Paris",
		 * 	 "trust_location":  true 
		 * }
		 * </pre>
		 */
		@JsonProperty("place")
		public Place getPlace() {
			return this.place;
		}

		/**
		 * "station_name": "LA"
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
		 * "public_ext_data": true
		 */
		@JsonProperty("public_ext_data")
		public Boolean isPublicData() {
			return this.publicData;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			// TODO
			builder.append("id", this.id);
			builder.append("firmware", this.firmware);
			builder.append("ip", this.ip);
			builder.append("lastFirmwareUpdate", this.lastFirmwareUpdate);
			builder.append("lastRadioStore", this.lastRadioStore);
			builder.append("lastStatusStore", this.lastStatusStore);
			builder.append("lastUpgrade", this.lastUpgrade);
			builder.append("moduleName", this.moduleName);
			builder.append("modules", this.modules);
			builder.append("place", this.place);
			builder.append("publicData", this.publicData);
			builder.append("stationName", this.stationName);
			builder.append("type", this.type);
			builder.append("owner", this.owner);

			return builder.toString();
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Module extends AbstractMessagePart {

		private String id;
		private Integer firmware;
		private String mainDevice;
		private String moduleName;
		private Boolean publicData;
		private Integer rfStatus;
		private String type;
		private List<String> measurements;

		/**
		 * "firmware": 4
		 */
		@JsonProperty("firmware")
		public Integer getFirmware() {
			return this.firmware;
		}

		/**
		 * "_id": "02:00:00:ee:bc:49"
		 */
		@JsonProperty("_id")
		public String getId() {
			return this.id;
		}

		/**
		 * "main_device": "f0:4d:a2:ee:bc:49"
		 */
		@JsonProperty("main_device")
		public String getMainDevice() {
			return this.mainDevice;
		}

		/**
		 * "data_type":["Temperature","Co2","Humidity","Noise","Pressure"]
		 */
		@JsonProperty("data_type")
		public List<String> getMeasurements() {
			return this.measurements;
		}

		/**
		 * "module_name": "Outside"
		 */
		@JsonProperty("module_name")
		public String getModuleName() {
			return this.moduleName;
		}

		/**
		 * "rf_status": 161
		 */
		@JsonProperty("rf_status")
		public Integer getRfStatus() {
			return this.rfStatus;
		}

		/**
		 * "type": "NAModule1"
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * "public_ext_data": true
		 */
		@JsonProperty("public_ext_data")
		public Boolean isPublicData() {
			return this.publicData;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("id", this.id);
			builder.append("firmware", this.firmware);
			builder.append("mainDevice", this.mainDevice);
			builder.append("moduleName", this.moduleName);
			builder.append("publicData", this.publicData);
			builder.append("rfStatus", this.rfStatus);
			builder.append("type", this.type);

			return builder.toString();
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Place extends AbstractMessagePart {

		private Integer altitude;
		private String country;
		private List<Integer> location;
		private String timezone;
		private Boolean trustedLocation;

		/**
		 * "altitude": 33
		 */
		@JsonProperty("altitude")
		public Integer getAltitude() {
			return this.altitude;
		}

		/**
		 * "country": "FR"
		 */
		@JsonProperty("country")
		public String getCountry() {
			return this.country;
		}

		/**
		 * <pre>
		 * "location": [
		 *   2.35222,
		 *   48.85661 
		 * ]
		 * </pre>
		 */
		@JsonProperty("location")
		public List<Integer> getlocation() {
			return this.location;
		}

		/**
		 * "timezone": "Europe/Paris"
		 */
		@JsonProperty("timezone")
		public String getTimezone() {
			return this.timezone;
		}

		/**
		 * "trust_location": true
		 */
		@JsonProperty("trust_location")
		public Boolean isTrustedLocation() {
			return this.trustedLocation;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("altitude", this.altitude);
			builder.append("country", this.country);
			builder.append("location", this.location);
			builder.append("timezone", this.timezone);
			builder.append("trustedLocation", this.trustedLocation);

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

	public List<Module> getModules() {
		return this.body.modules;
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
