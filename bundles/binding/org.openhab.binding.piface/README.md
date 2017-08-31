# Piface Binding

A Piface extension board is attached to a Raspberry Pi via the GPIO connector. It has 8 digital input and 8 digital output pins. It is not possible to set the value of input pins, or read the value of output pins. There is plenty of documentation on the web about the Piface board and how it can be used.

## Prerequisites

In order to communicate with the Raspberry Pi carrying the PiFace daughter board please install the scripts that can be found [here](https://github.com/openhab/openhab1-addons/tree/master/bundles/binding/org.openhab.binding.piface/scripts). If you upgrade from an older openHAB version make sure that you update the scripts to avoid watchdog errors in your logfile.

In newer RasPi kernel versions the hardware resource allocation is handled by the [device tree](http://www.raspberrypi.org/documentation/configuration/device-tree.md). This may stop the SPI kernel module from loading after an update of the RasPi firmware (**rpi-update** command) even if the the ohpiface scripts are correctly installed and where already working. If you experiencing issues please

1. Check if the SPI kernel module is loaded
1. Check if the SPI kernel module can be loaded
1. Modify **/boot/config.txt** to enable SPI in the device tree
1. Reboot your Raspberry

### Check if the SPI kernel module (spi_bcm2708 or spi_bcm2835) is loaded

The command 

```
lsmod | grep spi_bcm2708
```

or

```
lsmod | grep spi_bcm2835
```

checks if the kernel module is loaded. If the module is loaded you will get output like

```
spi_bcm2708             6018  0
```

if not - no output is generated.

### Check if the SPI kernel module can be loaded

If the module is not loaded you may try

```
modprobe spi_bcm2708
```

to load the kernel module. If you get an error message 

### Enable SPI in the device tree

Add the line

```
device_tree_param=spi=on
```

to the file `/boot/config.txt` to enable SPI in the device tree and reboot. The kernel module should now load into the kernel and the ohpiface scripts should work again.

## Binding Configuration

This binding can be configured in the file `services/piface.cfg`.

The binding supports multiple Piface devices by specifying a unique `<PifaceId>` in the binding configuration. The `<PifaceId>` is used in the item configuration to identify which device it is associated with.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<Pifaceid>`.host | |   Yes   | Host IP address of the Piface device |
| `<Pifaceid>`.listenerport | 15432 | No | Port the Piface device is listening on for commands FROM the binding |
| `<Pifaceid>`.monitorport | 15433 | No | Port the Piface device uses to send updates TO the binding |
| `<Pifaceid>`.sockettimeout | 1000 | No | Socket timeout in milliseconds when sending packets |
| `<Pifaceid>`.maxretries | 3 | No | Number of retry attempts before failing a packet |
| watchdog.interval | 60000 | No | Watchdog polling interval in milliseconds |

where `<Pifaceid>` is a simple name you choose for a specific Piface device; the name is used in your item configurations (see below).  Do not use `watchdog` for `<Pifaceid>` as it is reserved.  You can repeat entries in `services/piface.cfg` to address multiple Piface devices.

## Item Configuration

The binding supports Switch and Contact item types only. In order to associate an item with a particular Piface pin/port, you must first define the `<PifaceId>`, as configured in the binding configuration, and then specify the pin type and number.

The syntax (by way of example) of the item configuration is shown below:

```
/* Two Piface items - an OUT switch, and an IN contact */
Switch     PifaceSwitch1       "Switch 1"  { piface="pifaceid1:OUT:1" }
Contact    PifaceContact1      "Contact 1" { piface="pifaceid1:IN:1" }
```

In this example the first item is a Switch, which when activated in openHAB, will set OUTPUT pin 1 on the Piface board high/low. I.e. this is enabling openHAB to turn on/off something in the real-world - e.g. a garage door opener.

The second item is a Contact item which will be updated to open/closed when INPUT pin 1 on the Piface board is set high/low. I.e. this is reading the state of something in the real-world - e.g. it could be a reed switch on a garage door.

When the binding is initialised, it will attempt to read the current state of any INPUT pins and update the items on the openHAB event bus. Output pins cannot be read currently so should be initialised to a known state by a rule in your openHAB configuration.

### Watchdog

WATCHDOG monitor ensures your Pi is connected and responding. No further configuration is required as long as you do not want to make use of the watchdog feature. 

By binding a switch/contact item to the WATCHDOG command for a Piface board you will be able to check your Pi is alive. The watchdog polling interval is configured in your [binding configuration](#binding-configuration).

```
Switch  PiFaceWatchDog1        "Watchdog 1"  { piface="pifaceid1:WATCHDOG" }
```

## Examples

Below is an example of my setup. I am using one of the output relays to control my underfloor heating system, and the other to activate my garage door opener. I also have 3 inputs configured, one from the underfloor thermostat controller, one from a reed switch on my garage door, and one attached to a homemade water sensor (just two pieces of aluminium foil wrapped around a piece of plastic with a 5mm gap).

### Item Definitions

```
// Underfloor heating
Switch   UFThermostat      "Underfloor Thermostat" <heating>        { piface="piface1:IN:0" }
Switch   UFHeatpump        "Underfloor Heatpump"   <heating>        { piface="piface1:OUT:1" }

// Garage door
Contact  GarageDoor        "Garage Door [%s]"      <garagedoor>     { piface="piface1:IN:3" }
Switch   GarageDoorOpener  "Garage Door Opener"    <remotecontrol>  { piface="piface1:OUT:0" }

// Hot Water Cylinder cupboard water sensor
Switch   HWCWaterSensor    "HWC Water Sensor"      <watersensor>    { piface="piface1:IN:7" }
```

### Heating Rules

The rules for these items are below. The underfloor heating rule is pretty simple, if the thermostat decides it is time to heat openHAB simply passes this signal onto the heat pump, unless I am away on holiday! There is also a rule to check if the forecast is for a cold day ahead, and if the heat pump isn't already running at 5am, to give the heating a boost for a couple of hours. It should be noted I only run the heat pump from 9pm till 7am as this is when I get cheap power.

```
// turn on/off the underfloor heatpump based on the thermostat
rule "Underfloor heating"
when
    Item UFThermostat changed
then
    // don't turn on heating if we are on 'holiday' (unless there is a house sitter)
    if (Holidays_Holiday.state != ON || Presence_HouseSitter.state == ON) {
        if (UFThermostat.state == ON)
            sendCommand(UFHeatpump, ON)
        else
            sendCommand(UFHeatpump, OFF)
    }
end

// if the underfloor heatpump is off at 5am and tomorrow is looking
// cold then we give the underfloor a boost for a couple of hours
rule "Underfloor boost"
when
    Time cron "0 0 5 * * ?"
then
    // the minimum forecast max temperature before we attempt to boost
    var boostTempThreshold = 10

    // don't turn on heating if we are on 'holiday' (unless there is a house sitter)
    if (Holidays_Holiday.state != ON || Presence_HouseSitter.state == ON) {
        // if the heat pump is currently off then check the weather forecast for today
        if (UFHeatpump.state == OFF && Weather_TodayMax.state < boostTempThreshold) {
            sendCommand(UFHeatpump, ON)
            sendTweet("Boosting underfloor heating since it looks chilly today")
        }
    }
end

rule "Shutoff underfloor heating"
when
    Time cron "0 0 7 * * ?"
then
    if (UFHeatpump.state == ON) {
        sendCommand(UFHeatpump, OFF);
    }
end
```

### Garage Door Rules

The garage door opener is a simple rule that effectively turns the switch into a 'button press', simulating the button being held down for one second. I also have a few notifications setup so any XBMC instances that are running get a popup when the garage door is opened. There are other rules linked to the garage door as well, i.e. to turn on the internal garage light for 5 mins when the door is opened.

```
var Timer openGarageDoorTimer = null

rule "Garage door opener"
when
    Item GarageDoorOpener received command ON
then
    if (openGarageDoorTimer != null)
        openGarageDoorTimer.cancel()

    // release the garage door 'opener' after a short delay
    openGarageDoorTimer = createTimer(now.plusSeconds(1)) [|
        sendCommand(GarageDoorOpener, OFF)
    ]
end

rule "Garage door open notification"
when
    Item GarageDoor changed to OPEN
then
    sendXbmcNotification("192.168.1.40", 80, "openHAB Alert", "Garage door is open!")
    sendXbmcNotification("192.168.1.41", 80, "openHAB Alert", "Garage door is open!")
end
```

### HWC Sensor Rules

The HWC sensor rule is there to alert me of any leak and to 'dampen' the notification frequency, since my sensor tends to change rapidly when it gets wet - flicking on/off very quickly!

```
rule "HWC Water Sensor"
when
    Item HWCWaterSensor changed to ON
then
    // the water sensor can rapidly switch on/off when water detected - don't want 100's of alerts
    // so ignore all activations for 5 mins once it is triggered
    if (!ignore_HWCWaterSensor) {
        // send an email and notification
        sendMail("xxx@xxx.com", "openHAB Alert", "WATER detected in HWC cupboard!!!")
        sendXMPP("xxx@xxx.com", "WATER detected in HWC cupboard!!!")

        // send a notification to both XBMCs
        sendXbmcNotification("192.168.1.40", 80, "openHAB Alert", "WATER detected in HWC cupboard!!!")
        sendXbmcNotification("192.168.1.41", 80, "openHAB Alert", "WATER detected in HWC cupboard!!!")

        // we only send the alerts once every 5 mins
        ignore_HWCWaterSensor = true

        if (timer_HWCWaterSensor != null)
            timer_HWCWaterSensor.cancel()

        // reset the ignore sensor flag after 5 mins
        timer_HWCWaterSensor = createTimer(now.plusMinutes(5)) [|
            ignore_HWCWaterSensor = false
        ]
    }
end
```
