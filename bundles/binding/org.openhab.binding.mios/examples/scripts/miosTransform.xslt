<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (c) 2010-2015, openHAB.org and others.

    All rights reserved. This program and the accompanying materials
    are made available under the terms of the Eclipse Public License v1.0
    which accompanies this distribution, and is available at
    http://www.eclipse.org/legal/epl-v10.html

-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:param name="MIOS_UNIT" select="'house'"/>

<xsl:variable name="BAD_CHARS">_#-!@$%^&amp;*=+~`[]{}\\|:;&quot;&lt;\>?/.() </xsl:variable>

<xsl:template match="/root">Group GAll
Group GPersist (GAll)

Group GSystem "System Information [%d]" &lt;office>

Group GRooms (GAll)
Group GScenes (GAll)
Group GDevices (GAll)

<xsl:apply-templates select="rooms"/>
<xsl:apply-templates select="devices/device[(not(@category_num) or (@category_num!='13'))
                             and @device_type!='urn:micasaverde-com:serialportroot'
                             and @device_type!='urn:micasaverde-org:device:SerialPort:1'
                             and @device_type!='urn:micasaverde-org:device:SerialPortRoot:1']"/>
<xsl:apply-templates select="scenes"/>

/* If you want more MiOS Internals, uncomment the following. */
/* Number   SystemDataVersion "System Data Version [%d]" (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/DataVersion"}                               */
/* DateTime SystemLoadTime "System Load Time [%1$ta, %1$tm/%1$te %1$tR]" &lt;calendar> (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/LoadTime"}    */
/* String   SystemLocalTime "System Local Time [%s]" (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/LocalTime"}                                     */
/* DateTime SystemTimeStamp "System Time Stamp [%1$ta, %1$tm/%1$te %1$tR]" &lt;calendar> (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/TimeStamp"} */
/* Number   SystemUserDataDataVersion "System User Data Version [%d]" (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/UserData_DataVersion"}         */

/* If you want Z-Wave Status, uncomment the following. */
/* Number   SystemZWaveStatus "System ZWave Status [%d]" (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/ZWaveStatus"}                                            */
/* String   SystemZWaveStatusString "System ZWave Status String [%d]" (GSystem) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,system:/ZWaveStatus,in:MAP(miosZWaveStatusIn.map)"} */
</xsl:template>

<xsl:template match="rooms">
<xsl:for-each select="room">Group GRoom<xsl:value-of select="@id"/> "<xsl:value-of select="translate(@name, '&quot;', '')"/> [%d]" &lt;office> (GRooms)
</xsl:for-each>
</xsl:template>

<xsl:template match="device">
<xsl:variable name="DeviceName">
<xsl:value-of select="translate(normalize-space(@name), $BAD_CHARS, '')"/>
</xsl:variable>

