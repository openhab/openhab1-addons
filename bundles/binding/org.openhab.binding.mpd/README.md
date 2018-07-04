# MPD Binding

[Music Player Daemon (MPD)](http://www.musicpd.org/) is a flexible, powerful, server-side application for playing music. Through plugins and libraries it can play a variety of sound files while being controlled by its network protocol. 

With the openHAB MPD binding you can start/stop playing music in specific rooms / on various channels, change volume, and even see which track is playing.

## Binding Configuration

This binding can be configured in the file `services/mpd.cfg`.


| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<player-id>`.host | |  Yes   | IP address or hostname of the MPD player |
| `<player-id>`.port | |  Yes   | Port number on which the MPD player is listening |

where `<player-id>` is a unique name that you give for the player, such as `livingroom`, `bath`, etc.

## Item Configuration

The syntax for the MPD binding configuration string is explained here:

```
mpd="<openHAB-command>:<player-id>:<player-commandLine>[,<openHAB-command>:<player-id>:<player-commandLine>][,...]"
```

where the parts in `[]` are optional.

The Player-Id corresponds with the configuration in `mpd.cfg` where one has to configure the MPDs:

## Examples

Here are some examples of valid binding configuration strings:

```
mpd="ON:bath:play, OFF:bath:stop"
mpd="INCREASE:bath:volume_increase, DECREASE:bath:volume_decrease"
```

As a result, your lines in the items file might look like the following:

```
Switch Mpd_Bathroom_StartStop	"Start/Stop"	(Bathroom)	{ mpd="ON:bad:play, OFF:bad:stop" }
```

### Track Names

Support for track names was added in 1.5 and also ability to set exact volume for dimmer items (previously only increase/decrease actions were available)

#### Items

```
String CurrentTrack    "Current track [%s]" { mpd="TITLE:bad:tracktitle" }
String CurrentArtist    "Current artist [%s]" { mpd="ARTIST:bad:trackartist" }
String ConcatInfo       "Now playing [%s]"

Switch Mpd_Bathroom_StartStop       "Start/Stop"   (Bathroom)  { mpd="ON:bad:play, OFF:bad:stop" }
Switch Mpd_Bathroom_NextPrev        "Track control"   (Bathroom)    { mpd="ON:bad:next, OFF:bad:prev" } 
Dimmer Mpd_Bathroom_VolumeControl   "Volume [%d%%]"       (Bathroom)  { mpd="INCREASE:bad:volume_increase, DECREASE:bad:volume_decrease, PERCENT:bad:volume" }
```

### Rules 

To concatenate artist and title:

```
rule "concat"
when
  Item CurrentTrack received update or
  Item CurrentArtist received update
then
  ConcatInfo.postUpdate(CurrentTrack.state.toString + " / " + CurrentArtist.state.toString)
end
```

#### Sitemap

```
Text item=CurrentTrack
Text item=CurrentArtist
Text item=ConcatInfo
Switch item=Mpd_Bathroom_StartStop mappings=[OFF="Pause", ON="Play"]
Switch item=Mpd_Bathroom_NextPrev  mappings=[OFF="Previous", ON="Next"]
Slider item=Mpd_Bathroom_VolumeControl
```

### Find song in MPD database by title and play this song

#### Items

```
Number PlayHappyBirthdays "Play Happy Birthday" <none> (All) { mpd="ON:playsong=HappyBirthdays" }
```

#### Sitemap

```
Switch item=PlayHappyBirthdays mappings=[ON="Play" ]
```

### Play song from current list

It can be used to choose radio station (streaming URI) from playlist defined.

###  Items

```
Number CurrentRadioStation "Radio" <none> (All) { mpd="NUMBER:p1:playsongid" }
```

### Sitemap

```
Selection item=CurrentRadioStation mappings=[1="ZET Chilli",2="Radio Kolor",3="Radio 7",4="Złote Przeboje",5="EskaROCK",6="RMF Classic",7="RMF Maxxx",11="ESKA",9="RMF",10="MUZO.FM",8="PR 3",12="TOK FM" ]
```
