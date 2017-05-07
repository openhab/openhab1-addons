# EcoTouch Binding

The openHAB EcoTouch binding allows interaction with a heat pump.


## Prerequisites

Make sure the display unit of the heat pump is connected to the network and
that the network settings are configured. By default, the heat pump uses DHCP.


## Binding Configuration

The binding can be configured in the file `services/ecotouch.cfg`.

| Property | Default    | Required | Description |
|----------|------------|:--------:|-------------|
| ip       |            | Yes      | ip or DNS name of the heat pump |
| username | waterkotte | No       | username for login |
| password | waterkotte | No       | password for login |

NOTE: Since Waterkotte Display software version 1.6.xx, the username/password
default value is `waterkotte`. Prior to that, the username default was `admin`
and the password default was `wtkadmin`.


## Item Configuration

The binding configuration format is simple and looks like this:

    ecotouch="<eventType>"

where `<eventType>` is one of the following values:

| Description | `<eventType>` | ItemClass | Write | Notes |
| ----------- | ------------- | --------- | ----- | ----- |
| Außentemperatur | temperature_outside | number |  | 
| Außentemperatur gemittelt über 1h | temperature_outside_1h | number |  | 
| Außentemperatur gemittelt über 24h | temperature_outside_24h | number |  | 
| Quelleneintrittstemperatur | temperature_source_in | number |  | Ai1 Geo only |
| Quellenaustrittstemperatur | temperature_source_out | number |  | Ai1 Geo only | 
| Umgebungstemperatur | temperature_surrounding | number |  | Ai1 Air only |
| Verdampfungstemperatur | temperature_evaporation | number |  | 
| Sauggastemperatur | temperature_suction | number | | Ai1 Geo only | 
| Sauggas | temperature_suction_air | number |  | Ai1 Air only |
| Ölsumpf | temperature_sump | number |  | Ai1 Air only |
| Verdampfungsdruck | pressure_evaporation | number |  | 
| Temperatur Rücklauf Soll | temperature_return_set | number |  | 
| Temperatur Rücklauf | temperature_return | number |  | 
| Temperatur Vorlauf | temperature_flow | number |  | 
| Kondensationstemperatur | temperature_condensation | number |  | 
| Kondensationsdruck | pressure_condensation | number |  | 
| Speichertemperatur | temperature_storage | number |  | 
| Raumtemperatur | temperature_room | number |  | 
| Raumtemperatur gemittelt über 1h | temperature_room_1h | number |  | 
| Warmwassertemperatur | temperature_water | number |  | 
| Pooltemperatur | temperature_pool | number |  | 
| Solarkollektortemperatur | temperature_solar | number |  | 
| Solarkreis Vorlauf | temperature_solar_flow | number |  | 
| Ventilöffnung elektrisches Expansionsventil % | position_expansion_valve | number |  | 
| elektrische Leistung Verdichter | power_compressor | number |  | 
| abgegebene thermische Heizleistung der Wärmepumpe | power_heating | number |  | 
| abgegebene thermische KälteLeistung der Wärmepumpe | power_cooling | number |  | 
| COP Heizleistung | cop_heating | number |  | 
| COP Kälteleistung | cop_cooling | number |  | 
| Aktuelle Heizkreistemperatur | temperature_heating_return | number |  | 
| Geforderte Temperatur im Heizbetrieb | temperature_heating_set | number |  | 
| Sollwertvorgabe Heizkreistemperatur | temperature_heating_set2 | number | yes | 
| Aktuelle Kühlkreistemperatur | temperature_cooling_return | number |  | 
| Geforderte Temperatur im Kühlbetrieb | temperature_cooling_set | number |  | 
| Sollwertvorgabe Kühlbetrieb | temperature_cooling_set2 | number | yes | 
| Sollwert Warmwassertemperatur | temperature_water_set | number |  | 
| Sollwertvorgabe Warmwassertemperatur | temperature_water_set2 | number | yes | 
| Sollwert Poolwassertemperatur | temperature_pool_set | number |  | 
| Sollwertvorgabe Poolwassertemperatur | temperature_pool_set2 | number | yes | 
| geforderte Verdichterleistung | compressor_power | number |  | 
| % Heizungsumwälzpumpe | percent_heat_circ_pump | number |  | 
| % Quellenpumpe | percent_source_pump | number |  | 
| % Leistung Verdichter | percent_compressor | number |  | 
| Heizkurve - Norm-Aussen-Temp (Auslegungstemp)| nviNormAussen | number | yes | 
| Heizkurve - Temp Heizkreis bei Auslegungstemp | nviHeizkreisNorm | number | yes | 
| Heizkurve - Temp Heizgrenze | nviTHeizgrenze | number | yes | 
| Heizkurve - Temp Heizkreis Heizgrenze | nviTHeizgrenzeSoll | number | yes | 
| Heizkurve - max. VL-Temp | maxVLTemp | number | yes | 
| Kühlen Einschalt-Aussentemp | coolEnableTemp | number | yes | 
| geforderte Kühltemperatur | nviSollKuehlen | number |  | 
| Firmware-Version Regler | version_controller | number |  | 
| Build-Nr. Regler | version_controller_build | number |  | 
| Bios-Version Regler | version_bios | number |  | 
| Datum: Tag | date_day | number |  | 
| Datum: Monat | date_month | number |  | 
| Datum: Jahr | date_year | number |  | 
| Uhrzeit: Stunde | time_hour | number |  | 
| Uhrzeit: Minute | time_minute | number |  | 
| Betriebsstunden Verdichter 1 | operating_hours_compressor1 | number |  | 
| Betriebsstunden Verdichter 2 | operating_hours_compressor2 | number |  | 
| Betriebsstunden Heizungsumwälzpumpe | operating_hours_circulation_pump | number |  | 
| Betriebsstunden Quellenpumpe | operating_hours_source_pump | number |  | 
| Betriebsstunden Solarkreis | operating_hours_solar | number |  | 
| Handabschaltung Heizbetrieb | enable_heating | switch | yes | 
| Handabschaltung Kühlbetrieb | enable_cooling | switch | yes | 
| Handabschaltung Warmwasserbetrieb | enable_warmwater | switch | yes | 
| Handabschaltung Pool_Heizbetrieb | enable_pool | switch | yes | 
| Status der Wärmepumpenkomponenten | state | number |  | State as a number, see below for possible mapping. |
| Status Quellenpumpe | state_sourcepump | switch |  | 
| Status Heizungsumwälzpumpe | state_heatingpump | switch |  | 
| Status Freigabe Regelung EDV / Magnetventil | state_evd | switch |  | 
| Status Verdichter 1 | state_compressor1 | switch |  | 
| Status Verdichter 2 | state_compressor2 | switch |  | 
| Status externer Wärmeerzeuger | state_extheater | switch |  | 
| Status Alarmausgang | state_alarm | switch |  | 
| Status Motorventil Kühlbetrieb | state_cooling | switch |  | 
| Status Motorventil Warmwasser | state_water | switch |  | 
| Status Motorventil Pool | state_pool | switch |  | 
| Status Solarbetrieb | state_solar | switch |  | 
| Status 4-Wege-Ventil | state_cooling4way | switch |  | 
| Temperaturanpassung für die Heizung -2,0 / +2,0 | adapt_heating | number | yes | -2 bis +2 (0,5)---> 0 bis 8
| Handschaltung Heizungspumpe | manual_heatingpump |  | yes | (H-0-A) H= manuell ein (Handschaltung), 0= aus, A= Automatik
| Handschaltung Quellenpumpe | manual_sourcepump |  | yes | (H-0-A)
| Handschaltung Solarpumpe 1 | manual_solarpump1 |  | yes | (H-0-A)
| Handschaltung Solarpumpe 2 | manual_solarpump2 |  | yes | (H-0-A)
| Handschaltung Speicherladepumpe | manual_tankpump |  | yes | (H-0-A)
| Handschaltung Brauchwasserventil | manual_valve |  | yes | (H-0-A)
| Handschaltung Poolventil | manual_poolvalve |  | yes | (H-0-A)
| Handschaltung Kühlventil | manual_coolvalve |  | yes | (H-0-A)
| Handschaltung Vierwegeventil | manual_4wayvalve |  | yes | (H-0-A)
| Handschaltung Multiausgang Ext. | manual_multiext |  | yes | (H-0-A)

