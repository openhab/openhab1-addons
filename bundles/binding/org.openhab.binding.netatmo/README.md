# Netatmo Binding

The Netatmo binding integrates the Netatmo Personal Weather Station into openHAB. Its different modules allow you to measure temperature, humidity, air pressure, carbon dioxide concentration in the air, as well as the ambient noise level.

The Netatmo Welcome Camera is also supported; it is a home camera with face recognition. It notifies you when it sees someone it knows, but also when it sees a stranger. See information below to obtain the necessary informations to setup your Netatmo Gear, also see [the Netatmo website](http://www.netatmo.com/) for details on their products.

There is also a binding specifically for openHAB 2 [here](https://www.openhab.org/addons/bindings/netatmo/).

> The Netatmo Binding (1.x) is considered a legacy binding and does not show up by default as an add-on in the OpenHAB 2 Paper UI. In order for the binding to show up as a add-on, you need to go to Configuration->System and enable both **Access Remote Repositories** and **Include Legacy 1.x Bindings** and save the setting. Once you have done this, you will see both **Netatmo Binding** and **Netatmo Binding (1.x)** in Add-ons, you will want to install **Netatmo Binding**.

## Prerequisites

To make the binding work an [OAuth2](http://oauth.net/2/) authorization has to be performed by hand to let openHAB connect to your Netatmo devices. The following is a step by step guide to do so.

A note on the notation: Variable are written like this `<VARIABLE_NAME>` when replacing the variable, replace the `<` and `>` as well. E.g. Assuming `<CLIENT_ID>` is 1234 then,

```
https://api.netatmo.com/oauth2/authorize?client_id=<CLIENT_ID>
```

should be replaced with

```
https://api.netatmo.com/oauth2/authorize?client_id=1234
```

### 1. Application creation

Create an application at https://dev.netatmo.com/dev/createapp .

### 2. Authorize your Application

[Authorize the application](https://dev.netatmo.com/doc/authentication/authcode) by visiting the following URL in your Browser.

```
https://api.netatmo.com/oauth2/authorize?client_id=<CLIENT_ID>&redirect_uri=http%3Alocalhost%3A8080%2Ftest%2F&scope=<SCOPE>&state=42"
```

The variables to fill in are:

* `<CLIENT_ID>` Your client ID taken from your App at https://dev.netatmo.com/dev/listapps
* `<SCOPE>` A list of devices and capabilities. The full scope would be `read_station read_thermostat write_thermostat read_camera access_camera`, see the [Netatmo Scope Documentation](https://dev.netatmo.com/doc/authentication/scopes) for more info. Please ensure that the variable is [URL encoded](http://www.w3schools.com/tags/ref_urlencode.asp) if your browser doesn't do it automatically, e.g. the URL encoded complete list of scopes would be `read_station%20read_thermostat%20write_thermostat%20read_camera%20access_camera`

When entering the URL to your browser it will take you to the Netatmo Webpage asking you to authorize your application to access your Netatmo data. The page should look something like [this](https://dev.netatmo.com/images/dev/auth_app.jpg). After this step your Application should appear as an authorized application in your Netatmo profile. Please make sure it is there, the setup cannot continue if your app is not authorized.

### 3. Retrieve a refresh token
[Retrieve a refresh token](https://dev.netatmo.com/doc/authentication/refreshtoken) from Netatmo API, using e.g. curl. 

```
curl -d 'grant_type=password&client_id=<CLIENT_ID>&client_secret=<CLIENT_SECRET>&username=<USERNAME>&password=<PASSWORD>&scope=<SCOPE>' 'https://api.netatmo.net/oauth2/token'
```

The variables to fill in are:

* `<CLIENT_ID>` Same as before
* `<CLIENT_SECRET>` Your client secret taken from your App at https://dev.netatmo.com/dev/listapps
* `<USERNAME>` Your Netatmo username
* `<PASSWORD>` Your Netatmo password
* `<SCOPE>` Same as before.

A successful response looks like this:

```JSON
{
    "access_token":"2YotnFZFEjr1zCsicMWpAA",
    "expires_in":10800,
    "refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA",
}
```

## Binding Configuration

Add client id, client secret and refresh token to the file `services/netatmo.cfg`.

```
clientid=<CLIENT_ID>
clientsecret=<CLIENT_SECRET>
refreshtoken=<REFRESH_TOKEN>
```

The variables to fill in are:

* `<CLIENT_ID>` Same as before
* `<CLIENT_SECRET>` Same as before
* `<REFRESH_TOKEN>` The refresh token from the previous response

## Measurement Units

You can optionally set the unit system and/or pressure units. The unit systems options are either Metric (Celsius/meters/millimeters) or US (Fahrenheit/feet/inches) and is case insensitive. The  temperature (Celsius or Fahrenheit), rain (millimeter or inches) and altitude (meters or feet) are affected by this parameter. If not specified, openHAB defaults to Metric.

```
unitsystem=m
unitsystem=us
```

The pressure unit is either mbar, inHg, or mmHg and is used for pressure, and is case insenstive. If not specified, openHAB defaults to mbar.

```
pressureunit=mbar
pressureunit=inHg
pressureunit=mmHg
```

## Items and Rules Configuration

The IDs for the modules can be extracted from the developer documentation on the netatmo site.  First login with your user. Then some examples of the documentation contain the **real results** of your weather station. Get the IDs of your devices (indoor, outdoor, rain gauge) here:

```
https://dev.netatmo.com/doc/methods/devicelist
```

main_device is the ID of the "main device", the indoor sensor. This is equal to the MAC address of the Netatmo.

The other modules you can recognize by "module_name" and then note the "_id" which you need later.

### Another way to get the IDs is to calculate them

You have to calculate the ID for the outside module as follows: (it cannot be read from the app)
if the first serial character is "h":  start with "02",
if the first serial character is "i": start with "03",

append ":00:00:",

split the rest into three parts of two characters and append with a colon as delimeter.

For example your serial number "h00bcdc" should end up as "02:00:00:00:bc:dc".

### Indoor

#### Example items for the **indoor module**:

```
Number Netatmo_Indoor_CO2 "Carbon dioxide [%d ppm]" {netatmo="00:00:00:00:00:00#Co2"}
// Since 1.9 also with the keyword weather=
Number Netatmo_Indoor_CO2 "Carbon dioxide [%d ppm]" {netatmo="weather=00:00:00:00:00:00#Co2"}
```

#### Supported types for the indoor module

* Temperature
* Humidity
* Co2
* Pressure
* Noise
* WifiStatus
* Altitude
* Latitude
* Longitude
* TimeStamp
* min_temp
* date_min_temp
* max_temp
* date_max_temp
* min_hum
* date_min_hum
* max_hum
* date_max_hum
* min_pressure
* date_min_pressure
* max_pressure
* date_max_pressure
* min_noise
* date_min_noise
* max_noise
* date_max_noise
* min_co2
* date_min_co2
* max_co2
* date_max_co2

### Outdoor

#### Example item for the **outdoor module** (first id is the main module, second id is the outdoor module):

```
Number Netatmo_Outdoor_Temperature "Outdoor temperature [%.1f °C]" {netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Temperature"}
// Since 1.9 also with the keyword weather=
Number Netatmo_Outdoor_Temperature "Outdoor temperature [%.1f °C]" {netatmo="weather=00:00:00:00:00:00#00:00:00:00:00:00#Temperature"}
```

### Supported types for the outdoor module

* Temperature
* Humidity
* RfStatus
* BatteryVP
* TimeStamp
* min_temp
* date_min_temp
* max_temp
* date_max_temp
* min_hum
* date_min_hum
* max_hum
* date_max_hum

### Rain

#### Example item for the **rain gauge** (first id is the main module, second id is the rain module):

```
Number Netatmo_Rain_Current "Rain [%.1f mm]" {netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Rain"}
// Since 1.9 also with the keyword weather=
Number Netatmo_Rain_Current "Rain [%.1f mm]" {netatmo="weather=00:00:00:00:00:00#00:00:00:00:00:00#Rain"}
```

#### Supported types for the rain guage

* Rain
* Humidity
* sum_rain
* RfStatus
* BatteryVP

### Wind

#### Example item for the **wind module** (first id is the main module, second id is the wind module):

```
Number Netatmo_Wind_Strength "Wind Strength [%.0f KPH]" {netatmo="00:00:00:00:00:00#00:00:00:00:00:00#WindStrength"}
// Since 1.9 also with the keyword weather=
Number Netatmo_Wind_Strength "Wind Strength [%.0f KPH]" {netatmo="weather=00:00:00:00:00:00#00:00:00:00:00:00#WindStrength"}
```

#### Supported types for the wind module

* WindStrength
* WindAngle
* GustStrength
* GustAngle
* date_max_gust
* RfStatus
* BatteryVP

Types **WindStrength**, **WindAngle**, **GustStrength**, **GustAngle** are for a specific time frame, and default to the latest measurement. 

Other possible time frames are:

* 30min
* 1hour
* 3hours
* 1day
* 1week
* 1month

The type **date_max_gust** is also for a specific time frame, and defaults to 1 day. It will only work with the ranges **1day**, **1week**, and **1month**.

Example items with different time frames:

```
Number   Netatmo_Wind_Strength_Current        "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindStrength"}
Number   Netatmo_Wind_Wind_Angle_Current      "Wind Angle [%d°]"             (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindAngle"}
Number   Netatmo_Wind_Gust_Strength_Current   "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustStrength"}
Number   Netatmo_Wind_Gust_Angle_Current      "Wind Angle [%d°]"             (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustAngle"}

Number   Netatmo_Wind_Strength_Today          "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindStrength,1day"}
Number   Netatmo_Wind_Wind_Angle_Today        "Wind Angle [%d°]"             (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindAngle,1day"}
Number   Netatmo_Wind_Gust_Strength_Today     "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustStrength,1day"}
Number   Netatmo_Wind_Gust_Angle_Today        "Wind Angle [%d°]"             (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustAngle,1day"}
DateTime Netatmo_Wind_Max_Gust_Date_Today     "Date Max Gust [%1$tD %1$tr]"  (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#date_max_gust"}

Number   Netatmo_Wind_Strength_Month          "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindStrength,1month"}
Number   Netatmo_Wind_Wind_Angle_Month        "Wind Angle [%d°]"             (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindAngle,1month"}
Number   Netatmo_Wind_Gust_Strength_Month     "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustStrength,1month"}
Number   Netatmo_Wind_Gust_Angle_Month        "Wind Angle [%d°]"             (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustAngle,1month"}
DateTime Netatmo_Wind_Max_Gust_Date_Month     "Date Max Gust [%1$tD %1$tr]"  (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#date_max_gust,1month"}

// Since 1.9 also with the keyword weather=

Number   Netatmo_Wind_Strength_Current        "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindStrength"}
Number   Netatmo_Wind_Wind_Angle_Current      "Wind Angle [%d°]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindAngle"}
Number   Netatmo_Wind_Gust_Strength_Current   "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustStrength"}
Number   Netatmo_Wind_Gust_Angle_Current      "Wind Angle [%d°]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustAngle"}

Number   Netatmo_Wind_Strength_Today          "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindStrength,1day"}
Number   Netatmo_Wind_Wind_Angle_Today        "Wind Angle [%d°]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindAngle,1day"}
Number   Netatmo_Wind_Gust_Strength_Today     "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustStrength,1day"}
Number   Netatmo_Wind_Gust_Angle_Today        "Wind Angle [%d°]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustAngle,1day"}
DateTime Netatmo_Wind_Max_Gust_Date_Today     "Date Max Gust [%1$tD %1$tr]"  (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#date_max_gust"}

Number   Netatmo_Wind_Strength_Month          "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindStrength,1month"}
Number   Netatmo_Wind_Wind_Angle_Month        "Wind Angle [%d°]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindAngle,1month"}
Number   Netatmo_Wind_Gust_Strength_Month     "Wind Strength [%.0f KPH]"     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustStrength,1month"}
Number   Netatmo_Wind_Gust_Angle_Month        "Wind Angle [%d°]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustAngle,1month"}
DateTime Netatmo_Wind_Max_Gust_Date_Month     "Date Max Gust [%1$tD %1$tr]"  (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#date_max_gust,1month"}
```

### Min, Max and Sum Types

Types that contain **_min**, **_max**, or **_sum** are for a specific time frame, with a default of 1 day. Possible time frames are:

* 30min
* 1hour
* 3hours
* 1day
* 1week
* 1month

The types that contain **_date** will only work with the ranges **1day**, **1week**, and **1month**.

Example items with different time frames:

```
Number   Netatmo_Indoor_Max_Temp_Today       "Indoor Maximum Temperature Today [%.2f C]"      (Netatmo)  {netatmo="00:00:00:00:00:00#max_temp"}
Number   Netatmo_Indoor_Max_Temp_This_Week   "Indoor Maximum Temperature This Week [%.2f C]"  (Netatmo)  {netatmo="00:00:00:00:00:00#max_temp,1week"}
Number   Netatmo_Indoor_Max_Temp_This_Month  "Indoor Maximum Temperature This Month [%.2f C]" (Netatmo)  {netatmo="00:00:00:00:00:00#max_temp,1month"}
Number   Netatmo_Rain_Today                  "Rain Today [%.02f mm]"                          (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#sum_rain"}
Number   Netatmo_Rain_Week                   "Rain This Week [%.02f mm]"                      (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#sum_rain,1week""}
Number   Netatmo_Rain_Month                  "Rain This Month [%.02f mm]"                     (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#sum_rain,1month""}

// Since 1.9 also with the keyword weather=

Number   Netatmo_Indoor_Max_Temp_Today       "Indoor Maximum Temperature Today [%.2f C]"      (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_temp"}
Number   Netatmo_Indoor_Max_Temp_This_Week   "Indoor Maximum Temperature This Week [%.2f C]"  (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_temp,1week"}
Number   Netatmo_Indoor_Max_Temp_This_Month  "Indoor Maximum Temperature This Month [%.2f C]" (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_temp,1month"}
Number   Netatmo_Rain_Today                  "Rain Today [%.02f mm]"                          (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#sum_rain"}
Number   Netatmo_Rain_Week                   "Rain This Week [%.02f mm]"                      (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#sum_rain,1week""}
Number   Netatmo_Rain_Month                  "Rain This Month [%.02f mm]"                     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#sum_rain,1month""}
```
  
### Example Items

```
Number   Netatmo_Indoor_Temperature    "Indoor Temperature [%.2f C]"                       (Netatmo)  {netatmo="00:00:00:00:00:00#Temperature"}
Number   Netatmo_Indoor_Humidity       "Indoor Humidity [%d %%]"                           (Netatmo)  {netatmo="00:00:00:00:00:00#Humidity"}
Number   Netatmo_Indoor_CO2            "Indoor Carbon dioxide [%d ppm]"                    (Netatmo)  {netatmo="00:00:00:00:00:00#Co2"}
Number   Netatmo_Indoor_Pressure       "Indoor Pressure [%.2f mbar]"                       (Netatmo)  {netatmo="00:00:00:00:00:00#Pressure"}
Number   Netatmo_Indoor_Noise          "Indoor Noise [%d db]"                              (Netatmo)  {netatmo="00:00:00:00:00:00#Noise"}
Number   Netatmo_Indoor_wifi           "Indoor Wifi status [%d / 4]"                       (Netatmo)  {netatmo="00:00:00:00:00:00#Wifistatus"}
Number   Netatmo_Indoor_altitude       "Indoor Altitude [%f]"                              (Netatmo)  {netatmo="00:00:00:00:00:00#Altitude"}
Number   Netatmo_Indoor_latitude       "Indoor Latitude [%.6f]"                            (Netatmo)  {netatmo="00:00:00:00:00:00#Latitude"}
Number   Netatmo_Indoor_longitude      "Indoor Longitude [%.6f]"                           (Netatmo)  {netatmo="00:00:00:00:00:00#Longitude"}
Number   Netatmo_Indoor_Min_Temp       "Indoor Minimum Temperature Today [%.2f C]"         (Netatmo)  {netatmo="00:00:00:00:00:00#min_temp"}
DateTime Netatmo_Indoor_Min_Temp_Date  "Indoor Minimum Temperature Today [%1$tD %1$tr]"    (Netatmo)  {netatmo="00:00:00:00:00:00#date_min_temp"}
Number   Netatmo_Indoor_Max_Temp       "Indoor Maximum Temperature Today [%.2f C]"         (Netatmo)  {netatmo="00:00:00:00:00:00#max_temp"}
DateTime Netatmo_Indoor_Max_Temp_Date  "Indoor Maximum Temperature Today [%1$tD %1$tr]"    (Netatmo)  {netatmo="00:00:00:00:00:00#date_max_temp"}
Number   Netatmo_Indoor_Min_Hum        "Indoor Minimum Humidity Today [%d %%]"             (Netatmo)  {netatmo="00:00:00:00:00:00#min_hum"}
DateTime Netatmo_Indoor_Min_Hum_Date   "Indoor Minimum Humidity Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="00:00:00:00:00:00#date_min_hum"}
Number   Netatmo_Indoor_Max_Hum        "Indoor Maximum Humidity Today [%d %%]"             (Netatmo)  {netatmo="00:00:00:00:00:00#max_hum"}
DateTime Netatmo_Indoor_Max_Hum_Date   "Indoor Maximum Humidity Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="00:00:00:00:00:00#date_max_hum"}
Number   Netatmo_Indoor_Min_Press      "Indoor Minimum Pressure Today [%.2f mbar]"         (Netatmo)  {netatmo="00:00:00:00:00:00#min_pressure"}
DateTime Netatmo_Indoor_Min_Temp_Press "Indoor Minimum Pressure Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="00:00:00:00:00:00#date_min_pressure"}
Number   Netatmo_Indoor_Max_Press      "Indoor Maximum Pressure Today [%.2f mbar]"         (Netatmo)  {netatmo="00:00:00:00:00:00#max_pressure"}
DateTime Netatmo_Indoor_Max_Temp_Press "Indoor Maximum Pressure Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="00:00:00:00:00:00#date_max_pressure"}
Number   Netatmo_Indoor_Min_Noise      "Indoor Minimum Noise Today [%d db]"                (Netatmo)  {netatmo="00:00:00:00:00:00#min_noise"}
DateTime Netatmo_Indoor_Min_Noise_Date "Indoor Minimum Noise Today [%1$tD %1$tr]"          (Netatmo)  {netatmo="00:00:00:00:00:00#date_min_noise"}
Number   Netatmo_Indoor_Max_Noise      "Indoor Maximum Noise Today [%d db]"                (Netatmo)  {netatmo="00:00:00:00:00:00#max_noise"}
DateTime Netatmo_Indoor_Max_Noise_Date "Indoor Maximum Noise Today [%1$tD %1$tr]"          (Netatmo)  {netatmo="00:00:00:00:00:00#date_max_noise"}
Number   Netatmo_Indoor_Min_CO2        "Indoor Minimum Carbon Dioxide Today [%d ppm]"      (Netatmo)  {netatmo="00:00:00:00:00:00#min_co2"}
DateTime Netatmo_Indoor_Min_CO2_Date   "Indoor Minimum Carbon Dioxide Today [%1$tD %1$tr]" (Netatmo)  {netatmo="00:00:00:00:00:00#date_min_co2"}
Number   Netatmo_Indoor_Max_CO2        "Indoor Maxinum Carbon Dioxide Today [%d ppm]"      (Netatmo)  {netatmo="00:00:00:00:00:00#max_co2"}
DateTime Netatmo_Indoor_Max_CO2_Date   "Indoor Maximum Carbon Dioxide Today [%1$tD %1$tr]" (Netatmo)  {netatmo="00:00:00:00:00:00#date_max_co2"}

Number   Netatmo_Outdoor_Temperature   "Outdoor Temperature [%.2f °C]"                     (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#Temperature"}
Number   Netatmo_Outdoor_Humidity      "Outdoor Humidity [%.2f %%]"                        (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#Humidity"}
Number   Netatmo_Outdoor_Rfstatus      "Outdoor RF status [%d / 5]"                        (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#Rfstatus"}
Number   Netatmo_Outdoor_Batteryvp     "Outdoor battery status [%d %%]"                    (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#Batteryvp"}
Number   Netatmo_Outdoor_Min_Temp      "Outdoor Mininum Temperature Today [%.2f C]"        (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#min_temp"}
DateTime Netatmo_Outdoor_Min_Temp_Date "Outdoor Minimum Temperature Today [%1$tD %1$tr]"   (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#date_min_temp"}
Number   Netatmo_Outdoor_Max_Temp      "Outdoor Maximum Temperature Today [%.2f C]"        (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#max_temp"}
DateTime Netatmo_Outdoor_Max_Temp_Date "Outdoor Maximum Temperature Today [%1$tD %1$tr]"   (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#date_max_temp"}
Number   Netatmo_Outdoor_Min_Hum       "Outdoor Mininum Humidity Today [%d %%]"            (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#min_hum"}
DateTime Netatmo_Outdoor_Min_Hum_Date  "Outdoor Minimum Humidity Today [%1$tD %1$tr]"      (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#date_min_hum"}
Number   Netatmo_Outdoor_Max_Hum       "Outdoor Maximum Humidity Today [%d %%]"            (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#max_hum"}
DateTime Netatmo_Outdoor_Max_Hum_Date  "Outdoor Maximum Humidity Today [%1$tD %1$tr]"      (Netatmo)  {netatmo="00:00:00:00:00:00#02:00:00:00:00:00#date_max_hum"}

Number   Netatmo_Rain_Current          "Rain Current [%.02f mm]"                           (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#Rain"}
Number   Netatmo_Rain_Today            "Rain Today [%.02f mm]"                             (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#sum_rain"}
Number   Netatmo_Rain_Rfstatus         "Rain RF Status [%d / 5]"                           (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#Rfstatus"}
Number   Netatmo_Rain_Batteryvp        "Rain battery status [%d %%]"                       (Netatmo)  {netatmo="00:00:00:00:00:00#05:00:00:00:00:be#Batteryvp"}

Number   Netatmo_Wind_Strength         "Wind Strength [%.0f KPH]"                          (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindStrength"}
Number   Netatmo_Wind_Wind_Angle       "Wind Angle [%d°]"                                  (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#WindAngle"}
Number   Netatmo_Wind_Gust_Strength    "Wind Strength [%.0f KPH]"                          (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustStrength"}
Number   Netatmo_Wind_Gust_Angle       "Wind Angle [%d°]"                                  (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#GustAngle"}
DateTime Netatmo_Wind_Max_Gust_Date    "Date Max Gust [%1$tD %1$tr]"                       (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#date_max_gust"}
Number   Netatmo_Wind_Rfstatus         "Wind RF Status [%d / 5]"                           (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#Rfstatus"}
Number   Netatmo_Wind_Batteryvp        "Wind battery status [%d %%]"                       (Netatmo)  {netatmo="00:00:00:00:00:00#06:00:00:00:00:de#Batteryvp"}

// Since 1.9 also with the keyword weather=

Number   Netatmo_Indoor_Temperature    "Indoor Temperature [%.2f C]"                       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Temperature"}
Number   Netatmo_Indoor_Humidity       "Indoor Humidity [%d %%]"                           (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Humidity"}
Number   Netatmo_Indoor_CO2            "Indoor Carbon dioxide [%d ppm]"                    (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Co2"}
Number   Netatmo_Indoor_Pressure       "Indoor Pressure [%.2f mbar]"                       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Pressure"}
Number   Netatmo_Indoor_Noise          "Indoor Noise [%d db]"                              (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Noise"}
Number   Netatmo_Indoor_wifi           "Indoor Wifi status [%d / 4]"                       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Wifistatus"}
Number   Netatmo_Indoor_altitude       "Indoor Altitude [%f]"                              (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Altitude"}
Number   Netatmo_Indoor_latitude       "Indoor Latitude [%.6f]"                            (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Latitude"}
Number   Netatmo_Indoor_longitude      "Indoor Longitude [%.6f]"                           (Netatmo)  {netatmo="weather=00:00:00:00:00:00#Longitude"}
Number   Netatmo_Indoor_Min_Temp       "Indoor Minimum Temperature Today [%.2f C]"         (Netatmo)  {netatmo="weather=00:00:00:00:00:00#min_temp"}
DateTime Netatmo_Indoor_Min_Temp_Date  "Indoor Minimum Temperature Today [%1$tD %1$tr]"    (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_min_temp"}
Number   Netatmo_Indoor_Max_Temp       "Indoor Maximum Temperature Today [%.2f C]"         (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_temp"}
DateTime Netatmo_Indoor_Max_Temp_Date  "Indoor Maximum Temperature Today [%1$tD %1$tr]"    (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_max_temp"}
Number   Netatmo_Indoor_Min_Hum        "Indoor Minimum Humidity Today [%d %%]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#min_hum"}
DateTime Netatmo_Indoor_Min_Hum_Date   "Indoor Minimum Humidity Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_min_hum"}
Number   Netatmo_Indoor_Max_Hum        "Indoor Maximum Humidity Today [%d %%]"             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_hum"}
DateTime Netatmo_Indoor_Max_Hum_Date   "Indoor Maximum Humidity Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_max_hum"}
Number   Netatmo_Indoor_Min_Press      "Indoor Minimum Pressure Today [%.2f mbar]"         (Netatmo)  {netatmo="weather=00:00:00:00:00:00#min_pressure"}
DateTime Netatmo_Indoor_Min_Temp_Press "Indoor Minimum Pressure Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_min_pressure"}
Number   Netatmo_Indoor_Max_Press      "Indoor Maximum Pressure Today [%.2f mbar]"         (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_pressure"}
DateTime Netatmo_Indoor_Max_Temp_Press "Indoor Maximum Pressure Today [%1$tD %1$tr]"       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_max_pressure"}
Number   Netatmo_Indoor_Min_Noise      "Indoor Minimum Noise Today [%d db]"                (Netatmo)  {netatmo="weather=00:00:00:00:00:00#min_noise"}
DateTime Netatmo_Indoor_Min_Noise_Date "Indoor Minimum Noise Today [%1$tD %1$tr]"          (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_min_noise"}
Number   Netatmo_Indoor_Max_Noise      "Indoor Maximum Noise Today [%d db]"                (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_noise"}
DateTime Netatmo_Indoor_Max_Noise_Date "Indoor Maximum Noise Today [%1$tD %1$tr]"          (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_max_noise"}
Number   Netatmo_Indoor_Min_CO2        "Indoor Minimum Carbon Dioxide Today [%d ppm]"      (Netatmo)  {netatmo="weather=00:00:00:00:00:00#min_co2"}
DateTime Netatmo_Indoor_Min_CO2_Date   "Indoor Minimum Carbon Dioxide Today [%1$tD %1$tr]" (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_min_co2"}
Number   Netatmo_Indoor_Max_CO2        "Indoor Maxinum Carbon Dioxide Today [%d ppm]"      (Netatmo)  {netatmo="weather=00:00:00:00:00:00#max_co2"}
DateTime Netatmo_Indoor_Max_CO2_Date   "Indoor Maximum Carbon Dioxide Today [%1$tD %1$tr]" (Netatmo)  {netatmo="weather=00:00:00:00:00:00#date_max_co2"}

Number   Netatmo_Outdoor_Temperature   "Outdoor Temperature [%.2f °C]"                     (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#Temperature"}
Number   Netatmo_Outdoor_Humidity      "Outdoor Humidity [%.2f %%]"                        (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#Humidity"}
Number   Netatmo_Outdoor_Rfstatus      "Outdoor RF status [%d / 5]"                        (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#Rfstatus"}
Number   Netatmo_Outdoor_Batteryvp     "Outdoor battery status [%d %%]"                    (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#Batteryvp"}
Number   Netatmo_Outdoor_Min_Temp      "Outdoor Mininum Temperature Today [%.2f C]"        (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#min_temp"}
DateTime Netatmo_Outdoor_Min_Temp_Date "Outdoor Minimum Temperature Today [%1$tD %1$tr]"   (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#date_min_temp"}
Number   Netatmo_Outdoor_Max_Temp      "Outdoor Maximum Temperature Today [%.2f C]"        (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#max_temp"}
DateTime Netatmo_Outdoor_Max_Temp_Date "Outdoor Maximum Temperature Today [%1$tD %1$tr]"   (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#date_max_temp"}
Number   Netatmo_Outdoor_Min_Hum       "Outdoor Mininum Humidity Today [%d %%]"            (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#min_hum"}
DateTime Netatmo_Outdoor_Min_Hum_Date  "Outdoor Minimum Humidity Today [%1$tD %1$tr]"      (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#date_min_hum"}
Number   Netatmo_Outdoor_Max_Hum       "Outdoor Maximum Humidity Today [%d %%]"            (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#max_hum"}
DateTime Netatmo_Outdoor_Max_Hum_Date  "Outdoor Maximum Humidity Today [%1$tD %1$tr]"      (Netatmo)  {netatmo="weather=00:00:00:00:00:00#02:00:00:00:00:00#date_max_hum"}

Number   Netatmo_Rain_Current          "Rain Current [%.02f mm]"                           (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#Rain"}
Number   Netatmo_Rain_Today            "Rain Today [%.02f mm]"                             (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#sum_rain"}
Number   Netatmo_Rain_Rfstatus         "Rain RF Status [%d / 5]"                           (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#Rfstatus"}
Number   Netatmo_Rain_Batteryvp        "Rain battery status [%d %%]"                       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#05:00:00:00:00:be#Batteryvp"}

Number   Netatmo_Wind_Strength         "Wind Strength [%.0f KPH]"                          (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindStrength"}
Number   Netatmo_Wind_Wind_Angle       "Wind Angle [%d°]"                                  (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#WindAngle"}
Number   Netatmo_Wind_Gust_Strength    "Wind Strength [%.0f KPH]"                          (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustStrength"}
Number   Netatmo_Wind_Gust_Angle       "Wind Angle [%d°]"                                  (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#GustAngle"}
DateTime Netatmo_Wind_Max_Gust_Date    "Date Max Gust [%1$tD %1$tr]"                       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#date_max_gust"}
Number   Netatmo_Wind_Rfstatus         "Wind RF Status [%d / 5]"                           (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#Rfstatus"}
Number   Netatmo_Wind_Batteryvp        "Wind battery status [%d %%]"                       (Netatmo)  {netatmo="weather=00:00:00:00:00:00#06:00:00:00:00:de#Batteryvp"}
```

### Example Rules

#### Example rule to send a mail if carbon dioxide reaches a certain threshold

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

#### Example rule to send notifications if the module battery level is low

```
rule "Monitor Netatmo battery levels"
when
    Time cron "00 10 * * * ?" or
    System started
then
    if( Netatmo_Outdoor_Batteryvp.state < 20) {
        logInfo("NetatmoBatteryAlerts","Netatmo Outdoor Sensor Battery is low: " + Netatmo_Outdoor_Batteryvp.state + " Sending alert!")
        var String mailSubject = "Netatmo Outdoor Sensor Battery is low: " + Netatmo_Outdoor_Batteryvp.state + "!"
        sendMail("<MAIL>", mailSubject, mailSubject);
    }

    if( Netatmo_Rain_Batteryvp.state < 20) {
        logInfo("NetatmoBatteryAlerts","Netatmo Rain Battery is low: " + Netatmo_Rain_Batteryvp.state + " Sending alert!")
        var String mailSubject = "Netatmo Rain Gauge Battery is low: " + Netatmo_Rain_Batteryvp.state + "!"
        sendMail("<MAIL>", mailSubject, mailSubject);
    }
end
```

## Welcome Camera

## Setup

First, create your developer Account and App, as described above. The Steps are all the same, but for Welcome, you will need some more information (Home ID, Face ID etc) to get all the Input you need. There are two different ways to retrieve those infos: 

### Option 1

Obtain the Home ID from the Netatmo Web Interface (will only work with a PC or Mac and a browser that is displaying links on mouse over)

* Go to https://my.netatmo.com/app/camera
* Log in with your Netatmo Credentials
* Click on the Gear-Icon in the upper right corner
* In the next window hover your mouse pointer over one of the Options as Localisation or Timezone and check the preview address for the page (in Chrome in the lower left corner)
* Correct Addresses look like this: https://my.netatmo.com/settingscamera/localisation/000000000000000000000000
* The 000000000000000000000000-Part is your Home ID. Write it down, you will need this in the next steps

### Option 2

#### Obtain the Home ID from Shell

* Download and install this small Node.JS-Tool from shell: https://www.npmjs.com/package/netatmo
* After everything is set up, edit the test.js file and fill in the needed informations for the Module to run:
```
var auth = {
  "client_id": "",
  "client_secret": "",
  "username": "",
  "password": "",
};
```
* Then give it a go (nodejs test.js) and check for the outputs, your Home ID should be right there

#### Obtain Face ID, Camera ID, Event ID etc.

Now that you got your Home ID you can go to https://dev.netatmo.com/dev/resources/technical/reference/welcome/gethomedata and collect the other informations to setup your Netatmo items:

* In the developer area, click on the arrow the says "Try this method by yourself with our TRY IT module." in the right pane
* Fill in Home ID and size (300 for example) and click "Try IT"
* Below you will get the result in XML-like structure
* Click through the several subfolders to obtain everything you need

### Home

Example item for the **home** (first id is the home id):

```
String Welcome_Home                "Home [%s]"          {netatmo="camera=000000000000000000000000#Name"}
String Welcome_Home_Place_Country  "Home Country [%s]"  {netatmo="camera=000000000000000000000000#PlaceCountry"}
String Welcome_Home_Place_Timezone "Home Timezone [%s]" {netatmo="camera=000000000000000000000000#PlaceTimezone"}
```

#### Supported types for welcome camera home

* Name
* PlaceCountry
* PlaceTimezone

### Person

#### Example item for the person

First id is the home id, second is the person id:

```
String   Welcome_Person_Name       "Person Name [%s]"                                 {netatmo="camera=000000000000000000000000#00000000-0000-0000-0000-000000000000#Pseudo"}
DateTime Welcome_Person_LastSeen   "Person LastSeen [%1$ta, %1$td.%1$tm.%1$tY %1$tR]" {netatmo="camera=000000000000000000000000#00000000-0000-0000-0000-000000000000#LastSeen"}
Switch   Welcome_Person_AtHome     "Person [%s]"                                      {netatmo="camera=000000000000000000000000#00000000-0000-0000-0000-000000000000#OutOfSight"}
String   Welcome_Person_FaceId     "Person FaceId [%s]"                               {netatmo="camera=000000000000000000000000#00000000-0000-0000-0000-000000000000#FaceId"}
String   Welcome_Person_FaceKey    "Person FaceKey [%s]"                              {netatmo="camera=000000000000000000000000#00000000-0000-0000-0000-000000000000#FaceKey"}
```

#### Supported types for welcome camera person

* Pseudo
* LastSeen
* OutOfSight
* FaceId
* FaceKey

### Unknown Person

#### Example item for the unknown person

First id is the home id:

```
Number Welcome_Unknown_Home       "Unknown @Home Count [%d]"    {netatmo="camera=000000000000000000000000#UNKNOWN#HomeCount"}
Number Welcome_Unknown_Away       "Unknown @Away Count [%d]"    {netatmo="camera=000000000000000000000000#UNKNOWN#AwayCount"}
String Welcome_Unknown_LastSeen   "Unknown lastSeenList [%s]"   {netatmo="camera=000000000000000000000000#UNKNOWN#LastSeenList"}
String Welcome_Unknown_OutOfSight "Unknown OutOfSightList [%s]" {netatmo="camera=000000000000000000000000#UNKNOWN#OutOfSightList"}
String Welcome_Unknown_FaceId     "Unknown FaceIdList [%s]"     {netatmo="camera=000000000000000000000000#UNKNOWN#FaceIdList"}
String Welcome_Unknown_FaceKey    "Unknown FaceKeyList [%s]"    {netatmo="camera=000000000000000000000000#UNKNOWN#FaceKeyList"}
```

#### Supported types for welcome camera unknown person

* HomeCount
* AwayCount
* LastSeenList
* OutOfSightList
* FaceIdList
* FaceKeyList

### Camera

Example item for the **camera** (first id is the home id, second is the camera_id):

```
String Welcome_Camera            "Camera [%s]"              {netatmo="camera=000000000000000000000000#00:00:00:00:00:00#Name"}
String Welcome_Camera_Status     "Camera Status [%s]"       {netatmo="camera=000000000000000000000000#00:00:00:00:00:00#Status"}
String Welcome_Camera_SDStatus   "Camera SD Status [%s]"    {netatmo="camera=000000000000000000000000#00:00:00:00:00:00#SdStatus"}
String Welcome_Camera_AlimStatus "Camera Power Status [%s]" {netatmo="camera=000000000000000000000000#00:00:00:00:00:00#AlimStatus"}
```

#### Supported types for welcome camera unknown person

* Name
* Status
* SdStatus
* AlimStatus


## Common problems

### Missing Certificate Authority

> NOTE: Netatmo switched from SmartCom to Go Daddy for its certificate authority. With this change, you should not encounter the below error anymore. 

```
javax.net.ssl.SSLHandshakeException:
sun.security.validator.ValidatorException:
PKIX path building failed:
sun.security.provider.certpath.SunCertPathBuilderException:
unable to find valid certification path to requested target
```

This can be solved by installing the StartCom CA Certificate into the local JDK like this:

* Download the certificate from https://www.startssl.com/certs/ca.pem or use wget https://www.startssl.com/certs/ca.pem

* Then import it into the keystore (the password is "changeit")

```
$JAVA_HOME/bin/keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -alias StartCom-Root-CA -file ca.pem
```

If $JAVA_HOME is not set then run the command:

```shell
update-alternatives --list java
```

This should output something similar to:

```shell
/usr/lib/jvm/java-8-oracle/jre/bin/java
```

Use everything before /jre/... to set the JAVA_HOME environment variable:

```shell
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
```

After you set the environment variable, try:

```shell
ls -l $JAVA_HOME/jre/lib/security/cacerts
```

If it's set correctly then you should see something similar to:

```shell
-rw-r--r-- 1 root root 101992 Nov 4 10:54 /usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts
```

Now try and rerun the keytool command. If you didn't get errors, you should be good to go.

[(source)](http://jinahya.wordpress.com/2013/04/28/installing-the-startcom-ca-certifcate-into-the-local-jdk/)

Alternative approach if above solution does not work: 
 
```shell
sudo keytool -delete -alias StartCom-Root-CA -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit  
```  
    
Download the certificate from https://api.netatmo.net to $JAVA_HOME/jre/lib/security/ and save it as api.netatmo.net.crt (X.509 / PEM)

```shell
sudo $JAVA_HOME/bin/keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -alias StartCom-Root-CA -file api.netatmo.net.crt 
```  

The password is "changeit".

## Sample data

If you want to evaluate this binding but have not got a Netatmo station yourself yet, you can add the Netatmo office in Paris to your account:

http://www.netatmo.com/en-US/addguest/index/TIQ3797dtfOmgpqUcct3/70:ee:50:00:02:20

## Icons

The following icons are used by original Netatmo web app:

| Modules | Battery | Signal | Wifi |
|:-------:|:-------:|:------:|:----:|
| ![http://my.netatmo.com/img/my/app/module_int.png](http://my.netatmo.com/img/my/app/module_int.png) | ![http://my.netatmo.com/img/my/app/battery_verylow.png](http://my.netatmo.com/img/my/app/battery_verylow.png) | ![http://my.netatmo.com/img/my/app/signal_verylow.png](http://my.netatmo.com/img/my/app/signal_verylow.png) | ![http://my.netatmo.com/img/my/app/wifi_low.png](http://my.netatmo.com/img/my/app/wifi_low.png) |
| ![http://my.netatmo.com/img/my/app/module_ext.png](http://my.netatmo.com/img/my/app/module_ext.png) | ![http://my.netatmo.com/img/my/app/battery_low.png](http://my.netatmo.com/img/my/app/battery_low.png) | ![http://my.netatmo.com/img/my/app/signal_low.png](http://my.netatmo.com/img/my/app/signal_low.png) | ![http://my.netatmo.com/img/my/app/wifi_low.png](http://my.netatmo.com/img/my/app/wifi_low.png) |
| ![http://my.netatmo.com/img/my/app/module_rain.png](http://my.netatmo.com/img/my/app/module_rain.png) | ![http://my.netatmo.com/img/my/app/battery_medium.png](http://my.netatmo.com/img/my/app/battery_medium.png) | ![http://my.netatmo.com/img/my/app/signal_medium.png](http://my.netatmo.com/img/my/app/signal_medium.png) | ![http://my.netatmo.com/img/my/app/wifi_high.png](http://my.netatmo.com/img/my/app/wifi_high.png) |
| | ![http://my.netatmo.com/img/my/app/battery_high.png](http://my.netatmo.com/img/my/app/battery_high.png) | ![http://my.netatmo.com/img/my/app/signal_high.png](http://my.netatmo.com/img/my/app/signal_high.png) | ![http://my.netatmo.com/img/my/app/wifi_full.png](http://my.netatmo.com/img/my/app/wifi_full.png) |
| | ![http://my.netatmo.com/img/my/app/battery_full.png](http://my.netatmo.com/img/my/app/battery_full.png) | ![http://my.netatmo.com/img/my/app/signal_full.png](http://my.netatmo.com/img/my/app/signal_full.png) | |
