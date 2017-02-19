# Sallegra Binding

The Sallegra binding supports the Sallegra Dimmer DA-ET-4, Sallegra Relay R-ET-4 and Sallegra Input ADI-ET-8/8.

This binding syncs the openHAB status with the actual status on the Sallegra Module.

## Prerequisites

A supported Sallegra device with the corresponding IP address, password and access to it via HTTP/Port 80.

## Binding Configuration

This binding can be configured in the file `services/sallegra.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<modulename>`.hostname | | Yes | hostname or IP address of the Sallegra module |
| `<modulename>`.password | | Yes | password of the Sallegra module |

where `<modulename>` is a simple name you choose, like `bedroom`, to identify the specific module in items.

## Item Configuration

The syntax is:

```
sallegra="<modulename>:<modulespecific>:<port>"
```

where:

* `<modulename>` defines the name of the module. This is the name you earlier set for the module in the binding configuration.
* `<port>` defines the hardware port on the Sallegra module.  On dimmer and relay modules, this is exactly 1:1 the relay/dimmer port number you can find on the Device Sign. On the input module you have to use the complete string of the input. You can find them in the xml file of the input module.
* `<modulespecific>` is a specific value to kind of specifiy the module. You should use the following strings according to the device:

| Module     | `<modulespecific>` |
|------------|--------------------|
| R-ET-4     | **relay** |
| ADI-ET-8/8 | **input** |
| DA-ET-4    | **dimmer** |


## Examples

You can use the following parameters in the item configuration to control a Sallegra module with the Openhab binding.

```
Switch Light_Livingroom   "Light_Livingroom"  { sallegra="livingroom:relay:1" }
String Input1             "Input1 [%s]"       { sallegra="inputmodule:input:DI8" }
Dimmer Light_Bedroom      "Dimmer 1 [%d %%]"  { sallegra="bedroom:dimmer:1" }
```
