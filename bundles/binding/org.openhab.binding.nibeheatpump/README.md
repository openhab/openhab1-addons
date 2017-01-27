# Nibe Heatpump Binding

The Nibe heat pump binding is used to get live data from from Nibe heat pumps without using a Modbus adapter (but instead using an Arduino UNO and RS485 and Ethernet shields). This binding should be compatible with at least the F1145 and F1245 heat pump models.

## Prerequisites

When Modbus adapter support is enabled from the heat pump UI, the heat pump will start to send telegrams every now and then. A telegram contains a maximum of 20 registers. Those 20 registers can be configured via the Nibe ModbusManager application. 

Unfortunately Nibe has made this tricky: telegram from heat pump should be acknowledged, otherwise heat pump will raise an alarm and go in alarm state. Acknowledge (ACK or NAK) should be sent accurately. This can be resolved by using the `nibegw` program, which can be run on unix/linux (such as a Raspberry Pi) or Arduino-based boards. 

`nibegw` is an application that read telegrams from a serial port (which requires an RS-485 adapter), sends ACK/NAK to the heat pump and relays untouched telegrams to openHAB via UDP packets. The Nibe Heat Pump binding will listen to a UDP port and parse register data from UDP telegrams.

This is an Arduino-based solution, tested with Arduino uno + RS485 and Ethernet shields.

