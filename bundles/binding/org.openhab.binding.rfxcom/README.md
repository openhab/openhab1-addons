# Intro to the RFXCOM device

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

## The RFXCOM Binding

The binding should be compatible at least with RFXtrx433 USB 433.92MHz transceiver, which contains both receiver and transmitter functions. The RFXtrx433E is fully compatible with the RFXtrx433 and has in addition the possibility to transmit RFY (Somfy RTS) RF command at the frequency used by Somfy.

### RFXCOM binding currently supports:

Blinds1, Control, Current, Curtain1, Energy, Humidity, Interface, Lighting1, Lighting2, Lighting4, Lighting5, Lighting6, Factory, Interface, Rain, Rfy, Security1, Security2, TemperatureHumidity, Temperature, Thermostat1, Transmitter, Wind packet types.

For installation of the binding, please see Wiki page [[Bindings]].

----

## Binding Configuration

First of all you need to configure the following values in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    ############## RFXCOM Binding ###################

    # Serial port of RFXCOM interface
    # Valid values are e.g. COM1 for Windows and /dev/ttyS0 or /dev/ttyUSB0 for Linux
    rfxcom:serialPort=

    # Set mode command for controller (optional)
    # E.g. rfxcom:setMode=0D000000035300000C2F00000000
    # rfxcom:setMode=

#### rfxcom:serialPort

The `rfxcom:serialPort` value is the identification of the serial port on the host system where RFXCOM controller is connected, e.g. `COM1` on Windows,`/dev/ttyS0` on Linux or `/dev/tty.PL2303-0000103D` on Mac.

NOTE: On Linux, should the RFXCOM device be added to the `dialout` group, you may get an error stating the the serial port cannot be opened when the RfxCom plugin tries to load.  You can get around this by adding the `openhab` user to the `dialout` group like this: `usermod -a -G dialout openhab`.

Also on Linux you may have issues with the USB if using two serial USB devices e.g. RFXcom and ZWave. See the wiki page for more on symlinking the USB ports [[symlinks]]

#### rfxcom:setMode

The `rfxcom:setMode` value is optional. Set mode command can be used to configure RFXCOM controller to listening to various receiver protocols. This is very useful because the receiver will become more sensitive when only the required protocols are enabled.

You can use the RFXmngr application [found in the Downloads section of the RFXcom website (windows is required)](http://www.rfxcom.com/) to get the valid configuration command. Command must be a 28 characters (14 bytes) hexadecimal string.  You can also use RFXmngr to get the Device Ids needed to bind each item to openHAB.

## Item Binding Configuration

In order to bind an item to RFXCOM device, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items). The syntax of the binding configuration strings accepted is the following:

    in:  rfxcom="<DeviceId:ValueSelector"
    out: rfxcom=">DeviceId:PacketType.SubType:ValueSelector"

### Decimal or Hex DeviceID

_(Prior to 1.8 a common mistake when setting up this binding, was to use the wrong format for the DeviceID. Though the logback since 1.8 seems to be reporting the DeviceID in the correct Decimal format (note that this maybe due to a firmware upgrade of the device and not necessarily the binding). The following section may not be relevant to new installs, but given it is such a major "gotcha" it is worth being aware of.)_

RFXmngr and the openHAB logback outputs may display something like this example from a HomeEasy wireless wall switch.

```
Packet Length   = 0B
Packettype      = Lighting2
Subtype         = AC
Id              = 00130FE2
Unitcode        = 11
Command         = Off
Dim level       = 0%
```

You wouldn't be forgiven for thinking the id is **00130FE2** but this is not the Id you need.

