## Binding for LD382 LED controllers

This binding allows to operate cheap LD382 LED controllers in Openhab. Currently only V1.0.05 is supported. The newer version "LD382A" ist not supported yet.

# openhab.cfg

```
wifilight:LD382.host=<IP address of the controller>
wifilight:LD382.port=<Port of the controller. This is usually 5577
```

# items
```
Switch ldSwitch "An/Aus" {wifilight="LD382:0"}
Dimmer dimBlue "Blau" {wifilight="LD382:blue"}
Dimmer dimRed "Rot" {wifilight="LD382:red"} 
Dimmer dimGreen "Gruen" {wifilight="LD382:green"}
Dimmer dimWhite "Weiss" {wifilight="LD382:white"}
Color mlColor "Farbe" {wifilight="LD382:rgb"}
Number sceneLightWZ
Number sceneLightWZSeq
```
# sitemap
```
Frame {
	Switch item=sceneLightWZ label="Light scene" mappings=[1=S1, 2=S2, 3=S3, 4=S4] icon="color_rgb"
	Switch item=sceneLightWZSeq label="Next light scene" mappings=[1="Next light scene"] icon="color_rgb"
	Text label="LED Control" icon="living_room" {
		Slider item=dimBlue icon="color_blue"
		Slider item=dimRed icon="color_red"
		Slider item=dimGreen icon="color_green"
		Slider item=dimWhite 
		Colorpicker item=mlColor icon="color_rgb"
		Switch item=ldSwitch
	}
}	
```
# Icons
Find some extra icons in the 'icons' subfolder of the binding source. Copy them to 'webapps/images'

# example rule

This example demonstrates how to switch "scenes" with a simple button.

```
import org.openhab.core.library.types.*

rule "Light scene switch"
	when
		Item sceneLightWZ received command
	then
		if (receivedCommand == 1) {
			sendCommand(mlColor, "0,0,0")
			sendCommand(dimWhite, 100)
		} else if (receivedCommand == 2) {
			sendCommand(dimRed, 40)
			sendCommand(dimGreen, 20)
			sendCommand(dimBlue, 10)
			sendCommand(dimWhite, 100)			
		} else if (receivedCommand == 3) {
		    sendCommand(dimRed, 80)
			sendCommand(dimGreen, 10)
			sendCommand(dimBlue, 30)
			sendCommand(dimWhite, 100)	
		} else if (receivedCommand == 4) {
			sendCommand(dimRed, 100)
			sendCommand(dimGreen, 100)
			sendCommand(dimBlue, 70)
			sendCommand(dimWhite, 100)				
		}
end


rule "step-through-scences"
	when
		Item sceneLightWZSeq received command
	then
		var Number current = 0
		if (sceneLightWZ.state instanceof DecimalType) {
			current = sceneLightWZ.state as DecimalType
			if (current ==4) {current = 0}
		} else {
			current = 0
		}
		sceneLightWZ.sendCommand(current+1)	
end
```