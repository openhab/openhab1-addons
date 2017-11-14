# Anel NET-PwrCtrl Binding 

Monitor and control Anel NET-PwrCtrl devices.

| [Anel NET-PwrCtrl HUT](http://anel-elektronik.de/SITE/produkte/hut/hut.htm) | [Anel NET-PwrCtrl IO](http://anel-elektronik.de/SITE/produkte/io/io.htm) | [Anel NET-PwrCtrl HOME](http://anel-elektronik.de/SITE/produkte/home/home.htm) |
| --- | --- | --- |
| [![Anel NET-PwrCtrl HUT](http://anel-elektronik.de/SITE/image/leisten/HUT-120.gif)](http://anel-elektronik.de/SITE/produkte/hut/hut.htm) | [![Anel NET-PwrCtrl PRO](http://anel-elektronik.de/SITE/image/leisten/PRO-120.gif)](http://anel-elektronik.de/SITE/produkte/io/io.htm) | [![Anel NET-PwrCtrl HOME](http://anel-elektronik.de/SITE/image/leisten/HOME-DE-120.gif)](http://anel-elektronik.de/SITE/produkte/home/home.htm)

NET-PwrCtrl devices are power sockets / relays that can be configured via browser but they can also be controlled over the network, e.g. with an Android or iPhone app - and also with openHAB via this binding.
The NET-PwrCtrl HUT and NET-PwrCtrl IO also have 8 I/O pins which can either be used to directly switch the sockets, or they can be used as general input switches in openHAB.
Here is a video demonstrating a switch and a dimmer (voice is German), explanation of the setup is given in the diagram below:

[![Anel example](http://img.youtube.com/vi/31ycP53jZVs/0.jpg)](http://www.youtube.com/watch?v=31ycP53jZVs)

[Anel demo setup](http://2.bp.blogspot.com/-XbiK9Fe1Ek0/VFPc2lwMKeI/AAAAAAAABDM/wEdTETUfo0w/s1600/Anel-demo-setup.png)

**Note that the binding is still untested for other devices than the _NET-PwrCtrl HUT_, because I do not own any of the others. I suppose the binding works well with the _NET-PwrCtrl IO_ because it has the same features, but it may not yet work for the others!**

## Binding Configuration

The binding can be configured in the file `services/anel.cfg`.  In the table below, `<name>` must be an identifier that is also used for the item bindings.


| Key | Default | Required | Description |
|-----|---------|:--------:|-------------|
| `<name>.host` | net-control | recommended | IP or network address |
| `<name>.udpReceivePort` | 77 | No | UDP receive port |
| `<name>.udpSendPort` | 75 | No | UDP send port |
| `<name>.user` | user7 | No | User name |
| `<name>.password` | anel | No | Password |
| `refresh` | 60000 | No | Global refresh interval in milliseconds |
| `cachePeriod` | 0 | No | Cache the state for `cachePeriod` minutes so only changes are posted (optional, defaults to 0 = disabled).  Example: if period is 60, once per hour all states are posted to the event bus; changes are always and immediately posted to the event bus. |


### Notes

* At least one option must be set for an identifier for the binding to work.
* The most obvious and important option is `host`, it is in fact mandatory if multiple devices are used.
* The host name, ports, and credentials are device-specific settings that must be configured via the device's browser interface.
* Port numbers above 1024 are recommended because ports below 1024 are typically reserved and their access restricted on some devices/networks.
* For multiple devices, different ports must be used.

## Item Configuration

There are different types of item bindings, all of them are qualified with the device's identifier used in the binding configuration:

The device's name is a string type, the device's temperature is a number:

```
String anelName "Anel1 network name [%s]" { anel="anel1:NAME" }
Number anelTemperature "Anel1 temperature [%s]" { anel="anel1:TEMPERATURE" }
```

The actual relay states are also switchable if they are not locked.
Note that the locked states and the relay's names are read-only.

```
Switch f1 { anel="anel1:F1", autoupdate="false" }
String f1name { anel="anel1:F1NAME" }
Switch f1locked { anel="anel1:F1LOCKED" }
```

The I/O states are only switchable if they are configured as 'input' (point of view of the anel device), otherwise they are read-only.
Again, the names are also read-only.

```
Switch io1 { anel="anel1:IO1" }
String io1name = { anel="anel1:IO1NAME" }
Switch io1isinput { anel="anel1:IO8ISINPUT" }
```

Note: **all read-only properties must be configured via the device's browser interface.**

General format and full list of binding items:

```
anel="<identifier>:<item>"
```

| item | item type | purpose | changeable |
| --- | --- | --- | --- |
| `NAME` | String | device's name | no
| `TEMPERATURE` | Number | device's temperature | no
| `F1NAME` | String | name of relay 1 | no
| `F2NAME` | String | name of relay 2 | no
| `F3NAME` | String | name of relay 3 | no
| `F4NAME` | String | name of relay 4 | no
| `F5NAME` | String | name of relay 5 | no
| `F6NAME` | String | name of relay 6 | no
| `F7NAME` | String | name of relay 7 | no
| `F8NAME` | String | name of relay 8 | no
| `F1LOCKED` | Switch | whether or not relay 1 is locked | no
| `F2LOCKED` | Switch | whether or not relay 2 is locked | no
| `F3LOCKED` | Switch | whether or not relay 3 is locked | no
| `F4LOCKED` | Switch | whether or not relay 4 is locked | no
| `F5LOCKED` | Switch | whether or not relay 5 is locked | no
| `F6LOCKED` | Switch | whether or not relay 6 is locked | no
| `F7LOCKED` | Switch | whether or not relay 7 is locked | no
| `F8LOCKED` | Switch | whether or not relay 8 is locked | no
| `F1` | Switch | state of relay 1 | only if `F1LOCKED = OFF`
| `F2` | Switch | state of relay 2 | only if `F2LOCKED = OFF`
| `F3` | Switch | state of relay 3 | only if `F3LOCKED = OFF`
| `F4` | Switch | state of relay 4 | only if `F4LOCKED = OFF`
| `F5` | Switch | state of relay 5 | only if `F5LOCKED = OFF`
| `F6` | Switch | state of relay 6 | only if `F6LOCKED = OFF`
| `F7` | Switch | state of relay 7 | only if `F7LOCKED = OFF`
| `F8` | Switch | state of relay 8 | only if `F8LOCKED = OFF`
| `IO1NAME` | String | name of I/O 1 | no
| `IO2NAME` | String | name of I/O 2 | no
| `IO3NAME` | String | name of I/O 3 | no
| `IO4NAME` | String | name of I/O 4 | no
| `IO5NAME` | String | name of I/O 5 | no
| `IO6NAME` | String | name of I/O 6 | no
| `IO7NAME` | String | name of I/O 7 | no
| `IO8NAME` | String | name of I/O 8 | no
| `IO1ISINPUT` | Switch | whether I/O 1 is input | no
| `IO2ISINPUT` | Switch | whether I/O 2 is input | no
| `IO3ISINPUT` | Switch | whether I/O 3 is input | no
| `IO4ISINPUT` | Switch | whether I/O 4 is input | no
| `IO5ISINPUT` | Switch | whether I/O 5 is input | no
| `IO6ISINPUT` | Switch | whether I/O 6 is input | no
| `IO7ISINPUT` | Switch | whether I/O 7 is input | no
| `IO8ISINPUT` | Switch | whether I/O 8 is input | no
| `IO1` | Switch | state of I/O 1 | only if `IO1ISINPUT = ON`
| `IO2` | Switch | state of I/O 2 | only if `IO2ISINPUT = ON`
| `IO3` | Switch | state of I/O 3 | only if `IO3ISINPUT = ON`
| `IO4` | Switch | state of I/O 4 | only if `IO4ISINPUT = ON`
| `IO5` | Switch | state of I/O 5 | only if `IO5ISINPUT = ON`
| `IO6` | Switch | state of I/O 6 | only if `IO6ISINPUT = ON`
| `IO7` | Switch | state of I/O 7 | only if `IO7ISINPUT = ON`
| `IO8` | Switch | state of I/O 8 | only if `IO8ISINPUT = ON`
| `SENSOR_TEMPERATURE` | Number | sensor temperature (device firmware >= 6.1) | no
| `SENSOR_HUMIDITY` | Number | sensor humidity (device firmware >= 6.1) | no
| `SENSOR_BRIGHTNESS` | Number | sensor brightness (device firmware >= 6.1) | no


## Example Rules

Although the device's configuration can be used to directly switch a relay with an input channel, the very same can be done with this rule:

```
rule "regular switch on Anel1 IO1 input for relay 1"
when Item io1 changed then
    sendCommand(f1, io1.state)
end
```

An input channel can also be used as a push button (also demonstrated in the [video](http://www.youtube.com/watch?v=31ycP53jZVs)):

```
rule "push button switch on Anel1 IO2 input for relay 2"
when Item io2 changed to ON then
    sendCommand(f2, if (f2.state != ON) ON else OFF)
end
```

In combination with the [MiLight Binding](https://github.com/openhab/openhab/wiki/Milight-Binding), this rule uses I/O 3 as dimmer for MiLight bulb `milight_zone1` (also demonstrated in the [video](http://www.youtube.com/watch?v=31ycP53jZVs)).

As long as I/O 3 is pressed, the bulb dims up until its brightness reaches 100%.

```
var org.openhab.model.script.actions.Timer timer2

rule "switch dimmer on Anel1 IO3"
when Item io3 changed to OFF then
    sendCommand(milight_zone1, 10)
    timer2 = createTimer(now.plusMillis(333)) [|
        val int newValue = (milight_zone1.state as DecimalType).intValue + 5
        if (newValue > 100) {
            timer2 = null
        } else if (timer2 != null) {
            sendCommand(milight_zone1, newValue)
            if (io3.state == OFF)
                timer2.reschedule(now.plusMillis(333))
        }
    ]
end
```
