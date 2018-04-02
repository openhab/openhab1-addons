# Novelan/Luxtronic Heat Pump Binding

This binding was originally created for the Novelan heat pump. Since the Novelan control unit is based on the Luxtronic 2 contol unit of Alpha Innotec, this binding should work with all heat pumps that use this type of controller.

Note: The whole functionality was reverse engineered via tcpdump, so use it at your own risk. 

These parameters can be changed:

* Heating operation mode
* Warm water operation mode
* Cooling operation mode
* Offset of the heating curve
* Target temperature for warm water

## Prerequisites

The heat pump bundle connects to your heat pump via network. Make sure your heat pump is connected to your network and the network settings are configured. To access the network settings go in the heat pump Service menu -> system control -> IP address.

## Binding Configuration

This binding can be configured in the file `services/novelanheatpump.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| ip       |         |   Yes    | IP address of the heat pump to connect to |
| refresh  | 60000   |   No     | refresh inverval in milliseconds |
| port     | 8888    |   No     | port number of the heat pump to connect to Please be aware that from firmware version 1.73, Alpha Innotec has changed the port to 8889. |

## Item Configuration

The item configuration format is simple and looks like this:

```
novelanheatpump="<eventType>"
```

where `<eventType>` is one of the following values:

| `<eventType>` | Item Type | Meaning |
|---------------|-----------|---------|
| `temperature_outside` | Number | the measured temperature by the outside sensor |
| `temperature_outside_avg` | Number | the average measured temperature by the outside sensor |
| `temperature_return` | Number | the temperature returned by floor heating |
| `temperature_reference_return` | Number | the reference temperature of the heating water |
| `temperature_supplay` | Number | the temperature sent to the floor heating |
| `temperature_servicewater_reference` | Number | the reference temperature of the servicewater |
| `temperature_servicewater` | Number | the temperature of the servicewater |
| `state` | String | contains the time of the state and the state; Possible states are error, running, stopped, defrosting |
| `simple_state` | String | contains only the short statename; Possible states are error, running, stopped, defrosting |
| `extended_state` | String | contains the time of the state and the state; Possible states are error, heating, standby, switch-on delay, switching cycle | blocked, provider lock time, service water, screed heat up, defrosting, pump flow, desinfection, cooling, pool water, heating ext., service water ext., | flow monitoring, ZWE operation |
| `temperature_solar_collector` | Number | the temperature of the sensor in the solar collector |
| `temperature_hot_gas` | Number | 
| `temperature_probe_in` | Number | temperature flowing to probe head |
| `temperature_probe_out` | Number | temperature coming  from probe head |
| `temperature_mk1` | Number | |
| `temperature_mk1_reference` | Number | | 
| `temperature_mk2` | Number | |
| `temperature_mk2_reference` | Number | |
| `temperature_external_source` | Number | 
| `hours_compressor1` | String | operating hours of compressor one |
| `starts_compressor1` | Number | total starts of compressor one |
| `hours_compressor2` | String | operating hours of compressor two |
| `starts_compressor2` | Number | total starts of compressor two |
| `temperature_out_external` | Number | |
| `hours_zwe1` | String | |
| `hours_zwe2` | String | |
| `hours_zwe3` | String | |
| `hours_heatpump` | String | operating hours of heatpump |
| `hours_heating` | String | operating hours of heating |
| `hours_warmwater` | String | operating hours creating warm water |
| `hours_cooling` | String | operating hours of cooling |
| `thermalenergy_heating` | Number | total energy for heating in KWh |
| `thermalenergy_warmwater` | Number | total energy for creating warm water in KWh |
| `thermalenergy_pool` | Number | total energy for heating pool in KWh |
| `thermalenergy_total` | Number | sum of all total energy in KWh |
| `massflow` | Number | |
| `temperature_solar_storage` | Number | the temperature of the solar storage |
| `heating_operation_mode` | Number | operation mode (0="Auto", 1="Zuheizer", 2="Party", 3="Ferien", 4="Aus") |
| `heating_temperature` | Number | heating curve offset |
| `warmwater_operation_mode` | Number | (0="Auto", 1="Zuheizer", 2="Party", 3="Ferien", 4="Aus") |
| `warmwater_temperature` | Number | target temperature for warm water |
| `cooling_operation_mode` | Number | (1="Auto", 0="Off") |
| `cooling_release_temperature` | Number | cooling release temeprature |
| `cooling_inlet_temperature` | Number | cooling inlet temeprature |
| `cooling_start_hours` | Number | cooling start after hours |
| `cooling_stop_hours` | Number | cooling stop after hours |


## Examples

### Items

```
Number HeatPump_Temperature_1   "Wärmepumpe Außentemperatur [%.1f °C]"   <temperature> (gHeatpump) { novelanheatpump="temperature_outside" }
Number HeatPump_Temperature_2   "Rücklauf [%.1f °C]"  <temperature> (gHeatpump) { novelanheatpump="temperature_return" }
Number HeatPump_Temperature_3   "Rücklauf Soll [%.1f °C]" <temperature> (gHeatpump) { novelanheatpump="temperature_reference_return" }
Number HeatPump_Temperature_4   "Vorlauf [%.1f °C]"    <temperature> (gHeatpump) { novelanheatpump="temperature_supplay" }
Number HeatPump_Temperature_5   "Brauchwasser Soll [%.1f °C]"  <temperature> (gHeatpump) { novelanheatpump="temperature_servicewater_reference" }
Number HeatPump_Temperature_6   "Brauchwasser Ist [%.1f °C]"   <temperature> (gHeatpump) { novelanheatpump="temperature_servicewater" }
Number HeatPump_Temperature_7   "Solarkollektor [%.1f °C]" <temperature> (gHeatpump) { novelanheatpump="temperature_solar_collector" }
Number HeatPump_Temperature_8   "Solarspeicher [%.1f °C]"  <temperature> (gHeatpump) { novelanheatpump="temperature_solar_storage" }
String HeatPump_State   "Status [%s]"   <temperature> (gHeatpump) { novelanheatpump="state" }

