# JSON configuration for _vaillant-bai00_

ID                  | Class   | Command             | Item type | Description                                                
---                 | ---     | ---                 | ---       | ---                                                        
speed_fan           | boiler  | speed_fan           | Number    | Fan speed (?)                                              
status_flow         | boiler  | temp_flow           | Number    | Status flow temperature - {0=ok, 85=circuit, 170=cutoff}   
status_return       | boiler  | temp_return         | Number    | Status return temperature - {0=ok, 85=circuit, 170=cutoff} 
temp_d_flow         | boiler  | temp_d_flow         | Number    | Desired flow temperature (°C)                              
temp_flow           | boiler  | temp_flow           | Number    | Flow temperature (°C)                                      
temp_return         | boiler  | temp_return         | Number    | Return temperature (°C)                                    
value_io            | boiler  | value_io            | Number    | Ionization current                                         
status_cylinder     | dhw     | temp_cylinder       | Number    | Storage temperature status - {0=ok, 85=circuit, 170=cutoff}
temp_cylinder       | dhw     | temp_cylinder       | Number    | Storage temperature                                        
level_modulation    | heating | level_modulation    | Number    | Modulation level (%)                                       
level_modulation-kw | heating | level_modulation-kw | Number    | Modulation level (kW)                                      
pressure            | heating | pressure            | Number    | System pressure (bar)                                      
status_pressure     | heating | pressure            | Number    | Status system pressure - {0=ok, 85=circuit, 170=cutoff}    
