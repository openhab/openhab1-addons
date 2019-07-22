
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
| bridgeTCPPort  | 51200                  |    No    | TCP port (80 or 51200) for accessing the Velux Bridge.    |
| bridgePassword | velux123               |    No    | Password for authentication against the Velux Bridge.(**) |
| timeoutMsecs   | 1000                   |    No    | Initial Connection timeout in milliseconds                |
| retries        | 5                      |    No    | Number of retries during I/O                              |
| refreshMsecs   | 15000                  |    No    | Refresh interval in milliseconds.                          |

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
| reload       | Switch        | Reload information from bridge into binding                     | bridge     | N/A       |
| timestamp    | Number        | Timestamp of last successful device interaction                 | bridge     | N/A       |
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
| serial       | Rollershutter | IO-Homecontrol'ed device (\*\*\*\*) (\*\*\*\*\*)		 | actuator   | required  |

Notes:
(\*\*\*) The existence of this item triggers the continuous realtime status updates of any Velux item like shutters even if they are manually controlled by other controllers.

(\*\*\*\*) To enable a complete invertion of all parameter values (i.e. for Velux windows), add a trailing star to the eight-byte serial number. For an example,
see below at item `Velux DG Window Bathroom`.

(\*\*\*\*\*) Somfy devices does not provides a valid serial number to the Velux KLF200 gateway: The bridge reports a registration of the serial number 00:00:00:00:00:00:00:00. Therefore the binding implements a fallback to allow an item specification with a actuator name instead of actuator serial number whenever such an invalid serial number occurs. For an example, see below at item `Velux OG Somfy Shutter`.


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

Switch  V_RELOAD    "Reload info from bridge"       { velux="thing=bridge;channel=reload" }
String  V_STATUS    "Status [%s]"                   { velux="thing=bridge;channel=status" }
String  V_TIMESTAMP "Timestamp [%.1f]"              { velux="thing=bridge;channel=timestamp" }
String  V_CHECK     "Velux Config Check [%s]"       { velux="thing=bridge;channel=check" }
String  V_FIRMWARE  "Firmware [%s]"                 { velux="thing=bridge;channel=firmware" }
String	V_CONF_LAN_IP	"KLF LAN IP [%s]"	{ velux="thing=bridge;channel=ipAddress" }
String	V_CONF_LAN_SUBNET "KLF LAN Subnet [%s]"	{ velux="thing=bridge;channel=subnetMask" }
String	V_CONF_LAN_GW	"KLF LAN Gateway [%s]"	{ velux="thing=bridge;channel=defaultGW" }
Switch	V_CONF_LAN_DHCP	"KLF LAN DHCP [%s]"	{ velux="thing=bridge;channel=DHCP" }
String	V_CONF_WLAN_SSID "KLF WLAN SSID [%s]"	{ velux="thing=bridge;channel=WLANSSID" }
String	V_CONF_WLAN_PW	"KLF WLAN Password [%s]"{ velux="thing=bridge;channel=WLANPassword" }


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
        Switch  item=V_RELOAD
        Text    item=V_STATUS
        Text    item=V_TIMESTAMP
        Text    item=V_CHECK
        Text    item=V_FIRMWARE
        Text    item=V_CONF_LAN_IP
        Text    item=V_CONF_LAN_SUBNET
        Text    item=V_CONF_LAN_GW
        Switch  item=V_CONF_LAN_DHCP
        Text    item=V_CONF_WLAN_SSID
        Text    item=V_CONF_WLAN_PW
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

Switch  V_RELOAD    "Reload info from bridge"           { velux="thing=bridge;channel=reload" }
String  V_STATUS    "Status [%s]"                   	{ velux="thing=bridge;channel=status" }
String  V_TIMESTAMP "Timestamp [%.1f]"              	{ velux="thing=bridge;channel=timestamp" }
String  V_CHECK     "Velux Config Check [%s]"       	{ velux="thing=bridge;channel=check" }
String  V_FIRMWARE  "Firmware [%s]"                 	{ velux="thing=bridge;channel=firmware" }
String	V_CONF_LAN_IP	"KLF LAN IP [%s]"		{ velux="thing=bridge;channel=ipAddress" }
String	V_CONF_LAN_SUBNET "KLF LAN Subnet [%s]"		{ velux="thing=bridge;channel=subnetMask" }
String	V_CONF_LAN_GW	"KLF LAN Gateway [%s]"		{ velux="thing=bridge;channel=defaultGW" }
Switch	V_CONF_LAN_DHCP	"KLF LAN DHCP [%s]"		{ velux="thing=bridge;channel=DHCP" }
String	V_CONF_WLAN_SSID "KLF WLAN SSID [%s]"		{ velux="thing=bridge;channel=WLANSSID" }
String	V_CONF_WLAN_PW	"KLF WLAN Password [%s]"	{ velux="thing=bridge;channel=WLANPassword" }


// Velux Shutters

