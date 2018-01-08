## Satel Actions

The Satel Action bundle provides actions to read the event log of the connected alarm system, check current connection status and override configured user code.

## Prerequisites

Satel Binding v1.x must be installed and configured in order to use Satel actions.

## Actions

*   `boolean satelIsConnected()` - returns `true` if connected to communication module and `false` otherwise
*   `Object[] satelReadEvent(int eventIndex)` - reads event log record for given index; records must be read one by one from most recent record under index `-1`; see record description below
*   `String satelReadDeviceName(String deviceType, int deviceNumber)` - reads description of a device; device type must be one of PARTITION, ZONE, USER, EXPANDER, LCD, OUTPUT, TIMER, TELEPHONE, OBJECT
*   `void satelSetUserCode(String newUserCode)` - overrides configured user code to a given one; allows to execute commands not available for default user configured in the settings
*   `void satelResetUserCode()` - reverts user code set by `satelSetUserCode(String newUserCode)` to the value configured in openhab.cfg/satel.cfg.

### Event record fields

| Field               | Type         | Description                                                                                                     |
|---------------------|--------------|-----------------------------------------------------------------------------------------------------------------|
| timestamp           | DateTimeType | time when the event happened                                                                                    |
| partition           | Integer      | partition number                                                                                                |
| event class         | Enum         | one of ZONE_ALARMS, PARTITION_ALARMS, ARMING, BYPASSES, ACCESS_CONTROL, TROUBLES, USER_FUNCTIONS, SYSTEM_EVENTS |
| event code          | Integer      | code of the event                                                                                               |
| restoration flag    | Boolean      |                                                                                                                 |
| event description   | String       |                                                                                                                 |
| kind of description | Integer      |                                                                                                                 |
| source              | Integer      |                                                                                                                 |
| object              | Integer      |                                                                                                                 |
| user control number | Integer      |                                                                                                                 |
| next event index    | Integer      | index that must be passed to read next record from the log                                                      |
| current event index | Integer      | index of the current record                                                                                     |

## Examples

### Rule to send email on each alarm with 10 most recent records from the event log

satel.rules

```java
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

satel.items:

```java
Switch AlarmPart1 "Alarm on partition #1" { satel="partition:alarm_memory:1" }
```

### Rule that changes user code for 10 minutes

After that time user code is reverted to the one configured in the Satel binding (1.x) configuration

satel.rules

```java
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

satel.items

```java
String Keypad_Char ">"
```

satel.sitemap

```
Text label="Enter user code" icon="settings" {
  Switch item=Keypad_Char mappings=[ "1"="1", "2"="2", "3"="3" ]
  Switch item=Keypad_Char mappings=[ "4"="4", "5"="5", "6"="6" ]
  Switch item=Keypad_Char mappings=[ "7"="7", "8"="8", "9"="9" ]
  Switch item=Keypad_Char mappings=[ "*"="*", "0"="0", "-"="#" ]
}
```
