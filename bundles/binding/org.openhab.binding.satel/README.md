# Satel Integra Alarm System Binding

The Satel Integra Alarm System allows openHAB to connect to your alarm system via TCP/IP network with ETHM-1 module installed, or via RS-232 serial port with INT-RS module installed.

## Binding Configuration

This binding can be configured in the file `services/satel.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| host     |         | if connecting via ETHM-1 module | Satel ETHM-1 module hostname or IP address. Valid only for ETHM-1 module. Leave this as the default if using INT-RS module. In order to use ETHM-1 module, it is required to enable "integration" protocol for the module in Integra configuration (DLOADX). |
| port     |  7094   | if connecting via INT-RS module | For INT-RS, it specifies the serial port on the host system to which the module is connected; e.g. `COM1` on Windows, `/dev/ttyS0` or `/dev/ttyUSB0` on Linux.<br/>For ETHM-1, it specifies the TCP port on which the module listens for new connections. |
| timeout  |  5000   |    No    | timeout value for connect, read and write operations, specified in milliseconds. Defaults of 5000 is 5 seconds. |
| refresh  |  10000  |    No    | refresh value, in milliseconds. As of version 2.03 ETHM-1 Plus firmware, the module disconnects after 25 seconds of inactivity. Setting `refresh` to a value greater than 25000 will cause inability to correctly communicate with the module. |
| user_code |        |    No    | security code (password) of the user used for control operations like arming, changing state of outputs, etc. It is recommended to use a dedicated user for openHAB integration. If empty, the binding will work in read-only mode. |
| encryption_key |   |    No    | key to use for encrypting communication between openHAB and ETHM-1 module. To disable encryption, leave it empty. Encryption requires support for 192 bit AES keys. Oracle Java by default supports only 128 bit keys, therefore ["Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files"](http://www.oracle.com/technetwork/java/javase/downloads/index.html) must be installed. OpenJDK supports unlimited AES keys by default (but OpenJDK is sometimes discouraged for openHAB). |

## Item Configuration

Supported item types: Contact, Switch, Number, and Rollershutter. 

In order to bind to the Satel Integra Alarm system, you need to add settings for items defined in your items file. Here is item configuration string syntax:

```
satel="<object_type>[:<state_type>][:<object_number>,...][:<option>=<value>,...]"
```

where:

* sections in `[square brackets]` is optional
* `<object_type>` is case insensitive
* `<state_type>` is case insensitive
* `<object_number>` is an integer number in range 1-256
* `<option>=<value>` are comma-separared, case insensitive pairs of names and values, separated by the `=` character
* 

For all but the Rollershutter item type, only one `<object_number>` is allowed. For Rollershutter item, there must be exactly two `<object_number>`s specified. As there is no information about roller shutter position, state is updated to 0% when the `UP` command is received, and 100% when the `DOWN` command is received, assuming the state will be eventually reached.

Number items can be used only if `<object_number>` is not given, and the number specifies cardinality of objects that are in a given state. For example, if object is "zone" and state is "violated", item will tell you number of zones violated. See examples section for detailed configuration syntax.

### Valid object_type values

| Type      | Description |
|-----------|-------------|
| zone      | defines a zone: PIR, contact, etc. |
| partition | defines a partition |
| output    | defines an output |
| doors     | defines doors |
| status    | defines a status item |
| module    | defines connection status item |
| trouble   | defines a trouble |
| trouble_memory | defines memory of a trouble |


### Valid state_type values for "zone" objects

| Type | Notes |
|------|-------|
| violation |  |
| tamper |  |
| alarm |  |
| tamper_alarm |  |
| alarm_memory |  |
| tamper_alarm_memory |  |
| bypass | ON command bypasses the zone, OFF unbypasses. |
| no_violation_trouble |  |
| long_violation_trouble |  |
| isolate | ON command isolates the zone. |
| masked |  |
| masked_memory |  |


### Valid state_type values for "partition" objects

| Type  | Notes |
|-------|-------|
| armed | ON command arms specified partition in mode 0, OFF disarms. Forces arming if "force_arm" option is specified. |
| really_armed | ON command arms specified partition in mode 0, OFF disarms. Forces arming if "force_arm" option is specified. |
| armed_mode_1 | ON command arms specified partition in mode 1, OFF disarms. Forces arming if "force_arm" option is specified. |
| armed_mode_2 | ON command arms specified partition in mode 2, OFF disarms. Forces arming if "force_arm" option is specified. |
| armed_mode_3 | ON command arms specified partition in mode 3, OFF disarms. Forces arming if "force_arm" option is specified. |
| first_code_entered |  |
| entry_time |  |
| exit_time_gt_10 |  |
| exit_time_lt_10 |  |
| temporary_blocked |  |
| blocked_for_guard |  |
| alarm | OFF command clears alarms for specified partition |
| alarm_memory | OFF command clears alarms for specified partition |
| fire_alarm | OFF command clears alarms for specified partition |
| fire_alarm_memory | OFF command clears alarms for specified partition |
| violated_zones |  |
| verified_alarms | OFF command clears alarms for specified partition |
| warning_alarms | OFF command clears alarms for specified partition |


### Valid `state_type` values for "doors" objects

| Type        | Notes |
|-------------|-------|
| opened      | ON command opens the door. |
| opened_long |  |


### Valid `state_type` values for "status" objects

| Type | Notes |
|------|-------|
| service_mode |  |
| troubles | OFF command clears troubles memory |
| troubles_memory | OFF command clears troubles memory |
| acu100_present |  |
| intrx_present |  |
| grade23_set |  |
| date_time | DateTimeType or StringType command changes Integra date and time |


### Valid `state_type` values for "module" objects

| Type | Notes |
|------|-------|
| connected | status of connection to the module |
| connected_since | date and time when current connection has been established |
| connection_errors | number of consecutive connection errors; clears on successful connection |


### Valid `state_type` values for "trouble" objects

| Type | Notes |
|------|-------|
| technical_zone | object number range: 1-128 |
| expander_ac | object number range: 1-64 |
| expander_batt | object number range: 1-64 |
| expander_nobatt | object number range: 1-64 |
| system | object number range: 1-24, see table below  |
| ptsa_ac | AC trouble of CA-64 PTSA modules, object number range: 1-8 |
| ptsa_batt | BATT trouble of CA-64 PTSA modules, object number range: 1-8 |
| ptsa_nobatt | NO BATT trouble of CA-64 PTSA modules, object number range: 1-8 |
| ethm1 | object number range: 1-8 |
| proximity_a | object number range: 1-64 |
| proximity_b | object number range: 1-64 |
| expander_overload | object number range: 1-64 |
| jammed_acu100 | object number range: 1-16 |
| device_lobatt | object number range: 1-120 |
| device_nocomm | object number range: 1-120 |
| output_nocomm | object number range: 1-120 |
| expander_nocomm | object number range: 1-64 |
| expander_switcherooed | object number range: 1-64 |
| keypad_nocomm | object number range: 1-8 |
| keypad_switcherooed | object number range: 1-8 |
| ethm1_nolan | object number range: 1-8 |
| expander_tamper | object number range: 1-64 |
| keypad_tamper | object number range: 1-8 |
| keypad_init | object number range: 1-8 |
| auxiliary_stm | object number range: 1-8 |
| master_keyfob | object number range: 1-8 |
| user_keyfob | object number range: 1-240 |
| device_lobatt1 | for Integra 256 Plus - last 120 devices, object number range: 1-120 |
| device_nocomm1 | for Integra 256 Plus - last 120 devices, object number range: 1-120 |
| output_nocomm1 | for Integra 256 Plus - last 120 devices, object number range: 1-120 |
| technical_zone1 | for Integra 256 Plus - zones 129..256, object number range: 1-128 |


### Valid `state_type` values for "trouble_memory" objects

| Type | Notes |
|------|-------|
| technical_zone | object number range: 1-128 |
| expander_ac | object number range: 1-64 |
| expander_batt | object number range: 1-64 |
| expander_nobatt | object number range: 1-64 |
| system | object number range: 1-24, see table below  |
| ptsa_ac | AC trouble of CA-64 PTSA modules, object number range: 1-8 |
| ptsa_batt | BATT trouble of CA-64 PTSA modules, object number range: 1-8 |
| ptsa_nobatt | NO BATT trouble of CA-64 PTSA modules, object number range: 1-8 |
| ethm1 | object number range: 1-8 |
| proximity_a | object number range: 1-64 |
| proximity_b | object number range: 1-64 |
| expander_overload | object number range: 1-64 |
| jammed_acu100 | object number range: 1-16 |
| lcd_restart | object number range: 1-8 |
| expander_restart | object number range: 1-64 |
| device_lobatt | object number range: 1-120 |
| device_nocomm | object number range: 1-120 |
| output_nocomm | object number range: 1-120 |
| expander_nocomm | object number range: 1-64 |
| expander_switcherooed | object number range: 1-64 |
| keypad_nocomm | object number range: 1-8 |
| keypad_switcherooed | object number range: 1-8 |
| ethm1_nolan | object number range: 1-8 |
| expander_tamper | object number range: 1-64 |
| keypad_tamper | object number range: 1-8 |
| keypad_init | object number range: 1-8 |
| auxiliary_stm | object number range: 1-8 |
| long_violation | object number range: 1-128 |
| no_violation | object number range: 1-128 |
| zone_tamper | object number range: 1-128 |
| technical_zone1 | for Integra 256 Plus - zones 129..256, object number range: 1-128 |
| user_keyfob | object number range: 1-240 |
| device_lobatt1 | for Integra 256 Plus - last 120 devices, object number range: 1-120 |
| device_nocomm1 | for Integra 256 Plus - last 120 devices, object number range: 1-120 |
| output_nocomm1 | for Integra 256 Plus - last 120 devices, object number range: 1-120 |
| long_violation1 | for Integra 256 Plus - zones 129..256, object number range: 1-128 |
| no_violation1 | for Integra 256 Plus - zones 129..256, object number range: 1-128 |
| zone_tamper1 | for Integra 256 Plus - zones 129..256, object number range: 1-128 |


### System troubles

| Number | Description |
|------|-------------|
| 1 | OUT1 trouble |
| 2 | OUT2 trouble |
| 3 | OUT3 trouble |
| 4 | OUT4 trouble |
| 5 | +KPD trouble |
| 6 | +EX1 or +EX2 trouble |
| 7 | BATT trouble |
| 8 | AC trouble |
| 9 | DT1 trouble |
| 10 | DT2 trouble |
| 11 | DTM trouble |
| 12 | RTC trouble |
| 13 | no DTR signal |
| 14 | no BATT present |
| 15 | external modem initialization trouble |
| 16 | external model command (ATE0V1Q0H0S0=0) trouble |
| 17 | no voltage on telephone line (INTEGRA 24, 32, 64 and 128) |
| 17 | auxiliary ST processor trouble (INTEGRA 128-WRL) |
| 18 | bad signal on telephone line |
| 19 | no signal on telephone line |
| 20 | monitoring to station 1 trouble |
| 21 | monitoring to station 2 trouble |
| 22 | EEPROM or access to RTC trouble |
| 23 | RAM memory trouble |
| 24 | INTEGRA main panel restart memory |


### Valid options

| Name | Description |
|------|-------------|
| force_arm | forces arming for arming commands |
| commands_only | item accepts commands, but state of the item is not updated |
| invert_state | uses 0 as active state (zones and outputs only) |


### Event record fields

| Field | Type | Description |
|-------|------|-------------|
| timestamp | DateTimeType | time when the event happened |
| partition | Integer | partition number |
| event class | Enum | one of ZONE_ALARMS, PARTITION_ALARMS, ARMING, BYPASSES, ACCESS_CONTROL, TROUBLES, USER_FUNCTIONS, SYSTEM_EVENTS |
| event code | Integer | code of the event |
| restoration flag | Boolean |  |
| event description | String |  |
| kind of description | Integer |  |
| source | Integer |  |
| object | Integer |  |
| user control number | Integer |  |
| next event index | Integer | index that must be passed to read next record from the log |
| current event index | Integer | index of the current record |


## Examples

Partition item with ability to arm and disarm:

```
Switch PartitionArmed "Partition armed" { satel="partition:armed:1" }
```

Sitemap definitions for above example. The second one allows only to arm the partition:

```
Switch item=PartitionArmed
Switch item=PartitionArmed mappings=[ON="Arm"]
```

***

Partition item with ability to force arming:

```
Switch Partition1 "Partition armed" { satel="partition:armed:1:force_arm" }
```

***

Simple contact item:

```
Contact	Zone1 "Zone #1 violated" { satel="zone:violation:1" }
```

***

Zone bypass status with ability to change the state:

```
Switch	Zone1 "Zone #1 bypass" { satel="zone:bypass:1" }
```

***

Number of zones violated:

```
Number	ZonesViolated "Zones violated [%d]" { satel="zone:violation" }
```

***

Simple output item with ability to change its state:

```
Switch	Output1 "Output #1" { satel="output:1" }
```

***

Number of partitions with "alarm" state:

```
Number PartitionsInAlarm "Partitions alarmed [%d]" { satel="partition:alarm" }
```

***

Troubles memory item with clear ability:

```
Switch TroublesMemory "Troubles in the system" { satel="status:troubles_memory" }
```

***

Roller shutter item:

```
Rollershutter KitchenBlinds "Kitchen blinds" { satel="output:10,11" }
```

***
Doors open/closed status with ability to open them:

```
Switch Doors1 "Doors #1" { satel="doors:opened:1" }
```

***

Time synchronization using NTP binding:

```
DateTime AlarmDateTime "Current time [%1$tF %1$tR]" { satel="status:date_time" }
DateTime NtpDateTime   "NTP time [%1$tF %1$tR]"     {ntp="Europe/Berlin:de_DE" }
```

Rule for above example:

```
rule "Alarm time sync"
when
	Item NtpDateTime received update
