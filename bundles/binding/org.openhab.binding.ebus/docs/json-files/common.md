# JSON configuration for _common_

ID                  | Class        | Command        | Item type | Description                                                
---                 | ---          | ---            | ---       | ---                                                        
performance_burner  | auto_stroker | -              | Number    | Setting degree between min. and max. boiler performance (%)
state_air_pressure  | auto_stroker | -              | Switch    | Air pressure monitor                                       
state_alarm         | auto_stroker | -              | Switch    | Alarm                                                      
state_flame         | auto_stroker | -              | Switch    | Flame                                                      
state_gas_pressure  | auto_stroker | -              | Switch    | Gas pressure monitor                                       
state_pump          | auto_stroker | -              | Switch    | State heating pump                                         
state_valve1        | auto_stroker | -              | Switch    | Valve1                                                     
state_valve2        | auto_stroker | -              | Switch    | Valve2                                                     
state_water_flow    | auto_stroker | -              | Switch    | Water flow                                                 
status_auto_stroker | auto_stroker | -              | Number    | Status indication                                          
temp_boiler         | auto_stroker | -              | Number    | Boiler temperature (°C)                                    
temp_dhw            | auto_stroker | -              | Number    | DHW temperature (°C)                                       
temp_outside        | auto_stroker | -              | Number    | Outside temperature (°C)                                   
temp_return         | auto_stroker | -              | Number    | Return temperature (°C)                                    
date                | common       | -              | Text      | Current date                                               
date_day            | common       | -              | Number    | Date day                                                   
date_month          | common       | -              | Number    | Date month                                                 
date_year           | common       | -              | Number    | Date year                                                  
device              | common       | identification | Text      | Device ID                                                  
device              | common       | -              | Text      | Device ID                                                  
hardware_version    | common       | identification | Text      | Hardware Version                                           
hardware_version    | common       | -              | Text      | Hardware Version                                           
software_version    | common       | identification | Text      | Software Version                                           
software_version    | common       | -              | Text      | Software Version                                           
temp_ouside         | common       | -              | Number    | Outside temperature (°C)                                   
time                | common       | -              | Text      | Current time                                               
time_hour           | common       | -              | Number    | Time hour                                                  
time_min            | common       | -              | Number    | Time min                                                   
time_sec            | common       | -              | Number    | Time sec.                                                  
vendor              | common       | identification | Number    | Vendor                                                     
vendor              | common       | -              | Number    | Vendor                                                     
level_modulation    | controller   | -              | Number    | Setting degree                                             
pressure_d_boiler   | controller   | -              | Number    | Boiler desire pressure (bar)                               
status_heat_req1    | controller   | -              | Number    | Status heat request 1                                      
status_heat_req2    | controller   | -              | Number    | Status heat request 2                                      
temp_d_boiler       | controller   | -              | Number    | Boiler desire temperature (°C)                             
temp_d_dhw          | controller   | -              | Number    | DWH desire temperature (°C)                                
value_fuel          | controller   | -              | Number    | Election of fuel                                           
performance_forced  | controller2  | -              | Number    | Forced performance                                         
state_dhw           | controller2  | -              | Switch    | State DHW(?) active                                        
state_hc            | controller2  | -              | Switch    | State heating circuit active                               
temp_d_boiler       | controller2  | -              | Number    | Boiler desire temperature (°C)                             
temp_d_dhw          | controller2  | -              | Number    | DWH desire temperature (°C)                                
temp_outside        | controller2  | -              | Number    | Outside temperature (°C)                                   
