/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * The User object. The User object contains information pertaining to the 
 * User associated with a thermostat.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/User.shtml">Object</a>
 * @author John Cocula
 * @author Ecobee
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AbstractMessagePart {
	private String userName;
	private String displayName;
	private String firstName;
	private String lastName;
	private String honorific;
	private String registerDate;
	private String registerTime;
	private String defaultThermostatIdentifier;
	private String managementRef;
	private String utilityRef;
	private String supportRef;
	private String phoneNumber;

	/**
	 * @return the User login userName. Usually a valid email address.
	 */
	@JsonProperty("userName")
	public String getUserName() {
		return this.userName;
	}

	/**
	 * @return the User display name
	 */
	@JsonProperty("displayName")
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * @param displayName the User display name
	 */
	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the User first name
	 */
	@JsonProperty("firstName")
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the User first name
	 */
	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the User last name
	 */
	@JsonProperty("lastName")
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the User last name
	 */
	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the User title such as Mr. or Mrs.
	 */
	@JsonProperty("honorific")
	public String getHonorific() {
		return this.honorific;
	}

	/**
	 * @return the User date of registration
	 */
	@JsonProperty("registerDate")
	public String getRegisterDate() {
		return this.registerDate;
	}
	/**
	 * @return the User time of registration
	 */
	@JsonProperty("registerTime")
	public String getRegisterTime() {
		return this.registerTime;
	}

	/**
	 * @return the Thermostat identifier this User is associated with
	 */
	@JsonProperty("defaultThermostatIdentifier")
	public String getDefaultThermostatIdentifier() {
		return this.defaultThermostatIdentifier;
	}

	/**
	 * @return the User management reference
	 */
	@JsonProperty("managementRef")
	public String getManagementRef() {
		return this.managementRef;
	}

	/**
	 * @return the User utility reference
	 */
	@JsonProperty("utilityRef")
	public String getUtilityRef() {
		return this.utilityRef;
	}

	/**
	 * @return the User support reference
	 */
	@JsonProperty("supportRef")
	public String getSupportRef() {
		return this.supportRef;
	}

	/**
	 * @return the User phone number
	 */
	@JsonProperty("phoneNumber")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @param phoneNumber the User phone number
	 */
	@JsonProperty("phoneNumber")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("userName", this.userName);
		builder.append("displayName", this.displayName);
		builder.append("firstName", this.firstName);
		builder.append("lastName", this.lastName);
		builder.append("honorific", this.honorific);
		builder.append("registerDate", this.registerDate);
		builder.append("registerTime", this.registerTime);
		builder.append("defaultThermostatIdentifier", this.defaultThermostatIdentifier);
		builder.append("managementRef", this.managementRef);
		builder.append("utilityRef", this.utilityRef);
		builder.append("supportRef", this.supportRef);
		builder.append("phoneNumber", this.phoneNumber);

		return builder.toString();
	}
}
