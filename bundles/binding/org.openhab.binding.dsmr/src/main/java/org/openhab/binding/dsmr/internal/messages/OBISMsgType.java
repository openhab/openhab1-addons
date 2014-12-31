package org.openhab.binding.dsmr.internal.messages;

import java.util.Arrays;
import java.util.List;

import org.openhab.binding.dsmr.internal.DSMRVersion;
import org.openhab.core.types.State;

/**
 * List of OBISMsgType
 * <p>
 * Each OBISMsgType consists of the following attributes:
 * <p>
 * <ul>
 * <li>OBIS Identifier (reduced form)
 * <li>Unit
 * <li>Applicable DMSRVersions
 * <li>Readable Description
 * <li>OpenHab Binding Identifier
 * <li>The native 
 * </ul>
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public enum OBISMsgType {
	UNKNOWN("", "", DSMRVersion.NONE, "Unknown OBIS message type", "Unknown",
			OBISStringMsg.class),

	/* General messages (DSMR V4) */
	P1_VERSION_OUTPUT_V4("1-3:0.2.8", "", DSMRVersion.V4_VERSIONS,
			"Version information for P1 output", "P1VersionOutput",
			OBISStringMsg.class), P1_TIMESTAMP("0-0:1.0.0", "",
			DSMRVersion.V4_VERSIONS, "Timestamp of the P1 output",
			"P1Timestamp", OBISDateMsg.class),

	/* Electricity Meter */
	EMETER_EQUIPMENT_IDENTIFIER_V2_X("0-0:42.0.0", "", DSMRVersion.V2_VERSIONS,
			"Equipment identifier DSMR v2.x", "eEquipmentId",
			OBISStringMsg.class), 
	EMETER_EQUIPMENT_IDENTIFIER("0-0:96.1.1", "",
			DSMRVersion.V30_UP, "Equipment identifier", "eEquipmentId",
			OBISStringMsg.class), 
	EMETER_DELIVERY_TARIFF1("1-0:1.8.1", "kWh",
			DSMRVersion.ALL_VERSIONS, "Total meter delivery tariff 1",
			"eDeliveryTariff1", OBISFloatMsg.class), 
	EMETER_DELIVERY_TARIFF2(
			"1-0:1.8.2", "kWh", DSMRVersion.ALL_VERSIONS,
			"Total meter delivery tariff 2", "eDeliveryTariff2",
			OBISFloatMsg.class), 
	EMETER_PRODUCTION_TARIFF1("1-0:2.8.1", "kWh",
			DSMRVersion.ALL_VERSIONS, "Total meter production tariff 1",
			"eProductionTariff1", OBISFloatMsg.class), 
	EMETER_PRODUCTION_TARIFF2(
			"1-0:2.8.2", "kWh", DSMRVersion.ALL_VERSIONS,
			"Total meter production tariff 2", "eProductionTariff2",
			OBISFloatMsg.class), 
	EMETER_TARIFF_INDICATOR("0-0:96.14.0", "",
			DSMRVersion.ALL_VERSIONS, "Tariff indicator", "eTariffIndicator",
			OBISStringMsg.class), 
	EMETER_ACTUAL_DELIVERY("1-0:1.7.0", "kW",
			DSMRVersion.ALL_VERSIONS, "Actual power delivery",
			"eActualDelivery", OBISFloatMsg.class), 
	EMETER_ACTUAL_PRODUCTION(
			"1-0:2.7.0", "kW", DSMRVersion.ALL_VERSIONS,
			"Actual power production", "eActualProduction", OBISFloatMsg.class), 
	EMETER_TRESHOLD_V2_1(
			"1-0:17.0.0", "A", new DSMRVersion[] { DSMRVersion.V21 },
			"The actual threshold Electricity in A (DSMR v2.1)", "eTreshold",
			OBISIntegerMsg.class), 
	EMETER_TRESHOLD("0-0:17.0.0", "A",
			new DSMRVersion[] { DSMRVersion.V22, DSMRVersion.V30 },
			"The actual threshold Electricity in A (DSMR v2.2 / v3)",
			"eTreshold", OBISIntegerMsg.class), 
	EMETER_TRESHOLD_V4(
			"0-0:17.0.0", "kW", DSMRVersion.V4_VERSIONS,
			"The actual threshold Electricity in kW (DSMR v4)", "eTreshold",
			OBISFloatMsg.class), 
	EMETER_SWITCH_POSITION_V2_1("1-0:96.3.10",
			"", new DSMRVersion[] { DSMRVersion.V21 },
			"Actual switch position Electricity DSMR v2.1", "eSwitchPosition",
			OBISIntegerMsg.class), 
	EMETER_SWITCH_POSITION_V2_2("0-0:24.4.0",
			"", new DSMRVersion[] { DSMRVersion.V22 },
			"Actual switch position Electricity DSMR v2.2", "eSwitchPosition",
			OBISIntegerMsg.class), 
	EMETER_SWITCH_POSITION("0-0:96.3.10", "",
			DSMRVersion.V30_UP, "Actual switch position Electricity",
			"eSwitchPosition", OBISIntegerMsg.class), 
	EMETER_POWER_FAILURES(
			"0-0:96.7.21", "", DSMRVersion.V4_VERSIONS,
			"Number of Power failures", "ePowerFailures", OBISFloatMsg.class), 
	EMETER_LONG_POWER_FAILURES(
			"0-0:96.7.9", "", DSMRVersion.V4_VERSIONS,
			"Number of Long Power failures", "eLongPowerFailures",
			OBISFloatMsg.class),
	/* TODO: Don't support multiple values in OBISMsg yet */
	// EMETER_POWER_FAILURE_LOG("1-0:99.97.0", "", DSMRVersion.V4_VERSIONS,
	// "Power Failure event log", "ePowerFailureEventLog",
	// OBISPowerFailureEventLog.class),
	EMETER_VOLTAGE_SAGS_L1("1-0:32.32.0", "", DSMRVersion.V4_VERSIONS,
			"Number of voltage sags L1", "eVoltageSagsL1", OBISFloatMsg.class), 
	EMETER_VOLTAGE_SAGS_L2(
			"1-0:52.32.0", "", DSMRVersion.V4_VERSIONS,
			"Number of voltage sags L2", "eVoltageSagsL2", OBISFloatMsg.class), 
	EMETER_VOLTAGE_SAGS_L3(
			"1-0:72.32.0", "", DSMRVersion.V4_VERSIONS,
			"Number of voltage sags L3", "eVoltageSagsL3", OBISFloatMsg.class), 
	EMETER_VOLTAGE_SWELLS_L1(
			"1-0:32.36.0", "", DSMRVersion.V4_VERSIONS,
			"Number of voltage swells L1", "eVoltageSwellsL1",
			OBISFloatMsg.class), 
	EMETER_VOLTAGE_SWELLS_L2("1-0:52.36.0", "",
			DSMRVersion.V4_VERSIONS, "Number of voltage swells L2",
			"eVoltageSwellsL2", OBISFloatMsg.class), 
	EMETER_VOLTAGE_SWELLS_L3(
			"1-0:72.36.0", "", DSMRVersion.V4_VERSIONS,
			"Number of voltage swells L3", "eVoltageSwellsL3",
			OBISFloatMsg.class), 
	EMETER_TEXT_CODE("0-0:96.13.1", "",
			DSMRVersion.ALL_VERSIONS, "Text message code (8 digits)",
			"eTextCode", OBISStringMsg.class), 
	EMETER_TEXT_STRING(
			"0-0:96.13.0", "", DSMRVersion.ALL_VERSIONS, "Text message",
			"eTextMessage", OBISStringMsg.class), 
	EMETER_INSTANT_CURRENT_L1(
			"1-0:31.7.0", "A", DSMRVersion.V404, "Instantenous current L1",
			"eInstantCurrentL1", OBISFloatMsg.class), 
	EMETER_INSTANT_CURRENT_L2(
			"1-0:51.7.0", "A", DSMRVersion.V404, "Instantenous current L2",
			"eInstantCurrentL2", OBISFloatMsg.class), 
	EMETER_INSTANT_CURRENT_L3(
			"1-0:71.7.0", "A", DSMRVersion.V404, "Instantenous current L3",
			"eInstantCurrentL3", OBISFloatMsg.class), 
	EMETER_INSTANT_POWER_DELIVERY_L1(
			"1-0:21.7.0", "kW", DSMRVersion.V404,
			"Instantenous active power delivery L1", "eInstantPowerDeliveryL1",
			OBISFloatMsg.class), 
	EMETER_INSTANT_POWER_DELIVERY_L2("1-0:41.7.0",
			"kW", DSMRVersion.V404, "Instantenous active power delivery L2",
			"eInstantPowerDeliveryL2", OBISFloatMsg.class), 
	EMETER_INSTANT_POWER_DELIVERY_L3(
			"1-0:61.7.0", "kW", DSMRVersion.V404,
			"Instantenous active power delivery L3", "eInstantPowerDeliveryL3",
			OBISFloatMsg.class), 
	EMETER_INSTANT_POWER_PRODUCTION_L1(
			"1-0:22.7.0", "kW", DSMRVersion.V404,
			"Instantenous active power production L1",
			"eInstantPowerProductionL1", OBISFloatMsg.class), 
	EMETER_INSTANT_POWER_PRODUCTION_L2(
			"1-0:42.7.0", "kW", DSMRVersion.V404,
			"Instantenous active power production L2",
			"eInstantPowerProductionL2", OBISFloatMsg.class), 
	EMETER_INSTANT_POWER_PRODUCTION_L3(
			"1-0:62.7.0", "kW", DSMRVersion.V404,
			"Instantenous active power production L3",
			"eInstantPowerProductionL3", OBISFloatMsg.class),

	/* Gas Meter */
	GMETER_DEVICE_TYPE("0--1:24.1.0", "", DSMRVersion.V30_UP, "Device Type",
			"gDeviceType", OBISStringMsg.class), 
	GMETER_EQUIPMENT_IDENTIFIER_V2_X(
			"7-0:0.0.0", "", DSMRVersion.V2_VERSIONS,
			"Gas Equipment identifier", "gEquipmentId", OBISStringMsg.class), 
	GMETER_EQUIPMENT_IDENTIFIER(
			"0--1:96.1.0", "", DSMRVersion.V30_UP, "Equipment identifier",
			"gEquipmentId", OBISStringMsg.class), 
	GMETER_24H_DELIVERY_V2_X(
			"7-0:23.1.0", "", DSMRVersion.V2_VERSIONS,
			"Gas Delivery of the past 24 hours", "g24hDelivery",
			OBISStringMsg.class), 
	GMETER_24H_DELIVERY_COMPENSATED_V2_X(
			"7-0:23.2.0", "", DSMRVersion.V2_VERSIONS,
			"Temperature compensated gas Delivery of the past 24 hours",
			"g24hDeliveryCompensated", OBISStringMsg.class),
	// GMETER_24H_DELIVERY_V3("0--1:24.3.0", "", DSMRVersion.V3_VERSIONS,
	// "Delivery of the past 24 hours", "gEquipmentId",
	// OBISGasReadingV4Msg.class),
	// GMETER_24H_DELIVERY_V4("0--1:24.2.1", "", DSMRVersion.V4_VERSIONS,
	// "Delivery of the past 24 hours", "gEquipmentId",
	// OBISGasReadingV4Msg.class),
	GMETER_VALVE_POSITION_V2_1("7-0:96.3.10", "", DSMRVersion.V2_VERSIONS,
			"Valve position", "gValvePosition", OBISIntegerMsg.class), 
	GMETER_VALVE_POSITION_V2_2(
			"7-0:24.4.0", "", DSMRVersion.V2_VERSIONS, "Valve position",
			"gValvePosition", OBISIntegerMsg.class), 
	GMETER_VALVE_POSITION(
			"0--1:24.4.0", "", DSMRVersion.V30_UP, "Valve position",
			"gValvePosition", OBISIntegerMsg.class),

	/* Heating Meter */
	HMETER_DEVICE_TYPE("0--1:24.1.0", "", DSMRVersion.V30_UP, "Device Type",
			"hDeviceType", OBISStringMsg.class), 
	HMETER_EQUIPMENT_IDENTIFIER_V2_X(
			"5-0:0.0.0", "", DSMRVersion.V2_VERSIONS, "Equipment identifier",
			"hEquipmentId", OBISStringMsg.class), 
	HMETER_EQUIPMENT_IDENTIFIER(
			"0--1:96.1.0", "", DSMRVersion.V30_UP, "Equipment identifier",
			"hEquipmentId", OBISStringMsg.class),
	HMETER_VALUE_V2_X(
			"5-0:1.0.0", "GJ", DSMRVersion.V2_VERSIONS, "Last hour delivery",
			"hValue", OBISFloatMsg.class), 
	HMETER_VALUE_HEAT_V3("0--1:24.3.0",
			"GJ", DSMRVersion.V3_VERSIONS, "Last hour delivery", "hValue",
			OBISFloatMsg.class), 
	HMETER_VALUE_HEAT_V4("0--1:24.2.1", "GJ",
			DSMRVersion.V4_VERSIONS, "Last hour delivery", "hValue",
			OBISFloatMsg.class), 
	HMETER_VALVE_POSITION("0--1:24.4.0", "",
			DSMRVersion.V30_UP, "Valve position", "hValvePosition",
			OBISIntegerMsg.class),

	/* Cooling Meter */
	CMETER_DEVICE_TYPE("0--1:24.1.0", "", DSMRVersion.V30_UP, "Device Type",
			"cDeviceType", OBISStringMsg.class), 
	CMETER_EQUIPMENT_IDENTIFIER_V2_X(
			"6-0:0.0.0", "", DSMRVersion.V2_VERSIONS, "Equipment identifier",
			"cEquipmentId", OBISStringMsg.class), 
	CMETER_EQUIPMENT_IDENTIFIER(
			"0--1:96.1.0", "", DSMRVersion.V30_UP, "Equipment identifier",
			"cEquipmentId", OBISStringMsg.class), 
	CMETER_VALUE_V2_X(
			"6-0:1.0.0", "GJ", DSMRVersion.V2_VERSIONS, "Value", "cValue",
			OBISFloatMsg.class), 
	CMETER_VALUE_COLD_V3("0--1:24.3.1", "GJ",
			DSMRVersion.V3_VERSIONS, "Last hour delivery", "cValue",
			OBISFloatMsg.class), 
	CMETER_VALUE_COLD_V4("0--1:24.2.1", "GJ",
			DSMRVersion.V4_VERSIONS, "Last hour delivery", "cValue",
			OBISFloatMsg.class), 
	CMETER_VALVE_POSITION("0--1:24.4.0", "",
			DSMRVersion.V30_UP, "Valve position", "cValvePosition",
			OBISIntegerMsg.class),

	/* Water Meter */
	WMETER_DEVICE_TYPE("0--1:24.1.0", "", DSMRVersion.V30_UP, "Device Type",
			"wDeviceType", OBISStringMsg.class), 
	WMETER_EQUIPMENT_IDENTIFIER_V2_X(
			"8-0:0.0.0", "", DSMRVersion.V2_VERSIONS, "Equipment identifier",
			"wEquipmentId", OBISStringMsg.class), 
	WMETER_EQUIPMENT_IDENTIFIER(
			"0--1:96.1.0", "", DSMRVersion.V30_UP, "Equipment identifier",
			"wEquipmentId", OBISStringMsg.class), 
	WMETER_VALUE_V2_X(
			"8-0:1.0.0", "m3", DSMRVersion.V2_VERSIONS, "Last hour delivery",
			"wValue", OBISFloatMsg.class), 
	WMETER_VALUE_V3("0--1:24.3.0", "m3",
			DSMRVersion.V3_VERSIONS, "Last hourly meter reading", "wValue",
			OBISFloatMsg.class), 
	WMETER_VALUE_V4("0--1:24.2.1", "m3",
			DSMRVersion.V4_VERSIONS, "Last hourly meter reading", "wValue",
			OBISFloatMsg.class), 
	WMETER_VALVE_POSITION("0--1:24.4.0", "",
			DSMRVersion.V30_UP, "Water valve position", "wValvePosition",
			OBISIntegerMsg.class),

	/* Generic Meter (DSMR v3 / v4) */
	GENMETER_DEVICE_TYPE("0--1:24.1.0", "", DSMRVersion.V30_UP, "Device Type",
			"genericDeviceType", OBISStringMsg.class), 
	GENMETER_EQUIPMENT_IDENTIFIER(
			"0--1:96.1.0", "", DSMRVersion.V30_UP, "Equipment identifier",
			"genericEquipmentId", OBISStringMsg.class), 
	GENMETER_VALUE_V3(
			"0--1:24.3.0", "", DSMRVersion.V3_VERSIONS,
			"Last hourly meter reading", "genericValue", OBISFloatMsg.class), 
	GENMETER_VALUE_V4(
			"0--1:24.2.1", "", DSMRVersion.V4_VERSIONS,
			"Last hourly meter reading", "genericValue", OBISFloatMsg.class), 
	GENMETER_VALVE_POSITION(
			"0--1:24.4.0", "", DSMRVersion.V30_UP,
			"Generic valve/switch position", "genericValvePosition",
			OBISIntegerMsg.class),

	/* Slave E Meter (DSMR v4) */
	SEMETER_DEVICE_TYPE("0--1:24.1.0", "", DSMRVersion.V4_VERSIONS,
			"Slave Electricity Device Type", "seDeviceType",
			OBISStringMsg.class), 
	SEMETER_EQUIPMENT_IDENTIFIER("0--1:96.1.0",
			"", DSMRVersion.V4_VERSIONS,
			"Slave Electricity Equipment identifier", "seEquipmentId",
			OBISStringMsg.class), 
	SEMETER_VALUE_V4("0--1:24.2.1", "kWh",
			DSMRVersion.V4_VERSIONS,
			"Slave Electricity last hourly meter reading", "seValue",
			OBISFloatMsg.class), 
	SEMETER_SWITCH_POSITION("0--1:24.4.0", "",
			DSMRVersion.V4_VERSIONS, "Slave Electricity switch position",
			"seSwitchPosition", OBISIntegerMsg.class);

	public final String obisReference;
	public final String unit;
	public final String bindingReference;
	public final String description;
	public final Class<? extends OBISMessage<? extends State>> clazz;
	public final List<DSMRVersion> applicableVersions;

	private OBISMsgType(
			String obisReference,
			String unit,
			DSMRVersion[] applicableVersions,
			String description,
			String bindingReference,
			Class<? extends OBISMessage<? extends State>> clazz) {
		this.obisReference = obisReference;
		this.unit = unit;
		this.applicableVersions = Arrays.asList(applicableVersions);
		this.description = description;
		this.bindingReference = bindingReference;
		this.clazz = clazz;
	}

	private OBISMsgType(
			String obisReference,
			String unit,
			DSMRVersion applicableVersion,
			String description,
			String bindingReference,
			Class<? extends OBISMessage<? extends State>> clazz) {
		this(obisReference, unit, new DSMRVersion[] { applicableVersion },
				description, bindingReference, clazz);
	}
}
