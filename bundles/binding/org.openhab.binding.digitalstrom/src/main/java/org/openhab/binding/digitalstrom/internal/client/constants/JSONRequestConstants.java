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
 * @author 	Alexander Betker
 * @since 1.3.0
 * @version	digitalSTROM-API 1.14.5
 */
public interface JSONRequestConstants {
	
	// Symbols
	public final static String	SLASH_SYMBOL							= "/";
	
	public final static String	FIRST_PARAMETER_CONCAT_SYMBOL			= "?";
	
	public final static String	NEXT_PARAMETER_CONCAT_SYMBOL			= "&";
	
	public final static String	EQUAL_SIGN_SYMBOL						= "=";
	
	
	// Classes
	public final static String	JSON_TO_STRING							= "json";
	
	public final static String	SYSTEM_TO_STRING						= "system";
	
	public final static String	DEVICE_TO_STRING						= "device";
	
	public final static String	ZONE_TO_STRING							= "zone";
	
	public final static String	APARTMENT_TO_STRING						= "apartment";
	
	public final static String	SET_TO_STRING							= "set";
	
	public final static String	CIRCUIT_TO_STRING						= "circuit";
	
	public final static String	PROPERTY_TO_STRING						= "property";
	
	public final static String	EVENT_TO_STRING							= "event";
	
	public final static String	STRUCTURE_TO_STRING						= "structure";
	
	public final static String	METERING_TO_STRING						= "metering";
	
	
	// Methods
	public final static String	LOGIN_TO_STRING							= "login";
	
	public final static String	LOGOUT_TO_STRING						= "logout";
	
	public final static String	CALLSCENE_TO_STRING						= "callScene";
	
	public final static String	SAVESCENE_TO_STRING						= "saveScene";
	
	public final static String	UNDOSCENE_TO_STRING						= "undoScene";
	
	public final static String	TURN_ON_TO_STRING						= "turnOn";
	
	public final static String	TURN_OFF_TO_STRING						= "turnOff";
	
	public final static String	INCREASE_VALUE_TO_STRING				= "increaseValue";
	
	public final static String	DECREASE_VALUE_TO_STRING				= "decreaseValue";
	
	public final static String	GET_STRUCTURE_TO_STRING 				= "getStructure";
	
	public final static String	GET_DEVICES_TO_STRING					= "getDevices";
	
	public final static String	GET_CIRCUITS_TO_STRING					= "getCircuits";
	
	public final static String	LOGIN_APPLICATION_TO_STRING 			= "loginApplication";
	
	public final static String	GET_NAME_TO_STRING 						= "getName";
	
	public final static String	SET_NAME_TO_STRING 						= "setName";
	
	public final static String	SUBSCRIBE_TO_STRING						= "subscribe";
	
	public final static String	UNSUBSCRIBE_TO_STRING					= "unsubscribe";
	
	public final static String	GET_TO_STRING							= "get";
	
	public final static String	SET_VALUE_TO_STRING						= "setValue";
	
	public final static String	GET_CONSUMPTION_TO_STRING				= "getConsumption";
	
	public final static String	RESCAN_TO_STRING						= "rescan";
	
	public final static String	SCENE_SET_NAME_TO_STRING				= "sceneSetName";
	
	public final static String	SCENE_GET_NAME_TO_STRING				= "sceneGetName";
	
	public final static String	PUSH_SENSOR_VALUES_TO_STRING			= "pushSensorValues";
	
	public final static String	GET_REACHABLE_SCENES_TO_STRING			= "getReachableScenes";
	
	public final static String	GET_STATE_TO_STRING						= "getState";
	
	public final static String	GET_GROUPS_TO_STRING					= "getGroups";
	
	public final static String	GET_ENERGY_METER_VALUE_TO_STRING		= "getEnergyMeterValue";
	
	public final static String	GET_STRING_TO_STRING					= "getString";
	
	public final static String	GET_INTEGER_TO_STRING					= "getInteger";
	
	public final static String	GET_BOOLEAN_TO_STRING					= "getBoolean";
	
	public final static String	SET_STRING_TO_STRING					= "setString";
	
	public final static String	SET_INTEGER_TO_STRING					= "setInteger";
	
	public final static String	SET_BOOLEAN_TO_STRING					= "setBoolean";
	
	public final static String	GET_CHILDREN_TO_STRING					= "getChildren";
	
	public final static String	SET_FLAG_TO_STRING						= "setFlag";
	
	public final static String	GET_FLAGS_TO_STRING						= "getFlags";
	
	public final static String	QUERY_TO_STRING							= "query";
	
	public final static String	REMOVE_TO_STRING						= "remove";
	
	public final static String	GET_TYPE_TO_STRING						= "getType";
	
	public final static String	GET_SPEC_TO_STRING						= "getSpec";
	
	public final static String	VERSION_TO_STRING						= "version";
	
