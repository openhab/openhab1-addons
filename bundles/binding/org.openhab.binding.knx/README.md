# KNX Binding

The openHAB KNX binding allows one to connect to [KNX Home Automation](http://www.knx.org/) installations. Switching lights on and off, activating your roller shutters or changing room temperatures are only some examples.

To access your KNX bus you either need an KNX IP gateway (like e.g. the [Gira KNX IP Router](http://www.gira.com/en/gebaeudetechnik/systeme/knx-eib_system/knx-produkte/systemgeraete/knx-ip-router.html)) or a PC running [EIBD](http://www.auto.tuwien.ac.at/~mkoegler/index.php/eibd) (free open source component that enables communication with the KNX bus).

> Please note that the KNX Binding is using **224.0.23.12:3671/UDP** by default to connect to your gateway.

## Binding Configuration

This binding can be configured in the file `services/knx.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip |  | optional if serialPort or connection type 'ROUTER' is specified | KNX gateway IP address |
| busaddr | 0.0.0 |  No | Local KNX Binding bus address. Use it when two or more openHAB Instances are connected to the same KNX bus. |
| ignorelocalevents | false | No | Ignore local KNX Events, prevents internal events coming from 'openHAB event bus' a second time to be sent back to the 'openHAB event bus'. Note: To send back events second time is a Bug, but for backward compatibility, the behavior is not changed. For new installations, its recommend to set to `true` |
| type | TUNNEL | No | KNX IP connection type. Could be either `TUNNEL` or `ROUTER`. Note: If you cannot get the ROUTER mode working (even if it claims it is connected), use `TUNNEL` mode instead, with setting both the ip of the KNX gateway and the localIp. |
| port | 3671 | No | KNX gateway port.  Note: If you use eibd, setting to 6720 |
| localIp |  |  | Local endpoint to specify the multicast interface, no port is used |
| serialPort |  | if connecting via serial port | Serial port of FT1.2 KNX interface (ignored if `ip` is specified). Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux |
| pause | 50 |  No | Pause in milliseconds between two read requests on the KNX bus during initialization |
| timeout | 10000 | No | Timeout in milliseconds to wait for a response from the KNX bus |
| readRetries | 3 | No | Number of read retries while initialization items from the KNX bus |
| autoReconnectPeriod | 0 | No | Seconds between connect retries when KNX link has been lost 0 means never retry, it will only reconnect on next write or read request. Note: without periodic retries all events will be lost up to the next read/write request |
| maxRefreshQueueEntries | 10000 | No | Number of entries permissible in the item refresher queue.  |
| numberOfThreads | 5 | No | Number of parallel threads for refreshing items. |
| scheduledExecutor ServiceShutdown TimeoutString | 5 | No | Seconds to wait for an orderly shutdown of the auto refresher's ScheduledExecutorServicw |
| useNAT | false | No | Use NAT (Network Address Translation) |

### Example

```
ip=224.0.23.12
type=ROUTER
```

#### Specific sample configuration for KNX communication between two openHAB installations:

This configuration works without any physical KNX Hardware. 

Uses default KNX multicast IP address: `224.0.23.12:3671` for communication.

KNX Configuration first openHAB:

```
 busaddr=15.15.248
 type=ROUTER
```

KNX Configuration second openHAB:

```
busaddr=15.15.249
type=ROUTER
```

The multicast IP address 224.0.23.12 is reserved for KNXnet/IP at the IANA (Internet Assigned Numbers Authority) for this purpose. 
If a different multicast IP address is required, it must lie within the range of 239.0.0.0 to 239.255.255.255. 
For alternative multicast IP adress for example, add in KNX configuration:

```
ip=239.0.0.1
```

## Items Configuration

### Description

In order to bind an item to a KNX device you need to provide configuration settings. The easiest way to do so is to add  binding information in your 'item file' (in the folder configurations/items`). The syntax for the KNX binding configuration string is explained here:

```
knx="[<][<dptId>:]<mainGA>[[+[<]<listeningGA>]+[<]<listeningGA>..], [<][<dptId>:]<mainGA>[[+[<]<listeningGA>]+[<]<listeningGA>..]"
```

Since version 1.6:

<!-- not very nice, but a bug in markdown (see below) requires pure html -->
<pre><code>knx="[&lt;[(&lt;autoRefresh&gt;)]][&lt;dptId&gt;:]&lt;mainGA&gt;[[+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;]+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;..], [&lt;[(&lt;autoRefresh&gt;)]][&lt;dptId&gt;:]&lt;mainGA&gt;[[+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;]+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;..]"
</code></pre>
<!-- the following isn't working: everything after <mainGA>[[+[ is swallowed
```
knx=[<[(<autoRefresh>)]][<dptId>:]<mainGA>[[+[<[(<autoRefresh>)]]<listeningGA>]+[<[(<autoRefresh>)]]<listeningGA>..], [<[(<autoRefresh>)]][<dptId>:]<mainGA>[[+[<[(<autoRefresh>)]]<listeningGA>]+[<[(<autoRefresh>)]]<listeningGA>..]
```
-->

where parts in brackets `[]` signify an optional information.
 
Each comma-separated section corresponds to a KNX datapoint. There is usually one datapoint defined per accepted command type of an openHAB item. If no datapoint type id is defined for the datapoint, this is automatically derived from the list of accepted command types of the item - i.e. the second datapoint definition is mapped to the second accepted command type of the item.

The optional '<' sign tells whether the group address of the datapoint accepts read requests on the KNX bus (it does, if the sign is there). Since 1.6: the optional autoRefresh time in seconds specifies that this datapoint is to be cyclically reread. If autoRefresh is omitted then the read will only occur once, when initializing the KNX binding.

Each item type accepts different command types. When binding an item to KNX you can provide one KNX group address ("mainGA") and several listening group addresses ("listeningGA") to each commandtype.

mainGAs are used for updating the status of openHAB items via KNX. There can only be one mainGA for an openHAB item (Highlander principle :-)
listeningGAs are used for obtaining status changes from KNX. There can be multiple listeningGAs for one openHAB item.

### Example

Given we want to bind a Dimmer Item to KNX, we have first to check which commands an openHAB dimmer item does accept:

On page [Items](https://github.com/openhab/openhab1-addons/wiki/Explanation-of-items#item-type) we find that an openHAB Dimmer item accepts three types of commands:

|Itemname|Description|Command Types|
|--------|-----------|-------------|
|Dimmer|Item carrying a percentage value for dimmers|OnOff, IncreaseDecrease, Percent|

Also [in the sources](https://github.com/openhab/openhab1-addons/tree/master/bundles/core/org.openhab.core.library/src/main/java/org/openhab/core/library/items/DimmerItem.java), we can find this information:

```java
acceptedCommandTypes.add(OnOffType.class);
acceptedCommandTypes.add(IncreaseDecreaseType.class);
acceptedCommandTypes.add(PercentType.class);
```

So, we first have to bind the `OnOffType` command to the respective KNX group addresses, then the `IncreaseDecreaseType` command and finally the `PercentType` command. Please note that the sequence of these commands is relevant.

In our example we assign the following KNX group addresses to the different commands:

|Command Type|Main Group Address|Listening Address(es)|Comment|
|------------|------------------|---------------------|-------|
|OnOff|1/3/20|0/3/20|-|
|IncreaseDecrease|1/3/21|-|no listening GAs here as INCREASE and DECREASE are only commands but not valid states|
|Percent|1/3/22|0/3/22 and 0/8/15||||

The respective line in the items definition file would therefore look like this:

```
Dimmer TestDimmer "TestDimmer [%s]" (Lights) { knx="1/3/20+0/3/20, 1/3/21, 1/3/22+0/3/22+0/8/15" }
```

If you have a dimmer that does not support INCREASE/DECREASE commands and thus you do not have a GA to provide in the middle, you can also directly define the datapoint types (DPTs) in the configuration. The above example would then look like this (without INCREASE/DECREASE support):

```
Dimmer TestDimmer "TestDimmer [%s]" (Lights) { knx="1.001:1/3/20+0/3/20, 5.001:1/3/22+0/3/22+0/8/15" }
```

### Command types for items

For identifying the different command types for items, please either have a look into the [openHAB source code](https://github.com/openhab/openhab/tree/1.8/bundles/core/org.openhab.core.library/src/main/java/org/openhab/core/library/items/) or see Wiki page [Items](Explanation-of-Items#itemtype).

### Further examples

Here are some further examples for valid binding configuration strings:

For a SwitchItem:

```
knx="1/1/10"
knx="1.001:1/1/10"
knx="<1/1/10"
knx="<(5)1/1/10"
knx="<1/1/10+0/1/13+0/1/14+0/1/15"
knx="<(10)1/1/10+0/1/13+0/1/14+0/1/15"
knx="1/1/10+<0/1/13+0/1/14+0/1/15"
knx="1/1/10+<(60)0/1/13+0/1/14+0/1/15"
```

For a RollershutterItem:

```
knx="4/2/10"
knx="4/2/10, 4/2/11"
knx="4/2/10, 4/2/11, 4/2/12"
knx="1.008:4/2/10, 5.001:4/2/11"
knx="<4/2/10+0/2/10, 5.001:4/2/11+0/2/11"
knx="<(60)4/2/10+0/2/10, 5.001:4/2/11+0/2/11"
```

As a result, your lines in the items file might look like the following:

```
/* Lights */
Switch Light_GF_Living_Table "Table" (GF_Living, Lights) { knx="1/1/10+0/1/5" }

/* Rollershutters Up/Down, Stop/Move */
Rollershutter Shutter_GF_Living "Shutter" (GF_Living, Shutters) { knx="4/2/10, 4/2/11" }

/* Rollershutters  Up/Down, Stop/Move, Position */
Rollershutter Shutter_GF_Living "Shutter" (GF_Living, Shutters) { knx="4/2/10, 4/2/11, 4/2/12" }

/* Indoor Temperatures */
Number Temperature_GF_Living "Temperature [%.1f °C]" <temperature> (GF_Living) { knx="<5/2/12" }
```

Further KNX binding examples can be found later in this document.

### Supported Datapoint Types

The KNX binding supports a limited set of Datapoint types (DPTs). If your item configuration contains a DPT that is not supported by the KNX binding, openHAB will throw an exception during startup ("DPT n.nnn is not supported by the KNX binding").

To get an overview of the supported DPTs, it's best to look into the source code of the KNX binding and the library it depends on. The DPTs for the binding are defined in [KNXCoreTypeMapper](https://github.com/openhab/openhab1-addons/blob/master/bundles/binding/org.openhab.binding.knx/src/main/java/org/openhab/binding/knx/internal/dpt/KNXCoreTypeMapper.java). The constants (and their mapping to DPTs) are defined in the library [calimero](https://github.com/calimero-project/calimero/tree/master/src/tuwien/auto/calimero/dptxlator).

### Forward Data from other Bindings to another Binding (KNX)

You may want in some cases to get data from one Binding (for example 1Wire) to KNX. You will be able to do this through rules, but also can do this by an Item definition.

```
Number Temperature_UG_HWR   "HWR [%.1f °C]"  <temperature>  (Temperature_UG, UG_HWR) { onewire="28.7871CF040000#temperature", knx="5/1/40" }
```

In this Example a 1Wire Temperature Sensor will also be available on knx adress 5/1/40

### KNX Logging

It is possible to capture log events from calimero. These log events contain detailed information from the KNX bus (what is written to the bus, what gets read from the bus, ...)

The logger is named `tuwien.auto.calimero` and can be set to `DEBUG`.

## Further examples

### KNX basic configuration

The items may include the KNX group address to use them. They might be actively read by openHAB or not. They look like this:


```
Number Lighting_Room_Sensor "Lighting in the Room [Lux](%.2f)"  <switch> (Room,Lighting) { knx = "<0/1/1" }
```

This is a sensor item. It uses the 0/1/1 group address and openHAB will ask for its value periodically because there is a "<" sign before the address. It is a number item, called `Lighting_Room_Sensor`, and belongs to `Room` and `Lighting` groups.

```
Switch Light_Room_Table  "Table Light" (Room, Lights) { knx = "<0/1/10+0/1/30"}
```

This is a switch item that has two addresses. openHAB may responds to events in any of them, but may actively read the first one.

### How to send Date and Time from NTP to KNX

This example sends the current date and time from the NTP to the KNX binding

```
DateTime Date "Date & Time [%1$td.%1$tm.%1$tY %1$tT]" { ntp="Europe/Berlin:de_DE", knx="11.001:0/0/1, 10.001:0/0/2" } 
```

**Items**: 

  0/0/1 is the GA for Date

  0/0/2 is the GA for Time

Additional information on date and time formatting can be found [here](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax)

### How to use KNX data types 2\.xxx Priority Control

KNX data types 2.xxx are supported.

This examples shows the usage of DPT 2.001 "Switch Control".

item definition:

```  
Number item2_001 "2.001 Switch Control" { knx="2.001:1/2/3"}
```

sitemap definition:

```        
Selection item=item2_001 mappings=[ 0="priority override disabled (off)", 1="priority override disabled (on)", 2="priority override: off", 3="priority override: on" ]
```

or

```
Switch item=item2_001 mappings=[ 0="priority override disabled (off)", 1="priority override disabled (on)", 2="priority override: off", 3="priority override: on" ]
```

### How to use KNX scenes

KNX devices differ in the ways scenes can be activated and learned (programmed). Some devices require a bit trigger using data point type 1.022 "DPT_SCENE_AB", which will activate either scene A or B.

These devices can be used as follows:

item definition:

```  
Number item1_022 "1.022 SCENE AB" { knx="1.022:1/2/3"}
```

sitemap definition:

```        
Selection item=item1_022 mappings=[ 0="Scene A", 1="Scene B" ]
```

or

```
Switch item=item1_022 mappings=[ 0="Scene A", 1="Scene B" ]
```

Some devices require a byte using data point type 18.001 "DPT_SCENE_CONTROL", which will activate or learn one of 64 possible scenes. Adding 128 to the scene number to allow switching to learn mode.

Example: "activate Scene 2" requires value 1, "learn Scene 2" requires value 129 

These devices can be used as follows (starting with version 1.6.0):

item definition:
  
```
Number item18_001 "Scene Control" {knx="18.001:1/2/3"}
```

sitemap definition:

```        
Selection item=item18_001 mappings=[ 0="Scene 1", 1="Scene 2", 2="Scene 3", 3="Scene 4", 128="learn Scene 1", 129="learn Scene 2", 130="learn Scene 3" ]
```

or

```
Switch item=d18_001 mappings=[0="Scene 1", 1="Scene 2", 2="Scene 3", 128="learn Scene 1", 129=" learn Scene 2", 130="learn Scene 3"]
```

If you have a device requiring 17.001 DPT_SCENE_NUMBER for selecting or indicating scenes, then use one of the above mentioned examples and replace 18.001 with 17.001. Additionally, remove all "learn" mappings.
