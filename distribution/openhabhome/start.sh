#!/bin/bash
#
# openHAB, the open Home Automation Bus.
# Copyright (C) 2010, openHAB.org <admin@openhab.org>
#
# See the contributors.txt file in the distribution for a
# full listing of individual contributors.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as
# published by the Free Software Foundation; either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, see <http://www.gnu.org/licenses>.
#
# Additional permission under GNU GPL version 3 section 7
#
# If you modify this Program, or any covered work, by linking or
# combining it with Eclipse (or a modified version of that library),
# containing parts covered by the terms of the Eclipse Public License
# (EPL), the licensors of this Program grant you additional permission
# to convey the resulting work.
#


# set path to eclipse folder. If local folder, use '.'; otherwise, use /path/to/eclipse/
eclipsehome="server";

# get path to equinox jar inside $eclipsehome folder
cp=$(find $eclipsehome -name "org.eclipse.equinox.launcher_*.jar" | sort | tail -1);

echo Launching the openHAB runtime...
java -Declipse.ignoreApp=true -Dosgi.noShutdown=true -D32 -Djetty.home=. -Dlogback.configurationFile=logs/logback.xml -Djava.library.path=lib -jar $cp $* -console

