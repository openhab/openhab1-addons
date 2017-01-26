# Mochad X10 Binding

This binding makes it possible to control X10 devices via a server running the [Mochad X10 daemon by mmauka](http://sourceforge.net/projects/mochad/). Mochad is a Linux TCP gateway daemon for the X10 CM15A RF (radio frequency) and PL (power line) controller and the CM19A RF controller. 

With the current version of the binding items of type Switch, Dimmer, and Rollershutter can be controlled. The binding only uses one-way communication so no status reading (yet).

## Binding Configuration

This binding can be configured in the file `services/mochadx10.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| hostIp   |         |   Yes    | the IP address of the server on which the Mochad X10 daemon is running. |
| hostPort | 1099    |   No     | the port number on which the Mochad X10 daemon is communicating. |

## Item configuration

The format of the binding configuration is simple and looks like this:

```
mochadx10="<houseCode><unitCode>[:<transmitMethod>][:<dimMethod>]"
```

where parts in `[brackets]` indicate optional components. 

Addressing of X10 devices is done using a so called house code and a unit code. The house code is specified by characters 'a' to 'p'. The unit code is a number from 1 to 16. This way a number of 255 X10 devices can be controlled by one X10 controller. 

X10 devices can be controlled by sending commands over the power-line or by sending RF commands. To explicitly specify the `transmitMethod` a value of `pl` for power-line or `rf` for radio frequency can be used. If `transmitMethod` is not specified it defaults to `pl`.

X10 dimmer devices can use one the `dim/bright` commands or the `xdim` command to set the level of lighting. To explicitly specify the `dimMethod` of an X10 device, set it to `dim` for using the 'dim/bright' commands, or to `xdim` for using the `xdim` command. If `dimMethod` is not specified it defaults to `xdim`

## Examples

Here are some examples of valid binding configuration strings, as defined in the items configuration file:

```
Dimmer Light_Corridor_Dimmer "Hallway Dimmer [%d %%]" (GF_Corridor) {mochadx10="a1"}
Dimmer Light_Living_Dimmer "Living Dimmer [%d %%]" (GF_Living) {mochadx10="a2:pl:dim"}
Switch Mech_Vent "Mechanical ventilation" (GF_Kitchen) {mochadx10="b12"}
Rollershutter "Bedroom" (FF_Bedroom) {mochadx10="m3:rf"}
```
