# Weather Binding

The Weather binding collects current and forecast weather data from different providers with a free weather API. You can also display weather data with highly customizable html layouts and icons.

![](https://farm4.staticflickr.com/3946/15407522168_7ea34d51e1_o.png)

There is also a binding specifically openHAB 2 [here](https://www.openhab.org/addons/bindings/yahooweather/) for Yahoo! Weather.

## Table of Contents

<!-- MarkdownTOC -->

- [Binding Configuration](#binding-configuration)
	- [API Keys for Weather Providers](#api-keys-for-weather-providers)
	- [Location Configuration](#location-configuration)
	- [Configuration Example](#configuration-example)
- [Item Configuration](#item-configuration)
	- [Number formatting](#number-formatting)
	- [Units](#units)
	- [Unit conversion](#unit-conversion)
	- [Examples](#examples)
	- [Forecast](#forecast)
	- [Data accuracy](#data-accuracy)
	- [Common Id](#common-id)
- [Html Layouts](#html-layouts)
	- [HTML Layout Gallery](#html-layout-gallery)
- [Debugging and Tracing](#debugging-and-tracing)
- [Troubleshooting](#troubleshooting)
- [Downloads](#downloads)

<!-- /MarkdownTOC -->


## Binding Configuration

The binding can be configured in the file `services/weather.cfg`.

### API Keys for Weather Providers

Before you can use a weather provider, you need to register a free apikey on the website of the provider.  

The apikey for the different weather providers, at least one must be specified.

> [Hamweather](http://hamweather.com) has two apikeys (client_id, secret_id).
> [Yahoo](https://weather.yahoo.com) does not need an apikey.

| Property                  | Description |
|---------------------------|-------------|
| apikey.ForecastIo         | API key for [ForecastIo](http://forecast.io) |
| apikey.OpenWeatherMap     | API key for [OpenWeatherMap](http://openweathermap.org) |
| apikey.WorldWeatherOnline | API key for [WorldWeatherOnline](http://worldweatheronline.com) |
| apikey.Wunderground       | API key for [Wunderground](http://wunderground.com) |
| apikey.Hamweather         | `client_id` for [Hamweather](http://hamweather.com) |
| apikey2.Hamweather        | `client_secret` for [Hamweather](http://hamweather.com) |
| apikey.Meteoblue          | API key for [MeteoBlue](https://www.meteoblue.com/) |

### Location Configuration

Now you can specify your location(s). Each location has a `<locationId>` that can be referenced from an item.

You can specify multiple locations by repeating these properties with different values for `<locationId>`.

| Property                               | Description |
|----------------------------------------|-------------|
| location.`<locationId>`.name           | the name of the location, useful for displaying in html layouts (optional) |
| location.`<locationId>`.latitude       | the latitude the weather is retrieved from (not required for Yahoo) |
| location.`<locationId>`.longitude      | the longitude the weather is retrieved from (not required for Yahoo) |
| location.`<locationId>`.woeid          | required for Yahoo, the numeric Where On Earth ID, found at end of your weather.yahoo.com URL |
| location.`<locationId>`.provider       | reference to a provider name |
| location.`<locationId>`.language       | the language of the weather condition text (see provider homepage for supported languages) |
| location.`<locationId>`.updateInterval | the interval in minutes the weather is retrieved |
| location.`<locationId>`.units          | whether to use metric (SI) or imperial (US) values; may not be supported by all providers

**Important:** Each weather provider has a daily request limit for the free weather API. Also the weather does not change quickly, so please choose a moderate updateInterval. The request limit can be found on the weather provider website. 

### Configuration Example

Let's display the current temperature and humidity in Salzburg (AT) from Yahoo.  

services/weather.cfg

```
location.home.woeid=547826
location.home.provider=Yahoo
location.home.language=de
location.home.updateInterval=10
```

yourweather.items

```
Number   Temperature   "Temperature [%.2f °C]"   {weather="locationId=home, type=temperature, property=current"}
Number   Humidity      "Humidity [%d %%]"        {weather="locationId=home, type=atmosphere, property=humidity"}
```

For Yahoo, you don't need an apikey, but you do need a woeid (which you can find as the numeric digits at the end of your weather.yahoo.com URL). The location has the locationId *home* and updates the weather data every 10 minutes.  

In the items file, you reference the `<locationId>` and the type and property to display (see below for more).

Let's say you want to switch to another provider, ForecastIo. All you have to do is register your apikey, configure it, supply your latitude and longitude, and change the provider in your location:  

The apikeys below are not registered and are for example purposes only.

services/weather.cfg 

```
apikey.ForecastIo=sdf7g69fdgdfg679dfg69sdgkj
location.home.latitude=47.8011
location.home.longitude=13.0448
location.home.provider=ForecastIo
```

Now the weather data is retrieved from ForecastIo, your item file does not need to be changed! Let's say you want to have the current temperature from ForecastIo and the humidity from OpenWeatherMap.  

services/weather.cfg

```
apikey.ForecastIo=sdf7g69fdgdfg679dfg69sdgkj
apikey.OpenWeatherMap=766967gdfgdfgs9g76dsfg5ds76g521

location.home-FIO.latitude=47.8011
location.home-FIO.longitude=13.0448
location.home-FIO.provider=ForecastIo
location.home-FIO.language=de
location.home-FIO.updateInterval=10

location.home-OWM.latitude=47.8011
location.home-OWM.longitude=13.0448
location.home-OWM.provider=OpenWeatherMap
location.home-OWM.language=de
location.home-OWM.updateInterval=10
```

yourweather.items

```
Number   Temperature   "Temperature [%.2f °C]"   {weather="locationId=home-FIO, type=temperature, property=current"}
Number   Humidity      "Humidity [%d %%]"        {weather="locationId=home-OWM, type=atmosphere, property=humidity"}
```

Or you want to see the current temperature if there is a difference between providers and which one gives the best result for your location. This can be done with all available type/properties of course.

```
Number   Temperature_FIO   "Temperature-FIO [%.2f °C]"   {weather="locationId=home-FIO, type=temperature, property=current"}
Number   Temperature_OWM   "Temperature-OWM [%.2f °C]"   {weather="locationId=home-OWM, type=temperature, property=current"}
```

## Item Configuration

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

### Units

Some providers (eg. ForecastIO) allow passing a units parameter in method calls in order to specify which units (metric (SI) or imperial (US)) should be used by returned data.  Not all providers support this, however.

Starting with the 1.10.0 release of the binding, the configuration contains a new setting to allow this to be user configurable:

    #location.<locationId1>.units=

The values used for this setting will depend on the provider being used. As an example, for ForecastIO, this allows retrieving the current conditions in either `si` (metric) or `us` (imperial) units:

    Light rain on Sunday through Wednesday, with temperatures bottoming out at 29°C on Monday.
    Light rain on Sunday through Wednesday, with temperatures bottoming out at 84°F on Monday.

### Unit conversion

The default units are:

* speed: kilometer per hour
* temperature: celsius
* precipitation: rain in millimeters and snow in centimeter
* pressure: millibar

You can convert numeric values to other units with the `unit` parameter. Example to convert the temperature from Celsius to Fahrenheit:

```
Number   Temperature_F    "Temperature [%.2f °F]"   {weather="locationId=home, type=temperature, property=current, unit=fahrenheit"}
``` 

All possible conversions can be found in the following Items section.

### Examples

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

0 = todays forecast, 1 = tomorrows forecast, and so on.

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

## Html Layouts

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

```
${weather:temperature.current[fahrenheit]} 
${weather:temperature.current(%.1f)[fahrenheit]} 
${weather:wind.speed[mph]} 
```

See `example.html` in the layouts folder. You can download the weather-data folder in the [download section](#download).

**Using the layout file:**  

You must specify a locationId and a layout parameter, iconset is optional (default=colorful)  
Either directly:  

```
http://HOST:PORT/weather?locationId=home&layout=example&iconset=colorful
```

or in a sitemap:  

```
Webview url="/weather?locationId=home&layout=example&iconset=colorful" height=7
```

(This does not work for OpenHAB2 and this page needs an update for how to use this on OH2)

### HTML Layout Gallery

I would like to create a weather layout gallery. If you have a great looking html weather layout you want it to share, just [send it to me](mailto:gerrieg.openhab@icloud.com) an i will put it on this wiki page.

## Debugging and Tracing

The root logger for this binding is `org.openhab.binding.weather`. If you want to see which data a provider sends, set the level to DEBUG.

If you want to see even more, set the level to `TRACE` to also see the communication with the provider.

## Troubleshooting

I assume, the binding is installed.

* In the openHAB logfile you must see for each configured apikey one line with  

```
ProviderConfig[providerName=xx, apiKey=xxx]
```  

and for each configured location  

```
LocationConfig[providerName=xxx, language=xx, updateInterval=xx, latitude=xx.xxxx, longitude=xx.xxxx, woeid=xxx, locationId=xxx, name=xxx]
```

If these entries do not exist, there is a problem in your weather.cfg. A common problem is a space in front of the config properties.

* If the items are still not populated, switch the binding to [DEBUG mode](#debugging-and-tracing) and start openHab. Now you should see for every weather item a entry in your logfile:

```
Adding item ... with WeatherBindingConfig[locationId=..., type=..., property=..., roundingMode=..., scale=...]
```

If you don't see these entries, check your items file.

* As mentioned earlier, every weather provider sends different data and not all available binding properties are set. If you want to know which provider sends which properties to optimize your items, switch the binding to [DEBUG mode](#debugging-and-tracing). At every refresh the weather data is logged to the logfile.

* important note if you're installing from a debian based distribution: openhab-addon-action-weather and openhab-addon-binding-weather are not compatible with each other and will silently fail (at least since 1.7.1). If you made sure your openhab.cfg and your items are fine but you still don't see any `ProviderConfig` lines you might wan't to check the installed openhab addons.

## Downloads

* [weather-data](https://drive.google.com/file/d/0Bw7zjCgsXYnHQTlGcndMR19DSUk/view?usp=sharing) with icons and example layout file 
* [map-files](https://drive.google.com/file/d/0Bw7zjCgsXYnHbnVIdkFGaVZIbHM/view?usp=sharing) with short condition text **for the commonId property** 
