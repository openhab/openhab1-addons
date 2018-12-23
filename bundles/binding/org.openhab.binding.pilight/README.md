# pilight Binding

The pilight binding allows openHAB to communicate with a [pilight](http://www.pilight.org/) instance running pilight version 6.0 or greater.

> pilight is a free open source full fledge domotica solution that runs on a Raspberry Pi, HummingBoard, BananaPi, Radxa, but also on *BSD and various linuxes (tested on Arch, Ubuntu and Debian). It's open source and freely available for anyone. pilight works with a great deal of devices and is frequency independent. Therefor, it can control devices working at 315Mhz, 433Mhz, 868Mhz etc. Support for these devices are dependent on community, because we as developers don't own them all.

pilight is a cheap way to control 'Click On Click Off' devices. It started as an application for the Raspberry Pi (using the GPIO interface) but it's also possible now to connect it to any other PC using an Arduino Nano. You will need a cheap 433Mhz transceiver in both cases. See the [Pilight manual](https://manual.pilight.org/electronics/wiring.html) for more information.

The binding supports Switch, Dimmer, Contact, String, and Number items.

## Binding Configuration

This binding can be configured in the file `services/pilight.cfg`.

Multiple pilight instances are supported. You must set the .host and .port values for each. In pilight, 

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<instance>`.host | |   Yes   | IP address of the pilight daemon |
| `<instance>`.port | |   Yes   | port of the pilight daemon. You must explicitly configure the port or otherwise a random port will be used and the binding will not be able to connect.   For example, 5000. |
| `<instance>`.delay | |  No    | optional delay (in millisecond) between consecutive commands.  Recommended value without band pass filter: 1000. Recommended value with band pass filter: somewhere between 200-500 |

where `<instance>` is a name you choose, being a simple word that you will reference from your item configurations.

## Item Configuration

The following syntax is supported since version 1.7:

```
pilight="<instance>#<device>,property=value"
```

where `<instance>` is one of the instances you defined in the binding configuration, and `<device>` is the same as specified in your pilight `config.json` file. The `<property>=<value>` part is only needed for String and Number items.  See the examples below.

## Examples

items/pilight.items

```
Switch  KakuDeskLamp    "Desk lamp"             (Lamps)         {pilight="kaku#desklamp"}
Switch  KakuFloorLamp   "Floor lamp"            (Lamps)         {pilight="kaku#floorlamp"}

Dimmer  KakuCeiling     "Ceiling"               (Lamps)         {pilight="kaku#ceiling"}

Number  KakuTemperature  "Temperature"           (Sensors)      {pilight="kaku#weather,property=temperature"}
Number  KakuHumidity     "KakuHumidity [%.0f%%]" (Sensors)      {pilight="kaku#weather,property=humidity"}
String  KakuBattery      "Battery [%s]"          (Sensors)      {pilight="kaku#weather,property=battery"}

Contact KakuDoorSensor  "Door sensor [%s]"      (Sensors)       {pilight="kaku#doorsensor"}
```

sitemaps/fragment.sitemap

```
Switch item=KakuDeskLamp
Switch item=KakuFloorLamp
Slider item=KakuCeiling
Text item=KakuTemperature
Text item=KakuHumidity
Text item=KakuBattery
Text item=KakuDoorSensor
```

## Additional info

For more information/questions:

- [Dutch pilight support thread](http://gathering.tweakers.net/forum/list_messages/1581828/4)
