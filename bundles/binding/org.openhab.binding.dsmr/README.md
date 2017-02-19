# DSMR Binding

The openHAB DSMR binding is targeted for Dutch users having a smart meter ('Slimme meter' in Dutch).  Data of Dutch smart meters can be obtained via the P1-port. When connecting this port from a serial port the data can be read out.

This binding reads the P1-port of the Dutch Smart Meters that comply to NTA8130, DSMR v2.1, DSMR v2.2, DSMR v3.0, DSMR v4.0 or DSMR v4.04.

Users not living in the Netherlands that want to read a meter should review the [IEC-62056-21 Meter Binding](https://github.com/openhab/openhab/wiki/IEC-62056---21-Meter-Binding).

## Binding Configuration

The binding can be configured in the file `services/dsmr.cfg`.

| Property                 | Default | Required | Description                               |
|--------------------------|---------|:--------:|-------------------------------------------|
| port                     |         | Yes      | Port of the DSMR                          |
| gas.channel              | 1       | No       | Channel for the gas meter                 |
| water.channel            | 2       | No       | Channel for the water meter               |
| heating.channel          | 3       | No       | Channel for the heating meter             |
| cooling.channel          | 4       | No       | Channel for the cooling meter             |
| generic.channel          | 5       | No       | Channel for the generic meter             |
| slaveelectricity.channel | 6       | No       | Channel for the slave electricity meter   |


The channels of the additional meters correspond to the M-Bus channel of the
smart meter that the additional meter is connected to. The main electricity
meter is always at channel 0; configuration of this meter is not necessary
and not supported.


### Differences between DSMR versions

The serial port settings for DSMR v4 and up (115200 8n1) differ from NTA8130,
v2.1, v2.2 and v3.0 (9600 7e1). The DSMR binding will automatically detect the
applicable serial port settings.


## Item Configuration

The syntax for the DSMR binding configuration string is:

```
dsmr="[dsmr item id]"
```

The following table shows the full list of available dsmr values:

| DSMR item id       | Description | Available for DSMR version | Unit |
|--------------------|-------------|----------------------------|------|
| **General values** |             |                            |      |
| P1VersionOutput    | Version information for P1 output | v4.0 and up | |
| P1Timestamp        | Timestamp of the P1 output |v4.0 and up||
| **Electricity meter values**|    |                            |      |
| eEquipmentId       | Equipment identifier|All versions|
| eDeliveryTariff0   | Total meter delivery tariff 0|<sup>1</sup>|kWh
| eDeliveryTariff1   | Total meter delivery tariff 1|All versions|kWh
| eDeliveryTariff2   | Total meter delivery tariff 2|All versions|kWh
| eProductionTariff0 | Total meter production tariff 0|<sup>1</sup>|kWh|
| eProductionTariff1 | Total meter production tariff 1|All versions|kWh|
| eProductionTariff2 | Total meter production tariff 2|All versions|kWh|
| eTariffIndicator   | Tariff indicator|All versions|
| eActualDelivery    | Actual power delivery|All versions|kW
| eActualProduction  | Actual power production|All versions|kW
| eTreshold          | The actual threshold Electricity|All versions|A for v2.1, v2.2, v3.0<br>kW for v4.0 and v4.04
| eSwitchPosition    | Actual switch position|All versions|
| ePowerFailures     | Number of power failures|v4.0 and up|
| eLongPowerFailures | Number of long power failures|v4.0 and up|
| eNumberOfLogEntries| Number of power failure entries in the event log|v4.0 and up|
| eDatePowerFailureX | Date of power failure (entry *X*)|v4.0 and up<br>*X* is a value [1-10]|
| eDurationPowerFailureX |Duration of power failure (entry *X*)|v4.0 and up<br>*X* is a value [1-10]|seconds
| eVoltageSagsL1     | Number of voltage sags L1|v4.0 and up|
| eVoltageSagsL2     | Number of voltage sags L2|v4.0 and up|
| eVoltageSagsL3     | Number of voltage sags L3|v4.0 and up|
| eVoltageSwellsL1   | Number of voltage swells L1|v4.0 and up|
| eVoltageSwellsL2   | Number of voltage swells L2|v4.0 and up|
| eVoltageSwellsL3   | Number of voltage swells L3|v4.0 and up|
| eTextCode          | Version information for P1 output|All versions|
| eTextMessage       | Version information for P1 output|All versions|
| eInstantCurrentL1  | Instantenous current L1|v4.04 and up|A
| eInstantCurrentL2  | Instantenous current L2|v4.04 and up|A
| eInstantCurrentL3  | Instantenous current L3|v4.04 and up|A
| eInstantPowerDeliveryL1 |Instantenous active power delivery L1|v4.04 and up|kW
| eInstantPowerDeliveryL2 |Instantenous active power delivery L2|v4.04 and up|kW
| eInstantPowerDeliveryL3 |Instantenous active power delivery L3|v4.04 and up|kW
| eInstantPowerProductionL1 |Instantenous active power production L1|v4.04 and up|kW
| eInstantPowerProductionL2 |Instantenous active power production L2|v4.04 and up|kW
| eInstantPowerProductionL3 |Instantenous active power production L3|v4.04 and up|kW
| **Gas meter values**||||
| gDeviceType        | Device Type|v3.0 and up|
| gEquipmentId       | Equipment identifier|All versions|
| gValueTS           | Timestamp of the last measurement (local time)|v4.0 and up|Date & time
| gNumberOfValues    | Number of values available for gValue en gValue*X*|v3.0|
| gUnit              | Unit of the values|v3.0|
| gValue             | Delivery of:<br>-Last hour (v3.0 / v4.0 and up)<br>-Last 24 hours (v2.1 / v2.2)|All versions<sup>2</sup>|m3
| gValueX            | Meter reading *X* of the buffer (hourly period)|v3.0<sup>2</sup><br>*X* is a value [2-10]<br>Higher values are older|m3
| gProfileStatus     | Unknown|v3.0|
| gRecordingPeriod   | Duration of a value recording|v3.0|minutes
| gValueCompensated  | Temperature compensated delivery of the last 24 hours|v2.1 and v2.2|m3
| gValvePosition     | Valve position|All versions|
| **Heating meter values**||||
| hDeviceType        | Device Type|v3.0 and up|
| hEquipmentId       | Equipment identifier|All versions|
| hValueTS           | Timestamp of the last measurement (local time)|v4.0 and up|Date &amp; time
| hValue             | Last hour delivery|All versions|GJ
| hValvePosition     | Valve position|v3.0 and up|
| **Cooling meter values**||||
| cDeviceType        | Device Type|v3.0 and up|
| cEquipmentId       | Equipment identifier|All versions|
| cValueTS           | Timestamp of the last measurement (local time)|v4.0 and up|Date &amp; time
| cValue             | Last hour delivery|All versions|GJ
| cValvePosition     | Valve position|v3.0 and up|
| **Cooling meter values**||||
| wDeviceType        | Device Type|v3.0 and up|
| wEquipmentId       | Equipment identifier|All versions|
| wValueTS           | Timestamp of the last measurement (local time)|v4.0 and up|Date &amp; time
| wValue             | Last hour delivery|All versions|m3
| wValvePosition     | Valve position|v3.0 and up|
| **Generic meter values**||||
| genericDeviceType  | Device Type|v3.0|
| genericEquipmentId | Equipment identifier|v3.0|
| genericValue       | Last hour delivery|v3.0|
| genericValvePosition |Valve/Switch position|v3.0|
| **Slave electricity meter values**||||
| seDeviceType       | Device Type|v4.0 and up|
| seEquipmentId      | Equipment identifier|v4.0 and up|
| seValueTS          | Timestamp of the last measurement (local time)|v4.0 and up|Date &amp; time
| seValue            | Last hour delivery|v4.0 and up|kWh
| seSwitchPosition   | Switch position|v4.0 and up

<sup>1</sup> This item isn't part of any specification, but the ITRON ACE4000 GTMM Mk3 does use this value.<br/>
<sup>2</sup> Gas values for DSMR v3.0 are available in a list of max. 10 entries. The binding assumes the first value is the most recent (and thus available in `gValue`)

## Examples

### Item configuration examples

```
Number P1_Actual_Delivery "Actual usage [%.3f kW]" {dsmr="eActualDelivery"}
Number P1_Meter_DeliveryLow "Meterstand reading low tariff [%.3f kWh]" {dsmr="eDeliveryTariff1"}
Number P1_Meter_DeliveryNormal "Meter reading normal tariff[%.3f kWh]" {dsmr="eDeliveryTariff2"}
```


## Notes

### Logging

To increase verbosity of the logging for debugging purposes, set the
`org.openhab.binding.dsmr` log level to DEBUG. Reduce back to INFO or DEFAULT
after capturing the desired logs.


### Test results

Meter | DSMR version | Electricity | Gas | Water | Heating | Cooling | General | Slave electricity
----- |:------------:|:-----------:|:---:|:-----:|:-------:|:-------:|:-------:|:----------------:
ISKRA MT382|3.0|OK|OK|Not tested|Not tested|Not tested|Not tested|N/A
ISKRA AM550|5.0|OK|OK|Not tested|Not tested|Not tested|Not tested|Unknown
Itron ACE4000 GTMM Mk3|NTA8130|OK|Not tested|Not tested|Not tested|Not tested|N/A|N/A
Kaifa E0003|4.04|OK|OK|Not tested|Not tested|Not tested|N/A|Not tested
Kaifa MA304|4.04|OK|Not tested|Not tested|Not tested|Not tested|N/A|Not tested
Kamstrup 162JxC|3.0|OK|OK|Not tested|Not tested|Not tested|Not tested|N/A
Landys + Gyr E350|3.0|OK|Not tested|Not tested|Not tested|Not tested|Not tested|N/A

### CRC

A meter that conforms to DSMR v4 or higher includes a CRC on the complete message. The binding does not check the CRC yet.