This is the correct Id but it is in the wrong Hexadecimal format. You need to convert to Decimal, a Google search for `HEX to DEC` will provide many online tools to do this [binaryhexconverter.com](http://www.binaryhexconverter.com/hex-to-decimal-converter) is one. You simply put the HEX `00130FE2` in the correct field and press convert.

Use the converted Decimal number (easy to identify as it won't contain any letters) as the DeviceID.

So continuing with the above example. The displayed Id in HEX was `00130FE2` this is converted into DEC `1249250`. Also for this packettype (Lighting2) you need to join the Unitcode `11` to the DeviceId, so final the binding for the item is:

```
Switch    Wall_Switch_FF_Office    {rfxcom="<1249250.11:Command"}
```


## Finding the correct values for your item bindings

If you want to add cheap 433 MHz devices like PT2622 remotes, contacts, sensors or wireless outlets and don't know the correct values, you can start OpenHAB in debug mode, press the buttons on the original remote or act on the sensor.

To pair the entry-level wireless remote outlets (Elro, Intertechno, Intertek, Pollin, ...) you can do the following:

- Enable Lighting4 protocol using the RFXMgr or `setMode`
- Run OpenHAB in debug mode
- Press a button on the original remote controller (here I press `A ON`) and read the DeviceId `Id` and the pulse width `Pulse` from the output:

```text
21:35:20.096 [DEBUG] [.b.r.internal.RFXComConnection:148  ] - Data received:
- Packet type = LIGHTING4
- Id = 1285
- Command = ON
- Pulse = 318
```

You can use these values to configure the binding to receive and send data.

Alternately add the following to your `logback.xml` file to only see the RFXcom logs.

    <logger name="org.openhab.binding.rfxcom" level="DEBUG" />

** Some devices like Oregon Thermo Hygrometer changes the Device id every time you remove the batteries, when you change your batteries you will have to get the new deviceId and update the Item binding **

----
## Device specific PacketType syntaxes

| Packet  | Format | Examples  |
| :------------- | :------------- | -------------: |
| Lighting1  | `SensorId.UnitCode` | **B.1** or **B.2** or **B.0** <sup>1</sup> |
| Lighting2 | `SensorId.UnitCode` | **636602.1** or **636602.0**  <sup>1</sup> |
| Lighting4 | `SensorId` | **1285** |
| Lighting5 | `SensorId.UnitCode` | **636602.1** |
| Lighting6 | `SensorId.GroupCode.UnitCode` | **257.B.1** or **64343.B.2** or **636602.H.5** |
| Curtain1 | `SensorId.UnitCode` | **P.1** <sup>2</sup>|
| TemperatureHumidity, Current, Energy etc | `SensorId.UnitCode` | **2561**|
| Security2 | `S2_SensorId` | **S2_12567** |
| RfyMessage | `Id1.Id2.Id3.UnitCode` | **0.12.12.1** <sup>3</sup>|

<sup>1</sup> Where "0" would control all items on device,

<sup>2</sup> See the RFXcom documents/manual for more information

<sup>3</sup> See the RFXcom documents/manual, for defining the Ids and Unit code, you should pair the RFXcom with your somfy motor using RFXMgr program

----

## Item configuration examples

**Weather Station**
```java
Number OutdoorTemperature { rfxcom="<2561:Temperature" }
Number OutdoorHumidity { rfxcom="<2561:Humidity" }
Number RainRate { rfxcom="<30464:RainRate" }
Number WindSpeed    { rfxcom="<19968:WindSpeed" }
```

**Switches**
```java
Switch Btn1 { rfxcom="<636602.1:Command" }
Number Btn1SignalLevel { rfxcom="<636602.1:SignalLevel" }
Dimmer Btn1DimLevel { rfxcom="<636602.1:DimmingLevel" }
String Btn2RawData { rfxcom="<636602.2:RawData" }
Switch ChristmasTreeLights { rfxcom=">636602.1:LIGHTING2.AC:Command" }
Rollershutter CurtainDownstairs { rfxcom=">P.1:CURTAIN1.HARRISON:Shutter" }
Rollershutter ShutterBedroom { rfxcom=">1.0.0.1:RFY.RFY:Shutter" }
```

**SECURITY1.X10_SECURITY_MOTION**
```java
Switch swMotion { rfxcom="<4541155:Motion" }
Number MSensor_Bat { rfxcom="<4541155:BatteryLevel" }
```
**LIGHTING4.PT2262**
```java
Switch swWallController { rfxcom="<1285:Command" } // receive wireless wall switch
Switch pCoffeeMachine { rfxcom=">1285.315:LIGHTING4.PT2262:Command" } // control wireless outlet
```

**LIGHTING5.IT** (Flamingo FA500, Flamingo FA500S/2, Flamingo FA500S/3)
```java
Switch fa500_remote_A { rfxcom="<2622466.1:Command" } 
Switch fa500_remote_B { rfxcom="<2622466.2:Command" }
Switch fa500_remote_C { rfxcom="<2622466.3:Command" }
Switch fa500_remote_D { rfxcom="<2622466.4:Command" }

// Note: does not mimic the remote but uses another subtype and code which 
// should be learned to the socket separately 
Switch virtual_fa500s_switch_1 { rfxcom=">2061.1:LIGHTING5.IT:Command" } 
Switch virtual_fa500s_switch_2 { rfxcom=">2061.2:LIGHTING5.IT:Command" }
```

**THERMOSTAT1**
```java
Number RFXTemp_Living { rfxcom="<30515:Temperature" } 
Number RFXTemp_LivingSP { rfxcom="<30515:SetPoint" }
Contact RFXTemp_LivingRoom_Stat { rfxcom="<30515:Contact" }
```

**LIGHTWAVERF**
```java
Switch Light1 { rfxcom=">3155730.3:LIGHTING5.LIGHTWAVERF:Command"}
Dimmer Light2 "Light2 [%d %%]" { rfxcom=">3155730.4:LIGHTING5.LIGHTWAVERF:DimmingLevel"  }
```

**LIGHTWAVERF Mood Switch**
```java
Number Button_MoodSwitch { rfxcom="<15942554.16:Mood" }
```

**OWL CM160 Energy Monitor**
```java
Number Owl_InstantAmps { rfxcom="<63689:InstantAmps"}
Number Owl_TotalAmpHours { rfxcom="<63689:TotalAmpHours"  }
```

**OWL CM113 Energy Monitor**
```java
Number Owl_Amps { rfxcom="<35072:Channel2Amps" }
```

----



## Specifics for PacketTypes and SubTypes  (PacketType.SubType)

<table>
  <tr><td><b>PacketType.SubType</b></td><td><b>Description</b></td><td><b>ValueSelector</b></td></tr>
  <tr><td>LIGHTING1.X10</td><td>working</td><td>Command</td></tr>
  <tr><td>LIGHTING1.ARC</td><td>working</td><td>Command</td></tr>
  <tr><td>LIGHTING1.AB400D</td><td>Untested</td><td></td></tr>
  <tr><td>LIGHTING1.WAVEMAN</td><td>Untested</td><td></td></tr>
  <tr><td>LIGHTING1.EMW200</td><td>Untested</td><td></td></tr>
  <tr><td>LIGHTING1.IMPULS</td><td>working</td><td>Command</td></tr>
  <tr><td>LIGHTING1.RISINGSUN</td><td>Untested</td><td></td></tr>
  <tr><td>LIGHTING1.PHILIPS</td><td>Untested</td><td></td></tr>
  <tr><td>LIGHTING1.ENERGENIE</td><td>working</td><td>Command</td></tr>
  <tr><td>LIGHTING2.AC</td><td>working</td><td>Command, DimmingLevel</td></tr>
  <tr><td>LIGHTING2.HOME_EASY_EU</td><td>working</td><td>Command,DimmingLevel</td></tr>
  <tr><td>LIGHTING2.ANSLUT</td><td>Untested</td><td></td></tr>
  <tr><td>LIGHTING4.PT2262</td><td>working</td><td>Command</td></tr>
  <tr><td>LIGHTING5.LIGHTWAVERF</td><td>working</td><td>Command, DimmingLevel</td></tr>
  <tr><td>LIGHTING5.IT</td><td>working</td><td>Command</td></tr>
  <tr><td>LIGHTING6.BLYSS</td><td>working</td><td>Command</td></tr>
  <tr><td>CURTAIN1.HARRISON</td><td>Harrison curtain rail, e.g. Neta 12</td><td>Shutter</td></tr>
   <tr><td>TEMPERATURE.La Crosse TX17</td><td>working</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.Oregon 2.1<br>THGN122_123_132_THGR122_228_238_268</td><td>working</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.THGN800_THGR810</td><td>working</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.RTGR328</td><td>Untested</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.THGR328</td><td>Untested</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.WTGR800</td><td>Untested</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.<br>THGR918_THGRN228_THGN50</td><td>Untested</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.TFA_TS34C__CRESTA</td><td>Working</td><td>Temperature, Humidity, HumidityStatus, BatteryLevel, SignalLevel</td></tr>
  <tr><td>TEMPERATUREHUMIDITY.<br>WT260_WT260H_WT440H_WT450_WT450H</td><td>Untested</td><td></td></tr>
  <tr><td>TEMPERATUREHUMIDITY.VIKING_02035_02038</td><td>Working</td><td>Temperature, Humidity, Humidity status, Signal level, Battery level</td></tr>
  <tr><td>SECURITY1.X10_SECURITY_MOTION</td><td>working</td><td>Motion</td></tr>
  <tr><td>SECURITY2</td><td>working<br>e.g. JFL SHC3.0</td><td>Contact,Contact1,Contact2,Contact3,SignalLevel,BatteryLevel</td></tr>
  <tr><td>THERMOSTAT1</td><td>Digimax 210 working</td><td>Temperature, SetPoint, Contact</td></tr>

  <tr><td>ENERGY.ELEC1</td><td>Owl CM113 Working</td><td>Channel1Amps, Channel2Amps, Channel3Amps</td></tr>
  <tr><td>ENERGY.ELEC2</td><td>Owl CM160 Working</td><td>InstantAmps, TotalAmpHours</td></tr>
  <tr><td>RFY.RFY</td><td>Working</td><td>Shutter</td>

</table>

`ValueSelector` specify ...

<table>
  <tr><td><b>Value selector</b></td><td><b>Valid OpenHAB data type</b></td><td><b>Values</b></td><td><b>Notes</b></td></tr>
  <tr><td>RawData</td><td>StringItem</td><td></td><td>Full message in hex.</td></tr>
  <tr><td>Data</td><td>StringItem</td><td></td><td>Message without header. Since 1.9.0.</td></tr>
  <tr><td>Command</td><td>SwitchItem</td><td>ON, OFF, GROUP_ON, GROUP_OFF</td><td></td></tr>
  <tr><td>SignalLevel</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>DimmingLevel</td><td>DimmerItem</td><td>UP, DOWN, PERCENTAGE</td><td></td></tr>
  <tr><td>Temperature</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Humidity</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>HumidityStatus</td><td>StringItem</td><td></td><td></td></tr>
  <tr><td>BatteryLevel</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Shutter</td><td>RollershutterItem</td><td>OPEN, CLOSE, STOP</td><td></td></tr>
  <tr><td>Motion</td><td>SwitchItem</td><td>MOTION, NO_MOTION</td><td></td></tr>
  <tr><td>Voltage</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>SetPoint</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Pressure</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Forecast</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>RainRate</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>RainTotal</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>WindDirection</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>WindSpeed</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Gust</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>ChillFactor</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>InstantPower</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>TotalUsage</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Voltage</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>InstantAmps</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>TotalAmpHours</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Channel1Amps</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Channel2Amps</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Channel3Amps</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Status</td><td>StringItem</td><td></td><td></td></tr>
  <tr><td>Mood</td><td>NumberItem</td><td></td><td></td></tr>
  <tr><td>Contact</td><td>ContactItem</td><td></td><td></td></tr>
  <tr><td>Contact1</td><td>ContactItem</td><td></td><td></td></tr>
  <tr><td>Contact2</td><td>ContactItem</td><td></td><td></td></tr>
  <tr><td>Contact3</td><td>ContactItem</td><td></td><td></td></tr>
</table>