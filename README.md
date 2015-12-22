
## Introduction

The open Home Automation Bus (openHAB) project aims at providing a universal integration platform for all things around home automation. It is a pure Java solution, fully based on OSGi. The Equinox OSGi runtime and Jetty as a web server build the core foundation of the runtime.

It is designed to be absolutely vendor-neutral as well as hardware/protocol-agnostic. openHAB brings together different bus systems, hardware devices and interface protocols by dedicated bindings. These bindings send and receive commands and status updates on the openHAB event bus. This concept allows designing user interfaces with a unique look&feel, but with the possibility to operate devices based on a big number of different technologies. Besides the user interfaces, it also brings the power of automation logics across different system boundaries.

For further Information please refer to our homepage http://www.openhab.org. The release binaries can be found in the ['releases' section on Github](https://github.com/openhab/openhab/releases). Nightly Snapshot-Builds can be obtained from [Cloudbees](https://openhab.ci.cloudbees.com/job/openHAB/).


## Demo

To see openHAB in action, you can directly access our demo server - choose one of these options:
- Check out the [Classic UI on the demo server](http://demo.openhab.org:8080/openhab.app?sitemap=demo) (use !WebKit-based browser, e.g. Safari or Chrome)
- Check out the [GreenT UI on the demo server](http://demo.openhab.org:8080/greent/) (use !WebKit-based browser, e.g. Safari or Chrome)
- Install the [native Android client](https://play.google.com/store/apps/details?id=org.openhab.habdroid) from the Google Play Store on your Android 4.x smartphone, which is preconfigured to use the demo server.
- Install the [native iOS client from the AppStore](http://itunes.apple.com/us/app/openhab/id492054521?mt=8) on your iPhone, iPod Touch or iPad, which is preconfigured to use the demo server.
- Try the [REST API](http://demo.openhab.org:8080/rest) directly on the demo server

If you just want to watch for a start, you might also like our [YouTube channel](http://www.youtube.com/playlist?list=PLGlxCdrGUagz6lfgo9SlNLhdwI4la_VSv)!

[![HABDroid](https://developer.android.com/images/brand/en_app_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=org.openhab.habdroid) [![iOSApp](http://raw.github.com/wiki/openhab/openhab/images/app-store-badges.png)](http://itunes.apple.com/us/app/openhab/id492054521?mt=8)


## Quick Start

If you do not care about reading docs and just want to see things running, here are the quick start instructions for you:

1. [Download](http://www.openhab.org/downloads.html) the release version of the openHAB runtime (or alternatively the [latest snapshot build](https://openhab.ci.cloudbees.com/job/openHAB))
1. Unzip it to some local folder
1. [Download](http://www.openhab.org/downloads.html) the demo configuration files
1. Unzip to your openHAB folder
1. run `start.sh` resp. `start.bat`
1. Point your browser at [http://localhost:8080/openhab.app?sitemap=demo](http://localhost:8080/openhab.app?sitemap=demo)

If you want to use more bindings, you can download the [addons.zip](http://www.openhab.org/downloads.html) and extract it into the addons folder of the openHAB runtime.

If you are interested in more details, please see the [setup guide](https://github.com/openhab/openhab/wiki/Quick-Setup-an-openHAB-Server).


## Further Reading

Check out [the presentations](https://github.com/openhab/openhab/wiki/Presentations) that have been done about openHAB so far. If you are interested in the system architecture and its internals, please check out the wiki for the [Architecture](https://github.com/openhab/openhab/wiki).

![](http://raw.github.com/wiki/openhab/openhab/images/features.png)

## Community: How to get Support and How to Contribute

If you are looking for support, please check out the [different support channels](https://github.com/openhab/openhab/wiki/Support-options-for-openHAB) that we provide.

As any good open source project, openHAB welcomes any participation in the project. Read more in the [how to contribute](https://github.com/openhab/openhab/wiki/How-To-Contribute) guide.

If you are a developer and want to jump right into the sources and execute openHAB from within Eclipse, please have a look at the [IDE setup](https://github.com/openhab/openhab/wiki/IDE-Setup) procedures.

[![](http://raw.github.com/wiki/openhab/openhab/images/twitter.png)](http://twitter.com/openHAB)

## Trademark Disclaimer

Product names, logos, brands and other trademarks referred to within the openHAB website are the property of their respective trademark holders. These trademark holders are not affiliated with openHAB or our website. They do not sponsor or endorse our materials.
