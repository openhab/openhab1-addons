# HTTP Binding

If you want to have openHAB request a URL when commands are sent to items, or have it poll a given URL frequently and update items' states, install and configure this binding.

## Binding Configuration

The HTTP binding can be configured in the file `services/http.cfg`.  However, all configuration properties are optional.

| Property | Default | Description |
|----------|---------|-------------|
| timeout  | 5000    | the binding waits for HTTP responses for up to five seconds (5000 milliseconds).  For example, to wait up to 20 seconds for responses, change this value to 20000 |
| granularity | 1000 | the binding checks once every second (1000 milliseconds) to see if any bound items should be retrieved.  For example, to only check once every five seconds, change this value to 5000 |
| format   | true    | the binding will format the URL to include the current state or command, or the current date/time as described [below](#dynamic-urls) However, there may be cases where you want to include the special formatting characters in the URL and suppress the formatting, in which case change this value to `false` |
| `<cacheItemName1>`.url | | URL of the first cache item: the binding supports page caching. Caching is usable when multiple items could be parsed from the same URL.  Choose a convenient name for `<cacheItemName1>` and this can be used in item binding strings (described below) |
| `<cacheItemName1>`.updateInterval | | Update interval in milliseconds for first cache item: how often the binding will retrieve the URL specified in `<cacheItemName1>`.url |
| `<cacheItemName2>`.url | | URL of the second cache item: the binding supports page caching. Caching is usable when multiple items could be parsed from the same URL.  Choose a convenient name for `<cacheItemName1>` and this can be used in item binding strings (described below) |
| `<cacheItemName2>`.updateInterval | | Update interval in milliseconds for second cache item: how often the binding will retrieve the URL specified in `<cacheItemName1>`.url |

### Example of how to configure an HTTP cache item

```
weatherCache.url=http://weather.yahooapis.com/forecastrss?w=566473&u=c
weatherCache.updateInterval=60000
```

You can use `weatherCache` in your items like this:

```
Number temperature { http="<[weatherCache:10000:XSLT(demo_yahoo_weather_temperature.xsl)]" }
Number windSpeed { http="<[weatherCache:10000:XSLT(demo_yahoo_weather_wind_speed.xsl)]" }
```

## Item Configuration

The syntax for the HTTP binding configuration string is given below.

Receive repeated updates from a URL ("in" binding):

```
http="<[<url>:<refreshintervalinmilliseconds>:<transformationrule>]"
```

Send commands to a URL ("out" binding):

```
http=">[<command>:<httpmethod>:<url>:<postcontent>]"
```


The `:<postcontent>` section is optional, new in version 1.9.0, and only applies when `<httpmethod>` is `POST`.  The media type used is always `text/plain`.  `<postcontent>` can be a literal string, the special keyword `default` which means the string version of the command, or a transformation like `MAP(my.map)`.

For the "out" binding, there are two special commands available:

- `*` - this means the following URL is called regardless of which command has been issued
- `CHANGED` - this means the following URL is called whenever the state of the given item has changed

Here are some examples of valid binding configuration strings:

```
http=">[ON:POST:http://www.domain.org/home/lights/23871/?status=on&type=text] >[OFF:POST:http://www.domain.org/home/lights/23871/?status=off]"
http="<[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*?<title>(.*?)</title>.*)]"
http=">[ON:POST:http://www.domain.org/home/lights/23871/?status=on&type=text] >[OFF:POST:http://www.domain.org/home/lights/23871/?status=off]"
http=">[*:POST:http://www.domain.org/home/lights/23871/?status=%2$s&type=text] <[http://www.domain.org/weather/openhabcity/daily:60000:REGEX(.*?<title>(.*?)</title>.*)]"
http=">[CHANGED:POST:http://www.domain.org/home/lights/23871/?status=%2$s&date=%1$tY-%1$tm-%1$td]"
```

Here are full item examples of sending content in the body of a POST method (new in 1.9.0):

```
Switch MyItem1 { http=">[ON:POST:http://sample.com/myitem1:on] >[OFF:POST:http://sample.com/myitem1:off]" }
Switch MyItem2 { http=">[*:POST:http://sample.com/myitem2:default]" }
Switch MyItem3 { http=">[*:POST:http://sample.com/myitem3:MAP(onoff.map)]" }
```

As a result, lines in the items file might look like the following:

```
Number Weather_Temperature "Temperature [%.1f °C]"  <temperature>  (Weather) { http="<[http://weather.yahooapis.com/forecastrss?w=638242&u=c:60000:XSLT(demo_yahoo_weather.xsl)]" }
```

## Dynamic URLs

The given URL can be enhanced using the well known Syntax of the [java.util.Formatter](http://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html). The binding currently adds these parameters to the `String.format()` automatically:

1. the current date (as java.util.Date)
1. the current Command or State

To reference these values the indexed format syntax is used. A well prepared URL looks like this:

```
http://www.domain.org/home/lights/23871/?status=%2$s&date=%1$tY-%1$tm-%1$td
```

Each format string starts with '%' followed by an optional index e.g. `2$`, where `2` is the index of the parameter arg given to the format(format, args...) method. Besides the index you have to specify the format to be applied to the argument. eg. `s` to format a String in the given example or `d` to format an Integer, or `.1f` to format a floating point number with one decimal place.

## HTTP headers

It is possible to define optional HTTP headers which will be sent during the HTTP method call. Those optional headers can be added to the url in the form `header1=value1&header2=value2...`. This headers string should be enclosed in curly brackets right after the url itself (before the separation colon).

Example:

```
http="<[https://www.flukso.net/api/sensor/xxxx?interval=daily{X-Token=mytoken&X-version=1.0}:60000:REGEX(.*?<title>(.*?)</title>(.*))]"
```

## HTTP Basic authentication

It is possible to use optional HTTP Basic authentication to provide a username and password for a request. There is a very good article on Wikipedia for [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication). In the earlier days it was possible to prepend `username:password@` to the hostname in the url. The use of this format is deprecated since RFC 3986.

### Authorization header

When your username or password contains reserved characters like an `@` or a `:` symbol you should encode the username and password in Base64 and use a HTTP header. The header's value is the base64-encoding of `username:password` (which results in `dXNlcm5hbWU6cGFzc3dvcmQ=`). There is an online decoder/encoder [here](https://www.base64encode.org/).

Example:

```
http="<[http://sample.com{Authorization=Basic dXNlcm5hbWU6cGFzc3dvcmQ=}]"
```

## Handling JSON

You can use the JavaScript transformation:

```
String DirecTV1_Ch "Current Channel" { http="<[http://10.90.30.100:8080/tv/getTuned:30000:JS(getValue.js)]" }
```

Then create the file `transform/getValue.js`:

```
JSON.parse(input).title;
```

You can also use the JsonPath transformation, which allows a direct query of JSON data:

```
Number Weather_OutTemp "Value: [%.1f °C]" { http="<[http://weewx/now.json:60000:JSONPATH($.stats.current.outTemp)]" }
```

[jsonpath.com](http://jsonpath.com/) is a handy tool to create the JSONPATH transformation.
