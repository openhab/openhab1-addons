org.openhab.binding.netatmo
===========================

openHAB Netatmo Binding

Configuration
-------------

* Create an application at http://dev.netatmo.com/dev/createapp

* Retrieve a refresh token from Netatmo API, using e.g. curl:

```
curl -d "grant_type=password&client_id=123456789012345678901234&client_secret=ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHI&username=example@example.com&password=example" "http://api.netatmo.net/oauth2/token"
```

* Add client id, client secret and refresh token to openhab.cfg

```
netatmo:refresh=300000
netatmo:clientid=123456789012345678901234
netatmo:clientsecret=ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHI
netatmo:refreshtoken=ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDE
```

* Configure items and rules

Example item for the indoor sensor:
```
Number Netatmo_Indoor_CO2 "Carbon dioxide [%d ppm]" {netatmo="00:00:00:00:00:00#Co2"}
```

Supported are Temperature, Humidity, Co2, Pressure and Noise.

Example item for the outdoor sensor (first id is the device, second id is the module):
```
Number Netatmo_Outdoor_Temperature "Outdoor temperature [%.1f °C]" {netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Temperature"}
```

Supported are Temperature and Humidity for the outdoor modules. Temperature, Humidity and Co2 for the indoor modules.

Example rule to send a mail if carbon dioxide reaches a certain threshold:
```
var boolean co2HighWarning = false
var boolean co2VeryHighWarning = false

rule "Monitor carbon dioxide level"
	when
		Item Netatmo_Indoor_CO2 changed
	then
		if(Netatmo_Indoor_CO2.state > 1000) {
			if(co2HighWarning == false) {
				sendMail("example@example.com",
				         "High carbon dioxide level!",
				         "Carbon dioxide level is " + Netatmo_Indoor_CO2.state + " ppm.")
				co2HighWarning = true
			}
		} else if(Netatmo_Indoor_CO2.state > 2000) {
			if(co2VeryHighWarning == false) {
				sendMail("example@example.com",
				         "Very high carbon dioxide level!",
				         "Carbon dioxide level is " + Netatmo_Indoor_CO2.state + " ppm.")
				co2VeryHighWarning = true
			}
		} else {
			co2HighWarning = false
			co2VeryHighWarning = false
		}
end
```

Sample data
-----------
If you want to evaluate this binding but have not got a Netatmo station yourself
yet, you can add the Netatmo office in Paris to your account:

http://www.netatmo.com/en-US/addguest/index/TIQ3797dtfOmgpqUcct3/70:ee:50:00:02:20

Icons
-----
The following icons are used by original Netatmo web app:

http://my.netatmo.com/img/my/app/module_int.png
http://my.netatmo.com/img/my/app/module_ext.png

http://my.netatmo.com/img/my/app/battery_verylow.png
http://my.netatmo.com/img/my/app/battery_low.png
http://my.netatmo.com/img/my/app/battery_medium.png
http://my.netatmo.com/img/my/app/battery_high.png
http://my.netatmo.com/img/my/app/battery_full.png

http://my.netatmo.com/img/my/app/signal_verylow.png
http://my.netatmo.com/img/my/app/signal_low.png
http://my.netatmo.com/img/my/app/signal_medium.png
http://my.netatmo.com/img/my/app/signal_high.png
http://my.netatmo.com/img/my/app/signal_full.png

http://my.netatmo.com/img/my/app/wifi_low.png
http://my.netatmo.com/img/my/app/wifi_medium.png
http://my.netatmo.com/img/my/app/wifi_high.png
http://my.netatmo.com/img/my/app/wifi_full.png
