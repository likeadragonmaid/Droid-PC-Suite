/*****************************************************************************
 * updater/Updater.java: Updater initialization logic for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2019 Shou
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.apache.commons.io.IOUtils;

public class Updater {

	@SuppressWarnings("resource")
	public Updater() {
		try {
			System.out.println("Checking for updates...");
			URL url1 = new URL("https://raw.githubusercontent.com/gibcheesepuffs/Droid-PC-Suite/master/.release-version.txt");
			ReadableByteChannel obj1 = Channels.newChannel(url1.openStream());
			FileOutputStream outputstream1 = new FileOutputStream(".release-version.txt");
			outputstream1.getChannel().transferFrom(obj1, 0, Long.MAX_VALUE);
			FileReader file = new FileReader(".release-version.txt");
			BufferedReader reader = new BufferedReader(file);
			String DownloadedString = reader.readLine();
			File file2 = new File(".release-version.txt");
			if (file2.exists() && !file2.isDirectory()) {
				file2.delete();
			}
			double AvailableUpdate = Double.parseDouble(DownloadedString);
			InputStreamReader reader2 = new InputStreamReader(
					getClass().getResourceAsStream("/others/app-version.txt"));
			String tmp = IOUtils.toString(reader2);
			double ApplicationVersion = Double.parseDouble(tmp);
			if (AvailableUpdate > ApplicationVersion) {
				System.out.println("Your Droid PC Suite version: V" + ApplicationVersion);
				System.out.println(
						"New update V" + AvailableUpdate + " is available! Please download latest version now!");
				URL url2 = new URL(
						"https://raw.githubusercontent.com/gibcheesepuffs/Droid-PC-Suite/master/.release-changelog.txt");
				ReadableByteChannel obj2 = Channels.newChannel(url2.openStream());
				FileOutputStream outputstream2 = new FileOutputStream(".release-changelog.txt");
				outputstream2.getChannel().transferFrom(obj2, 0, Long.MAX_VALUE);
				UpdaterGUI obj = new UpdaterGUI();
				obj.setVisible(true);
			} else {
				System.out.println(
						"You are running an unofficial or a custom build of Droid PC Suite...\nUse official builds for support! Download here:\nhttps://forum.xda-developers.com/android/development/tool-droid-pc-suite-t3398599");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
