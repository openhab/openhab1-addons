# Davis Binding

The openHAB Davis binding supports reading data from Davis weather stations.  Most Davis weather stations should be supported.

The binding is based on the [Serial Communication Reference Manual](http://www.google.at/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCQQFjAA&url=http%3A%2F%2Fwww.davisnet.com%2Fsupport%2Fweather%2Fdownload%2FVantageSerialProtocolDocs_v261.pdf&ei=yns1VLO9B9Pe7Ab9hYDgDQ&usg=AFQjCNEUP_O6jjV3tHaxc7_faaLKWAtw2g&sig2=0YuJy45Qmk76RlffOqayuA&bvm=bv.76943099,d.ZGU) from Davis.


## Binding Configuration

The binding can be configured in the file `services/davis.cfg`.

| Property | Default | Required | Description                            |
|----------|---------|:--------:|----------------------------------------|
| port     |         | Yes      | The serial port of the Weather station |
| refresh  | 10000   | No       | The refresh interval (in milliseconds) |


## Item Configuration

Item bindings should conform to this format:

    davis="<value-key>"

The following table defines the allowable values for `<value-key>`.

| Value-Key                   |
|-----------------------------|
| bar_trend                   |
| barometer                   |
| console_battery_voltage     |
| dew_point                   |
| firmware_date               |
| firmware_version            |
| heat_index                  |
| inside_humidity             |
| inside_temp                 |
| last_15min_rain             |
| last_24h_rain               |
| last_hour_rain              |
| outside_humidity            |
| outside_temp                |
| rain_rate                   |
| receivers                   |
| rx_count_consecutive        |
| rx_count_crc                |
| rx_count_missed             |
| rx_count_resync             |
| rx_count_total              |
| solar_radiation             |
| station_type                |
| thsw_index                  |
| time                        |
| transmitter_battery_status  |
| uv                          |
| wind_chill                  |
| wind_direction              |
| wind_direction_10min__gust  |
| wind_speed                  |
| wind_speed_10min_avg        |
| wind_speed_10min_avg_hires  |
| wind_speed_10min_gust_hires |
| wind_speed_2min_avg_hires   |


## Examples

	String StationType		"StationType: [%s]"		{ davis="station_type" }
	Number Receivers		"Receivers: [%.0f]"		{ davis="receivers" }
	Number TransmitterBattery	"Transmitter Battery [%.1f]"	{ davis="transmitter_battery_status" } 
	Number ConsoleBattVoltage	"Console Battery [%.2f V]"	{ davis="console_battery_voltage" } 

	DateTime Date		"Date [%1$tA, %1$td.%1$tm.%1$tY]"	{ davis="time" }

	Number InsideTemp	"Inside Temp [%.1f °C ]"	{ davis="inside_temp" } 
	Number OutsideTemp	"Outside Temp [%.1f °C ]"	{ davis="outside_temp" } 
	Number DewPoint		"Dew Point [%.1f °C ]"		{ davis="dew_point" } 
	Number HeatIndex	"Heat Index [%.1f °C ]"		{ davis="heat_index" } 
	Number WindChill	"Wind Chill [%.1f °C ]"		{ davis="wind_chill" } 
	Number THSWIndex	"THSW Index[%.1f °C ]"		{ davis="thsw_index" } 

	Number InsideHumidity	"Inside Humidity [%.0f %% ]"	{ davis="inside_humidity" }
	Number OutsideHumidity	"Outside Humidity [%.0f %% ]"	{ davis="outside_humidity" }

	String BarTrend		"Barometer Trend [%s]"	{ davis="bar_trend" }
	Number Barometer	"Barometer [%.0f hPa]"	{ davis="barometer" }

	Number WindSpeed		"Wind Speed [%.1f km/h]"			{ davis="wind_speed" }
	Number WindSpeed10minAvg	"Wind Speed 10min Avg [%.1f km/h]"		{ davis="wind_speed_10min_avg" }
	Number WindDirection		"Wind Direction [%.0f °]"			{ davis="wind_direction" }
	Number WindSpeed10minHiRes	"Wind Speed 10min Avg HiRes [%.1f km/h]"	{ davis="wind_speed_10min_avg_hires" }
	Number WindSpeed2minHiRes	"Wind Speed 10min Avg HiRes [%.1f km/h]"	{ davis="wind_speed_2min_avg_hires" }
	Number WindSpeed10minGustHiRes	"Wind Speed 10min Avg HiRes [%.1f km/h]"	{ davis="wind_speed_10min_gust_hires" }
	Number WindDirectionGust	"Wind Direction Gust [%.0f °]"			{ davis="wind_direction_10min__gust" }

	Number RainRate		"Rain Rate [%.1f mm/h ]"	{ davis="rain_rate" } 
	Number Rain15min	"Rain Rate [%.1f mm/h ]"	{ davis="last_15min_rain" } 
	Number Rainlast1h	"Rain Rate [%.1f mm/h ]"	{ davis="last_hour_rain" } 
	Number Rainlast24h	"Rain Rate [%.1f mm/h ]"	{ davis="last_24h_rain" } 

	Number UVIndex	"UV Index [%.1f ]"		{ davis="uv" } 
	Number Solar	"Solar Radiation [%.0f W/m²]"	{ davis="solar_radiation" } 

	String firmware_date	"Firmware Date: [%s]"		{ davis="firmware_date" }
	String firmware_version	"Firmware Version: [%s]"	{ davis="firmware_version" }

	String rx_total		"RX Packets total: [%s]"				{ davis="rx_count_total" }
	String rx_missed	"RX Packets missed: [%s]"				{ davis="rx_count_missed" }
	String rx_resync	"RX Resyncs: [%s]"					{ davis="rx_count_resync" }
	String rx_consec	"RX Packets received in a row without error: [%s]"	{ davis="rx_count_consecutive" }
	String rx_crc		"RX Packets CRC failes: [%s]"				{ davis="rx_count_crc" }
