# Astro Actions

With the Astro actions, you can calculate sunrise and sunset DataTime values in rules.

**Important:** The action also requires the installation of the corresponding Astro 1.x binding.

## Examples

```javascript
import java.util.Date

rule "Astro Action Example"
when
  ...
then
  var Date current = now.toDate
  var double lat = xx.xxxxxx
  var double lon = xx.xxxxxx

  logInfo("sunRiseStart: ", new DateTimeType(getAstroSunriseStart(current, lat, lon)).toString)
  logInfo("sunRiseEnd: ", new DateTimeType(getAstroSunriseEnd(current, lat, lon)).toString)

  logInfo("sunSetStart: ", new DateTimeType(getAstroSunsetStart(current, lat, lon)).toString)
  logInfo("sunSetEnd: ", new DateTimeType(getAstroSunsetEnd(current, lat, lon)).toString)
end
```
