/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal.protocol;


import org.openhab.binding.dscalarm.internal.protocol.APICode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that processes API Messages
 * 
 * @author Russell Stephens
 * @author Donn Renk
 * @since 1.6.0
 */
public class APIMessage {

	private static final Logger logger = LoggerFactory.getLogger(APIMessage.class);

	public enum APIMessageType {
		PANEL_EVENT,
		PARTITION_EVENT,
		ZONE_EVENT,
		KEYPAD_EVENT;
	}
	
	private String apiMessage = "";
	private String apiName = "";
	private String apiDescription = "";
	private String apiCodeReceived = "";
	private APIMessageType apiMessageType = APIMessageType.PANEL_EVENT;
	private int partition = 0;
	private int zone = 0;
	private String data = "";
	private String mode= "";
	private String user = "";

	/**
	 * Constructor. Creates a new instance of the APIMessage class.
	 * 
	 * @param message - the message received
	 */
	public APIMessage(String message) {
		apiMessage = message;
		parseAPIMessage();
	}
	
	/**
	 * Parses the API message and extracts the information
	 */
	private void parseAPIMessage() {
		
		if (apiMessage.length() > 3) {
			try {
				apiMessage = apiMessage.substring(0, apiMessage.length() - 2);
				apiCodeReceived = apiMessage.substring(0, 3);
			
				if(apiMessage.length() >= 4) {
					data = apiMessage.substring(3);
				}
			}
			catch (Exception e) {
	        	logger.error("parseAPIMessage(): Error processing message - {}",apiMessage );
	        	apiCodeReceived = "000";
			}
		   	 
			if(APICode.getAPICodeValue(apiCodeReceived) != null) {
				
				switch (APICode.getAPICodeValue(apiCodeReceived)) {
					case CommandAcknowledge: /*500*/
						apiName = "Command Acknowledge";
						apiDescription = apiCodeReceived + ": A command has been received successfully.";
						break;
					case CommandError: /*501*/
						apiName = "Command Error";
						apiDescription = apiCodeReceived + ": A command has been received with a bad checksum.";
						break;
					case SystemError: /*502*/
						apiName = "System Error";
						apiDescription = apiCodeReceived + ": An error has been detected.";
						break;
					case LoginResponse: /*505*/
						apiName = "Login Interaction";
						apiDescription = apiCodeReceived + ": Login response (failed=0, success=1, time out=2, password request=3).";
						break;
					case KeypadLEDState: /*510*/
						apiName = "Keypad LED State - Partition 1 Only";
						apiDescription = apiCodeReceived + ": A change of state in the Partition 1 keypad LEDs.";
						apiMessageType = APIMessageType.KEYPAD_EVENT;
						break;
					case KeypadLEDFlashState: /*511*/
						apiName = "Keypad LED Flash State - Partition 1 Only";
						apiDescription = apiCodeReceived + ": A change of state in the Partition 1 keypad LEDs as to whether to flash or not.";
						apiMessageType = APIMessageType.KEYPAD_EVENT;
						break;
					case TimeDateBroadcast: /*550*/
						apiName = "Time-Date Broadcast";
						apiDescription = apiCodeReceived + ": The current security system time.";
						data = apiMessage.substring(4);
						break;
					case RingDetected: /*560*/
						apiName = "Ring Detected";
						apiDescription = apiCodeReceived + ": A ring on the telephone line.";
						break;
					case IndoorTemperatureBroadcast: /*561*/
						apiName = "Indoor Temperature Broadcast";
						apiDescription = apiCodeReceived + ": The interior temperature and the thermostat number.";
						break;
					case OutdoorTemperatureBroadcast: /*562*/
						apiName = "Outdoor Temperature Broadcast";
						apiDescription = apiCodeReceived + ": The exterior temperature and the thermostat number.";
						break;
					case ThermostatSetPoints: /*563*/
						apiName = "Thermostat Set Points";
						apiDescription = apiCodeReceived + ": Cooling and heating set points and the thermostat number.";
						break;
					case BroadcastLabels: /*570*/
						apiName = "Broadcast Labels";
						apiDescription = apiCodeReceived + ": Labels stored in the DSC Alarm.";
						break;
					case BaudRateSet: /*580*/
						apiName = "Baud Rate Set";
						apiDescription = apiCodeReceived + ": Baud Rate of the serial interface.";
						break;
					
					case ZoneAlarm: /*601*/
						apiName = "Zone Alarm";
						apiDescription = apiCodeReceived + ": A zone has gone into alarm.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						zone = Integer.parseInt(apiMessage.substring(4));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case ZoneAlarmRestore: /*602*/
						apiName = "Zone Alarm Restore";
						apiDescription = apiCodeReceived + ": A zone alarm has been restored.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						zone = Integer.parseInt(apiMessage.substring(4));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case ZoneTamper: /*603*/
						apiName = "Zone Tamper";
						apiDescription = apiCodeReceived + ": A zone has a tamper condition.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						zone = Integer.parseInt(apiMessage.substring(4));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case ZoneTamperRestore: /*604*/
						apiName = "Zone Tamper Restored";
						apiDescription = apiCodeReceived + ": A zone tamper condition has been restored.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						zone = Integer.parseInt(apiMessage.substring(4));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case ZoneFault: /*605*/
						apiName = "Zone Fault";
						apiDescription = apiCodeReceived + ": A zone has a fault condition.";
						zone = Integer.parseInt(apiMessage.substring(3));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case ZoneFaultRestore: /*606*/
						apiName = "Zone Fault Restored";
						apiDescription = apiCodeReceived + ": A zone fault condition has been restored.";
						zone = Integer.parseInt(apiMessage.substring(3));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case ZoneOpen: /*609*/
						apiName = "Zone Open";
						apiDescription = apiCodeReceived + ": General status of the zone - open.";
						apiMessageType = APIMessageType.ZONE_EVENT;
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case ZoneRestored: /*610*/
						apiName = "Zone Restored";
						apiDescription = apiCodeReceived + ": General status of the zone - restored.";
						zone = Integer.parseInt(apiMessage.substring(3));
						apiMessageType = APIMessageType.ZONE_EVENT;
						break;
					case EnvisalinkZoneTimerDump: /*615*/
						apiName = "Envisalink Zone Timer Dump";
						apiDescription = apiCodeReceived + ": The raw zone timers used inside the Envisalink.";
						break;
					case DuressAlarm: /*620*/
						apiName = "Duress Alarm";
						apiDescription = apiCodeReceived + ": A duress code has been entered on a system keypad.";
						break;
					case FireKeyAlarm: /*621*/
						apiName = "Fire Key Alarm";
						apiDescription = apiCodeReceived + ": A Fire key alarm has been activated.";
						break;
					case FireKeyRestored: /*622*/
						apiName = "Fire Key Alarm Restore";
						apiDescription = apiCodeReceived + ": A Fire key alarm has been restored.";
						break;
					case AuxiliaryKeyAlarm: /*623*/
						apiName = "Auxiliary Key Alarm";
						apiDescription = apiCodeReceived + ": An Auxiliary key alarm has been activated.";
						break;
					case AuxiliaryKeyRestored: /*624*/
						apiName = "Auxiliary Key Alarm Restore";
						apiDescription = apiCodeReceived + ": An Auxiliary key alarm has been restored.";
						break;
					case PanicKeyAlarm: /*625*/
						apiName = "Panic Key Alarm";
						apiDescription = apiCodeReceived + ": A Panic key alarm has been activated.";
						break;
					case PanicKeyRestored: /*626*/
						apiName = "Panic Key Alarm Restore";
						apiDescription = apiCodeReceived + ": A Panic key alarm has been restored.";
						break;
					case AuxiliaryInputAlarm: /*631*/
						apiName = "2-Wire Smoke/Aux Alarm";
						apiDescription = apiCodeReceived + ": A 2-wire smoke/Auxiliary alarm has been activated.";
						break;
					case AuxiliaryInputAlarmRestored: /*632*/
						apiName = "2-Wire Smoke/Aux Restore";
						apiDescription = apiCodeReceived + ": A 2-wire smoke/Auxiliary alarm has been restored.";
						break;
					case PartitionReady: /*650*/
						apiName = "Partition Ready";
						apiDescription = apiCodeReceived + ": Partition can now be armed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionNotReady: /*651*/
						apiName = "Partition Not Ready";
						apiDescription = apiCodeReceived + ": Partition can not be armed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionArmed: /*652*/
						apiName = "Partition Armed (0=Away, 1=Stay, 2=ZEA, 3=ZES)";
						apiDescription = apiCodeReceived + ": Partition has been armed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						mode = apiMessage.substring(4);
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionReadyForceArming: /*653*/
						apiName = "Partition Ready � Force Arming Enabled";
						apiDescription = apiCodeReceived + ": Partition can now be armed (Force Arming Enabled).";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionInAlarm: /*654*/
						apiName = "Partition In Alarm";
						apiDescription = apiCodeReceived + ": A partition is in alarm.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionDisarmed: /*655*/
						apiName = "Partition Disarmed";
						apiDescription = apiCodeReceived + ": A partition has been disarmed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case ExitDelayInProgress: /*656*/
						apiName = "Exit Delay in Progress";
						apiDescription = apiCodeReceived + ": A partition is in Exit Delay.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case EntryDelayInProgress: /*657*/
						apiName = "Entry Delay in Progress";
						apiDescription = apiCodeReceived + ": A partition is in Entry Delay.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case KeypadLockout: /*658*/
						apiName = "Keypad Lock-out";
						apiDescription = apiCodeReceived + ": A partition is in Keypad Lockout.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionFailedToArm: /*659*/
						apiName = "Partition Failed to Arm";
						apiDescription = apiCodeReceived + ": An attempt to arm the partition has failed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PGMOutputInProgress: /*660*/
						apiName = "PGM Output is in Progress";
						apiDescription = apiCodeReceived + ": *71, *72, *73, or *74 has been pressed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case ChimeEnabled: /*663*/
						apiName = "Chime Enabled";
						apiDescription = apiCodeReceived + ": The door chime feature has been enabled.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case ChimeDisabled: /*664*/
						apiName = "Chime Disabled";
						apiDescription = apiCodeReceived + ": The door chime feature has been disabled.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case InvalidAccessCode: /*670*/
						apiName = "Invalid Access Code";
						apiDescription = apiCodeReceived + ": An access code that was entered was invalid.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case FunctionNotAvailable: /*671*/
						apiName = "Function Not Available";
						apiDescription = apiCodeReceived + ": A function that was selected is not available.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case FailureToArm: /*672*/
						apiName = "Failure to Arm";
						apiDescription = apiCodeReceived + ": An attempt was made to arm the partition and it failed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartitionBusy: /*673*/
						apiName = "Partition is Busy";
						apiDescription = apiCodeReceived + ": The partition is busy.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case SystemArmingInProgress: /*674*/
						apiName = "System Arming in Progress";
						apiDescription = apiCodeReceived + ": This system is auto-arming and is in arm warning delay.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case SystemInInstallerMode: /*680*/
						apiName = "System in Installers Mode";
						apiDescription = apiCodeReceived + ": The whole system is in installers mode.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						break;
						
					case UserClosing: /*700*/
						apiName = "User Closing";
						apiDescription = apiCodeReceived + ": A partition has been armed by a user.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						user = apiMessage.substring(4);
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case SpecialClosing: /*701*/
						apiName = "Special Closing";
						apiDescription = apiCodeReceived + ": A partition has been armed by one of the following methods: Quick Arm, Auto Arm, Keyswitch, DLS software, Wireless Key.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case PartialClosing: /*702*/
						apiName = "Partial Closing";
						apiDescription = apiCodeReceived + ": A partition has been armed but one or more zones have been bypassed.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case UserOpening: /*750*/
						apiName = "User Opening";
						apiDescription = apiCodeReceived + ": A partition has been disarmed by a user.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						user = apiMessage.substring(4);
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
					case SpecialOpening: /*751*/
						apiName = "Special Opening";
						apiDescription = apiCodeReceived + ": A partition has been disarmed by one of the following methods: Quick Arm, Auto Arm, Keyswitch, DLS software, Wireless Key.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						apiMessageType = APIMessageType.PARTITION_EVENT;
						break;
	
					case PanelBatteryTrouble: /*800*/
						apiName = "Panel Battery Trouble";
						apiDescription = apiCodeReceived + ": The panel has a low battery.";
						break;
					case PanelBatteryTroubleRestore: /*801*/
						apiName = "Panel Battery Trouble Restore";
						apiDescription = apiCodeReceived + ": The panel�s low battery has been restored.";
						break;
					case PanelACTrouble: /*802*/
						apiName = "Panel AC Trouble";
						apiDescription = apiCodeReceived + ": AC power to the panel has been removed.";
						break;
					case PanelACRestore: /*803*/
						apiName = "Panel AC Restore";
						apiDescription = apiCodeReceived + ": AC power to the panel has been restored.";
						break;
					case SystemBellTrouble: /*806*/
						apiName = "System Bell Trouble";
						apiDescription = apiCodeReceived + ": An open circuit has been detected across the bell terminals.";
						break;
					case SystemBellTroubleRestore: /*807*/
						apiName = "System Bell Trouble Restore";
						apiDescription = apiCodeReceived + ": The bell trouble has been restored.";
						break;
					case TLMLine1Trouble: /*810*/
						apiName = "TML Line 1 Trouble";
						apiDescription = apiCodeReceived + ": The phone line is a open or shorted condition.";
						break;
					case TLMLine1TroubleRestore: /*811*/
						apiName = "TML Line 1 Trouble Restore";
						apiDescription = apiCodeReceived + ": The phone line trouble condition has been restored.";
						break;
					case TLMLine2Trouble: /*812*/
						apiName = "TML Line 2 Trouble";
						apiDescription = apiCodeReceived + ": The phone line is a open or shorted condition.";
						break;
					case TLMLine2TroubleRestore: /*813*/
						apiName = "TML Line 2 Trouble Restore";
						apiDescription = apiCodeReceived + ": The phone line trouble condition has been restored.";
						break;
					case FTCTrouble: /*814*/
						apiName = "FTC Trouble";
						apiDescription = apiCodeReceived + ": The panel has failed to communicate successfully to the monitoring station.";
						break;
					case BufferNearFull: /*816*/
						apiName = "Buffer Near Full";
						apiDescription = apiCodeReceived + ": The panel event buffer is 75% full from when it was last uploaded to DLS.";
						break;
					case GeneralDeviceLowBattery: /*821*/
						apiName = "General Device Low Battery";
						apiDescription = apiCodeReceived + ": A wireless zone has a low battery.";
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case GeneralDeviceLowBatteryRestore: /*822*/
						apiName = "General Device Low Battery Restore";
						apiDescription = apiCodeReceived + ": A wireless zone has a low battery.";
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case WirelessKeyLowBatteryTrouble: /*825*/
						apiName = "Wireless Key Low Battery Trouble";
						apiDescription = apiCodeReceived + ": A wireless key has a low battery.";
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case WirelessKeyLowBatteryTroubleRestore: /*826*/
						apiName = "Wireless Key Low Battery Trouble Restore";
						apiDescription = apiCodeReceived + ": A wireless key low battery condition has been restored.";
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case HandheldKeypadLowBatteryTrouble: /*827*/
						apiName = "Handheld Keypad Low Battery Trouble";
						apiDescription = apiCodeReceived + ": A handhekd keypad has a low battery.";
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case HandheldKeypadLowBatteryTroubleRestore: /*("828*/
						apiName = "Handheld Keypad Low Battery Trouble Restore";
						apiDescription = apiCodeReceived + ": A handhekd keypad low battery condition has been restored.";
						zone = Integer.parseInt(apiMessage.substring(3));
						break;
					case GeneralSystemTamper: /*829*/
						apiName = "General System Tamper";
						apiDescription = apiCodeReceived + ": A tamper has occurred with a system module.";
						break;
					case GeneralSystemTamperRestore: /*830*/
						apiName = "General System Tamper Restore";
						apiDescription = apiCodeReceived + ": A general system Tamper has been restored.";
						break;
					case HomeAutomationTrouble: /*831*/
						apiName = "Home Automation Trouble";
						apiDescription = apiCodeReceived + ": A Escort 5580 module trouble.";
						break;
					case HomeAutomationTroubleRestore: /*832*/
						apiName = "Home Automation Trouble Restore";
						apiDescription = apiCodeReceived + ": A Escort 5580 module trouble has been restored.";
						break;
					case TroubleLEDOn: /*840*/
						apiName = "Trouble LED ON";
						apiDescription = apiCodeReceived + ": The trouble LED on a keypad is ON.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						break;
					case TroubleLEDOff: /*841*/
						apiName = "Trouble LED OFF";
						apiDescription = apiCodeReceived + ": The trouble LED on a keypad is OFF.";
						partition = Integer.parseInt(apiMessage.substring(3, 4));
						break;
					case FireTroubleAlarm: /*842*/
						apiName = "Fire Trouble Alarm";
						apiDescription = apiCodeReceived + ": Fire trouble alarm.";
						break;
					case FireTroubleAlarmRestore: /*843*/
						apiName = "Fire Trouble Alarm Restore";
						apiDescription = apiCodeReceived + ": Fire trouble alarm restored.";
						break;
					case VerboseTroubleStatus: /*849*/
						apiName = "Verbose Trouble Status";
						apiDescription = apiCodeReceived + ": a trouble appears on the system and roughly every 5 minutes until the trouble is cleared.";
						break;
					case KeybusFault: /*896*/
						apiName = "Keybus Fault";
						apiDescription = apiCodeReceived + ": Keybus fault condition.";
						break;
					case KeybusFaultRestore: /*896*/
						apiName = "Keybus Fault Restore";
						apiDescription = apiCodeReceived + ": Keybus fault has been restored.";
						break;
	
					case CodeRequired: /*900*/
						apiName = "Code Required";
						apiDescription = apiCodeReceived + ": Tells the API to enter an access code.";
						break;
					case LCDUpdate: /*901*/
						apiName = "LCD Update";
						apiDescription = apiCodeReceived + ": Text of the IT-100 menu has changed.";
						break;
					case LCDCursor: /*902*/
						apiName = "LCD Cursor";
						apiDescription = apiCodeReceived + ": Cursor position has changed.";
						break;
					case LEDStatus: /*903*/
						apiName = "LED Status";
						apiDescription = apiCodeReceived + ": LED Status has changed.";
						apiMessageType = APIMessageType.KEYPAD_EVENT;
						break;
					case BeepStatus: /*904*/
						apiName = "Beep Status";
						apiDescription = apiCodeReceived + ": Beep status sent.";
						break;
					case ToneStatus: /*905*/
						apiName = "Tone Status";
						apiDescription = apiCodeReceived + ": Tone status sent.";
						break;
					case BuzzerStatus: /*906*/
						apiName = "Buzzer Status";
						apiDescription = apiCodeReceived + ": Buzzer status sent.";
						break;
					case DoorChimeStatus: /*907*/
						apiName = "Door Chime Status";
						apiDescription = apiCodeReceived + ": Door Chime status sent.";
						break;
					case SoftwareVersion: /*908*/
						apiName = "Software Version";
						apiDescription = apiCodeReceived + ": Current software version.";
						break;
					case CommandOutputPressed: /*912*/
						apiName = "Command Output Pressed";
						apiDescription = apiCodeReceived + ": Tells the API to enter an access code.";
						break;
					case MasterCodeRequired: /*921*/
						apiName = "Master Code Required";
						apiDescription = apiCodeReceived + ": Tells the API to enter a master access code.";
						break;
					case InstallersCodeRequired: /*922*/
						apiName = "Installers Code Required";
						apiDescription = apiCodeReceived + ": Tells the API to enter an installers access code.";
						break;
	
					default:
						apiName = "Unknown Code";
						apiDescription = "Unknown code received: " + apiCodeReceived;
						data = "";
						break;
				}
		
				logger.debug("parseAPIMessage(): Message Received ({}) - Code: {}, Name: {}, Description: {}, Data: {}",apiMessage,apiCodeReceived, apiName, apiDescription, data );
			}
			else {
				logger.debug("parseAPIMessage(): Invalid Message Received" );
			}
		}
	}
	
	
	/**
	 * Returns a string representation of a APIMessage
	 * 
	 * @return APIMessage string
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Code: \"");
		sb.append(apiCodeReceived);
		sb.append("\"");

		sb.append(", Name: \"");
		sb.append(apiName);
		sb.append("\"");

		sb.append(", Description: \"");
		sb.append(apiDescription);
		sb.append("\"");

		if (partition != 0) {
			sb.append(", Partition: ");
			sb.append(partition);
		}

		if (zone != 0) {
			sb.append(", Zone: ");
			sb.append(zone);
		}

		if (data != "") {
			sb.append(", Data: ");
			sb.append(data);
		}

		if (mode != "") {
			sb.append(", Mode: ");
			sb.append(mode);
		}

		if (user != "") {
			sb.append(", user: ");
			sb.append(user);
		}

		return sb.toString();
	}

	
	/**
	 * Returns the modified API message received
	 * 
	 * @return apiMessage
	 */
	public String getAPIMessage() {
		return apiMessage;
	}

