#!/bin/bash
echo "Compiling SCSS..."
compass compile . -c ./config.rb -f
rm -rf ./.sass-cache
echo "---- DONE! ----"
