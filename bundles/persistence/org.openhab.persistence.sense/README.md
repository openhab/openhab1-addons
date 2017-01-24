Documentation of the Sen.Se Persistence Service

## Introduction

This service allows you to feed item data to Sen.Se web site (see http://open.sen.se for more details)

## Features

This persistence service supports only writing information to the Sen.Se web site.
You need a Sen.Se API key and data feed to put data to. Each item being persisted represents a separate feed.

## Installation

For installation of this persistence package please follow the same steps as if you would [install a binding](Bindings).

Additionally, place a persistence file called sense.persist in the `${openhab.home}/configuration/persistence` folder.

## Configuration

### openhab.cfg

This persistence service can be configured in the "Sen.Se Persistence Service" section in `openhab.cfg`. You need to specify your Sen.Se API key as

    sense:apikey=your_api_key

### Prepare open.sen.se for OpenHAB

To connect your OpenHAB installation to open.sen.se you have to [create a _custom device_](http://open.sen.se/devices/add/custom/#navbar) in open.sen.se. Follow the wizard there ("Get started"). Give your device a name that makes sense to you, and where the form says "Device is" choose "Other". Leave "Sending data" as "HTTP Posting" and "Receiving Data" as "HTTP Polling". Add as many feeds as you have items to display, choosing the option "Input: data flows from device to Sen.se" for each feed.

Under http://open.sen.se/devices/ you should see the device that you have just created. If you click on "Profile & Settings" you will see - amongst other things - a list of the input feeds that you created. If you click on an input feed you will see all its data, including a _feed ID_ (currently a 5-digit integer).

Note the feed ID of each input feed that you want to connect to an item in OpenHAB: you will need this information to configure the file `sense.persist`.

### sense.persist

All persistence files (including `sense.persist`) have the same syntax, which is documented in the [generic persistence file documentation](https://github.com/openhab/openhab/wiki/Persistence).

Choose whatever strategies make sense in your application. This is totally independent of open.sen.se.

In the `Items {}` section you will create the connection between OpenHAB and open.sen.se. The syntax of the items section is as follows:
```
Items {
    <itemlist1> [-> "<alias1>"] : [strategy = <strategy1>, <strategy2>, ...]
    <itemlist2> [-> "<alias2>"] : [strategy = <strategyX>, <strategyY>, ...]
    ...
}
```
Possible values for `<itemlist>` can be seen in the [generic persistence file documentation](https://github.com/openhab/openhab/wiki/Persistence), as can values for the `strategy` clauses.

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