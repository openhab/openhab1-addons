Documentation of the Wemo binding bundle

## Introduction

The openHAB Wemo binding allows to send commands Belkin Wemo Switches.

For installation of the binding, please see Wiki page [[Bindings]].

####This Binding will be available with openHAB version 1.6 !!!

Wemo Binding needs no configuration in openhab.cfg


## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    wemo="<friendlyName>"

The friendlyName is given to your device during initial setup with your Android or IOS device.
If not shure about the friendlyNames of your devices, take a look in your openhab.log. The discovered devices are listed.

Examples, how to configure your items in your items file:

    Switch WallFanOffice 	{wemo="WemoFanOffice"}					#Item Name and friendlyName must not be identical!

