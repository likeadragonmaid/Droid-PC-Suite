![Droid PC Suite Logo](https://imagizer.imageshack.us/v2/xq90/922/jccKMG.png)
Droid PC Suite contains set of tools for administration of android smartphones or tablets that supports communication over USB using Android Debug Bridge Technology. It works on all major operating systems.

## Features:
* App Manager (Install/ Uninstall Apps & Bloatware)
* Sophisticated file manager
* ADB tools
* Take screenshots
* Record android device screen
* Flash any file using flasher
* Wipe partitions
* Unroot
* Reboot to different modes
* Unlock/ Lock bootloader
* View and save logcat
* Advanced backups
* Restore from backups
* Bypass “your” device security
* build.prop Editor
* ADB Terminal
* Unpack APKs
* Repack APKs
* Deodex APKs and Jars
* Sign APKs and Zips
* Zip Align APKs
* Cyptographic hash sums comparing and calculator

## How to build from source using eclipse:

1. Make sure that you are latest version of Java on your workstation
2. Git clone this project
3. Select a workspace and create a new java project using eclipse
4. Add the source to project
5. Add all the files to build path which are stored in /src/libs/
6. Run Main.java
7. Export the whole project as runnable JAR and package all the libraries
8. Copy the tools directory and paste it in the sae directory as of exported jar, or some features will not work as expected!
9. Click finish

### Windows
> After following all building steps, copy all adb and fastboot files to same directory as of exported Droid PC Suite binary. Then copy tools directory to Droid PC Suite directory.

### Linux/ MacOS/ BSD (Any Unix-Like OS)
> Make sure android-tools-adb and android-tools-fastboot are installed on your workstation (These packages can have different name for different distros or operating systems). After following all building steps, set Droid PC Suite binary as executable. Also copy tools directory to Droid PC Suite directory.

## Note: This project is compiled against "openjdk version 1.8.0_102" and requires Java 8 to work properly!
