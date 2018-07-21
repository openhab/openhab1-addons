# DSC PowerSeries Alarm System Binding

This binding mintors and controls the DSC PowerSeries Alarm System, utilizing the EyezOn Envisalink 3/2DS interface or the DSC IT-100 RS-232 interface.

> Note: if you are using the DSC Alarm Binding 2.0 for OpenHab2 please follow the documentation here: [https://github.com/openhab/openhab2-addons/tree/master/addons/binding/org.openhab.binding.dscalarm](https://github.com/openhab/openhab2-addons/tree/master/addons/binding/org.openhab.binding.dscalarm)

The DSC PowerSeries Alarm System is a popular do-it-yourself home security system, which can be monitored and controlled remotely through a standard web-browser or mobile device.

The openHAB DSC Alarm binding provides connectivity to the DSC Alarm panel via a TCP socket connection to the EyesOn Envisalink 3/2DS interface or a RS-232 serial connection to the DSC IT-100 interface.

Additionally there is a DSC Alarm action bundle that can be installed along with the DSC Alarm binding.  The action provides the ability to send DSC Alarm commands directly to the DSC Alarm system using rules.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/dscalarm/).

## Binding Configuration

This binding can be configured in the file `services/dscalarm.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| deviceType | `it100` (default for serial connection) or `envisalink` (default for tcp connection) | No | DSC Alarm interface device type |
| serialPort | | if connecting via serial port | Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux. Leave undefined if not connecting by serial port. |
| baud     |         |   No     | DSC Alarm baud rate for serial connections. Valid values are 9600 (default), 19200, 38400, 57600, and 115200. Leave undefined if using default. |
| ip       |         | if connecting via network | DSC Alarm IP address for a TCP connection. Leave undefined if not connecting by network connection. |
| tcpPort  | 4025    |   No     | DSC Alarm TCP port for a TCP connection to either an EyezOn Envisalink on 4025 (default) or a TCP serial server to IT-100. Leave undefined if not connecting by network connection. |
| password |         |          | DSC Alarm password for logging into the EyezOn Envisalink 3/2DS interface. |
| usercode |         |          | DSC Alarm user code for logging certain DSC Alarm commands. |
| pollPeriod | 1     |   No     | DSC Alarm poll period. Amount of time elapsed in minutes between poll commands sent to the DSC Alarm. Valid values are 1-15. |
| suppressAcknowledgementMsgs | false | No | Suppress Acknowledgement Messages. Set to `true` to suppress the display of Acknowledgement messages, such as the Command Acknowledge message after a poll command is sent. |

The primary setting will be the IP address of the EyezOn Envisalink 3/2DS interface or the serial port name of the DSC IT-100.  The *password*, *usercode*, *baud*, *pollPeriod*, and *suppressAcknowledgementMsgs* settings are optional.  The *deviceType* and *tcpPort* settings are used to connect to an IT-100 interface through a TCP/IP serial server.  If these settings are not set, the binding will resort to the system defaults.

## Item Configuration

In order to bind to the DSC Alarm system you can add items to an item file using the following format:

```
dscalarm="DSCAlarmDeviceType:<partitionID>:<zoneID>:DSCAlarmItemType"

