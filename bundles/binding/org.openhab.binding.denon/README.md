## Introduction

This binding supports Denon AV receivers. It is available from openHAB 1.7.0 onwards. It should be compatible with previous generation receivers (AVR-X2000/X3000 etc) as well as the current ones (X2100W/X3100W). 

The binding seems to work with some [Marantz receivers](https://groups.google.com/d/msg/openhab/bSTEfSRt0RU/rOI6uWUe7bIJ) too.
## Configuration

```
# denon:<instance>.<property>=value

# IP adress of the Denon receiver instance
denon:avr2000.host=192.168.1.70

# Optional, set connection method for receiving updates. Can be http or telnet. 
# Denon receivers only support one concurrent telnet connection, so use http if 
# you have any other app using the telnet connection. Default = telnet
#denon:avr2000.update=telnet

# Optional, this sets the refresh interval (in milliseconds) for all instances 
# if you're using the http connection method. Default = 5000 
#denon:refresh=5000

```
## Items

All items use the following format:

```
{denon="instance#<property>"}
```

| Property | Type | Description | Accepts |
| :------------- |:-------------| :-----| :-----|
| **General** |
| PW | Switch | Main power | On, Off |
| ZM | Switch | Main zone power | On, Off |
| Z2 | Switch | Zone 2 power | On, Off |
| SURROUNDMODE | String | Current surround mode | Read only | 
| **Volume** |
| MV | Dimmer | Main volume | Percent, Increase/Decrease |
| MU | Switch | Mute | On, Off |
| Z2ZV | Dimmer | Zone 2 volume | Percent, Increase/Decrease |
| Z2MU | Switch | Zone 2 mute | On, Off |
| **Input** | 
| INPUT | String | Select main input | Input name | 
| SICD | Switch | Switch main input to CD | On |
| SITV | Switch | Switch main input to TV | On |
| Z2SOURCE | Switch | Switch zone 2 input to source | On |
| Z2TUNER | Switch | Switch zone 2 input to tuner | On |
| **Now playing**|
| TRACK | String | Current track that is playing, only available when playing something from network or USB. | Read only |
| ARTIST | String | Current artist | Read only |
| ALBUM | String | Current album | Read only |

Input mode names are different for different models. Check your control protocol documentation for the right values. The input mode switch commands in this table are not complete. The binding supports al SI* and Z2* commands. 

The inputs for main and 2nd zone can be switched by sending ON command to the corresponding switch. The main input can also be switched by setting the INPUT property to the input name (without SI, see examples). 

All zone commands also work on Zone 3 and Zone 4, if your receiver supports this (Z2MU -> Z3MU, etc.). 

## Advanced

A lot of other commands can also be sent. Define them as a Switch item and send the ON command. There is no feedback on their state however. See the control protocol documentation for a list of commands.

There is special property 'COMMAND' that forwards all Denon commands that are sent to it: 

```
String DenonCommand			"Command"				{denon="avr2000#COMMAND"}
```

This can be used in sitemaps:

```
Switch item=DenonCommand label="Surround Mode" mappings=[MSSTANDARD="Standard", MSSTEREO="Stereo"]
```

And scripts:

```
sendCommand(DenonCommand, "MNMEN ON")
```

## Control protocol documentation

- [AVR-X2000/E400](http://www2.aerne.com/Public/dok-sw.nsf/0c6187bc750a16fcc1256e3c005a9740/96a2ba120706d10dc1257bdd0033493f/$FILE/AVRX2000_E400_PROTOCOL(10.1.0)_V04.pdf)
- [AVR-X4000](http://assets.denon.com/documentmaster/de/avrx4000_protocol%2810.3.0%29_v01.pdf)
- [AVR-3311CI/AVR-3311/AVR-991](http://www.awe-europe.com/documents/Control%20Docs/Denon/Archive/AVR3311CI_AVR3311_991_PROTOCOL_V7.1.0.pdf)
- [CEOL Piccolo DRA-N5/RCD-N8](http://www.audioproducts.com.au/downloadcenter/products/Denon/CEOLPICCOLOBK/Manuals/DRAN5_RCDN8_PROTOCOL_V.1.0.0.pdf)
- [Marantz Control Protocol (2014+)](http://m.us.marantz.com/DocumentMaster/US/Marantz%202014%20NR%20Series%20-%20SR%20Series%20RS232%20IP%20Protocol.xls)

## Examples

Items

```
Switch DenonPower			"Power"					{denon="avr2000#PW"}
Switch DenonMainZone		"Main Zone"				{denon="avr2000#ZM"}
Dimmer DenonVolume			"Volume [%.1f]"			{denon="avr2000#MV"}
Switch DenonMute			"Mute"					{denon="avr2000#MU"}
String DenonSurroundMode	"Surround mode [%s]"	{denon="avr2000#SURROUNDMODE"}
String DenonCommand			"Command"				{denon="avr2000#COMMAND", autoupdate=false}

String DenonCurrentTrack		"Current track [%s]"		{denon="avr2000#TRACK"}
String DenonCurrentArtist		"Current artist [%s]"		{denon="avr2000#ARTIST"}
String DenonCurrentAlbum		"Current album [%s]"		{denon="avr2000#ALBUM"}

String DenonInput			"Input [%s]"			{denon="avr2000#INPUT"}
Switch DenonInputCD			"Input CD"				{denon="avr2000#SICD"}
Switch DenonInputUSBIPOD	"Input USB/IPOD"		{denon="avr2000#SIUSB/IPOD"}
Switch DenonInputNet		"Input Network"			{denon="avr2000#SINET"}
Switch DenonInputSpotify	"Input Spotify"			{denon="avr2000#SISPOTIFY"}
Switch DenonInputTuner		"Input Tuner"			{denon="avr2000#SITUNER"}
Switch DenonInputDVD		"Input DVD"				{denon="avr2000#SIDVD"}
Switch DenonInputBluray		"Input Bluray"			{denon="avr2000#SIBD"}
Switch DenonInputTV			"Input TV"				{denon="avr2000#SITV"}
Switch DenonInputSATCBL		"Input SAT/CBL"			{denon="avr2000#SISAT/CBL"}
Switch DenonInputMplay		"Input Mediaplayer"		{denon="avr2000#SIMPLAY"}
Switch DenonInputGame		"Input Game"			{denon="avr2000#SIGAME"}
Switch DenonInputAux1		"Input Aux1"			{denon="avr2000#SIAUX1"}

Switch DenonZoneTwo					"Zone 2"				{denon="avr2000#Z2"}
Dimmer DenonZoneTwoVolume			"Volume [%.1f]"			{denon="avr2000#Z2ZV"}
Switch DenonZoneTwoMute				"Zone 2 Mute"			{denon="avr2000#Z2MU"}

Switch DenonZoneTwoInputSource		"Zone 2 Input Source"	{denon="avr2000#Z2SOURCE"}
Switch DenonZoneTwoInputUSB			"Zone 2 Input USB"		{denon="avr2000#Z2USB/IPOD"}
Switch DenonZoneTwoInputTuner		"Zone 2 Tuner"			{denon="avr2000#Z2TUNER"}
```

Sitemap

```
Frame label="Main Zone" {
	Switch item=DenonPower
	Switch item=DenonMainZone
	Slider item=DenonVolume
	Setpoint item=DenonVolume minValue=0 maxValue=100 step=0.5
	Switch item=DenonMute
	Switch item=DenonCommand label="Surround Mode" mappings=[MSSTANDARD="Standard", MSSTEREO="Stereo"]
	Text item=DenonSurroundMode
}
Frame label="Main Zone Input" {
	Selection label="Input" item=DenonInput mappings=[CD="CD", TUNER="Tuner", DVD="DVD", BD="Bluray", TV="TV", "SAT/CBL"="SAT/CBL", MPLAY="Mediaplayer", GAME="Game", AUX1="Aux1"]
	Text item=DenonInput
	Switch item=DenonInputCD mappings=[ON="CD"]
	Switch item=DenonInputTuner mappings=[ON="Tuner"]
	Switch item=DenonInputDVD mappings=[ON="DVD"]
	Switch item=DenonInputBluray mappings=[ON="Bluray"]
	Switch item=DenonInputTV mappings=[ON="TV"]
	Switch item=DenonInputSATCBL mappings=[ON="SAT/CBL"]
	Switch item=DenonInputMplay mappings=[ON="Mediaplayer"]
	Switch item=DenonInputGame mappings=[ON="Game"]
	Switch item=DenonInputAux1 mappings=[ON="Aux1"]
}
Frame label="Title Info" visibility=[DenonCurrentTrack!="Undefined",DenonCurrentArtist!="Undefined"] {
	Text item=DenonCurrentTrack
	Text item=DenonCurrentArtist
	Text item=DenonCurrentAlbum
	Webview url="http://192.168.1.70/NetAudio/art.asp-jpg" height=9
}
Frame label="Zone 2" {
	Switch item=DenonZoneTwo
	Switch item=DenonZoneTwoMute
	Slider item=DenonZoneTwoVolume
	Switch item=DenonZoneTwoInputUSB mappings=[ON="USB"]
	Switch item=DenonZoneTwoInputSource mappings=[ON="Source"]
	Switch item=DenonZoneTwoInputTuner mappings=[ON="Tuner"]
}
```