# WR3223 ventilation controller Binding

The WR3223 ventilation controller is used for room ventilation systems, for example in houses produced by company “Schwörer Haus”. The manufacturer of the controller is [Hermes Electronic](http://www.hermes-electronic.de/).

The WR3223 binding can read several measurements and the current state of the ventilation system. It can also adjust the fan speed, operation mode and the target temperature.

## Prerequisites

The WR3223 can be connected by an RS232 or USB adapter. The adapter can be orderd from Hermes Electronic. If you want to connect the WR3223 by yourself, have a look at: [https://community.openhab.org/t/wr3223-ventilation-controller-schworer-haus/](https://community.openhab.org/t/wr3223-ventilation-controller-schworer-haus/)

To control the WR3223, you have to disconnect the control panel, otherwise the WR3223 will ignore the command from the serial port.

## Binding Configuration

This binding can be configured in the file `services/wr3223.cfg`.

The connection must be configured via either the serialPort setting or via the host and port settings.

| Property       | Default | Required | Description |
|----------------|---------|:--------:|-------------|
| serialPort     |         |   Yes (if serial port is used)     | Serial port where the WR3223 is connected. E.g. /dev/ttyUSB0 |
| host           |         |   Yes (if TCP is used)     | IP address of the TCP to serial gateway |
| port           |         |   Yes (if TCP is used)     | port number of the TCP to serial gateway  |
| refresh        | 15000   |   No     | Refresh interval in milliseconds. The WR3223 needs a refresh at least every 20 seconds! |
| controllerAddr | 1       |   No     | The controller address is normally 1.

## Item Configuration

The syntax of the binding configuration strings accepted is the following:

```
wr3223="<device-command>"
```

where `<device-command>` is replaced with the WR3223 command from the list below:

| `<device-command>`                          | Item Type | Read / Write | Notes                                                    |
|---------------------------------------------|-----------|--------------|----------------------------------------------------------|
| temperature_evaporator                      | Number    | Read         | de: Verdampfertemperatur (T1)                            |
| temperature_condenser                       | Number    | Read         | de: Kondensatortemperatur (T2)                           |
| temperature_outside                         | Number    | Read         | de: Aussentemperatur (T3)                                |
| temperature_outgoing_air                    | Number    | Read         | de: Ablufttemperatur (Raumtemperatur) (T4)               |
| temperature_after_heat_exchanger            | Number    | Read         | de: Temperatur nach Wärmetauscher (Fortluft) (T5)        |
| temperature_supply_air                      | Number    | Read         | de: Zulufttemperatur (T6)                                |
| temperature_after_brine_preheating          | Number    | Read         | de: Temperatur nach Solevorerwärmung (T7)                |
| temperature_after_preheating                | Number    | Read         | de: Temperatur nach Wärmetauscher (T8)                   |
| rotation_speed_supply_air_motor             | Number    | Read         | de: Drehzahl Zuluftmotor                                 |
| rotation_speed_exhaust_air_motor            | Number    | Read         | de: Drehzahl Abluftmotor                                 |
| bypass                                      | Contact   | Read         | de: Zustand Bypass                                       |
| compressor                                  | Contact   | Read         | de: Zustand Kompressor Relais                            |
| additional_heater_relais                    | Contact   | Read         | de: Zustand Zusatzheizung Relais                         |
| additional_heater_status                    | Contact   | Read         | de: Zusatzheizung An/Aus                                 |
| additional_heater_open                      | Switch    | Read / Write | de: Zusatzheizung ausgeschaltet (0) oder freigegeben (1) |
| additional_heater_activate                  | Switch    | Read / Write | de: Zusatzheizung ein/ausschalten                        |
| bypass_relay                                | Contact   | Read         | de: Zustand Netzrelais Bypass                            |
| preheating_radiator_active                  | Contact   | Read         | de: Vorheizen aktiv                                      |
| control_device_active                       | Contact   | Read         | de: Bedienteil  aktiv                                    |
| earth_heat_exchanger                        | Contact   | Read         | de: Erdwärmetauscher                                     |
| magnet_valve                                | Contact   | Read         | de: Magnetventil                                         |
| openhab_interface_active                    | Contact   | Read         | de: Bedienung über RS Schnittstelle (WR3223 Binding)     |
| preheating_radiator                         | Contact   | Read         | de: Vorheizregister                                      |
| warm_water_post_heater                      | Contact   | Read         | de: WW Nachheizregister                                  |
| ventilation_level_available                 | Contact   | Read         | de: Luftstufe vorhanden                                  |
| ventilation_level                           | Number    | Read / Write | de: Aktuelle Luftstufe                                   |
| operation_mode                              | Number    | Read / Write | de: Betriebsart                                          |
| cooling_mode                                | Switch    | Read / Write | de: Kühlen                                               |
| temperature_supply_air_target               | Number    | Read / Write | de: Zuluftsoll Temperatur                                |
| heat_feedback_rate                          | Number    | Read         | de: Aktuelle Rückwärmzahl in %                           |
| speed_deviation_max_level_1                 | Number    | Read / Write | de: Max. Drehzahlabweichung Zu-/Abluft in Stufe 1        |
| speed_deviation_max_level_2                 | Number    | Read / Write | de: Max. Drehzahlabweichung Zu-/Abluft in Stufe 2        |
| speed_deviation_max_level_3                 | Number    | Read / Write | de: Max. Drehzahlabweichung Zu-/Abluft in Stufe 3        |
| speed_increase_earth_heat_exchanger_level_1 | Number    | Read / Write | de: Drehzahlerhöhung Zuluftventilator Stufe 1, wenn Erdwärmetauscher ein (0% bis 40%) |
| speed_increase_earth_heat_exchanger_level_2 | Number    | Read / Write | de: Drehzahlerhöhung Zuluftventilator Stufe 2, wenn Erdwärmetauscher ein (0% bis 40%) |
| speed_increase_earth_heat_exchanger_level_3 | Number    | Read / Write | de: Drehzahlerhöhung Zuluftventilator Stufe 3, wenn Erdwärmetauscher ein (0% bis 40%) |
| air_exchange_decrease_outside_temperature   | Number    | Read / Write | de: Luftwechsel um 3% reduziert ab Außentemp. ...°C (-20°C bis +10°C) |
| ventilation_speed_level_1                   | Number    | Read / Write | de: Luftstufe 1, % des max. Ventilatorstellwerts (40 bis 100%)        |
| ventilation_speed_level_2                   | Number    | Read / Write | de: Luftstufe 2, % des max. Ventilatorstellwerts (40 bis 100%)        |
| ventilation_speed_level_3                   | Number    | Read / Write | de: Luftstufe 3, % des max. Ventilatorstellwerts (40 bis 100%)        |
| summer_earth_heat_exchanger_activation_temperature | Number | Read / Write | de: Einschalt-Außentemperatur Erdwämietauscher im Sommer (20°C bis 40°C)   |
| winter_earth_heat_exchanger_activation_temperature | Number | Read / Write | de: Einschalt-Außentemperatur Erdwärmetauscher im Winter (-20°C bis 10°C)  |
| defrosting_start_temperature                | Number    | Read / Write | de: Beginn Abtauung ab Verdampfertemperatur ...°C        |
| defrosting_end_temperature                  | Number    | Read / Write | de: Ende Abtauung ab Verdampfertemperatur ...°C          |
| defrosting_ventilation_level                | Number    | Read / Write | de: Lüfterstufe im Abtaubetrieb                          |
| defrosting_hold_off_time                    | Number    | Read / Write | de: Sperrzeit für den nächsten Abtauvorgang              |
| defrosting_overtravel_time                  | Number    | Read / Write | de: Abtaunachlauzeit                                     |
| defrosting_heat_feedback_rate               | Number    | Read / Write | de: Abtaurückwärmezahl Schaltpunkt (20% bis 80 %)        |
| solar_max                                   | Number    | Read / Write | de: Solar max                                            |
| solar_usage                                 | Number    | Read         | de: Solar Nutzen (Stunden)                               |
| delta_t_off                                 | Number    | Read / Write | de: Delta T Aus Temperaturdifferenz zwischen Speicher u. Kollektor |
| delta_t_on                                  | Number    | Read / Write | de: Delta T Ein Temperaturdifferenz zwischen Speicher u. Kollektor |
| temperature_condenser_max                   | Number    | Read / Write | de: Maximale Kondensatortemperatur                       |
| idle_time_pressure_reduction                | Number    | Read / Write | de: Pausezeit für Druckabbau bei automatischer Umschaltung |
| support_fan_level_1_earth_heat_exchanger    | Number    | Read / Write | de: Unterstuetzungsgeblaese bei Luftstufe 1 bei EWT      |
| support_fan_level_2_earth_heat_exchanger    | Number    | Read / Write | de: Unterstuetzungsgeblaese bei Luftstufe 2 bei EWT      |
| support_fan_level_3_earth_heat_exchanger    | Number    | Read / Write | de: Unterstuetzungsgeblaese bei Luftstufe 3 bei EWT      |
| control_voltage_outgoing_air                | Number    | Read         | de: Steuerspannung Abluft                                |
| control_voltage_supply_air                  | Number    | Read         | de: Steuerspannung Zuluft                                |
| warm_water_target_temperature               | Number    | Read / Write | de: Warmwasser Sollwert                                  |
| heat_pump_status                            | Contact   | Read         | de: Wärmepumpe An/Aus                                    |
| heat_pump_open                              | Switch    | Read / Write | de: Wärmepumpe freigegeben (1) oder aus (0)              |
| heat_pump_activate                          | Switch    | Read / Write | de: Wärmepumpe Ein/-Ausschalten                          |
| evu_blockade                                | Contact   | Read         | de: EVU Abschaltung                                      |
| malfunction                                 | Contact   | Read         | de: Störung vorhanden                                    |
| error_temp_sensor_short                     | Contact   | Read         | de: Kurzsch. TS                                          |
| error_offset                                | Contact   | Read         | de: Offset error                                         |
| error_temp_sensor_interupt                  | Contact   | Read         | de: Unterbr. TS                                          |
| error_high_pressure                         | Contact   | Read         | de: Hochdruckfehler                                      |
| error_sys_ram                               | Contact   | Read         | de: Error sys ram                                        |
| error_sys_rom                               | Contact   | Read         | de: Error sys rom                                        |
| error_sys_ee                                | Contact   | Read         | de: Error sys ee                                         |
| error_sys_io                                | Contact   | Read         | de: Error sys io                                         |
| error_sys_67_ad                             | Contact   | Read         | de: Error sys 67 ad                                      |
| error_supply_air                            | Contact   | Read         | de: Zuluft fehlt                                         |
| error_outgoing_air                          | Contact   | Read         | de: Abluft  fehlt                                        |
| error_condenser                             | Contact   | Read         | de: Kondensationsfehler                                  |
| error_preheating                            | Contact   | Read         | de: Vorheizfehler                                         |

## Examples

### Items

```
Number wr3223_operation_mode "Betriebsart [%.0f]" { wr3223="operation_mode" }
Number wr3223_ventilation_level "Aktuelle Luftstufe [%.0f]" { wr3223="ventilation_level" }
Number wr3223_temperature_supply_air_target "Soll Temperatur [%.1f]"  { wr3223="temperature_supply_air_target" }
```

### Sitemap (fragment)

```
Switch item=wr3223_ventilation_level label="Stufe" mappings=[0="Aus", 1="1", 2="2", 3="3"]
Switch item=wr3223_operation_mode label="Betriebsart" mappings=[1="Sommer", 2="Abluft", 3="Winter"]
Setpoint item=wr3223_temperature_supply_air_target minValue=16 maxValue=28 step=1
```
