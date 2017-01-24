Documentation of the EnOcean binding bundle <br/>
[![EnOcean Binding](http://img.youtube.com/vi/GpERJflmJKQ/0.jpg)](http://www.youtube.com/watch?v=GpERJflmJKQ) 

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## EnOcean Binding Configuration
### openhab.cfg

The following config params are used for the EnOcean binding.

- enocean:serialPort

The serial port (can be a virtual one) where the enocean transceiver is connected to. An USB adapter creates a virtual serial port (normally /dev/ttyUSB0 under linux)

#### Example

    ######################## EnOcean Binding ###########################
    enocean:serialPort=/dev/ttyUSB0

## Generic Item Binding Configuration

### Items

General format:

    enocean="{id=<id_of_enocean_device> [, eep = <EEP_name>][, channel = <channelName>][, parameter = <parameterName>]}"

The device id is printed on the device package. When the EEP ([EnOcean Equipment Profile](http://www.enocean-alliance.org/eep/)) is needed, the eep (e.g. "F6.02.01") is also printed on the device package. In OpenHAB the three profile elements of the EEP are separated
 by colons `:`(e.g. "F6:02:01"). All parameters in [] are optional and only used for some devices.

## Supported Devices

These EEP Profiles are already supported. 
If your device is not listed, please add an issue for it at openhab.

- Rocker Switches
    - F6:02:01 (Light and Blind Control - Application Style 1)
- Environment Sensors
    - A5:02:05 (Temperature Range from 0° to 40°)
- Contact Sensors
    - D5:00:01 (Single Input Contact)
 
### Rocker Switches

- eep = F6:02:01, F6:02:02
- channel = A / B
- parameter =
    - I: The I button (normally the one with the solid arrow sign) was pressed (Switch, pressed=Update to ON, released=Update to OFF)
    - O: The O button (normally the one with the empty arrow sign) was pressed (Switch, pressed=Update to ON, released=Update to OFF)

Be aware that the parameters are still under discussion and can be subject to change.

#### Examples

##### Standard usage:

    Switch Button_Up () {enocean="{id=00:00:00:00, eep=F6:02:01, channel=B, parameter=I}"}

- pressed: ON
- released: OFF

EnOcean (roller shutter-) actors are not supported yet (27. November 2013).  
EnOcean switch to control not EnOcean actors:
##### To control a roller shutter

    Rollershutter myShutters () {enocean="{id=00:00:00:00, eep=F6:02:01}"}

- Short press down: Close roller shutter or stop it when it was started shortly ago
- Short press up: Open roller shutter or stop it when it was started shortly ago
- Long press down: Close roller shutter as long as the button is pressed
- Long press up: Open roller shutter as long as the button is pressed

See [Example of roller shutter controlling](Samples-Item-Definitions#how-to-control-a-homematic-roller-shutter-with-an-enocean-rocker)

##### To control a dimmer (left buttons = channel A):

    Dimmer myLights () {enocean="{id=00:00:00:00, channel=A, eep=F6:02:01}"}
- Short press down: Switch light ON
- Short press up: Switch light OFF
- Long press down: Dim light UP as long as the button is pressed (INCREASE every 300ms)
- Long press up: Dim light DOWN as long as the button is pressed (DECREASE every 300ms)

##### To us as a normal Switch (supported with 1.4.0):

    Switch mySwitch () {enocean="{id=00:00:00:00, channel=A, eep=F6:02:01}"}
- Press down: Switch udated to ON
- Press up: Switch updated to OFF

### Environment Sensors

- eep = A5:02:05
- channel = none
- parameter =
    - TEMPERATURE: The current temperature (Number, unit=°C)

#### Examples

    Number UG_Flur_Temp "Temperature [%.1f °C]" <temperature> () {enocean="{id=00:00:00:00, eep=A5:02:05, parameter=TEMPERATURE}"}

### Contact Sensors

- eep = D5:00:01
- channel = none
- parameter =
    - CONTACT_STATE: Contact Open / Closed (OpenClosedType)

#### Examples

    In the .items file:
    Number mySwitch "Test Switch" <contact> {enocean="{id=00:00:00:00, eep=D5:00:01, parameter=CONTACT_STATE:closed}"}

## Controller Hardware

### USB 300

USB stick to control EnOcean devices. Only supported controller for now. Part of the EnOcean Development Kit (EDK).

## System Requirements / Special Installations

### Synology DS213+

See this [Openhab Forum](https://groups.google.com/forum/#!topic/openhab/SVcstuqC8H8) entry for details. 