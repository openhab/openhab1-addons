Documentation of the [VDR](http://www.tvdr.de) Binding Bundle

## Introduction

If you have any suggestions or questions don't hesitate to contact me ([iwow](http://groups.google.com/groups/profile?enc_user=2fum3R0AAACkAo_xkzjYxXMLRwdKLvZ72A6NcFQ3yZH-XCKBlyRD_Q)) or use the [openHAB google group](http://groups.google.com/group/openhab).

For installation of the binding, please see Wiki page [[Bindings]].

## Generic Item Binding Configuration

In order to bind an item to a VDR, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items). The syntax for the VDR binding configuration string is explained here:

    vdr="<VDR-ID>:<command>[,<VDR-ID>:<command>][,..]"

The parts in brackets `[]` signify an optional information.
 
Command must be one of the following:

- `message`: show message on OSD; must be bound to a String item
- `powerOff`: switch VDR off (same as hit power on remote control); must be bound to a Switch item
- `recording`: recording state; must be bound to a Switch item
- `channel`: channel up / down; can be bound to a Switch item(ON=increase, OFF=decrease) or a number item
- `volume`: volume up / down; can be bound to a Switch item (ON=increase, OFF=decrease) or a number item (value between 0 and 255)

The VDR-Id corresponds to the configuration in openhab.cfg where one has to configure the VDRs. Which looks like this 

    vdr:<id>.host=[host]
    vdr:<id>.port=[port]

Here are some examples for valid binding configuration strings:

For an SwitchItem:

    vdr="LivingRoom:powerOff"
    vdr="LivingRoom:recording"

For a StringItem:

    vdr="LivingRoom:message"

As a result, your lines in the items file might look like the following:

    Switch VDR_LivingroomPower          "VDR (livingroom) Power"          (VDR) {vdr="LivingRoom:powerOff",wol="192.168.1.2#F4:6D:15:32:F3:F7" }
    String VDR_LivingroomOSDMessage     "VDR (livingroom) OSD message"           {vdr="LivingRoom:message"}
    Switch VDR_LivingroomChannelUpDown	"VDR Livingroom Channel Up/Down"  (VDR)  {vdr="LivingRoom:channel"}
    Number VDR_LivingroomChannel        "VDR Livingroom Channel"          (VDR)  {vdr="LivingRoom:channel"}
    Switch VDR_LivingroomVolumeUpDown	"VDR Livingroom Volume Up/Down"   (VDR)  {vdr="LivingRoom:volume"}
    Number VDR_LivingroomVolume         "VDR Livingroom Volume"           (VDR)  {vdr="LivingRoom:volume"}
    Switch VDR_LivingroomRecording      "VDR (livingroom) Recording"      (VDR)  {vdr="LivingRoom:recording"}

## Relationship to other openHAB modules

Together with other bindings (e.g. NetworkHealthBinding, WoLBinding) and the openHAB rule engine you can realize different use-cases.

### Switch VDR PC on if TV switch on:

(requires TV with LAN connection)

First you need two items with NetworkHealthBinding

    Switch TV_PowerState                "TV Power State"     (VDR)   {nh="192.168.1.2" }
    Switch VDR_LIVINGROOM_POWER_STATUS  "VDR Power Status"   (VDR)   {nh="192.168.1.3" } 

A rule for the openHAB rule engine (not drools) can look like this:

    rule CheckTVStateOn
    when
    	Item TV_PowerState changed to ON
    then
    	if(VDR_LIVINGROOM_POWER_STATUS?.state==OFF) {
        	sendCommand(VDR_LivingroomPower, ON);
        }    
    end
 
### Switch VDR PC off if TV switch off:

Same requirements and prerequisites as VDR PC on use-case above.

A rule for the openHAB rule engine (not drools) can look like this:

    rule CheckTVStateOff
    when
    	Item TV_PowerState changed to OFF
    then
    	if(VDR_LIVINGROOM_POWER_STATUS?.state==ON) {
    		if (VDR_LivingroomRecording?.state==OFF) {
        		sendCommand(VDR_LivingroomPower, OFF);
        	}
        }    
    end
    