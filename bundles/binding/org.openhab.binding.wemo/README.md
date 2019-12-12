# Wemo Binding

This binding integrates the [Belkin WeMo Family](http://www.belkin.com/us/Products/c/home-automation/).  The integration happens either through the WeMo-Link bridge (feature still to come) which acts as an IP gateway to the ZigBee devices, or through Wi-Fi connection to standalone devices.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/wemo/).

## Binding Configuration

The Wemo binding needs no configuration.

## Item Configuration

The syntax of the binding configuration strings accepted is the following:

```
wemo="<UDN>;[<channel-type>]"
```

If you are not sure about your WeMo devices`<UDN>`, have a look in the openHAB log file. The binding lists all discovered WeMo devices when it is started.

The channel type of your item definition is **optional**, it will default to channel type `state`.
The following channel types are possible for Insight switch devices:

| `<channel-type>` | Description |
|------------------|-------------|
| state            | Whether the device is on or off |
| lastChangedAt    | DateTime the device was last turned on or off |
| lastOnFor        | Time in seconds the device was last turned on for |
| onToday          | Time in seconds the device has been on for today |
| onTotal          | Time in seconds the device has been on for in total over timespan period |
| timespan         | Time in seconds over which onTotal applies. Typically 2 weeks except when device first used |
| averagePower     | Average power in Watts. Unclear how this is calculated exactly |
| currentPower     | Current power usage in Watts. 0 if switched off |
| energyToday      | Energy in Wh used today |
| energyTotal      | Energy in Wh used in total |
| standbyLimit     | Minimum amount of energy draw required to register state as on |

## Examples

items:

```
Switch Socket1               { wemo="Socket-1-0-12345678" }
Switch Insight1              { wemo="Insight-1-0-87654321" }
Number Insight_currentPower  "Current Power [%.0f]"  { wemo="Insight-1-0-87654321;currentPower" }
Number Insight_onToday       "On Today [%.0f]"       { wemo="Insight-1-0-87654321;onToday" }
Number Insight_onTotal       "On Total [%.0f]"       { wemo="Insight-1-0-87654321;onTotal" }
Number Insight_energyToday   "Energy Today [%.0fWh]" { wemo="Insight-1-0-87654321;energyToday" }
Contact Motion1              { wemo="Sensor-1-0-56437891" }
```
