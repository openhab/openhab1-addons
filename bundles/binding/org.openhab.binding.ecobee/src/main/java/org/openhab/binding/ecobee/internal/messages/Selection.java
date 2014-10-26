/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import static org.apache.commons.lang.StringUtils.join;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * The selection object defines the resources and information to return as part
 * of a response. The selection is required in all requests however meaning of
 * some selection fields is only meaningful to certain types of requests.
 * 
 * <p>
 * The <code>selectionType</code> parameter defines the type of selection to
 * perform. The <code>selectionMatch</code> specifies the matching criteria for
 * the type specified.
 * 
 * <table>
 * <tr>
 * <td>Selection Type</td>
 * <td>Account Type</td>
 * <td>Selection Match Example</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>registered</td>
 * <td>Smart only.</td>
 * <td>match is not used.</td>
 * <td>When this is set the thermostats registered to the current user will be
 * returned. This is only usable with Smart thermostats registered to a user. It
 * does not work on EMS thermostats and may not be used by a Utility who is not
 * the owner of thermostats.</td>
 * </tr>
 * <tr>
 * <td>thermostats</td>
 * <td>All accounts</td>
 * <td>identifier1,identifier2,etc...</td>
 * <td>Select only those thermostats listed in the CSV match criteria. No spaces
 * in the CSV string. There is a limit of 25 identifiers per request.</td>
 * </tr>
 * <tr>
 * <td>managementSet</td>
 * <td>EMS/Utility only.</td>
 * <td>/Toronto/Campus/BuildingA</td>
 * <td>Selects all thermostats for a given management set defined by the
 * Management/Utility account. This is only available to Management/Utility
 * accounts. "/" is the root, represented by the "My Sets" set.</td>
 * </tr>
 * </table>
 * 
 * @see <a
 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Selection.shtml">Selection</a>
 * @author John Cocula
 * @author Ecobee
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Selection extends AbstractMessagePart {
	public static final String REGISTERED_WILDCARD = "*";
	public static final String MANAGEMENT_SET_DELIMITER = "/";

	private SelectionType selectionType;
	private String selectionMatch;
	private Boolean includeRuntime;
	private Boolean includeExtendedRuntime;
	private Boolean includeElectricity;
	private Boolean includeSettings;
	private Boolean includeLocation;
	private Boolean includeProgram;
	private Boolean includeEvents;
	private Boolean includeDevice;
	private Boolean includeTechnician;
	private Boolean includeUtility;
	private Boolean includeManagement;
	private Boolean includeAlerts;
	private Boolean includeWeather;
	private Boolean includeHouseDetails;
	private Boolean includeOemCfg;
	private Boolean includeEquipmentStatus;
	private Boolean includeNotificationSettings;
	private Boolean includePrivacy;
	private Boolean includeVersion;

	/**
	 * The <code>SelectionType</code> defines the type of selection to perform.
	 * 
	 * @see <a
	 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Selection.shtml">Selection
	 *      Object</a>
	 * @author John Cocula
	 * @author Ecobee
	 */
	public static enum SelectionType {

		/**
		 * No doc found.
		 */
		NONE("none"),

		/**
		 * All accounts.
		 * 
		 * <p>
		 * Select only those thermostats listed in the CSV match criteria. No
		 * spaces in the CSV string. There is a limit of 25 identifiers per
		 * request.
		 */
		THERMOSTATS("thermostats"),

		/**
		 * Smart only.
		 * 
		 * <p>
		 * When this is set the thermostats registered to the current user will
		 * be returned. This is only usable with Smart thermostats registered to
		 * a user. It does not work on EMS thermostats and may not be used by a
		 * Utility who is not the owner of thermostats.
		 */
		REGISTERED("registered"),

		/**
		 * No doc found.
		 */
		USER("user"),

		/**
		 * EMS/Utility only.
		 * 
		 * <p>
		 * Selects all thermostats for a given management set defined by the
		 * Management/Utility account. This is only available to
		 * Management/Utility accounts. "/" is the root, represented by the
		 * "My Sets" set.
		 */
		MANAGEMENT_SET("managementSet");

		private final String type;

		private SelectionType(final String type) {
			this.type = type;
		}

		@JsonCreator
		public static SelectionType forValue(String v) {
			for (SelectionType st : SelectionType.values()) {
				if (st.type.equals(v)) {
					return st;
				}
			}
			throw new IllegalArgumentException("Invalid selection type: " + v);
		}

		@Override
		@JsonValue
		public String toString() {
			return this.type;
		}
	}

	/**
	 * Give a selectionMatch string, infer what kind of selectionType should be
	 * used.
	 * 
	 * @param selectionMatch
	 *            the match string to inspect
	 * @returns the selectionType to be used with the selectionMatch
	 */
	private SelectionType inferSelectionType(String selectionMatch) {
		SelectionType selectionType;

		if (REGISTERED_WILDCARD.equals(selectionMatch)) {
			selectionType = SelectionType.REGISTERED;
		} else if (selectionMatch.startsWith(MANAGEMENT_SET_DELIMITER)) {
			selectionType = SelectionType.MANAGEMENT_SET;
		} else {
			selectionType = SelectionType.THERMOSTATS;
		}
		return selectionType;
	}

	/**
	 * Return <code>true</code> if the given string matches the known format for
	 * thermostat identifiers.
	 * 
	 * @param thermostatIdentifier the string to test
	 * @return <code>true</code> if the given string matches the known format
	 *         for thermostat identifiers.
	 */
	public static boolean isThermostatIdentifier(String thermostatIdentifier) {
		return thermostatIdentifier.matches("[0-9]+");
	}

	/**
	 * Construct a Selection object using a single <code>selectionMatch</code>,
	 * and inferring the <code>selectionType</code> from the
	 * <code>selectionMatch</code>.
	 * 
	 * @param selectionMatch
	 *            based on the syntax, infer the selectionType
	 */
	public Selection(@JsonProperty("selectionMatch") final String selectionMatch) {
		this.selectionType = inferSelectionType(selectionMatch);
		this.selectionMatch = selectionMatch;
	}

	/**
	 * Construct a Selection object.
	 * 
	 * @param selectionType
	 *            the type of match data supplied.
	 * @param selectionMatch
	 */
	public Selection(
			@JsonProperty("selectionType") final SelectionType selectionType,
			@JsonProperty("selectionMatch") final String selectionMatch) {
		this.selectionType = selectionType;
		this.selectionMatch = selectionMatch;
	}

	/**
	 * @return the type of match data supplied.
	 */
	@JsonProperty("selectionType")
	public SelectionType getSelectionType() {
		return this.selectionType;
	}

	/**
	 * @param selectionType
	 *            the type of match data supplied.
	 */
	@JsonProperty("selectionType")
	public void setSelectionType(final SelectionType selectionType) {
		this.selectionType = selectionType;
	}

	/**
	 * @return the match data based on selectionType (e.g. a list of thermostat
	 *         identifiers in the case of a selectionType of thermostats)
	 */
	@JsonProperty("selectionMatch")
	public String getSelectionMatch() {
		return this.selectionMatch;
	}

	/**
	 * @param selectionMatch
	 *            the match data based on selectionType (e.g. a list of
	 *            thermostat identifiers in the case of a
	 *            <code>selectionType</code> of <code>"thermostats"</code>)
	 */
	@JsonProperty("selectionMatch")
	public void setSelectionMatch(final String selectionMatch) {
		this.selectionMatch = selectionMatch;
	}

	/**
	 * @param thermostatIdentifiers
	 *            set a list of thermostat identifiers as the
	 *            <code>selectionMatch</code> and set the
	 *            <code>selectionType</code> to
	 *            {@code SelectionType.THERMOSTATS}.
	 */
	public void setSelectionMatch(final Set<String> thermostatIdentifiers) {
		this.selectionType = SelectionType.THERMOSTATS;
		this.selectionMatch = join(thermostatIdentifiers, ',');
	}

	/**
	 * @return include the {@link Thermostat.Runtime} object. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeRuntime")
	public Boolean getIncludeRuntime() {
		return this.includeRuntime;
	}

	/**
	 * @param includeRuntime
	 *            include the thermostat {@link Thermostat.Runtime} object. If
	 *            not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeRuntime")
	public void setIncludeRuntime(final Boolean includeRuntime) {
		this.includeRuntime = includeRuntime;
	}

	/**
	 * @return include the extended thermostat runtime object. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeExtendedRuntime")
	public Boolean getIncludeExtendedRuntime() {
		return this.includeExtendedRuntime;
	}

	/**
	 * @param includeExtendedRuntime
	 *            include the @{link Thermostat.ExtendedRuntime} object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeExtendedRuntime")
	public void setIncludeExtendedRuntime(final Boolean includeExtendedRuntime) {
		this.includeExtendedRuntime = includeExtendedRuntime;
	}

	/**
	 * @return include the {@link Thermostat.Electricity} readings object. If
	 *         not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeElectricity")
	public Boolean getIncludeElectricity() {
		return this.includeElectricity;
	}

	/**
	 * @param includeElectricity
	 *            include the {@link Thermostat.Electricity} readings object. If
	 *            not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeElectricity")
	public void setIncludeElectricity(final Boolean includeElectricity) {
		this.includeElectricity = includeElectricity;
	}

	/**
	 * @return include the {@link Thermostat.Settings} object. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeSettings")
	public Boolean getIncludeSettings() {
		return this.includeSettings;
	}

	/**
	 * @param includeSettings
	 *            include the {@link Thermostat.Settings} object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeSettings")
	public void setIncludeSettings(final Boolean includeSettings) {
		this.includeSettings = includeSettings;
	}

	/**
	 * @return include the {@link Thermostat.Location} object. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeLocation")
	public Boolean getIncludeLocation() {
		return this.includeLocation;
	}

	/**
	 * @param includeLocation
	 *            include the {@link Thermostat.Location} object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeLocation")
	public void setIncludeLocation(final Boolean includeLocation) {
		this.includeLocation = includeLocation;
	}

	/**
	 * @return include the {@link Thermostat.Program} object. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeProgram")
	public Boolean getIncludeProgram() {
		return this.includeProgram;
	}

	/**
	 * @param includeProgram
	 *            include the {@link Thermostat.Program} object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeProgram")
	public void setIncludeProgram(final Boolean includeProgram) {
		this.includeProgram = includeProgram;
	}

	/**
	 * @return include the {@link Thermostat.Event}s calendar objects. If not
	 *         specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeEvents")
	public Boolean getIncludeEvents() {
		return this.includeEvents;
	}

	/**
	 * @param includeEvents
	 *            include the {@link Thermostat.Event}s calendar objects. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeEvents")
	public void setIncludeEvents(final Boolean includeEvents) {
		this.includeEvents = includeEvents;
	}

	/**
	 * @return include the {@link Thermostat.Device} configuration objects. If
	 *         not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeDevice")
	public Boolean getIncludeDevice() {
		return this.includeDevice;
	}

	/**
	 * @param includeDevice
	 *            include the {@link Thermostat.Device} configuration objects.
	 *            If not specified, defaults to false.
	 */
	@JsonProperty("includeDevice")
	public void setIncludeDevice(final Boolean includeDevice) {
		this.includeDevice = includeDevice;
	}

	/**
	 * @return include the {@link Thermostat.Technician} object. If not
	 *         specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeTechnician")
	public Boolean getIncludeTechnician() {
		return this.includeTechnician;
	}

	/**
	 * @param includeTechnician
	 *            include the {@link Thermostat.Technician} object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeTechnician")
	public void setIncludeTechnician(final Boolean includeTechnician) {
		this.includeTechnician = includeTechnician;
	}

	/**
	 * @return include the {@link Thermostat.Utility} company object. If not
	 *         specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeUtility")
	public Boolean getIncludeUtility() {
		return this.includeUtility;
	}

	/**
	 * @param includeUtility
	 *            include the {@link Thermostat.Utility} company object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeUtility")
	public void setIncludeUtility(final Boolean includeUtility) {
		this.includeUtility = includeUtility;
	}

	/**
	 * @return include the {@link Thermostat.Management} company object. If not
	 *         specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeManagement")
	public Boolean getIncludeManagement() {
		return this.includeManagement;
	}

	/**
	 * @param includeManagement
	 *            include the {@link Thermostat.Management} company object. If
	 *            not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeManagement")
	public void setIncludeManagement(final Boolean includeManagement) {
		this.includeManagement = includeManagement;
	}

	/**
	 * @return include the unacknowledged {@link Thermostat.Alert} objects. If
	 *         not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeAlerts")
	public Boolean getIncludeAlerts() {
		return this.includeAlerts;
	}

	/**
	 * @param includeAlerts
	 *            include the unacknowledged {@link Thermostat.Alert} objects.
	 *            If not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeAlerts")
	public void setIncludeAlerts(final Boolean includeAlerts) {
		this.includeAlerts = includeAlerts;
	}

	/**
	 * @return include the current {@link Thermostat.Weather} forecast object.
	 *         If not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeWeather")
	public Boolean getIncludeWeather() {
		return this.includeWeather;
	}

	/**
	 * @param includeWeather
	 *            include the current {@link Thermostat.Weather} forecast
	 *            object. If not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeWeather")
	public void setIncludeWeather(final Boolean includeWeather) {
		this.includeWeather = includeWeather;
	}

	/**
	 * @return include the current {@link Thermostat.HouseDetails} object. If
	 *         not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeHouseDetails")
	public Boolean getIncludeHouseDetails() {
		return this.includeHouseDetails;
	}

	/**
	 * @param includeHouseDetails
	 *            include the current {@link Thermostat.HouseDetails} object. If
	 *            not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeHouseDetails")
	public void setIncludeHouseDetails(final Boolean includeHouseDetails) {
		this.includeHouseDetails = includeHouseDetails;
	}

	/**
	 * @return include the current thermostat OemCfg object. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeOemCfg")
	public Boolean getIncludeOemCfg() {
		return this.includeOemCfg;
	}

	/**
	 * @param includeOemCfg
	 *            include the current thermostat OemCfg object. If not
	 *            specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeOemCfg")
	public void setIncludeOemCfg(final Boolean includeOemCfg) {
		this.includeOemCfg = includeOemCfg;
	}

	/**
	 * @return include the current thermostat equipment status information. If
	 *         not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeEquipmentStatus")
	public Boolean getIncludeEquipmentStatus() {
		return this.includeEquipmentStatus;
	}

	/**
	 * @param includeEquipmentStatus
	 *            include the current thermostat equipment status information.
	 *            If not specified, defaults to <code>false</code>.
	 */
	@JsonProperty("includeEquipmentStatus")
	public void setIncludeEquipmentStatus(final Boolean includeEquipmentStatus) {
		this.includeEquipmentStatus = includeEquipmentStatus;
	}

	/**
	 * @return include the current alert and reminders
	 *         {@link Thermostat.NotificationSettings}. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeNotificationSettings")
	public Boolean getIncludeNotificationSettings() {
		return this.includeNotificationSettings;
	}

	/**
	 * @param includeNotificationSettings
	 *            include the current alert and reminders
	 *            {@link Thermostat.NotificationSettings}. If not specified,
	 *            defaults to <code>false</code>.
	 */
	@JsonProperty("includeNotificationSettings")
	public void setIncludeNotificationSettings(
			final Boolean includeNotificationSettings) {
		this.includeNotificationSettings = includeNotificationSettings;
	}

	/**
	 * @return include the current thermostat privacy settings. Note: access to
	 *         this object is restricted to callers with implicit
	 *         authentication, setting this value to true without proper
	 *         credentials will result in an authentication exception.
	 */
	@JsonProperty("includePrivacy")
	public Boolean getIncludePrivacy() {
		return this.includePrivacy;
	}

	/**
	 * @param includePrivacy
	 *            include the current thermostat privacy settings. Note: access
	 *            to this object is restricted to callers with implicit
	 *            authentication, setting this value to true without proper
	 *            credentials will result in an authentication exception.
	 */
	@JsonProperty("includePrivacy")
	public void setIncludePrivacy(final Boolean includePrivacy) {
		this.includePrivacy = includePrivacy;
	}

	/**
	 * @return include the {@link Thermostat.Version}. If not specified,
	 *         defaults to <code>false</code>.
	 */
	@JsonProperty("includeVersion")
	public Boolean getIncludeVersion() {
		return this.includeVersion;
	}

	/**
	 * @param includeVersion
	 *            include the {@link Thermostat.Version}. If not specified,
	 *            defaults to <code>false</code>.
	 */
	@JsonProperty("includeVersion")
	public void setIncludeVersion(final Boolean includeVersion) {
		this.includeVersion = includeVersion;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("selectionType", this.selectionType);
		builder.append("selectionMatch", this.selectionMatch);
		builder.append("includeRuntime", this.includeRuntime);
		builder.append("includeExtendedRuntime", this.includeExtendedRuntime);
		builder.append("includeElectricity", this.includeElectricity);
		builder.append("includeSettings", this.includeSettings);
		builder.append("includeLocation", this.includeLocation);
		builder.append("includeProgram", this.includeProgram);
		builder.append("includeEvents", this.includeEvents);
		builder.append("includeDevice", this.includeDevice);
		builder.append("includeTechnician", this.includeTechnician);
		builder.append("includeUtility", this.includeUtility);
		builder.append("includeManagement", this.includeManagement);
		builder.append("includeAlerts", this.includeAlerts);
		builder.append("includeWeather", this.includeWeather);
		builder.append("includeHouseDetails", this.includeHouseDetails);
		builder.append("includeOemCfg", this.includeOemCfg);
		builder.append("includeEquipmentStatus", this.includeEquipmentStatus);
		builder.append("includeNotificationSettings",
				this.includeNotificationSettings);
		builder.append("includePrivacy", this.includePrivacy);
		builder.append("includeVersion", this.includeVersion);

		return builder.toString();
	}
}
