Documentation of the Stiebel Eltron Heatpump binding bundle

## Introduction

This binding is used to communicate Stiebel Eltron LWZ heat pumps via a serial interface.
The binding is inspired by the work of [Monitoring a Stiebel Eltron LWZ] (http://robert.penz.name/heat-pump-lwz) which is hosted here [Heatpumpmonitor] (https://launchpad.net/heatpumpmonitor) written in python.

The following functionality has been implemented:
* reading all Settings, status, time , sensor data from the heat pump
* protocol parse for different versions of LWZ is configurable via xml file.
  Other versions like 2.16, 4.09, 4.19, 4.38 should be easy to extend as parser configuration can be derived from [protocol versions] (http://bazaar.launchpad.net/~robert-penz-name/heatpumpmonitor/trunk/files/head:/protocolVersions/)
* changing Settings of number setting parameter in the heat pump (still in beta)
* updating the time in the heat pump (still in beta)

For installation of the binding, please see Wiki page [Bindings](https://github.com/openhab/openhab/wiki/Bindings).

## Stiebel Eltron Heatpump LWZ - Binding Configuration
### openhab.cfg
The openhab.cfg file (in the folder '${openhab_home}/configurations') contain following config parameter to configure the binding.

    ################################ stiebelheatpump Binding######################################
    #
    # the serial port to use for connecting to the metering device e.g. COM1 for Windows and /dev/ttyS0 or
    # /dev/ttyUSB0 for Linux
    stiebelheatpump:serialPort=/dev/ttyS4

    # Baudrate of the serial interface. Default is 9600.
    # default is 9600
    stiebelheatpump:baudRate=9600

    # timeout on serial interface when establish connection
    # default is 5
    stiebelheatpump:serialTimeout=5

    # Perform a module status query every x miliseconds (optional, defaults to 60000 (10 minutes)). 
    stiebelheatpump:refresh=60000

    # version string of the heatpump
    stiebelheatpump:version=2.06

## Generic Item Binding Configuration

### Item Binding Configuration

In order to bind an item to the data received from the meter device , you need to provide item configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:
The syntax of an item configuration is shown in the following line in general:

    stiebelheatpump="<request definition item name from version xml>"

These request definition are defined in a version specific protocol definition file.
Here a section from the 2.06 version to read the current measurements values in the heatpump:

    `<request requestByte="FB" description="Reads measurements" name="CurrentValues">
      <recordDefinition name="CollectorTemperatur" dataType="Sensor" scale="0.1" length="2" position="4" unit="°C"/>
      <recordDefinition name="OutsideTemperature" dataType="Sensor" scale="0.1" length="2" position="6" unit="°C"/>
      <recordDefinition name="FlowTemperature" dataType="Sensor" scale="0.1" length="2" position="8" unit="°C"/>
      <recordDefinition name="ReturnTemperature" dataType="Sensor" scale="0.1" length="2" position="10" unit="°C"/>
      <recordDefinition name="HotGasTemperature" dataType="Sensor" scale="0.1" length="2" position="12" unit="°C"/>
      <recordDefinition name="CylinderTemperature" dataType="Sensor" scale="0.1" length="2" position="14" unit="°C"/>
      <recordDefinition name="EvaporatorTemperature" dataType="Sensor" scale="0.1" length="2" position="20" unit="°C"/>
      <recordDefinition name="CondenserTemperature" dataType="Sensor" scale="0.1" length="2" position="22" unit="°C"/>
      <recordDefinition name="ExtractFanSpeed" dataType="Sensor" scale="1.0" length="1" position="30" unit="°C"/>
      <recordDefinition name="SupplyFanSpeed" dataType="Sensor" scale="1.0" length="1" position="31" unit="°C"/>
      <recordDefinition name="ExhaustFanSpeed" dataType="Sensor" scale="1.0" length="1" position="32" unit="°C"/>
      <recordDefinition name="FilteredOutsideTemperature" dataType="Sensor" scale="0.1" length="2" position="34" unit="°C"/>
     </request>`

To add items in your site find here an example:

    Number CylinderTemperature "Boilertemperatur [%.1f °C]" { stiebelheatpump="CylinderTemperature" }

The "CylinderTemperature" is defined in the 2.06.xml protocol parser configuration in the resources of the binding.
The version file which shall be considered to parse the heat pump protocol is defined in stiebelheatpump:version configuration parameter in your openhab.cfg.

### Tested Hardware

The binding has been successfully tested with below hardware configuration:
* [Stiebel Eltron LWZ 303](https://www.stiebel-eltron.de/content/dam/ste/de/de/products/downloads/erneuerbare_energien/lueftung/Bedienungs-_u._Installationsanleitungen__LWZ_303-403__DM0000017729-ome.pdf) connected with [Mate-N-Lok RS232](http://robert.penz.name/heat-pump-lwz/)

