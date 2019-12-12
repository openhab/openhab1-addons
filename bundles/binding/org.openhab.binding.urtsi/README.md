# Somfy URTSI II Binding

This binding communicates with Somfy URTSI II devices over a serial connection.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/urtsi/).

## Binding Configuration

This binding can be configured in the file `services/urtsi.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<deviceId>`.port | | Yes    | identification of the serial port on the host system, e.g. "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac. |

where:

* `<deviceId>` is a name you choose to identify which device you want to control via your items.  You can specify multiple devices in the configuration.

## Item Configuration

The format of the binding configuration is simple and looks like this:

```
urtsi="<deviceId>:<channelId>"
```

where:

* `<deviceId>` corresponds device which is introduced in `services/urtsi.cfg`.
* `<channelId>` is the configured RTS channel you want the item to bind to. Each URTSI device supports up to 16 channels (1 - 16).

Only Rollershutter items are allowed to use this binding. The binding is able to handle UP, DOWN and MOVE commands.

As a result, your lines in the items file might look like the following:

```
Rollershutter RollershutterKitchen       "Kitchen"         { urtsi="device1:1" }
Rollershutter RollershutterLivingRoom    "Living room"     { urtsi="device1:2" }
```
