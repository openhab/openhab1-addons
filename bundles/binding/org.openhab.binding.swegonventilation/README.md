## Introduction

Swegon ventilation binding is used to get live data from Swegon ventilation systems. Binding should be compatible with at least Swegon CASA models.

## Swegon gateway

**swegongw** is an application that reads packets from serial port (RS-485 adapter is needed) and relays them to openHAB via UDP. Swegon ventilation binding listens on the UDP port and extracts control data from UDP packets.

C code is available [here](https://github.com/openhab/openhab/blob/master/bundles/binding/org.openhab.binding.swegonventilation/SwegonGW/swegongw.c)  

Build command: 

    gcc -std=gnu99 -o swegongw swegongw.c

Execution:

    swegongw -v -d /dev/ttyUSB0 -a 192.168.1.10

`swegongw` help is available by executing the following command:

    swegongw -h 

For the installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

`openhab.cfg` file (in `${openhab_home}/configurations` folder).

    ######################## Swegon ventilation Binding ###################################
    #
    # UDP port (optional, defaults to 9998)
    # swegonventilation:udpPort =9998

The `swegonventilation:udpPort` value specifies UDP port on which the binding will listen. Configuration is optional, by default binding listens on UDP port 9998.

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder `configurations/items`). Example of the item binding:

    swegonventilation="<data>"

Where `<data>` identifies a data item. See a complete list below.

## List of supported data items

| Data | Item Type | Purpose                     | Note |
| ------- | --------- | --------------------------- | ---- |
| T1      | Number    | Temperature sensor 1  || 
| T2      | Number    | Temperature sensor 2  || 
| T3      | Number    | Temperature sensor 3  ||  
| T4      | Number    | Temperature sensor 4  || 
| T5      | Number    | Temperature sensor 5  || 
| T6      | Number    | Temperature sensor 6  || 
| T7      | Number    | Temperature sensor 7  || 
| T8      | Number    | Temperature sensor 8  ||  
| OutdoorTemperature | Number    | Temperature sensor  | T1 |
| SupplyAirTemperature | Number    | Temperature sensor  | T2 |
| ExtractAirTemperature | Number    | Temperature sensor  | T3 |
| SupplyAirTemperatureReheated | Number    | Temperature sensor  | T4 |
| ExhaustAirTemperature | Number    | Temperature sensor  | T8 |
| SupplyAirFanSpeed | Number    | Fan speed  rpm | |
| ExtractAirFanSpeed | Number    | Fan speed  rpm | |
| Efficiency | Number    | Efficiency  | Calculated by system |
| EfficiencySupply | Number    | Efficiency  | Calculated by binding |
| EfficiencyExtract | Number    | Efficiency  | Calculated by binding |
| FanSpeed | Number    | Fan speed | Fan speed 1…5 |
| PreheatState | Switch    | Heating  | Preheat state |
| ReheatState | Switch    | Heating | Reheat state |

## Examples

    Number	OutdoorTemperature	{ swegonventilation="OutdoorTemperature" }
    Number	SupplyAirFanSpeed	{ swegonventilation="SupplyAirFanSpeed" }
    Switch	Preheat	                { swegonventilation ="PreheatState" }