# Stiebel Eltron LWZ Binding

This binding is used to communicate Stiebel Eltron LWZ heat pumps via a serial interface.  The binding is inspired by the work of [Monitoring a Stiebel Eltron LWZ](http://robert.penz.name/heat-pump-lwz) which is hosted at [Heatpumpmonitor](https://launchpad.net/heatpumpmonitor) and is written in Python.

The following functionality has been implemented:

* reading all settings, status, time, and sensor data from the heat pump
* protocol parse for different versions of LWZ is configurable via xml file.  Other versions like 2.16, 4.09, 4.19, 4.38 should be easy to extend as parser configuration can be derived from [protocol versions](http://bazaar.launchpad.net/~robert-penz-name/heatpumpmonitor/trunk/files/head:/protocolVersions/).
* changing settings of number setting parameter in the heat pump (still in beta)
* updating the time in the heat pump (still in beta)

The binding has been successfully tested with these hardware configurations:

* [Stiebel Eltron LWZ 303](https://www.stiebel-eltron.de/content/dam/ste/de/de/products/downloads/erneuerbare_energien/lueftung/Bedienungs-_u._Installationsanleitungen__LWZ_303-403__DM0000017729-ome.pdf) connected with [Mate-N-Lok RS232](http://robert.penz.name/heat-pump-lwz/)

## Binding Configuration

This binding can be configured in the file `services/stiebelheatpump.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| serialPort |       |    Yes   | serial port to use for connecting to the metering device; e.g., `COM1` for Windows, `/dev/ttyS0` or `/dev/ttyUSB0` for Linux |
| baudRate | 9600    |    No    | baud rate of the serial interface |
| serialTimeout | 5  |    No    | timeout on serial interface, in seconds, when attempting to establish connection |
| refresh  | 60000   |    No    | perform a module status query every `refresh` miliseconds |
| version  |         |    ?     | version string of the heatpump. It it used when parsing the heat pump protocol. Example: `2.06`. |

## Item Configuration

The syntax of an item configuration is shown in the following line in general:

```
stiebelheatpump="<request definition item name from version xml>"
```

These request definitions are defined in a version-specific protocol definition file.  Here a section from the 2.06 version to read the current measurement values in the heatpump:

```xml
<request requestByte="FB" description="Reads measurements" name="CurrentValues">
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
</request>
```

## Example

items/stiebelheatpumpdemo.items

```
Number CylinderTemperature "Boilertemperatur [%.1f °C]" { stiebelheatpump="CylinderTemperature" }
```

The "CylinderTemperature" is defined in the 2.06.xml protocol parser configuration in the resources of the binding.
