# Onkyo AV Receiver Binding

This binding is compatible with Onkyo AV receivers which support ISCP (Integra Serial Control Protocol) over Ethernet (eISCP) and serial ports.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/onkyo/).

## Binding Configuration

This binding can be configured in the file `services/onkyo.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<id1>`.host | | if `<id1>`.serialPortName is not specified | IP address of the first Onkyo AV receiver to control |
| `<id1>`.port | 60128 | No | TCP port address of the first Onkyo to control |
| `<id1>`.serialPortName | | if `<id1>`.host is not specified | name of the serial port device through which to control the first Onkyo receiver |
| `<id2>`.host | | if `<id2>`.serialPortName is not specified | IP address of the second Onkyo device to control |
| `<id2>`.port | 60128 | No | TCP port address of the second Onkyo to control |
| `<id2>`.serialPortName | | if `<id2>`.host is not specified | name of the serial port device through which to control the first Onkyo receiver |

where `<idN>` is a unique name you choose to identify the specific Onkyo receiver you wish to control (for example, `hometheater`).  This name will be used in item configurations.

### Examplea

```
hometheater.host=192.168.1.100
hometheater.port=60128
```

```
hometheater.serialPortName=/dev/ttyUSB0
```

## Item Configuration

The syntax of the binding configuration strings accepted is the following:

```
onkyo="<openHAB-command>:<id>:<device-command>[,<openHAB-command>:<device-id>:<device-command>][,...]"
```

where:

* parts in brackets `[]` signify optional information.
* `<openHAB-command>` corresponds to an openHAB command like `ON`, `OFF`, `DECREASE`, etc.
* `<id>` corresponds to the unique name which you introduced in the binding configuration.
* `<device-command>` corresponds to the Onkyo AV receiver command. See complete list below.

Examples, how to configure your items:

```
Switch onkyoPower  {onkyo="ON:hometheater:POWER_ON, OFF:hometheater:POWER_OFF"}
Dimmer onkyoVolume {onkyo="INCREASE:hometheater:VOLUME_UP, DECREASE:hometheater:VOLUME_DOWN"}
```

### List of predefined Onkyo AV receiver commands

- **Main zone**
  - _Power_
    - POWER_OFF
    - POWER_ON
    - POWER_QUERY
  - _Mute_
    - UNMUTE
    - MUTE
    - MUTE_QUERY
  - _Volume_
    - VOLUME_UP
    - VOLUME_DOWN
    - VOLUME_QUERY
    - VOLUME_SET
    - SET_VOLUME
    - VOLUME
  - _Source_
    - SOURCE_DVR
    - SOURCE_VCR
    - SOURCE_SATELLITE
    - SOURCE_CABLE
    - SOURCE_GAME
    - SOURCE_AUXILIARY
    - SOURCE_AUX
    - SOURCE_VIDEO5
    - SOURCE_AUX2
    - SOURCE_COMPUTER
    - SOURCE_PC
    - SOURCE_BLURAY
    - SOURCE_DVD
    - SOURCE_TAPE1
    - SOURCE_TAPE2
    - SOURCE_PHONO
    - SOURCE_CD
    - SOURCE_FM
    - SOURCE_AM
    - SOURCE_TUNER
    - SOURCE_MUSICSERVER
    - SOURCE_INTERETRADIO
    - SOURCE_USB
    - SOURCE_USB_BACK
    - SOURCE_NETWORK
    - SOURCE_MULTICH
    - SOURCE_SIRIUS
    - SOURCE_UP
    - SOURCE_DOWN
    - SOURCE_QUERY
    - SOURCE_SET
    - SET_SOURCE
    - SOURCE
  - _Video Wide_
    - VIDEO_WIDE_AUTO
    - VIDEO_WIDE_43
    - VIDEO_WIDE_FULL
    - VIDEO_WIDE_ZOOM
    - VIDEO_WIDE_WIDEZOOM
    - VIDEO_WIDE_SMARTZOOM
    - VIDEO_WIDE_NEXT
    - VIDEO_WIDE_QUERY
  - _Listen Mode_
    - LISTEN_MODE_STEREO
    - LISTEN_MODE_ALCHANSTEREO
    - LISTEN_MODE_AUDYSSEY_DSX
    - LISTEN_MODE_PLII_MOVIE_DSX
    - LISTEN_MODE_PLII_MUSIC_DSX
    - LISTEN_MODE_PLII_GAME_DSX
    - LISTEN_MODE_NEO_CINEMA_DSX
    - LISTEN_MODE_NEO_MUSIC_DSX
    - LISTEN_MODE_NEURAL_SURROUND_DSX
    - LISTEN_MODE_NEURAL_DIGITAL_DSX
    - LISTEN_MODE_UP
    - LISTEN_MODE_DOWN
    - LISTEN_MODE_QUERY
