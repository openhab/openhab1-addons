# WAGO Binding

The WAGO Binding provides an interface to Ethernet-enabled Modbus-Controllers from WAGO.

It polls the controller in a configurable interval.

It fetches the configuration xml-file from the WAGO-Controller and derives the used modules, their position and their type from it.

## Binding Configuration

This binding can be configured in the file `services/wago.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  |  ???    |    No    | poll interval in milliseconds |
| `<slave-name>`.ip | |   Yes   | Modbus slave IP address |
| `<slave-name>`.username | | Yes | Modbus slave username |
| `<slave-name>`.password | | Yes | Modbus slave password |


## Item Configuration

```
{wago="<slave-name>:<module>:<coil>"}
```

## Example

Example for a simple switch item bound to coil 3 of module 1 of "slave2":<

```
Switch MySwitch "My WAGO Switch" (ALL) {wago="slave2:1:3"}
```

