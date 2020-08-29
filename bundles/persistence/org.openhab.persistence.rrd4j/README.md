# rrd4j Persistence

The [rrd4j](https://github.com/rrd4j/rrd4j) persistence service is based on a round-robin database.

In contrast to a "normal" database such as db4o, a round-robin database does not grow in size - it has a fixed allocated size.
This is accomplished by saving a fixed amount of datapoints and by doing data compression, which means that the older the data is, the less values are available. 
The data is kept in several "archives", each holding the data for its set timeframe at a defined level of granularity.
The starting point for all archives is the actually saved data sample (Item value).
So while you might store a sample value every minute for the last 8 hours, you might store the average per day for the last year.

This service cannot be directly queried, because of its data compression, which means that it cannot provide precise answers to all queries.

NOTE: rrd4j is for storing numerical data only.
It cannot store complex data types.

## Persistence Process

Round-robin databases (RRDs) have fixed length so called "archives" for storing values.
Think of an archive as a "drawer" with a fixed number of "storage boxes" in it.

The persistence service reads data "samples" from the OpenHAB core at regular intervals, and these are then put into the storage boxes.
Either a) the samples are stored singly directly into a box, or b) multiple samples are consolidated (using a consolidation function) into a box.

The service starts by storing samples in the leftmost box in the drawer.
Once the leftmost box is full, the service starts filling the next box to the right; and so on.
Once the rightmost box in the drawer is full, the leftmost box is emptied, the content of all boxes is moved one box to the left, and new content is added to the rightmost box.

An example is shown below.
Whereby the values indicated in the example may vary as chosen by the user..

- Samples are taken at intervals of `60` seconds
- They are consolidated by the `AVERAGE` function, over `10` samples, into boxes i.e. a box covers `10 X 60` seconds
- The full archive contains `250` boxes i.e. the archive/drawer covers `60 X 10 X 250` seconds

## Configuration

Two things must be done in order for an Item to get persisted:

1. it must have a persistence strategy defined in the `rrd4j.persist` file.
2. it must have a `datasource` defined as follows..

## Datasources

The database comprises at least one datasource.
The rrd4j service automatically creates one internal _**default**_ datasource for you.
Other datasources **may** be configured in addition, in the `services/rrd4j.cfg` file.

