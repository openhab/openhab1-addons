## Table of contents

* [Introduction](#introduction)
* [Binding installation](#binding-installation)
 * [openHAB 1.8 (OH1)](#openhab-18-oh1)
 * [openHAB 2.0 (OH2)](#openhab-20-oh2)
* [Binding configuration](#binding-configuration)
 * [Stick configuration](#stick-configuration)
 * [Device configuration](#device-configuration)
 * [Binding configuration example](#binding-configuration-example)
* [Item configuration](#item-configuration)
 * [Syntax](#syntax)
 * [Commands](#commands)
 * [Variables](#variables)
 * [Item configuration example](#item-configuration-example)
* [Sitemap with example items](#sitemap-with-example-items)
* [Known issues](#known-issues)
* [Change log](#change-log)


## Introduction

The Plugwise binding adds support to openHAB for [Plugwise](https://www.plugwise.com) ZigBee devices using the Stick. 

The binding supports the following Plugwise devices:
* [Circle](https://www.plugwise.com/products/appliances-and-lighting/energy-meters-and-switches/circle), a power outlet plug that provides energy measurement and switching control of appliances
* [Circle+](https://www.plugwise.com/products/appliances-and-lighting/energy-meters-and-switches/circle), a special Circle that coordinates the ZigBee network and acts as network gateway
* [Scan](https://www.plugwise.com/products/appliances-and-lighting/presence-and-light-sensor/scan), a wireless motion (PIR) and light sensor
* [Sense](https://www.plugwise.com/products/indoor-climate/humidity-and-temperature-sensor/sense), a wireless temperature and humidity sensor
* [Stealth](https://www.plugwise.com/products/appliances-and-lighting/energy-meters-and-switches/stealth), a Circle with a more compact form factor that can be built-in
* [Stick](https://www.plugwise.com/home-start), the ZigBee USB controller that openHAB uses to communicate with the Circle+
* [Switch](https://www.plugwise.com/products/appliances-and-lighting/energy-meters-and-switches/switch), a wireless wall switch 

Users should use the Plugwise [Source](https://www.plugwise.com/source) software to define the network, reset devices, perform firmware upgrade, and so forth, e.g. anything which requires user input or interactivity. 

> Currently only "V2" of the Plugwise protocol is supported. It is adviced that users of the binding upgrade their devices to the latest firmware using the Plugwise Source software.

## Binding installation

### openHAB 1.8 (OH1)

This is the latest stable release of openHAB.

1. Follow [Getting Started](http://www.openhab.org/getting-started/index.html) for installation of OH1.
2. To use the Plugwise Binding extract `org.openhab.binding.serial-1.8.3.jar` and `org.openhab.binding.plugwise-1.8.3.jar` from `distribution-1.8.3-addons.zip` to the openHAB `addons` directory.

> The latest development version of the binding (1.9.0) can also be used with OH1 by downloading it from the [openHAB build server](https://openhab.ci.cloudbees.com/job/openHAB1-Addons/ws/bundles/binding/org.openhab.binding.plugwise/target/org.openhab.binding.plugwise-1.9.0-SNAPSHOT.jar).

### openHAB 2.0 (OH2)

Version 2.0 is the openHAB development version. It may be a little buggy but it looks better with its Basic UI and Paper UI.

1. Follow the [Demo Tutorial](http://docs.openhab.org/tutorials/demo.html) for installation of OH2.
2. To use the Plugwise Binding, open Paper UI, click on `Extensions`, scroll down the list until you see the Plugwise Binding and click on its `Install` button.

> The latest development version of the binding (1.9.0) is already part of OH2.


## Binding configuration

### Stick configuration

Before the binding can be used, the serial port of the Stick needs to be configured. This is done by uncommenting `stick.port` and assigning a proper value to it in the (binding) configuration file:

OH1: `configurations/openhab.cfg`

OH2: `conf/services/plugwise.cfg`

> * In OH1 all Plugwise binding configuration keys are prefixed with `plugwise:`. In OH2 this prefix is optional.
> * In OH2 you can also configure the Stick in Paper UI via `Configuration > Bindings > Plugwise Binding > Configure`.
> * To determine the serial port in Linux, insert the Stick, then execute the `dmesg` command. The last few lines of the output will contain the USB port of the Stick (e.g. `ttyUSB0`). In Windows the Device Manager lists it in the `Ports (COM & LPT)` section.
> * On some Linux distributions (e.g. Raspbian) an OS restart may be required before the Stick is properly configured.
> * To access the serial port of the Stick on Linux, the user running openHAB needs to be part of the 'dialout' group. E.g. for the user 'openhab' issue the following command: `sudo adduser openhab dialout`

### Device configuration

The Plugwise device configuration is also stored in the (binding) configuration file. The binding does not (yet) support automatic device type detection. When no devices are configured, the binding assumes each device to be a Circle. In version 1.9.0 support was added for other device types. These need to be configured in the configuration file. The configuration file also allows you to assign names to MAC addresses of Plugwise devices. This makes [Item configurations](#item-configuration) easier to understand.

### Binding configuration example

Below follows an example configuration with all possible device types. It is also used for the [Item configuration example](#item-configuration-example).

    ############################## Plugwise Binding #######################################
    #
    # the serial port of the Stick, e.g. "COM1" for Windows or "/dev/ttyUSB0" for Linux
    # "stick" is reserved Plugwise ID
    plugwise:stick.port=/dev/ttyUSB0
    # interval in ms to wait between messages sent on the ZigBee network
    #plugwise:stick.interval=150

    # you can also name your device circleplus and omit the line specifying the circleplus type (backwards compatibility)
    plugwise:lamp.mac=000D6F0000A1B1C1
    plugwise:lamp.type=circleplus

    # the default device type is circle, so in this case the type could be ommitted (backwards compatibility)
    plugwise:fan.mac=000D6F0000A2B2C2
    plugwise:fan.type=circle

    plugwise:motionsensor.mac=000D6F0000A3B3C3
    plugwise:motionsensor.type=scan

    plugwise:climatesensor.mac=000D6F0000A4B4C4
    plugwise:climatesensor.type=sense

    plugwise:fridge.mac=000D6F0000A5B5C5
    plugwise:fridge.type=stealth

    plugwise:lightswitches.mac=000D6F0000A6B6C6
    plugwise:lightswitches.type=switch

> * The device `.type=` configuration option is available since version 1.9.0 of the binding. In older versions of the binding a Circle+ is configured by giving it the name `circleplus`.
> * openHAB may need to be restarted when you change the `stick.port` or device configurations.
> * The MACs are stickered to the back of Plugwise devices. The binding uses full MACs i.e. also the fine print on the sticker. If you don't want to get off your chair, climb up ladders and unplug devices all across your home, causing all sorts of havoc; you can also find them in Source. Open `Settings > Appliances`. Then double click on an appliance. Click on the little Circle icon to the right of the short Address to see the details of a module and the full MAC (Address). Similarly the MACs of a Scan, Sense and Switch can also be obtained from the Appliances screen by double clicking them in the `Sensors and other modules` list.


## Item configuration


### Syntax

The Plugwise Binding binds Plugwise devices to openHAB items. This is done using a binding specific syntax in the openHAB item configuration files. The item configuration files are stored in:

OH1: `configurations/items`

OH2: `conf/items`

The syntax below uses the following elements:
* `<command>` is an `OnOff` type command/variable, i.e. either `ON` or `OFF`
* `<plugwise id>` corresponds with the device name (from the device configuration, i.e. `plugwise:<plugwise id>.mac=[MAC]`) or the full MAC address    
* `<plugwise command>` is the command to be send to the Plugwise device when `<command>` is received
* `<plugwise variable>` is a device status variable polled or pushed from the device and stored in an Item
* `<polling interval>` is the interval in seconds to poll the given variable

> The binding syntax for OH1 and OH2 is the same.

#### Polled state 

Mains powered devices (Circle(+), Stealth) are polled by the binding for their state. 

The syntax for items that trigger `OnOff` commands is:

    plugwise="[<command>:<plugwise id>:<plugwise command>:<polling interval>], [<command>:<plugwise id>:<plugwise command>:<polling interval>], ..."

***

The syntax for items that store a device status variable is:

    plugwise="[<plugwise id>:<plugwise variable>:<polling interval>], [<plugwise id>:<plugwise variable>:<polling interval>], ..."


#### Pushed state 

Battery powered devices (Scan, Sense, Switch) push their state to the binding. 

The syntax for items that have an `OnOff` state have is:

    plugwise="[<command>:<plugwise id>:<plugwise variable>], [<command>:<plugwise id>:<plugwise variable>], ..."

***

The syntax for items that store a device status variable is:

    plugwise="[<plugwise id>:<plugwise variable>], [<plugwise id>:<plugwise variable>], ..."


### Commands

Valid `<plugwise command>`'s are:

<table>
  <tr><td><b>Command</b></td><td><b>Item type</b></td><td><b>Description</b></td><td><b>Supported devices</b></td></tr>
  <tr><td>state</td><td>OnOff</td><td>Switch the internal relay On or Off</td><td>Circle(+), Stealth</td></tr>
</table>

### Variables

Valid `<plugwise variable>`s are:

<table>
  <tr><td><b>Variable</b></td><td><b>Item type</b></td><td><b>Description</b></td><td><b>Supported devices</b></td></tr>
  <tr><td>clock</td><td>String</td><td>Time as indicated by the internal clock of the device</td><td>Circle(+), Stealth</td></tr>
  <tr><td>lasthour</td><td>Number</td><td>Energy consumption over the last hour, in kWh</td><td>Circle(+), Stealth</td></tr>
  <tr><td>lasthour-stamp</td><td>DateTime</td><td>Date/Time stamp of the last hourly energy consumption history entry</td><td>Circle(+), Stealth</td></tr>
  <tr><td>power</td><td>Number</td><td>Current energy consumption, measured over 1 second interval, in Watt</td><td>Circle(+), Stealth</td></tr>
  <tr><td>power-stamp</td><td>DateTime</td><td>Date/Time stamp of the last energy consumption measurement</td><td>Circle(+), Stealth</td></tr>
  <tr><td>realtime-clock</td><td>DateTime</td><td>Date/Time as indicated by the internal clock of the Circle+</td><td>Circle+</td></tr>

  <tr><td>lastseen</td><td>DateTime</td><td>Date/Time stamp of the last received message. Because there is no battery level indication this is a helpful value to determine if a battery powered device is still operating properly even when no state changes occur<br><u>Since: v1.9.0</u></td><td>Scan, Sense, Switch</td></tr>
  <tr><td>triggered</td><td>OnOff</td><td>Most recent switch action initiated by the device. When daylight override is enabled on a Scan this corresponds one to one with motion detection<br><u>Since: v1.9.0</u></td><td>Scan, Sense</td></tr>
  <tr><td>triggered-stamp</td><td>DateTime</td><td>Date/Time stamp of the last trigger state update<br><u>Since: v1.9.0</u></td><td>Scan, Sense</td></tr>
  <tr><td>humidity</td><td>Number</td><td>Current relative humidity (%)<br><u>Since: v1.9.0</u></td><td>Sense</td></tr>
  <tr><td>humidity-stamp</td><td>DateTime</td><td>Date/Time stamp of the last relative humidity measurement<br><u>Since: v1.9.0</u></td><td>Sense</td></tr>
  <tr><td>temperature</td><td>Number</td><td>Current temperature (°C)<br><u>Since: v1.9.0</u></td><td>Sense</td></tr>
  <tr><td>temperature-stamp</td><td>DateTime</td><td>Date/Time stamp of the last temperature measurement<br><u>Since: v1.9.0</u></td><td>Sense</td></tr>
  <tr><td>left-button-state</td><td>OnOff</td><td>Current state of the left button<br><u>Since: v1.9.0</u></td><td>Switch</td></tr>
  <tr><td>left-button-state-stamp</td><td>DateTime</td><td>Date/Time stamp of the last left button state update<br><u>Since: v1.9.0</u></td><td>Switch</td></tr>
  <tr><td>right-button-state</td><td>OnOff</td><td>Current state of the right button<br><u>Since: v1.9.0</u></td><td>Switch</td></tr>
  <tr><td>right-button-state-stamp</td><td>DateTime</td><td>Date/Time stamp of the last right button state update<br><u>Since: v1.9.0</u></td><td>Switch</td></tr>
</table>

### Item configuration example

For each device type, command and variable type an example is given below. This example uses the devices defined in the [Binding configuration example](#binding-configuration-example).

    /* Circle+ */
    Switch Lamp_Switch "Switch" <switch> { plugwise="[ON:lamp:state:15], [OFF:lamp:state:15]" }
    String Lamp_Clock "Clock [%s]" <clock> { plugwise="[lamp:clock:15]"}
    Number Lamp_Power "Power [%.1f W]" <energy> { plugwise="[lamp:power:10]"}
    DateTime Lamp_Power_Stamp "Power stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[lamp:power-stamp:15]"}
    Number Lamp_Last_Hour "Last hour [%.3f kWh]" <chart> { plugwise="[lamp:lasthour:60]"}
    DateTime Lamp_Last_Hour_Stamp "Last hour stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[lamp:lasthour-stamp:60]"}
    DateTime Lamp_Real_Time_Clock "Real time clock [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <clock> { plugwise="[lamp:realtime-clock:60]"}


    /* Circle or Stealth */
    Switch Fan_Switch "Switch" <switch> { plugwise="[ON:fan:state:15], [OFF:fan:state:15]" }
    String Fan_Clock "Clock [%s]" <clock> { plugwise="[fan:clock:15]" }
    Number Fan_Power "Power [%.1f W]" <energy> { plugwise="[fan:power:10]" }
    DateTime Fan_Power_Stamp "Power stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[fan:power-stamp:15]" }
    Number Fan_Last_Hour "Last hour [%.3f kWh]" <chart> { plugwise="[fan:lasthour:60]" }
    DateTime Fan_Last_Hour_Stamp "Last hour stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[fan:lasthour-stamp:60]" }


    /* Scan */
    Switch Motion_Sensor_Switch "Triggered [%s]" <switch> { plugwise="[ON:motionsensor:triggered], [OFF:motionsensor:triggered]" }
    DateTime Motion_Sensor_Triggered_Stamp "Triggered stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[motionsensor:triggered-stamp]" }
    DateTime Motion_Sensor_Last_Seen "Last seen [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <clock> { plugwise="[motionsensor:lastseen]" }


    /* Sense */
    Switch Climate_Sensor_Switch "Triggered [%s]" <switch> { plugwise="[ON:climatesensor:triggered], [OFF:climatesensor:triggered]" }
    DateTime Climate_Sensor_Triggered_Stamp "Triggered stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[climatesensor:triggered-stamp]" }
    Number Climate_Sensor_Humidity "Humidity [%.1f %%]" <humidity> { plugwise="[climatesensor:humidity]" }
    DateTime Climate_Sensor_Humidity_Stamp "Humidity stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[climatesensor:humidity-stamp]" }
    Number Climate_Sensor_Temperature "Temperature [%.1f °C]" <temperature> { plugwise="[climatesensor:temperature]" }
    DateTime Climate_Sensor_Temperature_Stamp "Temperature stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[climatesensor:temperature-stamp]" }
    DateTime Climate_Sensor_Last_Seen "Last seen [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <clock> { plugwise="[climatesensor:lastseen]" }


    /* Switch */
    Switch Light_Switches_Left_Button_State "Left button [%s]" <switch> { plugwise="[ON:lightswitches:left-button-state], [OFF:lightswitches:left-button-state]" }
    DateTime Light_Switches_Left_Button_State_Stamp "Left button stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[lightswitches:left-button-state-stamp]" }
    Switch Light_Switches_Right_Button_State "Right button [%s]" <switch> { plugwise="[ON:lightswitches:right-button-state], [OFF:lightswitches:right-button-state]" }
    DateTime Light_Switches_Right_Button_State_Stamp "Right button stamp [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <calendar> { plugwise="[lightswitches:right-button-state-stamp]" }
    DateTime Light_Switches_Last_Seen "Last seen [%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS]" <clock> { plugwise="[lightswitches:lastseen]" }

> A MAC can be used instead of a device name, e.g. `fan` can be replaced with `000D6F0000A2B2C2`:

> `Number Fan_Power "Power [%.1f W]" <energy> { plugwise="[000D6F0000A2B2C2:power:10]" }`

## Sitemap with example items

You can now add all these items to a sitemap as shown in the example below. Sitemaps are located in:

OH1: `configurations/sitemaps`

OH2: `conf/sitemaps`

    sitemap default label="Main Menu"
    {

	    Frame label="Lamp (Circle+)" {
		    Switch item=Lamp_Switch
		    Text item=Lamp_Clock
		    Text item=Lamp_Power
		    Text item=Lamp_Power_Stamp
		    Text item=Lamp_Last_Hour
		    Text item=Lamp_Last_Hour_Stamp
		    Text item=Lamp_Real_Time_Clock
	    }

	    Frame label="Fan (Circle)" {
		    Switch item=Fan_Switch
		    Text item=Fan_Clock
		    Text item=Fan_Power
		    Text item=Fan_Power_Stamp
		    Text item=Fan_Last_Hour
		    Text item=Fan_Last_Hour_Stamp
	    }
	
	    Frame label="Motion Sensor (Scan)" {
		    Text item=Motion_Sensor_Switch
		    Text item=Motion_Sensor_Triggered_Stamp
		    Text item=Motion_Sensor_Last_Seen
	    }
	
	    Frame label="Climate Sensor (Sense)" {
		    Text item=Climate_Sensor_Switch
		    Text item=Climate_Sensor_Triggered_Stamp
		    Text item=Climate_Sensor_Humidity
		    Text item=Climate_Sensor_Humidity_Stamp
		    Text item=Climate_Sensor_Temperature
		    Text item=Climate_Sensor_Temperature_Stamp
		    Text item=Climate_Sensor_Last_Seen
	    }
	
	    Frame label="Light Switches (Switch)" {
		    Text item=Light_Switches_Left_Button_State
		    Text item=Light_Switches_Left_Button_State_Stamp
		    Text item=Light_Switches_Right_Button_State
		    Text item=Light_Switches_Right_Button_State_Stamp
		    Text item=Light_Switches_Last_Seen
	    }

    }

Click on a thumbnail below to see how this sitemap is rendered in the various openHAB user interfaces.

<table>
  <tr><td><b>OH1 Classic UI</b></td><td><b>OH2 Classic UI</b></td><td><b>OH2 Basic UI</b></td></tr>
  <tr>
    <td>
      <a href="https://cloud.githubusercontent.com/assets/12213581/17384808/d583edd4-59de-11e6-8c81-840f8a58592a.png">
        <img src="https://cloud.githubusercontent.com/assets/12213581/17384808/d583edd4-59de-11e6-8c81-840f8a58592a.png" alt="openHAB 1.8 Classic UI" height="300"/>
      </a>
    </td>
    <td>
      <a href="https://cloud.githubusercontent.com/assets/12213581/17384889/574492c4-59df-11e6-93e8-4855ad0a7e15.png">
      <img src="https://cloud.githubusercontent.com/assets/12213581/17384889/574492c4-59df-11e6-93e8-4855ad0a7e15.png" alt="openHAB 2.0 Classic UI" height="300"/>
      </a>
    </td>
    <td>
      <a href="https://cloud.githubusercontent.com/assets/12213581/17384911/7f9c0b6c-59df-11e6-8e55-48d2a8e053ef.png">
        <img src="https://cloud.githubusercontent.com/assets/12213581/17384911/7f9c0b6c-59df-11e6-8e55-48d2a8e053ef.png" alt="openHAB 2.0 Basic UI" height="300"/>
      </a>
    </td>
  </tr>
</table>

## Known issues

* OH2: When you rename or remove devices from `plugwise.cfg` they are still persisted in openHAB. To fix this enter `config:delete Plugwise` in the openHAB console and restart openHAB. This will clear the persisted Plugwise configuration and reload it from `plugwise.cfg`


## Change log

### openHAB 1.4.0

* Issue [#456](https://github.com/openhab/openhab/issues/456) - Various fixes of Plugwise binding ([#11](https://github.com/openhab/openhab/pull/11))
* Change Plugwise Binding from `AbstractBinding` to `AbstractActiveBinding` + Fix issue [#456](https://github.com/openhab/openhab/issues/456) ([#40](https://github.com/openhab/openhab/pull/40))
* Fixes for issues [#649](https://github.com/openhab/openhab/issues/649), [#496](https://github.com/openhab/openhab/issues/496) and [#497](https://github.com/openhab/openhab/issues/497) ([#810](https://github.com/openhab/openhab/pull/810))
* Further improvement of Quartz Job scheduling ([#812](https://github.com/openhab/openhab/pull/812))
* Fix configuration file parsing, quartz scheduling problem ([#823](https://github.com/openhab/openhab/pull/823))

### openHAB 1.5.0

* Various fixes ([#850](https://github.com/openhab/openhab/pull/850))

### openHAB 1.9.0

* Clean up logging ([#4122](https://github.com/openhab/openhab/pull/4122))
* Fix [#1003](https://github.com/openhab/openhab/issues/1003): High CPU load ([#4162](https://github.com/openhab/openhab/pull/4162))
* Add support for Scan, Sense, Stealth and Switch [#4565](https://github.com/openhab/openhab/issues/4565) ([#4586](https://github.com/openhab/openhab/pull/4586))
* Add openHAB 2 feature addon ([#4644](https://github.com/openhab/openhab/pull/4644))
* `NullPointerException` may occur when concurrent threads get `PlugwiseDevice` from `Stick` ([#4648](https://github.com/openhab/openhab/pull/4648))
* Fix Circles not always calibrated ([#4669](https://github.com/openhab/openhab/pull/4669))
* Improve power state switching, fix ObjectAlreadyExistsException when (re)starting binding ([#4700](https://github.com/openhab/openhab/pull/4700))
* FindBugs issues fix ([#4704](https://github.com/openhab/openhab/pull/4704))
* Reliability improvements ([#4797](https://github.com/openhab/openhab/pull/4797))
* Fix exceptions at binding startup, improve device cache ([#4842](https://github.com/openhab/openhab/pull/4842))
* Fix device names can sometimes not be resolved ([#4901](https://github.com/openhab/openhab/pull/4901))
* Power measurement improvements ([#5009](https://github.com/openhab/openhab/pull/5009))
