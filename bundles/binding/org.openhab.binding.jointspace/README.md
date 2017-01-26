# jointSPACE Binding

The jointSPACE binding lets you control your Philips TV that is compatible with the [jointSPACE JSON API](http://jointspace.sourceforge.net/projectdata/documentation/jasonApi/index.html) over Ethernet. 

It allows you to:

* Send Button commands
* Set and Read Volume/Mute
* Set and Get Colors of Ambilight "Pixels"
* Set and Read Source

## Prerequisites

1. The TV has to be in the network and turned on.
1. The TV has to be compatible with the API. This should be possible for all models since 2011, but a list of models and firmwares can be found [here](http://jointspace.sourceforge.net/download.html)
1. If the TV is compatible and the newest firmware is installed, the API has to be activated. Therefore, you have to input on the remote "5646877223" (which spells jointspace on the digits) while watching TV. A popup should appear saying that the activation was successful. 
1. To check it if everything works correctly, you can browse to `http://<ip-of-your-TV>:1925/1/examples/audio/volume.html`. There you should see a page allowing you to change the volume of the TV.

## Binding Configuration

This binding can be configured in the file `services/jointspace.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refreshInterval | 60000 | No  | refresh interval, in milliseconds, of the worker thread. Polling interval for Source, Volume, Ambilight Color, ... |
| ip       |         |   Yes    | IP address of the jointspace enabled device |
| port     | 1925    |   No     | Port of the jointspace API. |


## Item Configuration

All binding configurations have the structure 

```
jointspace="<openhab-command>:<jointspace-command>"
```

`<openhab-command>` can be for example:

* `ON`, `OFF` for Switch items
* `1,2,3,...` for Number items
* `HSB` react on HSB type commands
* `DEC` react on all Decimal type commands
* `*` react on all commands
* `POLL` poll the value periodical in the worker thread

Every time the binding receives such a command for that item, it executes (or forwards) the command to the TV.

For valid `<jointspace-command>` refer to the separate sections and to the samples below.

### Remote

In order to send a button of the remote use `key.X` where `X` can be for example:

* `Digit1`
* `VolumeUp`
* `Standby`

A full list of supported keys refer to [here](http://jointspace.sourceforge.net/projectdata/documentation/jasonApi/1/doc/API-Method-input-key-POST.html)

### Volume

* To set the volume send a decimal type to a item configured as `volume`
* To poll the volume use `POLL:volume` and the mute state as `POLL:volume.mute`

### Source

* Get the current source as a string item with `POLL:source`
* Set the current source with `source.X` where X can be `hdmi1`, `tv`, ...

### Ambilight

#### Ambilight Mode

Ambilight has three different modes:

* `manual`: Lets you set the ambilight values directly over the API 
* `internal`: Uses the colors specified from the internal algorithm based on the image shown
* `expert`: Uses the manually sets pixels as input for the algorithm. Don't know if this is really of use.

In order to set the mode use as a jointspace-command `ambilight.mode.X`

#### Ambilight Color

**In order to set the color, the ambilight mode has to be set to `manual` first**

Please refer to the examples below in order to see how to setup different ambilight pixel combination.

#### Layer

The ambilight pixels are grouped in different layers, for example `left` for all left pixels. In order to find out which layers are supported goto `http://<ip-adress>:1925/1/examples/ambilight/ambilight.html` or refer to [here](http://jointspace.sourceforge.net/projectdata/documentation/jasonApi/1/doc/API-Method-ambilight-cached-POST.html)

## Examples

```
/* Demo items */
Switch MuteSwitch				"Mute" <settings> {jointspace="ON:key.Mute, OFF:key.Mute, POLL:volume.mute"}
Switch Taste1					"Taste1" {jointspace="ON:key.Digit1, OFF:key.Digit2"}
Dimmer VolumeTV 				"Volume [%d]"		<slider> {jointspace="*:volume, POLL:volume"}
Switch ActivateAmbilight		"Ambilight Manipulation" <settings> 		 {jointspace="ON:ambilight.mode.manual, OFF:ambilight.mode.internal"}
Number Ambilight_mode			"Ambilight Mode"	    <settings>	 {jointspace="0:ambilight.mode.internal, 1:ambilight.mode.manual, 2:ambilight.mode.expert"}
Color  AmbilightAll			    "Ambilight All"			<colorwheel> {jointspace="HSB:ambilight.color"}
Color  AmbilightLeft			"Ambilight Left"		<colorwheel> {jointspace="HSB:ambilight[layer1[left]].color"}
Color  AmbilightRight			"Ambilight Right"		<colorwheel> {jointspace="HSB:ambilight[layer1[right]].color"}
Color  AmbilightPixelLeft		"Ambilight PixelLeft"	<colorwheel> {jointspace="HSB:ambilight[layer1[left[0]]].color, POLL:ambilight[layer1[left[0]]].color"}

Switch Standby "Standby" {jointspace="*:key.Standby"}
Switch VolumeUpDown "VolumeUpDown" {jointspace="ON:key.VolumeUp, OFF:key.VolumeDown"}
Switch ChannelStep "ChannelUpDown" {jointspace="ON:key.ChannelStepUp, OFF:key.ChannelStepDown"}
Switch AmbilightOnOff "AmbilightOnOff" {jointspace="*:key.AmbilightOnOff"}
Switch Source "Source" {jointspace="*:key.Source"}
Switch PlayPause "PlayPause" {jointspace="*:key.PlayPause"}

String CurrentSource "Source" {jointspace="POLL:source"}
Number SetSource "Set Source" {jointspace="1:source.tv, 2:source.hdmi1, 3:source.hdmi2, 4:source.hdmi3, 5:source.hdmiside"}
```

## Known Limitations

* The TV has to be on to control it. This is a limitation as the network interface is off for the TV in standby. Thus it is not possible to turn the TV on with this Binding
* Right now only one device with jointspace capabilities is supported
* Reading ambilight color values only works for specific pixels, but not for layers
* There is an ambilight bug in some models (including mine), that setting manual colors doesn't work for the right side

