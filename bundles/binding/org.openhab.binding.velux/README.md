
# Velux Binding

This binding integrates the <B>Velux</B> devices with help of a gateway, the <B>Velux Bridge KLF200</B>, which is able to control 200 actuators.
The Velux Binding interacts via the Velux Bridge with any [io-homecontrol](http://www.io-homecontrol.com/)-based
devices like window openers, shutters and others.

Based on the VELUX API this binding integrates <B>Velux</B> and other io-homecontrol devices directly into the openHAB, avoiding the necessity of any cloud-based mediation infrastructures. The complete home-automation will work even without any Internet connectivity.

For details about the features, see the following websites:
- [Velux](http://www.velux.com)
- [Velux API](http://www.velux.com/api/klf200)

## Overview

As the API is widely open, there are several use cases which are supported by the Bridge:
From the complete configuration of a set of io-homecontrol devices including registration, naming, grouping, crypto key setup and exchange, the definition of intended settings, so called scenes, up to the control of single devices, i.e. ```open window of bathroom up to 45%```.

The following areas are covered:

| Topic                   | Details                                                                                                    |
|-------------------------|------------------------------------------------------------------------------------------------------------|
| General bridge commands | SW version(\*), Gateway state(\*), Learn state, Clock, Reboot, FactoryReset, Network Setup(+)                |
| Configuration Services  | Node Discovery, Node Removal, Controller Copy, Crypto Key Generation, Crypto Key Exchange, Actuator config | 
| Information Services    | House Monitoring Service(\*), Node information(+), Group information                                        |
| Activation Logging      |                                                                                                            |
| Command Handling        | Command activation(\*), Command interruption(\*), Status Request(\*), Actuator Identification(\*), Limitations |
| Scene Handling          | Scene definition, Scene execution(\*), Scene deletion, Scene renaming, Scene Overview(\*)                    |
| Physical I/O Handling   | I/O Port setup                                                                                             |

Items marked with (\*) are fully implemented. Items marked with (+) have only partial support.

## Binding Configuration

The binding can be configured by parameters in the global configuration file `openhab.cfg`.

| Property       | Default                | Required | Description                                               |
|----------------|------------------------|:--------:|-----------------------------------------------------------|
| bridgeIPAddress|                        |   Yes    | Hostname or address for accessing the Velux Bridge.       |
| bridgeProtocol | slip                   |    No    | Underlying communication protocol (http/https/slip).      |
| bridgeTCPPort  | 80                     |    No    | TCP port (80 or 51200)for accessing the Velux Bridge.     |
| bridgePassword | velux123               |    No    | Password for authentication against the Velux Bridge.(**) |
| timeoutMsecs   | 2000                   |    No    | Initial Connection timeout in milliseconds                |
| retries        | 6                      |    No    | Number of retries during I/O                              |

(**) Note: This password is the API password that is printed on the back of the unit. Normally it differs from the password of the web frontend.

Advise: if you see a significant number of messages per day like

```
 communicate(): socket I/O failed continuously (x times).
```

please increase the parameters retries or/and timeoutMsecs.

For your convenience you'll see a log entry for the recognized configuration within the log file i.e.

```
2018-07-23 20:40:24.746 [INFO ] [.b.velux.internal.VeluxBinding] - veluxConfig[bridgeIPAddress=192.168.42.1,bridgeTCPPort=80,bridgePassword=********,timeoutMsecs=2000,retries=10]
```


## Discovery

Unfortunately there is no way to discover the Velux bridge within the local network. Be aware that all Velux scenes have to be added to the local Velux Bridge configuration as described in the Velux setup procedure.

## Item Configuration

The Items of a Velux Bridge consists in general of a pair of mastertype and subtype definition.
In the appropriate items file, i.e. velux.items, this looks like

```
{ velux="thing=<Mastertype>;channel=<Subtype>" }
```

Optionally the subtype is enhanced with parameters like the appropriate name of the scene.

```
{ velux="thing=<Mastertype>;channel=<Subtype>#<Parameter>" }
```

| Mastertype | Description                                                               |
|------------|---------------------------------------------------------------------------|
| bridge     | The Velux KLF200 represents a gateway to all Velux devices.               |
| scene      | Named ordered set of product states which can be activated for execution. |
| actuator   | IO-home controlled device which can be maintained by parameter settings.  |


### Subtype


| Subtype      | Item Type     | Description                                                     | Mastertype | Parameter |
|--------------|---------------|-----------------------------------------------------------------|------------|-----------|
| action       | Switch        | Activates a set of predefined product settings                  | scene      | required  |
| silentMode   | Switch        | Modification of the silent mode of the defined product settings | scene      | required  |
| status       | String        | Current Bridge State (\*\*\*)                                      | bridge     | N/A       |
| doDetection  | Switch        | Start of the product detection mode                             | bridge     | N/A       |
| firmware     | String        | Software version of the Bridge                                  | bridge     | N/A       |
| ipAddress    | String        | IP address of the Bridge                                        | bridge     | N/A       |
| subnetMask   | String        | IP subnetmask of the Bridge                                     | bridge     | N/A       |
| defaultGW    | String        | IP address of the Default Gateway of the Bridge                 | bridge     | N/A       |
| DHCP         | Switch        | Flag whether automatic IP configuration is enabled              | bridge     | N/A       |
| WLANSSID     | String        | Name of the wireless network                                    | bridge     | N/A       |
| WLANPassword | String        | WLAN Authentication Password                                    | bridge     | N/A       |
| products     | String        | List of all recognized products                                 | bridge     | N/A       |
| scenes       | String        | List of all defined scenes                                      | bridge     | N/A       |
| check        | String        | Checks of current item configuratio                             | bridge     | N/A       |
| shutter      | Rollershutter | Virtual rollershutter as combination of different scenes        | bridge     | required  |
| serial       | Rollershutter | IO-Homecontrol'ed device        				 | actuator   | required  |
| serial       | Switch        | IO-Homecontrol'ed device        				 | actuator   | required  |

Note (\*\*\*): The existence of this item triggers the continuous realtime status updates of any Velux item like shutters even if they are manually controlled by other controllers.


### Subtype Parameters

In case of the scene-related subtypes, action and silentMode, the spezification of the related scene as parameters is necessary;
```
{ velux="thing=scene;channel=<Subtype>#<Parameter>" }
```

The subtype shutter requires an even pair of parameters, each defining the shutter level and the related scene:
```
{ velux="thing=brigde;channel=shutter#<Level1>,<Scene1>,<Level2>,<Scene2>" }
```

### Virtual shutter

As the bridge does not support a real rollershutter interaction, this binding provides a virtual rollershutter consisting of different scenes which set a specific shutter level. Therefore the item definition contains multiple pairs of rollershutter levels each followed by a scene name, which leads to this setting.


## Full Example for firmware version One



### Items

```
//  Group for simulating push buttons

Group:Switch:OR(ON, OFF)    gV  "PushButton"

// Velux Scenes

Switch  V_DG_W_S_OPEN   "Velux DG Rolladen West open"       (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_West_000" }
Switch  V_DG_W_S_SUNNY  "Velux DG Rolladen West sunny"      (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_West_090" }
Switch  V_DG_W_S_CLOSED "Velux DG Rolladen West closed"     (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_West_100" }

Switch  V_DG_O_S_OPEN   "Velux DG Rolladen Ost open"        (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_Ost_000" }
Switch  V_DG_O_S_SUNNY  "Velux DG Rolladen Ost sunny"       (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_Ost_090" }
Switch  V_DG_O_S_CLOSED "Velux DG Rolladen Ost closed"      (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_Ost_100" }

Switch  V_DG_M_S_OPEN   "Velux DG Rolladen Mitte open"      (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_Mitte_000" }
Switch  V_DG_M_S_SUNNY  "Velux DG Rolladen Mitte sunny"     (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_Mitte_090" }
Switch  V_DG_M_S_CLOSED "Velux DG Rolladen Mitte closed"    (gV)    { velux="thing=scene;channel=action#V_DG_Shutter_Mitte_100" }

Switch  V_DG_M_W_OPEN   "Velux DG Window open"          (gV)    { velux="thing=scene;channel=action#V_DG_Window_Mitte_000" }
Switch  V_DG_M_W_UNLOCKED "Velux DG Window unlocked"        (gV)    { velux="thing=scene;channel=action#V_DG_Window_Mitte_010" }
Switch  V_DG_M_W_CLOSED "Velux DG Window closed"        (gV)    { velux="thing=scene;channel=action#V_DG_Window_Mitte_100" }

Switch  V_DG_OPEN   "Velux DG open"             (gV)    { velux="thing=scene;channel=action#V_DG_Shutters_000" }
Switch  V_DG_SUNNY  "Velux DG sunny"            (gV)    { velux="thing=scene;channel=action#V_DG_Shutters_090" }
Switch  V_DG_CLOSED "Velux DG closed"           (gV)    { velux="thing=scene;channel=action#V_DG_Shutters_100" }

// Velux Bridge parameters

String  V_FIRMWARE  "Firmware [%s]"                 { velux="thing=bridge;channel=firmware" }
String  V_STATUS    "Status [%s]"                   { velux="thing=bridge;channel=status" }
String  V_CHECK     "Velux Config Check [%s]"           { velux="thing=bridge;channel=check" }

// Velux Shutters

Rollershutter V_DG_W_S  "Velux DG Rolladen West [%d]"   { velux="thing=bridge;channel=shutter#0,V_DG_Shutter_West_000,90,V_DG_Shutter_West_090,
100,V_DG_Shutter_West_100"}
Rollershutter V_DG_O_S  "Velux DG Rolladen Ost [%d]"    { velux="thing=bridge;channel=shutter#0,V_DG_Shutter_Ost_000,90,V_DG_Shutter_Ost_090,10
0,V_DG_Shutter_Ost_100"}
Rollershutter V_DG_M_S  "Velux DG Rolladen Mitte [%d]"  { velux="thing=bridge;channel=shutter#0,V_DG_Shutter_Mitte_000,90,V_DG_Shutter_Mitte_09
0,100,V_DG_Shutter_Mitte_100"}
Rollershutter V_DG_M_W  "Velux DG Window Mitte [%d]"    { velux="thing=bridge;channel=shutter#0,V_DG_Window_Mitte_000,10,V_DG_Window_Mitte_010,
100,V_DG_Window_Mitte_100"}
```

### Sitemap

```
sitemap velux label="Velux Environment"
{
    Frame label="Velux Shutter and Window" {
        Switch  item=V_DG_W_S
        Switch  item=V_DG_O_S
        Switch  item=V_DG_M_S
        Switch  item=V_DG_M_W
    }
    Frame label="Velux Bridge" {
        Text    item=V_CHECK
        Text    item=V_STATUS
        Text    item=V_FIRMWARE
    }
}
```

### Rules

```
/**
 * This rule simulates the push button behaviour.
 */
rule "PushButton of group gV"
    when
        Item gV changed
    then
        // waiting a second.
            Thread::sleep(1000)
        // Foreach-Switch-is-ON
        gV.allMembers.filter( s | s.state == ON).forEach[i|
            // switching OFF
                i.sendCommand(OFF)
        ]
    end
```

## Full Example for firmware version Two



### Items

```
// Velux Bridge parameters

String  V_FIRMWARE  "Firmware [%s]"                     { velux="thing=bridge;channel=firmware" }
String  V_STATUS    "Status [%s]"                       { velux="thing=bridge;channel=status" }
String  V_CHECK     "Velux Config Check [%s]"           { velux="thing=bridge;channel=check" }

// Velux Shutters

Rollershutter V_DG_M_W	"Velux DG Window Bathroom [%d]"	{ velux="thing=actuator;channel=serial#01:52:21:3E:26:0C:1B:01"}
Rollershutter V_DG_M_S	"Velux DG Shutter Bathroom [%d]"{ velux="thing=actuator;channel=serial#01:52:00:21:00:07:00:02"}
Rollershutter V_DG_W_S	"Velux DG Shutter West [%d]"	{ velux="thing=actuator;channel=serial#01:53:09:40:21:0C:2A:03" }
Rollershutter V_DG_E_S	"Velux DG Shutter East [%d]"	{ velux="thing=actuator;channel=serial#11:56:32:14:5A:21:1C:04" }
```

### Sitemap

```
sitemap velux label="Velux Environment"
{
    Frame label="Velux Shutter and Window" {
        Switch  item=V_DG_W_S
        Switch  item=V_DG_E_S
        Switch  item=V_DG_M_S
        Switch  item=V_DG_M_W
    }
    Frame label="Velux Bridge" {
        Text    item=V_CHECK
        Text    item=V_STATUS
        Text    item=V_FIRMWARE
    }
}
```

### Debugging

For those who are interested in more detailed insight of the processing of this binding, a deeper look can be achieved by increased loglevel.

During startup of normal operations, there should be only some few messages within the logfile, like:
```
[INFO ] [.velux.internal.VeluxActivator] - velux binding has been started.
[INFO ] [.b.velux.internal.VeluxBinding] - Active items are: [V_DG_M_W, ..., V_DG_M_S].
[INFO ] [.b.velux.internal.VeluxBinding] - velux refresh interval set to 15000 milliseconds.
[INFO ] [.service.AbstractActiveService] - velux Refresh Service has been started
[INFO ] [.b.velux.internal.VeluxBinding] - veluxConfig[bridgeProtocol=slip,bridgeIPAddress=192.168.45.9,bridgeTCPPort=51200,bridgePassword=********,timeoutMsecs=500,retries=16,refreshMsecs=15000,isBulkRetrievalEnabled=true]
[INFO ] [v.bridge.slip.io.SSLconnection] - Starting velux bridge connection.
[INFO ] [.o.b.velux.bridge.slip.SClogin] - velux bridge connection successfully established (login succeeded).
[INFO ] [.o.b.v.h.VeluxBridgeHandlerOH1] - Found velux scenes:
	Scene "V_DG_Shutter_West_100" (index 5) with non-silent mode and 0 actions
	Scene "V_DG_Shutter_West_000" (index 4) with non-silent mode and 0 actions
	Scene "V_DG_Shutter_East_090" (index 10) with non-silent mode and 0 actions
...
	Scene "V_DG_Shutter_East_000" (index 8) with non-silent mode and 0 actions
	Scene "V_DG_Shutter_East_100" (index 9) with non-silent mode and 0 actions	.
[INFO ] [.o.b.v.h.VeluxBridgeHandlerOH1] - Found velux actuators:
        Product "Shutter Room 1" / ROLLER_SHUTTER (bridgeIndex=1,serial=01:53:09:40:21:0C:2A:01,position=0010)
        Product "Shutter Bathroom" / ROLLER_SHUTTER (bridgeIndex=2,serial=01:52:00:21:00:07:00:02,position=8C95)
        Product "Shutter Office" / ROLLER_SHUTTER (bridgeIndex=3,serial=01:53:09:40:21:43:2A:03,position=0000)
        Product "Shutter Room 2" / ROLLER_SHUTTER (bridgeIndex=4,serial=11:56:32:14:5A:21:1C:04,position=08DF)
        Product "Bathroom" / WINDOW_OPENER (bridgeIndex=0,serial=01:52:21:3E:26:0C:1B:05,position=C800) .
[INFO ] [.o.b.v.h.VeluxBridgeHandlerOH1] - velux Bridge is online, now.
```

## Supported/Tested Firmware Revisions

The Velux Bridge in API version one (firmware version 0.1.1.*) allows activating a set of predefined actions, so called scenes. Therefore beside the bridge, only one main thing exists, the scene element. The next-generation firmware version two is not backward compatible, and does not provide a public web frontend, but version two does provide full access to any IO-Home compatible devices not limited to Velux and includes many different features.

| Firmware revision | Release date | Description                                                             |
|:-----------------:|:------------:|-------------------------------------------------------------------------|
| 0.1.1.0.41.0      | 2016-06-01   | Default factory shipping revision.                                      |
| 0.1.1.0.42.0      | 2017-07-01   | Public Web Frontend w/ JSON-API.                                        |
| 0.1.1.0.44.0      | 2017-12-14   | Public Web Frontend w/ JSON-API.                                        |
| 2.0.0.71	    | 2018-09-27   | Public SLIP-API w/ private-only WLAN-based Web Frontend w/ JSON-API.    |

Notes:
- Velux bridges cannot be returned to version one of the firmware after being upgraded to version two.
- Firmware updates are currently provided at [Velux download area](https://updates2.velux.com/).


## How to run it under openHAB2?

Use the Karaf console to install the openHAB1 compatibility layer with `feature:install openhab-runtime-compat1x`.
Create the basic configuration of the service i.e. with a file `services/velux.cfg`, define the items within i.e. `items/velux.items` and finally generate a UI within i.e. `sitemap/default.sitemap`.

## Unknown Velux devices

All known <B>Velux</B> devices can be handled by this binding. However, there might be some new ones which will be reported within the logfiles.Therefore, error messages like the one below should be reported to the maintainers so that the new Velux device type can be incorporated."

```
[ERROR] [g.velux.things.VeluxProductReference] - PLEASE REPORT THIS TO MAINTAINER: VeluxProductReference(3) has found an unregistered ProductTypeId.
```

