# XBMC binding (for KODI) (1.x)

This binding allows openHAB items to receive realtime updates about information like player state and running media from one or more instances of KODI (formerly known as XBMC).

[![openhab binding for xbmc](http://img.youtube.com/vi/N7_5phTVbo0/0.jpg)](http://www.youtube.com/watch?v=N7_5phTVbo0)

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/kodi/).

<!-- MarkdownTOC -->

- [Prerequisites](#prerequisites)
- [Binding Configuration](#binding-configuration)
    - [Configuration Example](#configuration-example)
- [Item Configuration](#item-configuration)
    - [Available properties](#available-properties)
- [Examples](#examples)
    - [Basic Items](#basic-items)
    - [Rule Example](#rule-example)
    - [XBMC with Wake-On-LAN and Network Health Bindings](#xbmc-with-wake-on-lan-and-network-health-bindings)
    - [Using Player.Open](#using-playeropen)
    - [Using Application.Volume option](#using-applicationvolume-option)
    - [Setting the Volume from a Rule](#setting-the-volume-from-a-rule)
    - [Using Screensaver.State](#using-screensaverstate)
    - [Using PVR.OpenTV](#using-pvropentv)

<!-- /MarkdownTOC -->


## Prerequisites

This binding has been developed and tested from XBMC Frodo (v12) to Kodi Jarvis (v16.1).

The binding uses XBMCs JSON-RPC API v6, both the HTTP and the websocket transports. These therefore need to be enabled and need to be reachable from your openHAB host (think firewall, routing etc.)

## Binding Configuration

This binding can be configured in the file `services/xbmc.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<instance>`.host | |   Yes   | Hostname / IP address of your XBMC host |
| `<instance>`.rsPort | 8080 | No | Port number for the JSON RPC service |
| `<instance>`.wsPort | 9090 | No | Port number for the websocket service |
| `<instance>`.username | xbmc | No | Username to connect to XBMC |
| `<instance>`.password| xbmc | No | Password to connect to XBMC |
| refreshInterval  | 60000 | No | Refresh interval, in milliseconds (60000 is one minute) |

where `<instance>` is a name you choose for the instance of XBMC you want to control, `livingroom` for instance.  When defining your items, you can use the `<instance>` name to refer to your instances. In doing so, you can easily change the address by which you XBMC instance can be reached without having to reconfigure all of your items. 

### Configuration Example

```
livingRoom.host=192.168.1.6
livingRoom.rsPort=8080
livingRoom.wsPort=9090
livingRoom.username=xbmc
livingRoom.password=xbmc
refreshInterval=60000
```

Although you *can* configure one or more instances, it is not strictly necessary to do so. As long as you don't intend to use ports or login data different from the default, you could just as well use this by directly referring to hostnames or IP addresses in you item binding configuration.

## Item Configuration

Items can be bound to the XBMC binding using this format:

```
xbmc="<direction>[<host>|<property>]"
```

where:

* `<direction>` is `<` for incoming (player state, media title etc.), `>` for outgoing (Send "Play/Pause" or "Stop" etc.) or `=` for bi-directional (send/receive volume)
* `<host>` is a named `<instance>` prefixed with `#`, or without the `#` prefix it's interpreted as a hostname or IP address.
* `<property>` is the property to bind to (more on this below)

### Available properties

The list of properties you can use in your binding configuration is:

Property                             | Direction  | Description
-------------------------------------|:----------:| ------------
Application.Volume                   | `=`        | Show and adjust the volume in the XBMC application
GUI.ShowNotification                 | `>`        | Show a notification in the XBMC UI
Input.ExecuteAction.aspectratio      | `>`        | Changes Aspect ratio
Input.ExecuteAction.back             | `>`        | Moves cursor BACK
Input.ExecuteAction.codecinfo        | `>`        | Shows codec information
Input.ExecuteAction.contextmenu      | `>`        | Shows context menu
Input.ExecuteAction.down             | `>`        | Moves cursor RIGHT
Input.ExecuteAction.info             | `>`        | Shows INFO screen 
Input.ExecuteAction.left             | `>`        | Moves cursor LEFT
Input.ExecuteAction.osd              | `>`        | Shows OSD
Input.ExecuteAction.right            | `>`        | Moves cursor RIGHT
Input.ExecuteAction.select           | `>`        | Moves cursor RIGHT
Input.ExecuteAction.togglefullscreen | `>`        | Toggles full screen / Windowed screen
Input.ExecuteAction.up               | `>`        | Moves cursor UP 
Input.ExecuteAction.????             | `>`        | Any other command from [this list](http://kodi.wiki/view/JSON-RPC_API/v6#Input.Action)
Label.Player.Time                    | `<`        | Display media time 
Label.System.Uptime                  | `<`        | Display system uptime
Label.VideoPlayer.VideoCodec         | `<`        | Display video codec
Label.????.????                      | `<`        | Any other command from [this list](http://kodi.wiki/view/InfoLabels)
Player.Album                         | `<`        | Currently playing album (music only)
Player.Artist                        | `<`        | Currently playing artist (music only)
Player.Label                         | `<`        | Currently playing title (in case of radio streams) 
Player.Open                          | `>`        | Open a URL for playback
Player.PlayPause                     | `>`        | Play/pause playback
Player.ShowTitle                     | `<`        | Currently playing show title: TV show name, empty for other types
Player.State                         | `<`        | Current player state: 'Play', 'Pause', 'End' (Stopped due to end of content), 'Stop' (Stopped by user). An 'End' state is immediately followed by a 'Stop' state.
Player.Stop                          | `>`        | Stop playback
Player.Title                         | `<`        | Currently playing title: movie name, TV episode name, music track name
Player.Type                          | `<`        | Currently playing type: 'movie', 'episode', 'channel', or 'unknown' for a PVR recording
Player.<???>                         | `<`        | Any other player property supported by the XBMC JSON RPC API
Property.audiostreams                | `<`        | Returns the list of all audiostreams of the media
Property.canchangespeed              | `<`        | Returns "true" if the media speed can be changed
Property.canmove                     | `<`        | Returns "true" if the media can be moved
Property.canrepeat                   | `<`        | Returns "true" if the media can be repeated
Property.canrotate                   | `<`        | Returns "true" if the media can be rotated
Property.canseek                     | `<`        | Returns "true" if the media can be seeked
Property.canshuffle                  | `<`        | Returns "true" if the playlist can be shuffled
Property.canzoom                     | `<`        | Returns "true" if the media can be zoomed
Property.currentaudiostream          | `<`        | Returns the current audio stream
Property.currentsubtitle             | `<`        | Gets the current subtitle language of the playing media
Property.percentage                  | `<`        | Gets the elapsed percentage of the playing media 
Property.position                    | `<`        | Returns the current position
Property.repeat                      | `<`        | Returns "true" if the media can be repeated
Property.shuffled                    | `<`        | Returns "true" if the playlist is shuffled
Property.speed                       | `<`        | Returns speed multiplier of the current playing media (1x, 2x, etc.)
Property.subtitleenabled             | `<`        | Returns "true" if the subtitles are enabled
Property.subtitles                   | `<`        | Return the current subtitle
Property.time                        | `<`        | Gets the elapsed time of the playing media
Property.totalTime                   | `<`        | Gets the total time of the playing media 
Property.type                        | `<`        | Gets the media type
Property.????                        | `<`        | Any other property from [this list](http://kodi.wiki/view/JSON-RPC_API/v6#Player.Property.Name) 
PVR.OpenRadio                        | `>`        | Open a PVR Radio Channel for playback
PVR.OpenTV                           | `>`        | Open a PVR TV Channel by name for playback
Refresh                              | `>`        | Force refresh of incoming properties
Screensaver.State                    | `<`        | Current screensaver state: (ON if screensaver active)
System.Hibernate                     | `>`        | Send OFF to put the system in Hibernate mode
System.Reboot                        | `>`        | Send OFF to reboot the system
System.Shutdown                      | `>`        | Send OFF to power down the system
System.State                         | `<`        | State of the XBMC application (ON if connected, OFF is XBMC unavailable)
System.Suspend                       | `>`        | Send OFF to put the system in Suspend mode

Incoming properties that are not part of any XBMC notification, will be refreshed with each refresh interval of the binding (default 60 seconds). 

## Examples

### Basic Items

```
String XBMC_LR_PlayerState  "XBMC String" {xbmc="<[#livingRoom|Player.State]"}
String XBMC_LR_PlayerTitle  "XBMC String" {xbmc="<[#livingRoom|Player.Title]"}

String XBMC_BR_PlayerState  "XBMC String" {xbmc="<[#bedroom|Player.State]"}

String XBMC_IP_PlayerState  "XBMC String" {xbmc="<[192.168.1.6|Player.State]"}
String XBMC_DNS_PlayerState "XBMC String" {xbmc="<[xbmc.local|Player.Title]"}

Number XBMC_LR_Volume       "XBMC Number" {xbmc="=[#livingRoom|Application.Volume]"} 
```

### Rule Example

As can be seen in the introduction video, i have a dimmable lamp next to my tv. I use this to light up the room after sunset when i press pause and go back to the previous level on resume, unless the lamp state has been altered while on pause.

This is of course an example very specific to my preferences, but should give you an idea on what can be done.

```
import java.util.Date

var Number brightnessBeforePause

rule "Lights on when paused"
when
        Item LivingRoom_XBMC changed from Play to Pause
then
        var Date sunsetTime = (G_SUNSET_V.state as DateTimeType).calendar.time
        var Date sunriseTime = (G_SUNRISE_V.state as DateTimeType).calendar.time
        if( now.toDate().after(sunsetTime) || now.toDate().before(sunriseTime)){
                brightnessBeforePause = LivingRoom_Lamp.state as DecimalType
                if ( brightnessBeforePause < 50){
                        sendCommand( LivingRoom_Lamp, "50")     
                }
        }
end

rule "Lights off when pause end"
when
        Item LivingRoom_XBMC changed from Pause to Play
then
        if (LivingRoom_Lamp.state == 50){               
                sendCommand( LivingRoom_Lamp, brightnessBeforePause)
        }
end
```

### XBMC with Wake-On-LAN and Network Health Bindings

The following configuration can be used to power on your XBMC machine using the Wake-On-LAN Binding and power it off using the XBMC Binding. The Network Health binding updates the state after some time if XBMC was powered on or off without using openHAB. You have to insert your own IP and MAC address of course.

```
Switch XBMC_Power "XBMC Power" {xbmc=">[#openelec|System.Shutdown]", wol="192.168.1.0#xx:xx:xx:xx:xx:xx", nh="openelec:80"}
```

### Using Player.Open

To use the Player.Open command, you would need to define a String item with this binding, then send an Update to this String item. This will cause Openhab to send the Player.Open command to the specified XBMC instance:

Items:

```
String XBMC_OpenMedia    "XBMC Player file [%s]"  { xbmc=">[#woonkamer|Player.Open]" }
Switch XBMC_Radio        "XBMC Start Radio"       
```

Rule:

```
rule "Start Radio"
when
    Item XBMC_Radio changed to ON
then
    postUpdate(XBMC_Radio, OFF)
    sendCommand(XBMC_OpenMedia, "http://icecast.omroep.nl/3fm-bb-mp3")
end
```

This will introduce a Switch item with an associated rule that will send the `Player.Open` command when the switch is pressed. 

### Using Application.Volume option

Item:

```
Dimmer XBMC_Volume "XBMC Volume [%.1f]" (XBMC) { xbmc="=[#woonkamer|Application.Volume]" }
```

Sitemap:

```
Slider item=XBMC_Volume step=10
```

### Setting the Volume from a Rule

```
XBMC_Volume.sendCommand(50)
```

### Using Screensaver.State

Item:

```
Switch LG_Screensaver "XBMC Screensaver Living Room" {xbmc="<[#woonkamer|Screensaver.State]"}
```

Rule:

```
rule "turn tv on or off"
when
    Item LG_Screensaver changed
then
    if(LG_Screensaver.state == ON)
    {
        logInfo("screensaver", "turn tv OFF")
        sendCommand(TV_Power,OFF)
    } else {
        logInfo("screensaver", "turn tv ON")
        sendCommand(TV_Power,ON)
    }
end
```

### Using PVR.OpenTV

Item:

```
String LG_OpenTVChannel "Open TV Channel [%s]"  { xbmc=">[#woonkamer|PVR.OpenTV]" }
```

Rule:

```
rule "Voice Command open tv channel"
 when
    Item VoiceCommand received command
 then
    sendCommand(LG_OpenTVChannel,VoiceCommand.state.toString)
end
```
