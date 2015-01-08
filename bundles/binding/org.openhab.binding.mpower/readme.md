# Introduction

A binding for Ubiquiti mPower strips.

The binding supports
- Sending on/off commands
- Receiving data from the sensors
- Multiple strips

# Setup

This binding requires Firmware version "MF.v2.1.4". installed on your mPower strip(s). In case you need to update, use the url 

```
http://<ip of you mpower>/system.cgi
```


In your openhab.cfg add the following lines

```
mpower:<smybolic name>.host=<ip of your mPower>
mpower:<smybolic name>.user=<username>
mpower:<smybolic name>.password=<password>
mpower:<smybolic name>.secure=<true|false>
mpower:<smybolic name>.refresh=<refresh intervall in ms>
```

For multiple strips replicate this section with another symbolic name. E.g.:

```
mpower:mp1.host=192.168.0.15
mpower:mp1.user=ubnt
mpower:mp1.password=secret
mpower:mp1.secure=true
mpower:mp1.refresh=10000

mpower:mp2.host=192.168.0.27
mpower:mp2.user=ubnt
mpower:mp2.password=secret
mpower:mp2.secure=false
mpower:mp2.refresh=60000
```

Remarks:
- for the option "refresh": it defines how often the items are updated (in ms). In case the data has not changed (same voltage etc) no update will be performed.
- for the option "secure": use values "true" or "false" to enable a HTTPS connection.


# Item Binding Configuration

For your item bindings the following item addressing is available:
```
{mpower}="<smybolic name>:<socket number>:<switch|voltage|energy|power>
```
For example this is a full set of items for a 6-port mPower strip

```
Switch mp1 "mp1-1" {mpower="mp1:1:switch"}
Switch mp2 "mp1-2" {mpower="mp1:2:switch"}
Switch mp3 "mp1-3" {mpower="mp1:3:switch"}
Switch mp4 "mp1-4" {mpower="mp1:4:switch"}
Switch mp5 "mp1-5" {mpower="mp1:5:switch"}
Switch mp6 "mp1-6" {mpower="mp1:6:switch"}

Number mp1Volt "mp1-1 voltage [%d V]" {mpower="mp1:1:voltage"}
Number mp2Volt "mp1-2 voltage [%d V]" {mpower="mp1:2:voltage"}
Number mp3Volt "mp1-3 voltage [%d V]" {mpower="mp1:3:voltage"}
Number mp4Volt "mp1-4 voltage [%d V]" {mpower="mp1:4:voltage"}
Number mp5Volt "mp1-5 voltage [%d V]" {mpower="mp1:5:voltage"}
Number mp6Volt "mp1-6 voltage [%d V]" {mpower="mp1:6:voltage"}

Number mp1Energy "mp1-1 energy [%d Wh]" {mpower="mp1:1:energy"}
Number mp2Energy "mp1-2 energy [%d Wh]" {mpower="mp1:2:energy"}
Number mp3Energy "mp1-3 energy [%d Wh]" {mpower="mp1:3:energy"}
Number mp4Energy "mp1-4 energy [%d Wh]" {mpower="mp1:4:energy"}
Number mp5Energy "mp1-5 energy [%d Wh]" {mpower="mp1:5:energy"}
Number mp6Energy "mp1-6 energy [%d Wh]" {mpower="mp1:6:energy"}

Number mp1Power "mp1-1 power [%.1f W]" {mpower="mp1:1:power"}
Number mp2Power "mp1-2 power [%.1f W]" {mpower="mp1:2:power"}
Number mp3Power "mp1-3 power [%.1f W]" {mpower="mp1:3:power"}
Number mp4Power "mp1-4 power [%.1f W]" {mpower="mp1:4:power"}
Number mp5Power "mp1-5 power [%.1f W]" {mpower="mp1:5:power"}
Number mp6Power "mp1-6 power [%.1f W]" {mpower="mp1:6:power"}
```

# Persistence




# Warning
This binding is still under development. Don't use it in production.
