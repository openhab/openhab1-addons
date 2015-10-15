/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.messages;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import org.openhab.core.types.UnDefType;

/**
 * The Thermostat Java Bean is the central piece of the ecobee API. All objects relate in one way or another to a real
 * thermostat. The Thermostat class and its component classes define the real thermostat device.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Thermostat.shtml">Thermostat</a>
 * @author John Cocula
 * @since 1.7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Thermostat extends AbstractMessagePart {

	/**
	 * Numeric values whose value is not known are expressed as -5002. This is the numeric equivalent to a null value.
	 * The value of -5002 had been chosen as an unknown value because the representation of -500.2F is below absolute
	 * zero when representing temperatures.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/technical-notes.shtml">Core Concepts</a>
	 */
	public static final int UNKNOWN_VALUE = -5002;

	/**
	 * There is a concept of dates "before time began" which is equivalent to a NULL time and "end of time" which
	 * represents infinite durations (i.e. events). The API represents these as:
	 * 
	 * Before Time Began Date: 2008-01-02
	 * 
	 * End of Time Date: 2035-01-01
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/technical-notes.shtml">Core Concepts</a>
	 */
	public static final Date BEFORE_TIME_BEGAN = new GregorianCalendar(2008, 1, 2).getTime();
	public static final Date END_OF_TIME = new GregorianCalendar(2035, 1, 1).getTime();

	private String identifier;
	private String name;
	private String thermostatRev;
	private Boolean isRegistered;
	private String modelNumber;
	private Date lastModified;
	private Date thermostatTime;
	private Date utcTime;
	private List<Alert> alerts;
	private Settings settings;
	private Runtime runtime;
	private ExtendedRuntime extendedRuntime;
	private Electricity electricity;
	private List<Device> devices;
	private Location location;
	private Technician technician;
	private Utility utility;
	private Management management;
	private Weather weather;
	private List<Event> events;
	private Program program;
	private HouseDetails houseDetails;
	private ThermostatOemCfg oemCfg;
	private String equipmentStatus;
	private NotificationSettings notificationSettings;
	private ThermostatPrivacy privacy;
	private Version version;
	@JsonProperty("remoteSensors")
	private List<RemoteSensor> remoteSensorList;
	@JsonIgnore
	private Map<String, RemoteSensor> remoteSensors;

	public Thermostat(@JsonProperty("identifier") String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Return a JavaBean property by name.
	 * 
	 * @param name
	 *            the named property to return
	 * @return the named property's value
	 * @see BeanUtils#getProperty()
	 * @throws IllegalAccessException
	 *             if the caller does not have access to the property accessor method
	 * @throws InvocationTargetException
	 *             if the property accessor method throws an exception
	 * @throws NoSuchMethodException
	 *             if the accessor method for this property cannot be found
	 */
	public Object getProperty(String name) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {

		return PropertyUtils.getProperty(this, name);
	}

	/**
	 * Set the specified property value, performing type conversions as required to conform to the type of the
	 * destination property. Nested beans are created if they are currently <code>null</code>.
	 * 
	 * @param name
	 *            property name (can be nested/indexed/mapped/combo)
	 * @param value
	 *            value to be set
	 * @throws IllegalAccessException
	 *             if the caller does not have access to the property accessor method
	 * @throws InvocationTargetException
	 *             if the property accessor method throws an exception
	 */
	public void setProperty(String name, Object value) throws IllegalAccessException, InvocationTargetException {

		if (name.startsWith("settings") && this.settings == null) {
			this.settings = new Settings();
		} else if (name.startsWith("location") && this.location == null) {
			this.location = new Location();
		} else if (name.startsWith("program") && this.program == null) {
			this.program = new Program();
		} else if (name.startsWith("houseDetails") && this.houseDetails == null) {
			this.houseDetails = new HouseDetails();
		} else if (name.startsWith("notificationSettings") && this.notificationSettings == null) {
			this.notificationSettings = new NotificationSettings();
		}

		BeanUtils.setProperty(this, name, value);
	}

	/**
	 * @return the unique thermostat serial number.
	 */
	@JsonProperty("identifier")
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * @return the user defined name for a thermostat
	 */
	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the user defined name for a thermostat
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return current thermostat configuration revision
	 */
	@JsonProperty("thermostatRev")
	public String getThermostatRev() {
		return this.thermostatRev;
	}

	/**
	 * @return whether the user registered the thermostat
	 */
	@JsonProperty("isRegistered")
	public Boolean getIsRegistered() {
		return this.isRegistered;
	}

	/**
	 * @return the thermostat model number. Values: idtSmart, idtEms, siSmart, siEms
	 */
	@JsonProperty("modelNumber")
	public String getModelNumber() {
		return this.modelNumber;
	}

	/**
	 * @return the last modified date time for the thermostat configuration.
	 */
	@JsonProperty("lastModified")
	public Date getLastModified() {
		return this.lastModified;
	}

	/**
	 * @return the current time in the thermostat's time zone TODO time zone adjust (@watou)
	 *         http://wiki.fasterxml.com/JacksonFAQDateHandling
	 */
	@JsonProperty("thermostatTime")
	public Date getThermostatTime() {
		return this.thermostatTime;
	}

	/**
	 * @return the current time in UTC
	 */
	@JsonProperty("utcTime")
	public Date getUtcTime() {
		return this.utcTime;
	}

	/**
	 * @return the list of Alert objects tied to the thermostat
	 */
	@JsonProperty("alerts")
	public List<Alert> getAlerts() {
		return this.alerts;
	}

	/**
	 * @return the thermostat Setting object linked to the thermostat
	 */
	@JsonProperty("settings")
	public Settings getSettings() {
		return this.settings;
	}

	/**
	 * @param settings
	 *            the thermostat Setting object linked to the thermostat
	 */
	@JsonProperty("settings")
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * @return the Runtime state object for the thermostat
	 */
	@JsonProperty("runtime")
	public Runtime getRuntime() {
		return this.runtime;
	}

	/**
	 * @return the ExtendedRuntime object for the thermostat
	 */
	@JsonProperty("extendedRuntime")
	public ExtendedRuntime getExtendedRuntime() {
		return this.extendedRuntime;
	}

	/**
	 * @return the Electricity object for the thermostat
	 */
	@JsonProperty("electricity")
	public Electricity getElectricity() {
		return this.electricity;
	}

	/**
	 * @return the list of Device objects linked to the thermostat
	 */
	@JsonProperty("devices")
	public List<Device> getDevices() {
		return this.devices;
	}

	/**
	 * @return the Location object for the thermostat
	 */
	@JsonProperty("location")
	public Location getLocation() {
		return this.location;
	}

	/**
	 * @param location
	 *            the Location object for the thermostat
	 */
	@JsonProperty("location")
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the Technician object associated with the thermostat containing the technician contact information
	 */
	@JsonProperty("technician")
	public Technician getTechnician() {
		return this.technician;
	}

	/**
	 * @return the Utility object associated with the thermostat containing the utility company information
	 */
	@JsonProperty("utility")
	public Utility getUtility() {
		return this.utility;
	}

	/**
	 * @return the Management object associated with the thermostat containing the management company information
	 */
	@JsonProperty("management")
	public Management getManagement() {
		return this.management;
	}

	/**
	 * @return the Weather object linked to the thermostat representing the current weather on the thermostat
	 */
	@JsonProperty("weather")
	public Weather getWeather() {
		return this.weather;
	}

	/**
	 * @return the list of Event objects linked to the thermostat representing any events that are active or scheduled.
	 */
	@JsonProperty("events")
	public List<Event> getEvents() {
		return this.events;
	}

	/**
	 * @return the Program object for the thermostat
	 */
	@JsonProperty("program")
	public Program getProgram() {
		return this.program;
	}

	/**
	 * @param program
	 *            the Program object for the thermostat
	 */
	@JsonProperty("program")
	public void setProgram(Program program) {
		this.program = program;
	}

	/**
	 * @return the HouseDetails object that contains the information about the house the thermostat is installed in
	 */
	@JsonProperty("houseDetails")
	public HouseDetails getHouseDetails() {
		return this.houseDetails;
	}

	/**
	 * @param houseDetails
	 *            the HouseDetails object that contains the information about the house the thermostat is installed in
	 */
	@JsonProperty("houseDetails")
	public void setHouseDetails(HouseDetails houseDetails) {
		this.houseDetails = houseDetails;
	}

	/**
	 * @return the ThermostatOemCfg object that contains information about the OEM specific thermostat
	 */
	@JsonProperty("oemCfg")
	public ThermostatOemCfg getOemCfg() {
		return this.oemCfg;
	}

	/**
	 * @param oemCfg
	 *            the ThermostatOemCfg object that contains information about the OEM specific thermostat
	 */
	@JsonProperty("oemCfg")
	public void setOemCfg(ThermostatOemCfg oemCfg) {
		this.oemCfg = oemCfg;
	}

	/**
	 * The status of all equipment controlled by this Thermostat. Only running equipment is listed in the CSV String. If
	 * no equipment is currently running an empty string is returned.
	 * 
	 * Values: heatPump, heatPump2, heatPump3, compCool1, compCool2, auxHeat1, auxHeat2, auxHeat3, fan, humidifier,
	 * dehumidifier, ventilator, economizer, compHotWater, auxHotWater.
	 * 
	 * @return the equipmentStatus
	 */
	@JsonProperty("equipmentStatus")
	public String getEquipmentStatus() {
		return this.equipmentStatus;
	}

	/**
	 * @return the NotificationSettings object containing the configuration for Alert and Reminders for the Thermostat
	 */
	@JsonProperty("notificationSettings")
	public NotificationSettings getNotificationSettings() {
		return this.notificationSettings;
	}

	/**
	 * @param notificationSettings
	 *            the NotificationSettings object containing the configuration for Alert and Reminders for the
	 *            Thermostat
	 */
	@JsonProperty("notificationSettings")
	public void setNotificationSettings(NotificationSettings notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	/**
	 * @return the Privacy object containing the privacy settings for the Thermostat. Note: access to this object is
	 *         restricted to callers with implicit authentication.
	 */
	@JsonProperty("privacy")
	public ThermostatPrivacy getPrivacy() {
		return this.privacy;
	}

	/**
	 * @param privacy
	 *            the Privacy object containing the privacy settings for the Thermostat. Note: access to this object is
	 *            restricted to callers with implicit authentication.
	 */
	@JsonProperty("privacy")
	public void setPrivacy(ThermostatPrivacy privacy) {
		this.privacy = privacy;
	}

	/**
	 * @return the version information about the thermostat
	 */
	@JsonProperty("version")
	public Version getVersion() {
		return version;
	}

	/**
	 * @return the list of RemoteSensor objects for the Thermostat.
	 */
	@JsonProperty("remoteSensors")
	public List<RemoteSensor> getRemoteSensorList() {
		return this.remoteSensorList;
	}

	/**
	 * @return the name-based map of RemoteSensor objects.
	 */
	@JsonIgnore
	public Map<String, RemoteSensor> getRemoteSensors() {
		return this.remoteSensors;
	}

	/**
	 * @return the running event or null if there is none
	 */
	@JsonIgnore
	public Event getRunningEvent() {
		if (this.events != null) {
			for (Event event : this.events) {
				if (event.isRunning()) {
					return event;
				}
			}
		}
		return null;
	}

	/**
	 * Create a named-based map of RemoteSensors from the list of RemoteSensors, for ease of access from beanutils.
	 */
	protected void sync() {
		this.remoteSensors = new HashMap<String, RemoteSensor>();
		if (this.remoteSensorList != null) {
			for (RemoteSensor rs : this.remoteSensorList) {
				rs.sync();
				this.remoteSensors.put(rs.getName(), rs);
			}
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("identifier", this.identifier);
		builder.append("name", this.name);
		builder.append("thermostatRev", this.thermostatRev);
		builder.append("isRegistered", this.isRegistered);
		builder.append("modelNumber", this.modelNumber);
		builder.append("lastModified", this.lastModified);
		builder.append("thermostatTime", this.thermostatTime);
		builder.append("utcTime", this.utcTime);
		builder.append("alerts", this.alerts);
		builder.append("settings", this.settings);
		builder.append("runtime", this.runtime);
		builder.append("extendedRuntime", this.extendedRuntime);
		builder.append("electricity", this.electricity);
		builder.append("devices", this.devices);
		builder.append("location", this.location);
		builder.append("technician", this.technician);
		builder.append("utility", this.utility);
		builder.append("management", this.management);
		builder.append("weather", this.weather);
		builder.append("events", this.events);
		builder.append("program", this.program);
		builder.append("houseDetails", this.houseDetails);
		builder.append("oemCfg", this.oemCfg);
		builder.append("equipmentStatus", this.equipmentStatus);
		builder.append("notificationSettings", this.notificationSettings);
		builder.append("privacy", this.privacy);
		builder.append("version", this.version);
		builder.append("remoteSensors", this.remoteSensorList);

		return builder.toString();
	}

	/**
	 * The Alert object represents an alert generated either by a thermostat or user which requires user attention. It
	 * may be an error, or a reminder for a filter change. Alerts may not be modified directly but rather they must be
	 * acknowledged using the Acknowledge Function.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Alert.shtml">Alert</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Alert extends AbstractMessagePart {
		private String acknowledgeRef;
		private String date;
		private String time;
		private String severity;
		private String text;
		private Integer alertNumber;
		private String alertType;
		@JsonProperty("isOperatorAlert")
		private Boolean _isOperatorAlert;
		private String reminder;
		@JsonProperty("showIdt")
		private Boolean _showIdt;
		@JsonProperty("showWeb")
		private Boolean _showWeb;
		@JsonProperty("sendEmail")
		private Boolean _sendEmail;
		private String acknowledgement;
		@JsonProperty("remindMeLater")
		private Boolean _remindMeLater;
		private String thermostatIdentifier;
		private String notificationType;

		/**
		 * @return the acknowledgeRef
		 */
		@JsonProperty("acknowledgeRef")
		public String getAcknowledgeRef() {
			return this.acknowledgeRef;
		}

		/**
		 * @return the date
		 */
		@JsonProperty("date")
		public String getDate() {
			return this.date;
		}

		/**
		 * @return the time
		 */
		@JsonProperty("time")
		public String getTime() {
			return this.time;
		}

		/**
		 * @return the severity
		 */
		@JsonProperty("severity")
		public String getSeverity() {
			return this.severity;
		}

		/**
		 * @return the text
		 */
		@JsonProperty("text")
		public String getText() {
			return this.text;
		}

		/**
		 * @return the alertNumber
		 */
		@JsonProperty("alertNumber")
		public Integer getAlertNumber() {
			return this.alertNumber;
		}

		/**
		 * @return the alertType
		 */
		@JsonProperty("alertType")
		public String getAlertType() {
			return this.alertType;
		}

		/**
		 * @return the isOperatorAlert
		 */
		@JsonProperty("isOperatorAlert")
		public Boolean isOperatorAlert() {
			return this._isOperatorAlert;
		}

		/**
		 * @return the reminder
		 */
		@JsonProperty("reminder")
		public String getReminder() {
			return this.reminder;
		}

		/**
		 * @return the showIdt
		 */
		@JsonProperty("showIdt")
		public Boolean showIdt() {
			return this._showIdt;
		}

		/**
		 * @return the showWeb
		 */
		@JsonProperty("showWeb")
		public Boolean showWeb() {
			return this._showWeb;
		}

		/**
		 * @return the sendEmail
		 */
		@JsonProperty("sendEmail")
		public Boolean sendEmail() {
			return this._sendEmail;
		}

		/**
		 * @return the acknowledgement
		 */
		@JsonProperty("acknowledgement")
		public String getAcknowledgement() {
			return this.acknowledgement;
		}

		/**
		 * @return the remindMeLater
		 */
		@JsonProperty("remindMeLater")
		public Boolean remindMeLater() {
			return this._remindMeLater;
		}

		/**
		 * @return the thermostatIdentifier
		 */
		@JsonProperty("thermostatIdentifier")
		public String getThermostatIdentifier() {
			return this.thermostatIdentifier;
		}

		/**
		 * @return the notificationType
		 */
		@JsonProperty("notificationType")
		public String getNotificationType() {
			return this.notificationType;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());

			builder.append("acknowledgeRef", this.acknowledgeRef);
			builder.append("date", this.date);
			builder.append("time", this.time);
			builder.append("severity", this.severity);
			builder.append("text", this.text);
			builder.append("alertNumber", this.alertNumber);
			builder.append("alertType", this.alertType);
			builder.append("isOperatorAlert", this._isOperatorAlert);
			builder.append("reminder", this.reminder);
			builder.append("showIdt", this._showIdt);
			builder.append("showWeb", this._showWeb);
			builder.append("sendEmail", this._sendEmail);
			builder.append("acknowledgement", this.acknowledgement);
			builder.append("remindMeLater", this._remindMeLater);
			builder.append("thermostatIdentifier", this.thermostatIdentifier);
			builder.append("notificationType", this.notificationType);

			return builder.toString();
		}
	}

	/**
	 * The Settings Java Bean contains all the configuration properties of the thermostat in which it is contained.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Settings.shtml">Settings</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Settings extends AbstractMessagePart {
		private HvacMode hvacMode;
		private String lastServiceDate; // TODO Jackson 1.9 dates (@watou)
		private Boolean serviceRemindMe;
		private Integer monthsBetweenService;
		private String remindMeDate; // TODO Jackson 1.9 dates (@watou)
		private VentilatorMode vent;
		private Integer ventilatorMinOnTime;
		private Boolean serviceRemindTechnician;
		private String eiLocation;
		private Temperature coldTempAlert;
		private Boolean coldTempAlertEnabled;
		private Temperature hotTempAlert;
		private Boolean hotTempAlertEnabled;
		private Integer coolStages;
		private Integer heatStages;
		private Temperature maxSetBack;
		private Temperature maxSetForward;
		private Temperature quickSaveSetBack;
		private Temperature quickSaveSetForward;
		private Boolean hasHeatPump;
		private Boolean hasForcedAir;
		private Boolean hasBoiler;
		private Boolean hasHumidifier;
		private Boolean hasErv;
		private Boolean hasHrv;
		private Boolean condensationAvoid;
		private Boolean useCelsius;
		private Boolean useTimeFormat12;
		private String locale;
		private String humidity;
		private String humidifierMode;
		private Integer backlightOnIntensity;
		private Integer backlightSleepIntensity;
		private Integer backlightOffTime;
		private Integer soundTickVolume;
		private Integer soundAlertVolume;
		private Integer compressorProtectionMinTime;
		private Temperature compressorProtectionMinTemp;
		private Temperature stage1HeatingDifferentialTemp;
		private Temperature stage1CoolingDifferentialTemp;
		private Integer stage1HeatingDissipationTime;
		private Integer stage1CoolingDissipationTime;
		private Boolean heatPumpReversalOnCool;
		private Boolean fanControlRequired;
		private Integer fanMinOnTime;
		private Temperature heatCoolMinDelta;
		private Temperature tempCorrection;
		private String holdAction;
		private Boolean heatPumpGroundWater;
		private Boolean hasElectric;
		private Boolean hasDehumidifier;
		private String dehumidifierMode;
		private Integer dehumidifierLevel;
		private Boolean dehumidifyWithAC;
		private Integer dehumidifyOvercoolOffset;
		private Boolean autoHeatCoolFeatureEnabled;
		private Boolean wifiOfflineAlert;
		private Temperature heatMinTemp;
		private Temperature heatMaxTemp;
		private Temperature coolMinTemp;
		private Temperature coolMaxTemp;
		private Temperature heatRangeHigh;
		private Temperature heatRangeLow;
		private Temperature coolRangeHigh;
		private Temperature coolRangeLow;
		private String userAccessCode;
		private Integer userAccessSetting;
		private Temperature auxRuntimeAlert;
		private Temperature auxOutdoorTempAlert;
		private Temperature auxMaxOutdoorTemp;
		private Boolean auxRuntimeAlertNotify;
		private Boolean auxOutdoorTempAlertNotify;
		private Boolean auxRuntimeAlertNotifyTechnician;
		private Boolean auxOutdoorTempAlertNotifyTechnician;
		private Boolean disablePreHeating;
		private Boolean disablePreCooling;
		private Boolean installerCodeRequired;
		private String drAccept;
		private Boolean isRentalProperty;
		private Boolean useZoneController;
		private Integer randomStartDelayCool;
		private Integer randomStartDelayHeat;
		private Integer humidityHighAlert;
		private Integer humidityLowAlert;
		private Boolean disableHeatPumpAlerts;
		private Boolean disableAlertsOnIdt;
		private Boolean humidityAlertNotify;
		private Boolean humidityAlertNotifyTechnician;
		private Boolean tempAlertNotify;
		private Boolean tempAlertNotifyTechnician;
		private Integer monthlyElectricityBillLimit;
		private Boolean enableElectricityBillAlert;
		private Boolean enableProjectedElectricityBillAlert;
		private Integer electricityBillingDayOfMonth;
		private Integer electricityBillCycleMonths;
		private Integer electricityBillStartMonth;
		private Integer ventilatorMinOnTimeHome;
		private Integer ventilatorMinOnTimeAway;
		private Boolean backlightOffDuringSleep;
		private Boolean autoAway;
		private Boolean smartCirculation;
		private Boolean followMeComfort;
		private String ventilatorType;
		private Boolean isVentilatorTimerOn;
		private Date ventilatorOffDateTime;
		private Boolean hasUVFilter;
		private Boolean coolingLockout;
		private Boolean ventilatorFreeCooling;
		private Boolean dehumidifyWhenHeating;
		private String groupRef;
		private String groupName;
		private Integer groupSetting;

		/**
		 * @return the current HVAC mode the thermostat is in. Values: auto, auxHeatOnly, cool, heat, off.
		 */
		@JsonProperty("hvacMode")
		public HvacMode getHvacMode() {
			return this.hvacMode;
		}

		/**
		 * @param hvacMode
		 *            the current HVAC mode the thermostat is in. Values: auto, auxHeatOnly, cool, heat, off.
		 */
		@JsonProperty("hvacMode")
		public void setHvacMode(HvacMode hvacMode) {
			this.hvacMode = hvacMode;
		}

		/**
		 * @return the last service date of the HVAC equipment
		 */
		@JsonProperty("lastServiceDate")
		public String getLastServiceDate() {
			return this.lastServiceDate;
		}

		/**
		 * @param lastServiceDate
		 *            the last service date of the HVAC equipment
		 */
		@JsonProperty("lastServiceDate")
		public void setLastServiceDate(String lastServiceDate) {
			this.lastServiceDate = lastServiceDate;
		}

		/**
		 * @return whether to send an alert when service is required again
		 */
		@JsonProperty("serviceRemindMe")
		public Boolean getServiceRemindMe() {
			return this.serviceRemindMe;
		}

		/**
		 * @param serviceRemindMe
		 *            whether to send an alert when service is required again
		 */
		@JsonProperty("serviceRemindMe")
		public void setServiceRemindMe(Boolean serviceRemindMe) {
			this.serviceRemindMe = serviceRemindMe;
		}

		/**
		 * @return the user configured monthly interval between HVAC service reminders
		 */
		@JsonProperty("monthsBetweenService")
		public Integer getMonthsBetweenService() {
			return this.monthsBetweenService;
		}

		/**
		 * @param monthsBetweenService
		 *            the user configured monthly interval between HVAC service reminders
		 */
		@JsonProperty("monthsBetweenService")
		public void setMonthsBetweenService(Integer monthsBetweenService) {
			this.monthsBetweenService = monthsBetweenService;
		}

		/**
		 * @return the date to be reminded about the next HVAC service date
		 */
		@JsonProperty("remindMeDate")
		public String getRemindMeDate() {
			return this.remindMeDate;
		}

		/**
		 * @param remindMeDate
		 *            the date to be reminded about the next HVAC service date
		 */
		@JsonProperty("remindMeDate")
		public void setRemindMeDate(String remindMeDate) {
			this.remindMeDate = remindMeDate;
		}

		/**
		 * @return the ventilator mode. Values: auto, minontime, on, off
		 */
		@JsonProperty("vent")
		public VentilatorMode getVent() {
			return this.vent;
		}

		/**
		 * @param vent
		 *            the ventilator mode. Values: auto, minontime, on, off
		 */
		@JsonProperty("vent")
		public void setVent(VentilatorMode vent) {
			this.vent = vent;
		}

		/**
		 * @return the minimum time in minutes the ventilator is configured to run. The thermostat will always guarantee
		 *         that the ventilator runs for this minimum duration whenever engaged.
		 */
		@JsonProperty("ventilatorMinOnTime")
		public Integer getVentilatorMinOnTime() {
			return this.ventilatorMinOnTime;
		}

		/**
		 * @param ventilatorMinOnTime
		 *            the minimum time in minutes the ventilator is configured to run. The thermostat will always
		 *            guarantee that the ventilator runs for this minimum duration whenever engaged.
		 */
		@JsonProperty("ventilatorMinOnTime")
		public void setVentilatorMinOnTime(Integer ventilatorMinOnTime) {
			this.ventilatorMinOnTime = ventilatorMinOnTime;
		}

		/**
		 * @return whether the technician associated with this thermostat should receive the HVAC service reminders as
		 *         well
		 */
		@JsonProperty("serviceRemindTechnician")
		public Boolean getServiceRemindTechnician() {
			return this.serviceRemindTechnician;
		}

		/**
		 * @param serviceRemindTechnician
		 *            whether the technician associated with this thermostat should receive the HVAC service reminders
		 *            as well
		 */
		@JsonProperty("serviceRemindTechnician")
		public void setServiceRemindTechnician(Boolean serviceRemindTechnician) {
			this.serviceRemindTechnician = serviceRemindTechnician;
		}

		/**
		 * @return a note about the physical location where the SMART or EMS Equipment Interface module is located
		 */
		@JsonProperty("eiLocation")
		public String getEiLocation() {
			return this.eiLocation;
		}

		/**
		 * @param eiLocation
		 *            a note about the physical location where the SMART or EMS Equipment Interface module is located
		 */
		@JsonProperty("eiLocation")
		public void setEiLocation(String eiLocation) {
			this.eiLocation = eiLocation;
		}

		/**
		 * @return the temperature at which a cold temp alert is triggered
		 */
		@JsonProperty("coldTempAlert")
		public Temperature getColdTempAlert() {
			return this.coldTempAlert;
		}

		/**
		 * @param coldTempAlert
		 *            the temperature at which a cold temp alert is triggered
		 */
		@JsonProperty("coldTempAlert")
		public void setColdTempAlert(Temperature coldTempAlert) {
			this.coldTempAlert = coldTempAlert;
		}

		/**
		 * @return whether cold temperature alerts are enabled
		 */
		@JsonProperty("coldTempAlertEnabled")
		public Boolean getColdTempAlertEnabled() {
			return this.coldTempAlertEnabled;
		}

		/**
		 * @param coldTempAlertEnabled
		 *            whether cold temperature alerts are enabled
		 */
		@JsonProperty("coldTempAlertEnabled")
		public void setColdTempAlertEnabled(Boolean coldTempAlertEnabled) {
			this.coldTempAlertEnabled = coldTempAlertEnabled;
		}

		/**
		 * @return the temperature at which a hot temp alert is triggered
		 */
		@JsonProperty("hotTempAlert")
		public Temperature getHotTempAlert() {
			return this.hotTempAlert;
		}

		/**
		 * @param hotTempAlert
		 *            the temperature at which a hot temp alert is triggered
		 */
		@JsonProperty("hotTempAlert")
		public void setHotTempAlert(Temperature hotTempAlert) {
			this.hotTempAlert = hotTempAlert;
		}

		/**
		 * @return whether hot temperature alerts are enabled
		 */
		@JsonProperty("hotTempAlertEnabled")
		public Boolean getHotTempAlertEnabled() {
			return this.hotTempAlertEnabled;
		}

		/**
		 * @param hotTempAlertEnabled
		 *            whether hot temperature alerts are enabled
		 */
		@JsonProperty("hotTempAlertEnabled")
		public void setHotTempAlertEnabled(Boolean hotTempAlertEnabled) {
			this.hotTempAlertEnabled = hotTempAlertEnabled;
		}

		/**
		 * @return the number of cool stages the connected HVAC equipment supports
		 */
		@JsonProperty("coolStages")
		public Integer getCoolStages() {
			return this.coolStages;
		}

		/**
		 * @return the number of heat stages the connected HVAC equipment supports
		 */
		@JsonProperty("heatStages")
		public Integer getHeatStages() {
			return this.heatStages;
		}

		/**
		 * @return the maximum automated set point set back offset allowed in degrees
		 */
		@JsonProperty("maxSetBack")
		public Temperature getMaxSetBack() {
			return this.maxSetBack;
		}

		/**
		 * @param maxSetBack
		 *            the maximum automated set point set back offset allowed in degrees
		 */
		@JsonProperty("maxSetBack")
		public void setMaxSetBack(Temperature maxSetBack) {
			this.maxSetBack = maxSetBack;
		}

		/**
		 * @return the maximum automated set point set forward offset allowed in degrees
		 */
		@JsonProperty("maxSetForward")
		public Temperature getMaxSetForward() {
			return this.maxSetForward;
		}

		/**
		 * @param maxSetForward
		 *            the maximum automated set point set forward offset allowed in degrees
		 */
		@JsonProperty("maxSetForward")
		public void setMaxSetForward(Temperature maxSetForward) {
			this.maxSetForward = maxSetForward;
		}

		/**
		 * @return the set point set back offset, in degrees, configured for a quick save event
		 */
		@JsonProperty("quickSaveSetBack")
		public Temperature getQuickSaveSetBack() {
			return this.quickSaveSetBack;
		}

		/**
		 * @param quickSaveSetBack
		 *            the set point set back offset, in degrees, configured for a quick save event
		 */
		@JsonProperty("quickSaveSetBack")
		public void setQuickSaveSetBack(Temperature quickSaveSetBack) {
			this.quickSaveSetBack = quickSaveSetBack;
		}

		/**
		 * @return the set point set forward offset, in degrees, configured for a quick save event
		 */
		@JsonProperty("quickSaveSetForward")
		public Temperature getQuickSaveSetForward() {
			return this.quickSaveSetForward;
		}

		/**
		 * @param quickSaveSetForward
		 *            the set point set forward offset, in degrees, configured for a quick save event
		 */
		@JsonProperty("quickSaveSetForward")
		public void setQuickSaveSetForward(Temperature quickSaveSetForward) {
			this.quickSaveSetForward = quickSaveSetForward;
		}

		/**
		 * @return whether the thermostat is controlling a heat pump
		 */
		@JsonProperty("hasHeatPump")
		public Boolean getHasHeatPump() {
			return this.hasHeatPump;
		}

		/**
		 * @return whether the thermostat is controlling a forced air furnace
		 */
		@JsonProperty("hasForcedAir")
		public Boolean getHasForcedAir() {
			return this.hasForcedAir;
		}

		/**
		 * @return whether the thermostat is controlling a boiler
		 */
		@JsonProperty("hasBoiler")
		public Boolean getHasBoiler() {
			return this.hasBoiler;
		}

		/**
		 * @return whether the thermostat is controlling a humidifier
		 */
		@JsonProperty("hasHumidifier")
		public Boolean getHasHumidifier() {
			return this.hasHumidifier;
		}

		/**
		 * @return whether the thermostat is controlling an energy recovery ventilator
		 */
		@JsonProperty("hasErv")
		public Boolean getHasErv() {
			return this.hasErv;
		}

		/**
		 * @return whether the thermostat is controlling a heat recovery ventilator
		 */
		@JsonProperty("hasHrv")
		public Boolean getHasHrv() {
			return this.hasHrv;
		}

		/**
		 * @return whether the thermostat is in frost control mode
		 */
		@JsonProperty("condensationAvoid")
		public Boolean getCondensationAvoid() {
			return this.condensationAvoid;
		}

		/**
		 * @param condensationAvoid
		 *            whether the thermostat is in frost control mode
		 */
		@JsonProperty("condensationAvoid")
		public void setCondensationAvoid(Boolean condensationAvoid) {
			this.condensationAvoid = condensationAvoid;
		}

		/**
		 * @return whether the thermostat is configured to report in degrees Celsius
		 */
		@JsonProperty("useCelsius")
		public Boolean getUseCelsius() {
			return this.useCelsius;
		}

		/**
		 * @param useCelsius
		 *            whether the thermostat is configured to report in degrees Celsius
		 */
		@JsonProperty("useCelsius")
		public void setUseCelsius(Boolean useCelsius) {
			this.useCelsius = useCelsius;
		}

		/**
		 * @return whether the thermostat is using 12hr time format
		 */
		@JsonProperty("useTimeFormat12")
		public Boolean getUseTimeFormat12() {
			return this.useTimeFormat12;
		}

		/**
		 * @param useTimeFormat12
		 *            whether the thermostat is using 12hr time format
		 */
		@JsonProperty("useTimeFormat12")
		public void setUseTimeFormat12(Boolean useTimeFormat12) {
			this.useTimeFormat12 = useTimeFormat12;
		}

		/**
		 * @return the locale
		 */
		@JsonProperty("locale")
		public String getLocale() {
			return this.locale;
		}

		/**
		 * @param locale
		 *            the locale to set
		 */
		@JsonProperty("locale")
		public void setLocale(String locale) {
			this.locale = locale;
		}

		/**
		 * @return the minimum humidity level (in percent) set point for the humidifier
		 */
		@JsonProperty("humidity")
		public String getHumidity() {
			return this.humidity;
		}

		/**
		 * @param humidity
		 *            the minimum humidity level (in percent) set point for the humidifier
		 */
		@JsonProperty("humidity")
		public void setHumidity(String humidity) {
			this.humidity = humidity;
		}

		/**
		 * @return the humidifier mode. Values: auto, manual, off
		 */
		@JsonProperty("humidifierMode")
		public String getHumidifierMode() {
			return this.humidifierMode;
		}

		/**
		 * @param humidifierMode
		 *            the humidifier mode. Values: auto, manual, off
		 */
		@JsonProperty("humidifierMode")
		public void setHumidifierMode(String humidifierMode) {
			this.humidifierMode = humidifierMode;
		}

		/**
		 * @return the thermostat backlight intensity when on. A value between 1 and 10.
		 */
		@JsonProperty("backlightOnIntensity")
		public Integer getBacklightOnIntensity() {
			return this.backlightOnIntensity;
		}

		/**
		 * @param backlightOnIntensity
		 *            the thermostat backlight intensity when on. A value between 1 and 10.
		 */
		@JsonProperty("backlightOnIntensity")
		public void setBacklightOnIntensity(Integer backlightOnIntensity) {
			this.backlightOnIntensity = backlightOnIntensity;
		}

		/**
		 * @return the thermostat backlight intensity when asleep. A value between 1 and 10.
		 */
		@JsonProperty("backlightSleepIntensity")
		public Integer getBacklightSleepIntensity() {
			return this.backlightSleepIntensity;
		}

		/**
		 * @param backlightSleepIntensity
		 *            the thermostat backlight intensity when asleep. A value between 1 and 10.
		 */
		@JsonProperty("backlightSleepIntensity")
		public void setBacklightSleepIntensity(Integer backlightSleepIntensity) {
			this.backlightSleepIntensity = backlightSleepIntensity;
		}

		/**
		 * @return the time in seconds before the thermostat screen goes into sleep mode
		 */
		@JsonProperty("backlightOffTime")
		public Integer getBacklightOffTime() {
			return this.backlightOffTime;
		}

		/**
		 * @param backlightOffTime
		 *            the time in seconds before the thermostat screen goes into sleep mode
		 */
		@JsonProperty("backlightOffTime")
		public void setBacklightOffTime(Integer backlightOffTime) {
			this.backlightOffTime = backlightOffTime;
		}

		/**
		 * @return the volume level for key presses on the thermostat. A value between 1 and 10.
		 */
		@JsonProperty("soundTickVolume")
		public Integer getSoundTickVolume() {
			return this.soundTickVolume;
		}

		/**
		 * @param soundTickVolume
		 *            the volume level for key presses on the thermostat. A value between 1 and 10.
		 */
		@JsonProperty("soundTickVolume")
		public void setSoundTickVolume(Integer soundTickVolume) {
			this.soundTickVolume = soundTickVolume;
		}

		/**
		 * @return the volume level for alerts on the thermostat. A value between 1 and 10.
		 */
		@JsonProperty("soundAlertVolume")
		public Integer getSoundAlertVolume() {
			return this.soundAlertVolume;
		}

		/**
		 * @param soundAlertVolume
		 *            the volume level for alerts on the thermostat. A value between 1 and 10.
		 */
		@JsonProperty("soundAlertVolume")
		public void setSoundAlertVolume(Integer soundAlertVolume) {
			this.soundAlertVolume = soundAlertVolume;
		}

		/**
		 * @return the compressorProtectionMinTime
		 */
		@JsonProperty("compressorProtectionMinTime")
		public Integer getCompressorProtectionMinTime() {
			return this.compressorProtectionMinTime;
		}

		/**
		 * @param compressorProtectionMinTime
		 *            the compressorProtectionMinTime to set
		 */
		@JsonProperty("compressorProtectionMinTime")
		public void setCompressorProtectionMinTime(Integer compressorProtectionMinTime) {
			this.compressorProtectionMinTime = compressorProtectionMinTime;
		}

		/**
		 * @return the compressorProtectionMinTemp
		 */
		@JsonProperty("compressorProtectionMinTemp")
		public Temperature getCompressorProtectionMinTemp() {
			return this.compressorProtectionMinTemp;
		}

		/**
		 * @param compressorProtectionMinTemp
		 *            the compressorProtectionMinTemp to set
		 */
		@JsonProperty("compressorProtectionMinTemp")
		public void setCompressorProtectionMinTemp(Temperature compressorProtectionMinTemp) {
			this.compressorProtectionMinTemp = compressorProtectionMinTemp;
		}

		/**
		 * @return the stage1HeatingDifferentialTemp
		 */
		@JsonProperty("stage1HeatingDifferentialTemp")
		public Temperature getStage1HeatingDifferentialTemp() {
			return this.stage1HeatingDifferentialTemp;
		}

		/**
		 * @param stage1HeatingDifferentialTemp
		 *            the stage1HeatingDifferentialTemp to set
		 */
		@JsonProperty("stage1HeatingDifferentialTemp")
		public void setStage1HeatingDifferentialTemp(Temperature stage1HeatingDifferentialTemp) {
			this.stage1HeatingDifferentialTemp = stage1HeatingDifferentialTemp;
		}

		/**
		 * @return the stage1CoolingDifferentialTemp
		 */
		@JsonProperty("stage1CoolingDifferentialTemp")
		public Temperature getStage1CoolingDifferentialTemp() {
			return this.stage1CoolingDifferentialTemp;
		}

		/**
		 * @param stage1CoolingDifferentialTemp
		 *            the stage1CoolingDifferentialTemp to set
		 */
		@JsonProperty("stage1CoolingDifferentialTemp")
		public void setStage1CoolingDifferentialTemp(Temperature stage1CoolingDifferentialTemp) {
			this.stage1CoolingDifferentialTemp = stage1CoolingDifferentialTemp;
		}

		/**
		 * @return the stage1HeatingDissipationTime
		 */
		@JsonProperty("stage1HeatingDissipationTime")
		public Integer getStage1HeatingDissipationTime() {
			return this.stage1HeatingDissipationTime;
		}

		/**
		 * @param stage1HeatingDissipationTime
		 *            the stage1HeatingDissipationTime to set
		 */
		@JsonProperty("stage1HeatingDissipationTime")
		public void setStage1HeatingDissipationTime(Integer stage1HeatingDissipationTime) {
			this.stage1HeatingDissipationTime = stage1HeatingDissipationTime;
		}

		/**
		 * @return the stage1CoolingDissipationTime
		 */
		@JsonProperty("stage1CoolingDissipationTime")
		public Integer getStage1CoolingDissipationTime() {
			return this.stage1CoolingDissipationTime;
		}

		/**
		 * @param stage1CoolingDissipationTime
		 *            the stage1CoolingDissipationTime to set
		 */
		@JsonProperty("stage1CoolingDissipationTime")
		public void setStage1CoolingDissipationTime(Integer stage1CoolingDissipationTime) {
			this.stage1CoolingDissipationTime = stage1CoolingDissipationTime;
		}

		/**
		 * @return the heatPumpReversalOnCool
		 */
		@JsonProperty("heatPumpReversalOnCool")
		public Boolean getHeatPumpReversalOnCool() {
			return this.heatPumpReversalOnCool;
		}

		/**
		 * @param heatPumpReversalOnCool
		 *            the heatPumpReversalOnCool to set
		 */
		@JsonProperty("heatPumpReversalOnCool")
		public void setHeatPumpReversalOnCool(Boolean heatPumpReversalOnCool) {
			this.heatPumpReversalOnCool = heatPumpReversalOnCool;
		}

		/**
		 * @return the fanControlRequired
		 */
		@JsonProperty("fanControlRequired")
		public Boolean getFanControlRequired() {
			return this.fanControlRequired;
		}

		/**
		 * @param fanControlRequired
		 *            the fanControlRequired to set
		 */
		@JsonProperty("fanControlRequired")
		public void setFanControlRequired(Boolean fanControlRequired) {
			this.fanControlRequired = fanControlRequired;
		}

		/**
		 * @return the minimum time, in minutes, to run the fan each hour. Value from 1 to 60.
		 */
		@JsonProperty("fanMinOnTime")
		public Integer getFanMinOnTime() {
			return this.fanMinOnTime;
		}

		/**
		 * @param fanMinOnTime
		 *            the minimum time, in minutes, to run the fan each hour. Value from 1 to 60.
		 */
		@JsonProperty("fanMinOnTime")
		public void setFanMinOnTime(Integer fanMinOnTime) {
			this.fanMinOnTime = fanMinOnTime;
		}

		/**
		 * @return the minimum temperature difference between the heat and cool values. Used to ensure that when
		 *         thermostat is in auto mode, the heat and cool values are separated by at least this value.
		 */
		@JsonProperty("heatCoolMinDelta")
		public Temperature getHeatCoolMinDelta() {
			return this.heatCoolMinDelta;
		}

		/**
		 * @param heatCoolMinDelta
		 *            the minimum temperature difference between the heat and cool values. Used to ensure that when
		 *            thermostat is in auto mode, the heat and cool values are separated by at least this value.
		 */
		@JsonProperty("heatCoolMinDelta")
		public void setHeatCoolMinDelta(Temperature heatCoolMinDelta) {
			this.heatCoolMinDelta = heatCoolMinDelta;
		}

		/**
		 * @return the tempCorrection
		 */
		@JsonProperty("tempCorrection")
		public Temperature getTempCorrection() {
			return this.tempCorrection;
		}

		/**
		 * @param tempCorrection
		 *            the tempCorrection to set
		 */
		@JsonProperty("tempCorrection")
		public void setTempCorrection(Temperature tempCorrection) {
			this.tempCorrection = tempCorrection;
		}

		/**
		 * @return the default end time setting the thermostat applies to user temperature holds. Values
		 *         useEndTime4hour, useEndTime2hour (EMS Only), nextPeriod, indefinite, askMe
		 */
		@JsonProperty("holdAction")
		public String getHoldAction() {
			return this.holdAction;
		}

		/**
		 * @param holdAction
		 *            the default end time setting the thermostat applies to user temperature holds. Values
		 *            useEndTime4hour, useEndTime2hour (EMS Only), nextPeriod, indefinite, askMe
		 */
		@JsonProperty("holdAction")
		public void setHoldAction(String holdAction) {
			this.holdAction = holdAction;
		}

		/**
		 * @return the heatPumpGroundWater
		 */
		@JsonProperty("heatPumpGroundWater")
		public Boolean getHeatPumpGroundWater() {
			return this.heatPumpGroundWater;
		}

		/**
		 * @return whether the thermostat is connected to an electric HVAC system
		 */
		@JsonProperty("hasElectric")
		public Boolean getHasElectric() {
			return this.hasElectric;
		}

		/**
		 * @return whether the thermostat is connected to a dehumidifier
		 */
		@JsonProperty("hasDehumidifier")
		public Boolean getHasDehumidifier() {
			return this.hasDehumidifier;
		}

		/**
		 * @return the dehumidifier mode. Values: on, off.
		 */
		@JsonProperty("dehumidifierMode")
		public String getDehumidifierMode() {
			return this.dehumidifierMode;
		}

		/**
		 * @param dehumidifierMode
		 *            the dehumidifier mode. Values: on, off.
		 */
		@JsonProperty("dehumidifierMode")
		public void setDehumidifierMode(String dehumidifierMode) {
			this.dehumidifierMode = dehumidifierMode;
		}

		/**
		 * @return the dehumidification set point in percentage
		 */
		@JsonProperty("dehumidifierLevel")
		public Integer getDehumidifierLevel() {
			return this.dehumidifierLevel;
		}

		/**
		 * @param dehumidifierLevel
		 *            the dehumidification set point in percentage
		 */
		@JsonProperty("dehumidifierLevel")
		public void setDehumidifierLevel(Integer dehumidifierLevel) {
			this.dehumidifierLevel = dehumidifierLevel;
		}

		/**
		 * @return whether the thermostat should use AC overcool to dehumidify.
		 */
		@JsonProperty("dehumidifyWithAC")
		public Boolean getDehumidifyWithAC() {
			return this.dehumidifyWithAC;
		}

		/**
		 * @param dehumidifyWithAC
		 *            whether the thermostat should use AC overcool to dehumidify. When set to true a positive integer
		 *            value must be supplied for dehumidifyOvercoolOffset otherwise an API validation exception will be
		 *            thrown. TODO implement constraint here (@watou).
		 */
		@JsonProperty("dehumidifyWithAC")
		public void setDehumidifyWithAC(Boolean dehumidifyWithAC) {
			this.dehumidifyWithAC = dehumidifyWithAC;
		}

		/**
		 * @return whether the thermostat should use AC overcool to dehumidify and what that temperature offset should
		 *         be. A value of 0 means this feature is disabled and dehumidifyWithAC will be set to false. Value
		 *         represents the value in F to subtract from the current set point.
		 */
		@JsonProperty("dehumidifyOvercoolOffset")
		public Integer getDehumidifyOvercoolOffset() {
			return this.dehumidifyOvercoolOffset;
		}

		/**
		 * @param dehumidifyOvercoolOffset
		 *            whether the thermostat should use AC overcool to dehumidify and what that temperature offset
		 *            should be. A value of 0 means this feature is disabled and dehumidifyWithAC will be set to false.
		 *            Value represents the value in F to subtract from the current set point. Values should be in the
		 *            range 0 - 50 and be divisible by 5.
		 */
		@JsonProperty("dehumidifyOvercoolOffset")
		public void setDehumidifyOvercoolOffset(Integer dehumidifyOvercoolOffset) {
			this.dehumidifyOvercoolOffset = dehumidifyOvercoolOffset;
		}

		/**
		 * @return the autoHeatCoolFeatureEnabled
		 */
		@JsonProperty("autoHeatCoolFeatureEnabled")
		public Boolean getAutoHeatCoolFeatureEnabled() {
			return this.autoHeatCoolFeatureEnabled;
		}

		/**
		 * @param autoHeatCoolFeatureEnabled
		 *            the autoHeatCoolFeatureEnabled to set
		 */
		@JsonProperty("autoHeatCoolFeatureEnabled")
		public void setAutoHeatCoolFeatureEnabled(Boolean autoHeatCoolFeatureEnabled) {
			this.autoHeatCoolFeatureEnabled = autoHeatCoolFeatureEnabled;
		}

		/**
		 * @return the wifiOfflineAlert
		 */
		@JsonProperty("wifiOfflineAlert")
		public Boolean getWifiOfflineAlert() {
			return this.wifiOfflineAlert;
		}

		/**
		 * @param wifiOfflineAlert
		 *            the wifiOfflineAlert to set
		 */
		@JsonProperty("wifiOfflineAlert")
		public void setWifiOfflineAlert(Boolean wifiOfflineAlert) {
			this.wifiOfflineAlert = wifiOfflineAlert;
		}

		/**
		 * @return the minimum heat set point allowed by the thermostat firmware
		 */
		@JsonProperty("heatMinTemp")
		public Temperature getHeatMinTemp() {
			return this.heatMinTemp;
		}

		/**
		 * @return the maximum heat set point allowed by the thermostat firmware
		 */
		@JsonProperty("heatMaxTemp")
		public Temperature getHeatMaxTemp() {
			return this.heatMaxTemp;
		}

		/**
		 * @return the minimum cool set point allowed by the thermostat firmware
		 */
		@JsonProperty("coolMinTemp")
		public Temperature getCoolMinTemp() {
			return this.coolMinTemp;
		}

		/**
		 * @return the maximum cool set point allowed by the thermostat firmware
		 */
		@JsonProperty("coolMaxTemp")
		public Temperature getCoolMaxTemp() {
			return this.coolMaxTemp;
		}

		/**
		 * @return the maximum heat set point configured by the user's preferences
		 */
		@JsonProperty("heatRangeHigh")
		public Temperature getHeatRangeHigh() {
			return this.heatRangeHigh;
		}

		/**
		 * @param heatRangeHigh
		 *            the maximum heat set point configured by the user's preferences
		 */
		@JsonProperty("heatRangeHigh")
		public void setHeatRangeHigh(Temperature heatRangeHigh) {
			this.heatRangeHigh = heatRangeHigh;
		}

		/**
		 * @return the minimum heat set point configured by the user's preferences
		 */
		@JsonProperty("heatRangeLow")
		public Temperature getHeatRangeLow() {
			return this.heatRangeLow;
		}

		/**
		 * @param heatRangeLow
		 *            the minimum heat set point configured by the user's preferences
		 */
		@JsonProperty("heatRangeLow")
		public void setHeatRangeLow(Temperature heatRangeLow) {
			this.heatRangeLow = heatRangeLow;
		}

		/**
		 * @return the maximum cool set point configured by the user's preferences
		 */
		@JsonProperty("coolRangeHigh")
		public Temperature getCoolRangeHigh() {
			return this.coolRangeHigh;
		}

		/**
		 * @param coolRangeHigh
		 *            the maximum cool set point configured by the user's preferences
		 */
		@JsonProperty("coolRangeHigh")
		public void setCoolRangeHigh(Temperature coolRangeHigh) {
			this.coolRangeHigh = coolRangeHigh;
		}

		/**
		 * @return the minimum heat set point configured by the user's preferences
		 */
		@JsonProperty("coolRangeLow")
		public Temperature getCoolRangeLow() {
			return this.coolRangeLow;
		}

		/**
		 * @param coolRangeLow
		 *            the minimum heat set point configured by the user's preferences
		 */
		@JsonProperty("coolRangeLow")
		public void setCoolRangeLow(Temperature coolRangeLow) {
			this.coolRangeLow = coolRangeLow;
		}

		/**
		 * @return the userAccessCode
		 */
		@JsonProperty("userAccessCode")
		public String getUserAccessCode() {
			return this.userAccessCode;
		}

		/**
		 * @param userAccessCode
		 *            the userAccessCode to set
		 */
		@JsonProperty("userAccessCode")
		public void setUserAccessCode(String userAccessCode) {
			this.userAccessCode = userAccessCode;
		}

		/**
		 * @return the userAccessSetting
		 */
		@JsonProperty("userAccessSetting")
		public Integer getUserAccessSetting() {
			return this.userAccessSetting;
		}

		/**
		 * @param userAccessSetting
		 *            the userAccessSetting to set
		 */
		@JsonProperty("userAccessSetting")
		public void setUserAccessSetting(Integer userAccessSetting) {
			this.userAccessSetting = userAccessSetting;
		}

		/**
		 * @return the auxRuntimeAlert
		 */
		@JsonProperty("auxRuntimeAlert")
		public Temperature getAuxRuntimeAlert() {
			return this.auxRuntimeAlert;
		}

		/**
		 * @param auxRuntimeAlert
		 *            the auxRuntimeAlert to set
		 */
		@JsonProperty("auxRuntimeAlert")
		public void setAuxRuntimeAlert(Temperature auxRuntimeAlert) {
			this.auxRuntimeAlert = auxRuntimeAlert;
		}

		/**
		 * @return the auxOutdoorTempAlert
		 */
		@JsonProperty("auxOutdoorTempAlert")
		public Temperature getAuxOutdoorTempAlert() {
			return this.auxOutdoorTempAlert;
		}

		/**
		 * @param auxOutdoorTempAlert
		 *            the auxOutdoorTempAlert to set
		 */
		@JsonProperty("auxOutdoorTempAlert")
		public void setAuxOutdoorTempAlert(Temperature auxOutdoorTempAlert) {
			this.auxOutdoorTempAlert = auxOutdoorTempAlert;
		}

		/**
		 * @return the auxMaxOutdoorTemp
		 */
		@JsonProperty("auxMaxOutdoorTemp")
		public Temperature getAuxMaxOutdoorTemp() {
			return this.auxMaxOutdoorTemp;
		}

		/**
		 * @param auxMaxOutdoorTemp
		 *            the auxMaxOutdoorTemp to set
		 */
		@JsonProperty("auxMaxOutdoorTemp")
		public void setAuxMaxOutdoorTemp(Temperature auxMaxOutdoorTemp) {
			this.auxMaxOutdoorTemp = auxMaxOutdoorTemp;
		}

		/**
		 * @return the auxRuntimeAlertNotify
		 */
		@JsonProperty("auxRuntimeAlertNotify")
		public Boolean getAuxRuntimeAlertNotify() {
			return this.auxRuntimeAlertNotify;
		}

		/**
		 * @param auxRuntimeAlertNotify
		 *            the auxRuntimeAlertNotify to set
		 */
		@JsonProperty("auxRuntimeAlertNotify")
		public void setAuxRuntimeAlertNotify(Boolean auxRuntimeAlertNotify) {
			this.auxRuntimeAlertNotify = auxRuntimeAlertNotify;
		}

		/**
		 * @return the auxOutdoorTempAlertNotify
		 */
		@JsonProperty("auxOutdoorTempAlertNotify")
		public Boolean getAuxOutdoorTempAlertNotify() {
			return this.auxOutdoorTempAlertNotify;
		}

		/**
		 * @param auxOutdoorTempAlertNotify
		 *            the auxOutdoorTempAlertNotify to set
		 */
		@JsonProperty("auxOutdoorTempAlertNotify")
		public void setAuxOutdoorTempAlertNotify(Boolean auxOutdoorTempAlertNotify) {
			this.auxOutdoorTempAlertNotify = auxOutdoorTempAlertNotify;
		}

		/**
		 * @return the auxRuntimeAlertNotifyTechnician
		 */
		@JsonProperty("auxRuntimeAlertNotifyTechnician")
		public Boolean getAuxRuntimeAlertNotifyTechnician() {
			return this.auxRuntimeAlertNotifyTechnician;
		}

		/**
		 * @param auxRuntimeAlertNotifyTechnician
		 *            the auxRuntimeAlertNotifyTechnician to set
		 */
		@JsonProperty("auxRuntimeAlertNotifyTechnician")
		public void setAuxRuntimeAlertNotifyTechnician(Boolean auxRuntimeAlertNotifyTechnician) {
			this.auxRuntimeAlertNotifyTechnician = auxRuntimeAlertNotifyTechnician;
		}

		/**
		 * @return the auxOutdoorTempAlertNotifyTechnician
		 */
		@JsonProperty("auxOutdoorTempAlertNotifyTechnician")
		public Boolean getAuxOutdoorTempAlertNotifyTechnician() {
			return this.auxOutdoorTempAlertNotifyTechnician;
		}

		/**
		 * @param auxOutdoorTempAlertNotifyTechnician
		 *            the auxOutdoorTempAlertNotifyTechnician to set
		 */
		@JsonProperty("auxOutdoorTempAlertNotifyTechnician")
		public void setAuxOutdoorTempAlertNotifyTechnician(Boolean auxOutdoorTempAlertNotifyTechnician) {
			this.auxOutdoorTempAlertNotifyTechnician = auxOutdoorTempAlertNotifyTechnician;
		}

		/**
		 * @return whether the thermostat should use pre heating to reach the set point on time
		 */
		@JsonProperty("disablePreHeating")
		public Boolean getDisablePreHeating() {
			return this.disablePreHeating;
		}

		/**
		 * @param disablePreHeating
		 *            whether the thermostat should use pre heating to reach the set point on time
		 */
		@JsonProperty("disablePreHeating")
		public void setDisablePreHeating(Boolean disablePreHeating) {
			this.disablePreHeating = disablePreHeating;
		}

		/**
		 * @return whether the thermostat should use pre cooling to reach the set point on time
		 */
		@JsonProperty("disablePreCooling")
		public Boolean getDisablePreCooling() {
			return this.disablePreCooling;
		}

		/**
		 * @param disablePreCooling
		 *            whether the thermostat should use pre cooling to reach the set point on time
		 */
		@JsonProperty("disablePreCooling")
		public void setDisablePreCooling(Boolean disablePreCooling) {
			this.disablePreCooling = disablePreCooling;
		}

		/**
		 * @return whether an installer code is required
		 */
		@JsonProperty("installerCodeRequired")
		public Boolean getInstallerCodeRequired() {
			return this.installerCodeRequired;
		}

		/**
		 * @param installerCodeRequired
		 *            whether an installer code is required
		 */
		@JsonProperty("installerCodeRequired")
		public void setInstallerCodeRequired(Boolean installerCodeRequired) {
			this.installerCodeRequired = installerCodeRequired;
		}

		/**
		 * @return whether Demand Response requests are accepted by this thermostat. Possible values are: always, askMe,
		 *         customerSelect, defaultAccept, defaultDecline, never.
		 */
		@JsonProperty("drAccept")
		public String getDrAccept() {
			return this.drAccept;
		}

		/**
		 * @param drAccept
		 *            whether Demand Response requests are accepted by this thermostat. Possible values are: always,
		 *            askMe, customerSelect, defaultAccept, defaultDecline, never.
		 */
		@JsonProperty("drAccept")
		public void setDrAccept(String drAccept) {
			this.drAccept = drAccept;
		}

		/**
		 * @return whether the property is a rental or not
		 */
		@JsonProperty("isRentalProperty")
		public Boolean getIsRentalProperty() {
			return this.isRentalProperty;
		}

		/**
		 * @param isRentalProperty
		 *            whether the property is a rental or not
		 */
		@JsonProperty("isRentalProperty")
		public void setIsRentalProperty(Boolean isRentalProperty) {
			this.isRentalProperty = isRentalProperty;
		}

		/**
		 * @return whether to use a zone controller or not
		 */
		@JsonProperty("useZoneController")
		public Boolean getUseZoneController() {
			return this.useZoneController;
		}

		/**
		 * @param useZoneController
		 *            whether to use a zone controller or not
		 */
		@JsonProperty("useZoneController")
		public void setUseZoneController(Boolean useZoneController) {
			this.useZoneController = useZoneController;
		}

		/**
		 * @return whether random start delay is enabled for cooling
		 */
		@JsonProperty("randomStartDelayCool")
		public Integer getRandomStartDelayCool() {
			return this.randomStartDelayCool;
		}

		/**
		 * @param randomStartDelayCool
		 *            whether random start delay is enabled for cooling
		 */
		@JsonProperty("randomStartDelayCool")
		public void setRandomStartDelayCool(Integer randomStartDelayCool) {
			this.randomStartDelayCool = randomStartDelayCool;
		}

		/**
		 * @return whether random start delay is enabled for heating
		 */
		@JsonProperty("randomStartDelayHeat")
		public Integer getRandomStartDelayHeat() {
			return this.randomStartDelayHeat;
		}

		/**
		 * @param randomStartDelayHeat
		 *            whether random start delay is enabled for heating
		 */
		@JsonProperty("randomStartDelayHeat")
		public void setRandomStartDelayHeat(Integer randomStartDelayHeat) {
			this.randomStartDelayHeat = randomStartDelayHeat;
		}

		/**
		 * @return the humidity level to trigger a high humidity alert
		 */
		@JsonProperty("humidityHighAlert")
		public Integer getHumidityHighAlert() {
			return this.humidityHighAlert;
		}

		/**
		 * @param humidityHighAlert
		 *            the humidity level to trigger a high humidity alert
		 */
		@JsonProperty("humidityHighAlert")
		public void setHumidityHighAlert(Integer humidityHighAlert) {
			this.humidityHighAlert = humidityHighAlert;
		}

		/**
		 * @return the humidity level to trigger a low humidity alert
		 */
		@JsonProperty("humidityLowAlert")
		public Integer getHumidityLowAlert() {
			return this.humidityLowAlert;
		}

		/**
		 * @param humidityLowAlert
		 *            the humidity level to trigger a low humidity alert
		 */
		@JsonProperty("humidityLowAlert")
		public void setHumidityLowAlert(Integer humidityLowAlert) {
			this.humidityLowAlert = humidityLowAlert;
		}

		/**
		 * @return whether heat pump alerts are disabled
		 */
		@JsonProperty("disableHeatPumpAlerts")
		public Boolean getDisableHeatPumpAlerts() {
			return this.disableHeatPumpAlerts;
		}

		/**
		 * @param disableHeatPumpAlerts
		 *            whether heat pump alerts are disabled
		 */
		@JsonProperty("disableHeatPumpAlerts")
		public void setDisableHeatPumpAlerts(Boolean disableHeatPumpAlerts) {
			this.disableHeatPumpAlerts = disableHeatPumpAlerts;
		}

		/**
		 * @return whether alerts are disabled from showing on the thermostat
		 */
		@JsonProperty("disableAlertsOnIdt")
		public Boolean getDisableAlertsOnIdt() {
			return this.disableAlertsOnIdt;
		}

		/**
		 * @param disableAlertsOnIdt
		 *            whether alerts are disabled from showing on the thermostat
		 */
		@JsonProperty("disableAlertsOnIdt")
		public void setDisableAlertsOnIdt(Boolean disableAlertsOnIdt) {
			this.disableAlertsOnIdt = disableAlertsOnIdt;
		}

		/**
		 * @return whether humidification alerts are enabled to the thermostat owner
		 */
		@JsonProperty("humidityAlertNotify")
		public Boolean getHumidityAlertNotify() {
			return this.humidityAlertNotify;
		}

		/**
		 * @param humidityAlertNotify
		 *            whether humidification alerts are enabled to the thermostat owner
		 */
		@JsonProperty("humidityAlertNotify")
		public void setHumidityAlertNotify(Boolean humidityAlertNotify) {
			this.humidityAlertNotify = humidityAlertNotify;
		}

		/**
		 * @return whether humidification alerts are enabled to the technician associated with the thermostat
		 */
		@JsonProperty("humidityAlertNotifyTechnician")
		public Boolean getHumidityAlertNotifyTechnician() {
			return this.humidityAlertNotifyTechnician;
		}

		/**
		 * @param humidityAlertNotifyTechnician
		 *            whether humidification alerts are enabled to the technician associated with the thermostat
		 */
		@JsonProperty("humidityAlertNotifyTechnician")
		public void setHumidityAlertNotifyTechnician(Boolean humidityAlertNotifyTechnician) {
			this.humidityAlertNotifyTechnician = humidityAlertNotifyTechnician;
		}

		/**
		 * @return whether temperature alerts are enabled to the thermostat owner
		 */
		@JsonProperty("tempAlertNotify")
		public Boolean getTempAlertNotify() {
			return this.tempAlertNotify;
		}

		/**
		 * @param tempAlertNotify
		 *            whether temperature alerts are enabled to the thermostat owner
		 */
		@JsonProperty("tempAlertNotify")
		public void setTempAlertNotify(Boolean tempAlertNotify) {
			this.tempAlertNotify = tempAlertNotify;
		}

		/**
		 * @return hether temperature alerts are enabled to the technician associated with the thermostat
		 */
		@JsonProperty("tempAlertNotifyTechnician")
		public Boolean getTempAlertNotifyTechnician() {
			return this.tempAlertNotifyTechnician;
		}

		/**
		 * @param tempAlertNotifyTechnician
		 *            hether temperature alerts are enabled to the technician associated with the thermostat
		 */
		@JsonProperty("tempAlertNotifyTechnician")
		public void setTempAlertNotifyTechnician(Boolean tempAlertNotifyTechnician) {
			this.tempAlertNotifyTechnician = tempAlertNotifyTechnician;
		}

		/**
		 * @return the dollar amount the owner specifies for their desired maximum electricity bill
		 */
		@JsonProperty("monthlyElectricityBillLimit")
		public Integer getMonthlyElectricityBillLimit() {
			return this.monthlyElectricityBillLimit;
		}

		/**
		 * @param monthlyElectricityBillLimit
		 *            the dollar amount the owner specifies for their desired maximum electricity bill
		 */
		@JsonProperty("monthlyElectricityBillLimit")
		public void setMonthlyElectricityBillLimit(Integer monthlyElectricityBillLimit) {
			this.monthlyElectricityBillLimit = monthlyElectricityBillLimit;
		}

		/**
		 * @return whether electricity bill alerts are enabled
		 */
		@JsonProperty("enableElectricityBillAlert")
		public Boolean getEnableElectricityBillAlert() {
			return this.enableElectricityBillAlert;
		}

		/**
		 * @param enableElectricityBillAlert
		 *            whether electricity bill alerts are enabled
		 */
		@JsonProperty("enableElectricityBillAlert")
		public void setEnableElectricityBillAlert(Boolean enableElectricityBillAlert) {
			this.enableElectricityBillAlert = enableElectricityBillAlert;
		}

		/**
		 * @return whether electricity bill projection alerts are enabled
		 */
		@JsonProperty("enableProjectedElectricityBillAlert")
		public Boolean getEnableProjectedElectricityBillAlert() {
			return this.enableProjectedElectricityBillAlert;
		}

		/**
		 * @param enableProjectedElectricityBillAlert
		 *            whether electricity bill projection alerts are enabled
		 */
		@JsonProperty("enableProjectedElectricityBillAlert")
		public void setEnableProjectedElectricityBillAlert(Boolean enableProjectedElectricityBillAlert) {
			this.enableProjectedElectricityBillAlert = enableProjectedElectricityBillAlert;
		}

		/**
		 * @return the electricityBillingDayOfMonth
		 */
		@JsonProperty("electricityBillingDayOfMonth")
		public Integer getElectricityBillingDayOfMonth() {
			return this.electricityBillingDayOfMonth;
		}

		/**
		 * @param electricityBillingDayOfMonth
		 *            the electricityBillingDayOfMonth to set
		 */
		@JsonProperty("electricityBillingDayOfMonth")
		public void setElectricityBillingDayOfMonth(Integer electricityBillingDayOfMonth) {
			this.electricityBillingDayOfMonth = electricityBillingDayOfMonth;
		}

		/**
		 * @return the owner's billing cycle duration in months
		 */
		@JsonProperty("electricityBillCycleMonths")
		public Integer getElectricityBillCycleMonths() {
			return this.electricityBillCycleMonths;
		}

		/**
		 * @param electricityBillCycleMonths
		 *            the owner's billing cycle duration in months
		 */
		@JsonProperty("electricityBillCycleMonths")
		public void setElectricityBillCycleMonths(Integer electricityBillCycleMonths) {
			this.electricityBillCycleMonths = electricityBillCycleMonths;
		}

		/**
		 * @return the annual start month of the owner's billing cycle
		 */
		@JsonProperty("electricityBillStartMonth")
		public Integer getElectricityBillStartMonth() {
			return this.electricityBillStartMonth;
		}

		/**
		 * @param electricityBillStartMonth
		 *            the annual start month of the owner's billing cycle
		 */
		@JsonProperty("electricityBillStartMonth")
		public void setElectricityBillStartMonth(Integer electricityBillStartMonth) {
			this.electricityBillStartMonth = electricityBillStartMonth;
		}

		/**
		 * @return the number of minutes to run ventilator per hour when home
		 */
		@JsonProperty("ventilatorMinOnTimeHome")
		public Integer getVentilatorMinOnTimeHome() {
			return this.ventilatorMinOnTimeHome;
		}

		/**
		 * @param ventilatorMinOnTimeHome
		 *            the number of minutes to run ventilator per hour when home
		 */
		@JsonProperty("ventilatorMinOnTimeHome")
		public void setVentilatorMinOnTimeHome(Integer ventilatorMinOnTimeHome) {
			this.ventilatorMinOnTimeHome = ventilatorMinOnTimeHome;
		}

		/**
		 * @return the number of minutes to run ventilator per hour when away
		 */
		@JsonProperty("ventilatorMinOnTimeAway")
		public Integer getVentilatorMinOnTimeAway() {
			return this.ventilatorMinOnTimeAway;
		}

		/**
		 * @param ventilatorMinOnTimeAway
		 *            the number of minutes to run ventilator per hour when away
		 */
		@JsonProperty("ventilatorMinOnTimeAway")
		public void setVentilatorMinOnTimeAway(Integer ventilatorMinOnTimeAway) {
			this.ventilatorMinOnTimeAway = ventilatorMinOnTimeAway;
		}

		/**
		 * @return whether or not to turn the backlight off during sleep
		 */
		@JsonProperty("backlightOffDuringSleep")
		public Boolean getBacklightOffDuringSleep() {
			return this.backlightOffDuringSleep;
		}

		/**
		 * @param backlightOffDuringSleep
		 *            whether or not to turn the backlight off during sleep
		 */
		@JsonProperty("backlightOffDuringSleep")
		public void setBacklightOffDuringSleep(Boolean backlightOffDuringSleep) {
			this.backlightOffDuringSleep = backlightOffDuringSleep;
		}

		/**
		 * @return when set to true if no occupancy motion detected thermostat will go into indefinite away hold, until
		 *         either the user presses resume schedule or motion is detected.
		 */
		@JsonProperty("autoAway")
		public Boolean getAutoAway() {
			return this.autoAway;
		}

		/**
		 * @return when set to true if a larger than normal delta is found between sensors the fan will be engaged for
		 *         15min/hour.
		 */
		@JsonProperty("smartCirculation")
		public Boolean getSmartCirculation() {
			return this.smartCirculation;
		}

		/**
		 * @param smartCirculation
		 *            when set to true if a larger than normal delta is found between sensors the fan will be engaged
		 *            for 15min/hour.
		 */
		@JsonProperty("smartCirculation")
		public void setSmartCirculation(Boolean smartCirculation) {
			this.smartCirculation = smartCirculation;
		}

		/**
		 * @return true if a sensor has detected presence for more than 10 minutes then include that sensor in temp
		 *         average. If no activity has been seen on a sensor for more than 1 hour then remove this sensor from
		 *         temperature average.
		 */
		@JsonProperty("followMeComfort")
		public Boolean getFollowMeComfort() {
			return this.followMeComfort;
		}

		/**
		 * @param followMeComfort
		 *            set to true if a sensor has detected presence for more than 10 minutes then include that sensor in
		 *            temp average. If no activity has been seen on a sensor for more than 1 hour then remove this
		 *            sensor from temperature average.
		 */
		@JsonProperty("followMeComfort")
		public void setFollowMeComfort(Boolean followMeComfort) {
			this.followMeComfort = followMeComfort;
		}

		/**
		 * @return the type of ventilator present for the Thermostat. The possible values are none, ventilator, hrv, and
		 *         erv.
		 */
		@JsonProperty("ventilatorType")
		public String getVentilatorType() {
			return this.ventilatorType;
		}

		/**
		 * @return whether the ventilator timer is on or off. The default value is false. If set to true the
		 *         ventilatorOffDateTime is set to now() + 20 minutes. If set to false the ventilatorOffDateTime is set
		 *         to its default value.
		 */
		@JsonProperty("isVentilatorTimerOn")
		public Boolean getIsVentilatorTimerOn() {
			return this.isVentilatorTimerOn;
		}

		/**
		 * @param isVentilatorTimerOn
		 *            whether the ventilator timer is on or off. The default value is false. If set to true the
		 *            ventilatorOffDateTime is set to now() + 20 minutes. If set to false the ventilatorOffDateTime is
		 *            set to its default value.
		 */
		@JsonProperty("isVentilatorTimerOn")
		public void setIsVentilatorTimerOn(Boolean isVentilatorTimerOn) {
			this.isVentilatorTimerOn = isVentilatorTimerOn;
		}

		/**
		 * @return the date and time the ventilator will run until. The default value is 2014-01-01 00:00:00.
		 */
		@JsonProperty("ventilatorOffDateTime")
		public Date getVentilatorOffDateTime() {
			return this.ventilatorOffDateTime;
		}

		/**
		 * @return whether the HVAC system has a UV filter. The default value is true.
		 */
		@JsonProperty("hasUVFilter")
		public Boolean getHasUVFilter() {
			return this.hasUVFilter;
		}

		/**
		 * @param hasUVFilter
		 *            whether the HVAC system has a UV filter. The default value is true.
		 */
		@JsonProperty("hasUVFilter")
		public void setHasUVFilter(Boolean hasUVFilter) {
			this.hasUVFilter = hasUVFilter;
		}

		/**
		 * @return whether to permit the cooling to operate when the outdoor temperature is under a specific threshold,
		 *         currently 55F. The default value is false.
		 */
		@JsonProperty("coolingLockout")
		public Boolean getCoolingLockout() {
			return this.coolingLockout;
		}

		/**
		 * @param coolingLockout
		 *            whether to permit the cooling to operate when the outdoor temperature is under a specific
		 *            threshold, currently 55F. The default value is false.
		 */
		@JsonProperty("coolingLockout")
		public void setCoolingLockout(Boolean coolingLockout) {
			this.coolingLockout = coolingLockout;
		}

		/**
		 * @return the ventilatorFreeCooling
		 */
		@JsonProperty("ventilatorFreeCooling")
		public Boolean getVentilatorFreeCooling() {
			return this.ventilatorFreeCooling;
		}

		/**
		 * @param ventilatorFreeCooling
		 *            the ventilatorFreeCooling to set
		 */
		@JsonProperty("ventilatorFreeCooling")
		public void setVentilatorFreeCooling(Boolean ventilatorFreeCooling) {
			this.ventilatorFreeCooling = ventilatorFreeCooling;
		}

		/**
		 * @return whether to permit dehumidifier to operate when the heating is running. The default value is false.
		 */
		@JsonProperty("dehumidifyWhenHeating")
		public Boolean getDehumidifyWhenHeating() {
			return this.dehumidifyWhenHeating;
		}

		/**
		 * @param dehumidifyWhenHeating
		 *            whether to permit dehumidifier to operate when the heating is running. The default value is false.
		 */
		@JsonProperty("dehumidifyWhenHeating")
		public void setDehumidifyWhenHeating(Boolean dehumidifyWhenHeating) {
			this.dehumidifyWhenHeating = dehumidifyWhenHeating;
		}

		/**
		 * @return the unique reference to the group this thermostat belongs to, if any. See GET Group request and POST
		 *         Group request for more information.
		 */
		@JsonProperty("groupRef")
		public String getGroupRef() {
			return this.groupRef;
		}

		/**
		 * @param groupRef
		 *            the unique reference to the group this thermostat belongs to, if any. See GET Group request and
		 *            POST Group request for more information.
		 */
		@JsonProperty("groupRef")
		public void setGroupRef(String groupRef) {
			this.groupRef = groupRef;
		}

		/**
		 * @return the name of the the group this thermostat belongs to, if any. See GET Group request and POST Group
		 *         request for more information.
		 */
		@JsonProperty("groupName")
		public String getGroupName() {
			return this.groupName;
		}

		/**
		 * @param groupName
		 *            the name of the the group this thermostat belongs to, if any. See GET Group request and POST Group
		 *            request for more information.
		 */
		@JsonProperty("groupName")
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		/**
		 * @return the setting value for the group this thermostat belongs to, if any. See GET Group request and POST
		 *         Group request for more information.
		 */
		@JsonProperty("groupSetting")
		public Integer getGroupSetting() {
			return this.groupSetting;
		}

		/**
		 * @param groupSetting
		 *            the setting value for the group this thermostat belongs to, if any. See GET Group request and POST
		 *            Group request for more information.
		 */
		@JsonProperty("groupSetting")
		public void setGroupSetting(Integer groupSetting) {
			this.groupSetting = groupSetting;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("hvacMode", this.hvacMode);
			builder.append("lastServiceDate", this.lastServiceDate);
			builder.append("serviceRemindMe", this.serviceRemindMe);
			builder.append("monthsBetweenService", this.monthsBetweenService);
			builder.append("remindMeDate", this.remindMeDate);
			builder.append("vent", this.vent);
			builder.append("ventilatorMinOnTime", this.ventilatorMinOnTime);
			builder.append("serviceRemindTechnician", this.serviceRemindTechnician);
			builder.append("eiLocation", this.eiLocation);
			builder.append("coldTempAlert", this.coldTempAlert);
			builder.append("coldTempAlertEnabled", this.coldTempAlertEnabled);
			builder.append("hotTempAlert", this.hotTempAlert);
			builder.append("hotTempAlertEnabled", this.hotTempAlertEnabled);
			builder.append("coolStages", this.coolStages);
			builder.append("heatStages", this.heatStages);
			builder.append("maxSetBack", this.maxSetBack);
			builder.append("maxSetForward", this.maxSetForward);
			builder.append("quickSaveSetBack", this.quickSaveSetBack);
			builder.append("quickSaveSetForward", this.quickSaveSetForward);
			builder.append("hasHeatPump", this.hasHeatPump);
			builder.append("hasForcedAir", this.hasForcedAir);
			builder.append("hasBoiler", this.hasBoiler);
			builder.append("hasHumidifier", this.hasHumidifier);
			builder.append("hasErv", this.hasErv);
			builder.append("hasHrv", this.hasHrv);
			builder.append("condensationAvoid", this.condensationAvoid);
			builder.append("useCelsius", this.useCelsius);
			builder.append("useTimeFormat12", this.useTimeFormat12);
			builder.append("locale", this.locale);
			builder.append("humidity", this.humidity);
			builder.append("humidifierMode", this.humidifierMode);
			builder.append("backlightOnIntensity", this.backlightOnIntensity);
			builder.append("backlightSleepIntensity", this.backlightSleepIntensity);
			builder.append("backlightOffTime", this.backlightOffTime);
			builder.append("soundTickVolume", this.soundTickVolume);
			builder.append("soundAlertVolume", this.soundAlertVolume);
			builder.append("compressorProtectionMinTime", this.compressorProtectionMinTime);
			builder.append("compressorProtectionMinTemp", this.compressorProtectionMinTemp);
			builder.append("stage1HeatingDifferentialTemp", this.stage1HeatingDifferentialTemp);
			builder.append("stage1CoolingDifferentialTemp", this.stage1CoolingDifferentialTemp);
			builder.append("stage1HeatingDissipationTime", this.stage1HeatingDissipationTime);
			builder.append("stage1CoolingDissipationTime", this.stage1CoolingDissipationTime);
			builder.append("heatPumpReversalOnCool", this.heatPumpReversalOnCool);
			builder.append("fanControlRequired", this.fanControlRequired);
			builder.append("fanMinOnTime", this.fanMinOnTime);
			builder.append("heatCoolMinDelta", this.heatCoolMinDelta);
			builder.append("tempCorrection", this.tempCorrection);
			builder.append("holdAction", this.holdAction);
			builder.append("heatPumpGroundWater", this.heatPumpGroundWater);
			builder.append("hasElectric", this.hasElectric);
			builder.append("hasDehumidifier", this.hasDehumidifier);
			builder.append("humidifierMode", this.humidifierMode);
			builder.append("dehumidifierLevel", this.dehumidifierLevel);
			builder.append("dehumidifyWithAC", this.dehumidifyWithAC);
			builder.append("dehumidifyOvercoolOffset", this.dehumidifyOvercoolOffset);
			builder.append("autoHeatCoolFeatureEnabled", this.autoHeatCoolFeatureEnabled);
			builder.append("wifiOfflineAlert", this.wifiOfflineAlert);
			builder.append("heatMinTemp", this.heatMinTemp);
			builder.append("heatMaxTemp", this.heatMaxTemp);
			builder.append("coolMinTemp", this.coolMinTemp);
			builder.append("coolMaxTemp", this.coolMaxTemp);
			builder.append("heatRangeHigh", this.heatRangeHigh);
			builder.append("heatRangeLow", this.heatRangeLow);
			builder.append("coolRangeHigh", this.coolRangeHigh);
			builder.append("coolRangeLow", this.coolRangeLow);
			builder.append("userAccessCode", this.userAccessCode);
			builder.append("userAccessSetting", this.userAccessSetting);
			builder.append("auxRuntimeAlert", this.auxRuntimeAlert);
			builder.append("auxOutdoorTempAlert", this.auxOutdoorTempAlert);
			builder.append("auxMaxOutdoorTemp", this.auxMaxOutdoorTemp);
			builder.append("auxRuntimeAlertNotify", this.auxRuntimeAlertNotify);
			builder.append("auxOutdoorTempAlertNotify", this.auxOutdoorTempAlertNotify);
			builder.append("auxRuntimeAlertNotifyTechnician", this.auxRuntimeAlertNotifyTechnician);
			builder.append("auxOutdoorTempAlertNotifyTechnician", this.auxOutdoorTempAlertNotifyTechnician);
			builder.append("disablePreHeating", this.disablePreHeating);
			builder.append("disablePreCooling", this.disablePreCooling);
			builder.append("installerCodeRequired", this.installerCodeRequired);
			builder.append("drAccept", this.drAccept);
			builder.append("isRentalProperty", this.isRentalProperty);
			builder.append("useZoneController", this.useZoneController);
			builder.append("randomStartDelayCool", this.randomStartDelayCool);
			builder.append("randomStartDelayHeat", this.randomStartDelayHeat);
			builder.append("humidityHighAlert", this.humidityHighAlert);
			builder.append("humidityLowAlert", this.humidityLowAlert);
			builder.append("disableHeatPumpAlerts", this.disableHeatPumpAlerts);
			builder.append("disableAlertsOnIdt", this.disableAlertsOnIdt);
			builder.append("humidityAlertNotify", this.humidityAlertNotify);
			builder.append("humidityAlertNotifyTechnician", this.humidityAlertNotifyTechnician);
			builder.append("tempAlertNotify", this.tempAlertNotify);
			builder.append("tempAlertNotifyTechnician", this.tempAlertNotifyTechnician);
			builder.append("monthlyElectricityBillLimit", this.monthlyElectricityBillLimit);
			builder.append("enableElectricityBillAlert", this.enableElectricityBillAlert);
			builder.append("enableProjectedElectricityBillAlert", this.enableProjectedElectricityBillAlert);
			builder.append("electricityBillingDayOfMonth", this.electricityBillingDayOfMonth);
			builder.append("electricityBillCycleMonths", this.electricityBillCycleMonths);
			builder.append("electricityBillStartMonth", this.electricityBillStartMonth);
			builder.append("ventilatorMinOnTimeHome", this.ventilatorMinOnTimeHome);
			builder.append("ventilatorMinOnTimeAway", this.ventilatorMinOnTimeAway);
			builder.append("backlightOffDuringSleep", this.backlightOffDuringSleep);
			builder.append("autoAway", this.autoAway);
			builder.append("smartCirculation", this.smartCirculation);
			builder.append("followMeComfort", this.followMeComfort);
			builder.append("ventilatorType", this.ventilatorType);
			builder.append("isVentilatorTimerOn", this.isVentilatorTimerOn);
			builder.append("ventilatorOffDateTime", this.ventilatorOffDateTime);
			builder.append("hasUVFilter", this.hasUVFilter);
			builder.append("coolingLockout", this.coolingLockout);
			builder.append("ventilatorFreeCooling", this.ventilatorFreeCooling);
			builder.append("dehumidifyWhenHeating", this.dehumidifyWhenHeating);
			builder.append("groupRef", this.groupRef);
			builder.append("groupName", this.groupName);
			builder.append("groupSetting", this.groupSetting);

			return builder.toString();
		}
	}

	/**
	 * Possible values for hvacMode
	 */
	public static enum HvacMode {
		AUTO("auto"), AUX_HEAT_ONLY("auxHeatOnly"), COOL("cool"), HEAT("heat"), OFF("off");

		private final String mode;

		private HvacMode(String mode) {
			this.mode = mode;
		}

		@JsonValue
		public String value() {
			return mode;
		}

		@JsonCreator
		public static HvacMode forValue(String v) {
			for (HvacMode hm : HvacMode.values()) {
				if (hm.mode.equals(v)) {
					return hm;
				}
			}
			throw new IllegalArgumentException("Invalid hvacMode: " + v);
		}

		@Override
		public String toString() {
			return this.mode;
		}
	}

	/**
	 * Possible values for vent
	 */
	public static enum VentilatorMode {
		AUTO("auto"), MIN_ON_TIME("minontime"), ON("on"), OFF("off");

		private final String mode;

		private VentilatorMode(String mode) {
			this.mode = mode;
		}

		@JsonValue
		public String value() {
			return mode;
		}

		@JsonCreator
		public static VentilatorMode forValue(String v) {
			for (VentilatorMode vm : VentilatorMode.values()) {
				if (vm.mode.equals(v)) {
					return vm;
				}
			}
			throw new IllegalArgumentException("Invalid vent: " + v);
		}

		@Override
		public String toString() {
			return this.mode;
		}
	}

	/**
	 * The Runtime Java Bean represents the last known thermostat running state. This state is composed from the last
	 * interval status message received from a thermostat. It is also updated each time the thermostat posts
	 * configuration changes to the server.
	 * 
	 * <p>
	 * The runtime object contains the last 5 minute interval value sent by the thermostat for the past 15 minutes of
	 * runtime. The thermostat updates the server every 15 minutes with the last three 5 minute readings.
	 * 
	 * <p>
	 * The actual temperature and humidity will also be updated when the equipment state changes by the thermostat, this
	 * may occur at a frequency of 3 minutes, however it is only transmitted when there is an equipment state change on
	 * the thermostat.
	 * 
	 * <p>
	 * See Thermostat Interval Report Data for additional information about the interval readings.
	 * 
	 * <p>
	 * The Runtime class is read-only.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Runtime.shtml">Runtime</a>
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/technical-notes.shtml#data">Thermostat
	 *      Interval Report Data</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Runtime extends AbstractMessagePart {
		private String runtimeRev;
		private Boolean connected;
		private Date firstConnected;
		private Date connectDateTime;
		private String disconnectDateTime; // TODO: Jackson 1.9 can't handle
											// date only (no time) (@watou)
		private Date lastModified;
		private Date lastStatusModified;
		private String runtimeDate; // TODO: Jackson 1.9 can't handle date only
									// (no time) (@watou)
		private Integer runtimeInterval;
		private Temperature actualTemperature;
		private Integer actualHumidity;
		private Temperature desiredHeat;
		private Temperature desiredCool;
		private Integer desiredHumidity;
		private Integer desiredDehumidity;
		private String desiredFanMode;

		/**
		 * @return the current runtime revision. Equivalent in meaning to the runtime revision number in the thermostat
		 *         summary call.
		 */
		@JsonProperty("runtimeRev")
		public String getRuntimeRev() {
			return this.runtimeRev;
		}

		/**
		 * @return whether the thermostat is currently connected to the server
		 */
		@JsonProperty("connected")
		public Boolean getConnected() {
			return this.connected;
		}

		/**
		 * @return the UTC date/time stamp of when the thermostat first connected to the ecobee server
		 */
		@JsonProperty("firstConnected")
		public Date getFirstConnected() {
			return this.firstConnected;
		}

		/**
		 * @return the last recorded connection date and time
		 */
		@JsonProperty("connectDateTime")
		public Date getConnectDateTime() {
			return this.connectDateTime;
		}

		/**
		 * @return the last recorded disconnection date and time
		 */
		@JsonProperty("disconnectDateTime")
		public String getDisconnectDateTime() {
			return this.disconnectDateTime;
		}

		/**
		 * @return the UTC date/time stamp of when the thermostat was updated. Format: YYYY-MM-DD HH:MM:SS
		 */
		@JsonProperty("lastModified")
		public Date getLastModified() {
			return this.lastModified;
		}

		/**
		 * @return the UTC date/time stamp of when the thermostat last posted its runtime information. Format:
		 *         YYYY-MM-DD HH:MM:SS
		 */
		@JsonProperty("lastStatusModified")
		public Date getLastStatusModified() {
			return this.lastStatusModified;
		}

		/**
		 * @return the UTC date of the last runtime reading. Format: YYYY-MM-DD
		 */
		@JsonProperty("runtimeDate")
		public String getRuntimeDate() {
			return this.runtimeDate;
		}

		/**
		 * @return the last 5 minute interval which was updated by the thermostat telemetry update. Subtract 2 from this
		 *         interval to obtain the beginning interval for the last 3 readings. Multiply by 5 mins to obtain the
		 *         minutes of the day. Range: 0-287
		 */
		@JsonProperty("runtimeInterval")
		public Integer getRuntimeInterval() {
			return this.runtimeInterval;
		}

		/**
		 * @return the current temperature displayed on the thermostat
		 */
		@JsonProperty("actualTemperature")
		public Temperature getActualTemperature() {
			return this.actualTemperature;
		}

		/**
		 * @return the current humidity % shown on the thermostat
		 */
		@JsonProperty("actualHumidity")
		public Integer getActualHumidity() {
			return this.actualHumidity;
		}

		/**
		 * @return the desired heat temperature as per the current running program or active event
		 */
		@JsonProperty("desiredHeat")
		public Temperature getDesiredHeat() {
			return this.desiredHeat;
		}

		/**
		 * @return the desired cool temperature as per the current running program or active event.
		 */
		@JsonProperty("desiredCool")
		public Temperature getDesiredCool() {
			return this.desiredCool;
		}

		/**
		 * @return the desired humidity set point
		 */
		@JsonProperty("desiredHumidity")
		public Integer getDesiredHumidity() {
			return this.desiredHumidity;
		}

		/**
		 * @return the desired dehumidification set point
		 */
		@JsonProperty("desiredDehumidity")
		public Integer getDesiredDehumidity() {
			return this.desiredDehumidity;
		}

		/**
		 * @return the desired fan mode. Values: auto, on or null if the HVAC system is off. TODO handle null as valid
		 *         value (@watou).
		 */
		@JsonProperty("desiredFanMode")
		public String getDesiredFanMode() {
			return this.desiredFanMode;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("runtimeRev", this.runtimeRev);
			builder.append("connected", this.connected);
			builder.append("firstConnected", this.firstConnected);
			builder.append("connectDateTime", this.connectDateTime);
			builder.append("disconnectDateTime", this.disconnectDateTime);
			builder.append("lastModified", this.lastModified);
			builder.append("lastStatusModified", this.lastStatusModified);
			builder.append("runtimeDate", this.runtimeDate);
			builder.append("runtimeInterval", this.runtimeInterval);
			builder.append("actualTemperature", this.actualTemperature);
			builder.append("actualHumidity", this.actualHumidity);
			builder.append("desiredHeat", this.desiredHeat);
			builder.append("desiredCool", this.desiredCool);
			builder.append("desiredHumidity", this.desiredHumidity);
			builder.append("desiredDehumidity", this.desiredDehumidity);
			builder.append("desiredFanMode", this.desiredFanMode);

			return builder.toString();
		}
	}

	/**
	 * The ExtendedRuntime Java Bean contains the last three 5 minute interval values sent by the thermostat for the
	 * past 15 minutes of runtime. The interval values are valuable when you are interested in analyzing the runtime
	 * data in a more granular fashion, at 5 minute increments rather than the more general 15 minute value from the
	 * Runtime Object.
	 * 
	 * <p>
	 * For the runtime values (i.e. heatPump, auxHeat, cool, etc.) refer to the {@link Thermostat#settings} values (
	 * {@link Settings#hasHeatPump}, {@link Settings#heatStages}, {@link Settings#coolStages}) to determine whether a
	 * heat pump exists and how many stages the thermostat supports.
	 * 
	 * <p>
	 * The actual temperature and humidity will also be updated when the equipment state changes by the thermostat, this
	 * may occur at a frequency of 3 minutes, however it is only transmitted when there is an equipment state change on
	 * the thermostat.
	 * 
	 * <p>
	 * The extended runtime object is read-only.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/ExtendedRuntime.shtml">ExtendedRuntime</a>
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/technical-notes.shtml#data">Thermostat
	 *      Interval Report Data</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ExtendedRuntime extends AbstractMessagePart {
		private Date lastReadingTimestamp;
		private String runtimeDate; // TODO Jackson 1.9 can't also deal with date-only dates (@watou)
		private Integer runtimeInterval;
		private List<Temperature> actualTemperature;
		private List<Integer> actualHumidity;
		private List<Temperature> desiredHeat;
		private List<Temperature> desiredCool;
		private List<Integer> desiredHumidity;
		private List<Integer> desiredDehumidity;
		private List<Temperature> dmOffset;
		private List<String> hvacMode;
		private List<Integer> heatPump1;
		private List<Integer> heatPump2;
		private List<Integer> auxHeat1;
		private List<Integer> auxHeat2;
		private List<Integer> auxHeat3;
		private List<Integer> cool1;
		private List<Integer> cool2;
		private List<Integer> fan;
		private List<Integer> humidifier;
		private List<Integer> dehumidifier;
		private List<Integer> economizer;
		private List<Integer> ventilator;
		private Integer currentElectricityBill;
		private Integer projectedElectricityBill;

		/**
		 * @return the UTC timestamp of the last value read. This timestamp is updated at a 15 min interval by the
		 *         thermostat. For the 1st value, it is timestamp - 10 mins, for the 2nd value it is timestamp - 5 mins.
		 *         Consider day boundaries being straddled when using these values.
		 */
		@JsonProperty("lastReadingTimestamp")
		public Date getLastReadingTimestamp() {
			return this.lastReadingTimestamp;
		}

		/**
		 * @return the UTC date of the last runtime reading. Format: YYYY-MM-DD
		 */
		@JsonProperty("runtimeDate")
		public String getRuntimeDate() {
			return this.runtimeDate;
		}

		/**
		 * @return the last 5 minute interval which was updated by the thermostat telemetry update. Subtract 2 from this
		 *         interval to obtain the beginning interval for the last 3 readings. Multiply by 5 mins to obtain the
		 *         minutes of the day. Range: 0-287
		 */
		@JsonProperty("runtimeInterval")
		public Integer getRuntimeInterval() {
			return this.runtimeInterval;
		}

		/**
		 * @return the last three 5 minute actual temperature readings
		 */
		@JsonProperty("actualTemperature")
		public List<Temperature> getActualTemperature() {
			return this.actualTemperature;
		}

		/**
		 * @return the last three 5 minute actual humidity readings
		 */
		@JsonProperty("actualHumidity")
		public List<Integer> getActualHumidity() {
			return this.actualHumidity;
		}

		/**
		 * @return the last three 5 minute desired heat temperature readings
		 */
		@JsonProperty("desiredHeat")
		public List<Temperature> getDesiredHeat() {
			return this.desiredHeat;
		}

		/**
		 * @return the last three 5 minute desired cool temperature readings
		 */
		@JsonProperty("desiredCool")
		public List<Temperature> getDesiredCool() {
			return this.desiredCool;
		}

		/**
		 * @return the last three 5 minute desired humidity readings
		 */
		@JsonProperty("desiredHumidity")
		public List<Integer> getDesiredHumidity() {
			return this.desiredHumidity;
		}

		/**
		 * @return the last three 5 minute desired dehumidification readings
		 */
		@JsonProperty("desiredDehumidity")
		public List<Integer> getDesiredDehumidity() {
			return this.desiredDehumidity;
		}

		/**
		 * @return the last three 5 minute desired Demand Management temperature offsets. This value is Demand
		 *         Management adjustment value which was applied by the thermostat. If the thermostat decided not to
		 *         honor the adjustment, it will send 0 for the interval. Compare these values with the values sent in
		 *         the DM message to determine whether the thermostat applied the adjustment.
		 */
		@JsonProperty("dmOffset")
		public List<Temperature> getDmOffset() {
			return this.dmOffset;
		}

		/**
		 * @return the last three 5 minute HVAC Mode reading. These values indicate which stage was energized in the 5
		 *         minute interval. Values: heatStage1On, heatStage2On, heatStage3On, heatOff, compressorCoolStage1On,
		 *         compressorCoolStage2On, compressorCoolOff, compressorHeatStage1On, compressorHeatStage2On,
		 *         compressorHeatOff, economyCycle.
		 */
		@JsonProperty("hvacMode")
		public List<String> getHvacMode() {
			return this.hvacMode;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the heat pump stage 1 runtime.
		 */
		@JsonProperty("heatPump1")
		public List<Integer> getHeatPump1() {
			return this.heatPump1;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the heat pump stage 2 runtime.
		 */
		@JsonProperty("heatPump2")
		public List<Integer> getHeatPump2() {
			return this.heatPump2;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the auxiliary heat stage 1. If the thermostat does not have a heat pump, this is heat
		 *         stage 1.
		 */
		@JsonProperty("auxHeat1")
		public List<Integer> getAuxHeat1() {
			return this.auxHeat1;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the auxiliary heat stage 2. If the thermostat does not have a heat pump, this is heat
		 *         stage 2.
		 */
		@JsonProperty("auxHeat2")
		public List<Integer> getAuxHeat2() {
			return this.auxHeat2;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the heat stage 3 if the thermostat does not have a heat pump. Auxiliary stage 3 is not
		 *         supported.
		 */
		@JsonProperty("auxHeat3")
		public List<Integer> getAuxHeat3() {
			return this.auxHeat3;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the cooling stage 1.
		 */
		@JsonProperty("cool1")
		public List<Integer> getCool1() {
			return this.cool1;
		}

		/**
		 * @return the last three 5 minute HVAC Runtime values in seconds (0-300 seconds) per interval. This value
		 *         corresponds to the cooling stage 2.
		 */
		@JsonProperty("cool2")
		public List<Integer> getCool2() {
			return this.cool2;
		}

		/**
		 * @return the last three 5 minute fan Runtime values in seconds (0-300 seconds) per interval.
		 */
		@JsonProperty("fan")
		public List<Integer> getFan() {
			return this.fan;
		}

		/**
		 * @return the last three 5 minute humidifier Runtime values in seconds (0-300 seconds) per interval.
		 */
		@JsonProperty("humidifier")
		public List<Integer> getHumidifier() {
			return this.humidifier;
		}

		/**
		 * @return the last three 5 minute dehumidifier Runtime values in seconds (0-300 seconds) per interval.
		 */
		@JsonProperty("dehumidifier")
		public List<Integer> getDehumidifier() {
			return this.dehumidifier;
		}

		/**
		 * @return the last three 5 minute economizer Runtime values in seconds (0-300 seconds) per interval.
		 */
		@JsonProperty("economizer")
		public List<Integer> getEconomizer() {
			return this.economizer;
		}

		/**
		 * @return the last three 5 minute ventilator Runtime values in seconds (0-300 seconds) per interval.
		 */
		@JsonProperty("ventilator")
		public List<Integer> getVentilator() {
			return this.ventilator;
		}

		/**
		 * @return the latest value of the current electricity bill as interpolated from the thermostat's readings from
		 *         a paired electricity meter.
		 */
		@JsonProperty("currentElectricityBill")
		public Integer getCurrentElectricityBill() {
			return this.currentElectricityBill;
		}

		/**
		 * @return the latest estimate of the projected electricity bill as interpolated from the thermostat's readings
		 *         from a paired electricity meter.
		 */
		@JsonProperty("projectedElectricityBill")
		public Integer getProjectedElectricityBill() {
			return this.projectedElectricityBill;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("lastReadingTimestamp", this.lastReadingTimestamp);
			builder.append("runtimeDate", this.runtimeDate);
			builder.append("runtimeInterval", this.runtimeInterval);
			builder.append("actualTemperature", this.actualTemperature);
			builder.append("actualHumidity", this.actualHumidity);
			builder.append("desiredHeat", this.desiredHeat);
			builder.append("desiredCool", this.desiredCool);
			builder.append("desiredHumidity", this.desiredHumidity);
			builder.append("desiredDehumidity", this.desiredDehumidity);
			builder.append("dmOffset", this.dmOffset);
			builder.append("hvacMode", this.hvacMode);
			builder.append("heatPump1", this.heatPump1);
			builder.append("heatPump2", this.heatPump2);
			builder.append("auxHeat1", this.auxHeat1);
			builder.append("auxHeat2", this.auxHeat2);
			builder.append("auxHeat3", this.auxHeat3);
			builder.append("cool1", this.cool1);
			builder.append("cool2", this.cool2);
			builder.append("fan", this.fan);
			builder.append("humidifier", this.humidifier);
			builder.append("dehumidifier", this.dehumidifier);
			builder.append("economizer", this.economizer);
			builder.append("ventilator", this.ventilator);
			builder.append("currentElectricityBill", this.currentElectricityBill);
			builder.append("projectedElectricityBill", this.projectedElectricityBill);

			return builder.toString();
		}
	}

	/**
	 * The Electricity class contains the last collected electricity usage measurements for the thermostat. An
	 * electricity object is composed of ElectricityDevices, each of which contains readings from an ElectricityTier.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Electricity.shtml">Electricity</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Electricity extends AbstractMessagePart {
		private List<ElectricityDevice> devices;

		Electricity(@JsonProperty("devices") List<ElectricityDevice> devices) {
			this.devices = devices;
		}

		/**
		 * @return the list of ElectricityDevice objects associated with the thermostat, each representing a device such
		 *         as an electric meter or remote load control.
		 */
		@JsonProperty("devices")
		public List<ElectricityDevice> getDevices() {
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

	/**
	 * An ElectricityDevice represents an energy recording device. At this time, only meters are supported by the API.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/ElectricityDevice.shtml">ElectricityDevice</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ElectricityDevice extends AbstractMessagePart {
		private List<ElectricityTier> tiers;
		private List<Date> lastUpdate;
		private List<String> cost;
		private List<String> consumption;

		/**
		 * @return the list of ElectricityTiers containing the break down of daily electricity consumption of the device
		 *         for the day, broken down per pricing tier
		 */
		@JsonProperty("tiers")
		public List<ElectricityTier> getTiers() {
			return this.tiers;
		}

		/**
		 * @return the last date/time the reading was updated in UTC time
		 */
		@JsonProperty("lastUpdate")
		public List<Date> getLastUpdate() {
			return this.lastUpdate;
		}

		/**
		 * @return the last three daily electricity cost reads from the device in cents with a three decimal place
		 *         precision.
		 */
		@JsonProperty("cost")
		public List<String> getCost() {
			return this.cost;
		}

		/**
		 * @return the last three daily electricity consumption reads from the device in KWh with a three decimal place
		 *         precision.
		 */
		@JsonProperty("consumption")
		public List<String> getConsumption() {
			return this.consumption;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("tiers", this.tiers);
			builder.append("lastUpdate", this.lastUpdate);
			builder.append("cost", this.cost);
			builder.append("consumption", this.consumption);

			return builder.toString();
		}
	}

	/**
	 * An ElectricityTier object represents the last reading from a given pricing tier if the utility provides such
	 * information. If there are no pricing tiers defined, than an unnamed tier will represent the total reading. The
	 * values represented here are a daily cumulative total in kWh. The cost is likewise a cumulative total in cents.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/ElectricityTier.shtml">ElectricityTier</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ElectricityTier extends AbstractMessagePart {
		private String name;
		private String consumption;
		private String cost;

		/**
		 * @return the tier name as defined by the {@link Utility}. May be an empty string if the tier is undefined or
		 *         the usage falls outside the defined tiers.
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the last daily consumption reading collected. The reading format and precision is to three decimal
		 *         places in kWh.
		 */
		@JsonProperty("consumption")
		public String getConsumption() {
			return this.consumption;
		}

		/**
		 * @return the daily cumulative tier cost in dollars if defined by the Utility. May be an empty string if
		 *         undefined.
		 */
		@JsonProperty("cost")
		public String getCost() {
			return this.cost;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("name", this.name);
			builder.append("consumption", this.consumption);
			builder.append("cost", this.cost);

			return builder.toString();
		}
	}

	/**
	 * Represents a device attached to the thermostat. Devices may not be modified remotely; all changes must occur on
	 * the thermostat.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Device.shtml">Device</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Device extends AbstractMessagePart {
		private Integer deviceId;
		private String name;
		private List<Sensor> sensors;
		private List<Output> outputs;

		/**
		 * @return a unique ID for the device
		 */
		@JsonProperty("deviceId")
		public Integer getDeviceId() {
			return this.deviceId;
		}

		/**
		 * @return the user supplied device name
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the list of {@link Sensor} objects associated with the device
		 */
		@JsonProperty("sensors")
		public List<Sensor> getSensors() {
			return this.sensors;
		}

		/**
		 * @return the list of {@link Output} objects associated with the device
		 */
		@JsonProperty("outputs")
		public List<Output> getOutputs() {
			return this.outputs;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("deviceId", this.deviceId);
			builder.append("name", this.name);
			builder.append("sensors", this.sensors);
			builder.append("outputs", this.outputs);

			return builder.toString();
		}
	}

	/**
	 * The RemoteSensor object represents a sensor connected to the thermostat.
	 * 
	 * The remote sensor data will only show computed occupancy, as does the thermostat. Definition - For a given
	 * sensor, computed occupancy means a sensor is occupied if any motion was detected in the past 30 minutes.
	 * RemoteSensor data changes trigger the runtimeRevision to be updated. The data updates are sent at an interval of
	 * 3 mins maximum. This means that you should not poll quicker than once every 3 mins for revision changes.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/RemoteSensor.shtml">RemoteSensor</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RemoteSensor extends AbstractMessagePart {
		private String id;
		private String name;
		private String type;
		private String code;
		private Boolean inUse;
		@JsonProperty("capability")
		private List<RemoteSensorCapability> capabilityList;
		@JsonIgnore
		private Map<String, RemoteSensorCapability> capability;

		/**
		 * @return the unique sensor identifier. It is composed of deviceName + deviceId separated by colons, for
		 *         example: <code>rs:100</code>
		 */
		@JsonProperty("id")
		public String getId() {
			return this.id;
		}

		/**
		 * @return the user assigned sensor name
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the type of remote sensor. Values: <code>thermostat</code>, <code>ecobee3_remote_sensor</code>,
		 *         <code>monitor_sensor</code>, <code>control_sensor</code>.
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return the unique 4-digit alphanumeric sensor code. For ecobee3 remote sensors this corresponds to the code
		 *         found on the back of the physical sensor.
		 */
		@JsonProperty("code")
		public String getCode() {
			return this.code;
		}

		/**
		 * @return this flag indicates whether the remote sensor is currently in use by a comfort setting. See
		 *         {@link Climate} for more information.
		 */
		@JsonProperty("inUse")
		public Boolean getInUse() {
			return this.inUse;
		}

		/**
		 * @return a list of {@link RemoteSensorCapability} objects
		 */
		@JsonProperty("capability")
		public List<RemoteSensorCapability> getCapabilityList() {
			return this.capabilityList;
		}

		/**
		 * @return a type-based map of {@link RemoteSensorCapability} objects
		 */
		@JsonIgnore
		public Map<String, RemoteSensorCapability> getCapability() {
			return this.capability;
		}

		/**
		 * Create a map of RemoteSensorCapability objects, keyed by type, for easy beanutils reference.
		 */
		protected void sync() {
			this.capability = new HashMap<String, RemoteSensorCapability>();
			if (this.capabilityList != null) {
				for (RemoteSensorCapability rsc : this.capabilityList) {
					this.capability.put(rsc.getType(), rsc);
				}
			}
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("id", this.id);
			builder.append("name", this.name);
			builder.append("type", this.type);
			builder.append("code", this.inUse);
			builder.append("capability", this.capabilityList);

			return builder.toString();
		}
	}

	/**
	 * The RemoteSensorCapability object represents the specific capability of a sensor connected to the thermostat.
	 * 
	 * For the occupancy type capability the data will only show computed occupancy, as does the thermostat.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/RemoteSensorCapability.shtml">RemoteSensorCapability</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RemoteSensorCapability extends AbstractMessagePart {
		private String id;
		private String type;
		@JsonProperty("value")
		private String valueString;

		/**
		 * @return the unique sensor capability identifier. For example: 1
		 */
		@JsonProperty("id")
		public String getId() {
			return this.id;
		}

		/**
		 * @return The type of sensor capability. Values: <code>adc</code>, <code>co2</code>, <code>dryContact</code>,
		 *         <code>humidity</code>, <code>temperature</code>, <code>occupancy</code>, <code>unknown</code>.
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return The data value for this capability, always a String. Temperature values are expressed as degrees
		 *         Fahrenheit, multiplied by 10. For example, a temperature of 72F would be returned as the value "720".
		 *         Occupancy values are "true" or "false". Humidity is expressed as a % value such as "45". Unknown
		 *         values are returned as "unknown".
		 */
		@JsonProperty("value")
		public String getValueString() {
			return this.valueString;
		}

		/**
		 * @return a properly typed version of the value, depending on <code>type</code>. Types are not documented for
		 *         <code>adc</code>, <code>co2</code>, or <code>dryContact</code> and so are returned as strings, until
		 *         the documentation explains.
		 */
		@JsonIgnore
		public Object getValue() {
			try {
				if ("temperature".equals(type)) {
					return new Temperature(Integer.parseInt(valueString));
				} else if ("occupancy".equals(type)) {
					if ("true".equals(valueString)) {
						return Boolean.TRUE;
					} else if ("false".equals(valueString)) {
						return Boolean.FALSE;
					} else {
						return UnDefType.NULL;
					}
				} else if ("humidity".equals(type)) {
					return Integer.parseInt(valueString);
				} else {
					return valueString;
				}
			} catch (NumberFormatException nfe) {
				return UnDefType.NULL;
			}
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("id", this.id);
			builder.append("type", this.type);
			builder.append("value", this.valueString);

			return builder.toString();
		}
	}

	/**
	 * The Sensor class represents a sensor connected to the thermostat. Sensors may not be modified using the API,
	 * however some configuration may occur through the web portal.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Sensor.shtml">Sensor</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Sensor extends AbstractMessagePart {
		private String name;
		private String manufacturer;
		private String model;
		private Integer zone;
		private Integer sensorId;
		private String type;
		private String usage;
		private Integer numberOfBits;
		private Integer bconstant;
		private Integer thermistorSize;
		private Integer tempCorrection;
		private Integer gain;
		private Integer maxVoltage;
		private Integer multiplier;
		private List<SensorState> states;

		/**
		 * @return the sensor name
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the sensor manufacturer
		 */
		@JsonProperty("manufacturer")
		public String getManufacturer() {
			return this.manufacturer;
		}

		/**
		 * @return the sensor model
		 */
		@JsonProperty("model")
		public String getModel() {
			return this.model;
		}

		/**
		 * @return the thermostat zone the sensor is associated with
		 */
		@JsonProperty("zone")
		public Integer getZone() {
			return this.zone;
		}

		/**
		 * @return the unique sensor identifier
		 */
		@JsonProperty("sensorId")
		public Integer getSensorId() {
			return this.sensorId;
		}

		/**
		 * @return the type of sensor. Values: adc, co2, dryCOntact, humidity, temperature, unknown
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return the sensor usage type. Values: dischargeAir, indoor, monitor, outdoor
		 */
		@JsonProperty("usage")
		public String getUsage() {
			return this.usage;
		}

		/**
		 * @return the numberOfBits
		 */
		@JsonProperty("numberOfBits")
		public Integer getNumberOfBits() {
			return this.numberOfBits;
		}

		/**
		 * @return the bconstant
		 */
		@JsonProperty("bconstant")
		public Integer getBconstant() {
			return this.bconstant;
		}

		/**
		 * @return the thermistorSize
		 */
		@JsonProperty("thermistorSize")
		public Integer getThermistorSize() {
			return this.thermistorSize;
		}

		/**
		 * @return the tempCorrection
		 */
		@JsonProperty("tempCorrection")
		public Integer getTempCorrection() {
			return this.tempCorrection;
		}

		/**
		 * @return the gain
		 */
		@JsonProperty("gain")
		public Integer getGain() {
			return this.gain;
		}

		/**
		 * @return the maxVoltage
		 */
		@JsonProperty("maxVoltage")
		public Integer getMaxVoltage() {
			return this.maxVoltage;
		}

		/**
		 * @return the multiplier
		 */
		@JsonProperty("multiplier")
		public Integer getMultiplier() {
			return this.multiplier;
		}

		/**
		 * @return a list of {@link SensorState} objects
		 */
		@JsonProperty("states")
		public List<SensorState> getStates() {
			return this.states;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("name", this.name);
			builder.append("manufacturer", this.manufacturer);
			builder.append("model", this.model);
			builder.append("zone", this.zone);
			builder.append("sensorId", this.sensorId);
			builder.append("type", this.type);
			builder.append("usage", this.usage);
			builder.append("numberOfBits", this.numberOfBits);
			builder.append("bconstant", this.bconstant);
			builder.append("thermistorSize", this.thermistorSize);
			builder.append("tempCorrection", this.tempCorrection);
			builder.append("gain", this.gain);
			builder.append("maxVoltage", this.maxVoltage);
			builder.append("multiplier", this.multiplier);
			builder.append("states", this.states);

			return builder.toString();
		}
	}

	/**
	 * A sensor state is a configurable trigger for a number of {@link StateAction}s.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/State.shtml">State</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SensorState extends AbstractMessagePart {
		private Integer maxValue;
		private Integer minValue;
		private String type;
		private List<StateAction> actions;

		/**
		 * @return the maximum value the sensor can generate
		 */
		@JsonProperty("maxValue")
		public Integer getMaxValue() {
			return this.maxValue;
		}

		/**
		 * @return the minimum value the sensor can generate
		 */
		@JsonProperty("minValue")
		public Integer getMinValue() {
			return this.minValue;
		}

		/**
		 * @return the type Values: coolHigh, coolLow, heatHigh, heatLow, high, low, transitionCount, normal
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return the list of {@link StateAction} objects associated with the sensor
		 */
		@JsonProperty("actions")
		public List<StateAction> getActions() {
			return this.actions;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("maxValue", this.maxValue);
			builder.append("minValue", this.minValue);
			builder.append("type", this.type);
			builder.append("actions", this.actions);

			return builder.toString();
		}
	}

	/**
	 * A StateAction defines an action to take when a {@link SensorState} is triggered.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Action.shtml">Action</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class StateAction extends AbstractMessagePart {
		private String type;
		private Boolean sendAlert;
		private Boolean sendUpdate;
		private Integer activationDelay;
		private Integer deactivationDelay;
		private Integer minActionDuration;
		private Temperature heatAdjustTemp;
		private Temperature coolAdjustTemp;
		private String activateRelay;
		private Boolean activateRelayOpen;

		/**
		 * @return the type Values: activateRelay, adjustTemp, doNothing, shutdownAC, shutdownAuxHeat, shutdownSystem,
		 *         shutdownCompression, switchToOccupied, switchToUnoccupied, turnOffDehumidifer, turnOffHumidifier,
		 *         turnOnCool, turnOnDehumidifier, turnOnFan, turnOnHeat, turnOnHumidifier.
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return flag to enable an alert to be generated when the state is triggered
		 */
		@JsonProperty("sendAlert")
		public Boolean getSendAlert() {
			return this.sendAlert;
		}

		/**
		 * @return the sendUpdate
		 */
		@JsonProperty("sendUpdate")
		public Boolean getSendUpdate() {
			return this.sendUpdate;
		}

		/**
		 * @return delay in seconds before the action is triggered by the state change
		 */
		@JsonProperty("activationDelay")
		public Integer getActivationDelay() {
			return this.activationDelay;
		}

		/**
		 * @return the deactivationDelay
		 */
		@JsonProperty("deactivationDelay")
		public Integer getDeactivationDelay() {
			return this.deactivationDelay;
		}

		/**
		 * @return the minActionDuration
		 */
		@JsonProperty("minActionDuration")
		public Integer getMinActionDuration() {
			return this.minActionDuration;
		}

		/**
		 * @return the heatAdjustTemp
		 */
		@JsonProperty("heatAdjustTemp")
		public Temperature getHeatAdjustTemp() {
			return this.heatAdjustTemp;
		}

		/**
		 * @return the coolAdjustTemp
		 */
		@JsonProperty("coolAdjustTemp")
		public Temperature getCoolAdjustTemp() {
			return this.coolAdjustTemp;
		}

		/**
		 * @return the activateRelay
		 */
		@JsonProperty("activateRelay")
		public String getActivateRelay() {
			return this.activateRelay;
		}

		/**
		 * @return the activateRelayOpen
		 */
		@JsonProperty("activateRelayOpen")
		public Boolean isActivateRelayOpen() {
			return this.activateRelayOpen;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("type", this.type);
			builder.append("sendAlert", this.sendAlert);
			builder.append("sendUpdate", this.sendUpdate);
			builder.append("activationDelay", this.activationDelay);
			builder.append("deactivationDelay", this.deactivationDelay);
			builder.append("minActionDuration", this.minActionDuration);
			builder.append("heatAdjustTemp", this.heatAdjustTemp);
			builder.append("coolAdjustTemp", this.coolAdjustTemp);
			builder.append("activateRelay", this.activateRelay);
			builder.append("activateRelayOpen", this.activateRelayOpen);

			return builder.toString();
		}
	}

	/**
	 * An output is a relay connected to the thermostat.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Output.shtml">Output</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Output extends AbstractMessagePart {
		private String name;
		private Integer zone;
		private Integer outputId;
		private String type;
		private Boolean sendUpdate;
		private Boolean activeClosed;
		private Integer activationTime;
		private Integer deactivationTime;

		/**
		 * @return the name of the output
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the thermostat zone the output is associated with
		 */
		@JsonProperty("zone")
		public Integer getZone() {
			return this.zone;
		}

		/**
		 * @return the unique output identifier number
		 */
		@JsonProperty("outputId")
		public Integer getOutputId() {
			return this.outputId;
		}

		/**
		 * @return the type of output. Values: compressor1, compressor2, dehumidifier, economizer, fan, heat1, heat2,
		 *         heat3, heatPumpReversal, humidifer, none, occupancy, userDefined, ventilator, zoneCool, zoneFan,
		 *         zoneHeat
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return the sendUpdate
		 */
		@JsonProperty("sendUpdate")
		public Boolean getSendUpdate() {
			return this.sendUpdate;
		}

		/**
		 * @return the activeClosed
		 */
		@JsonProperty("activeClosed")
		public Boolean getActiveClosed() {
			return this.activeClosed;
		}

		/**
		 * @return the activationTime
		 */
		@JsonProperty("activationTime")
		public Integer getActivationTime() {
			return this.activationTime;
		}

		/**
		 * @return the deactivationTime
		 */
		@JsonProperty("deactivationTime")
		public Integer getDeactivationTime() {
			return this.deactivationTime;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("name", this.name);
			builder.append("zone", this.zone);
			builder.append("outputId", this.outputId);
			builder.append("type", this.type);
			builder.append("sendUpdate", this.sendUpdate);
			builder.append("activeClosed", this.activeClosed);
			builder.append("activationTime", this.activationTime);
			builder.append("deactivationTime", this.deactivationTime);

			return builder.toString();
		}
	}

	/**
	 * The Location describes the physical location and coordinates of the thermostat as entered by the thermostat
	 * owner. The address information is used in a geocode look up to obtain the thermostat coordinates. The coordinates
	 * are used to obtain accurate weather information.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Location.shtml">Location</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Location extends AbstractMessagePart {
		private Integer timeZoneOffsetMinutes;
		private String timeZone;
		private Boolean isDaylightSaving;
		private String streetAddress;
		private String city;
		private String provinceState;
		private String country;
		private String postalCode;
		private String phoneNumber;
		private String mapCoordinates;

		/**
		 * @return the timezone offset in minutes from UTC
		 */
		@JsonProperty("timeZoneOffsetMinutes")
		public Integer getTimeZoneOffsetMinutes() {
			return this.timeZoneOffsetMinutes;
		}

		/**
		 * @return the Olson timezone the thermostat resides in (e.g America/Toronto)
		 */
		@JsonProperty("timeZone")
		public String getTimeZone() {
			return this.timeZone;
		}

		/**
		 * @param timeZone
		 *            the Olson timezone the thermostat resides in (e.g America/Toronto)
		 */
		@JsonProperty("timeZone")
		public void setTimeZone(String timeZone) {
			this.timeZone = timeZone;
		}

		/**
		 * @return whether the thermostat should factor in daylight savings when displaying the date and time
		 */
		@JsonProperty("isDaylightSaving")
		public Boolean getIsDaylightSaving() {
			return this.isDaylightSaving;
		}

		/**
		 * @param isDaylightSaving
		 *            whether the thermostat should factor in daylight savings when displaying the date and time
		 */
		@JsonProperty("isDaylightSaving")
		public void setIsDaylightSaving(Boolean isDaylightSaving) {
			this.isDaylightSaving = isDaylightSaving;
		}

		/**
		 * @return the thermostat location street address
		 */
		@JsonProperty("streetAddress")
		public String getStreetAddress() {
			return this.streetAddress;
		}

		/**
		 * @param streetAddress
		 *            the thermostat location street address
		 */
		@JsonProperty("streetAddress")
		public void setStreetAddress(String streetAddress) {
			this.streetAddress = streetAddress;
		}

		/**
		 * @return the thermostat location city
		 */
		@JsonProperty("city")
		public String getCity() {
			return this.city;
		}

		/**
		 * @param city
		 *            the thermostat location city
		 */
		@JsonProperty("city")
		public void setCity(String city) {
			this.city = city;
		}

		/**
		 * @return the thermostat location state or province
		 */
		@JsonProperty("provinceState")
		public String getProvinceState() {
			return this.provinceState;
		}

		/**
		 * @param provinceState
		 *            the thermostat location state or province
		 */
		@JsonProperty("provinceState")
		public void setProvinceState(String provinceState) {
			this.provinceState = provinceState;
		}

		/**
		 * @return the thermostat location country
		 */
		@JsonProperty("country")
		public String getCountry() {
			return this.country;
		}

		/**
		 * @param country
		 *            the thermostat location country
		 */
		@JsonProperty("country")
		public void setCountry(String country) {
			this.country = country;
		}

		/**
		 * @return the thermostat location ZIP or postal code
		 */
		@JsonProperty("postalCode")
		public String getPostalCode() {
			return this.postalCode;
		}

		/**
		 * @param postalCode
		 *            the thermostat location ZIP or postal code
		 */
		@JsonProperty("postalCode")
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		/**
		 * @return the thermostat owner's phone number
		 */
		@JsonProperty("phoneNumber")
		public String getPhoneNumber() {
			return this.phoneNumber;
		}

		/**
		 * @param phoneNumber
		 *            the thermostat owner's phone number
		 */
		@JsonProperty("phoneNumber")
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		/**
		 * @return the lat/long geographic coordinates of the thermostat location
		 */
		@JsonProperty("mapCoordinates")
		public String getMapCoordinates() {
			return this.mapCoordinates;
		}

		/**
		 * @param mapCoordinates
		 *            the lat/long geographic coordinates of the thermostat location
		 */
		@JsonProperty("mapCoordinates")
		public void setMapCoordinates(String mapCoordinates) {
			this.mapCoordinates = mapCoordinates;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("timeZoneOffsetMinutes", this.timeZoneOffsetMinutes);
			builder.append("timeZone", this.timeZone);
			builder.append("isDaylightSaving", this.isDaylightSaving);
			builder.append("streetAddress", this.streetAddress);
			builder.append("city", this.city);
			builder.append("provinceState", this.provinceState);
			builder.append("country", this.country);
			builder.append("postalCode", this.postalCode);
			builder.append("phoneNumber", this.phoneNumber);
			builder.append("mapCoordinates", this.mapCoordinates);

			return builder.toString();
		}
	}

	/**
	 * The Technician object contains information pertaining to the technician associated with a thermostat. The
	 * technician may not be modified through the API.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Technician.shtml">Technician</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Technician extends AbstractMessagePart {
		private String contractorRef;
		private String name;
		private String phone;
		private String streetAddress;
		private String city;
		private String provinceState;
		private String country;
		private String postalCode;
		private String email;
		private String web;

		/**
		 * @return the internal ecobee unique identifier for this contractor
		 */
		@JsonProperty("contractorRef")
		public String getContractorRef() {
			return this.contractorRef;
		}

		/**
		 * @return the company name of the technician
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the technician's contact phone number
		 */
		@JsonProperty("phone")
		public String getPhone() {
			return this.phone;
		}

		/**
		 * @return the technician's street address
		 */
		@JsonProperty("streetAddress")
		public String getStreetAddress() {
			return this.streetAddress;
		}

		/**
		 * @return the technician's city
		 */
		@JsonProperty("city")
		public String getCity() {
			return this.city;
		}

		/**
		 * @return the technician's State or province
		 */
		@JsonProperty("provinceState")
		public String getProvinceState() {
			return this.provinceState;
		}

		/**
		 * @return the technician's country
		 */
		@JsonProperty("country")
		public String getCountry() {
			return this.country;
		}

		/**
		 * @return the technician's ZIP or postal code
		 */
		@JsonProperty("postalCode")
		public String getPostalCode() {
			return this.postalCode;
		}

		/**
		 * @return the technician's email address
		 */
		@JsonProperty("email")
		public String getEmail() {
			return this.email;
		}

		/**
		 * @return the technician's web site
		 */
		@JsonProperty("web")
		public String getWeb() {
			return this.web;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("contractorRef", this.contractorRef);
			builder.append("name", this.name);
			builder.append("phone", this.phone);
			builder.append("streetAddress", this.streetAddress);
			builder.append("city", this.city);
			builder.append("provinceState", this.provinceState);
			builder.append("country", this.country);
			builder.append("postalCode", this.postalCode);
			builder.append("email", this.email);
			builder.append("web", this.web);

			return builder.toString();
		}
	}

	/**
	 * The Utility information the {@link Thermostat} belongs to. The utility may not be modified through the API.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Utility.shtml">Utility</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Utility extends AbstractMessagePart {
		private String name;
		private String phone;
		private String email;
		private String web;

		/**
		 * @return the Utility company name
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the Utility company contact phone number
		 */
		@JsonProperty("phone")
		public String getPhone() {
			return this.phone;
		}

		/**
		 * @return the Utility company email address
		 */
		@JsonProperty("email")
		public String getEmail() {
			return this.email;
		}

		/**
		 * @return the Utility company web site
		 */
		@JsonProperty("web")
		public String getWeb() {
			return this.web;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("name", this.name);
			builder.append("phone", this.phone);
			builder.append("email", this.email);
			builder.append("web", this.web);

			return builder.toString();
		}
	}

	/**
	 * The Management object contains information about the management company the thermostat belongs to. The Management
	 * object is read-only, it may be modified in the web portal.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Management.shtml">Management</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Management extends AbstractMessagePart {
		private String administrativeContact;
		private String billingContact;
		private String name;
		private String phone;
		private String email;
		private String web;
		private Boolean showAlertIdt;
		private Boolean showAlertWeb;

		/**
		 * @return the administrative contact name
		 */
		@JsonProperty("administrativeContact")
		public String getAdministrativeContact() {
			return this.administrativeContact;
		}

		/**
		 * @return the billing contact name
		 */
		@JsonProperty("billingContact")
		public String getBillingContact() {
			return this.billingContact;
		}

		/**
		 * @return the company name
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return the phone number
		 */
		@JsonProperty("phone")
		public String getPhone() {
			return this.phone;
		}

		/**
		 * @return the contact email address
		 */
		@JsonProperty("email")
		public String getEmail() {
			return this.email;
		}

		/**
		 * @return the company web site
		 */
		@JsonProperty("web")
		public String getWeb() {
			return this.web;
		}

		/**
		 * @return the showAlertIdt
		 */
		@JsonProperty("showAlertIdt")
		public Boolean getShowAlertIdt() {
			return this.showAlertIdt;
		}

		/**
		 * @return the showAlertWeb
		 */
		@JsonProperty("showAlertWeb")
		public Boolean getShowAlertWeb() {
			return this.showAlertWeb;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("administrativeContact", this.administrativeContact);
			builder.append("billingContact", this.billingContact);
			builder.append("name", this.name);
			builder.append("phone", this.phone);
			builder.append("email", this.email);
			builder.append("web", this.web);
			builder.append("showAlertIdt", this.showAlertIdt);
			builder.append("showAlertWeb", this.showAlertWeb);

			return builder.toString();
		}
	}

	/**
	 * The Weather object contains the weather and forecast information for the thermostat's location.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Weather.shtml">Weather</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Weather extends AbstractMessagePart {
		private Date timestamp;
		private String weatherStation;
		private List<WeatherForecast> forecasts;

		/**
		 * @return the time stamp in UTC of the weather forecast
		 */
		@JsonProperty("timestamp")
		public Date getTimestamp() {
			return this.timestamp;
		}

		/**
		 * @return the weather station identifier
		 */
		@JsonProperty("weatherStation")
		public String getWeatherStation() {
			return this.weatherStation;
		}

		/**
		 * @return the list of latest weather station forecasts
		 */
		@JsonProperty("forecasts")
		public List<WeatherForecast> getForecasts() {
			return this.forecasts;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("timestamp", this.timestamp);
			builder.append("weatherStation", this.weatherStation);
			builder.append("forecasts", this.forecasts);

			return builder.toString();
		}
	}

	/**
	 * The Weather Forecast contains the weather forecast information for the thermostat. The first forecast is the most
	 * accurate, later forecasts become less accurate in distance and time.
	 * 
	 * The <code>weatherSymbol</code> field can be used by the API caller to display a particular icon or message for
	 * example. The values mapping for the weather to <code>weatherSymbol</code> field is:
	 * 
	 * <table>
	 * <tbody>
	 * <tr>
	 * <th>meaning</th>
	 * <th>weatherSymbol</th>
	 * </tr>
	 * <tr>
	 * <td>no_symbol</td>
	 * <td>-2</td>
	 * </tr>
	 * <tr>
	 * <td>sunny</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>few_clouds</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>partly_cloudy</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>mostly_cloudy</td>
	 * <td>3</td>
	 * </tr>
	 * <tr>
	 * <td>overcast</td>
	 * <td>4</td>
	 * </tr>
	 * <tr>
	 * <td>drizzle</td>
	 * <td>5</td>
	 * </tr>
	 * <tr>
	 * <td>rain</td>
	 * <td>6
	 * <tr>
	 * <td>freezing_rain</td>
	 * <td>7</td>
	 * </tr>
	 * <tr>
	 * <td>showers</td>
	 * <td>8</td>
	 * </tr>
	 * <tr>
	 * <td>hail</td>
	 * <td>9</td>
	 * </tr>
	 * <tr>
	 * <td>snow</td>
	 * <td>10</td>
	 * </tr>
	 * <tr>
	 * <td>flurries</td>
	 * <td>11</td>
	 * </tr>
	 * <tr>
	 * <td>freezing_snow</td>
	 * <td>12</td>
	 * </tr>
	 * <tr>
	 * <td>blizzard</td>
	 * <td>13</td>
	 * </tr>
	 * <tr>
	 * <td>pellets</td>
	 * <td>14
	 * <tr>
	 * <td>thunderstorm</td>
	 * <td>15</td>
	 * </tr>
	 * <tr>
	 * <td>windy</td>
	 * <td>16</td>
	 * </tr>
	 * <tr>
	 * <td>tornado</td>
	 * <td>17</td>
	 * </tr>
	 * <tr>
	 * <td>fog</td>
	 * <td>18</td>
	 * </tr>
	 * <tr>
	 * <td>haze</td>
	 * <td>19</td>
	 * </tr>
	 * <tr>
	 * <td>smoke</td>
	 * <td>20</td>
	 * </tr>
	 * <tr>
	 * <td>dust</td>
	 * <td>21</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/WeatherForecast.shtml">WeatherForecast</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class WeatherForecast extends AbstractMessagePart {
		private Integer weatherSymbol;
		private Date dateTime;
		private String condition;
		private Temperature temperature;
		private Integer pressure;
		private Integer relativeHumidity;
		private Integer dewpoint;
		private Integer visibility;
		private Integer windSpeed;
		private Integer windGust;
		private String windDirection;
		private Integer windBearing;
		private Integer pop;
		private Temperature tempHigh;
		private Temperature tempLow;
		private Integer sky;

		/**
		 * @return the weatherSymbol
		 */
		@JsonProperty("weatherSymbol")
		public Integer getWeatherSymbol() {
			return this.weatherSymbol;
		}

		/**
		 * @return the time stamp of the weather forecast
		 */
		@JsonProperty("dateTime")
		public Date getDateTime() {
			return this.dateTime;
		}

		/**
		 * @return the condition
		 */
		@JsonProperty("condition")
		public String getCondition() {
			return this.condition;
		}

		/**
		 * @return the current temperature
		 */
		@JsonProperty("temperature")
		public Temperature getTemperature() {
			return this.temperature;
		}

		/**
		 * @return the current barometric pressure
		 */
		@JsonProperty("pressure")
		public Integer getPressure() {
			return this.pressure;
		}

		/**
		 * @return the current humidity
		 */
		@JsonProperty("relativeHumidity")
		public Integer getRelativeHumidity() {
			return this.relativeHumidity;
		}

		/**
		 * @return the dewpoint
		 */
		@JsonProperty("dewpoint")
		public Integer getDewpoint() {
			return this.dewpoint;
		}

		/**
		 * @return the visibility
		 */
		@JsonProperty("visibility")
		public Integer getVisibility() {
			return this.visibility;
		}

		/**
		 * @return the wind speed as an integer in mph * 1000
		 */
		@JsonProperty("windSpeed")
		public Integer getWindSpeed() {
			return this.windSpeed;
		}

		/**
		 * @return the windGust
		 */
		@JsonProperty("windGust")
		public Integer getWindGust() {
			return this.windGust;
		}

		/**
		 * @return the wind direction
		 */
		@JsonProperty("windDirection")
		public String getWindDirection() {
			return this.windDirection;
		}

		/**
		 * @return the wind bearing
		 */
		@JsonProperty("windBearing")
		public Integer getWindBearing() {
			return this.windBearing;
		}

		/**
		 * @return the probability of precipitation
		 */
		@JsonProperty("pop")
		public Integer getPop() {
			return this.pop;
		}

		/**
		 * @return the predicted high temperature for the day
		 */
		@JsonProperty("tempHigh")
		public Temperature getTempHigh() {
			return this.tempHigh;
		}

		/**
		 * @return the predicted low temperature for the day
		 */
		@JsonProperty("tempLow")
		public Temperature getTempLow() {
			return this.tempLow;
		}

		/**
		 * @return the cloud cover condition
		 */
		@JsonProperty("sky")
		public Integer getSky() {
			return this.sky;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("weatherSymbol", this.weatherSymbol);
			builder.append("dateTime", this.dateTime);
			builder.append("condition", this.condition);
			builder.append("temperature", this.temperature);
			builder.append("pressure", this.pressure);
			builder.append("relativeHumidity", this.relativeHumidity);
			builder.append("dewpoint", this.dewpoint);
			builder.append("visibility", this.visibility);
			builder.append("windSpeed", this.windSpeed);
			builder.append("windGust", this.windGust);
			builder.append("windDirection", this.windDirection);
			builder.append("windBearing", this.windBearing);
			builder.append("pop", this.pop);
			builder.append("tempHigh", this.tempHigh);
			builder.append("tempLow", this.tempLow);
			builder.append("sky", this.sky);

			return builder.toString();
		}
	}

	/**
	 * The event object represents a scheduled thermostat program change. All events have a start and end time during
	 * which the thermostat runtime settings will be modified. Events may not be directly modified, various Functions
	 * provide the capability to modify the calendar events and to modify the program. The event list is sorted with
	 * events ordered by whether they are currently running and the internal priority of each event. It is safe to take
	 * the first event which is running and show it as the currently running event. When the resume function is used,
	 * events are removed in the order they are listed here.
	 * 
	 * Note that the start/end date/time for the event must be in thermostat time and are not specified in UTC.
	 * 
	 * Event Priorities
	 * 
	 * The events are listed from top priority first to lowest priority. They will appear in the events list in the same
	 * order as listed here provided they are active currently.
	 * 
	 * <table>
	 * <tr>
	 * <td>Type</td>
	 * <td>Event Type</td>
	 * </tr>
	 * <tr>
	 * <td>hold</td>
	 * <td>Hold temperature event.</td>
	 * </tr>
	 * <tr>
	 * <td>demandResponse</td>
	 * <td>Demand Response event.</td>
	 * </tr>
	 * <tr>
	 * <td>sensor</td>
	 * <td>Sensor generated event.</td>
	 * </tr>
	 * <tr>
	 * <td>switchOccupancy</td>
	 * <td>EMS only event to flip unoccupied to occupied, and vice versa. Look at name to determine whether "occupied"
	 * or "unoccupied".</td>
	 * </tr>
	 * <tr>
	 * <td>vacation</td>
	 * <td>Vacation event.</td>
	 * </tr>
	 * <tr>
	 * <td>quickSave</td>
	 * <td>Quick Save event.</td>
	 * </tr>
	 * <tr>
	 * <td>today</td>
	 * <td>Today widget generated event.</td>
	 * </tr>
	 * <tr>
	 * <td>template</td>
	 * <td>A vacation event that reflects the thermostat owner's default preferences for any created vacation. Template
	 * events are never active and are only used to store the last used vacation settings of the thermostat owner.</td>
	 * </tr>
	 * </table>
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Event.shtml">Event</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event extends AbstractMessagePart {
		private String type;
		private String name;
		@JsonProperty("running")
		private Boolean running;
		// TODO Jackson 1.9 dates (@watou)
		private String startDate;
		private String startTime;
		private String endDate;
		private String endTime;
		@JsonProperty("isOccupied")
		private Boolean _isOccupied;
		@JsonProperty("isCoolOff")
		private Boolean _isCoolOff;
		@JsonProperty("isHeatOff")
		private Boolean _isHeatOff;
		private Temperature coolHoldTemp;
		private Temperature heatHoldTemp;
		private FanMode fan;
		private VentilatorMode vent;
		private Integer ventilatorMinOnTime;
		@JsonProperty("isOptional")
		private Boolean _isOptional;
		@JsonProperty("isTemperatureRelative")
		private Boolean _isTemperatureRelative;
		private Temperature coolRelativeTemp;
		private Temperature heatRelativeTemp;
		@JsonProperty("isTemperatureAbsolute")
		private Boolean _isTemperatureAbsolute;
		private Integer dutyCyclePercentage;
		private Integer fanMinOnTime;
		private Boolean occupiedSensorActive;
		private Boolean unoccupiedSensorActive;
		private Temperature drRampUpTemp;
		private Integer drRampUpTime;
		private String linkRef;
		private String holdClimateRef;

		/**
		 * @return the type of event. Values: hold, demandResponse, sensor, switchOccupancy, vacation, quickSave, today
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @return the unique event name
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @return whether the event is currently active or not
		 */
		@JsonProperty("running")
		public Boolean isRunning() {
			return this.running;
		}

		/**
		 * @return the event start date in thermostat local time
		 */
		@JsonProperty("startDate")
		public String getStartDate() {
			return this.startDate;
		}

		/**
		 * @return the event start time in thermostat local time
		 */
		@JsonProperty("startTime")
		public String getStartTime() {
			return this.startTime;
		}

		/**
		 * @return the event end date in thermostat local time
		 */
		@JsonProperty("endDate")
		public String getEndDate() {
			return this.endDate;
		}

		/**
		 * @return the event end time in thermostat local time
		 */
		@JsonProperty("endTime")
		public String getEndTime() {
			return this.endTime;
		}

		/**
		 * @return whether there are persons occupying the property during the event
		 */
		@JsonProperty("isOccupied")
		public Boolean isOccupied() {
			return this._isOccupied;
		}

		/**
		 * @return whether cooling will be turned off during the event
		 */
		@JsonProperty("isCoolOff")
		public Boolean isCoolOff() {
			return this._isCoolOff;
		}

		/**
		 * @return whether heating will be turned off during the event
		 */
		@JsonProperty("isHeatOff")
		public Boolean isHeatOff() {
			return this._isHeatOff;
		}

		/**
		 * @return the cooling absolute temperature to set
		 */
		@JsonProperty("coolHoldTemp")
		public Temperature getCoolHoldTemp() {
			return this.coolHoldTemp;
		}

		/**
		 * @return the heating absolute temperature to set
		 */
		@JsonProperty("heatHoldTemp")
		public Temperature getHeatHoldTemp() {
			return this.heatHoldTemp;
		}

		/**
		 * @return the fan mode during the event. Values: auto, on Default: based on current climate and hvac mode
		 */
		@JsonProperty("fan")
		public FanMode getFan() {
			return this.fan;
		}

		/**
		 * @return the ventilator mode during the event. Values: auto, minontime, on, off
		 */
		@JsonProperty("vent")
		public VentilatorMode getVent() {
			return this.vent;
		}

		/**
		 * @return the minimum amount of time the ventilator equipment must stay on on each duty cycle
		 */
		@JsonProperty("ventilatorMinOnTime")
		public Integer getVentilatorMinOnTime() {
			return this.ventilatorMinOnTime;
		}

		/**
		 * @return whether this event is mandatory or the end user can cancel it
		 */
		@JsonProperty("isOptional")
		public Boolean isOptional() {
			return this._isOptional;
		}

		/**
		 * @return whether the event is using a relative temperature setting to the currently active program climate
		 */
		@JsonProperty("isTemperatureRelative")
		public Boolean isTemperatureRelative() {
			return this._isTemperatureRelative;
		}

		/**
		 * @return the relative cool temperature adjustment
		 */
		@JsonProperty("coolRelativeTemp")
		public Temperature getCoolRelativeTemp() {
			return this.coolRelativeTemp;
		}

		/**
		 * @return the relative heat temperature adjustment
		 */
		@JsonProperty("heatRelativeTemp")
		public Temperature getHeatRelativeTemp() {
			return this.heatRelativeTemp;
		}

		/**
		 * @return whether the event uses absolute temperatures to set the values. Default: true for DRs
		 */
		@JsonProperty("isTemperatureAbsolute")
		public Boolean isTemperatureAbsolute() {
			return this._isTemperatureAbsolute;
		}

		/**
		 * @return the dutyCyclePercentage
		 */
		@JsonProperty("dutyCyclePercentage")
		public Integer getDutyCyclePercentage() {
			return this.dutyCyclePercentage;
		}

		/**
		 * @return the minimum number of minutes to run the fan each hour. Range: 0-60, Default: 0
		 */
		@JsonProperty("fanMinOnTime")
		public Integer getFanMinOnTime() {
			return this.fanMinOnTime;
		}

		/**
		 * @return the occupiedSensorActive
		 */
		@JsonProperty("occupiedSensorActive")
		public Boolean isOccupiedSensorActive() {
			return this.occupiedSensorActive;
		}

		/**
		 * @return the unoccupiedSensorActive
		 */
		@JsonProperty("unoccupiedSensorActive")
		public Boolean isUnoccupiedSensorActive() {
			return this.unoccupiedSensorActive;
		}

		/**
		 * @return unsupported. Future feature
		 */
		@JsonProperty("drRampUpTemp")
		public Temperature getDrRampUpTemp() {
			return this.drRampUpTemp;
		}

		/**
		 * @return unsupported. Future feature
		 */
		@JsonProperty("drRampUpTime")
		public Integer getDrRampUpTime() {
			return this.drRampUpTime;
		}

		/**
		 * @return unique identifier set by the server to link one or more events and alerts together
		 */
		@JsonProperty("linkRef")
		public String getLinkRef() {
			return this.linkRef;
		}

		/**
		 * @return used for display purposes to indicate what climate (if any) is being used for the hold
		 */
		@JsonProperty("holdClimateRef")
		public String getHoldClimateRef() {
			return this.holdClimateRef;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("type", this.type);
			builder.append("name", this.name);
			builder.append("running", this.running);
			builder.append("startDate", this.startDate);
			builder.append("startTime", this.startTime);
			builder.append("endDate", this.endDate);
			builder.append("endTime", this.endTime);
			builder.append("isOccupied", this._isOccupied);
			builder.append("isCoolOff", this._isCoolOff);
			builder.append("isHeatOff", this._isHeatOff);
			builder.append("coolHoldTemp", this.coolHoldTemp);
			builder.append("heatHoldTemp", this.heatHoldTemp);
			builder.append("fan", this.fan);
			builder.append("vent", this.vent);
			builder.append("ventilatorMinOnTime", this.ventilatorMinOnTime);
			builder.append("isOptional", this._isOptional);
			builder.append("isTemperatureRelative", this._isTemperatureRelative);
			builder.append("coolRelativeTemp", this.coolRelativeTemp);
			builder.append("heatRelativeTemp", this.heatRelativeTemp);
			builder.append("isTemperatureAbsolute", this._isTemperatureAbsolute);
			builder.append("dutyCyclePercentage", this.dutyCyclePercentage);
			builder.append("fanMinOnTime", this.fanMinOnTime);
			builder.append("occupiedSensorActive", this.occupiedSensorActive);
			builder.append("unoccupiedSensorActive", this.unoccupiedSensorActive);
			builder.append("drRampUpTemp", this.drRampUpTemp);
			builder.append("drRampUpTime", this.drRampUpTime);
			builder.append("linkRef", this.linkRef);
			builder.append("holdClimateRef", this.holdClimateRef);

			return builder.toString();
		}
	}

	/**
	 * The thermostat Program is a container for the {@link #schedule} and its {@link #climates}.
	 * 
	 * See Core Concepts for details on how the program is structured. The {@link #schedule} property is a two
	 * dimensional array containing the climate names.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Program.shtml">Program</a>
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/technical-notes.shtml">Core Concepts</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Program extends AbstractMessagePart {
		private List<List<String>> schedule;
		private List<Climate> climates;
		private String currentClimateRef;

		/**
		 * @return the schedule object defining the program schedule
		 */
		@JsonProperty("schedule")
		public List<List<String>> getSchedule() {
			return this.schedule;
		}

		/**
		 * @param schedule
		 *            the schedule object defining the program schedule
		 */
		@JsonProperty("schedule")
		public void setSchedule(List<List<String>> schedule) {
			this.schedule = schedule;
		}

		/**
		 * @return the list of {@link Climate} objects defining all the climates in the program schedule
		 */
		@JsonProperty("climates")
		public List<Climate> getClimates() {
			return this.climates;
		}

		/**
		 * @param climates
		 *            the list of {@link Climate} objects defining all the climates in the program schedule
		 */
		@JsonProperty("climates")
		public void setClimates(List<Climate> climates) {
			this.climates = climates;
		}

		/**
		 * @return the currently active climate, identified by its ClimateRef
		 */
		@JsonProperty("currentClimateRef")
		public String getCurrentClimateRef() {
			return this.currentClimateRef;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("schedule", this.schedule);
			builder.append("climates", this.climates);
			builder.append("currentClimateRef", this.currentClimateRef);

			return builder.toString();
		}
	}

	/**
	 * A climate defines a thermostat settings template which is then applied to individual period cells of the
	 * schedule. The result is that if you modify the Climate, all schedule cells which reference that Climate will
	 * automatically be changed.
	 * 
	 * <p>
	 * When adding a Climate it is optional whether you reference the new Climate in the schedule cells in the same
	 * request or not. However, when deleting a Climate (by omitting that entire Climate object from the POST request)
	 * it can not be be deleted if it is still referenced in the schedule cells.
	 * 
	 * <p>
	 * There are three default Climates for each {@link Thermostat}, with possible <code>climateRef</code> values of
	 * "away", "home", and "sleep". There are two default Climates for the EMS thermostat, with possible
	 * <code>climateRef</code> values of "occupied" and "unoccupied". None of these defaults can be deleted and trying
	 * to do so will return an exception. The remaining fields can be modified.
	 * 
	 * <p>
	 * Climates may be modified (you can add, update or remove climates). However, it is important to note that the
	 * <code>climateRef</code> is required and read-only for an existing climate and cannot be changed. The
	 * {@link Climate#name} can be edited so long as it is unique.
	 * 
	 * <p>
	 * If the <code>climateRef</code> for an existing climate is not included in an API call it is assumed this is a net
	 * new climate. The climateRef must always be supplied for the default climates.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Climate.shtml">Climate</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Climate extends AbstractMessagePart {
		private String name;
		private String climateRef;
		private Boolean _isOccupied;
		private Boolean _isOptimized;
		private FanMode coolFan;
		private FanMode heatFan;
		private VentilatorMode vent;
		private Integer ventilatorMinOnTime;
		private String owner;
		private String type;
		private Integer colour;
		private Temperature coolTemp;
		private Temperature heatTemp;

		public Climate(@JsonProperty("name") String name) {
			this.name = name;
		}

		/**
		 * @return the unique climate name. The name may be changed without affecting the program integrity so long as
		 *         uniqueness is maintained.
		 */
		@JsonProperty("name")
		public String getName() {
			return this.name;
		}

		/**
		 * @param name
		 *            the unique climate name. The name may be changed without affecting the program integrity so long
		 *            as uniqueness is maintained.
		 */
		@JsonProperty("name")
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the unique climate identifier. Changing the identifier is not possible and it is generated on the
		 *         server for each climate. If this value is not supplied a new climate will be created. For the default
		 *         climates and existing user created climates the climateRef should be supplied - see note above.
		 */
		@JsonProperty("climateRef")
		public String getClimateRef() {
			return this.climateRef;
		}

		/**
		 * @return a flag indicating whether the property is occupied by persons during this climate
		 */
		@JsonProperty("isOccupied")
		public Boolean isOccupied() {
			return this._isOccupied;
		}

		/**
		 * @param isOccupied
		 *            a flag indicating whether the property is occupied by persons during this climate
		 */
		@JsonProperty("isOccupied")
		public void setIsOccupied(Boolean isOccupied) {
			this._isOccupied = isOccupied;
		}

		/**
		 * @return a flag indicating whether ecobee optimized climate settings are used by this climate
		 */
		@JsonProperty("isOptimized")
		public Boolean isOptimized() {
			return this._isOptimized;
		}

		/**
		 * @param isOptimized
		 *            a flag indicating whether ecobee optimized climate settings are used by this climate
		 */
		@JsonProperty("isOptimized")
		public void setIsOptimized(Boolean isOptimized) {
			this._isOptimized = isOptimized;
		}

		/**
		 * @return the cooling fan mode. Default: on Values: auto, on
		 */
		@JsonProperty("coolFan")
		public FanMode getCoolFan() {
			return this.coolFan;
		}

		/**
		 * @param coolFan
		 *            the cooling fan mode. Default: on Values: auto, on
		 */
		@JsonProperty("coolFan")
		public void setCoolFan(FanMode coolFan) {
			this.coolFan = coolFan;
		}

		/**
		 * @return the heating fan mode. Default: on Values: auto, on
		 */
		@JsonProperty("heatFan")
		public FanMode getHeatFan() {
			return this.heatFan;
		}

		/**
		 * @param heatFan
		 *            the heating fan mode. Default: on Values: auto, on
		 */
		@JsonProperty("heatFan")
		public void setHeatFan(FanMode heatFan) {
			this.heatFan = heatFan;
		}

		/**
		 * @return the ventilator mode. Default: off Values: auto, minontime, on, off
		 */
		@JsonProperty("vent")
		public VentilatorMode getVent() {
			return this.vent;
		}

		/**
		 * @param vent
		 *            the ventilator mode. Default: off Values: auto, minontime, on, off
		 */
		@JsonProperty("vent")
		public void setVent(VentilatorMode vent) {
			this.vent = vent;
		}

		/**
		 * @return the minimum time, in minutes, to run the ventilator each hour
		 */
		@JsonProperty("ventilatorMinOnTime")
		public Integer getVentilatorMinOnTime() {
			return this.ventilatorMinOnTime;
		}

		/**
		 * @param ventilatorMinOnTime
		 *            the minimum time, in minutes, to run the ventilator each hour
		 */
		@JsonProperty("ventilatorMinOnTime")
		public void setVentilatorMinOnTime(Integer ventilatorMinOnTime) {
			this.ventilatorMinOnTime = ventilatorMinOnTime;
		}

		/**
		 * @return the climate owner. Default: system Values: adHoc, demandResponse, quickSave, sensorAction,
		 *         switchOccupancy, system, template, user
		 */
		@JsonProperty("owner")
		public String getOwner() {
			return this.owner;
		}

		/**
		 * @param owner
		 *            the climate owner. Default: system Values: adHoc, demandResponse, quickSave, sensorAction,
		 *            switchOccupancy, system, template, user
		 */
		@JsonProperty("owner")
		public void setOwner(String owner) {
			this.owner = owner;
		}

		/**
		 * @return the type of climate. Default: program Values: calendarEvent, program
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		/**
		 * @param type
		 *            the type of climate. Default: program Values: calendarEvent, program
		 */
		@JsonProperty("type")
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the integer conversion of the HEX color value used to display this climate on the thermostat and on
		 *         the web portal
		 */
		@JsonProperty("colour")
		public Integer getColour() {
			return this.colour;
		}

		/**
		 * @param colour
		 *            the integer conversion of the HEX color value used to display this climate on the thermostat and
		 *            on the web portal
		 */
		@JsonProperty("colour")
		public void setColour(Integer colour) {
			this.colour = colour;
		}

		/**
		 * @return the cool temperature for this climate
		 */
		@JsonProperty("coolTemp")
		public Temperature getCoolTemp() {
			return this.coolTemp;
		}

		/**
		 * @param coolTemp
		 *            the cool temperature for this climate
		 */
		@JsonProperty("coolTemp")
		public void setCoolTemp(Temperature coolTemp) {
			this.coolTemp = coolTemp;
		}

		/**
		 * @return the heat temperature for this climate
		 */
		@JsonProperty("heatTemp")
		public Temperature getHeatTemp() {
			return this.heatTemp;
		}

		/**
		 * @param heatTemp
		 *            the heat temperature for this climate
		 */
		@JsonProperty("heatTemp")
		public void setHeatTemp(Temperature heatTemp) {
			this.heatTemp = heatTemp;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("name", this.name);
			builder.append("climateRef", this.climateRef);
			builder.append("isOccupied", this._isOccupied);
			builder.append("isOptimized", this._isOptimized);
			builder.append("coolFan", this.coolFan);
			builder.append("heatFan", this.heatFan);
			builder.append("vent", this.vent);
			builder.append("ventilatorMinOnTime", this.ventilatorMinOnTime);
			builder.append("owner", this.owner);
			builder.append("type", this.type);
			builder.append("colour", this.colour);
			builder.append("coolTemp", this.coolTemp);
			builder.append("heatTemp", this.heatTemp);

			return builder.toString();
		}
	}

	/**
	 * The HouseDetails object contains the information about the house the thermostat is installed in.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/HouseDetails.shtml">HouseDetails</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class HouseDetails extends AbstractMessagePart {
		private String style;
		private Integer size;
		private Integer numberOfFloors;
		private Integer numberOfRooms;
		private Integer numberOfOccupants;
		private Integer age;

		/**
		 * @return the style of house. Values: other, apartment, condominium, detached, loft, multiPlex, rowHouse,
		 *         semiDetached, townhouse, and 0 for unknown
		 */
		@JsonProperty("style")
		public String getStyle() {
			return this.style;
		}

		/**
		 * @param style
		 *            the style of house. Values: other, apartment, condominium, detached, loft, multiPlex, rowHouse,
		 *            semiDetached, townhouse, and 0 for unknown
		 */
		@JsonProperty("style")
		public void setStyle(String style) {
			this.style = style;
		}

		/**
		 * @return the size of the house in square feet
		 */
		@JsonProperty("size")
		public Integer getSize() {
			return this.size;
		}

		/**
		 * @param size
		 *            the size of the house in square feet
		 */
		@JsonProperty("size")
		public void setSize(Integer size) {
			this.size = size;
		}

		/**
		 * @return the number of floors or levels in the house
		 */
		@JsonProperty("numberOfFloors")
		public Integer getNumberOfFloors() {
			return this.numberOfFloors;
		}

		/**
		 * @param numberOfFloors
		 *            the numberOfFloors to set
		 */
		@JsonProperty("numberOfFloors")
		public void setNumberOfFloors(Integer numberOfFloors) {
			this.numberOfFloors = numberOfFloors;
		}

		/**
		 * @return the number of rooms in the house
		 */
		@JsonProperty("numberOfRooms")
		public Integer getNumberOfRooms() {
			return this.numberOfRooms;
		}

		/**
		 * @param numberOfRooms
		 *            the number of rooms in the house
		 */
		@JsonProperty("numberOfRooms")
		public void setNumberOfRooms(Integer numberOfRooms) {
			this.numberOfRooms = numberOfRooms;
		}

		/**
		 * @return the number of occupants living in the house
		 */
		@JsonProperty("numberOfOccupants")
		public Integer getNumberOfOccupants() {
			return this.numberOfOccupants;
		}

		/**
		 * @param numberOfOccupants
		 *            the number of occupants living in the house
		 */
		@JsonProperty("numberOfOccupants")
		public void setNumberOfOccupants(Integer numberOfOccupants) {
			this.numberOfOccupants = numberOfOccupants;
		}

		/**
		 * @return the age of house in years
		 */
		@JsonProperty("age")
		public Integer getAge() {
			return this.age;
		}

		/**
		 * @param age
		 *            the age of house in years
		 */
		@JsonProperty("age")
		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("style", this.style);
			builder.append("size", this.size);
			builder.append("numberOfFloors", this.numberOfFloors);
			builder.append("numberOfRooms", this.numberOfRooms);
			builder.append("numberOfOccupants", this.numberOfOccupants);
			builder.append("age", this.age);

			return builder.toString();
		}
	}

	/**
	 * The OemCfg object contains information about the OEM specific thermostat.
	 * 
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ThermostatOemCfg extends AbstractMessagePart {

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());

			return builder.toString();
		}
	}

	/**
	 * The Thermostat NotificationSettings object is a container for the configuration of the possible alerts and
	 * reminders which can be generated by the Thermostat.
	 * 
	 * <p>
	 * The NotificationsSettings supports retrieval through a Thermostat GET call, setting the
	 * <code>includeNotificationSettings</code> to <code>true</code> in the {@link Selection}.
	 * 
	 * <p>
	 * The NotificationsSettings object can also be updated using the Thermostat POST method. When POSTing updates to
	 * this object please take a note of the required fields, allowed values, and notes about the email address below.
	 * 
	 * <p>
	 * The type corresponds to the {@link Alert#notificationType} returned when alerts are included in the selection.
	 * See {@link Alert} for more information. When the type is anything other than alert its configuration will be
	 * listed here as part of the NotificationSettings.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/NotificationSettings.shtml">NotificationSettings</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class NotificationSettings extends AbstractMessagePart {
		private List<String> emailAddresses;
		private Boolean emailNotificationsEnabled;
		private List<EquipmentSetting> equipment;
		private List<GeneralSetting> general;
		private List<LimitSetting> limit;

		/**
		 * @return the list of email addresses alerts and reminders will be sent to.
		 */
		@JsonProperty("emailAddresses")
		public List<String> getEmailAddresses() {
			return this.emailAddresses;
		}

		/**
		 * @param emailAddresses
		 *            the list of email addresses alerts and reminders will be sent to. The full list of email addresses
		 *            must be sent in any update request. If any are missing from that list they will be deleted. If an
		 *            empty list is sent, any email addresses will be deleted.
		 */
		@JsonProperty("emailAddresses")
		public void setEmailAddresses(List<String> emailAddresses) {
			this.emailAddresses = emailAddresses;
		}

		/**
		 * @return boolean value representing whether or not alerts and reminders will be sent to the email addresses
		 *         listed above when triggered
		 */
		@JsonProperty("emailNotificationsEnabled")
		public Boolean getEmailNotificationsEnabled() {
			return this.emailNotificationsEnabled;
		}

		/**
		 * @param emailNotificationsEnabled
		 *            boolean value representing whether or not alerts and reminders will be sent to the email addresses
		 *            listed above when triggered
		 */
		@JsonProperty("emailNotificationsEnabled")
		public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
			this.emailNotificationsEnabled = emailNotificationsEnabled;
		}

		/**
		 * @return the list of equipment specific alert and reminder settings
		 */
		@JsonProperty("equipment")
		public List<EquipmentSetting> getEquipment() {
			return this.equipment;
		}

		/**
		 * @param equipment
		 *            the list of equipment specific alert and reminder settings
		 */
		@JsonProperty("equipment")
		public void setEquipment(List<EquipmentSetting> equipment) {
			this.equipment = equipment;
		}

		/**
		 * @return the list of general alert and reminder settings
		 */
		@JsonProperty("general")
		public List<GeneralSetting> getGeneral() {
			return this.general;
		}

		/**
		 * @param general
		 *            the list of general alert and reminder settings
		 */
		@JsonProperty("general")
		public void setGeneral(List<GeneralSetting> general) {
			this.general = general;
		}

		/**
		 * @return the list of limit specific alert and reminder settings
		 */
		@JsonProperty("limit")
		public List<LimitSetting> getLimit() {
			return this.limit;
		}

		/**
		 * @param limit
		 *            the list of limit specific alert and reminder settings
		 */
		@JsonProperty("limit")
		public void setLimit(List<LimitSetting> limit) {
			this.limit = limit;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("emailAddresses", this.emailAddresses);
			builder.append("emailNotificationsEnabled", this.emailNotificationsEnabled);
			builder.append("equipment", this.equipment);
			builder.append("general", this.general);
			builder.append("limit", this.limit);

			return builder.toString();
		}
	}

	/**
	 * The EquipmentSetting object represents the alert/reminder type which is associated with and dependent upon
	 * specific equipment controlled by the Thermostat. It is used when getting/setting the Thermostat
	 * NotificationSettings object.
	 * 
	 * <p>
	 * Note: Only the notification settings for the equipment/devices currently controlled by the Thermostat are
	 * returned during GET request, and only those same settings can be updated using the POST request.
	 * 
	 * <p>
	 * The type corresponds to the {@link Alert#notificationType} returned when alerts are also included in the
	 * selection. See {@link Alert} for more information.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/EquipmentSetting.shtml">EquipmentSetting</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class EquipmentSetting extends AbstractMessagePart {
		private String filterLastChanged; // TODO Jackson 1.9 date handling (@watou)
		private Integer filterLife;
		private String filterLifeUnits;
		private String remindMeDate; // TODO Jackson 1.9 date handling (@watou)
		private Boolean enabled;
		private String type;
		private Boolean remindTechnician;

		public EquipmentSetting(@JsonProperty("type") String type) {
			this.type = type;
		}

		/**
		 * @return the date the filter was last changed for this equipment. String format: YYYY-MM-DD
		 */
		@JsonProperty("filterLastChanged")
		public String getFilterLastChanged() {
			return this.filterLastChanged;
		}

		/**
		 * @param filterLastChanged
		 *            the date the filter was last changed for this equipment. String format: YYYY-MM-DD
		 */
		@JsonProperty("filterLastChanged")
		public void setFilterLastChanged(String filterLastChanged) {
			this.filterLastChanged = filterLastChanged;
		}

		/**
		 * @return the value representing the life of the filter. This value is expressed in month or hour, which is
		 *         specified in the the {@link #filterLifeUnits} property.
		 */
		@JsonProperty("filterLife")
		public Integer getFilterLife() {
			return this.filterLife;
		}

		/**
		 * @param filterLife
		 *            the value representing the life of the filter. This value is expressed in month or hour, which is
		 *            specified in the the {@link #filterLifeUnits} property.
		 */
		@JsonProperty("filterLife")
		public void setFilterLife(Integer filterLife) {
			this.filterLife = filterLife;
		}

		/**
		 * @return the units the {@link #filterLife} field is measured in. Possible values are: month, hour. month has a
		 *         range of 1 - 12. hour has a range of 100 - 1000.
		 */
		@JsonProperty("filterLifeUnits")
		public String getFilterLifeUnits() {
			return this.filterLifeUnits;
		}

		/**
		 * @param filterLifeUnits
		 *            the units the {@link #filterLife} field is measured in. Possible values are: month, hour. month
		 *            has a range of 1 - 12. hour has a range of 100 - 1000.
		 */
		@JsonProperty("filterLifeUnits")
		public void setFilterLifeUnits(String filterLifeUnits) {
			this.filterLifeUnits = filterLifeUnits;
		}

		/**
		 * @return the date the reminder will be triggered. This is a read-only field and cannot be modified through the
		 *         API. The value is calculated and set by the thermostat.
		 */
		@JsonProperty("remindMeDate")
		public String getRemindMeDate() {
			return this.remindMeDate;
		}

		/**
		 * @return boolean value representing whether or not alerts/reminders are enabled for this notification type or
		 *         not
		 */
		@JsonProperty("enabled")
		public Boolean isEnabled() {
			return this.enabled;
		}

		/**
		 * @param enabled
		 *            boolean value representing whether or not alerts/reminders are enabled for this notification type
		 *            or not
		 */
		@JsonProperty("enabled")
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * @return boolean value representing whether or not alerts/reminders should be sent to the
		 *         technician/contractor associated with the thermostat
		 */
		@JsonProperty("remindTechnician")
		public Boolean getRemindTechnician() {
			return this.remindTechnician;
		}

		/**
		 * @param remindTechnician
		 *            boolean value representing whether or not alerts/reminders should be sent to the
		 *            technician/contractor associated with the thermostat.
		 */
		@JsonProperty("remindTechnician")
		public void setRemindTechnician(Boolean remindTechnician) {
			this.remindTechnician = remindTechnician;
		}

		/**
		 * @return the type of notification. Possible values are: hvac, furnaceFilter, humidifierFilter,
		 *         dehumidifierFilter, ventilator, ac, airFilter, airCleaner, uvLamp
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("filterLastChanged", this.filterLastChanged);
			builder.append("filterLife", this.filterLife);
			builder.append("filterLifeUnits", this.filterLifeUnits);
			builder.append("remindMeDate", this.remindMeDate);
			builder.append("enabled", this.enabled);
			builder.append("type", this.type);
			builder.append("remindTechnician", this.remindTechnician);

			return builder.toString();
		}
	}

	/**
	 * The GeneralSetting object represent the General alert/reminder type. It is used when getting/setting the
	 * Thermostat {@link NotificationSettings} object.
	 * 
	 * <p>
	 * The <code>type</code> corresponds to the {@link Alert#notificationType} returned when alerts are included in the
	 * selection. See {@link Alert} for more information.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/GeneralSetting.shtml">GeneralSetting</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class GeneralSetting extends AbstractMessagePart {
		private Boolean enabled;
		private String type;
		private Boolean remindTechnician;

		/**
		 * @param type
		 *            the type of notification. Possible values are: temp
		 */
		public GeneralSetting(@JsonProperty("type") String type) {
			this.type = type;
		}

		/**
		 * @return boolean value representing whether or not alerts/reminders are enabled for this notification type or
		 *         not
		 */
		@JsonProperty("enabled")
		public Boolean isEnabled() {
			return this.enabled;
		}

		/**
		 * @param enabled
		 *            boolean value representing whether or not alerts/reminders are enabled for this notification type
		 *            or not
		 */
		@JsonProperty("enabled")
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * @return boolean value representing whether or not alerts/reminders should be sent to the
		 *         technician/contractor associated with the thermostat
		 */
		@JsonProperty("remindTechnician")
		public Boolean getRemindTechnician() {
			return this.remindTechnician;
		}

		/**
		 * @param remindTechnician
		 *            boolean value representing whether or not alerts/reminders should be sent to the
		 *            technician/contractor associated with the thermostat
		 */
		@JsonProperty("remindTechnician")
		public void setRemindTechnician(Boolean remindTechnician) {
			this.remindTechnician = remindTechnician;
		}

		/**
		 * @return the type of notification. Possible values are: temp
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("enabled", this.enabled);
			builder.append("type", this.type);
			builder.append("remindTechnician", this.remindTechnician);

			return builder.toString();
		}
	}

	/**
	 * The LimitSetting object represents the alert/reminder type which is associated specific values, such as highHeat
	 * or lowHumidity. It is used when getting/setting the Thermostat NotificationSettings object.
	 * 
	 * The type corresponds to the {@link Alert#notificationType} returned when alerts are also included in the
	 * selection. See {@link Alert} for more information.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/LimitSetting.shtml">LimitSetting</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class LimitSetting extends AbstractMessagePart {
		private Integer limit;
		private Boolean enabled;
		private String type;
		private Boolean remindTechnician;

		/**
		 * @param type
		 *            the type of notification. Possible values are: lowTemp, highTemp, lowHumidity, highHumidity,
		 *            auxHeat, auxOutdoor
		 */
		public LimitSetting(@JsonProperty("type") String type) {
			this.type = type;
		}

		/**
		 * @return the value of the limit to set. For temperatures the value is expressed as degrees Fahrenheit,
		 *         multipled by 10. For humidity values are expressed as a percentage from 5 to 95. See here for more
		 *         information.
		 */
		@JsonProperty("limit")
		public Integer getLimit() {
			return this.limit;
		}

		/**
		 * @param limit
		 *            the value of the limit to set. For temperatures the value is expressed as degrees Fahrenheit,
		 *            multipled by 10. For humidity values are expressed as a percentage from 5 to 95. See here for more
		 *            information.
		 */
		@JsonProperty("limit")
		public void setLimit(Integer limit) {
			this.limit = limit;
		}

		/**
		 * @return boolean value representing whether or not alerts/reminders are enabled for this notification type or
		 *         not
		 */
		@JsonProperty("enabled")
		public Boolean isEnabled() {
			return this.enabled;
		}

		/**
		 * @param enabled
		 *            boolean value representing whether or not alerts/reminders are enabled for this notification type
		 *            or not
		 */
		@JsonProperty("enabled")
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		/**
		 * @return boolean value representing whether or not alerts/reminders should be sent to the
		 *         technician/contractor associated with the thermostat
		 */
		@JsonProperty("remindTechnician")
		public Boolean getRemindTechnician() {
			return this.remindTechnician;
		}

		/**
		 * @param remindTechnician
		 *            boolean value representing whether or not alerts/reminders should be sent to the
		 *            technician/contractor associated with the thermostat
		 */
		@JsonProperty("remindTechnician")
		public void setRemindTechnician(Boolean remindTechnician) {
			this.remindTechnician = remindTechnician;
		}

		/**
		 * @return the type of notification. Possible values are: lowTemp, highTemp, lowHumidity, highHumidity, auxHeat,
		 *         auxOutdoor
		 */
		@JsonProperty("type")
		public String getType() {
			return this.type;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("limit", this.limit);
			builder.append("enabled", this.enabled);
			builder.append("type", this.type);
			builder.append("remindTechnician", this.remindTechnician);

			return builder.toString();
		}
	}

	/**
	 * The ThermostatPrivacy object containing the privacy settings for the Thermostat. Note: access to this object is
	 * restricted to callers with implicit authentication.
	 * 
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ThermostatPrivacy extends AbstractMessagePart {

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());

			return builder.toString();
		}
	}

	/**
	 * The Version object contains version information about the thermostat.
	 * 
	 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Version.shtml">Version</a>
	 * @author John Cocula
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Version extends AbstractMessagePart {
		private String thermostatFirmwareVersion;

		/**
		 * @return the thermostat firmware version number
		 */
		@JsonProperty("thermostatFirmwareVersion")
		public String getThermostatFirmwareVersion() {
			return this.thermostatFirmwareVersion;
		}

		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("thermostatFirmwareVersion", this.thermostatFirmwareVersion);

			return builder.toString();
		}
	}
}
