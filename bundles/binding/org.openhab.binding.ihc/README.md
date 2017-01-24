Documentation of the IHC / ELKO binding Bundle

## Introduction

This binding is for the "Intelligent Home Control" building automation system originally made by LK, but now owned by Schneider Electric and sold as "IHC Intelligent Home Control". It is based on a star configured topology with wires to each device. The system is made up of a central controller and up to 8 input modules and 16 output modules. Each input module can have 16 digital inputs and each output module 8 digital outputs, resulting in a total of 128 input and 128 outputs per controller.

For installation of the binding, please see Wiki page [[Bindings]].
 
## Binding Configuration

add to ${openhab_home}/configuration/

Configure binding by adding following lines to your OpenHAB configuration file and fill IP address, user name and password according your controller configuration. Timeout need to be configured in milliseconds (5000 = 5 seconds). 

Binding will download project file from the controller. Binding also listening controller state changes and when controller state is changed from init to ready state (controller is reprogrammed), project file will be download again from the controller.

    ######################## IHC / ELKO LS Binding ########################################
    # Controller IP address 
    ihc:ip=
    
    # Username and password for Controller
    ihc:username=
    ihc:password=
    
    # Timeout for controller communication
    ihc:timeout=5000

IHC / ELKO LS controller communication interface is SOAP (Simple Object Access Protocol) based limited to HTTPS transport protocol. **Since version 1.7 binding, there is no need to add certificate manually to trust store anymore as binding trust all certificates automatically**

## Item Binding Configuration

IHC / ELKO LS binding use resource ID's to control and listening notification to/from the controller. You can find correct resource ID's from your IHC / ELKO LS project file. Binding support both decimal and hexadecimal values for resource ID's values. Hexadecimal value need to be specified with 0x prefix.

### New configuration syntax
The syntax of the binding configuration to an item can contain the following:

One InOut-Binding (+Out-Bindings): If defined the item receives updates from the IHC and received commands from openhab gets mapped to the IHC accordingly. 

    ihc="ResourceId[:refreshintervalinseconds]"

One In-Binding (+Out-Bindings): If defined, the item receives updates from the IHC.

    ihc="<ResourceId"

A set of Out-Binding: Multiple Out-Bindings can be defined. If the item receives a command specified in this list, the corresponding resourceId and value will be set.

    ihc=">[Command:ResourceId(:VALUE)]"

Command: The Command that should be mapped.
<table>
  <tr><th>VALUE</th><th>Meaning</th></tr>
  <tr><td>undefined</td><td>Default mapping will be used, see below</td>
  <tr><td>0</td><td>OnOffType.OFF</td></tr>
  <tr><td>1</td><td>OnOffType.ON</td></tr>
  <tr><td>>1</td><td>Switch (OnOffType.ON, sleep for VALUE ms, OnOffType.OFF)</td></tr>
</table>

Example:

    Rollershutter Rollershutter_Demo "Rollershutter Demo" (Rollershutters) {ihc=">[UP:15675921:100],>[DOWN:15676177:100]"}

### Old configuration syntax (before 1.7.0, still supported in newer versions)
The syntax of the binding configuration strings accepted is the following:

    ihc="[>]ResourceId[:refreshintervalinseconds]"

where parts in brackets [] signify an optional information.

The optional '>' sign tells whether resource is out binding only, where internal update from OpenHAB bus is just transmitted to the controller.

Refresh interval could be used for forcefully synchronous resource values from controller.

Binding will automatically enable runtime value notifications from controller for all configured resources.

Currently OpenHAB's Number, Switch, Contact, String and DateTime items are supported.

<table>
  <tr><td><b>OpenHAB data type</b></td><td><b>IHC / ELKO LS data type(s)</b></td></tr>
  <tr><td>Number</td><td>WSFloatingPointValue, WSIntegerValue, WSBooleanValue, WSTimerValue, WSWeekdayValue</td></tr>
  <tr><td>Switch</td><td>WSBooleanValue</td></tr>
  <tr><td>Contact</td><td>WSBooleanValue</td></tr>
  <tr><td>String</td><td>WSEnumValue</td></tr>
  <tr><td>DateTime</td><td>WSDateValue, WSTimeValue</td></tr>
