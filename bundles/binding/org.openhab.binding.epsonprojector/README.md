Documentation of the Epson projector binding Bundle

## Introduction

Binding should be compatible with Epson projectors which support ESC/VP21 protocol over serial port.

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

First of all you need to introduce your Epson projector's in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    ################################# Epson Projector Binding ######################################
    
    # Serial port of the first Epson projector to control 
    # epsonprojector:<devId1>.serialPort=
    
    # Serial port of the second Epson projector to control 
    # epsonprojector:<devId2>.serialPort=

The `epsonprojector:<devId1>.serialPort` value is the identification of the serial port on the host system where projector is connected, e.g. "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac.

Examples, how to configure your receiver device:

    epsonprojector:hometheater.serialPort=/dev/tty.usbserial

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    epsonprojector="<direction><device-id>:<device-command>[:<update-interval>]"

where `<direction>` is one of the following values:
- < - for inbound communication
- > - for outbound communication
- `*` - for either inbound or outbound communication, e.g bi-directional

The `<device-id>` corresponds device which is introduced in openhab.cfg.

The `<device-command>` corresponds Epson projector command. See complite list below.

The `<update-interval>` corresponds update interval of the item. Update interval is only supported by the inbound and bi-directional items. Some of the commands, projector will only response when projector is running. This situation can be handle with special interval, where interval is started with "ON," prefix. 

Examples, how to configure your items:

    Switch Power { epsonprojector="hometheater:Power:60000" }
    String Source { epsonprojector="hometheater:Source:ON,60000" }
    Number LampTime { epsonprojector="<hometheater:LampTime:60000" }

# List of supported commands

| Command       | Item Type           | Purpose  | Note  |
| ------------- | ------------------- | -------- | ----- |
| Power | Switch |  |  | 
| PowerState | String |  |  | 
| LampTime | Number |  |  | 
| KeyCode | Number |  |  | 
| VerticalKeystone | Number |  |  | 
| HorizontalKeystone | Number |  |  | 
| AutoKeystone | Number |  |  | 
| AspectRatio | String |  |  | 
| Luminance | String |  |  | 
| Source | String |  |  | 
| DirectSource | Number |  |  | 
| Brightness | Number |  |  | 
| Contrast | Number |  |  | 
| Density | Number |  |  | 
| Tint | Number |  |  | 
| Sharpness | Number |  |  | 
| ColorTemperature | Number |  |  | 
| FleshTemperature | Number |  |  | 
| ColorMode | String |  |  | 
| HorizontalPosition | Number |  |  | 
| VerticalPosition | Number |  |  | 
| Tracking | Number |  |  | 
| Sync | Number |  |  | 
| OffsetRed | Number |  |  | 
| OffsetGreen | Number |  |  | 
| OffsetBlue | Number |  |  | 
| GainRed | Number |  |  | 
| GainGreen | Number |  |  | 
| GainBlue | Number |  |  | 
| Gamma | String |  |  | 
| GammaStep | Number |  |  | 
| Color | String |  |  | 
| Mute | Switch |  |  | 
| HorizontalReverse | Switch |  |  | 
| VerticalReverse | Switch |  |  | 
| Background | String |  |  | 
| ErrCode | Number |  |  | 
| ErrMessage | String |  |  | 

## ESC/VP21 Command User’s Guide for Home Projectors Projectors Projector

All information (command list, supported devices, ..) about Epsons ESC/VP21-Protocol can be found here:

http://www.epson.com/cgi-bin/Store/support/supAdvice.jsp?type=highlights&noteoid=184485

and

ftp://download.epson-europe.com/pub/download/3756/epson375633eu.xlsx

## Demo

epson.items

    Switch epsonPower                          { epsonprojector="hometheater:Power:60000" }
    String epsonSource        "Source [%s]"    { epsonprojector="hometheater:Source:ON,60000" }
    Number epsonDirectSource  "Direct Source"  { epsonprojector="hometheater:DirectSource:ON,60000"}
    
    Switch epsonMute               { epsonprojector="hometheater:Mute:ON,60000" }
    
    Switch epsonHorizontalReverse  { epsonprojector="hometheater:HorizontalReverse:ON,60000" }
    Switch epsonVerticalReverse    { epsonprojector="hometheater:VerticalReverse:ON,60000" }
    
    String epsonAspectRatio       "AspectRatio [%s]"        { epsonprojector="hometheater:AspectRatio:ON,60000" }
    String epsonColorMode         "ColorMode [%s]"          { epsonprojector="hometheater:ColorMode:ON,60000" }
    Number epsonColorTemperature  "Color Temperature [%d]"  <colorwheel>   { epsonprojector="<hometheater:ColorTemperature:ON,60000" }
    
    Number epsonLampTime    "Lamp Time [%d h]"  <switch>       { epsonprojector="<hometheater:LampTime:60000" }
    Number epsonErrCode     "ErrCode [%d]"      <"siren-on">   { epsonprojector="<hometheater:ErrCode:ON,60000" }
    String epsonErrMessage  "ErrMessage [%s]"   <"siren-off">  { epsonprojector="<hometheater:ErrMessage:ON,60000" }

epson.sitemap

    sitemap epson label="Epson Projector Demo"
    {
        Frame label="Controls" {
            Switch     item=epsonPower         label="Power"
            Text       item=epsonSource
            Selection  item=epsonDirectSource  label="DirectSource" mappings=[20="COMPONENT", 32="PC", 48=HDMI1, 160=HDMI2, 65=VIDEO, 66=SVIDEO]
            Switch     item=epsonMute label="Mute"
        }
        Frame label="Flip Projection" {
            Switch  item=epsonHorizontalReverse label="Horizontal Reverse"
            Switch  item=epsonVerticalReverse   label="Vertical Reverse"
        }
        Frame label="Info" {
            Text  item=epsonAspectRatio
            Text  item=epsonColorMode
            Text  item=epsonColorTemperature
            Text  item=epsonLampTime
            Text  item=epsonErrCode
            Text  item=epsonErrMessage
        }
    }

###Screenshot:

![alt text](http://wiki.openhab-samples.googlecode.com/hg/screenshots/epsonprojector_demo.png "Epson Projector demo")