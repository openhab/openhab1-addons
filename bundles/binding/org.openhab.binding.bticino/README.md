### Introduction

The openHAB Bticino binding allows to connect to Bticino My Home Automation installations by OpenWebNet protocol.
For example you can switching lights on and off, activating your roller shutters etc.

To access your Bticino My Home bus you need an IP gateway (like e.g. the [MH200N](http://www.homesystems-legrandgroup.com/BtHomeSystems/productDetail.action?lang=EN&productId=016), [F453](http://www.homesystems-legrandgroup.com/BtHomeSystems/productDetail.action?productId=027), [F454](http://www.homesystems-legrandgroup.com/BtHomeSystems/productDetail.action?productId=006)).

For installation of the binding, please see Wiki page [[Bindings]].

## Binding Configuration in openhab.cfg

    # OpenWebNet gateway IP address / Hostname
    bticino:webserver.host=

    # OpenWebNet gateway Port (optional, defaults to 20000)
    bticino:webserver.port=

    # OpenWebNet bus status rescan interval (optional, defaults to 120 seconds)
    bticino:webserver.rescan_secs=

A sample configuration could look like:

    bticino:webserver.host=192.168.1.35
    bticino:webserver.rescan_secs=3600

## Bind Items to Bticino

### Description
In order to bind an item to a Bticino device you need to provide configuration settings. The easiest way to do so is to add  binding information in your 'item file' (in the folder configurations/items). The syntax for the Bticino binding configuration string is explained here:

    bticino="if=webserver;who=<who>;what=<what>;where=<where>"

    Table of WHO

    Code  Description
     0    Scenarios
     1    Lightning
     2    Automation
     3    Load control
     4    Temperature Control
     5    Alarm
     6    VDES
    13    Gateway Management
    15    CEN commands
    16/22 Sound diffusion
    17    MH200N scenarios
    18    Energy management
    25    CEN plus / scenarios plus / dry contacts
    1001  Automation diagnostic
    1004  Thermoregulation diagnostic
    1013  Device diagnostic

For more details refer to the official document [OpenWebNet Introduction](http://www.myopen-legrandgroup.com/resources/own_protocol/m/own_documents/16.aspx).

At the moment only 2 (Automation), 1 (Lightning), 15 (Basic & Evolved CEN) and part of the 4 (Temperature Control) are supported.

### Items example

    Group Entrance
    Group Corridor
    Group Living                                                                                                                                                                                 
    Group Plugs
    Group RollerUpShutters

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

## Sitemap example

    sitemap demo label="Main panel" {

    	 Frame label="Rooms" {
                Group item=Entrance label="Entrance"                                                                                                                                                   
                Group item=Living label="Living room"
         }

    	 Frame label="Sceneries" {                                                              
                Switch item=Movie_Scenery mappings=[OFF="Turn OFF",ON="Turn ON"]                                                                                                                                                                         
         }  

    }

## Rule example

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

    // Doorbel ringer light
    rule "Doorbel ringer"
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