- **Zone 2**
  - _Power_
    - ZONE2_POWER_OFF
    - ZONE2_POWER_ON
    - ZONE2_POWER_QUERY
  - _Mute_
    - ZONE2_UNMUTE
    - ZONE2_MUTE
    - ZONE2_MUTE_QUERY
  - _Volume_
    - ZONE2_VOLUME_UP
    - ZONE2_VOLUME_DOWN
    - ZONE2_VOLUME_QUERY
    - ZONE2_VOLUME_SET
    - ZONE2_SET_VOLUME
    - ZONE2_VOLUME
  - _Source_
    - ZONE2_SOURCE_DVR
    - ZONE2_SOURCE_VCR
    - ZONE2_SOURCE_SATELLITE
    - ZONE2_SOURCE_CABLE
    - ZONE2_SOURCE_GAME
    - ZONE2_SOURCE_AUXILIARY
    - ZONE2_SOURCE_AUX
    - ZONE2_SOURCE_VIDEO5
    - ZONE2_SOURCE_AUX2
    - ZONE2_SOURCE_COMPUTER
    - ZONE2_SOURCE_PC
    - ZONE2_SOURCE_BLURAY
    - ZONE2_SOURCE_DVD
    - ZONE2_SOURCE_TAPE1
    - ZONE2_SOURCE_TAPE2
    - ZONE2_SOURCE_PHONO
    - ZONE2_SOURCE_CD
    - ZONE2_SOURCE_FM
    - ZONE2_SOURCE_AM
    - ZONE2_SOURCE_TUNER
    - ZONE2_SOURCE_MUSICSERVER
    - ZONE2_SOURCE_INTERETRADIO
    - ZONE2_SOURCE_USB
    - ZONE2_SOURCE_USB_BACK
    - ZONE2_SOURCE_NETWORK
    - ZONE2_SOURCE_MULTICH
    - ZONE2_SOURCE_SIRIUS
    - ZONE2_SOURCE_UP
    - ZONE2_SOURCE_DOWN
    - ZONE2_SOURCE_QUERY
    - ZONE2_SOURCE_SET
    - ZONE2_SET_SOURCE
    - ZONE2_SOURCE
- **Zone 3**
  - _Power_
    - ZONE3_POWER_OFF
    - ZONE3_POWER_ON
    - ZONE3_POWER_QUERY
  - _Mute_
    - ZONE3_UNMUTE
    - ZONE3_MUTE
    - ZONE3_MUTE_QUERY
  - _Volume_
    - ZONE3_VOLUME_UP
    - ZONE3_VOLUME_DOWN
    - ZONE3_VOLUME_QUERY
    - ZONE3_VOLUME_SET
    - ZONE3_SET_VOLUME
    - ZONE3_VOLUME
  - _Source_
    - ZONE3_SOURCE_DVR
    - ZONE3_SOURCE_VCR
    - ZONE3_SOURCE_SATELLITE
    - ZONE3_SOURCE_CABLE
    - ZONE3_SOURCE_GAME
    - ZONE3_SOURCE_AUXILIARY
    - ZONE3_SOURCE_AUX
    - ZONE3_SOURCE_VIDEO5
    - ZONE3_SOURCE_AUX2
    - ZONE3_SOURCE_COMPUTER
    - ZONE3_SOURCE_PC
    - ZONE3_SOURCE_BLURAY
    - ZONE3_SOURCE_DVD
    - ZONE3_SOURCE_TAPE1
    - ZONE3_SOURCE_TAPE2
    - ZONE3_SOURCE_PHONO
    - ZONE3_SOURCE_CD
    - ZONE3_SOURCE_FM
    - ZONE3_SOURCE_AM
    - ZONE3_SOURCE_TUNER
    - ZONE3_SOURCE_MUSICSERVER
    - ZONE3_SOURCE_INTERETRADIO
    - ZONE3_SOURCE_USB
    - ZONE3_SOURCE_USB_BACK
    - ZONE3_SOURCE_NETWORK
    - ZONE3_SOURCE_MULTICH
    - ZONE3_SOURCE_SIRIUS
    - ZONE3_SOURCE_UP
    - ZONE3_SOURCE_DOWN
    - ZONE3_SOURCE_QUERY
    - ZONE3_SOURCE_SET
    - ZONE3_SET_SOURCE
    - ZONE3_SOURCE