By default, if `services/rrd4j.cfg` does not exist, or if an Item is not explicitly listed in a `<dsName>.items` property value in it, then the respective Item will be persisted according to the [default datasource settings](#default-datasource).

By constrast if an Item **is** explicitly listed in a `<dsName>.items` property value, then it will be persisted according to those respective datasource settings.

Each datasource is defined by three property values (`def`, `archives`, `items`).
Whereby each `archives` property can comprise settings for one or more archives.

The various datasource property values are explained in the table below.

| Property            | Description |
|---------------------|-------------|
| `<dsName>`.def      | Definition of the range of sample values to be taken, and when. The format is `<dsType>,<heartBeat>,<minValue>,<maxValue>,<sampleInterval>` |
| `<dsName>`.archives | List of archives to be created. Each archive defines which subset of data samples shall be archived, and for how long. Consists of one or more archive entries separated by a ":" character. The format for one archive entry is `<consolidationFunction>,<xff>,<samplesPerBox>,<boxCount>` |
| `<dsName>`.items    | List of Items whose values shall be sampled and stored in the archive. The format is `Item1,Item2` _**Note: the same Item is not allowed to be listed in more than one datasource!**_ |

For example..

```
ctr24h.def=COUNTER,900,0,U,60
ctr24h.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,10,144
ctr24h.items=Item1,Item2
```

The description of the various datasource property elements is as follows:

### `<dsName>` (Datasource Name)

The name of the datasource.
It must be an alphanumeric string.

### `<dsType>` (Datasource Type)

Defines the type of data to be stored.
It must be one of the following string values:

- **COUNTER** represents a ever-incrementing value (historically this was used for packet counters or traffic counters on network interfaces, a typical home-automation application would be your electricity meter). If you store the values of this counter in a simple database and make a chart of that, you'll most likely see a nearly flat line, because the increments per time are small compared to the absolute value (e.g. your electricity meter reads 60567 kWh, and you add 0.5 kWh per hour, than your chart over the whole day will show 60567 at the start and 60579 at the end of your chart. That is nearly invisible. RRD4J helps you out and will display the difference from one stored value to the other (depending on the selected size). Please note that the persistence extensions will return difference instead of the actual values if you use this type; this especially leads to wrong values if you try to restoreOnStartup!
- **GAUGE** represents the reading of e.g. a temperature sensor. You'll see only small deviation over the day and your values will be within a small range, clearly visible within a chart.
- **ABSOLUTE** is like a counter, but RRD4J assumes that the counter is reset when the value is read. So these are basically the delta values between the reads.
- **DERIVE** is like a counter, but it can also decrease and therefore have a negative delta.

### `<heartBeat>` (Heart Beat)

The heartbeat parameter helps the database to detect missing values.
i.e. if no new value is stored after "heartBeat" seconds, the value is considered missing when charting.

It must be a positive integer value.

### `<minValue> / <maxValue>` (Minimum resp. Maximum Value)

These parameters define the range of acceptable sample values for that datasource.
They must be either:

- A numeric value, or
- The letter "U" (unlimited)

### `<sampleInterval>` (Sample Interval)

The time interval (seconds) between reading consecutive samples from the OpenHAB core.

It must be a positive integer value.

### `<consolidationFunction>` (Consolidation Function)

Determines the type of data compression to be used when more than one sample is to be stored in a single "storage box".
So if you use the `AVERAGE` function, and two samples of `20.0` and `21.0` are to be stored, then the value `20.5` would be stored in the box.

It must be one of the following strings:

- **AVERAGE** the average of all the samples is stored in the box
- **MIN** the lowest sample is stored in the box
- **MAX** the highest sample is stored in the box
- **LAST** the last sample is stored in the box
- **FIRST** the first sample is stored in the box
- **TOTAL** the sum of all samples is stored in the box

All archives of a datasource must use the same `<consolidationFunction>`.

### `<xff>` (X-files Factor)

Defines the maximum allowed proportion of data samples that are stored as NaN ("Not a Number") relative to the set number of `<samplesPerBox>`. In case this proportion is above the set value, NaN will be persisted instead of the consolidated value. Using 0.5 would require at least 50 percent of the data samples to hold a value other than NaN.

It must be a value between 0 and 1.

### `<samplesPerBox>` (Samples Per Box)

The number of consecutive data samples that will be consolidated to create a single entry ("storage box") in the database.
If `<samplesPerBox>` is greater than 1 then the samples will be consolidated into the "storage box" by means of the `<consolidationFunction>` described above.
The time span covered by a single "storage box" is therefore (`<sampleInterval>` x `<samplesPerBox>`) seconds.

It must be a positive integer value.

### `<boxCount>` (Box Count)

The number of "storage boxes" in the archive.
The time span covered by a full archive is therefore (`<sampleInterval>` x `<samplesPerBox>` x `<boxCount>`) seconds.

It must be a positive integer value.

### Multiple Possible Archives

As already said, each datasource can have one or more archives.
The purpose of having several archives is that it allows a different granularity of data storage over different timespans.

In the example below..

```
ctr24h.def=COUNTER,900,0,U,60
ctr24h.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,10,144
ctr24h.items=Item1,Item2
```

The `ctr24.def` defines a datasource which is using a COUNTER, a `<hearBeat>` of 900 seconds, a `<minValue>` of 0, a `<maxValue>` of unlimited  and a `<sampleInterval>` of 60 seconds.

The first archive entry in the `ctr24.archives` parameter has `480` boxes each containing `1` sample (or to be exact the `AVERAGE` of `1` sample).
So it covers `480 X 60` seconds of data (8 hours) at a granularity of one minute.
As a general rule the first archive (and maybe the only one) should have `<samplesPerBox> = 1` so that each sample is stored in one box.

And the second archive entry has `144` boxes each containing the `AVERAGE` of `10` samples.
So it covers `144 X 10 X 60` seconds of data (24 hours) at a granularity of ten minutes.

## Default Datasource

The service always automatically creates an internal default datasource with the properties below.

```
defaultNumeric.def=GAUGE,60,U,U,60
defaultNumeric.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,4,360:AVERAGE,0.5,14,644:AVERAGE,0.5,60,720:AVERAGE,0.5,720,730:AVERAGE,0.5,10080,520
```

The default datasource type is GAUGE, the heartbeat is 60s, minimum and maximum values are unlimited, and the sample interval is 60s.

The default archives are:

| Archive | Boxes | Samples per Box | Period covered |
|:---------:|:---------:|:--------:|:-------------:|
| 1 | 480 | 1 | 8 hrs |
| 2 | 360 | 4 | 24 hrs |
| 3 | 644 | 14 | 6.26 days |
| 4 | 720 | 60 | 30 days |
| 5 | 730 | 720 | 365 days |
| 6 | 520 | 10080 | 10 years |

There is no `.items` parameter for the default datasource.
Implicitly this means that any Item with an allocated strategy in the `rrd4j.persist` file will be persisted using the above-mentioned default settings -
_**exception**:_ the Item is explicitly listed in the `.items` property value of a datasource in the `rrd4j.cfg` file.

---

## Examples

### `rrd4j.cfg` file

```
ctr24h.def=COUNTER,900,0,U,60
ctr24h.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,10,144
ctr24h.items=Item1,Item2
ctr7d.def=COUNTER,900,0,U,60
ctr7d.archives=AVERAGE,0.5,1,480:AVERAGE,0.5,10,144:AVERAGE,0.5,60,672
ctr7d.items=Item3,Item4
```

### `rrd4j.persist` file:

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

**IMPORTANT:**
The strategy `everyMinute` (60 seconds) **must** be used, otherwise no data will be persisted (stored).
Other strategies can be used too.

---

## Troubleshooting

From time to time, you may find that if you change the Item type of a persisted data point, you may experience charting or other problems. To resolve this issue, remove the old `<item_name>`.rrd file in the `${openhab_home}/etc/rrd4j` folder or `/var/lib/openhab/persistence/rrd4j` folder for apt-get installed openHABs.

Restoring Item values after startup takes some time. Rules may already have started to run in parallel. Especially in rules that are started via the "System started" trigger, it may happen that the restore has not yet completed resulting in non-defined Item values. In these cases the use of restored Item values should be delayed by a couple of seconds. This delay has to be determined experimentally.
