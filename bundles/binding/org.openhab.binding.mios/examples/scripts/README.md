# MiOS Binding Tools and Utilities

## MiOS openHAB Item file Generator

The MiOS openHAB Item file Generator tools extract information from a live/running MiOS Unit, and convert them into an Items file suitable for use in openHAB.  These tools are intended to build a starting point Items file for the end-user, using the current-generation of the MiOS Binding.

NOTE: The rules for generating the Items file may change over time.  The output of the script may change without notice, and should be reviewed prior to use.

### OS Requirements

These scripts are intended to be run on a Linux, Unix or MacOS X system.

The MiOS openHAB Item file Generator scripts consist of:

* `miosLoad.sh` - A loader script that calls a MiOS Unit, over HTTP, and extracts it's metadata in the form of a `user_data.xml` file.
* `miosTransform.sh` - A wrapper script to transform a MiOS Unit `user_data.xml` file into the associated Items file.
* `miosTransform.xslt` - An XSLT file containing the rules for conversion from a MiOS Unit `user_data.xml` file into an Items file.

The following components are assumed installed on the system:

* `bash`
* `xsltproc` - http://xmlsoft.org/XSLT/xsltproc.html
* `curl` - http://curl.haxx.se/

### How to use

To run the conversion process, the following steps should be executed:

* Load (`miosLoad.sh`) - retrieves an XML version of the MiOS Device MetaData from your MiOS Unit.
In some cases, MiOS emits _invalid_ XML, so this content may have to be hand-edited prior to feeding it into the next step.  This script takes a single parameter, which is the IP Address, or DNS name, of the MiOS Unit to call.  The output of this phase is a file called `user_data.xml`

* Transform (`miosTransform.sh`) - converts the XML into a Textual openHAB Items file.
This script takes a single parameter, which is used for both the name of the generated Items file, as well as the MiOS Unit name used in openhab.cfg (eg. "house").  
If this completes successfully, it will have generated an output file like `house.items` which, after suitable manual review, can be used as an openHAB Items file.

This is an example of what it's like to run these command line scripts in sequence:

```ShellSession
Code: [Select]
me$ ./miosLoad.sh 192.168.1.100
Loading MiOS Unit Metadata from 192.168.1.100...
Metadata Loaded into user_data.xml!
me$ ./miosTransform.sh house
Transforming MiOS Unit Metadata from user_data.xml...
Metadata Transformed into house.items!
Duplicate Item names requiring manual fixes:
  String   DownstairsDeviceStatus "Downstairs Device Status [%s]" (GDevices) {mios="unit:house,device:385/status"}
  Number   DownstairsId "ID [%d]" (GDevices) {mios="unit:house,device:385/id"}
  String   LivingRoomSonosPIcon "Living Room Sonos (P) Icon [%s]" (GDevices,GRoom2) {mios="unit:house,device:295/service/DeviceProperties/Icon"}
  String   MasterBedroomSonosIcon "Master Bedroom Sonos Icon [%s]" (GDevices,GRoom7) {mios="unit:house,device:331/service/DeviceProperties/Icon"}
  String   SceneControllerConfigured "_Scene Controller Configured [%s]" (GDevices) {mios="unit:house,device:394/service/HaDevice1/Configured"}
  String   SceneControllerDeviceStatus "_Scene Controller Device Status [%s]" (GDevices) {mios="unit:house,device:393/status"}
  String   SceneControllerDeviceStatus "_Scene Controller Device Status [%s]" (GDevices) {mios="unit:house,device:394/status"}
  Number   SceneControllerId "ID [%d]" (GDevices) {mios="unit:house,device:394/id"}
  Number   SceneControllerId "ID [%d]" (GDevices) {mios="unit:house,device:4/id"}
  String   SceneControllerScenes "_Scene Controller Scenes [%s]" (GDevices) {mios="unit:house,device:393/service/SceneController1/Scenes"}
  String   SceneControllerScenes "_Scene Controller Scenes [%s]" (GDevices) {mios="unit:house,device:394/service/SceneController1/Scenes"}
  String   SceneMasterBathroom "Master Bathroom Scene" <sofa> (GScenes) {mios="unit:house,scene:34/status", autoupdate="false"}
  String   SceneMasterBathroom "Master Bathroom Scene" <sofa> (GScenes) {mios="unit:house,scene:35/status", autoupdate="false"}
  String   SceneTestArmed "TestArmed Scene" <sofa> (GScenes) {mios="unit:house,scene:73/status", autoupdate="false"}
  String   SceneTestDisarmed "TestDisarmed Scene" <sofa> (GScenes) {mios="unit:house,scene:76/status", autoupdate="false"}
  String   UpstairsDeviceStatus "Upstairs Device Status [%s]" (GDevices) {mios="unit:house,device:337/status"}
  Number   UpstairsId "ID [%d]" (GDevices) {mios="unit:house,device:337/id"}
  Contact  SceneMasterBathroomActive "Active [%s]" <sofa> (GScenes) {mios="unit:house,scene:34/active"}
  Contact  SceneMasterBathroomActive "Active [%s]" <sofa> (GScenes) {mios="unit:house,scene:35/active"}
  Contact  SceneTestArmedActive "Active [%s]" <sofa> (GScenes) {mios="unit:house,scene:79/active"}
  Contact  SceneTestDisarmedActive "Active [%s]" <sofa> (GScenes) {mios="unit:house,scene:80/active"}
```

At this point, you'll have a _fairly_ complete `house.items` file for use in openHAB.  The conversion process takes a number of steps to aid in generating unique Item names in the generated content, but any output in the _Duplicate Item names_ section should be addressed prior to use as an openHAB Items file.

The tool includes almost all of the _by-hand_ conversion rules used in other MiOS user's openHAB Configurations. MiOS UPnP State variables like `SwitchPower1/Status` become an openHAB `Switch` Item for example.   

The generator tools will get you close to a working openHAB Items file, especially for commonly used MiOS Device types.  For exotic MiOS Device Types, not handled by the MiOS internal defaults, the Items file will need to be augmented with the appropriate MAP Transformations.  This involves editing the file, and adding the appropriate `in:`, `out:`, and `command:` parameters per the documentation for the MiOS Binding.

