/*****************************************************************************
 * filemanager/FileManager.java: FileManager main class for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2016 Karanvir Singh
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

package filemanager;

import java.io.File;
import java.util.ArrayList;
import filemanager.Explorer;
import java.awt.Toolkit;

public class FileManager {
	public static void main(String[] args) {
		Explorer explorer = new Explorer();
		explorer.setIconImage(Toolkit.getDefaultToolkit().getImage(FileManager.class.getResource("/graphics/Icon.png")));
	}

	public static void restartApplication() throws Exception {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());

		if (!currentJar.getName().endsWith(".jar"))
			return;

		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
}