- **Net/USB**
  - _Operations_
    - NETUSB_OP_PLAY
    - NETUSB_OP_STOP
    - NETUSB_OP_PAUSE
    - NETUSB_OP_TRACKUP
    - NETUSB_OP_TRACKDWN
    - NETUSB_OP_FF
    - NETUSB_OP_REW
    - NETUSB_OP_REPEAT
    - NETUSB_OP_RANDOM
    - NETUSB_OP_DISPLAY
    - NETUSB_OP_RIGHT
    - NETUSB_OP_LEFT
    - NETUSB_OP_UP
    - NETUSB_OP_DOWN
    - NETUSB_OP_SELECT
    - NETUSB_OP_1
    - NETUSB_OP_2
    - NETUSB_OP_3
    - NETUSB_OP_4
    - NETUSB_OP_5
    - NETUSB_OP_6
    - NETUSB_OP_7
    - NETUSB_OP_8
    - NETUSB_OP_9
    - NETUSB_OP_0
    - NETUSB_OP_DELETE
    - NETUSB_OP_CAPS
    - NETUSB_OP_SETUP
    - NETUSB_OP_RETURN
    - NETUSB_OP_CHANUP
    - NETUSB_OP_CHANDWN
    - NETUSB_OP_MENU
    - NETUSB_OP_TOPMENU
  - _Song Info_
    - NETUSB_SONG_ARTIST_QUERY
    - NETUSB_SONG_ALBUM_QUERY
    - NETUSB_SONG_TITLE_QUERY
    - NETUSB_SONG_ELAPSEDTIME_QUERY
    - NETUSB_SONG_TRACK_QUERY
    - NETUSB_PLAY_STATUS_QUERY

## Advanced commands

If you want to use commands that are not predefined by the binding you can use them with `#` as a prefix.

```
Dimmer volume { onkyo="INCREASE:hometheater:VOLUME_UP, DECREASE:hometheater:VOLUME_DOWN, *:hometheater:#MVL%02X" }
```

openHAB sends volume INCREASE -> binding sends VOLUME_UP (eISCP command=MVLUP)

openHAB sends volume DECREASE -> binding sends VOLUME_DOWN (eISCP command=MVLDOWN)

openHAB sends volume 30 -> binding sends eISCP command `MVL1E` (set volume level to 30)

```
Number zone4Selector { onkyo="*:hometheater:#SL4%02X" }
```

```
Switch onkyoPower { onkyo="*:hometheater:#PWR%02X" }
```

A list of all commands that are supported by Onkyo's eISCP can be found [here](http://blog.siewert.net/?cat=18).

Be aware that openHAB uses decimal numbers but ISCP uses hex.

For example: The documentation says "NET" (as a source) is the value "2B". You need to translate this from HEX to DEC for openhab. (2B = 43)

items:

```
Number onkyoZ2Selector "Source [%d]" {onkyo="INIT:avr:#SLZQSTN, *:avr:#SLZ%02X"}
```

sitemap:

```
Selection item=onkyoZ2Selector label="Source" mappings=[127=OFF, 43=NET, 1=SAT]
```

## Limitations

- NETUSB_SONG_ELAPSEDTIME_QUERY - NET/USB Time Info
- Elapsed time/Track Time Max 99:59
- NETUSB_SONG_TRACK_QUERY - NET/USB Track Info
- Current Track/Toral Track Max 9999
- NJA - NET/USB Jacket Art
- Album Cover cannot be processed yet.

## Full Example

items/onkyo.items

