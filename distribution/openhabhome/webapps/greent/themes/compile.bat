@echo off
echo Compiling ALL SCSS...

for  /d %%f in (*) do (
 pushd "%%f/scss"
 echo --------------
 echo compile %%f/scss
 call compile.bat
 echo --------------
 popd
)

echo ---- ALL DONE! ----