	public final static String	TIME_TO_STRING							= "time";
	
	public final static String	FROM_APARTMENT_TO_STRING				= "fromApartment";
	
	public final static String	BY_ZONE_TO_STRING						= "byZone";
	
	public final static String	BY_GROUP_TO_STRING						= "byGroup";
	
	public final static String	BY_DSID_TO_STRING						= "byDSID";
	
	public final static String	ADD_TO_STRING							= "add";
	
	public final static String	SUBTRACT_TO_STRING						= "subtract";
	
	public final static String	LOGGED_IN_USER_TO_STRING				= "loggedInUser";
	
	public final static String	ZONE_ADD_DEVICE_TO_STRING				= "zoneAddDevice";
	
	public final static String	ADD_ZONE_TO_STRING						= "addZone";
	
	public final static String	REMOVE_ZONE_TO_STRING					= "removeZone";
	
	public final static String	REMOVE_DEVICE_TO_STRING					= "removeDevice";
	
	public final static String	PERSIST_SET_TO_STRING					= "persistSet";
	
	public final static String	UNPERSIST_SET_TO_STRING					= "unpersistSet";
	
	public final static String	ADD_GROUP_TO_STRING						= "addGroup";
	
	public final static String	GROUP_ADD_DEVICE_TO_STRING				= "groupAddDevice";
	
	public final static String	GROUP_REMOVE_DEVICE_TO_STRING			= "groupRemoveDevice";
	
	public final static String	GET_RESOLUTIONS_TO_STRING				= "getResolutions";
	
	public final static String	GET_SERIES_TO_STRING					= "getSeries";
	
	public final static String	GET_VALUES_TO_STRING					= "getValues";
	
	public final static String	GET_LATEST_TO_STRING					= "getLatest";
	
	public final static String	ADD_TAG_TO_STRING						= "addTag";
	
	public final static String	REMOVE_TAG_TO_STRING					= "removeTag";
	
	public final static String	HAS_TAG_TO_STRING						= "hasTag";
	
	public final static String	GET_TAGS_TO_STRING						= "getTags";
	
	public final static String	LOCK_TO_STRING							= "lock";
	
	public final static String	UNLOCK_TO_STRING						= "unlock";
	
	public final static String	GET_SENSOR_EVENT_TABLE_ENTRY_TO_STRING	= "getSensorEventTableEntry";
	
	public final static String	SET_SENSOR_EVENT_TABLE_ENTRY_TO_STRING	= "setSensorEventTableEntry";
	
	public final static String	ADD_TO_AREA_TO_STRING					= "addToArea";
	
	public final static String	REMOVE_FROM_AREA_TO_STRING				= "removeFromArea";
	
	public final static String	SET_CONFIG_TO_STRING					= "setConfig";
	
	public final static String	GET_CONFIG_TO_STRING					= "getConfig";
	
	public final static String	GET_CONFIG_WORD_TO_STRING				= "getConfigWord";
	
	public final static String	SET_JOKER_GROUP_TO_STRING				= "setJokerGroup";
	
	public final static String	SET_BUTTON_ID_TO_STRING					= "setButtonID";
	
	public final static String	SET_BUTTON_INPUT_MODE_TO_STRING			= "setButtonInputMode";
	
	public final static String	SET_OUTPUT_MODE_TO_STRING				= "setOutputMode";
	
	public final static String	SET_PROG_MODE_TO_STRING					= "setProgMode";
	
	public final static String	GET_OUTPUT_VALUE_TO_STRING				= "getOutputValue";
	
	public final static String	SET_OUTPUT_VALUE_TO_STRING				= "setOutputValue";
	
	public final static String	GET_SCENE_MODE_TO_STRING				= "getSceneMode";
	
	public final static String	SET_SCENE_MODE_TO_STRING				= "setSceneMode";
	
	public final static String	GET_TRANSITION_TIME_TO_STRING			= "getTransitionTime";
	
	public final static String	SET_TRANSITION_TIME_TO_STRING			= "setTransitionTime";
	
	public final static String	GET_LED_MODE_TO_STRING					= "getLedMode";
	
	public final static String	SET_LED_MODE_TO_STRING					= "setLedMode";
	
	public final static String	GET_SENSOR_VALUE_TO_STRING				= "getSensorValue";
	
	public final static String	GET_SENSOR_TYPE_TO_STRING				= "getSensorType";
	
	
	// Parameter-Names
	public final static String	TOKEN_TO_STRING							= "token";
	
	public final static String	NAME_TO_STRING 							= "name";
	
	public final static String	NEW_NAME_TO_STRING 						= "newName";
	
	public final static String	DSID_TO_STRING 							= "dsid";
	
	public final static String	SCENENUMBER_TO_STRING 					= "sceneNumber";
	
	public final static String	LOGIN_TOKEN_TO_STRING 					= "loginToken";
	
