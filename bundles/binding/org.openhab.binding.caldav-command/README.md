# CalDAV Command Binding

This binding can be used to connect through the CalDAV Interface to calendars.
First of all you need to add the org.openhab.io.caldav-version.jar to the addons folder.

**Newest**
Newest Version can be downloaded from the build-agent


openhab.cfg
* `caldavio:<calendar-id>:url=`
* `caldavio:<calendar-id>:username=`
* `caldavio:<calendar-id>:password=`
* `caldavio:<calendar-id>:reloadInterval=<minutes>`
* `caldavio:<calendar-id>:preloadTime=<minutes>`
* `caldavio:<calendar-id>:historicLoadTime=<minutes>`
* `caldavio:<calendar-id>:disableCertificateVerification=<true|false>`
* `caldavio:<calendar-id>:charset=<well formed charset name>`
* `caldavio:timeZone=<Timezone>`

**Restrictions**
* The calendar-id must be just upper- and lowercase characters. (e. g. private or work, something like 1 or private-home is not allowed)
* disableCertificateVerification can just be set to true (default is false) if ssl is used.
* timeZone must just be used if the local timezone of the pc is not the correct one. E. g. if you are living in Berlin and your calendar timezone is Berlin and your local pc timezone is Berlin you must not define this setting
* '' for item configurations are optional (eventNr:1 and eventNr:'1' is the same). I prefer to use ''

### Note for openHAB 2

