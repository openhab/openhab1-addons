# Weather Actions

The Weather Action service provides meteorological information to your scripts and rules.

*   `getHumidex(double temperature, int hygro)`: Compute the Humidex index given temperature in Celsius and hygrometry (relative percent).  Returns Humidex index value.
*   `getBeaufortIndex(double speed)`: Compute the [Beaufort scale](http://en.wikipedia.org/wiki/Beaufort_scale) for a given wind speed in m/s.  Returns the Beaufort Index between 0 and 12.  `transform/beaufort.map`:

```
0=Calm
1=Light air
2=Light breeze
3=Gentle breeze
4=Moderate breeze
5=Fresh breeze
6=Strong breeze
7=High wind
8=Gale
9=Strong/severe gale
10=Storm
11=Violent storm
12=Hurricane force
```

*   `getSeaLevelPressure(double pressure, double temp, double altitude)`: Compute the [Sea Level Pressure](http://keisan.casio.com/exec/system/1224575267), given absolute pressure in hPa, temperature in Celsius, and altitude in meters.  Returns equivalent sea level pressure.
*   `getWindDirection(int degree)`: Transform an orientation angle (in degrees) to its cardinal string equivalent.  Returns string representing the direction.
