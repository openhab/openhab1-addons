# Nikobus Binding

This binding allows openHAB to interact with the [Nikobus](http://www.niko.eu/enus/niko/products/home-automation-with-nikobus/) home automation system. 

[![Demo Video Nikobus](http://img.youtube.com/vi/QiNb-8QxXpo/0.jpg)](http://www.youtube.com/watch?v=QiNb-8QxXpo)

More specifically, it allows openHAB to:

* send (simulated) button presses to the Nikobus
* react to button presses which occur on the Nikobus
* change the status of switch channels on a Nikobus switch module
* request the status of switch channels on a Nikobus switch module
* change the status of dimmer channels on a Nikobus dimmer module
* request the status of dimmer channels on a Nikobus dimmer module
* send commands to the Nikobus roller shutter module

This binding works with at least the following hardware:

* PC-link module (05-200)
* Push buttons (05-060-01, 05-064-01), RF Transmitter (05-314), PIR Sensor (430-00500)
* 4 channel switch module (05-002-02)
* 12 channel switch module (05-000-02)

## Binding Configuration

This binding can be configured in the file `services/nikobus.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| serial.port |      |    Yes   | The name of the serial port device to which the PC-Link module is connected.  For example: `/dev/ttyUSB0` |
| refresh  | 600     |    No    | Rate in seconds at which a module status query is performed.  When a value greater than 0 is specified, the binding will perform a status query for a channel group every `refresh` seconds. In order to not flood the bus with too many status requests (if you have many modules), the refresh only requests the status of a single channel group. After the next interval, the status of the following channel group is requested and so on... |

## Item Configuration

### Buttons

Once an openHAB item has been configured as a Nikobus button, it will receive a status update to ON when the physical button is pressed.  When an item receives the ON command from openHAB, it will send a simulated button press to the Nikobus. This means you could also define virtual buttons in openHAB with non-existing addresses (e.g., `#N000001`) and use those in the programming of your Nikobus.

To configure an item for a button in openHAB, use the following format:

```
nikobus="<address>:<pressType>[<moduleAddress>-<channelGroup>, <moduleAddress>-<channelGroup>, ...]"
```

where:

* `<address>` is the address of the button.  See below for how to determine addresses.
* `<pressType>` is an indication for a `SHORT` (less than one second) or `LONG` button press. `:<pressType>` is optional and defaults to `:SHORT` if not provided. A button which is configured as `LONG` will not be triggered by any `SHORT` presses of that button. A button which is configured as `SHORT` will not be triggered by any `LONG` presses of that button.
* `<moduleAddress>` represents the address of the switch module
* `<channelGroup>` represents the first or second channel group in the module

Since all the channels in the entire channel group are switched to their new state, it's important that openHAB knows the current state of all the channels in that group. Otherwise a channel which was switched on by a button, may be switched off again by the command.

The section:

```
[<moduleAddress>-<channelGroup>, <moduleAddress>-<channelGroup>, ...]
```

is optional, and is used to include detail on which channel groups the button press affects. Every status query takes between 300-600 ms, so to get the best performance, only add the affected channel groups in the configuration.

Examples of button items:

```
Switch Office_Top_Left_S "Office Light On/Off Short Press" { nikobus="#N003334:SHORT" }
Switch Office_Top_Left_L "Office Light On/Off Long Press"  { nikobus="#N003334:LONG" }
Switch Kitchen_Light     "Kitchen Light On/Off"            { nikobus="#N003333" }
```

or

```
Switch Office_Top_Left_S "Office Light On/Off Short Press" {nikobus="#N003334:SHORT[C964-1]"}
Switch Office_Top_Left "Office and Kitchen Light On/Off" {nikobus="#N006884[C964-1,C964-2]"}
```

### Switches

To configure an item for a switch in openHAB, use the following format:

```
nikobus="<moduleAddress>:<channel>"
```

where:

* `<moduleAddress>` is the address of the switch module.  See below for determining addresses.
* `<channel>` is a number in the range 1-12 indicating the switch channel

Examples of switch items:

```
Switch Office_Top_Left_S "Office Light On/Off Short Press" { nikobus="#N003334:SHORT[C964-1]" }
Switch Office_Top_Left "Office and Kitchen Light On/Off" { nikobus="#N006884[C964-1,C964-2]" }
```

### Discovering Module Addresses and Channels

In order to configure items in openHAB, you will first need to know the module addresses and channels of your switch and dimmer modules.

This binding's root logger is `org.openhab.binding.nikobus`.  Enable `DEBUG` logging to discover module addresses and channels that will be logged.  In openHAB 2, this can be performed from the console at the `openhab>` prompt:

```
log:set DEBUG org.openhab.binding.nikobus
log:tail
```

Press Control-C to stop monitoring the log and return to the prompt.

When done, you can return the binding's logging to the quieter INFO level.



### Configuring a Switch Module

The binding supports both the 4 channel switch module (05-002-02) and the 12 channel switch module (05-000-02).

The large module contains 2 channel groups, where the first group controls channels 1-6 and the second one controls channels 7-12.  The small module contains only a single channel group controlling all 4 channels.

All commands sent to/received from the Nikobus switch module are for a single channel group.

In order to be able to read the status of a Nikobus switch module channel or to switch a channel directly on the switch module without mimicking a button press, you will need to configure items for each channel on the switch modules.

To find out the address of your switch module, press and hold the yellow "mode" button on the switch module until you hear a beep. This will trigger the module to send out its identification on the bus, which should then be logged in openHAB.  When you check the openHAB log, you should see an entry similar to:

```
... Received NikobusCommand [command=$18C96400100167FF78607E, repeats=1]
```

The four characters following `$18` are the switch module address. In the example above, that's `C964`.  With this address, you can now define the different channels in your item configuration, e.g:

```
Switch light_office {nikobus="C964:1"}
Switch light_hallway {nikobus="C964:2"}
Switch light_living {nikobus="C964:3"}
Switch light_kitchen {nikobus="C964:4"}
Switch light_diningroom {nikobus="C964:5"}
Switch light_toilet {nikobus="C964:6"} 
 ```

Once the channels are defined, you can switch them ON or OFF using regular openHAB commands.

Changing the status of the switch module channels is always done for a complete channel group. So sending an ON command to the `light_office` item in the example above sets the status for channels 1-6: channel 1 will be set to ON and channel 2 to 6 will be set to whatever the last state was that openHAB has for those channels.

Since all the channels in the entire channel group are switched to their new state, it's important that openHAB knows the current state of all the channels in that group. Otherwise, a channel which was switched on by a button may be switched off again by the command.

In order to keep an up to date state of the channels in openHAB, button configurations can be extended to include detail on which channel groups the button press affects.

When configured, the status of the channel groups to which the button is linked, will be queried every time the button is pressed.
Every status query takes between 300-600 ms, so to get the best performance, only add the affected channel groups in the configuration, which has the following format:

```
Switch myButton {nikobus="<button>[<moduleAddress>-<channelGroup>, <moduleAddress>-<channelGroup>, ...]"}
```

Where `<button>` represents the standard button configuration as explained in the previous section; `<moduleAddress>` represents the address of the switch module; `<channelGroup>` represents the first or second channel group in the module. 

Example configurations may look like:

```
Switch Office_Top_Left_S "Office Light On/Off Short Press" {nikobus="#N003334:SHORT[C964-1]"}
Switch Office_Top_Left "Office and Kitchen Light On/Off" {nikobus="#N006884[C964-1,C964-2]"}
```
