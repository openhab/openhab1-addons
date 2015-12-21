/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm.internal.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerator for API Command and Message Codes
 * @author Russell Stephens
 * @since 1.6.0
 */
public enum APICode {
	
	Poll("000"),
	StatusReport("001"),
	LabelsRequest("002"),
    NetworkLogin("005"),
    DumpZoneTimers("008"),
    SetTimeDate("010"),
    CommandOutputControl("020"),
    PartitionArmControlAway("030"),
    PartitionArmControlStay("031"),
    PartitionArmControlZeroEntryDelay("032"),
    PartitionArmControlWithUserCode("033"),
    PartitionDisarmControl("040"),
    TimeStampControl("055"),
    TimeDateBroadcastControl("056"),
    TemperatureBroadcastControl("057"),
    VirtualKeypadControl("058"),
    TriggerPanicAlarm("060"),
    KeyStroke("070"),
    KeySequence("071"),
    EnterUserCodeProgramming("072"),
    EnterUserProgramming("073"),
    KeepAlive("074"),
    BaudRateChange("080"),
    GetTemperatureSetPoint("095"),
    TemperatureChange("096"),
    SaveTemperatureSetting("097"),
    CodeSend("200"),

    CommandAcknowledge("500"),
    CommandError("501"),
    SystemError("502"),
    LoginResponse("505"),
    KeypadLEDState("510"),
    KeypadLEDFlashState("511"),
    TimeDateBroadcast("550"),
    RingDetected("560"),
    IndoorTemperatureBroadcast("561"),
    OutdoorTemperatureBroadcast("562"),
    ThermostatSetPoints("563"),
    BroadcastLabels("570"),
    BaudRateSet ("580"),
    
    ZoneAlarm("601"),
    ZoneAlarmRestore("602"),
    ZoneTamper("603"),
    ZoneTamperRestore("604"),
    ZoneFault("605"),
    ZoneFaultRestore("606"),
    ZoneOpen("609"),
    ZoneRestored("610"),
    EnvisalinkZoneTimerDump("615"),
    DuressAlarm("620"),
    FireKeyAlarm("621"),
    FireKeyRestored("622"),
    AuxiliaryKeyAlarm("623"),
    AuxiliaryKeyRestored("624"),
    PanicKeyAlarm("625"),
    PanicKeyRestored("626"),
    AuxiliaryInputAlarm("631"),
    AuxiliaryInputAlarmRestored("632"),
    PartitionReady("650"),
    PartitionNotReady("651"),
    PartitionArmed("652"),
    PartitionReadyForceArming("653"),
    PartitionInAlarm("654"),
    PartitionDisarmed("655"),
    ExitDelayInProgress("656"),
    EntryDelayInProgress("657"),
    KeypadLockout("658"),
    PartitionFailedToArm("659"),
    PGMOutputInProgress("660"),
    ChimeEnabled("663"),
    ChimeDisabled("664"),
    InvalidAccessCode("670"),
    FunctionNotAvailable("671"),
    FailureToArm("672"),
    PartitionBusy("673"),
    SystemArmingInProgress("674"),
    SystemInInstallerMode("680"),
    
    UserClosing("700"),
    SpecialClosing("701"),
    PartialClosing("702"),
    UserOpening("750"),
    SpecialOpening("751"),
    
    PanelBatteryTrouble("800"),
    PanelBatteryTroubleRestore("801"),
    PanelACTrouble("802"),
    PanelACRestore("803"),
    SystemBellTrouble("806"),
    SystemBellTroubleRestore("807"),
    TLMLine1Trouble("810"),
    TLMLine1TroubleRestore("811"),
    TLMLine2Trouble("812"),
    TLMLine2TroubleRestore("813"),
    FTCTrouble("814"),
    BufferNearFull("816"),
    GeneralDeviceLowBattery("821"),
    GeneralDeviceLowBatteryRestore("822"),
    WirelessKeyLowBatteryTrouble("825"),
    WirelessKeyLowBatteryTroubleRestore("826"),
    HandheldKeypadLowBatteryTrouble("827"),
    HandheldKeypadLowBatteryTroubleRestore("828"),
    GeneralSystemTamper("829"),
    GeneralSystemTamperRestore("830"),
    HomeAutomationTrouble("831"),
    HomeAutomationTroubleRestore("832"),
    TroubleLEDOn("840"),
    TroubleLEDOff("841"),
    FireTroubleAlarm("842"),
    FireTroubleAlarmRestore("843"),
    VerboseTroubleStatus("849"),
    KeybusFault("896"),
    KeybusFaultRestore("897"),

    CodeRequired("900"),
    LCDUpdate("901"),
    LCDCursor("902"),
    LEDStatus("903"),
    BeepStatus("904"),
    ToneStatus("905"),
    BuzzerStatus("906"),
    DoorChimeStatus("907"),
    SoftwareVersion("908"),
    CommandOutputPressed("912"),
    MasterCodeRequired("921"),
    InstallersCodeRequired("922"),

    UnknownCode("-1");

	private String code;

	/**
	 * Lookup map to get a APICode value from its string code
	 */
	private static Map<String, APICode> codeToAPICodeValue;
	
	/**
	 * Constructor
	 * 
	 * @param code
	 */
	private APICode(String code) {
		this.code = code;
	}

	/**
	 * Initialize the lookup map that gets a APICode value from a string code
	 */
	private static void initMapping() {
		codeToAPICodeValue = new HashMap<String, APICode>();
		for (APICode s : values()) {
			codeToAPICodeValue.put(s.code, s);
		}
	}
	
	/**
	 * The API command/message code string (example '005')
	 */
	public String getCode() {
		return code;
	}
	
   /**
	 * Lookup function to return the APICode value based on the string code. 
	 * Returns null if the string code is not found
	 * 
	 * @param code
	 * @return enum value
	 */
	public static APICode getAPICodeValue(String code) {
		APICode apiCode;
		
		if (codeToAPICodeValue == null) {
			initMapping();
		}
		
		apiCode = codeToAPICodeValue.get(code);
		
		if(apiCode == null)
			apiCode = UnknownCode;
		
		return apiCode;
	}
}
