openHAB Build Instructions
==========================

Thanks for your interest in the openHAB project (http://www.openHAB.org)!

Building and running the project is fairly easy if you follow the steps
detailed below.


1. PREREQUISITES
================

The openHAB build infrastructure is based on Maven in order to make it
as easy as possible to get up to speed. If you know Maven already then
there won't be any surprises for you. If you have not worked with Maven
yet, just follow the instructions and everything will miraculously work ;-)

What you need before you start:
- Maven3 from http://maven.apache.org/download.html
- Git from http://git-scm.com/downloads

Make sure that both "mvn" and "git" commands are available on your path


2. CHECKOUT
===========

Check out the project sources from GitHub:

> git clone https://github.com/openhab/openhab.git

You will now have all required sources in the openhab subdirectory.


3. BUILDING WITH MAVEN
======================

To build openHAB from the sources, Maven takes care of everything:
- change into the openhab directory ("cd openhab")
- run "mvn clean install" to compile and package all sources

The build result will be available in the folder 
openhab/distribution/target. Both the runtime as well as
the designer zips are placed in there.


4. STARTING THE RUNTIME
=======================

- unzip the file distribution-<version>-runtime.zip to a local folder
- unzip the file distribution-<version>-demo.zip over the same folder
- launch the runtime with the "start.sh" (resp. "start.bat") script
- check if everything has started correctly: http://localhost:8080/openhab.app?sitemap=demo


5. STARTING THE DESIGNER
========================

- unzip the file distribution-<version>-designer-<platform>.zip to a local folder
- run the executable "openHAB-Designer.exe"
