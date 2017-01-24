## Introduction

This binding supports the LightwaveRF products using the LightwaveRF Wifi Link that is available as part of that range.

## Binding Configuration

First you need to configure the following values in the openhab.cfg file (in the folder '${openhab_home}/configurations'). The defaults should suffice unless you know what you are doing.

    ################################### LightwaveRf Binding #####################################
    #
    # The IP Address of the LightwaveRf Wifi Link you can use the broadcast address (required)
    lightwaveRf:ip=255.255.255.255
    # The port to monitor for messages you shouldn't need to change this
    lightwaveRf:receiveport=9760
    # The port to send messages on, it will also be monitored for incoming messages 
    # you shouldn't need to change this
    lightwaveRf:sendport=9761
    # For a new computer you will need to register it with the wifi link to be allowed to send messages
    # setting this to true we will send a registration message on startup. You will need to confirm
    # registration on the wifi link. There is no harm leaving this as true but you can set to false
    # once you have registerd for the first time.
    lightwaverf:registeronstartup=true
    # Delay between sending messages in ms to avoid swapming Wifi Link
    lightwaverf:senddelay=2000
    # Timeout for OK Messages in ms, we will retry messages we don't receive an ok for in the timeout
    lightwaverf:okTimeout=1000

## Item Binding Configuration

To configure the items you first need to add the devices using the LightwaveRF App on iPhone or Android. You can add switches, dimmers and radiator valves.

Once you have done this you need to find out some identifiers to allow the devices to be added to Openhab as below. 

