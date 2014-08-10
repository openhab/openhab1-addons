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

```

The primary setting will be the IP address of the EyezOn Envisalink 3/2DS interface or the serial port name of the DSC IT-100. The *password*, *usercode*, and *baud* settings are optional. If not set, the binding will resort to the system defaults.

## Item Binding

In order to bind to the DSC Alarm system you can add items to an item file using the following format:

```
dscalarm="DSCAlarmDeviceType:<partitionID>:<zoneID>:DSCAlarmItemType"

```

The DSCAlarmDeviceType indicates one of three device types of the DSC Alarm System.  They consist of the following:

<table>
    <tr><td><b>DSC Alarm Device Type</b></td><td><b>Description</b></td></tr>
    <tr><td>panel</td><td>The basic representation of the DSC Alarm System.</td></tr>
    <tr><td>partition</td><td>Represents a controllable area within a DSC Alarm system.</td></tr>
    <tr><td>zone</td><td>Represents a physical device such as a door, window, or motion sensor.</td></tr>
</table>

The parameters *partitionID* and *zoneID* will depend on the DSC Alarm device type.  A DSC Alarm device type of 'partition' requires a *partitionID* in the range 1-8, which will depend on how your DSC Alarm system is configured.  A DSC Alarm device type of 'zone' requires *zoneID* in the range 1-64, as well as the *partitionID*.

The DSCAlarmItemType maps the binding to an openHAB item type.  Here are the supported DSC Alarm Item Types:

<table>
    <tr><td><b>DSC Alarm Item Type</b></td><td><b>openHAB Item Type</b></td><td><b>Description</b></td></tr>
    <tr><td>panel_connection</td><td>Number</td><td>Panel connection status.</td></tr>
    <tr><td>panel_message</td><td>String</td><td>Event messages received from the DSC Alarm system.</td></tr>
    <tr><td>panel_system_error</td><td>String</td><td>DSC Alarm system error.</td></tr>
    <tr><td>panel_time_date</td><td>DateTime</td><td>DSC Alarm system time and date.</td></tr>
    <tr><td>partition_status</td><td>String</td><td>A partitions current status.</td></tr>
    <tr><td>partition_arm_mode</td><td>Number</td><td>A partitions current arm mode.</td></tr>
    <tr><td>zone_general_status</td><td>Contact</td><td>A zones general (open/closed) status.</td></tr>
    <tr><td>zone_alarm_status</td><td>String</td><td>A zones alarm status.</td></tr>
    <tr><td>zone_tamper_status</td><td>String</td><td>A zones tamper status.</td></tr>
    <tr><td>zone_fault_status</td><td>String</td><td>A zones fault status.</td></tr>
    <tr><td>zone_bypass_mode</td><td>Number</td><td>A zones bypass mode.</td></tr>
    
</table>

The following is an example of an item file:

```
Group DSCAlarm
Group DSCAlarmPanel (DSCAlarm)
Group DSCAlarmPartitions (DSCAlarm)
Group DSCAlarmZones (DSCAlarm)

/* DSC Alarm Items */
Number PANEL_CONNECTION "Panel Connected: [%d]" (DSCAlarmPanel) {dscalarm="panel:panel_connection"}
String PANEL_MESSAGE "Panel Message:[%s]" <"shield-1"> (DSCAlarmPanel) {dscalarm="panel:panel_message"}
String PARTITION1_STATUS "Partition 1 Status:[%s]" (DSCAlarmPartitions) {dscalarm="partition:1:partition_status"}
Number PARTITION1_ARM_MODE "Partition Arm Mode:[%d]" (DSCAlarmPartitions) {dscalarm="partition:1:partition_arm_mode"}

/*DSC Alarm Zones */
Contact ZONE1_GENERAL_STATUS "Tamper Switch" (DSCAlarmZones) {dscalarm="zone:1:1:zone_general_status"}
Number ZONE1_BYPASS_MODE "Tamper Switch Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:1:zone_bypass_mode"}
String ZONE1_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:1:zone_alarm_status"}
String ZONE1_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:1:zone_fault_status"}
String ZONE1_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:1:zone_tamper_status"}

