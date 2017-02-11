# Insteon Hub Binding

DO NOT USE THIS BINDING. The InsteonHub binding is outdated and no longer supported. Use the InsteonPLM binding instead.

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Configuring the binding

If you only have one Insteon Hub, configure the following values in the openhab.cfg file (in the folder '${openhab_home}/configurations'). If you have multiple Insteon Hubs, please see the section below on configuring multiple hubs.

    ################################ Insteon Hub Binding #########################################
    
    insteonhub:host=10.0.0.2


## Items Configuration

All item configurations require a device property to be defined.  This device property is 3-bytes and is defined in hex notation (example: 12.AB.34).  An easy way to find a device's ID is by using the Insteon app.

In addition to the device, a bindingType property needs to be defined.  Valid types are "dimmer", "switch", and "input_ubyte".  The following sections define these types in greater detail.


## dimmer bindingType

The dimmer bindingType supports Dimmer and Rollershutter item types.

Example for Dimmer:

    Dimmer Dim1 "Kitchen Lights" { insteonhub = "device=12.AB.34, bindingType=dimmer" }

Example for Rollershutter:

    Rollershutter RS1 "My Rollershutter" { insteonhub = "device=11.AA.11, bindingType=dimmer" }


## switch bindingType

Example:

    Switch Sw1 "Basement Lights" { insteonhub = "device=22.BB.22, bindingType=switch" }


## input_ubyte bindingType

Insteon reports analog device values as a number from 0 to 255.  input_ubyte reports this value of the associated device as a Number to the openhab bus.  This bindingType is read-only.  For input as a percentage from 0 to 100, use the dimmer bindingType.

Example:

    Number Num1 "Depth Sensor" { insteonhub = "device=33.CC.33, bindingType=input_ubyte" }


## input_percent bindingType

To declare a sensor that is mapped to a read-only percent type, you can use the input_percent binding type.

Example:

    Number Num1 "Some Analog Sensor" { insteonhub = "device=44.DD.44, bindingType=input_percent" }


## input_on_off bindingType

To declare a sensor that is mapped to a read-only On/Off type, the input_on_off binding type can be used.

Example:

    Switch TheSwitch "Some Read-Only Switch" { insteonhub = "device=55.EE.55, bindingType=input_on_off" }


## input_open_closed bindingType

Used to declare a contact sensor.  This is a read-only type that translates a device's state to Open/Closed events.

Example:

    Contact FrontWindow "Front Window" { insteonhub = "device=66.FF.66, bindingType=input_open_closed" }


## Multiple Hubs

If you need to support multiple Insteon Hubs, you can optionally configure them in the openhab.cfg file with unique IDs

    ################################ Insteon Hub Binding #########################################
    
    insteonhub:FIRST_HUB.host=10.0.0.2
    insteonhub:HUB_2.host=10.0.0.3

When configuring for multiple hubs, you will need to specify the hub ID in the item configurations.  Here is an example:

    Dimmer Dim1 "Kitchen Lights" { insteonhub = "device=12.AB.34, bindingType=dimmer, hubid=FIRST_HUB" }