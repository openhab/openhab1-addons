Documentation of the Energenie binding bundle

## Introduction

The openHAB Energenie binding allows to send commands to multiple Gembird energenie PMS-LAN power extenders.

For installation of the binding, please see Wiki page [[Bindings]].

####This Binding will be available with openHAB version 1.6 !!!

## Energenie Binding Configuration in openhab.cfg

First of all you need to introduce your Energenie PMS-LAN(s) in the openhab.cfg file (in the folder '${openhab_home}/configurations')

### Example

    ################################ Energenie Binding #################################
    #
    # Host of the first PMS-LAN to control 
    # energenie:<pmsId1>.host=
    # Password to login to the first PMS-LAN
    # energenie:<pmsId1>.password=
    #
    # Host of the second PMS-LAN to control 
    # energenie:<pmsId2>.host=
    # Password to login to the second PMS-LAN
    # energenie:<pmsId2>.password=
    ################################ Energenie Binding #################################

The `energenie:<pmsId1>.host` value is the ip address of your PMS-LAN.

The `energenie:<pmsId1>.password` value is the password you configured on the web interface of your PMS-LAN (default password is "1".

Examples, how to configure your receiver device:

    energenie:pms1.host=192.168.1.100
    energenie:bridge1.password=1	(default)

## Item Binding Configuration

In order to bind an item to the device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    energenie="<deviceId>;<socketID>"

The device-id corresponds to the PMS-LAN which is defined in openhab.cfg.

The socketID corresponds to the number of your socket yout want to control (1-4).

Examples, how to configure your items in your items file:

    Switch Light_OfficeDesk 	{energenie="pms1;1"}					#Switch for socket 1 on bridge1

