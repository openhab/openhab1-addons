## Introduction

This is an OpenHAB binding for a DSC PowerSeries Alarm System utilizing the EyezOn Envisalink 3/2DS interface or the DSC IT-100 RS-232 interface.

The DSC PowerSeries Alarm System is a popular do-it-yourself home security system, which can be monitored and controlled remotely through a standard web-browser or mobile device.

The OpenHAB DSC Alarm binding provides connectivity to the DSC Alarm panel via a TCP socket connection to the EyesOn Envisalink 3/2DS interface or a RS-232 serial connection to the DSC IT-100 interface.

## Binding Configuration

There are some configuration settings that you can set in the openhab.cfg file. Include the following in your openhab.cfg.

```
############################## DSC Alarm Binding #####################################
#
# DSC Alarm port name for a serial connection.
# Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux.
# Leave undefined if not connecting by serial port.
#dscalarm:serialPort=

# DSC Alarm baud rate for serial connections.
# Valid values are 9600 (default), 19200, 38400, 57600, and 115200.
# Leave undefined if using default.
#dscalarm:baud=

# DSC Alarm IP address for a TCP connection. 
# Leave undefined if not connecting by network connection.
#dscalarm:ip=

# DSC Alarm password for logging into the EyezOn Envisalink 3/2DS interface.
# Leave undefined if using default.
#dscalarm:password=

# DSC Alarm user code for logging certain DSC Alarm commands.
# Leave undefined if using default.
#dscalarm:usercode=

# DSC Alarm poll period.
# Amount of time elapsed in minutes between poll commands sent to the DSC Alarm.
# Valid values are 1-15 (Default = 1).
# Leave undefined if using default.
#dscalarm:pollPeriod=
```

The primary setting will be the IP address of the EyezOn Envisalink 3/2DS interface or the serial port name of the DSC IT-100.  The *password*, *usercode*, *baud*, and *pollPeriod* settings are optional.  If not set, the binding will resort to the system defaults.

## Item Binding

In order to bind to the DSC Alarm system you can add items to an item file using the following format:

```
dscalarm="DSCAlarmDeviceType:<partitionID>:<zoneID>:DSCAlarmItemType"

```

The DSCAlarmDeviceType indicates one of four device types of the DSC Alarm System.  They consist of the following:

<table>
    <tr><td><b>DSC Alarm Device Type</b></td><td><b>Description</b></td></tr>
    <tr><td>panel</td><td>The basic representation of the DSC Alarm System.</td></tr>
    <tr><td>partition</td><td>Represents a controllable area within a DSC Alarm system.</td></tr>
    <tr><td>zone</td><td>Represents a physical device such as a door, window, or motion sensor.</td></tr>
    <tr><td>keypad</td><td>Represents the central administrative unit.</td></tr>
</table>

The parameters *partitionID* and *zoneID* will depend on the DSC Alarm device type.  A DSC Alarm device type of 'partition' requires a *partitionID* in the range 1-8, which will depend on how your DSC Alarm system is configured.  A DSC Alarm device type of 'zone' requires *zoneID* in the range 1-64, as well as the *partitionID*.

The DSCAlarmItemType maps the binding to an openHAB item type.  Here are the supported DSC Alarm Item Types:

<table>
    <tr><td><b>DSC Alarm Item Type</b></td><td><b>openHAB Item Type</b></td><td><b>Description</b></td></tr>
    <tr><td>panel_connection</td><td>Number</td><td>Panel connection status.</td></tr>
    <tr><td>panel_message</td><td>String</td><td>Event messages received from the DSC Alarm system.</td></tr>
    <tr><td>panel_system_error</td><td>String</td><td>DSC Alarm system error.</td></tr>
    <tr><td>panel_time</td><td>DateTime</td><td>DSC Alarm system time and date.</td></tr>
    <tr><td>panel_time_stamp</td><td>Switch</td><td>Turn DSC Alarm message time stamping ON/OFF.</td></tr>
    <tr><td>panel_time_broadcast</td><td>Switch</td><td>Turn DSC Alarm time broadcasting ON/OFF.</td></tr>
    <tr><td>panel_fire_key_alarm</td><td>Switch</td><td>A fire key alarm has happened.</td></tr>
    <tr><td>panel_panic_key_alarm</td><td>Switch</td><td>A panic key alarm has happened.</td></tr>
    <tr><td>panel_aux_key_alarm</td><td>Switch</td><td>An auxiliary key alarm has happened.</td></tr>
    <tr><td>panel_aux_input_alarm</td><td>Switch</td><td>An auxiliary input alarm has happened.</td></tr>
    <tr><td>partition_status</td><td>String</td><td>A partitions current status.</td></tr>
    <tr><td>partition_arm_mode</td><td>Number</td><td>A partitions current arm mode. The possible values are:
