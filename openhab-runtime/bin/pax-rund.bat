@echo off
SETLOCAL
set _SCRIPTS_=%~dp0

java -cp ".;%_SCRIPTS_%;%_SCRIPTS_%\pax-runner-1.4.0.jar" org.ops4j.pax.runner.daemon.DaemonLauncher %1 %2 %3 %4 %5 %6 %7 %8 %9 