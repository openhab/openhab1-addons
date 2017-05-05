# GPIO Binding

This binding is for the local GPIO subsystem. It is currently only exposed to user space by [Linux GPIO framework](https://www.kernel.org/doc/Documentation/gpio/sysfs.txt). Being based on a kernel implementation, it's hardware agnostic and works on different boards without modification (this is on theory only, not all existing boards can be tested). 

The difference from other bindings dealing with GPIOs is that it works with GPIO subsystem on the board on which openHAB runs and doesn't require third-party programs/daemons running. The binding consists of two components: base module (org.openhab.io.gpio) which implements low-level GPIO access and provides API for high-level modules (can be used by other bindings needing to interact directly with GPIOs) and the binding itself (org.openhab.binding.gpio) which introduces hardware GPIO pins as full-featured openHAB items capable of generating events or receiving commands depending of their type (input or output).

## Prerequisites

* Linux-based OS with GPIO driver loaded (check whether exists directory `/sys/class/gpio`), usually it's compiled into the kernel for all recent boards which exposes GPIOs
* Mounted `sysfs` pseudo file system, the mount point can be:

 * Automatically determined if `procfs` is mounted under path `/proc`, this is the default path in almost all configurations
 * Manually set in openHAB configuration file, key `gpio:sysfs`
 * See https://github.com/java-native-access/jna for supported platforms 
 * root privileges, openHAB should be run under "root" account.  

Alternatively you can add the user "openhab" to the usergroup "gpio", if your distribution (like rasbian) does have such a group.  

```shell
sudo adduser openhab gpio
```

_NOTE: Some boards may need additional pin configuration prior using them, for example OMAP-based processors are using pin multiplexing which require changing the mode for some of the pins. Please refer to board's System Reference Manual for more information whether preliminary configuration is needed and how to do it._

Some manual configuration may be needed.  The following edit to `/etc/default/openhab` may be necessary (see discussion above in Prerequisites section).  If needed, set:

```
JAVA_ARGS=-Djna.boot.library.path=/usr/lib/jni
```

The `JAVA_ARGS` edit may not be necessary, and could cause openHAB to crash.  If it does, simply undo the edit and restart openHAB.

If you chose above to add the openhab user to the gpio group, then you shouldn't make the changes below to run as root.  If you did not add the openhab user to the gpio group, make a further edit to `/etc/default/openhab`:

```
USER_AND_GROUP=root:root
```

And then edit `/usr/lib/systemd/system/openhab.service` and set the following:

```
User=root
Group=root
```
 
## Binding Configuration

This binding can be configured in the file `services/gpio.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| sysfs    |         |          | optional directory path where `sysfs` pseudo file system is mounted. If isn't specified it will be determined automatically (if `procfs` is mounted under `/proc`). |
| debounce | 0       |          | optional time interval in milliseconds in which pin interrupts for input pins will be ignored to prevent bounce effect seen mainly on buttons. Global option for all pins, can be customized per pin in item configuration. |

Examples:

```
sysfs=/sys
debounce=10
```

_NOTE: While change in these global options is allowed at runtime it's not advisable to do that. This is because only newly created pins will use the new values while currently existing pins will use the old one._

_NOTE: These options are optional, in most circumstances you don't have to specify them._

## Item Configuration

Allowed item types are `Contact` and `Switch`. Type `Contact` is used for input pins, `Switch` - for output pins. The configuration string is following:

```
gpio="pin:PIN_NUMBER [debounce:DEBOUNCE_INTERVAL] [activelow:yes|no] [force:yes|no] [initialValue:high|low]"
```

Key-value pairs are separated by space; their order isn't important. Character's case is also insignificant. Key-value pair `pin` is mandatory, `debounce`, `activelow` and `initialValue` are optional. If omitted `activelow` is set to `no`, `debounce` - to global option in configuration file (`debounce`) or 0 (zero) if neither is specified, initialValue is set to LOW. 

`PIN_NUMBER` is the number of the GPIO pin as seen by the kernel (not necessarily the same as the physical pin number).  

`DEBOUNCE_INTERVAL` is the time interval in milliseconds in which pin interrupts for input pins will be ignored to prevent bounce effect seen mainly on buttons. Note that underlying OS isn't real time nor the application is, so debounce implementation isn't something on which you can rely on 100%, you may need to experiment with this value. 

When `activelow` is set to `no` (or omitted) the pins behaves normally: output pins will be set `high` on `ON` command and `low` on `OFF`, input pins will generate `OPEN` event when they are `high` and `CLOSED` when are `low`. However, if `activelow` is set to `yes` the logic is inverted: when `ON` command is sent to output pin it will be set to `low`, on `OFF` command - to `high`. Input pins will generate `OPEN` event when they are `low` and `CLOSED` event on `high`.

`initialValue` is the state of the pin which is set during initialization. It is applicable only for oputput pins (item Switch) and can be HIGH or LOW.

The "force" option can be used to forcefully get hold of the configured pin even if it is currently in use, so it automatically gets unexported and exported again.

Examples:

```
Switch LED "LED" { gpio="pin:1" }
Switch NormallyClosedRelay "Normally Closed Relay" { gpio="pin:2 activelow:yes initialValue:high" }
Contact NormallyOpenPushButton "Normally Open Push Button" { gpio="pin:3 debounce:10" }
Contact PIR "PIR" { gpio="pin:4 activelow:yes" }
Contact NormallyClosedPushButton "Normally Closed Push Button" { gpio="pin:5 debounce:10 activelow:yes" }
Contact ForcePin "Force access to pin" { gpio="pin:6 force:yes" }
```

## Automatic unexport when using init.d script

When using the init.d startup script from this wiki ([link](https://github.com/openhab/openhab1-addons/wiki/Samples-Tricks#how-to-configure-openhab-to-start-automatically-on-linux)) and you try to stop openHAB it will not unexport your gpio pins. You can do this manually or edit the startup script like bellow.

First find the stop section: Look for the do_stop() function and insert your unexports:

init.d script:

```shell
do_stop()
{
# Return
#   0 if daemon has been stopped
#   1 if daemon was already stopped
#   2 if daemon could not be stopped
#   other if a failure occurred
start-stop-daemon --stop --quiet --retry=TERM/30/KILL/5 --pidfile $PIDFILE $
#unexport all gpio's
echo 22 > /sys/class/gpio/unexport
echo 23 > /sys/class/gpio/unexport
echo 24 > /sys/class/gpio/unexport
echo 10 > /sys/class/gpio/unexport
echo 9 > /sys/class/gpio/unexport
echo 25 > /sys/class/gpio/unexport
echo 11 > /sys/class/gpio/unexport
echo 8 > /sys/class/gpio/unexport
RETVAL="$?"
[ "$RETVAL" = 2 ] && return 2
# Wait for children to finish too if this is a daemon that forks
# and if the daemon is only ever run from this initscript.
# If the above conditions are not satisfied then add some other code
# that waits for the process to drop all resources that could be
# needed by services started subsequently.  A last resort is to
# sleep for some time.
start-stop-daemon --stop --quiet --oknodo --retry=0/30/KILL/5 --exec $DAEMON
[ "$?" = 2 ] && return 2
# Many daemons don't delete their pidfiles when they exit.
rm -f $PIDFILE
return "$RETVAL"
}
```

Naturally edit the number after the echo to the gpio pin you use and insert or remove lines matching the number of gpio's in use.

Now you can stop and start openhab without your GPIO pins getting blocked!
