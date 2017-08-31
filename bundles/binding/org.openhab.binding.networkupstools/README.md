# Network UPS Tools Binding

The primary goal of the [Network UPS Tools](http://www.networkupstools.org/) (NUT) project is to provide support for power devices, such as uninterruptible power supplies (UPS), Power Distribution Units and Solar Controllers.

Network UPS Tools (NUT) provides many control and monitoring features, with a uniform control and management interface.
More than 100 different manufacturers, and several thousands of models are compatible.

This binding lets you integrate NUT servers with openHAB.

## Binding Configuration

This binding can be configured in the file `services/networkupstools.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<instance name>.<parameter>` |  | Yes | X |
| refresh  |         |   No     | refresh interval for state updates in milliseconds |
| `<name>`.device |  |   Yes    | UPS device name, `ups` for example |
| `<name>`.host |    |   No     | UPS server hostname |
| `<name>`.port |    |   No     | UPS server port, 3493 for example |
| `<name>`.login |   |   No     | UPS server login |
| `<name>`.pass |    |   No     | UPS server password |


where `<name>` is a name you choose to identify a specific UPS device that is managed by NUT servers, `ups1` for instance.  You can configure any number of UPS devices managed by NUT servers. Every UPS is identified by instance name ("ups1" in the example above). You use instance name in the item definitions.

### Item Configuration

he syntax for the binding configuration string is explained here:

```
networkupstools="<name>:<property name>"
```

Here are some examples of valid binding configuration strings:

```
networkupstools="ups1:input.voltage"
networkupstools="ups1:ups.load"
networkupstools="ups1:ups.status"
```

As a result, your lines in the items file might look like the following:

```
Number Ups_Output_Voltage "UPS output voltage [%.1f V]" (Ups) {networkupstools="ups1:output.voltage"}
String Ups_Status "UPS status [%s]" (Ups) {networkupstools="ups1:ups.status"}
```

Supported item types are Number and String.

Supported property names differs between UPSes. You can use upsc command to get a list of properties for your ups:

```
$ upsc ups1
battery.charge: 100
battery.voltage: 13.40
battery.voltage.high: 13.00
battery.voltage.low: 10.40
battery.voltage.nominal: 12.0
device.mfr:
device.model: 750VA
device.type: ups
driver.name: blazer_usb
driver.parameter.pollinterval: 2
driver.parameter.port: auto
driver.version: 2.7.1
driver.version.internal: 0.10
input.current.nominal: 3.0
input.frequency: 50.0
input.frequency.nominal: 50
input.voltage: 240.3
input.voltage.fault: 168.4
input.voltage.nominal: 230
output.voltage: 238.0
ups.beeper.status: enabled
ups.delay.shutdown: 30
ups.delay.start: 180
ups.firmware:
ups.load: 8
ups.mfr:
ups.model: 750VA
ups.productid: 5161
ups.status: OL
ups.type: offline / line interactive
ups.vendorid: 0665
```
