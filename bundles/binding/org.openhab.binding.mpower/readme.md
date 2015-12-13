# Introduction

A binding for Ubiquiti mPower strips.

The binding supports
- Sending on/off commands
- Receiving data from the sensors
- Multiple strips

The binding has only been tested with the EU 6 port version. Please provide feedback to the author in case you are using different devices.

# Setup

This binding requires Firmware version "MF.v2.1.11". installed on your mPower strip(s). In case you need to update, use the url 

```
http://<ip of you mpower>/system.cgi
```


In your openhab.cfg add the following lines

```
mpower:refresh=<connection check intervall in ms>
mpower:<smybolic name>.host=<ip of your mPower>
mpower:<smybolic name>.user=<username>
mpower:<smybolic name>.password=<password>
mpower:<smybolic name>.refresh=<refresh intervall in ms>
```

For multiple strips replicate this section with another symbolic name. E.g.:

```
mpower:refresh=60000

mpower:mp1.host=192.168.0.15
mpower:mp1.user=ubnt
mpower:mp1.password=secret
mpower:mp1.refresh=5000

mpower:mp2.host=192.168.0.27
mpower:mp2.user=ubnt
mpower:mp2.password=secret
mpower:mp2.refresh=5000
```

Remarks:
- for the option "mpower:refresh": it defines how often the SSH connection will be checked. Best to set this value at 60000.
- for the option "mpower:<smybolic name>.refresh": it defines how often the items are updated (in ms). In case the data has not changed (same voltage etc) no update will be performed. A value of 5000 is appropriate.


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

In case you want to persist the daily energy consumption I suggest the following approach:

Make sure that the "cf_count" values are reset each night. You can do this by connecting to your mPower device via SSH and adding the following lines to /tmp/system.cfg 

```
cron.1.job.1.cmd=echo 1 > /proc/power/clear_ae4;echo 1 > /proc/power/clear_ae5;echo 1 > /proc/power/clear_ae6;echo 1 > /proc/power/clear_ae1;echo 1 > /proc/power/clear_ae2;echo 1 > /proc/power/clear_ae3;
cron.1.job.1.schedule=59 23 * * *
cron.1.job.1.status=enabled
cron.1.status=enabled
cron.1.user=ubnt
cron.status=enabled
```

You need to apply your changes with the "save" command.

After some persistence and chart configuration you will be able to closely monitor energy consumption with your mPower device.
