## Binding for LD382 LED controllers



# config

#
wifilight:LD382.host=192.168.0.205
wifilight:LD382.port=5577


# items

Dimmer dimBlue    {wifilight="LD382:blue"}
Switch mlSwitch    {wifilight="LD382:0"}
Color mlColor      {wifilight="LD382:rgb"}

# sitemap

Text label="Wohnzimmer" icon="living_room" {
			Slider item=dimBlue
			Switch item=mlSwitch
			Colorpicker item=mlColor icon="slider"
            }