The CalDAV binding is compatible with openHAB 2 just like any other openHAB 1.x binding. There is currently just one bug in the way the configuration file is parsed. Create the configuration file `caldavio.cfg` with the additional prefix `caldavio:`. Do the same for `caldavCommand.cfg` and `caldavCommandPersonal.cfg`.
For more details see the [discussion here](https://github.com/openhab/openhab/issues/4074#issuecomment-202737544). 

***

# CalDAV Command
Binding file: org.openhab.binding.caldav-command-version.jar

Used to execute commands through an event, triggered at the start or the end of an event.
The event summary is free selectable. The event description must fullfill special syntax.
Syntax is `<BEGIN|END>:<Item-Name>:<Command>`.
Each item trigger must be a single line without linebreaks. Every line which starts with "BEGIN" will be executed at the begin of the event. Each line with an "END" will be executed at the end of the event. You can define multiple lines, which must not be ordered. For example:
<pre>BEGIN:Heater_Livingroom:22
BEGIN:Heater_Corridor:22
END:Heater_Livingroom:16
END:Heater_Corridor:16
END:Notification_Dummy:Heizung heruntergefahren</pre>

Additionaly you can define an item to listen to upcoming changes of an item (which will be triggered through an event). Two types are available the command which will be set and the trigger time.
Syntax is `caldavCommand="itemName:<Item-Name to listen to> type:<VALUE|DATE>"`
Furthermore a switch can be defined to disable the automatic execution (through calendar) of an item. 
Syntax is `caldavCommand="itemName:<Item-Name to listen to> type:<DISABLE>"`

openhab.cfg
`caldavCommand:readCalendars=<calendar-id>` (multiple calendars can be seperated by commas)

## Description of type
* VALUE: the value which will send to the command (can be of any type, depends on command in event and accepted commands of item)
* DATE: the time on which the event occurs (item type: DateTime)
* DISABLE: can turn off the automatic execution of the given item (item type: Switch)

## Default item
Since 1.9.0, the CalDAV Command binding also supports a default item which may optionally be added to openhab.cfg:
<pre>caldavCommand:defaultItemOnBegin=&lt;item&gt;</pre>
If the command calendar contains items whose description does not follow the `BEGIN/END:<item>:<value>` pattern AND a default item is specified, then the respective lines are interpreted as `BEGIN:<defaultItemOnBegin>:<line>`

This is really nice to use in combination with a rule such as used for [voice control](https://github.com/openhab/openhab/wiki/Controlling-openHAB-with-your-voice) - set the default item to: `caldavCommand:defaultItemOnBegin=VoiceCommand`

Then you can use the very same logic of your voice commands also in your calendar events.

Depending on your rule implementation, it is possible to use event entries like these:

* "Swtich on light in kitchen"
* "Switch off radio"
* "Close roller shutter on first floor" etc.

***

# CalDAV Personal
Binding file: org.openhab.binding.caldav-personal-version.jar

* Used to detect presence through calendar events.
* Used to show upcoming/active events in openhab.

### openhab.cfg
* `caldavPersonal:usedCalendars=<calendar-id>` (multiple calendars can be seperated by commas)
* `caldavPersonal:homeIdentifiers=<values seperated by commans>` (if one of these identifiers can be found inside the place of the event, this event will not be used for presence)

### items
* `caldavPersonal="calendar:'<calendar-ids, comma separated>' type:'<UPCOMING|ACTIVE|EVENT>' eventNr:'<event-nr, first one is 1>' value:'<NAME|DESCRIPTION|PLACE|START|END|TIME>"'`
* `caldavPersonal="calendar:'<calendar-ids>' type:'PRESENCE'" (type must be Switch)`

## filtering
You've got the option to show just specific events.
* `filter-name:'<regular expression>'`
* `filter-category:'<categories, comma separated>'`  (your caldav event must contain at least ALL the categories you specify here)
* `filter-category-any:'<categories, comma separated>'` (your caldav event must at least contain one of the categories you specify here)

### Example for filtering
* just showing upcoming free days
`caldavPersonal="calendar:'robert,common' type:'EVENT' eventNr:'1' value:'START' filter-name:'Gleittag|Urlaub|Frei'"`
* just showing events for the next garbage pick-up
`caldavPersonal="calendar:'common' type:'EVENT' eventNr:'1' value:'START' filter-category:'Müllabholung'"`
* item config showing the next event in which we are sending a heat event to the bathroom :  
`DateTime bathroom_NextEventDate "bathriil next evt. [%1$tT, %1$td.%1$tm.%1$tY]" <calendar> { caldavPersonal="calendar:chauffagecmd type:UPCOMING eventNr:1 value:'START' filter-category-any:'bathroom,wholefloor'"}`  
==> if you have one event at 8pm with 1 category "bathroom" (setting heater on), and another event at 9pm with category "wholefloor" (setting heaterS on), then this item will match both events. 

## Description of type
* UPCOMING: the next upcoming events, not the active ones
* ACTIVE: events which are currently on (internally used for presence detection)
* EVENT: all events, active as well as upcoming

## Description of value
* NAME: Name of the event (itemtype: String)
* DESCRIPTION: Event content (itemtype: String)
* PLACE: Place of event (itemtype: String)
* START: start time (itemtype: DateTime)
* END: end time (itemtype: DateTime)
* TIME: start/end time (itemtype: String)
* NAMEANDTIME: name and start- to end time (itemtype:String)

# Logging
* `<logger name="org.openhab.binding.caldav_personal" level="TRACE"/>`
* `<logger name="org.openhab.binding.caldav_command" level="TRACE"/>`
* `<logger name="org.openhab.io.caldav" level="TRACE"/>`

# Tested calDAV Servers with examples
As far as i know are these
## ownCloud (my reference implementation)
    caldavio:openhab_tasks:url=http://server.de/owncloud/remote.php/caldav/calendars/openHAB/tasks
    caldavio:openhab_tasks:username=username
    caldavio:openhab_tasks:password=password
    caldavio:openhab_tasks:reloadInterval=10
    caldavio:openhab_tasks:preloadTime=20000
## baikal
    caldavio:kalendername:url=https://server_ip/baikal/cal.php/calendars/username/kalender_id  
    caldavio:kalendername:username=username  
    caldavio:kalendername:password=password  
    caldavio:kalendername:reloadInterval=10  
    caldavio:kalendername:preloadTime=20000
## google (performance issue, because the timestamp of files is not correct)
    caldavio:openhab_tasks:url=https://www.google.com/calendar/dav/email@gmail.com/events
    caldavio:openhab_tasks:username=email@gmail.com
    caldavio:openhab_tasks:password=password
    caldavio:openhab_tasks:reloadInterval=10
    caldavio:openhab_tasks:preloadTime=20000
## (zarafa?)

## Locate URL
This site may help to find the Calendar URL (i.e. Google Shared Calendars):
http://www.ict4g.net/adolfo/notes/2015/07/04/determingurlofcaldav.html

## Google 2-factor authentication
If 2-factor authentication has been enabled, create an application password using https://support.google.com/accounts/answer/185833?hl=en Use this password instead of your account password.

# Persistence
    caldav-persistence:calendarId=history
    caldav-persistence:duration=10
    caldav-persistence:singleEvents=false
Saves the events to the calendar named history with a length of 10 minutes

# Presence Simulation
You can simulate presence with this binding.
To do this you have to
* enable the caldav-command binding
* enbale the caldav-persistence binding
* configure the Items you need for simulation for caldav persistence
* configure the caldav persistence to singleEvents=true and an offset you want. One week or two make sense.
* keep openHAB running for a week or more and the persistent events will occur again in the future regarding the offset you set up

# Known Problems (or limitations)
* If you are using multiple calendars you have to set the thread count for quartz to this calendar amount or higher otherwise some calendars will not be loaded.
* You have to set the preloadInterval to a higher or equal value as the recurring events in the calendar exists.

# Example configuration

There are three calendars defined. One of them is used just for executing commands in openhab (Command-kalender). The others are used to show the upcoming events (Müllkalender, Dienstlicher/privater Kalender).
In every case, the binding org.openhab.io.caldav-<version>.jar is needed. For executing commands the additional binding org.openhab.binding.caldav-command-<version>.jar is needed. For upcoming events or presence simulation the binding org.openhab.binding.caldav-personal-<version>.jar needs to be included.

openhab.cfg

    ################################ CalDav Binding #######################################
    #
    #caldavio:<calendar-id>:url=
    #caldavio:<calendar-id>:username=
    #caldavio:<calendar-id>:password=
    #caldavio:<calendar-id>:reloadInterval=<minutes>
    #caldavio:<calendar-id>:preloadTime=<minutes>
    #caldavio:timeZone=<e. g. Europe/Berlin>

    # Dienstlicher/privater Kalender
    caldavio:dienstlich:url=http://192.168.2.5/owncloud/remote.php/caldav/calendars/user/pers%C3%B6nlich
    caldavio:dienstlich:username=user
    caldavio:dienstlich:password=password
    caldavio:dienstlich:reloadInterval=60
    caldavio:dienstlich:preloadTime=2880
    caldavio:timeZone=Europe/Berlin

    # Müllkalender
    caldavio:muell:url=http://192.168.2.5/owncloud/remote.php/caldav/calendars/user/m%C3%BCll
    caldavio:muell:username=user
    caldavio:muell:password=password
    caldavio:muell:reloadInterval=1440
    caldavio:muell:preloadTime=2880
    caldavio:timeZone=Europe/Berlin

    # Command-kalender``
    caldavio:command:url=http://192.168.2.5/owncloud/remote.php/caldav/calendars/user/command
    caldavio:command:username=user
    caldavio:command:password=password
    caldavio:command:reloadInterval=10
    caldavio:command:preloadTime=1440
    caldavio:timeZone=Europe/Berlin

    # Additionally needed binding: org.openhab.binding.caldav-command-<version>.jar
    # used to execute commands by a triggered event
    # multiple calendars (calerdar-id) can be seperated by commas
    #caldavCommand:readCalendars=<calendar-id>
    caldavCommand:readCalendars=command

    # Additionally needed binding: org.openhab.binding.caldav-personal-<version>.jar
    # used to record and simulate presence and to show upcoming/active events
    # multiple calendars (calerdar-id) can be seperated by commas
    #caldavPersonal:usedCalendars=<calendar-id>
    caldavPersonal:usedCalendars=dienstlich,muell

    # If one of these identifiers can be found inside the place of the event, 
    # this event will not be used for presence
    #caldavPersonal:homeIdentifiers=<values seperated by commas>

The items-File:

    String OfficeCalName0	"Termin jetzt [%s]"		<calendar>	{ caldavPersonal="calendar:dienstlich type:ACTIVE eventNr:1 value:NAME" } //eventNr for concurrent events
    DateTime OfficeCalTime0	"Beginn [%1$tT, %1$td.%1$tm.%1$tY]"			<calendar>	{ caldavPersonal="calendar:dienstlich type:ACTIVE eventNr:1 value:START" } //eventNr for concurrent events
    String OfficeCalName1	"nächster Termin [%s]"	<calendar>	{ caldavPersonal="calendar:dienstlich type:UPCOMING eventNr:1 value:NAME" }
    DateTime OfficeCalTime1	"Beginn [%1$tT, %1$td.%1$tm.%1$tY]"			<calendar>	{ caldavPersonal="calendar:dienstlich type:UPCOMING eventNr:1 value:START" }
    String OfficeCalName2	"übernächster Termin [%s]" <calendar> { caldavPersonal="calendar:dienstlich type:UPCOMING eventNr:2 value:NAME" }
    DateTime OfficeCalTime2	"Beginn [%1$tT, %1$td.%1$tm.%1$tY]" 			<calendar> { caldavPersonal="calendar:dienstlich type:UPCOMING eventNr:2 value:START" }