<br/>
0=disarmed<br/>
1=armed away<br/>
2=armed stay<br/>
3=away no delay<br/>
4=stay no delay<br/>
</td></tr>
    <tr><td>partition_armed</td><td>Switch</td><td>A partition has been armed.</td></tr>
    <tr><td>partition_entry_delay</td><td>Switch</td><td>A partition is in entry delay mode.</td></tr>
    <tr><td>partition_exit_delay</td><td>Switch</td><td>A partition is in exit delay mode.</td></tr>
    <tr><td>partition_in_alarm</td><td>Switch</td><td>A partition is in alarm.</td></tr>
    <tr><td>zone_general_status</td><td>Contact</td><td>A zones general (open/closed) status.</td></tr>
    <tr><td>zone_alarm_status</td><td>String</td><td>A zones alarm status.</td></tr>
    <tr><td>zone_tamper_status</td><td>String</td><td>A zones tamper status.</td></tr>
    <tr><td>zone_fault_status</td><td>String</td><td>A zones fault status.</td></tr>
    <tr><td>zone_bypass_mode</td><td>Number</td><td>A zones bypass mode.</td></tr>
    <tr><td>zone_in_alarm</td><td>Switch</td><td>A zone is in alarm.</td></tr>
    <tr><td>zone_tamper</td><td>Switch</td><td>A zone tamper condition has happened.</td></tr>
    <tr><td>zone_fault</td><td>Switch</td><td>A zone fault condition has happened.</td></tr>
    <tr><td>zone_tripped</td><td>Switch</td><td>A zone has tripped.</td></tr>
    <tr><td>keypad_ready_led</td><td>Number</td><td>Keypad Ready LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/>
</td></tr>
    <tr><td>keypad_armed_led</td><td>Number</td><td>Keypad Armed LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_memory_led</td><td>Number</td><td>Keypad Memory LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_bypass_led</td><td>Number</td><td>Keypad Bypass LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_trouble_led</td><td>Number</td><td>Keypad Trouble LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_program_led</td><td>Number</td><td>Keypad Program LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_fire_led</td><td>Number</td><td>Keypad Fire LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_backlight_led</td><td>Number</td><td>Keypad Backlight LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>
    <tr><td>keypad_ac_led</td><td>Number</td><td>Keypad AC LED Status. The values are:
<br/>
0=OFF<br/>
1=ON<br/>
2=Flashing<br/></td></tr>    
</table>

The following is an example of an item file:

