/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.constants;

/**
 * @author Alexander Betker
 * @since 1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public enum JSONApiResponseKeysEnum {
	
	RESPONSE_OK								("ok"),
	RESPONSE_SUCCESSFUL						("true"),
	RESPONSE_MESSAGE						("message"),
	
	RESULT									("result"),
	
	APARTMENT_GET_NAME						("name"),
	APARTMENT_GET_CONSUMPTION				("consumption"),
	APARTMENT_GET_STRUCTURE					("apartment"),
	APARTMENT_GET_STRUCTURE_ZONES			("zones"),
	APARTMENT_GET_STRUCTURE_ZONES_ID		("id"),
	APARTMENT_GET_STRUCTURE_ZONES_NAME		("name"),
	APARTMENT_GET_STRUCTURE_ZONES_ISPRESENT	("isPresent"),
	APARTMENT_GET_STRUCTURE_ZONES_DEVICES	("devices"),
	APARTMENT_GET_STRUCTURE_ZONES_GROUPS	("groups"),
	
	APARTMENT_GET_DEVICES					("result"),
	APARTMENT_GET_CIRCUITS					("circuits"),
	
	ZONE_GET_NAME							("name"),
	ZONE_GET_CONSUMPTION					("consumption"),
	ZONE_SCENE_GET_NAME						("name"),
	ZONE_GET_REACHABLE_SCENES				("reachableScenes"),
	
	DEVICE_GET_NAME						("name"),
	DEVICE_GET_SPEC						("result"),
	DEVICE_GET_GROUPS					("groups"),
	DEVICE_GET_GROUPS_ID				("id"),
	DEVICE_GET_GROUPS_NAME				("name"),
	DEVICE_GET_STATE					("isOn"),
	DEVICE_GET_CONSUMPTION				("consumption"),
	DEVICE_HAS_TAG						("hasTag"),
	DEVICE_GET_TAGS						("tags"),
	DEVICE_GET_CONFIG					("result"),
	DEVICE_GET_CONFIG_CLASS				("class"),
	DEVICE_GET_CONFIG_INDEX				("index"),
	DEVICE_GET_CONFIG_VALUE				("value"),
	DEVICE_GET_CONFIG_WORD				("result"),
	DEVICE_GET_OUTPUT_VALUE				("value"),
	DEVICE_GET_SCENE_MODE				("result"),
	DEVICE_GET_SCENE_MODE_SCENE_ID		("sceneID"),
	DEVICE_GET_SCENE_MODE_DONT_CARE		("dontCare"),
	DEVICE_GET_SCENE_MODE_LOCAL_PRIO	("localPrio"),
	DEVICE_GET_SCENE_MODE_SPECIAL_MODE	("specialMode"),
	DEVICE_GET_SCENE_MODE_FLASH_MODE	("flashMode"),
	DEVICE_GET_SCENE_MODE_LEDCON_INDEX	("ledconIndex"),
	DEVICE_GET_SCENE_MODE_DIMTIME_INDEX	("dimtimeIndex"),
	DEVICE_GET_TRANSITION_TIME			("result"),
	DEVICE_GET_TRANSITION_TIME_INDEX	("dimtimeIndex"),
	DEVICE_GET_TRANSITION_TIME_UP		("up"),
	DEVICE_GET_TRANSITION_TIME_DOWN		("down"),
	
	DEVICE_GET_LED_MODE						("result"),
	DEVICE_GET_LED_MODE_INDEX				("ledconIndex"),
	DEVICE_GET_LED_MODE_COLOR				("colorSelect"),
	DEVICE_GET_LED_MODE_SELECT				("modeSelect"),
	DEVICE_GET_LED_MODE_DIM_MODE			("dimMode"),
	DEVICE_GET_LED_MODE_RGB					("rgbMode"),
	DEVICE_GET_LED_MODE_GROUP_COLOR_MODE	("groupColorMode"),
	
	DEVICE_GET_SENSOR_VALUE					("result"),
	DEVICE_GET_SENSOR_VALUE_SENSOR_VALUE	("sensorValue"),
	DEVICE_GET_SENSOR_TYPE					("result"),
	DEVICE_GET_SENSOR_TYPE_TYPE				("sensorType"),
	
	DEVICE_GET_SENSOR_EVENT_TABLE_ENTRY					("result"),
	DEVICE_GET_SENSOR_EVENT_TABLE_ENTRY_EVENT_INDEX		("eventIndex"),
	DEVICE_GET_SENSOR_EVENT_TABLE_ENTRY_EVENT_NAME		("eventName"),
	DEVICE_GET_SENSOR_EVENT_TABLE_ENTRY_IS_SCENE_DEVICE	("isSceneDevice"),
	DEVICE_GET_SENSOR_EVENT_TABLE_ENTRY_SENSOR_INDEX	("sensorIndex"),
	DEVICE_GET_SENSOR_EVENT_TABLE_TEST					("test"),
	DEVICE_GET_SENSOR_EVENT_TABLE_ACTION				("action"),
	DEVICE_GET_SENSOR_EVENT_TABLE_VALUE					("value"),
	DEVICE_GET_SENSOR_EVENT_TABLE_HYSTERSIS				("hysteresis"),
	DEVICE_GET_SENSOR_EVENT_TABLE_VALIDITY				("validity"),
	
	// Device
	DEVICE_NAME				("name"),
	DEVICE_ID				("id"),
	DEVICE_ID_QUERY			("dSID"),
	DEVICE_FUNCTION_ID		("functionID"),
	DEVICE_PRODUCT_REVISION	("productRevision"),
	DEVICE_PRODUCT_ID		("productID"),
	DEVICE_HW_INFO			("hwInfo"),
	DEVICE_ON				("on"),
	DEVICE_OUTPUT_MODE		("outputMode"),
	DEVICE_BUTTON_ID		("buttonID"),
	DEVICE_IS_PRESENT		("isPresent"),
	DEVICE_IS_PRESENT_QUERY	("present"),
	DEVICE_ZONE_ID			("zoneID"),
	DEVICE_ZONE_ID_QUERY	("ZoneID"),
	DEVICE_GROUPS			("groups"),
	
	// DeviceSpec
	DEVICE_SPEC_FUNCTION_ID	("functionID"),
	DEVICE_SPEC_PRODUCT_ID	("productID"),
	DEVICE_SPEC_REVISION_ID	("revisionID"),
	
	EVENT_GET_EVENT			("events"),
	EVENT_GET_EVENT_ERROR	("message"),
	EVENT_NAME				("name"),
	EVENT_PROPERTIES		("properties"),
	
	DS_METER_QUERY				("dSMeters"),
	DS_METER_DSID				("dsid"),
	DS_METER_DSID_QUERY			("dSID"),
	DS_METER_IS_PRESENT			("isPresent"),
	DS_METER_IS_PRESENT_QUERY	("present"),
	
	DS_METER_POWER_CONSUMPTION_QUERY		("powerConsumption"),
	DS_METER_ENERGY_METER_VALUE_QUERY		("energyMeterValue"),
	DS_METER_ENERGY_METER_VALUE_WS_QUERY	("energyMeterValueWs"),
	
	// Group
	GROUP_ID					("id"),
	GROUP_NAME					("name"),
	GROUP_IS_PRESENT			("isPresent"),
	GROUP_DEVICES				("devices"),
	
	CIRCUIT_GET_NAME			("name"),
	CIRCUIT_GET_CONSUMPTION		("consumption"),
	CIRCUIT_GET_METER_VALUE		("meterValue"),
	
	PROPERTY_GET_STRING			("value"),
	PROPERTY_GET_INTEGER		("value"),
	PROPERTY_GET_BOOLEAN		("value"),
	PROPERTY_NAME				("name"),
	PROPERTY_TYPE				("type"),
	PROPERTY_QUERY				("result"),
	PROPERTY_QUERY_ZONE_ID		("ZoneID"),
	PROPERTY_QUERY_DEVICE_ID	("dSID"),
	PROPERTY_GET_CHILDREN		("result"),
	PROPERTY_GET_PROPERTY_TYPE	("type"),
	PROPERTY_GET_FLAGS			("result"),
	
	SYSTEM_GET_VERSION			("version"),
	SYSTEM_GET_TIME				("time"),
	SYSTEM_LOGIN				("token"),
	SYSTEM_LOGGED_IN_USER		("name"),
	
	SET_FROM_APARTMENT			("self"),
	SET_BY_ZONE					("self"),
	SET_BY_GROUP				("self"),
	SET_BY_DSID					("self"),
	SET_ADD						("self"),
	SET_SUBTRACT				("self"),
	SET_GET_CONSUMPTION			("consumption"),
	
	STRUCTURE_PERSIST			("groupID"),
	
	METERING_GET_RESOLUTIONS		("resolutions"),
	METERING_GET_RESOLUTION			("resolution"),
	METERING_GET_SERIES				("series"),
	METERING_GET_SERIES_DSID		("dsid"),
	METERING_GET_SERIES_TYPE		("type"),
	METERING_GET_VALUES				("result"),
	METERING_GET_VALUES_METER_ID	("meterID"),
	METERING_GET_VALUES_TYPE		("type"),
	METERING_GET_VALUES_UNIT		("unit"),
	METERING_GET_VALUES_RESOLUTION	("resolution"),
	METERING_GET_VALUES_VALUES		("values"),
	
	METERING_GET_LATEST			("values"),
	METERING_GET_LATEST_DSID	("dsid"),
	METERING_GET_LATEST_VALUE	("value"),
	METERING_GET_LATEST_DATE	("date"),
	
	QUERY_ZONE_ID				("ZoneID");
	
	
	private final String key;
	
	private JSONApiResponseKeysEnum(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

}
