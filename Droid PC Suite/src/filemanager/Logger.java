/*****************************************************************************
 * filemanager/Logger.java: Logger class for Droid PC Suite
 *****************************************************************************
 * Copyright (C) 2019 Shouko Komi
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

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	private static List logList;
	private File logFile;
	private static BufferedWriter writer;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

	@SuppressWarnings("static-access")
	public Logger(List logList) {
		this.logList = logList;
		try {
			logFile = new File(".log.txt");
			File oldLog = new File(".lastLog.txt");
			if (oldLog.exists()) {
				oldLog.delete();
			}
			logFile.renameTo(new File(".lastLog.txt"));
			writer = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e) {
			writeLogfile("cant initialize logger - please manually remove logfile and restart File Manager");
		}
	}

	public static void writeToLog(String msg) {
		logList.add(msg);
		logList.select(logList.getItemCount() - 1);
		writeLogfile(msg);
	}

	private static void writeLogfile(String msg) {
		Calendar now = Calendar.getInstance();

		try {
			writer.write(sdf.format(now.getTime()) + ": " + msg + System.lineSeparator());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
