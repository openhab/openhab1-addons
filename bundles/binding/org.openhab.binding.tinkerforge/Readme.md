# Bugfixes
 ## 1.4.0
  * Missing updates of Items if a Tinkerforge Device is referenced in several Items
 ## 1.5.0
  * Reconnect support for IO16 Bricklet
  * polled values now are only send once to the eventbus
# Incompatible Changes
 ## 1.4.0
  * LCDBacklight is a sub device of LCD20x4 Bricklet (items file must be changed)
  * LCD20x4Button posts an update not a command anymore (rules must be changed)
  * IndustrialQuadRelay sub id numbering now starts from zero (items file must be changed)
# New Features
 ## 1.4.0
  * new Devices
    * Bricklet Industrial Quad Relay
    * Bricklet Industrial Digital In 4
    * Bricklet IO-16
    * support for serveral Item types
       * NumberItem
       * SwitchItem
       * ContactItem
  * handle disconnected brickds
     * on binding startup make retries every second
     * when binding is running use the Tinkerforge autoreconnect feature
 ## 1.5.0
  * new Devices
# Remote Switch Bricklet
* openhab.cfg (mandatory)
```
tinkerforge:rs1.uid=jKw
tinkerforge:rs1.type=bricklet_remote_switch
tinkerforge:rs1.typeADevices=rslr1 rslr2
tinkerforge:rs1.typeBDevices=kitchen
tinkerforge:rs1.typeCDevices=floor

tinkerforge:rs_living_room.uid=jKw
tinkerforge:rs_living_room.subid=rslr1
tinkerforge:rs_living_room.type=remote_switch_a
tinkerforge:rs_living_room.houseCode=31
tinkerforge:rs_living_room.receiverCode=8

tinkerforge:rs_living_room2.uid=jKw
tinkerforge:rs_living_room2.subid=rslr2
tinkerforge:rs_living_room2.type=remote_switch_a
tinkerforge:rs_living_room2.houseCode=31
tinkerforge:rs_living_room2.receiverCode=4

tinkerforge:rs_kitchen.uid=jKw
tinkerforge:rs_kitchen.subid=kitchen
tinkerforge:rs_kitchen.type=remote_switch_b
tinkerforge:rs_kitchen.address=344
tinkerforge:rs_kitchen.unit=9

tinkerforge:rs_floor.uid=jKw
tinkerforge:rs_floor.subid=floor
tinkerforge:rs_floor.type=remote_switch_c
tinkerforge:rs_floor.systemCode=A
tinkerforge:rs_floor.deviceCode=8
```
items file
```
Switch r0           {tinkerforge="uid=jKw, subid=rslr1"}
Switch r1           {tinkerforge="uid=jKw, subid=rslr2"}
Switch rb           {tinkerforge="uid=jKw, subid=kitchen"}
Switch rc           {tinkerforge="uid=jKw, subid=floor"}
```
* sitemap file
```
Switch item=r0
Switch item=r1
Switch item=rb
Switch item=rc
```

# Motion Detection Bricklet
* items file
```
Contact motion      "motion [MAP(en.map):MOTION%s]" {tinkerforge="uid=m3W"}
```
* sitemap file
```
Text item=motion
```
* en.map file
```
MOTIONCLOSED=no motion
MOTIONOPEN=montion detected
```

# Bricklet MultiTouch
* openhab.cfg
```
tinkerforge:touch.uid=jUS
tinkerforge:touch.type=bricklet_multitouch
tinkerforge:touch.sensitivity=181
tinkerforge:touch.recalibrate=true

tinkerforge:touche0.uid=jUS
tinkerforge:touche0.type=electrode
tinkerforge:touche0.subid=electrode1
tinkerforge:touche0.disableElectrode=true

tinkerforge:touche1.uid=jUS
tinkerforge:touche1.type=proximity
tinkerforge:touche1.subid=proximity
tinkerforge:touche1.disableElectrode=true
```
* items file
```
Contact electrode0      "electrode0 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode0"}
Contact electrode1      "electrode1 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode1"}
Contact electrode2      "electrode2 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode2"}
Contact electrode3      "electrode3 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode3"}
Contact electrode4      "electrode4 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode4"}
Contact electrode5      "electrode5 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode5"}
Contact electrode6      "electrode6 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode6"}
Contact electrode7      "electrode7 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode7"}
Contact electrode8      "electrode8 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode8"}
Contact electrode9      "electrode9 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode9"}
Contact electrode10     "electrode10 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode10"}
Contact electrode11     "electrode11 [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=electrode11"}
Contact proximity       "proximity [MAP(en.map):%s]" {tinkerforge="uid=jUS, subid=proximity"}
```
* sitemap file
```
Text item=electrode0
Text item=electrode1
Text item=electrode2
Text item=electrode3
Text item=electrode4
Text item=electrode5
Text item=electrode6
Text item=electrode7
Text item=electrode8
Text item=electrode9
Text item=electrode10
Text item=electrode11
Text item=proximity
```

