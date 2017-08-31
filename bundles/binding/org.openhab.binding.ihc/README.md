# IHC / ELKO Binding

This binding is for the "Intelligent Home Control" building automation system originally made by LK, but now owned by Schneider Electric and sold as "IHC Intelligent Home Control". It is based on a star-configured topology with wires to each device. The system is made up of a central controller and up to 8 input modules and 16 output modules. Each input module can have 16 digital inputs and each output module 8 digital outputs, resulting in a total of 128 input and 128 outputs per controller.

## Binding Configuration

This binding can be configured in the file `services/ihc.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         |   Yes    | Controller IP address |
| username |         |   Yes    | Username for the controller |
| password |         |   Yes    | Password for the controller |
| timeout  |         |   Yes    | Timeout for controller communication in milliseconds (5000 for example) |

The binding will download the project file from the controller. It will also listen to controller state changes, and when the controller state is changed from init to ready state (controller is reprogrammed), the project file will be download again from the controller.

IHC / ELKO LS controller communication interface is SOAP (Simple Object Access Protocol) based, limited to HTTPS transport protocol.

## Item Configuration

IHC / ELKO LS binding use resource ID's to control and listen for notification to/from the controller. You can find the correct resource ID's from your IHC / ELKO LS project file. The binding supports both decimal and hexadecimal values for resource ID's values. Hexadecimal value need to be specified with 0x prefix.

### New configuration syntax

The syntax of the binding configuration to an item can contain the following:

One InOut-Binding (+Out-Bindings): If defined the item receives updates from the IHC and received commands from openhab gets mapped to the IHC accordingly. 

```
ihc="ResourceId[:refreshintervalinseconds]"
```

One In-Binding (+Out-Bindings): If defined, the item receives updates from the IHC.

```
ihc="<ResourceId"
```

A set of Out-Binding: Multiple Out-Bindings can be defined. If the item receives a command specified in this list, the corresponding resourceId and value will be set.

```
ihc=">[Command:ResourceId(:VALUE)]"
```

Command: The Command that should be mapped.

| Value | Meaning |
|-------|---------|
| undefined | Default mapping will be used, see below |
| 0     | OFF |
| 1     | ON |
| \>1   | Switch ON, sleep for Value milliseconds, OFF |

Example:

```
Rollershutter Rollershutter_Demo "Rollershutter Demo" (Rollershutters) {ihc=">[UP:15675921:100],>[DOWN:15676177:100]"}
```

Number, Switch, Contact, String and DateTime items are supported.


| openHAB item type | IHC / ELKO LS data type(s) |
|-------------------|----------------------------|
| Number            | WSFloatingPointValue, WSIntegerValue, WSBooleanValue, WSTimerValue, WSWeekdayValue |
| Switch            | WSBooleanValue |
| Contact           | WSBooleanValue |
| String            | WSEnumValue |
| DateTime          | WSDateValue, WSTimeValue |

Examples how to find resource ID's from project file (from .vis file) and map them to OpenHAB data types.

| openHAB item type | Resource id from project file |
|-------------------|-------------------------------|
| Switch | `<dataline_input id="_0x3f295a" …>` |
| Switch | `<dataline_output id="_0x3ce35b" …>` |
| Switch | `<airlink_input id="_0x5b555c" …>` |
| Dimmer | `<airlink_dimming id="_0x3ec5d" …>` |
| Switch | `<resource_flag id="_0x97e00a" …>` |
| Number | `<resource_temperature id="_0x3f4d14" …>` |
| Number | `<resource_timer id="_0x97de10" …>` |
| Number | `<resource_counter id="_0x97df0c" …>` |
| Number | `<resource_weekday id="_0x97e109" …>` |
| Number | `<resource_light_level id="_0x97dc13" …>` |
| Number | `<resource_integer id="_0x97e20b" …>` |
| DateTime | `<resource_time id="_0x97db0d" …>` |
| DateTime | `<resource_date id="_0x97dd0e" …>` |
| String | `<resource_enum id="_0x98050f" …>` |


## Examples

Weather temperature is download from internet and updated to IHC controller object where resource id is 1234567:

```
Number Weather_Temperature "Outside Temp. (Yahoo) [%.1f °C]" <temperature> (Weather_Chart) { http="<[http://weather.yahooapis.com/forecastrss?w=638242&u=c:60000:XSLT(demo_yahoo_weather.xsl)]", ihc=">1234567" }
```

Binding listens to all state changes from controller's resource id 9953290 and updates state changes to `Light_Kitchen` item. All state changes from OpenHAB will be also transmitted to the controller.

```
Switch Light_Kitchen {ihc="9953290"}
```

Such as previous example, but resource value will additionally asked from controller ones per every minute.

```
Number Temperature_Kitchen "Temperature [%.1f °C]" <temperature> (Temperature, FF_Kitchen) { ihc="0x97E00A:60" }
```