Number HeatPump_Retrun_External     "Rücklauf Extern [%.1f °C]"   <temperature> (gHeatpump) { novelanheatpump="temperature_out_external" } // return external
Number HeatPump_Hot_Gas     "Temperatur Heissgas [%.1f °C]"    <temperature> (gHeatpump) { novelanheatpump="temperature_hot_gas" } // return hot gas
Number HeatPump_Outside_Avg     "mittlere Aussentemperatur [%.1f °C]"  <temperature> (gHeatpump) { novelanheatpump="temperature_outside_avg" } 
Number HeatPump_Probe_in    "Sondentemperatur Eingang [%.1f °C]"   <temperature> (gHeatpump) { novelanheatpump="temperature_probe_in" } 
Number HeatPump_Probe_out   "Sondentemperatur Ausgang [%.1f °C]"   <temperature> (gHeatpump) { novelanheatpump="temperature_probe_out" } 
Number HeatPump_Mk1     "Vorlauftemperatur MK1 IST [%.1f °C]"  <temperature> (gHeatpump) { novelanheatpump="temperature_mk1" } 
Number HeatPump_Mk1_Reference   "Vorlauftemperatur MK1 SOLL [%.1f °C]" <temperature> (gHeatpump) { novelanheatpump="temperature_mk1_reference" } 
Number HeatPump_Mk2     "Vorlauftemperatur MK2 IST [%.1f °C]"  <temperature> (gHeatpump) { novelanheatpump="temperature_mk2" } 
Number HeatPump_Mk2_Reference   "Vorlauftemperatur MK2 SOLL [%.1f °C]" <temperature> (gHeatpump) { novelanheatpump="temperature_mk2_reference" } 
Number HeatPump_External_Source     "Temperatur externe Energiequelle [%.1f °C]"   <temperature> (gHeatpump) { novelanheatpump="temperature_external_source" } 
String HeatPump_Hours_Compressor1   "Betriebsstunden Verdichter1 [%s]"  <clock> (gHeatpump) { novelanheatpump="hours_compressor1" } 
Number HeatPump_Starts_Compressor1  "Verdichter 1 [%.1f]"   <clock> (gHeatpump) { novelanheatpump="starts_compressor1" } 
String HeatPump_Hours_Compressor2   "Betriebsstunden Verdichter2 [%s]"  <clock> (gHeatpump) { novelanheatpump="hours_compressor2" } 
Number HeatPump_Starts_Compressor2  "Verdichter 2 [%.1f]"   <clock> (gHeatpump) { novelanheatpump="starts_compressor2" } 
String HeatPump_Hours_Zwe1  "Betriebsstunden ZWE1 [%s]" <clock> (gHeatpump) { novelanheatpump="hours_zwe1" }
String HeatPump_Hours_Zwe2  "Betriebsstunden ZWE2 [%s]" <clock> (gHeatpump) { novelanheatpump="hours_zwe2" }
String HeatPump_Hours_Zwe3  "Betriebsstunden ZWE3 [%s]" <clock> (gHeatpump) { novelanheatpump="hours_zwe3" }
String HeatPump_Hours_Heatpump  "Betriebsstunden [%s]"  <clock> (gHeatpump) { novelanheatpump="hours_heatpump" } 
String HeatPump_Hours_Heating   "Betriebsstunden Heizung [%s]"  <clock> (gHeatpump) { novelanheatpump="hours_heating" }
String HeatPump_Hours_Warmwater "Betriebsstunden Brauchwasser [%s]" <clock> (gHeatpump) { novelanheatpump="hours_warmwater" }
String HeatPump_Hours_Cooling   "Betriebsstunden Kuehlung [%s]" <clock> (gHeatpump) { novelanheatpump="hours_cooling" }
Number HeatPump_Thermalenergy_Heating   "Waermemenge Heizung [%.1f KWh]"    <energy> (gHeatpump) { novelanheatpump="thermalenergy_heating" }
Number HeatPump_Thermalenergy_Warmwater     "Waermemenge Brauchwasser [%.1f KWh]"   <energy> (gHeatpump) { novelanheatpump="thermalenergy_warmwater" }
Number HeatPump_Thermalenergy_Pool  "Waermemenge Schwimmbad [%.1f KWh]" <energy> (gHeatpump) { novelanheatpump="thermalenergy_pool" }
Number HeatPump_Thermalenergy_Total     "Waermemenge gesamt seit Reset [%.1f KWh]"  <energy> (gHeatpump) { novelanheatpump="thermalenergy_total" }
Number HeatPump_Massflow    "Massentrom [%.1f L/h]" <energy> (gHeatpump) { novelanheatpump="massflow" }
String HeatPump_State_Ext   "Status [%s]"   <temperature> (gHeatpump) { novelanheatpump="extended_state" }

