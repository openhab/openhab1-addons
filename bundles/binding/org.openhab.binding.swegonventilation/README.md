# Swegon Ventilation Binding 

This binding is used to get live data from Swegon ventilation systems into items in openHAB. It should be compatible with at least Swegon CASA models.

## Prerequisites

`swegongw` is an application that reads packets from a serial port (an RS-485 adapter is needed) and relays them to openHAB via UDP. The Swegon Ventilation binding listens on the UDP port and extracts control data from UDP packets.

C code is available [here](https://github.com/openhab/openhab/blob/master/bundles/binding/org.openhab.binding.swegonventilation/SwegonGW/swegongw.c).

Build: 

```
gcc -std=gnu99 -o swegongw swegongw.c
```

Run:

```
swegongw -h 
swegongw -v -d /dev/ttyUSB0 -a 192.168.1.10
```

## Binding Configuration

This binding can be configured in the file `services/swegonventilation.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| udpPort  | 9998    |    No    | UDP port on which the binding will listen |

## Item Configuration

Format:

```
    swegonventilation="<data>"
```

Where `<data>` identifies a datum to retrieve:

| `<data>`                     | Item Type | Purpose                     | Note |
|------------------------------|-----------|-----------------------------|------|
| T1                           | Number    | Temperature sensor 1        |      | 
| T2                           | Number    | Temperature sensor 2        |      | 
| T3                           | Number    | Temperature sensor 3        |      |  
| T4                           | Number    | Temperature sensor 4        |      | 
| T5                           | Number    | Temperature sensor 5        |      | 
| T6                           | Number    | Temperature sensor 6        |      | 
| T7                           | Number    | Temperature sensor 7        |      | 
| T8                           | Number    | Temperature sensor 8        |      |  
| OutdoorTemperature           | Number    | Temperature sensor          | T1   |
| SupplyAirTemperature         | Number    | Temperature sensor          | T2   |
| ExtractAirTemperature        | Number    | Temperature sensor          | T3   |
| SupplyAirTemperatureReheated | Number    | Temperature sensor          | T4   |
| ExhaustAirTemperature        | Number    | Temperature sensor          | T8   |
| SupplyAirFanSpeed            | Number    | Fan speed  rpm              |      |
| ExtractAirFanSpeed           | Number    | Fan speed  rpm              |      |
| Efficiency                   | Number    | Efficiency                  | Calculated by system |
| EfficiencySupply             | Number    | Efficiency                  | Calculated by binding |
| EfficiencyExtract            | Number    | Efficiency                  | Calculated by binding |
| FanSpeed                     | Number    | Fan speed                   | Fan speed 1…5 |
| PreheatState                 | Switch    | Heating                     | Preheat state |
| ReheatState                  | Switch    | Heating                     | Reheat state |

## Examples

```
Number	OutdoorTemperature	{ swegonventilation="OutdoorTemperature" }
Number	SupplyAirFanSpeed	{ swegonventilation="SupplyAirFanSpeed" }
Switch	Preheat	            { swegonventilation="PreheatState" }
```
