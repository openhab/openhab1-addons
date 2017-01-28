# panStamp Binding

panStamp are low-power wireless modules programmable from Arduino.  The panStamp binding enables openHAB to connect to a network of panStamp devices. 

## Prerequisites

The binding requires you to have a panStamp running the 'modem' sketch connected to a serial port. 

It is imperative that you understand the panStamp environment before trying to use this binding. If you do understand the panStamp ecosystem, this documentation should provide a straightforward guide to getting your panStamps to talk to openHAB. 

## Binding Configuration 

This binding can be configured in the file `services/panstamp.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| serial.port |      |   Yes    | serial port that the binding will use to communicate to the modem and the panStamp network. Typically this will be something like `/dev/ttyUSB0` on Linux or `COM3` on Windows. It is recommended that, before trying to connect the openHAB panStamp binding to the modem, you test the modem using a terminal program such as minicom. |
| serial.speed | 38400 |  No    | Serial port speed. Typically 38400, but can be adjusted to 9600 and 19200. |
| network.channel | 0 |   No    | frequency/channel on which the SWAP network operates |
| network.id | 0xb547 |   No    | network ID used by the SWAP network |
| network.deviceAddress | 1 | No | SWAP sender address used by the modem device |
| directory.xml | etc/panstamp/xml | No | directory where panStamp XML device definitions can be found. If you only use standard sketches, this directory is not required |
| debug.port |       |    No    | TCP port used to debug the panStamp network. This port is used to connect the panStamp-tools GUI client for debugging |

Example:

```
# The serial port to which the panStamp modem is connected
panstamp:serial.port=/dev/ttyUSB0
panstamp:serial.speed=38400
# SWAP Network settings
panstamp:network.channel=0
panstamp:network.id=0xb547
panstamp:network.deviceAddress=1
# PanStamp device files
panstamp:directory.xml=etc/panstamp/xml
# TCP debugger settings
panstamp:debug.port=3000
```

# Item Configuration

Items are configured using a slightly verbose but very readable syntax:

```
panstamp="address=<addr>,productCode=<man/prod>,register=<reg>,endpoint='<end>',[unit=<unit>]"
```

All the item parameters are explained below:

Parameter|Description|Required
---------|-----------|--------
address|The panStamp device address. This is the address of the specific panStamp device.|yes
productCode|The product code, consisting of manufacturer ID and product ID separated by a '/'.|yes
register|The register ID of the panStamp register (on the device) to which the endpoint is mapped.|yes
endpoint|The name of the endpoint addressed by the item.|yes
unit|The conversion unit for the endpoint. This could for example be C or F for temperatures.|no

The productCode, register and endpoint parameters map directly to the panStamp device definitions. 

panStamp item configurations are simple in large part because the panStamp system provides excellent meta data which is used by the binding to determine what a device is, what its endpoints do, and how to convert data to and from it.

## Examples

This example defines an item connected to panStamp device 3, register 12, endpoint 'Temperature'. The product code for the device is 1/4 and the endpoint data is read as degrees Celsius.

```
Number Temperature_PS3      "Temperature [%.1f °C]" <temperature>   (Temperature, PanStamp) { 
    panstamp="address=3,productCode=1/4,register=12,endpoint='Temperature',unit=C" }
```

This example uses another endpoint on the same panStamp as above to read the devices' battery voltage:

```
Number Voltage_PS3      "Voltage [%.2f V]"  <energy>    (Temperature, PanStamp) { 
    panstamp="address=3,productCode=1/4,register=11,endpoint='Voltage'" }
```

In the above two examples, the temperature and voltage sensors are inputs. Values received from the network will be updated to the items. In the next example, we have a switch which toggles a relay driven by a panStamp:

```
Switch Button_PS4 "Porch Light"  { panstamp="address=4,productCode=1/7,register=11,endpoint='Binary 7'" }
```

## Network configuration and debugging 

The panStamp binding provides an optional feature to allow the user to configure or debug the panStamp network using a GUI tool while openHAB manages the network (and therefore owns the serial port). This option is enabled by providing the `debug.port` configuration parameter. Doing this has the upside of being able to change panStamp configurations without stopping openHAB or without using a separate panStick. 

## References

* [panStamp GUI tool](https://github.com/GideonLeGrange/panstamp-tools)
* [panstamp-java library](https://github.com/GideonLeGrange/panstamp-java)
* [panStamp commercial web site](http://panstamp.com)
* [panStamp community forum](http://panstamp.org)
