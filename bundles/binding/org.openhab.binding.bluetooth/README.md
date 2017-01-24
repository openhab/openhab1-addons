Bluetooth Binding Bundle

## Introduction

The Bluetooth binding is used to connect openhab with a Bluetooth device. With it you can make openhab  react when bluetooth devices that get in range of your network.

The Bluetooth binding supports three different types of openhab items: Switches, Numbers and Strings.

- Switches can be bound to a certain bluetooth device address so that they are switched on if the device is in range and off otherwise.
- Number items simply determine how many devices are currently in range.
- String items are updated with a comma-separated list of device names that are in range.

For each item you can configure whether to observe: only paired, unpaired or all devices.

#### Restrictions

Binding is not working for Windows 8.1 (both 32 and 64 bit) and MAC OS (Yosemite).

## Generic Item Binding Configuration

In order to bind an item to a Bluetooth event you need to provide configuration settings. The easiest way to do this is to add binding information in your item file (in the folder configurations/items`). The syntax for the Bluetooth binding configuration string is as follows:


* for switch items: `bluetooth="<deviceAddress>[!]"`, where `<deviceAddress>` is the technical address of the device, e.g. `EC935BD417C5`; the optional exclamation mark defines whether the devices needs to be paired with the host.
* for string items: `bluetooth="[*|!|?]"`, where '`!`' denotes to only observe paired devices, '`?`' denotes to only observe unpaired devices and '`*`' accepts any device.
* for number items: `bluetooth="[*|!|?]"`, where '`!`' denotes to only observe paired devices, '`?`' denotes to only observe unpaired devices and '`*`' accepts any device.

***

* Switch items: will receive an ON / OFF update on the bus
* String items: will be sent a comma separated list of all device names
* Number items will show the number of bluetooth devices in range


If a friendly name cannot be resolved for a device, its address will be used instead as its name when listing it on a String item.


Here are some examples for valid binding configuration strings:

    bluetooth="EC935BD417C5"
    bluetooth="EC935BD417C5!"
    bluetooth="*"
    bluetooth="!"
    bluetooth="?"

As a result, your lines in the items file might look like follows:

    Switch MyMobile     	                                  { bluetooth="EC935BD417C5!" }
    String UnknownDevices    "Unknown devices in range: [%s]" { bluetooth="?" }
    Number NoOfPairedDevices "Paired devices in range: [%d]"  { bluetooth="!" }

## Installation

* For general installation of the binding, please see Wiki page [[Bindings]].
* To access the local bluetooth device, the binding uses [BlueCove](http://bluecove.org/).
* BlueCove uses native libraries (JNI) to access the platform specific bluetooth stack.
* There are prebuilt native libraries for Windows and Mac OS X (IMHO).
* The native libraries need to be built for Linux as follows

### Linux Installation

To access the bluetooth stack on linux systems, we have to build the native libraries.
We have to replace the bluecove stuff that is bundled with the mainline bluetooth binding with the new one, and then rebuild the binding for our target system (so the correct stuff comes with it).

#### 1. Build BlueCove

_Install necessary packages to build:_  base-devel subversion maven bluez-libs

(example for [ARM] Arch Linux).
<pre>pacman --needed -S base-devel subversion maven bluez-libs</pre>

_Checkout BlueCove repository_ (tested with v2.1.0)
<pre>svn checkout https://bluecove.googlecode.com/svn/tags/2.1.0/</pre>

_Enter the directory and edit the pom.xml file_ -  disable some modules we don't need (we are only interested in bluecove and bluecove-gpl -- I did not check, if bluevoce-site-skin is necessary for bluecove build).

<pre>
    &lt;modules>
        &lt;module>bluecove-site-skin&lt;/module>
        &lt;module>bluecove&lt;/module>
&lt;!--
        &lt;module>bluecove-emu&lt;/module>
        &lt;module>bluecove-tests&lt;/module>
        &lt;module>bluecove-emu-gui&lt;/module>
-->
        &lt;module>bluecove-gpl&lt;/module>
&lt;!--
        &lt;module>bluecove-bluez&lt;/module>
        &lt;module>bluecove-examples&lt;/module>
-->
    &lt;/modules>
</pre>

_Start build using maven._
<pre>mvn</pre>

The build will fail on bluecove-gpl, caused by missing header file(s).

Don't know why the header files are not generated at the build process, but that problem should be reported upstream.

_Generate (missing) JNI header files_
<pre>javah -d ./bluecove-gpl/src/main/c/ \
  -cp ./bluecove-gpl/target/classes:./bluecove/target/classes \
  com.intel.bluetooth.BluetoothStackBlueZ \
  com.intel.bluetooth.BluetoothStackBlueZConsts \
  com.intel.bluetooth.BluetoothStackBlueZNativeTests</pre>

_Resume build_
<pre>mvn -rf :bluecove</pre>

Necessary build results:
<pre>./bluecove/target/bluecove-2.1.0.jar
./bluecove-gpl/target/bluecove-gpl-2.1.0.jar</pre>

#### 2. Adjust binding dependencies

This could be done using different ways, e.g. you could use the Eclipse IDE.
* open META-INF/MANIFEST.MF
* Change "Runtime", "Classpath" (replace the old bluecove jar with the two new ones)
* In "Build" the "Binary Build" should be changed automatically, if you change the runtime classpath.
* Check, if "MANIFEST.MF" and "build.properties" was changed.
* Save file

#### 3. Build openHAB Bluetooth binding

* Delete the bluecove jar file in the lib subdirectory of the binding.
* Add the two build jar files (bluecove-2.1.0.jar and bluecove-gpl-2.1.0.jar) to the lib subdirectory of the binding.
* Be sure binding dependencies are adjusted (step 2 above)
* Clean and rebuild the binding.

The binding will work now on the target system, the bluecove jar (and the native libraries) are generated. 
