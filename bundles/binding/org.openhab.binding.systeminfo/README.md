## Introduction

The System Information binding provides operating system monitoring data, including:

- System memory, swap, CPU, load average, uptime
- Per-process memory, CPU
- File system metrics
- Network interface metrics

The binding uses the Hyperic SIGAR API to access system information regardless of the underlying platform (Windows, Linux, OS X...). 

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In the `openhab.cfg` file (in the folder `${openhab_home}/configurations`):

    ############################### Systeminfo Binding ####################################
    #
    # Interval in milliseconds when to find new refresh candidates
    # (optional, defaults to 1000)
    #systeminfo:granularity=
    
    # Data Storage Unit, where B=Bytes, K=kB, M=MB, T=TB (optional, defaults to M)
    #systeminfo:units=
    
    # New as of 1.9:
    # Alternative native library to load (required for ARM/Linux or custom, not required
    # for standard platforms). Choices for ARM/Linux are cubian, odroid-u3 or raspbian.
    # Results in loading library having the name [lib]sigar-<variant>[.so|.sl|.dll|.dylib]
    #systeminfo:variant=

## Hyperic SIGAR Native libraries

### 1.9

As of version 1.9, the SIGAR native libraries for standard platforms (and cubian, odroid-u3 and raspbian ARM/Linux systems) are included and loaded automatically by the binding.  To use a platform-specific native library that is not [included in the binding JAR](https://github.com/openhab/openhab/tree/master/bundles/binding/org.openhab.binding.systeminfo/lib), place it in your `${openhabhome}/lib` folder and make sure it is named as described above in the instructions for the `systeminfo:variant` configuration property.

### 1.8 and previous

The System Information binding does not include SIGAR native libraries currently. The platform-specific SIGAR native libraries can be found [here](http://sourceforge.net/projects/sigar/files/sigar/1.6/hyperic-sigar-1.6.4.tar.gz/download) for several platforms (see `sigar-bin/lib` folder). The pre-built libraries need to be moved into the `${openhabhome}/lib` folder.

#### ARM-based devices (1.8 and previous)

ARM-based devices, such as Raspberry PI, require manual setup. See [discussion](https://groups.google.com/forum/#!searchin/openhab/systeminfo/openhab/18C7FYpxWTQ/BT_iGycwcKsJ).

There are 3 pre-compiled libraries:

* [Raspbian](https://groups.google.com/group/openhab/attach/ab7030271be23f05/sigar-raspbian.zip?part=0.1)
* [Cubian](https://groups.google.com/group/openhab/attach/ab7030271be23f05/sigar-cubian.zip?part=0.2)
* [Odroid-U3](https://groups.google.com/group/openhab/attach/9a73ad8e7b990530/sigar-odroid-u3.zip?part=0.1)

The instructions below help you to install the library. The example uses raspbian and an ``apt-get`` installation of openHAB as an example, please modify to suit your needs.

Download raspbian library to your RPi:

    $ wget https://groups.google.com/group/openhab/attach/ab7030271be23f05/sigar-raspbian.zip?part=0.1 -O ~/sigar-raspbian.zip

Unzip archive:

    $ unzip ~/sigar-raspbian.zip -d ~

Create a ``lib`` folder first for ``apt-get`` installations:

    $ sudo mkdir /usr/share/openhab/lib

(If you run openHAB from one single folder, please find the path to ``lib`` yourself. Usually: `{openhabhome}/lib`).

Then copy the downloaded files to the new `lib` folder:

    $ sudo cp ~/sigar-raspbian/lib/* /usr/share/openhab/lib

Remove temporary files:

    $ rm -r ~/sigar-raspbian

If you do not want to keep the .zip file, remove it, too.

    $ rm ~/sigar-raspbian.zip

Done! Now install the binding and add your items.

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings by adding some binding information in your item file (in the folder `configurations/items`). The syntax of the binding configuration strings accepted is the following:

    systeminfo="<commandType>:<refreshPeriod>(<target>)"

Where 

`<commandType>` corresponds to the command type. See complete list below.
Note that the output of some commands (eg. DirUsage) will be affected by filesystem permissions. ie. Directories that the process is not permitted access to cannot be include in the tally.

`<refreshPeriod>` corresponds to the update interval of the item in milliseconds.

`<target>` corresponds to the target of the command. Target field is mandatory only for commands, which need target. See further details from supported command list below.

## List of supported commands (commandType)

<table>
  <tr><th>Command</th><th>Item Type</th><th>Purpose</th><th>Note</th></tr>
  <tr><td>LoadAverage1Min</td><td>Number</td><td></td><td></td></tr>
  <tr><td>LoadAverage5Min</td><td>Number</td><td></td><td></td></tr>
  <tr><td>LoadAverage15Min</td><td>Number</td><td></td><td></td></tr>
  <tr><td>CpuCombined</td><td>Number</td><td></td><td></td></tr>
  <tr><td>CpuUser</td><td>Number</td><td></td><td></td></tr>
  <tr><td>CpuSystem</td><td>Number</td><td></td><td></td></tr>
  <tr><td>CpuNice</td><td>Number</td><td></td><td></td></tr>
  <tr><td>CpuWait</td><td>Number</td><td></td><td></td></tr>
  <tr><td>Uptime</td><td>Number</td><td></td><td></td></tr>
  <tr><td>UptimeFormatted</td><td>String</td><td></td><td></td></tr>
  <tr><td>MemFree</td><td>Number</td><td></td><td></td></tr>
  <tr><td>MemFreePercent</td><td>Number</td><td></td><td></td></tr>
  <tr><td>MemUsed</td><td>Number</td><td></td><td></td></tr>
  <tr><td>MemUsedPercent</td><td>Number</td><td></td><td></td></tr>
  <tr><td>MemActualFree</td><td>Number</td><td></td><td></td></tr>
  <tr><td>MemActualUsed</td><td>Number</td><td></td><td></td></tr>
  <tr><td>MemTotal</td><td>Number</td><td></td><td></td></tr>
  <tr><td>SwapFree</td><td>Number</td><td></td><td></td></tr>
  <tr><td>SwapTotal</td><td>Number</td><td></td><td></td></tr>
  <tr><td>SwapUsed</td><td>Number</td><td></td><td></td></tr>
  <tr><td>SwapPageIn</td><td>Number</td><td></td><td></td></tr>
  <tr><td>SwapPageOut</td><td>Number</td><td></td><td></td></tr>
  <tr><td>NetTxBytes</td><td>Number</td><td></td><td>target = net interface name (1</td></tr>
  <tr><td>NetRxBytes</td><td>Number</td><td></td><td>target = net interface name (1</td></tr>
  <tr><td>DiskReads</td><td>Number</td><td></td><td>target = disk name (2</td></tr>
  <tr><td>DiskWrites</td><td>Number</td><td></td><td>target = disk name (2</td></tr>
  <tr><td>DiskReadBytes</td><td>Number</td><td></td><td>target = disk name (2</td></tr>
  <tr><td>DiskWriteBytes</td><td>Number</td><td></td><td>target = disk name (2</td></tr>
  <tr><td>DirUsage</td><td>Number</td><td></td><td>target = directory path (if folder contains lot of files scan can take a while!)</td></tr>
  <tr><td>DirFiles</td><td>Number</td><td></td><td>target = directory path (if folder contains lot of files scan can take a while!)</td></tr>
  <tr><td>ProcessRealMem</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessVirtualMem</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessCpuPercent</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessCpuSystem</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessCpuUser</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessCpuTotal</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessUptime</td><td>Number</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessUptimeFormatted</td><td>String</td><td></td><td>target = process name (3</td></tr>
  <tr><td>ProcessCpuPercent</td><td>Number</td><td></td><td>target = process name (3</td></tr>
</table>

Since 1.7.0
<table>
  <tr><th>Command</th><th>Item Type</th><th>Purpose</th><th>Note</th></tr>
  <tr><td>FileSystemUsed</td><td>Number</td><td></td><td>target = name of the directory on which filesystem is mounted</td></tr>
  <tr><td>FileSystemFree</td><td>Number</td><td></td><td>target = name of the directory on which filesystem is mounted</td></tr>
  <tr><td>FileSystemTotal</td><td>Number</td><td></td><td>target = name of the directory on which filesystem is mounted</td></tr>
  <tr><td>FileSystemUsagePercent</td><td>Number</td><td></td><td>target = name of the directory on which filesystem is mounted</td></tr>
  <tr><td>FileSystemFiles</td><td>Number</td><td></td><td>target = name of the directory on which filesystem is mounted</td></tr>
  <tr><td>FileSystemFreeFiles</td><td>Number</td><td></td><td>target = name of the directory on which filesystem is mounted</td></tr>
</table>

(1 interface name:
Check supported interface names by ifconfig, ipconfig or openhab debug log E.g. "21:56:12.930 DEBUG o.o.b.s.internal.SysteminfoBinding[- valid net interfaces: [lo0, en0, en1, p2p0, vboxnet0](:479])

(2 disk name:
Check supported disk names by iostat or openhab debug log "21:56:12.931 DEBUG o.o.b.s.internal.SysteminfoBinding[- valid disk names: [/dev/disk0s2](:493])"

(3 process name supports:

<table>
  <tr><th>Usage</th><th>Example</th><th>Explanatory</th></tr>
  <tr><td>$$</td><td>$$</td><td>current process</td></tr>
  <tr><td>processname</td><td>eclipse</td><td>process name contains "eclipse"</td></tr>
  <tr><td>`**`processname</td><td>`**`eclipse</td><td>process name ends to "eclipse"</td></tr>
  <tr><td>processname`**`</td><td>eclipse`**`</td><td>process name start with "eclipse"</td></tr>
  <tr><td>=processname</td><td>=eclipse</td><td>process name equals "eclipse"</td></tr>
  <tr><td>#PTQL</td><td>#State.Name.eq=eclipse</td><td>Sigar Process Table Query Language (see https://support.hyperic.com/display/SIGAR/PTQL)</td></tr>
</table>

Examples, how to configure your items:

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

Examples, how to configure your sitemap:

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

## Extensions

System information can be extended in multiple ways.

### Raspberry Pi

The Raspberry Pi provides the `vcgencmd` command. More information about [vcgencmd usage](http://www.elinux.org/RPI_vcgencmd_usage) can be found on the Internet.

A good and working example is to [[read the temperatures|Raspberry Pi System Temperature]].