```
//
// Main
//
// Power
Switch onkyoPower          "Power"                   {onkyo="INIT:hometheater:POWER_QUERY, ON:hometheater:POWER_ON, OFF:hometheater:POWER_OFF"}
// Sleep
Number onkyoSleep          "Sleep Timer [%d Min]"    {onkyo="INIT:hometheater:#SLPQSTN, 0:hometheater:#SLPOFF, *:hometheater:#SLP%02X, 0:hometheater:#SLPOFF"}
// Mute
Switch onkyoMute           "Mute"                    {onkyo="INIT:hometheater:MUTE_QUERY, ON:hometheater:MUTE, OFF:hometheater:UNMUTE"}
// Volume
Dimmer onkyoVolume         "Volume [%d]"             {onkyo="INIT:hometheater:VOLUME_QUERY, INCREASE:hometheater:VOLUME_UP, DECREASE:hometheater:VOLUME_DOWN, *:hometheater:VOLUME_SET"}
//Source
Number onkyoSource         "Source"                  {onkyo="INIT:hometheater:SOURCE_QUERY, INCREASE:hometheater:SOURCE_UP, DECREASE:hometheater:SOURCE_DOWN, *:hometheater:SOURCE_SET"}
//Video Modes
Number onkyoVideoWide      "Video Wide Mode"         {onkyo="INIT:hometheater:VIDEO_WIDE_QUERY, INCREASE:hometheater:VIDEO_WIDE_NEXT, *:hometheater:#VWM%02X"}
Number onkyoVideoPicture   "Video Picture Mode"      {onkyo="INIT:hometheater:#VPMQSTN, INCREASE:hometheater:#VPMUP, *:hometheater:#VPM%02X"}
//Audio Mode
Number onkyoListenMode     "Listen Mode"             {onkyo="INIT:hometheater:LISTEN_MODE_QUERY, INCREASE:hometheater:LISTEN_MODE_UP, DECREASE:hometheater:LISTEN_MODE_DOWN, *:hometheater:#LMD%02X"}
Switch onkyoAudysseyDynEQ  "Audysses Dynamic EQ"     {onkyo="INIT:hometheater:#ADQQSTN, OFF:hometheater:#ADQ00, ON:hometheater:#ADQ01"}
Number onkyoAudysseyDynVol "Audysses Dynamic Volume" {onkyo="INIT:hometheater:#ADVQSTN, INCREASE:hometheater:#ADVUP, *:hometheater:#ADV%02X"}
//Information
String onkyoAudio          "Audio [%s]"              {onkyo="INIT:hometheater:#IFAQSTN"}
String onkyoVideo          "Video [%s]"              {onkyo="INIT:hometheater:#IFVQSTN"}
// Display
Number onkyoDisplayMode    "Display Mode"            {onkyo="INIT:hometheater:#DIFQSTN, INCREASE:hometheater:#DIFTG, *:hometheater:#DIF%02X"}
Number onkyoDimmerLevel    "Display Dimmer Level"    {onkyo="INIT:hometheater:#DIMQSTN, INCREASE:hometheater:#DIMDIM, *:hometheater:#DIM%02X"}

//
// Zone 2
//
// Power
Switch onkyoZ2Power   "Power"       {onkyo="INIT:hometheater:ZONE2_POWER_QUERY, ON:hometheater:ZONE2_POWER_ON, OFF:hometheater:ZONE2_POWER_OFF"}
// Mute
Switch onkyoZ2Mute    "Mute"        {onkyo="INIT:hometheater:ZONE2_MUTE_QUERY:, ON:hometheater:ZONE2_MUTE, OFF:hometheater:ZONE2_UNMUTE"}
// Volume
Dimmer onkyoZ2Volume  "Volume [%d]" {onkyo="INIT:hometheater:ZONE2_VOLUME_QUERY, INCREASE:hometheater:ZONE2_VOLUME_UP, DECREASE:hometheater:ZONE2_VOLUME_DOWN, *:hometheater:ZONE2_VOLUME_SET"}
//Source
Number onkyoZ2Source  "Source"      {onkyo="INIT:hometheater:ZONE2_SOURCE_QUERY, INCREASE:hometheater:ZONE2_SOURCE_UP, DECREASE:hometheater:ZONE2_SOURCE_DOWN, *:hometheater:ZONE2_SOURCE_SET"}

//
// Zone 3
//
// Power
Switch onkyoZ3Power  "Power"       {onkyo="INIT:hometheater:ZONE3_POWER_QUERY, ON:hometheater:ZONE3_POWER_ON, OFF:hometheater:ZONE3_POWER_OFF"}
// Mute
Switch onkyoZ3Mute   "Mute"        {onkyo="INIT:hometheater:ZONE3_MUTE_QUERY:, ON:hometheater:ZONE3_MUTE, OFF:hometheater:ZONE3_UNMUTE"}
// Volume
Dimmer onkyoZ3Volume "Volume [%d]" {onkyo="INIT:hometheater:ZONE3_VOLUME_QUERY, INCREASE:hometheater:ZONE3_VOLUME_UP, DECREASE:hometheater:ZONE3_VOLUME_DOWN, *:hometheater:ZONE3_VOLUME_SET"}
//Source
Number onkyoZ3Source "Source"      {onkyo="INIT:hometheater:ZONE3_SOURCE_QUERY, INCREASE:hometheater:ZONE3_SOURCE_UP, DECREASE:hometheater:ZONE3_SOURCE_DOWN, *:hometheater:ZONE3_SOURCE_SET"}

//
// NET/USB
//
// Controls
Switch onkyoNETPlay      "Play"             { onkyo="ON:hometheater:NETUSB_OP_PLAY", autoupdate="false"}
Switch onkyoNETPause     "Pause"            { onkyo="ON:hometheater:NETUSB_OP_PAUSE", autoupdate="false"}
Switch onkyoNETStop      "Stop"             { onkyo="ON:hometheater:NETUSB_OP_STOP", autoupdate="false"}
Switch onkyoNETTrackUp   "Track Up"         { onkyo="ON:hometheater:NETUSB_OP_TRACKUP", autoupdate="false"}
Switch onkyoNETTrackDown "Track Down"       { onkyo="ON:hometheater:NETUSB_OP_TRACKDWN", autoupdate="false"}
Switch onkyoNETFF        "Fast Forward"     { onkyo="ON:hometheater:NETUSB_OP_FF", autoupdate="false"}
Switch onkyoNETREW       "Rewind"           { onkyo="ON:hometheater:NETUSB_OP_REW", autoupdate="false"}
Number onkyoNETService   "Service"          { onkyo="INIT:hometheater:#NSVQST, *:hometheater:#NSV%02X0"}
Number onkyoNETList      "Select List Item" { onkyo="*:hometheater:#NLSL%01X"}
// Information
String onkyoNETArtist     "Artist [%s]"      {onkyo="INIT:hometheater:NETUSB_SONG_ARTIST_QUERY"}
String onkyoNETAlbum      "Album [%s]"       {onkyo="INIT:hometheater:NETUSB_SONG_ALBUM_QUERY"}
String onkyoNETTitle      "Title [%s]"       {onkyo="INIT:hometheater:NETUSB_SONG_TITLE_QUERY"}
String onkyoNETTrack      "Track [%s]"       {onkyo="INIT:hometheater:NETUSB_SONG_TRACK_QUERY"}
String onkyoNETTime       "Time [%s]"        {onkyo="INIT:hometheater:NETUSB_SONG_ELAPSEDTIME_QUERY"}
String onkyoNETPlayStatus "Play Status [%s]" {onkyo="INIT:hometheater:NETUSB_PLAY_STATUS_QUERY"}
```