Rollershutter V_DG_M_W	"Velux DG Window Bathroom [%d]"	{ velux="thing=actuator;channel=serial#01:52:21:3E:26:0C:1B:01*"}
Rollershutter V_DG_M_S	"Velux DG Shutter Bathroom [%d]"{ velux="thing=actuator;channel=serial#01:52:00:21:00:07:00:02"}
Rollershutter V_DG_W_S	"Velux DG Shutter West [%d]"	{ velux="thing=actuator;channel=serial#01:53:09:40:21:0C:2A:03" }
Rollershutter V_DG_E_S	"Velux DG Shutter East [%d]"	{ velux="thing=actuator;channel=serial#11:56:32:14:5A:21:1C:04" }
Rollershutter V_OG_W_S	"Velux OG Somfy Shutter [%d]"	{ velux="thing=actuator;channel=serial#Bathroom" }

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
        Switch  item=V_RELOAD
        Text    item=V_STATUS
        Text    item=V_TIMESTAMP
        Text    item=V_CHECK
        Text    item=V_FIRMWARE
        Text    item=V_CONF_LAN_IP
        Text    item=V_CONF_LAN_SUBNET
        Text    item=V_CONF_LAN_GW
        Switch  item=V_CONF_LAN_DHCP
        Text    item=V_CONF_WLAN_SSID
        Text    item=V_CONF_WLAN_PW
    }
}
```

## More automation samples

At this point some interesting automation rules are included to demonstrate the power of this gateway to the io-homecontrol world.


### Closing windows after a period of time

Especially in the colder months, it is advisable to close the window after adequate ventilation. Therefore, automatic closing after one minute is good to save on heating costs.
However, to allow the case of intentional prolonged opening, an automatic closure is made only with the window fully open.

```
/*
 * Start of imports
 */

import org.openhab.core.library.types.*

/*
 * Start of rules
 */

rule "V_WINDOW_changed"
when
	Item V_WINDOW changed
then
	logInfo("rules.V_WINDOW",	"V_WINDOW_changes() called.")
	//
	// Get the sensor value
	//
	val Number windowState = V_WINDOW.state as DecimalType
	logWarn("rules.V_WINDOW", "Window state is "+windowState+".")
	if (windowState < 80) {
		if (windowState == 0) {
			logWarn("rules.V_WINDOW", "V-WINDOW changed to fully open.")

			var int interval = 1
	
        		createTimer(now.plusMinutes(interval)) [|
				logWarn("rules.V_WINDOW:event", "event-V_WINDOW(): setting V-WINDOW to 100.")
                		sendCommand(V_WINDOW,100)
				V_WINDOW.postUpdate(100)
    				logWarn("rules.V_WINDOW:event", "event-V_WINDOW done.")
        		]
    		} else {
			logWarn("rules.V_WINDOW", "V-WINDOW changed to partially open.")
		}
    	}
	//
	// Check type of item
	//
	logDebug("rules.V_WINDOW",	"V_WINDOW_changes finished.")
end

/*
 * end-of-rules/V_WINDOW.rules
 */
```

## Debugging

For those who are interested in more detailed insight of the processing of this binding, a deeper look can be achieved by increased loglevel.

With Karaf you can use the following command sequence:
```
log:set TRACE org.openhab.binding.velux
log:tail
```

This, of course, is possible on command line with the commands:

```
% openhab-cli console log:set TRACE org.openhab.binding.velux 
% openhab-cli console log:tail org.openhab.binding.velux
```

On the other hand, if you prefer a textual configuration, you can append the logging definition with:

```
	<logger name="org.openhab.binding.velux" level="TRACE">
		<appender-ref ref="FILE" />
	</logger>
```

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


## Is it possible to run the both communication methods in parallel?

For environments with the firmware version 0.1.* on the gateway, the interaction with the bridge is limited to the HTTP/JSON based communication, of course. On the other hand, after upgrading the gateway firmware to version 2, it is possible to run the binding either using HTTP/JSON if there is a permanent connectivity towards the WLAN interface of the KLF200 or using SLIP towards the LAN interface of the gateway. For example the Raspberry PI can directly be connected via WLAN to the Velux gateway and providing the other services via the LAN interface (but not vice versa).


## Known Limitations

The communication based on HTTP/JSON is limited to one connection: If the binding is operational, you won't get access to the Web Frontend in parallel.

The SLIP communication is limited to two connections in parallel, i.e. two different openHAB bindings - or - one openHAB binding and another platform connection.

Both interfacing methods, HTTP/JSON and SLIP, can be run in parallel. Therefore, on the one hand you can use the Web Frontend for manual control and on the other hand a binding can do all automatic jobs.


## Unknown Velux devices

All known <B>Velux</B> devices can be handled by this binding. However, there might be some new ones which will be reported within the logfiles. Therefore, error messages like the one below should be reported to the maintainers so that the new Velux device type can be incorporated."

```
[ERROR] [g.velux.things.VeluxProductReference] - PLEASE REPORT THIS TO MAINTAINER: VeluxProductReference(3) has found an unregistered ProductTypeId.
```

