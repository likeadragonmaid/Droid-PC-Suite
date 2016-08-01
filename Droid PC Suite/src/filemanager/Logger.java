package filemanager;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

		} catch (Exception e) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}