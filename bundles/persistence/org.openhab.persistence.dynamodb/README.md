
Updating Amazon SDK

1. Clean `lib/*`
2. Update SDK version in `scripts/fetch_sdk_pom.xml`. You can use the [maven online repository browser](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-dynamodb) to find the latest version available online.
3. `scripts/fetch_sdk.sh`
4. Copy `scripts/target/site/dependencies.html` and `scripts/target/dependency/*.jar` to `lib/`
5. Generate `build.properties` entries
`ls lib/*.jar | python -c "import sys; print('  ' + ',\\\\\\n  '.join(map(str.strip, sys.stdin.readlines())))"`
6. Generate `META-INF/MANIFEST.MF` `Bundle-ClassPath` entries
`ls lib/*.jar | python -c "import sys; print('  ' + ',\\n  '.join(map(str.strip, sys.stdin.readlines())))"`
7. Generate `.classpath` entries
`ls lib/*.jar | python -c "import sys;pre='<classpathentry exported=\"true\" kind=\"lib\" path=\"';post='\"/>'; print('\\t' + pre + (post + '\\n\\t' + pre).join(map(str.strip, sys.stdin.readlines())) + post)"`