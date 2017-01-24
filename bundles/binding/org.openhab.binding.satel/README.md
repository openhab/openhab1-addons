## Introduction

This is documentation of openHAB binding for Satel Integra Alarm System which allows you to connect to your alarm system using TCP/IP protocol with ETHM-1 module installed or RS-232 protocol with INT-RS module installed.

For installation of the binding, please see Wiki page [Bindings](Bindings).

> NOTE: INT-RS module is supported since version 1.8 of the binding.

## Binding Configuration

There are some configuration settings that you can set in the openhab.cfg file. Include the following in your openhab.cfg.

```
############################### Satel Binding ###################################
#
# Satel ETHM-1 module hostname or IP.
# Leave this commented out for INT-RS module.  
#satel:host=

# ETHM-1 port to use (optional, defaults to 7094), if host setting is not empty.
# INT-RS port to use, if host setting is empty.
#satel:port=7094

# timeout value for both ETHM-1 and INT-RS (optional, in milliseconds, defaults to 5000)
#satel:timeout=5000

# refresh value (optional, in milliseconds, defaults to 10000)
#satel:refresh=10000

# user code for Integra control (optional, if empty binding works in read-only mode)
#satel:user_code=

# encryption key (optional, if empty communication is not encrypted)
#satel:encryption_key=
```

The only required parameter is _satel:host_ for the ETHM-1 module and _satel:port_ for the INT-RS module. The rest default to the values described in the configuration comments. In order to use ETHM-1 module it is required to enable "integration" protocol for the module in Integra configuration (DLOADX).

<table>
<tr><th>Option</th><th>Description</th></tr>
<tr><td>satel:host</td><td>Valid only for ETHM-1 module. Specifies either IP or host name of the module</td></tr>
<tr><td>satel:port</td><td>For INT-RS it specifies the serial port on the host system to which the module is connected, i.e. "COM1" on Windows, "/dev/ttyS0" or "/dev/ttyUSB0" on Linux<br>For ETHM-1 it specifies the TCP port on which the module listens for new connections. Defaults to 7094.</td></tr>
<tr><td>satel:timeout</td><td>Timeout value for connect, read and write operations specified in milliseconds. Defaults to 5 seconds</td></tr>
<tr><td>satel:refresh</td><td>Refresh interval in milliseconds. Defaults to 10 seconds.</td></tr>
<tr><td>satel:user_code</td><td>Security code (password) of the user used for control operations, like arming, changing state of outputs, etc. It is recommended to use dedicated user for openHAB integration.</td></tr>
<tr><td>satel:encryption_key</td><td>Key use for encrypting communication between openHAB and ETHM-1 module. To disable encrytpion leave it empty. See also the note below.</td></tr></table>

> NOTE: Encryption requires support for 192 bit AES keys. Oracle Java by default supports only 128 bit keys, therefore "[Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/index.html)" must be installed. OpenJDK supports unlimited AES keys by default.

> NOTE: As of version 2.03 ETHM-1 Plus firmware the module disconnects after 25 seconds of inactivity. Setting `satel:refresh` to value greater than 25000 will cause inability to correctly communicate with the module.

## Item Binding

In order to bind to the Integra Alarm system you need to add settings for items defined in your item file. Here is item configuration string syntax:

```
satel="<object_type>[:<state_type>][:<object_number>,...][:<option>=<value>,...]"
```

Name of object type, state type and option is case insensitive. For "output" objects state type cannot be specified and must be ommited. `object_number` must be integer number in range 1-256. Options are comma-separated pairs of name and value separated by `=` character.

Supported item types: `Contact`, `Switch`, `Number`, `Rollershutter`. 
For all but `Rollershutter` item type only one `object_number` is allowed. For `Rollershutter` item there must be exactly two object numbers specified. As there is no information about roller shutter position, state is updated to 0% when "UP" command is received and 100% when "DOWN" command is received, assuming the state will be eventually reached.
Number items can be used only if `object_number` is not given and the number specifies cardinality of objects that are in given state. For example if object is "zone" and state is "violated", item will tell you number of zones violated. See examples section for detailed configuration syntax.

