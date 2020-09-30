# Davis Binding

The openHAB Davis binding supports reading data from Davis weather stations.  Most Davis weather stations should be supported. 

The binding is based on the [Serial Communication Reference Manual](http://www.google.at/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCQQFjAA&url=http%3A%2F%2Fwww.davisnet.com%2Fsupport%2Fweather%2Fdownload%2FVantageSerialProtocolDocs_v261.pdf&ei=yns1VLO9B9Pe7Ab9hYDgDQ&usg=AFQjCNEUP_O6jjV3tHaxc7_faaLKWAtw2g&sig2=0YuJy45Qmk76RlffOqayuA&bvm=bv.76943099,d.ZGU) from Davis.


## Binding Configuration

The binding can be configured in the file `services/davis.cfg`.

Connection type must be specified as either serial (using the property 'port') or ip (using the property 'hostName').
These are mutually exclusive and both must not be simultaneously configured.

| Property             | Default  | Required                    | Description                                                           |
|----------------------|----------|-----------------------------|-----------------------------------------------------------------------|
| port                 |          | Yes if serial communication | The serial port of the Weather station                                |
| hostName             |          | Yes if ip communication     | HostName of the Weather station                                       |
| refresh              | 10000    | No                          | The refresh interval (in milliseconds)                                |
| readResponseWaitTime | 200|1000 | No                          | Wait time for response (in milliseconds); default is 200 for serial, and 1000 for ip |



## Item Configuration

Item bindings should conform to this format:

```
davis="<value-key>"
```

The following table defines the allowable values for `<value-key>`.

| Value-Key                   |
|-----------------------------|
| barometer                        |
| barometer_day_hi                 |
| barometer_day_hi_timeofday       |
| barometer_day_lo_timeofday       |
| barometer_day_lo                 |
| barometer_month_hi               |
| barometer_month_lo               |
| barometer_year_hi                |
| barometer_year_lo                |
| bar_trend                        |
| console_battery_voltage          |
| day_rain                         |
| dew_point                        |
| dew_point_day_hi                 |
| dew_point_day_hi_timeofday       |
| dew_point_day_lo                 |
| dew_point_day_lo_timeofday       |
| dew_point_month_hi               |
| dew_point_month_lo               |
| dew_point_year_hi                |
| dew_point_year_lo                |
| extra_humidity_1                 |
| extra_humidity_2                 |
| extra_humidity_3                 |
| extra_humidity_4                 |
| extra_humidity_5                 |
| extra_humidity_6                 |
| extra_humidity_7                 |
| extra_temperature_1              |
| extra_temperature_2              |
| extra_temperature_3              |
| extra_temperature_4              |
| extra_temperature_5              |
| extra_temperature_6              |
| extra_temperature_7              |
| firmware_date                    |
| firmware_version                 |
| heat_index                       |
| heat_index_day_hi                |
| heat_index_day_hi_timeofday      |
| heat_index_month_hi              |
| heat_index_year_hi               |
| inside_humidity                  |
| inside_humidity_daily_lo         |
| inside_humidity_day_hi           |
| inside_humidity_day_hi_timeofday |
| inside_humidity_day_lo_timeofday |
| inside_humidity_day_lo           |
| inside_humidity_month_hi         |
| inside_humidity_month_lo         |
| inside_humidity_year_hi          |
| inside_temp                      |
| inside_temp_day_hi               |
| inside_temp_day_hi_timeofday     |
| inside_temp_day_lo_timeofday     |
| inside_temp_day_lo               |
| inside_temp_month_hi             |
| inside_temp_month_lo             |
| inside_temp_year_hi              |
| inside_temp_year_lo              |
| last_15min_rain                  |
| last_24h_rain                    |
| last_hour_rain                   |
| leaf_temperature_1               |
| leaf_temperature_2               |
| leaf_temperature_3               |
| leaf_temperature_4               |
| leaf_wetnes_1                    |
| leaf_wetnes_2                    |
| leaf_wetnes_3                    |
| leaf_wetnes_4                    |
| month_rain                       |
| outside_humidity                 |
| outside_temp                     |
| outside_temp_day_hi              |
| outside_temp_day_hi_timeofday    |
| outside_temp_day_lo              |
| outside_temp_day_lo_timeofday    |
| outside_temp_month_hi            |
| outside_temp_month_lo            |
| outside_temp_year_hi             |
| outside_temp_year_lo             |
| rain_rate                        |
| rain_rate_day_hi                 |
| rain_rate_day_hi_timeofday       |
| rain_rate_hour_hi                |
| rain_rate_month_hi               |
| rain_rate_year_hi                |
| receivers                        |
| rx_count_consecutive             |
| rx_count_crc                     |
| rx_count_missed                  |
| rx_count_resync                  |
| rx_count_total                   |
| soil_moisture_1                  |
| soil_moisture_2                  |
| soil_moisture_3                  |
| soil_moisture_4                  |
| soil_temperature_1               |
| soil_temperature_2               |
| soil_temperature_3               |
| soil_temperature_4               |
| solar_radiation                  |
| solar_radiation_day_hi           |
| solar_radiation_day_hi_timeofday |
| solar_radiation_month_hi         |
| solar_radiation_year_hi          |
| station_type                     |
| storm_rain                       |
| thsw_index                       |
| thsw_index_day_hi                |
| thsw_index_day_hi_timeofday      |
| thsw_index_month_hi              |
| thsw_index_year_hi               |
| time                             |
| transmitter_battery_status       |
| uv                               |
| uv_day_hi                        |
| uv_day_hi_timeofday              |
| uv_month_hi                      |
| uv_year_hi                       |
| wind_chill                       |
| wind_chill_day_hi_timeofday      |
| wind_chill_day_lo                |
| wind_chill_month_lo              |
| wind_chill_year_lo               |
| wind_direction                   |
| wind_direction_10min_gust        |
| wind_speed                       |
| wind_speed_10min_avg             |
| wind_speed_10min_avg_hires       |
| wind_speed_10min_gust_hires      |
| wind_speed_2min_avg_hires        |
| wind_speed_day_hi                |
| wind_speed_day_hi_timeofday      |
| wind_speed_month_hi              |
| wind_speed_year_hi               |
| year_rain                        |


## Examples

```
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
```
