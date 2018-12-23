# Squeezebox Binding

This binding integrates the [Logitech Media Server](http://www.mysqueezebox.com) and compatible Squeeze players.

From the [Wikipedia entry](http://en.wikipedia.org/wiki/Squeezebox_%28network_music_player%29):

> Slim Devices was established in 2000, and was first known for its SlimServer used for streaming music, but launched a hardware player named SliMP3 able to play these streams in 2001. Although the first player was fairly simple only supporting wired Ethernet and MP3 natively, it was followed two years later by a slightly more advanced player which was renamed to Squeezebox. Other versions followed, gradually adding native support for additional file formats, Wi-Fi-support, gradually adding larger and more advanced displays as well as a version targeting audiophile users. Support for playing music from external streaming platforms such as Pandora, Napster, Last.fm and Sirius were also added. The devices in general have two operating modes; either standalone where the device connects to an internet streaming service directly, or to a local computer running the Logitech Media Server or a network-attached storage device. Both the server software and large parts of the firmware on the most recent players are released under open source licenses.

> In 2006, Slim Devices was acquired by Logitech for $20 million USD. Logitech continued the development of the player until they announced in August 2012 that it would be discontinued. Given the cross-platform nature of the server and software client, some users have ensured the continued use of the platform by utilizing the Raspberry Pi as dedicated Squeezebox device (both client and server).

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/squeezebox/).

## Table of Contents

<!-- MarkdownTOC -->

- [Binding Configuration](#binding-configuration)
	- [Example](#example)
- [Item Configuration](#item-configuration)
	- [Squeezebox commands](#squeezebox-commands)
	- [Squeezebox variables](#squeezebox-variables)
- [Examples](#examples)
	- [Full Example](#full-example)
	- [Additional Control of Logitech Media Server](#additional-control-of-logitech-media-server)
	- [Example for adding playlists](#example-for-adding-playlists)
	- [Example for displaying text](#example-for-displaying-text)
	- [More Examples](#more-examples)
- [Troubleshooting](#troubleshooting)

<!-- /MarkdownTOC -->


## Binding Configuration

This binding can be configured in the file `services/squeeze.cfg`.

| Key | Default | Required | Description |
|-----|---------|:--------:|-------------|
| server.host | |   Yes    | host (IP address) of your Logitech Media Server |
| server.retries | 3 | No  | number of retries to allow for a failed connection. |
| server.retryTimeout | 60 | No | timeout (in seconds) between retries of a failed connection to the Logitech Media Server |
| server.cliport | 9090 | No | port of the CLI interface of your Logitech Media Server |
| server.webport | 9000 | No | web port interface of the your Logitech Media Server |
| squeeze:ttsurl | `http://translate.google.com/translate_tts?tl=en&ie=UTF-8&client=openhab&q=%s` | No | TTS URL to use for generating text-to-speech voice announcements. The URL should contain one '%s' parameter which will be substituted with the text to be translated<br/>Another TTS service is http://www.voicerss.org/api/ which requires an API key): `https://api.voicerss.org/?key=YOURAPIKEYHERE&hl=en-gb&src=%s` |
| ttsmaxsentencelength | 100 | No | maximum TTS sentence length. For example, the Google TTS service only permits up to 100 characters (the default value). The Squeezebox speak action will break long strings into sentence chunks call the TTS service repeatedly |
| `<boxId1>`.id | | Yes    | MAC address of your first Squeezebox.  MAC addresses of players are case-sensitive. Use small letters (a-f) if the address contains them. Example: `de:ad:be:ef:12:34` |
| `<boxId1>`.id | | Yes    | MAC address of your nth Squeezebox.  MAC addresses of players are case-sensitive. Use small letters (a-f) if the address contains them. Example: `de:ad:be:ef:12:34` |

where `<boxIdN>` is a name you choose (like `bedroom` and `kitchen`) that will be used in both items and the action calls to select with which of your Squeezebox devices to communicate.  Do not use special characters for `<boxIdN>`.

### Example

First you need to let openHAB know where to find your Squeeze Server and each of your Squeezebox devices. This configuration is entered in your binding configuration file, and is used by both the Squeezebox binding and the Squeezebox Action:

```
server.host=192.168.1.129
bedroom.id=de:ad:be:ef:12:34
kitchen.id=ab:cd:ef:12:34:56
```

## Item Configuration

The syntax of an item configuration is shown in the following line in general:

```
squeeze="<boxId>:<command>[:<extra>]"
```

Where `<boxId>` matches one of the ids defined in your binding configuration, described above.

Examples:

Here are some examples of valid binding configuration strings:

```
squeeze="player1:volume"
squeeze="player1:title"
squeeze="player1:play"
```

### Squeezebox commands

Command           | Purpose
------------------|-------------------------
`power`           | Power on/off your device
`mute`            | Mute/unmute your device
`volume`          | Change volume by 5%
`play`            | Play the current title
`pause`           | Pause the current title
`stop`            | Stop the current title
`next`            | Skip to next title
`prev`            | Skip to previous title
`http:stream`     | Play the given http stream (obsolete as there is now a new squeezeboxPlayUrl() action for handling this inside rules directly)
`file:file`       | Play the given file on your server (obsolete as there is now a new squeezeboxPlayUrl() action for handling this inside rules directly)
`sync:player-id2` | Add `player-id2` to your device for synced playback

NOTE: when binding the 'play' command to a switch item you will trigger 'play' when the item receives the ON command. It will also trigger 'stop' when the item receives the OFF command. The same applies for 'stop' and 'pause' except ON=>stop/pause and OFF=>play. This is so you can setup a single item for controlling play/stop by defining mappings in your sitemap:

### Squeezebox variables

Variable      | Purpose
--------------|--------
`title`       | Title of the current song
`album`       | Album name of the current song
`artist`      | Artist name of the current song
`year`        | Release year of the current song
`genre`       | Genre name of the current song
`coverart`    | Address to cover art of the current song
`remotetitle` | Title of radio station currently playing
`ircode`      | String of the cached IR code

## Examples

### Full Example

items/squeezedemo.items

```
Dimmer sq_test_volume 	   "Volume [%.1f %%]"	{ squeeze="player1:volume" }
String sq_test_title	   "Title [%s]"			{ squeeze="player1:title" }
Switch sq_test_play	   	   "Play"				{ squeeze="player1:play" }
String sq_test_ircode	   "IR-Code [%s]" 		{ squeeze="player1:ircode" }
```

sitemaps/squeezedemo.sitemap.fragment

```
    Switch item=sq_test_play mappings=[ON="Play", OFF="Stop"]
```

And whenever the player state is changed from outside of openHAB, these items will be updated accordingly, since there is now no longer a separate item for 'play' and 'isPlaying'.

Squeezebox binding can store the latest IR code (form the infrared remote) in a variable, which can be used to do some actions. Look at this rule:

rules/squeezeirdemo.rules

```
rule "IR Code catched"
when
    Item sq_test_ircode received update
then
    if (sq_test_ircode.state=="00ff32cd") {
        sendCommand(Licht_Schlafzimmer, ON)
        logInfo("IR Code rules", "schalte Schlafzimmerlicht ein")
    } else if (sq_test_ircode.state=="00ff708f") {
        sendCommand(Licht_Schlafzimmer, OFF)
        logInfo("IR Code rules", "schalte Schlafzimmerlicht aus")
    }
end
```

### Additional Control of Logitech Media Server

Another method to gain some extra control of the LMS not provided by the binding is via HTTP GET requests. Using rules, a switch/number etc can be linked to the required HTTP GET request.

All available GET requests can be found on the LMS. 
In any browser enter `<IP Address of LMS>:9000`

Help button on the bottom left → Technical information → Logitech Media Server Web Interface
For the multiple player variable either the IP address or the MAC address can be used.

### Example for adding playlists

Item file:

```
Number Squeezebox_PlayList	"Playlists”
```

Rule file:

```
rule "Squeezebox_PlayList"
when
	Item Squeezebox_PlayList received command
then
	switch(receivedCommand) {
		case 0 : sendHttpGetRequest("http://<IP Address of LMS>:9000/?p0=playlist&p1=play&p2=<Name of Playlist>&player=<MAC Address of Player>")
		case 1 : sendHttpGetRequest("http://<IP Address of LMS>:9000/?p0=playlist&p1=play&p2=<Name of Next Playlist >&player=<MAC Address of Player>")
	}
end
```

Sitemap file fragment:

```
Selection item=Squeezebox_PlayList label="Start Playlist" mappings=[0="<Name of Playlist>", 1="<Name of Next Playlist >"] 
```

### Example for displaying text

Rule file:

```
rule "SqueezeboxDisplay"
when
	<any event>
then
	var String text= "Das ist ein Text mit variablem Inhalt: " + Item1.state.toString + 
	" und " + Item2.state.toString 
	var String url = "http://192.168.2.5:9000/status?p0=display&p1=&p2=" + text.encode("UTF-8") + "&p3=300&player=00:04:20:06:21:6d" 
            sendHttpGetRequest(url)
end
```

The is UTF-8 encoded. The URL calls the squeezebox server at port 9000 with this parameters: p1=upper display line, p2=lower display line, p3=duration of display, player= MAC address of player

### More Examples

![](https://dl.dropboxusercontent.com/u/1781347/wiki/2014-12-18%2017_13_05-openHAB.png)

Here you'll need to configure your players; please make sure that the id's match the name of your logitech configuration. Do not use special characters for the player id.

services/squeeze.cfg

```
server.host     = 192.168.10.59

Bad.id          = 00:04:20:28:65:c7
Bastelzimmer.id = 00:04:20:29:62:0e
Buero.id        = 00:04:20:29:a7:27
Kueche.id       = 00:04:20:28:65:91
Schlafzimmer.id = 00:04:20:2a:37:4b
TV.id           = 00:04:20:23:52:3c
Wohnbereich.id  = 00:04:20:2a:3b:21
```

items/squeeze.items

```
/* SqueezeBox */
Number squeezeSelectedPlayer
Number squeezeSelectedStation
Switch squeezePlay

Switch squeezeBadPower           "Bad" <squeeze> (gPlayerPower, gPlayerPowerOG) { squeeze="Bad:power" }
Switch squeezeBadPlay            "Bad"                                          { squeeze="Bad:play" }
Dimmer squeezeBadVolume          "Bad [%.1f %%]" <volume> (gPlayerVolume)       { squeeze="Bad:volume" }

Switch squeezeBastelzimmerPower  "Gästezimmer" <squeeze> (gPlayerPower, gPlayerPowerOG) { squeeze="Bastelzimmer:power" }
Switch squeezeBastelzimmerPlay   "Gästezimmer"                                          { squeeze="Bastelzimmer:play" }
Dimmer squeezeBastelzimmerVolume "Gästezimmer [%.1f %%]" <volume> (gPlayerVolume)       { squeeze="Bastelzimmer:volume" }

Switch squeezeBueroPower         "Büro" <squeeze> (gPlayerPower, gPlayerPowerOG) { squeeze="Buero:power" }
Switch squeezeBueroPlay          "Büro"                                          { squeeze="Buero:play" }
Dimmer squeezeBueroVolume        "Büro [%.1f %%]" <volume> (gPlayerVolume)       { squeeze="Buero:volume" }

Switch squeezeSchlafzimmerPower  "Schlafzimmer" <squeeze> (gPlayerPower, gPlayerPowerOG) { squeeze="Schlafzimmer:power" }
Switch squeezeSchlafzimmerPlay   "Schlafzimmer"                                          { squeeze="Schlafzimmer:play" }
Dimmer squeezeSchlafzimmerVolume "Schlafzimmer [%.1f %%]" <volume> (gPlayerVolume)       { squeeze="Schlafzimmer:volume" }

Switch squeezeTVPower            "TV" <squeeze> (gPlayerPower, gPlayerPowerEG, gTV) { squeeze="TV:power" }
Switch squeezeTVPlay             "TV"                                               { squeeze="TV:play" }
Dimmer squeezeTVVolume           "TV [%.1f %%]" <volume> (gPlayerVolume)            { squeeze="TV:volume" }

Switch squeezeKuechePower        "Küche" <squeeze> (gPlayerPower, gPlayerPowerEG) { squeeze="Kueche:power" }
Switch squeezeKuechePlay         "Küche"                                          { squeeze="Kueche:play" }
Dimmer squeezeKuecheVolume       "Küche [%.1f %%]" <volume> (gPlayerVolume)       { squeeze="Kueche:volume" }

Switch squeezeWohnbereichPower   "Wohnbereich" <squeeze> (gPlayerPower, gPlayerPowerEG) { squeeze="Wohnbereich:power" }
Switch squeezeWohnbereichPlay    "Wohnbereich"                                          { squeeze="Wohnbereich:play" }
Dimmer squeezeWohnbereichVolume  "Wohnbereich [%.1f %%]" <volume> (gPlayerVolume)       { squeeze="Wohnbereich:volume" }
```

rules/squeeze.rules

```Xtend
// Handle squeezebox radio station UI
rule "SqueezePlayerRadioStation"
when 
	Item squeezePlay changed or
	Item squeezeSelectedStation changed or
	Item squeezeSelectedPlayer changed
then
	logInfo("squeeze.rules", "SqueezePlayerPlay")
  
	var String [] players = newArrayList("Bad", "Bastelzimmer", "Buero", "Schlafzimmer", "TV", "Kueche", "Wohnbereich");
	var String[] urls = newArrayList(
	  "http://stream.srg-ssr.ch/drs1/mp3_128.m3u", // Radio SRF1
	  "http://stream.srg-ssr.ch/drs2/mp3_128.m3u", // Radio SRF2
	  "http://stream.srg-ssr.ch/drs3/mp3_128.m3u", // Radio SRF3
	  "http://www.swissgroove.ch/listen.m3u",      // Swiss Groove
	  "http://icecast.argovia.ch/argovia128.m3u",  // Radio Argovia
	  "http://stream.srg-ssr.ch/rsj/mp3_128.m3u",  // Swiss Jazz
	  "http://mp3-live.swr3.de/swr3_m.m3u"         // SWR 3
	  )

	logInfo("squeeze.rules", squeezeSelectedStation.toString)
	
	var stationIndex = ((squeezeSelectedStation.state as DecimalType).intValue - 1)
	var station = urls.get(stationIndex) as String;
	
	var playerIndex = ((squeezeSelectedPlayer.state as DecimalType).intValue - 1) 
	var player = players.get(playerIndex) as String
	
	logInfo("squeeze.rules", player)
	logInfo("squeeze.rules", station)
	 
	if (squeezePlay.state == ON) {
	  squeezeboxPlayUrl(player, station)
	} else {
	  squeezeboxStop(player)
	}

end
```

sitemaps/squeeze.sitemap

```
sitemap
{
	Frame label="System"  {
		Text label="Audio" icon="squeeze"  {
			Frame label="Alle"  {
				Switch item=gPlayerPowerAll label="EG & OG" 				
				Switch item=gPlayerPowerEG label="EG" 				
				Switch item=gPlayerPowerOG label="OG" 		
				Group item=gPlayerPower label="Ein / Aus"  {
					Switch item=squeezeBadPower           				
					Switch item=squeezeBastelzimmerPower  
					Switch item=squeezeBueroPower 	      
					Switch item=squeezeSchlafzimmerPower  
					Switch item=squeezeTVPower 			  
					Switch item=squeezeKuechePower 		  
					Switch item=squeezeWohnbereichPower   
				}
				Group item=gPlayerVolume label="Lautstärke"  {
					Slider item=squeezeBadVolume switchSupport 		    
					Slider item=squeezeBastelzimmerVolume switchSupport 
					Slider item=squeezeBueroVolume switchSupport 		
					Slider item=squeezeSchlafzimmerVolume switchSupport 
					Slider item=squeezeTVVolume switchSupport 		    
					Slider item=squeezeKuecheVolume switchSupport 		
					Slider item=squeezeWohnbereichVolume switchSupport 	
				}
				Slider item=gPlayerVolume label="Lautstärke" 				
			}			
			Frame label="Einzeln"  {				
				Selection item=squeezeSelectedPlayer label="Gerät" mappings=[1="Bad", 2="Gästezimmer", 3="Büro", 4="Schlafzimmer", 5="TV", 6="Küche", 7="Wohnbereich"]
				Selection item=squeezeSelectedStation label="Sender" mappings=[1="SRF 1", 2="SRF 2", 3="SRF 3", 4="Swiss Groove", 5="Argovia", 6="Swiss Jazz", 7="SWR 3"]
				Switch item=squeezePlay label="Stop / Play" mappings=[OFF="Stop", ON="Play"]
				
				Switch item=squeezeBadPower           visibility=[squeezeSelectedPlayer==1]				
				Switch item=squeezeBastelzimmerPower  visibility=[squeezeSelectedPlayer==2]
				Switch item=squeezeBueroPower 	      visibility=[squeezeSelectedPlayer==3]
				Switch item=squeezeSchlafzimmerPower  visibility=[squeezeSelectedPlayer==4]
				Switch item=squeezeTVPower 			  visibility=[squeezeSelectedPlayer==5]
				Switch item=squeezeKuechePower 		  visibility=[squeezeSelectedPlayer==6]
				Switch item=squeezeWohnbereichPower   visibility=[squeezeSelectedPlayer==7]
				
				Slider item=squeezeBadVolume switchSupport 		    visibility=[squeezeSelectedPlayer==1]
				Slider item=squeezeBastelzimmerVolume switchSupport visibility=[squeezeSelectedPlayer==2]
				Slider item=squeezeBueroVolume switchSupport 		visibility=[squeezeSelectedPlayer==3]
				Slider item=squeezeSchlafzimmerVolume switchSupport visibility=[squeezeSelectedPlayer==4]
				Slider item=squeezeTVVolume switchSupport 		    visibility=[squeezeSelectedPlayer==5]
				Slider item=squeezeKuecheVolume switchSupport 		visibility=[squeezeSelectedPlayer==6]
				Slider item=squeezeWohnbereichVolume switchSupport 	visibility=[squeezeSelectedPlayer==7] 			
			}		
		}		
	}	
}
```

## Troubleshooting

If you have also some issues with the example feel free to check [this discussion](https://community.openhab.org/t/wiki-squeezeboxexample-errors-cannot-cast-cannot-retrieve-item/5488).