```

The DSCAlarmDeviceType indicates one of four device types of the DSC Alarm System.  They consist of the following:

| DSC Alarm Device Type | Description |
|-----------------------|-------------|
| panel                 | The basic representation of the DSC Alarm System.|
| partition             | Represents a controllable area within a DSC Alarm system. |
| zone                  | Represents a physical device such as a door, window, or motion sensor.|
| keypad                | Represents the central administrative unit. |


The parameters *partitionID* and *zoneID* will depend on the DSC Alarm device type.  A DSC Alarm device type of 'partition' requires a *partitionID* in the range 1-8, which will depend on how your DSC Alarm system is configured.  A DSC Alarm device type of 'zone' requires *zoneID* in the range 1-64, as well as the *partitionID*.

The DSCAlarmItemType maps the binding to an openHAB item type.  Here are the supported DSC Alarm Item Types:

| DSC Alarm Item Type | openHAB Item Type| Description |
|---------------------|------------------|-------------|
| panel_connection | Number | Panel connection status. |
| panel_message | String | Event messages received from the DSC Alarm system. |
| panel_system_error | String | DSC Alarm system error. |
| panel_time | DateTime | DSC Alarm system time and date. |
| panel_time_stamp | Switch | Turn DSC Alarm message time stamping ON/OFF. |
| panel_time_broadcast | Switch | Turn DSC Alarm time broadcasting ON/OFF. |
| panel_fire_key_alarm | Switch | A fire key alarm has happened. |
| panel_panic_key_alarm | Switch | A panic key alarm has happened. |
| panel_aux_key_alarm | Switch | An auxiliary key alarm has happened. |
| panel_aux_input_alarm | Switch | An auxiliary input alarm has happened. |
| panel_trouble_led | Switch | The panel trouble LED is on. |
| panel_service_required | Switch | Service is required on the panel. |
| panel_ac_trouble | Switch | The panel has lost AC power. |
| panel_telephone_trouble | Switch | Telephone line fault. |
| panel_ftc_trouble | Switch | Failure to communicate with monitoring station. |
| panel_zone_fault | Switch | There is a fault condition on a zone/sensor. |
| panel_zone_tamper | Switch | There is a tamper condition on a zone/sensor. |
| panel_time_low_battery | Switch | There is a low battery condition on a zone/sensor. |
| panel_time_loss | Switch | Loss of time on the panel. |
| panel_trouble_message | String | Displays any trouble messages the panel might send. |
| partition_status | String | A partitions current status. |
| partition_arm_mode | Number | A partitions current arm mode. The possible values are:<br/>0=disarmed<br/>1=armed away<br/>2=armed stay<br/>3=away no delay<br/>4=stay no delay |
| partition_armed | Switch | A partition has been armed. |
| partition_entry_delay | Switch | A partition is in entry delay mode. |
| partition_exit_delay | Switch | A partition is in exit delay mode. |
| partition_in_alarm | Switch | A partition is in alarm. |
| partition_opening_closing_mode | String | Displays the opening/closing mode of a partition. |
| zone_general_status | Contact | A zones general (open/closed) status. |
| zone_alarm_status | String | A zones alarm status. |
| zone_tamper_status | String | A zones tamper status. |
| zone_fault_status | String | A zones fault status. |
| zone_bypass_mode | Number | A zones bypass mode. |
| zone_in_alarm | Switch | A zone is in alarm. |
| zone_tamper | Switch | A zone tamper condition has happened. |
| zone_fault | Switch | A zone fault condition has happened. |
| zone_tripped | Switch | A zone has tripped. |
| keypad_ready_led | Number | Keypad Ready LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_armed_led | Number | Keypad Armed LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_memory_led | Number | Keypad Memory LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_bypass_led | Number | Keypad Bypass LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_trouble_led | Number | Keypad Trouble LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_program_led | Number | Keypad Program LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_fire_led | Number | Keypad Fire LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_backlight_led | Number | Keypad Backlight LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |
| keypad_ac_led | Number | Keypad AC LED Status. The values are:<br/>0=OFF<br/>1=ON<br/>2=Flashing |

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

Switch PANEL_TROUBLE_LED "Panel Trouble LED" <warning> (DSCAlarmPanel) {dscalarm="panel:panel_trouble_led"}
Switch PANEL_SERVICE_REQUIRED <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_service_required"}
Switch PANEL_AC_TROUBLE <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_ac_trouble"}
Switch PANEL_TELEPHONE_TROUBLE <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_telephone_trouble"}
Switch PANEL_FTC_TROUBLE <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_ftc_trouble"}
Switch PANEL_ZONE_FAULT <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_zone_fault"}
Switch PANEL_ZONE_TAMPER <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_zone_tamper"}
Switch PANEL_ZONE_LOW_BATTERY <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_zone_low_battery"}
Switch PANEL_TIME_LOSS <yellowLED> (DSCAlarmPanel) {dscalarm="panel:panel_time_loss"}
String PANEL_TROUBLE_MESSAGE "Panel Trouble Message: [%s]" <"shield-1"> (DSCAlarmPanel) {dscalarm="panel:panel_trouble_message"}

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
String PARTITION1_OPENING_CLOSING_MODE "Partition 1 Opening/Closing Mode: [%s]" (DSCAlarmPartitions) {dscalarm="partition:1:partition_opening_closing_mode"}

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

			Text item=PANEL_SYSTEM_ERROR icon="MyImages/system-error"

			Text item=PANEL_TROUBLE_LED label="Panel Trouble Condition" {
				Text item=PANEL_TROUBLE_MESSAGE icon="shield-0"
				Text item=PANEL_SERVICE_REQUIRED label="Service Required"
				Text item=PANEL_AC_TROUBLE label="AC Trouble"
				Text item=PANEL_TELEPHONE_TROUBLE label="Telephone Line Trouble"
				Text item=PANEL_FTC_TROUBLE label="Failed to Communicate Trouble"
				Text item=PANEL_ZONE_FAULT label="Zone Fault"
				Text item=PANEL_ZONE_TAMPER label="Zone Tamper"
				Text item=PANEL_ZONE_LOW_BATTERY label="Zone Low Battery"
				Text item=PANEL_TIME_LOSS label="Panel Time Loss"					
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

## Change Log

### OpenHAB 1.6.0

* Initial commit of the DSC Alarm Binding. ([#1334](https://github.com/openhab/openhab/pull/1334))

### OpenHAB 1.7.0

* Added several on/off switch items to help with rule creation.  Added a binding configuration option to adjust the polling period performed by the binding. ([#1763](https://github.com/openhab/openhab/pull/1763))
* Features added include: renamed item 'panel_time_date' to 'panel_time'; added item 'panel_time_stamp' to allow the receiving of time stamped messages from the DSC Alarm system; added item 'panel_time_broadcast' to allow the reception of DSC Alarm system time broadcasts for display.  Fixes include: item 'panel_time_date' (renamed to 'panel_time') was not working at all; added several methods to binding class to eliminate extra 'for' loop in message receive thread; included user code fix for the IT-100 serial interface from [#2203](https://github.com/openhab/openhab/pull/2203). ([#2320](https://github.com/openhab/openhab/pull/2320))


### OpenHAB 1.8.0

* Added several new item types to allow the display of trouble conditions on the DSC Alarm.  A new item added to show which opening/closing method was used.  Added a configuration option to allow the suppression of acknowledgement messages from the DSC Alarm system. ([#2893](https://github.com/openhab/openhab/pull/2893))
* Added user code to user opening/closing messages. ([#2964](https://github.com/openhab/openhab/pull/2964))
* Added a DSC Alarm Action bundle that allows users to send DSC Alarm Commands directly to the alarm system from a rule. ([#3266](https://github.com/openhab/openhab/pull/3266))

### OpenHAB 1.8.1

* Added support for the DSC Alarm binding to communicate with an IT-100 through a TCP/IP serial server. Also, fixed a bug where the IT-100 serial interface requires a 6 digit usercode but was only receiving 4 digits.
([#3774](https://github.com/openhab/openhab/pull/3774))

