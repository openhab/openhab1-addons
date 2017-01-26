# TACmi Binding

This binding makes use of the CAN over Ethernet feature of the CMI from Technische Alternative. Since I only have the new UVR16x2, it has only been tested with this controller.

The binding currently supports the following functions:

* Receive data from analog outputs defined in TAPPS2
* Receive data from digital outputs defined in TAPPS2
* Send ON/OFF to digital inputs defined in TAPPS2

## Prerequisites

### Configure CAN outputs in TAPPS2

You need to configure CAN outputs in your Functional data on the UVR16x2. This can be done by using the TAPPS2 application from TA. Follow the user guide on how to do this.

### Configure your CMI for COE

Now follow the User Guide of the CMI on how to setup CAN over Ethernet (COE). Here you will map your outputs that you configured in the previous step. As the target device you need to put the IP of your OpenHAB server into the coe.csv. Don’t forget to reboot the CMI after you uploaded the coe.csv file.

## Binding Configuration

The binding can be configured in the file `services/tacmi.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| refresh  | 300000  |   No     | Refresh interval in milliseconds |
| cmiAddress |       |   Yes    | IP or hostname of the CMI device |

The refresh interval should be very small. The execute method of the binding blocks waits for new messages coming in for 10 seconds. If nothing has been received then the method is being restarted after the refresh interval has passed. If new data has been received then the first message is processed and the method is being restarted after the interval has passed. Messages are being processed one by one. Therefore, if you receive multiple message from the CMI, which is most likely the case, it always waits for the time configured in the refresh interval, before it processes the next one.

The CMI uses port 5441 for sending and receiving UDP packets.

## Item Configuration

The binding supports all 21 measure types that exist according to the TA documentation. Unfortunately, the documentation is not consistent here, so most of the types are supported only by generic names. The known measure types are:

* Temperature 
* Seconds
* Kilowatt
* Kilowatthours
* Megawatthours

For all other types, the generic names are

* Unknown2
* Unknown3
* Unknown5 to Unknown9
* Unknown13 to Unknown21

### Analog Outputs

```
{ tacmi="50#a#10#Temperature" }
```

* 50 = Target CAN Node as configured in the coe.csv file before
* a = analog output
* 10 = Number of the output as configured in TAPPS2
* Temperature = Measure Type of the analog value, currently only Temperature ans Seconds are supported

Example item:

```
Number TOelKessel "Kessel Temperatur [%.1f]" <heating> (Temperature, Heating_Chart) {tacmi="50#a#10#Temperature"}
```

### Digital Outputs

```
{ tacmi="50#d#5" }
```

* 50 = Target CAN Node as configured in coe.csv file before
* d = digital output
* 5 = Number of the output as configured in TAPPS2

Example item:

```
Switch Brenner "Brenner Anforderung" {tacmi="50#d#5"}
```

For the known measure types the binding also deals with the necessary scaling factors. All other measure types would be read "as is", so you might need to scale them accordingly. If you are able to figure out one of them, please update the binding or drop a note to @Wolfgang1966

Example item:

```
Number TOelKessel "Kessel Temperatur [%.1f]" <heating> (Temperature, Heating_Chart) {tacmi="50#a#10#Temperature"}
```

The total energy collected is split up into two measures (KWh and MWh) due to the limited range of analog values. To convert them, use a rule like this (UVRKwh and UVRMwh contain the received measures):

```
rule "UVR Total"
when Item UVRKwh received update
then
        var Number kwh = UVRKwh.state as DecimalType
        var Number mwh = UVRMwh.state as DecimalType
        if (kwh < 999.9 && kwh > 0 )
        {
                UVRTotal.postUpdate(kwh + 1000 * mwh)
        }
end
```

## Limitations

There was a bug in the CMI that messed up the UDP messages sent for analog outputs. As a workaround do not use the outputs 4, 8, 12, 16 and 17-32 in TAPPS2. The bug might have been fixed with recent updates of the CMI. This has not been tested though.

When defining an item that is being used as a digital input in your TAPPS2 then the binding only supports to use Digital Input 1. This is due to the way the COE protocol works as all 16 digital ports are being send within one message. 

For Analog Inputs it is only possible to use Analog Inputs 1, 5, 9, 13 in TAPPS2. This is also due to the way COE works as for analog messages it will send then in bundles of 4. You can however use multiple CAN nodes. According to TAPPS2 you can use CAN nodes from 1-64. So if you only have one UVR and the CMI then you should have 62 nodes available for use. I would not use the same item for sending and receiving even though that should be possible of you use one of the supported ports. Instead I would use separate items and then use rules to get the functionality that you want to achieve. That way it should be possible to have a switch item that is being updated by a CAN output and when you send a command to it, then it uses a different item with a different CAN node that maps to a CAN Input in TAPPS2. I have not tried this though yet.
