# openhab-ulux-binding

This is a work-in-progress openHAB binding for u::Lux switches.

## Building the binding

You have to symlink both subdirectories into the `bundles/binding` directory of the main openHAB source tree
and add them to the `modules` section in `bundles/binding/pom.xml`.

## Configuring the binding

Insert into your `openhab.cfg`

	# needed for every switch, mapping from switch id to IP address
	#ulux:switch.x=x.x.x.x
	ulux:switch.61=192.168.1.61

	# not used at the moment
	#ulux:designId=1

	# not used at the moment
	#ulux:projectId=1

	# should not be needed
	#ulux:bind_address=10.0.0.27

## Configuring your items

The configuration string consists of switch id, actor id and message name (default is EditValue).

### Switching an actor on and off

Send ON or OFF to a switch item with message type "EditValue" (which is the default).

`Switch Ulux_Switch  "Switch"     <none> { ulux="1:2:EditValue" }`

or simply

`Switch Ulux_Switch  "Switch"     <none> { ulux="1:2" }`

### Key events

`Switch Ulux_Key_1  "Key 1"     <none> { ulux="1:1:Key:1" }`
`Switch Ulux_Key_2  "Key 2"     <none> { ulux="1:1:Key:2" }`
`Switch Ulux_Key_3  "Key 3"     <none> { ulux="1:1:Key:3" }`
`Switch Ulux_Key_4  "Key 4"     <none> { ulux="1:1:Key:4" }`

### Playing an audio file stored on the switch

Send the index of the audio file (starting at 1) to a number item with message type "AudioPlayLocal". 

`Number Ulux_Audio   "Audio"      <none> { ulux="1:0:AudioPlayLocal"}`

### Audio playback or recording status and stopping

If an audio file is played back or the recording function is active (which is not yet supported
by the binding) this item switches to ON. Send an OFF if you want to stop it.

`Switch Ulux_Audio    "Audio"  <none> { ulux="1:0:Audio" }`

### Audio volume

`Dimmer Ulux_AudioVolume    "Audio Volume [%d]"  <none> { ulux="1:0:AudioVolume" }`

### Display state and switching the display on and off

Send ON or OFF to a switch item of type "Display". It's probably best to configure the
item with auto-update off as the switch immediately sends back its state.

`Switch Ulux_Display "Display"    <none> { ulux="1:0:Display", autoupdate="false" }`

### Proximity sensor

`Switch Ulux_Proximity  "Proximity"  <none> { ulux="1:0:Proximity" }`

### Ambient light sensor

ON if it's bright, OFF if it's dark.

`Switch Ulux_AmbientLight  "Ambient light"  <none> { ulux="1:0:AmbientLight" }`

### The page displayed on the switch

`Number Ulux_Page    "Page [%d]"  <none> { ulux="1:0:PageIndex" }`

### Displaying a decimal value, e.g. temperature

`Number Ulux_Value   "Value [%d]" <none> { ulux="1:1:EditValue" }`