<table>
<tr><td><b>Device</b></td><td><b>Identifier Required</b></td></tr> 
<tr><td>Light Switch</td><td>RoomId and DeviceId</td>
<tr><td>Dimmer</td><td>RoomId and DeviceId</td>
<tr><td>Radiator Valve</td><td>RoomId and Serial Number</td>
<tr><td>Energy Monitor</td><td>Serial Number</td>
<tr><td>Relay</td><td>RoomId and DeviceId</td>
<tr><td>WifiLink</td><td>Serial set to "wifilink"</td>
<tr><td>Mood (this isn't the Mood Switch but moods setup in the LightwaveRF App)</td><td>Room Id</td>
<tr><td>All Off</td><td>Room Id</td>
</table>

To do this sniff your network using something like wireshark whilst sending commands using the smartphone application. You should see messages like "100,!R1D3F1"

This means that lightwave switch/dimmer roomId = "1" and device id = "1"

For thermostats you will see a message like "100,!R1F*r" this means the room id = "1". There will then be a return message that looks like JSON with the following in it... "serial":"123DEF" this means the serial id = "123DEF".

The LightwaveRF binding works on the concept of giving each item a type. This will determine the value that item is loaded with when an update is received.

Valid types are

<table>
<tr><td><b>LightwaveRF Type</b></td><td><b>OpenhabType</b></td><td><b>Read/Write</b></td><td><b>Devices</b></td><td><b>From Version</b></td></tr>
<tr><td>DIMMER</td><td>Dimmer</td><td>Read/Write</td><td>Dimmer</td><td>1.7.0</td>
<tr><td>SWITCH</td><td>Switch</td><td>Read/Write</td><td>Switch</td><td>1.7.0</td>
<tr><td>MOOD</td><td>Number</td><td>Read/Write</td><td>Setup in LightwaveRF App</td><td>1.9.0</td>
<tr><td>ALL_OFF</td><td>Switch</td><td>Read/Write</td><td>Virtual Device</td><td>1.9.0</td>
<tr><td>UPDATETIME</td><td>DateTime</td><td>Read</td><td>Energy Monitor, Radiator Valves</td><td>1.9.0 (was called HEATING_UPDATETIME in 1.7.0)</td>
<tr><td>SIGNAL</td><td>Number</td><td>Read</td><td>Energy Monitor</td><td>1.9.0 (was called HEATING_SIGNAL in 1.7.0, removed from Radiator valve in 1.9.0)</td>
<tr><td>HEATING_CURRENT_TEMP</td><td>Number</td><td>Read</td><td>Radiator Valves</td><td>1.7.0</td>
<tr><td>HEATING_BATTERY</td><td>Number</td><td>Read</td><td>Radiator Valves</td><td>1.7.0</td>
<tr><td>HEATING_SET_TEMP</td><td>Number</td><td>Write</td><td>Radiator Valves</td><td>1.7.0</td>
<tr><td>HEATING_MODE</td><td>String</td><td>Read</td><td>Radiator Valves</td><td>1.7.0</td>
<tr><td>HEATING_OUTPUT</td><td>Percent</td><td>Read</td><td>Radiator Valves</td><td>1.9.0</td>
<tr><td>ENERGY_YESTERDAY_USAGE</td><td>Number</td><td>Read</td><td>Energy Monitor</td><td>1.9.0</td>
<tr><td>ENERGY_TODAY_USAGE</td><td>Number</td><td>Read</td><td>Energy Monitor</td><td>1.9.0</td>
<tr><td>ENERGY_MAX_USAGE</td><td>Number</td><td>Read</td><td>Energy Monitor</td><td>1.9.0</td>
<tr><td>ENERGY_CURRENT_USAGE</td><td>Number</td><td>Read</td><td>Energy Monitor</td><td>1.9.0</td>
<tr><td>RELAY</td><td>Number (-1 Close, 0 Stop, 1 Open)</td><td>Read/Write</td><td>Relay</td><td>1.9.0</td>
<tr><td>WIFILINK_IP</td><td>String</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
<tr><td>WIFILINK_DUSK_TIME</td><td>DateTime</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
<tr><td>WIFILINK_DAWN_TIME</td><td>DateTime</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
<tr><td>WIFILINK_UPTIME</td><td>Number</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
<tr><td>WIFILINK_LONGITUDE</td><td>String</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
<tr><td>WIFILINK_LATITUDE</td><td>String</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
<tr><td>WIFILINK_FIRMWARE</td><td>String</td><td>Read</td><td>Wifi Link</td><td>1.9.0</td>
</table>

## Force WiFi Link to register your device

Occasionally (perhaps only on the older models?), the WiFi Link will fail to ask you to confirm a new registration, regardless of the setting in openhab.cfg.

If this is the case you'll get an error like this in openhab.log

```
Error converting message: 200,ERR,1,"Not yet registered. Send !F*p to register"
```

To force the WiFi Link to register your new device paste this into a shell, making sure to set the IP address of the WiFi Link correctly. Then head over to the WiFi Link so you can press 'OK'.

```
echo -ne '001,!F*p|' | nc -w1 -u 192.168.x.xx 9760
```

## Radiator Valve Polling 

This binding will receive updates from the Radiator Valves whenever they are sent. However if you want a consistent update then you can add a poll_time to send a poll message at regular intervals. 

See below for a typical example configuration. Note the poll_time is in seconds 1800 seconds = 30 minutes in the example below. You only need to set the poll time on one item per radiator (I recommend the current temperature as per below). A poll will actually update all values.

Examples, configure for your items:
================

    Dimmer  Dimmer1 "Dimmer1 [%d %%]" (ALL) { lightwaverf="room=3,device=2,type=DIMMER" }
    Switch  Switch2 "Switch2" (ALL)         { lightwaverf="room=3,device=3,type=SWITCH" }
    
    Number RadiatorCTemp "Radiator [%.1f C]"  { lightwaverf="room=4,serial=BF3B01,type=HEATING_CURRENT_TEMP,poll=1800" }
    Number RadiatorSTemp "Radiator Set Temp [%.1f C]"  { lightwaverf="room=4,serial=BF3B01,type=HEATING_SET_TEMP" }
    Number RadiatorBatt "Radiator Battery [%.2f]"     { lightwaverf="room=4,serial=BF3B01,type=HEATING_BATTERY" }
    String RadiatorMode "Radiator [%s]"     { lightwaverf="room=4,serial=BF3B01,type=HEATING_MODE" }
    DateTime RadiatorUpdated "Radiator Updated [%1$tT, %1$tF]"  { lightwaverf="room=4,serial=AF4A02,type=HEATING_UPDATETIME" }

================