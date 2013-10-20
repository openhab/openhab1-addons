# Readme

## Introduction

The open Home Automation Bus (openHAB) project aims at providing a universal integration platform for all things around home automation. It is a pure Java solution, fully based on OSGi. The Equinox OSGi runtime and Jetty as a web server build the core foundation of the runtime.

It is designed to be absolutely vendor-neutral as well as hardware/protocol-agnostic. openHAB brings together different bus systems, hardware devices and interface protocols by dedicated bindings. These bindings send and receive commands and status updates on the openHAB event bus. This concept allows designing user interfaces with a unique look&feel, but with the possibility to operate devices based on a big number of different technologies. Besides the user interfaces, it also brings the power of automation logics across different system boundaries.

<wiki:video url="http://www.youtube.com/watch?v=ZCOQhe5KiD4" width="720" height="405" />

## News

September 26, 2013 -- We gave a [talk about openHAB](https://oracleus.activeevents.com/2013/connect/sessionDetail.ww?SESSION_ID=9177) at the !JavaOne Conference in San Francisco.

September 22, 2013 -- We are proud to announce that openHAB is a winner of the [Duke's Choice Award 2013](https://www.java.net/dukeschoice).

September 22, 2013 -- Our new landing page http://www.openhab.org has been published today.

September 20, 2013 -- We have moved our sources from Google Code to !GitHub - check out our [new git repository](https://github.com/openhab/openhab)!

September 8, 2013 -- openHAB 1.3 has been released! Read the [release notes](ReleaseNotes13) to learn what cool new features it brings.

August 22, 2013 -- We are happy to announce that we now have an official [project proposal at Eclipse](http://eclipse.org/proposals/technology.smarthome/). 

June 28, 2013 -- openHAB is the foundation of the [research project IEM](http://www.oberberg-aktuell.de/index.php?id=144&tx_ttnews%5Btt_news%5D=147147) of the university of applied science in cologne (campus Gummersbach).

June 04, 2013 -- openHAB won the first [IoT Challenge Award](http://iotevent.eu/announcement-the-winner-of-the-iot-challenge-2013/). Thanks to all contributors to make that possible!

May 28, 2013 -- Our talk ["Home Automation for Geeks"](http://parleys.com/play/5148922b0364bc17fc56c8c3) at Devoxx 2012 is now publicly available on Parleys.

May 21, 2013 -- openHAB is now finalist of the IoT Challenge. Thanks to all of you for [226 votes](http://challenge.iotevent.eu).

April 25, 2013 -- The [openHAB-Samples Wiki](https://code.google.com/p/openhab-samples/) has been created. We'd love to grant access to all of you!

April 16, 2013 -- Read the press coverage about the openHAB 1.2 release: [Heise Developer](http://www.heise.de/developer/meldung/30-Bindings-fuer-openHAB-1-2-1842540.html), [Automated Home](http://www.automatedhome.co.uk/software/latest-open-home-automation-bus-openhab-update-brings-raft-of-new-features.html), [The H-Open](http://www.h-online.com/open/news/item/30-bindings-for-openHAB-1-2-1843052.html), [HomeMatic INSIDE](http://www.homematic-inside.de/software/java/item/openhab), [JAXenter](http://it-republik.de/jaxenter/news/Licht-Pflanzen-Raumtemperatur-Heim-Automatisierung-mit-openHAB-067041.html), [Under Linux](https://under-linux.org/content.php?r=6201-Lançado-openHAB-1-2), [KV:\](http://www.kv.by/content/324564-novosti-svobodnogo-po) ([translated](http://www.google.com/translate?hl=en&ie=UTF8&sl=auto&tl=en&u=http%3A%2F%2Fwww.kv.by%2Fcontent%2F324564-novosti-svobodnogo-po))

... more [[News Archive|archived news]]

<a href="http://twitter.com/openHAB"><img src="http://wiki.openhab.googlecode.com/hg/images/twitter.png" /></a>

## Demo

To see openHAB in action, you can directly access our demo server - choose one of these options:
- Check out the [Classic UI on the demo server](http://demo.openhab.org:8080/openhab.app?sitemap=demo) (use !WebKit-based browser, e.g. Safari or Chrome)
- Check out the [GreenT UI on the demo server](http://demo.openhab.org:8080/greent/) (use !WebKit-based browser, e.g. Safari or Chrome)
- Install the [native Android client](https://play.google.com/store/apps/details?id=org.openhab.habdroid) from the Google Play Store on your Android 4.x smartphone, which is preconfigured to use the demo server.
- Install the [native iOS client from the AppStore](http://itunes.apple.com/us/app/openhab/id492054521?mt=8) on your iPhone, iPod Touch or iPad, which is preconfigured to use the demo server.
- Try the [REST API](http://demo.openhab.org:8080/rest) directly on the demo server

If you just want to watch for a start, you might also like our [YouTube channel](http://www.youtube.com/watch?v=F0ImuuIPjYk&list=PLKshtVD6aY8Ig9Vg6qCBy8kcm1Owdajgb)!

<table><tr><td><a href="https://play.google.com/store/apps/details?id=org.openhab.habdroid">
https://developer.android.com/images/brand/en_app_rgb_wo_45.png
</a></td><td><a href="http://itunes.apple.com/us/app/openhab/id492054521?mt=8"><img src="http://wiki.openhab.googlecode.com/hg/images/app-store-badges.png" height="46" /></a></td></tr></table>

## Upcoming Events

October 29th-31st, 2013 -- The Eclipse Smart Home project (and implicitly openHAB) will be presented by Kai and Thomas at [EclipseCon](http://www.eclipsecon.org/europe2013/eclipse-smart-home) Conference in Ludwigsburg, Germany.

November 12th, 2013 20:00 -- openHAB will be presented by Thomas at [Devoxx](http://www.devoxx.be/dv13-thomas-eichstdt-engelen.html?presId=3689) Conference in Antwerpen, Belgium. **Note:** This is BOF session which you can freely attend.

## Presentations

September 10th, 2013 -- openHAB has been presented by Thomas at [M2M Summit 2013](http://www.m2m-summit.com/index.php?article_id=209&clang=0) in Düsseldorf. The [slides](http://www.m2m-summit.com/index.php?article_id=219&clang=0) are now available. The recorded video is now available on [Youtube](http://www.youtube.com/watch?v=k8ig9kkuuqw&feature=share&list=PL7y_R_7H0YGw-K6Rtdtj9xgIIKHVSKNtM).

May 16, 2013 -- openHAB was presented by Kai and Thomas at [GeeCON](http://2013.geecon.org/schedule) Conference in Krakow (Poland). The [slides](http://s3-eu-west-1.amazonaws.com/presentations2013/1_presentation.pdf) are now available.

April 24, 2013 -- openHAB was presented by Kai and Thomas at [JAX](http://jax.de/2013/sessions/?tid=2880#session-25084) Conference in Mainz (Germany).

November 17, 2012 -- The experience of making a home automation system based entirely on Open Source technologies (Arduino and openHAB) was shared by Francesco di Guidieri and Sandro Salari at [Codemotion](http://venezia.codemotion.it/talk/make01.html) in Venice (Italy) ([translated](http://translate.google.de/translate?hl=en&sl=it&tl=en&u=http%3A%2F%2Fvenezia.codemotion.it%2Ftalk%2Fmake01.html)). You can view the [slides on Slideshare](http://www.slideshare.net/SandroSalari/make01).

November 14, 2012 -- openHAB was presented by Kai and Thomas at [Devoxx](http://www.devoxx.com/display/DV12/Home+Automation+for+Geeks) Conference in Antwerpen (Belgium). You can view the [slides on Slideshare](http://de.slideshare.net/xthirtynine/open-hab-devoxx-2012). The recorded video is now available [Parleys.com](http://parleys.com/play/5148922b0364bc17fc56c8c3).

October 18, 2012 -- openHAB was presented at [JUGS](http://www.jugs.org/2012-10-18.html), Java 
User Group in Stuttgart. You can view the [slides on Slideshare](http://www.slideshare.net/ThomasEichstdtEngelen/openhab-jug-stuttgart).

September 27, 2012 -- openHAB was presented at [rheinJUG](http://rheinjug.de/knowledge/vortr-mainmenu-28/188-openhab-heimautomatisierung-in-der-praxis), Java User Group in Düsseldorf. You can view the [recorded video](http://mediathek.hhu.de/watch/0b862d8c-cba3-4de8-9a46-c86fdbb0e849) and the [slides on Slideshare](http://www.slideshare.net/ThomasEichstdtEngelen/openhab-rheinjug-dsseldorf-14800519).

August 08, 2011 -- A [TechTalk](http://www.developergarden.com/apis/techtalk/openhab-home-automation-in-practice) about openHAB was held at Deutsche Telekom AG in Darmstadt. You can view the [recorded video](http://www.youtube.com/watch?v=m6A-Zew0DBc) and the [slides on Slideshare](http://www.slideshare.net/xthirtynine/openhab-techtalk-developergarden-darmstadt).


## Quick Start

If you do not care about reading docs and just want to see things running, here are the quick start instructions for you:
1. [Download](http://code.google.com/p/openhab/downloads/list?can=3) the release version of the openHAB runtime (or alternatively the [latest snapshot build](https://openhab.ci.cloudbees.com/job/openHAB))
1. Unzip it to some local folder
1. [Download](http://code.google.com/p/openhab/downloads/list?can=3) the demo files
1. Unzip to your openHAB folder
1. run `start.sh` resp. `start.bat`
1. Point your browser at [http://localhost:8080/openhab.app?sitemap=demo](http://localhost:8080/openhab.app?sitemap=demo)

If you want to use more bindings, you can download the [addons.zip](http://code.google.com/p/openhab/downloads/list?can=3) and extract it into the addons folder of the openHAB runtime.

If you are interested in more details, please see the [setup guide](http://code.google.com/p/openhab/wiki/Setup).


## Further Reading

Check out [[Presentations|the presentations]] that have been done about openHAB so far. If you are interested in the system architecture and its internals, please check out the wiki for the [[Architecture|architecture overview]].

http://wiki.openhab.googlecode.com/hg/images/features.png

## Community: How to get Support and How to Contribute

If you are looking for support, please check out the [[Support|different support channels]] that we provide.

As any good open source project, openHAB welcomes any participation in the project. Read more in the [[How To Contribute|how to contribute]] guide.

If you are a developer and want to jump right into the sources and execute openHAB from within Eclipse, please have a look at the [IDE setup](IDESetup) procedures.

## Thank you!

We'd like say "thank you" to all openHAB users and contributors, Google for providing this great platform and innoQ for sponsoring bandwidth and infrastructure.

<table><tr>
<td><a href="https://twitter.com/openHAB"><img height=50px src="https://twitter.com/images/resources/twitter-bird-blue-on-white.png" /></a>
</td>
<td><a href="https://plus.google.com/112853105449135193256" rel="publisher" style="text-decoration:none;">
<img src="https://ssl.gstatic.com/images/icons/gplus-32.png" alt="Google+" style="border:0;width:32px;height:32px;"/></a></td>
<td><a href="https://play.google.com/store/apps/details?id=org.openhab.habdroid">
https://developer.android.com/images/brand/en_app_rgb_wo_45.png
</a></td><td><a href="http://itunes.apple.com/us/app/openhab/id492054521?mt=8"><img src="http://wiki.openhab.googlecode.com/hg/images/app-store-badges.png" height="48" /></a></td>
<td><a href="https://openhab.ci.cloudbees.com/job/openHAB">
<img src="http://www.cloudbees.com/sites/default/files/Button-Built-on-CB-1.png" /></a>
</td>
<td><a href="http://www.innoq.com"><img src="http://wiki.openhab.googlecode.com/hg/images/innoQ.png" alt="Thank you innoQ for sponsoring bandwidth and infrastructure"/></a>
</td>
<td>
<wiki:gadget url="http://www.ohloh.net/p/482836/widgets/project_partner_badge.xml" height="53" border="0"/>
</td>
</tr></table>

## Trademark Disclaimer

Product names, logos, brands and other trademarks referred to within the openHAB website are the property of their respective trademark holders. These trademark holders are not affiliated with openHAB or our website. They do not sponsor or endorse our materials.