# openhab-ulux-binding

This is a work-in-progress openHAB binding for u::Lux switches.

## `openhab.cfg`

	# needed for every switch, mapping from switch id to IP address
	#ulux:switch.x=x.x.x.x
	ulux:switch.61=192.168.1.61

	# not used at the moment
	#ulux:designId=1

	# not used at the moment
	#ulux:projectId=1

	# should not be needed
	#ulux:bind_address=10.0.0.27

## Example item configurations

The configuration string consists of switch id, actor id and message name (default is EditValue).

### Playing an audio file stored on the switch

Send the index of the audio file (starting at 1) to a number item with message type "AudioPlayLocal". 

`Number Ulux_Audio   "Audio"      <none> { ulux="1:0:AudioPlayLocal"}`

### Switching the display on and off

Send ON or OFF to a switch item with the message type "Activate".

`Switch Ulux_Display "Display"    <none> { ulux="1:0:Activate" }`

### Other examples

`Number Ulux_Page    "Page [%d]"  <none> { ulux="1:0:PageIndex" }`
`Number Ulux_Value   "Value [%d]" <none> { ulux="1:1:EditValue" }`
`Switch Ulux_Switch  "Switch"     <none> { ulux="1:2" }`
