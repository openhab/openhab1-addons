Documentation of the PLCLogo binding Bundle

## Introduction

This binding provides native support of Siemens Logo! PLC devices. Communication with Logo is done via Moka7 library. Currently only two devices are supported: 0BA7 (Logo! 7) and 0BA8 (Logo! 8). Additionally multiple devices are supported. Different families of Logo! devices should work also, but was not tested now due to lack of hardware.  
Binding works nicely at least 100ms polling rate.

**_This binding will be available from 1.9.0 onwards or is available from corresponding snapshot_**

## Generic Item Binding Configuration
Follow blocks can be read from on both device types: `I`, `Q`, `M`, `AI`, `AQ` and `AM`. For Logo! 8 devices `NI`, `NQ`, `NAI` and `NAQ` are supported additionally. `Contact` items are used for digital inputs, `Switch` items for digital inputs/outputs and `Number` items for analog inputs/outputs. 

Writing to the PLC can be done via `VB` (byte values) and `VW` (word values) for both device types. Use `Switch` items for writing of digital and `Number` of analog values.  

The configuration pattern for `Contact` and `Switch` items is  
`plclogo=”<instancename>:<memorylocation>.<maskbit>”`  

Configuration pattern of `Number` items will be  
`plclogo=”<instancename>:<memorylocation>”`  

Some examples are listed below.  

# Binding Configuration in openhab.cfg
plclogo:<plcname>.host= `IP` address of the LOGO! PLC  
plclogo:<plcname>.remoteTSAP= the `TSAP` (in hex) of the `remote` LOGO! PLC, as configured by LogoSoft Comfort. Common used value is 0x0200.  
plclogo:<plcname>:localTSAP= the `TSAP` (in hex) to be used by the `local` instance. Common used value is 0x0300.  
plclogo:refresh= `polling interval` in milliseconds to be used when querying the LOGO!. Default is 5000mS

# Item Configuration
```
Switch ReadOutputQ13 {plclogo="plc:Q13"}
```
read value of output block `Q13` from Logo! device named 'plc'

```
Switch ReadWriteBinaryValue {plclogo="plc:VB0.0"} }
```
read/write binary input block mapped to `VB` address 0, bit 0

```
Number ReadWriteAnalogValue {plclogo="plc:VW5"}
```
read/write analog input block mapped to `VW` address 5
