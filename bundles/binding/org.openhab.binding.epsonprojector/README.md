# Epson Projector Binding

This binding should be compatible with Epson projectors which support ESC/VP21 protocol over serial port.

## Binding Configuration

This binding can be configured in the file `services/epsonprojector.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<device-id>`.serialPort | | Yes | serial port device name that is connected to the Epson projector to control, e.g. `COM1` on Windows, `/dev/ttyS0` on Linux or `/dev/tty.PL2303-0000103D` on Mac |

where `<device-id>` is a name you choose (like `hometheater`), and this configuration property can be repeated with different names to control more than one Epson projector.

Examples:

```
hometheater.serialPort=/dev/tty.usbserial
office.serialPort=/dev/ttyS0
```

### Using tcp instead of serialPort

Alternatively you can connect to your projector via tcp/telnet if you are using some kind of serial2IP converter.  
Like: https://community.openhab.org/t/benq-beamer-binding-rs232-over-wifi-ir-receiver/26138/4

In that case the configuration looks like this:

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<device-id>`.host | | Yes | IP address of your converter |
| `<device-id>`.port | | Yes | Port the device is listening on. e.g. `23` |

Examples:

```
hometheater.host=192.168.1.234
hometheater.port=23
```

If you are switching from serial to TCP you might need to delete the cache (`/var/lib/openhab2/config/org/openhab/epsonprojector.config`).

## Item Configuration

The syntax is:

```
epsonprojector="<direction><device-id>:<device-command>[:<update-interval>]"
```

where:

* `<direction>` is one of the following values:
 * `<` - for inbound communication
 * `>` - for outbound communication
 * `*` - for either inbound or outbound communication, e.g bi-directional

* `<device-id>` corresponds device which is introduced in the binding configuration

* `<device-command>` corresponds to the Epson projector command. See complete list below.

* `<update-interval>` is optional, and corresponds to the update interval of the item. Update interval is only supported by the inbound and bi-directional items. For some of the commands, the projector will only respond when projector is running. This situation can be handled with special interval, where interval is started with "ON," prefix. 

Examples, how to configure your items:

```
Switch Power    { epsonprojector="hometheater:Power:60000" }
String Source   { epsonprojector="hometheater:Source:ON,60000" }
Number LampTime { epsonprojector="<hometheater:LampTime:60000" }
```

### List of supported commands

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

### References

* [ESC/VP21 Command User’s Guide for HomeProjectors](http://files.support.epson.com/pdf/pltw1_/pltw1_cm.pdf)
* [ESC/VP21 Command User’s Guide](ftp://download.epson-europe.com/pub/download/3756/epson375633eu.xlsx)

## Full Example

items/epson.items

```
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
```

sitemaps/epson.sitemap

```
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
```
