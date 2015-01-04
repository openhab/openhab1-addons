/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal.messages;

import java.util.Arrays;
import java.util.List;
import org.openhab.binding.dsmr.internal.DSMRMeterType;
import org.openhab.binding.dsmr.internal.DSMRVersion;
import org.openhab.binding.dsmr.internal.cosem.CosemDate;
import org.openhab.binding.dsmr.internal.cosem.CosemFloat;
import org.openhab.binding.dsmr.internal.cosem.CosemInteger;
import org.openhab.binding.dsmr.internal.cosem.CosemString;
import org.openhab.binding.dsmr.internal.cosem.CosemValueDescriptor;

/**
 * List of OBISMsgType
 * <p>
 * Each OBISMsgType consists of the following attributes:
 * <p>
 * <ul>
 * <li>OBIS Identifier (reduced form)
 * <li>Applicable {@link DSMRMeterType}
 * <li>Applicable {@link DSMRVersion}
 * <li>Description of the values (See {@link CosemValueDescriptor})
 * <li>Human readable description
 * </ul>
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public enum OBISMsgType {
	UNKNOWN("",  
			DSMRMeterType.NA, 
			new CosemValueDescriptor(CosemString.class, "", ""),
			DSMRVersion.NONE, 
			"Unknown OBIS message type"),
			
	/* General messages (DSMR V4) */
	P1_VERSION_OUTPUT_V4("1-3:0.2.8", 
			DSMRMeterType.NA, 
			new CosemValueDescriptor(CosemString.class, "", "P1VersionOutput"),
			DSMRVersion.V4_VERSIONS,
			"Version information for P1 output"), 
	P1_TIMESTAMP("0-0:1.0.0", 
			DSMRMeterType.NA, 
			new CosemValueDescriptor(CosemDate.class, "", "P1Timestamp"),
			DSMRVersion.V4_VERSIONS, 
			"Timestamp of the P1 output"),

	/* Electricity Meter */
	EMETER_EQUIPMENT_IDENTIFIER_V2_X("0-0:42.0.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eEquipmentId"),
			DSMRVersion.V2_VERSIONS,
			"Equipment identifier DSMR v2.x"), 
	EMETER_EQUIPMENT_IDENTIFIER("0-0:96.1.1",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eEquipmentId"),
			DSMRVersion.V30_UP, 
			"Equipment identifier"), 
	EMETER_DELIVERY_TARIFF1("1-0:1.8.1", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eDeliveryTariff1"),
			DSMRVersion.ALL_VERSIONS, 
			"Total meter delivery tariff 1"), 
	EMETER_DELIVERY_TARIFF2("1-0:1.8.2",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eDeliveryTariff2"),
			DSMRVersion.ALL_VERSIONS,
			"Total meter delivery tariff 2"), 
	EMETER_PRODUCTION_TARIFF1("1-0:2.8.1",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eProductionTariff1"),
			DSMRVersion.ALL_VERSIONS, 
			"Total meter production tariff 1"), 
	EMETER_PRODUCTION_TARIFF2("1-0:2.8.2", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eProductionTariff2"),
			DSMRVersion.ALL_VERSIONS,
			"Total meter production tariff 2"), 
	EMETER_TARIFF_INDICATOR("0-0:96.14.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eTariffIndicator"),
			DSMRVersion.ALL_VERSIONS, 
			"Tariff indicator"), 
	EMETER_ACTUAL_DELIVERY("1-0:1.7.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eActualDelivery"),
			DSMRVersion.ALL_VERSIONS, 
			"Actual power delivery"), 
	EMETER_ACTUAL_PRODUCTION("1-0:2.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eActualProduction"),
			DSMRVersion.ALL_VERSIONS,
			"Actual power production"), 
	EMETER_TRESHOLD_V2_1("1-0:17.0.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "A", "eTreshold"),
			new DSMRVersion[] { DSMRVersion.V21 },
			"The actual threshold Electricity in A (DSMR v2.1)"), 
	EMETER_TRESHOLD("0-0:17.0.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "A", "eTreshold"),
			new DSMRVersion[] { DSMRVersion.V22, DSMRVersion.V30 },
			"The actual threshold Electricity in A (DSMR v2.2 / v3)"), 
	EMETER_TRESHOLD_V4("0-0:17.0.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eTreshold"),
			DSMRVersion.V4_VERSIONS,
			"The actual threshold Electricity in kW (DSMR v4)"), 
	EMETER_SWITCH_POSITION_V2_1("1-0:96.3.10",  
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "eSwitchPosition"),
			new DSMRVersion[] { DSMRVersion.V21 },
			"Actual switch position Electricity DSMR v2.1"), 
	EMETER_SWITCH_POSITION_V2_2("0-0:24.4.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "eSwitchPosition"),
			new DSMRVersion[] { DSMRVersion.V22 },
			"Actual switch position Electricity DSMR v2.2"), 
	EMETER_SWITCH_POSITION("0-0:96.3.10",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "eSwitchPosition"),
			DSMRVersion.V30_UP, 
			"Actual switch position Electricity"), 
	EMETER_POWER_FAILURES("0-0:96.7.21",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "ePowerFailures"),
			DSMRVersion.V4_VERSIONS,
			"Number of Power failures"), 
	EMETER_LONG_POWER_FAILURES("0-0:96.7.9",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eLongPowerFailures"),
			DSMRVersion.V4_VERSIONS,
			"Number of Long Power failures"),
	EMETER_POWER_FAILURE_LOG("1-0:99.97.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor[] { 
				new CosemValueDescriptor(CosemInteger.class, "", "eNumberOfLogEntries"),
				new CosemValueDescriptor(CosemString.class, "", ""),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure1"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure1"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure2"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure2"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure3"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure3"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure4"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure4"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure5"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure5"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure6"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure6"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure7"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure7"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure8"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure8"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure9"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure9"),
				new CosemValueDescriptor(CosemDate.class, "", "eDatePowerFailure10"),
				new CosemValueDescriptor(CosemInteger.class, "s", "eDurationPowerFailure10")},
			DSMRVersion.V4_VERSIONS,
			"Power Failure event log"),
	EMETER_VOLTAGE_SAGS_L1("1-0:32.32.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSagsL1"),
			DSMRVersion.V4_VERSIONS,
			"Number of voltage sags L1"), 
	EMETER_VOLTAGE_SAGS_L2("1-0:52.32.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSagsL2"),
			DSMRVersion.V4_VERSIONS,
			"Number of voltage sags L2"), 
	EMETER_VOLTAGE_SAGS_L3("1-0:72.32.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSagsL3"),
			DSMRVersion.V4_VERSIONS,
			"Number of voltage sags L3"), 
	EMETER_VOLTAGE_SWELLS_L1("1-0:32.36.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSwellsL1"),
			DSMRVersion.V4_VERSIONS,
			"Number of voltage swells L1"), 
	EMETER_VOLTAGE_SWELLS_L2("1-0:52.36.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSwellsL2"),
			DSMRVersion.V4_VERSIONS, 
			"Number of voltage swells L2"), 
	EMETER_VOLTAGE_SWELLS_L3("1-0:72.36.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSwellsL3"),
			DSMRVersion.V4_VERSIONS,
			"Number of voltage swells L3"), 
	EMETER_TEXT_CODE("0-0:96.13.1",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eTextCode"),
			DSMRVersion.ALL_VERSIONS, 
			"Text message code (8 digits)"), 
	EMETER_TEXT_STRING("0-0:96.13.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eTextMessage"),
			DSMRVersion.ALL_VERSIONS, 
			"Text message"), 
	EMETER_INSTANT_CURRENT_L1("1-0:31.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "A", "eInstantCurrentL1"),
			DSMRVersion.V404, 
			"Instantenous current L1"), 
	EMETER_INSTANT_CURRENT_L2("1-0:51.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "A", "eInstantCurrentL2"),
			DSMRVersion.V404, 
			"Instantenous current L2"), 
	EMETER_INSTANT_CURRENT_L3("1-0:71.7.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "A", "eInstantCurrentL3"),
			DSMRVersion.V404, 
			"Instantenous current L3"), 
	EMETER_INSTANT_POWER_DELIVERY_L1("1-0:21.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerDeliveryL1"),
			DSMRVersion.V404,
			"Instantenous active power delivery L1"), 
	EMETER_INSTANT_POWER_DELIVERY_L2("1-0:41.7.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerDeliveryL2"),
			DSMRVersion.V404, 
			"Instantenous active power delivery L2"), 
	EMETER_INSTANT_POWER_DELIVERY_L3("1-0:61.7.0",
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerDeliveryL3"),
			DSMRVersion.V404,
			"Instantenous active power delivery L3"), 
	EMETER_INSTANT_POWER_PRODUCTION_L1("1-0:22.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerProductionL1"),
			DSMRVersion.V404,
			"Instantenous active power production L1"), 
	EMETER_INSTANT_POWER_PRODUCTION_L2("1-0:42.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerProductionL2"),
			DSMRVersion.V404,
			"Instantenous active power production L2"), 
	EMETER_INSTANT_POWER_PRODUCTION_L3("1-0:62.7.0", 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerProductionL3"),
			DSMRVersion.V404,
			"Instantenous active power production L3"),

	/* Gas Meter */
	GMETER_DEVICE_TYPE("0--1:24.1.0", 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "", "gDeviceType"),
			DSMRVersion.V30_UP, 
			"Device Type"), 
	GMETER_EQUIPMENT_IDENTIFIER_V2_X("7-0:0.0.0", 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "", "gEquipmentId"),
			DSMRVersion.V2_VERSIONS,
			"Equipment identifier"), 
	GMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0",  
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "", "gEquipmentId"),
			DSMRVersion.V30_UP, 
			"Equipment identifier"),
	GMETER_24H_DELIVERY_V2_X("7-0:23.1.0", 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "m3", "gValue"),
			DSMRVersion.V2_VERSIONS,
			"Delivery of the past hour(v3.0 and up) or 24 hours (v2.1 / v2.2)"), 
	GMETER_24H_DELIVERY_COMPENSATED_V2_X("7-0:23.2.0",
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "m3", "gValueCompensated"),
			DSMRVersion.V2_VERSIONS,
			"Temperature compensated delivery of the past 24 hours"),
	GMETER_VALUE_V3("0--1:24.3.0", 
			DSMRMeterType.GAS,
			new CosemValueDescriptor[]{ 
				new CosemValueDescriptor(CosemDate.class, "", "gValueTS"),
				new CosemValueDescriptor(CosemString.class, "", "gProfileStatus"),
				new CosemValueDescriptor(CosemInteger.class, "", "gRecordingPeriod"),
				new CosemValueDescriptor(CosemInteger.class, "", "gNumberOfValues"),
				new CosemValueDescriptor(CosemString.class, "", ""),
				new CosemValueDescriptor(CosemString.class, "", "gUnit"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue2"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue3"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue4"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue5"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue6"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue7"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue8"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue9"),
				new CosemValueDescriptor(CosemInteger.class, "", "gValue10")},
			DSMRVersion.V3_VERSIONS,
			"Delivery of the past 24 hours"),
	GMETER_VALUE_V4("0--1:24.2.1", 
			DSMRMeterType.GAS,
			new CosemValueDescriptor[]{ 
				new CosemValueDescriptor(CosemDate.class, "", "gValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "m3", "gValue")},
			DSMRVersion.V4_VERSIONS,
			"Delivery of the past 24 hours"),
	GMETER_VALVE_POSITION_V2_1("7-0:96.3.10",
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemInteger.class, "", "gValvePosition"),
			DSMRVersion.V2_VERSIONS,
			"Valve position"), 
	GMETER_VALVE_POSITION_V2_2("7-0:24.4.0", 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemInteger.class, "", "gValvePosition"),
			DSMRVersion.V2_VERSIONS, 
			"Valve position"), 
	GMETER_VALVE_POSITION("0--1:24.4.0", 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemInteger.class, "", "gValvePosition"),
			DSMRVersion.V30_UP, 
			"Valve position"),

	/* Heating Meter */
	HMETER_DEVICE_TYPE("0--1:24.1.0",
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemString.class, "", "hDeviceType"),
			DSMRVersion.V30_UP, 
			"Device Type"), 
	HMETER_EQUIPMENT_IDENTIFIER_V2_X("5-0:0.0.0",  
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemString.class, "", "hEquipmentId"),
			DSMRVersion.V2_VERSIONS, 
			"Equipment identifier"), 
	HMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0", 
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemString.class, "", "hEquipmentId"),
			DSMRVersion.V30_UP, 
			"Equipment identifier"),
	HMETER_VALUE_V2_X("5-0:1.0.0", 
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemFloat.class, "GJ", "hValue"),
			DSMRVersion.V2_VERSIONS, 
			"Last hour delivery"), 
	HMETER_VALUE_HEAT_V3("0--1:24.3.0", 
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemFloat.class, "GJ", "hValue"),
			DSMRVersion.V3_VERSIONS, 
			"Last hour delivery"), 
	HMETER_VALUE_HEAT_V4("0--1:24.2.1",
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemDate.class, "", "hValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "GJ", "hValue")},
			DSMRVersion.V4_VERSIONS, 
			"Last hour delivery"), 
	HMETER_VALVE_POSITION("0--1:24.4.0",
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemInteger.class, "", "hValvePosition"),
			DSMRVersion.V30_UP, 
			"Valve position"),

	/* Cooling Meter */
	CMETER_DEVICE_TYPE("0--1:24.1.0", 
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemString.class, "", "cDeviceType"),
			DSMRVersion.V30_UP, 
			"Device Type"), 
	CMETER_EQUIPMENT_IDENTIFIER_V2_X("6-0:0.0.0",
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemString.class, "", "cEquipmentId"),
			DSMRVersion.V2_VERSIONS, 
			"Equipment identifier"), 
	CMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0", 
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemString.class, "", "cEquipmentId"),
			DSMRVersion.V30_UP, 
			"Equipment identifier"), 
	CMETER_VALUE_V2_X("6-0:1.0.0", 
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemFloat.class, "GJ", "cValue"),
			DSMRVersion.V2_VERSIONS, 
			"Value"), 
	CMETER_VALUE_COLD_V3("0--1:24.3.1",
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemFloat.class, "GJ", "cValue"),
			DSMRVersion.V3_VERSIONS, 
			"Last hour delivery"), 
	CMETER_VALUE_COLD_V4("0--1:24.2.1",
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor[]{
				new CosemValueDescriptor(CosemDate.class, "", "cValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "GJ", "cValue")},
			DSMRVersion.V4_VERSIONS, 
			"Last hour delivery"), 
	CMETER_VALVE_POSITION("0--1:24.4.0",
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemInteger.class, "", "cValvePosition"),
			DSMRVersion.V30_UP, 
			"Valve position"),

	/* Water Meter */
	WMETER_DEVICE_TYPE("0--1:24.1.0", 
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemString.class, "", "wDeviceType"),
			DSMRVersion.V30_UP, 
			"Device Type"), 
	WMETER_EQUIPMENT_IDENTIFIER_V2_X("8-0:0.0.0",
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemString.class, "", "wEquipmentId"),
			DSMRVersion.V2_VERSIONS, 
			"Equipment identifier"), 
	WMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0",
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemString.class, "", "wEquipmentId"),
			DSMRVersion.V30_UP, 
			"Equipment identifier"), 
	WMETER_VALUE_V2_X("8-0:1.0.0",
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemFloat.class, "m3", "wValue"),
			DSMRVersion.V2_VERSIONS, 
			"Last hour delivery"), 
	WMETER_VALUE_V3("0--1:24.3.0",
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemFloat.class, "m3", "wValue"),
			DSMRVersion.V3_VERSIONS, "Last hourly meter reading"), 
	WMETER_VALUE_V4("0--1:24.2.1",
			DSMRMeterType.WATER, 
			new CosemValueDescriptor[]{
				new CosemValueDescriptor(CosemDate.class, "", "wValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "m3", "wValue")},
			DSMRVersion.V4_VERSIONS, 
			"Last hourly meter reading"), 
	WMETER_VALVE_POSITION("0--1:24.4.0",
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemInteger.class, "", "wValvePosition"),
			DSMRVersion.V30_UP, 
			"Water valve position"),

	/* Generic Meter (DSMR v3 only) */
	GENMETER_DEVICE_TYPE("0--1:24.1.0",
			DSMRMeterType.GENERIC, 
			new CosemValueDescriptor(CosemString.class, "", "genericDeviceType"),
			DSMRVersion.V3_VERSIONS, 
			"Device Type"), 
	GENMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0",
			DSMRMeterType.GENERIC, 
			new CosemValueDescriptor(CosemString.class, "", "genericEquipmentId"),
			DSMRVersion.V3_VERSIONS,
			"Equipment identifier"), 
	GENMETER_VALUE_V3("0--1:24.3.0", 
			DSMRMeterType.GENERIC, 
			new CosemValueDescriptor(CosemFloat.class, "", "genericValue"),
			DSMRVersion.V3_VERSIONS,
			"Last hourly meter reading"), 

	/* Slave E Meter (DSMR v4) */
	SEMETER_DEVICE_TYPE("0--1:24.1.0", 
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "seDeviceType"),
			DSMRVersion.V4_VERSIONS,
			"Slave Electricity Device Type"), 
	SEMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0", 
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "seEquipmentId"),
			DSMRVersion.V4_VERSIONS,
			"Slave Electricity Equipment identifier"), 
	SEMETER_VALUE_V4("0--1:24.2.1",
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor[]{
				new CosemValueDescriptor(CosemDate.class, "", "seValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "kWh", "seValue")},
			DSMRVersion.V4_VERSIONS,
			"Slave Electricity last hourly meter reading"), 
	SEMETER_SWITCH_POSITION("0--1:24.4.0",
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "seSwitchPosition"),
			DSMRVersion.V4_VERSIONS, 
			"Slave Electricity switch position");

	/** OBIS reduced identifier */
	public String obisId;
	
	/** COSEM value descriptors */
	public final List<CosemValueDescriptor> cosemValueDescriptors;
	
	/** Description of this message type */
	public final String description;
	
	/** Applicable DSMR versions for this message type */
	public final List<DSMRVersion> applicableVersions;
	
	/** Applicable meter type for this message type*/
	public final DSMRMeterType meterType;

	/**
	 * Constructor
	 * 
	 * @param obisId
	 *            OBIS Identifier for the OBIS message
	 * @param meterType
	 *            the {@link DSMRMeterType} that this message is applicable for
	 * @param cosemValueDescriptors
	 *            array of {@link CosemValueDescriptor} that describe the values
	 *            of the message
	 * @param applicableVersions
	 *            array of {@link DSMRVersion} that this message is applicable
	 *            for
	 * @param description
	 *            human readable description of the OBIS message
	 */
	private OBISMsgType(
			String obisId,
			DSMRMeterType meterType,
			CosemValueDescriptor[] cosemValueDescriptors,
			DSMRVersion[] applicableVersions,
			String description) {
		this.obisId = obisId;
		this.meterType = meterType;
		this.cosemValueDescriptors = Arrays.asList(cosemValueDescriptors);
		this.applicableVersions = Arrays.asList(applicableVersions);
		this.description = description;
	}

	/**
	 * Constructor
	 * 
	 * @param obisId
	 *            OBIS Identifier for the OBIS message
	 * @param meterType
	 *            the {@link DSMRMeterType} that this message is applicable for
	 * @param cosemValueDescriptor
	 *            {@link CosemValueDescriptor} that describes the value of the
	 *            message
	 * @param applicableVersion
	 *            {@link DSMRVersion} that this message is applicable for
	 * @param description
	 *            human readable description of the OBIS message
	 */
	private OBISMsgType(
			String obisId,
			DSMRMeterType meterType,
			CosemValueDescriptor cosemValueDescriptor,
			DSMRVersion applicableVersion,
			String description) {
		this(obisId, meterType, new CosemValueDescriptor[] { cosemValueDescriptor }, 
				new DSMRVersion[] { applicableVersion },
				description);
	}

	/**
	 * Constructor
	 * 
	 * @param obisId
	 *            OBIS Identifier for the OBIS message
	 * @param meterType
	 *            the {@link DSMRMeterType} that this message is applicable for
	 * @param cosemValueDescriptor
	 *            {@link CosemValueDescriptor} that describes the value
	 *            of the message
	 * @param applicableVersions
	 *            array of {@link DSMRVersion} that this message is applicable
	 *            for
	 * @param description
	 *            human readable description of the OBIS message
	 */
	private OBISMsgType(
			String obisId,
			DSMRMeterType meterType,
			CosemValueDescriptor cosemValueDescriptor,
			DSMRVersion[] applicableVersions,
			String description) {
		this(obisId, meterType, new CosemValueDescriptor[] { cosemValueDescriptor }, 
				applicableVersions,
				description);
	}
	
	/**
	 * Constructor
	 * 
	 * @param obisId
	 *            OBIS Identifier for the OBIS message
	 * @param meterType
	 *            the {@link DSMRMeterType} that this message is applicable for
	 * @param cosemValueDescriptors
	 *            array of {@link CosemValueDescriptor} that describe the values
	 *            of the message
	 * @param applicableVersions
	 *            {@link DSMRVersion} that this message is applicable
	 *            for
	 * @param description
	 *            human readable description of the OBIS message
	 */
	private OBISMsgType(
			String obisId,
			DSMRMeterType meterType,
			CosemValueDescriptor[] cosemValueDescriptors,
			DSMRVersion applicableVersion,
			String description) {
		this(obisId, meterType, cosemValueDescriptors, 
				new DSMRVersion[] { applicableVersion },
				description);
	}
}
