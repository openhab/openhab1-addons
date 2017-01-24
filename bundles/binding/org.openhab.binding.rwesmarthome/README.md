**IMPORTANT:** _The following openHAB 1.x binding only works with the "old" RWE SmartHome software and **will NOT WORK anymore with the RWE 2.0 Software (released on 01.09.2016)**. Do not update your RWE Software, if you still like to use this binding!_
_A successor of the RWE SmartHome binding for openHAB 2.x is currently in the works and will be release sometime end of 2016._

# Introduction
This binding allows to integrate RWE Smarthome into OpenHAB. It uses an inofficial interface, which is limited in some cases, as explained below. As the interface has some delay and polling is needed to receive all changes from the RWE Smarthome Central (SHC), it may take one or two seconds, until a device finally responds. However, this is tolerable in most cases as time critical rules can be done in the RWE Smarthome Central itself.

**DISCLAIMER:** This binding is based on an inofficial interface, which may be changed or even closed by RWE at any time.

# General configuration
The basic configuration is done in openhab.cfg and looks like this:

```
############################## RWE Smarthome Binding ##############################
#
# Hostname / IP address of the RWE Smarthome server
rwesmarthome:host=192.168.0.2
 
# Username / password of the RWE Smarthome server
rwesmarthome:username=myuser
rwesmarthome:password=mysecretpassword
 
# The interval in seconds to check if the communication with the RWE Smarthome server is still alive.
# If no message receives from the RWE Smarthome server, the binding restarts. (optional, default is 300)
# rwesmarthome:alive.interval=300

# The interval in seconds to wait after the binding configuration has changed before the device states
# will be reloaded from the RWE SHC. (optional, default is 15)
# rwesmarthome:binding.changed.interval=15
```

Simply enter the hostname or IP address of your RWE Smarthome Central (SHC) as well as the corresponding username and password.

**Important:**
The binding only runs with TLSv1, which is default until java 1.7. If you use java 1.8, you have to add `-Djdk.tls.client.protocols=TLSv1` to the java command in your start.sh/start.bat file.
(in OpenHAB2 the java command is somewhere at the end of runtime/karaf/bin/karaf)


# General item binding configuration
All items use the format `{rwe="id=<logical-device-id>,param=<parameter>"}`, where `<logical-device-id>` is a unique number for each logical device (e.g. `2951a048-1d21-5caf-d866-b63bc00280f4`) and `<parameter>` the desired parameter of the device. A thermostat for example has a 'settemperature' and a 'operationmodeauto' to choose from.

As the id's are internal numbers and not visible in the RWE software, the binding **outputs a list of found devices including the supported parameters to the OpenHAB console/logfile**. For the ease of use and a quick startup, it even outputs an example item-config for each item, which can easily be copied into the *.items config file.

The following parameters are available:

| Property | Type | Description | 
| :------------- |:-------------| :-----|
|  contact | Contact | Window/Door contact  |
|  temperature | Number | Temperature sensor |
|  settemperature | Number | Desired temperature of a thermostat |
|  operationmodeauto | Switch | Operationmode of a thermostat |
|  humidity | Number | Humidity sensor |
|  luminance | Number | Luminance sensor |
|  switch | Switch | Switch state, e.g. wall plug |
|  dimmer | Dimmer | Dimmer |
|  dimmerinverted | Dimmer | Dimmer with inverted values |
|  rollershutter | Rollershutter | Rollershutter |
|  rollershutterinverted | Rollershutter | Rollershutter with inverted values, recommended! |
|  smokedetector | String | State of a smokedetector |
|  alarm | Switch | Siren of a smokedetector |
|  variable | Switch | Variable as defined in SHC |
|  totalenergy | Number | Total energy consumption in kWh of a PowerControl/EnergyControl (since 1.9.0) |
|  energypermonthinkwh | Number | energy consumption per month in kWh of a PowerControl/EnergyControl (since 1.9.0) |
|  energypermonthineuro | Number | energy consumption in EUR of a PowerControl/EnergyControl (since 1.9.0) |
|  energyperdayinkwh | Number | energy consumption per day in kWh of a PowerControl/EnergyControl (since 1.9.0) |
|  energyperdayineuro | Number | energy consumption in EUR of a PowerControl/EnergyControl (since 1.9.0) |
|  powerinwatt | Number | current power consumption in Watts of a PowerControl/EnergyControl (since 1.9.0) |

Supported (confirmed) devices and corresponding parameters are:
* RWE SmartHome Bewegungsmelder innen (WMD): luminance only, movement only via variable, see examples.
* RWE SmartHome Heizkörperthermostat (RST): temperature, settemperature, operationmodeauto, humidity
* RWE SmartHome Rauchmelder (WSD): smokedetector, alarm
* RWE SmartHome Tür-/Fenstersensor (WDS): contact
* RWE SmartHome Unterputz-Dimmer (ISD2): dimmer, dimmerinverted
* RWE SmartHome Unterputz-Lichtschalter (ISS2): switch
* RWE SmartHome Unterputz-Rollladensteuerung (ISR2): rollershutter, rollershutterinverted
* RWE SmartHome Wandsender (WSC2): only indirect via variable, see examples.
* RWE SmartHome Zwischenstecker (PSS): switch
* RWE SmartHome Zwischenstecker aussen (PSSOz): switch
* RWE SmartHome Zwischenstecker dimmbar (PSD): dimmer, dimmerinverted
* RWE SmartHome Power Control (since 1.9.0): totalenergy, energypermonthinkwh, energypermonthineuro, energyperdayinkwh, energyperdayineuro, powerinwatt
* RWE SmartHome Power Control Solar (since 1.9.0): totalenergy, energypermonthinkwh, energypermonthineuro, energyperdayinkwh, energyperdayineuro, powerinwatt
* RWE SmartHome Energy Control (since 1.9.0): totalenergy, energypermonthinkwh, energypermonthineuro, energyperdayinkwh, energyperdayineuro, powerinwatt 

