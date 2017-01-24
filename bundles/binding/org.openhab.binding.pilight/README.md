Documentation for the pilight binding

## Introduction

This page describes the pilight binding, which allows openhab to communicate with a [pilight](http://www.pilight.org/) instance:

> pilight is a free open source full fledge domotica solution that runs on a Raspberry Pi, HummingBoard, BananaPi, Radxa, but also on *BSD and various linuxes (tested on Arch, Ubuntu and Debian). It's open source and freely available for anyone. pilight works with a great deal of devices and is frequency independent. Therefor, it can control devices working at 315Mhz, 433Mhz, 868Mhz etc. Support for these devices are dependent on community, because we as developers don't own them all.

pilight is a cheap way to control 'Click On Click Off' devices. It started as an application for the Raspberry Pi (using the GPIO interface) but it's also possible now to connect it to any other PC using an Arduino Nano. You will need a cheap 433Mhz transceiver in both cases. See the [Pilight manual](http://manual.pilight.org/en/electronics-wiring) for more information.

There are some differences between version 1.6 and 1.7 of this binding:

| openHAB version | Supported pilight version | Supported Items
|-----------------|-----------------|-----------------|
| 1.6 | 5.0 | Switch, Dimmer, String, Number
| 1.7 | >= 6.0 | Switch, Dimmer, Contact, String, Number

In openHAB 1.6, pilight version 5 is supported. In the latest version of openHAB (1.7), only pilight versions 6 and later supported. Make sure you're running the right version of pilight, since the API between those two versions is quite different. 

### Installation 

- Copy the pilight binding jar to your openhab addons directory
- Configure the pilight daemon in openhab.cfg
- Add controlable items to openhab .items file 
- Use items in sitemap 

### Configuration openhab.cfg

This binding supports multiple pilight instances. You must set the .host and .port values. In pilight, you must explicitly configure this "port" or otherwise a random port will be used and the binding will not be able to connect. 

```
#
# pilight:<instance name>.<parameter>=<value>
#
# IP address of the pilight daemon 
#pilight:kaku.host=192.168.1.22
#
# Port of the pilight daemon
#pilight:kaku.port=5000
#
# Optional delay (in millisecond) between consecutive commands. 
# Recommended value without band pass filter: 1000 
# Recommended value with band pass filter: somewhere between 200-500 
#pilight:kaku.delay=1000
```

### Item configuration

**openHAB 1.6**

    pilight="<instance>#<room>:<device>,property=value"

Room and device are the same as specified in your pilight config.json. The 'property=value' part is only needed for String and Number items. 

Examples:

```
Switch  KakuDeskLamp    "Desk lamp"             (Lamps)         {pilight="kaku#study:desklamp"}
Switch  KakuFloorLamp   "Floor lamp"            (Lamps)         {pilight="kaku#study:floorlamp"}

Dimmer  KakuCeiling     "Ceiling"               (Lamps)         {pilight="kaku#living:ceiling"}

Number  KakuTemperature  "Temperature"           (Sensors)      {pilight="kaku#outside:weather,property=temperature"}
Number  KakuHumidity     "KakuHumidity [%.0f%%]" (Sensors)      {pilight="kaku#outside:weather,property=humidity"}
String  KakuBattery      "Battery [%s]"          (Sensors)      {pilight="kaku#outside:weather,property=battery"}
```

**openHAB 1.7**

    pilight="<instance>#<device>,property=value"

Device names are the same as specified in your pilight config.json. The 'property=value' part is only needed for String and Number items. 

Examples:

```
Switch  KakuDeskLamp    "Desk lamp"             (Lamps)         {pilight="kaku#desklamp"}
Switch  KakuFloorLamp   "Floor lamp"            (Lamps)         {pilight="kaku#floorlamp"}

Dimmer  KakuCeiling     "Ceiling"               (Lamps)         {pilight="kaku#ceiling"}

Number  KakuTemperature  "Temperature"           (Sensors)      {pilight="kaku#weather,property=temperature"}
Number  KakuHumidity     "KakuHumidity [%.0f%%]" (Sensors)      {pilight="kaku#weather,property=humidity"}
String  KakuBattery      "Battery [%s]"          (Sensors)      {pilight="kaku#weather,property=battery"}

Contact KakuDoorSensor  "Door sensor [%s]"      (Sensors)       {pilight="kaku#doorsensor"}
```

### Usage in sitemap

```
Switch item=KakuDeskLamp
Switch item=KakuFloorLamp
Slider item=KakuCeiling
Text item=KakuTemperature
Text item=KakuHumidity
Text item=KakuBattery
Text item=KakuDoorSensor
```

### Additional info

For more information/questions:

- [Dutch pilight support thread](http://gathering.tweakers.net/forum/list_messages/1581828/4)