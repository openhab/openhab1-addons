# Mystrom Eco Power Binding

The openHAB Mystrom binding allows you to send commands to Mystrom Eco Power Switches, receive consumption numbers and states of devices.

## Prerequisites

You must have an account at the Mystrom website.

## Binding Configuration

The binding needs user and password of mystrom site to be added to the file `services/mystromecopower.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| userName | | Yes | your user name for the Mystrom website |
| password | | Yes | your password for the Mystrom website |

## Item Binding Configuration

The syntax of the binding configuration strings accepted is the following:

```
mystromecopower="<friendlyName>"
```

The friendlyName is given to your device on the Mystrom site.

If not sure about the `<friendlyName>`s of your devices, take a look in your openhab.log. The discovered devices are listed.

| Item Type | Description |
|-----------|-------------|
| Switch | used to get state on/off of the device, accepted commands are `ON` or `OFF`. For master device, if you send the `OFF` command it will ll restart the master. |
| Number | used to receive consumption in Watts of the device. Please note that the binding does not accept DecimalType commands. |
| String | used to receive state on/off/offline |


## Examples

How to configure your items in your items file, you can configure as Switch, Number or String.  

```
Switch WallFanOffice_Switch 	{mystromecopower="lightBathroom"}
Number WallFanOffice_Number 	{mystromecopower="lightBathroom"}
String WallFanOffice_String 	{mystromecopower="lightBathroom"}					
```
