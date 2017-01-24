Documentation for tellstick binding

## Introduction

Binding is tested against Tellstick DUO, it should also work with a basic Tellstick.


Supports RF 433 Mhz protocols like: Nexa, HomeEasy, X10, CoCo (KlikAanKlikUit), Oregon e.o. <br>
See further information from http://www.telldus.com

Tellstick binding support currently Sensors (Temperatur and Humidity) and Devices (Switch, Dimmable and Dimmable without absolute level)

For installation of the binding, please see Wiki page [[Bindings]].
The addon must be downloaded from 
https://openhab.ci.cloudbees.com/job/openHAB/lastStableBuild/org.openhab.binding$org.openhab.binding.tellstick/
until we get it packaged into the addons zip.
## Binding Configuration

First of all you need to make sure that your JVM is matching your installed Telldus Center. 
This normally means OpenHab must run on a 32bit JVM for windows and a 64bit JVM for linux.
For windows the binding is hardcoded to look for Telldus Center in Programs Files ("C:/Program Files/Telldus/;C:/Program Files (x86)/Telldus/").
If you have trouble getting the telldus core library to work you can modify the library path using

    tellstick:library_path="FOLDER OF tellduscore dll/so"
If you don't have a Tellstick Duo or the number of device events is less than 1 every 10 minutes you should increase the `max_idle` option in the Tellstick settings file:

    tellstick:max_idle=600000

You will have to use Telldus Center to add all your devices. It is also easiest to find the sensor ID by using the Telldus Center. To configure a device you need the name and type of device, to configure a sensor you need the ID and Type of sensor. I recommend using tdtool -l to list all your devices and sensors. Run OpenHAB in debug mode to see that everything starts up correctly and that you are receiving sensor/device updates.

### The item configuration for devices is:

    tellstick="<deviceName>:<deviceType>:[<specialCase>][<resendCount>]"

+ The **deviceName** must match the name in Telldus Center.  
+ The **deviceType** is either: Command for on/off, DimmingLevel for absolute dimmable device.
+ **SpecialCase** is used for the dimmable without absolute level(devices that is dimmable with clicking twice). For this case use Dimmable as specialCase.  
+ **ResendCount** is just number of times to resend command.

### The item configuration for a sensor is:
  
    tellstick="<sensorId>:<valueType>:[<useValueType>]:[<protocol>]

+ **SensorId** is the sensorId taken from Telldus Center or debug logs.  
+ **ValueType** is either Temperatur or Humidity, based on sensor.  
+ **UseValueType** is for special cases where the value in ValueType is actually something else, supports BatteryLevel (Humidity:BatteryLevel) and Motion (Temperature:Motion). This is implemented for homemade temp/humid and motion sensor [Forum](http://elektronikforumet.com/forum/viewtopic.php?f=3&t=63772&hilit=telldus).
+ **Protocol** if you have multiple sensors with same ID you might need to specify the protocol to make it unique

##Configuration examples

Switch:

     Switch	GF_Dining_Aquarium "Aquarium" <aquarium> {tellstick="Aquarium:Command"}
Dimmer without absolute (dims when clicking **on** button twice):
   
    Switch	GF_Kitchen_Wall "Wall"  { tellstick="Kitchen Backwall:Command:Dimmable:1" }
Temp sensor:
      
    Number	GF_Kitchen_Temp	"Temperature [%.1f °C]"	<temperature> {tellstick="14:Temperature"}
Temp sensor with protocol defined:

    Number      GF_Temp "Temperature [%.1f °C]" <temperature> { tellstick="21:Temperature:Temperature:oregon" }﻿
Wind sensor

    Number      Outside_Wind_Avg "Wind Average [%d]"  <wind>  { tellstick="22:WindAvg" }﻿                  
Battery Level

    Number	GF_Kitchen_Battery "Battery [%d]" <battery> { tellstick="82:Humidity:BatteryLevel" }### Telldus Tellstick

Below is an example of how to capture events from Telldus Tellstick and forward them to the openhab bus as events through REST. The example works for on/off switches and wireless sensors but can easily be extended to dimmers, etc. Events are sent to openhab regardless if a telldus state change originates from a wireless remote or from other software (e. g. Switchking or Telldus Center). In other word, the script keeps openhab in sync with telldus states.

You need:
- Tellstick Duo (RF Transmitter/Receiver)
- Telldus Core software from here: http://developer.telldus.se/
- Tellcore Python lib: https://pypi.python.org/pypi/tellcore-py

See wiki section [Binding configurations](https://code.google.com/p/openhab-samples/wiki/BindingConfig?ts=1378067942&updated=BindingConfig#How_to_send_commands_to_Telldus_Tellstick) for the outbound example (openhab to tellstick).

Tested on Ubuntu Server 13.04.
```python
    #!/usr/bin/env python
    
    import sys
    import time
    
    import tellcore.telldus as td
    from tellcore.constants import *
    
    import httplib
    
    openhab = "localhost:8080"
    headers = {"Content-type": "text/plain"}
    connErr = "No connection to openhab on http://" + openhab
    
    METHODS = {TELLSTICK_TURNON: 'ON',
               TELLSTICK_TURNOFF: 'OFF',
               TELLSTICK_BELL: 'BELL',
               TELLSTICK_TOGGLE: 'toggle',
               TELLSTICK_DIM: 'dim',
               TELLSTICK_LEARN: 'learn',
               TELLSTICK_EXECUTE: 'execute',
               TELLSTICK_UP: 'up',
               TELLSTICK_DOWN: 'down',
               TELLSTICK_STOP: 'stop'}
    
    def raw_event(data, controller_id, cid):
        string = "[RAW] {0} <- {1}".format(controller_id, data)
        print(string)
    
    def device_event(id_, method, data, cid):
        method_string = METHODS.get(method, "UNKNOWN STATE {0}".format(method))
        string = "[DEVICE] {0} -> {1}".format(id_, method_string)
        if method == TELLSTICK_DIM:
            string += " [{0}]".format(data)
        print(string)
        url = "/rest/items/td_device_{0}/state".format(id_)
        try:
            conn = httplib.HTTPConnection(openhab)
            conn.request('PUT', url, method_string, headers)
        except:
            print(connErr)
    
    def sensor_event(protocol, model, id_, dataType, value, timestamp, cid):
        string = "[SENSOR] {0} [{1}/{2}] ({3}) @ {4} <- {5}".format(
            id_, protocol, model, dataType, timestamp, value)
        print(string)
        url = "/rest/items/td_sensor_{0}_{1}_{2}/state".format(protocol, id_, dataType)
        try:
            conn = httplib.HTTPConnection(openhab)
            conn.request('PUT', url, value, headers)
        except:
            print(connErr)
    
    try:
        import asyncio
        loop = asyncio.get_event_loop()
        dispatcher = td.AsyncioCallbackDispatcher(loop)
    except ImportError:
        loop = None
        dispatcher = td.QueuedCallbackDispatcher()

    core = td.TelldusCore(callback_dispatcher=dispatcher)
    callbacks = []
    
    callbacks.append(core.register_device_event(device_event))
    callbacks.append(core.register_raw_device_event(raw_event))
    callbacks.append(core.register_sensor_event(sensor_event))
    
    try:
        while True:
            core.callback_dispatcher.process_pending_callbacks()
            time.sleep(0.5)
    except KeyboardInterrupt:
        pass
```