sitemaps/onkyo.sitemap

```
sitemap onkyo label="Onkyo Demo"
{
    Frame label="Zones" {
        Text label="Main" icon="sofa" {
            Frame label="Power" {
                Switch    item=onkyoPower
                Selection item=onkyoSleep mappings=[0=Off, 5="5 Min", 10="10 Min", 15="15 Min", 30="30 Min", 77="77 Min", 90="90 Min"]
            }
            Frame label="Volume" {
                Switch item=onkyoMute
                Slider item=onkyoVolume
            }
            Frame label="Source" {
                Selection item=onkyoSource mappings=[0="VCR/DVR", 1="CBL/SAT", 2=GAME, 5=PC, 16="BD/DVD", 35=CD, 43="NET/USB", 45=Airplay, 127=OFF]
            }
            Frame label="Video Modes" {
                Selection item=onkyoVideoWide label="Video Wide" mappings=[0=Auto, 1="4:3", 2=Full, 3=Zoom, 4="Wide Zoom", 5="Smart Zoom"]
                Selection item=onkyoVideoPicture label="Video Picture" mappings=[0=Trough, 1=Custom, 2=Cinema, 3=Game, 5="ISF Day", 6="ISF Night", 7="Streaming", 8=Direct]
            }
            Frame label="Audio Modes" {
                Selection item=onkyoListenMode mappings=[0=Stereo, 1=Direct, 2=Surround, 15=Mono, 31="Whole House Mode", 66="THX Cinema", 31="Whole House"]
                Switch    item=onkyoAudysseyDynEQ
                Selection item=onkyoAudysseyDynVol mappings=[0=OFF, 1=Low, 2=Mid, 3=High]
            }
            Frame label="Information" {
                Text item=onkyoAudio
                Text item=onkyoVideo
            }
            Frame label="Display" {
                Selection item=onkyoDisplayMode mappings=[0="Source + Vol", 2="Digital Format (temporary)", 3="Video Format (temporary)"]
                Selection item=onkyoDimmerLevel mappings=[0="Bright", 1="Dim", 2="Dark", 3="Shut-Off", 8="Bright & LED OFF"]
            }
        }
        Text label="Zone 2" icon="bedroom" {
            Frame label="Power" {
               Switch item=onkyoZ2Power
            }
            Frame label="Volume" {
                Switch item=onkyoZ2Mute
                Slider item=onkyoZ2Volume
            }
            Frame label="Source" {
                Selection  item=onkyoZ2Source label="Source Selection" mappings=[0="VCR/DVR", 1="CBL/SAT", 2=GAME, 5=PC, 16="BD/DVD", 35=CD, 43="NET/USB", 45=Airplay, 127=OFF]
            }
        }

        Text label="Zone 3" icon="bath" {
            Frame label="Power" {
               Switch item=onkyoZ3Power
            }
            Frame label="Volume" {
                Switch item=onkyoZ3Mute
                Slider item=onkyoZ3Volume
            }
            Frame label="Source" {
                Selection  item=onkyoZ3Source label="Source Selection" mappings=[0="VCR/DVR", 1="CBL/SAT", 2=GAME, 5=PC, 16="BD/DVD", 35=CD, 43="NET/USB", 45=Airplay, 127=OFF]
            }
        }
        Text label="NET/USB" icon="video" {
            Frame label="Controls" {
                Switch    item=onkyoNETPlay
                Switch    item=onkyoNETPause
                Switch    item=onkyoNETStop
                Switch    item=onkyoNETTrackUp
                Switch    item=onkyoNETTrackDown
                Switch    item=onkyoNETFF
                Switch    item=onkyoNETREW
                Selection item=onkyoNETService mappings=[0="Media Server (DLNA)", 1=Favorite, 2=vTuner, 3=SIRIUS, 6="Last.fm", 14=TuneIn Radio]
                Selection item=onkyoNETList    mappings=[0="1", 1="2", 2="3", 3="4", 4="5", 5="6", 6="7", 7="8", 8="9", 9="10"]
            }
            Frame label="Information" {
                Text item=onkyoNETArtist
                Text item=onkyoNETAlbum
                Text item=onkyoNETTitle
                Text item=onkyoNETTrack
                Text item=onkyoNETTime
            }
        }


    }
}
```