**Valid `object_type` values:**
<table><tr><th>Type</th><th>Description</th></tr>
<tr><td>zone</td><td>defines a zone: PIR, contact, etc.</td></tr>
<tr><td>partition</td><td>defines a partition</td></tr>
<tr><td>output</td><td>defines an output</td></tr>
<tr><td>doors</td><td>defines doors</td></tr>
<tr><td>status</td><td>defines a status item</td></tr>
<tr><td>module</td><td>defines connection status item</td></tr></table>


**Valid `state_type` values for "zone" objects:**
> NOTE: Sending commands to zones is available since v1.9 of the binding

<table><tr><th>Type</th><th>Notes</th></tr>
<tr><td>violation</td><td></td></tr>
<tr><td>tamper</td><td></td></tr>
<tr><td>alarm</td><td></td></tr>
<tr><td>tamper_alarm</td><td></td></tr>
<tr><td>alarm_memory</td><td></td></tr>
<tr><td>tamper_alarm_memory</td><td></td></tr>
<tr><td>bypass</td><td>ON command bypasses the zone, OFF unbypasses.</td></tr>
<tr><td>no_violation_trouble</td><td></td></tr>
<tr><td>long_violation_trouble</td><td></td></tr>
<tr><td>isolate</td><td>ON command isolates the zone.</td></tr>
<tr><td>masked</td><td></td></tr>
<tr><td>masked_memory</td><td></td></tr></table>


**Valid `state_type` values for "partition" objects:**
<table><tr><th>Type</th><th>Notes</th></tr>
<tr><td>armed</td><td>ON command arms specified partition in mode 0, OFF disarms. Forces arming if "force_arm" option is specified.</td></tr>
<tr><td>really_armed</td><td>ON command arms specified partition in mode 0, OFF disarms. Forces arming if "force_arm" option is specified.</td></tr>
<tr><td>armed_mode_1</td><td>ON command arms specified partition in mode 1, OFF disarms. Forces arming if "force_arm" option is specified.</td></tr>
<tr><td>armed_mode_2</td><td>ON command arms specified partition in mode 2, OFF disarms. Forces arming if "force_arm" option is specified.</td></tr>
<tr><td>armed_mode_3</td><td>ON command arms specified partition in mode 3, OFF disarms. Forces arming if "force_arm" option is specified.</td></tr>
<tr><td>first_code_entered</td><td></td></tr>
<tr><td>entry_time</td><td></td></tr>
<tr><td>exit_time_gt_10</td><td></td></tr>
<tr><td>exit_time_lt_10</td><td></td></tr>
<tr><td>temporary_blocked</td><td></td></tr>
<tr><td>blocked_for_guard</td><td></td></tr>
<tr><td>alarm</td><td>OFF command clears alarms for specified partition</td></tr>
<tr><td>alarm_memory</td><td>OFF command clears alarms for specified partition</td></tr>
<tr><td>fire_alarm</td><td>OFF command clears alarms for specified partition</td></tr>
<tr><td>fire_alarm_memory</td><td>OFF command clears alarms for specified partition</td></tr>
<tr><td>violated_zones</td><td></td></tr>
<tr><td>verified_alarms</td><td>OFF command clears alarms for specified partition</td></tr>
<tr><td>warning_alarms</td><td>OFF command clears alarms for specified partition</td></tr></table>


**Valid `state_type` values for "doors" objects:**
> NOTE: Sending commands to doors is available since v1.9 of the binding

<table><tr><th>Type</th><th>Notes</th></tr>
<tr><td>opened</td><td>ON command opens the doors.</td></tr>
<tr><td>opened_long</td><td></td></tr></table>


**Valid `state_type` values for "status" objects:**
<table><tr><th>Type</th><th>Notes</th></tr>
<tr><td>service_mode</td><td></td></tr>
<tr><td>troubles</td><td>OFF command clears troubles memory</td></tr>
<tr><td>troubles_memory</td><td>OFF command clears troubles memory</td></tr>
<tr><td>acu100_present</td><td></td></tr>
<tr><td>intrx_present</td><td></td></tr>
<tr><td>grade23_set</td><td></td></tr>
<tr><td>date_time</td><td>DateTimeType or StringType command changes Integra date and time</td></tr></table>