### Mappings

Heatpump event type "state" can be mapped to strings. Different heatpump models seem to have slightly different state values.
Mappings for the Ai1 Geo have been copied from [here](http://www.haustechnikdialog.de/Forum/t/173357/Waterkotte-via-Ethernet-OpenHAB-und-Android-App?page=6).

| State | Ai1 Geo | Ai1 Air	|
| ----- | ----- | ----- |
| 0 | aus	| aus |
| 1 | nur Solepumpe 1	| |
| 2 | nur Heizkreispumpe 2 | Standby |
| 3 | unbekannt | |
| 7 | unbekannt | |
| 10 | | Heizbetrieb |
| 15 | Heizbetrieb | |
| 131 | Naturkühlung | |
| 256 | Vorlauf Warmwasser | Vorlauf Warmwasser |
| 258 | Nachlauf Warmwasser | Nachlauf Warmwasser |
| 259 | unbekannt 259 vor WW Bereitung | |
| 263 | unbekannt 263 vor WW Bereitung | |
| 266 | | Warmwasserbereitung |
| 271 | Warmwasserbereitung | |


## Examples

### Items examples

    /* Waterkotte EcoTouch heat pump DEMO */
    Group Heatpump
    Group Heatpump_outside
    Group Heatpump_source
    Group Heatpump_control
    Group Heatpump_water
    Group Heatpump_heating
    Group Heatpump_power
    Group Heatpump_state
    Number Chart_Period "Chart Period"
    Number HeatPump_Temperature_3   "Wärmepumpe Außentemperatur [%.1f °C]"   <temperature> (Heatpump,Heatpump_outside) { ecotouch="temperature_outside" }
    Number HeatPump_Temperature_4   "Wärmepumpe Außentemperatur 1h [%.1f °C]"   <temperature> (Heatpump,Heatpump_outside) { ecotouch="temperature_outside_1h" }
    Number HeatPump_Temperature_5   "Wärmepumpe Außentemperatur 24h [%.1f °C]"   <temperature> (Heatpump,Heatpump_outside) { ecotouch="temperature_outside_24h" }
    Number HeatPump_Temperature_6   "Wärmepumpe Quelleneintrittstemperatur [%.1f °C]"   <temperature> (Heatpump,Heatpump_source) { ecotouch="temperature_source_in" }
    Number HeatPump_Temperature_7   "Wärmepumpe Quellenaustrittstemperatur [%.1f °C]"   <temperature> (Heatpump,Heatpump_source) { ecotouch="temperature_source_out" }
    Number HeatPump_Temperature_8   "Wärmepumpe Vorlauf [%.1f °C]"   <temperature> (Heatpump,Heatpump_control) { ecotouch="temperature_flow" }
    Number HeatPump_Temperature_9   "Wärmepumpe Rücklauf [%.1f °C]"   <temperature> (Heatpump,Heatpump_control) { ecotouch="temperature_return" }
    Number HeatPump_Temperature_10   "Wärmepumpe Rücklauf Soll [%.1f °C]"   <temperature> (Heatpump,Heatpump_control) { ecotouch="temperature_return_set" }
    Number HeatPump_Temperature_11   "Wärmepumpe Warmwasser Ist [%.1f °C]"   <temperature> (Heatpump,Heatpump_water) { ecotouch="temperature_water" }
    Number HeatPump_Temperature_12   "Wärmepumpe Warmwasser Soll [%.1f °C]"   <temperature> (Heatpump,Heatpump_water) { ecotouch="temperature_water_set" }
    Number HeatPump_Temperature_13   "Wärmepumpe Warmwasser Soll2 [%.1f °C]"   <temperature> (Heatpump,Heatpump_water) { ecotouch="temperature_water_set2" }
    Number HeatPump_Temperature_14   "Wärmepumpe Heizung Ist [%.1f °C]"   <temperature> (Heatpump,Heatpump_heating) { ecotouch="temperature_heating_return" }
    Number HeatPump_Temperature_15   "Wärmepumpe Heizung Soll [%.1f °C]"   <temperature> (Heatpump,Heatpump_heating) { ecotouch="temperature_heating_set" }
    Number HeatPump_Temperature_16   "Wärmepumpe Heizung Soll2 [%.1f °C]"   <temperature> (Heatpump,Heatpump_heating) { ecotouch="temperature_heating_set2" }
    Number HeatPump_power_1   "Wärmepumpe elektrische Leistung [%.1f kW]"   <energy> (Heatpump,Heatpump_power) { ecotouch="power_compressor" }
    Number HeatPump_power_2   "Wärmepumpe thermische Leistung [%.1f kW]"   <energy> (Heatpump,Heatpump_power) { ecotouch="power_heating" }
    Number HeatPump_state            "Wärmepumpe Status [%i]"   <settings> (Heatpump) { ecotouch="state" }
    Number HeatPump_state_sourcepump "Wärmepumpe Status Quellenpumpe [%i]"   <settings> (Heatpump,Heatpump_state) { ecotouch="state_sourcepump" }
    Number HeatPump_state_heatingpump "Wärmepumpe Status Heizungsumwälzpumpe [%i]"   <settings> (Heatpump,Heatpump_state) { ecotouch="state_heatingpump" }
    Number HeatPump_state_compressor1 "Wärmepumpe Status Kompressor [%i]"   <settings> (Heatpump,Heatpump_state) { ecotouch="state_compressor1" }
    Number HeatPump_state_water      "Wärmepumpe Status Motorventil Warmwasser[%i]"   <settings> (Heatpump,Heatpump_state) { ecotouch="state_water" }

### Sitemap examples

        Frame label="Waterkotte" {
                Text item=HeatPump_Temperature_3 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_outside period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_outside period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_outside period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_6 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_source period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_source period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_source period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_7 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_source period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_source period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_source period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_8 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_control period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_control period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_control period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_9 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_control period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_control period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_control period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_10 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_control period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_control period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_control period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_11 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_water period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_water period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_water period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_12 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_water period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_water period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_water period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_13 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_water period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_water period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_water period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_14 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_heating period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_heating period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_heating period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_15 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_heating period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_heating period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_heating period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_Temperature_16 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_heating period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_heating period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_heating period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_power_1 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_power period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_power period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_power period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
                Text item=HeatPump_power_2 {
                        Frame {
                                Switch item=Chart_Period label="Chart Period" mappings=[0="Hour", 1="Day", 2="Week"]
                                Chart item=Heatpump_power period=h refresh=60000 visibility=[Chart_Period==0, Chart_Period=="Uninitialized"]
                                Chart item=Heatpump_power period=D refresh=60000 visibility=[Chart_Period==1]
                                Chart item=Heatpump_power period=W refresh=60000 visibility=[Chart_Period==2]
                        }
                }
        }


### Gallery

The following charts are created via the rrd4j chart and heatpump bundle.

![outside temperature](https://github.com/sibbi77/openhab/wiki/images/binding-ecotouch_chart1.png)

![power consumption](https://github.com/sibbi77/openhab/wiki/images/binding-ecotouch_chart2.png)
