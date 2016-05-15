# JSON configuration for _vaillant-bai00_

ID                                                             | Item type  | Description                                                          
---                                                            | ---        | ---                                                                  
**boiler.blocktime_boiler**                                    | ``Number`` | Max. burner anti-cycling time heating at 20 °C flow temperature (min)
**boiler.level_part_load**                                     | ``Number`` | Heating partial load (kW)                                            
**boiler.mode_summer_winter_switch**                           | ``Number`` | Summer/winter operating mode - {0=off, 1=on}                         
**boiler.modulation_pump**                                     | ``Number`` | Actual pump speed (%)                                                
**boiler.postrun_pump**                                        | ``Number`` | Overrun time of internal pump for heating mode (min)                 
**boiler.pressure**.pressure                                   | ``Number`` | System pressure (bar)                                                
**boiler.pressure**.status                                     | ``Number`` | Status system pressure - {0=Ok, 85=Circuit, 170=Cutoff}              
**boiler.set_blocktime_boiler**.blocktime_boiler               | ``Number`` | Max. burner anti-cycling time heating at 20 °C flow temperature (min)
**boiler.set_level_part_load**.level_part_load                 | ``Number`` | Heating partial load (kW)                                            
**boiler.set_postrun_pump**.postrun_pump                       | ``Number`` | Overrun time of internal pump for heating mode (min)                 
**boiler.set_state_return_regulation**.state_return_regulation | ``Number`` | Heating flow/return regulation changeover - {0=flow, 1=return}       
**boiler.speed_d_fan**                                         | ``Number`` | Fan speed target value (rpm)                                         
**boiler.speed_fan**                                           | ``Number`` | Fan speed (rpm)                                                      
**boiler.state_diverter_valve**                                | ``Number`` | Position of the diverter valve - {0=heating, 40=parallel, 100=dhw}   
**boiler.state_gas_valve**                                     | ``Number`` | Gas valve status - {240=off, 15=on}                                  
**boiler.state_pump**                                          | ``Number`` | Status of internal pump - {0=off, 1=on}                              
**boiler.state_pump_ext**                                      | ``Number`` | Status of external heating pump - {0=off, 1=on}                      
**boiler.state_return_regulation**                             | ``Number`` | Heating flow/return regulation changeover - {0=flow, 1=return}       
**boiler.temp_d_flow**                                         | ``Number`` | Flow temperature target value (or return target value) (°C)          
**boiler.temp_d_flow_ext**                                     | ``Number`` | Target value from external eBus controller (°C)                      
**boiler.temp_flow**.status                                    | ``Number`` | Status flow temperature - {0=Ok, 85=Circuit, 170=Cutoff}             
**boiler.temp_flow**.temp_flow                                 | ``Number`` | Flow temperature (°C)                                                
**boiler.temp_outlet**.status                                  | ``Number`` | Outlet temp. actual value status - {0=ok, 85=circuit, 170=cutoff}    
**boiler.temp_outlet**.temp_outlet                             | ``Number`` | Outlet temp. actual value (°C)                                       
**boiler.temp_outside**.status                                 | ``Number`` | Status outside temperature - {0=Ok, 85=Circuit, 170=Cutoff}          
**boiler.temp_outside**.temp_outside                           | ``Number`` | Outside temperature (°C)                                             
**boiler.temp_return**.status                                  | ``Number`` | Status return temperature - {0=Ok, 85=Circuit, 170=Cutoff}           
**boiler.temp_return**.temp_return                             | ``Number`` | Return temperature (°C)                                              
**boiler.value_io**                                            | ``Number`` | Digitised ionisation value                                           
**controller.state_thermostat_24V**                            | ``Number`` | Room thermostat 24V - {0=off, 1=on}                                  
**controller.state_thermostat_RT**                             | ``Number`` | Room thermostat at terminal RT - {0=off, 1=on}                       
**dhw.runtime**                                                | ``Number`` | Operating hours, hot water generation (h)                            
**dhw.starts**                                                 | ``Number`` | Burner start-ups in hot water mode (x100)                            
**dhw.state_dhw_demand**                                       | ``Number`` | Hot water requirement - {0=off, 1=on}                                
**dhw.state_dhw_demand_ebus**                                  | ``Number`` | Hot water generation enabled by eBUS controller - {0=off, 1=on}      
**dhw.temp_cylinder**.status                                   | ``Number`` | Hot water sensor status - {0=ok, 85=circuit, 170=cutoff}             
**dhw.temp_cylinder**.temp_cylinder                            | ``Number`` | Measured value of hot water sensor (°C)                              
**dhw.temp_d_cylinder**                                        | ``Number`` | Cylinder temperature target value (°C)                               
**dhw.temp_d_dhw**                                             | ``Number`` | Hot water temperature target value (°C)                              
**heating.runtime**                                            | ``Number`` | Operating hours, heating (h)                                         
**heating.starts**                                             | ``Number`` | Burner start-ups in heating mode (x100)                              

_bold part is the command-id part_

