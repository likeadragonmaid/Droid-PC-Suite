package dpcs;

import java.io.File;

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
		thread.sleep(5000);
		image.dispose();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Interface().setVisible(true);
				try {
					System.out.println("Droid PC Suite loaded");
					System.out.println("Killing old running adb instance, if any...");
					Process p1 = Runtime.getRuntime().exec("adb kill-server");
					p1.waitFor();
					File file1 = new File(".checkadbconnection");
					System.out.println("Resetting connectivity service...");
					if (file1.exists() && !file1.isDirectory()) {
						file1.delete();
						File file2 = new File("su");
						System.out.println("Starting root detection service...");
						if (file2.exists() && !file2.isDirectory()) {
							file2.delete();
						}
					}
					System.out.println("Connectivity and root detection service started...");
					System.out.println("Looking for connected devices...");
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});
	}
}
