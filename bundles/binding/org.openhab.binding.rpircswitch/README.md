### Introduction
This binding enables the management of remote controlled (RC) switches via a 433-MHz transmitter connected to a Raspberry Pi.

The binding can be used with a 433-MHz transmitter connected to a Raspberry Pi as described in the following assembly instruction: https://www.raspberrypi.org/forums/viewtopic.php?f=37&t=66946

### Configuration in openhab.cfg
In the openHAB configuration you have to configure the GPIO port from which the RC transmitter receives its data.

If the RC transmitter is connected to the GPIO port 0, add the following line to the configuration file:
```
rpircswitch:gpioPin=0
```

### Items
The rpircswitch binding only supports *SwitchItems* which can be configured with the following syntax:
```
Switch name { rpircswitch="groupAddress:deviceAddress" }
```
with

 - *name* - an arbitrary name
 - *groupAddress* - the ID of the switch group
 - *deviceAddress* - the ID of the switch within the group

Group and device address can usually be configured in the RC switch device by adjusting DIP switches.

Example:
```
Switch	SleepingRoom	{rpircswitch="11111:4"}
Switch	LivingRoom		{rpircswitch="11111:1"}
```

### Sitemap
To add the two sample switches to your sitemap, you can add the following lines to the sitemap configuration:  
```
Switch item=SleepingRoom label="Sleeping Room"
Switch item=LivingRoom label="Living Room"
```