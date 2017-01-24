Documentation of the Milight binding bundle

## Introduction

[![openHAB Milight](http://img.youtube.com/vi/zNe9AkQbfmc/0.jpg)](http://www.youtube.com/watch?v=zNe9AkQbfmc)

The openHAB Milight binding allows to send commands to multiple Milight bridges.
For installation of the binding, please see Wiki page [[Bindings]].

##API v3.0 additions for RGBW bulbs are available with openHAB release 1.4.0

## Milight Binding Configuration in openhab.cfg

First of all you need to introduce your Milight bridge(s) in the openhab.cfg file (in the folder '${openhab_home}/configurations')

### Example

    ################################ Milight Binding #################################
    
    # Host of the first Milight bridge to control 
    # milight:<MilightId1>.host=
    # Port of the bridge to control (optional, defaults to 50000)
    # milight:<MilightId1>.port=
    #
    # Host of the second Milight bridge to control 
    # milight:<MilightId2>.host=
    # Port of the bridge to control (optional, defaults to 50000)
    # milight:<MilightId2>.port=

The `milight:<MilightId1>.host` value is the ip address of the Milight bridge.

The `milight:<MilightId1>.port` value is UDP port address of the Milight bridge. Port value is an optional parameter but has to be changed for the V3.0 version of milight bridge to 8899.

Examples, how to configure your receiver device:

    milight:bridge1.host=192.168.1.100
    milight:bridge1.port=50000

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    milight="<deviceId>;<channelNumber>;<commandType>;<steps>"
where `<commandType>` is optional for switch items, unless you want to use commandType nightMode, whiteMode or set dimming steps.


The device-id corresponds to the bridge which is defined in openhab.cfg.

The channelNumber corresponds to the bulbs/channels on your bridge, where 0 reflects all white bulbs, 1-4 white bulb channels and 5 all rgb bulbs.
For the new RGBW bulbs use channel number 6 (all RGBW bulbs), 7-10 (RGBW channels 1 to 4)

The deviceCommand corresponds to the way you want to control your Milight bulbs.

Valid command types for white bulbs:

    brightness 			controls the brightness of your bulbs
    colorTemperature	changes from cold white to warm white and vice versa
    nightMode 			dimms your bulbs to a very low level to use them as a night light

Command types for rgb bulbs:

	rgb	             	changes the color and brightness of your rgb bulbs
	discoMode           changes the discoMode for rgb bulbs
	discoSpeed	     	changes the speed of your chosen discoMode

The following command is valid for RGBW bulbs only :

    whiteMode 			sets RGBW bulbs to white mode

`<steps>` is valid for RGBW bulbs only and has to be used with `commandType=brightness`.
This optional setting can be used if your RGBW bulbs are not dimming in 27 steps as this is the default [(See the API)](http://www.limitlessled.com/dev/). 

Limitations:
The rgb bulbs do not support changing their saturation, so the colorpicker will only set the hue and brightness of it.

Examples, how to configure your items in your items file:

    Switch Light_Groundfloor 	{milight="bridge1;0"}					#Switch for all white bulbs on bridge1
    Switch Light_GroundfloorN	{milight="bridge1;0;nightMode"}			#Activate the NightMode for all bulbs on bridge1
    Dimmer Light_LivingroomB 	{milight="bridge1;1;brightness"}		#Dimmer changing brightness for bulb1 on bridge1
    Dimmer Light_LivingroomC 	{milight="bridge1;1;colorTemperature"}	#Dimmer changing colorTemperature for bulb1 on bridge1
    Dimmer RGBW_LivingroomB 	{milight="bridge1;7;brightness;27"}		#Dimmer changing brightness for RGBW bulb1 on bridge1 with 27 dimming steps
    Color Light_Party			{milight="bridge2;5;rgb"}				#Colorpicker for rgb bulbs at bridge2

The command types discoMode and discoSpeed should be configured as pushbuttons as they only support INCREASE and DECREASE commands:

items:

    Dimmer DiscoMode		{milight="bridge1;5;discoMode"}
    Dimmer DiscoSpeed		{milight="bridge1;5;discoSpeed"}
    
sitemap:

    Switch item=DiscoMode mappings=[DECREASE='-', INCREASE='+']
    Switch item=DiscoSpeed mappings=[DECREASE='-', INCREASE='+']

Disco Mode for RGBW bulbs can only be stepped in one direction, so please use INCREASE command only for those.


Example for Scenes:

items:

    Number Light_scene		"Scenes"
    Color  Light_scene_ColorSelect "Scene Selector"   <colorwheel> (MiLight)	{ milight="bridge1;10;rgb" }


sitemap:

    Selection item=Light_scene mappings=[0="weiß", 1="rot", 2="gelb", 3="grün", 4="dunkelgrün", 5="cyan", 6="blau", 7="magenta"]
    
rules:
[https://en.wikipedia.org/wiki/HSL_and_HSV](https://en.wikipedia.org/wiki/HSL_and_HSV)

    rule "Light Scenes"
    when
	Item Light_scene received command 
    then
	if (receivedCommand==0) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(0),new PercentType(0),new PercentType(100)))
	}
	if (receivedCommand==1) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(0),new PercentType(100),new PercentType(100)))
	}
	if (receivedCommand==2) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(60),new PercentType(100),new PercentType(100)))
	}
	if (receivedCommand==3) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(120),new PercentType(100),new PercentType(100)))
	}
	if (receivedCommand==4) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(120),new PercentType(100),new PercentType(50)))
	}
	if (receivedCommand==5) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(180),new PercentType(100),new PercentType(100)))
	}
	if (receivedCommand==6) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(240),new PercentType(100),new PercentType(100)))
	}
	if (receivedCommand==7) { 
		sendCommand(Light_scene_ColorSelect, new HSBType(new DecimalType(300),new PercentType(100),new PercentType(100)))
	}
    end
  