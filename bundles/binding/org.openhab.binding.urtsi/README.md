Documentation of the Somfy URTSI II binding Bundle

## Introduction

The **Somfy URTSI II binding bundle** is available as a separate (optional) download. If you want to communicate with a Somfy URTSI II device, please place this bundle in the folder ${openhab_home}/addons and add binding information to your configuration. See the following sections on how to do this.

## Binding Configuration

First of all you need to introduce your Somfy URTSI II devices in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    ######################## Somfy URTSI II Binding ############################################
    
    # Host and port of the first device to control
    # urtsi:<deviceId1>.port=
    
    # Host and port of the second device to control
    # urtsi:<deviceId2>.port=
    

The `urtsi:<deviceId>.port` value is the identification of the serial port on the host system, e.g. "COM1" on Windows, "/dev/ttyS0" on Linux or "/dev/tty.PL2303-0000103D" on Mac.


## Item Binding Configuration

In order to bind an item to an URTSI II device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`).

The format of the binding configuration is simple and looks like this:

    urtsi="\<deviceId\>:\<channelId\>"

The **deviceId** corresponds device which is introduced in openhab.cfg.

The **channelId** is the configured RTS channel you want the item to bind to. Each URTSI device supports up to 16 channels (1 - 16).

Only rollershutter items are allowed to use this binding. The binding is able to handle UP, DOWN and MOVE commands.

As a result, your lines in the items file might look like the following:

    Rollershutter RollershutterKitchen       "Kitchen"         { urtsi="device1:1" }
    Rollershutter RollershutterLivingRoom    "Living room"     { urtsi="device1:2" }