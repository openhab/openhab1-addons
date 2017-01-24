The Weather binding collects current and forecast weather data from different providers with a free weather API. You can also display weather data with highly customizable html layouts and icons.

![](https://farm4.staticflickr.com/3946/15407522168_7ea34d51e1_o.png)

Currently supported weather providers:
- [ForecastIo](http://forecast.io)
- [OpenWeatherMap](http://openweathermap.org)
- [Wunderground](http://wunderground.com)
- [WorldWeatherOnline](http://worldweatheronline.com)
- [Hamweather](http://hamweather.com)
- [Yahoo](https://weather.yahoo.com)
- [MeteoBlue](https://www.meteoblue.com/) **new in 1.9**

**Important changes in 1.8**: The item token for layouts has been removed to make the binding openHAB 2 compatible (see Example tokens) and new example weather-data file in [download section](#download).

**Important changes for Yahoo provider**: In January 2016, Yahoo discontinued the service that allows you to use latitude and longitude to locate your weather location.  To continue to use the Yahoo weather provider, you must upgrade the weather binding to 1.8.1 or later and supply a woeid (Where On Earth ID) for your location, as shown in the example below.  You can find your woeid by copying the numeric digits at the end of the URL for your location at weather.yahoo.com.

## Configuration
### openhab.cfg
```
############################## Weather Binding ##############################
#
# The apikey for the different weather providers, at least one must be specified
# Note: Hamweather requires two apikeys: client_id=apikey, client_secret=apikey2
#weather:apikey.ForecastIo=
#weather:apikey.OpenWeatherMap=
#weather:apikey.WorldWeatherOnline=
#weather:apikey.Wunderground=
#weather:apikey.Hamweather=
#weather:apikey2.Hamweather=
#weather:apikey.Meteoblue=

# location configuration, you can specify multiple locations
#weather:location.<locationId1>.name=
#weather:location.<locationId1>.latitude=   (not required for Yahoo)
#weather:location.<locationId1>.longitude=  (not required for Yahoo)
#weather:location.<locationId1>.woeid=      (required for Yahoo)
#weather:location.<locationId1>.provider=
#weather:location.<locationId1>.language=
#weather:location.<locationId1>.updateInterval=

#weather:location.<locationId2>.name=
#weather:location.<locationId2>.latitude=   (not required for Yahoo)
#weather:location.<locationId2>.longitude=  (not required for Yahoo)
#weather:location.<locationId2>.woeid=      (required for Yahoo)
#weather:location.<locationId2>.provider=
#weather:location.<locationId2>.language=
#weather:location.<locationId2>.updateInterval=
```
Before you can use a weather provider, you need to register a free apikey on the website of the provider.  
**Note:** Hamweather has two apikeys (client_id, secret_id), Yahoo does not need an apikey.

Now you can specify locations. Each location has a locationId that can be referenced from an item. A location has five required parameters.
- **latitude, longitude:** the coordinates the weather is retrieved from (not required for Yahoo)
- **woeid:** required for Yahoo, the numeric Where On Earth ID, found at end of your weather.yahoo.com URL
- **provider:** a reference to a provider name
- **language:** the language of the weather condition text (see provider homepage for supported languages)
- **updateInterval:** the interval in minutes the weather is retrieved
- **name:** (optional), the name of the location, useful for displaying in html layouts

**Important:** Each weather provider has a daily request limit for the free weather api. Also the weather does not change quickly, so please choose a moderate updateInterval. The request limit can be found on the weather provider website. 

**Example:** Let's display the current temperature and humidity in Salzburg (AT) from Yahoo.  
**openhab.cfg**
```
weather:location.home.woeid=547826
weather:location.home.provider=Yahoo
weather:location.home.language=de
weather:location.home.updateInterval=10
```
**Item**
```
Number   Temperature   "Temperature [%.2f °C]"   {weather="locationId=home, type=temperature, property=current"}
Number   Humidity      "Humidity [%d %%]"        {weather="locationId=home, type=atmosphere, property=humidity"}
```
For Yahoo you don't need a apikey, but you do need a woeid (which you can find as the numeric digits at the end of your weather.yahoo.com URL). The location has the locationId *home* and updates the weather data every 10 minutes.  
In the item file, you reference the locationId and the type and property to display (see below for more).  
Let's say you want to switch to another provider, ForecastIo. All you have to do is register your apikey, configure it, supply your latitude and longitude, and change the provider in your location:  
**openhab.cfg** (example key is not a registered key!)
```
weather:apikey.ForecastIo=sdf7g69fdgdfg679dfg69sdgkj
weather:location.home.latitude=47.8011
weather:location.home.longitude=13.0448
weather:location.home.provider=ForecastIo
```
Now the weather data is retrieved from ForecastIo, your item file does not need to be changed! Let's say you want to have the current temperature from ForecastIo and the humidity from OpenWeatherMap.  
**openhab.cfg**
```
// example keys are not a registered keys!
weather:apikey.ForecastIo=sdf7g69fdgdfg679dfg69sdgkj
weather:apikey.OpenWeatherMap=766967gdfgdfgs9g76dsfg5ds76g521

weather:location.home-FIO.latitude=47.8011
weather:location.home-FIO.longitude=13.0448
weather:location.home-FIO.provider=ForecastIo
weather:location.home-FIO.language=de
weather:location.home-FIO.updateInterval=10

weather:location.home-OWM.latitude=47.8011
weather:location.home-OWM.longitude=13.0448
weather:location.home-OWM.provider=OpenWeatherMap
weather:location.home-OWM.language=de
weather:location.home-OWM.updateInterval=10
```
**Item**
```
Number   Temperature   "Temperature [%.2f °C]"   {weather="locationId=home-FIO, type=temperature, property=current"}
Number   Humidity      "Humidity [%d %%]"        {weather="locationId=home-OWM, type=atmosphere, property=humidity"}
```
Or you want to see the current temperature if there is a difference between providers and which one gives the best result for your location. This can be done with all available type/properties of course.
```
Number   Temperature_FIO   "Temperature-FIO [%.2f °C]"   {weather="locationId=home-FIO, type=temperature, property=current"}
Number   Temperature_OWM   "Temperature-OWM [%.2f °C]"   {weather="locationId=home-OWM, type=temperature, property=current"}
```
### Available bindings
* **type** `atmosphere`
    * **property** `humidity, visibility, visibility, pressure, pressure, pressureTrend, ozone, uvIndex`
* **type** `clouds`
    * **property** `percent`
* **type** `condition`
    * **property** `text, observationTime, id, lastUpdate, commonId`
* **type** `precipitation`
    * **property** `rain, rain, snow, snow, probability`
* **type** `temperature`
    * **property** `current, min, max, feel, dewpoint, minMax`
* **type** `wind`
    * **property** `speed, direction, degree, gust, chill`

Important: type and property are case sensitive! So enter the values exactly as shown.  
Every weather provider sends different data, so not all properties are set. If you want to know which provider sends which properties, switch the binding to [DEBUG Mode](#debugging-and-tracing) (see below).

### Number formatting

Each item can be formatted with the parameters roundingMode and scale. Supported roundingModes and what they do can be found in the [JavaDocs](http://docs.oracle.com/javase/7/docs/api/java/math/RoundingMode.html). Default values are roundingMode=half_up and scale=2.

**Example:**
```
// default
String   Temperatur_MinMax   "Min/Max [%s °C]"   {weather="locationId=home, forecast=0, type=temperature, property=minMax"}
> Temperatur_MinMax state updated to 8.91/17.25

// scale set to 0
String   Temperatur_MinMax   "Min/Max [%s °C]"   {weather="locationId=home, forecast=0, type=temperature, property=minMax, scale=0"}
> Temperatur_MinMax state updated to 9/17

// roundingMode and scale set
String   Temperatur_MinMax   "Min/Max [%s °C]"   {weather="locationId=home, forecast=0, type=temperature, property=minMax, roundingMode=down, scale=0"}
> Temperatur_MinMax state updated to 8/17
```

### Unit conversion

The default units are:
* speed: kilometer per hour
* temperature: celsius
* precipitation: rain in millimeters and snow in centimeter
* pressure: millibar

You can convert numeric values to other units with the `unit` parameter. Example to convert the temperature from celsius to fahrenheit:
```
Number   Temperature_F    "Temperature [%.2f °F]"   {weather="locationId=home, type=temperature, property=current, unit=fahrenheit"}
``` 

All possible conversions can be found in the following Items section.

### Items
If you copy and paste, don't forget to change the locationId to the one you specified.
```
// atmosphere
Number   Humidity    	  "Humidity [%d %%]"  	  {weather="locationId=home, type=atmosphere, property=humidity"}
Number   Visibility    	  "Visibility [%.2f km]"  {weather="locationId=home, type=atmosphere, property=visibility"}
Number   Visibility_Mph   "Visibility [%.2f mi]"  {weather="locationId=home, type=atmosphere, property=visibility, unit=mph"}
Number   Pressure    	  "Pressure [%.2f mb]"    {weather="locationId=home, type=atmosphere, property=pressure"}
Number   Pressure_Inches  "Pressure [%.2f in]"    {weather="locationId=home, type=atmosphere, property=pressure, unit=inches"}
String   Pressure_Trend   "Pressuretrend [%s]"    {weather="locationId=home, type=atmosphere, property=pressureTrend"}
Number   Ozone            "Ozone [%d ppm]"    	  {weather="locationId=home, type=atmosphere, property=ozone"}
Number   UV_Index         "UV Index"              {weather="locationId=home, type=atmosphere, property=uvIndex, scale=0"}

// clouds
Number   Clouds   "Clouds [%.0f %%]"   {weather="locationId=home, type=clouds, property=percent"}

// condition
String   Condition        "Condition [%s]"      {weather="locationId=home, type=condition, property=text"}
String   Condition_ID     "Condition id [%s]"   {weather="locationId=home, type=condition, property=id"}
DateTime ObservationTime  "Observation time [%1$td.%1$tm.%1$tY %1$tH:%1$tM]"   {weather="locationId=home, type=condition, property=observationTime"}
DateTime LastUpdate       "Last update [%1$td.%1$tm.%1$tY %1$tH:%1$tM]"        {weather="locationId=home, type=condition, property=lastUpdate"}
String   CommonId         "Common id [%s]"      {weather="locationId=home, type=condition, property=commonId"}

// precipitation
Number   Rain          "Rain [%.2f mm/h]"   {weather="locationId=home, type=precipitation, property=rain"}
Number   Rain_Inches   "Rain [%.2f in/h]"   {weather="locationId=home, type=precipitation, property=rain, unit=inches"}
Number   Snow          "Snow [%.2f mm/h]"   {weather="locationId=home, type=precipitation, property=snow"}
Number   Snow_Inches   "Snow [%.2f in/h]"   {weather="locationId=home, type=precipitation, property=snow, unit=inches"}
Number   Precip_Probability   "Precip probability [%d %%]"   {weather="locationId=home, type=precipitation, property=probability"}
// new total property in 1.8, only Wunderground
Number   Precip_Total         "Precip total [%d mm]"   {weather="locationId=home, type=precipitation, property=total"}
Number   Precip_Total_Inches  "Precip total [%d in]"   {weather="locationId=home, type=precipitation, property=total, unit=inches"}

// temperature
Number   Temperature      "Temperature [%.2f °C]"       {weather="locationId=home, type=temperature, property=current"}
Number   Temperature_F    "Temperature [%.2f °F]"       {weather="locationId=home, type=temperature, property=current, unit=fahrenheit"}
Number   Temp_Feel        "Temperature feel [%.2f °C]"  {weather="locationId=home, type=temperature, property=feel"}
Number   Temp_Feel_F      "Temperature feel [%.2f °F]"  {weather="locationId=home, type=temperature, property=feel, unit=fahrenheit"}
Number   Temp_Dewpoint    "Dewpoint [%.2f °C]"          {weather="locationId=home, type=temperature, property=dewpoint"}
Number   Temp_Dewpoint_F  "Dewpoint [%.2f °F]"          {weather="locationId=home, type=temperature, 
property=dewpoint, unit=fahrenheit"}
// min and max values only available in forecasts
Number   Temp_Min         "Temperature min [%.2f °C]"   {weather="locationId=home, type=temperature, property=min"}
Number   Temp_Min_F       "Temperature min [%.2f °F]"   {weather="locationId=home, type=temperature, property=min, unit=fahrenheit"}
Number   Temp_Max         "Temperature max [%.2f °C]"   {weather="locationId=home, type=temperature, property=max"}
Number   Temp_Max_F       "Temperature max [%.2f °F]"   {weather="locationId=home, type=temperature, property=max, unit=fahrenheit"}
String   Temp_MinMax      "Min/Max [%s °C]"             {weather="locationId=home, type=temperature, property=minMax"}
String   Temp_MinMax_F    "Min/Max [%s °F]"             {weather="locationId=home, type=temperature, property=minMax, unit=fahrenheit"}

// wind
Number   Wind_Speed           "Windspeed [%.2f km/h]"    {weather="locationId=home, type=wind, property=speed"}
Number   Wind_Speed_Beaufort  "Windspeed Beaufort [%d]"  {weather="locationId=home, type=wind, property=speed, unit=beaufort"}
Number   Wind_Speed_Knots     "Windspeed [%.2f kn]"      {weather="locationId=home, type=wind, property=speed, unit=knots"}
Number   Wind_Speed_Mps       "Windspeed [%.2f mps]"     {weather="locationId=home, type=wind, property=speed, unit=mps"}
Number   Wind_Speed_Mph       "Windspeed [%.2f mph]"     {weather="locationId=home, type=wind, property=speed, unit=mph"}
String	 Wind_Direction       "Wind direction [%s]"      {weather="locationId=home, type=wind, property=direction"}
Number   Wind_Degree          "Wind degree [%.0f °]"     {weather="locationId=home, type=wind, property=degree"}
Number   Wind_Gust            "Wind gust [%.2f km/h]"    {weather="locationId=home, type=wind, property=gust"}
Number   Wind_Gust_Beaufort   "Wind gust Beaufort [%d]"  {weather="locationId=home, type=wind, property=gust, unit=beaufort"}
Number   Wind_Gust_Knots      "Wind gust [%.2f kn]"      {weather="locationId=home, type=wind, property=gust, unit=knots"}
Number   Wind_Gust_Mps        "Wind gust [%.2f mps]"     {weather="locationId=home, type=wind, property=gust, unit=mps"}
Number   Wind_Gust_Mph        "Wind gust [%.2f mph]"     {weather="locationId=home, type=wind, property=gust, unit=mph"}
Number   Wind_Chill           "Wind chill [%.2f °C]"     {weather="locationId=home, type=wind, property=chill"}
Number   Wind_Chill_F         "Wind chill [%.2f °F]"     {weather="locationId=home, type=wind, property=chill, unit=fahrenheit"}

// weather station (only Wunderground and Hamweather), needs version 1.7 or greater of the binding
String   Station_Name         "Station Name [%s]"        {weather="locationId=home, type=station, property=name"}
String   Station_Id           "Station Id [%s]"          {weather="locationId=home, type=station, property=id"}
Number   Station_Latitude     "Station Latitude [%.6f]"  {weather="locationId=home, type=station, property=latitude, scale=6"}
Number   Station_Longitude    "Station Longitude [%.6f]" {weather="locationId=home, type=station, property=longitude, scale=6"}

```
### Forecast
All bindings can also be used for forecasts. You only have to add the forecast day in the item.  
Display tomorrows min and max temperature forecast:
```
Number   Temp_Min         "Temperature min [%.2f °C]"   {weather="locationId=home, forecast=1, type=temperature, property=min"}
Number   Temp_Max         "Temperature max [%.2f °C]"   {weather="locationId=home, forecast=1, type=temperature, property=max"}
```
0 = todays forecast  
1 = tomorrows forecast  
and so on  

Each provider sends different forecast days. 
- ForecastIo: 8 days (0-7)
- OpenWeatherMap: 5 days (0-4)
- WorldWeatherOnline: 5 days (0-4)
- Wunderground: 10 days (0-9)
- Hamweather: 5 days (0-4)
- Yahoo: 10 days (0-9)

**Note:** If you omit the forecast property, the *current* conditions are shown, if you specify forecast=0, the forecast for *today* is shown.

### Data accuracy
It highly depends on your location and which weather provider you choose. Some providers updates the data in realtime, others only once in a hour. You have to test yourself and find the best provider for your location.

### Common Id
The common id property `(locationId=..., type=condition, property=commonId)` is an attempt to have a unique weather id for all providers. This is useful for displaying weather icons and a short condition text message. The documentation from the different weather providers are partially poor, hence the mapping is partially a guess and needs to be optimized with your help. If you think the commonId/weather icon is wrong, just [contact me](mailto:gerrieg.openhab@icloud.com).  
If you want to see the current mapping, you can open the file `common-id-mappings.xml` in the binding jar.

Example to use a common condition text message:
```
String   CommonCondition   "[MAP(weather_en.map):%s]"   {weather="locationId=..., type=condition, property=commonId"}
```
Example map files for english and german are in the [download section](#download).

### Html Layouts
The binding provides a url to serve highly customizable html layouts for displaying weather data and icons. You have to add some folders/files to the openhab webapps folder:  
![](https://farm6.staticflickr.com/5602/14973200854_0b374490a5_o.png)

The weather-data folder is the root, the images folder contains the different iconsets with 32 weather icons. In the layouts folder are the layout html files. You can add as many html layout files you want. You can use tokens to map weather data into the html layout.

**Tokens:**
* Weather: `${weather:TYPE.PROPERTY(FORMATTER)}`
* Forecast: `${forecast(DAY):TYPE.PROPERTY(FORMATTER)}`
* LocationConfig: `${config:CONFIG_PROPERTY}`
* RequestParameter: `${param:PARAMETER_NAME}`

**Example:**
```
// locationConfig data from openhab.cfg
${config:name}
${config:latitude}
${config:longitude}

// weather and forecast data directly from the weather objects
${weather:atmosphere.humidity}
${weather:temperature.current}
${weather:temperature.current(%.1f)}
${weather:condition.observationTime(%1$td.%1$tm.%1$tY %1$tH:%1$™)}
${forecast(0):temperature.minMax(%.0f)}

// request parameter
${param:iconset}
```

**Unit conversion**  
this PR required: https://github.com/openhab/openhab/pull/3385
```
${weather:temperature.current[fahrenheit]} 
${weather:temperature.current(%.1f)[fahrenheit]} 
${weather:wind.speed[mph]} 
```

See `example.html` in the layouts folder. You can download the weather-data folder in the [download section](#download).

**Using the layout file:**  
You must specify a locationId and a layout parameter, iconset is optional (default=colorful)  
Either directly:  
`http://HOST:PORT/weather?locationId=home&layout=example&iconset=colorful`  
or in a sitemap:  
`Webview url="/weather?locationId=home&layout=example&iconset=colorful" height=7`

(This does not work for OpenHAB2 and this WIKI needs an update for how to use this on OH2)

Note: The binding works with openHab 1.5.x too, for the Webview in a sitemap you need 1.6

### Html Layout Gallery
I would like to create a weather layout gallery. If you have a great looking html weather layout you want it to share, just [send it to me](mailto:gerrieg.openhab@icloud.com) an i will put it on this wiki page.

### Debugging and Tracing
If you want to see which data a provider sends, switch the loglevel in logback.xml to DEBUG.
```
<logger name="org.openhab.binding.weather" level="DEBUG" />
```
If you want to see even more, switch to TRACE to also see the communication with the provider.
```
<logger name="org.openhab.binding.weather" level="TRACE" />
```

### Troubleshooting
I assume, the binding is in your addons folder.

* In the openHAB logfile you must see for each configured apikey one line with  
```ProviderConfig[providerName=xx, apiKey=xxx]```  
and for each configured location  
```LocationConfig[providerName=xxx, language=xx, updateInterval=xx, latitude=xx.xxxx, longitude=xx.xxxx, woeid=xxx, locationId=xxx, name=xxx]```
If these entries do not exist, there is a problem in your openhab.cfg. A common problem is a space in front of the config properties.

* If the items are still not populated, switch the binding to [DEBUG mode](#debugging-and-tracing) and start openHab. Now you should see for every weather item a entry in your logfile:  
```Adding item ... with WeatherBindingConfig[locationId=..., type=..., property=..., roundingMode=..., scale=...]```  
If you don't see these entries, check your item file.

* As mentioned earlier, every weather provider sends different data and not all available binding properties are set. If you want to know which provider sends which properties to optimize your items, switch the binding to [DEBUG mode](#debugging-and-tracing). At every refresh the weather data is logged to the logfile.

* important note if you're installing from a debian based distribution: openhab-addon-action-weather and openhab-addon-binding-weather are not compatible with each other and will silently fail (at least since 1.7.1). If you made sure your openhab.cfg and your items are fine but you still don't see any `ProviderConfig` lines you might wan't to check the installed openhab addons.

### Download
[weather-data](https://drive.google.com/file/d/0Bw7zjCgsXYnHWmV6cHRwWnhjSFU/view?usp=sharing) with icons and example layout file  
[weather-data 1.8](https://drive.google.com/file/d/0Bw7zjCgsXYnHQTlGcndMR19DSUk/view?usp=sharing) with icons and example layout file **for openHab 1.8**  
[map-files](https://drive.google.com/file/d/0Bw7zjCgsXYnHbnVIdkFGaVZIbHM/view?usp=sharing) with short condition text **for the commonId property** 