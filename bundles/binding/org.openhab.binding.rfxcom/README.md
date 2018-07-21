# RFXCOM Binding

The binding should be compatible at least with RFXtrx433 USB 433.92MHz transceiver, which contains both receiver and transmitter functions. The RFXtrx433E is fully compatible with the RFXtrx433, and has in addition the possibility to transmit RFY (Somfy RTS) RF command at the frequency used by Somfy.

The RFXCOM binding currently supports the following packet types:

* Blinds1
* Control
* Current
* Curtain1
* Energy
* Humidity
* Interface
* Lighting1
* Lighting2
* Lighting4
* Lighting5
* Lighting6
* Factory
* Interface
* Rain
* Rfy
* Security1
* Security2
* TemperatureHumidity
* Temperature
* Thermostat1
* Transmitter
* Wind

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/rfxcom/).

## Table of Contents

<!-- MarkdownTOC -->

- [RFXCOM Devices](#rfxcom-devices)
- [Binding Configuration](#binding-configuration)
- [Item Configuration](#item-configuration)
  - [DeviceId must be provided in decimal](#deviceid-must-be-provided-in-decimal)
  - [PacketType.SubType Examples](#packettypesubtype-examples)
  - [Known Values for PacketType.SubType](#known-values-for-packettypesubtype)
  - [Value Selectors](#value-selectors)
- [Examples](#examples)
  - [Weather Station](#weather-station)
  - [Switches](#switches)
  - [SECURITY1.X10_SECURITY_MOTION](#security1x10securitymotion)
  - [LIGHTING4.PT2262](#lighting4pt2262)
  - [LIGHTING5.IT \(Flamingo FA500, Flamingo FA500S/2, Flamingo FA500S/3\)](#lighting5it-flamingo-fa500-flamingo-fa500s2-flamingo-fa500s3)
  - [THERMOSTAT1](#thermostat1)
  - [LIGHTWAVERF](#lightwaverf)
  - [LIGHTWAVERF Mood Switch](#lightwaverf-mood-switch)
  - [OWL CM160 Energy Monitor](#owl-cm160-energy-monitor)
  - [OWL CM113 Energy Monitor](#owl-cm113-energy-monitor)

<!-- /MarkdownTOC -->


## RFXCOM Devices

The RFXtrx433 device is a very versatile transceiver that can send and receive information to and from a wide number of compatible devices from different brands and manufacturers. 

Supports RF 433 Mhz protocols like: HomeEasy, Cresta, X10, La Crosse, OWL, CoCo ([KlikAanKlikUit](http://www.klikaanklikuit.nl)), PT2262, Oregon e.o.

**Suitable for receiving e.g. following 433.92MHz sensors:**

* Cent-a-meter, Electrisave, OWL ampere and power meters,
Alecto, Cresta, Fine Offset, Hideki, LaCrosse, Oregon, Rubicson, TFA, Viking weather sensors,
* Avidsen, Chacon, NEXA, Flamingo, Blyss, Proove smoke detectors,
* Atlantic Meiantech, Visonic, X10 alarm sensors,
* Oregon scales,
* Maverick BBQ Rubicson sensors
* Keeloq, HCS301 based sensors (Security2 protocol)
* Chacon, Home Easy, COCO, NEXA, X10, Flamingo FA500R remote controls.

**And suitable for controlling e.g. following 433.92MHz devices:**

* ANSLUT, BBSB, Blyss, Brennestuhl, Chacon, COCO, DI.O, ELRO, Energenie, Eurodomest, Flamingo, HomeEasy, Pulse, Inter Techno, K ambrook, COCO, LightwaveRF, Livolo, Mercury, NEXA, Phenix, Proove, risingsun, Sartano, Siemens, X10, XDOM dimmers / switches,
* Byron SX, Select Plus doorbell,
* A-OK, Bofu, Ematronic, Hasta, RAEX, Rohrmotor24,  Roller Trol, Somfy (RFY), Yooda shutter / awning motors,
* Harrison, Forest curtain motors,
* Kingpin, Media Mount, Proluxx projection,
* Avidsen, Chacon, NEXA, Flamingo, Blyss, Proove smoke alarms with  siren,
* Mertik Maxitrol fireplace,
* Aoke relay,
* TRC02 RGB, MDREMOTE LED strip controller,
* Smartwares radiator valve.

**And with the RFXtrx433E suitable for controlling e.g. following 433.42MHz devices:**

* Somfy RTS,
* ASA

See RFXtrx User Guide for the complete list of supported sensors and devices [http://www.rfxcom.com](http://www.rfxcom.com) and firmware update announcements.

**Whilst the range of supported devices is impressive, not all of them are compatible with the binding for openHAB yet**

## Binding Configuration

The binding can be configured in the file `services/rfxcom.cfg`.

| Key | Default | Required | Description |
|-----|---------|:--------:|-------------|
| serialPort |  |    Yes   | the serial port on the host system where RFXCOM controller is connected, e.g. `COM1` on Windows,`/dev/ttyS0` on Linux or `/dev/tty.PL2303-0000103D` on Mac.<br/>NOTE: On Linux, should the RFXCOM device be added to the `dialout` group, you may get an error stating the the serial port cannot be opened when the RfxCom plugin tries to load.  You can get around this by adding the `openhab` user to the `dialout` group like this: `usermod -a -G dialout openhab`. Also on Linux you may have issues with the USB if using two serial USB devices e.g. RFXcom and ZWave. See the [wiki page on symlinks](https://github.com/openhab/openhab1-addons/wiki/symlinks). |
| setMode |     |    No    | can be used to configure RFXCOM controller to listen to various receiver protocols. This is very useful because the receiver will become more sensitive when only the required protocols are enabled. Example: `0D000000035300000C2F00000000`. You can use the RFXmngr application [found in the Downloads section of the RFXcom website (Windows only)](http://www.rfxcom.com/) to get the valid configuration command. Command must be a 28 characters (14 bytes) hexadecimal string.  You can also use RFXmngr to get the Device Ids needed to bind each item to openHAB. |

## Item Configuration

The syntax for "in" bindings (updating item states):

```
rfxcom="<<DeviceId>:<ValueSelector>"
```

The syntax for "out" bindings (sending commands to items):

```
rfxcom="><DeviceId>:<PacketType>.<SubType>:<ValueSelector>"
```

### DeviceId must be provided in decimal

RFXmngr and the openHAB logging outputs may display something like this example from a HomeEasy wireless wall switch.

```
Packet Length   = 0B
Packettype      = Lighting2
Subtype         = AC
Id              = 00130FE2
Unitcode        = 11
Command         = Off
Dim level       = 0%
```

You wouldn't be forgiven for thinking the `<DeviceId>` is `00130FE2` but this is not the Id you need.

This is the correct `<DeviceId>` but it is in the wrong (hexadecimal) format. You need to convert it to decimal, using a tool like [binaryhexconverter.com](http://www.binaryhexconverter.com/hex-to-decimal-converter). You simply put the HEX `00130FE2` in the correct field and press convert.

Use the converted decimal number (easy to identify as it won't contain any letters) as the `<DeviceId>`.

So continuing with the above example. The displayed Id in HEX was `00130FE2` this is converted into DEC `1249250`. Also for this packettype (Lighting2) you need to join the Unitcode `11` to the `<DeviceId>`, so finally the binding for the item is:

```
Switch    Wall_Switch_FF_Office    {rfxcom="<1249250.11:Command"}
```

#### Finding the correct DeviceId for your items

If you want to add cheap 433 MHz devices like PT2622 remotes, contacts, sensors or wireless outlets and don't know the correct values, you can start openHAB in debug mode, press the buttons on the original remote or act on the sensor.

To pair the entry-level wireless remote outlets (Elro, Intertechno, Intertek, Pollin, ...) you can do the following:

* Enable Lighting4 protocol using the RFXMgr or `setMode`
* Run openHAB in debug mode
* Press a button on the original remote controller (here I press `A ON`) and read the DeviceId `Id` and the pulse width `Pulse` from the output:

```text
[DEBUG] [.b.r.internal.RFXComConnection:148  ] - Data received:
- Packet type = LIGHTING4
- Id = 1285
- Command = ON
- Pulse = 318
```

You can use these values to configure the binding to receive and send data.

Alternately, you can enable DEBUG logging for the logger named `org.openhab.binding.rfxcom`.

> Some devices like Oregon Thermo Hygrometer changes the `<DeviceId>` every time you remove the batteries.  When you change your batteries you will have to get the new `<DeviceId>` and update the item.

----

### PacketType.SubType Examples

| Packet       | `<PacketType>.<SubType>` | Examples  |
| -------------|--------------------------|-----------|
| Lighting1    | `SensorId.UnitCode` | `B.1` or `B.2` or `B.0` (note 1) |
| Lighting2    | `SensorId.UnitCode` | `636602.1` or `636602.0`  (note 1) |
| Lighting4    | `SensorId` | `1285` |
| Lighting5    | `SensorId.UnitCode` | `636602.1` |
| Lighting6    | `SensorId.GroupCode.UnitCode` | `257.B.1` or `64343.B.2` or `636602.H.5` |
| Curtain1     | `SensorId.UnitCode` | `P.1` (note 2) |
| TemperatureHumidity, Current, Energy etc | `SensorId.UnitCode` | `2561` |
| Security2    | `S2_SensorId` | `S2_12567` |
| RfyMessage   | `Id1.Id2.Id3.UnitCode` | `0.12.12.1` (note 3) |

Notes:

1. Where "0" would control all items on device,
1. See the RFXcom documents/manual for more information
1. See the RFXcom documents/manual, for defining the Ids and Unit code, you should pair the RFXcom with your somfy motor using RFXMgr program

### Known Values for PacketType.SubType

| `<PacketType>.<SubType>` | Description | ValueSelector |
|--------------------------|-------------|---------------|
| LIGHTING1.X10 | working | Command |
| LIGHTING1.ARC | working | Command |
| LIGHTING1.AB400D | Untested |  |
| LIGHTING1.WAVEMAN | Untested |  |
| LIGHTING1.EMW200 | Untested |  |
| LIGHTING1.IMPULS | working | Command |
| LIGHTING1.RISINGSUN | Untested |  |
| LIGHTING1.PHILIPS | Untested |  |
| LIGHTING1.ENERGENIE | working | Command |
| LIGHTING2.AC | working | Command, DimmingLevel |
| LIGHTING2.HOME_EASY_EU | working | Command,DimmingLevel |
| LIGHTING2.ANSLUT | Untested |  |
| LIGHTING4.PT2262 | working | Command |
| LIGHTING5.LIGHTWAVERF | working | Command, DimmingLevel |
| LIGHTING5.IT | working | Command |
| LIGHTING6.BLYSS | working | Command |
| CURTAIN1.HARRISON | Harrison curtain rail, e.g. Neta 12 | Shutter |
| TEMPERATURE.La Crosse TX17 | working |  |
| TEMPERATUREHUMIDITY.Oregon 2.1<br>THGN122_123_132_THGR122_228_238_268 | working |  |
| TEMPERATUREHUMIDITY.THGN800_THGR810 | working |  |
| TEMPERATUREHUMIDITY.RTGR328 | Untested |  |
| TEMPERATUREHUMIDITY.THGR328 | Untested |  |
| TEMPERATUREHUMIDITY.WTGR800 | Untested |  |
| TEMPERATUREHUMIDITY.<br>THGR918_THGRN228_THGN50 | Untested |  |
| TEMPERATUREHUMIDITY.TFA_TS34C__CRESTA | Working | Temperature, Humidity, HumidityStatus, BatteryLevel, SignalLevel |
| TEMPERATUREHUMIDITY.<br>WT260_WT260H_WT440H_WT450_WT450H | Untested |  |
| TEMPERATUREHUMIDITY.VIKING_02035_02038 | Working | Temperature, Humidity, Humidity status, Signal level, Battery level |
| SECURITY1.X10_SECURITY_MOTION | working | Motion |
| SECURITY2 | working<br>e.g. JFL SHC3.0 | Contact,Contact1,Contact2,Contact3,SignalLevel,BatteryLevel |
| THERMOSTAT1 | Digimax 210 working | Temperature, SetPoint, Contact |
| ENERGY.ELEC1 | Owl CM113 Working | Channel1Amps, Channel2Amps, Channel3Amps |
| ENERGY.ELEC2 | Owl CM160 Working | InstantAmps, TotalAmpHours |
| RFY.RFY | Working | Shutter |


### Value Selectors

| `<ValueSelector>` | Item type | Values | Notes |
|---------------|-------------------------|--------|-------|
| RawData | String |  | Full message in hex. |
| Data | String |  | Message without header. Since 1.9.0. |
| Command | Switch | ON, OFF, GROUP_ON, GROUP_OFF |  |
| SignalLevel | Number |  |  |
| DimmingLevel | Dimmer | UP, DOWN, PERCENTAGE |  |
| Temperature | Number |  |  |
| Humidity | Number |  |  |
| HumidityStatus | String |  |  |
| BatteryLevel | Number |  |  |
| Shutter | Rollershutter | OPEN, CLOSE, STOP |  |
| Motion | Switch | MOTION, NO_MOTION |  |
| Voltage | Number |  |  |
| SetPoint | Number |  |  |
| Pressure | Number |  |  |
| Forecast | Number |  |  |
| RainRate | Number |  |  |
| RainTotal | Number |  |  |
| WindDirection | Number |  |  |
| WindSpeed | Number |  |  |
| Gust | Number |  |  |
| ChillFactor | Number |  |  |
| InstantPower | Number |  |  |
| TotalUsage | Number |  |  |
| Voltage | Number |  |  |
| InstantAmps | Number |  |  |
| TotalAmpHours | Number |  |  |
| Channel1Amps | Number |  |  |
| Channel2Amps | Number |  |  |
| Channel3Amps | Number |  |  |
| Status | String |  |  |
| Mood | Number |  |  |
| Contact | Contact |  |  |
| Contact1 | Contact |  |  |
| Contact2 | Contact |  |  |
| Contact3 | Contact |  |  |

## Examples

### Weather Station

```
Number OutdoorTemperature { rfxcom="<2561:Temperature" }
Number OutdoorHumidity { rfxcom="<2561:Humidity" }
Number RainRate { rfxcom="<30464:RainRate" }
Number WindSpeed    { rfxcom="<19968:WindSpeed" }
```

### Switches

```
Switch Btn1 { rfxcom="<636602.1:Command" }
Number Btn1SignalLevel { rfxcom="<636602.1:SignalLevel" }
Dimmer Btn1DimLevel { rfxcom="<636602.1:DimmingLevel" }
String Btn2RawData { rfxcom="<636602.2:RawData" }
Switch ChristmasTreeLights { rfxcom=">636602.1:LIGHTING2.AC:Command" }
Rollershutter CurtainDownstairs { rfxcom=">P.1:CURTAIN1.HARRISON:Shutter" }
Rollershutter ShutterBedroom { rfxcom=">1.0.0.1:RFY.RFY:Shutter" }
```

### SECURITY1.X10_SECURITY_MOTION

```
Switch swMotion { rfxcom="<4541155:Motion" }
Number MSensor_Bat { rfxcom="<4541155:BatteryLevel" }
```
### LIGHTING4.PT2262

```
Switch swWallController { rfxcom="<1285:Command" } // receive wireless wall switch
Switch pCoffeeMachine { rfxcom=">1285.315:LIGHTING4.PT2262:Command" } // control wireless outlet
```

### LIGHTING5.IT (Flamingo FA500, Flamingo FA500S/2, Flamingo FA500S/3)

```
Switch fa500_remote_A { rfxcom="<2622466.1:Command" } 
Switch fa500_remote_B { rfxcom="<2622466.2:Command" }
Switch fa500_remote_C { rfxcom="<2622466.3:Command" }
Switch fa500_remote_D { rfxcom="<2622466.4:Command" }

// Note: does not mimic the remote but uses another subtype and code which 
// should be learned to the socket separately 
Switch virtual_fa500s_switch_1 { rfxcom=">2061.1:LIGHTING5.IT:Command" } 
Switch virtual_fa500s_switch_2 { rfxcom=">2061.2:LIGHTING5.IT:Command" }
```

### THERMOSTAT1

```
Number RFXTemp_Living { rfxcom="<30515:Temperature" } 
Number RFXTemp_LivingSP { rfxcom="<30515:SetPoint" }
Contact RFXTemp_LivingRoom_Stat { rfxcom="<30515:Contact" }
```

### LIGHTWAVERF

```
Switch Light1 { rfxcom=">3155730.3:LIGHTING5.LIGHTWAVERF:Command"}
Dimmer Light2 "Light2 [%d %%]" { rfxcom=">3155730.4:LIGHTING5.LIGHTWAVERF:DimmingLevel"  }
```

### LIGHTWAVERF Mood Switch

```
Number Button_MoodSwitch { rfxcom="<15942554.16:Mood" }
```

### OWL CM160 Energy Monitor

```
Number Owl_InstantAmps { rfxcom="<63689:InstantAmps"}
Number Owl_TotalAmpHours { rfxcom="<63689:TotalAmpHours"  }
```

### OWL CM113 Energy Monitor

```
Number Owl_Amps { rfxcom="<35072:Channel2Amps" }
```
