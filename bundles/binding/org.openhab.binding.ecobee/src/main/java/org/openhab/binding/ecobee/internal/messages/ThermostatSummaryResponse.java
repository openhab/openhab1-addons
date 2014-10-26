/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This response contains a list of thermostat configuration and state revisions. 
 * This request is a light-weight polling method which will only return the revision 
 * numbers for the significant portions of the thermostat data. It is the 
 * responsibility of the caller to store these revisions for future determination 
 * whether changes occurred at the next poll interval.
 * 
 * <p>
 * The intent is to permit the caller to determine whether a thermostat has changed 
 * since the last poll. Retrieval of a whole thermostat including runtime data is 
 * expensive and impractical for large amounts of thermostat such as a management 
 * set hierarchy, especially if nothing has changed. By storing the retrieved 
 * revisions, the caller may determine whether to get a thermostat and which 
 * sections of the thermostat should be retrieved.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/operations/get-thermostat-summary.shtml">GET Thermostat Summary</a>
 * @author John Cocula
 * @author Ecobee
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThermostatSummaryResponse extends ApiResponse {
	private List<Revision> revisionList;
	private List<Status> statusList;

	/**
	 * Objects of this class, when compared to previously returned instances,
	 * allow you to determine if changes have occurred in the thermostat's
	 * program, HVAC mode, settings, configuration, alerts, telemetry, or running
	 * state of connected equipment, and if the thermostat is currently connected to
	 * ecobee's servers.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Revision extends AbstractMessagePart {

		private String thermostatIdentifier;
		private String thermostatName;
		private boolean connected;
		private String thermostatRevision;
		private String alertsRevision;
		private String runtimeRevision;
		private String internalRevision;

		/**
		 * Construct a Revision object.
		 * 
		 * @param csv a colon separated list of values (in order):
		 * 
		 * <table>
		 * <tr>
		 * <td>Value</td>	
		 * <td>Type</td>	
		 * <td>Description</td>
		 * </tr>
		 * <td>
		 * <td>Thermostat Identifier</td>	
		 * <td>String</td>	
		 * <td>The thermostat identifier.</td>
		 * </tr>
		 * <tr>
		 * <td>Thermostat Name</td>	
		 * <td>String</td>	
		 * <td>The thermostat name, otherwise an empty field if one is not set.</td>
		 * </tr>
		 * <tr>
		 * <td>Connected</td>	
		 * <td>Boolean</td>	
		 * <td>Whether the thermostat is currently connected to the ecobee servers.</td>
		 * </tr>
		 * <tr>
		 * <td>Thermostat Revision</td>	
		 * <td>String</td>	
		 * <td>Current thermostat revision. This revision is incremented whenever the 
		 * thermostat program, hvac mode, settings or configuration change.</td>
		 * </tr>
		 * <tr>
		 * <td>Alerts Revision</td>
		 * <td>String</td>
		 * <td>Current revision of the thermostat alerts. This 
		 * revision is incremented whenever a new alert is issued or an alert is 
		 * modified (acknowledged or deferred).</td>
		 * </tr>
		 * <tr>
		 * <td>Runtime Revision</td>	
		 * <td>String</td>	
		 * <td>The current revision of the thermostat runtime settings. This revision 
		 * is incremented whenever the thermostat transmits a new status message, or 
		 * updates the equipment state. The shortest interval this revision may change 
		 * is 3 minutes.</td>
		 * </tr>
		 * <tr>
		 * <td>Interval Revision</td>	
		 * <td>String</td>	
		 * <td>The current revision of the thermostat interval runtime settings. 
		 * This revision is incremented whenever the thermostat transmits a new 
		 * status message. The thermostat does this on a 15 minute interval.</td>
		 * </tr>
		 * </table>
		 * 
		 * <p>
		 * It is the responsibility of the caller to ensure that the sizes 
		 * of the revisionList and statusList are parsed in a flexible manner, 
		 * as additional revisions and statuses will be added with new 
		 * features and functionality.
		 */
		@JsonCreator
		Revision( final String csv ) {
			String[] fields = csv.split(":");
			
			assert fields.length >= 7 : "unable to parse revision";
			
			thermostatIdentifier	= fields[0];
			thermostatName			= fields[1];
			connected				= fields[2].equals("true");
			thermostatRevision		= fields[3];
			alertsRevision			= fields[4];
			runtimeRevision			= fields[5];
			internalRevision		= fields[6];
		}

		/**
		 * @return the thermostat identifier
		 */
		public String getThermostatIdentifier() {
			return this.thermostatIdentifier;
		}

		/**
		 * @return the thermostat name, otherwise an empty string if one is not set.
		 */
		public String getThermostatName() {
			return this.thermostatName;
		}

		/**
		 * Return <code>true</code> if the thermostat is currently connected to the
		 * ecobee servers.
		 * 
		 * @return whether the thermostat is currently connected to the ecobee servers.
		 */
		public boolean isConnected() {
			return this.connected;
		}

		/* 
		 * Revisions are UTC date/time stamps in the format: YYMMDDHHMMSS. 
		 * However, due to possible time drift between the API consumer, 
		 * the server and thermostat, it is recommended that they are treated 
		 * as a string, rather than as a date/time stamp. The recommended method 
		 * to test for revision changes is to simply do a string comparison on 
		 * the previous and current revision. If the strings match, nothing 
		 * changed. Otherwise request the thermostat including the relevant 
		 * information which changed.
		 */

		/**
		 * Return <code>true</code> if the thermostat program, HVAC mode, 
		 * settings or configuration has changed since the some previous 
		 * revision.
		 * 
		 * @param previous the previous status for this thermostat.  May be
		 * <code>null</code>, in which case this method returns <code>true</code>.
		 * @return <code>true</code> if the thermostat program, HVAC mode, 
		 * settings or configuration has changed since the given revision.
		 * @throws IllegalArgumentException if you are attempting to compare
		 * different thermostats.
		 */
		public boolean hasThermostatChanged( final Revision previous ) {
			if (previous == null) return true;
			if (!this.thermostatIdentifier.equals(previous.thermostatIdentifier))
				throw new IllegalArgumentException("comparing different thermostats.");
			else return ! this.thermostatRevision.equals(previous.thermostatRevision);
		}

		/**
		 * Return <code>true</code> if a new alert has been issued or an alert 
		 * has been modified (acknowledged or deferred) since some previous Revision.
		 * 
		 * @param previous the previous revision for this thermostat.  May be
		 * <code>null</code>, in which case this method returns <code>true</code>.
		 * @return <code>true</code> if a new alert has been issued or an alert 
		 * has been modified (acknowledged or deferred) since the given revision.
		 * @throws IllegalArgumentException if you are attempting to compare
		 * different thermostats.
		 */
		public boolean hasAlertsChanged( final Revision previous ) {
			if (previous == null) return true;
			if (!this.thermostatIdentifier.equals(previous.thermostatIdentifier))
				throw new IllegalArgumentException("comparing different thermostats.");
			else return ! this.alertsRevision.equals(previous.alertsRevision);
		}

		/**
		 * Return <code>true</code> if the thermostat has transmitted a new 
		 * status message, or has updated the equipment state since some 
		 * previous revision. The shortest interval this revision may change 
		 * is 3 minutes. 
         *
		 * <p>
		 * {@link #hasTransmittedNewStatus(ThermostatSummaryResponse.Revision)} differs from 
		 * this method in that this method returns <code>true</code> 
		 * when any thermostat runtime information has changed, whereas
		 * {@link #hasTransmittedNewStatus(ThermostatSummaryResponse.Revision)} only returns <code>true</code>
		 * when the thermostat has transmitted its 15 minute interval status message 
		 * to the server. 
		 * This method returns <code>true</code> when the thermostat has sent the 
		 * interval status message every 15 minutes as well as whenever the 
		 * equipment state changes on the thermostat, and it has transmitted that 
		 * information. The equipment message can come at a frequency of 3 minutes. 
		 * When expecting only updates to the thermostat telemetry data, use 
		 * {@link #hasTransmittedNewStatus(ThermostatSummaryResponse.Revision)}.
		 * 
		 * @param previous the previous revision for this thermostat.  May be
		 * <code>null</code>, in which case this method returns <code>true</code>.
		 * @return <code>true</code> if the thermostat has transmitted a new 
		 * status message, or has updated the equipment state since some 
		 * previous Revision. The shortest interval this revision may change 
		 * is 3 minutes. 
		 * @throws IllegalArgumentException if you are attempting to compare
		 * different thermostats.
		 * @see #hasTransmittedNewStatus(ThermostatSummaryResponse.Revision)
		 */
		public boolean hasRuntimeChanged( final Revision previous ) {
			if (previous == null) return true;
			if (!this.thermostatIdentifier.equals(previous.thermostatIdentifier))
				throw new IllegalArgumentException("comparing different thermostats.");
			else return ! this.runtimeRevision.equals(previous.runtimeRevision);
		}

		/**
		 * Return <code>true</code> if the thermostat has transmitted a new 
		 * status message since some previous Revision. The thermostat does 
		 * this on a 15 minute interval.
		 * 
		 * <p>
		 * This method differs from 
		 * {@link #hasRuntimeChanged(ThermostatSummaryResponse.Revision)} in that
		 * {@link #hasRuntimeChanged(ThermostatSummaryResponse.Revision)} returns <code>true</code> 
		 * when any thermostat runtime information has changed, whereas
		 * This method is only <code>true</code> when the thermostat
		 * has transmitted its 15-minute interval status message 
		 * to the server. 
		 * 
		 * {@link #hasRuntimeChanged(ThermostatSummaryResponse.Revision)} returns <code>true</code> 
		 * when the thermostat has transmitted its interval status message
		 * every 15 mins as well as whenever the equipment state changes on
		 * and it has transmitted that information. The equipment message 
		 * can come at a frequency of 3 minutes. 
		 * When expecting only updates to the thermostat telemetry data, use 
		 * this method to detect if changes have occurred.
		 * 
		 * @param previous the previous revision for this thermostat.  May be
		 * <code>null</code>, in which case this method returns <code>true</code>.
		 * @return <code>true</code> if the thermostat has transmitted a new 
		 * status message since some previous revision. The thermostat does 
		 * this on a 15 minute interval.
		 * @throws IllegalArgumentException if you are attempting to compare
		 * different thermostats.
		 * @throws IllegalArgumentException if you are attempting to compare
		 * different thermostats.
		 * @see #hasRuntimeChanged(ThermostatSummaryResponse.Revision)
		 */
		public boolean hasTransmittedNewStatus( final Revision previous ) {
			if (previous == null) return true;
			if (!this.thermostatIdentifier.equals(previous.thermostatIdentifier))
				throw new IllegalArgumentException("comparing different thermostats.");
			else return ! this.internalRevision.equals(previous.internalRevision);
		}
		
		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("thermostatIdentifier", this.thermostatIdentifier);
			builder.append("thermostatName", this.thermostatName);
			builder.append("connected", this.connected ? "true" : "false");
			builder.append("thermostatRevision", this.thermostatRevision);
			builder.append("alertsRevision", this.alertsRevision);
			builder.append("runtimeRevision", this.runtimeRevision);
			builder.append("internalRevision", this.internalRevision);
			
			return builder.toString();			
		}
	}

	/**
	 * A class version of the data returned in each status in the statusList.
	 * 
	 * <p>
	 * The Thermostat Summary can also return the status of the 
	 * equipment controlled by the Thermostat.
	 * The {@link #getRunningEquipment()} lists all equipment
	 * which is currently running. If a specific equipment type is not 
	 * present in the set then it is not running, or "OFF". To 
	 * retrieve this data the {@link Selection} object specified in the request 
	 * should have the {@link Selection#setIncludeEquipmentStatus(Boolean)}
	 * set to <code>true</code>. The default is <code>false</code>.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Status extends AbstractMessagePart {

		private String thermostatIdentifier;
		private Set<String> runningEquipment;
	
		/**
		 * Construct a Status object.
		 * 
		 * @param csv contains a colon separated list of values (in order):
		 * 
		 * <table>
		 * <tr>
		 * <td>Value</td>	
		 * <td>Type</td>	
		 * <td>Description</td>
		 * </tr>
		 * <tr>
		 * <td>Thermostat Identifier</td>	
		 * <td>String</td>	
		 * <td>The thermostat identifier.</td>
		 * </tr>
		 * <tr>
		 * <td>Equipment Status</td>	
		 * <td>String</td>	
		 * <td>If no equipment is currently running no data is returned. 
		 * Possible values are: heatPump, heatPump2, heatPump3, compCool1, compCool2, auxHeat1, 
		 * auxHeat2, auxHeat3, fan, humidifier, dehumidifier, ventilator, economizer, compHotWater, 
		 * auxHotWater.</td>
		 * </tr>
		 * </table>
		 * 
		 */
		@JsonCreator
		Status( final String csv ) {
			String[] fields = csv.split(":");
			
			assert fields.length >= 1 : "unable to parse status";
			
			thermostatIdentifier	= fields[0];
			
			runningEquipment = new HashSet<String>();
			
			if (fields.length >= 2) {
				for (String equip : fields[1].split(",")) {
					if (equip.length() == 0) continue;
					runningEquipment.add(equip);
				}
			}
		}

		/**
		 * Return <code>true</code> if no equipment is currently running.
		 * 
		 * @return <code>true</code> if no equipment is currently running.
		 */
		public boolean isIdle() {
			return runningEquipment.isEmpty();
		}
		
		/**
		 * Return <code>true</code> if any heating equipment is currently running.
		 * 
		 * @return <code>true</code> if any heating equipment is running.
		 */
		public boolean isHeating() {
			return 	runningEquipment.contains("heatPump") ||
					runningEquipment.contains("heatPump2") ||
					runningEquipment.contains("heatPump3") ||
					runningEquipment.contains("auxHeat1") ||
					runningEquipment.contains("auxHeat2") ||
					runningEquipment.contains("auxHeat3");
		}
		
		/**
		 * Return <code>true</code> if any cooling equipment is currently running.
		 * 
		 * @return <code>true</code> if any cooling equipment is currently running.
		 */
		public boolean isCooling() {
			return 	runningEquipment.contains("compCool1") ||
					runningEquipment.contains("compCool2");
		}

		/**
		 * Return <code>true</code> if the named equipment is running.
		 * 
		 * @param equipment the name of the equipment to check.
		 * Possible names are: heatPump, heatPump2, heatPump3, compCool1, compCool2, auxHeat1, 
		 * auxHeat2, auxHeat3, fan, humidifier, dehumidifier, ventilator, economizer, compHotWater, 
		 * auxHotWater.
		 * @return <code>true</code> if the named equipment is running
		 */
		public boolean isRunning(final String equipment) {
			return runningEquipment.contains(equipment);
		}

		/**
		 * @return the thermostat identifier
		 */
		public String getThermostatIdentifier() {
			return this.thermostatIdentifier;
		}

		/**
		 * @return A set of the running equipment.
		 * Possible contents are: heatPump, heatPump2, heatPump3, compCool1, compCool2, auxHeat1, 
		 * auxHeat2, auxHeat3, fan, humidifier, dehumidifier, ventilator, economizer, compHotWater, 
		 * auxHotWater.
		 */
		public Set<String> getRunningEquipment() {
			return this.runningEquipment;
		}
		
		@Override
		public String toString() {
			final ToStringBuilder builder = createToStringBuilder();
			builder.appendSuper(super.toString());
			builder.append("thermostatIdentifier", this.thermostatIdentifier);
			builder.append("runningEquipment", this.runningEquipment);
			
			return builder.toString();			
		}
	}

	/**
	 * @return the list of CSV revision values
	 */
	@JsonProperty("revisionList")
	public List<Revision> getRevisionList() {
		return this.revisionList;
	}
	
	/**
	 * @return the list of CSV status values
	 * 
	 * The statusList is only returned when the request {@link Selection} object has the 
	 * {@link Selection#setIncludeEquipmentStatus(Boolean)} called with <code>true</code>.
	 *  The default is <code>false</code>.
	 */
	@JsonProperty("statusList")
	public List<Status> getStatusList() {
		return this.statusList;
	}
	
	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("revisionList", this.revisionList);
		builder.append("statusList", this.statusList);

		return builder.toString();
	}
}
