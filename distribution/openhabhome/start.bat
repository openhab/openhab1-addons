@REM
@REM openHAB, the open Home Automation Bus.
@REM Copyright (C) 2010, openHAB.org <admin@openhab.org>
@REM
@REM See the contributors.txt file in the distribution for a
@REM full listing of individual contributors.
@REM
@REM This program is free software; you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as
@REM published by the Free Software Foundation; either version 3 of the
@REM License, or (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with this program; if not, see <http://www.gnu.org/licenses>.
@REM
@REM Additional permission under GNU GPL version 3 section 7
@REM
@REM If you modify this Program, or any covered work, by linking or
@REM combining it with Eclipse (or a modified version of that library),
@REM containing parts covered by the terms of the Eclipse Public License
@REM (EPL), the licensors of this Program grant you additional permission
@REM to convey the resulting work.
@REM

@echo off

:: set path to eclipse folder. If local folder, use '.'; otherwise, use c:\path\to\eclipse
set ECLIPSEHOME=.\server
 
:: get path to equinox jar inside ECLIPSEHOME folder
for /f "delims= tokens=1" %%c in ('dir /B /S /OD %ECLIPSEHOME%\plugins\org.eclipse.equinox.launcher_*.jar') do set EQUINOXJAR=%%c
 
:: start Eclipse w/ java
echo Launching the openHAB runtime...
java -jar %EQUINOXJAR% %*
