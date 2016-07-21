## Introduction

This is a OpenHab binding for Russound audio controllers

The binding supports the following functionality.

Turn audio zone on/off
Adjust volume for audio zone
Adjust source for audio zone

Installation

Copy the binding jar (org.openhab.omnilink*.jar) to the addons directory

add the following to your openhab.cfg

################################# Russound ############################################
# Enter the host IP of the computer which has exposed the RNET serial communication via the 
# python script tcp_serial_redirect.py
# eg. python ./tcp_serial_redirect.py -b 19200 /dev/ttyUSB0 
# this script effectively makes the serial connection (via /dev/ttyUSB0 port on my linux)
# available on the internal network, uses port 7777 by default.
# This allows one to have the serial connection to the Russound devices on a different computer
# than the Openhab executable

# Enter the port(optional) (7777) and host ip 
#
russound:host=192.168.1.30
#russound:port=7777


## Items

if you want to manually add a item, the following types are supported:

format is {russound:"controller/zone/command"}

The following are valid commands
power
volume
source

Note: There is also a so-called magic command to monitor if the binding
      has a current connection to the russound devices.

Contact	Russound_Connection	"Audio Connected: [%s]" {russound="connection"}

Important:  Both the Item name and the binding have to remain the same.


## Item Examples

Switch RussoundAudioGreatRoom_power "Gt Room Power [%s]" 	(gAudio_GreatRoom,AudioZonesMain,GroupSwitch)	{russound="0/5/power"}
Dimmer RussoundAudioGreatRoom_volume	"Volume [%d%%]"	(gAudio_GreatRoom,AudioZonesMain)	{russound="0/5/volume"}
Number RussoundAudio_GreatRoom_source "Source Id [%.0f]"	(gAudio_GreatRoom)	{russound="0/5/source"}

## Example Rules

Here are a sample rules I have used for the binding

import org.openhab.core.library.types.*
import org.openhab.core.persistence.*
import org.openhab.model.script.actions.*

var Timer timer

    rule "Turn off patio speakers if on for > 4 hours"
    when
    	Item RussoundAudio_RoofTop_power changed
    then
        if(RussoundAudio_RoofTop_power.state==ON) {
        	logDebug("Sun.rules","Roof top speakers are ON")
            timer = createTimer(now.plusHours(4)) [|
                // do something!
       			logDebug("Sun.rules","Attempting to turn off roof top speakers")
       			pushNotification("OpenHab", "Roof top speakers turned off, were on for 4 hours")  
       			sendCommand(RussoundAudio_RoofTop_power,OFF)        
            ]
        } else {
        	logDebug("Sun.rules","Roof top state: " + RussoundAudio_RoofTop_power.state)
            if(timer!=null) {
                timer.cancel
                timer = null
            }
        }
    end
    
    rule "Turn off patio speakers at 11pm"
    when
    	Time cron "0 0 23 * * ?"
    then
    	if(RussoundAudio_RoofTop_power.state==ON) {
			logDebug("Sun.rules","Attempting to turn off roof top speakers")
			pushNotification("OpenHab", "Roof top speakers turned off at 11pm.")  
			sendCommand(RussoundAudio_RoofTop_power,OFF)
		}        
    end
    
    rule "Alert when connection lost to Russounds"
    when
    	Item Russound_Connection changed from CLOSED to OPEN
    then
			pushNotification("OpenHab", "Connection to Russound Lost.")  
    end
    

