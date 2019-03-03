#!/usr/bin/env python3
import glob
import re

jars = sorted(glob.glob('lib/*.jar'))

# 1. Read current .classpath
with open('.classpath') as f:
    classpath = f.read()
# 2. Find reference to dynamodb library and replace it with placeholder
classpath = re.sub(r'.*lib/aws-java-sdk-dynamodb.*',
                   'LIBS_HERE', classpath)
# 3. Remove all lib references
classpath = ''.join(
    filter(lambda line: 'lib/' not in line, classpath.splitlines(True)))

# 4. Replace placeholder of step 2 with new jars
import sys
pre = '<classpathentry exported=\"true\" kind=\"lib\" path=\"'
post = '\"/>'
new_classpath_entries = ('\t' + pre + (post + '\n\t' +
                                       pre).join(map(str.strip, jars)) + post)
new_classpath = classpath.replace('LIBS_HERE', new_classpath_entries)
# 5. write .classpath
with open('.classpath', 'w') as f:
    f.write(new_classpath)
