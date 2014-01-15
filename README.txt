This repository provides additional bundles for openHAB.

CUL transport
=============
For the most bindings this bundle is essential. It provides access to culfw 
(culfw.de) based devices such as the CULLite. Most other bindings provided
here depend on this transport. Simply put org.openhab.io.transport.cul-{version}.jar
in your addon directory to use it

FHT binding
===========
This binding uses the CUL transport to communicate with FHT-80b based devices. Currently
room thermostats, Valves (reading only) and window contacts are supported.
The very least your openhab.cfg needs to contain two new lines:

- fht:device=serial:/dev/ttyACM0
- fht:housecode=6261

Where the housecode needs to be a valid hexadecimal housecode which isn't used by your devices
yet. Additional you can configure this binding to update the date and time of the room
thermostats at certain times and to actively request status updates. This can be achieved
with:
- fht:time.update=true
- fht:time.update.cron=0 15 04 * * ?
- fht:reports=true;
- fht:reports.cron=0 15 05 * * ?

A item configuration consists of specifying the housecode of the device communicate with and the 
infortmation you are interested in. For example
- {fht="housecode=3D49;address=00;datapoint=VALVE"}
would receive the valve opening if the first valve actor with the housecode 3D49.
For addressing the FHT-80b itself you have to omit the address part.
Valid datapoints are MEASURED_TEMP, DESIRED_TEMP, BATTERY, WINDOW, VALVE. Please refer
to the FHT documentation which data points can be read from or written to a device.

FS20 binding
============
This binding allows you to communicate with FS20 devices. To use it you need the CUL transport
and have to specify the CUL device to use in your openhab.cfg
- fs20:device=serial:/dev/ttyACM0

The item config looks like
- {fs20="C05B10"} 
where C05B10 is the complete rf address of the device including the housecode.
Depending on the item type commands will be converted to FS20 commands if possible.

Intertechno binding
===================
This binding can send commands to Intertechno switches. Other devices or the receiption of Intertechno
messages is not supported. Again this binding needs the CUL transport and you have to specify
the device to use in your openhab.cfg
- culintertechno:device=serial:/dev/ttyACM0

The item config depends on the specific Intertechno device. There are three parser for convenient
configuration. If you have an "original", FLS or REV Intertechno device you can specify classic, fls,
or rev as the type and then just specify the group and switch address as you see it on the housing of
the switch.
For example configuring a FLS type switch looks like:
- {culintertechno="type=fls;group=IV;address=1"}

In case you have an unknown Intertechno device you can specifiy "raw" parameters for the CUL:
- {culintertechno="type=raw;address="FFF000FF,on=0FF,off=0F0"}
Please refer to http://www.fhemwiki.de/wiki/Intertechno_Code_Berechnung for more information on
Intertechno and its addresses.



openHAB Build Instructions
==========================

Thanks for your interest in the openHAB project (http://www.openHAB.org)!

Building and running the project is fairly easy if you follow the steps
detailed below.


1. PREREQUISITES
================

The openHAB build infrastructure is based on Maven in order to make it
as easy as possible to get up to speed. If you know Maven already then
there won't be any surprises for you. If you have not worked with Maven
yet, just follow the instructions and everything will miraculously work ;-)

What you need before you start:
- Maven3 from http://maven.apache.org/download.html
- Git from http://git-scm.com/downloads

Make sure that both "mvn" and "git" commands are available on your path


2. CHECKOUT
===========

Check out the project sources from GitHub:

> git clone https://github.com/openhab/openhab.git

You will now have all required sources in the openhab subdirectory.


3. BUILDING WITH MAVEN
======================

To build openHAB from the sources, Maven takes care of everything:
- change into the openhab directory ("cd openhab")
- run "mvn clean install" to compile and package all sources

The build result will be available in the folder 
openhab/distribution/target. Both the runtime as well as
the designer zips are placed in there.


4. STARTING THE RUNTIME
=======================

- unzip the file distribution-<version>-runtime.zip to a local folder
- unzip the file distribution-<version>-demo.zip over the same folder
- launch the runtime with the "start.sh" (resp. "start.bat") script
- check if everything has started correctly: http://localhost:8080/openhab.app?sitemap=demo


5. STARTING THE DESIGNER
========================

- unzip the file distribution-<version>-designer-<platform>.zip to a local folder
- run the executable "openHAB-Designer.exe"
