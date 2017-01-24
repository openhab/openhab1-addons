Documentation for Global Cache IR binding

## Introduction
This page describes the Global Cache IR binding, which allows openhab items to send commands to the corresponding IR devices from one or more instances of Global Cache. For installation of the binding, please see Wiki page [Bindings](https://github.com/openhab/openhab/wiki/Bindings).
![Global Cache Device](http://www.smarthome.com/media/catalog/product/8/1/8115big.jpg)
## Global Binding Configuration
The Global Cache IR binding allows you to define named instances of Global Cache device in your openhab.cfg. When defining your item binding configuration you can use the name to refer to your instances. In doing so, you can easily change the address by which your Global Cache instance can be reached without having to reconfigure all of your bindings.

The syntax of the binding configuration is like this:
```
gc100ir : {instanceName}.host = <IP address of the GC100 device>
```
Where
* instanceName is name by which you can refer to this instance in your item binding configuration.

### Example
```
########################### GC100 IR Binding ###########################
	
# Hostname / IP address of your GC100 host
gc100ir:living.host=192.168.2.70
```

## Item Binding Configuration

**Configuration format**
```
gc100ir = "{[instanceName|module|connector|code]}"
```
Where
* instanceName is prefixed by ‘#’, a named instance configured in the openhab.cfg.
* module is the numeric value which specifies the module number of GC100.
* connector is the numeric value which specifies the connector number of GC100.
* code is the Global Cache format IR code which is to be sent over IR devices as a command through GC100. For conversion between Global Cache format IR codes and Hex (CCF) codes, you can download iConvert tool (Only for Windows OS) from [Global Cache](http://www.globalcache.com/downloads/) downloads page. You can also download different IR Hex (CCF) codes to control other devices from [http://www.remotecentral.com](http://www.remotecentral.com).

### Example
The following example is tested with DENON 1940CI DVD player to control it using IR receiver through global cache. IR receiver receives the IR pulse from Global Cache device and sends it as IR command to the DVD player.

![DENON 1940CI DVD Player](http://static.trustedreviews.com/94/8d9886/6e1c/7077-dendvd1940bk.jpg)

```
String	GC100_IR_DENON_DVD_LIVING_POWER_MODE_ON	{ gc100ir="[#living|4|1|38028,1,1,10,31,10,31,10,31,10,71,10,31,10,70,10,31,10,31,10,31,10,70,10,70,10,31,10,71,10,31,10,31,10,1765,10,31,10,31,10,31,10,71,10,31,10,31,10,71,10,70,10,71,10,31,10,31,10,70,10,31,10,71,10,71,10,1685,10,31,10,31,10,31,10,71,10,31,10,71,10,31,10,31,10,31,10,70,10,70,10,31,10,71,10,31,10,31,10,1764]" }
```

## Sitemap
```
Switch 	item=GC100_IR_DENON_DVD_LIVING_POWER_MODE_ON  label="Power ON"  mappings=[POWERON="POWER ON"]
Switch 	item=GC100_IR_DENON_DVD_LIVING_POWER_MODE_STANDBY  label="Stand by"  mappings=[STANDBY="STAND BY"]
Switch 	item=GC100_IR_DENON_DVD_LIVING_PLAY_STATE  label="Play"  mappings=[PLAY="PLAY"]
Switch 	item=GC100_IR_DENON_DVD_LIVING_PAUSE_STATE  label="Pause"  mappings=[PAUSE="PAUSE"]
```

## iTach IP2IR and WF2IR
![](http://i0.wp.com/www.globalcache.com/wp-content/uploads/2009/10/iTachIP2IR-medtrans.png?resize=150%2C128)

This binding works with the iTach devices. For configuration, use 1 for module and 1-3 for connectors, depending on which connector your IR emitter is connected.