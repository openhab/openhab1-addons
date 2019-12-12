## System Information Binding

The System Information binding provides operating system monitoring data, including system memory, swap, CPU, load average, uptime, per-process memory, per-process CPU, file system metrics and network interface metrics.

The binding uses the Hyperic SIGAR API to access system information regardless of the underlying platform (Windows, Linux, OS X...). 

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/systeminfo/).

## Table of Contents

<!-- MarkdownTOC -->

- [Binding Configuration](#binding-configuration)
	- [Hyperic SIGAR Native libraries](#hyperic-sigar-native-libraries)
- [Item Configuration](#item-configuration)
	- [Notes](#notes)
- [Examples](#examples)
	- [Items](#items)
	- [Sitemaps](#sitemaps)
	- [Raspberry Pi System Temperature](#raspberry-pi-system-temperature)

<!-- /MarkdownTOC -->


## Binding Configuration

This binding can be configured in the file `services/systeminfo.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| granularity | 1000 |   No     | Interval in milliseconds when to find new refresh candidates |
| units    | M       |   No     | Data Storage Unit, where B=Bytes, K=kB, M=MB, T=TB |
| variant  |         | required for ARM/Linux or custom, not required for standard platforms | Alternative native library to load. Choices for ARM/Linux are `cubian`, `odroid-u3` or `raspbian`. This results in loading a native library having the name `[lib]sigar-<variant>[.so|.sl|.dll|.dylib]` |

### Hyperic SIGAR Native libraries

The SIGAR native libraries for standard platforms (and cubian, odroid-u3 and raspbian ARM/Linux systems) are included and loaded automatically by the binding.  To use a platform-specific native library that is not [included in the binding JAR](https://github.com/openhab/openhab1-addons/tree/master/bundles/binding/org.openhab.binding.systeminfo/lib), place it in your `lib` folder and make sure it is named as described above in the instructions for the `variant` configuration property.

## Item Configuration

The syntax of the binding configuration strings accepted is the following:

```
systeminfo="<commandType>:<refreshPeriod>(<target>)"
```

Where:

* `<commandType>` corresponds to the command type. See complete list below. Note that the output of some commands (eg. DirUsage) will be affected by filesystem permissions. ie. Directories that the process is not permitted access to cannot be include in the tally.

* `<refreshPeriod>` corresponds to the update interval of the item in milliseconds.

* `<target>` corresponds to the target of the command. Target field is mandatory only for commands, which need target. See further details from supported command list below.

| Command | Item Type | Purpose | Note |
|---------|-----------|---------|------|
| CpuCombined | Number |  |  |
| CpuNice | Number |  |  |
| CpuSystem | Number |  |  |
| CpuUser | Number |  |  |
| CpuWait | Number |  |  |
| DirFiles | Number |  | target = directory path (if folder contains lot of files scan can take a while!) |
| DirUsage | Number |  | target = directory path (if folder contains lot of files scan can take a while!) |
| DiskReadBytes | Number |  | target = disk name (2) |
| DiskReads | Number |  | target = disk name (2) |
| DiskWriteBytes | Number |  | target = disk name (2) |
| DiskWrites | Number |  | target = disk name (2) |
| FileSystemFiles | Number |  | target = name of the directory on which filesystem is mounted |
| FileSystemFree | Number |  | target = name of the directory on which filesystem is mounted |
| FileSystemFreeFiles | Number |  | target = name of the directory on which filesystem is mounted |
| FileSystemTotal | Number |  | target = name of the directory on which filesystem is mounted |
| FileSystemUsagePercent | Number |  | target = name of the directory on which filesystem is mounted |
| FileSystemUsed | Number |  | target = name of the directory on which filesystem is mounted |
| LoadAverage15Min | Number |  |  |
| LoadAverage1Min | Number |  |  |
| LoadAverage5Min | Number |  |  |
| MemActualFree | Number |  |  |
| MemActualUsed | Number |  |  |
| MemFree | Number |  |  |
| MemFreePercent | Number |  |  |
| MemTotal | Number |  |  |
| MemUsed | Number |  |  |
| MemUsedPercent | Number |  |  |
| NetRxBytes | Number |  | target = net interface name (1) |
| NetTxBytes | Number |  | target = net interface name (1) |
| ProcessCpuPercent | Number |  | target = process name (3) |
| ProcessCpuPercent | Number |  | target = process name (3) |
| ProcessCpuSystem | Number |  | target = process name (3) |
| ProcessCpuTotal | Number |  | target = process name (3) |
| ProcessCpuUser | Number |  | target = process name (3) |
| ProcessRealMem | Number |  | target = process name (3) |
| ProcessUptime | Number |  | target = process name (3) |
| ProcessUptimeFormatted | String |  | target = process name (3) |
| ProcessVirtualMem | Number |  | target = process name (3) |
| SwapFree | Number |  |  |
| SwapPageIn | Number |  |  |
| SwapPageOut | Number |  |  |
| SwapTotal | Number |  |  |
| SwapUsed | Number |  |  |
| Uptime | Number |  |  |
| UptimeFormatted | String |  |  |

### Notes

* (1) interface name:

Check supported interface names by ifconfig, ipconfig or openhab debug log E.g. "21:56:12.930 DEBUG o.o.b.s.internal.SysteminfoBinding[- valid net interfaces: [lo0, en0, en1, p2p0, vboxnet0](:479])

* (2) disk name:

Check supported disk names by iostat or openhab debug log "21:56:12.931 DEBUG o.o.b.s.internal.SysteminfoBinding[- valid disk names: [/dev/disk0s2](:493])"

* (3) process name supports:

| Usage | Example | Explanatory |
|-------|---------|-------------|
| $$    | $$      | current process |
| processname | eclipse | process name contains "eclipse" |
| `**`processname | `**`eclipse | process name ends to "eclipse" |
| processname`**` | eclipse`**` | process name start with "eclipse" |
| =processname | =eclipse | process name equals "eclipse" |
| #PTQL | #State.Name.eq=eclipse | [Sigar Process Table Query Language](https://support.hyperic.com/display/SIGAR/PTQL) |


## Examples

### Items

```
Group System
Number loadAverage1min  "Load avg. 1min [%.1f]" (System) { systeminfo="LoadAverage1Min:5000" }
Number loadAverage5min  "Load avg. 5min [%.1f]" (System) { systeminfo="LoadAverage5Min:5000" }
Number loadAverage15min "Load avg. 15min [%.1f]"    (System) { systeminfo="LoadAverage15Min:5000" }

Number cpuCombined  "CPU combined [%.1f]"   (System) { systeminfo="CpuCombined:5000" }
Number cpuUser  "CPU user [%.1f]"   (System) { systeminfo="CpuUser:5000" }
Number cpuSystem    "CPU system [%.1f]" (System) { systeminfo="CpuSystem:5000" }
Number cpuNice  "CPU nice [%.1f]"   (System) { systeminfo="CpuNice:5000" }
Number cpuWait "CPU wait [%.1f]"    (System) { systeminfo="CpuWait:5000" }

Number uptime   "Uptime [%.1f]" (System) { systeminfo="Uptime:5000" }
String uptimeFormatted  "Uptime [%s]" (System) { systeminfo="UptimeFormatted:5000" }

Number memFreePercentPeriod "MemFree chart selected [%.1f]" (System)
Number memFreePercent   	"Mem free [%.1f%%]" (System) { systeminfo="MemFreePercent:5000" }
Number memUsed  			"Mem used [%.1f]"   (System) { systeminfo="MemUsed:5000" }
Number memUsedPercent   	"Mem used [%.1f%%]" (System) { systeminfo="MemUsedPercent:5000" }
Number memActualFree    	"Mem actual free [%.1f]"    (System) { systeminfo="MemActualFree:5000" }
Number memActualUsed    	"Mem actual used [%.1f]"    (System) { systeminfo="MemActualUsed:5000" }
Number memTotal 			"Mem total [%.1f]"  (System) { systeminfo="MemTotal:5000" }

Number swapFree "Swap free [%.1f]"  (System) { systeminfo="SwapFree:5000" }
Number swapTotal    "Swap total [%.1f]" (System) { systeminfo="SwapTotal:5000" }
Number swapUsed "Swap used [%.1f]"  (System) { systeminfo="SwapUsed:5000" }
Number swapPageIn   "Swap pagein [%.1f]"    (System) { systeminfo="SwapPageIn:5000" }
Number swapPageOut  "Swap pageout [%.1f]"   (System) { systeminfo="SwapPageOut:5000" }

Number netTxBytes   "Next tx bytes [%.1f]"  (System) { systeminfo="NetTxBytes:5000:en1" }
Number netRxBytes   "Next rx bytes [%.1f]"  (System) { systeminfo="NetRxBytes:5000:en1" }

Number diskReads    "Disk reads [%.1f]" (System) { systeminfo="DiskReads:5000:/dev/disk1" }
Number diskWrites   "Disk writes [%.1f]"    (System) { systeminfo="DiskWrites:5000:/dev/disk1" }
Number diskReadBytes    "Disk read bytes [%.1f]"    (System) { systeminfo="DiskReadBytes:5000:/dev/disk1" }
Number diskWriteBytes   "Disk write bytes [%.1f]"   (System) { systeminfo="DiskWriteBytes:5000:/dev/disk1" }

Number dirUsage "Dir usage [%.1f]"  (System) { systeminfo="DirUsage:5000:/Users/foo" }
Number dirFiles "Dir files [%.1f]"  (System) { systeminfo="DirFiles:5000:/Users/foo" }

Number openhabRealMem   "Real mem [%.1f]"   (System) { systeminfo="ProcessRealMem:5000:$$" }
Number openhabVirtualMem    "Virtual mem [%.1f]"    (System) { systeminfo="ProcessVirtualMem:5000:$$" }
Number openhabCpuPercent    "Cpu percent [%.1f%%]"  (System) { systeminfo="ProcessCpuPercent:5000:$$" }
Number openhabCpuSystem "CPU system [%.1f]" (System) { systeminfo="ProcessCpuSystem:5000:$$" }
Number openhabCpuUser   "CPU user [%.1f]"   (System) { systeminfo="ProcessCpuUser:5000:$$" }
Number openhabCpuTotal  "CPU total [%.1f]"  (System) { systeminfo="ProcessCpuTotal:5000:$$" }
Number openhabUptime    "Uptime [%d]"   (System) { systeminfo="ProcessUptime:5000:$$" }
String openhabUptimeFormatted   "Uptime form. [%s]" (System) { systeminfo="ProcessUptimeFormatted:5000:$$" }

Number eclipseRealMem1  "Real mem1 [%.1f%%]"  (System) { systeminfo="ProcessCpuPercent:10000:eclipse" }
Number eclipseRealMem2  "Real mem2 [%.1f%%]"  (System) { systeminfo="ProcessCpuPercent:10000:*eclipse" }
Number eclipseRealMem3  "Real mem3 [%.1f%%]"  (System)  { systeminfo="ProcessCpuPercent:10000:eclipse*" }
Number eclipseRealMem4  "Real mem4 [%.1f%%]"  (System) { systeminfo="ProcessCpuPercent:10000:=eclipse" }
Number eclipseRealMem5  "Real mem5 [%.1f%%]"  (System) { systeminfo="ProcessCpuPercent:10000:#State.Name.eq=eclipse" }
```

### Sitemaps

```
Frame {
	Group item=System label="System Info" icon="system" {
		Frame {
			Text item=uptime
			Text item=cpuCombined icon="system"
		}
		
		Frame label="Load" {
			Text item=loadAverage1min
			Text item=loadAverage5min
			Text item=loadAverage15min
		}
		Frame label="Memory" {
			Text item=memFreePercent  {
				Frame {
					Switch item=memFreePercentPeriod label="Periode" mappings=[0="Time", 1="Dag", 2="Uke"]
					Chart item=memFreePercent period=h refresh=30000 visibility=[memFreePercentPeriod==0, memFreePercentPeriod=="Uninitialized"]
					Chart item=memFreePercent period=D refresh=30000 visibility=[memFreePercentPeriod==1]
					Chart item=memFreePercent period=W refresh=30000 visibility=[memFreePercentPeriod==2]
				}
			}
			Text item=memUsed 
			Text item=memUsedPercent
			Text item=memActualFree 
			Text item=memActualUsed 
			Text item=memTotal 
		}
		
		Frame label="Swap" {
			Text item=swapFree 
			Text item=swapTotal
			Text item=swapUsed 
		}
		
		Frame label="Openhab" {
			Text item=openhabRealMem
			Text item=openhabVirtualMem
			Text item=openhabCpuPercent
			Text item=openhabCpuSystem
			Text item=openhabUptime
			Text item=openhabUptimeFormatted
		}
	}
}
```

### Raspberry Pi System Temperature

Although unrelated to the System Information binding, you can augment the capability of this binding using other add-ons.

#### Prerequisites

* openHAB has been installed on Raspberry Pi
* The Exec 1.x Binding has been installed
* RRD4J Persistence has been installed
* The JavaScript transformation service has been installed
* User `openhab` is member of the `video` group

The user `openhab` needs to be member of the `video` group to be able to run the `vcgencmd` command. Otherwise you will see a [VCHI initialization failed](http://raspberrypi.stackexchange.com/questions/7546/munin-node-plugins-vchi-initialization-failed) error message.

The effect of this group membership is only taken after a reboot.

```
$ sudo usermod -a -G video openhab
$ sudo reboot
```

#### Solution

This solution was developed for a [Raspberry Pi 2 model B](https://www.raspberrypi.org/products/raspberry-pi-2-model-b/) running [Raspbian](https://www.raspberrypi.org/downloads/raspbian/)) and an "apt-get" installation of openHAB.

The temperatures for both CPU and GPU can be read through terminal commands:

```
$ cat /sys/class/thermal/thermal_zone0/temp
46540
$ /opt/vc/bin/vcgencmd measure_temp
temp=47.1'C
```

This means the output can be captured using the Exec Binding.

However, the CPU temperature is returned as millidegrees Celsius. The GPU temperature has some surrounding text for readability. Both need some transformations to get the values into openHAB properly.

The CPU temperature is computed through a Javascript transformation.
The GPU temperature is captured through a Regex transformation.

This example gets the CPU temperature in degrees Celsius every 60 seconds and persistently stores them for presenting in a graph. It assumes that all items containing system information (potentially including those for the System Information Binding) are in a `systems.items` file (any other items file will do).

#### Example configuration

Create a `transform/milli.js` file with this content:

```
(function(i){ return i / 1000; })(input)
```

Add to `items/system.items` file:

```
// System temperatures
Group  System_Temperature_Chart (System, Charts)
Number System_Temperature_Chart_Period "Periode" (System)
Number System_Temperature_CPU "Temperature CPU [%.1f °C]" <temperature> (System_Temperature_Chart) { exec="<[cat /sys/class/thermal/thermal_zone0/temp:60000:JS(milli.js)]" }
Number System_Temperature_GPU "Temperature GPU [%.1f °C]" <temperature> (System_Temperature_Chart) { exec="<[/opt/vc/bin/vcgencmd measure_temp:60000:REGEX(temp=(.*?)'C)]" }
```

Add to `rrd4j.persist` file:

```java
Strategies {
    // for rrd charts, we need a cron strategy
    everyMinute : "0 * * * * ?"
}

Items {
    // persist items on every change and every minute
    System_Temperature_Chart* : strategy = everyChange, everyMinute, restoreOnStartup
}
```

Add to `sitemaps/default.sitemap` file:

```
Text item=System_Temperature_CPU label="Temperature [%.1f °C]" {
	Frame {
		Text item=System_Temperature_CPU					
		Text item=System_Temperature_GPU
	}
	Frame {
		Switch item=System_Temperature_Chart_Period mappings=[0="1h", 1="4h", 2="8h", 3="12h", 4="24h"]
		Chart  item=System_Temperature_Chart period=h   refresh=60000 visibility=[System_Temperature_Chart_Period==0, System_Temperature_Chart_Period=="Uninitialized"]
		Chart  item=System_Temperature_Chart period=4h  refresh=60000 visibility=[System_Temperature_Chart_Period==1]
		Chart  item=System_Temperature_Chart period=8h  refresh=60000 visibility=[System_Temperature_Chart_Period==2]
		Chart  item=System_Temperature_Chart period=12h refresh=60000 visibility=[System_Temperature_Chart_Period==3]
		Chart  item=System_Temperature_Chart period=D   refresh=60000 visibility=[System_Temperature_Chart_Period==4]
	}
}
```
