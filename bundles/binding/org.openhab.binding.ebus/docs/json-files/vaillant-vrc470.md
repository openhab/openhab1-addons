# JSON configuration for _vaillant-vrc470_

ID                                                      | Item type  | Description                                                                                                                                             
---                                                     | ---        | ---                                                                                                                                                     
**controller.status_global_system_off**                 | ``Number`` | Activation of operation mode system off - {0=Off, 1=On}                                                                                                 
**controller.temp_d_room_disp**                         | ``Number`` | HC1 Currently displayed room desired temperature                                                                                                        
**controller.temp_outside**.status                      | ``Number`` | Outside temperature status - {0=Ok, 85=Circuit, 170=Cutoff}                                                                                             
**controller.temp_outside**.temp_outside                | ``Number`` | Outside temperature                                                                                                                                     
**controller.temp_room**.status                         | ``Number`` | Room temperature status - {0=Ok, 85=Circuit, 170=Cutoff}                                                                                                
**controller.temp_room**.temp_room                      | ``Number`` | Room temperature                                                                                                                                        
**controller.temp_room_disp**                           | ``Number`` | Room temperature                                                                                                                                        
**dhw.program_dhw_circuit**                             | ``Number`` | DHW Operation mode - {0=Off, 1=On, 2=Auto, 3=Auto Sunday, 4=Party, 6=LoadDHW, 7=Holiday}                                                                
**dhw.set_program_dhw_circuit**.program                 | ``Number`` | DHW Operation mode - {0=Off, 1=On, 2=Auto, 3=Auto Sunday, 4=Party, 6=LoadDHW, 7=Holiday}                                                                
**dhw.set_temp_d_dhw**.temp_d_dhw                       | ``Number`` | DHW setpoint                                                                                                                                            
**dhw.temp_d_actual_dhw**                               | ``Number`` | DHW actual desired temperature                                                                                                                          
**dhw.temp_d_dhw**                                      | ``Number`` | DHW setpoint                                                                                                                                            
**heating.program_heating_circuit**                     | ``Number`` | HC1 Operation mode - {0=Off, 1=Manual, 2=Auto, 3=Day, 4=Night, 5=Summer}                                                                                
**heating.program_heating_circuit_special**             | ``Number`` | HC1 Operation mode - {0=Nothing, 1=Party, 2=OneDayHome, 3=OneDayNotHome, 4=Holiday, 5=Home, 6=QuickVeto, 7=OneTimeVentilation, 8=WhisperMode, 9=LoadDHW}
**heating.set_program_heating_circuit**.program         | ``Number`` | HC1 Operation - {0=Off, 1=Manual, 2=Auto, 3=Day, 4=Night, 5=Summer}                                                                                     
**heating.set_program_heating_circuit_special**.program | ``Number`` | HC1 Operation - {0=Nothing, 1=Party, 2=OneDayHome, 3=OneDayNotHome, 4=Holiday, 5=Home, 6=QuickVeto, 7=OneTimeVentilation, 8=WhisperMode, 9=LoadDHW}     
**heating.set_temp_d_day**.temp_d_day                   | ``Number`` | HC1 Day setpoint                                                                                                                                        
**heating.set_temp_d_night**.temp_d_night               | ``Number`` | HC1 Night setpoint                                                                                                                                      
**heating.set_temp_hcurve**.temp_hcurve                 | ``Number`` | HC1 Heating curve                                                                                                                                       
**heating.temp_d_day**                                  | ``Number`` | HC1 Day setpoint                                                                                                                                        
**heating.temp_d_night**                                | ``Number`` | HC1 Night setpoint                                                                                                                                      
**heating.temp_d_room_override**                        | ``Number`` | HC1 Manual override setpoint                                                                                                                            
**heating.temp_hcurve**                                 | ``Number`` | HC1 Heating curve                                                                                                                                       
**heating.temp_vf1**.status                             | ``Number`` | VF1 temperature status - {0=Ok, 85=Circuit, 170=Cutoff}                                                                                                 
**heating.temp_vf1**.temp_vf1                           | ``Number`` | VF1 temperature                                                                                                                                         

_bold part is the command-id part_

