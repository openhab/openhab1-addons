Documentation of the Yamaha receiver binding bundle

# Details
This binding connects openHAB with various Yamaha Receivers.

Tested Receivers:
* V473
* V475
* V477
* V479
* V481
* V575
* V579
* V671
* V675
* V679
* V773
* V775
* R-N500
* RX-A1030
* RX-A1050 (Aventage)
* RX-A2020
* RX-A3000
* RX-A810
* RX-S601D
* RX-V477
* RX-V581
* RX-V673
* RX-V677
* RX-V771
* HTR-4065
* HTP-4069

Please add successfully tested receivers!

# Configuration

Inside `openhab.cfg` you need only the host definition:

```
############################# Yamaha Receiver Binding #################################

# The IP address of the Yamaha Receiver (required)
yamahareceiver:<uid>.host=<ip-address>
````

The `<uid>` represents your instance name inside your items list, while `<ip-address>` defines the ip address from your receiver.

For example:
```
yamahareceiver:mySoundSystem.host=192.168.1.1 
```

## Item Binding Syntax
The binding syntax for the receiver follows the following schema:

```
{ yamahareceiver="uid=<uid>, zone=<zone>, bindingType=<type>" }
```

The `<uid>` gets replaced with the one you defined in the `openhab.cfg`file. The `<type>` defines what should be bound (see list of possible binding types below and `<zone>` is the zone you might have defined in your receiver.

Allowed zone entries are:

| Entry   | Zone      |
|---------|-----------|
| `main`  | Main Zone |
| `zone2` | Zone 2    |
| `zone3` | Zone 3    |
| `zone4` | Zone 4    |


For example:

```
{ yamahareceiver="uid=mySoundSystem, zone=main, bindingType=power" }
```

## Binding Types

| Binding           | openHAB Type | Description                                                                                                           |
|-------------------|--------------|-----------------------------------------------------------------------------------------------------------------------|
| `power`           | `Switch`     | Switch the receiver ON or OFF (ON only works if the receiver's "Network Standby" setting is enabled)                 |
| `mute`            | `Switch`     | Mute or unmute the receiver                                                                                           |
| `volume`          | `Dimmer`     | Sets the receiver's volume (percentage)                                                                               |
| `input`           | `String`     | Set the input selection, depends on your receiver's real inputs (Examples: HDMI1, HDMI2, AV4, TUNER, "NET RADIO", Spotify, etc.) |
| `surroundProgram` | `String`     | Set the surround mode (Examples: 2ch Stereo, 7ch Stereo, Hall in Munich, Straight, Surround Decoder)                  |
 
# Examples
**openhab.cfg**
```
#Yamaha Receiver 
yamahareceiver:living.host=192.168.1.1
```

**.items**
```
Switch Yamaha_Power "Power [%s]" <television> { yamahareceiver="uid=living, zone=main,  bindingType=power" }
Dimmer Yamaha_Volume "Volume [%.1f %%]" { yamahareceiver="uid=living, zone=main, bindingType=volumePercent" }
Switch Yamaha_Mute "Mute [%s]" { yamahareceiver="uid=living, zone=main, bindingType=mute" }
String Yamaha_Input "Input [%s]" { yamahareceiver="uid=living, zone=main, bindingType=input" } 
String Yamaha_Surround "Surround [%s]" { yamahareceiver="uid=living, zone=main, bindingType=surroundProgram" } 
Number Yamaha_NetRadio "Net Radio" { yamahareceiver="uid=living, zone=main, bindingType=netRadio" }
````

**.sitemap**
```
Selection item=Yamaha_NetRadio label="Sender" mappings=[1="N Joy", 2="Radio Sport", 3="RDU", 4="91ZM", 5="Hauraki"]
Selection item=Yamaha_Input mappings=[HDMI1="BlueRay",HDMI2="Satellite","NET RADIO"="NetRadio",TUNER="Tuner"]
Selection item=Yamaha_Surround label="Surround Mode" mappings=["2ch Stereo"="2ch","7ch Stereo"="7ch"]
```

> Warning: The `"` around `NET RADIO` is mandatory. This key (left from the equal sign) is a value that must be send to the receiver **with** the space inside. If you omit the `"` the binding would only send the `NET` and the receiver won't react. Same are in surround definition!