```
Group DSCAlarm
Group DSCAlarmPanel (DSCAlarm)
Group DSCAlarmPartitions (DSCAlarm)
Group DSCAlarmZones (DSCAlarm)
Group DSCAlarmKeypads (DSCAlarm)

/* DSC Alarm Items */

/* DSC Alarm Panel Items */
Number PANEL_CONNECTION "Panel Connected: [%d]" (DSCAlarmPanel) {dscalarm="panel:panel_connection"}
Number PANEL_COMMAND "Panel Commands" (DSCAlarmPanel) {dscalarm="panel:panel_command"}
String PANEL_MESSAGE "Panel Message: [%s]" <"shield-1"> (DSCAlarmPanel) {dscalarm="panel:panel_message"}
String PANEL_SYSTEM_ERROR "Panel System Error: [%s]" <"shield-1"> (DSCAlarmPanel) {dscalarm="panel:panel_system_error"}

DateTime PANEL_TIME "Panel Time [%1$tA, %1$tm/%1$td/%1$tY %1tT]" <calendar> (DSCAlarmPanel) {dscalarm="panel:panel_time"}
Switch PANEL_TIME_STAMP (DSCAlarmPanel) {dscalarm="panel:panel_time_stamp"}
Switch PANEL_TIME_BROADCAST (DSCAlarmPanel) {dscalarm="panel:panel_time_broadcast"}

Switch PANEL_FIRE_KEY_ALARM (DSCAlarmPanel) {dscalarm="panel:panel_fire_key_alarm"}
Switch PANEL_PANIC_KEY_ALARM (DSCAlarmPanel) {dscalarm="panel:panel_panic_key_alarm"}
Switch PANEL_AUX_KEY_ALARM (DSCAlarmPanel) {dscalarm="panel:panel_aux_key_alarm"}
Switch PANEL_AUX_INPUT_ALARM (DSCAlarmPanel) {dscalarm="panel:panel_aux_input_alarm"}

/* DSC Alarm Partition Items */
String PARTITION1_STATUS "Partition 1 Status: [%s]" (DSCAlarmPartitions) {dscalarm="partition:1:partition_status"}
Number PARTITION1_ARM_MODE "Partition Arm Mode: [%d]" (DSCAlarmPartitions) {dscalarm="partition:1:partition_arm_mode"}

Switch PARTITION1_ARMED (DSCAlarmPartitions) {dscalarm="partition:1:partition_armed"}
Switch PARTITION1_ENTRY_DELAY (DSCAlarmPartitions) {dscalarm="partition:1:partition_entry_delay"}
Switch PARTITION1_EXIT_DELAY (DSCAlarmPartitions) {dscalarm="partition:1:partition_exit_delay"}
Switch PARTITION1_IN_ALARM (DSCAlarmPartitions) {dscalarm="partition:1:partition_in_alarm"}

/* DSC Alarm Zones Items */
Contact ZONE1_GENERAL_STATUS "Tamper Switch" (DSCAlarmZones) {dscalarm="zone:1:1:zone_general_status"}
Number ZONE1_BYPASS_MODE "Tamper Switch Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:1:zone_bypass_mode"}
String ZONE1_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:1:zone_alarm_status"}
String ZONE1_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:1:zone_fault_status"}
String ZONE1_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:1:zone_tamper_status"}
Switch ZONE1_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:1:zone_in_alarm"}
Switch ZONE1_TAMPER (DSCAlarmZones) {dscalarm="zone:1:1:zone_tamper"}
Switch ZONE1_FAULT (DSCAlarmZones) {dscalarm="zone:1:1:zone_fault"}
Switch ZONE1_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:1:zone_tripped"}

Contact ZONE9_GENERAL_STATUS "Front Door Sensor" (DSCAlarmZones, FrontFoyer) {dscalarm="zone:1:9:zone_general_status"}
Number ZONE9_BYPASS_MODE "Front Door Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:9:zone_bypass_mode"}
String ZONE9_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:9:zone_alarm_status"}
String ZONE9_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:9:zone_fault_status"}
String ZONE9_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:9:zone_tamper_status"}
Switch ZONE9_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:9:zone_in_alarm"}
Switch ZONE9_TAMPER (DSCAlarmZones) {dscalarm="zone:1:9:zone_tamper"}
Switch ZONE9_FAULT (DSCAlarmZones) {dscalarm="zone:1:9:zone_fault"}
Switch ZONE9_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:9:zone_tripped"}

Contact ZONE10_GENERAL_STATUS "Deck Door Sensor" (DSCAlarmZones, FamilyRoom) {dscalarm="zone:1:10:zone_general_status"}
Number ZONE10_BYPASS_MODE "Deck Door Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:10:zone_bypass_mode"}
String ZONE10_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:10:zone_alarm_status"}
String ZONE10_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:10:zone_fault_status"}
String ZONE10_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:10:zone_tamper_status"}
Switch ZONE10_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:10:zone_in_alarm"}
Switch ZONE10_TAMPER (DSCAlarmZones) {dscalarm="zone:1:10:zone_tamper"}
Switch ZONE10_FAULT (DSCAlarmZones) {dscalarm="zone:1:10:zone_fault"}
Switch ZONE10_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:10:zone_tripped"}

Contact ZONE11_GENERAL_STATUS "Back Door Sensor" (DSCAlarmZones, UtilityRoom) {dscalarm="zone:1:11:zone_general_status"}
Number ZONE11_BYPASS_MODE "Back Door Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:11:zone_bypass_mode"}
String ZONE11_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:11:zone_alarm_status"}
String ZONE11_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:11:zone_fault_status"}
String ZONE11_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:11:zone_tamper_status"}
Switch ZONE11_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:11:zone_in_alarm"}
Switch ZONE11_TAMPER (DSCAlarmZones) {dscalarm="zone:1:11:zone_tamper"}
Switch ZONE11_FAULT (DSCAlarmZones) {dscalarm="zone:1:11:zone_fault"}
Switch ZONE11_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:11:zone_tripped"}

Contact ZONE12_GENERAL_STATUS "Side Door Sensor" (DSCAlarmZones, SideFoyer) {dscalarm="zone:1:12:zone_general_status"}
Number ZONE12_BYPASS_MODE "Side Door Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:12:zone_bypass_mode"}
String ZONE12_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:12:zone_alarm_status"}
String ZONE12_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:12:zone_fault_status"}
String ZONE12_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:12:zone_tamper_status"}
Switch ZONE12_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:12:zone_in_alarm"}
Switch ZONE12_TAMPER (DSCAlarmZones) {dscalarm="zone:1:12:zone_tamper"}
Switch ZONE12_FAULT (DSCAlarmZones) {dscalarm="zone:1:12:zone_fault"}
Switch ZONE12_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:12:zone_tripped"}

Contact ZONE13_GENERAL_STATUS "Garage Door 1 Sensor" (DSCAlarmZones, Garage) {dscalarm="zone:1:13:zone_general_status"}
Number ZONE13_BYPASS_MODE "Garage Door 1 Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:13:zone_bypass_mode"}
String ZONE13_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:13:zone_alarm_status"}
String ZONE13_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:13:zone_fault_status"}
String ZONE13_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:13:zone_tamper_status"}
Switch ZONE13_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:13:zone_in_alarm"}
Switch ZONE13_TAMPER (DSCAlarmZones) {dscalarm="zone:1:13:zone_tamper"}
Switch ZONE13_FAULT (DSCAlarmZones) {dscalarm="zone:1:13:zone_fault"}
Switch ZONE13_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:13:zone_tripped"}

Contact ZONE14_GENERAL_STATUS "Garage Door 2 Sensor" (DSCAlarmZones, Garage) {dscalarm="zone:1:14:zone_general_status"}
Number ZONE14_BYPASS_MODE "Garage Door 2 Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:14:zone_bypass_mode"}
String ZONE14_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:14:zone_alarm_status"}
String ZONE14_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:14:zone_fault_status"}
String ZONE14_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:14:zone_tamper_status"}
Switch ZONE14_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:14:zone_in_alarm"}
Switch ZONE14_TAMPER (DSCAlarmZones) {dscalarm="zone:1:14:zone_tamper"}
Switch ZONE14_FAULT (DSCAlarmZones) {dscalarm="zone:1:14:zone_fault"}
Switch ZONE14_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:14:zone_tripped"}

Contact ZONE15_GENERAL_STATUS "Garage Window Sensor" (DSCAlarmZones, Garage) {dscalarm="zone:1:15:zone_general_status"}
Number ZONE15_BYPASS_MODE "Garage Window Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:15:zone_bypass_mode"}
String ZONE15_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:15:zone_alarm_status"}
String ZONE15_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:15:zone_fault_status"}
String ZONE15_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:15:zone_tamper_status"}
Switch ZONE15_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:15:zone_in_alarm"}
Switch ZONE15_TAMPER (DSCAlarmZones) {dscalarm="zone:1:15:zone_tamper"}
Switch ZONE15_FAULT (DSCAlarmZones) {dscalarm="zone:1:15:zone_fault"}
Switch ZONE15_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:15:zone_tripped"}

Contact ZONE25_GENERAL_STATUS "Utility Room Motion Sensor" (DSCAlarmZones,  UtilityRoom) {dscalarm="zone:1:25:zone_general_status"}
Number ZONE25_BYPASS_MODE "Utility Room Motion Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:25:zone_bypass_mode"}
String ZONE25_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:25:zone_alarm_status"}
String ZONE25_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:25:zone_fault_status"}
String ZONE25_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:25:zone_tamper_status"}
Switch ZONE25_IN_ALARM (DSCAlarmZones) {dscalarm="zone:1:25:zone_in_alarm"}
Switch ZONE25_TAMPER (DSCAlarmZones) {dscalarm="zone:1:25:zone_tamper"}
Switch ZONE25_FAULT (DSCAlarmZones) {dscalarm="zone:1:25:zone_fault"}
Switch ZONE25_TRIPPED (DSCAlarmZones) {dscalarm="zone:1:25:zone_tripped"}

/* DSC Alarm Keypad Items */
Number KEYPAD_READY_LED "Ready LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_ready_led"}
Number KEYPAD_ARMED_LED "Armed LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_armed_led"}
Number KEYPAD_MEMORY_LED "Memory LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_memory_led"}
Number KEYPAD_BYPASS_LED "Bypass LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_bypass_led"}
Number KEYPAD_TROUBLE_LED "Trouble LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_trouble_led"}
Number KEYPAD_PROGRAM_LED "Program LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_program_led"}
Number KEYPAD_FIRE_LED "Fire LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_fire_led"}
Number KEYPAD_BACKLIGHT_LED "Backlight LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_backlight_led"}
Number KEYPAD_AC_LED "AC LED Status: [%d]" (DSCAlarmKeypads) {dscalarm="keypad:keypad_ac_led"}
```

