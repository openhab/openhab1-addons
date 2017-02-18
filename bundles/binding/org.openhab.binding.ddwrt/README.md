# DD-WRT Binding

The openHAB DD-WRT Binding allows interaction with a DD-WRT device.


## Prerequisites

In order to use the binding with a DD-WRT device, the telnet connection must be activated in the DD-WRT web interface.
This process does not always work. Test it with a telnet command shell.


## Binding Configuration

The binding can be configured in the file `services/ddwrt.cfg`.

| Property        | Default | Required | Description                  |
|-----------------|---------|:--------:|------------------------------|
| ip              |         | No       | The IP address of the device |
| port            | 23      | No       | The port to be used          |
| username        |         | No       | The username for the device  |
| password        |         | No       | The password for the device  |
| interface_24    |         | No       | The 2.4 GHz wifi interface   |
| interface_50    |         | No       | The 5 GHz wifi interface     |
| interface_guest |         | No       | The guest wifi interface     |


## Item Configuration

Item bindings should conform to the following format:

```
    ddwrt="<key>"
```

Where `<key>` may take any of these values:

| Key        |
|------------|
| routertype |
| wlan24     |
| wlan50     |
| wlanguest  |


## Examples

    String DEVICE_NAME {ddwrt="routertype"}
    Switch WIFI_24     {ddwrt="wlan24"}
    Switch WIFI_50     {ddwrt="wlan50"}
    Switch WIFI_GUEST  {ddwrt="wlanguest"}


## Notes

There is a bug in the DD-WRT firmware. The activation of this interface needs a workaround so it takes some seconds longer than the native devices.

Tested with Archer V2 and DD-WRT v3.0-r30880 std (11/14/16).
