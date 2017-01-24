Documentation of the Pulseaudio binding Bundle

## Introduction

For installation of the binding, please see Wiki page [[Bindings]].

## Prerequisites

You need a running pulseaudio server whith module module-cli-protocol-tcp loaded and accessible by the server which runs your openHAB instance.

## Generic Item Binding Configuration

In order to bind an item to a Pulseaudio server, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder configurations/items`). 
The syntax for the Pulseaudio binding configuration string is explained here: 

    pulseaudio="<ServerName>:<PulseaudioItemName>:<command>"
Where:

`<ServerName>` is the name of the pulseaudio server as it is defined in your config.

`<PulseaudioItemName>` is the name of a pulseaudio sink or source as it is defined on the related server (you can find out the names with the pactl command).

`<command>` [optional] is one of:
- EXISTS: queries if the item exists on the pulseaudio server or not
- MUTED: queries if the item is muted or not
- SLAVE_SINKS: queries the names of the members of a combined sink (must be bound to a string item)

Here are some examples of valid binding configuration strings: 

    pulseaudio="Main:combined-music"
    pulseaudio="Main:combined-music:SLAVE_SINKS"
    pulseaudio="Main:combined-music:EXISTS"
    pulseaudio="Main:combined-music:MUTED"

As a result, your lines in the items file might look like the following: 

    Dimmer Sink_FF_Multiroom_Volume	{ pulseaudio="Main:combined-music" }
    String Sink_FF_Multiroom_Slaves { pulseaudio="Main:combined-music:SLAVE_SINKS" }
    Switch Sink_FF_Multiroom_Exists { pulseaudio="Main:combined-music:EXISTS" }
    Switch Sink_FF_Multiroom_Muted { pulseaudio="Main:combined-music:MUTED" }

In order to find out the names of your pulseaudio sinks you can use the pactl command line tool on the server, which is running your pulseaudio instance

    pactl list sinks short
which returns something like this

    9       combined-music  module-combine-sink.c   s16le 2ch 44100Hz       RUNNING
where combined.music is the name of the sink

## Example Use Case

The Pulseaudio Binding can be used to e.g. to mute a sink or to change the volume of one

### Items

    Switch Sink_FF_Multiroom_Muted { pulseaudio="Main:combined-music:MUTED" }

### Rule

    rule "mute pulseaudio sink on incoming call"
    when
    	Item Incoming_Call received update
    then
    	if (Incoming_Call.state==ON) {
    		Sink_FF_Multiroom_Muted.sendCommand(ON)
    	} else {
    		Sink_FF_Multiroom_Muted.sendCommand(OFF)
    	}
    end