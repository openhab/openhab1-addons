# JSON configuration for _vaillant_

ID                           | Class     | Command                      | Item type | Description                                                  
---                          | ---       | ---                          | ---       | ---                                                          
bit_brw_off                  | burner_v  | -                            | Switch    | Brauchwasser aus, vom Regler                                 
bit_heating_off              | burner_v  | -                            | Switch    | Heizen aus, vom Regler                                       
circulating_pump             | burner_v  | -                            | Switch    | Zirkulations Pumpe                                           
heating_on                   | burner_v  | -                            | Switch    | Heizen Ein                                                   
internal_pump                | burner_v  | -                            | Switch    | Interne Pumpe                                                
service_water_on             | burner_v  | -                            | Switch    | Brauchwasser Ein                                             
temp_boiler                  | burner_v  | -                            | Number    | Brauchwasser Speichertemperatur                              
temp_outside                 | burner_v  | -                            | Number    | Aussentemperatur                                             
temp_return                  | burner_v  | -                            | Number    | Rücklauftemperatur                                           
temp_t_boiler                | burner_v  | -                            | Number    | Brauchwasser Sollwert                                        
temp_t_flow                  | burner_v  | -                            | Number    | Sollwert Vorlauftemperatur vom Regler                        
temp_t_water                 | burner_v  | -                            | Number    | Sollwert Brauchwasser vom Regler                             
temp_vessel                  | burner_v  | -                            | Number    | Vorlauftemperatur                                            
temp_water                   | burner_v  | -                            | Number    | Brauchwasser Auslauftemperatur                               
unknown_00                   | burner_v  | -                            | Number    | Unbekannt immer 00                                           
unknown_03                   | burner_v  | -                            | Number    | Unbekannt immer 3d                                           
unknown_3c                   | burner_v  | -                            | Number    | Unbekannt immer 60d                                          
unknown_50                   | burner_v  | -                            | Number    | Unbekannt immer 80d                                          
unknown_6e                   | burner_v  | -                            | Number    | Unbekannt immer 110d                                         
unknown_ff                   | burner_v  | -                            | Number    | Unbekannt immer FF                                           
date                         | control_v | -                            | ???       | Aktuelles Datum                                              
date                         | control_v | dcf77data                    | ???       | Aktuelles Datum                                              
date_day                     | control_v | -                            | Number    | Datum Tag                                                    
date_day                     | control_v | dcf77data                    | Number    | Datum Tag                                                    
date_month                   | control_v | -                            | Number    | Datum Monat                                                  
date_month                   | control_v | dcf77data                    | Number    | Datum Monat                                                  
date_time                    | control_v | -                            | ???       | Aktuelles Datum und Zeit                                     
date_time                    | control_v | dcf77data                    | ???       | Aktuelles Datum und Zeit                                     
date_weekday                 | control_v | -                            | Number    | Datum Wochentag                                              
date_weekday                 | control_v | dcf77data                    | Number    | Datum Wochentag                                              
date_year                    | control_v | -                            | Number    | Datum Jahr                                                   
date_year                    | control_v | dcf77data                    | Number    | Datum Jahr                                                   
state_dcf                    | control_v | dcf77data                    | Number    | DCF77 Status - 00 kein Empfang, 01 Empfang, 02 Sync, 03 Valid
temp_outdoor                 | control_v | -                            | Number    | Aussentemperatur Istwert                                     
temp_outside                 | control_v | dcf77data                    | Number    | Aussentemperatur                                             
time                         | control_v | -                            | ???       | Aktuelle Zeit                                                
time                         | control_v | dcf77data                    | ???       | Aktuelle Zeit                                                
time_hour                    | control_v | -                            | Number    | Zeit Stunde                                                  
time_hour                    | control_v | dcf77data                    | Number    | Zeit Stunden                                                 
time_min                     | control_v | -                            | Number    | Zeit Min                                                     
time_min                     | control_v | dcf77data                    | Number    | Zeit Minuten                                                 
time_sec                     | control_v | -                            | Number    | Zeit Sek.                                                    
time_sec                     | control_v | dcf77data                    | Number    | Zeit Sekunden                                                
op_hrs_pump                  | solar_v   | solar_data1                  | Number    | Pumpe Laufzeit                                               
op_hrs_pump2                 | solar_v   | solar_data1                  | Number    | Pumpe Laufzeit                                               
solar_pump                   | solar_v   | solar_data1                  | Number    | Solarpumpe                                                   
solar_pump2                  | solar_v   | solar_data1                  | Number    | Solarpumpe                                                   
status_flow                  | solar_v   | temp_flow                    | Number    | Sensor vorhanden                                             
status_return                | solar_v   | temp_return                  | Number    | Sensor vorhanden                                             
temp_flow                    | solar_v   | temp_flow                    | Number    | Kollektor Vorlauf                                            
temp_flow                    | solar_v   | solar_data1                  | Number    | Kollektor KOL1                                               
temp_flow2                   | solar_v   | solar_data1                  | Number    | Kollektor KOL2                                               
temp_return                  | solar_v   | temp_return                  | Number    | Kollektor Rücklauf                                           
performance_circulation_pump | water_v   | performance_circulation_pump | Number    | WW - Zirkulationspumpe Leistung (%)                          
status_ntc1                  | water_v   | temp_ntc1                    | Number    | NTC Status 1                                                 
status_ntc2                  | water_v   | temp_ntc2                    | Number    | NTC Status 2                                                 
status_ntc3                  | water_v   | temp_ntc3                    | Number    | NTC Status 3                                                 
temp_ntc1                    | water_v   | temp_ntc1                    | Number    | NTC Sensor 1                                                 
temp_ntc2                    | water_v   | temp_ntc2                    | Number    | NTC Sensor 2                                                 
temp_ntc3                    | water_v   | temp_ntc3                    | Number    | NTC Sensor 3                                                 
volume_flow                  | water_v   | volume_flow                  | Number    | WW - Pufferkreislauf Volumenstrom                            
