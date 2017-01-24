Documentation of the DMX binding Bundle<br/>
<!--[Demo Video](http://www.youtube.com/watch?v=o0J4qyyfhio)-->
[![Demo Video](http://img.youtube.com/vi/o0J4qyyfhio/0.jpg)](http://www.youtube.com/watch?v=o0J4qyyfhio)

## Introduction

The DMX binding is available as a separate download. 

Using this binding, you can control DMX devices like RGB led strips, dimmers and more from within openHAB.  

The DMX binding consists out of 2 bundles: the core binding bundle (org.openhab.binding.dmx.jar), which is responsible for managing the openhab items and commands and the device interface bundle, which is responsible for sending commands to a DMX device.

The following device interface bundles are available:
- org.openhab.binding.dmx.artnet : An interface using Artnet (Available as of 1.4.0. only!)
- org.openhab.binding.dmx.ola : An interface using [OLA](http://opendmx.net/index.php/OLA) as a bridge. This interface only supports localhost bindings.
- org.openhab.binding.dmx.lib485 : This interface will provide an interface to the [dmx485 library](http://dmx485.sourceforge.net/). This interface is currently in an experimental stage and should not be used.

To use the DMX binding, install the CORE bundle and ONE device interface bundle in the folder ${openhab_home}/addons and add binding information to your configuration. Depending on which interface bundle you use, you will need to define appropriate connection details in the openhab.cfg file. The table below lists some sample configurations:

<table>
<tr><th>Interface</th><th>Example Configuration</th><th>Comment</th></tr>
<tr><td>Artnet</td><td>dmx:connection=192.168.2.151,192.168.2.201</td><td></td></tr>
<tr><td>OLA</td><td>dmx:connection=localhost:9010</td><td></td></tr>
<tr><td>Lib485</td><td>dmx:connection=localhost:9020</td><td></td></tr>
</table>

Starting with 1.9.0 you can configure how often the output will be refreshed. Every 35ms the binding checks whether one of the channels has changed. If this is the case, the new values will be send to the configured output. If none of the channels have changed, prior versions of the binding did never send updates. This works in most cases, however it is not compliant with the underlying standards (not sending updates for 1s is considered as DATA LOST in the E1.11 standard, the receiver behaviour for this case is not defined). There is a new configuration option
     `repeatMode = <value>`
where `always` is the default value and will send every update, regardless of a change. Other options are `never` which is the behaviour of pre-1.9.0 versions and `reduced` which sends three updates if nothing has changed and then suppresses the output for approx. 800 ms. If you have a directly connected interface (e.g. RS485) you should stay with the default. If you use ArtNet you can use the reduced option and minimize network load.
 
If you use the OLA interface, you will also need to configure OLA.  
More information on configuring the binding and installing OLA is available in the next sections.

## Generic Item Binding Configuration

In order to bind an item to a DMX channel, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items). 

DMX channels can be linked to switch, dimmer and color items.

The DMX binding configuration contains a channel configuration and 0 or more command configurations. It has the following structure:

    dmx="CHANNEL[<channel-config>], <OPENHAB-COMMAND>[<dmx-command>], <OPENHAB-COMMAND>[<dmx-command>], ..."

### Channel Configuration

There can be only one channel configuration structure per openHAB item.  The channel configuration links one or more DMX channels to an item and it contains the following structure:
    CHANNEL[<channels>/<channel-width>:<status-update-frequency>]

<table>
  <tr><td>channels</td><td>CSV list of DMX channel numbers of the device.</td></tr>
  <tr><td>channel-width</td><td>Optional width of the DMX channels on a device (e.g 1 for switch, 3 for RGB, 4 for RGBW).  When channel-width is used, only a single channel may be specified in 'channels'.  If no value is specified in a binding to a color item, a default width of 3 is assumed.</td></tr>
  <tr><td>status-update-frequency</td><td>Optional delay in ms between status updates for continuously changing values.  If this value is omitted or a value < 100 is used, no status updates are sent to the openHAB bus.  If you linked the same DMX channel to multiple openHAB items, you should have at most one item configured with the status-update-frequency to prevent unnecessary events on the openHAB bus.</td></tr>
</table>


### Example Channel Configurations

1) RGB led strip of which the first channel is 7 and which receives a maximum of 1 status update per second.

    Color rgb_strip_living_room "RGB Ledstrip Living Room" {dmx="CHANNEL[7:1000]"}
The same binding could also be written as:

    Color rgb_strip_living_room "RGB Ledstrip Living Room" {dmx="CHANNEL[7,8,9:1000]"}
2) A binding to a dimmer item which is linked to only the blue channel of the RGB strip above:

    Dimmer rgb_strip_living_room_blue_only "Living Room Blue" {dmx="CHANNEL[9]"}
3) An RGBW led strip starting at channel 20.

    Color rgbw_strip_kitchen "RGBW Ledstrip Kitchen" {dmx="CHANNEL[20/4:1000]"}

### Command Configuration

Using the command configuration, you can override behavior of the default openHAB commands for the DMX devices. The command configuration has the following generic structure:

    <OPENHAB-COMMAND>[<dmx-command>|<command-parameters>|<command-parameters>|..]

<table>
  <tr><td>OPENHAB-COMMAND</td><td>Openhab command to override, e.g. ON, OFF, INCREASE, DECREASE, ...</td></tr>
  <tr><td>dmx-command</td><td>DMX-command name, e.g. FADE</td></tr>
  <tr><td>command-parameters</td><td>Repeatable command parameter sections for use with the DMX command</td></tr>
</table>

### FADE Command

The FADE command accepts the following parameter structure(s):

    <fade-time>:<target-channel-value>,<target-channel-value>,...:<hold-time>

<table>
  <tr><td>fade-time</td><td>The time in ms to use to fade from the current values to the new target values.</td></tr>
  <tr><td>target-channel-value</td><td>A CSV list of channel fade target values for each of the DMX channels.  The values must be in the range of 0-255.  If this list contains fewer values than the number of DMX channels specified in the channel configuration, the target values are repeated for the remaining DMX channels.</td></tr>
  <tr><td>hold-time</td><td>The time in ms to hold the target values. A value of -1 can be used to hold indefinitely.</td></tr>
</table>

### Example Fade Configurations

1) A wake up light which takes 60 seconds to fade from nothing to full brightness.

    Dimmer light_bed_room “Light Bedroom” {dmx="CHANNEL[20:1000], ON[FADE|60000:255,255,255:-1]"}

