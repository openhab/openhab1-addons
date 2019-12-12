# Harmony Hub Binding

The Harmony Hub binding is used to enable communication between openHAB and one or more Logitech Harmony Hub devices. The API exposed by the Harmony Hub is relatively limited, but it does allow for reading the current activity as well as setting the activity and sending device commands.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/harmonyhub/).

## Binding Configuration

The binding can be configured in the file `services/harmonyhub.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| discoveryName |    |   Yes    | Name given to your hub when setup |
| `<qualifier>`.discoveryName | | No | Optionally you may add a unique qualifier to your configuration to support multiple hubs |

### Examples

```
discoveryName=Great Room
greatroom.discoveryName=Great Room
```

## Item Configuration

The Harmony Hub binding supports both outgoing and incoming item bindings of the form:

```
{ harmonyhub="<direction>[<optional qualifier>:<binding> ...]" }
```

`<direction>` may be:

Incoming (IN) (read only)

```
<[....]
```

Outbound (OUT) (write only)

```
>[....]
```

Bi-Directional (BOTH) (read and write)

```
*[....]
```

and where `<binding>` can be:

### Current activity (status)

Current Activity supports both IN(<), OUT(>) and BOTH(*) directions.

Displays the current activity:

```
String Harmony_Activity         "activity [%s]" { harmonyhub="<[currentActivity]" }
```

Displays the current activity for a named hub:

```
String Harmony_Activity         "activity [%s]" { harmonyhub="<[familyroom:currentActivity]" }
```

Optionally a string from can be sent to a current activity binding (ex: from an button).

```
String Harmony_Activity         "activity [%s]" { harmonyhub="*[familyroom:currentActivity]" }
```

And in your Sitemap:

```
Switch item=Harmony_Activity mappings=[PowerOff='PowerOff', Roku='Roku',TiVo='TiVo']
```

### Start activity (command)

Start Activity supports the OUT(>) direction.

Start the specified activity (where activity can either be the activity id or label).

```
String HarmonyHubPowerOff       "powerOff"      { harmonyhub=">[start:PowerOff]" }
String HarmonyHubWatchTV        "watchTV"       { harmonyhub=">[start:Watch TV]" }
```

Start the specified activity on a named hub (where activity can either be the activity id or label).

```
String HarmonyHubPowerOff       "powerOff"      { harmonyhub=">[familyroom:start:PowerOff]" }
String HarmonyHubWatchTV        "watchTV"       { harmonyhub=">[familyroom:start:Watch TV]" }
```

### Press button (command)

Start Activity supports the OUT(>) direction.

Press the specified button on the given device (where device can either be the device id or label).

```
String HarmonyHubMute           "mute"          { harmonyhub=">[press:Denon AV Receiver:Mute]" }
```

Press the specified button on the given named device (where device can either be the device id or label).

```
String HarmonyHubMute           "mute"          { harmonyhub=">[familyroom:press:Denon AV Receiver:Mute]" }
```

See also the Harmony Hub Actions for performing actions from openHAB rules.

## Other

Under debug logging the IO bundle will print the list of devices, activities and the full JSON config for each hub.  This can be used to populate items.

The JSON configuration can also be obtained using the Harmony Android app by syncing with the hub (Menu-> Harmony Setup - > Sync).  The configuration will be saved in  home/Harmony/userConfig.json. 
 
## Shell

The [harmony-java-client](https://github.com/tuck182/harmony-java-client) project on GitHub provides a simple shell for querying a Harmony Hub, and can be used to retrieve the full configuration of the hub to determine ids of available activities and devices as well as device commands (buttons).
