# Sager Weathercaster Binding

The Sager Weathercaster is a scientific instrument for accurate prediction of the weather.  **This binding is currently incompatible with openHAB 2.**

## Limitations

* To operate, this binding will need to use items provided by other means (e.g. Weather Binding, Netatmo, a 1-Wire personal weather station...)
* This binding directly queries a persistence service for historical data to analyze

For these reasons, this binding is not a binding in the usual sense, and might have trouble operating in newer runtimes.

## Binding Configuration

This binding can be configured in the file `services/sagercaster.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| latitude |         |    Yes   | latitude of the observed items |
| persistence |      |    No    | name of the persistence service to use when searching past values for wind direction and sea level pressure. Default persistence service will be used if nothing filled here |

## Item Configuration

Input items

```
Number winddirection ... {sagercaster="windbearing"}
```

The wind direction expressed in 0-360°. **This item has to be persisted** 

```
Number seapressure ... {sagercaster="sealevelpressure"}
```

The Seal Level Pressure, expressed in hPa. **This item has to be persisted**

```
Number cloudlevel ... {sagercaster="cloudlevel"}
```

The current cloud level, expressed in %

```
Switch israining ... {sagercaster="raining"}
```

An item indicating wether it's raining or not. This item **must be a SwitchItem** 

```
Number beaufort ... {sagercaster="windspeed"}
```

Windspeed, **expressed in Beaufort Index**.

## Examples

Here are the list of items you can put in your .items file in order to use binding functionalities:

```
Group  swc "SagerWeatherCaster"

// Items directly derived from inputs
String swc_compass 		"Wind Orientation [%s]" 								(swc) { sagercaster = "compass" }
Number swc_windtrend 	"Wind Trend [MAP(sager/windtrend.map):%s]" 				(swc) { sagercaster = "windtrend" }
Number swc_presstrend 	"Pressure Tren [MAP(sager/presstrend.map):%s]" 			(swc) { sagercaster = "pressuretrend" }

// SagerWeatherCaster Forecast Items
String swc_forecast 	"Weather Forecast [MAP(sager/forecast.map):%s]" 		(swc) { sagercaster = "forecast" }
String swc_velocity 	"Wind Forecast [MAP(sager/windvelocity.map):%s]" 		(swc) { sagercaster = "velocity" }
Number swc_windfrom 	"Wind varying from [MAP(sager/winddirection_ntz.map):%s]" 	(swc) { sagercaster = "windfrom" }
Number swc_windto 	"Wind varying to [MAP(sager/winddirection_ntz.map):%s]" 	(swc) { sagercaster = "windto" 	}
```

## Observation delay

You'll have noticed that some inputs for the binding needs to be persisted. SagerWeatherCaster needs an observation period of minimum 6 hours to start producing outputs.

## Download

[Sager Transform Files](https://www.dropbox.com/s/6n16x15t3fisbmq/sager.zip?dl=0) This files shall be placed in a `transform/sager` folder for human readable results of the SagerWeatherCaster algorithm.

