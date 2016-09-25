/*****************************************************************************
 * dpcs/Repacker.java: Repacker class for Droid PC Suite
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

package dpcs;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Repacker {

	public Repacker() {
		try {
			File PathOfDirectoryToBeZipped = null;
			JFileChooser chooser1 = new JFileChooser();
			chooser1.setDialogTitle("Select folder with APK contents");
			chooser1.setCurrentDirectory(new java.io.File("."));
			chooser1.setAcceptAllFileFilterUsed(false);
			chooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int userSelection = chooser1.showOpenDialog(chooser1);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				PathOfDirectoryToBeZipped = chooser1.getSelectedFile();
			}
			ZipFile zipFile = new ZipFile(PathOfDirectoryToBeZipped + ".apk");
			ZipParameters parameters = new ZipParameters();
			parameters.setIncludeRootFolder(false);
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zipFile.addFolder(PathOfDirectoryToBeZipped, parameters);
			JOptionPane.showMessageDialog(null,
					"APK repacked successfully! Saved at\n" + PathOfDirectoryToBeZipped + ".apk");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}