then
	AlarmDateTime.sendCommand(new StringType(NtpDateTime.state.toString))
end
```

***

Connection status, item definition:

```
Switch AlarmConnection "Connection status" <network> { satel="module:connected" }
DateTime AlarmConnSince "Connected since [%1$tF %1$tR]" { satel="module:connected_since" }
```

***

Rule to send email on each alarm with 10 most recent records from the event log:

```
rule "Satel Action test"
when
        AlarmPart1 changed to ON
then
        var Integer eventIdx = -1
        var String details
        var String msgBody = ""

        if (satelIsConnected()) {
            logInfo("EventLog", "Start")
            (1..10).forEach[
                    val Object[] eventRec = satelReadEvent(eventIdx)
                    val kind = eventRec.get(6) as Integer
                    val source = eventRec.get(7) as Integer
                    val object = eventRec.get(8) as Integer
                    val ucn = eventRec.get(9) as Integer
    
                    if (kind == 0) {
                        details = ""
                    } else if (kind == 1) {
                        details = ", partition: " + satelReadDeviceName("PARTITION", eventRec.get(1)) + ", zone: " + satelReadDeviceName("ZONE", source)
                    } else if (kind == 2) {
                        details = ", partition: " + satelReadDeviceName("PARTITION", eventRec.get(1)) + ", user: " + satelReadDeviceName("USER", source)
                    } else if (kind == 4) {
                        if (source == 0) {
                            details = " (mainboard)"
                        } else if (source <= 128) {
                            details = ", zone: " + satelReadDeviceName("ZONE", source)
                        } else if (source <= 192) {
                            details = ", expander: " + satelReadDeviceName("EXPANDER", source)
                        } else {
                            details = ", lcd: " + satelReadDeviceName("LCD", source)
                        }
                    } else if (kind == 5) {
                        details = ", partition: " + satelReadDeviceName("PARTITION", eventRec.get(1))
                    } else if (kind == 6) {
                        details = ", keypad: " + satelReadDeviceName("LCD", eventRec.get(1)) + ", user: " + satelReadDeviceName("USER", source)
                    } else if (kind == 7) {
                        details = ", user: " + satelReadDeviceName("USER", source)
                    } else if (kind == 15) {
                        details = ", partition: " + satelReadDeviceName("PARTITION", eventRec.get(1)) + ", timer: " + satelReadDeviceName("TIMER", source)
                    } else if (kind == 30) {
                        details = ", keypad: " + satelReadDeviceName("LCD", eventRec.get(1)) + ", ip: " + source + "." + (object*32 + ucn) + details
                    } else if (kind == 31) {
                        details = "." + source + "." + (object*32 + ucn)
                    } else {
                        details = ", kind=" + kind + ", partition=" + eventRec.get(1) + ", source=" + source + ", object=" + object + ", ucn=" + ucn
                    }

                    if (kind != 31) {
                        msgBody = msgBody + "\n" + eventRec.get(0) + ": " + eventRec.get(5) + details
                    }
                    eventIdx = eventRec.get(10)
            ]
            logInfo("EventLog", "End")
            sendMail("you@email.net", "Even log", msgBody)
	}