Contact ZONE9_GENERAL_STATUS "Front Door Sensor" (DSCAlarmZones, GF_FFoyer) {dscalarm="zone:1:9:zone_general_status"}
Number ZONE9_BYPASS_MODE "Tamper SwitchBypass Mode" (DSCAlarmZones) {dscalarm="zone:1:9:zone_bypass_mode"}
String ZONE9_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:9:zone_alarm_status"}
String ZONE9_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:9:zone_fault_status"}
String ZONE9_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:9:zone_tamper_status"}

Contact ZONE10_GENERAL_STATUS "Deck Door Sensor" (DSCAlarmZones, GF_Family) {dscalarm="zone:1:10:zone_general_status"}
Number ZONE10_BYPASS_MODE "Deck Door Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:10:zone_bypass_mode"}
String ZONE10_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:10:zone_alarm_status"}
String ZONE10_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:10:zone_fault_status"}
String ZONE10_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:10:zone_tamper_status"}

Contact ZONE11_GENERAL_STATUS "Back Door Sensor" (DSCAlarmZones, GF_Utility) {dscalarm="zone:1:11:zone_general_status"}
Number ZONE11_BYPASS_MODE "Back Door Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:11:zone_bypass_mode"}
String ZONE11_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:11:zone_alarm_status"}
String ZONE11_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:11:zone_fault_status"}
String ZONE11_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:11:zone_tamper_status"}

Contact ZONE12_GENERAL_STATUS "Side Door Sensor" (DSCAlarmZones, GF_SFoyer) {dscalarm="zone:1:12:zone_general_status"}
Number ZONE12_BYPASS_MODE "Side Door Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:12:zone_bypass_mode"}
String ZONE12_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:12:zone_alarm_status"}
String ZONE12_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:12:zone_fault_status"}
String ZONE12_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:12:zone_tamper_status"}

Contact ZONE13_GENERAL_STATUS "Garage Door 1 Sensor" (DSCAlarmZones, GF_Garage) {dscalarm="zone:1:13:zone_general_status"}
Number ZONE13_BYPASS_MODE "Garage Door 1 Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:13:zone_bypass_mode"}
String ZONE13_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:13:zone_alarm_status"}
String ZONE13_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:13:zone_fault_status"}
String ZONE13_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:13:zone_tamper_status"}

Contact ZONE14_GENERAL_STATUS "Garage Door 2 Sensor" (DSCAlarmZones, GF_Garage) {dscalarm="zone:1:14:zone_general_status"}
Number ZONE14_BYPASS_MODE "Garage Door 2 Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:14:zone_bypass_mode"}
String ZONE14_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:14:zone_alarm_status"}
String ZONE14_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:14:zone_fault_status"}
String ZONE14_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:14:zone_tamper_status"}

Contact ZONE15_GENERAL_STATUS "Garage Window Sensor" (DSCAlarmZones, GF_Garage) {dscalarm="zone:1:15:zone_general_status"}
Number ZONE15_BYPASS_MODE "Garage Window Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:15:zone_bypass_mode"}
String ZONE15_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:15:zone_alarm_status"}
String ZONE15_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:15:zone_fault_status"}
String ZONE15_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:15:zone_tamper_status"}

Contact ZONE25_GENERAL_STATUS "Utility Room Motion Sensor" (DSCAlarmZones, GF_Utility) {dscalarm="zone:1:25:zone_general_status"}
Number ZONE25_BYPASS_MODE "Utility Room Motion Sensor Bypass Mode" (DSCAlarmZones) {dscalarm="zone:1:25:zone_bypass_mode"}
String ZONE25_ALARM_STATUS "Alarm Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:25:zone_alarm_status"}
String ZONE25_FAULT_STATUS "Fault Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:25:zone_fault_status"}
String ZONE25_TAMPER_STATUS "Tamper Status : [%s]" (DSCAlarmZones) {dscalarm="zone:1:25:zone_tamper_status"}
```
Here is an example sitemap:

```
Frame label="Alarm System" {
	Text label="DSC Alarm System" icon="shield-1" {
		Frame label="Panel" {
			Switch item=PANEL_CONNECTION label="Panel Connection" icon="shield-1" mappings=[1="Connected", 0="Disconnected"]
			Text item=PANEL_MESSAGE icon="shield-1"
		}
		Frame label="Partitions" {
				Text item=PARTITION1_STATUS icon="shield-1" {
					Switch item=PARTITION1_ARM_MODE label="Partition 1 Arm Options" mappings=[0="Disarm", 1="Away", 2="Stay", 3="Zero"]
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

