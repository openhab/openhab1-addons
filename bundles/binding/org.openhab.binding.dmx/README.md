# DMX Binding

The openHAB DMX binding can control DMX devices like RGB LED strips, dimmers
and more.

<!--[Demo Video](http://www.youtube.com/watch?v=o0J4qyyfhio)-->
[![Demo Video](http://img.youtube.com/vi/o0J4qyyfhio/0.jpg)](http://www.youtube.com/watch?v=o0J4qyyfhio)

The DMX binding consists of 2 bundles: the core binding bundle
(org.openhab.binding.dmx.jar), which is responsible for managing the openHAB
items and commands and the device interface bundle, which is responsible for
sending commands to a DMX device.

The following device interface bundles are available:
- org.openhab.binding.dmx.artnet : An interface using Artnet (since 1.4.0)
- org.openhab.binding.dmx.ola : An interface using
[OLA](https://www.openlighting.org/ola/) as a bridge. This interface only
supports localhost bindings.


## Prerequisites

To use the DMX binding, install the core bundle and **one** device interface
bundle and add binding information to your configuration, then define
appropriate connection details.

If you use the OLA interface, you will also need to configure OLA.
See "Installing OLA" below. 


## Binding Configuration

The binding can be configured in the file `services/dmx.cfg`.

| Property   | Default | Required | Description                                              |
|------------|---------|:--------:|----------------------------------------------------------|
| connection |         | Yes      | A set of IP addresses and ports to use to connect        |
| repeatMode | always  | No       | How often the output will be refreshed (in milliseconds) |

`repeatMode` may take any of these values: always, never, reduced.

- `always` will send every update, regardless of a change
- `never` sends updates only when there has been a change (this is the behaviour
of pre-1.9.0 versions)
- `reduced` sends three updates if nothing has changed and then suppresses the
output for approximately 800 milliseconds


## Item Configuration

DMX channels can be linked to switch, dimmer and color items.

The DMX binding configuration contains a channel configuration and 0 or more
command configurations. It has the following structure:

    dmx="CHANNEL[<channel-config>], <OPENHAB-COMMAND>[<dmx-command>], <OPENHAB-COMMAND>[<dmx-command>], ..."


### Channel Configuration

There can be only one channel configuration structure per openHAB item.  The
channel configuration links one or more DMX channels to an item and it
contains the following structure:

    CHANNEL[<channels>/<channel-width>:<status-update-frequency>]

| Item                    | Description |
|-------------------------|-------------|
| channels                | CSV list of DMX channel numbers of the device |
| channel-width           | Optional width of the DMX channels on a device (e.g 1 for switch, 3 for RGB, 4 for RGBW).  When channel-width is used, only a single channel may be specified in `channels`.  If no value is specified in a binding to a color item, a default width of 3 is assumed. |
| status-update-frequency | Optional delay in milliseconds between status updates for continuously changing values.  If this value is omitted or a value less than 100 is used, no status updates are sent to the openHAB bus.  If the same DMX channel is linked to multiple openHAB items, at most one item should be configured with the status-update-frequency to prevent unnecessary events on the openHAB bus. |


### Command Configuration

Using the command configuration, behavior of the default openHAB commands for
the DMX devices can be overridden. The command configuration has the following
generic structure:

    <OPENHAB-COMMAND>[<dmx-command>|<command-parameters>|<command-parameters>|..]

| Item               | Description |
|--------------------|-------------|
| OPENHAB-COMMAND    | openHAB command to override, e.g. ON, OFF, INCREASE, DECREASE, etc. |
| dmx-command        | DMX-command name, e.g. FADE                                         |
| command-parameters | Repeatable command parameter sections for use with the DMX command  |


### FADE Command

The FADE command accepts the following parameter structure(s):

    <fade-time>:<target-channel-value>,<target-channel-value>,...:<hold-time>

| Item                 | Description |
|----------------------|-------------|
| fade-time            | The time (in milliseconds) to use to fade from the current values to the new target values. |
| target-channel-value | A CSV list of channel fade target values for each of the DMX channels.  The values must be in the range of 0-255.  If this list contains fewer values than the number of DMX channels specified in the channel configuration, the target values are repeated for the remaining DMX channels. |
| hold-time            | The time (in milliseconds) to hold the target values.  A value of -1 can be used to hold indefinitely. |


### SFADE Command

The SFADE (Suspending Fade) command is very similar to the FADE command.  It
differs from the FADE command in that it suspends any active fades before
executing a new fade.  After the fade has completed, the original fade which
was running before the SFADE started is resumed. It accepts the following
parameter structure(s):

    <fade-time>:<target-channel-value>,<target-channel-value>,...:<hold-time>


## Examples

### Configuration settings examples

| Interface | Example Configuration                  |
------------|-----------------------------------------
| Artnet    | connection=192.168.2.151,192.168.2.201 |
| OLA       | connection=localhost:9010              |
| Lib485    | connection=localhost:9020              |


### Channel configuration examples

1. RGB LED strip of which the first channel is 7 and which receives a maximum of 1 status update per second:

    Color rgb_strip_living_room "RGB Ledstrip Living Room" {dmx="CHANNEL[7:1000]"}

  The same binding could also be written as:

    Color rgb_strip_living_room "RGB Ledstrip Living Room" {dmx="CHANNEL[7,8,9:1000]"}

1. A binding to a dimmer item which is linked to only the blue channel of the RGB strip above:

    Dimmer rgb_strip_living_room_blue_only "Living Room Blue" {dmx="CHANNEL[9]"}

1. An RGBW LED strip starting at channel 20:

    Color rgbw_strip_kitchen "RGBW Ledstrip Kitchen" {dmx="CHANNEL[20/4:1000]"}

### Example Fade command configurations

1. A wake up light which takes 60 seconds to fade from nothing to full brightness.

    Dimmer light_bed_room “Light Bedroom” {dmx="CHANNEL[20:1000], ON[FADE|60000:255,255,255:-1]"}

1. A light which switches on to full brightness immediately and then fades out after 30 seconds

    Dimmer light_hall “Light Stairway Hall” {dmx="CHANNEL[25:1000], ON[FADE|0:255,255,255:30000|5000:0,0,0:-1]"}

1. An alternating green yellow fade on 2 RGB LED strips.

    Switch xmas_leds "Start Yellow-Green Fade Loop" {dmx="CHANNEL[7/6], ON[FADE|500:127,36,0,0,36,0:2000|500:0,36,0,127,36,0:2000]"}

### Example Suspending Fade command configurations

1. A short blue white flash pattern on the first 18 dmx channels, which will temporarily replace the active fades on those channels.

    Switch all_living_room_leds "Doorbell Flash"	{dmx="CHANNEL[1/18], ON[FADE|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:0,0,255:-1]"}

## Installing OLA

As a quick reference, an example installation is described below on how to
install OLA on a Raspberry Pi running Raspbian Wheezy and configure it for
use with an Entec Open DMX USB dongle.

For detailed instructions on how to install and configure OLA on different
platforms or with different devices, consult the [OLA documentation](https://www.openlighting.org/ola/getting-started/downloads/).  

### Installing OLA binaries

To install OLA, edit the software repository list using the following command:

    sudo nano /etc/apt/sources.list
    
And add the following line to the list:

    deb   http://apt.openlighting.org/raspbian  wheezy main
    
After saving the file, install the binaries with the following commands:

    sudo apt-get update
    sudo apt-get install ola
    
When prompted whether to start OLA at boot, select Yes.


### Configuring OLA devices

OLA can bind to many different protocols and devices (check
[here](https://www.openlighting.org/ola/) for a full
list of supported protocols and devices).  In a typical setup, only a
single device is needed.  To optimize performance, it is recommended to disable
all other devices and protocols which are not used.  

Depending on how OLA was installed, the OLA configuration files for the devices
and protocols will be in the directory /var/lib/ola/conf/ or ~/.ola.

The ola-ftdidmx.conf configuration file is needed for the Open DMX USB.
Disable all other configurations by editing the configuration files and
changing the value of the `enabled` property to `false`.

Next, open the following USB device rule file:

    sudo nano /etc/udev/rules.d/30-ftdidmx.rules

and add the following rule:

    ACTION=="add", BUS=="usb", SYSFS{idVendor}=="0403", SYSFS{idProduct}=="6001", GROUP:="dialout", MODE:="0660"

Restart the Raspberry to start OLA.

### Using a custom device connection interface

To use a custom interface rather than OLA can be easily achieved by creating a
new osgi bundle which implements the org.openhab.binding.dmx.DmxConnection
interface.

To use, simply deploy the core dmx bundle and the custom interface bundle.


## Notes

### The repeatMode configuration setting

Starting with 1.9.0 you can configure how often the output will be refreshed.
Every 35ms the binding checks whether one of the channels has changed. If
this is the case, the new values will be sent to the configured output. If
none of the channels have changed, prior versions of the binding never sent
updates. This works in most cases, however it is not compliant with the
underlying standards (not sending updates for 1 second is considered as DATA
LOST in the E1.11 standard; the receiver behaviour for this case is not
defined).

If using a directly connected interface (e.g. RS485), stay with the default
value of `always`. If using ArtNet, use the `reduced` option and minimize
network load.
