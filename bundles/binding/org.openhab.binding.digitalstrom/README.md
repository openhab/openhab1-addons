Documentation of the digitalSTROM binding bundle

## Introduction

digitalSTROM is the solution for intelligent living. The smart home system represents digital lifestyle and an integrated connectivity concept. Intuitive in use and simple to install, the technology communicates via the existing power lines thus integrating all the electrical appliances as well as broadband devices in a home.

This results in an infrastructure allowing connection of any applications, products and services via open interfaces. Successful miniaturization and digital intelligence make digitalSTROM suitable for both retrofitting and installation in new builds, without interfering in the existing room design. Analogous to the world of smartphones and app stores, digitalSTROM has open interfaces allowing unlimited creativity for solutions in smart living: In addition to apps for additional convenience at home, new marketing opportunities arise for suppliers of products and services. digitalSTROM AG is headquartered in Schlieren-Zurich (CH) and Wetzlar (D).

digitalSTROM binding bundle is available as a separate (optional) download. If you want to let openHAB communicate with digitalSTROM DSS11 server, please place this bundle in the folder ${openhab_home}/addons and add binding information to your configuration.
 
For installation of the binding, please see Wiki page [[Bindings]].

## Preliminaries

### digitalSTROM server add-on

- Unzip [DigitalSTROM_OPENHAB_ADD-ON.zip](https://github.com/openhab/openhab/raw/master/bundles/binding/org.openhab.binding.digitalstrom/DigitalSTROM_OPENHAB_ADD-ON.zip)
- copy "openhab/openhab.js" to your dss server "/usr/share/dss/add-ons/"
- copy "openhab.xml" to your dss server "/usr/share/dss/data/subscriptions.d/"

### digitalSTROM server certificate

DigitalSTROM JSON service interface is limited to HTTPS protocol. 
For security reason, we need to create a self signed cetificate with correct given hostname. To do so, follow these steps:

    1. openssl genrsa -out privkey.pem 1024
    
    2. chmod 400 privkey.pem
    
    3. openssl req -new -key privkey.pem -out certreq.csr
    
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
    
    4. openssl x509 -req -days 9000 -in certreq.csr -signkey privkey.pem -out dsscert.pem
    
    5. cat privkey.pem >> dsscert.pem
Copy the newly generated certificate onto your digitalSTROM server and
import it to java trusted store of your openHAB server by using java keytool.

Keytool usage:

    $JAVA_HOME/bin/keytool -importcert -alias <some descriptive name> -keystore <path to keystore> -file <certificate file>

Keytool usage example (OS X):

    sudo keytool -importcert -alias DSS -keystore /System/Library/Java/Support/CoreDeploy.bundle/Contents/Home/lib/security/cacerts -file dss.local.pem


## digitalSTROM Binding Configuration

### openhab.cfg

The following config params are used for the digitalSTROM binding.

- (optional) digitalstrom:refreshinterval
Refresh interval (defaults to 1000 ms)
- digitalstrom:uri
**Hostname** and port of the digitalSTROM server (dSS)
- (optional) digitalstrom:connectTimeout
Connect timeout (defaults to 4000 ms)
- (optional) digitalstrom:readTimeout
Connect timeout (defaults to 10000 ms)
- digitalstrom:loginToken
To login without a user and password; loginToken must be enabled once
- digitalstrom:user
- digitalstrom:password
To login with username and password; default username and password is dssadmin
If you have loginToken and username with password the loginToken will be prefered by default

Obtaining loginToken

Point your browser to
https://dss.local:8080/json/system/requestApplicationToken?applicationName=openHAB

Store the result in openhab.cfg file

After receiving the application token you need to enable it in your digitalSTROM configurator under
System -> Zugriffsberechtigung
### Example

    ################################ digitalSTROM Binding #################################
    
    # Refresh interval (defaults to 1000 ms)
    digitalstrom:refreshinterval=1000
    
    # Hostname and port of the digitalSTROM server (dSS)
    digitalstrom:uri=https://dss.local:8080
    
    # Connect timeout (defaults to 4000 ms)
    digitalstrom:connectTimeout=4000
    
    # Connect timeout (defaults to 10000 ms)
    digitalstrom:readTimeout=10000
    
    # to login without a user and password; loginToken must be enabled once
    digitalstrom:loginToken=
    
    # to login with username and password; default user is dssadmin and default password is dssadmin
    # if you have loginToken and username with password the loginToken will be prefered by default
    digitalstrom:user=dssadmin
    digitalstrom:password=dssadmin


## Generic Item Binding Configuration

### Syntax

In order to bind an item to a digitalSTROM device you need to provide configuration settings. 
The simplest way is:

    digitalstrom="dsid:<digitalSTROM device id>" //device with example dsid (You can also use the shorter dsid, known as S.N: xxxxxxxx (8 letters) )
Thats it! It's enough to control a device.

### Example

    Switch Light_GF_Corridor_Ceiling    "Ceiling"       {digitalstrom="dsid:3504175fe000000000000001"}
    Dimmer Light_GF_Living_Table        "Table"         {digitalstrom="dsid:3504175fe000000000000001"}
    
    Rollershutter Shutter_GF_Living     "Livingroom"    {digitalstrom="dsid:3504175fe000000000000001"}

All syntax keywords for the digitalSTROM are explained here:

    symbols:	
        :               // to refer a value to a key
        #               // separator for the next key-value pair
    
    keys:	
        dsid            // digitalSTROM device id
        dsmid           // digitalSTROM meter id (dSM)
        consumption     // optional for metering
        timeinterval	// timeinterval to initiate a metering job
        context         // in some cases use context:
        zoneid          // only in combination with a NumberItem or StringItem for apartment or zone calls
        groupid         // only in combination with a NumberItem or StringItem for apartment or zone calls
    
    context:
        slat            // important if it's a roller shutter: to have a item to control the slats
        awning          // important for roller shutter: if it's a marquee/awning to show the right icon -> open-close
        apartment       // in combination with a Number- or StringItem to make apartment calls
        zone            // in combination with a Number- or StringItem to make zone calls
    
    consumption:	
        ACTIVE_POWER    // in use with a device or meter -> current power consumption (w)
        OUTPUT_CURRENT  // only in use with a device 	-> amperage (mA)
        ELECTRIC_METER  // only in use with a meter (wh)

## Further examples

### Rollershutter

    Rollershutter Shutter_GF_Living     "Livingroom"    {digitalstrom="dsid:3504175fe000000000000001"}

in case of marquee/awning add context param "awning"

    Rollershutter Shutter_GF_Living     "Livingroom"    {digitalstrom="dsid:3504175fe000000000000001#context:awning"}

### Jalousie

for up and down use

    Rollershutter Shutter_GF_Living_UP_DOWN     "Livingroom UP/DOWN"    {digitalstrom="dsid:3504175fe000000000000001"}

add second item to adjuste the slats (use the same dsid)

    Rollershutter Shutter_GF_Living_UP_DOWN     "Livingroom UP/DOWN"    {digitalstrom="dsid:3504175fe000000000000001#context:slat"}


### Scenes

To call apartment or zone scenes use number- or string item in the .items file:

    Number|String Apartment_Scene       "Apartment Scene" {digitalstrom="context:apartment"}
    Number|String All_Apartment_Lights  "All lights"      {digitalstrom="context:apartment#groupid:1"}          //optional add groupid
    
    Number|String Zone_Scene            "Room Scene"      {digitalstrom="context:zone#zoneid:65535"}
    Number|String All_Zone_Lights       "All room lights" {digitalstrom="context:zone#zoneid:65535#groupid:1"}  //optional add groupid

and in .sitemap add mappings:
    Selection item=Apartment_Scene  label="Apartment Scene Selection"   mappings=[65=Panik, 72=Gehen]	// here you have to use the right (a valid) sceneID
    
    Switch item=Zone_Scene          label="Room Scene"                  mappings=[14=On, 13=Off]        // here you have to use the right (a valid) sceneID

### Consumption

To poll and e.g. persist/visualize the curent power consumption (ACTIVE_POWER) you should use string- or number items.

### = Device consumption =

    Number Power_Consumption_TV	"TV [%d W]" {digitalstrom="dsid:3504175fe000000000000001#consumption:ACTIVE_POWER#timeinterval:60000"}	// read device power consumption every 60 seconds
Be aware! Your system will work very slow on sensor reading!

Rule 8 	"Application processes that do automatic cyclic reads or writes of
		device parameters are subject to a request limit: at maximum one request
		per minute and circuit is allowed."(digitalSTROM: [p.36)

Rule 9 	"Application processes that do automatic cyclic reads of measured
		values are subject to a request limit: at maximum one request per minute
		and circuit is allowed."(digitalSTROM: [http://developer.digitalstrom.org/Architecture/ds-basics.pdf](http://developer.digitalstrom.org/Architecture/ds-basics.pdf]) p.36)

### = Circuit consumption =

    Number Consumption_dSM "dSM [%d W]"	{digitalstrom="dsmid:3504175fe000001000000001#consumption:ACTIVE_POWER#timeinterval:3000"} // read power consumption every 3 seconds (it 'works' because here we read cached values)

### = Apartment consumption =

    Number Consumption_House "Total house consumption [%s W]"	{digitalstrom="dsmid:ALL#consumption:ACTIVE_POWER#timeinterval:3000"}

### = General meterenig/consumption note =

- The timeinterval only initiates a metering job, but you don't have the guarantee that the worker will start it in this time!
 
- You should better not read the consumption of a device, but of meters(dSMs).

### = Other important note =

In the first time the system learns, how to react on specific scene calls (sensor reading). But after some time it will work fast!