# Bricklet TemperatureIR
* openhab.cfg (optional)
```
tinkerforge:objIR.uid=kr2
tinkerforge:objIR.subid=object_temperature
tinkerforge:objIR.type=object_temperature
tinkerforge:objIR.emissivity=65535
tinkerforge:objIR.threshold=0

tinkerforge:ambIR.uid=kr2
tinkerforge:ambIR.subid=ambient_temperature
tinkerforge:ambIR.type=ambient_temperature
tinkerforge:ambIR.threshold=0
```
* items
```
Number AmbientTemperature                 "AmbientTemperature [%.1f C]"  { tinkerforge="uid=kr2, subid=ambient_temperature" }
Number ObjectTemperature                 "ObjectTemperature [%.1f C]"  { tinkerforge="uid=kr2, subid=object_temperature" }
```
* sitemap
```
Text item=AmbientTemperature
Text item=ObjectTemperature
   ```

# Bricklet SoundIntensity
* openhab.cfg (optional)
```
tinkerforge:sound.uid=iQE
tinkerforge:sound.type=bricklet_soundintensity
tinkerforge:sound.threshold=1
tinkerforge:sound.callbackPeriod=5000
```
* items
```
Number SoundIntensity                 "Sound [%.1f]"  { tinkerforge="uid=iQE" }
```
* sitemap
```
Text item=SoundIntensity
```

# Bricklet Moisture
* openhab.cfg (optional)
```
tinkerforge:moisture.uid=kve
tinkerforge:moisture.type=bricklet_moisture
tinkerforge:moisture.threshold=0
tinkerforge:moisture.callbackPeriod=5000
tinkerforge:moisture.movingAverage=90
```
* items
```
Number Moisture                 "Moisture [%.1f]"  { tinkerforge="uid=kve" }
```
* sitemap
```
Text item=Moisture 
```

# Bricklet DistanceUS
* openhab.cfg (optional)
```
tinkerforge:distanceUS.uid=mXq
tinkerforge:distanceUS.type=bricklet_distanceUS
tinkerforge:distanceUS.threshold=0
tinkerforge:distanceUS.callbackPeriod=100
tinkerforge:distanceUS.movingAverage=100
```
* items
```
Number DistanceUS                 "DistanceUS [%.1f]"  { tinkerforge="uid=mXq" }
```
* sitemap
```
Text item=DistanceUS
```

# Bricklet VoltageCurrent
* openhab.cfg (optional)
```
tinkerforge:voltageCurrent.uid=dN5
tinkerforge:voltageCurrent.type=bricklet_voltageCurrent
tinkerforge:voltageCurrent.averaging=3
tinkerforge:voltageCurrent.voltageConversionTime=4
tinkerforge:voltageCurrent.currentConversionTime=4

tinkerforge:vc_voltage.uid=dN5
tinkerforge:vc_voltage.subid=voltageCurrent_voltage
tinkerforge:vc_voltage.type=voltageCurrent_voltage
tinkerforge:vc_voltage.threshold=20
tinkerforge:vc_voltage.callbackPeriod=100

tinkerforge:vc_current.uid=dN5
tinkerforge:vc_current.subid=voltageCurrent_current
tinkerforge:vc_current.type=voltageCurrent_current
tinkerforge:vc_current.threshold=10
tinkerforge:vc_current.callbackPeriod=100

tinkerforge:vc_power.uid=dN5
tinkerforge:vc_power.subid=voltageCurrent_power
tinkerforge:vc_power.type=voltageCurrent_power
tinkerforge:vc_power.threshold=10
tinkerforge:vc_power.callbackPeriod=100
```
* items
```
Number Voltage       "Voltage [%d mV]"  { tinkerforge="uid=dN5, subid=voltageCurrent_voltage" }
Number Current       "Current [%d mA]"  { tinkerforge="uid=dN5, subid=voltageCurrent_current" }
Number Power         "Power [%d mW]"  { tinkerforge="uid=dN5, subid=voltageCurrent_power" }
```

* sitemap
```
Text item=Voltage
Text item=Current
Text item=Power
```

# Bricklet Tilt
* openhab.cfg: no entries needed
* items
```
Contact tiltContact     "tilt [MAP(en.map):%s]" { tinkerforge="uid=j7k" }
Number tiltSensor       "tilt [MAP(en.map):%s]"  { tinkerforge="uid=j7k" }
Switch tiltSwitch         "tilt" { tinkerforge="uid=j7k" }
```

* sitemap
```
Text item=tiltContact
Text item=tiltSensor
Switch item=tiltSwitch
```
* en.map (optional)
```
0=closed
1=open
2=vibrating
```

# other changes
 ## 1.4.0
   * updated Tinkerforge API to 2.0.12
 ## 1.5.0
   * updated Tinkerforge API to 2.1.0
   * Threshold values now have the unit as the sensor value (incompatible change, you have to update your openhab.cfg)
   * polling is only done for devices which don't support CallbackListeners / InterruptListeners