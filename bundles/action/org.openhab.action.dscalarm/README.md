# Documentation for the DSC Alarm Action Bundle

## Introduction

This bundle exposes openHAB Rule extensions to be used with the [DSC Alarm Binding](https://github.com/openhab/openhab/wiki/DSC-Alarm-Binding).

It allows the sending of DSC Alarm specific commands from within [openHAB Rules](https://github.com/openhab/openhab/wiki/Rules):

## Releases

* 1.8.0 - First release

## Configuration

The DSC Alarm Action bundle relies on the DSC Alarm Binding being installed and configured, along with the installation of the DSC Alarm Action Bundle (JAR) file.

## Extensions

Currently the DSC Alarm Action Bundle supports a single rule extension with the following formats:

* `sendDSCAlarmCommand(String command)` - sends a DSC Alarm command.
* `sendDSCAlarmCommand(String command, String data)` - same as above but with command specific required data.

The 'command' parameter is a string numeric code.  The following table shows which commands are supported:

<table>
	<tr><td><b>DSC Alarm Command Code</b></td><td><b>Description</b></td><td><b>Data</b></td><td><b>Supported Interface</b></td></tr>
	<tr><td>'000'</td><td>Poll</td><td>NONE</td><td>Envisalink, IT100</td></tr>
	<tr><td>'001'</td><td>Status Report</td><td>NONE</td><td>Envisalink, IT100</td></tr>
	<tr><td>'002'</td><td>Labels Request</td<td>NONE</td><td>IT100</td></tr>
	<tr><td>'005'</td><td>Network Login</td><td>1-6 Character - [Password]</td><td>Envisalink</td></tr>
	<tr><td>'008'</td><td>Dump Zone Timers</td><td>NONE</td><td>Envisalink</td></tr>
	<tr><td>'010'</td><td>Set Time and Date</td><td>NONE</td><td>Envisalink, IT100</td></tr>
	<tr><td>'020'</td><td>Command Output Control</td><td>2 Characters - [Partition # (1-8)][PGM # (1-4)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'030'</td><td>Partition Arm Control - Away</td><td>1 Character - [Partition # (1-8)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'031'</td><td>Partition Arm Control - Stay</td><td>1 Character - [Partition # (1-8)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'032'</td><td>Partition Arm Control - No Entry Delay</td><td>1 Character - [Partition # (1-8)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'033'</td><td>Partition Arm Control - With User Code</td><td>5-7 Characters - [Partition # (1-8)][User Code (4-6)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'040'</td><td>Partition Disarm Control - With User Code</td><td>5-7 Characters - [Partition # (1-8)][User Code (4-6)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'055'</td><td>Time Stamp Control</td><td>1 Character - [On/Off (1,0)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'056'</td><td>Time/Date Broadcast Control</td><td>1 Character - [On/Off (1,0)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'057'</td><td>Temperature Broadcast Control</td><td>1 Character - [On/Off (1,0)]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'058'</td><td>Virtual Keypad Control</td><td>1 Character - [On/Off (1,0)]</td><td>IT100</td></tr>
	<tr><td>'060'</td><td>Trigger Panic Alarm</td><td>1 Character - [1=Fire, 2=Ambulance, 3=Police]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'070'</td><td>Single Keystroke</td><td>1 Character - [Single ASCII Character]</td><td>Envisalink, IT100</td></tr>
	<tr><td>'071'</td><td>Keystroke String</td><td>1-6 Characters - [1-6 ASCII Character String]</td><td>Envisalink</td></tr>
	<tr><td>'074'</td><td>Keep Alive</td><td>1 Character - [Partition # (1-8)]</td><td>Envisalink</td></tr>
	<tr><td>'200'</td><td>Code Send</td><td>4-6 Characters - [User Code (4-6)]</td><td>Envisalink, IT100</td></tr>
</table>

## Examples

To invoke the action see the examples below:

Add an item to your items file such as a switch:

```
Switch PollCommand "Send a poll command to the DSC Alarm System"
```

The following rule will trigger whenever the switch is turned ON:

```
    rule "PollCommand"
        when 
            Item PollCommand received command ON
        then
            sendDSCAlarmCommand("000")
    end
```

Again add an item to your items file:

```
Switch BypassZone1 "Bypass Zone 1"
```

This rule triggers when the switch is turned ON:

```
    rule "BypassZone1"
        when 
            Item BypassZone1 received command ON
        then
            sendDSCAlarmCommand("071","*101#")
    end
```