	public final static String	USER_TO_STRING							= "user";
	
	public final static String	PASSWORD_TO_STRING						= "password";
	
	public final static String	SUBSCRIPTIONID_TO_STRING				= "subscriptionID";
	
	public final static String	TIMEOUT_TO_STRING						= "timeout";
	
	public final static String	GROUP_ID_TO_STRING						= "groupID";
	
	public final static String	GROUP_NAME_TO_STRING					= "groupName";
	
	public final static String	VALUE_TO_STRING							= "value";
	
	public final static String	FORCE_TO_STRING							= "force";
	
	public final static String	ID_TO_STRING							= "id";
	
	public final static String	ENABLE_TO_STRING						= "enable";
	
	public final static String	DISABLE_TO_STRING						= "disable";
	
	public final static String	UNASSIGNED_TO_STRING					= "unassigned";
	
	public final static String	SOURCE_DSID_TO_STRING					= "sourceDSID";
	
	public final static String	SENSOR_TYPE_TO_STRING					= "sensorType";
	
	public final static String	SENSOR_VALUE_TO_STRING					= "sensorValue";
	
	public final static String	FLAG_TO_STRING							= "flag";
	
	public final static String	PATH_TO_STRING							= "path";
	
	public final static String	RAISE_TO_STRING							= "raise";
	
	public final static String	CONTEXT_TO_STRING						= "context";
	
	public final static String	LOCATION_TO_STRING						= "location";
	
	public final static String	SELF_TO_STRING							= "self";
	
	public final static String	ZONE_ID_TO_STRING						= "zoneID";
	
	public final static String	ZONE_NAME_TO_STRING						= "zoneName";
	
	public final static String	OTHER_TO_STRING							= "other";
	
	public final static String	DEVICE_ID_TO_STRING						= "deviceID";
	
	public final static String	UNIT_TO_STRING							= "unit";
	
	public final static String	START_TIME_TO_STRING					= "startTime";
	
	public final static String	END_TIME_TO_STRING						= "endTime";
	
	public final static String	VALUE_COUNT_TO_STRING					= "valueCount";
	
	public final static String	RESOLUTION_TO_STRING					= "resolution";
	
	public final static String	TYPE_TO_STRING							= "type";
	
	public final static String	FROM_TO_STRING							= "from";
	
	public final static String	TAG_TO_STRING							= "tag";
	
	public final static String	CLASS_TO_STRING							= "class";
	
	public final static String	INDEX_TO_STRING							= "index";
	
	public final static String	BUTTON_ID_TO_STRING						= "buttonID";
	
	public final static String	MODE_ID_TO_STRING						= "modeID";
	
	public final static String	MODE_TO_STRING							= "mode";
	
	public final static String	OFFSET_TO_STRING						= "offset";
	
	public final static String	SCENE_ID_TO_STRING						= "sceneID";
	
	public final static String	DONT_CARE_TO_STRING						= "dontCare";
	
	public final static String	LOCAL_PRIO_TO_STRING					= "localPrio";
	
	public final static String	SPECIAL_MODE_TO_STRING					= "specialMode";
	
	public final static String	FLASH_MODE_TO_STRING					= "flashMode";
	
	public final static String	LED_CON_INDEX_TO_STRING					= "ledconIndex";
	
	public final static String	DIM_TIME_INDEX_TO_STRING				= "dimtimeIndex";
	
	public final static String	UP_TO_STRING							= "up";
	
	public final static String	DOWN_TO_STRING							= "down";
	
	public final static String	COLOR_SELECT_TO_STRING					= "colorSelect";
	
	public final static String	MODE_SELECT_TO_STRING					= "modeSelect";
	
	public final static String	DIM_MODE_TO_STRING						= "dimMode";
	
	public final static String	RGB_MODE_TO_STRING						= "rgbMode";
	
	public final static String	GROUP_COLOR_MODE_TO_STRING				= "groupColorMode";
	
	public final static String	SENSOR_INDEX_TO_STRING					= "sensorIndex";
	
	public final static String	EVENT_INDEX_TO_STRING					= "eventIndex";
	
	public final static String	AREA_SCENE_TO_STRING					= "areaScene";
	
	public final static String	EVENT_NAME_TO_STRING					= "eventName";
	
	public final static String	TEST_TO_STRING							= "test";
	
	public final static String	HYSTERSIS_TO_STRING						= "hysteresis";
	
	public final static String	VALIDITY_TO_STRING						= "validity";
	
	public final static String	ACTION_TO_STRING						= "action";
	
	public final static String	BUTTON_NUMBER_TO_STRING					= "buttonNumber";
	
	public final static String	CLICK_TYPE_TO_STRING					= "clickType";
	
