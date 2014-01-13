## Introduction

The open Home Automation Bus (openHAB) project aims at providing a universal integration platform for all things around home automation. It is a pure Java solution, fully based on OSGi. The Equinox OSGi runtime and Jetty as a web server build the core foundation of the runtime.

It is designed to be absolutely vendor-neutral as well as hardware/protocol-agnostic. openHAB brings together different bus systems, hardware devices and interface protocols by dedicated bindings. These bindings send and receive commands and status updates on the openHAB event bus. This concept allows designing user interfaces with a unique look&feel, but with the possibility to operate devices based on a big number of different technologies. Besides the user interfaces, it also brings the power of automation logics across different system boundaries.

## News
December 31, 2013 -- The openHAB 1.4 release has been scheduled for February 2, 2014 - stay tuned!

November 07, 2013 -- The Eclipse SmartHome project has been officially created by the Eclipse Foundation

September 22, 2013 -- We are proud to announce that openHAB is a winner of the [Duke's Choice Award 2013](https://www.java.net/dukeschoice).

September 22, 2013 -- Our new landing page http://www.openhab.org has been published today.

September 20, 2013 -- We have moved our sources from Google Code to GitHub - check out our [new git repository](https://github.com/openhab/openhab)!

September 8, 2013 -- openHAB 1.3 has been released! Read the [Release-Notes-13](https://github.com/openhab/openhab/wiki/Release-Notes-13) to learn what cool new features it brings.


... more [archived news](https://github.com/openhab/openhab/wiki/News-Archive)

[![](http://raw.github.com/wiki/openhab/openhab/images/twitter.png)](http://twitter.com/openHAB)

## Demo

To see openHAB in action, you can directly access our demo server - choose one of these options:
- Check out the [Classic UI on the demo server](http://demo.openhab.org:8080/openhab.app?sitemap=demo) (use !WebKit-based browser, e.g. Safari or Chrome)
- Check out the [GreenT UI on the demo server](http://demo.openhab.org:8080/greent/) (use !WebKit-based browser, e.g. Safari or Chrome)
- Install the [native Android client](https://play.google.com/store/apps/details?id=org.openhab.habdroid) from the Google Play Store on your Android 4.x smartphone, which is preconfigured to use the demo server.
- Install the [native iOS client from the AppStore](http://itunes.apple.com/us/app/openhab/id492054521?mt=8) on your iPhone, iPod Touch or iPad, which is preconfigured to use the demo server.
- Try the [REST API](http://demo.openhab.org:8080/rest) directly on the demo server

If you just want to watch for a start, you might also like our [YouTube channel](http://www.youtube.com/playlist?list=PLGlxCdrGUagz6lfgo9SlNLhdwI4la_VSv)!

[![HABDroid](https://developer.android.com/images/brand/en_app_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=org.openhab.habdroid) [![iOSApp](http://wiki.openhab.googlecode.com/hg/images/app-store-badges.png)](http://itunes.apple.com/us/app/openhab/id492054521?mt=8)

## Downloads

The released binaries of openHAB can be downloaded here

* [Runtime](https://code.google.com/p/openhab/downloads/detail?name=openhab-runtime-1.3.1.zip)
* [Addons](https://code.google.com/p/openhab/downloads/detail?name=openhab-addons-1.3.1.zip)
* [Demofiles](https://code.google.com/p/openhab/downloads/detail?name=openhab-demo-configuration-1.3.1.zip)
* [Designer Linux32](https://code.google.com/p/openhab/downloads/detail?name=openhab-designer-linux-1.3.1.zip)
* [Designer Linux64](https://code.google.com/p/openhab/downloads/detail?name=openhab-designer-linux64-1.3.1.zip)
* [Designer MacOSX](https://code.google.com/p/openhab/downloads/detail?name=openhab-designer-macosx64-1.3.1.zip)
* [Designer Win32/64](https://code.google.com/p/openhab/downloads/detail?name=openhab-designer-win-1.3.1.zip)
* [GreenT](https://code.google.com/p/openhab/downloads/detail?name=openhab-greent-1.3.1.zip)

The nightly Snapshot-Builds can be found at [Cloudbees](https://openhab.ci.cloudbees.com/job/openHAB/)

## Presentations

December 18, 2013 -- Eclipse SmartHome was presented by Kai at the [Eclipse Day at Googleplex](http://wiki.eclipse.org/Eclipse_Day_At_Googleplex_2013) in Mountain View.

November 29, 2013 -- Eclipse SmartHome was presented by Kai at [Nordic Coding](http://www.meetup.com/Nordic-Coding/events/150854112/?a=ea1_grp&rv=ea1&_af_eid=150854112&_af=event) in Kiel.

November 12, 2013 -- openHAB was presented by Thomas at [Devoxx](http://www.devoxx.be/dv13-thomas-eichstdt-engelen.html?presId=3689) Conference in Antwerpen, Belgium.

October 30, 2013 -- The Eclipse SmartHome project (and implicitly openHAB) was presented by Kai at [EclipseCon](http://www.eclipsecon.org/europe2013/eclipse-smart-home) Conference in Ludwigsburg, Germany.

September 26, 2013 -- We gave a [talk about openHAB](https://oracleus.activeevents.com/2013/connect/sessionDetail.ww?SESSION_ID=9177) at the JavaOne Conference in San Francisco.

September 10, 2013 -- openHAB has been presented by Thomas at [M2M Summit 2013](http://www.m2m-summit.com/index.php?article_id=209&clang=0) in Düsseldorf. The [slides](http://www.m2m-summit.com/index.php?article_id=219&clang=0) are now available. The recorded video is now available on [Youtube](http://www.youtube.com/watch?v=k8ig9kkuuqw&feature=share&list=PL7y_R_7H0YGw-K6Rtdtj9xgIIKHVSKNtM).

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

If you are interested in more details, please see the [setup guide](https://github.com/openhab/openhab/wiki/Quick-Setup-an-openHAB-Server).


## Further Reading

Check out [the presentations](https://github.com/openhab/openhab/wiki/Presentations) that have been done about openHAB so far. If you are interested in the system architecture and its internals, please check out the wiki for the [Architecture](https://github.com/openhab/openhab/wiki).

![](http://raw.github.com/wiki/openhab/openhab/images/features.png)

## Community: How to get Support and How to Contribute

If you are looking for support, please check out the [different support channels](https://github.com/openhab/openhab/wiki/Support-options-for-openHAB) that we provide.

As any good open source project, openHAB welcomes any participation in the project. Read more in the [how to contribute](https://github.com/openhab/openhab/wiki/How-To-Contribute) guide.

If you are a developer and want to jump right into the sources and execute openHAB from within Eclipse, please have a look at the [IDE setup](https://github.com/openhab/openhab/wiki/IDE-Setup) procedures.

## Trademark Disclaimer

Product names, logos, brands and other trademarks referred to within the openHAB website are the property of their respective trademark holders. These trademark holders are not affiliated with openHAB or our website. They do not sponsor or endorse our materials.
