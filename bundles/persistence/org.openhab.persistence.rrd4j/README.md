# rrd4j Persistence

The [rrd4j](https://github.com/rrd4j/rrd4j) Persistence service is based on a round-robin database.

In contrast to a "normal" database such as db4o, a round-robin database does not grow in size - it has a fixed allocated size, which is used. 
This is accomplished by saving a fixed amount of datapoints and by doing data compression, which means that the older the data is, the less values are available. 
The data is kept in several "archives", each holding the data for its set timeframe in the set granularity.
The start point for all archives is the actually saved datapoint.
So while you might have a value every minute for the last 8 hours, you might only have one every day for the last year.

This service cannot be directly queried, because of the data compression. You could not provide precise answers for all questions. 

NOTE: rrd4j is for storing numerical data only.
Attempting to use rrd4j to store complex datatypes (e.g. for restore-on-startup) will not work.

<!-- MarkdownTOC -->

- [Configuration](#configuration)
    - [Datasource types](#datasource-types)
    - [Heartbeat, MIN, MAX](#heartbeat-min-max)
    - [Step\(s\)](#steps)
    - [Example](#example)
- [Troubleshooting](#troubleshooting)

<!-- /MarkdownTOC -->

## Configuration

This service can be configured in the file `services/rrd4j.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| `<dsname>`.def | |        | `<dstype>,<heartbeat>,[<min>\|U],[<max>\|U],<step>`. For example, `COUNTER,900,0,U,300` |
| `<dsname>`.archives | |        | `<consolidationfunction>,<xff>,<steps>,<rows>`. For example, `AVERAGE,0.5,1,365:AVERAGE,0.5,7,300` |
| `<dsname>`.items  |     |      | `<list of items for this dsname>`. For example, `Item1,Item2` |

where:

- Sections in `[square brackets]` are optional.
- `<dsname>` is a name you choose for the datasource.
- See [Datasource types](#datasource-types) for an explanation of `<dstype>`.
- See [Heartbeat, MIN, MAX](#heartbeat-min-max) for an explanation of `<heartbeat>`, `<min>`, `<max>` and `U`.
- See [Step\(s\)](#steps) for an explanation of `<step>`, `<consolidationfunction>`, `<xff>`, `<steps>`, and `<rows>`.
- `<list of items for this dsname>` is explained in

Round-robin databases (RRDs) have fixed-length so-called "archives" for storing values. 
One RRD can have (in general) several datasources and each datasource can have several archives. 
openHAB only supports one datasource per RRD (i.e. per stored item), which is named DATASOURCE_STATE.
Multiple configurations (with differing .items settings) can be used (see example below).

### Datasource types

Depending on the data to be stored, several types for datasources exist:

- **COUNTER** represents a ever-incrementing value (historically this was used for packet counters or traffic counters on network interfaces, a typical home-automation application would be your electricity meter). If you store the values of this counter in a simple database and make a chart of that, you'll most likely see a nearly flat line, because the increments per time are small compared to the absolute value (e.g. your electricity meter reads 60567 kWh, and you add 0.5 kWh per hour, than your chart over the whole day will show 60567 at the start and 60579 at the end of your chart. That is nearly invisible. RRD4J helps you out and will display the difference from one stored value to the other (depending on the selected size). Please note that the persistence extensions will return difference instead of the actual values if you use this type; this especially leads to wrong values if you try to restoreOnStartup!
- **GAUGE** represents the reading of e.g. a temperature sensor. You'll see only small deviation over the day and your values will be within a small range, clearly visible within a chart.
- **ABSOLUTE** is like a counter, but RRD4J assumes that the counter is reset when the value is read. So these are basically the delta values between the reads.
- **DERIVE** is like a counter, but it can also decrease and therefore have a negative delta.

### Heartbeat, MIN, MAX

Each datasource also has a value for heartbeat, minimum and maximum. This heartbeat setting helps the database to detect "missing" values, i.e. if no new value is stored after "heartbeat" seconds, the value is considered missing when charting. Minimum and maximum define the range of acceptable values for that datasource.

### Step(s)

Step (set in `.def=<dstype>,<heartbeat>,[<min>|U],[<max>|U],<step>` with step in seconds)

Sets the time interval (seconds) between consecutive readings.

- Steps or Granularity (set in `.archives=<consolidationfunction>,<xff>,<steps>,<rows>`

Steps are the number of consecutive readings that are used the create a single entry into the database for this time interval.
The time interval covered is calculated by (step x steps) seconds.

Now for the archives: As already said, each datasource can have several archives.
Think of an archive as a drawer with a fixed number of boxes in it.
Each (step x steps) seconds (the step is globally defined for the RRD, 60s in our example) the leftmost box is emptied, the content of all boxes is moved one box to the left and new content is added to the rightmost box.
The "steps" value is defined per archive it is the third parameter in the archive definition.
The number of boxes is defined as the fourth parameter.

The purpose to have several archives is raised if a different granularity is needed while displaying data for different timespans.
In the example below data for each minute are saved for the last 8 hours (granularity 1), looking at the last 24 hours a granularity of 10 (i.e. 10 readings are consolidated to one reading) is used and so forth.
For the first archive (and maybe the only one) a steps-size of one should be used.
This way a sample is taken after each step.
  
### Example

So in the example shown below, we have 480 boxes, which each represent the value of one minute (Step is set to 60s, Granularity = 1).
If more than one value is added to the database within (step x steps) seconds (and thus more than one value would be stored in one box), the "consolidation function" is used.
openHAB uses AVERAGE as default for numeric values, so if you add 20 and 21 within one minute, 20.5 would be stored.
480 minutes is 8 hours, so we have a 8h with the granularity of one minute.

The next archive has 144 boxes, which each represent the value of ten minutes (Step is set to 60s, Granularity = 10).
1440 minutes is 24 hours, so we have a full day with the granularity of 10 minutes.

The same goes for following archives, for larger time spans, the stored values are less "exact". 
However, usually you are not interested in the exact values for a selected minute some time ago.

services/rrd4j.cfg:

```
ctr24h.def=COUNTER,900,0,U,60
ctr24h.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,10,144
ctr24h.items=Item1,Item2
ctr7d.def=COUNTER,900,0,U,60
ctr7d.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,10,144:AVERAGE,0.5,60,672
ctr7d.items=Item3,Item4
```
In case no rrd4j.cfg is created the following default configuration will be used for all items persisted (i.e. all items with an allocated strategy in the rrd4j.persist file).

Default rrd4j.cfg

```
defaultNumeric.def=GAUGE,60,U,U,60
defaultNumeric.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,4,360:AVERAGE,0.5,14,644:AVERAGE,0.5,60,720:AVERAGE,0.5,720,730:AVERAGE,0.5,10080,520
```
The Datasource type is GAUGE, the heartbeat is 60s, MIN and MAX are unlimited and the step size is 60s.
The archives are:

| Archive | Boxes | Granularity [min] | Period covered |
|:---------:|:---------:|:--------:|:-------------:|
| 1 | 480 | 1 | 8 hrs |
| 2 | 360 | 4 | 24 hrs |
| 3 | 644 | 14 | 6.26 days |
| 4 | 720 | 60 | 30 days |
| 5 | 730 | 720 | 365 days |
| 6 | 520 | 10080 | 10 years |

All item- and event-related configuration is done in the file `persistence/rrd4j.persist`, i.e. openHAB will persist items that are setup in this file with a strategy. 

**IMPORTANT**

The strategy `everyMinute` (60 seconds) **MUST** be used, otherwise no data will be persisted (stored).
Other strategies can be used too.

rrd4j.persist:

```java
Strategies {
    // for rrd charts, we need a cron strategy
    everyMinute : "0 * * * * ?"
}

Items {
    // persist items on every change and every minute
    * : strategy = everyChange, everyMinute
}
```

## Troubleshooting

From time to time, you may find that if you change the item type of a persisted data, you may experience charting or other problems. To resolve this issue, remove the old `<item_name>`.rrd file in the `${openhab_home}/etc/rrd4j` folder or `/var/lib/openhab/persistence/rrd4j` folder for apt-get installed openHABs.

Restore of items after startup is taking some time. Rules are already started in parallel. Especially in rules that are started via "System started" trigger, it may happen that the restore is not completed resulting in defined items. In these cases the use of restored items has to be delayed by a couple of seconds. This delay has to be determined experimentally.
