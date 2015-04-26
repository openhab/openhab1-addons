openhab-harmony-binding
=======================

openHAB Binding for the Logitech Harmony Hub


### Introduction

The Harmony Hub binding is used to enable communication between openHAB and multiple Logitech Harmony Hub devices. The API exposed by the Harmony Hub is relatively limited, but it does allow for reading the current activity as well as setting the activity and sending device commands.

### Installing

Add the org.openhab.binding.harmonyhub<version>.jar, org.openhab.io.harmonyhub<version>.jar and optionally the org.openhab.action.harmonyhub<version>.jar to the addons folder of an openHAB runtime installation and restart.

### Usage

#### Configuration

The following configuration items are required to be set in openhab.cfg:

	harmonyhub:host=<local ip address of your hub>
	harmonyhub:username=<your logitech username>
	harmonyhub:password=<your logitech password>

Optionally you may add a unique qualifier to your configuration to support multiple hubs


	harmonyhub:qualifier.host=<local ip address of your hub>
	harmonyhub:qualifier.username=<your logitech username>
	harmonyhub:qualifier.password=<your logitech password>

replace 'qualifier' with a string that describes the hub (ex: familyroom )

#### Bindings

The Harmony binding supports both outgoing and incoming item bindings of the form:

    { harmonyhub="<direction>[<optional qualifier>:<binding> ...]" }

`<direction>` may be :

Incoming (IN) (read only)

	<[....]

Outbound (OUT) (write only)

	>[....]
Bi-Directional (BOTH) (read and write)

	*[....]

and where `<binding>` can be:

##### Current activity (status)
Current Activity supports both IN(<), OUT(>) and BOTH(*) directions.

Displays the current activity:

    String Harmony_Activity         "activity [%s]" { harmonyhub="<[currentActivity]" }

Displays the current activity for a named hub:

    String Harmony_Activity         "activity [%s]" { harmonyhub="<[familyroom:currentActivity]" }

Optionally a string from can be sent to a current activity binding (ex: from an button).

	String Harmony_Activity         "activity [%s]" { harmonyhub="*[familyroom:currentActivity]" }

And in the Sitemap.

	Switch item=Harmony_Activity mappings=[PowerOff='PowerOff', Roku='Roku',TiVo='TiVo']
    
##### Start activity (command)

Start Activity supports the OUT(>) direction.

Start the specified activity (where activity can either be the activity id or label).

	String HarmonyHubPowerOff       "powerOff"      { harmonyhub=">[start:PowerOff]" }
	String HarmonyHubWatchTV        "watchTV"       { harmonyhub=">[start:Watch TV]" }

Start the specified activity on a named hub (where activity can either be the activity id or label).

	String HarmonyHubPowerOff       "powerOff"      { harmonyhub=">[familyroom:start:PowerOff]" }
	String HarmonyHubWatchTV        "watchTV"       { harmonyhub=">[familyroom:start:Watch TV]" }

##### Press button (command)

Start Activity supports the OUT(>) direction.

Press the specified button on the given device (where device can either be the device id or label).

	String HarmonyHubMute           "mute"          { harmonyhub=">[press:Denon AV Receiver:Mute]" }

Press the specified button on the given named device (where device can either be the device id or label).

	String HarmonyHubMute           "mute"          { harmonyhub=">[familyroom:press:Denon AV Receiver:Mute]" }


#### Actions

The following actions are supported in rules:

##### Press button

	harmonyPressButton(<device>, <command>)
	harmonyPressButton(<qualifier>,<device>, <command>)

##### Start activity

	harmonyStartActivity(<activity>)
	harmonyStartActivity(<qualifier>,<activity>)

#### Other
 Under debug logging the IO bundle will print the list of devices, activities and the full JSON config for each hub.  This can be used to populate items.
 
### Shell

The [harmony-java-client](https://github.com/tuck182/harmony-java-client) project on GitHub provides a simple shell for querying a Harmony Hub, and can be used to retrieve the full configuration of the hub to determine ids of available activities and devices as well as device commands (buttons).

