# digitalSTROM Binding

The openHAB digitalSTROM binding allows interaction with digitalSTROM devices.

<!-- MarkdownTOC depth=1 -->

- [Prerequisites](#prerequisites)
- [Binding Configuration](#binding-configuration)
- [Item Configuration](#item-configuration)
- [Examples](#examples)
- [Notes](#notes)

<!-- /MarkdownTOC -->

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/digitalstrom/).

 
## Prerequisites

### digitalSTROM server add-on

- Unzip [DigitalSTROM_OPENHAB_ADD-ON.zip](https://github.com/openhab/openhab/raw/master/bundles/binding/org.openhab.binding.digitalstrom/DigitalSTROM_OPENHAB_ADD-ON.zip)
- copy "openhab/openhab.js" to your dss server "/usr/share/dss/add-ons/"
- copy "openhab.xml" to your dss server "/usr/share/dss/data/subscriptions.d/"

### digitalSTROM server certificate

The digitalSTROM JSON service interface is limited to the https protocol. 
For security reasons, creating a self signed cetificate with correct given hostname is requird.
Follow these steps:

1. openssl genrsa -out privkey.pem 1024
1. chmod 400 privkey.pem
1. openssl req -new -key privkey.pem -out certreq.csr
    
    Enter your details:
    
    Country Name (2 letter code) [AU]: DE // not important
    
    State or Province Name (full name) [Some-State]: NRW // not important
    
    Locality Name (eg, city) []: Gummersbach // not important
    
    Organization Name (eg, company) [Internet Widgits Pty Ltd]: dss.local // network name of your dss, important
    
    Organizational Unit Name (eg, section) []: dss.local // network name of your dss, important
    
    Common Name (eg, YOUR name) []: dss.local // network name of your dss, important
    
    Email Address []: // not important
    
    Please enter the following 'extra' attributes to be sent with your certificate request A challenge password []: // not important, press enter
    
    An optional company name []: // not important, press enter
    
1. openssl x509 -req -days 9000 -in certreq.csr -signkey privkey.pem -out dsscert.pem 
1. cat privkey.pem >> dsscert.pem
    
Copy the newly generated certificate onto your digitalSTROM server and
import it to java trusted store of your openHAB server by using java keytool.

Keytool usage:

    $JAVA_HOME/bin/keytool -importcert -alias <some descriptive name> -keystore <path to keystore> -file <certificate file>

Keytool usage example (OS X):

    sudo keytool -importcert -alias DSS -keystore /System/Library/Java/Support/CoreDeploy.bundle/Contents/Home/lib/security/cacerts -file dss.local.pem

### loginToken

- To obtain a loginToken, point your browser to https://dss.local:8080/json/system/requestApplicationToken?applicationName=openHAB
- Store the result in openhab.cfg file
- After receiving the application token you need to enable it in your digitalSTROM configurator under System -> Zugriffsberechtigung


## Binding Configuration

The binding can be configured in the file `services/digitalstrom.cfg`.

| Property        | Default | Required | Description |
|-----------------|---------|:--------:|-------------|
| uri             |         | Yes      | The hostname and port of the digitalSTROM server (dSS); eg. https://dss.local:8080
| connectTimeout  | 4000    | No       | The connect timeout (in milliseconds)
| loginToken      |         | No       | A token to allow login without a username and password; required if `user` and `password` settings are not specified
| password        | dssadmin| No       | The username to use for login; required if `loginToken` setting is not specified
| readTimeout     | 10000   | No       | The read timeout (in milliseconds)
| refreshinterval | 1000    | No       | The refresh interval (in milliseconds)
| user            | dssadmin| No       | The password to use for login; required if `loginToken` setting is not specified

Note: if the `loginToken`, `user`, and `password` settings are all specified, the `loginToken` will be used for login.

## Item Configuration

Item bindings must conform to the following format:

    digitalstrom="<key1>:<value1>[#<keyN>:<valueN>]"

See examples below.

The following keys are supported:

        dsid            // digitalSTROM device id; the shorter dsid, known as S.N: xxxxxxxx (8 letters), is also supported
        dsmid           // digitalSTROM meter id (dSM)
        consumption     // optional for metering
        timeinterval    // timeinterval to initiate a metering job
        context         // in some cases use context:
        zoneid          // only in combination with a NumberItem or StringItem for apartment or zone calls
        groupid         // only in combination with a NumberItem or StringItem for apartment or zone calls

The following are the supported values for the `consumption` key:

        ACTIVE_POWER    // in use with a device or meter -> current power consumption (w)
        OUTPUT_CURRENT  // only in use with a device 	-> amperage (mA)
        ELECTRIC_METER  // only in use with a meter (wh)

The following are the supported values for the `context` key:

        slat            // important if it's a roller shutter: to have a item to control the slats
        awning          // important for roller shutter: if it's a marquee/awning to show the right icon -> open-close
        apartment       // in combination with a NumberItem or StringItem to make apartment calls
        zone            // in combination with a NumberItem or StringItem to make zone calls
    

## Examples

### Items

    Switch Light_GF_Corridor_Ceiling    "Ceiling"       {digitalstrom="dsid:3504175fe000000000000001"}
    Dimmer Light_GF_Living_Table        "Table"         {digitalstrom="dsid:3504175fe000000000000001"}
    
### Rollershutter

    Rollershutter Shutter_GF_Living     "Livingroom"    {digitalstrom="dsid:3504175fe000000000000001"}

In case of marquee/awning add the `context` key with a value of `awning`:

    Rollershutter Shutter_GF_Living     "Livingroom"    {digitalstrom="dsid:3504175fe000000000000001#context:awning"}

### Jalousie

For up and down use:

    Rollershutter Shutter_GF_Living_UP_DOWN     "Livingroom UP/DOWN"    {digitalstrom="dsid:3504175fe000000000000001"}

Add the `context` key with a value of `slat` to adjust the slats:

    Rollershutter Shutter_GF_Living_UP_DOWN     "Livingroom UP/DOWN"    {digitalstrom="dsid:3504175fe000000000000001#context:slat"}

### Scenes

To call apartment or zone scenes use NumberItems or StringItems:

    Number|String Apartment_Scene       "Apartment Scene" {digitalstrom="context:apartment"}
    Number|String All_Apartment_Lights  "All lights"      {digitalstrom="context:apartment#groupid:1"}          //optional add groupid
    
    Number|String Zone_Scene            "Room Scene"      {digitalstrom="context:zone#zoneid:65535"}
    Number|String All_Zone_Lights       "All room lights" {digitalstrom="context:zone#zoneid:65535#groupid:1"}  //optional add groupid

And add mappings in the .sitemap file:

    Selection item=Apartment_Scene  label="Apartment Scene Selection"   mappings=[65=Panik, 72=Gehen]	// here you have to use a valid sceneID
    Switch item=Zone_Scene          label="Room Scene"                  mappings=[14=On, 13=Off]      // here you have to use a valid sceneID

### Consumption

To poll and/or persist/visualize the curent power consumption (ACTIVE_POWER), use StringItems or NumberItems.

#### Device consumption

    Number Power_Consumption_TV	"TV [%d W]" {digitalstrom="dsid:3504175fe000000000000001#consumption:ACTIVE_POWER#timeinterval:60000"}	// read device power consumption every 60 seconds

Be aware that the system will work very slow on sensor reading.

The [digitalSTROM Basic Concepts document](http://developer.digitalstrom.org/Architecture/ds-basics.pdf) (p. 36) says:

Rule 8 	"Application processes that do automatic cyclic reads or writes of
		device parameters are subject to a request limit: at maximum one request
		per minute and circuit is allowed."

Rule 9 	"Application processes that do automatic cyclic reads of measured
		values are subject to a request limit: at maximum one request per minute
		and circuit is allowed."

#### Circuit consumption

    Number Consumption_dSM "dSM [%d W]"	{digitalstrom="dsmid:3504175fe000001000000001#consumption:ACTIVE_POWER#timeinterval:3000"} // read power consumption every 3 seconds (it 'works' because here we read cached values)

#### Apartment consumption

    Number Consumption_House "Total house consumption [%s W]"	{digitalstrom="dsmid:ALL#consumption:ACTIVE_POWER#timeinterval:3000"}

#### General metering/consumption note

- The timeinterval only initiates a metering job; there is no guarantee that the worker will start it in this time!
- It is better to read the consumption of meters (dSMs) instead of devices.

## Notes

At first the system is slow while it learns how to react on specific scene calls (sensor reading). But after some time it will work fast!
