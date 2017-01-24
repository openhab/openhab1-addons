Documentation of the Sonos binding Bundle

## Introduction

**Note:** The Sonos binding communicates with the Sonos devices through the UPNP (Universal Plug And Play) protocol. Users of this binding might wish to familiarise them with the protocol and slang. UPNP defines a subscription model whereby a UPNP client can subscribe to UPNP Events that are transmitted by a UPNP device. Sonos Players do emit quite a bit of Events and some are used to capture status variables (see below). 

Sonos Players support multi-room audio. Sonos achieves this by grouping Sonos Players into Zone Groups, and having a Player, the group coordinator, play music which is "streamed" to all the Players part of the Group. That way you can walk from room to room with the same music playing everywhere. Even more, since some Sonos Player models have a line-in socket, you can plug any device into such a Sonos Player, and have its content streamed to any other Player. For example, you can connect a CD player to a Sonos in the basement, and have that music streamed to the Sonos in the bedroom. Or, alternatively, you can connect the line-out of your openHAB system to the line-in of a Sonos, and use some text-to-speech scripts and rules to create a public address system.

Sonos Players also support playlists, music streaming services like Spotify, Rapsody,… as well as alarm clocks that you can program to wake up with your favourite music

Sonos !ZoneBridges are not supported.

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In order to bind an item to a Sonos Player, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the Sonos binding configuration string is explained here:

The format of the binding configuration is simple and looks like this:

    sonos="[<command>:<sonos id>:<sonos command>], [<command>:<sonos id>:<sonos command>], ..."

for Items that trigger action or commands on the Sonos Player, and

    sonos="[<sonos id>:<sonos variable>], [<sonos id>:<sonos variable>], ..."

for either Number or String Items that rather store a status variable or other from the Sonos Player

where the `<sonos id>` corresponds with the UDN (UPNP Unique Device Name)  - or - the configuration in openhab.cfg where one can configure the Sonos players, which looks like this

    sonos:<sonos id>.udn=[UDN]

and where `<sonos command>` is the command to be sent to the Sonos Player when `<command>` is received. Some `<sonos commands>` take input variables, which are taken from the current Item variable. In case status variables are used then any value received from the Sonos Player for the defined `<sonos variable>` is used to update the Item

