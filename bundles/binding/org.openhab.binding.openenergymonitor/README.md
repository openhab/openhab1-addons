Documentation of the Open Energy Monitor binding Bundle

## Introduction

[Open Energy Monitor](http://openenergymonitor.org) binding is used to get live data from open energy monitor devices.

## RFM12Pi gateway

**rfm12pigw** is application read telegram from RFM12Pi module and relay untouched telegrams to openhab via UDP packet. Open Energy Monitor binding listening UDP port and parse data from UDP telegrams.

C code is available on [here](https://github.com/openhab/openhab/blob/master/bundles/binding/org.openhab.binding.openenergymonitor/RFM12PiGW/rfm12pigw.c)  

#Note: To work with the new [RFM69Pi module](http://wiki.openenergymonitor.org/index.php/RFM69Pi_V3) serial baud 38400 must be used

build command: 

    gcc -std=gnu99 -o rfm12pigw rfm12pigw.c

execution:

    rfm12pigw -v -d /dev/ttyAMA0 -a 192.168.1.10

rfm12pigw help is avail be by command
execution:

    rfm12pigw -h 

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

openhab.cfg file (in the folder '${openhab_home}/configurations').

    ########################### Open Energy Monitor Binding ###############################
    #
    # UDP port of the Open Energy Monitor devices (optional, defaults to 9997)
    #openenergymonitor:udpPort=9997
    #
    #openenergymonitor:<parser rule name>=<node address>:<data type(byte indexes)>
    #openenergymonitor:<parser rule name>=<node address>:<data type(byte indexes)>

The `openenergymonitor:udpPort` value specify UDP port which binding will listening. Configuration is optional, by default binding listening UDP port 9997.

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    openenergymonitor =""

Where 

…


## Examples (openhab.cfg)

    openenergymonitor:phase1RealPower=10:U16(2|1)
    openenergymonitor:phase1ApparentPower=10:U16(4|3)
    openenergymonitor:phase1Current=10:U16(6|5)
    openenergymonitor:phase1PowerFactor=10:U16(8|7)
    openenergymonitor:phase2RealPower=10:U16(10|9)
    openenergymonitor:phase2ApparentPower=10:U16(12|11)
    openenergymonitor:phase2Current=10:U16(14|13)
    openenergymonitor:phase2PowerFactor=10:U16(16|15)
    openenergymonitor:phase3RealPower=10:U16(18|17)
    openenergymonitor:phase3ApparentPower=10:U16(20|19)
    openenergymonitor:phase3Current=10:U16(22|21)
    openenergymonitor:phase3PowerFactor=10:U16(24|23)
    openenergymonitor:realPower=10:U16(26|25)
    openenergymonitor:apparentPower=10:U16(28|27)
    openenergymonitor:voltage=10:U16(30|29)
    openenergymonitor:pulseCount=10:U32(34|33|32|31)
    openenergymonitor:pulsePower=10:U16(36|35)

## Examples (items)

    Number RealPower { openenergymonitor="realPower" }
    Number ApparentPower { openenergymonitor="apparentPower" }
    Number Voltage { openenergymonitor="voltage" }
    Number Phase1RealPower { openenergymonitor="phase1RealPower" }
    Number Phase1ApparentPower { openenergymonitor="phase1ApparentPower" }
    Number Phase1Current { openenergymonitor="phase1Current:JS(divideby100.js)" }
    Number Phase1PowerFactor { openenergymonitor="phase1PowerFactor" }
    Number Phase2RealPower { openenergymonitor="phase2RealPower" }
    Number Phase2ApparentPower { openenergymonitor="phase2ApparentPower" }
    Number Phase2Current { openenergymonitor="phase2Current:JS(divideby100.js)" }
    Number Phase2PowerFactor { openenergymonitor="phase2PowerFactor" }
    Number Phase3RealPower { openenergymonitor="phase3RealPower" }
    Number Phase3ApparentPower { openenergymonitor="phase3ApparentPower" }
    Number Phase3Current { openenergymonitor="phase3Current / 100" }
    Number Phase3PowerFactor { openenergymonitor="phase3PowerFactor" }
    Number PulseEnergy { openenergymonitor="cumulative(pulseCount):JS(divideby5000.js)" }
    Number PulsePower { openenergymonitor="pulsePower" }
    Number Phase123RealPower { openenergymonitor="phase1RealPower+phase2RealPower+phase3RealPower" }
    Number Phase123Current { openenergymonitor="phase1Current+phase2Current+phase3Current:JS(divideby100.js)" }