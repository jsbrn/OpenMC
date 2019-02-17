echo off
set folder=%1
set target="C:\Users\Jeremy\Documents\Github\OpenMC\target"
echo Deleting OpenMC.jar
del %source%\OpenMC.jar
echo Renaming shaded jar
ren %source%\original-OpenMC.jar OpenMC.jar
echo Copying to plugins folder
robocopy %source% %target% OpenMC.jar /MOV