</table>

Examples how to find resource ID's from project file (from .vis file) and map them to OpenHAB data types.

<table>
  <tr><td><b>OpenHAB data type</b></td><td><b>Resource id from project file</b></td></tr>
  <tr><td>Switch</td><td>&lt;dataline_input id="_0x3f295a" …&gt;</td></tr>
  <tr><td>Switch</td><td>&lt;dataline_output id="_0x3ce35b" …&gt;</td></tr>
  <tr><td>Switch</td><td>&lt;airlink_input id="_0x5b555c" …&gt;</td></tr>
  <tr><td>Dimmer</td><td>&lt;airlink_dimming id="_0x3ec5d" …&gt;</td></tr>
  <tr><td>Switch</td><td>&lt;resource_flag id="_0x97e00a" …&gt;</td></tr>

  <tr><td>Number</td><td>&lt;resource_temperature id="_0x3f4d14" …&gt;</td></tr>
  <tr><td>Number</td><td>&lt;resource_timer id="_0x97de10" …&gt;</td></tr>
  <tr><td>Number</td><td>&lt;resource_counter id="_0x97df0c" …&gt;</td></tr>
  <tr><td>Number</td><td>&lt;resource_weekday id="_0x97e109" …&gt;</td></tr>
  <tr><td>Number</td><td>&lt;resource_light_level id="_0x97dc13" …&gt;</td></tr>
  <tr><td>Number</td><td>&lt;resource_integer id="_0x97e20b" …&gt;</td></tr>
  <tr><td>DateTime</td><td>&lt;resource_time id="_0x97db0d" …&gt;</td></tr>
  <tr><td>DateTime</td><td>&lt;resource_date id="_0x97dd0e" …&gt;</td></tr>
  <tr><td>String</td><td>&lt;resource_enum id="_0x98050f" …&gt;</td></tr>
</table>


Examples, how to configure your items (e.g. demo.items):

Weather temperature is download from internet and updated to IHC controller object where resource id is 1234567:

    Number Weather_Temperature "Outside Temp. (Yahoo) [%.1f °C]" <temperature> (Weather_Chart) { http="<[http://weather.yahooapis.com/forecastrss?w=638242&u=c:60000:XSLT(demo_yahoo_weather.xsl)]", ihc=">1234567" }

Binding listens all state changes from controller's resource id 9953290 and update state changes to OpenHAB Light_Kitchen item. All state changes from OpenHAB will be also transmitted to the controller (e.g. command from OpenHAB console 'openhab send Light_Kitchen ON').

    Switch Light_Kitchen {ihc="9953290"}

Such as previous example, but resource value will additionally asked from controller ones per every minute.

    Number Temperature_Kitchen "Temperature [%.1f °C]" <temperature> (Temperature, FF_Kitchen) { ihc="0x97E00A:60" }

## Binding version before 1.7.0

If you are still using old IHC binding version (before 1.7.0), you need to follow instructions below.
  
Controller TLS certificate is self signed, so by default OpenHAB (precisely Java) will not allow TLS connection to controller for security reason. You need to import controller TLS certificate to trusted list by using java keytool. 

You can download controller TLS certificate e.g. by Firefox browser; just open HTTPS connection to your controller IP address (https://192.168.1.2), click "lock" icon (just before URL box) -> more information -> security tab -> view certificate -> details tab -> export.

Keytool usage:

    $JAVA_HOME/bin/keytool -importcert -alias <some descriptive name> -keystore <path to keystore> -file <certificate file>

See more information about the keytool from [here](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html).

Keytool usage example (OS X):

    sudo keytool -importcert -alias ELKO -keystore /System/Library/Java/Support/CoreDeploy.bundle/Contents/Home/lib/security/cacerts -file ELKOLivingSystemController.pem
