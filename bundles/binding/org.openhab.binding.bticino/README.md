# Bticino Binding

The openHAB Bticino binding allows openHAB to connect to Bticino My Home Automation installations by OpenWebNet protocol.

For example you can switching lights on and off, activating your roller shutters etc.

## Prerequisites

To access your Bticino My Home bus you need an IP gateway (like e.g. the [MH200N](http://www.homesystems-legrandgroup.com/BtHomeSystems/productDetail.action?lang=EN&productId=016), [F453](http://www.homesystems-legrandgroup.com/BtHomeSystems/productDetail.action?productId=027), [F454](http://www.homesystems-legrandgroup.com/BtHomeSystems/productDetail.action?productId=006)).

## Binding Configuration

This binding can be configured in the file `services/bticino.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| webserver.host |   |   Yes    | OpenWebNet gateway IP address / Hostname |
| webserver.port | 20000 | No   | OpenWebNet gateway port |
| webserver.passwd | 12345 | No   | OpenWebNet gateway password |
| webserver.rescan_secs | 120 | No | OpenWebNet bus status rescan interval |
| webserver.heating_zones | 0 | No | OpenWebNet heating zones |
| webserver.shutter_run_msecs | 0 | No | OpenWebNet Runtime of Shutter to calculate Position Feedback |

A sample configuration could look like:

```
webserver.host=192.168.1.35
webserver.rescan_secs=3600
```

##  Item Configuration

The syntax for the Bticino binding configuration string is explained here:

```
bticino="if=webserver;who=<who>;what=<what>;where=<where>"
```

where `<who>` is from the following table:

| Code  | Description |
|-------|-------------|
|  0    | Scenarios |
|  **1**    | Lightning |
|  **2**    | Automation |
|  3    | Load control |
|  **4**    | Temperature Control |
|  5    | Alarm |
|  6    | VDES |
| 13    | Gateway Management |
| **15**    | CEN commands |
| 16/22 | Sound diffusion |
| 17    | MH200N scenarios |
| 18    | Energy management |
| 25    | CEN plus / scenarios plus / dry contacts |
| 1001  | Automation diagnostic |
| 1004  | Thermoregulation diagnostic |
| 1013  | Device diagnostic |


At the moment only 2 (Automation), 1 (Lightning), 15 (Basic & Evolved CEN) and part of the 4 (Temperature Control) are supported.

For more details refer to the official document [OpenWebNet Introduction](http://www.myopen-legrandgroup.com/resources/own_protocol/m/own_documents/16.aspx).

## Examples

items/yourbticino.items

```
Group Entrance
Group Corridor
Group Living                                                                                                                                                                                 
Group Plugs
Group RollerUpShutters
Group Heating
Group HeatingActor

// Sceneries
Switch Movie_Scenery "Movie scenery"

// Plugs
Switch Plug_AV_Amplifier "Audio Video amplifier" (Living, Plugs) <socket> {bticino="if=webserver;who=1;what=0;where=1"}
Switch Plug_Subwoofer "Subwoofer" (Living, Plugs) <socket> {bticino="if=webserver;who=1;what=0;where=2"}

// Lights
Switch Spotlights_TV "TV spotlights" (Living, Lights) {bticino="if=webserver;who=1;what=0;where=35"}
Switch Corridor_Warning "Corridor warning light" (Corridor, Lights) {bticino="if=webserver;who=1;what=0;where=36"}
// CEN example
Switch Doorbell_Light "Doorbell courtesy light" (Entrance, Lights) {bticino="if=webserver;who=15;what=01;where=98"}

// Rollershutters 
Rollershutter RollUpShutter_1 "Roller-up shutter 1" (Living, RollerUpShutters) {bticino="if=webserver;who=2;what=0;where=46"}
Rollershutter RollUpShutter_2 "Roller-up shutter 2" (Living, RollerUpShutters) {bticino="if=webserver;who=2;what=0;where=47"}

//Heating
Number ActualTemp_1 "Actual Temperature 1 [%.1f °C]" <temperature> (Heating) {bticino="if=default;who=4;what=0;where=1"}
Number ControlTemp_1 "Control Temperature 1 [%.1f °C]" <temperature> (Heating) {bticino="if=default;who=4;what=12;where=1"}
Number OffsetTemp_1 "Offset Temperature 1 [MAP(yourbticino_offset.map):%s]" <heating_offset> (Heating) {bticino="if=default;who=4;what=13;where=1"}
Number SetTemp_1 "Set Temperature [%.1f °C]" <heating_set> (Heating) {bticino="if=default;who=4;what=14;where=1"}

Number HCtrlMode_1 "Control Mode [MAP(yourbticino.map):%s]" <settings> (Heating) {bticino="if=default;who=4;what=100;where=1"}
Number HOpMode_1 "Operation Mode [MAP(yourbticino.map):%s]" <heating_cooling> (Heating) {bticino="if=default;who=4;what=101;where=1"}

Contact HValve_1 "Actor 1 [MAP(yourbticino.map):%s]" <undefloor_heating> (HeatingActor) {bticino="if=default;who=4;what=20;where=1"}

Contact HPumpe "Pump [MAP(yourbticino.map):%s]" <pump> (HeatingActor) {bticino="if=default;who=4;what=20;where=0"}
Number MHCtrlMode "Main Unit Control Mode [MAP(yourbticino.map):%s]" <heating> (Heating) {bticino="if=default;who=4;what=100;where=0"}
Number HMCtrlRemote "Main Unit Remote [MAP(yourbticino.map):%s]" <heating> (Heating) {bticino="if=default;who=4;what=102;where=0"}
String HMCtrlStatus "Main Unit Status [%s]" <heating> (Heating) {bticino="if=default;who=4;what=103;where=0"}
String HMCtrlFailure "Main Unit Failure [%s]" <heating> (Heating) {bticino="if=default;who=4;what=104;where=0"}
```

transform/yourbticino.map

```
CLOSED=Heating OFF
OPEN=Heating ON

1=HEATING
2=COLLING

20=Remote Control disabled
21=Remote Control enabled

102=FREEZE PROTECTION
103=OFF
110=MANUAL
111=AUTOMATIK

202=FREEZE PROTECTION
203=OFF
210=MANUAL
211=AUTOMATIK

302=FREEZE PROTECTION
303=OFF
310=MANUAL
311=AUTOMATIK

1101=Program Winter 1
1102=Program Winter 2
1103=Program Winter 3

1201=Scenario Winter 1
1202=Scenario Winter 2
..
1216=Scenario Winter 16

2101=Program Summer 1
2102=Program Summer 2
2103=Program Summer 3


2201=Scenario Winter 1
2202=Scenario Winter 2
..
2216=Scenario Winter 16
```

transform/yourbticino_offset.map

```
NULL=----
0=+0 °C
1=+1 °C
2=+2 °C
3=+3 °C
4=-OFF-
5=FREEZE
-1=-1 °C
-2=-2 °C
-3=-3 °C
```

sitemaps/yourbticino.sitemap

```
sitemap yourbticino label="Main panel" {

     Frame label="Rooms" {
            Group item=Entrance label="Entrance"                                                                                                                                                   
            Group item=Living label="Living room"
     }

     Frame label="Sceneries" {                                                              
            Switch item=Movie_Scenery mappings=[OFF="Turn OFF",ON="Turn ON"]                                                                                                                                                                         
     }  
     
     Frame label="Heating Main Control Unit" {
            Selection item=HMCtrlMode label="Control Mode Main Unit [%.1f]" mappings=[103="OFF", 102="FREEZE PROTECTION", 1101="Program Winter 1", 1201="Scenario Winter 1"]
            Text item=HMCtrlRemote 
            Text item=HMCtrlStatus
            Text item=HMCtrlFailure
            Default item=HPumpe 
     }            
     Frame label="Heating Room 1" {
            Text item=ActualTemp_1
            Text item=OffsetTemp_1 
            Setpoint item=SetTemp_1 label="Set Point Temperature [%.1f °C]" step=0.5 minValue=16 maxValue=25
            Text item=ControlTemp_1
            Selection item=HCtrlMode_1 label="Control Mode [%.1f]" mappings=[103="OFF", 110="MANUAL", 111="AUTOMATIC", 102="FREEZE PROTECTION"]
            Text item=HOpMode_1
            Text item=HValve_1
     }
}
```

rules/yourbticino.rules

```
// Movie scenery management
rule "Movie scenery"
when
    Item Movie_Scenery received command
then
    if (receivedCommand == ON) {

        sendCommand(Plug_AV_Amplifier, ON)  
        sendCommand(Plug_Subwoofer, ON)
        sendCommand(Spotlights_TV, ON)
        sendCommand(RollUpShutter_1, DOWN)
        sendCommand(RollUpShutter_2, DOWN)

    } else if (receivedCommand == OFF) {
    
        sendCommand(Plug_Subwoofer, OFF)
        sendCommand(Plug_AV_Amplifier, OFF) 
        sendCommand(Spotlights_TV, ON)
        sendCommand(RollUpShutter_1, UP)
        sendCommand(RollUpShutter_2, UP)

    }   
end

// Doorbell ringer light
rule "Doorbell ringer"
when
    Item Doorbell_Light received update
then
    if (Doorbell_Light.state == ON) {

            var Number idx = 0
            
            // Number of rings
            while (idx < 5) {

                    sendCommand(Corridor_Warning, ON)
                    Thread::sleep(1500)
                    sendCommand(Corridor_Warning, OFF)
                    Thread::sleep(1500)
                    idx = idx + 1

            }

    }
end
```
