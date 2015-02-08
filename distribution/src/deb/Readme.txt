How it works
- for security reasons the openHAB runtime runs as unprivileged non root user
- all files and directories contained in the deb file are owned by root:root
- most files are readable by others (mode 644 or 444)
- most directories are readable by others (mode 755)
- some files are only readable for the openHAB runtime user and his primary group (mode 440)
  - user.cfg, keystore
- the permissions of files or directories which should be writable by the unprivileged openhab 
  runtime user are adopted at startup time
- openHAB runtime users and groups were created with postinst because user and group
  information can be obtained from /etc/default/openhab
- file and directory permissions are adapted at daemon startup time
  - changes to /etc/default/openhab during application lifetime are honored
- files which are written or updated during runtime are owned by the openHAB user and group
  from /etc/default/openhab
  - the directory /usr/share/openhab/webapps/static is owned by the openHAB user because
    the version and uuid file are created at runtime startup
  - the directory /var/log/openhab is owned by openHAB to enable the creation of log files
    for the openHAB runtime
  - /var/lib/openHAB/workspace is used as osgi workspace and therefore owned by the openHAB user
  - sub directories of /var/lib/openHAB/ are used for storing user data like persistence 
    database files, therefore /var/lib/openHAB/ is owned by the openHAB user
    - rrd4j persistence stores its files in /var/lib/openhab/rrd4j, this is achieved by setting the 
      the java system property smarthome.userdata=/var/lib/openhab
    - db4o persistence stores its files in /var/lib/openhab/db4o, this is achieved by setting
      the java system property smarthome.userdata=/var/lib/openhab
- systemd and sysv init from one deb package
  - is inspired by the deb packaging of elasticsearch:
    https://github.com/elasticsearch/elasticsearch/issues/8493
    and relies on this recommendation from a debian forum:
    http://lists.alioth.debian.org/pipermail/pkg-systemd-maintainers/2014-December/005077.html
  - shares configuration through /usr/share/openhab/bin/openhab.in.sh and functionality through
    /usr/share/openhab/bin/setpermissions.sh
- systemd init executes /usr/share/openhab/bin/setpermissions.sh as root, this is granted by
  setting PermissionsStartOnly=true



Changes:
- removed fragile handling of openhab group from deb packaging
- updated db4o persistence service to use the smarthome.userdata system property
- relies on evolved rrd4j persistence service, which uses the smarthome.userdata property
- changed /etc/init.d/openhab to set smarthome.userdata to /var/lib/openhab
- added systemd service description for openhab
- added script which sets permissions on various runtime directories according to OPENHAB_USER_GROUP variable
- removed (desired) symlink from /usr/share/openhab/webapps/static to /var/lib/openhab/webapps
