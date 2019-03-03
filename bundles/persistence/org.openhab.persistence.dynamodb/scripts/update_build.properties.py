#!/usr/bin/env python3
import glob
import re

jars = sorted(glob.glob('lib/*.jar'))

# 1. Read current build.properties
with open('build.properties') as f:
    build_props = f.read()
# 2. Find reference to dynamodb library and replace it with placeholder
build_props = re.sub(r'[ ]*lib/aws-java-sdk-dynamodb.*',
                     'LIBS_HERE', build_props)
# 3. Remove all lib references
build_props = ''.join(
    filter(lambda line: 'lib/' not in line, build_props.splitlines(True)))

# 4. Replace placeholder of step 2 with new jars
new_props = build_props.replace('LIBS_HERE',
                                '               '
                                + ',\\\n               '.join(jars))
# 5. write build.properties
with open('build.properties', 'w') as f:
    f.write(new_props)