end
```

Item definition for the above rule:

```
Switch AlarmPart1 "Alarm on partition #1" { satel="partition:alarm_memory:1" }
```

***

Rule that changes user code for 10 minutes. After that time user code is reverted to the one configured in `openhab.cfg`.

```
var String userCode = ""
var Timer keypadTimer = null
var Timer userCodeTimer = null

rule "Keypad char entered"
when
	Item Keypad_Char changed
then
	if (Keypad_Char.state == "-") {
		satelSetUserCode(userCode)
		userCode = ""
		if (userCodeTimer != null) {
			userCodeTime.cancel
		}
		userCodeTimer = createTimer(now.plusMinutes(10)) [|
			logInfo("Keypad", "Reverting user code")
			satelResetUserCode()
		]
	} else if (Keypad_Char.state == "*") {
		satelResetUserCode()
		userCode = ""
	} else {
		userCode = userCode + Keypad_Char.state
	}

	if (keypadTimer != null) {
		keypadTimer.cancel
	}
	keypadTimer = createTimer(now.plusSeconds(5)) [|
		userCode = ""
		Keypad_Char.postUpdate("")
	]
end
```

Item definition for above rule:

```
String Keypad_Char ">"
```

Sitemap keypad to enter user code for above rule:

```
Text label="Enter user code" icon="settings" {
	Switch item=Keypad_Char mappings=[ "1"="1", "2"="2", "3"="3" ]
	Switch item=Keypad_Char mappings=[ "4"="4", "5"="5", "6"="6" ]
	Switch item=Keypad_Char mappings=[ "7"="7", "8"="8", "9"="9" ]
	Switch item=Keypad_Char mappings=[ "*"="*", "0"="0", "-"="#" ]
}
```

## Security considerations

### User for openHAB integration

To control Integra partitions and outputs, you need to provide security code of a user in behalf of all those operations will be executed. It is highly recommended to use a separate user for openHAB integration with only required access rights set in Integra configuration, like access to certain partitions, etc. This allows you to distinguish actions made by openHAB and a user using Integra panel, also it will block unwanted operations in case someone breaks into your local network.

### Disarming and clearing alarms

Although this binding allows you to configure disarming a partition and clearing alarms for a partition, this should be used only in cases when security is not the priority. Don't forget both these operations can be executed in openHAB without specifying a user code, which is required to disarm or clear alarms using Integra panel. Consider adding a keypad in your sitemap to temporarily change user code to execute sensitive operations. You can find such keypad in the [examples](#examples) section.

## Media

* [Arming and clearing troubles](https://www.youtube.com/watch?v=ogdgn0Dk1G8)

## Future

* OH2 version
