Documentation of the Wemo binding bundle

## Introduction

This binding integrates the [Belkin WeMo Family](http://www.belkin.com/us/Products/c/home-automation/).
The integration happens either through the WeMo-Link bridge (feature still to come), which acts as an IP gateway to the ZigBee devices or through WiFi connection to standalone devices.

For installation of the binding, please see Wiki page [[Bindings]].

####The new channels for energy measurement of WeMo Insight devices will be available with openHAB Version 1.8 !!!

Wemo Binding needs no configuration in openhab.cfg


## Item Binding Configuration (Version 1.7)

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    wemo="<friendlyName>"

The friendlyName is given to your device during initial setup with your Android or IOS device.
If not shure about the friendlyNames of your devices, take a look in your openhab.log. The discovered devices are listed.

Examples, how to configure your items in your items file:

    Switch WallFanOffice   {wemo="WemoFanOffice"}        #Item Name and friendlyName must not be identical!


## Item Binding Configuration (Version 1.8)

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    wemo="<UDN>;[<channel-type>]"


If you are not shure about your WeMo devices UDN, have a look into openHAB logfile. The Binding lists all discovered WeMo devices at start.

The channel type of your item definition is **optional**, it will default to channel type "**state**".
The following channel types are possible for Insight switch devices:

    state, currentPower, onToday, onTotal

Examples, how to configure your items in your items file:

    Switch Socket1               {wemo="Socket-1-0-12345678"}
	Switch Insight1              {wemo="Insight-1-0-87654321"}
	Number Insight_currentPower  {wemo="Insight-1-0-87654321;currentPower"}
	Number Insight_onToday       {wemo="Insight-1-0-87654321;onToday"}
	Number Insight_onTotal       {wemo="Insight-1-0-87654321;onTotal"}
	Contact Motion1              {wemo="Sensor-1-0-56437891"}

