# JSON configuration for _wolf-cgb2_

ID               | Class   | Command                     | Item type | Description                                                          
---              | ---     | ---                         | ---       | ---                                                                  
count_power_on   | boiler  | count_power_on              | Number    | Power ON count                                                       
level_modulation | boiler  | level_modulation            | Number    | Modulation level (%)                                                 
runtime          | boiler  | runtime                     | Number    | Mains hours (h)                                                      
software_version | boiler  | fw_version                  | Number    | Software Version HCM2                                                
temp_flow        | boiler  | temp_flow                   | Number    | Boiler temperature (°C)                                              
temp_flue_gas    | boiler  | temp_exhaust                | Number    | Current flue gas temperature (°C)                                    
temp_return      | boiler  | temp_return                 | Number    | Boiler return temperature (°C)                                       
value_io         | boiler  | value_actual_io             | Number    | Actual I/O value                                                     
runtime          | burner  | runtime                     | Number    | Burner hours run (h)                                                 
starts           | burner  | starts                      | Number    | Burner starts                                                        
program_dhw      | dhw     | set_dhw_circuit_program     | Number    | DHW program - {0=standby, 1=auto, 2=always on}                       
program_dhw      | dhw     | dhw_circuit_program         | Number    | DHW program - {0=standby, 1=auto, 2=always on}                       
temp_dhw         | dhw     | temp_dhw                    | Number    | DHW temperature (°C)                                                 
pressure         | heating | pressure                    | Number    | System pressure (bar)                                                
program_heating  | heating | set_heating_circuit_program | Number    | Heating program - {0=standby, 1=auto, 2=heating mode, 3=economy mode}
program_heating  | heating | heating_circuit_program     | Number    | Heating program - {0=standby, 1=auto, 2=heating mode, 3=economy mode}
speed_pump       | heating | speed_pump                  | Number    | Heating circuit pump speed (%)                                       
