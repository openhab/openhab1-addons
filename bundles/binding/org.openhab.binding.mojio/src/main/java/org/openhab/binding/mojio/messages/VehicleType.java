/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mojio.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * VehicleType is a core type for the responses to the Vehicles mojio request
 *
 * @author Vladimir Pavluk
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleType extends GenericMojioResponse {
  @JsonProperty("Name")
  public String name;
  @JsonProperty("LicensePlate")
  public String licensePlate;
  @JsonProperty("VIN")
  public String VIN;
  @JsonProperty("IgnitionOn")
  public boolean ignitionOn;
  @JsonProperty("FuelLevel")
  public double fuelLevel;
  @JsonProperty("LastLocation")
  public VehicleLocation lastLocation;
  @JsonProperty("LastAcceleration")
  public double lastAcceleration;
  @JsonProperty("LastVirtualOdometer")
  public double lastVirtualOdometer;
  @JsonProperty("LastOdometer")
  public double lastOdometer;
  @JsonProperty("LastRpm")
  public double lastRpm;
  @JsonProperty("LastFuelEfficiency")
  public double lastFuelEfficiency;
  @JsonProperty("LastBatteryVoltage")
  public double batteryVoltage;
  @JsonProperty("LastSpeed")
  public double lastSpeed;
  @JsonProperty("LastAltitude")
  public double lastAltitude;
  @JsonProperty("LastHeading")
  public double lastHeading;
}
