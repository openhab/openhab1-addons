## Introduction

The HTTP binding is available as a separate (optional) download.
If you want to let openHAB request a URL when special events occur or let it poll a given URL frequently, place this binding's JAR file in the folder `${openhab_home}/addons` and add binding information to your configuration. See the following sections on how to do this. 

## Generic Item Binding Configuration

In order to bind an item to an HTTP request, you need to provide configuration settings. The easiest way to do so is to add some binding information in your item file (in the folder `configurations/items`). The syntax for the HTTP binding configuration string is:

    in:  http:"<[<url>:<refreshintervalinmilliseconds>:<transformationrule>]"
    out: http:">[<command>:<httpmethod>:<url>:<postcontent>]"

The `:<postcontent>` section is optional, new in version 1.9.0, and only applies when `<httpmethod>` is `POST`.  The media type used is always `text/plain`.  `<postcontent>` can be a literal string, the special keyword `default` which means the string version of the command, or a transformation like `MAP(my.map)`.

For the Out binding there are two special commands available:

- `*` - this means the following URL is called regardless of which command has been issued
- `CHANGED` - this means the following URL is called whenever the state of the given item has changed

Here are some examples of valid binding configuration strings:

    http=">[ON:POST:http://www.domain.org/home/lights/23871/?status=on&type=text] >[OFF:POST:http://www.domain.org/home/lights/23871/?status=off]"
    http="<[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*?<title>(.*?)</title>.*)]"
    http=">[ON:POST:http://www.domain.org/home/lights/23871/?status=on&type=text] >[OFF:POST:http://www.domain.org/home/lights/23871/?status=off]"
    http=">[*:POST:http://www.domain.org/home/lights/23871/?status=%2$s&type=text] <[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*?<title>(.*?)</title>.*)]"
    http=">[CHANGED:POST:http://www.domain.org/home/lights/23871/?status=%2$s&date=%1$tY-%1$tm-%1$td]"

Here are full item examples of sending content in the body of a POST method (new in 1.9.0):

    Switch MyItem1 { http=">[ON:POST:http://sample.com/myitem1:on] >[OFF:POST:http://sample.com/myitem1:off]" }
    Switch MyItem2 { http=">[*:POST:http://sample.com/myitem2:default]" }
    Switch MyItem3 { http=">[*:POST:http://sample.com/myitem3:MAP(onoff.map)]" }

As a result, lines in the items file might look like the following:

    Number Weather_Temperature "Temperature [%.1f °C]"  <temperature>  (Weather) { http="<[http://weather.yahooapis.com/forecastrss?w=638242&u=c:60000:XSLT(demo_yahoo_weather.xsl)]" }
    
## Transformation rules

openHAB supports several types of transformations.

**XSLT transformations**

In most cases, you will get the information you need into an XML structured document, and you need a way to extract only the value you want: here is where [XSLT transformations](Samples-XSLT-Transformations) can help. 

## Dynamic URLs

The given URL can be enhanced using the well known Syntax of the [java.util.Formatter](http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html). The binding currently adds these parameters to the `String.format()` automatically:

1. the current date (as java.util.Date)
1. the current Command or State

To reference these values the indexed format syntax is used. A well prepared URL looks like this:

    http://www.domain.org/home/lights/23871/?status=%2$s&date=%1$tY-%1$tm-%1$td

Each format string starts with '%' followed by an optional index e.g. '2$' whereas '2' is the index of the parameter arg given to the format(format, args...) method. Besides the index you have to specify the format to be applied to the argument. eg. 's' to format a String in the given example or 'd' to format an Integer, or '.1f' to format a Float with one decimal place.

## HTTP headers

It is possible to define optional HTTP headers which will be sent during the HTTP method call. Those optional headers can be added to the url in the form `header1=value1&header2=value2....` This headers string should be enclosed in curly brackets right after the url itself (before the separation colon).

Example:

    http="<[https://www.flukso.net/api/sensor/xxxx?interval=daily{X-Token=mytoken&X-version=1.0}:60000:REGEX(.*?<title>(.*?)</title>(.*))]"

## Handling JSON

[Javascript transforms](Transformations#java-script-transformation-service) can be used to parse JSON input. First, define your item:

`String DirecTV1_Ch "Current Channel" { http="<[http://10.90.30.100:8080/tv/getTuned:30000:JS(getValue.js)]" }`

Then put a file `getValue.js` in `$OPENHAB_DIR/configuration/transform/`

The content of getValue.js is:

    JSON.parse(input).title;

You can also use [JSONPATH](Transformations#jsonpath-transformation-service) which allows a direct query of JSON data:

    'Number Weather_OutTemp "Value: [%.1f °C]" { http="<[http://weewx/now.json:60000:JSONPATH($.stats.current.outTemp)]" }'

[jsonpath.com](http://jsonpath.com/) is a handy tool to create the JSONPATH transformation.

## Configuration in openhab.cfg (optional)

By default, the binding waits for HTTP responses for up to five seconds (5000 milliseconds).  If you need to change this timeout value, edit `openhab.cfg` and set `http:timeout` to the number of milliseconds to wait.  For example, to wait up to 20 seconds for responses, add or uncomment this line:

    http:timeout=20000

By default, the binding checks once every second (1000 milliseconds) to see if any bound items should be retrieved.  If you need to change this refresh value, edit `openhab.cfg` and set `http:granularity` to the number of milliseconds to wait between checks.  For example, to only check once every five seconds, add or uncomment this line:

    http:granularity=5000

By default, the binding will format the URL to include the current state or command, or the current date/time as described above in *Dynamic URLs*.  However, there may be cases where you want to include the special formatting characters in the URL and suppress the formatting.  Therefore, please specify this in your configuration:

    http:format=false

## Caching

Since v1.3, the HTTP binding supports page caching. Caching is usable when multiple items could be parsed from the same URL.

Cache functionality can be configured in the openhab.cfg file (in the folder '${openhab_home}/configurations').

    # URL of the first cache item
    #http:<cacheItemName1>.url=
    # Update interval for first cache item
    #http:<cacheItemName1>.updateInterval=
    
    # URL of the second cache item
    #http:<cacheItemName2>.url=
    # Update interval for second cache item
    #http:<cacheItemName2>.updateInterval=

The `http:<cacheItemName1>.url` value is the valid URL. 
The `http:<cacheItemName1>.updateInterval` value is update interval in milliseconds.

Examples of how to configure your HTTP cache item:

#### Configuration:

    http:weatherCache.url=http://weather.yahooapis.com/forecastrss?w=566473&u=c
    http:weatherCache.updateInterval=60000

#### Items:

    Number temperature { http="<[weatherCache:10000:XSLT(demo_yahoo_weather_temperature.xsl)]" }
    Number windSpeed { http="<[weatherCache:10000:XSLT(demo_yahoo_weather_wind_speed.xsl)]" }
