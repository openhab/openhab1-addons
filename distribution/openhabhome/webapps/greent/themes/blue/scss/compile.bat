@echo off
echo Compiling SCSS...
call compass compile . -c ./config.rb
rmdir /s /q .sass-cache
echo ---- DONE! ----