	public final static String	SCENE_DEVICE_MODE_TO_STRING				= "sceneDeviceMode";
	
	
	// values
	public final static String	TRUE_TO_STRING							= "true";
	
	
	// Prefixes-Classes
	public final static String	JSON_PREFIX 							= SLASH_SYMBOL+JSON_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_SYSTEM_PREFIX 						= JSON_PREFIX+SYSTEM_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_APARTMENT_PREFIX 					= JSON_PREFIX+APARTMENT_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_DEVICE_PREFIX						= JSON_PREFIX+DEVICE_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_ZONE_PREFIX						= JSON_PREFIX+ZONE_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_SET_PREFIX							= JSON_PREFIX+SET_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_CIRCUIT_PREFIX						= JSON_PREFIX+CIRCUIT_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_EVENT_PREFIX						= JSON_PREFIX+EVENT_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_PROPERTY_PREFIX					= JSON_PREFIX+PROPERTY_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_STRUCTURE_PREFIX					= JSON_PREFIX+STRUCTURE_TO_STRING+SLASH_SYMBOL;
	
	public final static String	JSON_METERING_PREFIX					= JSON_PREFIX+METERING_TO_STRING+SLASH_SYMBOL;
	
	
	// Parameter
	public final static String	PARAMETER_NAME 							= NAME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_NEW_NAME 						= NEW_NAME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_USER							= USER_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_PASSWORD						= PASSWORD_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_DSID							= DSID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SCENE_NUMBER					= SCENENUMBER_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_TOKEN							= TOKEN_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SUBSCRIPTION_ID				= SUBSCRIPTIONID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_TIMEOUT						= TIMEOUT_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_GROUP_ID						= GROUP_ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_GROUP_NAME					= GROUP_NAME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_VALUE							= VALUE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_FORCE							= FORCE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_ID							= ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_UNASSDIGNED					= UNASSIGNED_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SOURCE_DSID					= SOURCE_DSID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SENSOR_TYPE					= SENSOR_TYPE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SENSOR_VALUE					= SENSOR_VALUE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_QUERY							= QUERY_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_FLAG							= FLAG_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_PATH							= PATH_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_CONTEXT						= CONTEXT_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_LOCATION						= LOCATION_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SELF							= SELF_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_ZONE_ID						= ZONE_ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_ZONE_NAME						= ZONE_NAME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_OTHER							= OTHER_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_DEVICE_ID						= DEVICE_ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_ZONE							= ZONE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SET							= SET_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_TYPE							= TYPE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_RESOLUTION					= RESOLUTION_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_UNIT							= UNIT_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_START_TIME					= START_TIME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_END_TIME						= END_TIME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_VALUE_COUNT					= VALUE_COUNT_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_FROM							= FROM_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_TAG							= TAG_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_CLASS							= CLASS_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_INDEX							= INDEX_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_BUTTON_ID						= BUTTON_ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_MODE_ID						= MODE_ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_MODE							= MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_OFFSET						= OFFSET_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SCENE_ID						= SCENE_ID_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_DONT_CARE						= DONT_CARE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_LOCAL_PRIO					= LOCAL_PRIO_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SPECIAL_MODE					= SPECIAL_MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_FLASH_MODE					= FLASH_MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_LED_CON_INDEX					= LED_CON_INDEX_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_DIM_TIME_INDEX				= DIM_TIME_INDEX_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_UP							= UP_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_DOWN							= DOWN_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_COLOR_SELECT					= COLOR_SELECT_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_MODE_SELECT					= MODE_SELECT_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_DIM_MODE						= DIM_MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_RGB_MODE						= RGB_MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_GROUP_COLOR_MODE				= GROUP_COLOR_MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SENSOR_INDEX					= SENSOR_INDEX_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_EVENT_INDEX					= EVENT_INDEX_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_AREA_SCENE					= AREA_SCENE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_EVENT_NAME					= EVENT_NAME_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_TEST							= TEST_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_HYSTERSIS						= HYSTERSIS_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_VALIDITY						= VALIDITY_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_ACTION						= ACTION_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_BUTTON_NUMBER					= BUTTON_NUMBER_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_CLICK_TYPE					= CLICK_TYPE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	PARAMETER_SCENE_DEVICE_MODE				= SCENE_DEVICE_MODE_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	// Infix-Parameter
	public final static String	INFIX_PARAMETER_TIMEOUT					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_TIMEOUT;
	
	public final static String	INFIX_PARAMETER_SUBSCRIPTION_ID			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SUBSCRIPTION_ID;
	
	public final static String	INFIX_PARAMETER_PASSWORD				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_PASSWORD;
	
	public final static String	INFIX_PARAMETER_NEW_NAME				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_NEW_NAME;
	
	public final static String	INFIX_PARAMETER_GROUP_ID				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_GROUP_ID;
	
	public final static String	INFIX_PARAMETER_GROUP_NAME				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_GROUP_NAME;
	
	public final static String	INFIX_PARAMETER_VALUE					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_VALUE;
	
