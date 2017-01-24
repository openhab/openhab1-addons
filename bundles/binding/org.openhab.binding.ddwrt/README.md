_**Note:** This Binding is available in 1.9.0 and later releases._

Documentation of the DD-WRT binding bundle

## Introduction

For installation of the binding, please see Wiki page [[Bindings]], or you can add [this JAR](https://openhab.ci.cloudbees.com/job/openHAB1-Addons/lastSuccessfulBuild/artifact/bundles/binding/org.openhab.binding.ddwrt/target/org.openhab.binding.ddwrt-1.9.0-SNAPSHOT.jar) to your `addons` folder.

Adapt your openhab.cfg to your configuration:
* IP address of DD-WRT to connect to<BR>
    ddwrt:ip=192.168.1.1<BR>
    ddwrt:port=23<BR>

* You need to configure the user and password of your DD-WRT<BR>
    ddwrt:username=root<BR>
    ddwrt:password=xxxxxxx<BR>

* Interface for the 2.4 GHz wifi<BR>
    ddwrt:interface_24=ath0<BR>
* Interface for the 5 GHz wifi<BR>
    ddwrt:interface_50=ath1<BR>
* Virtuall-Interface for the guest wifi<BR>
    ddwrt:interface_guest=ath0.1<BR>


## Prepare your DD-WRT device
* You have to activate the telnet connection in the DD-WRT web interface.
* The changing of the telnet port in the DD-WRT web interface is not always working. Test it with a telnet command shell.

## Generic Item Binding Configuration

In order to bind an item to the DD-WRT device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder `configurations/items`). 

## Switching WIFI

The following items switch WIFI, GUEST_WIFI, and the NAME of the device as string:

    String DEVICE_NAME {ddwrt="routertype"}
    Switch WIFI_24     {ddwrt="wlan24"}
    Switch WIFI_50     {ddwrt="wlan50"}
    Switch WIFI_GUEST  {ddwrt="wlanguest"}

The guest network is usually a virtual network device. There is a bug in the DD-WRT firmware. The activation of this interface needs a workaround so it takes some seconds more as the native devices.

Tested with Archer V2 and DD-WRT v3.0-r30880 std (11/14/16)
