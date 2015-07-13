==========================================================================
 Pi4J :: Java library for Raspberry Pi
==========================================================================

## PROJECT INFORMATION

Project website: http://www.pi4j.com/ <br />
Project discussion group: https://groups.google.com/forum/#!forum/pi4j <br />
Project issues list: https://github.com/Pi4J/pi4j/issues <br />
<br />
Release builds are available from 
   *  [Maven Central] http://search.maven.org/#search%7Cga%7C1%7Ccom.pi4j
   *  [Downloads] http://pi4j.com/download.html

Snapshot builds are available from 
   *  [Sonatype OSS] https://oss.sonatype.org/index.html#nexus-search;quick~pi4j
   *  [Downloads] http://pi4j.com/download.html
   
 
Copyright (C) 2012-2015 Pi4J

## LICENSE
 
 Licensed under the GNU LGPLv3 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.gnu.org/licenses/lgpl.txt
  
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
## FUTURE PLANS

   *  See future enhancements here:  
      https://github.com/Pi4J/pi4j/issues?labels=enhancement&page=1&state=open

## RELEASES

2015-04-18 :: 1.0

  *  Released under GNU LGPLv3 license.
  *  Added support for RaspberryPi v2 Model B
  *  Added support for RaspberryPi Model A+
  *  Added support for RaspberryPi Model B+
  *  Added support for RaspberryPi Compute Module
  *  Added support for debounce logic on digital input pin events
  *  Added support for Button component interface
  *  Added support for Pibrella device
  *  Added support for SPI modes
  *  Added support for building pi4j-native project directly on the Pi (using maven)
  *  Added easy install/uninstall scripts (get.pi4j.com)
  *  Added APT package and repository support (repository.pi4j.com)
  *  Simplified native library loader to find embedded hard-float native lib
  *  Fixed JDK8 build errors.
  *  Removed support (and complicated builds) for soft-float ABI
  *  Updated documentation in preparation for 1.0 release
  *  Cleaned up build
  *  Enhancements Added: https://github.com/Pi4J/pi4j/issues?q=label%3Aenhancement+is%3Aclosed+milestone%3A%22RELEASE+1.0%22+
  *  Bugs/Defects Fixed: https://github.com/Pi4J/pi4j/issues?q=label%3Abug+is%3Aclosed+milestone%3A%22RELEASE+1.0%22+

2013-03-17 :: 0.0.5

  *  Added support for PCF8574 GpioProvider
  *  Added sample code for using PCF8574 GpioProvider
  *  Fixed issue where hard-float ABI was not detected properly when using Oracle JDK 8 early access edition.
     https://github.com/Pi4J/pi4j/issues/26
  *  Added support for Motor and Stepper motor component interfaces. 
  *  Added GPIO based stepper motor implementation and sample program. 
  *  Added support for Motion sensor component interface. 
  *  Added GPIO based motion sensor implementation. 
  *  Added LCD component interface 
  *  Added GPIO based 4/8 bit LCD display component implementation and sample program.
  *  Added GPIO based 4/8 bit LCD WiringPi example program
  *  Added support for a GpioController.shutdown() method to cleanup terminate all Pi4J threads and executors.
     https://github.com/Pi4J/pi4j/issues/9
  *  Added support for a user-definable ExecutorServiceFactory to allow user program to provide the implementation
     for executor service instances and managed thread pools.  
     https://github.com/Pi4J/pi4j/issues/10
  *  Fixed 'java.util.concurrent.RejectedExecutionException' issue where there were not 
     enough default thread in the pool for concurrent tasks.
     https://github.com/Pi4J/pi4j/issues/31
  *  Fixed issue where SerialDataMonitorThread was not shutting down on program exit.
     https://github.com/Pi4J/pi4j/issues/33
  *  Fixed issue where hard-float ABI was not detected properly when using Oracle JDK 8 early access edition.
     https://github.com/Pi4J/pi4j/issues/26
  *  Added support for exceptions on serial port access methods. 
     https://github.com/Pi4J/pi4j/issues/8
  *  Fixed issue where process streams were not being closed properly
     https://github.com/Pi4J/pi4j/issues/35
  *  Fixed issue where serial.write(byte[]) was throwing java.lang.StringIndexOutOfBoundsException
     https://github.com/Pi4J/pi4j/issues/37
  *  Added additional system information API methods to access memory, frequency, CPU temperature, voltage, and board model data.
     https://github.com/Pi4J/pi4j/issues/30
  *  Fixed MCP23008 and MCP23017 getState() return values  
  *  Added support for MCP23S17 GpioProvider
  *  Added support for Pi-Face GpioProvider 
  *  Added support for Pi-Face Device Interface 
  *  Added MCP23S17GpioExample sample code    
  *  Added PiFaceExample sample code 
  *  Added WiringPiSPIExample sample code    
  *  Added LED component interface and GpioLEDCompoment impl    
  *  Added GpioSwitchCompoment impl    