Here is an example sitemap:

```
Frame label="Alarm System" {
	Text label="DSC Alarm System" icon="shield-1" {
		Frame label="Panel" {
			Switch item=PANEL_CONNECTION label="Panel Connection" icon="shield-1" mappings=[1="Connected", 0="Disconnected"]
			Text item=PANEL_MESSAGE icon="shield-1"
			Selection item=PANEL_COMMAND icon="shield-1" mappings=[0="Poll", 1="Status Report", 2="Labels Request (Serial Only)", 8="Dump Zone Timers (TCP Only)", 10="Set Time/Date", 200="Send User Code"]
			Text item=PANEL_TIME {
				Switch item=PANEL_TIME_STAMP label="Panel Time Stamp"
				Switch item=PANEL_TIME_BROADCAST label="Panel Time Broadcast"
			}
		}

		Frame label="Partitions" {
				Text item=PARTITION1_STATUS icon="shield-1" {
					Switch item=PARTITION1_ARM_MODE label="Partition 1 Arm Options" mappings=[0="Disarm", 1="Away", 2="Stay", 3="Zero", 4="W/Code"]
				}
		}

		Frame label="Keypad" {
			Text label="Keypad LED Status" icon="shield-1" {
				Switch item=KEYPAD_READY_LED label="Ready LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_ARMED_LED label="Armed LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_MEMORY_LED label="Memory LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_BYPASS_LED label="Bypass LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_TROUBLE_LED label="Trouble LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_PROGRAM_LED label="Program LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_FIRE_LED label="Fire LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_BACKLIGHT_LED label="Backlight LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
				Switch item=KEYPAD_AC_LED label="AC LED Status:" mappings=[0="Off", 1="On", 2="Flashing"]
			}
		}

		Frame label="Zones" {
			Text item=ZONE1_GENERAL_STATUS {
				Switch item=ZONE1_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE1_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE1_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE1_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}

			Text item=ZONE9_GENERAL_STATUS {
				Switch item=ZONE9_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE9_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE9_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE9_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE10_GENERAL_STATUS {
				Switch item=ZONE10_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE10_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE10_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE10_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE11_GENERAL_STATUS {
				Switch item=ZONE11_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE11_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE11_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE11_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE12_GENERAL_STATUS {
				Switch item=ZONE12_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE12_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE12_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE12_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE13_GENERAL_STATUS {
				Switch item=ZONE13_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE13_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE13_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE13_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE14_GENERAL_STATUS {
				Switch item=ZONE14_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE14_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE14_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE14_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE15_GENERAL_STATUS {
				Switch item=ZONE15_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE15_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE15_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE15_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
			Text item=ZONE25_GENERAL_STATUS {
				Switch item=ZONE25_BYPASS_MODE icon="MyImages/Zone-Alarm" mappings=[0="Armed", 1="Bypassed"]
				Frame label="Other Status:" {
					Text item=ZONE25_ALARM_STATUS icon="MyImages/Status-warning"
					Text item=ZONE25_FAULT_STATUS icon="MyImages/Status-warning"
					Text item=ZONE25_TAMPER_STATUS icon="MyImages/Status-warning"
				}
			}
		}
	}
}
```