###Finding Your UPNP Unique Device Name###
The easiest way to find your UPNP UDN is to use a UPNP scanner app. For Windows, UPnPTest [(download here)](http://noeld.com/programs.asp?cat=dstools#UPnPTest) works well. Once you've unzipped, run either the 32-bit or the 64-bit version that matches your architecture (if in doubt, try both).

In the UPnPTest app, you will see the following screen:

![](http://i.imgur.com/LKc2SjW.png)

Right click on your device, and select 'Properties'. You will then see your UDN at the bottom:

![](http://i.imgur.com/2TTMTOg.png)




## Sonos Commands

The Sonos Player is very complete but also complex device. For a perfect integration within OpenHAB it is assumed that the user will be using the Sonos Desktop Controller software to define playlists, browse music and so forth, e.g. anything which requires user input or interactivity. Therefore the Sonos Commands supported from within openHAB are mostly limited to those actions that require little or no user interaction

Valid `<sonos command>`'s are:

<table>
  <tr><td><b>Command</b></td><td><b>Item Type</b></td><td><b>Purpose</b></td><td><b>Note</b></td></tr>
  <tr><td>add</td><td>String</td><td>add another Sonos Player to this Player's group</td><td>String is the id of the player to add</td></tr>
  <tr><td>alarm</td><td>OnOff</td><td>set the first occurring alarm either ON or OFF</td><td>Alarms have to be first defined through the Sonos Desktop Controller</td></tr>
 <tr><td>favorite</td><td>String</td><td>play the named favorite entry</td><td>the entry has to be defined in the Sonos Favorites List in the Sonos Desktop or Mobile Controller (since 1.8)</td></tr>
  <tr><td>led</td><td>OnOff</td><td>get or set the white led on the front of the Player</td><td></td></tr>
  <tr><td>mute</td><td>!OnOff</td><td>get or set the mute state of the Master volume of the Player</td><td></td></tr>
  <tr><td>next</td><td>OnOff</td><td>skip to next track</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>pause</td><td>OnOff</td><td>pause playing music</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>play</td><td>OnOff</td><td>play music</td><td>both ON and OFF can be used to trigger this command</td></tr>
   <tr><td>playline</td><td>String</td><td>play the line-in stream of the the named sonos UIN</td><td>UIN is the "RINCON..." identifier string </td></tr>
   <tr><td>playlist</td><td>String</td><td>play the named playlist from favorites</td><td>the playlist has to be -pre-defined/created in the Sonos Controller software</td></tr>
   <tr><td>playuri</td><td>String</td><td>play the named URI</td><td>the URI is either in the "//host/folder/filename.mp3" format or either in the "x-...-..." format used for other types of sources used by Sonos. This command clears the current queue</td></tr>
  <tr><td>previous</td><td>OnOff</td><td>skip to previous track</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>publicaddress</td><td>OnOff</td><td>put all Players in one group, and stream audio from the line-in from the Player that triggered the command</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>radio</td><td>String</td><td>play the named radio station</td><td>the station has to be defined in the list of Favorite Stations in the Sonos Desktop Controller</td></tr>
  <tr><td>remove</td><td>String</td><td>remove the named Sonos Player from this Player's group</td><td>String is the id of the player to remove</td></tr>
  <tr><td>restore</td><td>OnOff</td><td>restore the state of all Players</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>standalone</td><td>OnOff</td><td>make the Sonos Player 'leave' its group and become a standalone Player</td><td></td></tr>
  <tr><td>save</td><td>OnOff</td><td>save the state (group membership, current track, position in track, volume) of all the Players</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>snooze</td><td>Decimal</td><td>snooze the alarm, if running, with x minutes</td><td></td></tr>
  <tr><td>stop</td><td>OnOff</td><td>stop playing</td><td>both ON and OFF can be used to trigger this command</td></tr>
  <tr><td>volume</td><td>Percent</td><td>get or set the volume of the Player</td><td></td></tr>
</table>

## Sonos Status Variables

Valid `<sonos variable>`'s are:

<table>
  <tr><td><b>Variable</b></td><td><b>Item Type</b></td><td><b>Purpose</b></td><td><b>Note</b></td></tr>
  <tr><td>alarmproperties</td><td>String</td><td>Properties of the alarm currently running</td><td></td></tr>
  <tr><td>alarmrunning</td><td>OnOff</td><td>set to ON if the alarm was triggered</td><td></td></tr>
  <tr><td>currenttitle</td><td>String</td><td>title of currently playing song</td><td></td></tr>
  <tr><td>currentartist</td><td>String</td><td>artist of currently playing song</td><td></td></tr>
  <tr><td>currentalbum</td><td>String</td><td>album of currently playing song</td><td></td></tr>
  <tr><td>currenttrack</td><td>String</td><td>the track or radio station currently playing</td><td></td></tr>
  <tr><td>linein</td><td>OnOff</td><td>set to ON if the line-in of the Player is connected</td><td></td></tr>
  <tr><td>localcoordinator</td><td>OnOff</td><td>set to ON if this Player is the zone group coordinator</td><td></td></tr>
  <tr><td>transportstate</td><td>String</td><td>the transport state of the Player (STOPPED, PLAYING,…)</td><td></td></tr>
  <tr><td>zonename</td><td>String</td><td>Name of the Zone the Sonos Player belongs to</td><td></td></tr>
  <tr><td>zonegroup</td><td>String</td><td>XML formatted string with the current zonegroup configuration</td><td></td></tr>
  <tr><td>zonegroupid</td><td>String</td><td>Id of the Zone group the Sonos Player belongs to</td><td></td></tr>
</table>

## Examples
##Simple On/Off for one speaker###

The following files will get a single speaker to turn on and off via the web interface.

openhab.config:

    # skipping beginning of config file
    # Use your own UDN number (see instructions above)
    sonos:kitchen.udn=RINCON_000E58F3CD0A00000
    # skipping rest of config file

onoff.items:

    Switch Sonos "Power Sonos" { sonos="[ON:kitchen:play], [OFF:kitchen:stop]" }
    Dimmer VolumeSonos "Sonos Volume" { sonos="[kitchen:volume]" }

onoff.sitemap:

    sitemap demo label="Main Menu"
    Switch item=Sonos
    Slider item=VolumeSonos


###Other Examples###

Here are some examples of valid binding configuration strings:

    sonos="[ON:office:play], [OFF:office:stop]"
    sonos="[office:currenttrack]"

As a result, your lines in the items file might look like the following:

    Switch ledstatus     "LedStatus"     (Sonos)   {sonos="[ON:living:led], [OFF:living:led]", autoupdate="false"}
    String currenttrack  "CurrentTrack [%s]"  (Sonos)   {sonos="[RINCON_000E581369DC01400:currenttrack]", autoupdate="false"}
    String radiostation  "RadioStation [%s]"  (Sonos)   {sonos="[living:radio]", autoupdate="false"}
    Switch PlayLivingRoom "Play/Pause"        (Sonos)   {sonos="[ON:living:play],[OFF:living:pause]"}
    Switch next		  "Next track"        (Sonos)   {sonos="[ON:living:next]"}

Favorites item example:

    String Sonos_Office_favorite  						(Sonos)   {sonos="[office:favorite]"}

Favorites sitemap example:

    Switch item=Sonos_Office_favorite mappings=["Ben Harper Radio"="Ben Harper","Today's Hits Radio"="Hits","Today's Alternative Radio"="Alt"]

## Troubleshooting
Sonos commands take time to execute. If applied to fast, the SONOS player will ignore the command and indicated this by an error message in the OSGi console.
Especially the "group" and "radio" command need several seconds to execute. Also the timing is not predictable and the worst case has to be determined experimentally. In rules you may use "Thread::sleep(x)  //(x: ms)"  to delay the execution of SONOS command accordingly.

The volume button on the loudspeakers create events and can trigger respective rule. Note: the "Play/Pause" button will not create related events.

## UPNP Problems
Note:The binding is making use of a buggy UPNP library that result in memory leaks which may crash OPENHAB occasionally.