2012-12-16 :: 0.0.4
 
   *  Added support for building a Pi4J .deb installer package for Debian/Raspian Linux distribution. (BETA)
   *  Modified NetworkInfo static methods to permit empty array return values when a network property cannot be obtained.
      https://github.com/Pi4J/pi4j/issues/3
   *  Fixed issue in newer Raspberry Pi 512mb firmware where Pi4J was not compiled against latest wiringPi library and thus not detecting the hardware properly.
      https://github.com/Pi4J/pi4j/issues/3
   *  Fixed defect where Pi4J library prevented consuming program from exiting properly.
      https://github.com/Pi4J/pi4j/issues/4 
      https://github.com/Pi4J/pi4j/issues/6
   *  Added support for MCP23008 GPIO Provider using I2C bus.
   *  Added support for MCP23017 GPIO Provider using I2C bus.
   *  Modified build script to support concurrent soft-float and hard-float native library builds
   *  Modified build script to include pulling wiringPi sources and compiling when building native library
   *  Added power controller device interface, base impl, and reference impl
   *  Added GpioController.provisionXXXPin methods that allow name to be optional
   *  Relocated project downloads location to google code site; github deprecated downloads.
   *  Added Google Groups discussion forum to project docs. 
   *  Added getTag() and setTag() to GpioPin interface and impl. 
   *  Added TemperatureConversion utility. 
   *  Added optional blocking argument to GpioPin pulse method to support blocking pulse invocations 

2012-10-22 :: 0.0.3-SNAPSHOT
 
   *  Refactored Gpio interfaces to permit extensible GpioProviders for add-on devices.
   *  Refactored Gpio interfaces to separate digital, analog, and PWM types of input and output pins.
   *  Added support for analog GPIO pins (interfaces, methods and events). 
   *  Added Olimex AVR-IO-M16 GpioProvider to serve as an example of extending a new GpioProvider for GPIO expansion. 
   *  Added Olimex AVR-IO-M16 sources for ARV micro-controller.
   *  Removed setDirection() from GpioPin; embedded this functionality into existing setMode() method.  Eliminating duplication.
   *  Removed old test programs from pi4j-core and moved wiringPi examples to pi4j-example project.  
   *  Added support for I2C communication.
   *  Added blink() methods for GPIO pins


2012-09-23 :: 0.0.2-SNAPSHOT
 
   *  Refactored Gpio interface to permit methods using Pin argument to function without requiring a GpioPin to first be provisioned
   *  Added OSGi manifest 
   *  Optimized GPIO interrupt thread handling to only be created if GpioPin listeners or triggers have been registered.
   *  Now disabling/stopping GPIO interrupt native thread monitoring if all GpioPin listeners or triggers are unregistered.
   *  Added additional convenience methods in GpioPin interface for evaluating state, direction, resistance, mode, etc. 
   *  There were two Gpio classes; renamed one to GpioController to avoid confusion.
   *  Added OSGi service project to export Pi4J GPIO as an OSGi service.  


2012-09-19 :: 0.0.1-SNAPSHOT
 
   *  Initial ALPHA release
 
 [![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/umajeric/pi4j/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
