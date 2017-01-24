### Note: this documentation is for OpenHAB v1.8. Documentation for OpenHAB 1.7 [can be found here](https://github.com/berndpfrommer/openhab/blob/insteonplm/bundles/binding/org.openhab.binding.insteonplm/src/main/docs/insteoplm_v1.7.md). Please install the InsteonPLM v1.8 jar file from cloudbees before asking questions on the forum, as the 1.7 version is way outdated.

## Introduction

Insteon is a home area networking technology developed primarily for
connecting light switches and loads. Insteon devices send messages
either via the power line, or by means of radio frequency (RF) waves,
or both (dual-band). A considerable number of Insteon compatible
devices such as switchable relays, thermostats, sensors etc are
available. More about Insteon can be found on [Wikipedia](http://en.wikipedia.org/wiki/Insteon).

This binding provides access to the Insteon network by means of either an
Insteon PowerLinc Modem (PLM), a legacy Insteon Hub 2242-222 (pre-2014) or the new 2245-222 ("2014") Insteon Hub.
The modem can be connected to the openHAB server either via a serial port (Model 2413S) or a USB port
(Model 2413U). As of 1.9, the modem can also be connected via TCP (such as ser2net).  The binding translates openHAB commands into Insteon
messages and sends them on the Insteon network. Relevant messages from
the Insteon network (like notifications about switches being toggled)
are picked up by the modem and converted to openHAB status updates by
the binding. The binding also supports sending and receiving of legacy X10 messages.

OpenHAB is not a configuration tool! To configure and set up your devices, link the devices manually via the set buttons, or use the free [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal) software. The free HouseLinc software from Insteon can also be used for configuration, but it wipes the modem link database clean on its initial use, requiring to re-link the modem to all devices.

## Insteon devices

Every Insteon device *type* is uniquely identified by its Insteon
*product key*, a six digit hex number. For some of the older device
types (in particular the SwitchLinc switches and dimmers), Insteon
does not give a product key, so an arbitrary fake one of the format
Fxx.xx.xx (or Xxx.xx.xx for X10 devices) is assigned by the binding.

Finally, each Insteon device comes with a hard-coded Insteon *address*
of the format 'xx.xx.xx' that can be found on a label on the device. This address should be
recorded for every device in the network, as it is a mandatory part of
the binding configuration string.

The following devices have been tested and should work out of the box:
<table>
<tr>
<td>Model</td><td>Description</td><td>Product Key</td><td>tested by</td>
</tr>
<tr>
<td>2477D</td><td>SwitchLinc Dimmer</td><td>F00.00.01</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2477S</td><td>SwitchLinc Switch</td><td>F00.00.02</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2845-222</td><td>Hidden Door Sensor</td><td>F00.00.03</td><td>Josenivaldo Benito</td>
</tr>
<tr>
<td>2876S</td><td>ICON Switch</td><td>F00.00.04</td><td>Patrick Giasson</td>
</tr>
<tr>
<td>2456D3</td><td>LampLinc V2</td><td>F00.00.05</td><td>Patrick Giasson</td>
</tr>
<tr>
<td>2442-222</td><td>Micro Dimmer</td><td>F00.00.06</td><td>Josenivaldo Benito</td>
</tr>
<tr>
<td>2453-222</td><td>DIN Rail On/Off</td><td>F00.00.07</td><td>Josenivaldo Benito</td>
</tr>
<tr>
<td>2452-222</td><td>DIN Rail Dimmer</td><td>F00.00.08</td><td>Josenivaldo Benito</td>
</tr>
<tr>
<td>2458-A1</td><td>MorningLinc RF Lock Controller</td><td>F00.00.09</td><td>cdeadlock</td>
</tr>
<tr>
<td>2852-222</td><td>Leak Sensor</td><td>F00.00.0A</td><td>Kirk McCann</td>
</tr>
<tr>
<td>2672-422</td><td>LED Dimmer</td><td>F00.00.0B</td><td>???</td>
</tr>
<tr>
<td>2476D</td><td>SwitchLinc Dimmer</td><td>F00.00.0C</td><td>LiberatorUSA</td>
</tr>
<tr>
<td>2634-222</td><td>On/Off Dual-Band Outdoor Module</td><td>F00.00.0D</td><td>LiberatorUSA</td>
</tr>
<tr>
<td>2342-2</td><td>Mini Remote</td><td>F00.00.10</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2663-222</td><td>On/Off Outlet</td><td>0x000039</td><td>SwissKid</td>
</tr>
<tr>
<td>2466D</td><td>ToggleLinc Dimmer</td><td>F00.00.11</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2466S</td><td>ToggleLinc Switch</td><td>F00.00.12</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2672-222</td><td>LED Bulb</td><td>F00.00.13</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2487S</td><td>KeypadLinc On/Off 6-Button</td><td>F00.00.14</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2334-232</td><td>KeypadLink Dimmer 6-Button</td><td>F00.00.15</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2334-232</td><td>KeypadLink Dimmer 8-Button</td><td>F00.00.16</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2423A1</td><td>iMeter Solo Power Meter</td><td>F00.00.17</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2423A1</td><td>Thermostat 2441TH</td><td>F00.00.18</td><td>Daniel Campbell, Bernd Pfrommer</td>
</tr>
<tr>
<td>2457D2</td><td>LampLinc Dimmer</td><td>F00.00.19</td><td>Jonathan Huizingh</td>
</tr>
<tr>
<td>2475SDB</td><td>In-LineLinc Relay</td><td>F00.00.1A</td><td>Jim Howard</td>
</tr>
<tr>
<td>2635-222</td><td>On/Off Module</td><td>F00.00.1B</td><td>Jonathan Huizingh</td>
</tr>
<tr>
<td>2475F</td><td>FanLinc Module</td><td>F00.00.1C</td><td>Brian Tillman</td>
</tr>
<tr>
<td>2456S3</td><td>ApplianceLinc</td><td>F00.00.1D</td><td>???</td>
</tr>
<tr>
<td>2674-222</td><td>LED Bulb (recessed)</td><td>F00.00.1E</td><td>Steve Bate</td>
</tr>
<tr>
<td>2477SA1</td><td>220V 30-amp Load Controller N/O</td><td>F00.00.1F</td><td>Shawn R.</td>
</tr>
<tr>
<td>2342-222</td><td>Mini Remote (8 Button)</td><td>F00.00.20</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2450</td><td>IO Link</td><td>0x00001A</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2486D</td><td>KeypadLinc Dimmer</td><td>0x000037</td><td>Patrick Giasson, Joe Barnum</td>
</tr>
<tr>
<td>2484DWH8</td><td>KeypadLinc Countdown Timer</td><td>0x000041</td><td>Rob Nielsen</td>
</tr>
<tr>
<td>2413U</td><td>PowerLinc 2413U USB modem</td><td>0x000045</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2843-222</td><td>Wireless Open/Close Sensor</td><td>0x000049</td><td>Josenivaldo Benito</td>
</tr>
<tr>
<td>2842-222</td><td>Motion Sensor</td><td>0x00004A</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>2486DWH8</td><td>KeypadLinc Dimmer</td><td>0x000051</td><td>Chris Graham</td>
</tr>
<tr>
<td>2472D</td><td>OutletLincDimmer</td><td>0x000068</td><td>Chris Graham</td>
</tr>
<tr>
<td>X10 switch</td><td>generic X10 switch</td><td>X00.00.01</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>X10 dimmer</td><td>generic X10 dimmer</td><td>X00.00.02</td><td>Bernd Pfrommer</td>
</tr>
<tr>
<td>X10 motion</td><td>generic X10 motion sensor</td><td>X00.00.03</td><td>Bernd Pfrommer</td>
</tr>
</table>

## Insteon Groups and Scenes
How do Insteon devices tell other devices on the network that their state has changed? They send out a broadcast message, labeled with a specific *group* number. All devices (called *responders*) that are configured to listen to this message will then go into a pre-defined state. For instance when light switch A is switched to "ON", it will send out a message to group #1, and all responders will react to it, e.g they may go into the "ON" position as well. Since more than one device can participate, the sending out of the broadcast message and the subsequent state change of the responders is referred to as "triggering a scene". At the device and PLM level, the concept of a "scene" does not exist, so you will find it notably absent in the binding code and this document. A scene is strictly a higher level concept, introduced to shield the user from the details of how the communication is implemented.

Many Insteon devices send out messages on different group numbers, depending on what happens to them. A leak sensor may send out a message on group #1 when dry, and on group #2 when wet. The default group used for e.g. linking two light switches is usually group #1.

## Insteon binding process

Before Insteon devices communicate with one another, they must be
linked. During the linking process, one of the devices
will be the "Controller", the other the "Responder" (see e.g. the
[SwitchLinc Instructions](https://www.insteon.com/pdf/2477S.pdf)).

The responder listens to messages from the controller, and reacts to them. Note that except for
the case of a motion detector (which is just a controller to the
modem), the modem controls the device (e.g. send on/off messages to
it), and the device controls the modem (so the modem learns about the
switch being toggled). For this reason, most devices and in particular
switches/dimmers should be linked twice, with one taking the role of
controller during the first linking, and the other acting as
controller during the second linking process. To do so, first press
and hold the "Set" button on the modem until the light starts
blinking. Then press and hold the "Set" button on the remote device,
e.g. the light switch, until it double beeps (the light on the modem
should go off as well). Now do exactly the reverse: press and hold the
"Set" button on the remote device until its light starts blinking,
then press and hold the "Set" button on the modem until it double
beeps, and the light of the remote device (switch) goes off. Done.

For some of the more sophisticated devices the complete linking process can no longer be done with the set buttons, but requires software like e.g. [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal).

## Installation and Configuration

The binding does not support linking new devices on the fly, i.e. all
devices must be linked with the modem *before* starting the InsteonPLM
binding.

1. Copy the binding (e.g. `openhab.binding.insteonplm-<version>.jar` into the `openhab/addons` folder
2. Edit the relevant section in the openhab configuration file
   (`openhab/configurations/openhab.cfg`). Note that while multiple
   modems and/or hubs can be configured, the binding has never been tested for more
   than *one* port! More details about how to connect to the different supported devices are in the `openhab.cfg` file, but in short:
   - plain plm modem on a usb serial port on Linux OS: `insteonplm:port_0=/dev/ttyUSB0`
   - 2242-222 hub: use `insteonplm:port_0=/hub/your_hub_ip_address:9761`
   - 2245-222 hub (2014-): use `/hub2/my_user_name:my_password@myinsteonhub.mydomain:25105,poll_time=1000`
3. Add configuration information to the `.items` file (see below)
4. Optional: configure for debug logging into a separate file (see
trouble shooting section)

## Item Binding Configuration

Since Insteon devices can have multiple features (for instance a
switchable relay and a contact sensor) under a single Insteon address,
an openHAB item is not bound to a device, but to a given feature of a
device:

    insteonplm="<insteon_address>:<product_key>#feature[,<parameter>=value, ...]>"

For instance, the following lines would create two Number items referring to the same thermostat device, but to different features of it:

    Number  thermostatCoolPoint "cool point [%.1f °F]" { insteonplm="32.f4.22:F00.00.18#coolsetpoint" }
    Number  thermostatHeatPoint "heat point [%.1f °F]" { insteonplm="32.f4.22:F00.00.18#heatsetpoint" }


### Simple light switches

The following example shows how to configure a simple light switch (2477S) in the .items file:

    Switch officeLight "office light" {insteonplm="xx.xx.xx:F00.00.02#switch"}


### Simple dimmers

Here is how to configure a simple dimmer (2477D) in the .items file:

    Dimmer kitchenChandelier "kitchen chandelier" {insteonplm="xx.xx.xx:F00.00.01#dimmer"}

Dimmers can be configured with a maximum level when turning a device on or setting a percentage level. If a maximum level is configured, openHAB will never set the level of the dimmer above the level specified. The below example sets a maximum level of 70% for dim 1 and 60% for dim 2:

    Dimmer d1 "dimmer 1" {insteonplm="xx.xx.xx:F00.00.11#dimmer,dimmermax=70"}
    Dimmer d2 "dimmer 2" {insteonplm="xx.xx.xx:F00.00.15#loaddimmer,dimmermax=60"}

Setting a maximum level does not affect manual turning on or dimming a switch.

### On/Off Outlets
Here's how to configure the top and bottom outlet of the in-wall 2 outlet controller:

    Switch fOutTop "Front Outlet Top" <socket> {insteonplm="xx.xx.xx:0x000039#topoutlet"}
    Switch fOutBot "Front Outlet Bottom" <socket> {insteonplm="xx.xx.xx:0x000039#bottomoutlet"}

This will give you individual control of each outlet.
    
### Mini remotes

Link the mini remote to be a controller of the modem by using the set button. Link all buttons, one after the other. The 4-button mini remote sends out messages on groups 0x01 - 0x04, each corresponding to one button. The modem's link database (see [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal)) should look like this:

    0000 xx.xx.xx                       xx.xx.xx  RESP  10100010 group: 01 data: 02 2c 41
    0000 xx.xx.xx                       xx.xx.xx  RESP  10100010 group: 02 data: 02 2c 41
    0000 xx.xx.xx                       xx.xx.xx  RESP  10100010 group: 03 data: 02 2c 41
    0000 xx.xx.xx                       xx.xx.xx  RESP  10100010 group: 04 data: 02 2c 41

This goes into the items file:

    Switch miniRemoteButtonA	    "mini remote button a" {insteonplm="2e.7c.9a:F00.00.10#buttonA", autoupdate="false"}
    Switch miniRemoteButtonB	    "mini remote button b" {insteonplm="2e.7c.9a:F00.00.10#buttonB", autoupdate="false"}
    Switch miniRemoteButtonC	    "mini remote button c" {insteonplm="2e.7c.9a:F00.00.10#buttonC", autoupdate="false"}
    Switch miniRemoteButtonD	    "mini remote button d" {insteonplm="2e.7c.9a:F00.00.10#buttonD", autoupdate="false"}

This goes into the sitemap file:

    Switch item=miniRemoteButtonA label="mini remote button a" mappings=[ OFF="Off", ON="On"]
    Switch item=miniRemoteButtonB label="mini remote button b" mappings=[ OFF="Off", ON="On"]
    Switch item=miniRemoteButtonC label="mini remote button c" mappings=[ OFF="Off", ON="On"]
    Switch item=miniRemoteButtonD label="mini remote button d" mappings=[ OFF="Off", ON="On"]

The switches in the GUI just display the mini remote's most recent button presses. They are not operable because the PLM cannot trigger the mini remotes scenes.

### Motion sensors

Link such that the modem is a responder to the motion sensor. Create a contact.map file in the transforms directory as described elsewhere in this document. Then create entries in the .items file like this:

    Contact motionSensor "motion sensor [MAP(contact.map):%s]" {insteonplm="xx.xx.xx:0x00004A#contact"}
    Number motionSensorBatteryLevel "motion sensor battery level [%.1f]" {insteonplm="xx.xx.xx:0x00004A#data,field=battery_level"}
    Number motionSensorLightLevel "motion sensor light level [%.1f]" {insteonplm="xx.xx.xx:0x00004A#data,field=light_level"}

This will give you a contact, the battery level, and the light level. Note that battery and light level are only updated when either there is motion, or the sensor battery runs low.

### Hidden door sensors

Similar in operation to the motion sensor above.  Link such that the modem is a responder to the motion sensor. Create a contact.map file in the transforms directory like the following:

    OPEN=open
    CLOSED=closed
    -=unknown

Then create entries in the .items file like this:

    Contact doorSensor "Door sensor [MAP(contact.map):%s]" {insteonplm="xx.xx.xx:F00.00.03#contact"}
    Number doorSensorBatteryLevel "Door sensor battery level [%.1f]" insteonplm="xx.xx.xx:F00.00.03#data,field=battery_level"}

This will give you a contact and the battery level. Note that battery level is only updated when either there is motion, or the sensor battery runs low.


### Locks

Read the instructions very carefully: sync with lock within 5 feet to avoid bad connection, link twice for both ON and OFF functionality.

Put something like this into your .items file:

    Switch doorLock "Front Door [MAP(lock.map):%s]" {insteonplm="xx.xx.xx:F00.00.09#switch"}

and create a file "lock.map" in the transforms directory with these entries: 

    ON=Lock
    OFF=Unlock
    -=unknown

### I/O Linc (garage door openers)

The I/O Linc devices are really two devices in one: a relay and a contact. Link the modem both ways, as responder and controller using the set buttons as described in the instructions.

Add this map into your transforms directory as "contact.map":

    OPEN=open
    CLOSED=closed
    -=unknown

and this into your .items file:

    Switch garageDoorOpener "garage door opener" <garagedoor> {insteonplm="xx.xx.xx:0x00001A#switch", autoupdate="false"}
    Contact garageDoorContact "garage door contact [MAP(contact.map):%s]"    {insteonplm="xx.xx.xx:0x00001A#contact"}

To make it visible in the GUI, put this into your sitemap file:

    Switch item=garageDoorOpener label="garage door opener" mappings=[ ON="OPEN/CLOSE"]
    Text item=garageDoorContact

For safety reasons, only close the garage door if you have visual contact to make sure there is no obstruction! The use of automated rules for closing garage doors is dangerous.

>NOTE: If the I/O Linc returns the wrong value when the device is polled (For example you open the garage door and the state correctly shows OPEN, but during polling it shows CLOSED), you probably linked the device with the PLM or hub when the door was in the wrong position. You need unlink and then link again with the door in the opposite position. Please see the Insteon I/O Linc documentation for further details.

### Keypads

Before you attempt to configure the keypads, please familiarize yourself with the concept of an Insteon group.

The Insteon keypad devices typically control one main load and have a number of buttons that will send out group broadcast messages to trigger a scene. If you just want to use the main load switch within openhab just link modem and device with the set buttons as usual, no complicated linking is necessary. But if you want to get the buttons to work, read on.

Each button will send out a message for a different, predefined group. Complicating matters further, the button numbering used internally by the device must be mapped to whatever labels are printed on the physical buttons of the device. Here is an example correspondence table:

| Group | Button Number | 2487S Label |
|-------|---------------|-------------|
|  0x01 |        1      |   (Load)    |
|  0x03 |        3      |     A       |
|  0x04 |        4      |     B       |
|  0x05 |        5      |     C       |
|  0x06 |        6      |     D       |

When e.g. the "A" button is pressed (that's button #3 internally) a broadcast message will be sent out to all responders configured to listen to Insteon group #3. This means you must configure the modem as a responder to group #3 (and #4,#5,#6) messages coming from your keypad. For instructions how to do this, check out the [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal). You can even do that with the set buttons (see instructions that come with the keypad).

While capturing the messages that the buttons emit is pretty straight forward, controlling the buttons is  another matter. They cannot be simply toggled with a direct command to the device, but instead a broadcast message must be sent on a group number that the button has been programmed to listen to. This means you need to pick a set of unused groups that is globally unique (if you have multiple keypads, each one of them has to use different groups), one group for each button. The example configuration below uses groups 0xf3, 0xf4, 0xf5, and 0xf6. Then link the buttons such that they respond to those groups, and link the modem as a controller for them (see [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal) documentation). In your items file you specify these groups with the "group=" parameters such that the binding knows what group number to put on the outgoing message.


####Keypad switches

**Items**

Here is a simple example, just using the load (main) switch:

    Switch keypadSwitch    "main load" {insteonplm="xx.xx.xx:F00.00.14#loadswitch"}
    Number keypadSwitchManualChange "main manual change" {insteonplm="xx.xx.xx:F00.00.14#loadswitchmanualchange"}
    Switch keypadSwitchFastOnOff    "main fast on/off" {insteonplm="xx.xx.xx:F00.00.14#loadswitchfastonoff,related=xx.xx.xx"}

Most people will not use the fast on/off features or the manual change feature, so you really only need the first line. To make the buttons available, add these lines to your items file:

    Switch keypadSwitchA   "keypad button A"   {insteonplm="xx.xx.xx:F00.00.14#keypadbuttonA,group=0xf3"}
    Switch keypadSwitchB   "keypad button B"   {insteonplm="xx.xx.xx:F00.00.14#keypadbuttonB,group=0xf4"}
    Switch keypadSwitchC   "keypad button C"   {insteonplm="xx.xx.xx:F00.00.14#keypadbuttonC,group=0xf5"}
    Switch keypadSwitchD   "keypad button D"   {insteonplm="xx.xx.xx:F00.00.14#keypadbuttonD,group=0xf6"}

**Sitemap**

The following sitemap will bring the items to life in the GUI:

	Frame label="Keypad" {
	      Switch item=keypadSwitch label="main"
	      Switch item=keypadSwitchFastOnOff label="fast on/off"
	      Switch item=keypadSwitchManualChange label="manual change" mappings=[ 0="DOWN", 1="STOP",  2="UP"]
	      Switch item=keypadSwitchA label="button A"
	      Switch item=keypadSwitchB label="button B"
	      Switch item=keypadSwitchC label="button C"
	      Switch item=keypadSwitchD label="button D"
	}

####Keypad dimmers

The keypad dimmers are like keypad switches, except that the main load is dimmable.

**Items**

    Dimmer keypadDimmer "dimmer" {insteonplm="xx.xx.xx:F00.00.15#loaddimmer"}
    Switch keypadDimmerButtonA    "keypad dimmer button A [%d %%]"	{insteonplm="xx.xx.xx:F00.00.15#keypadbuttonA,group=0xf3"}

**Sitemap**

    Slider item=keypadDimmer switchSupport
    Switch item=keypadDimmerButtonA label="buttonA"


### Thermostats

The thermostat (2441TH) is one of the most complex Insteon devices available. It must first be properly linked to the modem using configuration software like [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal). The Insteon Terminal wiki describes in detail how to link the thermostat, and how to make it publish status update reports.

When all is set and done the modem must be configured as a controller to group 0 (not sure why), and a responder to groups 1-5 such that it picks up when the thermostat switches on/off heating and cooling etc, and it must be a responder to special group 0xEF to get status update reports when measured values (temperature) change. Symmetrically, the thermostat must be a responder to group 0, and a controller for groups 1-5 and 0xEF. The linking process is not difficult but needs some persistence. Again, refer to the [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal) documentation.

**Items**

This is an example of what to put into your .items file:

    Number  thermostatCoolPoint "cool point [%.1f °F]" { insteonplm="32.f4.22:F00.00.18#coolsetpoint" }
    Number  thermostatHeatPoint "heat point [%.1f °F]" { insteonplm="32.f4.22:F00.00.18#heatsetpoint" }
    Number  thermostatSystemMode "system mode [%d]" { insteonplm="32.f4.22:F00.00.18#systemmode" }
    Number  thermostatFanMode "fan mode [%d]" { insteonplm="32.f4.22:F00.00.18#fanmode" }
    Number  thermostatIsHeating "is heating [%d]" { insteonplm="32.f4.22:F00.00.18#isheating"}
    Number  thermostatIsCooling "is cooling [%d]" { insteonplm="32.f4.22:F00.00.18#iscooling"}
    Number  thermostatTempFahren  "temperature [%.1f °F]" { insteonplm="32.f4.22:F00.00.18#tempfahrenheit" }
    Number  thermostatTempCelsius  "temperature [%.1f °C]" { insteonplm="32.f4.22:F00.00.18#tempcelsius" }
    Number  thermostatHumidity "humidity [%.0f %%]" { insteonplm="32.f4.22:F00.00.18#humidity" }

Add this as well for some more exotic features:

    Number  thermostatACDelay "A/C delay [%d min]"  { insteonplm="32.f4.22:F00.00.18#acdelay" }
    Number  thermostatBacklight "backlight [%d sec]" { insteonplm="32.f4.22:F00.00.18#backlightduration" }
    Number  thermostatStage1 "A/C stage 1 time [%d min]" { insteonplm="32.f4.22:F00.00.18#stage1duration" }
    Number  thermostatHumidityHigh "humidity high [%d %%]" { insteonplm="32.f4.22:F00.00.18#humidityhigh" }
    Number  thermostatHumidityLow "humidity low [%d %%]"  { insteonplm="32.f4.22:F00.00.18#humiditylow" }


**Sitemap**

For the thermostat to display in the GUI, add this to the sitemap file:

    Text   item=thermostatTempCelsius icon="temperature"
    Text   item=thermostatTempFahren icon="temperature"
    Text   item=thermostatHumidity
    Setpoint item=thermostatCoolPoint icon="temperature" minValue=63 maxValue=90 step=1
    Setpoint item=thermostatHeatPoint icon="temperature" minValue=50 maxValue=80 step=1
    Switch item=thermostatSystemMode  label="system mode" mappings=[ 0="OFF",  1="HEAT", 2="COOL", 3="AUTO", 4="PROGRAM"]
    Switch item=thermostatFanMode  label="fan mode" mappings=[ 0="AUTO",  1="ALWAYS ON"]
    Switch item=thermostatIsHeating  label="is heating" mappings=[ 0="OFF",  1="HEATING"]
    Switch item=thermostatIsCooling  label="is cooling" mappings=[ 0="OFF",  1="COOLING"]
    Setpoint item=thermostatACDelay  minValue=2 maxValue=20 step=1
    Setpoint item=thermostatBacklight  minValue=0 maxValue=100 step=1
    Setpoint item=thermostatHumidityHigh  minValue=0 maxValue=100 step=1
    Setpoint item=thermostatHumidityLow   minValue=0 maxValue=100 step=1
    Setpoint item=thermostatStage1  minValue=1 maxValue=60 step=1

### Power Meters

The iMeter Solo reports both wattage and kilowatt hours, and is updated during the normal polling process of the devices. You can also manually update the current values from the device and reset the device. See the example below:
 
    Number iMeterWatts   "iMeter [%d watts]"  {insteonplm="xx.xx.xx:F00.00.17#meter,field=watts"}
    Number iMeterKwh     "iMeter [%.04f kwh]" {insteonplm="xx.xx.xx:F00.00.17#meter,field=kwh"}
    Switch iMeterUpdate  "iMeter Update"      {insteonplm="xx.xx.xx:F00.00.17#meter,cmd=update"}
    Switch iMeterReset   "iMeter Reset"       {insteonplm="xx.xx.xx:F00.00.17#meter,cmd=reset"}

### Fan Controllers

Here is an example configuration for a FanLinc module, which has a dimmable light and a variable speed fan:

**Items**

    Dimmer fanLincDimmer   "fanlinc dimmer [%d %%]" {insteonplm="xx.xx.xx:F00.00.1C#lightdimmer"}
    Number fanLincFan      "fanlinc fan" {insteonplm="xx.xx.xx:F00.00.1C#fan"}

**Sitemap**

    Slider item=fanLincDimmer switchSupport
    Switch item=fanLincFan label="fan speed" mappings=[ 0="OFF",  1="LOW", 2="MEDIUM", 3="HIGH"]


### X10 devices

It is worth noting that both the Inseon PLM and the 2014 Hub can both command X10 devices over the powerline, and also set switch stats based on X10 signals received over the powerline.  This allows openHAB not only control X10 devices without the need for other hardwaare, but it can also have rules that react to incoming X10 powerline commands.  While you cannot bind the the X10 devices to the Insteon PLM/HUB, here are some examples for configuring X10 devices. Be aware that most X10 switches/dimmers send no status updates, i.e. openHAB will not learn about switches that are toggled manually. Further note that
X10 devices are addressed with `houseCode.unitCode`, e.g. `A.2`.

    Switch x10Switch	"X10 switch" {insteonplm="A.1:X00.00.01#switch"}
    Dimmer x10Dimmer	"X10 dimmer" {insteonplm="A.5:X00.00.02#dimmer"}
    Contact x10Motion	"X10 motion" {insteonplm="A.3:X00.00.03#contact"}

## Direct sending of group broadcasts (triggering scenes)

The binding can command the modem to send broadcasts to a given Insteon group. Since it is a broadcast message, the corresponding item does *not* take the address of any device, but of the modem itself:

    Switch  broadcastOnOff "group on/off" { insteonplm="xx.xx.xx:0x000045#broadcastonoff,group=2"}

where "xx.xx.xx" stands for the modem's insteon address. Flipping this switch to "ON" will cause the modem to send a broadcast message with group=2, and all devices that are configured to respond to it should react.

## 3-way switch configurations and the "related" keyword

When an Insteon device changes its state because it is directly operated (for example by flipping a switch manually), it sends out a broadcast message to announce the state change, and the binding (if the PLM modem is properly linked as a responder) should update the corresponding openHAB items. Other linked devices however may also change their state in response, but those devices will *not* send out a broadcast message, and so openHAB will not learn about their state change until the next poll. One common scenario is e.g. a switch in a 3-way configuration, with one switch controlling the load, and the other switch being linked as a controller. In this scenario, the "related" keyword can be used to cause the binding to poll a related device whenever a state change occurs for another device. A typical example would be two dimmers (A and B) in a 3-way configuration:

    Dimmer A "dimmer 1" {insteonplm="aa.bb.cc:F00.00.01#dimmer,related=dd.ee.ff"}
    Dimmer B "dimmer 2" {insteonplm="dd.ee.ff:F00.00.01#dimmer,related=aa.bb.cc"}

More than one device can be polled by separating them with "+" sign, e.g. "related=aa.bb.cc+xx.yy.zz" would poll both of these devices. The implemenation of the *related* keyword is simple: if you add it to a feature, and that feature changes its state, then the *related* device will be polled to see if its state has updated.

## Trouble shooting

To get additional debugging information, insert the following into
your `logback.xml` file:

    <appender name="INSTEONPLMFILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${openhab.logdir:-logs}/insteonplm.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${openhab.logdir:-logs}/insteonplm-%d{yyyy-ww}.log.zip</fileNamePattern>
        <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
                <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{30}[:%line]- %msg%n%ex{5}</pattern>
        </encoder>
    </appender>
    <!-- Change DEBUG->TRACE for even more detailed logging -->
    <logger name="org.openhab.binding.insteonplm" level="DEBUG" additivity="false">
    <appender-ref ref="INSTEONPLMFILE" />
    </logger>

This will log additional debugging messages to a separate file in the
log directory.

**NOTE: Configuring logging with OpenHAB 2 is a bit different**
Insert the following into your `org.ops4j.pax.logging.cfg` file:

    # insteonPLM logger configuration
    log4j.logger.org.openhab.binding.insteonplm = DEBUG, insteonplm
    log4j.additivity.org.openhab.binding.insteonplm = false

    # File appender - insteonplm.log
    log4j.appender.insteonplm=org.apache.log4j.RollingFileAppender
    log4j.appender.insteonplm.layout=org.apache.log4j.PatternLayout
    log4j.appender.insteonplm.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %l - %m%n
    log4j.appender.insteonplm.file=${openhab.logdir}/insteonplm.log
    log4j.appender.insteonplm.append=true
    log4j.appender.insteonplm.maxFileSize=10MB
    log4j.appender.insteonplm.maxBackupIndex=10

### Device Permissions / Linux Device Locks

When OpenHAB is running as a non-root user (Linux/OSX) it is important to ensure it has write access not just to the PLM device, but to the os lock directory. Under openSUSE this is `/run/lock` and is managed by the **lock** group. 

Example commands to grant OpenHAB access (adjust for your distribution):
````
usermod -a -G dialout openhab
usermod -a -G lock openhab
````

Insufficient access to the lock directory will result in OpenHAB failing to access the device, even if the device itself is writable.

### Adding new device types (using existing device features)

Device types are defined in the file `device_types.xml`, which is inside the InsteonPLM bundle and thus not visible to the user. You can however load your own device_types.xml by referencing it in the openhab.cfg file like so:

    insteonplm:more_devices=/usr/local/openhab/rt/my_own_devices.xml

Where the `my_own_devices.xml` file defines a new device like this:

    <xml>
     <device productKey="F00.00.XX">
      <model>2456-D3</model>
      <description>LampLinc V2</description>
      <feature name="dimmer">GenericDimmer</feature>
      <feature name="lastheardfrom">GenericLastTime</feature>
     </device>
    </xml>

Finding the Insteon product key can be tricky since Insteon has not updated the product key table (http://www.insteon.com/pdf/insteon_devcats_and_product_keys_20081008.pdf) since 2008. If a web search does not turn up the product key, make one up, starting with "F", like: F00.00.99. Avoid duplicate keys by finding the highest fake product key in the `device_types.xml` file, and incrementing by one.

### Adding new device features

If you can't can't build a new device out of the existing device features (for a complete list see `device_features.xml`) you can add new features by specifying a file (let's call it `my_own_features.xml`) with the "more_devices" option in the `openhab.cfg` file:

    insteonplm:more_features=/usr/local/openhab/rt/my_own_features.xml

  In this file you can define your own features (or even overwrite an existing feature). In the example below a new feature "MyFeature" is defined, which can then be referenced from the `device_types.xml` file (or from `my_own_devices.xml`):

    <xml>
     <feature name="MyFeature">
	 <message-dispatcher>DefaultDispatcher</message-dispatcher>
	 <message-handler cmd="0x03">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x06">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x11">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x13">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x19">LightStateSwitchHandler</message-handler>
	 <command-handler command="OnOffType">IOLincOnOffCommandHandler</command-handler>
	 <poll-handler>DefaultPollHandler</poll-handler>
     </feature>
    </xml>

If you cannot cobble together a suitable device feature out of existing handlers you will have to define new ones by editing the corresponding Java classes in the source tree (see below).

### Adding new handlers (for developers experienced with Eclipse IDE)

If all else fails there are the Java sources, in particular the classes MessageHandler.java (what to do with messages coming in from the Insteon network), PollHandler.java (how to form outbound messages for device polling), and CommandHandler.java (how to translate openhab commands to Insteon network messages). To that end you'll need to become a bonafide openHAB developer, and set up an openHAB Eclipse build environment, following the online instructions. Before you write new handlers have a good look at the existing ones, they are quite flexible and configurable via parameters in `device_features.xml`.

## Known Limitations and Issues

1. Devices cannot be linked to the modem while the binding is
running. If new devices are linked, the binding must be restarted.
2. Setting up Insteon groups and linking devices cannot be done from within openHAB. Use the [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal) for that. If using Insteon Terminal (especially as root), ensure any stale lock files (For example, /var/lock/LCK..ttyUSB0) are removed before starting OpenHAB runtime. Failure to do so may result in "found no ports".
3. Very rarely during binding startup, a message arrives at the modem while the initial read of the modem
database happens. Somehow the modem then stops sending the remaining link records and the binding no longer is able to address the missing devices. The fix is to simply restart the binding.
4. The Insteon PLM device is know to break after about 2-3 years due to poorly sized capacitors of the power supply. With a bit of soldering skill you can repair it yourself, see http://pfrommer.us/home-automation or the original thread: http://forum.universal-devices.com/topic/13866-repair-of-2413s-plm-when-the-power-supply-fails/.
5. Using the Insteon Hub 2014 in conjunction with other applications (such as the InsteonApp) is not supported. Concretely, OpenHab will not learn when a switch is flipped via the Insteon App until the next poll, which could take minutes.

### Device Permissions / Linux Device Locks

When OpenHAB is running as a non-root user (Linux/OSX) it is important to ensure it has write access not just to the PLM device, but to the os lock directory. Under openSUSE this is `/run/lock` and is managed by the **lock** group. 

Example commands to grant OpenHAB access (adjust for your distribution):
````
usermod -a -G dialout openhab
usermod -a -G lock openhab
````

Insufficient access to the lock directory will result in OpenHAB failing to access the device, even if the device itself is writable.

### Adding new device types (using existing device features)

Device types are defined in the file `device_types.xml`, which is inside the InsteonPLM bundle and thus not visible to the user. You can however load your own device_types.xml by referencing it in the openhab.cfg file like so:

    insteonplm:more_devices=/usr/local/openhab/rt/my_own_devices.xml

Where the `my_own_devices.xml` file defines a new device like this:

    <xml>
     <device productKey="F00.00.XX">
      <model>2456-D3</model>
      <description>LampLinc V2</description>
      <feature name="dimmer">GenericDimmer</feature>
      <feature name="lastheardfrom">GenericLastTime</feature>
     </device>
    </xml>

Finding the Insteon product key can be tricky since Insteon has not updated the product key table (http://www.insteon.com/pdf/insteon_devcats_and_product_keys_20081008.pdf) since 2008. If a web search does not turn up the product key, make one up, starting with "F", like: F00.00.99. Avoid duplicate keys by finding the highest fake product key in the `device_types.xml` file, and incrementing by one.

### Adding new device features

If you can't can't build a new device out of the existing device features (for a complete list see `device_features.xml`) you can add new features by specifying a file (let's call it `my_own_features.xml`) with the "more_devices" option in the `openhab.cfg` file:

    insteonplm:more_features=/usr/local/openhab/rt/my_own_features.xml

  In this file you can define your own features (or even overwrite an existing feature). In the example below a new feature "MyFeature" is defined, which can then be referenced from the `device_types.xml` file (or from `my_own_devices.xml`):

    <xml>
     <feature name="MyFeature">
	 <message-dispatcher>DefaultDispatcher</message-dispatcher>
	 <message-handler cmd="0x03">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x06">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x11">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x13">NoOpMsgHandler</message-handler>
	 <message-handler cmd="0x19">LightStateSwitchHandler</message-handler>
	 <command-handler command="OnOffType">IOLincOnOffCommandHandler</command-handler>
	 <poll-handler>DefaultPollHandler</poll-handler>
     </feature>
    </xml>

If you cannot cobble together a suitable device feature out of existing handlers you will have to define new ones by editing the corresponding Java classes in the source tree (see below).

### Adding new handlers (for developers experienced with Eclipse IDE)

If all else fails there are the Java sources, in particular the classes MessageHandler.java (what to do with messages coming in from the Insteon network), PollHandler.java (how to form outbound messages for device polling), and CommandHandler.java (how to translate openhab commands to Insteon network messages). To that end you'll need to become a bonafide openHAB developer, and set up an openHAB Eclipse build environment, following the online instructions. Before you write new handlers have a good look at the existing ones, they are quite flexible and configurable via parameters in `device_features.xml`.

## Known Limitations and Issues

1. Devices cannot be linked to the modem while the binding is
running. If new devices are linked, the binding must be restarted.
2. Setting up Insteon groups and linking devices cannot be done from within openHAB. Use the [Insteon Terminal](https://github.com/pfrommerd/insteon-terminal) for that. If using Insteon Terminal (especially as root), ensure any stale lock files (For example, /var/lock/LCK..ttyUSB0) are removed before starting OpenHAB runtime. Failure to do so may result in "found no ports".
3. Very rarely during binding startup, a message arrives at the modem while the initial read of the modem
database happens. Somehow the modem then stops sending the remaining link records and the binding no longer is able to address the missing devices. The fix is to simply restart the binding.
4. The Insteon PLM device is know to break after about 2-3 years due to poorly sized capacitors of the power supply. With a bit of soldering skill you can repair it yourself, see http://pfrommer.us/home-automation or the original thread: http://forum.universal-devices.com/topic/13866-repair-of-2413s-plm-when-the-power-supply-fails/.
5. Using the Insteon Hub 2014 in conjunction with other applications (such as the InsteonApp) is not supported. Concretely, OpenHab will not learn when a switch is flipped via the Insteon App until the next poll, which could take minutes.