	/**
	 * Returns the API Code extracted from the API message
	 * 
	 * @return apiCodeReceived
	 */
	public String getAPICode() {
		return apiCodeReceived;
	}

	/**
	 * Returns the name of the API message
	 * 
	 * @return apiName
	 */
	public String getAPIName() {
		return apiName;
	}

	/**
	 * Returns the description of the API message
	 * 
	 * @return apiDescription
	 */
	public String getAPIDescription() {
		return apiDescription;
	}

	/**
	 * Returns the API Message Type
	 * 
	 * @return apiMessage
	 */
	public APIMessageType getAPIMessageType() {
		return apiMessageType;
	}

	/**
	 * Returns the partition information extracted from the API message
	 * 
	 * @return partition
	 */
	public int getPartition() {
		return partition;
	}

	/**
	 * Returns the zone information extracted from the API message
	 * 
	 * @return zone
	 */
	public int getZone() {
		return zone;
	}

	/**
	 * Returns the data extracted from the API message
	 * 
	 * @return apiData
	 */
	public String getAPIData() {
		return data;
	}

	/**
	 * Returns the partition mode information extracted from the API message
	 * 
	 * @return mode
	 */
	public String getMode() {
		return mode;
	}
	
	/**
	 * Returns the user code information extracted from the API message
	 * 
	 * @return user
	 */
	public String getUser() {
		return user;
	}
}
