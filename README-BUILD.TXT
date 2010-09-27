== Build: updating the version number of the project ==
For example to update to 0.5.0-SNAPSHOT
mvn org.sonatype.tycho:tycho-versions-plugin:set-version -DnewVersion=0.5.0-SNAPSHOT
This takes care of all OSGi files too.

