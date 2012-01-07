@echo off

:: set path to eclipse folder. If local folder, use '.'; otherwise, use c:\path\to\eclipse
set ECLIPSEHOME=server
 
:: get path to equinox jar inside ECLIPSEHOME folder
for /f "delims= tokens=1" %%c in ('dir /B /S /OD %ECLIPSEHOME%\plugins\org.eclipse.equinox.launcher_*.jar') do set EQUINOXJAR=%%c
 
:: start Eclipse w/ java
echo Launching the openHAB runtime...
java -Declipse.ignoreApp=true -Dosgi.noShutdown=true -D32 -Djetty.home=. -Dlogback.configurationFile=logs/logback.xml -Dfelix.fileinstall.dir=addons -Djava.library.path=lib -Djava.security.auth.login.config=./etc/login.conf -Dorg.terracotta.quartz.skipUpdateCheck=true -jar %EQUINOXJAR% %* -console