	public final static String	INFIX_PARAMETER_SCENE_NUMBER			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SCENE_NUMBER;
	
	public final static String	INFIX_PARAMETER_FORCE_TRUE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_FORCE+TRUE_TO_STRING;
	
	public final static String	INFIX_PARAMETER_NAME					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_NAME;
	
	public final static String	INFIX_PARAMETER_ID						= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_ID;
	
	public final static String	INFIX_PARAMETER_UNASSIGNED_TRUE			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_UNASSDIGNED+TRUE_TO_STRING;
	
	public final static String	INFIX_PARAMETER_SOURCE_DSID				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SOURCE_DSID;
	
	public final static String	INFIX_PARAMETER_SENSOR_TYPE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SENSOR_TYPE;
	
	public final static String	INFIX_PARAMETER_SENSOR_VALUE			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SENSOR_VALUE;
	
	public final static String	INFIX_PARAMETER_DSID					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_DSID;
	
	public final static String	INFIX_PARAMETER_QUERY					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_QUERY;
	
	public final static String	INFIX_PARAMETER_FLAG					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_FLAG;
	
	public final static String	INFIX_PARAMETER_PATH					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_PATH;
	
	public final static String	INFIX_PARAMETER_CONTEXT					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_CONTEXT;
	
	public final static String	INFIX_PARAMETER_LOCATION				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_LOCATION;
	
	public final static String	INFIX_PARAMETER_SELF					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SELF;
	
	public final static String	INFIX_PARAMETER_ZONE_ID					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_ZONE_ID;
	
	public final static String	INFIX_PARAMETER_ZONE_NAME				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_ZONE_NAME;
	
	public final static String	INFIX_PARAMETER_OTHER					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_OTHER;
	
	public final static String	INFIX_PARAMETER_DEVICE_ID				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_DEVICE_ID;
	
	public final static String	INFIX_PARAMETER_ZONE					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_ZONE;
	
	public final static String	INFIX_PARAMETER_SET						= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SET;
	
	public final static String	INFIX_PARAMETER_TYPE					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_TYPE;
	
	public final static String	INFIX_PARAMETER_RESOLUTION				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_RESOLUTION;
	
	public final static String	INFIX_PARAMETER_UNIT					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_UNIT;
	
	public final static String	INFIX_PARAMETER_START_TIME				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_START_TIME;
	
	public final static String	INFIX_PARAMETER_END_TIME				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_END_TIME;
	
	public final static String	INFIX_PARAMETER_VALUE_COUNT				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_VALUE_COUNT;
	
	public final static String	INFIX_PARAMETER_FROM					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_FROM;
	
	public final static String	INFIX_PARAMETER_TAG						= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_TAG;
	
	public final static String	INFIX_PARAMETER_CLASS					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_CLASS;
	
	public final static String	INFIX_PARAMETER_INDEX					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_INDEX;
	
	public final static String	INFIX_PARAMETER_BUTTON_ID				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_BUTTON_ID;
	
	public final static String	INFIX_PARAMETER_MODE_ID					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_MODE_ID;
	
	public final static String	INFIX_PARAMETER_MODE					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_MODE;
	
	public final static String	INFIX_PARAMETER_OFFSET					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_OFFSET;
	
	public final static String	INFIX_PARAMETER_SCENE_ID				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SCENE_ID;
	
	public final static String	INFIX_PARAMETER_DONT_CARE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_DONT_CARE;
	
	public final static String	INFIX_PARAMETER_LOCAL_PRIO				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_LOCAL_PRIO;
	
	public final static String	INFIX_PARAMETER_SPECIAL_MODE			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SPECIAL_MODE;
	
	public final static String	INFIX_PARAMETER_FLASH_MODE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_FLASH_MODE;
	
	public final static String	INFIX_PARAMETER_LED_CON_INDEX			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_LED_CON_INDEX;
	
	public final static String	INFIX_PARAMETER_DIM_TIME_INDEX			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_DIM_TIME_INDEX;
	
	public final static String	INFIX_PARAMETER_UP						= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_UP;
	
	public final static String	INFIX_PARAMETER_DOWN					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_DOWN;
	
	public final static String	INFIX_PARAMETER_COLOR_SELECT			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_COLOR_SELECT;
	
	public final static String	INFIX_PARAMETER_MODE_SELECT				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_MODE_SELECT;
	
	public final static String	INFIX_PARAMETER_DIM_MODE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_DIM_MODE;
	
	public final static String	INFIX_PARAMETER_RGB_MODE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_RGB_MODE;
	
	public final static String	INFIX_PARAMETER_GROUP_COLOR_MODE		= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_GROUP_COLOR_MODE;
	
	public final static String	INFIX_PARAMETER_SENSOR_INDEX			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SENSOR_INDEX;
	
	public final static String	INFIX_PARAMETER_EVENT_INDEX				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_EVENT_INDEX;
	
