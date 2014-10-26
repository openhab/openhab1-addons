/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A Group object defines the group (and the related Group settings) which a 
 * thermostat may belong to. There could be a number of Groups and also a number 
 * of thermostats in each Group. The Group object allows the caller to define the 
 * Group name, which user preferences are shared across all thermostats in that Group, 
 * and indeed which Thermostats are part of that Group.
 * 
 * <p>
 * The result is that if you modify the Group settings, for example set the 
 * <code>synchronizeAlerts</code> flag to true, any {@link Thermostat.Alert} changes made to 
 * any thermostat in that group will be shared with the remaining thermostats 
 * in the same group.
 * 
 * <p>
 * The Grouping algorithm uses a "first group wins" strategy when a {@link Thermostat} is 
 * referenced in multiple groups. What this means in practice is that when the API 
 * request is processed and a Thermostat is referenced in more than one group, that 
 * Thermostat will only be added to the first Group (at head of array) and not to 
 * the others.
 * 
 * <p>
 * If any of the <code>synchronizeXXX</code> fields are not supplied they will default to false. 
 * So to set all to false where previously some were set to true the caller can either 
 * pass all the <code>synchronizeXXX</code> fields explicitly, or pass none and the default will 
 * be set for each.
 * 
 * <p>
 * The Group object may be modified. However, it is important to note that if the 
 * groupRef is not sent by the caller it is assumed that this is a new group, even 
 * if the groupName has not changed, and a new groupRef will be generated and returned. 
 * Therefore when updating a Group the groupRef must always be sent.
 * 
 * <p>
 * Also note that if the thermostats list is not sent, or an empty list is sent, the 
 * Group will effectively be deleted as it will no longer contain any thermostats and 
 * any group information will be lost.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Group.shtml">Group</a>
 * @author John Cocula
 * @author Ecobee
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends AbstractMessagePart {
	private String groupRef;
	private String groupName;
	private Boolean synchronizeAlerts;
	private Boolean synchronizeSystemMode;
	private Boolean synchronizeSchedule;
	private Boolean synchronizeQuickSave;
	private Boolean synchronizeReminders;
	private Boolean synchronizeContractorInfo;
	private Boolean synchronizeUserPreferences;
	private Boolean synchronizeUtilityInfo;
	private Boolean synchronizeLocation;
	private Boolean synchronizeReset;
	private Boolean synchronizeVacation;
	private List<String> thermostats;

	/**
	 * Construct a Group.
	 * 
	 * @param groupName the name for the Group
	 */
	public Group( @JsonProperty("groupName") final String groupName ) {
		this.groupName = groupName;
	}

	/**
	 * @return the unique reference Id for the Group. 
	 */
	@JsonProperty("groupRef")
	public String getGroupRef() {
		return this.groupRef;
	}

	/**
	 * @param groupRef the unique reference Id for the Group. 
	 * If not supplied in the POST call, and new groupRef will be generated.
	 */
	@JsonProperty("groupRef")
	public void setGroupRef(final String groupRef) {
		this.groupRef = groupRef;
	}
	
	/**
	 * @return the name for the Group
	 */
	@JsonProperty("groupName")
	public String getGroupName() {
		return this.groupName;
	}

	/**
	 * @return flag for whether to synchronize {@link Thermostat.Alert}s with all other 
	 * {@link Thermostat}s in the Group. Default is false.
	 */
	@JsonProperty("synchronizeAlerts")
	public Boolean getSynchronizeAlerts() {
		return this.synchronizeAlerts;
	}

	/**
	 * @param synchronizeAlerts flag for whether to synchronize {@link Thermostat.Alert}s 
	 * with all other {@link Thermostat}s in the Group. Default is false.
	 */
	@JsonProperty("synchronizeAlerts")
	public void setSynchronizeAlerts(final Boolean synchronizeAlerts) {
		this.synchronizeAlerts = synchronizeAlerts;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat mode with all other Thermostats 
	 * in the Group. Default is false.
	 */
	@JsonProperty("synchronizeSystemMode")
	public Boolean getSynchronizeSystemMode() {
		return this.synchronizeSystemMode;
	}

	/**
	 * @param synchronizeSystemMode flag for whether to synchronize the Thermostat mode with 
	 * all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeSystemMode")
	public void setSynchronizeSystemMode(final Boolean synchronizeSystemMode) {
		this.synchronizeSystemMode = synchronizeSystemMode;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat schedule/Program details with 
	 * all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeSchedule")
	public Boolean getSynchronizeSchedule() {
		return this.synchronizeSchedule;
	}

	/**
	 * @param synchronizeSchedule flag for whether to synchronize the Thermostat schedule/Program 
	 * details with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeSchedule")
	public void setSynchronizeSchedule(final Boolean synchronizeSchedule) {
		this.synchronizeSchedule = synchronizeSchedule;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat quick save settings with all other 
	 * Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeQuickSave")
	public Boolean getSynchronizeQuickSave() {
		return this.synchronizeQuickSave;
	}

	/**
	 * @param synchronizeQuickSave flag for whether to synchronize the Thermostat quick save settings 
	 * with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeQuickSave")
	public void setSynchronizeQuickSave(final Boolean synchronizeQuickSave) {
		this.synchronizeQuickSave = synchronizeQuickSave;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat reminders with all other Thermostats 
	 * in the Group. Default is false.
	 */
	@JsonProperty("synchronizeReminders")
	public Boolean getSynchronizeReminders() {
		return this.synchronizeReminders;
	}

	/**
	 * @param synchronizeReminders flag for whether to synchronize the Thermostat reminders 
	 * with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeReminders")
	public void setSynchronizeReminders(final Boolean synchronizeReminders) {
		this.synchronizeReminders = synchronizeReminders;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat Technician/Contractor 
	 * Information with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeContractorInfo")
	public Boolean getSynchronizeContractorInfo() {
		return this.synchronizeContractorInfo;
	}

	/**
	 * @param synchronizeContractorInfo flag for whether to synchronize the Thermostat 
	 * Technician/Contractor Information with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeContractorInfo")
	public void setSynchronizeContractorInfo(final Boolean synchronizeContractorInfo) {
		this.synchronizeContractorInfo = synchronizeContractorInfo;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat user preferences with all other 
	 * Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeUserPreferences")
	public Boolean getSynchronizeUserPreferences() {
		return this.synchronizeUserPreferences;
	}

	/**
	 * @param synchronizeUserPreferences tflag for whether to synchronize the Thermostat user 
	 * preferences with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeUserPreferences")
	public void setSynchronizeUserPreferences(final Boolean synchronizeUserPreferences) {
		this.synchronizeUserPreferences = synchronizeUserPreferences;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat utility information with all 
	 * other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeUtilityInfo")
	public Boolean getSynchronizeUtilityInfo() {
		return this.synchronizeUtilityInfo;
	}

	/**
	 * @param synchronizeUtilityInfo flag for whether to synchronize the Thermostat utility 
	 * information with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeUtilityInfo")
	public void setSynchronizeUtilityInfo(final Boolean synchronizeUtilityInfo) {
		this.synchronizeUtilityInfo = synchronizeUtilityInfo;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat Location with all other 
	 * Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeLocation")
	public Boolean getSynchronizeLocation() {
		return this.synchronizeLocation;
	}

	/**
	 * @param synchronizeLocation flag for whether to synchronize the Thermostat Location 
	 * with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeLocation")
	public void setSynchronizeLocation(final Boolean synchronizeLocation) {
		this.synchronizeLocation = synchronizeLocation;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat reset with all other 
	 * Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeReset")
	public Boolean getSynchronizeReset() {
		return this.synchronizeReset;
	}

	/**
	 * @param synchronizeReset flag for whether to synchronize the Thermostat reset 
	 * with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeReset")
	public void setSynchronizeReset(final Boolean synchronizeReset) {
		this.synchronizeReset = synchronizeReset;
	}

	/**
	 * @return flag for whether to synchronize the Thermostat vacation Program with 
	 * all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeVacation")
	public Boolean getSynchronizeVacation() {
		return this.synchronizeVacation;
	}

	/**
	 * @param synchronizeVacation flag for whether to synchronize the Thermostat 
	 * vacation Program with all other Thermostats in the Group. Default is false.
	 */
	@JsonProperty("synchronizeVacation")
	public void setSynchronizeVacation(final Boolean synchronizeVacation) {
		this.synchronizeVacation = synchronizeVacation;
	}

	/**
	 * @return the list of Thermostat identifiers which belong to the group. 
	 * If an empty list is sent the Group will be deleted.
	 */
	@JsonProperty("thermostats")
	public List<String> getThermostats() {
		return this.thermostats;
	}

	/**
	 * @param thermostats the list of Thermostat identifiers which belong to the group. 
	 * If an empty list is sent the Group will be deleted.
	 */
	@JsonProperty("thermostats")
	public void setThermostats(final List<String> thermostats) {
		this.thermostats = thermostats;
	}
	
	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("groupRef", this.groupRef);
		builder.append("groupName", this.groupName);
		builder.append("synchronizeAlerts", this.synchronizeAlerts);
		builder.append("synchronizeSystemMode", this.synchronizeSystemMode);
		builder.append("synchronizeSchedule", this.synchronizeSchedule);
		builder.append("synchronizeQuickSave", this.synchronizeQuickSave);
		builder.append("synchronizeReminders", this.synchronizeReminders);
		builder.append("synchronizeContractorInfo", this.synchronizeContractorInfo);
		builder.append("synchronizeUserPreferences", this.synchronizeUserPreferences);
		builder.append("synchronizeUtilityInfo", this.synchronizeUtilityInfo);
		builder.append("synchronizeLocation", this.synchronizeLocation);
		builder.append("synchronizeReset", this.synchronizeReset);
		builder.append("synchronizeVacation", this.synchronizeVacation);
		builder.append("thermostats", this.thermostats);

		return builder.toString();
	}
}
