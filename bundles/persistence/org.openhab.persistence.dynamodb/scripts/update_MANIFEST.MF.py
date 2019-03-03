#!/usr/bin/env python3
import glob
import re

jars = sorted(glob.glob('lib/*.jar'))

# 1. Read current MANIFEST.MF
with open('META-INF/MANIFEST.MF') as f:
    manifest = f.read()
# 2. Find reference to dynamodb library and replace it with placeholder
manifest = re.sub(r'[ ]*lib/aws-java-sdk-dynamodb.*',
                  'LIBS_HERE', manifest)
# 3. Remove all lib references
manifest = ''.join(
    filter(lambda line: 'lib/' not in line, manifest.splitlines(True)))

# 4. Replace placeholder of step 2 with new jars
new_props = manifest.replace('LIBS_HERE',
                             '  '
                             + ',\n  '.join(jars))
# 5. write MANIFEST.MF
with open('META-INF/MANIFEST.MF', 'w') as f:
    f.write(new_props)
