/*****************************************************************************
 * filemanager/DataReceiver.java: DataReceiver class for Droid PC Suite
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class DataReceiver {
	private String selectedDevice = "";
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
	private File saveLocation;
	private static Properties probs;

	public DataReceiver() {
		probs = new Properties();
		try {
			probs.load(new FileReader(new File(".explorer.properties")));
			saveLocation = new File(probs.getProperty("saveLocation"));
		} catch (FileNotFoundException e) {
			setSaveLocation(new File(getSaveLocation()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		BufferedReader reader = null;
		InputStream processIn = null;
		try {
			new ProcessBuilder("adb", "root").start();
			Logger.writeToLog("adb started as root");
			Process process = new ProcessBuilder("adb", "version").start();
			processIn = process.getInputStream();
			reader = new BufferedReader(new InputStreamReader(processIn));
			Logger.writeToLog(reader.readLine());
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog(e.getMessage());
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (processIn != null)
					processIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayList<String> getDevices(boolean log) {
		ArrayList<String> ret = new ArrayList<String>();
		InputStream processIN = null;
		BufferedReader br = null;
		try {
			Process process = new ProcessBuilder("adb", "devices").start();
			processIN = process.getInputStream();
			br = new BufferedReader(new InputStreamReader(processIN));

			br.readLine();
			String line;
			while ((line = br.readLine()) != null && line.length() > 0) {
				String[] split = line.split("\t");
				if (split.length >= 1) {
					ret.add(split[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog(e.getMessage());
		} finally {
			try {
				if (br != null)
					br.close();
				if (processIN != null)
					processIN.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (log) {
			Logger.writeToLog(ret.size() + " devices found");
		}
		return ret;
	}

	public void connectDevice(final String ip) {
		if (ip != null && ip.length() > 0) {
			try {
				new ProcessBuilder("adb", "connect", ip).start();
				Logger.writeToLog(ip + " connected");
			} catch (Exception e) {
				e.printStackTrace();
				Logger.writeToLog(e.getMessage());
			}
		}
	}

	public ArrayList<FileObj> getDirContent(String dir) {
		if (selectedDevice.length() <= 0) {
			return null;
		}
		ArrayList<FileObj> ret = new ArrayList<FileObj>();

		Process process;
		BufferedReader br = null;
		InputStream processIN = null;
		try {
			process = new ProcessBuilder("adb", "-s", selectedDevice, "ls", dir).start();
			processIN = process.getInputStream();
			br = new BufferedReader(new InputStreamReader(processIN));

			String line;

			if (br.readLine() == null) {
				return null;
			}
			br.readLine();

			while ((line = br.readLine()) != null) {
				if (line.length() > 24) {

					String[] split = line.split(" ");

					long lastEdit = Long.parseLong(split[2], 16);
					Calendar date = Calendar.getInstance();
					date.setTimeInMillis((lastEdit * 1000));

					String name = "";

					for (int i = 27; i < line.length(); i++) {
						name += line.charAt(i);
					}

					ret.add(new FileObj(name, dir, sdf.format(date.getTime()), Long.parseLong(split[1], 16), false,
							false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog(e.getMessage());
		} finally {
			try {
				if (br != null)
					br.close();
				if (processIN != null)
					processIN.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	public boolean setSelectedDevice(String selectedDevice) {
		if (!this.selectedDevice.equals(selectedDevice)) {
			this.selectedDevice = selectedDevice;
			return true;
		}
		return false;
	}

	public File pullFile(String path) throws InterruptedException {
		try {
			String dest = saveLocation.getAbsolutePath();
			if (!saveLocation.exists()) {
				saveLocation.mkdirs();
			}
			String[] splits = path.split("/");
			if (path.endsWith("/")) {
				String interrimsPath = "";
				for (int i = 0; i < splits.length - 1; i++) {
					interrimsPath += splits[i] + "/";
				}
				path = interrimsPath + splits[splits.length - 1];
			}

			Process p = new ProcessBuilder("adb", "-s", selectedDevice, "pull", path, dest).start();
			p.waitFor();
			Logger.writeToLog(path + " pulled to " + dest);

			return new File(dest + "\\" + splits[splits.length - 1]);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog("failed to pull " + path);
			Logger.writeToLog(e.getMessage());
			return null;
		}
	}

	public void pushFile(String source, String destination) throws InterruptedException {
		try {
			Process process = new ProcessBuilder("adb", "-s", selectedDevice, "push", source, destination).start();
			process.waitFor();
			Logger.writeToLog(source + "failed to push " + destination);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog("failed to save properties" + source);
			Logger.writeToLog(e.getMessage());
		}
	}

	public void deleteFile(String path) throws InterruptedException {
		Process p;
		InputStream processIN = null;
		BufferedReader reader = null;
		try {
			p = new ProcessBuilder("adb", "-s", selectedDevice, "ls", path).start();
			processIN = p.getInputStream();
			reader = new BufferedReader(new InputStreamReader(processIN));
			String line = reader.readLine();
			if (line == null) {
				Logger.writeToLog("deleting " + path);
				Process process = new ProcessBuilder("adb", "-s", selectedDevice, "shell", "rm", path).start();
				process.waitFor();
			} else {
				reader.readLine();
				ArrayList<String> filesToDelete = new ArrayList<String>();

				while ((line = reader.readLine()) != null) {
					String[] splits = line.split(" ");
					filesToDelete.add(path + "/" + splits[3]);
				}
				if (filesToDelete.size() == 0) {
					Logger.writeToLog("deleting " + path);
					Process process = new ProcessBuilder("adb", "-s", selectedDevice, "shell", "rmdir", path).start();
					process.waitFor();
					return;
				} else {
					for (String toDel : filesToDelete) {
						deleteFile(toDel);
					}
					deleteFile(path);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog("failed to delete " + path);
			Logger.writeToLog(e.getMessage());
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (processIN != null)
					processIN.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getLanguage() {
		return probs.getProperty("language", "english");
	}

	public String getSaveLocation() {
		if (saveLocation != null) {
			return saveLocation.getAbsolutePath();
		} else {
			return System.getProperty("user.dir") + "\\pull";
		}
	}

	public void setSaveLocation(File saveLocation) {
		this.saveLocation = saveLocation;
		setProperty("saveLocation", this.saveLocation.getAbsolutePath());
	}

	public static void setProperty(String key, String value) {
		probs.setProperty(key, value);
		try {
			probs.store(new FileWriter(new File(".explorer.properties")), "");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.writeToLog("failed to delete ");
			Logger.writeToLog(e.getMessage());
		}
	}
}
