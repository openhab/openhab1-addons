### Sallegra Binding ###


## Introduction

- Sallegra Dimmer DA-ET-4, Sallegra Relay R-ET-4 and Sallegra Input ADI-ET-8/8 are currently supported
- Syncs the Openhab status with the actual status on the Sallegra Module

### Requirements

A supported Sallegra device with the corresponding IP address, password and access to it via HTTP/Port 80.

## Sallegra Binding Configuration

### openhab.cfg

These config parameters are used for the Sallegra binding. Values with a leading $ are meant to be replaced with the value in the actual setup.
```
############################### Sallegra Binding #######################################
#
# sallegra:$modulename.hostname=$hostname
sallegra:livingroom.hostname=192.168.0.52
# sallegra:$modulename.password=$password
sallegra:livingroom.password=admin

# sallegra:$modulename.hostname=$hostname
sallegra:bedroom.hostname=192.168.0.54
# sallegra:$modulename.password=$password
sallegra:bedroom.password=admin
```

### Item Configuration

You can use the following parameters in the item configuration to control a Sallegra module with the Openhab binding.

```
Switch Light_Livingroom   "Light_Livingroom"  { sallegra="livingroom:relay:1" }
String Input1             "Input1 [%s]"       { sallegra="inputmodule:input:DI8" }
Dimmer Light_Bedroom      "Dimmer 1 [%d %%]"  { sallegra="bedroom:dimmer:1" }
```

```
{ sallegra="$modulename:$modulespecific:$port" }
```

**$modulename**
Defines the name of the module. This is the name you earlier set for the module in the Openhab.cfg.

**$port**
This defines the Hardware Port on the Sallegra module. On dimmer and relay modules this is exactly 1:1 the relay/dimmer port number you can find on the Device Sign. On the Input module you have to use the complete string of the input. You can find them in the xml file of the input module.

**$modulespecific**
This is a specific value to kind of specifiy the module. You should use the following strings according to the device:

R-ET-4 -> **relay**

ADI-ET-8/8 -> **input**

DA-ET-4 -> **dimmer**