	public final static String	INFIX_PARAMETER_AREA_SCENE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_AREA_SCENE;
	
	public final static String	INFIX_PARAMETER_EVENT_NAME				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_EVENT_NAME;
	
	public final static String	INFIX_PARAMETER_TEST					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_TEST;
	
	public final static String	INFIX_PARAMETER_HYSTERSIS				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_HYSTERSIS;
	
	public final static String	INFIX_PARAMETER_VALIDITY				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_VALIDITY;
	
	public final static String	INFIX_PARAMETER_ACTION					= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_ACTION;
	
	public final static String	INFIX_PARAMETER_BUTTON_NUMBER			= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_BUTTON_NUMBER;
	
	public final static String	INFIX_PARAMETER_CLICK_TYPE				= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_CLICK_TYPE;
	
	public final static String	INFIX_PARAMETER_SCENE_DEVICE_MODE		= NEXT_PARAMETER_CONCAT_SYMBOL+PARAMETER_SCENE_DEVICE_MODE;
	
	
	// Apartment
	public final static String	JSON_APARTMENT_GET_STRUCTURE 			= JSON_APARTMENT_PREFIX+GET_STRUCTURE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_GET_NAME					= JSON_APARTMENT_PREFIX+GET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_SET_NAME					= JSON_APARTMENT_PREFIX+SET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_TURN_ON					= JSON_APARTMENT_PREFIX+TURN_ON_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_TURN_OFF					= JSON_APARTMENT_PREFIX+TURN_OFF_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_INCREASE_VALUE			= JSON_APARTMENT_PREFIX+INCREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_DECREASE_VALUE			= JSON_APARTMENT_PREFIX+DECREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_SET_VALUE				= JSON_APARTMENT_PREFIX+SET_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_CALLSCENE				= JSON_APARTMENT_PREFIX+CALLSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_SAVESCENE				= JSON_APARTMENT_PREFIX+SAVESCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_UNDOSCENE				= JSON_APARTMENT_PREFIX+UNDOSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_GET_CONSUMPTION			= JSON_APARTMENT_PREFIX+GET_CONSUMPTION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_GET_DEVICES				= JSON_APARTMENT_PREFIX+GET_DEVICES_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_GET_CIRCUITS				= JSON_APARTMENT_PREFIX+GET_CIRCUITS_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_APARTMENT_RESCAN					= JSON_APARTMENT_PREFIX+RESCAN_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Zone
	public final static String	JSON_ZONE_GET_NAME						= JSON_ZONE_PREFIX+GET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_SET_NAME						= JSON_ZONE_PREFIX+SET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_TURN_ON						= JSON_ZONE_PREFIX+TURN_ON_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_TURN_OFF						= JSON_ZONE_PREFIX+TURN_OFF_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_INCREASE_VALUE				= JSON_ZONE_PREFIX+INCREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_DECREASE_VALUE				= JSON_ZONE_PREFIX+DECREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_ENABLE						= JSON_ZONE_PREFIX+ENABLE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_DISABLE						= JSON_ZONE_PREFIX+DISABLE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_SET_VALUE						= JSON_ZONE_PREFIX+SET_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_CALLSCENE						= JSON_ZONE_PREFIX+CALLSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_SAVESCENE						= JSON_ZONE_PREFIX+SAVESCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_UNDOSCENE						= JSON_ZONE_PREFIX+UNDOSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_GET_CONSUMPTION				= JSON_ZONE_PREFIX+GET_CONSUMPTION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_SCENE_SET_NAME				= JSON_ZONE_PREFIX+SCENE_SET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_SCENE_GET_NAME				= JSON_ZONE_PREFIX+SCENE_GET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_PUSH_SENSOR_VALUES			= JSON_ZONE_PREFIX+PUSH_SENSOR_VALUES_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_ZONE_GET_REACHABLE_SCENES			= JSON_ZONE_PREFIX+GET_REACHABLE_SCENES_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Device
	public final static String	JSON_DEVICE_GET_NAME					= JSON_DEVICE_PREFIX+GET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_NAME					= JSON_DEVICE_PREFIX+SET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_SPEC					= JSON_DEVICE_PREFIX+GET_SPEC_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_GROUPS					= JSON_DEVICE_PREFIX+GET_GROUPS_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_TURN_ON						= JSON_DEVICE_PREFIX+TURN_ON_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_TURN_OFF					= JSON_DEVICE_PREFIX+TURN_OFF_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_INCREASE_VALUE				= JSON_DEVICE_PREFIX+INCREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_DECREASE_VALUE				= JSON_DEVICE_PREFIX+DECREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SAVESCENE					= JSON_DEVICE_PREFIX+SAVESCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_UNDOSCENE					= JSON_DEVICE_PREFIX+UNDOSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_CONSUMPTION				= JSON_DEVICE_PREFIX+GET_CONSUMPTION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_ADD_TAG						= JSON_DEVICE_PREFIX+ADD_TAG_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_REMOVE_TAG					= JSON_DEVICE_PREFIX+REMOVE_TAG_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_HAS_TAG						= JSON_DEVICE_PREFIX+HAS_TAG_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_TAGS					= JSON_DEVICE_PREFIX+GET_TAGS_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_LOCK						= JSON_DEVICE_PREFIX+LOCK_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_UNLOCK						= JSON_DEVICE_PREFIX+UNLOCK_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_SENSOR_EVENT_TABLE_ENTRY	= JSON_DEVICE_PREFIX+GET_SENSOR_EVENT_TABLE_ENTRY_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_SENSOR_EVENT_TABLE_ENTRY	= JSON_DEVICE_PREFIX+SET_SENSOR_EVENT_TABLE_ENTRY_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_ADD_TO_AREA					= JSON_DEVICE_PREFIX+ADD_TO_AREA_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_REMOVE_FROM_AREA			= JSON_DEVICE_PREFIX+REMOVE_FROM_AREA_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_ENABLE						= JSON_DEVICE_PREFIX+ENABLE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_DISABLE						= JSON_DEVICE_PREFIX+DISABLE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_STATE					= JSON_DEVICE_PREFIX+GET_STATE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_CALLSCENE					= JSON_DEVICE_PREFIX+CALLSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_CONFIG					= JSON_DEVICE_PREFIX+SET_CONFIG_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_CONFIG					= JSON_DEVICE_PREFIX+GET_CONFIG_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_CONFIG_WORD				= JSON_DEVICE_PREFIX+GET_CONFIG_WORD_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_JOKER_GROUP				= JSON_DEVICE_PREFIX+SET_JOKER_GROUP_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_BUTTON_ID					= JSON_DEVICE_PREFIX+SET_BUTTON_ID_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_BUTTON_INPUT_MODE			= JSON_DEVICE_PREFIX+SET_BUTTON_INPUT_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_OUTPUT_MODE				= JSON_DEVICE_PREFIX+SET_OUTPUT_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_PROG_MODE				= JSON_DEVICE_PREFIX+SET_PROG_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_OUTPUT_VALUE			= JSON_DEVICE_PREFIX+GET_OUTPUT_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_OUTPUT_VALUE			= JSON_DEVICE_PREFIX+SET_OUTPUT_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_SCENE_MODE				= JSON_DEVICE_PREFIX+GET_SCENE_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_SCENE_MODE				= JSON_DEVICE_PREFIX+SET_SCENE_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_TRANSITION_TIME			= JSON_DEVICE_PREFIX+GET_TRANSITION_TIME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_TRANSITION_TIME			= JSON_DEVICE_PREFIX+SET_TRANSITION_TIME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_LED_MODE				= JSON_DEVICE_PREFIX+GET_LED_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_LED_MODE				= JSON_DEVICE_PREFIX+SET_LED_MODE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_SENSOR_VALUE			= JSON_DEVICE_PREFIX+GET_SENSOR_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_GET_SENSOR_TYPE				= JSON_DEVICE_PREFIX+GET_SENSOR_TYPE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_DEVICE_SET_VALUE					= JSON_DEVICE_PREFIX+SET_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Circuit
	public final static String	JSON_CIRCUIT_GET_NAME					= JSON_CIRCUIT_PREFIX+GET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_CIRCUIT_SET_NAME					= JSON_CIRCUIT_PREFIX+SET_NAME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_CIRCUIT_GET_CONSUMPTION			= JSON_CIRCUIT_PREFIX+GET_CONSUMPTION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_CIRCUIT_GET_ENERGY_METER_VALUE		= JSON_CIRCUIT_PREFIX+GET_ENERGY_METER_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_CIRCUIT_RESCAN						= JSON_CIRCUIT_PREFIX+RESCAN_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Property
	public final static String	JSON_PROPERTY_GET_STRING				= JSON_PROPERTY_PREFIX+GET_STRING_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_GET_INTEGER				= JSON_PROPERTY_PREFIX+GET_INTEGER_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_GET_BOOLEAN				= JSON_PROPERTY_PREFIX+GET_BOOLEAN_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_SET_STRING				= JSON_PROPERTY_PREFIX+SET_STRING_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_SET_INTEGER				= JSON_PROPERTY_PREFIX+SET_INTEGER_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_SET_BOOLEAN				= JSON_PROPERTY_PREFIX+SET_BOOLEAN_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_GET_CHILDREN				= JSON_PROPERTY_PREFIX+GET_CHILDREN_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_GET_TYPE					= JSON_PROPERTY_PREFIX+GET_TYPE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_SET_FLAG					= JSON_PROPERTY_PREFIX+SET_FLAG_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_GET_FLAGS					= JSON_PROPERTY_PREFIX+GET_FLAGS_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_QUERY						= JSON_PROPERTY_PREFIX+QUERY_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_PROPERTY_REMOVE					= JSON_PROPERTY_PREFIX+REMOVE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Event
	public final static String	JSON_EVENT_RAISE						= JSON_EVENT_PREFIX+RAISE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_EVENT_SUBSCRIBE					= JSON_EVENT_PREFIX+SUBSCRIBE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_EVENT_UNSUBSCRIBE					= JSON_EVENT_PREFIX+UNSUBSCRIBE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_EVENT_GET							= JSON_EVENT_PREFIX+GET_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// System
	public final static String	JSON_SYSTEM_VERSION						= JSON_SYSTEM_PREFIX+VERSION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SYSTEM_TIME						= JSON_SYSTEM_PREFIX+TIME_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SYSTEM_LOGIN						= JSON_SYSTEM_PREFIX+LOGIN_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SYSTEM_LOGIN_APPLICATION			= JSON_SYSTEM_PREFIX+LOGIN_APPLICATION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL+LOGIN_TOKEN_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	JSON_SYSTEM_LOGOUT						= JSON_SYSTEM_PREFIX+LOGOUT_TO_STRING;
	
