# Global Cache IR Binding

This page describes the Global Cache IR binding (1.x), which allows openHAB items to send commands to the corresponding IR devices from one or more instances of Global Cache. 

![Global Cache Device](http://www.smarthome.com/media/catalog/product/8/1/8115big.jpg)

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/globalcache/).

## Binding Configuration

This binding can be configured in the `services/gc100ir.cfg` file.

The Global Cache IR binding allows you to define named instances of Global Cache device in your configuration. When defining your item configuration, you can use the name to refer to your instances.  In doing so, you can easily change the address by which your Global Cache instance can be reached without having to reconfigure all of your items.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<instance>`.host |  |   Yes    | IP address of the GC100 device |

Where `<instance>` is the name by which you can refer to this instance in your item configuration.

### Example

```
living.host=192.168.2.70
```

## Item Configuration

```
gc100ir="{[<instance>|<module>|<connector>|<code>]}"
```

Where

* `<instance>` is prefixed by ‘#’, a named instance configured in the `services/gc100ir.cfg`.
* `<module>` is the numeric value which specifies the module number of GC100.
* `<connector>` is the numeric value which specifies the connector number of GC100.
* `<code>` is the Global Cache format IR code which is to be sent over IR devices as a command through GC100. For conversion between Global Cache format IR codes and Hex (CCF) codes, you can download iConvert tool (Windows only) from [Global Cache](http://www.globalcache.com/downloads/) downloads page. You can also download different IR Hex (CCF) codes to control other devices from [http://www.remotecentral.com](http://www.remotecentral.com).

### Example

The following example is tested with DENON 1940CI DVD player to control it using IR receiver through global cache. IR receiver receives the IR pulse from Global Cache device and sends it as IR command to the DVD player.

![DENON 1940CI DVD Player](http://static.trustedreviews.com/94/8d9886/6e1c/7077-dendvd1940bk.jpg)

```
String	GC100_IR_DENON_DVD_LIVING_POWER_MODE_ON	{ gc100ir="[#living|4|1|38028,1,1,10,31,10,31,10,31,10,71,10,31,10,70,10,31,10,31,10,31,10,70,10,70,10,31,10,71,10,31,10,31,10,1765,10,31,10,31,10,31,10,71,10,31,10,31,10,71,10,70,10,71,10,31,10,31,10,70,10,31,10,71,10,71,10,1685,10,31,10,31,10,31,10,71,10,31,10,71,10,31,10,31,10,31,10,70,10,70,10,31,10,71,10,31,10,31,10,1764]" }
```

### Sitemap

```
Switch 	item=GC100_IR_DENON_DVD_LIVING_POWER_MODE_ON  label="Power ON"  mappings=[POWERON="POWER ON"]
Switch 	item=GC100_IR_DENON_DVD_LIVING_POWER_MODE_STANDBY  label="Stand by"  mappings=[STANDBY="STAND BY"]
Switch 	item=GC100_IR_DENON_DVD_LIVING_PLAY_STATE  label="Play"  mappings=[PLAY="PLAY"]
Switch 	item=GC100_IR_DENON_DVD_LIVING_PAUSE_STATE  label="Pause"  mappings=[PAUSE="PAUSE"]
```

### iTach IP2IR and WF2IR

![](http://i0.wp.com/www.globalcache.com/wp-content/uploads/2009/10/iTachIP2IR-medtrans.png?resize=150%2C128)

This binding works with the iTach devices. For configuration, use 1 for module and 1-3 for connectors, depending on which connector your IR emitter is connected.

