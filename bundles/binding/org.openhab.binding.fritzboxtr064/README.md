# OpenHAB Fritzbox Binding using TR064 protocol


Hi,
I created a binding for communication with fritzbox using SOAP requests (TR064 protocol). I tested it on

* 7270
* 7360SL (v6.30)
* 7390

**Features:**

* detect if MAC is online in network (presence detection)
* switching on/off 2,4Hz Wifi, 5GHz Wifi and Guest Wifi (if any)
* getting external IP address of fbox
* getting fbox model name
* call monitor
 * Switch Item: Receives "ON" state when call is incoming
 * Call Items: Shows external an internal number for incoming/outgoing calls
 * Resolve external call number to phonebook name
* enabling/disabling telephone answering machines (TAMs) 
* getting new messages per TAM
* getting missed calls for the last x days

**Prerequisites**

Java JRE 1.8.0+ (version 1.7 / openJDK7 is not working)

## Download
Go to [releases] (https://github.com/gitbock/fritzboxtr064/releases) on the top and download the desired *.jar file.

## Installation
Put the *.jar file into your openHAB "addons" directory.

## Configuration

### FritzBox
* Enable TR064: In the webui goto "Heimnetz" - "Netzwerkeinstellungen": enable option "Zugriff für Anwendungen zulassen" (enabled by default)
* Only if you want to use the call monitor feature (items starting with callmonitor_...), enable the interface by dialing #96\*5\* You may disable it again by dialing #96\*4\*

### Item Binding
```
String  fboxName            "FBox Model [%s]"           {fritzboxtr064="modelName"}
String  fboxWanIP           "FBox WAN IP [%s]"          {fritzboxtr064="wanip"}
Switch  fboxWifi24          "2,4GHz Wifi"               {fritzboxtr064="wifi24Switch"}
Switch  fboxWifi50          "5,0GHz Wifi"               {fritzboxtr064="wifi50Switch"}
Switch  fboxGuestWifi       "Guest Wifi"                {fritzboxtr064="wifiGuestSwitch"}
Contact cFboxMacOnline      "Presence (WiFi) [%s]"      {fritzboxtr064="maconline:11-11-11-11-11-11" }

# only when using call monitor
Switch  fboxRinging	 	  	"Phone ringing [%s]"                {fritzboxtr064="callmonitor_ringing" }
Call    fboxIncomingCall   	"Incoming call: [%1$s to %2$s]"     {fritzboxtr064="callmonitor_ringing" } 
Call    fboxOutgoingCall    "Outgoing call: [%1$s to %2$s]"     {fritzboxtr064="callmonitor_outgoing" }

# resolve numbers to names according phonebook
Call    fboxIncomingCallResolved   	"Incoming call: [%1$s to %2$s]"     {fritzboxtr064="callmonitor_ringing:resolveName" } 

# Telephone answering machine (TAM) items
# Number after tamSwitch is ID of configured TAM, start with 0
Switch  fboxTAM0Switch   "Answering machine ID 0"		{fritzboxtr064="tamSwitch:0"}
Number  fboxTAM0NewMsg   "New Messages TAM 0 [%s]"      {fritzboxtr064="tamNewMessages:0"}

# Missed calls: specify the number of last days which should be searched for missed calls
Number  fboxMissedCalls     "Missed Calls [%s]"      	{fritzboxtr064="missedCallsInDays:5"}

```




### openhab.cfg
Add the following to your openhab.cfg and configure the parameters:


```
############################# Fritz!Box TR064 Binding #######################################
#
## Binding for accessing FritzBoxes using the TR064 protocol. Uses http(s) requests.

# URL. Either use http://<fbox-ip>:49000 or https://<fbox-ip>:49443 (https preferred!)
fritzboxtr064:url=https://192.168.178.1:49443

# Refresh Interval (60000ms default)
fritzboxtr064:refresh=60000

# User Name (only use this value if you configured a user in fbox webui/config!)
# If this parameter is missing, "dslf-config" is used as default username
# It is recommended to to switch to authentication by username in fritzbox config
# and add a separate config user for this binding.
#fritzboxtr064:user=dslf-config

# PW
fritzboxtr064:pass=Fr!tZP@ssw0rd
```

## Known issues
* maconline will (sometimes?) [not return the proper online state] (https://github.com/gitbock/fritzboxtr064/issues/1)
* Smartphones sometimes tend to disable Wifi when sleeping to save battery. When this happens, presence detection shows device as offline of course. On Android to prevent disabling Wifi and stay connected the following settings can help:
  * set Wifi only to the band you are using, not auto
  * set stay awake option and enable detection option
 

![screenshot1](http://abload.de/img/screenshot_2015-10-058qse4.png)
![screenshot2](http://abload.de/img/screenshot_2015-10-05zqskj.jpg)

## Debug Logging
Insert the following line into you logback.xml or logback_debug.xml inside the configuration tag.

```
<configuration scan="true">
    [...]

    <!-- FritzBox TR064 binding -->
    <logger name="org.openhab.binding.fritzboxtr064" level="DEBUG" />

    [...]
</configuration>
```
After that, watch your openhab.log file for extended log output.


## Hints

### Sitemap
For the "Call" items use "Text" in your sitemap 

### Map for Presence Detection
Use a map for presence detection item:
Create file $openhab_home/configurations/transform/presence.map and add
```
1=present
0=not present
```
Now, as item configuration use:
```
Contact cFboxMacOnline		"Presence (Wifi) [MAP(presence.map):%d]"	<present>		{fritzboxtr064="maconline:11-22-33-44-55-66 }
```


