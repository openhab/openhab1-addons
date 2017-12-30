# DSC Alarm Actions

This bundle exposes openHAB rule extensions to be used with the DSC Alarm Binding (1.x binding).
It allows the sending of DSC Alarm specific commands from within rules.

## Prerequisites

The DSC Alarm Action bundle relies on the DSC Alarm Binding (1.x) being installed and configured.

## Actions

*   `sendDSCAlarmCommand(String command)` - sends a DSC Alarm command
*   `sendDSCAlarmCommand(String command, String data)` - same as above but with command specific required data

The `command` parameter is a string numeric code.
The following table shows which commands are supported:

| DSC Alarm Command Code | Description                               | Data                                                  | Supported Interface |
|------------------------|-------------------------------------------|-------------------------------------------------------|---------------------|
| `000`                  | Poll                                      | NONE                                                  | Envisalink, IT100   |
| `001`                  | Status Report                             | NONE                                                  | Envisalink, IT100   |
| `002`                  | Labels Request                            | NONE                                                  | IT100               |
| `005`                  | Network Login                             | 1-6 Character - [Password]                            | Envisalink          |
| `008`                  | Dump Zone Timers                          | NONE                                                  | Envisalink          |
| `010`                  | Set Time and Date                         | NONE                                                  | Envisalink, IT100   |
| `020`                  | Command Output Control                    | 2 Characters - [Partition # (1-8)][PGM # (1-4)]       | Envisalink, IT100   |
| `030`                  | Partition Arm Control - Away              | 1 Character - [Partition # (1-8)]                     | Envisalink, IT100   |
| `031`                  | Partition Arm Control - Stay              | 1 Character - [Partition # (1-8)]                     | Envisalink, IT100   |
| `032`                  | Partition Arm Control - No Entry Delay    | 1 Character - [Partition # (1-8)]                     | Envisalink, IT100   |
| `033`                  | Partition Arm Control - With User Code    | 5-7 Characters - [Partition # (1-8)][User Code (4-6)] | Envisalink, IT100   |
| `040`                  | Partition Disarm Control - With User Code | 5-7 Characters - [Partition # (1-8)][User Code (4-6)] | Envisalink, IT100   |
| `055`                  | Time Stamp Control                        | 1 Character - [On/Off (1,0)]                          | Envisalink, IT100   |
| `056`                  | Time/Date Broadcast Control               | 1 Character - [On/Off (1,0)]                          | Envisalink, IT100   |
| `057`                  | Temperature Broadcast Control             | 1 Character - [On/Off (1,0)]                          | Envisalink, IT100   |
| `058`                  | Virtual Keypad Control                    | 1 Character - [On/Off (1,0)]                          | IT100               |
| `060`                  | Trigger Panic Alarm                       | 1 Character - [1=Fire, 2=Ambulance, 3=Police]         | Envisalink, IT100   |
| `070`                  | Single Keystroke                          | 1 Character - [Single ASCII Character]                | Envisalink, IT100   |
| `071`                  | Keystroke String                          | 1-6 Characters - [1-6 ASCII Character String]         | Envisalink          |
| `074`                  | Keep Alive                                | 1 Character - [Partition # (1-8)]                     | Envisalink          |
| `200`                  | Code Send                                 | 4-6 Characters - [User Code (4-6)]                    | Envisalink, IT100   |

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
