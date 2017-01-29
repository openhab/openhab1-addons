# EnOcean Binding

[EnOcean](https://www.enocean.com/en/) provides reliable and self-powered wireless sensor solutions for the Internet of Things.  This binding allows openHAB to monitor and control EnOcean devices through the EnOcean USB 300 gateway.

[![EnOcean Binding](http://img.youtube.com/vi/GpERJflmJKQ/0.jpg)](http://www.youtube.com/watch?v=GpERJflmJKQ) 

## Prerequisites

* USB 300 stick to control EnOcean devices. It is the only supported controller for now, and part of the EnOcean Development Kit (EDK).
* See [Supported Devices](#supported-devices) for curently supported EnOcean Equipment Profiles ([EEPs](http://www.enocean-alliance.org/eep/)).
* If you see errors in your log containing `java.lang.UnsatisfiedLinkError`, [this discussion](https://groups.google.com/forum/#!topic/openhab/SVcstuqC8H8) might be helpful.

## Binding Configuration

This binding can be configured in the file `services/enocean.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| serialPort |       |   Yes    | serial port (can be a virtual one) where the EnOcean transceiver is connected to. A USB adapter creates a virtual serial port (often it's `/dev/ttyUSB0` under Linux) |

## Item Configuration

General format:

```
enocean="{id=<id_of_enocean_device> [, eep = <EEP_name>][, channel = <channel>][, parameter = <parameter>]}"
```

where:

* sections in `[brackets]` are optional
* `<id_of_enocean_device>` is printed on the device package
* `<EEP_name>`, is (when needed) the EEP (e.g. "F6.02.01") that is also printed on the device package.  In openHAB, the three profile elements of the EEP are separated by colons `:` (e.g. "F6:02:01").
* `<channel>` is specific to the supported devices (see below)
* `<parameter>` is specific to the supported devices (see below)

## Supported Devices

The EEP Profiles below are already supported. If your device is not listed, please add an issue for it [here](https://github.com/openhab/openhab1-addons/issues).

### Rocker Switches

- eep = F6:02:01 - Light and Blind Control - Application Style 1
- eep = F6:02:02
- channel = A / B
- parameter =
    - I: The I button (normally the one with the solid arrow sign) was pressed (Switch, pressed=Update to ON, released=Update to OFF)
    - O: The O button (normally the one with the empty arrow sign) was pressed (Switch, pressed=Update to ON, released=Update to OFF)

Be aware that the parameters are still under discussion and can be subject to change.

#### Examples

```
Switch Button_Up { enocean="{id=00:00:00:00, eep=F6:02:01, channel=B, parameter=I}" }
```

- pressed: ON
- released: OFF

EnOcean (roller shutter-) actors are not supported yet (27. November 2013). EnOcean switch to control not EnOcean actors:

##### To control a roller shutter

```
Rollershutter myShutters { enocean="{id=00:00:00:00, eep=F6:02:01}" }
```

- Short press down: Close roller shutter or stop it when it was started shortly ago
- Short press up: Open roller shutter or stop it when it was started shortly ago
- Long press down: Close roller shutter as long as the button is pressed
- Long press up: Open roller shutter as long as the button is pressed

##### To control a dimmer (left buttons = channel A):

```
Dimmer myLights { enocean="{id=00:00:00:00, channel=A, eep=F6:02:01}" }
```

- Short press down: Switch light ON
- Short press up: Switch light OFF
- Long press down: Dim light UP as long as the button is pressed (INCREASE every 300ms)
- Long press up: Dim light DOWN as long as the button is pressed (DECREASE every 300ms)

##### To us as a normal Switch (supported with 1.4.0):

```
Switch mySwitch { enocean="{id=00:00:00:00, channel=A, eep=F6:02:01}" }
```

- Press down: Switch udated to ON
- Press up: Switch updated to OFF

### Environment Sensors

- eep = A5:02:05 - Temperature Range from 0° to 40°
- channel = none
- parameter =
    - TEMPERATURE: The current temperature (Number, unit=°C)

#### Examples

```
Number UG_Flur_Temp "Temperature [%.1f °C]" <temperature> { enocean="{id=00:00:00:00, eep=A5:02:05, parameter=TEMPERATURE}" }
```

### Contact Sensors

- eep = D5:00:01 - Single Input Contact
- channel = none
- parameter =
    - CONTACT_STATE: Contact Open / Closed (OpenClosedType)

#### Examples


```
Number mySwitch "Test Switch" <contact> { enocean="{id=00:00:00:00, eep=D5:00:01, parameter=CONTACT_STATE:closed}" }
```
