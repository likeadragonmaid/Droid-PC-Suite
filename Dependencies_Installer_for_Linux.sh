#!/bin/bash
echo Updating apt-get cache...
sudo apt-get update
echo Downloading and installing dependencies...
sudo apt-get install android-tools-adb android-tools-fastboot
uname -a
read -p "Is this 64 bit system? yes or no? ";
if [ $REPLY == "yes" ]; then
	echo Now installing lib32ncurses5 and lib32z1
	sudo apt-get install lib32ncurses5 lib32z1
fi
echo Dependencies installed successfully!
echo Installed ADB Version:
adb version
read -p "Is Java 1.8 or above installed on your system? yes or no? ";
if [ $REPLY == "no" ]; then
	echo Now installing Java 1.8...
	sudo apt-get install openjdk-8-jre openjdk-8-jdk
	echo Java has been installed sucessfully!
fi
echo Installed Java version :
java -version
echo Please ensure that java version "1.8.0_91" or higher is installed on your system!