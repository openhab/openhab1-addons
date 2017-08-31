## ISY Binding

This is a openHAB binding for the Universal Devices ISY 994i router and PLM. Universal Devices offers a Java SDK for
external programs to communicate with the router. Alternatives include an extensive REST API. See details at
https://www.universal-devices.com/isy-developers/.

## Items Configuration with ISY Binding Details

The following settings can be added to the Items definition.

* ctrl - Controller address in the format "x.y.z.w". This can be the address of a ISY Insteon Scene. Insteon addresses use the first three bytes and the last byte identified a particular capability of the device.
* type - This can be used to select device types with special configuration needs.  See the "Device Types" section below.
* cmd - Specific ISY control to send to the ISY router as a command, **ST** is the default if left out
* addr - The Insteon address to monitor for the change. A scene does not report a status change, only the devices in the scene do. Specify the controller of the scene here. Defaults to the controller address if left out

## Device Types

You can specify a specific device type to let the binding know that a device is a special type.  This is done using the "type=" configuation item in the binding.

There are currently 3 types available:

* thermostat - required to perform temperature calculations properly
* lock - locks use the security command instead of the normal off/on commands
* heartbeat - several battery powered sensors send a heartbeat.  This is just a ST (status) with value 255, and it means that the sensor is still alive.  To make rules easier, you can define these as type=heartbeat with an item type of DateTime, and it will update with the current time every time the heartbeat is received.  You can then check the item using a rule to see if the DateTime item is too old.

## Item Examples

        /* Insteon-enabled thermostat.  Note "type=thermostat" to ensure proper temp calculations, and cmd=CLISPH to use the ISY setpoint heat value instead of status */
        Number Temperature_Indoor "Inside [%.2f °F]" <temperature> (All) {isy="ctrl=29.24.98.1,type=thermostat,cmd=ST"}
        Number Temperature_Setpoint		"Desired Temperature [%.2f °F]" <temperature> (All) {isy="ctrl=29.24.98.1,type=thermostat,cmd=CLISPH"}

        /* Regular ISY scene id #12220, but look at device 30.CB.66.1 to get the scene status */
        Switch Light_Hallway2 "Hallway" <switch> (GroundFloor,Lights) {isy="ctrl=12220,addr=30.CB.66.1"}
        /* Regular ISY device, controlled directly (won't update scenes!)
        Switch K_DiningRoom_Light "Dining Room Light" (GroundFloor,Lights) {isy="ctrl=F.C3.7C.1"}

        /* ZWave lock.  Note type=lock (makes it use the security commands necessary). The second (Number) entry watches the BATLVL value, which battery-powered devices use to report battery level in percent */
        Switch BDoorLock "Back Door Lock" <lock> (Locks) {isy="ctrl=ZW004_1,type=lock"}
        Number BDoorLock_Battery "Back Door Lock Battery [%d]" <battery> (Locks,BattLevels) {isy="ctrl=ZW004_1,cmd=BATLVL"}

        /* Insteon FanLinc, set up with a Keypadlinc.  4 scenes - Off, Low, Med, High */
        Switch LR_Fan_Off "LR Fan Off" (ISYScenes) {isy="ctrl=4622,addr=1c.e2.d1.3", autoupdate="false"}
        Switch LR_Fan_Low "LR Fan Low" (ISYScenes) {isy="ctrl=20771,addr=1c.e2.d1.4", autoupdate="false"}
        Switch LR_Fan_Med "LR Fan Med" (ISYScenes) {isy="ctrl=22031,addr=1c.e2.d1.5", autoupdate="false"}
        Switch LR_Fan_High "LR Fan High" (ISYScenes) {isy="ctrl=11187,addr=1c.e2.d1.6", autoupdate="false"}

        /* Insteon water sensor - it has 3 subdevices: 1 = dry, 2 = wet, 4 = heartbeat
           The heartbeat device sends an "ON" (255) ST code as a heartbeat.  With type=heartbeat, the binding
           sends a DateTime value.  You can use a rule to check that DateTime to see if you have received
           a heartbeat recently */
        Contact Water_Heater_Dry "Water Heater Sensor [MAP(watersensor-en.map):%s]" (Sensors) {isy="addr=25.AD.4F.1"}
        DateTime Water_Heater_Heartbeat "Water Heater HB [%1$td.%1$tm.%1$tY %1$tH:%1$tM]" (Sensors) {isy="addr=25.AD.4F.4", type=heartbeat}

The transform file (watersensor-en.map) for the water sensor is:

        CLOSED=Wet
        OPEN=Dry
        undefined=Unknown
        -=Unknown

## ISY Binding Configuration

The following settings configure the ISY binding in the openhab.cfg file.

* isy:refresh=60000 # refresh interval in milliseconds (optional, defaults to 60000 [1 minute])
* isy:upnp=true # if true, use UPNP to communicate with the ISY 994i
* isy:uuid=uuid:%your_uudid% # UUID of the ISY router
* isy:ip=192.168.x.x # the hostname of the ISY router
* isy:port=80 # the port of the ISY router
* isy:user=user # the user of the ISY router
* isy:password=password # the password of the ISY router
