#!/bin/bash
echo "Compiling ALL SCSS..."
for dir in ./*/
do
    dir=${dir%*/}
    echo "--------------"
    echo ${dir##*/}
    echo "--------------"
cd ${dir##*/}/scss
./compile.sh
cd ../../
done
#compass compile . -c ./config.rb -f
echo "---- DONE! ----"
