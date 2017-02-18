# PLCBus Binding

Currently only "One phase mode" of the PLCBus protocol is supported.

## Binding Configuration

This binding can be configured in the file `services/plcbus.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| port     |         |   Yes    | the name of the serial port you are using to connect, for example `/dev/ttyUSB0` or `COM1` |

## Item Configuration

The format of the binding configuration is simple and looks like this:

```
plcbus="<usercode> <unit> [seconds]"
```

## Examples

Here are some examples of valid binding configuration strings, as defined in the items configuration file:

```
Switch Switch1	          "Switch1"          <plcbus> { plcbus="D1 A1"}
Dimmer Dimmer1	          "Dimmer1"          <plcbus> { plcbus="D1 A2 5"}
Rollershutter Rollershutter1  "Rollershutter 1"  <plcbus> { plcbus="D1 A3"}
```
