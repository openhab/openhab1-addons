
# velux Binding (openHAB1)

This binding integrates the <B>Velux</B> devices with help of a special gateway, the <B>Velux Bridge KLF200</B>.
The Velux Binding interacts via the Velux Bridge with the Velux devices like controlling window openers, shutters and others.

For details about the feature, see the following website ![Velux](http://www.velux.com) 

## Binding Configuration

The binding can be configured by parameters in the global configuration file `/etc/openhab/configurations/openhab.cfg`.

| Property       | Default                | Required | Description                                           |
|----------------|------------------------|:--------:|-------------------------------------------------------|
| bridgeIPAddress| 127.0.0.1              |   Yes    | Name of address for accessing the Velux Bridge.       |
| bridgeTCPPort  | 80                     |    No    | TCP port for accessing the Velux Bridge.              |
| bridgePassword | velux123               |   Yes    | Password for authentication against the Velux Bridge. |
| timeoutMsecs   | 2000                   |    No    | Initial Connection timeout in milliseconds            |
| retries        | 6                      |    No    | Number of retries during I/O                          |

Advise: if you see a significant number of messages per day like

```
 "communicate(): socket I/O failed continuously (x times)."
```

please increase the parameters retries or/and timeoutMsecs.

For your convenience you'll see a log entry for the recognized global configuration within the log file i.e.

```
018-07-23 20:40:24.746 [INFO ] [.b.velux.internal.VeluxBinding] - veluxConfig[bridgeIPAddress=192.168.42.1,bridgeTCPPort=80,bridgePassword=********,timeoutMsecs=2000,retries=10]
```

## Supported Things

The Velux Bridge in API version One (firmware version 0.1.1.*) allows to activate a set of predefined actions, so called scenes. Therefore beside the bridge, only one main thing exists, the scene element. Unfortunatelly even the current firmware version 0.1.1.0.44.0 does not include enhancements on this fact.

| Firmware revision | Release date | Description                                                             |
|:-----------------:|:------------:|-------------------------------------------------------------------------|
| 0.1.1.0.41.0      | 2016-06-01   | Default factory shipping revision.                                      |
| 0.1.1.0.42.0      | 2017-07-01   | N/A                                                                     |
| 0.1.1.0.44.0      | 2017-12-14   | N/A                                                                     |

## Discovery

Unfortunatelly there is no way to discover the Velux bridge within the local network. Beware that all Velux scenes have to be added to the local Velux Bridge configuration as described in the Velux setup procedure.

## Item Configuration

The Items of a Velux Bridge consists in general of a pair of thing and channel definition.
In the appropriate items file, i.e. velux.items, this looks like

```
{ velux="thing=<thingIdentifier>channel=<channelIdentifier>" }
```

| Thing type | Description                                                               |
|------------|---------------------------------------------------------------------------|
| bridge     | The Velux KLF200 represents a gateway to all Velux devices.               |
| scene      | Named ordered set of product states which can be activated for execution. |


### Channels


| Channel Type ID | Item Type | Description                                                     | Thing types  |
|-----------------|-----------|-----------------------------------------------------------------|--------------|
| action          | Switch    | Activates a set of predefined product settings                  | scene        |
| silentMode      | Switch    | Modification of the silent mode of the defined product settings | scene        |
| status          | String    | Current Bridge State                                            | bridge       |
| doDetection     | Switch    | Start of the product detection mode                             | bridge       |
| firmware        | String    | Software version of the Bridge                                  | bridge       |
| ipAddress       | String    | IP address of the Bridge                                        | bridge       |
| subnetMask      | String    | IP subnetmask of the Bridge                                     | bridge       |
| defaultGW       | String    | IP address of the Default Gateway of the Bridge                 | bridge       |
| DHCP            | Switch    | Flag whether automatic IP configuration is enabled              | bridge       |
| WLANSSID        | String    | Name of the wireless network                                    | bridge       |
| WLANPassword    | String    | WLAN Authentication Password                                    | bridge       |
| products        | String    | List of all recognized products                                 | bridge       |
| scenes          | String    | List of all defined scenes                                      | bridge       |
| check           | String    | Checks of current item configuratio                             | bridge       |
| shutter         | Rollersh. | Virtual rollershutter as combination of different scenes        | bridge       |

### Virtual Channel shutter

As the bridge does not support a real rollershutter interaction, this binding provides a virtual rollershutter consisting of different scenes which set a specific shutter level. Therefore the item definition contain multiple pairs of rollershutter levels each followed by a scene name, which leads to this setting.


## Full Example



### Items velux.items

```
/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

/**
 * OpenHAB item defintion for velux binding:
 *  Velux Bridge and Devices
 *
 * @author Guenther Schreiner - Initial contribution
 */

//  Group for simulating push buttons

Group:Switch:OR(ON, OFF)    gV  "PushButton"

// Velux Scene channels

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

// Velux Bridge channels

String  V_FIRMWARE  "Firmware [%s]"                 { velux="thing=bridge;channel=firmware" }
String  V_STATUS    "Status [%s]"                   { velux="thing=bridge;channel=status" }
String  V_CHECK     "Velux Config Check [%s]"           { velux="thing=bridge;channel=check" }

// Velux Shutter channels

Rollershutter V_DG_W_S  "Velux DG Rolladen West [%d]"   { velux="thing=bridge;channel=shutter#0,V_DG_Shutter_West_000,90,V_DG_Shutter_West_090,
100,V_DG_Shutter_West_100"}
Rollershutter V_DG_O_S  "Velux DG Rolladen Ost [%d]"    { velux="thing=bridge;channel=shutter#0,V_DG_Shutter_Ost_000,90,V_DG_Shutter_Ost_090,10
0,V_DG_Shutter_Ost_100"}
Rollershutter V_DG_M_S  "Velux DG Rolladen Mitte [%d]"  { velux="thing=bridge;channel=shutter#0,V_DG_Shutter_Mitte_000,90,V_DG_Shutter_Mitte_09
0,100,V_DG_Shutter_Mitte_100"}
Rollershutter V_DG_M_W  "Velux DG Window Mitte [%d]"    { velux="thing=bridge;channel=shutter#0,V_DG_Window_Mitte_000,10,V_DG_Window_Mitte_010,
100,V_DG_Window_Mitte_100"}

//
// end-of-items/velux.items
//
```

### Sitemap velux.sitemap

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

### Rule velux.rules

```
/**
 * This is a rules to simulate the push button behaviour...
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
                sendCommand(i, OFF)
        ]
    end
```

### Debugging

For those who are interested in more detailed insight view of the processing of this binding, a deeper look can be achieved by increased loglevel.

Try to add the following lines to  `/etc/openhab/logback.xml` for common standalone installation:

```
    <!-- Velux binding logging -->
    <logger name="org.openhab.binding.velux" level="DEBUG">
        <appender-ref ref="TMPEVENTFILE" />
    </logger>
```


During startup of normal operations, there should be only some few messages within the logfile, like:

```
2018-07-23 21:27:09.434 [INFO ] [.b.velux.internal.VeluxBinding] - Velux refresh interval is 3600000 seconds.
2018-07-23 21:27:09.435 [INFO ] [.service.AbstractActiveService] - velux Refresh Service has been started
2018-07-23 21:27:09.435 [INFO ] [.b.velux.internal.VeluxBinding] - veluxConfig[bridgeIPAddress=192.168.41.1,bridgeTCPPort=80,bridgePassword=********,timeoutMsecs=2000,retries=6]

```

## Famous last words

All known <B>Velux<B> devices can be handled by this binding. In fact, there might be some new one which will be reported within the logfiles. Therefore, if you recognize an error message like:

```
20:59:05.721 [ERROR] [g.velux.things.VeluxProductReference] - PLEASE REPORT THIS TO MAINTAINER: VeluxProductReference(3) has found an unregistered ProductTypeId.
```

please pass the appropriate log line back to the maintainer to incorporate the new <B>Velux<B> device type.

