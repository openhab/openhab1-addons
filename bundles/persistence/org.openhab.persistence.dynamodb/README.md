
Updating Amazon SDK

1. Clean `lib/*`
2. `scripts/fetch_sdk.sh`
3. Copy `scripts/target/site/dependencies.html` and `scripts/target/dependency/*.jar` to `lib/`
4. Generate `build.properties` entries
`ls lib/*.jar | python -c "import sys; print('  ' + ',\\\\\\n  '.join(map(str.strip, sys.stdin.readlines())))"`
4. Generate `META-INF/MANIFEST.MF` `Bundle-ClassPath` entries
`ls lib/*.jar | python -c "import sys; print('  ' + ',\\n  '.join(map(str.strip, sys.stdin.readlines())))"`
6. Generate `.classpath` entries
`ls lib/*.jar | python -c "import sys;pre='<classpathentry exported=\"true\" kind=\"lib\" path=\"';post='\"/>'; print('\\t' + pre + (post + '\\n\\t' + pre).join(map(str.strip, sys.stdin.readlines())) + post)"`