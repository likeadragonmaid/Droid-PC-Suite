/*****************************************************************************
 * dpcs/Main.java: Main class for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2017 Karanvir Singh
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *****************************************************************************/

package dpcs;

import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class Main {
	@SuppressWarnings("static-access")
	public static void main(String args[]) throws Exception {
		System.out.println("Loading Droid PC Suite...");
		Splash image = new Splash();
		image.getContentPane().setBackground(Color.WHITE);
		image.setVisible(true);
		Thread thread = Thread.currentThread();
		thread.sleep(2500);
		image.dispose();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Interface().setVisible(true);
				try {
					System.out.println("Droid PC Suite loaded");
					System.out.println("Killing old running ADB instance, if running...");
					Process p1 = Runtime.getRuntime().exec("adb kill-server");
					p1.waitFor();
					File file1 = new File(".checkadbconnection");
					System.out.println("Starting connectivity service...");
					if (file1.exists() && !file1.isDirectory()) {
						file1.delete();
						File file2 = new File("su");
						System.out.println("Starting root detection service...");
						if (file2.exists() && !file2.isDirectory()) {
							file2.delete();
						}
					}
					System.out.println("Connectivity and root detection service started...");
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
