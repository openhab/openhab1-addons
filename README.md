# Chromecast-Openhab-Binding
Chromecast plugin for openhab

Command:
\<ip\>:\<direction\>:\<command\>

This is really early.
This commands bellow is kind of the only command that works.

Example:
Number  Volume "Volume [%s]" { chromecast="192.168.1.3:=:volume" }
String  Status "Status [%s]" { chromecast="192.168.1.3:=:status" }
String  App "App [%s]" { chromecast="192.168.1.3:<:app" }
