# Milight Binding

The Milight binding allows openHAB to send commands to multiple Milight bridges.

[![openHAB Milight](http://img.youtube.com/vi/zNe9AkQbfmc/0.jpg)](http://www.youtube.com/watch?v=zNe9AkQbfmc)

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/milight/).

## Binding Configuration

This binding can be configured in the file `services/milight.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<MilightId1>`.host | | Yes   | the IP address of the first Milight bridge to control |
| `<MilightId1>`.port | 50000 | No | UDP port address of the first bridge to control. Has to be changed for the V3.0 version of milight bridge to 8899 |
| `<MilightId2>`.host | | Yes   | the IP address of the second Milight bridge to control |
| `<MilightId2>`.port | 50000 | No | UDP port address of the second bridge to control. Has to be changed for the V3.0 version of milight bridge to 8899 |

Examples, how to configure your receiver device:

```
bridge1.host=192.168.1.100
bridge1.port=8899
```

## Item Configuration

The syntax of the binding configuration strings accepted is the following:

```
    milight="<deviceId>;<channelNumber>;<commandType>;<steps>"
```

where:

* `<deviceId>` corresponds to the bridge which is defined in the configuration
* `<commandType>` is optional for switch items, unless you want to use commandType nightMode, whiteMode or set dimming steps.
* `<channelNumber>` corresponds to the bulbs/channels on your bridge, where 0 reflects all white bulbs, 1-4 white bulb channels and 5 all rgb bulbs.

For the new RGBW bulbs use channel number 6 (all RGBW bulbs), 7-10 (RGBW channels 1 to 4)

* `<deviceCommand>` corresponds to the way you want to control your Milight bulbs.

Valid command types for white bulbs:

| Command Type         | Description |
|----------------------|-------------|
| brightness           | controls the brightness of your bulbs |
| colorTemperature     | changes from cold white to warm white and vice versa |
| nightMode            | dimms your bulbs to a very low level to use them as a night light |

Command types for rgb bulbs:

| Command Type         | Description |
|----------------------|-------------|
| rgb                  | changes the color and brightness of your rgb bulbs |
| discoMode            | changes the discoMode for rgb bulbs |
| discoSpeed           | changes the speed of your chosen discoMode |

The following command is valid for RGBW bulbs only :


| Command Type         | Description |
|----------------------|-------------|
| whiteMode            | sets RGBW bulbs to white mode |


* `<steps>` is valid for RGBW bulbs only and has to be used with `commandType=brightness`.

This optional setting can be used if your RGBW bulbs are not dimming in 27 steps as this is the default [(See an archive of the API)](https://github.com/Fantasmos/LimitlessLED-DevAPI). 

## Limitations

The RGB bulbs do not support changing their saturation, so the colorpicker will only set the hue and brightness of it.

## Examples

### How to configure your items in your items file

```
Switch Light_Groundfloor    {milight="bridge1;0"}                   #Switch for all white bulbs on bridge1
Switch Light_GroundfloorN   {milight="bridge1;0;nightMode"}         #Activate the NightMode for all bulbs on bridge1
Dimmer Light_LivingroomB    {milight="bridge1;1;brightness"}        #Dimmer changing brightness for bulb1 on bridge1
Dimmer Light_LivingroomC    {milight="bridge1;1;colorTemperature"}  #Dimmer changing colorTemperature for bulb1 on bridge1
Dimmer RGBW_LivingroomB     {milight="bridge1;7;brightness;27"}     #Dimmer changing brightness for RGBW bulb1 on bridge1 with 27 dimming steps
Color Light_Party           {milight="bridge2;5;rgb"}               #Colorpicker for rgb bulbs at bridge2
```

The command types `discoMode` and `discoSpeed` should be configured as pushbuttons as they only support `INCREASE` and `DECREASE` commands:

items:

```
Dimmer DiscoMode        {milight="bridge1;5;discoMode"}
Dimmer DiscoSpeed       {milight="bridge1;5;discoSpeed"}
```

sitemap:

```
Switch item=DiscoMode mappings=[DECREASE='-', INCREASE='+']
Switch item=DiscoSpeed mappings=[DECREASE='-', INCREASE='+']
```

Disco Mode for RGBW bulbs can only be stepped in one direction, so please use INCREASE command only for those.


Example for Scenes:

items:

```
Number Light_scene      "Scenes"
Color  Light_scene_ColorSelect "Scene Selector"   <colorwheel> (MiLight)    { milight="bridge1;10;rgb" }
```

sitemap:

```
    Selection item=Light_scene mappings=[0="weiß", 1="rot", 2="gelb", 3="grün", 4="dunkelgrün", 5="cyan", 6="blau", 7="magenta"]
```

rules:

[https://en.wikipedia.org/wiki/HSL_and_HSV](https://en.wikipedia.org/wiki/HSL_and_HSV)

```
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
```
