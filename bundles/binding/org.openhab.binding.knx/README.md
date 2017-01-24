Documentation of the KNX Binding Bundle
### Table of Contents

 * [Introduction](#introduction)
 * [Binding Configuration](#binding-configuration)
 * [Bind Items to KNX](#bind-items-to-knx)
 * [Supported Datapoint Types](#supported-datapoint-types)
 * [KNX Logging](#knx-logging)

## Introduction

The openHAB KNX binding allows to connect to [KNX Home Automation](http://www.knx.org/) installations. Switching lights on and off, activating your roller shutters or changing room temperatures are only some examples.

To access your KNX bus you either need an KNX IP gateway (like e.g. the [Gira KNX IP Router]
(http://www.gira.com/en/gebaeudetechnik/systeme/knx-eib_system/knx-produkte/systemgeraete/knx-ip-router.html)) or a PC running [EIBD](http://www.auto.tuwien.ac.at/~mkoegler/index.php/eibd) (free open source component that enables communication with the KNX bus).

> Please note that the KNX Binding is using **224.0.23.12:3671/UDP** by default to connect to your gateway.

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

You can find the configuration section for the KNX binding in file **[configurations/openhab.cfg](https://github.com/openhab/openhab/blob/master/distribution/openhabhome/configurations/openhab_default.cfg)**, section "KNX Binding". 

##### For your convenience you can see the relevant section as follows:

    # KNX gateway IP address 
    # (optional, if serialPort or connection type 'ROUTER' is specified)
    knx:ip=

    # Local KNX Binding bus address.
    # Use it, when two or more openHAB Instances are connected to the same KNX bus.
    # (optional, defaults to 0.0.0)
    #knx:busaddr=

    # Ignore local KNX Events, prevents internal events coming from
    # 'openHAB event bus' a second time to be sent back to the 'openHAB event bus'.
    # Note: To send back events second time is a Bug, but for backward compatibility, the behavior is not changed.
    # For new installations, its recommend to set "knx:ignorelocalevents=true"
    # (optional, defaults to false)
    #knx:ignorelocalevents=

    # KNX IP connection type. Could be either TUNNEL or ROUTER (optional, defaults to TUNNEL)
    # Note: If you cannot get the ROUTER mode working (even if it claims it is connected), 
    # use TUNNEL mode instead with setting both the ip of the KNX gateway and the localIp.
    knx:type=

    # KNX gateway port (optional, defaults to 3671)
    # Note: If you use eibd, setting to 6720
    knx:port=

    # Local endpoint to specify the multicast interface, no port is used (optional)
    knx:localIp=

    # Serial port of FT1.2 KNX interface (ignored, if ip is specified)
    # Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux
    knx:serialPort=

    # Pause in milliseconds between two read requests on the KNX bus during
    # initialization (optional, defaults to 50)
    knx:pause=

    # Timeout in milliseconds to wait for a response from the KNX bus (optional, 
    # defaults to 10000)
    knx:timeout=

    # Number of read retries while initialization items from the KNX bus (optional,
    # defaults to 3)
    knx:readRetries=

    # Seconds between connect retries when KNX link has been lost
    # 0 means never retry, it will only reconnect on next write or read request
    # Note: without periodic retries all events will be lost up to the next read/write request
    # (optional, default is 0)
    knx:autoReconnectPeriod=

    ### Auto refresh feature
    # Number of entries permissible in the item refresher queue. 
    # (optional, defaults to 10000)
    #knx:maxRefreshQueueEntries=

    # Number of parallel threads for refreshing items. (optional, defaults to 5)
    #knx:numberOfThreads=

    # Seconds to wait for an orderly shutdown of the auto refresher's 
    # ScheduledExecutorService. (optional, defaults to 5)
    #knx:scheduledExecutorServiceShutdownTimeoutString=

    # The following setting is planned to be added in the 1.9.0 version of OpenHAB:
    # Use NAT (Network Address Translation)
    #  (optional; defaults to false)
    #knx:useNAT=true

##### A sample configuration could look like:

    knx:ip=224.0.23.12
    knx:type=ROUTER
    
##### Specific sample configuration for KNX communication between two openHAB installations:
This configuration works without any physical KNX Hardware. 
Uses default KNX multicast IP address: *224.0.23.12:3671* for communication.

KNX Configuration first openHAB:

    knx:busaddr=15.15.248
    knx:type=ROUTER

KNX Configuration second openHAB:

    knx:busaddr=15.15.249
    knx:type=ROUTER
    
The multicast IP address 224.0.23.12 is reserved for KNXnet/IP at the IANA (Internet Assigned Numbers Authority) for this purpose. 
If a different multicast IP address is required, it must lie within the range of 239.0.0.0 to 239.255.255.255. 
For alternative multicast IP adress for example, add in KNX configuration:

    knx:ip=239.0.0.1

## Bind Items to KNX

### Description

In order to bind an item to a KNX device you need to provide configuration settings. The easiest way to do so is to add  binding information in your 'item file' (in the folder configurations/items`). The syntax for the KNX binding configuration string is explained here:

    knx="[<][<dptId>:]<mainGA>[[+[<]<listeningGA>]+[<]<listeningGA>..], [<][<dptId>:]<mainGA>[[+[<]<listeningGA>]+[<]<listeningGA>..]"

Since 1.6:

<!-- not very nice, but a bug in markdown (see below) requires pure html -->
<pre><code>knx="[&lt;[(&lt;autoRefresh&gt;)]][&lt;dptId&gt;:]&lt;mainGA&gt;[[+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;]+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;..], [&lt;[(&lt;autoRefresh&gt;)]][&lt;dptId&gt;:]&lt;mainGA&gt;[[+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;]+[&lt;[(&lt;autoRefresh&gt;)]]&lt;listeningGA&gt;..]"
</code></pre>
<!-- the following isn't working: everything after <mainGA>[[+[ is swallowed
    knx=[<[(<autoRefresh>)]][<dptId>:]<mainGA>[[+[<[(<autoRefresh>)]]<listeningGA>]+[<[(<autoRefresh>)]]<listeningGA>..], [<[(<autoRefresh>)]][<dptId>:]<mainGA>[[+[<[(<autoRefresh>)]]<listeningGA>]+[<[(<autoRefresh>)]]<listeningGA>..]
-->
where parts in brackets `[]` signify an optional information.
 
Each comma-separated section corresponds to a KNX datapoint. There is usually one datapoint defined per accepted command type of an openHAB item. If no datapoint type id is defined for the datapoint, this is automatically derived from the list of accepted command types of the item - i.e. the second datapoint definition is mapped to the second accepted command type of the item.

The optional '<' sign tells whether the group address of the datapoint accepts read requests on the KNX bus (it does, if the sign is there). Since 1.6: the optional autoRefresh time in seconds specifies that this datapoint is to be cyclically reread. If autoRefresh is omitted then the read will only occur once, when initializing the KNX binding.

Each itemtype (see page [Items](Explanation-of-Items#itemtype)) accepts different command types. When binding an item to KNX you can provide one KNX group address ("mainGA") and several listening group addresses ("listeningGA") to each commandtype.

mainGAs are used for updating the status of openHAB items via KNX. There can only be one mainGA for an openHAB item (Highlander principle :-)
listeningGAs are used for obtaining status changes from KNX. There can be multiple listeningGAs for one openHAB item.

### Example

Given we want to bind a Dimmer Item to KNX, we have first to check which commands an openHAB dimmer item does accept:

On page [Items](Explanation-of-Items#itemtype) we find that an openHAB Dimmer item accepts three types of commands:

|Itemname|Description|Command Types|
|--------|-----------|-------------|
|Dimmer|Item carrying a percentage value for dimmers|OnOff, IncreaseDecrease, Percent|

Also [in the sources](https://github.com/openhab/openhab/tree/master/bundles/core/org.openhab.core.library/src/main/java/org/openhab/core/library/items/DimmerItem.java), we can find this information:

    acceptedCommandTypes.add(OnOffType.class);
    acceptedCommandTypes.add(IncreaseDecreaseType.class);
    acceptedCommandTypes.add(PercentType.class);

So, we first have to bind the OnOff command to the respective KNX group addresses, then the IncreaseDecrease command and finally the Percent command. Please note that the sequence of these commands is relevant.

In our example we assign the following KNX group addresses to the different commands:

|Command Type|Main Group Address|Listening Address(es)|Comment|
|------------|------------------|---------------------|-------|
|OnOff|1/3/20|0/3/20|-|
|IncreaseDecrease|1/3/21|-|no listening GAs here as INCREASE and DECREASE are only commands but not valid states|
|Percent|1/3/22|0/3/22 and 0/8/15||||

The respective line in the items definition file would therefore look like this:

    Dimmer TestDimmer "TestDimmer [%s]" (Lights) { knx="1/3/20+0/3/20, 1/3/21, 1/3/22+0/3/22+0/8/15" }

If you have a dimmer that does not support INCREASE/DECREASE commands and thus you do not have a GA to provide in the middle, you can also directly define the datapoint types (DPTs) in the configuration. The above example would then look like this (without INCREASE/DECREASE support):

    Dimmer TestDimmer "TestDimmer [%s]" (Lights) { knx="1.001:1/3/20+0/3/20, 5.001:1/3/22+0/3/22+0/8/15" }

### Command types for items

For identifying the different command types for items, please either have a look into the [openHAB source code](https://github.com/openhab/openhab/tree/master/bundles/core/org.openhab.core.library/src/main/java/org/openhab/core/library/items/) or see Wiki page [Items](Explanation-of-Items#itemtype).

### Further examples

Here are some further examples for valid binding configuration strings:

For a SwitchItem:

    knx="1/1/10"
    knx="1.001:1/1/10"
    knx="<1/1/10"
    knx="<(5)1/1/10"
    knx="<1/1/10+0/1/13+0/1/14+0/1/15"
    knx="<(10)1/1/10+0/1/13+0/1/14+0/1/15"
    knx="1/1/10+<0/1/13+0/1/14+0/1/15"
    knx="1/1/10+<(60)0/1/13+0/1/14+0/1/15"

For a RollershutterItem:

    knx="4/2/10"
    knx="4/2/10, 4/2/11"
    knx="4/2/10, 4/2/11, 4/2/12"
    knx="1.008:4/2/10, 5.001:4/2/11"
    knx="<4/2/10+0/2/10, 5.001:4/2/11+0/2/11"
    knx="<(60)4/2/10+0/2/10, 5.001:4/2/11+0/2/11"

As a result, your lines in the items file might look like the following:

    /* Lights */
    Switch Light_GF_Living_Table "Table" (GF_Living, Lights) { knx="1/1/10+0/1/5" }
    
    /* Rollershutters Up/Down, Stop/Move */
    Rollershutter Shutter_GF_Living "Shutter" (GF_Living, Shutters) { knx="4/2/10, 4/2/11" }
    
    /* Rollershutters  Up/Down, Stop/Move, Position */
    Rollershutter Shutter_GF_Living "Shutter" (GF_Living, Shutters) { knx="4/2/10, 4/2/11, 4/2/12" }
    
    /* Indoor Temperatures */
    Number Temperature_GF_Living "Temperature [%.1f °C]" <temperature> (GF_Living) { knx="<5/2/12" }

Further KNX binding examples can be found in our openhab-samples WIKI:
[KNX Binding Examples](Samples-Binding-Config)

## Supported Datapoint Types

The KNX binding supports a limited set of Datapoint types (DPTs). If your item configuration contains a DPT that is not supported by the KNX binding, openHAB 1.4.0 and later will throw an exception during startup ("DPT n.nnn is not supported by the KNX binding").

To get an overview of the supported DPTs, it's best to look into the source code of the KNX binding and the library it depends on. The DPTs for the binding are defined in [KNXCoreTypeMapper](https://github.com/openhab/openhab/blob/master/bundles/binding/org.openhab.binding.knx/src/main/java/org/openhab/binding/knx/internal/dpt/KNXCoreTypeMapper.java). The constants (and their mapping to DPTs) are defined in the library [calimero](https://github.com/calimero-project/calimero/tree/master/src/tuwien/auto/calimero/dptxlator).

### Forward Data from other Bindings to another Binding (KNX)
You may want in some cases to get Data from one Binding (for example 1Wire) to KNX. You will be able to do this through rules, but also can do this by an Item definition.

    Number Temperature_UG_HWR   "HWR [%.1f °C]"  <temperature>  (Temperature_UG, UG_HWR) {onewire="28.7871CF040000#temperature", knx="5/1/40"}

In this Example a 1Wire Temperature Sensor will also be available on knx adress 5/1/40

### Further examples

## KNX Logging

Since version 1.5.0 of this binding, it is possible to capture log events from calimero. These log events contain detailed information from the KNX bus (what is written to the bus, what gets read from the bus, ...)

To enable this logging, the following line has to be added to `logback.xml`:

    <logger name="tuwien.auto.calimero" level="DEBUG" />