Arduino code is available [here](https://github.com/openhab/openhab/blob/master/bundles/binding/org.openhab.binding.nibeheatpump/NibeGW/NibeGW.ino)

C code is available on [here](https://github.com/openhab/openhab/blob/master/bundles/binding/org.openhab.binding.nibeheatpump/NibeGW/nibegw.c)  

build: 

```shell
gcc -std=gnu99 -o nibegw nibegw.c
```

run:

```shell
nibegw -h 
nibegw -v -d /dev/ttyUSB0 -a 192.168.1.10
```

## Binding Configuration

This binding can be configured using the file `services/nibeheatpump.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| udpPort  | 9999    |    No    | UDP port on which the binding will be listening |

## Item  Configuration

The syntax of the binding configuration strings accepted is the following:

```
nibeheatpump="<coilAddress>"
```

where `<coilAddress>` corresponds to the Modbus coil addresses:

| Address | Item Type | Purpose  | 
|---------|:---------:|----------|
| 47332   | Number    | Cooling supply temp at 20C |
| 47333   | Number    | Cooling supply temp at 40C |
| 47334   | Number    | Cooling close mixing valves |
| 47335   | Number    | Time between switch heat/cooling |
| 47329   | Number    | Cooling 2-pipe accessory |
| 47330   | Number    | Cooling 4-pipe accessory |
| 47331   | Number    | Min cooling supply temp |
| 47340   | Number    | Cooling with room sensor | 
| 47336   | Number    | Heat at room under temp | 
| 47337   | Number    | Cool at room over temp | 
| 47338   | Number    | Cooling mixing valve amp | 
| 47339   | Number    | Cooling mixing valve step delay | 
| 47319   | Number    | Shunt controlled add min runtime | 
| 47318   | Number    | Shunt controlled add min temp | 
| 47317   | Number    | Shunt controlled addition accessory | 
| 40152   | Number    | BT71 ext return temp | 
| 47313   | Number    | FLM defrost | 
| 47312   | Number    | FLM pump | 
| 47327   | Number    | Ground water pump accessory | 
| 47326   | Number    | Step controlled add mode | 
| 47324   | Number    | Step controlled add diff DM | 
| 47323   | Number    | Step controlled add start DM | 
| 47322   | Number    | Step controlled add accessory | 
| 47321   | Number    | Shunt controlled add mix valve wait |
| 47320   | Number    | Shunt controlled add mix valve amp | 
| 47302   | Number    | Climate system 2 accessory | 
| 47303   | Number    | Climate system 3 accessory | 
| 47310   | Number    | Climate system 2 shunt wait | 
| 43516   | Number    | PCA-Power relayes EP14 | 
| 47308   | Number    | Climate system 4 shunt wait | 
| 47309   | Number    | Climate system 3 shunt wait | 
| 43514   | Number    | PCA-Base relayes EP14 | 
| 40128   | Number    | EB22-BT3 return temp S3 | 
| 47306   | Number    | Climate system 3 mixing valve amp | 
| 40129   | Number    | EB21-BT3 return temp S2 | 
| 47307   | Number    | Climate system 2 mixing valve amp | 
| 47304   | Number    | Climate system 4 accessory | 
| 47305   | Number    | Climate system 4 mixing valve amp | 
| 47281   | Number    | Floor drying period 3 | 
| 47280   | Number    | Floor drying period 4 |  
| 43395   | Number    | HPAC relays  | 
| 47283   | Number    | Floor drying period 1  | 
| 47282   | Number    | Floor drying period 2  | 
| 40127   | Number    | EB23-BT3 return temp S4  |  
| 47285   | Number    | Floor drying temp 6  |  
| 47284   | Number    | Floor drying temp 7  | 
| 47287   | Number    | Floor drying temp 4  |  
| 47286   | Number    | Floor drying temp 5 | 
| 40115   | Number    | AZ1-BT26 temp collector in FLM 2 | 
| 47289   | Number    | Floor drying temp 2 | 
| 40114   | Number    | AZ1-BT26 temp collector in FLM 3 | 
| 47288   | Number    | Floor drying temp 3 | 
| 40113   | Number    | AZ1-BT26 temp collector in FLM 4 | 
| 47291   | Number    | Floor drying timer  | 
| 40112   | Number    | EB100-BT21 vented air temp  | 
| 47290   | Number    | Floor drying temp 1 | 
| 40118   | Number    | AZ1-BT27 temp collector out FLM 2  |  
| 40117   | Number    | AZ1-BT27 temp collector out FLM 3  | 
| 40116   | Number    | AZ1-BT27 temp collector out FLM 4  | 
| 47264   | Number    | Exhaust fan speed 1  | 
| 40107   | Number    | EB100-BT20 exhaust air temp  | 
| 47265   | Number    | Exhaust fan speed normal |
| 40110   | Number    | EB100-BT21 vented air temp |
| 40111   | Number    | EB100-BT21 vented air temp |
| 40108   | Number    | EB100-BT20 exhaust air temp | 
| 40109   | Number    | EB100-BT20 exhaust air temp | 
| 47271   | Number    | Fan return time 4 |
| 43416   | Number    | Compressor starts EB100-EP14 |
| 47272   | Number    | Fan return time 3 |
| 47273   | Number    | Fan return time 2 |
| 47274   | Number    | Fan return time 1 |
| 47275   | Number    | Filter reminder period | 
| 43420   | Number    | Total operation time compressor | 
| 47276   | Number    | Floor drying | 
| 47277   | Number    | Floor drying period 7 | 
| 47278   | Number    | Floor drying period 6 | 
| 47279   | Number    | Floor drying period 5 | 
| 45001   | Number    | Alarm number | 
| 43427   | Number    | Compressor state EP14 | 
| 43424   | Number    | Total hot water operation time compr | 
| 40081   | Number    | EB100-BE1 current phase 2 | 
| 40083   | Number    | EB100-BE1 current phase 1 | 
| 47263   | Number    | Exhaust fan speed 2 | 
| 47262   | Number    | Exhaust fan speed 3 | 
| 47261   | Number    | Exhaust fan speed 4 | 
| 40072   | Number    | BF1 Flow | 
| 40074   | Number    | EB100-FR1 anode status | 
| 40079   | Number    | EB100-BE1 current phase 3 | 
| 40067   | Number    | EM1-BT52 boiler temperature | 
| 40070   | Number    | BT25 external supply temp | 
| 40071   | Number    | BT25 external supply temp | 
| 40054   | Number    | EB100-FD1 temperature limiter | 
| 47212   | Number    | Max int addition power | 
| 47214   | Number    | Fuse | 
| 47209   | Number    | DM between addition steps | 
| 47208   | Number    | DM start addition | 
| 40033   | Number    | BT50 room temp S1 | 
| 40032   | Number    | EB21-BT50 room temp S2 | 
| 47210   | Number    | DM start addition with shunt | 
| 40046   | Number    | EQ1-BT65 PCS4 return temp | 
| 40045   | Number    | EQ1-BT64 PCS4 supply temp | 
| 47207   | Number    | DM start cooling | 
| 40044   | Number    | EP8-BT54 solar load temp | 
| 47206   | Number    | DM start heating | 
| 40043   | Number    | EP8-BT53 solar panel temp | 
| 40042   | Number    | CL11-BT51 pool 1 temp | 
| 40022   | Number    | EB100-EP14-BT17 suction | 
| 40016   | Number    | EB100-EP14-BT11 brine out temp | 
| 40017   | Number    | EB100-EP14-BT12 cond out | 
| 40018   | Number    | EB100-EP14-BT14 hot gas temp | 
| 40019   | Number    | EB100-EP14-BT15 liquid line | 
| 40028   | Number    | AZ1-BT26 temp collector in FLM 1 | 
| 40029   | Number    | AZ1-BT27 temp collector out FLM 1 | 
| 40030   | Number    | EB23-BT50 room temp S4 | 
| 40031   | Number    | EB22-BT50 room temp S3 | 
| 40025   | Number    | EB100-BT20 exhaust air temp | 
| 40026   | Number    | EB100-BT21 vented air temp | 
| 40005   | Number    | EB23-BT2 supply temp S4 | 
| 40004   | Number    | BT1 outdoor temp | 
| 40007   | Number    | EB21-BT2 supply temp S2 | 
| 40006   | Number    | EB22-BT2 supply temp S3 | 
| 40013   | Number    | BT6 hot water top | 
| 40012   | Number    | EB100-EP14-BT3 return temp | 
| 40015   | Number    | EB100-EP14-BT10 brine in temp | 
| 40014   | Number    | BT6 hot water load | 
| 40008   | Number    | BT2 supply temp S1 | 
| 43001   | Number    | Software version | 
| 43006   | Number    | Calculated supply temp S4 | 
| 43007   | Number    | Calculated supply temp S3 | 
| 43005   | Number    | Degree minutes | 
| 47136   | Number    | Period pool | 
| 47139   | Number    | Operational mode brine medium pump | 
| 47138   | Number    | Operational mode heat medium pump | 
| 47131   | Number    | Language | 
| 47134   | Number    | Period hot water | 
| 47135   | Number    | Period heat | 
| 47133   | Number    | Period pool 2 | 
| 48064   | Number    | FLM 4 speed 3 | 
| 48065   | Number    | FLM 4 speed 2 | 
| 48066   | Number    | FLM 4 speed 1 | 
| 48067   | Number    | FLM 4 speed normal | 
| 48068   | Number    | FLM 4 accessory | 
| 48069   | Number    | FLM 3 accessory | 
| 48070   | Number    | FLM 2 accessory | 
| 48071   | Number    | FLM 1 accessory | 
| 43230   | Number    | Accumulated energy | 
| 48073   | Number    | FLM cooling | 
| 48074   | Number    | Set point for BT74 | 
| 48088   | Number    | Pool 1 accesory | 
| 48090   | Number    | Pool 1 start temp | 
| 48093   | Number    | Pool 2 activated | 
| 48092   | Number    | Pool 1 stop temp | 
| 48094   | Number    | Pool 1 activated | 
| 48537   | Number    | Night cooling | 
| 48539   | Number    | Night cooling min diff | 
| 43239   | Number    | Total hot water operation time add | 
| 47570   | Number    | Operational mode | 
| 47538   | Number    | Start room temp nigh cooling | 
| 48055   | Number    | FLM 2 speed 2 | 
| 48054   | Number    | FLM 2 speed 3 | 
| 48053   | Number    | FLM 2 speed 4 | 
| 48063   | Number    | FLM 4 speed 4 | 
| 48062   | Number    | FLM 3 speed normal | 
| 48061   | Number    | FLM 3 speed 1 | 
| 48060   | Number    | FLM 3 speed 2 | 
| 48059   | Number    | FLM 3 speed 3 | 
| 48058   | Number    | FLM 3 speed 4 | 
| 48057   | Number    | FLM 2 speed normal | 
| 48056   | Number    | FLM 2 speed 1 | 
| 47005   | Number    | Heat curve S3 | 
| 43103   | Number    | HPAC state | 
| 47004   | Number    | Heat curve S4 | 
| 47007   | Number    | Heat curve S1 | 
| 47006   | Number    | Heat curve S2 | 
| 43091   | Number    | Internal electrical addition state | 
| 43086   | Number    | Prio | 
| 43084   | Number    | Internal electrical addition power | 
| 43081   | Number    | Total operation time addition | 
| 47036   | Number    | External adjust with room sensor S1 | 
| 47035   | Number    | External adjust with room sensor S2 | 
| 47034   | Number    | External adjust with room sensor S3 | 
| 47033   | Number    | External adjust with room sensor S4 | 
| 47032   | Number    | External adjustment S1 | 
| 47031   | Number    | External adjustment S2 | 
| 47030   | Number    | External adjustment S3 | 
| 47029   | Number    | External adjustment S4 | 
| 47028   | Number    | Point offset | 
| 47027   | Number    | Point offset outdoor temp | 
| 47026   | Number    | Own curve P1 | 
| 47025   | Number    | Own curve P2 | 
| 47024   | Number    | Own curve P3 | 
| 47022   | Number    | Own curve P5 | 
| 47023   | Number    | Own curve P4 | 
| 47020   | Number    | Own curve P7 | 
| 47021   | Number    | Own curve P6 | 
| 47018   | Number    | Max supply system 2 | 
| 47019   | Number    | Max supply system 1 | 
| 47016   | Number    | Max supply system 4 | 
| 47017   | Number    | Max supply system 3 | 
| 47014   | Number    | Min supply system 2 | 
| 47015   | Number    | Min supply system 1 | 
| 47012   | Number    | Min supply system 4 | 
| 47013   | Number    | Min supply system 3 | 
| 47010   | Number    | Offset S2 | 
| 47011   | Number    | Offset S1 | 
| 47008   | Number    | Offset S4 | 
| 47009   | Number    | Offset S3 | 
| 47402   | Number    | Room sensor factor S1 | 
| 47401   | Number    | Room sensor factor S2 | 
| 47400   | Number    | Room sensor factor S3 | 
| 47395   | Number    | Room sensor setpoint S4 | 
| 47394   | Number    | Use room sensor S1 | 
| 47393   | Number    | Use room sensor S2 | 
| 47392   | Number    | Use room sensor S3 | 
| 43024   | Number    | Status cooling | 
| 47399   | Number    | Room sensor factor S4 | 
| 47398   | Number    | Room sensor setpoint S1 | 
| 47397   | Number    | Room sensor setpoint S2 | 
| 47396   | Number    | Room sensor setpoint S3 | 
| 47418   | Number    | Speed brine pump | 
| 47048   | Number    | Stop temperature hot water normal | 
| 47049   | Number    | Stop temperature hot water economy | 
| 47416   | Number    | Speed circ pump economy | 
| 47050   | Number    | Periodic hot water | 
| 47417   | Number    | Speed circ pump cooling | 
| 47051   | Number    | Periodic hot water interval | 
| 43010   | Number    | Calculated cooling supply temp | 
| 47041   | Number    | Hot water mode | 
| 43008   | Number    | Calculated supply temp S2 | 
| 43009   | Number    | Calculated supply temp S1 | 
| 47043   | Number    | Start temperature hot water luxury | 
| 47414   | Number    | Speed circ pump heat | 
| 47044   | Number    | Start temperature hot water normal | 
| 47415   | Number    | Speed circ pump pool | 
| 47045   | Number    | Start temperature hot water economy | 
| 47046   | Number    | Stop temperature periodic hot water | 
| 47413   | Number    | Speed circ pump hot water | 
| 43013   | Number    | Freeze protection status | 
| 47047   | Number    | Stop temperature hot water luxury | 
| 47384   | Number    | Date format | 
| 47385   | Number    | Time format | 
| 47387   | Number    | Hot water production | 
| 47388   | Number    | Alarm lower room temp | 
| 47389   | Number    | Alarm lower HW temp | 
| 47391   | Number    | Use room sensor S4 | 
| 47378   | Number    | Max diff comp | 
| 47379   | Number    | Max diff add | 
| 47380   | Number    | Low brine out autoreset | 
| 47381   | Number    | Low brine out temp | 
| 47382   | Number    | High brine in | 
| 47383   | Number    | High brine in temp | 
| 47332   | Number    | Cooling supply temp at 20C | 
| 47333   | Number    | Cooling supply temp at 40C | 
| 47334   | Number    | Cooling close mixing valves | 
| 47335   | Number    | Time between switch heat/cooling | 
| 47329   | Number    | Cooling 2-pipe accessory | 
| 47330   | Number    | Cooling 4-pipe accessory | 
| 47331   | Number    | Min cooling supply temp | 
| 47340   | Number    | Cooling with room sensor | 
| 47336   | Number    | Heat at room under temp | 
| 47337   | Number    | Cool at room over temp | 
| 47338   | Number    | Cooling mixing valve amp | 
| 47339   | Number    | Cooling mixing valve step delay | 
| 47319   | Number    | Shunt controlled add min runtime | 
| 47318   | Number    | Shunt controlled add min temp | 
| 47317   | Number    | Shunt controlled addition accessory | 
| 40152   | Number    | BT71 ext return temp | 
| 47313   | Number    | FLM defrost | 
| 47312   | Number    | FLM pump | 
| 47327   | Number    | Ground water pump accessory | 
| 47326   | Number    | Step controlled add mode | 
| 47324   | Number    | Step controlled add diff DM | 
| 47323   | Number    | Step controlled add start DM | 
| 47322   | Number    | Step controlled add accessory | 
| 47321   | Number    | Shunt controlled add mix valve wait | 
| 47320   | Number    | Shunt controlled add mix valve amp | 
| 47302   | Number    | Climate system 2 accessory | 
| 47303   | Number    | Climate system 3 accessory | 
| 47310   | Number    | Climate system 2 shunt wait | 
| 43516   | Number    | PCA-Power relayes EP14 | 
| 47308   | Number    | Climate system 4 shunt wait | 
| 47309   | Number    | Climate system 3 shunt wait | 
| 43514   | Number    | PCA-Base relayes EP14 | 
| 40128   | Number    | EB22-BT3 return temp S3 | 
| 47306   | Number    | Climate system 3 mixing valve amp | 
| 40129   | Number    | EB21-BT3 return temp S2 | 
| 47307   | Number    | Climate system 2 mixing valve amp | 
| 47304   | Number    | Climate system 4 accessory | 
| 47305   | Number    | Climate system 4 mixing valve amp | 
| 47281   | Number    | Floor drying period 3 | 
| 47280   | Number    | Floor drying period 4 | 
| 43395   | Number    | HPAC relays | 
| 47283   | Number    | Floor drying period 1 | 
| 47282   | Number    | Floor drying period 2 | 
| 40127   | Number    | EB23-BT3 return temp S4 | 
| 47285   | Number    | Floor drying temp 6 | 
| 47284   | Number    | Floor drying temp 7 | 
| 47287   | Number    | Floor drying temp 4 | 
| 47286   | Number    | Floor drying temp 5 | 
| 40115   | Number    | AZ1-BT26 temp collector in FLM 2 | 
| 47289   | Number    | Floor drying temp 2 | 
| 40114   | Number    | AZ1-BT26 temp collector in FLM 3 | 
| 47288   | Number    | Floor drying temp 3 | 
| 40113   | Number    | AZ1-BT26 temp collector in FLM 4 | 
| 47291   | Number    | Floor drying timer | 
| 40112   | Number    | EB100-BT21 vented air temp | 
| 47290   | Number    | Floor drying temp 1 | 
| 40118   | Number    | AZ1-BT27 temp collector out FLM 2 | 
| 40117   | Number    | AZ1-BT27 temp collector out FLM 3 | 
| 40116   | Number    | AZ1-BT27 temp collector out FLM 4 | 
| 47264   | Number    | Exhaust fan speed 1 | 
| 40107   | Number    | EB100-BT20 exhaust air temp | 
| 47265   | Number    | Exhaust fan speed normal | 
| 40110   | Number    | EB100-BT21 vented air temp | 
| 40111   | Number    | EB100-BT21 vented air temp | 
| 40108   | Number    | EB100-BT20 exhaust air temp | 
| 40109   | Number    | EB100-BT20 exhaust air temp | 
| 47271   | Number    | Fan return time 4 | 
| 43416   | Number    | Compressor starts EB100-EP14 | 
| 47272   | Number    | Fan return time 3 | 
| 47273   | Number    | Fan return time 2 | 
| 47274   | Number    | Fan return time 1 | 
| 47275   | Number    | Filter reminder period | 
| 43420   | Number    | Total operation time compressor | 
| 47276   | Number    | Floor drying | 
| 47277   | Number    | Floor drying period 7 | 
| 47278   | Number    | Floor drying period 6 | 
| 47279   | Number    | Floor drying period 5 | 
| 45001   | Number    | Alarm number | 
| 43427   | Number    | Compressor state EP14 | 
| 43424   | Number    | Total hot water operation time compr | 
| 40081   | Number    | EB100-BE1 current phase 2 | 
| 40083   | Number    | EB100-BE1 current phase 1 | 
| 47263   | Number    | Exhaust fan speed 2 | 
| 47262   | Number    | Exhaust fan speed 3 | 
| 47261   | Number    | Exhaust fan speed 4 | 
| 40072   | Number    | BF1 Flow | 
| 40074   | Number    | EB100-FR1 anode status | 
| 40079   | Number    | EB100-BE1 current phase 3 | 
| 40067   | Number    | EM1-BT52 boiler temperature | 
| 40070   | Number    | BT25 external supply temp | 
| 40071   | Number    | BT25 external supply temp | 
| 40054   | Number    | EB100-FD1 temperature limiter | 
| 47212   | Number    | Max int addition power | 
| 47214   | Number    | Fuse | 
| 47209   | Number    | DM between addition steps | 
| 47208   | Number    | DM start addition | 
| 40033   | Number    | BT50 room temp S1 | 
| 40032   | Number    | EB21-BT50 room temp S2 | 
| 47210   | Number    | DM start addition with shunt | 
| 40046   | Number    | EQ1-BT65 PCS4 return temp | 
| 40045   | Number    | EQ1-BT64 PCS4 supply temp | 
| 47207   | Number    | DM start cooling | 
| 40044   | Number    | EP8-BT54 solar load temp | 
| 47206   | Number    | DM start heating | 
| 40043   | Number    | EP8-BT53 solar panel temp | 
| 40042   | Number    | CL11-BT51 pool 1 temp | 
| 40022   | Number    | EB100-EP14-BT17 suction | 
| 40016   | Number    | EB100-EP14-BT11 brine out temp | 
| 40017   | Number    | EB100-EP14-BT12 cond out | 
| 40018   | Number    | EB100-EP14-BT14 hot gas temp | 
| 40019   | Number    | EB100-EP14-BT15 liquid line | 
| 40028   | Number    | AZ1-BT26 temp collector in FLM 1 | 
| 40029   | Number    | AZ1-BT27 temp collector out FLM 1 | 
| 40030   | Number    | EB23-BT50 room temp S4 | 
| 40031   | Number    | EB22-BT50 room temp S3 | 
| 40025   | Number    | EB100-BT20 exhaust air temp | 
| 40026   | Number    | EB100-BT21 vented air temp | 
| 40005   | Number    | EB23-BT2 supply temp S4 | 
| 40004   | Number    | BT1 outdoor temp | 
| 40007   | Number    | EB21-BT2 supply temp S2 | 
| 40006   | Number    | EB22-BT2 supply temp S3 | 
| 40013   | Number    | BT6 hot water top | 
| 40012   | Number    | EB100-EP14-BT3 return temp | 
| 40015   | Number    | EB100-EP14-BT10 brine in temp | 
| 40014   | Number    | BT6 hot water load | 
| 40008   | Number    | BT2 supply temp S1 | 
| 43001   | Number    | Software version | 
| 43006   | Number    | Calculated supply temp S4 | 
| 43007   | Number    | Calculated supply temp S3 | 
| 43005   | Number    | Degree minutes | 
| 47136   | Number    | Period pool | 
| 47139   | Number    | Operational mode brine medium pump | 
| 47138   | Number    | Operational mode heat medium pump | 
| 47131   | Number    | Language | 
| 47134   | Number    | Period hot water | 
| 47135   | Number    | Period heat | 
| 47133   | Number    | Period pool 2 | 
| 48064   | Number    | FLM 4 speed 3 | 
| 48065   | Number    | FLM 4 speed 2 | 
| 48066   | Number    | FLM 4 speed 1 | 
| 48067   | Number    | FLM 4 speed normal | 
| 48068   | Number    | FLM 4 accessory | 
| 48069   | Number    | FLM 3 accessory | 
| 48070   | Number    | FLM 2 accessory | 
| 48071   | Number    | FLM 1 accessory | 
| 43230   | Number    | Accumulated energy | 
| 48073   | Number    | FLM cooling | 
| 48074   | Number    | Set point for BT74 | 
| 48088   | Number    | Pool 1 accesory | 
| 48090   | Number    | Pool 1 start temp | 
| 48093   | Number    | Pool 2 activated | 
| 48092   | Number    | Pool 1 stop temp | 
| 48094   | Number    | Pool 1 activated | 
| 48537   | Number    | Night cooling | 
| 48539   | Number    | Night cooling min diff | 
| 43239   | Number    | Total hot water operation time add | 
| 47570   | Number    | Operational mode | 
| 47538   | Number    | Start room temp nigh cooling | 
| 48055   | Number    | FLM 2 speed 2 | 
| 48054   | Number    | FLM 2 speed 3 | 
| 48053   | Number    | FLM 2 speed 4 | 
| 48063   | Number    | FLM 4 speed 4 | 
| 48062   | Number    | FLM 3 speed normal | 
| 48061   | Number    | FLM 3 speed 1 | 
| 48060   | Number    | FLM 3 speed 2 | 
| 48059   | Number    | FLM 3 speed 3 | 
| 48058   | Number    | FLM 3 speed 4 | 
| 48057   | Number    | FLM 2 speed normal | 
| 48056   | Number    | FLM 2 speed 1 | 
| 47005   | Number    | Heat curve S3 | 
| 43103   | Number    | HPAC state | 
| 47004   | Number    | Heat curve S4 | 
| 47007   | Number    | Heat curve S1 | 
| 47006   | Number    | Heat curve S2 | 
| 43091   | Number    | Internal electrical addition state | 
| 43086   | Number    | Prio | 
| 43084   | Number    | Internal electrical addition power | 
| 43081   | Number    | Total operation time addition | 
| 47036   | Number    | External adjust with room sensor S1 | 
| 47035   | Number    | External adjust with room sensor S2 | 
| 47034   | Number    | External adjust with room sensor S3 | 
| 47033   | Number    | External adjust with room sensor S4 | 
| 47032   | Number    | External adjustment S1 | 
| 47031   | Number    | External adjustment S2 | 
| 47030   | Number    | External adjustment S3 | 
| 47029   | Number    | External adjustment S4 | 
| 47028   | Number    | Point offset | 
| 47027   | Number    | Point offset outdoor temp | 
| 47026   | Number    | Own curve P1 | 
| 47025   | Number    | Own curve P2 | 
| 47024   | Number    | Own curve P3 | 
| 47022   | Number    | Own curve P5 | 
| 47023   | Number    | Own curve P4 | 
| 47020   | Number    | Own curve P7 | 
| 47021   | Number    | Own curve P6 | 
| 47018   | Number    | Max supply system 2 | 
| 47019   | Number    | Max supply system 1 | 
| 47016   | Number    | Max supply system 4 | 
| 47017   | Number    | Max supply system 3 | 
| 47014   | Number    | Min supply system 2 | 
| 47015   | Number    | Min supply system 1 | 
| 47012   | Number    | Min supply system 4 | 
| 47013   | Number    | Min supply system 3 | 
| 47010   | Number    | Offset S2 | 
| 47011   | Number    | Offset S1 | 
| 47008   | Number    | Offset S4 | 
| 47009   | Number    | Offset S3 | 
| 47402   | Number    | Room sensor factor S1 | 
| 47401   | Number    | Room sensor factor S2 | 
| 47400   | Number    | Room sensor factor S3 | 
| 47395   | Number    | Room sensor setpoint S4 | 
| 47394   | Number    | Use room sensor S1 | 
| 47393   | Number    | Use room sensor S2 | 
| 47392   | Number    | Use room sensor S3 | 
| 43024   | Number    | Status cooling | 
| 47399   | Number    | Room sensor factor S4 | 
| 47398   | Number    | Room sensor setpoint S1 | 
| 47397   | Number    | Room sensor setpoint S2 |
| 47396   | Number    | Room sensor setpoint S3 |
| 47418   | Number    | Speed brine pump |
| 47048   | Number    | Stop temperature hot water normal |
| 47049   | Number    | Stop temperature hot water economy |
| 47416   | Number    | Speed circ pump economy |
| 47050   | Number    | Periodic hot water |
| 47417   | Number    | Speed circ pump cooling |
| 47051   | Number    | Periodic hot water interval |
| 43010   | Number    | Calculated cooling supply temp |
| 47041   | Number    | Hot water mode |  | 
| 43008   | Number    | Calculated supply temp S2 |
| 43009   | Number    | Calculated supply temp S1 |
| 47043   | Number    | Start temperature hot water luxury |
| 47414   | Number    | Speed circ pump heat |
| 47044   | Number    | Start temperature hot water normal |
| 47415   | Number    | Speed circ pump pool |
| 47045   | Number    | Start temperature hot water economy |
| 47046   | Number    | Stop temperature periodic hot water |
| 47413   | Number    | Speed circ pump hot water |
| 43013   | Number    | Freeze protection status |
| 47047   | Number    | Stop temperature hot water luxury |
| 47384   | Number    | Date format |
| 47385   | Number    | Time format |
| 47387   | Number    | Hot water production |
| 47388   | Number    | Alarm lower room temp |
| 47389   | Number    | Alarm lower HW temp | 
| 47391   | Number    | Use room sensor S4 | 
| 47378   | Number    | Max diff comp |
| 47379   | Number    | Max diff add | 
| 47380   | Number    | Low brine out autoreset |
| 47381   | Number    | Low brine out temp |
| 47382   | Number    | High brine in |
| 47383   | Number    | High brine in temp |

## Examples

```
Number	DegreeMinutes	{ nibeheatpump="43005" }
Number	BT1_OutdoorTemp	{ nibeheatpump="40004" }
Number	BT2_SupplyTemp	{ nibeheatpump="40008" }
```
