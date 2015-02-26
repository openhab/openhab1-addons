#!/bin/bash

# Use defaults
COMPRESS_OPTIONS=""

# Extend list for more scripts
declare -a files=(
	"web/WebApp/Action/Logic.js"
)

# Check uglify installation
command -v uglifyjs >/dev/null 2>&1 || { echo >&2 "You need to have nodejs and uglifyjs2 (https://github.com/mishoo/UglifyJS2) installed!"; exit 1; }

echo "Minifying JavaScript"

for file in "${files[@]}"; do
	minFile=${file//.js/.min.js}
	echo "$file -> $minFile: "
	uglifyjs --compress $COMPRESS_OPTIONS --output $minFile -- $file
done
