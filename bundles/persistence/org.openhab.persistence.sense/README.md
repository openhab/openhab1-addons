# Sen.Se Persistence

This service allows you to feed item data to [Sen.Se web site](http://open.sen.se).

This persistence service supports only writing information, and so features such as `restoreOnStartup` and sitemap Chart widgets cannot be used with this service.

## Prerequisites

You need a Sen.Se API key and data feed to put data to. Each item being persisted represents a separate feed.

To connect openHAB to open.sen.se, you have to [create a _custom device_](http://open.sen.se/devices/add/custom/#navbar) in open.sen.se. Follow the wizard there ("Get started"). Give your device a name that makes sense to you, and where the form says "Device is" choose "Other". Leave "Sending data" as "HTTP Posting" and "Receiving Data" as "HTTP Polling". Add as many feeds as you have items to display, choosing the option "Input: data flows from device to Sen.se" for each feed.

Under [Devices](http://open.sen.se/devices/), you should see the device that you have just created. If you click on "Profile & Settings" you will see, amongst other things, a list of the input feeds that you created. If you click on an input feed you will see all its data, including a _feed ID_ (currently a 5-digit integer).

Note the feed ID of each input feed that you want to connect to an item in openHAB.  You will need this information to configure the file `persistence/sense.persist`.

## Configuration

This service can be configured in the file `services/sense.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| apikey   |         |   Yes    | your Sen.Se API key |

All item- and event-related configuration is done in the file `persistence/sense.persist`.  Choose whatever strategies make sense in your application. This is totally independent of open.sen.se.

In the `Items` section, you will create the connection between openHAB and open.sen.se. The syntax of the items section is as follows:

persistence/sense.persist

```
Items {
    <itemlist1> [-> "<alias1>"] : [strategy = <strategy1>, <strategy2>, ...]
    <itemlist2> [-> "<alias2>"] : [strategy = <strategyX>, <strategyY>, ...]
    ...
}
```

The important values for open.sen.se are the `alias`es. Substitute `<alias1>`, `<alias2>`, etc. with a feed ID from open.sen.se and the corresponding items will be sent to that feed.

The following example shows a very simple `sense.persist` which illustrates this:

```
Strategies {
	everyDay	: "0 0 0 * * ?"
	default		= everyChange, everyDay, restoreOnStartup
}

Items {
	Temperature_Item	-> "xxxxx"	:
	// where "Temperature_Item" is an OpenHAB item
	// and "xxxxx" is an open.sen.se feed ID enclosed in double quotes
	// Values from Temperature_Item are sent to open.sen.se feed "xxxxx"
	Humidity_Item		-> "yyyyy"	:
	// where "Humidity_Item" is an OpenHAB item
	// and "yyyyy" is an open.sen.se feed ID enclosed in double quotes
	// Values from Humidity_Item are sent to open.sen.se feed "yyyyy"
}
```

