Author: Kai Kreuzer

Usage
---

1. cd ./bundles/archetype/./org.openhab.archetype.action
2. mvn clean install
3. cd ../../action
4. mvn archetype:generate -B -DarchetypeGroupId=org.openhab.archetype -DarchetypeArtifactId=org.openhab.archetype.action -DarchetypeVersion=1.3.0-SNAPSHOT -Dauthor=<author> -Dversion=1.3.0 -DartifactId=org.openhab.action.<action-name> -Dpackage=org.openhab.action.<action-name> -Daction-name=<action-name>
5. import newly created project by issuing 'Import->Existing Java project'
6. active the new plugin in RunConfiguration 'Run Configurations->openHAB Runtime->Plugins->activate your plugin->Auto-start true'
