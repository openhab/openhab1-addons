**NOTE: If your are getting a log message saying something like 'javax.net.ssl.SSLHandshakeException', you will need a newer version of the binding than 1.8.1 and check out the description on how to solve this; [2863] (https://github.com/openhab/openhab/issues/2863#issuecomment-192738255)**

Documentation of the Samsung Smart Air Conditioner Binding. This Binding should be compatible with Samsung Smart Air Conditioner models

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration

First of all you need to introduce your Samsung AC's in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    ######################## SAMSUNG AC Binding ############################################

    # Host and port of the first AC to control
    # samsungac:<ACId1>.host=192.168.1.2
    # samsungac:<ACId1>.mac=7825AD1243BC
    # samsungac:<ACId1>.token=33965901-4482-M849-N716-373832354144
    # samsungac:<ACId1>.certificate=cacert.pem
    # samsungac:<ACId1>.password=something
    
    # Host and port of the second AC to control
    # samsungac:<ACId2>.host=192.168.1.3
    # samsungac:<ACId2>.mac=7825AD1243BC
    # samsungac:<ACId2>.token=22332423-4482-M849-N716-373832354144

`<ACId1>` and `<ACId2>` can be freely set to whatever name you want to give for a certain Air Conditioner (e.g. "Livingroom" or "Bedroom"), this could be looking like:

    samsungac:Livingroom

A working example for such configuration could look like:

    ######################## SAMSUNG TV Binding ############################################
    
    # Host and port of the first TV to control
    # samsungac:Livingroom.host=192.168.1.2
    # samsungac:Livingroom.mac=7825AD1243BC
    # samsungac:Livingroom.token=33965901-4482-M849-N716-373832354144
    # samsungac:Livingroom.password=something
    # samsungac:Livingroom.certificate=/tmp/cert/cacert.pem

If you do not know your Samsung Air Conditioners IP- or Mac-address you can download the jar, and run the following command. Then the details of the FIRST discovered air conditioner will be printed:

    java -cp addons/org.openhab.binding.samsungac_1.6.0.SNAPSHOT.jar org.binding.openhab.samsungac.communicator.SsdpDiscovery

When you have entered the host(IP) and mac details in the configuration, you will still need to retrieve a token from the air conditioner.
During first connection attempt, you do not have a token, so pay attention to the log. It will ask you to switch on and off the air conditioner within 30 seconds. You will then receive a token printed in the log. Keep it and put it in openhab.cfg.

Procedure:  

1. Turn AC on. Comment the line containing token like:

    ```
    # samsungac:Livingroom.token=
    ```
1. Start openHAB in debug mode with:  

    ```
    ./start_debug.sh
    ```
1. Watch the logs from a (different) console and look at AirConditioner messages:  

    ```
    tail -f openHAB.log | grep AirConditioner
    ```
1. You will find the messsage:  

    ```
    WARN  o.b.o.s.c.AirConditioner[:179]- NO TOKEN SET! Please switch off and on the air conditioner within 30 seconds  
    ```
1. Switch AC off and then on (be quick, you really have 20 seconds), you will then find your token in the logfile:                                      

    ```
    WARN  o.b.o.s.c.AirConditioner[:175]- Received TOKEN from AC: 'e56a6def-1ecf-47d4-bc5d-9c818eaec76d'  
    ```
1. Copy the token to openhab.cfg and uncomment the line  

## Item Binding Configuration

In order to bind an item to the device, you need to provide some configuration settings. The way to do so is to add binding information in your item file (in the folder configurations/items`). The syntax of the binding configuration strings accepted is the following:

    samsungac="[<device_id>|<device-command]"

The **device-id** corresponds device which is introduced in openhab.cfg.

The **device-command** corresponds the air conditioner command. See a list of the implemented ones below.

Examples, how to configure your items:

    Number ac_current_temp "Current temp [%.1f]" (AC) {samsungac="[Livingroom|AC_FUN_TEMPNOW]"}
    Switch ac_power (AC) {samsungac="[Livingroom|AC_FUN_POWER]"}
    Number ac_mode "Convenience mode" (AC) {samsungac="[Livingroom|AC_FUN_COMODE]"}
    Number ac_op_mode "Operation mode" (AC) {samsungac="[Livingroom|AC_FUN_OPMODE]"}
    Number ac_set_temp "Set temp [%.1f]" (AC) {samsungac="[Livingroom|AC_FUN_TEMPSET]"}
    Number ac_direction "Direction" (AC) {samsungac="[Livingroom|AC_FUN_DIRECTION]"}
    Number ac_windlevel "Windlevel" (AC) {samsungac="[Livingroom|AC_FUN_WINDLEVEL]"}

It is important that you specify these correctly in the sitemap, as each command has some special valid values. Here's an example of the sitemap:

    Frame label="Air conditioner"{
        Text item=ac_current_temp icon="temperature" label="Current temp [%.1f °C]"
        Setpoint item=ac_set_temp minValue=16 maxValue=28 step=1 icon="temperature"
        Switch item=ac_power icon="heating"
        Switch item=ac_mode label="Mode" icon="sofa" mappings=[0="Off", 1="Quiet", 2="Sleep", 3="Smart", 4="SoftCool", 5="TurboMode", 6="WindMode1", 7="WindMode2", 8="WindMode3"]
        Switch item=ac_op_mode icon="sofa" mappings=[0="Auto", 1="Cool", 2="Dry", 3="Wind", 4="Heat"]
        Switch item=ac_direction icon="wind" mappings=[1="SwingUD", 2="Rotation", 3="Fixed", 4="SwingLR"]
        Switch item=ac_windlevel icon="wind" mappings=[0="Auto", 1="Low", 2="Mid", 3="High", 4="Turbo"]
    }

The names can be changed in the sitemap, but the numbers will always mean the same. Eg. AC_FUN_COMODE with value 0 is always Off.

Supported Air conditioner commands (with valid values to send):

    AC_FUN_OPMODE:
         Auto(0), Cool(1), Dry(2), Wind(3), Heat(4)
    AC_FUN_TEMPSET: 
        16 to 28 
    AC_FUN_WINDLEVEL:
        Auto(0), Low(1), Mid(2), High(3), Turbo(4) 
    AC_FUN_POWER: 
        OFF, ON 
    AC_FUN_COMODE:
        Off(0), Quiet(1), Sleep(2), Smart(3), SoftCool(4), TurboMode(5), WindMode1(6), WindMode2(7), WindMode3(8) 
    AC_FUN_DIRECTION:
        SwingUD(1), Rotation(2), Fixed(3), SwingLR(4)

 The binding asks the air conditioner for its status every 60 seconds, this is the only update from the air conditioner. Sometimes the newest changed aren't reflected at once, so you might have to wait a bit before the air conditioner returns the correct state.
 The air conditioner returns an xml in the following format:
```
 <?xml version="1.0" encoding="utf-8" ?>
    <Response Type="DeviceState" Status="Okay">
            <DeviceState>
                    <Device DUID="7825AD1243BA" GroupID="AC" ModelID="AC" >
                        <Attr ID="AC_FUN_ENABLE" Type="RW" Value="Enable"/>
                        <Attr ID="AC_FUN_POWER" Type="RW" Value="On"/>
                        <Attr ID="AC_FUN_SUPPORTED" Type="R" Value="0"/>
                        <Attr ID="AC_FUN_OPMODE" Type="RW" Value="Heat"/>
                        <Attr ID="AC_FUN_TEMPSET" Type="RW" Value="20"/>
                        <Attr ID="AC_FUN_COMODE" Type="RW" Value="Off"/>
                        <Attr ID="AC_FUN_ERROR" Type="RW" Value="00000000"/>
                        <Attr ID="AC_FUN_TEMPNOW" Type="R" Value="21"/>
                        <Attr ID="AC_FUN_SLEEP" Type="RW" Value="0"/>
                        <Attr ID="AC_FUN_WINDLEVEL" Type="RW" Value="Auto"/>
                        <Attr ID="AC_FUN_DIRECTION" Type="RW" Value="Fixed"/>
                        <Attr ID="AC_ADD_AUTOCLEAN" Type="RW" Value="Off"/>
                        <Attr ID="AC_ADD_APMODE_END" Type="W" Value="0"/>
                        <Attr ID="AC_ADD_STARTWPS" Type="RW" Value="Direct"/>
                        <Attr ID="AC_ADD_SPI" Type="RW" Value="Off"/>
                        <Attr ID="AC_SG_WIFI" Type="W" Value="Connected"/>
                        <Attr ID="AC_SG_INTERNET" Type="W" Value="Connected"/>
                        <Attr ID="AC_ADD2_VERSION" Type="RW" Value="0"/>
                        <Attr ID="AC_SG_MACHIGH" Type="W" Value="0"/>
                        <Attr ID="AC_SG_MACMID" Type="W" Value="0"/>
                        <Attr ID="AC_SG_MACLOW" Type="W" Value="0"/>
                        <Attr ID="AC_SG_VENDER01" Type="W" Value="0"/>
                        <Attr ID="AC_SG_VENDER02" Type="W" Value="0"/>
            <Attr ID="AC_SG_VENDER03" Type="W" Value="0"/>
        </Device>
    </DeviceState>
</Response> 
```