### NetUsb

#### Navigation

When using openHAB, it may be desirable to be able to control your A/V receiver without being able to see the video output of the receiver. This may be the case when you are using extra zone outputs for whole house audio, etc.. Onkyo receivers implement a hierarchical menu structure where each page of menu can contain up to ten items. Older generation (circa 2010) and newer generation (circa 2014) were more Model View Control (MVC) friendly. Older units send menu pages one item at a time, but they send all ten items regardless of whether or not they are all used. Newer units will send the complete menu page in one status update. Units made in between are some what problematic as they send menu pages one item at a time, but they only send as many items as are on the page. This causes problems when one page has say ten items, and the next only has eight. There is no easy way to know that the last two menu items should be cleared as no end of menu indication is given.

My first attempt at solving this problems was to clear all menu items when the first item was received. This didn't work as expected as openHAB executes rules in different threads and there is no guarantee that they will execute in the order they were invoked. This would result in random menu items missing as the rule for the first item executed after they had been updated. I wasn't able to find a good solution for this, but I was able to get things functional by delaying the execution of all rules except the one for the first item. Older receivers don't need this and don't need to clear menu items as all items are sent on menu page changes.

#### Menu List Display

Onkyo receivers transmit menu items with a string matching the following REGEX:

NLSU[0-9]-[0-9a-zA-Z ]*

I'm sure there are other valid characters after the '-', but I'm not sure the set they are limited to (if any). The 'U' in "NLSU" is for Unicode. Old receivers running old firmware images may have an 'A' here for ASCII.

Onkyo receivers transmit the current cursor position (within the menu page) with a string matching the following REGEX:

NLSC[0-9][CP]

The last character is a 'C' when the cursor moves within the page. The last character is a 'P' when a page change occurs. The NETUSB_OP_UP and NETUSB_OP_DOWN commands can be used to move the cursor up/down within the page. The NETUSB_OP_LEFT and NETUSB_OP_RIGHT commands can be used to scroll the current menu page up/down. The NETUSB_OP_RETURN command can be used to move up in the menu hierarchy. The NETUSB_OP_SELECT command can be used to select the current menu item. The "NLSL[0-9]" command can be used to randomly select a menu item (this is not supported on older models). These elements can be combined with rules and dynamic color to implement a reasonable user interface.

For menu list display a string item is used for each item in the menu (ten strings). These strings capture the raw status updates from the receiver, but are not displayed. There is a rule for each menu item that removes the overhead characters and posts the value to another string item for display. 

Dynamic color is used to indicate the current cursor position. In order to do this an item is used to capture the raw cursor position from the receiver. A rule is run when it changes to strip out the cursor line and post it to another item that is used to control the dynamic color (this simplifies the color rules and is useful later).
  
#### Menu List Selection

I have included "NLSL[0-9]" commands on the displayed menu item strings in the hopes that openHAB will eventually support some type of selectable text that sends a command instead of going to a URL. Then the user can just select the menu item on receivers that support "NLSL[0-9]" commands. For older receivers, the NETUSB_OP_SELECT command is used. The user needs to navigate to the desired menu item before this command is sent. Setpoint elements are used for navigation as they provide a more compact arrangement.

### Items

