echo off
set source="C:\Users\Main\Documents\GitHub\OpenMC\target"
set target="C:\Users\Main\Documents\OpenMCTestServer\plugins"
echo Deleting OpenMC.jar
del %source%\OpenMC.jar
echo Renaming shaded jar
ren %source%\OpenMC-dev-shaded.jar OpenMC.jar
echo Copying to plugins folder
robocopy %source% %target% OpenMC.jar /MOV