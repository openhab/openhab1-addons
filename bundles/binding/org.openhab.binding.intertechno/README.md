# Intertechno

You can send intertechno in every mode. I.e. if you are using the CUL in slow RF mode or BidCos mode you can still send intetechno commands. The firmware will take care of switching to intertechno mode, sending the command and switching back.

## Installation and Configuration
See [[CUL Transport]] for general configuration options (such as serial device parameters).

In the openhab.cfg you simply need to specify the device to use. For example

> culintertechno:device=serial:/dev/ttyACM0

## Item configuration
The item configuration depends on your specific intertechno device. Unfortunately there is no single manufacturer for these devices but many of them using intertechno a little differently.
OpenHAB has currently support for different intertechno device types, but probably not all. For more detailed information please have a look [here](http://www.fhemwiki.de/wiki/Intertechno_Code_Berechnung).
Currently OpenHab can handle FLS, Rev, Classic and "raw" devices.
For all supported you simply have to read the position of the switches and use the read values as group and address.

FLS example

> {culintertechno="type=fls;group=I;address=1"}

REV exmaple

> {culintertechno="type=rev;group=I;address=1"}

Classic example

> {culintertechno="type=classic;group=I;address=1"}

If you have an unsupported intertechno device you can fallback to the raw mode

> {culintertechno="type=raw;address=FF00FF00;commandOn=FF;commandOff=F0"}

This configuration allows you to manual specify the base address and the appended on and off command.