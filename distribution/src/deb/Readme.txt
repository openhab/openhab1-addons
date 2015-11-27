How it works
- For security reasons the openHAB runtime runs as unprivileged non-root user
  (openHAB runtime user).
- The default user is named "openhab" and with the primary group "openhab".
- The user and group can be adapted with the variable USER_AND_GROUP in the
  /etc/default/openhab configuration file.
- The openHAB runtime user and group are created using the deb file postinst
  script. It's postinst not preinst because user and group information can be
  and is obtained from /etc/default/openhab.
- Permissions used for the deb file
  - All files and directories contained in the deb file are owned by user root and 
    group root.
  - Most files and directories are readable by others (file mode 644 or 444, directory mode 755).
  - For security reasons some files should only be readable for the openHAB runtime user
    and his primary group (mode 440). In the deb file they are owned by root:root, the
    ownership will be adapted at daemon startup.
    For now these files are /etc/openhab/configurations/user.cfg and /etc/openhab/jetty/etc/keystore.
  - There are files and directories which must be writable by the openHAB runtime user.
    These files / directories like all others are owned by root:root in the deb file.
    The permissions and owner are adapted at daemon startup time. Therefore changes to 
    USER_AND_GROUP in /etc/default/openhab during application lifetime are honored.
- The permissions and owner of files and directories which should be writable by the openHAB 
  runtime user are adopted at startup time.
  - the directory /usr/share/openhab/webapps/static is owned by the openHAB runtime user because
    the "version" and "uuid" files are created at runtime startup.
  - the directory /var/log/openhab is owned by the openHAB runtime user to enable the creation 
    of log files for the openHAB runtime
  - /var/lib/openHAB/workspace is used as osgi workspace and therefore owned by the openHAB 
    runtime user.
  - sub directories of /var/lib/openHAB/ are used for storing user data like persistence 
    database files, therefore /var/lib/openHAB/ is owned by the openHAB runtime user
    - rrd4j persistence stores its files in /var/lib/openhab/rrd4j, this is achieved by setting the 
      the java system property smarthome.userdata=/var/lib/openhab.
    - db4o persistence stores its files in /var/lib/openhab/db4o, this is achieved by setting
      the java system property smarthome.userdata=/var/lib/openhab.
- systemd and sysv init from one deb package
  - is inspired by the deb packaging of elasticsearch:
    https://github.com/elasticsearch/elasticsearch/issues/8493
    and relies on this recommendation from a debian forum:
    http://lists.alioth.debian.org/pipermail/pkg-systemd-maintainers/2014-December/005077.html
  - systemd and sysv init share configuration through /usr/share/openhab/bin/openhab.in.sh and 
    functionality through /usr/share/openhab/bin/setpermissions.sh
- systemd init executes /usr/share/openhab/bin/setpermissions.sh as root, this is granted by
  setting PermissionsStartOnly=true



Changes:
1:
- changed openhab.userdata back to smarthome.userdata
0:
- removed fragile handling of openhab group from deb packaging
- updated db4o persistence service to use the openhab.userdata system property
- changed rrd4j persistence service, to use openhab.userdata property instead of smarthome.userdata.
- changed /etc/init.d/openhab to set openhab.userdata property to /var/lib/openhab
- added systemd service description for openhab
- added script which sets permissions on various runtime directories according to OPENHAB_USER_GROUP variable
- removed symlink configuration from pom.xml for /usr/share/openhab/webapps/static to /var/lib/openhab/webapps,
  because it was broken and may be have unintentional behavior for the greent addon.

