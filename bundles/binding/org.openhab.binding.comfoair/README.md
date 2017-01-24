Documentation of the ComfoAir binding Bundle

## Introduction

Binding should be compatible with the Zehnder ComfoAir 350 ventilation system. ComfoAir 550 is untested but should supposedly use the same protocol. The same is true for the device WHR930 of StorkAir, G90-380 by Wernig and Santos 370 DC to Paul.

For installation of the binding, please see Wiki page [[Bindings]].


## Binding Configuration

First of all you need to introduce your ComfoAir in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    ######################## Zehnder ComfoAir ###########################
    
    # IP serial port of the Zehnder ComfoAir to connect to
    comfoair:port=/dev/ttyS0
    
    # refresh inverval in milliseconds (optional, defaults to 60000)
    #comfoair:refresh=

The `comfoair:port` value is the path from the serial device of the ComfoAir. For windows systems it is COMx

The `comfoair:refresh` value is the refresh interval. Refresh value is optional parameter.

Examples, how to configure your ComfoAir device:

    comfoair:port=/dev/ttyS0
    #comfoair:refresh=

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    comfoair="<device-command>"

The **device-command** corresponds ComfoAir command. See complete list below.

Examples, how to configure your items:

    Number airflowControl "Activated" {comfoair="activate"}
    Number airflowFanLevel "Level [%d]" {comfoair="fan_level"}
    Number airflowTargetTemperature "Target temperature [%.1f °C]" {comfoair="target_temperature"}
    
    Number airflowOutdoorIncomingTemperature "Outdoor incoming [%.1f °C]" {comfoair="outdoor_incomming_temperatur"}
    Number airflowOutdoorOutgoingTemperature "Outdoor outgoing [%.1f °C]" {comfoair="outdoor_outgoing_temperatur"}
    Number airflowIndoorIncommingTemperature "Indoor incomming [%.1f °C]" {comfoair="indoor_incomming_temperatur"}
    Number airflowIndoorOutgoingTemperature "Indoor outgoing [%.1f °C]" {comfoair="indoor_outgoing_temperatur"}
    Number airflowIncommingFan		"Incomming fan [%d %%]" {comfoair="incomming_fan"}
    Number airflowOutgoingFan		"Outgoing fan [%d %%]" {comfoair="outgoing_fan"}
    Number airflowFilterRuntime	        "Filter runtime [%d h]" {comfoair="filter_running"}
    Number airflowFilterErrorI	        "Filter (intern) [%s]" {comfoair="filter_error_intern"}
    Number airflowFilterErrorE	        "Filter (extern) [%s]" {comfoair="filter_error_extern"}
    String airflowError	                "Errorcode [%s]" {comfoair="error_message"}

Only **"activate"**, **"fan_level"** and **"target_temperature"** are writeable. All other device commands are readonly.

**"activate"** can handle 0 and 1 states
- 0 means CCEase Comfocontrol is active and the binding is in sleep state
- 1 means Binding is active and CCEase Comfocontrol is in sleep state

**"fan_level"** can handle 1, 2, 3 and 4 states
- 1 is Level A
- 2 is Level 1
- 3 is Level 2
- 4 is Level 3

**"target_temperature"** is a double value between 15.0 and 25.0 in 0.5 steps

## Limitations

- Either the ComfoAir binding or the CCEase Comfocontrol can be active
- You must implement auto mode by yourself with rules. But it is more powerful

## Rights to access the serial port

- Take care that the user that runs openhab has rights to access the serial port
- On Ubuntu systems that usually means adding the user to the group "dialout", i.e. 

         sudo usermod -a -G dialout openhab

  if openhab is your user.

**You can find more examples at**

[https://github.com/openhab/openhab/wiki/Samples-Comfo-Air-Binding]