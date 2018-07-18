<p align="center">
  <img src="https://raw.githubusercontent.com/kvsjxd/Droid-PC-Suite/master/Droid%20PC%20Suite/src/graphics/Splash.png">
</p>

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

Droid PC Suite contains set of tools for administration of android smartphones or tablets that supports communication over USB using Android Debug Bridge Technology. It works on all major operating systems.

# [Download](https://github.com/kvsjxd/Droid-PC-Suite/releases/download/1.9/DPCS.v1.9.Stable.zip)

## Features:
* App Manager (Install/Uninstall Apps + Bloatware)
* Sophisticated file manager
* ADB tools
* Take screenshots
* Record android device screen
* Flash any file using flasher
* Wipe partitions
* Unroot
* Reboot to different modes
* Unlock/ Lock bootloader
* Advanced backups
* Restore from backups
* Bypass 'your' device security (Android 4.4 and below)
* build.prop Editor
* Unpack APKs
* Repack APKs
* Cryptographic hash sums comparing and calculator
* Clear battery stats
* Launch activities of the apps
* ifconfig and netstat
* View running processes
* Bundled ADB, Fastboot, sqlite3 and various other related tools for Windows

<p align="center">
  <img src="https://github.com/kvsjxd/Droid-PC-Suite/raw/gh-pages/images/1.png">
</p>

## How to build from source using eclipse:
1. Make sure that you are using latest version of OpenJDK 8 or Oracle JDK 8 on your workstation
2. Git clone this project
3. Select a workspace and create a new java project using eclipse
4. Add the source to project
5. Add all the files to build path which are stored in /../src/libs/
6. Run Main.java
7. Export the whole project as runnable JAR and package all the libraries
8. Click finish

### Windows
> After following all building steps, copy all adb and fastboot files to same directory as of exported Droid PC Suite binary.
[You may like to download latest adb and fastboot for Windows directly from google](https://dl.google.com/android/repository/platform-tools-latest-windows.zip)

### GNU/Linux (any unix-like OS)
> Make sure that android-tools-adb and android-tools-fastboot are installed on your workstation (These packages can have different name for different distros or operating systems). After following all building steps, set Droid PC Suite binary as executable. [You may like to download latest adb and fastboot for GNU/Linux directly from google](https://dl.google.com/android/repository/platform-tools-latest-linux.zip)

### MacOS
> Make sure that you have installed latest adb and fastboot on your Mac and set Droid PC Suite binary as executable before trying to run Droid PC Suite. [You may like to download latest adb and fastboot for MacOS directly from google](https://dl.google.com/android/repository/platform-tools-latest-darwin.zip)
## Note: This project's releases are compiled against "openjdk version 1.8.0_144" and requires Java 8 to be installed on your computer to work properly. Using Java 9 or higher will result in DPCS getting crashed due to Unhandled exceptions, unless I release a patch please only use Java 8 to run DPCS.
Having trouble with /suggestions for /tips for /tricks with Droid PC Suite? Or just want to say Hi or Thanks? [Check out XDA-Developers forum](http://forum.xda-developers.com/android/development/tool-droid-pc-suite-t3398599)

## Issues on devices shipping with Google's Project Treble:
Since I'm not investing anymore of my time on this project, there is a high chance that some of the features may not work on Project Treble enabled devices since they have a few changes in their file system partitioning scheme. The features in DPCS that directly rely on /system partition to work are most likely broken.