	public final static String	JSON_SYSTEM_LOGGED_IN_USER				= JSON_SYSTEM_PREFIX+LOGGED_IN_USER_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Set
	public final static String	JSON_SET_FROM_APARTMENT					= JSON_SET_PREFIX+FROM_APARTMENT_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_BY_ZONE						= JSON_SET_PREFIX+BY_ZONE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_BY_GROUP						= JSON_SET_PREFIX+BY_GROUP_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_BY_DSID						= JSON_SET_PREFIX+BY_DSID_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_ADD							= JSON_SET_PREFIX+ADD_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_SUBTRACT						= JSON_SET_PREFIX+SUBTRACT_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_TURN_ON						= JSON_SET_PREFIX+TURN_ON_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_TURN_OFF						= JSON_SET_PREFIX+TURN_OFF_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_INCREASE_VALUE					= JSON_SET_PREFIX+INCREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_DECREASE_VALUE					= JSON_SET_PREFIX+DECREASE_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_ENABLE							= JSON_SET_PREFIX+ENABLE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_DISABLE						= JSON_SET_PREFIX+DISABLE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_SET_VALUE						= JSON_SET_PREFIX+SET_VALUE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_CALLSCENE						= JSON_SET_PREFIX+CALLSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_SAVESCENE						= JSON_SET_PREFIX+SAVESCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_UNDOSCENE						= JSON_SET_PREFIX+UNDOSCENE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_SET_GET_CONSUMPTION				= JSON_SET_PREFIX+GET_CONSUMPTION_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Structure
	public final static String	JSON_STRUCTURE_ZONE_ADD_DEVICE			= JSON_STRUCTURE_PREFIX+ZONE_ADD_DEVICE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_ADD_ZONE					= JSON_STRUCTURE_PREFIX+ADD_ZONE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_REMOVE_ZONE				= JSON_STRUCTURE_PREFIX+REMOVE_ZONE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_REMOVE_DEVICE			= JSON_STRUCTURE_PREFIX+REMOVE_DEVICE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_PERSIST_SET				= JSON_STRUCTURE_PREFIX+PERSIST_SET_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_UNPERSIST_SET			= JSON_STRUCTURE_PREFIX+UNPERSIST_SET_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_ADD_GROUP				= JSON_STRUCTURE_PREFIX+ADD_GROUP_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_GROUP_ADD_DEVICE			= JSON_STRUCTURE_PREFIX+GROUP_ADD_DEVICE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_STRUCTURE_GROUP_REMOVE_DEVICE		= JSON_STRUCTURE_PREFIX+GROUP_REMOVE_DEVICE_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Metering
	public final static String	JSON_METERING_GET_RESOLUTIONS			= JSON_METERING_PREFIX+GET_RESOLUTIONS_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_METERING_GET_SERIES				= JSON_METERING_PREFIX+GET_SERIES_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_METERING_GET_VALUES				= JSON_METERING_PREFIX+GET_VALUES_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	public final static String	JSON_METERING_GET_LATEST				= JSON_METERING_PREFIX+GET_LATEST_TO_STRING+FIRST_PARAMETER_CONCAT_SYMBOL;
	
	
	// Token
	public final static String	JSON_TOKEN_AT_LAST_PREFIX 				= NEXT_PARAMETER_CONCAT_SYMBOL+TOKEN_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	JSON_TOKEN_AT_FIRST						= FIRST_PARAMETER_CONCAT_SYMBOL+TOKEN_TO_STRING+EQUAL_SIGN_SYMBOL;
	
	public final static String	QUERY_GET_METERLIST						= "/apartment/dSMeters/*(dSID)";
}
