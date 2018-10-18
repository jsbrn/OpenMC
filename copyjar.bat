echo off
set source="C:\Users\Main\Documents\OpenMC\target"
set target="C:\Users\Main\Desktop\OpenMC_Server\plugins"
echo Deleting OpenMC.jar
del %source%\OpenMC.jar
echo Renaming shaded jar
ren %source%\OpenMC-dev-shaded.jar OpenMC.jar
echo Copying to plugins folder
robocopy %source% %target% OpenMC.jar /MOV