## Introduction

This is a Freeswitch binding for OpenHab.  

It connects to a freeswitch instance and can report on current active calls as well as show unread voicemails and if a MWI is on.

You can also send any command to freeswitch , see [Freeswitch Commands](http://wiki.freeswitch.org/wiki/Mod_commands) for more info.

## Installation 

Copy the binding (org.openhab.binding.freeswitch*.jar) to your addons directory.

Make sure you have the freeswitch ESL module listing on a public port, your event_socket.conf.xml in freeswitch should look something like:

```
<configuration name="event_socket.conf" description="Socket Client">
  <settings>
    <param name="nat-map" value="false"/>
    <param name="listen-ip" value="0.0.0.0"/>
    <param name="listen-port" value="8021"/>
    <param name="password" value="ClueCon"/>
    <!--<param name="apply-inbound-acl" value="lan"/>-->
  </settings>
</configuration>
```

In your openhab.cfg put the following entry with your freeswitch ESL config,

```
########################## Freeswitch Action configuration #################################
#
freeswitch:host=freeswitch.yourdomain.com
freeswitch:port=8021
freeswitch:password=ClueCon
```

## Items

this is a sample item entry for non filtered calls, any inbound call will be considered active, this is sufficient for most uses.

```
Switch Incoming_Call "Home Phone" (Phone) {freeswitch="active"}
Call Active_Call     "Connected [to %1$s from %2$s]" (Phone) {freeswitch="active"}
String Active_Call_ID "Caller ID [%s]" (Phone) {freeswitch="active"}
```

This item is for a filtered call, only calls with an inbound direction and a destination number of 5555551212 will be matched

```
Switch Incoming_Call "Home Phone" (Phone) {freeswitch="active:Call-Direction:inbound,Caller-Destination-Number:5555551212"}
Call Active_Call     "Connected [to %1$s from %2$s]" (Phone)  {freeswitch="active:Call-Direction:inbound,Caller-Destination-Number:5555551212"}
String Active_Call_ID "Caller ID [%s]" (Phone)  {freeswitch="active:Call-Direction:inbound,Caller-Destination-Number:5555551212"}
```
For messages and message waiting indicator (MWI) the voice mail account is specified

`Number Voice_Messages "Unread Messages [%d]" (Phone) {freeswitch="message_waiting:1000@pbx.mydomain.com"}`

To send api commands to Freeswitch add a simple api item

`String FS_API	"FS API [%s]" (phone)	{freeswitch="api"}`

There are three supported protocol types, "active", "message_waiting" and "api"

* Active types:
  * Filtered
    * calls can be filtered on freeswitch event headers, multiple headers can be used, all must match. 
    * filters format is "Key:Value,Key:Value,...."
    * Ex: `Switch Incoming_Call "Home Phone" (Phone) {freeswitch="active:Call-Direction:inbound,Caller-Destination-Number:5555551212"}`
    * see [Freesiwtch Events](http://wiki.freeswitch.org/wiki/Event_List) for more info on event headers
  * Non Filtered
    * if no filter is given any inbound call will be used
    * for multiple active calls the most recent active call's callerid will be displayed
    Ex: `Incoming_Call "Home Phone" (Phone) {freeswitch="active}`
  * Item Types
    * Switch will be on for an active call, off if no active calls
    * Call, this shows the destination and origination numbers
    * Text, this shows the Caller-ID fields (name : number) if sent by caller (regional specific I would imagine)
* Message_Waiting types:
  * Number, this shows the number of unread voice mails
  * Switch, on if MWI (message indicator) is yes.
* API types
  * String , sending a string to this will send a command to FS, the result will be sent back to this item as a comma seperated list
  * API types are really meant to be used in rules, an example might be:
    * `sendCommand(FS_API,"conference test-conf dial sofia/gateway/myvoipprovider/5555551212 5551212 5551212")`
    * This would tell freeswitch to dial 5555551212 into a conference named "test-conf"
    * the conference info would be returned as a update to the item.
  * see [Freeswitch Commands](http://wiki.freeswitch.org/wiki/Mod_commands) for more info.

message_waiting takes an extra argument which is the mailbox we want to check against.  This is usually the extention@domain 