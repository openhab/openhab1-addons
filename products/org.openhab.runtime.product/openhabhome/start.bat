@echo off

:: set path to eclipse folder. If local folder, use '.'; otherwise, use c:\path\to\eclipse
set ECLIPSEHOME=.
 
:: get path to equinox jar inside ECLIPSEHOME folder
for /f "delims= tokens=1" %%c in ('dir /B /S /OD %ECLIPSEHOME%\plugins\org.eclipse.equinox.launcher_*.jar') do set EQUINOXJAR=%%c
 
:: start Eclipse w/ java
echo Using %EQUINOXJAR% to start up Eclipse...
java -jar %EQUINOXJAR% %*
