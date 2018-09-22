# Sonos Binding

**Note:** The Sonos binding communicates with the Sonos devices through the UPnP (Universal Plug And Play) protocol. Users of this binding might wish to familiarise them with the protocol and slang. UPnP defines a subscription model whereby a UPnP client can subscribe to UPnP Events that are transmitted by a UPnP device. Sonos Players do emit quite a bit of Events and some are used to capture status variables (see below). 

Sonos Players support multi-room audio. Sonos achieves this by grouping Sonos Players into Zone Groups, and having a Player, the group coordinator, play music which is "streamed" to all the Players part of the Group. That way you can walk from room to room with the same music playing everywhere. Even more, since some Sonos Player models have a line-in socket, you can plug any device into such a Sonos Player, and have its content streamed to any other Player. For example, you can connect a CD player to a Sonos in the basement, and have that music streamed to the Sonos in the bedroom. Or, alternatively, you can connect the line-out of your openHAB system to the line-in of a Sonos, and use some text-to-speech scripts and rules to create a public address system.

Sonos Players also support playlists, music streaming services like Spotify, Rapsody, … as well as alarm clocks that you can program to wake up with your favourite music

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/sonos/).  That binding can discover your Sonos players and therefore does not need you to learn UDNs through alternate means.

## Limitations

* Sonos ZoneBridges are not supported.
* The binding is making use of a buggy UPNP library that result in memory leaks which may crash oenHAB occasionally.
* Sonos commands take time to execute. If applied too fast, the Sonos player will ignore the command and indicated this by an error message in the OSGi console.  Especially the "group" and "radio" command need several seconds to execute. Also the timing is not predictable and the worst case has to be determined experimentally. In rules you may use `Thread::sleep(x)  //(x: ms)` to delay the execution of Sonos command accordingly.
* The "Play/Pause" button will not create related events.

## Finding Your UPnP Unique Device Name (UDN)

