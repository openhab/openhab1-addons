# How to use the TACmi binding

This binding makes use of the CAN over Ethernet feature of the CMI from Technische Alternative. Since I only have the new UVR16x2, it has only been tested with this controller.

The binding currently supports the following functions:
* Receive data from analog outputs defined in TAPPS2
* Receive data from digital outputs defined in TAPPS2
* Send ON/OFF to digital inputs defined in TAPPS2

The configuration consists of the following steps:

1. Copy the binding into your addons folder
2. Configure the binding in openhab.cfg
3. Configure CAN outputs in TAPPS2
4. Configure your CMI for COE
5. Add items to your Openhab configurations


## Copy the binding into your addons folder


Simply copy the jar file into your addons directory.

## Configure the binding in openhab.cfg

The binding has only 2 configuration parameters that need to be set in openhab.cfg
```
############################## TACmi Binding ##############################
#
# Refresh interval in milliseconds (optional, defaults to 300000)
tacmi:refresh=20
# IP or hostname of the CMI device
tacmi:cmiAddress=10.10.10.10
```

The refresh interval should be very small. The execute method of the binding blocks waits for new messages coming in for 10 seconds. If nothing has been received then the method is being restarted after the refresh interval has passed. If new data has been received then the first message is processed and the method is being restarted after the interval has passed. Messages are being processed one by one. Therefore, if you receive multiple message from the CMI, which is most likely the case, it always waits for the time configured in the refresh interval, before it processes the next one.
The CMI uses port 5441 for sending and receiving UDP packets.

## Configure CAN outputs in TAPPS2

You need to configure CAN outputs in your Functional data on the UVR16x2. This can be done by using the TAPPS2 application from TA. Follow the user guide on how to do this.

## Configure your CMI for COE

Now follow the User Guide of the CMI on how to setup CAN over Ethernet (COE). Here you will map your outputs that you configured in the previous step. As the target device you need to put the IP of your OpenHAB server into the coe.csv. Don’t forget to reboot the CMI after you uploaded the coe.csv file.

## Add items to your Openhab configurations

Now you are ready to configure your items in OpenHAB.
The syntax is as follows:

### Analog Outputs

{tacmi="50#a#10#Temperature"}
* 50 = Target CAN Node as configured in the coe.csv file before
* a = analog output
* 10 = Number of the output as configured in TAPPS2
* Temperature = Measure Type of the analog value, currently only Temperature ans Seconds are supported

Example item:
```
Number TOelKessel "Kessel Temperatur [%.1f]" <heating> (Temperature, Heating_Chart) {tacmi="50#a#10#Temperature"}
```

### Digital Outputs

{tacmi="50#d#5"}
* 50 = Target CAN Node as configured in coe.csv file before
* d = digital output
* 5 = Number of the output as configured in TAPPS2

Example item:
```
Switch Brenner "Brenner Anforderung" {tacmi="50#d#5"}
```

# Limitations

Currently there is a bug in the CMI that messes up the UDP messages sent to analog outputs. As a workaround do not use the outputs 4, 8, 12, 16 and 17-32 in TAPPS2.

Currently the binding only supports to send digital values to the CMI.
When defining an item that is being used as a digital input in your TAPPS2 then the binding only supports to use Digital Input 1. This is due to the way the COE protocol works as all 16 digital ports are being send within one message. Once analog values will be supported it will only be possible to use Analog Inputs 1, 5, 9, 13 in TAPPS2. This is also due to the way COE works as for analog messages it will send then in bundles of 4. You can however use multiple CAN nodes. According to TAPPS2 you can use CAN nodes from 1-64. So if you only have one UVR and the CMI then you should have 62 nodes available for use. I would not use the same item for sending and receiving even though that should be possible of you use one of the supported ports. Instead I would use separate items and then use rules to get the functionality that you want to achieve. That way it should be possible to have a switch item that is being updated by a CAN output and when you send a command to it, then it uses a different item with a different CAN node that maps to a CAN Input in TAPPS2. I have not tried this though yet.



