## Introduction

Zibase is a french home automation box that support multiple RF protocols including 433 Mhz (Chacon, Visionic, Oregon...), ZWave, EnOcean, X2D.

The Zibase binding enable bi-directional communication between OpenHab and a [classic Zibase](http://www.zodianet.com/toolbox-zibase/zibase-classic.html).

Currently, this binding allows to 
- handle any transmiter's information the zibase receives (eg: temperature sensors)
- send command to receivers (eg: Roller Shutters)
- Call a scenario by its id
- Set and Get one of the 31 "public" Zibase variables values


Communication is done both in realtime throught network socket connection and via the sensors.xml flow. This binding is built over [J-Zapi](https://code.google.com/p/j-zapi/).

For installation of the binding, please see Wiki page [[Bindings]].

**Note :** only the Zibase 1 has been tested as I only have this model. Feel free to tr other model and send me your feedback !

## Main configuration

This binding provides several configuration parameters to handle zibase connection (see openhab_default.cfg).
- zibase:ip : this is the only mandatory one. You have to specifiy the IP Address of the Zibase on your Lan.
- zibase:listenerHost : The IP Address sent to Zibase to register to as a listener (defaults is 127.0.0.1)
- zibase:listenerPort : The TCP Port to use for data exchange with the Zibase (defaults is 9876)
- zibase:refresh : The refresh interval in ms for sensors.xml reading (defaults is 60000)

## Generic Item Binding Configuration

In order to bind an item to the Zibase, you need to provide configuration settings. The easiest way to do this is to add binding information in your item file (in the folder configurations/items). 

The format of the binding configuration is simple and looks like this:
    `zibase=<type>,<Id>,<value>`

where:
- `<type>` can be one of the following values:
  - EMT for an emitter
  - RCV for a receiver
  - SCE for a scenario
  - VAR for a variable
- `<Id>` is the ID of the device. Eg : OS12345, DS12345678, H8
- `<value>` depends on type (see below)

## Specific item binding configurations
### Emitter
Emitter devices are those which send messages to the Zibase. For example : door contact, temperature sensor or presence detector.
For emitter devices, the <value> parameter defines the value you want to extract from the message (each item send severals values at once like: protocol, battery status, Id...).

For these devices, <value> can be either an arbitrary value you want to get uppon receiving a message (see example below for door contact) or one of the following tags if you want a particular value, depending on what the device send :
* rf : get the protocol (eg: "chacon", "x2d868")
* noise : get the RF signal noise (in general around 2000, the lower value the better environment for RF)
* lev : get the RF signal strenght (from 0 : very poor, to 5 : perfect)
* dev : get vendor device's name (eg: "Temp-Hygro", "TH V1.0")
* bat : get the battery status. Can be either "ok" or "ko"
* ch : get the device's Rf channel (from 1 to 3)
* tem : get temperature as a float value (eg: 20.5) 
* temc : get the temperature ceil as a float value
* tra : get total rain
* cra : get current rain
* uvl : get ultra violet level
* awi : get average wind
* dir : get wind direction
* sta : unknown
* kwh : get kilowatts per hour as a float value (eg: 1434.1) 
* w : total watt consumption as an integer (eg: 1300)
* hum : get percentage of humidity as an integer (eg: 54) 
* area : get the area (usually for alarm related devices)
* flag1 : custom vendor flag
* flag2 : custom vendor flag
* flag3 : custom vendor flag

__Examples :__

Map an item to an oregon temperature sensor to get the temperature :

`Number BathRoomTemp "Bathroom temperature [%.1f]" <temperature> { zibase="EMT,OS439184641,tem" }`

Map the battery state of the same sensor :

`String BathRoomSensorBat` "Battery status of bathroom sensor : [%s]" { zibase="EMT,OS439184641,bat" }`

Map a window open status from a contact sensor which has 2 Ids : one for closing and one for opening :

`String WindowStatus "Windows: [%s]" <contact> { zibase="EMT,DS2124523778,open", zibase="EMT,DS2124523776,closed" }`
_In this example, "open" and "closed" are custom values that are affected to the item uppon receiving a message.

Map a presence detector (this time again with a custom value) :

`String PresenceInGarden "There is someone in the garden : [%s]" <presence> { zibase="EMT,H8,on" }`

### Receiver
Receiver devices are mostly devices you send a command to. For example a light switcher or a roller shutter. Mapping item to such a device will most likely be used in rules.

Note : some device also return their status. In this case, you can use the same item to display this status. 

For these devices, the parameter <value> must be set to the protocol of the device, which must be one of :
* CHACON
* DOMIA
* RFS10
* VISONIC433
* VISONIC868
* X10
* X2D433
* X2D433ALRM
* X2D868
* X2D868ALRM
* X2D868BOAC
* X2D868INSH
* X2D868PIWI
* ZWAVE

__Examples :__

Map an item to a light switch :

`Switch LivingRoomLight "Living room light" <switch> { zibase="RCV,A1,CHACON" } `

Map an item to a Chacon roller shutter:

`Rollershutter KitchenShutter	<rollershutter>	{ zibase="RCV,G9,CHACON"}`

Order Shutter to open depending on a light sensor in a rule :

    Rule "open kitchen shutter if it is the morning"
    when
        Item LightSensor changed from dark to light
    then   
        sendCommand(KitchenShutter,ON)
    end

### Scenario
Unlike receivers and emitters, scenarios are not representing a device but rules that are stored on the Zibase (using Zodianet cloud interface).

Using this, you can ask the zibase to launch a scenario via a "sendCommand()" openhab instruction. So for Scenarios, the <value> parameter is not used.

__Example:__

Map a scenario to an item :

`String ZibaseScenario27 { zibase="SCE,27"} `

Launch a scenario from a rule :

    ...
    sendCommand(ZibaseScenario26,ON)
    ...

### Variable
Like scenario, a variable does not bind a device but one of the 31 internal "public" variables of the Zibase. You can use them to store and get values from. A variable can contain only a 16 bits signed integer. That is to say an integer from -32768 to 32768.

Variables can be used both to get and to set a value. The <value> parameter is not used.

__Example :__

Map an item to the Zibase variable 14 :

`Number Variable14 { zibase="VAR,14" } `

Display an editable value through a sitemap :

    Text item=Variable14
    Setpoint item=Variable14 label="variable 14" minValue="-32768" maxValue="32768" step="10"

Set the value 50 to the variable in a rule :

    ...
    sendCommand(Variable14,50)
    ...