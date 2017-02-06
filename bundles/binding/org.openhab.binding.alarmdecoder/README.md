# Alarm Decoder Binding

The [Alarm Decoder](http://www.alarmdecoder.com) is a hardware adapter that interfaces with Ademco/Honeywell alarm panels. It acts essentially like a keypad, reading and writing messages on a serial bus that connects keypads with the main panel.

There are several versions of the adapter available: 

* ad2pi (a board that plugs into a Raspberry Pi and so offers network-based TCP connectivity)
* ad2serial (serial port access)
* or ad2usb (emulated serial port via USB).

This binding allows openHAB to access the status of contacts and motion detectors connected to Honeywell Vista 20p and similar alarm panels.

## Prerequisites

How to wire the alarm decoder into the panel is described in the alarm decoder quickstart guide.  Before working on the main panel, it is advisable to put the alarm system in test mode, and unplug the phone connection to it for good measure (don't forget to plug it back in when finished).

Understanding exactly what expansion boards are connected to the main panel is crucial for a successful setup of the AlarmDecoder and also helpful in interpreting the messages from the alarmdecoder.

While many of the expansion devices don't have labels on the outside, inserting a flat screwdriver into the right slot and prying gently will usually uncover a circuit board with numbers on it that can be web searched.

Although not mentioned in the quickstart guide, and only documented on an odd
[thread](http://archive.nutech.com/index.php?option=com_fireboard&Itemid=74&func=view&catid=4&id=656), configuring the virtual relay boards is absolutely necessary on panels like the Honeywell Vista 20p and similar, or else all of the eight on-board zones will not be visible! The process sounds intimidating, and it does require bypassing the installer code (see panel documentation about that), but it is not all that hard. 

Once the hardware has been set up properly, a simple, [well documented](http://www.alarmdecoder.com/wiki/index.php/Protocol)
clear text ASCII byte stream is obtained, either on a serial port, or (with the ad2pi appliance) a TCP port that can be telnet'd to.

Here is an example ASCII stream straight from the alarmdecoder:

```
   !SER2SOCK Connected
   !SER2SOCK SERIAL_CONNECTED
   [0000000110000000----],005,[f70000ff1005000028020000000000],"FAULT 05 MUSIC  ROOM WINDOW     "
   !REL:14,02,01
   [0000030110000000----],006,[f70000ff1006030028020000000000],"FAULT 06 OFFICE WINDOW          "
   !REL:14,02,00
   [0000000110000000----],014,[f70000ff1014000028020000000000],"FAULT 14 KITCHENDOOR            "
   !RFX:0717496,80
   !RFX:0717496,00
   !RFX:0610922,80
   !RFX:0610922,00
   [0000000110000000----],014,[f70000ff1014000028020000000000],"FAULT 14 KITCHENDOOR            "
```

Each alarm zone of the panel is represented by a unique combination of message *type* and *address*.
The message *type* depends on how the zone is connected to the panel: via radio frequency (RFX), a zone expander board (EXP), a relay
board (REL), or as a keypad (KPM). For instance: !REL:14,02,01 indicates that relay board 14, channel 02 has gone into state 01. The message *type* is REL, the *address* is 14,02.

Before configuring the binding one must determine which zone generates what message. The easiest way is to observe the ascii stream while 
faulting a given zone by e.g. opening a window or door.

## Binding Configuration

The binding can be configured in the file `services/alarmdecoder.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| connect  |         |   Yes    | You can configure either a TCP connect with a value in the format `tcp:<ad2pihostname.mydomain.com>:<port>` or a serial connection similar to `serial:/dev/ttyUSB0`. Warning: using an Alarm Decoder via serial port has not been debugged yet! |
| send_commands_and_compromise_security | false | No | To send commands from openHAB to the alarm panel, set this to `true`.  Once this option is set to true, the alarm system can potentially be disabled from openHAB. You have been warned! |


## Item Configuration

Once the binding configuration is taken care of, create a suitable file (e.g. alarmdecoder.items) in your `items` folder.

Here is an example file that instantiates some Number and Contact items, and a String item:

```
Number StatusRaw            "panel status: [%d]"         { alarmdecoder="KPM:00#status" }
Number StatusReady          "panel ready: [%d]"          { alarmdecoder="KPM:00#status,bit=17" }
Number StatusAway           "panel away: [%d]"           { alarmdecoder="KPM:00#status,bit=16" }
Number StatusHome           "panel home: [%d]"           { alarmdecoder="KPM:00#status,bit=15" }
Number StatusBacklight      "panel backlight: [%d]"      { alarmdecoder="KPM:00#status,bit=14" }
Number StatusProgramming    "panel programming: [%d]"    { alarmdecoder="KPM:00#status,bit=13" }
    
Number StatusBypass         "panel bypassed: [%d]"       { alarmdecoder="KPM:00#status,bit=9" }
Number StatusPower          "panel on AC: [%d]"          { alarmdecoder="KPM:00#status,bit=8" }
Number StatusChime          "panel chime: [%d]"          { alarmdecoder="KPM:00#status,bit=7" }
Number StatusAlarmOccured   "panel alarm occurred: [%d]" { alarmdecoder="KPM:00#status,bit=6" }
Number StatusAlarm          "panel alarm sounding: [%d]" { alarmdecoder="KPM:00#status,bit=5" }
Number StatusBatteryLow     "panel battery low: [%d]"    { alarmdecoder="KPM:00#status,bit=4" }
Number StatusDelay          "panel delay off: [%d]"      { alarmdecoder="KPM:00#status,bit=3" }
Number StatusFire           "panel fire: [%d]"           { alarmdecoder="KPM:00#status,bit=2" }
Number StatusZoneIssue      "panel zone issue: [%d]"     { alarmdecoder="KPM:00#status,bit=1" }
Number StatusArmedStay      "panel armed stay: [%d]"     { alarmdecoder="KPM:00#status,bit=0" }

Contact ContactReady        "panel ready: [%d]"          { alarmdecoder="KPM:00#contact,bit=17" }
Contact ContactAway         "panel away: [%d]"           { alarmdecoder="KPM:00#contact,bit=16" }
Contact ContactHome         "panel home: [%d]"           { alarmdecoder="KPM:00#contact,bit=15" }
Contact ContactBacklight    "panel backlight: [%d]"      { alarmdecoder="KPM:00#contact,bit=14" }
Contact ContactProgramming  "panel programming: [%d]"    { alarmdecoder="KPM:00#contact,bit=13" }
Contact ContactBypass       "panel bypassed: [%d]"       { alarmdecoder="KPM:00#contact,bit=9" }
Contact ContactPower        "panel on AC: [%d]"          { alarmdecoder="KPM:00#contact,bit=8" }
Contact ContactChime        "panel chime: [%d]"          { alarmdecoder="KPM:00#contact,bit=7" }
Contact ContactAlarmOccured "panel alarm occurred: [%d]" { alarmdecoder="KPM:00#contact,bit=6" }
Contact ContactAlarm        "panel alarm sounding: [%d]" { alarmdecoder="KPM:00#contact,bit=5" }
Contact ContactBatteryLow   "panel battery low: [%d]"    { alarmdecoder="KPM:00#contact,bit=4" }
Contact ContactDelay        "panel delay off: [%d]"      { alarmdecoder="KPM:00#contact,bit=3" }
Contact ContactFire         "panel fire: [%d]"           { alarmdecoder="KPM:00#contact,bit=2" }
Contact ContactZoneIssue    "panel zone issue: [%d]"     { alarmdecoder="KPM:00#contact,bit=1" }
Contact ContactArmedStay    "panel armed stay: [%d]"     { alarmdecoder="KPM:00#contact,bit=0" }

Number PanelBeeps           "panel beeps: [%d]"          { alarmdecoder="KPM:00#beeps" }
Number PanelZone            "panel zone: [%d]"           { alarmdecoder="KPM:00#zone" }
String PanelDisplay         "panel display: [%s]"        { alarmdecoder="KPM:00#text" }
```

Note that the status bits are accessible as either Contacts or Numbers. All but a few of them are quite useless.

Here is how to bind items to RFX, REL, and EXP messages:

```
Contact zone2                  "zone 2 [MAP(contact.map):%s]"   { alarmdecoder="EXP:07,08#contact" }
Contact zone1                  "zone 1 [MAP(contact.map):%s]"   { alarmdecoder="REL:13,01#contact" }
Contact motionContact          "motion sensor contact [MAP(contact.map):%s]" { alarmdecoder="RFX:0923844#contact,bitmask=0x80" }
Contact WindowContact          "window/door magnetic contact (5816) [MAP(contact.map):%s]" { alarmdecoder="RFX:0923844#contact,bitmask=0x20" }
Number  motionData             "motion sensor data [%d]"        { alarmdecoder="RFX:0923844#data" }
Number  motionLowBattery       "motion sensor battery [%d]"     { alarmdecoder="RFX:0923844#data,bit=1" }
Number  motionNeedsSupervision "motion sensor supervision [%d]" { alarmdecoder="RFX:0923844#data,bit=2" }
```

Just like for the KPM messages, the RFX messages are exposed either as a Number or Contact item. Since the REL and EXP messages just give
binary data, they are only mapped to contact items.

Bitmask values for RFX messages are as follows:

```
0x02 - battery alert
0x04 - supervision (tamper)
0x10 - loop3
0x20 - loop2
0x40 - loop4
0x80 - loop1
```

Most wireless devices will transmit their state on loop1 (0x80).  Honeywell 5816 sensors (often used on doors/windows) use loop1 (0x80) for external contacts, and loop2 (0x40) for the magnetic contact; the 5804 remote keyfob uses one zone for each button.

If you don't care much about security and want to operate your alarm keypad from within openHAB, enable `send_commands_and_compromise_security=true` as discussed above, and add these lines to your alarmdecoder.items file:

```
Number Line1 "" { alarmdecoder="SEND#1=1,2=2,3=3", autoupdate="false" }
Number Line2 "" { alarmdecoder="SEND#4=4,5=5,6=6", autoupdate="false" }
Number Line3 "" { alarmdecoder="SEND#7=7,8=8,9=9", autoupdate="false" }
Number Line4 "" { alarmdecoder="SEND#10=*,0=0,11=POUND", autoupdate="false" }

String Display "panel display: [%s]" { alarmdecoder="KPM:00#text" }
```

These items accept `DecimalType` commands from the openHAB bus, and map them to strings which are then SEND to the alarm panel.  In the above example, when a number 10 arrives, a "*" is sent to the alarmdecoder, or any string that is configured. Note that the special character sequence POUND will be further translated to "#".

To show the keypad in a sitemap as are used in various user interfaces, add the following lines need to your sitemap file:

```
Switch item=Line1 label="line1" mappings=[ 1="1____(OFF)",  2="2(AWAY)",  3="3__(STAY)"]
Switch item=Line2 label="line2" mappings=[ 4="____4_____",  5="5(TEST)",  6="6(BYPASS)"]
Switch item=Line3 label="line3" mappings=[ 7="7(INSTANT)",  8="8(CODE)",  9="9_(CHIME)"]
Switch item=Line4 label="line4" mappings=[10="*__(READY)",  0="___0___", 11="____#____"]
Text item=Display
```

## Long Range Radio (LRR) Messages

Starting from Release 1.10.0, [Long Range Radio (LRR) Messages](http://www.alarmdecoder.com/wiki/index.php/LRR_Support) are also supported.  LRR must be enabled in your alarm panel. For Vista series (or other Honeywell/Ademco) panels, this can be configured in field *29.

LRR format from Alarm Decoder is:

```
!LRR:<data>,<partition>,<event>
```

See [Alarm Decoder LRR Protocol](http://www.alarmdecoder.com/wiki/index.php/Protocol#LRR).

Because `<data>` could be anything, it cannot be tied to a particular item as opposed to item bindings such as Contacts mentioned above. Instead, the entire message is sent to the binding.

```
!LRR:<entiremessage>
```

### Example LRR usage

items/alarm_LRR.items (to receive the long-range radio message):

```
String sAlarmLRR "LLR Msg: [%s]" { alarmdecoder="LRR:00#text" }
String sAlarmLRR_lastUser "Last User: [%s]"
String sAlarmLRR_lastEvent "Last Event: [%s]"
DateTime sAlarmLRR_lastEventDateTime ""
String sAlarmLRR_lastPartition "Last Partition: [%s]"
```

rules/alarm_LRR.rules (to parse and route the message):

```
import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap

val Map<String, String> UserList = ImmutableMap.<String, String>builder()
    .put("001", "Installer")
    .put("002", "Master")
    .put("003", "User1")
    .put("004", "User2")
    .build()

rule "LRR Parser"
when
   Item sAlarmLRR received update
then
   var String msg = sAlarmLRR.state.toString
   var String[] parts = msg.split(",")
   if (parts.length != 3) {
      logError("rules", "sAlarmLRR rule parsing error.")
   } else {
      logInfo("rules", "Parsing sAlarmLRR==> {}", msg)
      var String user = UserList.get(parts.get(0)) 
      var String partition = parts.get(1)
      var String event = transform("MAP", "alarm_LRR_eventmap.map", parts.get(2).toString)
      
      sAlarmLRR_lastUser.postUpdate(user)
      sAlarmLRR_lastPartition.postUpdate(partition)
      sAlarmLRR_lastEvent.postUpdate(event)
      sAlarmLRR_lastEventDateTimepostUpdate(new DateTimeType())
   }
end
```

transform/alarm_LRR_eventmap.map:

```
ALARM_EXIT_ERROR=Zone not closed during arming
TROUBLE=Tamper or failure
BYPASS=Zone was bypassed
ACLOSS=AC power was lost
LOWBAT=Low battery indication
TEST_CALL=Testing mode
OPEN=Alarm disarmed
ARM_AWAY=Armed AWAY
ARM_STAY=Armed STAY
RFLOWBAT=RF Low battery
CANCEL=Alarm canceled second disarm
RESTORE=Alarm restored
TROUBLE_RESTORE=Trouble event cleared
BYPASS_RESTORE=Bypassed zone cleared
AC_RESTORE=AC power restored
LOWBAT_RESTORE=Low battery restored
RFLOWBAT_RESTORE=RF Low battery cleared
TEST_RESTORE=Testing Mode cleared
ALARM_PANIC=There was a panic
ALARM_FIRE=There was a fire
ALARM_ENTRY=There was an entry alarm
ALARM_AUX=Auxiliary alarm triggered
ALARM_AUDIBLE=Audible alarm
ALARM_SILENT=Silent alarm
ALARM_PERIMETER=Perimeter alarm
NULL=Unknown
```

## Quirks

1. The alarmdecoder cannot query the panel for the state of individual zones. For this reason, the binding puts contacts into the "unknown"
state, *until the panel goes into the READY state*. At that point, all contacts for which no messages have arrived are presumed to be in the
CLOSED state. In other words: to get to a clean slate after an openHAB restart, close all doors/windows such that the panel is READY.

1. The sitemap provided above may not display properly when viewing from the Classic UI. It has been confirmed to work with GreenT, and the Habdroid and iOS mobile apps.

