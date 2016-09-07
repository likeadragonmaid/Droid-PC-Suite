package updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
			URL url1 = new URL("https://raw.githubusercontent.com/kvsjxd/Droid-PC-Suite/master/.release-version.txt");
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
			double AppVersion = Double.parseDouble(tmp);
			if (AvailableUpdate > AppVersion) {
				System.out.println("Your Droid PC Suite version: V" + AppVersion);
				System.out.println(
						"New update V" + AvailableUpdate + " is available! Please download latest version now!");
				URL url2 = new URL(
						"https://raw.githubusercontent.com/kvsjxd/Droid-PC-Suite/master/.release-changelog.txt");
				ReadableByteChannel obj2 = Channels.newChannel(url2.openStream());
				FileOutputStream outputstream2 = new FileOutputStream(".release-changelog.txt");
				outputstream2.getChannel().transferFrom(obj2, 0, Long.MAX_VALUE);
				UpdaterGUI obj = new UpdaterGUI();
				obj.setVisible(true);
			} else {
				System.out.println("You are running latest Droid PC Suite...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}