Unconfirmed but probably supported devices and corresponding parameters are (please report and confirm, if you own one of the following devices and they are working):
* RWE SmartHome Bewegungsmelder aussen (WMDO): luminance only, movement only via variable, see examples
* RWE SmartHome Fernbedienung (BRC8): only indirect via variable, see examples
* RWE SmartHome Raumthermostat (WRT): temperature, settemperature, operationmodeauto, humidity
* RWE SmartHome Unterputz-Sender (ISC2): only indirect via variable, see examples

# Examples
Replace the example-id with the id's of your devices. You can find the id of your devices in the OpenHAB logfile or console when the RWE Smarthome binding starts.

```
Contact rweContact "Window livingroom [MAP(de.map):%s]" <contact> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=contact"}
Number rweHumidity "Humidity livingroom [%.1f %%]" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=humidity"}
Number rweLuminance "Luminance corridor [%d %%]" <slider> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=luminance"}
Number rweSettemp "Settemp living [%.1f °C]" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=settemperature"}
Number rweTemp "Temp living [%.1f °C]" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=temperature"}
Switch rweAlarm "Alarm corridor" <siren> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=alarm"}
Switch rweSettempOpMode "Settemp living auto" <temperature> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=operationmodeauto"}
Switch rweSmokeDetector "Smokedetector corridor" <fire> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=smokedetector"}
Switch rweSwitch "Light corridor" <switch> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=switch"}
Switch rweVariable "Variable TEST" <switch> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=variable"}
Rollershutter rweRollershutter "Rollershutter living [%d %%]" <rollershutter> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=rollershutterinverted"}
Dimmer rweDimmer "Light [%d %%]" <slider> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=dimmer"}
Number rweEnergyConsumptionTotal "EnergyConsumption total [%.3f kWh]" <energy> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=totalenergy"}
Number rweEnergyConsumptionMonthKWh "EnergyConsumption per month [%.3f kWh]" <energy> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=energypermonthinkwh"}
Number rweEnergyConsumptionMonthEuro "EnergyConsumption per month [%.2f €]" <energy> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=energypermonthineuro"}
Number rweEnergyConsumptionDayKWh "EnergyConsumption per day [%.3f kWh]" <energy> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=energyperdayinkwh"}
Number rweEnergyConsumptionDayEuro "EnergyConsumption per day [%.2f €]" <energy> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=energyperdayineuro"}
Number rwePowerConsumption "PowerConsumption [%.2f W]" <energy> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=powerinwatt"}
```

# Solutions/examples for not directly supported devices
Some devices like a simple pushbutton (e.g. "Wandsender WSC2") or a motion detector are not sent over the interface and thus not directly reachable by the binding. However, there are workarounds based on variables and RWE profiles, which allow using even those devices with OpenHAB.

The general idea is to exchange the state of a device via a variable. To do this, you have to create a variable in the RWE software and change the state of the variable by an RWE event profile. As the variable state is sent to OpenHAB, you can use an OpenHAB rule to react.

**Note:** Because of the delay of the interface it is not recommended to use this solution for time critical applications like switching your lights on when you press the button next to the door. You should implement time critical rules directly in the RWE software and use OpenHAB for the more sophisticated things. ;)

### Workarround for pushbuttons
Works with the following devices:
* RWE SmartHome Wandsender (WSC2)
* RWE SmartHome Unterputz-Sender (ISC2)
* RWE SmartHome Fernbedienung (BRC8)

1. Create a variable for each button, e.g. "SwitchLivingBtn1" and so on.
2. Create an event profile in the RWE software: if the upper button of the device is pushed, set variable "SwitchLivingBtn1" to ON for one second (activate the automatic switch-off after one second).
3. Create an OpenHAB item for the variable: `Switch rweSwitchLivingBtn1 "Switch Btn1" <switch> {rwe="id=2951a048-1d21-5caf-d866-b63bc00280f4,param=variable"}` (use the id of your specific variable "SwitchLivingBtn1", which you can find in the openhab.log at startup of the binding.
4. Create an OpenHAB rule to react:
```
rule "Toggle TV if button 1 is pressed"
when
	Item rweSwitchLivingBtn1 changed to ON
then
	if(TV.state == ON)
		sendCommand(TV, OFF)
	else
		sendCommand(TV, ON)
end	
```

### Workaround for motiondetectors
Works with the following devices:
* RWE SmartHome Bewegungsmelder innen (WMD)
* RWE SmartHome Bewegungsmelder aussen (WMDO)

The solution is mainly the same as for pushbuttons (see above). Simply set the auto-off-time in the RWE profile to a longer period, e.g. 1 minute. Now the variable is on, when a motion occurs until there is no motion any more for about a minute.