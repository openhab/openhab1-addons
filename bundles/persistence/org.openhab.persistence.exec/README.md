# Exec Persistence

This service allows you to execute commands in the underlying OS to persist item states. It could be used to, for example, call the original `rrdtool` CLI tool.

This persistence service supports only writing information, and so features such as `restoreOnStartup` and sitemap Chart widgets cannot be used with this service.

## Configuration

This service does not have a configuration.

All item- and event-related configuration is done in the file `persistence/exec.persist`.

Aliases have special meaning because they contain the command line to execute e.g. like

```
"echo \"%2$tY-%2$tm-%2$td %2$tT : %1$s\""
```

To enhance the given command line with the current state or the current date, the service incorporates the [String.format()](https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html) method. The first parameter is always the state of the particular item, the second parameter is the current date.
