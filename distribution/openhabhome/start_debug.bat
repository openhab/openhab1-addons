@echo off

:: set path to eclipse folder. If local folder, use '.'; otherwise, use c:\path\to\eclipse
set ECLIPSEHOME=server
 
:: get path to equinox jar inside ECLIPSEHOME folder
for /f "delims= tokens=1" %%c in ('dir /B /S /OD %ECLIPSEHOME%\plugins\org.eclipse.equinox.launcher_*.jar') do set EQUINOXJAR=%%c
 
:: debug options
set DEBUG="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"

:: start Eclipse w/ java
echo Launching the openHAB runtime...
java %DEBUG_OPTS% -Declipse.ignoreApp=true -Dosgi.noShutdown=true -D32 -Djetty.home=. -Dlogback.configurationFile=logs/logback.xml -Dfelix.fileinstall.dir=addons -Djava.library.path=lib -Djava.security.auth.login.config=./etc/login.conf -Dopenhab.securityEnabled -jar %EQUINOXJAR% %* -console