```
//
// NET/USB
//
// Controls
Switch onkyoNETPlay         "Play"              (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_PLAY", autoupdate="false"}
Switch onkyoNETPause        "Pause"             (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_PAUSE", autoupdate="false"}
Switch onkyoNETStop         "Stop"              (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_STOP", autoupdate="false"}
Switch onkyoNETTrackUp      "Track Up"          (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_TRACKUP", autoupdate="false"}
Switch onkyoNETTrackDown    "Track Down"        (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_TRACKDWN", autoupdate="false"}
Switch onkyoNETFF           "Fast Forward"      (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_FF", autoupdate="false"}
Switch onkyoNETREW          "Rewind"            (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_REW", autoupdate="false"}
Number onkyoNETService      "Service"           (gOnkyo1)   {onkyo="INIT:onkyo1:#NSVQST, *:onkyo1:#NSV%02X0"}
Number onkyoNETSelectList   "Select List Item"  (gOnkyo1)   {onkyo="*:onkyo1:#NLSL%01X"}
Switch onkyoNETUp           "Up"                (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_UP", autoupdate="false"}
Switch onkyoNETDown         "Down"              (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_DOWN", autoupdate="false"}
Switch onkyoNETLeft         "Left"              (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_LEFT", autoupdate="false"}
Switch onkyoNETRight        "Right"             (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_RIGHT", autoupdate="false"}
Switch onkyoNETReturn       "Return"            (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_RETURN", autoupdate="false"}
Switch onkyoNETSelect       "Select"            (gOnkyo1)   {onkyo="ON:onkyo1:NETUSB_OP_SELECT", autoupdate="false"}
String onkyoNETCursor       "Cursor [%s]"       (gOnkyo1)   {onkyo="*:onkyo1:#NLSC"}
String onkyoNETList0        "1 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU0"}
String onkyoNETList1        "2 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU1"}
String onkyoNETList2        "3 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU2"}
String onkyoNETList3        "4 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU3"}
String onkyoNETList4        "5 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU4"}
String onkyoNETList5        "6 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU5"}
String onkyoNETList6        "7 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU6"}
String onkyoNETList7        "8 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU7"}
String onkyoNETList8        "9 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSU8"}
String onkyoNETList9        "10 [%s]"           (gOnkyo1)   {onkyo="*:onkyo1:#NLSU9"}
String onkyoNETSel0         "1 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL0", autoupdate="false"}
String onkyoNETSel1         "2 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL1", autoupdate="false"}
String onkyoNETSel2         "3 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL2", autoupdate="false"}
String onkyoNETSel3         "4 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL3", autoupdate="false"}
String onkyoNETSel4         "5 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL4", autoupdate="false"}
String onkyoNETSel5         "6 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL5", autoupdate="false"}
String onkyoNETSel6         "7 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL6", autoupdate="false"}
String onkyoNETSel7         "8 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL7", autoupdate="false"}
String onkyoNETSel8         "9 [%s]"            (gOnkyo1)   {onkyo="*:onkyo1:#NLSL8", autoupdate="false"}
String onkyoNETSel9         "10 [%s]"           (gOnkyo1)   {onkyo="*:onkyo1:#NLSL9", autoupdate="false"}
// Information
String onkyoNETArtist       "Artist [%s]"       (gOnkyo1)   {onkyo="INIT:onkyo1:NETUSB_SONG_ARTIST_QUERY"}
String onkyoNETAlbum        "Album [%s]"        (gOnkyo1)   {onkyo="INIT:onkyo1:NETUSB_SONG_ALBUM_QUERY"}
String onkyoNETTitle        "Title [%s]"        (gOnkyo1)   {onkyo="INIT:onkyo1:NETUSB_SONG_TITLE_QUERY"}
String onkyoNETTrack        "Track [%s]"        (gOnkyo1)   {onkyo="INIT:onkyo1:NETUSB_SONG_TRACK_QUERY"}
String onkyoNETTime         "Time [%s]"         (gOnkyo1)   {onkyo="INIT:onkyo1:NETUSB_SONG_ELAPSEDTIME_QUERY"}
String onkyoNETPlayStatus   "Play Status [%s]"  (gOnkyo1)   {onkyo="INIT:onkyo1:NETUSB_PLAY_STATUS_QUERY"}
// Proxy
Number onkyoNETPage         "Page"              (gOnkyo1)
Number onkyoNETCursorPos    "Cursor"            (gOnkyo1)
```

#### Sitemap

```
            Text label="NET/USB" icon="sofa" {
                Frame label="Information" {
                    Text item=onkyoNETArtist
                    Text item=onkyoNETAlbum
                    Text item=onkyoNETTitle
                    Text item=onkyoNETTrack
                    Text item=onkyoNETTime
                }
                Frame label="Control"{
                    Text label="Navigation" icon="sofa" {
                        Frame {
                            Selection   item=onkyoNETService mappings=[0="Media Server (DLNA)", 1=Favorite, 2=vTuner, 3=SIRIUS, 6="Last.fm", 14="TuneIn Radio"]
//                          Selection   item=onkyoNETSelectList mappings=[0="1", 1="2", 2="3", 3="4", 4="5", 5="6", 6="7", 7="8", 8="9", 9="10"]
                            Switch      item=onkyoNETReturn
                            Switch      item=onkyoNETSelect
                            Setpoint    item=onkyoNETPage minValue=0 maxValue=2 step=1
                            Setpoint    item=onkyoNETCursorPos minValue=0 maxValue=9 step=1
                        }
                        Frame label="List" {
                            Text        item=onkyoNETSel0 valuecolor=[onkyoNETCursorPos=="9"="fuchsia"]
                            Text        item=onkyoNETSel1 valuecolor=[onkyoNETCursorPos=="8"="fuchsia"]
                            Text        item=onkyoNETSel2 valuecolor=[onkyoNETCursorPos=="7"="fuchsia"]
                            Text        item=onkyoNETSel3 valuecolor=[onkyoNETCursorPos=="6"="fuchsia"]
                            Text        item=onkyoNETSel4 valuecolor=[onkyoNETCursorPos=="5"="fuchsia"]
                            Text        item=onkyoNETSel5 valuecolor=[onkyoNETCursorPos=="4"="fuchsia"]
                            Text        item=onkyoNETSel6 valuecolor=[onkyoNETCursorPos=="3"="fuchsia"]
                            Text        item=onkyoNETSel7 valuecolor=[onkyoNETCursorPos=="2"="fuchsia"]
                            Text        item=onkyoNETSel8 valuecolor=[onkyoNETCursorPos=="1"="fuchsia"]
                            Text        item=onkyoNETSel9 valuecolor=[onkyoNETCursorPos=="0"="fuchsia"]
                        }
                    }
                    Text label="Transport" icon="sofa" {
                        Switch      item=onkyoNETPlay
                        Switch      item=onkyoNETPause
                        Switch      item=onkyoNETStop
                        Switch      item=onkyoNETTrackUp
                        Switch      item=onkyoNETTrackDown
                        Switch      item=onkyoNETFF
                        Switch      item=onkyoNETREW
                    }
                }
            }
```

