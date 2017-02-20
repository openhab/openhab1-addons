# Open Energy Monitor Binding

The [Open Energy Monitor](http://openenergymonitor.org) binding is used to get live data from open energy monitor devices.

## Prerequisites

`rfm12pigw` is an application that reads telegrams from an RFM12Pi module and relays untouched telegrams to openHAB via UD. Open Energy Monitor binding listen to a UDP port and parses data from UDP telegrams.

C code is available on [here](https://github.com/openhab/openhab1-addons/blob/master/bundles/binding/org.openhab.binding.openenergymonitor/RFM12PiGW/rfm12pigw.c)  

> Note: To work with the new [RFM69Pi module](http://wiki.openenergymonitor.org/index.php/RFM69Pi_V3), serial bitrate 38400 must be used

build command: 

```shell
    gcc -std=gnu99 -o rfm12pigw rfm12pigw.c
```

execution:

```shell
    rfm12pigw -h 
    rfm12pigw -v -d /dev/ttyAMA0 -a 192.168.1.10
```

## Binding Configuration

This binding can be configured in the file `services/openenergymonitor.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| udpPort  | 9997    |   No     | UDP port of the Open Energy Monitor devices |
| `<rule>` |         |   No     | `<node address>:<data type(byte indexes)>` |

where `<rule>` is in the format used in this example:

```
phase1RealPower=10:U16(2|1)
phase1ApparentPower=10:U16(4|3)
phase1Current=10:U16(6|5)
phase1PowerFactor=10:U16(8|7)
phase2RealPower=10:U16(10|9)
phase2ApparentPower=10:U16(12|11)
phase2Current=10:U16(14|13)
phase2PowerFactor=10:U16(16|15)
phase3RealPower=10:U16(18|17)
phase3ApparentPower=10:U16(20|19)
phase3Current=10:U16(22|21)
phase3PowerFactor=10:U16(24|23)
realPower=10:U16(26|25)
apparentPower=10:U16(28|27)
voltage=10:U16(30|29)
pulseCount=10:U32(34|33|32|31)
pulsePower=10:U16(36|35)
```

## Item Configuration

The syntax accepted is:

```
openenergymonitor="<rule>"
```

where `<rule>` was introduced in the binding configuration described above.

### Examples

```
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
```
