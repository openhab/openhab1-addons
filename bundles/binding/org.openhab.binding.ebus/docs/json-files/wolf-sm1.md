# JSON configuration for _wolf-sm1_

ID                     | Class | Command                | Item type | Description                        
---                    | ---   | ---                    | ---       | ---                                
e1                     | solar | e1                     | Number    | E1 - Return temp (°C)              
output_current         | solar | solar_yield            | Number    | Current output (kW)                
runtime_pump           | solar | runtime_pump           | Number    | Runtime solar circuit pump 1 (h)   
state_pump             | solar | solar_data             | Switch    | Pump status                        
temp_collector         | solar | temp_collector         | Number    | Solar collector temperature (°C)   
temp_collector         | solar | solar_data             | Number    | Temperature, collector 1 °C        
temp_collector_24h_max | solar | temp_collector_24h_max | Number    | Max. collector temperature 24h (°C)
temp_collector_24h_min | solar | temp_collector_24h_min | Number    | Min. collector temperature 24h (°C)
temp_cylinder          | solar | temp_boiler            | Number    | Solar cylinder temperature (°C)    
temp_cylinder          | solar | solar_data             | Number    | Temperature, solar cylinder 1 °C   
temp_cylinder_24h_max  | solar | temp_cylinder_24h_max  | Number    | Max. cylinder temperature 24h (°C) 
temp_cylinder_24h_min  | solar | temp_cylinder_24h_min  | Number    | Min. cylinder temperature 24h (°C) 
temp_return            | solar | temp_return            | Number    | Solar return temperature (°C)      
yield_today            | solar | solar_yield            | Text      | Yield today (kW/h)                 
yield_total            | solar | solar_yield            | Text      | Total yield (kW/h)                 