Number HeatPump_heating_operation_mode   "Heizung Betriebsart [%.0f]"  (gHeatpump) { novelanheatpump="heating_operation_mode" }
Number HeatPump_heating_temperature   "Heizung Temperatur [%.1f]"  (gHeatpump) { novelanheatpump="heating_temperature" }
Number HeatPump_warmwater_operation_mode   "Warmwasser Betriebsart [%.0f]"  (gHeatpump) { novelanheatpump="warmwater_operation_mode" }
Number HeatPump_warmwater_temperature   "Warmwasser Temperatur [%.1f]"  (gHeatpump) { novelanheatpump="warmwater_temperature" }
Number HeatPump_Cool_BA "Betriebsart" (gHeatpump) { novelanheatpump="cooling_operation_mode" }
Number HeatPump_Cooling_Release "Freigabe [%.1f °C]" (gHeatpump) { novelanheatpump="cooling_release_temperature" }
Number HeatPump_Cooling_Inlet "Vorlauf Soll [%.1f °C]" (gHeatpump) { novelanheatpump="cooling_inlet_temperature" }
Number HeatPump_Cooling_Start "AT Überschreitung[%.1f hrs]" (gHeatpump) { novelanheatpump="cooling_start_hours" }
Number HeatPump_Cooling_Stop "AT Unterschreitung[%.1f hrs]" (gHeatpump) { novelanheatpump="cooling_stop_hours" }
```

### Sitemap (fragment)

```
Switch item=HeatPump_heating_operation_mode  mappings=[0="Auto", 1="Zuheizer", 2="Party", 3="Ferien", 4="Aus"]
Setpoint item=HeatPump_heating_temperature minValue=-10 maxValue=10 step=0.5
Switch item=HeatPump_warmwater_operation_mode  mappings=[0="Auto", 1="Zuheizer", 2="Party", 3="Ferien", 4="Aus"]
Setpoint item=HeatPump_warmwater_temperature minValue=10 maxValue=65 step=1
```