In order to control Sonos devices, you must know their UPnP Unique Device Names (UDNs).  The easiest way to find your UPnP UDN is to use a UPnP scanner app. For Windows, [UPnPTest](http://noeld.com/programs.asp?cat=dstools#UPnPTest) works well. Once you've unzipped, run either the 32-bit or the 64-bit version that matches your architecture (if in doubt, try both).

In the UPnPTest app, you will see the following screen:

![](http://i.imgur.com/LKc2SjW.png)

Right click on your device, and select 'Properties'. You will then see your UDN at the bottom:

![](http://i.imgur.com/2TTMTOg.png)

## Binding Configuration

The binding can be configured in the file `services/sonos.cfg`.

| Key | Default | Required | Description |
|-----|---------|:--------:|-------------|
| `<sonos id>`.udn | | if not using the UDN directly in your items | UPnP Unique Device Name |

## Item Configuration

The format of the binding configuration is simple and looks like this:

```
sonos="[<command>:<sonos id>:<sonos command>], [<command>:<sonos id>:<sonos command>], ..."
```

for Items that trigger action or commands on the Sonos Player, and

```
sonos="[<sonos id>:<sonos variable>], [<sonos id>:<sonos variable>], ..."
```

for either Number or String Items that rather store a status variable or other from the Sonos Player.

* `<sonos id>` corresponds with the UDN (UPnP Unique Device Name)  *or* the `<sonos id>` you defined in the binding configuration.
* `<sonos command>` is the command to be sent to the Sonos Player when `<command>` is received. Some `<sonos command>`s take input variables, which are taken from the bound item. In case status variables are used then any value received from the Sonos Player for the defined `<sonos variable>` is used to update the item.

## Sonos Commands

The Sonos Player is very complete but also complex device. For a perfect integration within OpenHAB it is assumed that the user will be using the Sonos Desktop Controller software to define playlists, browse music and so forth, e.g. anything which requires user input or interactivity. Therefore the Sonos Commands supported from within openHAB are mostly limited to those actions that require little or no user interaction

Valid `<sonos command>`s are:

| Command | Item Type | Purpose | Note |
|---------|-----------|---------|------|
| add | String | add another Sonos Player to this Player's group | String is the id of the player to add |
| alarm | Switch | set the first occurring alarm either ON or OFF | Alarms have to be first defined through the Sonos Desktop Controller |
| favorite | String | play the named favorite entry | the entry has to be defined in the Sonos Favorites List in the Sonos Desktop or Mobile Controller (since 1.8) |
| led | Switch | get or set the white led on the front of the Player |  |
| mute | Switch | get or set the mute state of the Master volume of the Player |  |
| next | Switch | skip to next track | both ON and OFF can be used to trigger this command |
| pause | Switch | pause playing music | both ON and OFF can be used to trigger this command |
| play | Switch | play music | both ON and OFF can be used to trigger this command |
| playline | String | play the line-in stream of the the named sonos UIN | UIN is the "RINCON..." identifier string  |
| playlist | String | play the named playlist from favorites | the playlist has to be -pre-defined/created in the Sonos Controller software |
| playuri | String | play the named URI | the URI is either in the "//host/folder/filename.mp3" format or either in the "x-...-..." format used for other types of sources used by Sonos. This command clears the current queue |
| previous | Switch | skip to previous track | both ON and OFF can be used to trigger this command |
| publicaddress | Switch | put all Players in one group, and stream audio from the line-in from the Player that triggered the command | both ON and OFF can be used to trigger this command |
| radio | String | play the named radio station | the station has to be defined in the list of Favorite Stations in the Sonos Desktop Controller |
| remove | String | remove the named Sonos Player from this Player's group | String is the id of the player to remove |
| restore | Switch | restore the state of all Players | both ON and OFF can be used to trigger this command |
| standalone | Switch | make the Sonos Player 'leave' its group and become a standalone Player |  |
| save | Switch | save the state (group membership, current track, position in track, volume) of all the Players | both ON and OFF can be used to trigger this command |
| snooze | Number | snooze the alarm, if running, with x minutes |  |
| stop | Switch | stop playing | both ON and OFF can be used to trigger this command |
| volume | Dimmer | get or set the volume of the Player |  |

## Sonos Status Variables

Valid `<sonos variable>`s are:

| Variable | Item Type | Purpose | Note |
|----------|-----------|---------|------|
| alarmproperties | String | Properties of the alarm currently running |  |
| alarmrunning | Switch | set to ON if the alarm was triggered |  |
| currenttitle | String | title of currently playing song |  |
| currentartist | String | artist of currently playing song |  |
| currentalbum | String | album of currently playing song |  |
| currenttrack | String | the track or radio station currently playing |  |
| linein | Switch | set to ON if the line-in of the Player is connected |  |
| localcoordinator | Switch | set to ON if this Player is the zone group coordinator |  |
| transportstate | String | the transport state of the Player (STOPPED, PLAYING,…) |  |
| zonename | String | Name of the Zone the Sonos Player belongs to |  |
| zonegroup | String | XML formatted string with the current zonegroup configuration |  |
| zonegroupid | String | Id of the Zone group the Sonos Player belongs to |  |


## Examples

### Simple On/Off for one speaker

The following files will get a single speaker to turn on and off via the web interface.

services/sonos.cfg

```
kitchen.udn=RINCON_000E58F3CD0A00000
```

items/sonosdemo.items:

```
Switch Sonos "Power Sonos"        { sonos="[ON:kitchen:play], [OFF:kitchen:stop]" }
Dimmer VolumeSonos "Sonos Volume" { sonos="[kitchen:volume]" }
```

sitemaps/sonosdemo.sitemap:

```
    sitemap sonosdemo label="Main Menu"
    Switch item=Sonos
    Slider item=VolumeSonos
```

### Other Examples

items/sonos.items

```
Switch ledstatus     "LedStatus"          (Sonos)   {sonos="[ON:living:led], [OFF:living:led]", autoupdate="false"}
String currenttrack  "CurrentTrack [%s]"  (Sonos)   {sonos="[RINCON_000E581369DC01400:currenttrack]", autoupdate="false"}
String radiostation  "RadioStation [%s]"  (Sonos)   {sonos="[living:radio]", autoupdate="false"}
Switch PlayLivingRoom "Play/Pause"        (Sonos)   {sonos="[ON:living:play],[OFF:living:pause]"}
Switch next		  "Next track"              (Sonos)   {sonos="[ON:living:next]"}

### Favorites

items/sonosfavorites.items

```
String Sonos_Office_favorite  						(Sonos)   {sonos="[office:favorite]"}
```

items/sonosfavorites.sitemap.fragment

```
Switch item=Sonos_Office_favorite mappings=["Ben Harper Radio"="Ben Harper","Today's Hits Radio"="Hits","Today's Alternative Radio"="Alt"]
```
