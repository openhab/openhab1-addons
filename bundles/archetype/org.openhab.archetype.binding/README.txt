Author: Thomas.Eichstaedt-Engelen

Usage
---

1. cd ./bundles/archetype/./org.openhab.archetype.binding
2. mvn clean install
3. cd ../../binding
4. mvn archetype:generate -B -DarchetypeGroupId=org.openhab.archetype -DarchetypeArtifactId=org.openhab.archetype.binding -DarchetypeVersion=1.3.0-SNAPSHOT -Dauthor=<author> -Dversion=1.3.0 -DartifactId=org.openhab.binding.<binding-name> -Dpackage=org.openhab.binding.<binding-name> -Dbinding-name=<binding-name>
5. import newly created project by issuing 'Import->Existing Java project'
6. active the new plugin in RunConfiguration 'Run Configurations->openHAB Runtime->Plugins->activate your plugin->Auto-start true'