<xsl:variable name="DeviceNameFixed">
<xsl:value-of select="$DeviceName"/>
<xsl:if test="count(../device[translate(normalize-space(@name), $BAD_CHARS, '') = $DeviceName]) > 1">
<xsl:value-of select="@id"/>
</xsl:if>
</xsl:variable>
/* Device - <xsl:value-of select="@name"/> */
Number   <xsl:value-of select="$DeviceNameFixed"/>Id "ID [%d]" (GDevices) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,device:<xsl:value-of select="@id"/>/id"}
String   <xsl:value-of select="$DeviceNameFixed"/>DeviceStatus "<xsl:value-of select="@name"/> Device Status [MAP(miosDeviceStatusUI.map):%s]" (GDevices) {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,device:<xsl:value-of select="@id"/>/status"}
<xsl:for-each select="states">
  <xsl:apply-templates select="state[not (@service='urn:micasaverde-com:serviceId:HaDevice1' and @variable='IODevice')
                                     and not (@service='urn:micasaverde-com:serviceId:HaDevice1' and @variable='IODeviceXRef')
                                     and not (@service='urn:micasaverde-com:serviceId:HaDevice1' and @variable='IOPortPath')
                                     and not (@service='urn:micasaverde-com:serviceId:SwitchPower1' and @variable='Status')
                                     and not (@service='urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='ProviderKey')
                                     and not (@service='urn:garrettwp-com:serviceId:WPSwitch1' and @variable='username')
                                     and not (@service='urn:garrettwp-com:serviceId:WPSwitch1' and @variable='password')
                                     and not (@service='urn:macrho-com:serviceId:LiftMasterOpener1' and @variable='Username')
                                     and not (@service='urn:macrho-com:serviceId:LiftMasterOpener1' and @variable='Password')
                                     and not (@service='urn:rts-services-com:serviceId:ProgramLogicC')
                                     and not (@service='urn:rts-services-com:serviceId:ProgramLogicEG')
                                     and not (@service='urn:watou-com:serviceId:Nest1' and @variable='username')
                                     and not (@service='urn:watou-com:serviceId:Nest1' and @variable='password')
                                     and not (@service='urn:watou-com:serviceId:Nest1' and @variable='access_token')
                                     and not (@service='urn:watou-com:serviceId:Nest1' and @variable='userid')
                                     and not (@service='urn:upnp-org:serviceId:BatteryMonitor1' and @variable='LowDeviceList')
                                     and not (@service='urn:upnp-org:serviceId:BatteryMonitor1' and @variable='MidDeviceList')
                                     and not (@service='urn:upnp-org:serviceId:BatteryMonitor1' and @variable='HighDeviceList')
                                     and not (@service='urn:upnp-org:serviceId:BatteryMonitor1' and @variable='MonitoredDeviceList')
                                     and not (@service='urn:upnp-org:serviceId:BatteryMonitor1' and @variable='UnMonitoredDeviceList')
                                     and not (@service='urn:upnp-org:serviceId:VSwitch1' and @variable='UI7Check')
                                     and not (@service='urn:upnp-empuk-net:serviceId:SimpleAlarm1' and @variable='XendAppUsername')
                                     and not (@service='urn:upnp-empuk-net:serviceId:SimpleAlarm1' and @variable='XendAppPassword')
                                     and not (@service='urn:upnp-empuk-net:serviceId:SimpleAlarm1' and @variable='AwaySensorsDeviceList')
                                     and not (@service='urn:upnp-empuk-net:serviceId:SimpleAlarm1' and @variable='HomeSensorsDeviceList')
                                     and not (@service='urn:upnp-empuk-net:serviceId:SimpleAlarm1' and @variable='PanicSensorsDeviceList')
                                    ]"/>
</xsl:for-each>
</xsl:template>

<xsl:template match="state">
<xsl:variable name="ServiceAlias">
<xsl:choose>
<xsl:when test="@service = 'urn:upnp-org:serviceId:SwitchPower1'"                >SwitchPower1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:Dimming1'"                    >Dimming1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSensor1'"          >TemperatureSensor1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:HVAC_FanOperatingMode1'"      >HVAC_FanOperatingMode1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:HVAC_UserOperatingMode1'"     >HVAC_UserOperatingMode1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Cool'"   >TemperatureSetpoint1_Cool</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Heat'"   >TemperatureSetpoint1_Heat</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:AVTransport'"                 >AVTransport</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:RenderingControl'"            >RenderingControl</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:DeviceProperties'"            >DeviceProperties</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:HouseStatus1'"                >HouseStatus1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:ContentDirectory'"            >ContentDirectory</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:AudioIn'"                     >AudioIn</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:ZoneGroupTopology'"           >ZoneGroupTopology</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveDevice1'"         >ZWaveDevice1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'"        >ZWaveNetwork1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'"            >HaDevice1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HouseModes1'"          >HouseModes1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneControllerLED1'"  >SceneControllerLED1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'"      >SecuritySensor1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HumiditySensor1'"      >HumiditySensor1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1'"      >EnergyMetering1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneController1'"     >SceneController1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HVAC_OperatingState1'" >HVAC_OperatingState1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SerialPort1'"          >SerialPort1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'"            >DoorLock1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:AlarmPartition2'"      >AlarmPartition2</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Camera1'"              >Camera1</xsl:when>
<xsl:when test="@service = 'urn:upnp-arduino-cc:serviceId:arduino1'"             >Arduino1</xsl:when>
<xsl:when test="@service = 'urn:upnp-arduino-cc:serviceId:arduinonode1'"         >ArduinoNode1</xsl:when>
<xsl:when test="@service = 'urn:akbooer-com:serviceId:DataYours1'"               >DataYours1</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'"         >SystemMonitor</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:Config'"                >CDJConfig</xsl:when>
<xsl:when test="@service = 'urn:garrettwp-com:serviceId:WPSwitch1'"              >WPSwitch1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Nest1'"                >Nest1</xsl:when>
<xsl:when test="@service = 'urn:watou-com:serviceId:NestStructure1'"             >NestStructure1</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1'"        >Weather1</xsl:when>
<xsl:when test="@service = 'urn:demo-ted-striker:serviceId:PingSensor1'"         >PingSensor1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Sonos1'"               >Sonos1</xsl:when>
<xsl:when test="@service = 'urn:demo-paradox-com:serviceId:ParadoxSecurityEVO1'" >Paradox</xsl:when>
<xsl:when test="@service = 'upnp-rfxcom-com:serviceId:rfxtrx1'"                  >Rfxtrx</xsl:when>
<xsl:when test="@service = 'urn:rfxcom-com:serviceId:rfxtrx1'"                   >RfxtrxNew</xsl:when>
<xsl:when test="@service = 'urn:macrho-com:serviceId:LiftMasterOpener1'"         >LiftMasterOpener1</xsl:when>
<xsl:when test="@service = 'urn:directv-com:serviceId:DVR1'"                     >DirecTVDVR1</xsl:when>
<xsl:when test="@service = 'urn:richardgreen:serviceId:VeraAlert1'"              >VeraAlert1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:VSwitch1'"                    >VSwitch1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:DigitalSecurityCameraSettings1'">DigitalSecurityCameraSettings1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:DigitalSecurityCameraStillImage1'">DigitalSecurityCameraStillImage1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:CameraMotionDetection1'">CameraMotionDetection1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DiscretePower1'"       >DiscretePower1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:EnergyCalculator1'"           >EnergyCalculator1</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:FanSpeed1'"                   >FanSpeed1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:GenericSensor1'"       >GenericSensor1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:InputSelection1'"      >InputSelection1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:LightSensor1'"         >LightSensor1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Keypad1'"              >Keypad1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:LightSensor1'"         >LightSensor1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:MediaNavigation1'"     >MediaNavigation1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:MenuNavigation1'"      >MenuNavigation1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:MiosUpdater1'"         >MiosUpdater1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Misc1'"                >Misc1</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'"                 >MSwitch1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:NumericEntry1'"        >NumericEntry1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:PIP1'"                 >PIP1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Scene1'"               >Scene1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:TV1'"                  >TV1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:TogglePower1'"         >TogglePower1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Tuning1'"              >Tuning1</xsl:when>
<xsl:when test="@service = 'urn:futzle-com:serviceId:UPnPProxy1'"                >UPnPProxy1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:VideoAdjustment1'"     >VideoAdjustment1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Volume1'"              >Volume1</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:WMC1'"                 >WMC1</xsl:when>
<xsl:otherwise><xsl:value-of select="@service"/></xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:variable name="ItemType">
<xsl:choose>
<xsl:when test="@service = 'urn:upnp-org:serviceId:SwitchPower1'              and @variable = 'Status'"              >Switch</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:SwitchPower1'              and @variable = 'Target'"              >Integer</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:Dimming1'                  and @variable = 'LoadLevelStatus'"     >Dimmer</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:Dimming1'                  and @variable = 'LoadLevelTarget'"     >Dimmer</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSensor1'        and @variable = 'CurrentTemperature'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Cool' and @variable = 'CurrentSetpoint'"     >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Heat' and @variable = 'CurrentSetpoint'"     >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:RenderingControl'          and @variable = 'Volume'"              >Dimmer</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:RenderingControl'          and @variable = 'Mute'"                >Switch</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Camera1'            and @variable = 'AutoArchiveSeconds'"  >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Camera1'            and @variable = 'SensorArchiveSeconds'"     >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Camera1'            and @variable = 'AutoArchivePreserveDays'"  >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Camera1'            and @variable = 'Timeout'"             >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'Configured'"          >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'LastUpdate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'LastTimeCheck'"       >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'LastTimeOffset'"      >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'FirstConfigured'"     >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'BatteryDate'"         >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'BatteryLevel'"        >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'CommFailure'"         >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'CommFailureTime'"     >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'sl_BatteryAlarm'"     >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HaDevice1'          and @variable = 'PollingEnabled'"      >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'LastTrip'"            >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'LastTripAlert'"       >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'LastUntrip'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'LastUntripAlert'"     >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'LastTamper'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'Tripped'"             >Contact</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SecuritySensor1'    and @variable = 'Armed'"               >Switch</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1'    and @variable = 'KWHReading'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1'    and @variable = 'KWH'"                 >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1'    and @variable = 'Watts'"               >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1'    and @variable = 'UserSuppliedWattage'" >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:AlarmPartition2'    and @variable = 'LastAlarmActive'"     >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'      and @variable = 'ConsecutivePollFails'" >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'      and @variable = 'LastUpdate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'      and @variable = 'LastDongleBackup'"    >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'      and @variable = 'LastHeal'"            >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'      and @variable = 'LastRouteFailure'"    >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:ZWaveNetwork1'      and @variable = 'LastPollSuccess'"     >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:MiosUpdater1'       and @variable = 'LastCheck'"           >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'Status'"              >Switch</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'MinPinSize'"          >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'MaxPinSize'"          >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'sl_CodeChanged'"      >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'sl_LockChanged'"      >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'sl_LowBattery'"       >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'sl_LockButton'"       >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'sl_LockFailure'"      >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:DoorLock1'          and @variable = 'sl_PinFailed'"        >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:HumiditySensor1'    and @variable = 'CurrentLevel'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:GenericSensor1'     and @variable = 'CurrentLevel'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:LightSensor1'       and @variable = 'CurrentLevel'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneController1'   and @variable = 'LastSceneTime'"       >DateTime</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneController1'   and @variable = 'LastSceneID'"         >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneController1'   and @variable = 'NumButtons'"          >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneController1'   and @variable = 'sl_SceneActivated'"   >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:SceneController1'   and @variable = 'sl_SceneDeactivated'" >Integer</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Nest1'              and @variable = 'Status'"              >Contact</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:Weather1'           and @variable = 'LastUpdate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'LastUpdate'"               >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.1.StartDate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.2.StartDate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.3.StartDate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.4.StartDate'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.1.EndDate'"            >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.2.EndDate'"            >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.3.EndDate'"            >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alert.4.EndDate'"            >DateTime</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='WindDegrees'"                >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='WindGust'"                   >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Solar'"                      >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='UV'"                         >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Pressure'"                   >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='FeelsLike'"                  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='DewPoint'"                   >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='HeatIndex'"                  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='WindChill'"                  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Alerts'"                     >Integer</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecasts'"                  >Integer</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.HighTemperature'" >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.LowTemperature'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.MaxWindSpeed'"    >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.MaxWindDegrees'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.POP'"             >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.QPFDay'"          >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.QPFNight'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.SnowDay'"         >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.0.SnowNight'"       >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.HighTemperature'" >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.LowTemperature'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.MaxWindSpeed'"    >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.MaxWindDegrees'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.POP'"             >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.QPFDay'"          >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.QPFNight'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.SnowDay'"         >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.1.SnowNight'"       >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.HighTemperature'" >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.LowTemperature'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.MaxWindSpeed'"    >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.MaxWindDegrees'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.POP'"             >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.QPFDay'"          >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.QPFNight'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.SnowDay'"         >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.2.SnowNight'"       >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.HighTemperature'" >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.LowTemperature'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.MaxWindSpeed'"    >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.MaxWindDegrees'"  >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.POP'"             >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.QPFDay'"          >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.QPFNight'"        >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.SnowDay'"         >Number</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable='Forecast.3.SnowNight'"       >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='systemLuupRestartUnix'"      >DateTime</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='cmhLastRebootUnix'"          >DateTime</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='systemVeraRestartUnix'"      >DateTime</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='SamplePeriodCPU'"            >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='SamplePeriodMem'"            >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='SamplePeriodUptime'"         >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='memoryTotal'"                >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='memoryFree'"                 >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='memoryBuffers'"              >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='memoryCached'"               >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='memoryUsed'"                 >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='memoryAvailable'"            >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='cpuLoad1'"                   >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='cpuLoad5'"                   >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='cpuLoad15'"                  >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='procRunning'"                >Integer</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='procTotal'"                  >Integer</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='uptimeTotal'"                >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='uptimeIdle'"                 >Number</xsl:when>
<xsl:when test="@service = 'urn:cd-jackson-com:serviceId:SystemMonitor'  and @variable='systemLuupRestart'"          >Number</xsl:when>
<xsl:when test="@service = 'urn:demo-ted-striker:serviceId:PingSensor1'  and @variable='Period'"                     >Integer</xsl:when>
<xsl:when test="@service = 'urn:macrho-com:serviceId:LiftMasterOpener1'  and @variable='Timestamp'"                  >DateTime</xsl:when>
<xsl:when test="@service = 'urn:richardgreen:serviceId:VeraAlert1'       and @variable='LastUpdate'"                 >DateTime</xsl:when>
<xsl:when test="@service = 'urn:intvelt-com:serviceId:HueColors1'        and @variable='ColorTemperature'"           >Number</xsl:when>
<xsl:when test="@service = 'urn:intvelt-com:serviceId:HueColors1'        and @variable='Hue'"                        >Number</xsl:when>
<xsl:when test="@service = 'urn:intvelt-com:serviceId:HueColors1'        and @variable='Saturation'"                 >Number</xsl:when>
<xsl:when test="@service = 'urn:intvelt-com:serviceId:HueColors1'        and @variable='CurrentPreset'"              >Integer</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status1'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status2'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status3'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status4'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status5'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status6'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status7'"                    >Switch</xsl:when>
<xsl:when test="@service = 'urn:dcineco-com:serviceId:MSwitch1'          and @variable='Status8'"                    >Switch</xsl:when>
<xsl:when test="@service = 'upnp-rfxcom-com:serviceId:rfxtrx1'           and @variable='NbrDevices'"                 >Integer</xsl:when>
<xsl:when test="@service = 'upnp-rfxcom-com:serviceId:rfxtrx1'           and @variable='Voltage'"                    >Number</xsl:when>
<xsl:otherwise>String</xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:variable name="DeviceName">
<xsl:value-of select="translate(normalize-space(../../@name), $BAD_CHARS, '')"/>
</xsl:variable>

<xsl:variable name="DeviceNameFixed">
<xsl:value-of select="$DeviceName"/>
<xsl:if test="count(../../../device[translate(normalize-space(@name), $BAD_CHARS, '') = $DeviceName]) > 1">
<xsl:value-of select="../../@id"/>
</xsl:if>
</xsl:variable>

<xsl:variable name="VariableName">
<xsl:value-of select="translate(normalize-space(@variable), $BAD_CHARS, '')"/>
</xsl:variable>

<xsl:variable name="ItemName">
<xsl:value-of select="$DeviceNameFixed"/>
<xsl:value-of select="$VariableName"/>
<!-- ** Handle duplicate UPnP Variable names by picking the winning ServiceId explicitly, and suffixing the state@id for all others.
     ** Id and Device Status are reserved, since we use them up front in the generation process.
     **
     ** Some rules are for legacy ServiceId's that may have been introduced early in the Plugin Dev, but no longer used. -->
<xsl:if test="count(../state[translate(normalize-space(@variable), $BAD_CHARS, '') = $VariableName]) > 1
              or $VariableName = 'Id'
              or $VariableName = 'DeviceStatus'
             ">
<xsl:choose>
<xsl:when test="@variable='LastUpdate'      and @service = 'urn:micasaverde-com:serviceId:HaDevice1'"         ></xsl:when>
<xsl:when test="@variable='ZoneName'        and @service = 'urn:upnp-org:serviceId:DeviceProperties'"         ></xsl:when>
<xsl:when test="@variable='Target'          and @service = 'urn:upnp-org:serviceId:SwitchPower1'"             ></xsl:when>
<xsl:when test="@variable='Status'          and @service = 'urn:upnp-org:serviceId:SwitchPower1'"             ></xsl:when>
<xsl:when test="                                @service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Heat'" >Heat</xsl:when>
<xsl:when test="                                @service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Cool'" >Cool</xsl:when>
<xsl:when test="                                @service = 'urn:upnp-org:serviceId:HVAC_FanOperatingMode1'"    >Fan</xsl:when>
<xsl:when test="                                @service = 'urn:upnp-org:serviceId:HVAC_UserOperatingMode1'"   >User</xsl:when>
<xsl:otherwise><xsl:value-of select="@id"/></xsl:otherwise>
</xsl:choose>
</xsl:if>
</xsl:variable>

<xsl:variable name="ItemDescription">
<xsl:value-of select="../../@name"/>
<xsl:choose>
<xsl:when test="@variable = 'BatteryLevel'"         > Battery Level</xsl:when>
<xsl:when test="@variable = 'BatteryDate'"          > Battery Date</xsl:when>
<xsl:when test="@variable = 'Timestamp'"            > Timestamp</xsl:when>
<xsl:when test="@variable = 'FirstConfigured'"      > First Configured</xsl:when>
<xsl:when test="@variable = 'LastSceneTime'"        > Last Scene Time</xsl:when>
<xsl:when test="@variable = 'LastHeal'"             > Last Heal</xsl:when>
<xsl:when test="@variable = 'Tripped'"              > Tripped</xsl:when>
<xsl:when test="@variable = 'Armed'"                > Armed</xsl:when>
<xsl:when test="@variable = 'AlarmMemory'"          > Alarm Memory</xsl:when>
<xsl:when test="@variable = 'VendorStatusCode'"     > Vendor Status Code</xsl:when>
<xsl:when test="@variable = 'VendorStatusData'"     > Vendor Status Data</xsl:when>
<xsl:when test="@variable = 'VendorStatus'"         > Vendor Status</xsl:when>
<xsl:when test="@variable = 'LastAlarmActive'"      > Last Alarm Active</xsl:when>
<xsl:when test="@variable = 'LastUser'"             > Last User</xsl:when>
<xsl:when test="@variable = 'ArmMode'"              > Arm Mode</xsl:when>
<xsl:when test="@variable = 'DetailedArmMode'"      > Detailed Arm Mode</xsl:when>
<xsl:when test="@variable = 'Alarm'"                > Alarm</xsl:when>
<xsl:when test="@variable = 'Period'"               > Period</xsl:when>
<xsl:when test="@variable = 'Address'"              > Address</xsl:when>
<xsl:when test="@variable = 'PostalCode'"           > Postal Code</xsl:when>
<xsl:when test="@variable = 'CountryCode'"          > Country Code</xsl:when>
<xsl:when test="@variable = 'Location'"             > Location</xsl:when>
<xsl:when test="@variable = 'DirectStreamingURL'"   > Streaming URL</xsl:when>
<xsl:when test="@variable = 'Timeout'"              > Timeout</xsl:when>
<xsl:when test="@variable = 'Address'"              > Address</xsl:when>
<xsl:when test="@variable = 'Invert'"               > Invert</xsl:when>
<xsl:when test="@variable = 'Chime'"                > Chime</xsl:when>
<xsl:when test="@variable = 'Cycle'"                > Cycle</xsl:when>
<xsl:when test="@variable = 'Mute'"                 > Mute</xsl:when>
<xsl:when test="@variable = 'Mode'"                 > Mode</xsl:when>
<xsl:when test="@variable = 'Commands'"             > Commands</xsl:when>
<xsl:when test="@variable = 'Status'"               > Status</xsl:when>
<xsl:when test="@variable = 'LastTrip'"             > Last Trip</xsl:when>
<xsl:when test="@variable = 'LastError'"            > Last Error</xsl:when>
<xsl:when test="@variable = 'LastTimeCheck'"        > Last Time Check</xsl:when>
<xsl:when test="@variable = 'LastTimeOffset'"       > Last Time Offset</xsl:when>
<xsl:when test="@variable = 'LastRouteFailure'"     > Last Route Failure</xsl:when>
<xsl:when test="@variable = 'OccupancyState'"       > Occupancy State</xsl:when>
<xsl:when test="@variable = 'StreetAddress'"        > Street Address</xsl:when>
<xsl:when test="@variable = 'IgnoreRoom'"           > Ignore Room</xsl:when>
<xsl:when test="@variable = 'CurrentSetpoint'"      > Current Setpoint</xsl:when>
<xsl:when test="@variable = 'CurrentTemperature'"   > Current Temperature</xsl:when>
<xsl:when test="@variable = 'CurrentLevel'"         > Current Level</xsl:when>
<xsl:when test="@variable = 'SceneShortcuts'"       > Scene Shortcuts</xsl:when>
<xsl:when test="@variable = 'ProprietaryScenes'"    > Proprietary Scenes</xsl:when>
<xsl:when test="@variable = 'sl_SceneActivated'"    > Scene Activated</xsl:when>
<xsl:when test="@variable = 'sl_SceneDeactivated'"  > Scene Deactivated</xsl:when>
<xsl:when test="@variable = 'sl_LockFailure'"       > Lock Failure</xsl:when>
<xsl:when test="@variable = 'sl_LockButton'"        > Lock Button</xsl:when>
<xsl:when test="@variable = 'sl_PinFailed'"         > PIN Failed</xsl:when>
<xsl:when test="@variable = 'sl_LowBattery'"        > Low Battery</xsl:when>
<xsl:when test="@variable = 'sl_CodeChanged'"       > Code Changed</xsl:when>
<xsl:when test="@variable = 'sl_LockChanged'"       > Lock Changed</xsl:when>
<xsl:when test="@variable = 'sl_BatteryAlarm'"      > Battery Alarm</xsl:when>
<xsl:when test="@variable = 'sl_UserCode'"          > User Code</xsl:when>
<xsl:when test="@variable = 'Volume'"               > Volume</xsl:when>
<xsl:when test="@variable = 'Input'"                > Input</xsl:when>
<xsl:when test="@variable = 'Channel'"              > Channel</xsl:when>
<xsl:when test="@variable = 'Title'"                > Title</xsl:when>
<xsl:when test="@variable = 'URL'"                  > URL</xsl:when>
<xsl:when test="@variable = 'ModeStatus'"           > Mode Status</xsl:when>
<xsl:when test="@variable = 'FanStatus'"            > Fan Status</xsl:when>
<xsl:when test="@variable = 'ModeState'"            > Mode State</xsl:when>
<xsl:when test="@variable = 'ModeStateForEnergy'"   > Mode State (Energy)</xsl:when>
<xsl:when test="@variable = 'ManageLeds'"           > Manage LEDs</xsl:when>
<xsl:when test="@variable = 'FiresOffEvents'"       > Fires Off Events</xsl:when>
<xsl:when test="@variable = 'MinPinSize'"           > Min Pin Size</xsl:when>
<xsl:when test="@variable = 'MaxPinSize'"           > Max Pin Size</xsl:when>
<xsl:when test="@variable = 'UserSuppliedWattage'"  > User Supplied Wattage</xsl:when>
<xsl:when test="@variable = 'KWH'"                  > kWh</xsl:when>
<xsl:when test="@variable = 'Watts'"                > Watts</xsl:when>
<xsl:when test="@variable = 'ModeTarget'"           > Mode Target</xsl:when>
<xsl:when test="@variable = 'CommFailure'"          > Comms Failure</xsl:when>
<xsl:when test="@variable = 'Target'"               > Target</xsl:when>
<xsl:when test="@variable = 'LastUpdate'"           > Last Update</xsl:when>
<xsl:when test="@variable = 'LoadLevelTarget'"      > Load Level Target</xsl:when>
<xsl:when test="@variable = 'LoadLevelStatus'"      > Load Level Status</xsl:when>
<xsl:when test="@variable = 'LightSettings'"        > Light Settings</xsl:when>
<xsl:when test="@variable = 'Scenes'"               > Scenes</xsl:when>
<xsl:when test="@variable = 'Log'"                  > Log</xsl:when>
<xsl:when test="@variable = 'Logging'"              > Logging</xsl:when>
<xsl:when test="@variable = 'PollingEnabled'"       > Polling Enabled</xsl:when>
<xsl:when test="@variable = 'PollingFrequency'"     > Polling Frequency</xsl:when>
<xsl:when test="@variable = 'NumButtons'"           > #Buttons</xsl:when>
<xsl:when test="@variable = 'Configured'"           > Configured</xsl:when>
<xsl:when test="@variable = 'CurrentStatus'"        > Current Status</xsl:when>
<xsl:when test="@variable = 'TransportState'"       > Transport State</xsl:when>
<xsl:when test="@variable = 'TransportStatus'"      > Transport Status</xsl:when>
<xsl:when test="@variable = 'TransportPlaySpeed'"   > Transport Play Speed</xsl:when>
<xsl:when test="@variable = 'CurrentPlayMode'"      > Current Play Mode</xsl:when>
<xsl:when test="@variable = 'CurrentTrackTitle'"    > Current Track Title</xsl:when>
<xsl:when test="@variable = 'CurrentStreamTitle'"   > Current Stream Title</xsl:when>
<xsl:when test="@variable = 'CurrentAlbumArt'"      > Current Album Art</xsl:when>
<xsl:when test="@variable = 'CurrentTitle'"         > Current Title</xsl:when>
<xsl:when test="@variable = 'CurrentDetails'"       > Current Details</xsl:when>
<xsl:when test="@variable = 'CurrentArtist'"        > Current Artist</xsl:when>
<xsl:when test="@variable = 'CurrentAlbum'"         > Current Album</xsl:when>
<xsl:when test="@variable = 'CurrentTrack'"         > Current Track</xsl:when>
<xsl:when test="@variable = 'CurrentTrackDuration'" > Current Track Duration</xsl:when>
<xsl:when test="@variable = 'CurrentService'"       > Current Service</xsl:when>
<xsl:when test="@variable = 'CurrentRadio'"         > Current Radio</xsl:when>
<xsl:when test="@variable = 'ZoneName'"             > Zone Name</xsl:when>
<xsl:when test="@variable = 'Icon'"                 > Icon</xsl:when>
<xsl:when test="@variable = 'Preset1'"              > Preset 1</xsl:when>
<xsl:when test="@variable = 'Preset2'"              > Preset 2</xsl:when>
<xsl:when test="@variable = 'Preset3'"              > Preset 3</xsl:when>
<xsl:when test="@variable = 'Preset4'"              > Preset 4</xsl:when>
<xsl:when test="@variable = 'Preset5'"              > Preset 5</xsl:when>
<xsl:when test="@variable = 'Preset6'"              > Preset 6</xsl:when>
<xsl:when test="@variable = 'ColorTemperature'"     > Color Temperature</xsl:when>
<xsl:when test="@variable = 'Status1'"              > Status 1</xsl:when>
<xsl:when test="@variable = 'Status2'"              > Status 2</xsl:when>
<xsl:when test="@variable = 'Status3'"              > Status 3</xsl:when>
<xsl:when test="@variable = 'Status4'"              > Status 4</xsl:when>
<xsl:when test="@variable = 'Status5'"              > Status 5</xsl:when>
<xsl:when test="@variable = 'Status6'"              > Status 6</xsl:when>
<xsl:when test="@variable = 'Status7'"              > Status 7</xsl:when>
<xsl:when test="@variable = 'Status8'"              > Status 8</xsl:when>
<xsl:when test="@variable = 'Text1'"                > Text 1</xsl:when>
<xsl:when test="@variable = 'Text2'"                > Text 2</xsl:when>
<xsl:when test="@variable = 'Codesets'"             > Codesets</xsl:when>
<xsl:when test="@variable = 'Codeset'"              > Codeset</xsl:when>
<xsl:when test="@variable = 'Hue'"                  > Hue</xsl:when>
<xsl:when test="@variable = 'Saturation'"           > Saturation</xsl:when>
<xsl:when test="@variable = 'LinkStatus'"           > Link Status</xsl:when>
<xsl:when test="@variable = 'UUID'"                 > UUID</xsl:when>
<xsl:when test="@variable = 'LowLevel'"             > LowLevel</xsl:when>
<xsl:when test="@variable = 'MidLevel'"             > MidLevel</xsl:when>
<xsl:when test="@variable = 'HighLevel'"            > HighLevel</xsl:when>
<xsl:when test="@variable = 'LastRecipient'"        > Last Recipient</xsl:when>
<xsl:when test="@variable = 'LastMsgSent'"          > Last Message Sent</xsl:when>
<xsl:when test="@variable = 'Msg'"                  > Message</xsl:when>
<xsl:when test="@variable = 'Debug'"                > Debug</xsl:when>
<xsl:when test="@variable = 'DebugMode'"            > Debug Mode</xsl:when>
<xsl:when test="@variable = 'State'"                > State</xsl:when>
<xsl:when test="@variable = 'Options'"              > Options</xsl:when>
<xsl:when test="@variable = 'Devices'"              > Devices</xsl:when>
<xsl:when test="@variable = 'MaxLevel'"             > Maximum Level</xsl:when>
<xsl:when test="@variable = 'MinInterval'"          > Minimum Interval</xsl:when>
<xsl:when test="@variable = 'Remote'"               > Remote</xsl:when>
<xsl:when test="@variable = 'MfrId'"                > Manufacturer Id</xsl:when>
<xsl:when test="@variable = 'Last Check'"           > Last Check</xsl:when>
<xsl:when test="@variable = 'LastCheck'"            > Last Check</xsl:when>
<xsl:when test="@variable = 'AdjustMultiplier'"     > Adjust Multipler</xsl:when>
<xsl:when test="@variable = 'AdjustConstant'"       > Adjust Constant</xsl:when>
<xsl:when test="@variable = 'AdjustConstant2'"      > Adjust Constant 2</xsl:when>
<xsl:when test="@variable = 'RepeatEvent'"          > Repeat Event</xsl:when>
<xsl:when test="@variable = 'ActualUsage'"          > Actual Usage</xsl:when>
<xsl:when test="@variable = 'ArmedTripped'"         > Armed/Tripped</xsl:when>
<xsl:when test="@variable = 'KWHReading'"           > kWh Reading</xsl:when>
<xsl:when test="@variable = 'ZoneGroupState'"       > Zone Group State</xsl:when>
<xsl:when test="@variable = 'FirmwareVersion'"      > Firmware Version</xsl:when>
<xsl:when test="@variable = 'DiscoveryResult'"      > Discovery Result</xsl:when>
<xsl:when test="@variable = 'CurrentTransportActions'"      > Current Transport Actions</xsl:when>
<xsl:when test="@variable = 'PluginVersion'"        > Plugin Version</xsl:when>
<xsl:when test="@variable = 'DefaultLanguageTTS'"   > Default TTS Language</xsl:when>
<xsl:when test="@variable = 'DefaultEngineTTS'"     > Default TTS Engine</xsl:when>
<xsl:when test="@variable = 'GoogleTTSServerURL'"   > Google TTS URL</xsl:when>
<xsl:when test="@variable = 'OSXTTSServerURL'"      > OSX TTS URL</xsl:when>
<xsl:when test="@variable = 'MaryTTSServerURL'"     > Mary TTS URL</xsl:when>
<xsl:when test="@variable = 'MicrosoftClientId'"    > Microsoft Client Id</xsl:when>
<xsl:when test="@variable = 'MicrosoftClientSecret'" > Microsoft Client Secret</xsl:when>
<xsl:when test="@variable = 'CurrentMediaDuration'" > Current Media Duration</xsl:when>
<xsl:when test="@variable = 'CurrentCrossfadeMode'" > Current Crossfade Mode</xsl:when>
<xsl:when test="@variable = 'NumberOfTracks'"       > Number of Tracks</xsl:when>
<xsl:when test="@variable = 'SonosModel'"           > Sonos Model</xsl:when>
<xsl:when test="@variable = 'SonosModelName'"       > Sonos Model Name</xsl:when>
<xsl:when test="@variable = 'SonosServicesKeys'"    > Sonos Service Keys</xsl:when>
<xsl:when test="@variable = 'Favorites'"            > Favorites</xsl:when>
<xsl:when test="@variable = 'RelativeTimePosition'" > Relative Time Position</xsl:when>
<xsl:when test="@variable = 'SonosOnline'"          > Sonos Online</xsl:when>
<xsl:when test="@variable = 'CurrentTrackMetaData'" > Current Track Meta-Data</xsl:when>
<xsl:when test="@variable = 'SonosID'"              > Sonos ID</xsl:when>
<xsl:when test="@variable = 'GroupCoordinator'"     > Group Co-ordinator</xsl:when>
<xsl:when test="@variable = 'CurrentTrackURI'"      > Current Track URI</xsl:when>
<xsl:when test="@variable = 'AVTransportURI'"       > AV Transport URI</xsl:when>
<xsl:when test="@variable = 'DebugLogs'"            > Debug Logs</xsl:when>
<xsl:when test="@variable = 'FetchQueue'"           > Fetch Queue</xsl:when>
<xsl:when test="@variable = 'RouterIp'"             > Router IP</xsl:when>
<xsl:when test="@variable = 'RouterPort'"           > Router Port</xsl:when>
<xsl:when test="@variable = 'ProxyUsed'"            > Proxy Used</xsl:when>
<xsl:when test="@variable = 'StatusText'"           > Status Text</xsl:when>
<xsl:when test="@variable = 'DisabledDevices'"      > Disabled Devices</xsl:when>
<xsl:when test="@variable = 'AutoCreate'"           > Auto Create</xsl:when>
<xsl:when test="@variable = 'NbrDevices'"           > # Devices</xsl:when>
<xsl:when test="@variable = 'Voltage'"              > Voltage</xsl:when>
<xsl:when test="@variable = 'LastReceivedMsg'"      > Last Received Msg</xsl:when>
<xsl:when test="@variable = 'NorthQDevice'"         > NorthQ Device</xsl:when>
<xsl:when test="@variable = 'API'"                  > API</xsl:when>
<xsl:when test="@variable = 'Association'"          > Association</xsl:when>
<xsl:when test="@variable = 'Documentation'"        > Documentation</xsl:when>
<xsl:when test="@variable = 'ModeSetting'"          > Mode Setting</xsl:when>
<xsl:when test="@variable = 'IgnoreTripTime'"       > Ignore Trip Time</xsl:when>
<xsl:when test="@variable = 'LastPollSuccess'"      > Last Poll Success</xsl:when>
<xsl:when test="@variable = 'ChildrenSameRoom'"     > Children Same Room</xsl:when>
<xsl:when test="@variable = 'SketchVersion'"        > Sketch Version</xsl:when>
<xsl:when test="@variable = 'SketchName'"           > Sketch Name</xsl:when>
<xsl:when test="@variable = 'RelayNode'"            > Relay Node</xsl:when>
<xsl:when test="@variable = 'RelayNodeHR'"          > Relay Node HR</xsl:when>
<xsl:when test="@variable = 'ArduinoLibVersion'"    > Arduino Library Version</xsl:when>
<xsl:when test="@variable = 'PluginVersion'"        > Plugin Version</xsl:when>
<xsl:when test="@variable = 'ArmedTripped'"         > Armed/Tripped</xsl:when>
<xsl:when test="@variable = 'LastTripAlert'"        > Last Trip Alert</xsl:when>
<xsl:when test="@variable = 'LastUntripAlert'"      > Last Untrip Alert</xsl:when>
<xsl:when test="@variable = 'LastUntrip'"           > Last Untrip</xsl:when>
<xsl:when test="@variable = 'LastTamper'"           > Last Tamper</xsl:when>
<xsl:when test="@variable = 'LastUpdateHR'"         > Last Update HR</xsl:when>
<xsl:when test="@variable = 'ActualUsage'"          > Actual Usage</xsl:when>
<xsl:when test="@variable = 'InclusionMode'"        > Inclusion Mode</xsl:when>
<xsl:when test="@variable = 'ProcessNotifications'" > Process Notifications</xsl:when>
<xsl:when test="@variable = 'Version'"              > Version</xsl:when>
<xsl:when test="@variable = 'StartTime'"            > Start Time</xsl:when>
<xsl:when test="@variable = 'Children'"             > Children</xsl:when>
<xsl:when test="@variable = 'CommFailureTime'"      > Comm Failure Time</xsl:when>
<xsl:when test="@variable = 'ConsecutivePollFails'" > Consecutive Poll Fails</xsl:when>
<xsl:when test="@variable = 'InternalAlerts'"       > Internal Alerts</xsl:when>
<xsl:when test="@variable = 'UserProfileAssociation'" > User Profile Association</xsl:when>
<xsl:when test="@variable = 'Message'"              > Message</xsl:when>
<xsl:when test="@variable = 'HMode'"                > House Mode</xsl:when>
<xsl:when test="@variable = 'Unit'"                 > Unit</xsl:when>
<xsl:when test="../../@id = '1'"                    ><xsl:value-of select="concat(' ', @variable)"/></xsl:when>
<xsl:otherwise                                      > FIXME <xsl:value-of select="@variable"/></xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:variable name="ItemFormat">
<xsl:choose>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSensor1' and @variable = 'CurrentTemperature'"           > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Cool' and @variable = 'CurrentSetpoint'"       > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-org:serviceId:TemperatureSetpoint1_Heat' and @variable = 'CurrentSetpoint'"       > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'FeelsLike'"                  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'HeatIndex'"                  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'DewPoint'"                   > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'WindChill'"                  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.0.LowTemperature'"  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.0.HighTemperature'" > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.1.LowTemperature'"  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.1.HighTemperature'" > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.2.LowTemperature'"  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.2.HighTemperature'" > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.3.LowTemperature'"  > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:upnp-micasaverde-com:serviceId:Weather1' and @variable = 'Forecast.3.HighTemperature'" > [%.1f F]</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1' and @variable = 'Watts'"                    > [%.1f W]</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1' and @variable = 'KWH'"                      > [%.1f kWh]</xsl:when>
<xsl:when test="@service = 'urn:micasaverde-com:serviceId:EnergyMetering1' and @variable = 'UserSuppliedWattage'"      > [%.1f W]</xsl:when>
<xsl:when test="normalize-space($ItemType) = 'Integer'"                                                                > [%d]</xsl:when>
<xsl:when test="normalize-space($ItemType) = 'Number'"                                                                 > [%.4f]</xsl:when>
<xsl:when test="normalize-space($ItemType) = 'DateTime'"                                                               > [%1$ta, %1$tm/%1$te %1$tR]</xsl:when>
<xsl:when test="normalize-space($ItemType) = 'Contact'"                                                                > [MAP(en.map):%s]</xsl:when>
<xsl:when test="normalize-space($ItemType) = 'Switch'"                                                                 ></xsl:when>
<xsl:when test="normalize-space($ItemType) = 'Dimmer'"                                                                 > [%d %%]</xsl:when>
<xsl:otherwise                                                                                                         > [%s]</xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:variable name="ItemIcon">
<xsl:choose>
<xsl:when test="normalize-space($ItemType) = 'DateTime'">&lt;calendar> </xsl:when>
<xsl:when test="normalize-space($ItemType) = 'Contact'" >&lt;contact> </xsl:when>
</xsl:choose>
</xsl:variable>

<xsl:variable name="ItemGroups">
<xsl:choose>
<xsl:when test="../../@room != '0'"><xsl:value-of select="concat('(GDevices,GRoom', ../../@room, ') ')"/></xsl:when>
<xsl:otherwise>(GDevices) </xsl:otherwise>
</xsl:choose>
</xsl:variable>

<xsl:if test="@variable != ''
              and @service != 'urn:micasaverde-com:serviceId:ZWaveDevice1'
              and @service != 'urn:schemas-upnp-org:service:RenderingControl:1'
              and @service != 'urn:schemas-micasaverde-com:device:avmisc:1'
              and @service != 'urn:micasaverde-com:serviceId:AlarmPartition1'
              and @service != 'urn:demo-paradox-com:serviceId:AlarmPartition1'">
<xsl:choose>
<xsl:when test="$ItemType = 'Integer'">Number   </xsl:when>
<xsl:otherwise><xsl:value-of select="substring(concat($ItemType, '         '), 1, 9)"/></xsl:otherwise>
</xsl:choose>
<xsl:value-of select="$ItemName"/> &quot;<xsl:value-of select="$ItemDescription"/>
<xsl:value-of select="$ItemFormat"/>&quot; <xsl:value-of select="$ItemIcon"/><xsl:value-of select="$ItemGroups"/>{mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,device:<xsl:value-of select="../../@id"/>/service/<xsl:value-of select="$ServiceAlias"/>/<xsl:value-of select="@variable"/>"}
<xsl:if test="@variable = 'sl_UserCode' and @service = 'urn:micasaverde-com:serviceId:DoorLock1'">Number   <xsl:value-of select="$ItemName"/>_userid &quot;<xsl:value-of select="$ItemDescription"/> (ID) [%d]&quot; <xsl:value-of select="$ItemGroups"/>{mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,device:<xsl:value-of select="../../@id"/>/service/<xsl:value-of select="$ServiceAlias"/>/<xsl:value-of select="@variable"/>,in:REGEX(UserID=\&quot;(.+)\&quot; UserName=\&quot;.*\&quot;)"}
String   <xsl:value-of select="$ItemName"/>_username &quot;<xsl:value-of select="$ItemDescription"/> (Name) [%s]&quot; <xsl:value-of select="$ItemGroups"/>{mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,device:<xsl:value-of select="../../@id"/>/service/<xsl:value-of select="$ServiceAlias"/>/<xsl:value-of select="@variable"/>,in:REGEX(UserID=\&quot;.+\&quot; UserName=\&quot;(.*)\&quot;)"}
</xsl:if>
</xsl:if>
</xsl:template>

<xsl:template match="scenes">
<xsl:for-each select="scene">
<xsl:apply-templates select="."/>
</xsl:for-each>
</xsl:template>

<xsl:template match="scene">
<xsl:variable name="SceneName">
<xsl:value-of select="translate(normalize-space(@name), $BAD_CHARS, '')"/>
</xsl:variable>

<xsl:variable name="SceneNameFixed">
<xsl:value-of select="$SceneName"/>
<xsl:if test="count(../scene[translate(normalize-space(@name), $BAD_CHARS, '') = $SceneName]) > 1">
<xsl:value-of select="@id"/>
</xsl:if>
</xsl:variable>

<xsl:variable name="SceneGroups">
<xsl:choose>
<xsl:when test="@room != '0'">(GScenes,GRoom<xsl:value-of select="@room"/>)</xsl:when>
<xsl:otherwise>(GScenes)</xsl:otherwise>
</xsl:choose>
</xsl:variable>
/* Scene - <xsl:value-of select="@name"/> */
String   Scene<xsl:value-of select="$SceneNameFixed"/> "<xsl:value-of select="@name"/> Scene" &lt;sofa> <xsl:value-of select="$SceneGroups"/> {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,scene:<xsl:value-of select="@id"/>/status", autoupdate="false"}
Contact  Scene<xsl:value-of select="$SceneNameFixed"/>Active "Active [MAP(en.map):%s]" &lt;sofa> <xsl:value-of select="$SceneGroups"/> {mios="unit:<xsl:value-of select="$MIOS_UNIT"/>,scene:<xsl:value-of select="@id"/>/active"}
</xsl:template>
</xsl:stylesheet>
