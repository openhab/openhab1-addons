## Installation
You need the CUL transport (`org.openhab.io.transport.cul`) and the binding (`org.openhab.binding.hms`) in your addons folder.
## Configuration
In your openhab.cfg you need to specify which serial device is the CUL device. This simply done via

`hms:device=serial:/dev/ttyACM0`

if your serial device is /dev/ttyACM0
## Item configuration
This binding can only handle HMS components returning temperature or humidity values. Since these values are of type number, it only makes sence to use items of type Number or Text. In your items configuration file this could look like

`Number Temperature_Outdoor "Temp [%.1f °C]"      <temperature>	(Weather)	{hms="address=A1DB;datapoint=TEMPERATURE"}`

`Number Humidity_Outdoor     "Humidity [%.1f %%]" <humidity>	(Weather)	{hms="address=A1DB;datapoint=HUMIDITY"}`

In the example above the address of the HMS device is `A1DB`. By use of the attribute `datapoint` one can either assign the `HUMIDITY` or the `TEMPERATURE` value to a device.

If you don't know the address of your HMS device, simply search for lines like the following in your openhab.log, where the device address is printed out every time a new value is received. Keep in mind that new values are only reported every 5 minutes.

`08:10:05.892 INFO  o.o.b.hms.internal.HMSBinding[:124]- device: A1DB, T:  14.5,	H: 77.5, Bat.: ok`

