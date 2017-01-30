# Raspberry Pi RC Switch Binding

This binding enables the management of remote controlled (RC) switches via a 433-MHz transmitter connected to a Raspberry Pi.  It can be used with a 433-MHz transmitter connected to a Raspberry Pi as described in [this assembly instruction](https://www.raspberrypi.org/forums/viewtopic.php?f=37&t=66946).

![transmitter (left) and receiver (right)](https://lh4.googleusercontent.com/-dFv2BHCIHIw/Utrbo8PJsLI/AAAAAAAADAY/IhZOiytTX2Y/w740-h450-no/modules.png)

## Binding Configuration

This binding can be configured in the file `services/rpircswitch.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| gpioPin  |         |   Yes    | GPIO port from which the RC transmitter receives its data |

## Item Configuration

This binding only supports Switch items, which can be configured with the following syntax:

```
Switch YourItemName { rpircswitch="<groupAddress>:<deviceAddress>" }
```

where:

* `<groupAddress>` is the ID of the switch group
* `<deviceAddress>` is the ID of the switch within the group

Group and device address can usually be configured in the RC switch device by adjusting DIP switches.

## Example

items/rpircswitchdemo.items

```
Switch	SleepingRoom	{ rpircswitch="11111:4" }
Switch	LivingRoom		{ rpircswitch="11111:1" }
```

sitemaps/rpircswitch.sitemap.fragment

```
Switch item=SleepingRoom label="Sleeping Room"
Switch item=LivingRoom label="Living Room"
```
