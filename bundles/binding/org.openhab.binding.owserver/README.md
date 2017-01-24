## Introduction

Binding reads values from 1-wire devices connected to an [OW-SERVER](http://www.embeddeddatasystems.com/OW-SERVER-1-Wire-to-Ethernet-Server-Revision-2_p_152.html) (both Rev. 1 and 2).

It does not write values to these devices.
See [this wiki page](https://github.com/openhab/openhab/wiki/Samples-Binding-Config#how-to-turn-onoff-a-switch-from-ow-server-via-http-binding) for how to send commands to a 1-wire device via OW-SERVER.

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

First of all you need to introduce your OW-SERVER in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    ################################# EDS OWSever Binding ######################################
    
    # Host of the first OWServer device to control.
    # owserver:<serverId1>.host=
    # User name and password for web interface (optional)
    # owserver:<serverId1>.user=
    # owserver:<serverId1>.password=

    # Host of the second OWServer device to control.
    # owserver:<serverId2>.host=
    # User name and password for web interface (optional)
    # owserver:<serverId2>.user=
    # owserver:<serverId2>.password=

The `owserver:<serverId1>.host` value is the ip address of the OW-SERVER. 

The `owserver:<serverId1>.user` value is the user name to access the web interface.
The `owserver:<serverId1>.password` value is the password to access the web interface.

User and password values are optional parameters and only available in binding version 1.6+. You need this in case you enabled "Require name / password for all pages" in the web interface under `System Configuration | Network`.

Examples, how to configure your device:

    owserver:ow1.host=192.168.1.23
    owserver:ow1.user=admin
    owserver:ow1.password=eds

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    owserver="<<serverId1>:<ROMId>:<value-name>:<refreshInterval>"

The `<` in front of `<serverId1>` tells the binding to _read_ the following value.

The `serverId1` corresponds to the device which is introduced in openhab.cfg. This value must match the value in openhab.cfg and must consist only of letters, numbers and periods.

The `ROMId` corresponds to the ROM-ID of the OneWire-device you want to query.

The `value-name` corresponds to the value you want to query.

The `refreshInterval` is the interval in milliseconds to refresh the data.

You can find the `ROMid` and the `value-name` in the `details.xml` from the web interface.

Like: http://192.168.1.23/details.xml

    [..]
    <owd_EDS0068 Description="Temperature, Humidity, Barometric Pressure and Light Sensor">
      <Name>EDS0068</Name>
      <Family>7E</Family>
      <ROMId>C200100000XXXXXX</ROMId>
      [..]
      <Temperature Units="Centigrade">31.8750</Temperature>
      <Humidity Units="PercentRelativeHumidity">37.6875</Humidity>
    [..]

Examples, how to configure your items:

    Number bath_temp "Temperature [%.1f °C]"  { owserver="<ow1:C200100000XXXXXX:Temperature:60000" }
    Number bath_humidity "Humidity [%.1f %%]" { owserver="<ow1:C200100000XXXXXX:Humidity:60000" }

## Limitations

- It is only possible to read values
- The binding does only read values from the `details.xml`.
That means it is limited to 23 1-wire devices per OW-SERVER.