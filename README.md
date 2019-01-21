## Introduction

The open Home Automation Bus (openHAB) project aims at providing a universal integration platform for all things around home automation. It is a pure Java solution, fully based on OSGi.

It is designed to be absolutely vendor-neutral as well as hardware/protocol-agnostic. openHAB brings together different bus systems, hardware devices and interface protocols by dedicated bindings. These bindings send and receive commands and status updates on the openHAB event bus. This concept allows designing user interfaces with a unique look&feel, but with the possibility to operate devices based on a big number of different technologies. Besides the user interfaces, it also brings the power of automation logics across different system boundaries.

For further Information please refer to our homepage [www.openhab.org](http://www.openhab.org). 

## openHAB 2 Distribution

openHAB 2 is the successor of [openHAB 1](https://github.com/openhab/openhab/wiki). It is an open-source solution based on the [Eclipse SmartHome](https://www.eclipse.org/smarthome/) framework. It is fully written in Java and uses [Apache Karaf](http://karaf.apache.org/) together with [Eclipse Equinox](https://www.eclipse.org/equinox/) as an OSGi runtime and bundles this with [Jetty](https://www.eclipse.org/jetty/) as an HTTP server.

For the latest snapshot builds, please see to our [jenkins job](https://ci.openhab.org/job/openHAB-Distribution/).

## Getting Started

Please refer to [our tutorials](https://www.openhab.org/docs/tutorial/) on how to get started with openHAB 2.

## Community: How to Get Involved

As any good open source project, openHAB welcomes community participation in the project. Read more in the [how to contribute](CONTRIBUTING.md) guide.

If you are a developer and want to jump right into the sources and execute openHAB from within Eclipse, please have a look at the [IDE setup](https://www.openhab.org/docs/developer/development/ide.html) procedures.

You can also learn [how openHAB 2 bindings are developed](https://www.openhab.org/docs/developer/development/bindings.html).

In case of problems or questions, please join our vibrant [openHAB community](https://community.openhab.org/).

## Acknowledgements

<table>
<tr><td width=30%><img src="https://www.digitalocean.com/assets/media/logos-badges/png/DO_Powered_by_Badge_blue-fe4c6688.png"></td>
<td><a href="https://www.digitalocean.com">DigitalOcean</a> sponsors our <a href="https://community.openhab.org/">community forum</a> hosting.</td>
</tr>
<tr><td width=30%><img src="http://www.openhab.org/assets/images/bintray.png"/></td>
<td><a href="https://www.jfrog.com">JFrog</a> is providing an <a href="https://openhab.jfrog.io">Artifactory instance for our snapshots</a> as well as distributing our <a href="https://bintray.com/openhab">releases through Bintray</a>.</td>
</tr>
<tr><td width=30%><img src="http://www.ej-technologies.com/images/product_banners/jprofiler_large.png"/></td>
<td><a href="http://www.ej-technologies.com/">EJ Technologies</a> is providing us open source licenses for <a href="http://www.ej-technologies.com/products/jprofiler/overview.html">JProfiler</a> to make openHAB even more awesome.</td>
</tr>

</table>
