====
    openHAB, the open Home Automation Bus.
    Copyright (C) 2010, openHAB.org <admin@openhab.org>

    See the contributors.txt file in the distribution for a
    full listing of individual contributors.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as
    published by the Free Software Foundation; either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, see <http://www.gnu.org/licenses>.

    Additional permission under GNU GPL version 3 section 7

    If you modify this Program, or any covered work, by linking or
    combining it with Eclipse (or a modified version of that library),
    containing parts covered by the terms of the Eclipse Public License
    (EPL), the licensors of this Program grant you additional permission
    to convey the resulting work.
====

openHAB Build instructions
==========================

Thanks for your interest in the openHAB project!

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
- Mercurial from http://mercurial.selenic.com/

Make sure that both "mvn" and "hq" commands are available on your path


2. CHECKOUT
===========

Check out the project sources from Google code:

> hg clone https://openhab.googlecode.com/hg/ openhab 

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
- launch the runtime with the "start.sh" (resp. "start.bat") script
- check if everything has started correctly: http://localhost:8080/openhab.app?sitemap=demo


5. STARTING THE DESIGNER
========================

- unzip the file distribution-<version>-designer-<platform>.zip to a local folder
- run the executable "openHAB-Designer.exe"
