Documentation of the PLCBus binding Bundle

## Introduction

Currently only "One phase mode" of the PLCBus protocol is supported.

For installation of the binding, please see Wiki page [[Bindings]].

## openhab.cfg

At first it's required to set your serialport device in the main config file 'configurations/openhab.cfg':

    plcbus:port=/dev/ttyUSB0
or

    plcbus:port=COM1

## Generic Item Binding Configuration

In order to bind an item to a PLCBus device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax for the PLCBus binding configuration string is explained here:

The format of the binding configuration is simple and looks like this:

    plcbus="<usercode> <unit> [seconds]"

## Examples

Here are some examples of valid binding configuration strings, as defined in the items configuration file:

    Switch Switch1	          "Switch1"          <plcbus> { plcbus="D1 A1"}
    Dimmer Dimmer1	          "Dimmer1"          <plcbus> { plcbus="D1 A2 5"}
    Rollershutter Rollershutter1  "Rollershutter 1"  <plcbus> { plcbus="D1 A3"}