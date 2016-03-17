# JSON configuration for _vaillant-vrc430_

ID                                              | Item type  | Description                                                             
---                                             | ---        | ---                                                                     
**controller.temp_outside**.status              | ``Number`` | Outside temperature status - {0=Ok, 85=Circuit, 170=Cutoff}             
**controller.temp_outside**.temp_outside        | ``Number`` | Outside temperature                                                     
**controller.temp_room**.status                 | ``Number`` | Room temperature status - {0=Ok, 85=Circuit, 170=Cutoff}                
**controller.temp_room**.temp_room              | ``Number`` | Room temperature                                                        
**controller.temp_room_disp**                   | ``Number`` | Room temperature                                                        
**dhw.program_dhw_circuit**                     | ``Number`` | DHW Operation mode - {0=Off, 1=Manual, 2=Auto}                          
**dhw.set_program_dhw_circuit**.program         | ``Number`` | DHW Operation mode - {0=Off, 1=Manual, 2=Auto}                          
**dhw.set_temp_d_dhw**.temp_d_dhw               | ``Number`` | DHW setpoint                                                            
**dhw.temp_d_actual_dhw**                       | ``Number`` | DHW actual desired temperature                                          
**dhw.temp_d_dhw**                              | ``Number`` | DHW setpoint                                                            
**heating.program_heating_circuit**             | ``Number`` | HC1 Operation mode - {0=Off, 1=Manual, 2=Auto, 3=Day, 4=Night, 5=Summer}
**heating.set_program_heating_circuit**.program | ``Number`` | HC1 Operation - {0=Off, 1=Manual, 2=Auto, 3=Day, 4=Night, 5=Summer}     
**heating.set_temp_d_man**.temp_d_man           | ``Number`` | HC1 Day setpoint                                                        
**heating.set_temp_d_night**.temp_d_night       | ``Number`` | HC1 Night setpoint                                                      
**heating.set_temp_hcurve**.temp_hcurve         | ``Number`` | HC1 Heating curve                                                       
**heating.temp_d_man**                          | ``Number`` | HC1 Day setpoint                                                        
**heating.temp_d_night**                        | ``Number`` | HC1 Night setpoint                                                      
**heating.temp_d_room_override**                | ``Number`` | HC1 Manual override setpoint                                            
**heating.temp_hcurve**                         | ``Number`` | HC1 Heating curve                                                       

_bold part is the command-id part_

