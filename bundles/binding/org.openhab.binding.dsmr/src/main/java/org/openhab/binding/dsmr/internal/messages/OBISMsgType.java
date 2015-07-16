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
 * <li>Description of the values (See {@link CosemValueDescriptor})
 * <li>Human readable description
 * </ul>
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public enum OBISMsgType {
	UNKNOWN(new OBISIdentifier(null, null, -1, -1, null, null),  
			DSMRMeterType.NA, 
			new CosemValueDescriptor(CosemString.class, "", ""), 
			"Unknown OBIS message type"),
			
	/* General messages (DSMR V4) */
	P1_VERSION_OUTPUT_V4(new OBISIdentifier(1, 3, 0, 2, 8, null), 
			DSMRMeterType.NA, 
			new CosemValueDescriptor(CosemString.class, "", "P1VersionOutput"),
			"Version information for P1 output"), 
	P1_TIMESTAMP(new OBISIdentifier(0, 0, 1, 0, 0, null), 
			DSMRMeterType.NA, 
			new CosemValueDescriptor(CosemDate.class, "", "P1Timestamp"), 
			"Timestamp of the P1 output"),

	/* Electricity Meter */
	EMETER_EQUIPMENT_IDENTIFIER_V2_X(new OBISIdentifier(0, 0, 42, 0, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eEquipmentId"),
			"Equipment identifier DSMR v2.x"), 
	EMETER_EQUIPMENT_IDENTIFIER_NTA8130(new OBISIdentifier(0, 0, 96, 1, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eEquipmentId"), 
			"Equipment identifier (NTA8130)"), 
	EMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, 0, 96, 1, 1, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eEquipmentId"), 
			"Equipment identifier"), 
	EMETER_DELIVERY_TARIFF0(new OBISIdentifier(1, 0, 1, 8, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eDeliveryTariff0"), 
			"Total meter delivery tariff 0"), 
	EMETER_DELIVERY_TARIFF1(new OBISIdentifier(1, 0, 1, 8, 1, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eDeliveryTariff1"), 
			"Total meter delivery tariff 1"), 
	EMETER_DELIVERY_TARIFF2(new OBISIdentifier(1, 0, 1, 8, 2, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eDeliveryTariff2"),
			"Total meter delivery tariff 2"), 
	EMETER_PRODUCTION_TARIFF0(new OBISIdentifier(1, 0, 2, 8, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eProductionTariff0"), 
			"Total meter production tariff 0"),
	EMETER_PRODUCTION_TARIFF1(new OBISIdentifier(1, 0, 2, 8, 1, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eProductionTariff1"), 
			"Total meter production tariff 1"), 
	EMETER_PRODUCTION_TARIFF2(new OBISIdentifier(1, 0, 2, 8, 2, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kWh", "eProductionTariff2"),
			"Total meter production tariff 2"), 
	EMETER_TARIFF_INDICATOR(new OBISIdentifier(0, 0, 96, 14, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eTariffIndicator"), 
			"Tariff indicator"), 
	EMETER_ACTIVE_IMPORT_POWER(new OBISIdentifier(1, 0, 15, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "W", "eActualDelivery"),
			"Aggregrate active import power"),
	EMETER_ACTUAL_DELIVERY(new OBISIdentifier(1, 0, 1, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eActualDelivery"),
			"Actual power delivery"), 
	EMETER_ACTUAL_PRODUCTION(new OBISIdentifier(1, 0, 2, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eActualProduction"),
			"Actual power production"), 
	EMETER_TRESHOLD_V2_1(new OBISIdentifier(1, 0, 17, 0, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "A", "eTreshold"),
			"The actual threshold Electricity in A (DSMR v2.1)"), 
	EMETER_TRESHOLD(new OBISIdentifier(0, 0, 17, 0, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "A", "eTreshold"),
			"The actual threshold Electricity in A (DSMR v2.2 / v3)"), 
	EMETER_TRESHOLD_V4(new OBISIdentifier(0, 0, 17, 0, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eTreshold"),
			"The actual threshold Electricity in kW (DSMR v4)"), 
	EMETER_SWITCH_POSITION_V2_1(new OBISIdentifier(1, 0, 96, 3, 10, null),  
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "eSwitchPosition"),
			"Actual switch position Electricity DSMR v2.1"), 
	EMETER_SWITCH_POSITION_V2_2(new OBISIdentifier(0, 0, 24, 4, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "eSwitchPosition"),
			"Actual switch position Electricity DSMR v2.2"), 
	EMETER_SWITCH_POSITION(new OBISIdentifier(0, 0, 96, 3, 10, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "eSwitchPosition"), 
			"Actual switch position Electricity"), 
	EMETER_POWER_FAILURES(new OBISIdentifier(0, 0, 96, 7, 21, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "ePowerFailures"),
			"Number of Power failures"), 
	EMETER_LONG_POWER_FAILURES(new OBISIdentifier(0, 0, 96, 7, 9, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eLongPowerFailures"),
			"Number of Long Power failures"),
	EMETER_POWER_FAILURE_LOG(new OBISIdentifier(1, 0, 99, 97, 0, null), 
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
			"Power Failure event log"),
	EMETER_VOLTAGE_SAGS_L1(new OBISIdentifier(1, 0, 32, 32, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSagsL1"),
			"Number of voltage sags L1"), 
	EMETER_VOLTAGE_SAGS_L2(new OBISIdentifier(1, 0, 52, 32, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSagsL2"),
			"Number of voltage sags L2"), 
	EMETER_VOLTAGE_SAGS_L3(new OBISIdentifier(1, 0, 72, 32, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSagsL3"),
			"Number of voltage sags L3"), 
	EMETER_VOLTAGE_SWELLS_L1(new OBISIdentifier(1, 0, 32, 36, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSwellsL1"),
			"Number of voltage swells L1"), 
	EMETER_VOLTAGE_SWELLS_L2(new OBISIdentifier(1, 0, 52, 36, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSwellsL2"), 
			"Number of voltage swells L2"), 
	EMETER_VOLTAGE_SWELLS_L3(new OBISIdentifier(1, 0, 72, 36, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "", "eVoltageSwellsL3"),
			"Number of voltage swells L3"), 
	EMETER_TEXT_CODE(new OBISIdentifier(0, 0, 96, 13, 1, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eTextCode"), 
			"Text message code (8 digits)"), 
	EMETER_TEXT_STRING(new OBISIdentifier(0, 0, 96, 13, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "eTextMessage"), 
			"Text message"), 
	EMETER_INSTANT_CURRENT_L1(new OBISIdentifier(1, 0, 31, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "A", "eInstantCurrentL1"),
			"Instantenous current L1"), 
	EMETER_INSTANT_CURRENT_L2(new OBISIdentifier(1, 0, 51, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "A", "eInstantCurrentL2"), 
			"Instantenous current L2"), 
	EMETER_INSTANT_CURRENT_L3(new OBISIdentifier(1, 0, 71, 7, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "A", "eInstantCurrentL3"), 
			"Instantenous current L3"), 
	EMETER_INSTANT_POWER_DELIVERY_L1(new OBISIdentifier(1, 0, 21, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerDeliveryL1"),
			"Instantenous active power delivery L1"), 
	EMETER_INSTANT_POWER_DELIVERY_L2(new OBISIdentifier(1, 0, 41, 7, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerDeliveryL2"), 
			"Instantenous active power delivery L2"), 
	EMETER_INSTANT_POWER_DELIVERY_L3(new OBISIdentifier(1, 0, 61, 7, 0, null),
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerDeliveryL3"),
			"Instantenous active power delivery L3"), 
	EMETER_INSTANT_POWER_PRODUCTION_L1(new OBISIdentifier(1, 0, 22, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerProductionL1"),
			"Instantenous active power production L1"), 
	EMETER_INSTANT_POWER_PRODUCTION_L2(new OBISIdentifier(1, 0, 42, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerProductionL2"),
			"Instantenous active power production L2"), 
	EMETER_INSTANT_POWER_PRODUCTION_L3(new OBISIdentifier(1, 0, 62, 7, 0, null), 
			DSMRMeterType.ELECTRICITY, 
			new CosemValueDescriptor(CosemFloat.class, "kW", "eInstantPowerProductionL3"),
			"Instantenous active power production L3"),

	/* Gas Meter */
	GMETER_DEVICE_TYPE(new OBISIdentifier(0, null, 24, 1, 0, null), 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "", "gDeviceType"), 
			"Device Type"), 
	GMETER_EQUIPMENT_IDENTIFIER_V2_X(new OBISIdentifier(7, 0, 0, 0, 0, null), 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "", "gEquipmentId"),
			"Equipment identifier"), 
	GMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, null, 96, 1, 0, null),  
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemString.class, "", "gEquipmentId"), 
			"Equipment identifier"),
	GMETER_24H_DELIVERY_V2_X(new OBISIdentifier(7, 0, 23, 1, 0, null), 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemString.class, "m3", "gValue"),
				new CosemValueDescriptor(CosemDate.class, "", "gValueTS")},
			"Delivery of the past hour(v3.0 and up) or 24 hours (v2.1 / v2.2)"), 
	GMETER_24H_DELIVERY_COMPENSATED_V2_X(new OBISIdentifier(7, 0, 23, 2, 0, null),
			DSMRMeterType.GAS,
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemString.class, "m3", "gValueCompensated"),
				new CosemValueDescriptor(CosemDate.class, "", "gValueCompensatedTS")},
			"Temperature compensated delivery of the past 24 hours"),
	GMETER_VALUE_V3(new OBISIdentifier(0, null, 24, 3, 0, null), 
			DSMRMeterType.GAS,
			new CosemValueDescriptor[] { 
				new CosemValueDescriptor(CosemDate.class, "", "gValueTS"),
				new CosemValueDescriptor(CosemString.class, "", "gProfileStatus"),
				new CosemValueDescriptor(CosemInteger.class, "", "gRecordingPeriod"),
				new CosemValueDescriptor(CosemInteger.class, "", "gNumberOfValues"),
				new CosemValueDescriptor(CosemString.class, "", ""),
				new CosemValueDescriptor(CosemString.class, "", "gUnit"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue2"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue3"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue4"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue5"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue6"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue7"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue8"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue9"),
				new CosemValueDescriptor(CosemFloat.class, "", "gValue10")},
			"Delivery of the past 24 hours"),
	GMETER_VALUE_V4(new OBISIdentifier(0, null, 24, 2, 1, null), 
			DSMRMeterType.GAS,
			new CosemValueDescriptor[] { 
				new CosemValueDescriptor(CosemDate.class, "", "gValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "m3", "gValue")},
			"Delivery of the past 24 hours"),
	GMETER_VALVE_POSITION_V2_1(new OBISIdentifier(7, 0, 96, 3, 10, null),
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemInteger.class, "", "gValvePosition"),
			"Valve position"), 
	GMETER_VALVE_POSITION_V2_2(new OBISIdentifier(7, 0, 24, 4, 0, null), 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemInteger.class, "", "gValvePosition"), 
			"Valve position"), 
	GMETER_VALVE_POSITION(new OBISIdentifier(0, null, 24, 4, 0, null), 
			DSMRMeterType.GAS, 
			new CosemValueDescriptor(CosemInteger.class, "", "gValvePosition"), 
			"Valve position"),

	/* Heating Meter */
	HMETER_DEVICE_TYPE(new OBISIdentifier(0, null, 24, 1, 0, null),
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemString.class, "", "hDeviceType"), 
			"Device Type"), 
	HMETER_EQUIPMENT_IDENTIFIER_V2_X(new OBISIdentifier(5, 0, 0, 0, 0, null),  
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemString.class, "", "hEquipmentId"), 
			"Equipment identifier"), 
	HMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, null, 96, 1, 0, null), 
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemString.class, "", "hEquipmentId"), 
			"Equipment identifier"),
	HMETER_VALUE_V2_X(new OBISIdentifier(5, 0, 1, 0, 0, null), 
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemFloat.class, "GJ", "hValue"), 
				new CosemValueDescriptor(CosemDate.class, "", "hValueTS")},
			"Last hour delivery"), 
	HMETER_VALUE_HEAT_V3(new OBISIdentifier(0, null, 24, 3, 0, null), 
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemFloat.class, "GJ", "hValue"), 
			"Last hour delivery"), 
	HMETER_VALUE_HEAT_V4(new OBISIdentifier(0, null, 24, 2, 1, null),
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemDate.class, "", "hValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "GJ", "hValue")}, 
			"Last hour delivery"), 
	HMETER_VALVE_POSITION(new OBISIdentifier(0, null, 24, 4, 0, null),
			DSMRMeterType.HEATING, 
			new CosemValueDescriptor(CosemInteger.class, "", "hValvePosition"), 
			"Valve position"),

	/* Cooling Meter */
	CMETER_DEVICE_TYPE(new OBISIdentifier(0, null, 24, 1, 0, null), 
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemString.class, "", "cDeviceType"), 
			"Device Type"), 
	CMETER_EQUIPMENT_IDENTIFIER_V2_X(new OBISIdentifier(6, 0, 0, 0, 0, null),
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemString.class, "", "cEquipmentId"), 
			"Equipment identifier"), 
	CMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, null, 96, 1, 0, null), 
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemString.class, "", "cEquipmentId"), 
			"Equipment identifier"), 
	CMETER_VALUE_V2_X(new OBISIdentifier(6, 0, 1, 0, 0, null), 
			DSMRMeterType.COOLING,
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemFloat.class, "GJ", "cValue"),
				new CosemValueDescriptor(CosemDate.class, "", "cValueTS")},
			"Value"), 
	CMETER_VALUE_COLD_V3(new OBISIdentifier(0, null, 24, 3, 1, null),
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemFloat.class, "GJ", "cValue"), 
			"Last hour delivery"), 
	CMETER_VALUE_COLD_V4(new OBISIdentifier(0, null, 24, 2, 1, null),
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemDate.class, "", "cValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "GJ", "cValue")}, 
			"Last hour delivery"), 
	CMETER_VALVE_POSITION(new OBISIdentifier(0, null, 24, 4, 0, null),
			DSMRMeterType.COOLING, 
			new CosemValueDescriptor(CosemInteger.class, "", "cValvePosition"), 
			"Valve position"),

	/* Water Meter */
	WMETER_DEVICE_TYPE(new OBISIdentifier(0, null, 24, 1, 0, null), 
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemString.class, "", "wDeviceType"), 
			"Device Type"), 
	WMETER_EQUIPMENT_IDENTIFIER_V2_X(new OBISIdentifier(8, 0, 0, 0, 0, null),
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemString.class, "", "wEquipmentId"), 
			"Equipment identifier"), 
	WMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, null, 96, 1, 0, null),
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemString.class, "", "wEquipmentId"), 
			"Equipment identifier"), 
	WMETER_VALUE_V2_X(new OBISIdentifier(8, 0, 1, 0, 0, null),
			DSMRMeterType.WATER,
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemFloat.class, "m3", "wValue"),
				new CosemValueDescriptor(CosemDate.class, "", "cValueTS")},
			"Last hour delivery"), 
	WMETER_VALUE_V3(new OBISIdentifier(0, null, 24, 3, 0, null),
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemFloat.class, "m3", "wValue"),
			"Last hourly meter reading"), 
	WMETER_VALUE_V4(new OBISIdentifier(0, null, 24, 2, 1, null),
			DSMRMeterType.WATER, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemDate.class, "", "wValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "m3", "wValue")}, 
			"Last hourly meter reading"), 
	WMETER_VALVE_POSITION(new OBISIdentifier(0, null, 24, 4, 0, null),
			DSMRMeterType.WATER, 
			new CosemValueDescriptor(CosemInteger.class, "", "wValvePosition"), 
			"Water valve position"),

	/* Generic Meter (DSMR v3 only) */
	GENMETER_DEVICE_TYPE(new OBISIdentifier(0, null, 24, 1, 0, null),
			DSMRMeterType.GENERIC, 
			new CosemValueDescriptor(CosemString.class, "", "genericDeviceType"), 
			"Device Type"), 
	GENMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, null, 96, 1, 0, null),
			DSMRMeterType.GENERIC, 
			new CosemValueDescriptor(CosemString.class, "", "genericEquipmentId"),
			"Equipment identifier"), 
	GENMETER_VALUE_V3(new OBISIdentifier(0, null, 24, 3, 0, null), 
			DSMRMeterType.GENERIC, 
			new CosemValueDescriptor(CosemFloat.class, "", "genericValue"),
			"Last hourly meter reading"), 

	/* Slave E Meter (DSMR v4) */
	SEMETER_DEVICE_TYPE(new OBISIdentifier(0, null, 24, 1, 0, null), 
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "seDeviceType"),
			"Slave Electricity Device Type"), 
	SEMETER_EQUIPMENT_IDENTIFIER(new OBISIdentifier(0, null, 96, 1, 0, null), 
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor(CosemString.class, "", "seEquipmentId"),
			"Slave Electricity Equipment identifier"), 
	SEMETER_VALUE_V4(new OBISIdentifier(0, null, 24, 2, 1, null),
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor[] {
				new CosemValueDescriptor(CosemDate.class, "", "seValueTS"),
				new CosemValueDescriptor(CosemFloat.class, "kWh", "seValue")},
			"Slave Electricity last hourly meter reading"), 
	SEMETER_SWITCH_POSITION(new OBISIdentifier(0, null, 24, 4, 0, null),
			DSMRMeterType.SLAVE_ELECTRICITY, 
			new CosemValueDescriptor(CosemInteger.class, "", "seSwitchPosition"), 
			"Slave Electricity switch position");

	/** OBIS reduced identifier */
	public OBISIdentifier obisId;
	
	/** COSEM value descriptors */
	public final List<CosemValueDescriptor> cosemValueDescriptors;
	
	/** Description of this message type */
	public final String description;
		
	/** Applicable meter type for this message type*/
	public final DSMRMeterType meterType;

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
	 * @param description
	 *            human readable description of the OBIS message
	 */
	private OBISMsgType(
			OBISIdentifier obisId,
			DSMRMeterType meterType,
			CosemValueDescriptor cosemValueDescriptor,
			String description) {
		this(obisId, meterType, new CosemValueDescriptor[] { cosemValueDescriptor }, 
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
	 * @param description
	 *            human readable description of the OBIS message
	 */
	private OBISMsgType(
			OBISIdentifier obisId,
			DSMRMeterType meterType,
			CosemValueDescriptor[] cosemValueDescriptors,
			String description) {
		this.obisId = obisId;
		this.meterType = meterType;
		this.cosemValueDescriptors = Arrays.asList(cosemValueDescriptors);
		this.description = description;
	}
}
