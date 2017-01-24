
## Introduction

The primary goal of the [Network UPS Tools](http://www.networkupstools.org/) (NUT) project is to provide support for Power Devices, such as Uninterruptible Power Supplies (UPS), Power Distribution Units and Solar Controllers.
NUT provides many control and monitoring features, with a uniform control and management interface.
More than 100 different manufacturers, and several thousands models are compatible.

This binding let's you integrate NUT servers with openHAB.

For installation of the binding, please see Wiki page [[Bindings]].

## Configuration
### openhab.cfg
```
############################### NetworkUpsTools Binding ###############################
#
# networkupstools:<instance name>.<parameter>=<value>
#

# Refresh interval for state updates in milliseconds (optional)
networkupstools:refresh=60000

# UPS device name 
networkupstools:ups1.device=ups

# UPS server hostname (optional)
networkupstools:ups1.host=localhost

# UPS server port (optional)
networkupstools:ups1.port=3493

# UPS server login (optional)
#networkupstools:ups1.login=

# UPS server pass (optional)
#networkupstools:ups1.pass= 
```

In the openhab.cfg you can configure any number of UPS devices managed by NUT servers. Every UPS is identified by instance name ("ups1" in the example above). You use instance name in the item definitions.

### Generic Item Binding Configuration

In order to bind an item to a NetworkUpsTools property, you need to provide configuration settings. The syntax for the binding configuration string is explained here:

    networkupstools="<instance name>:<property name>"

Here are some examples of valid binding configuration strings:

    networkupstools="ups1:input.voltage"
    networkupstools="ups1:ups.load"
    networkupstools="ups1:ups.status"


As a result, your lines in the items file might look like the following:

    Number Ups_Output_Voltage "UPS output voltage [%.1f V]" (Ups) {networkupstools="ups1:output.voltage"}
    String Ups_Status "UPS status [%s]" (Ups) {networkupstools="ups1:ups.status"}

Supported item types are Number and String.

Supported property names differs between UPSes. You can use upsc command to get a list of properties for your ups:
```
jarek@nas ~ $ upsc ups1
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