**Valid `state_type` values for "module" objects:**
<table><tr><th>Type</th><th>Notes</th></tr>
<tr><td>connected</td><td>status of connection to the module</td></tr>
<tr><td>connected_since</td><td>date and time when current connection has been established</td></tr>
<tr><td>connection_errors</td><td>number of consecutive connection errors; clears on successful connection</td></tr></table>


**Valid options:**
<table><tr><th>Name</th><th>Description</th></tr>
<tr><td>force_arm</td><td>forces arming for arming commands</td></tr>
<tr><td>commands_only</td><td>item accepts commands, but state of the item is not updated</td></tr>
<tr><td>invert_state</td><td>uses 0 as active state (zones and outputs only)</td></tr></table>


## Satel Actions

_Available as of openHAB 1.9_

> NOTE: Satel Binding v1.9+ must be installed in order to use Satel Actions

The Satel Action bundle provides actions to read event log of the connected alarm system, check current connection status and override configured user code.

* `boolean satelIsConnected()` - returns `true` if connected to communication module and `false` otherwise
* `Object[] satelReadEvent(int eventIndex)` - reads event log record for given index; records must be read one by one from most recent record under index `-1`; see record description below
* `String satelReadDeviceName(String deviceType, int deviceNumber)` - reads description of a device; device type must be one of PARTITION, ZONE, USER, EXPANDER, LCD, OUTPUT, TIMER, TELEPHONE, OBJECT
* `void satelSetUserCode(String newUserCode)` - overrides configured user code to a given one; allows to execute commands not available for default user configured in the settings
* `void satelResetUserCode()` - reverts user code set by `satelSetUserCode(String newUserCode)` to the value configured in openhab.cfg/satel.cfg.

**Event record fields:**
<table><tr><th>Field</th><th>Type</th><th>Description</th></tr>
<tr><td>timestamp</td><td>DateTimeType</td><td>time when the event happened</td></tr>
<tr><td>partition</td><td>Integer</td><td>partition number</td></tr>
<tr><td>event class</td><td>Enum</td><td>one of ZONE_ALARMS, PARTITION_ALARMS, ARMING, BYPASSES, ACCESS_CONTROL, TROUBLES, USER_FUNCTIONS, SYSTEM_EVENTS</td></tr>
<tr><td>event code</td><td>Integer</td><td>code of the event</td></tr>
<tr><td>restoration flag</td><td>Boolean</td><td></td></tr>
<tr><td>event description</td><td>String</td><td></td></tr>
<tr><td>kind of description</td><td>Integer</td><td></td></tr>
<tr><td>source</td><td>Integer</td><td></td></tr>
<tr><td>object</td><td>Integer</td><td></td></tr>
<tr><td>user control number</td><td>Integer</td><td></td></tr>
<tr><td>next event index</td><td>Integer</td><td>index that must be passed to read next record from the log</td></tr>
<tr><td>current event index</td><td>Integer</td><td>index of the current record</td></tr></table>


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

**User for OH integration**

To control Integra partitions and outputs you need to provide security code of user in behalf all those operations will be executed. It is highly recommended to use a separate user for openHAB integration with only required access rights set in Integra configuration, like access to certain partitions, etc. This allows you to distinguish actions made by OH and a user using Integra panel, also it will block unwanted operations in case someone breaks into your local network.

**Disarming and clearing alarms**

Although this binding allows you to configure disarming a partition and clearing alarms for a partion, this should be used only in cases when security is not the priority. Don't forget both these operations can be executed in openHAB without specifying user code, which is required to disarm or clear alarms using Integra panel. Consider adding a keypad in your sitemap to temporarily change user code to execute sensitive operations. You can find such keypad in [examples](#examples) section.
Also don't forget to secure your openHAB installation by using HTTPS protocol and setting a user with password. Here is a page about security in openHAB: [Security](Security)

## Media
* [Arming and clearing troubles](https://www.youtube.com/watch?v=ogdgn0Dk1G8)

## TO DO

* troubles support (detailed)