#### Rules

```java
import java.util.concurrent.locks.ReentrantLock
import java.util.List
import java.util.ArrayList

var ReentrantLock                       onkyoLock  = new java.util.concurrent.locks.ReentrantLock()
var Integer                             onkyoCursorPos


rule "Init"
    when
        System started
    then
        onkyoLock.lock()
        try {
            onkyoCursorPos = 9
            postUpdate(onkyoNETCursorPos, onkyoCursorPos)
            postUpdate(onkyoNETPage, 1)
        } finally {
           onkyoLock.unlock()
        }
end


rule "Update Page"
    when
        Item onkyoNETPage changed
    then
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETPage.state.toString())
 
            if (onkyoNETPage.state > 1) {
                sendCommand(onkyoNETLeft, ON)               
            } else if (onkyoNETPage.state < 1) {
                sendCommand(onkyoNETRight, ON)              
            }
            postUpdate(onkyoNETPage, 1)
        } finally {
           onkyoLock.unlock()
        }
end


rule "Update CursorPos"
    when
        Item onkyoNETCursorPos changed
    then
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETCursorPos.state.toString())
 
            if (onkyoNETCursorPos.state < onkyoCursorPos) {
                sendCommand(onkyoNETDown, ON)               
            } else if (onkyoNETCursorPos.state > onkyoCursorPos) {
                sendCommand(onkyoNETUp, ON)             
            }
        } finally {
           onkyoLock.unlock()
        }
end


rule "Update Cursor"
    when
        Item onkyoNETCursor changed
    then
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETCursor.state.toString())
            onkyoCursorPos = 9 - new Integer(onkyoNETCursor.state.toString.substring(1,2))
            postUpdate(onkyoNETCursorPos, onkyoCursorPos)
        } finally {
           onkyoLock.unlock()
        }
end


/*
 * This rule processes Onkyo list updates and removes leading status type characters.
 */
rule "Update List 0 Item"
    when
        Item onkyoNETList0 received update
    then
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList0.state.toString())
            postUpdate(onkyoNETSel0, onkyoNETList0.state.toString.substring(3))             
            postUpdate(onkyoNETSel1, " ")               
            postUpdate(onkyoNETSel2, " ")               
            postUpdate(onkyoNETSel3, " ")               
            postUpdate(onkyoNETSel4, " ")               
            postUpdate(onkyoNETSel5, " ")               
            postUpdate(onkyoNETSel6, " ")               
            postUpdate(onkyoNETSel7, " ")               
            postUpdate(onkyoNETSel8, " ")               
            postUpdate(onkyoNETSel9, " ")               
        } finally {
           onkyoLock.unlock()
        }
end
rule "Update List 1 Item"
    when
        Item onkyoNETList1 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList1.state.toString())
            postUpdate(onkyoNETSel1, onkyoNETList1.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 2 Item"
    when
       Item onkyoNETList2 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList2.state.toString())
            postUpdate(onkyoNETSel2, onkyoNETList2.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 3 Item"
    when
        Item onkyoNETList3 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList3.state.toString())
            postUpdate(onkyoNETSel3, onkyoNETList3.state.toString.substring(3))             
        } finally{
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 4 Item"
    when
        Item onkyoNETList4 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList4.state.toString())
            postUpdate(onkyoNETSel4, onkyoNETList4.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 5 Item"
    when
        Item onkyoNETList5 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList5.state.toString())
            postUpdate(onkyoNETSel5, onkyoNETList5.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 6 Item"
    when
        Item onkyoNETList6 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList6.state.toString())
            postUpdate(onkyoNETSel6, onkyoNETList6.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 7 Item"
    when
        Item onkyoNETList7 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList7.state.toString())
            postUpdate(onkyoNETSel7, onkyoNETList7.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 8 Item"
    when
        Item onkyoNETList8 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList8.state.toString())
            postUpdate(onkyoNETSel8, onkyoNETList8.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
rule "Update List 9 Item"
    when
        Item onkyoNETList9 received update
    then
    createTimer(now.plusSeconds(1))[|
        onkyoLock.lock()
        try {
            logDebug("onkyo.rules", onkyoNETList9.state.toString())
            postUpdate(onkyoNETSel9, onkyoNETList9.state.toString.substring(3))             
        } finally {
           onkyoLock.unlock()
        }
    ]
end
```