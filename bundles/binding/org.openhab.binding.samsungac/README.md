# Samsung Air Conditioner Binding

This binding should be compatible with Samsung Smart Air Conditioner models.

## Binding Configuration

This binding can be configured in the file `services/samsungac.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<name>`.host |     |    Yes   | hostname or IP address of the first AC to control.  For example, `192.168.1.2` |
| `<name>`.mac |      |          | For example, `7825AD1243BC` |
| `<name>`.token |    |          | See [Obtaining a Token](#obtaining-a-token) below. For example, `33965901-4482-M849-N716-373832354144` |
| `<name>`.certificate |         | For example, cacert.pem |
| `<name>`.password   |          |  |

where `<name>` is to be replaced by a name you choose (e.g. "Livingroom" or "Bedroom") that easily identifies a specific air conditioner.  The properties can be duplicated with a new name so you can control multiple air conditioners, each with its own name.

### Example

```
Livingroom.host=192.168.1.2
Livingroom.mac=7825AD1243BC
Livingroom.token=33965901-4482-M849-N716-373832354144
Livingroom.password=something
Livingroom.certificate=/tmp/cert/cacert.pem
```

If you do not know your Samsung Air Conditioners IP address or MAC address you can download the jar, and run the following command. Then the details of the FIRST discovered air conditioner will be printed:

```shell
    java -cp addons/org.openhab.binding.samsungac_1.6.0.SNAPSHOT.jar org.binding.openhab.samsungac.communicator.SsdpDiscovery
```

### Obtaining a Token

When you have entered the hostname (or IP address) and MAC details in the configuration, you will still need to retrieve a `token` from the air conditioner.  During first connection attempt you do not have a token, so pay attention to the log. It will ask you to switch on and off the air conditioner within 30 seconds. You will then receive a token printed in the log. Enter it here.

Procedure:  

`1.` Turn on the AC. Set the token to an empty string:

```
Livingroom.token=
```

`2.` Start openHAB in debug mode with:  

```
./start_debug.sh
```

`3.` Watch the logs from a (different) console and look at AirConditioner messages:  

```
tail -f openhab.log | grep AirConditioner
```

`4.` You will find the messsage:  

```
WARN  o.b.o.s.c.AirConditioner[:179]- NO TOKEN SET! Please switch off and on the air conditioner within 30 seconds  
```

`5.` Switch AC off and then on (be quick, you really have 20 seconds), you will then find your token in the logfile:                                      

 ```
 WARN  o.b.o.s.c.AirConditioner[:175]- Received TOKEN from AC: 'e56a6def-1ecf-47d4-bc5d-9c818eaec76d'  
 ```

`6.` Copy the token to your configuration.

## Item Configuration

The syntax is:

```
samsungac="[<name>|<command>]"
```

where the `<name>` corresponds the value you gave for `<name>`s in the binding condfiguration, and `<command>` corresponds the air conditioner command from the following list:

Supported Air conditioner commands (with valid values to send):

* AC_FUN_OPMODE:
 *      Auto(0), Cool(1), Dry(2), Wind(3), Heat(4)
* AC_FUN_TEMPSET: 
 *     16 to 28 
* AC_FUN_WINDLEVEL:
 *     Auto(0), Low(1), Mid(2), High(3), Turbo(4) 
* AC_FUN_POWER: 
 *     OFF, ON 
* AC_FUN_COMODE:
 *     Off(0), Quiet(1), Sleep(2), Smart(3), SoftCool(4), TurboMode(5), WindMode1(6), WindMode2(7), WindMode3(8) 
* AC_FUN_DIRECTION:
 *     SwingUD(1), Rotation(2), Fixed(3), SwingLR(4)

## Examples


items/samsungacdemo.items

```
Number ac_current_temp "Current temp [%.1f]" (AC) { samsungac="[Livingroom|AC_FUN_TEMPNOW]" }
Switch ac_power                              (AC) { samsungac="[Livingroom|AC_FUN_POWER]" }
Number ac_mode "Convenience mode"            (AC) { samsungac="[Livingroom|AC_FUN_COMODE]" }
Number ac_op_mode "Operation mode"           (AC) { samsungac="[Livingroom|AC_FUN_OPMODE]" }
Number ac_set_temp "Set temp [%.1f]"         (AC) { samsungac="[Livingroom|AC_FUN_TEMPSET]" }
Number ac_direction "Direction"              (AC) { samsungac="[Livingroom|AC_FUN_DIRECTION]" }
Number ac_windlevel "Windlevel"              (AC) { samsungac="[Livingroom|AC_FUN_WINDLEVEL]" }
```

It is important that you specify these correctly in the sitemap, as each command has some special valid values. Here's an example of the sitemap:

sitemaps/samsungacdemo.sitemap.fragment

```
Frame label="Air conditioner"{
    Text item=ac_current_temp icon="temperature" label="Current temp [%.1f °C]"
    Setpoint item=ac_set_temp minValue=16 maxValue=28 step=1 icon="temperature"
    Switch item=ac_power icon="heating"
    Switch item=ac_mode label="Mode" icon="sofa" mappings=[0="Off", 1="Quiet", 2="Sleep", 3="Smart", 4="SoftCool", 5="TurboMode", 6="WindMode1", 7="WindMode2", 8="WindMode3"]
    Switch item=ac_op_mode icon="sofa" mappings=[0="Auto", 1="Cool", 2="Dry", 3="Wind", 4="Heat"]
    Switch item=ac_direction icon="wind" mappings=[1="SwingUD", 2="Rotation", 3="Fixed", 4="SwingLR"]
    Switch item=ac_windlevel icon="wind" mappings=[0="Auto", 1="Low", 2="Mid", 3="High", 4="Turbo"]
}
```

The names can be changed in the sitemap, but the numbers will always mean the same. For example, `AC_FUN_COMODE` with value 0 is always Off.

## Limitations

The binding asks the air conditioner for its status every 60 seconds, this is the only update from the air conditioner. Sometimes the newest changed aren't reflected at once, so you might have to wait a bit before the air conditioner returns the correct state.

The air conditioner returns XML in the following format:

```xml
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