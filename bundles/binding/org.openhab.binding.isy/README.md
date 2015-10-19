## Description

This is a openHAB binding for the Universal Devices ISY 994i router and PLM. Universal Devices offers a Java SDK for
external programs to communicate with the router. Alternatives include an extensive REST API. See details at
https://www.universal-devices.com/isy-developers/.

## Instructions

To build and install the isy binding, you need to first download the Java SDK that corresponds with the firmware version
of your ISY 994i.


1. [Download](https://www.universal-devices.com/isy-developers/.) the latest Java SDK zip file, e.g. ISY-WSDK-4.2.21.zip
1. Unzip it to some local folder
1. Copy the isy_inst.jar file into the org.openhab.binding.isy/lib folder
1. Provided that the API and the file name have not changed, there should not be a need to change the source code. Watch for
   compilation errors and adjust accordingly.
1. Configure the ISY binding in the openhab.cfg file. See below for configuration details
1. Build and deploy the binding like any other binding into the addons folder of a openHAB runtime

## Items Configuration with ISY Binding Details

The following settings can be added to the Items definition.

* ctrl - Controller address in the format "x.y.z.w". This can be the address of a ISY Insteon Scene. Insteon addresses use the first three bytes and the last byte identified a particular capability of the device.
* type - Only **thermostat** is supported at this time. This ensures that the temperature is properly calculated
* cmd - Specific ISY control to send to the ISY router as a command, **ST** is the default if left out
* addr - The Insteon address to monitor for the change. A scene does not report a status change, only the devices in the scene do. Specify the controller of the scene here. Defaults to the controller address if left out

Examples:

        Number Temperature_Indoor "Inside [%.2f °F]" <temperature> (All) {isy="ctrl=29.24.98.1,type=thermostat,cmd=ST"}
        Number Temperature_Setpoint		"Desired Temperature [%.2f °F]" <temperature> (All) {isy="ctrl=29.24.98.1,type=thermostat,cmd=CLISPH"}
        Switch Light_Hallway2 "Hallway" <switch> (GroundFloor,Lights) {isy="ctrl=12220,addr=30.CB.66.1"}

## ISY Binding Configuration

The following settings configure the ISY binding in the openhab.cfg file.

* isy:refresh=60000 # refresh interval in milliseconds (optional, defaults to 60000 [1 minute])
* isy:upnp=true # if true, use UPNP to communicate with the ISY 994i
* isy:uuid=uuid:%your_uudid% # UUID of the ISY router
* isy:ip=192.168.x.x # the hostname of the ISY router
* isy:port=80 # the port of the ISY router
* isy:user=user # the user of the ISY router
* isy:password=password # the password of the ISY router


## Disclaimer

There is no guarantee for the function of the binding or its correctness. Use at your own risk.