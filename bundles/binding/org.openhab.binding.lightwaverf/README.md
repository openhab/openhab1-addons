## LightwaveRF Binding

This binding supports the LightwaveRF products using the LightwaveRF Wifi Link that is available as part of that range.

## Binding Configuration

This binding can be configured in the file `services/lightwaverf.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         |   Yes    | The IP Address of the LightwaveRf Wifi Link. you can use the broadcast address 255.255.255.255 |
| receiveport | 9760 |   No     | The port to monitor for messages, you shouldn't need to change this |
| sendport | 9761    |   No     | The port to send messages on, it will also be monitored for incoming messages, you shouldn't need to change this |
| registeronstartup | false | No | For a new computer you will need to register it with the wifi link to be allowed to send messages.  Setting this to `true` we will send a registration message on startup. You will need to confirm registration on the wifi link. There is no harm leaving this as true but you can set to `false` once you have registerd for the first time. |
| senddelay |  2000 (?TBD) | ?  | Delay between sending messages in ms to avoid swapming Wifi Link |
| okTimeout | 1000 (?TBD) |  ?  | Timeout for OK Messages in ms, we will retry messages we don't receive an ok for in the timeout |

## Item Configuration

To configure items, you first need to add the devices using the LightwaveRF App on iPhone or Android. You can add switches, dimmers and radiator valves.

Once you have done this, you need to find out some identifiers to allow the devices to be added to Openhab as below. 

| Device | Identifier Required | 
|--------|---------------------|
| Light Switch | RoomId and DeviceId |
| Dimmer | RoomId and DeviceId |
| Radiator Valve | RoomId and Serial Number |
| Energy Monitor | Serial Number |
| Relay | RoomId and DeviceId |
| WifiLink | Serial set to "wifilink" |
| Mood (this isn't the Mood Switch but moods setup in the LightwaveRF App) | Room Id |
| All Off | Room Id |

To do this, sniff your network using something like wireshark whilst sending commands using the smartphone application. You should see messages like "100,!R1D3F1"

This means that lightwave switch/dimmer roomId = "1" and device id = "1"

For thermostats you will see a message like "100,!R1F*r" this means the room id = "1". There will then be a return message that looks like JSON with the following in it... "serial":"123DEF" this means the serial id = "123DEF".

The LightwaveRF binding works on the concept of giving each item a type. This will determine the value that item is loaded with when an update is received.

Valid types are

| LightwaveRF Type | openHAB Type | Read/Write | Devices | From Version |
| DIMMER | Dimmer | Read/Write | Dimmer | 1.7.0 |
| SWITCH | Switch | Read/Write | Switch | 1.7.0 |
| MOOD | Number | Read/Write | Setup in LightwaveRF App | 1.9.0 |
| ALL_OFF | Switch | Read/Write | Virtual Device | 1.9.0 |
| UPDATETIME | DateTime | Read | Energy Monitor, Radiator Valves | 1.9.0 (was called HEATING_UPDATETIME in 1.7.0) |
| SIGNAL | Number | Read | Energy Monitor | 1.9.0 (was called HEATING_SIGNAL in 1.7.0, removed from Radiator valve in 1.9.0) |
| HEATING_CURRENT_TEMP | Number | Read | Radiator Valves | 1.7.0 |
| HEATING_BATTERY | Number | Read | Radiator Valves | 1.7.0 |
| HEATING_SET_TEMP | Number | Write | Radiator Valves | 1.7.0 |
| HEATING_MODE | String | Read | Radiator Valves | 1.7.0 |
| HEATING_OUTPUT | Percent | Read | Radiator Valves | 1.9.0 |
| ENERGY_YESTERDAY_USAGE | Number | Read | Energy Monitor | 1.9.0 |
| ENERGY_TODAY_USAGE | Number | Read | Energy Monitor | 1.9.0 |
| ENERGY_MAX_USAGE | Number | Read | Energy Monitor | 1.9.0 |
| ENERGY_CURRENT_USAGE | Number | Read | Energy Monitor | 1.9.0 |
| RELAY | Number (-1 Close, 0 Stop, 1 Open) | Read/Write | Relay | 1.9.0 |
| WIFILINK_IP | String | Read | Wifi Link | 1.9.0 |
| WIFILINK_DUSK_TIME | DateTime | Read | Wifi Link | 1.9.0 |
| WIFILINK_DAWN_TIME | DateTime | Read | Wifi Link | 1.9.0 |
| WIFILINK_UPTIME | Number | Read | Wifi Link | 1.9.0 |
| WIFILINK_LONGITUDE | String | Read | Wifi Link | 1.9.0 |
| WIFILINK_LATITUDE | String | Read | Wifi Link | 1.9.0 |
| WIFILINK_FIRMWARE | String | Read | Wifi Link | 1.9.0 |

## Force WiFi Link to register your device

Occasionally (perhaps only on the older models?), the WiFi Link will fail to ask you to confirm a new registration, regardless of the setting in openhab.cfg.

If this is the case you'll get an error like this in `openhab.log`:

```
Error converting message: 200,ERR,1,"Not yet registered. Send !F*p to register"
```

To force the WiFi Link to register your new device paste this into a shell, making sure to set the IP address of the WiFi Link correctly. Then head over to the WiFi Link so you can press 'OK'.

```
echo -ne '001,!F*p|' | nc -w1 -u 192.168.x.xx 9760
```

### Radiator Valve Polling 

This binding will receive updates from the radiator valves whenever they are sent. However if you want a consistent update then you can add a poll_time to send a poll message at regular intervals. 

See below for a typical example configuration. Note the poll_time is in seconds 1800 seconds = 30 minutes in the example below. You only need to set the poll time on one item per radiator (I recommend the current temperature as per below). A poll will actually update all values.

Examples, configure for your items:

```
Dimmer  Dimmer1 "Dimmer1 [%d %%]" (ALL) { lightwaverf="room=3,device=2,type=DIMMER" }
Switch  Switch2 "Switch2" (ALL)         { lightwaverf="room=3,device=3,type=SWITCH" }

Number RadiatorCTemp "Radiator [%.1f C]"  { lightwaverf="room=4,serial=BF3B01,type=HEATING_CURRENT_TEMP,poll=1800" }
Number RadiatorSTemp "Radiator Set Temp [%.1f C]"  { lightwaverf="room=4,serial=BF3B01,type=HEATING_SET_TEMP" }
Number RadiatorBatt "Radiator Battery [%.2f]"     { lightwaverf="room=4,serial=BF3B01,type=HEATING_BATTERY" }
String RadiatorMode "Radiator [%s]"     { lightwaverf="room=4,serial=BF3B01,type=HEATING_MODE" }
DateTime RadiatorUpdated "Radiator Updated [%1$tT, %1$tF]"  { lightwaverf="room=4,serial=AF4A02,type=HEATING_UPDATETIME" }
```
