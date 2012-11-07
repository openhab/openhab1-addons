Author: Thomas.Eichstaedt-Engelen

Usage
---

1. cd ./bundles/archetype/./org.openhab.archetype.binding
2. mvn clean install
3. cd ../../binding
4. mvn archetype:generate -B -DarchetypeGroupId=org.openhab.archetype -DarchetypeArtifactId=org.openhab.archetype.binding -DarchetypeVersion=1.1.0-SNAPSHOT -Dauthor=<author> -Dversion=1.1.0 -DartifactId=org.openhab.binding.<binding-name> -Dpackage=org.openhab.binding.<binding-name> -Dbinding-name=<binding-name>
