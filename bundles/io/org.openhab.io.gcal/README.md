# Google Calendar Scheduler

The Google Calendar Scheduler allows you to control items in openHAB at scheduled times in the future.  It will send commands to items or update the state of items when defined on one of your Google Calendars.  

An additional persistence service, the Google Calendar Presence Simulator, writes item state changes as events on the calendar that will occur again some number of days in the future, to simulate your activity at home (such as turning lights on and off) when on vacation.

## Table of Contents

<!-- MarkdownTOC -->

- [Obtaining Credentials](#obtaining-credentials)
- [Service Configuration](#service-configuration)
- [Calendar Event Configuration](#calendar-event-configuration)
- [Presence Simulation](#presence-simulation)
  - [Solving Google Calendar Presence Simulation errors](#solving-google-calendar-presence-simulation-errors)

<!-- /MarkdownTOC -->

## Obtaining Credentials

Before you can integrate this service with your Google Calendar, you must have a Google API Console project.

* Login to your [Google API Manager](https://console.developers.google.com).
* From the project drop-down, select an existing project or create a new one by selecting **Create project**.
* In the sidebar under "API Manager", select Credentials, then select the OAuth consent screen tab.
* Choose an **Email Address**, specify a **Product Name**, and press Save.
* In the Credentials tab, select the Create credentials drop-down list, and choose **OAuth client ID**.
* Under **Application type**, select **Other**. 
* Put **Name** and press the **Create** button.
* Copy **client id** and **client secret**

## Service Configuration

This service can be configured in the file `services/gcal.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| client_id |        |   Yes    | Copied from the "Client ID" field on the Credentials page as described above in [Obtaining Credentials](#obtaining-credentials) |
| client_secret |    |   Yes    | Copied from the "Client secret" field on the Credentials page as described above in [Obtaining Credentials](#obtaining-credentials) |
| calendar_name |    |   Yes    | This is the name you gave to your Google Calendar, or the word 'primary' if you want to use your default Google calendar (not recommended).  The service will download calendar events from this calendar. |
| filter   |         |   No     | The filter criteria by which calendar events are searched. The Google Calendar API will do a text search to find calendar events that match the supplied terms. All calendar event fields are searched, except for extended properties. |
| refresh  | 900000  |   No     | the frequency (in milliseconds) with which the Google calendar will be checked for calendar events (900000 milliseconds is 15 minutes).  If you change this value, you must restart the service. |

After the first start, you need to authorize openHAB to allow use your calendar. Follow the instructions that appear in the openHAB log.  At the `openhab>` prompt, enter `log:tail`, or at the shell prompt enter `tail -f /path/to/your/openhab.log`.

```
[INFO ] [g.internal.GCalEventDownloader] -################################################################################################
[INFO ] [g.internal.GCalEventDownloader] - # Google-Integration: U S E R   I N T E R A C T I O N   R E Q U I R E D !!
[INFO ] [g.internal.GCalEventDownloader] - # 1. Open URL 'https://www.google.com/device'
[INFO ] [g.internal.GCalEventDownloader] - # 2. Type provided code ZPWT-UVXXS 
[INFO ] [g.internal.GCalEventDownloader] - # 3. Grant openHAB access to your Google calendar
[INFO ] [g.internal.GCalEventDownloader] - # 4. openHAB will automatically detect the permissions and complete the authentication process
[INFO ] [g.internal.GCalEventDownloader] - # NOTE: You will only have 1800 mins before openHAB gives up waiting for the access!!!
[INFO ] [g.internal.GCalEventDownloader] -################################################################################################
```

> If you later change your `client_id` and `client_secret` in the configuration, you will have to locate and delete the file `gcal_oauth2_token`, and stop and restart the service.  This is because the Google Calendar Scheduler does not detect that the OAuth token is no longer valid.  On openHAB 2.0.0 installed via `apt-get`, this file is located in the directory `/var/lib/openhab2/gcal`.

## Calendar Event Configuration

When creating or modifying an event on the calendar, its title can be anything, and its event description must contain the instructions in the following format:

```
start {
  send|update <item> <state>
}
end {
  send|update <item> <state>
}
```

or just

```
send|update <item> <state>
```

The commands in the `start` section will be executed at the event start time and the `end` section at the event end time. If these sections are not present, the commands will be executed at the event start time.

As a result, your lines in a calendar event might look like this:

```
start {
  send Light_Garden ON
  send Pump_Garden ON
}
end {
  send Light_Garden OFF
  send Pump_Garden OFF
}
```

or just:

```
send Light_Garden ON
send Pump_Garden ON
```

If the event description is entirely blank, then the event's start and end times are used to exclude that time period from processing.

> :warning: The calendar must not contain any events that do not conform to the above description, including public holidays, birthdays, any all-day events at all, personal calendar entries, etc.  It is recommended that you create a Google Calendar that is dedicated to the use of the Google Calendar Scheduler service.  Alternatively, careful use of the `filter` configuration parameter can be used to select only a subset of matching events on the calendar.

## Presence Simulation

The Google Calendar Presence Simulator is an openHAB Persistence service can be used to realize a simple but effective presence simulation feature (thanks Ralf for providing the concept). Every single change of an item that belongs to a certain group is posted as new calendar entry in the future. By default each entry is posted with an offset of 14 days (If you'd like to change the offset please change the parameter `offset` in your `services/gcal-persistence.cfg` file). Each calendar entry looks like the following:

* title: `[PresenceSimulation] <itemname>`
* content: `send <itemname> <value>`

To make use of Presence Simulation you have to walk through these configuration steps:

* configure the gcal-persistence bundle by adding the appropriate configuration. You must add only calendar_name. If you want to use your primary calendar just use the keyword `primary`. All other credentials are shared from Google Calendar Scheduler configuration.
* make sure your items file contains items that belong to the group `G_PresenceSimulation` - if you would like to change the group name, change it in `persistence/gcal.persist`.
* make sure one of your items files contains an item called `PresenceSimulation`.  If you would like to change the group name please change the parameter `executescript` in your `services/gcal-persistence.cfg` file.
* make sure the referenced Google Calendar is writeable by the given user at google.com.

Note: you also need to configure the Google Calendar Scheduler to be able to read the entries from the calendar and act on them.

To activate the Presence Simulation simply set `PresenceSimulation` to `ON` and the already downloaded events are being executed. Items in your smart home will then behave like they did 14 days earlier.

A sample `persist/gcal.persist` file looks like this:

```
Strategies {
	default = everyChange
}

Items {
	G_PresenceSimulation* : strategy = everyChange
}
```

### Solving Google Calendar Presence Simulation errors

To solve any issues with any service bundle, increase the logging. At the `openhab>` prompt you can enter:

```
log:set TRACE org.openhab.persistence.gcal
log:set TRACE org.openhab.io.gcal
```

You can later return them to `DEFAULT` or `INFO` level.

* "GCal PresenceSimulation Service isn't initialized properly! No entries will be uploaded to your Google Calendar"

    The persistence configuration is not correct; check that the `client_id` and `client_secret` are correct in the gcal configuration.

* "creating a new calendar entry throws an exception: Forbidden"

    This can have several causes:
    
    * The `client_id` or `client_secret` might not be correct.

    * The calendar name is not correct.

    * If your not using your own calendar, make sure the sharing settings are correct and the user has sufficient rights to create calendar entries.