2) A light which switches on to full brightness immediately and then fades out after 30 seconds

    Dimmer light_hall “Light Stairway Hall” {dmx="CHANNEL[25:1000], ON[FADE|0:255,255,255:30000|5000:0,0,0:-1]"}

3) An alternating green yellow fade on 2 RGB led strips.

    Switch xmas_leds "Start Yellow-Green Fade Loop" {dmx="CHANNEL[7/6], ON[FADE|500:127,36,0,0,36,0:2000|500:0,36,0,127,36,0:2000]"}

### SFADE Command

The SFADE (Suspending Fade) command is very similar to the FADE command.  It differs from the FADE command in that it suspend any active fades before executing a new fade.  After the fade has completed, the original fade which was running before the SFADE started is resumed. It accepts the following parameter structure(s):

    <fade-time>:<target-channel-value>,<target-channel-value>,...:<hold-time>

### Example Suspending Fade Configurations

1) A short blue white flash pattern on the first 18 dmx channels, which will temporarily replace the active fades on those channels.

    Switch all_living_room_leds "Doorbell Flash"	{dmx="CHANNEL[1/18], ON[FADE|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:255,255,255:125|0:0,0,255:125|0:0,0,255:-1]"}

## Installing OLA

As a quick reference, an example installation is described below on how to install OLA on a Raspberry Pi running Raspbian Wheezy and configure it for use with an Entec Open DMX USB dongle.

For detailed instructions on how to install and configure OLA on different platforms or with different devices, consult the [OLA wiki](http://opendmx.net/index.php/Download_%26_Install_OLA).  

### Installing OLA binaries

To install OLA, you’ll need to add a software repository. Edit your software repository list, using the following command:

    sudo nano /etc/apt/sources.list
And add the following line to the list:

    deb   http://apt.openlighting.org/raspbian  wheezy main
After saving the file, install the binaries with the following commands:

    sudo apt-get update
    sudo apt-get install ola
When prompted if you want to start OLA at boot, select Yes.

### Configuring OLA devices

OLA can bind to many different protocols and devices (check  [here](http://opendmx.net/index.php/Open_Lighting_Architecture) for a full list of supported protocols and devices).  In a typical setup, you will only need a single device.  To optimize performance, it is recommended to disable all other devices and protocols which are not used.  

Depending on how OLA was installed, you can find the OLA configuration files for the devices and protocols in the directory /var/lib/ola/conf/ or ~/.ola .

The ola-ftdidmx.conf configuration file is needed for the Open DMX USB.  Disable all other configurations by editing the configuration files and changing the value of the property ‘enabled’ to ‘false’.

Next, open the following USB device rule file:

    sudo nano /etc/udev/rules.d/30-ftdidmx.rules
and add the following rule:

    ACTION=="add", BUS=="usb", SYSFS{idVendor}=="0403", SYSFS{idProduct}=="6001", GROUP:="dialout", MODE:="0660"

Restart the Raspberry to start OLA.

### Using your own device connection interface

If want to use a custom interface rather than OLA, you can easily achieve this by creating a new osgi bundle, which implements a org.openhab.binding.dmx.DmxConnection interface.

To use, all you need to do is deploy the core dmx bundle and your interface bundle.