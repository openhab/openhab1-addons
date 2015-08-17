Documentation of the Mystrom Eco Power binding bundle

## Introduction

The openHAB Mystrom binding allows to send commands Mystrom Eco Power Switches, 
receive consumption numbers and states of devices.

For installation of the binding, please see Wiki page [[Bindings]].

####This Binding will be available with openHAB version 1.7 !!!

Mystrom Binding needs user and password of mystrom site in openhab.cfg, add the follwing lines into the config file:

	mystromecopower:userName=<b>youremail</b>
	mystromecopower:password=<b>yourpassword</b>

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    mystromecopower="<friendlyName>"

The friendlyName is given to your device on Mystrom site.
If not sure about the friendlyNames of your devices, take a look in your openhab.log. The discovered devices are listed.

Examples, how to configure your items in your items file, you can configure as Switch, Number or String, 
String is usefull for scripting because you can receive the state on, off or offline:

    Switch WallFanOffice_Switch 	{mystromecopower="lightBathroom"}
    Number WallFanOffice_Number 	{mystromecopower="lightBathroom"}
    String WallFanOffice_String 	{mystromecopower="lightBathroom"}					

*Switch: used to get state on/off of the device, accepted commands are <b>on</b>,<b>off</b>. For master device, if you send <b>off</b> command it'll restart the master.
*Number: used to receive consumption in Watt of the device, doesn't support command.
*String: used to receive state on/off/offline.

## Logs
Add the log into the logback.xml file, the logger name is org.openhab.binding.mystromecopower:
for INFO level you can set:

	&lt;logger name=&quot;org.openhab.binding.mystromecopower&quot; level=&quot;INFO&quot;&gt;
			&lt;appender-ref ref=&quot;STDOUT&quot; /&gt;
	&lt;/logger&gt;
