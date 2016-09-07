/*****************************************************************************
 * dpcs/Interface.java: The main interface for Droid PC Suite
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

@SuppressWarnings("serial")
public class Interface extends JFrame {
	JLabel FlasherDone, GeneralDone, WiperDone, BackupAndRestoreDone, ADBConnectionLabel, RootStatusLabel, AppStatus;
	JTextArea LogViewer, CalculatedCrypto, InputCrypto;
	boolean adbconnected = false, rooted = false;
	private JPanel contentPane;

	volatile boolean flag = true;
	Runnable r = new Runnable() {
		public void run() {
			while (flag) {
				try {
					adbconnected = false;
					Process p1 = Runtime.getRuntime().exec("adb devices");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb shell touch /sdcard/.CheckADBConnection");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb pull /sdcard/.CheckADBConnection");
					p3.waitFor();
					Process p4 = Runtime.getRuntime().exec("adb shell rm /sdcard/.CheckADBConnection");
					p4.waitFor();
					File file = new File(".CheckADBConnection");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						adbconnected = true;
						JTextArea Devicelistviewer = new JTextArea();
						Devicelistviewer.setEditable(false);
						Devicelistviewer.setOpaque(false);
						Process p5 = Runtime.getRuntime().exec("adb devices -l");
						p5.waitFor();
						int i = 0;
						String line;
						String[] array = new String[1024];
						BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
						while ((line = reader.readLine()) != null) {
							array[i] = line;
							Devicelistviewer.append(line);
						}
						if (Devicelistviewer.getLineCount() > 1) {
							JOptionPane.showMessageDialog(null,
									Devicelistviewer.getLineCount()
											+ " devices detected!\nOnly 1 device is allowed at a time!\nPlease disconnect other devices!",
									"Error", JOptionPane.ERROR_MESSAGE);
						}
						ADBConnectionLabel.setText("Device is connected");
					} else {
						adbconnected = false;
						ADBConnectionLabel.setText("Connect your device");
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
				try {
					File file = new File("su");
					Process p1 = Runtime.getRuntime().exec("adb pull /system/xbin/su");
					p1.waitFor();
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						rooted = true;
						RootStatusLabel.setText("Device is rooted");
					} else {
						if (adbconnected == true) {
							rooted = false;
							RootStatusLabel.setText("Device is not rooted");
						} else {
							rooted = false;
							RootStatusLabel.setText("");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		}
	};

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Interface() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Interface.class.getResource("/graphics/Icon.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				File file = new File(".logcat.txt");
				if (file.exists() && !file.isDirectory()) {
					file.delete();
				}
				File file2 = new File(".userapps.txt");
				if (file2.exists() && !file2.isDirectory()) {
					file2.delete();
				}
				File file3 = new File(".privapps.txt");
				if (file3.exists() && !file3.isDirectory()) {
					file3.delete();
				}
				File file4 = new File(".systemapps.txt");
				if (file4.exists() && !file4.isDirectory()) {
					file4.delete();
				}
			}
		});

		setTitle("Droid PC Suite");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1088, 715);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		JMenuItem mntmExit = new JMenuItem("Exit");

		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenu mnADBandFastbootTools = new JMenu("ADB and Fastboot tools");
		mnMenu.add(mnADBandFastbootTools);
		mnADBandFastbootTools.setToolTipText("Access various ADB and Fastboot tools");

		JMenuItem mntmDevicestate = new JMenuItem("View device state");
		mntmDevicestate.setToolTipText("Check android device state");
		mntmDevicestate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb get-state");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, "State: " + reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmAdbHelp = new JMenuItem("View ADB help");
		mntmAdbHelp.setToolTipText("Get help regarding ADB");
		mntmAdbHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ADBHelp obj = new ADBHelp();
				obj.setVisible(true);
			}
		});

		JMenuItem mntmNoOfUsers = new JMenuItem("Max user(s) supported?");
		mntmNoOfUsers.setToolTipText("Max no. of user(s) supported by android device");
		mntmNoOfUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell pm get-max-users");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnADBandFastbootTools.add(mntmNoOfUsers);
		mnADBandFastbootTools.add(mntmAdbHelp);

		JMenuItem mntmAdbVersion = new JMenuItem("View ADB version");
		mntmAdbVersion.setToolTipText("Check the version of ADB installed on your computer");
		mnADBandFastbootTools.add(mntmAdbVersion);
		mntmAdbVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb version");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmViewDeviceList = new JMenuItem("View connected device");
		mntmViewDeviceList.setToolTipText(
				"Displays connected device, it will show name and serial no. of the only connected device because of connectivity limit");
		mntmViewDeviceList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JTextArea Devicelistviewer = new JTextArea();
					Devicelistviewer.setEditable(false);
					Devicelistviewer.setForeground(Color.BLACK);
					Devicelistviewer.setOpaque(false);
					Process p1 = Runtime.getRuntime().exec("adb devices -l");
					p1.waitFor();
					int i = 0;
					String line;
					String[] array = new String[1024];
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					while ((line = reader.readLine()) != null) {
						array[i] = line;
						Devicelistviewer.append(line + "\n");
					}
					JOptionPane.showMessageDialog(null, Devicelistviewer);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnADBandFastbootTools.add(mntmViewDeviceList);
		mnADBandFastbootTools.add(mntmDevicestate);

		JMenuItem mntmViewFastbootHelp = new JMenuItem("View fastboot help");
		mntmViewFastbootHelp.setToolTipText("Get help regarding fastboot");
		mntmViewFastbootHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FastbootHelp obj = new FastbootHelp();
				obj.setVisible(true);
			}
		});
		mnADBandFastbootTools.add(mntmViewFastbootHelp);

		JMenuItem mntmSerialNo = new JMenuItem("View serial no.");
		mntmSerialNo.setToolTipText("Check ADB connectivity serial no. of your android device");
		mnADBandFastbootTools.add(mntmSerialNo);
		mntmSerialNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb get-serialno");
					p1.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					JOptionPane.showMessageDialog(null, "Serial No: " + reader.readLine());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmWaitForDevice = new JMenuItem("Wait for device");
		mntmWaitForDevice.setToolTipText("Ask ADB to wait for your device until the device can accept commands");
		mntmWaitForDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb wait-for-device");
					p1.waitFor();
					JOptionPane.showMessageDialog(null, "Waiting...");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnADBandFastbootTools.add(mntmWaitForDevice);

		JMenuItem mntmDeviceFeatures = new JMenuItem("Device features");
		mnMenu.add(mntmDeviceFeatures);
		mntmDeviceFeatures.setToolTipText("View list of features supported by the android device");
		mntmDeviceFeatures.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Devicefeatures obj = new Devicefeatures();
				obj.setVisible(true);
			}
		});

		JMenu mnDeviceSpecificTools = new JMenu("Device specific tools");
		mnMenu.add(mnDeviceSpecificTools);
		mnDeviceSpecificTools.setToolTipText("View tools which only work with few  or specific devices");

		JMenu mnHTC = new JMenu("HTC");
		mnDeviceSpecificTools.add(mnHTC);
		mnHTC.setToolTipText("View list of tools which only work with HTC devices");

		JMenuItem mntmGetCidNo = new JMenuItem("Get CID no.");
		mntmGetCidNo.setToolTipText("Get CID Number of the device");
		mntmGetCidNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb reboot fastboot");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("fastboot getvar cid");
					p2.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
					JOptionPane.showMessageDialog(null, reader.readLine());
					Process p3 = Runtime.getRuntime().exec("fastboot reboot");
					p3.waitFor();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenuItem mntmBootloaderRelatedInfo = new JMenuItem("Bootloader related info");
		mntmBootloaderRelatedInfo.setToolTipText("View CID No.,Main-ver, bootloader info Etc.");
		mntmBootloaderRelatedInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb reboot fastboot");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("fastboot getvar all");
					p2.waitFor();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
					JOptionPane.showMessageDialog(null, reader.readLine() + "\n");
					Process p3 = Runtime.getRuntime().exec("fastboot reboot");
					p3.waitFor();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnHTC.add(mntmBootloaderRelatedInfo);
		mnHTC.add(mntmGetCidNo);

		JMenuItem mntmWriteSuperCIDNo = new JMenuItem("Write Super CID no.");
		mntmWriteSuperCIDNo.setToolTipText("Write Super CID Number to device");
		mntmWriteSuperCIDNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int supercidno;
					Process p1 = Runtime.getRuntime().exec("adb reboot fastboot");
					p1.waitFor();
					supercidno = Integer.parseInt(JOptionPane.showInputDialog(null,
							"Enter the Super CID Number to be written :\nfor ex. 11111111"));
					Process p2 = Runtime.getRuntime().exec("fastboot oem writecid " + supercidno);
					p2.waitFor();
					JOptionPane.showMessageDialog(null, "Done, Click OK to reboot");
					Process p3 = Runtime.getRuntime().exec("fastboot reboot");
					p3.waitFor();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnHTC.add(mntmWriteSuperCIDNo);

		JMenu mnSamsung = new JMenu("Samsung");
		mnSamsung.setToolTipText("View list of tools which only work with Samsung devices");
		mnDeviceSpecificTools.add(mnSamsung);

		JMenuItem mntmDownloadMode = new JMenuItem("Download Mode");
		mntmDownloadMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot download");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		mntmDownloadMode.setToolTipText(
				"Reboot to Download Mode for flashing firmware to samsung device using Odin or Heimdall");
		mnSamsung.add(mntmDownloadMode);

		mnMenu.add(mntmExit);
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmCommonWorkarounds = new JMenuItem("Common workarounds");
		mntmCommonWorkarounds
				.setToolTipText("View solutions and tips to avoid the common problems while using this application");
		mntmCommonWorkarounds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Workarounds obj = new Workarounds();
				obj.setVisible(true);
			}
		});

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setToolTipText("Information about the application");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About obj = new About();
				obj.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);

		JMenuItem mntmCheckForUpdates = new JMenuItem("Check for updates");
		mntmCheckForUpdates.setToolTipText("Check for the new updates of this application");
		mntmCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Please download latest release from Github");
				try {
					Desktop.getDesktop().browse(new URL("https://github.com/kvsjxd/Droid-PC-Suite/releases").toURI());
				} catch (Exception e1) {
				}
			}
		});
		mnHelp.add(mntmCheckForUpdates);
		mnHelp.add(mntmCommonWorkarounds);

		JMenuItem mntmNeedHelp = new JMenuItem("Online help");
		mntmNeedHelp.setToolTipText("Get online help for Droid PC Suite");
		mntmNeedHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JOptionPane.showMessageDialog(null, "Post your queries on XDA-Developers thread");
					Desktop.getDesktop()
							.browse(new URL(
									"http://forum.xda-developers.com/android/development/tool-droid-pc-suite-t3398599")
											.toURI());
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});

		JMenuItem mntmForceConnect = new JMenuItem("Force connect");
		mnHelp.add(mntmForceConnect);
		mntmForceConnect.setToolTipText("Force connect android device to computer using ADB protocol");
		mntmForceConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Go to developer options and turn off android debugging and turn it on again");
				JOptionPane.showMessageDialog(null,
						"Now tap on Revoke USB debugging authorizations and confirm it by tapping OK on android device");
				JOptionPane.showMessageDialog(null, "Now disconnect your android device and reconnect it via USB");
				JOptionPane.showMessageDialog(null, "Reboot your device. After it completely boots up click OK");
				try {
					adbconnected = false;
					Process p1 = Runtime.getRuntime().exec("adb kill-server");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb devices");
					p2.waitFor();
					JOptionPane.showMessageDialog(null, "Check if your device asks to Allow USB debugging");
					JOptionPane.showMessageDialog(null,
							"If yes check always allow from this computer checkbox and tap OK on your android device");
					Process p3 = Runtime.getRuntime().exec("adb shell touch /sdcard/.CheckADBConnection");
					p3.waitFor();
					Process p4 = Runtime.getRuntime().exec("adb pull /sdcard/.CheckADBConnection");
					p4.waitFor();
					Process p5 = Runtime.getRuntime().exec("adb shell rm /sdcard/.CheckADBConnection");
					p5.waitFor();
					File file = new File(".CheckADBConnection");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						adbconnected = true;
						ADBConnectionLabel.setText("Device is connected");
						JOptionPane.showMessageDialog(null, "Success!");
					} else {
						adbconnected = false;
						ADBConnectionLabel.setText("");
						ADBConnectionLabel.setText("Connect your device");
						JOptionPane.showMessageDialog(null,
								"Please try again or perhaps try installing your android device adb drivers on PC");
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
				try {
					File file = new File("su");
					Process p1 = Runtime.getRuntime().exec("adb pull /system/xbin/su");
					p1.waitFor();
					if (file.exists() && !file.isDirectory()) {
						file.delete();
						rooted = true;
						RootStatusLabel.setText("Device is rooted");
					} else {
						if (adbconnected == true) {
							rooted = false;
							RootStatusLabel.setText("Device is not rooted");
						} else {
							rooted = false;
							RootStatusLabel.setText("");
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JMenu mnLegalInformation = new JMenu("Legal information");
		mnLegalInformation.setToolTipText("Vew legal information about the application");
		mnHelp.add(mnLegalInformation);

		JMenuItem mntmDroidPcSuite = new JMenuItem("Droid PC Suite license");
		mntmDroidPcSuite.setToolTipText("View Droid PC Suite licence");
		mntmDroidPcSuite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GPLLicense obj = new GPLLicense();
				obj.setVisible(true);
			}
		});
		mnLegalInformation.add(mntmDroidPcSuite);

		JMenuItem mntmOpenSourceLicenses = new JMenuItem("Open source licenses");
		mntmOpenSourceLicenses
				.setToolTipText("View other open source licences for other softwares used with this application");
		mntmOpenSourceLicenses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ApacheLicense obj = new ApacheLicense();
				obj.setVisible(true);
			}
		});
		mnLegalInformation.add(mntmOpenSourceLicenses);
		mnHelp.add(mntmNeedHelp);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		AppStatus = new JLabel("");
		AppStatus.setBounds(12, 230, 1062, 17);
		contentPane.add(AppStatus);

		RootStatusLabel = new JLabel("");
		RootStatusLabel.setBounds(921, 12, 153, 17);
		RootStatusLabel.setForeground(Color.RED);
		contentPane.add(RootStatusLabel);

		ADBConnectionLabel = new JLabel("");
		ADBConnectionLabel.setBounds(921, 0, 152, 17);
		ADBConnectionLabel.setForeground(Color.GREEN);
		contentPane.add(ADBConnectionLabel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 255, 1075, 447);
		contentPane.add(tabbedPane);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.WHITE);
		tabbedPane.addTab("General", null, panel_7, null);
		panel_7.setLayout(null);

		JButton btnADBTerminal = new JButton("ADB Terminal");
		btnADBTerminal.setToolTipText("Send commands to your android device via ADB protocol, EXPERIMENTAL!");
		btnADBTerminal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Terminal obj = new Terminal();
				obj.setVisible(true);
			}
		});

		JButton btnBuildpropeditor = new JButton("build.prop Editor");
		btnBuildpropeditor
				.setToolTipText("Editor for editing build properties of your android device, Use with Caution!");
		btnBuildpropeditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Buildpropeditor obj = new Buildpropeditor();
				obj.setVisible(true);
			}
		});
		btnBuildpropeditor.setBounds(541, 27, 220, 75);
		panel_7.add(btnBuildpropeditor);
		btnADBTerminal.setBounds(25, 27, 220, 75);
		panel_7.add(btnADBTerminal);

		JLabel lblNoteInstallationTo = new JLabel("# Only for android 4.4.x and higher");
		lblNoteInstallationTo.setBounds(25, 284, 1046, 15);
		panel_7.add(lblNoteInstallationTo);

		GeneralDone = new JLabel("");
		GeneralDone.setText("");
		GeneralDone.setBounds(766, 27, 300, 220);
		panel_7.add(GeneralDone);

		JButton btnFileManager = new JButton("File Manager");
		btnFileManager.setToolTipText("Access files on your android device");
		btnFileManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneralDone.setText("");
				filemanager.FileManager.main(null);
			}
		});
		btnFileManager.setBounds(25, 131, 220, 75);
		panel_7.add(btnFileManager);

		JLabel lblNeedsRoot = new JLabel(
				"* Needs root access, also may not work with some devices regardless of root access");
		lblNeedsRoot.setBounds(25, 311, 1046, 15);
		panel_7.add(lblNeedsRoot);

		JButton btnScreenshot = new JButton("Screenshot");
		btnScreenshot.setToolTipText("Screenshot your android device screen");
		btnScreenshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell screencap -p /sdcard/screenshot.png");
					p1.waitFor();
					JFileChooser directorychooser = new JFileChooser();
					directorychooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					directorychooser.setDialogTitle("Select path to save the screenshot");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png");
					directorychooser.setFileFilter(filter);
					directorychooser.setApproveButtonText("Save");
					int returnVal = directorychooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/screenshot.png "
								+ directorychooser.getSelectedFile().getAbsolutePath());
						p2.waitFor();
					}
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/screenshot.png");
					p3.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnScreenshot.setBounds(282, 131, 220, 75);
		panel_7.add(btnScreenshot);

		JButton btnScreenRecorder = new JButton("Screen Recorder #");
		btnScreenRecorder.setToolTipText("Record android device screen");
		btnScreenRecorder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] options = new String[] { "5 Sec", "30 Sec", "60 Sec", "180 Sec", "Custom" };
				int response = JOptionPane.showOptionDialog(null, "Select duration of recording", "Screen Recorder",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				int time = 0, bitrate = 8000000;
				try {
					if (response == 0) {
						time = 5;
					}
					if (response == 1) {
						time = 30;
					}
					if (response == 2) {
						time = 60;
					}
					if (response == 3) {
						time = 180;
					}
					if (response == 4) {
						time = Integer.parseInt(JOptionPane.showInputDialog(null,
								"Enter the duration of recording in seconds (1 - 180): for ex. 25 for 25 Seconds"));
						bitrate = Integer.parseInt(JOptionPane.showInputDialog(null,
								"Enter the bitrate of recording (Default = 8000000 (8Mbps))"));
					}
					JOptionPane.showMessageDialog(null, "You will need to wait for " + time + " seconds, Click ok");
					Process p1 = Runtime.getRuntime().exec("adb shell screenrecord --bit-rate " + bitrate
							+ " --time-limit " + time + " /sdcard/videorecording.mp4");
					p1.waitFor();
					JOptionPane.showMessageDialog(null, "Recording finished, Select destination to save the file");
					JFileChooser directorychooser = new JFileChooser();
					directorychooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					directorychooser.setDialogTitle("Select path to save the recording");
					FileNameExtensionFilter filter = new FileNameExtensionFilter("MP4 Files", "mp4");
					directorychooser.setFileFilter(filter);
					directorychooser.setApproveButtonText("Save");
					int returnVal = directorychooser.showOpenDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/videorecording.mp4 "
								+ directorychooser.getSelectedFile().getAbsolutePath());
						p2.waitFor();
					}
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/videorecording.mp4");
					p3.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnScreenRecorder.setBounds(541, 131, 220, 75);
		panel_7.add(btnScreenRecorder);

		JButton btnAppManager = new JButton("App Manager");
		btnAppManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneralDone.setText("");
				String[] MainOptions = new String[] { "Install apps", "Uninstall apps" };
				int MainResponse = JOptionPane.showOptionDialog(null, "Select an operation", "App Manager",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, MainOptions, MainOptions[0]);
				if (MainResponse == 0) {
					try {
						GeneralDone.setText("");
						String[] options = new String[] { "User apps", "Priv-apps", "System apps" };
						int response = JOptionPane.showOptionDialog(null, "Where to install the app?", "Installer",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
						if (response == 0) {
							try {
								GeneralDone.setText("");
								JFileChooser chooser = new JFileChooser();
								FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
								chooser.setFileFilter(filter);
								int returnVal = chooser.showOpenDialog(getParent());
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File file = chooser.getSelectedFile();
									String filename = chooser.getSelectedFile().getName();
									try {
										AppStatus.setText("Installing...");
										String[] commands = new String[3];
										commands[0] = "adb";
										commands[1] = "install";
										commands[2] = file.getAbsolutePath();
										AppStatus.setText("Installing App...");
										Process p1 = Runtime.getRuntime().exec(commands, null);
										p1.waitFor();
										AppStatus.setText(
												filename + " has been successfully installed on your android device!");
										GeneralDone.setIcon(
												new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
									} catch (Exception e1) {
										System.err.println(e1);
									}
								}
							} catch (Exception e1) {
							}
						}
						if (response == 1) {
							GeneralDone.setText("");
							JFileChooser chooser = new JFileChooser();
							FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
							chooser.setFileFilter(filter);
							int returnVal = chooser.showOpenDialog(getParent());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = chooser.getSelectedFile();
								try {
									AppStatus.setText("Installing...");
									Process p1 = Runtime.getRuntime().exec("adb remount");
									p1.waitFor();
									String[] pushcommand = new String[4];
									pushcommand[0] = "adb";
									pushcommand[1] = "push";
									pushcommand[2] = file.getAbsolutePath();
									pushcommand[3] = "/system/priv-app/";
									AppStatus.setText("Installing App...");
									Process p2 = Runtime.getRuntime().exec(pushcommand, null);
									p2.waitFor();
									AppStatus.setText("Rebooting your android device");
									Process p3 = Runtime.getRuntime().exec("adb reboot");
									p3.waitFor();
									AppStatus.setText("");
									GeneralDone
											.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
								} catch (Exception e1) {
									System.err.println(e1);
								}
							}
						}
						if (response == 2) {
							GeneralDone.setText("");
							JFileChooser chooser = new JFileChooser();
							FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
							chooser.setFileFilter(filter);
							int returnVal = chooser.showOpenDialog(getParent());
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								File file = chooser.getSelectedFile();
								try {
									AppStatus.setText("Installing...");
									Process p1 = Runtime.getRuntime().exec("adb remount");
									p1.waitFor();
									String[] pushcommand = new String[4];
									pushcommand[0] = "adb";
									pushcommand[1] = "push";
									pushcommand[2] = file.getAbsolutePath();
									pushcommand[3] = "/system/app/";
									Process p2 = Runtime.getRuntime().exec(pushcommand, null);
									p2.waitFor();
									AppStatus.setText("Rebooting your android device");
									Process p3 = Runtime.getRuntime().exec("adb reboot");
									p3.waitFor();
									AppStatus.setText("");
									GeneralDone
											.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
								} catch (Exception e1) {
									System.err.println(e1);
								}
							}
						}
					} catch (Exception e1) {
					}
				}
				if (MainResponse == 1) {
					try {
						GeneralDone.setText("");
						String[] options = new String[] { "User apps", "Priv-apps", "System apps" };
						int response = JOptionPane.showOptionDialog(null, "Which kind of app you want to uninstall?",
								"Uninstaller", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
								options[0]);
						if (response == 0) {
							try {
								UninstallUserApps obj = new UninstallUserApps();
								obj.setVisible(true);
							} catch (Exception e1) {
							}
						}
						if (response == 1) {
							try {
								UninstallPrivApps obj = new UninstallPrivApps();
								obj.setVisible(true);
							} catch (Exception e1) {
							}
						}
						if (response == 2) {
							try {
								UninstallSystemApps obj = new UninstallSystemApps();
								obj.setVisible(true);
							} catch (Exception e1) {
							}
						}
					} catch (Exception e1) {
					}
				}
			}
		});
		btnAppManager.setToolTipText("Manage Apps on your android device");
		btnAppManager.setBounds(282, 27, 220, 75);
		panel_7.add(btnAppManager);

		JLabel lblInstallationAndUninstallation = new JLabel(
				"Installation and Uninstallation of apps to Priv-app is only for android 4.4 and higher, requires root and even simply may no work on your device!");
		lblInstallationAndUninstallation.setBounds(25, 365, 1046, 15);
		panel_7.add(lblInstallationAndUninstallation);

		JLabel lblInstallationAndUninstallation_1 = new JLabel(
				"Installation and Uninstallation of apps to System requires root, and may not work for your device!");
		lblInstallationAndUninstallation_1.setBounds(25, 338, 1046, 15);
		panel_7.add(lblInstallationAndUninstallation_1);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(Color.WHITE);
		tabbedPane.addTab("Advanced", null, panel_8, null);
		panel_8.setLayout(null);

		JButton btnMemoryInformation = new JButton("Memory Information");
		btnMemoryInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Memoryinfo obj = new Memoryinfo();
				obj.setVisible(true);
			}
		});
		btnMemoryInformation.setToolTipText("View current memory information of android device");
		btnMemoryInformation.setBounds(541, 131, 220, 75);
		panel_8.add(btnMemoryInformation);

		JButton btnBatteryInformation = new JButton("Battery Information");
		btnBatteryInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Batteryinfo obj = new Batteryinfo();
				obj.setVisible(true);
			}
		});
		btnBatteryInformation.setToolTipText("View current battery information of android device");
		btnBatteryInformation.setBounds(541, 27, 220, 75);
		panel_8.add(btnBatteryInformation);

		JButton btnCpuInformation = new JButton("CPU Information");
		btnCpuInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CPUinfo obj = new CPUinfo();
				obj.setVisible(true);
			}
		});
		btnCpuInformation.setToolTipText("View current CPU information of android device");
		btnCpuInformation.setBounds(25, 131, 220, 75);
		panel_8.add(btnCpuInformation);

		JButton btnAppInformation = new JButton("App Information");
		btnAppInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Appinfo obj = new Appinfo();
				obj.setVisible(true);
			}
		});
		btnAppInformation.setToolTipText("View current app information of android device");
		btnAppInformation.setBounds(25, 27, 220, 75);
		panel_8.add(btnAppInformation);

		JButton btnKillApps = new JButton("Kill Apps");
		btnKillApps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] options = new String[] { "Enter package name", "Kill all apps" };
				int response = JOptionPane.showOptionDialog(null, "Which app(s) should be killed?", "Kill Apps",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (response == 0) {
					try {
						JOptionPane.showMessageDialog(null, "You can find an app package name from App Packages List");
						String selectedapp = (JOptionPane.showInputDialog(null, "Enter app's package name:"));
						Process p1 = Runtime.getRuntime().exec("adb shell am force-stop " + selectedapp);
						p1.waitFor();
						JOptionPane.showMessageDialog(null, selectedapp + " has been killed");
					} catch (Exception e1) {
					}
				}
				if (response == 1) {
					try {
						Process p1 = Runtime.getRuntime().exec("adb shell am kill-all");
						p1.waitFor();
						JOptionPane.showMessageDialog(null, "All safe-to-kill apps are killed");
					} catch (Exception e1) {
					}
				}
			}
		});
		btnKillApps.setToolTipText("Kill any app currently running on android device");
		btnKillApps.setBounds(282, 131, 220, 75);
		panel_8.add(btnKillApps);

		JButton btnWifiInformation = new JButton("WiFi Information");
		btnWifiInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Wifiinfo obj = new Wifiinfo();
				obj.setVisible(true);
			}
		});
		btnWifiInformation.setToolTipText("View current wifi information of android device");
		btnWifiInformation.setBounds(282, 236, 220, 75);
		panel_8.add(btnWifiInformation);

		JButton btnAppPackageList = new JButton("App Packages List");
		btnAppPackageList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppPackagesList obj = new AppPackagesList();
				obj.setVisible(true);
			}
		});
		btnAppPackageList.setToolTipText("View all installed app packages list information");
		btnAppPackageList.setBounds(282, 27, 220, 75);
		panel_8.add(btnAppPackageList);

		JLabel lblAdvancedToolsNote = new JLabel(
				"Note: All of the above tools are not supported by every device or ROM");
		lblAdvancedToolsNote.setBounds(25, 345, 736, 15);
		panel_8.add(lblAdvancedToolsNote);
		
		JButton btnUnroot = new JButton("Unroot Device");
		btnUnroot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File file1 = new File(".events");
					if (!file1.exists()) {
						List<String> lines = Arrays.asList("Unroot_Warning_Shown = True");
						Path file = Paths.get(".events");
						Files.write(file, lines, Charset.forName("UTF-8"));
						JOptionPane.showMessageDialog(null,
								"Only the SU Binary will get removed since there are lot of different root management\napplications for android available, I can't regularly search for them and add their\nsupport to this application. If you think this concerns you, you can help me by sending\nme a list of root management applicationsfor android like supersu, kingroot, kingoroot,\netc. But I can't promise that I will add support for each of them. Cheers! :)");
					}
					JOptionPane.showMessageDialog(null, "Unrooting work only on non-production builds of android");
					Process p1 = Runtime.getRuntime().exec("adb pull /system/xbin/su");
					p1.waitFor();
					File file2 = new File("su");
					if (file2.exists() && !file2.isDirectory()) {
						file2.delete();
						Process p2 = Runtime.getRuntime().exec("adb remount");
						p2.waitFor();
						Process p3 = Runtime.getRuntime().exec("adb shell su -c rm -r /system/xbin/su");
						p3.waitFor();
						JOptionPane.showMessageDialog(null, "Operation completed");
					} else {
						JOptionPane.showMessageDialog(null, "This device is not rooted");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnUnroot.setToolTipText("Unroot device by removing SU binary from the device");
		btnUnroot.setBounds(25, 236, 220, 75);
		panel_8.add(btnUnroot);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.WHITE);
		tabbedPane.addTab("Developer", null, panel_10, null);
		panel_10.setLayout(null);

		JButton btnUnpackAPKs = new JButton("Unpack APKs");
		btnUnpackAPKs.addActionListener(new ActionListener() {
			private Component parentFrame;

			public void actionPerformed(ActionEvent e) {
				File path = null;
				JFileChooser chooser1 = new JFileChooser();
				chooser1.setDialogTitle("Select an APK file to extract");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
				chooser1.setCurrentDirectory(new java.io.File("."));
				chooser1.setFileFilter(filter);
				int returnVal = chooser1.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser1.getSelectedFile();
					String filename = chooser1.getSelectedFile().getName();
					JFileChooser chooser2 = new JFileChooser();
					chooser2.setDialogTitle("Extract APK file to");
					chooser2.setCurrentDirectory(new java.io.File("."));
					chooser2.setAcceptAllFileFilterUsed(false);
					chooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int userSelection = chooser2.showSaveDialog(parentFrame);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						path = chooser2.getSelectedFile();
					}
					String outputDir = path.getAbsolutePath();
					java.util.zip.ZipFile zipFile = null;
					try {
						zipFile = new ZipFile(file);
						Enumeration<? extends ZipEntry> entries = zipFile.entries();
						while (entries.hasMoreElements()) {
							ZipEntry entry = entries.nextElement();
							File entryDestination = new File(outputDir, entry.getName());
							if (entry.isDirectory()) {
								entryDestination.mkdirs();
							} else {
								entryDestination.getParentFile().mkdirs();
								InputStream in = zipFile.getInputStream(entry);
								OutputStream out = null;
								out = new FileOutputStream(entryDestination);
								IOUtils.copy(in, out);
								IOUtils.closeQuietly(in);
								out.close();
							}
						}
						zipFile.close();
						JOptionPane.showMessageDialog(null, filename + " has been successfully extracted");
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "An error occured");
					}
				}
			}
		});
		btnUnpackAPKs.setToolTipText("Unpack APKs stored on disk");
		btnUnpackAPKs.setBounds(25, 131, 220, 75);
		panel_10.add(btnUnpackAPKs);

		JButton btnRepackAPKs = new JButton("Repack APKs");
		btnRepackAPKs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Repacker();
			}
		});
		btnRepackAPKs.setToolTipText("Repack previously unpacked APKs and save to them to disk");
		btnRepackAPKs.setBounds(282, 27, 220, 75);
		panel_10.add(btnRepackAPKs);

		JButton btnZipAlignApks = new JButton("Zip Align APKs");
		btnZipAlignApks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				JFileChooser chooser1 = new JFileChooser();
				chooser1.setDialogTitle("Select an APK file to align");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("APK Files", "apk");
				chooser1.setCurrentDirectory(new java.io.File("."));
				chooser1.setFileFilter(filter);
				int returnVal = chooser1.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser1.getSelectedFile();
				}
				String zipalignpath = System.getProperty("user.dir") + "/tools/zipalign/";
				if (SystemUtils.IS_OS_WINDOWS) {
					try {
						Process p1 = Runtime.getRuntime()
								.exec(zipalignpath + "zipalign.exe -f -v 4 " + file + " " + file + "_aligned.apk");
						p1.waitFor();
						JOptionPane.showMessageDialog(null, file.getName() + " aligned successfully!");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (SystemUtils.IS_OS_LINUX) {
					try {
						Process p1 = Runtime.getRuntime()
								.exec(zipalignpath + "./zipalign -f -v 4 " + file + " " + file + "_aligned");
						p1.waitFor();
						JOptionPane.showMessageDialog(null, file.getName() + " aligned successfully!");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnZipAlignApks.setToolTipText("Zip Align APK files stored on disk to improve their performance");
		btnZipAlignApks.setBounds(282, 131, 220, 75);
		panel_10.add(btnZipAlignApks);

		JButton btnSignAPKsAndZips = new JButton("Sign APKs and Zips");
		btnSignAPKsAndZips.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				JFileChooser chooser1 = new JFileChooser();
				chooser1.setDialogTitle("Select an APK or Zip file to sign");
				chooser1.setCurrentDirectory(new java.io.File("."));
				int returnVal = chooser1.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser1.getSelectedFile();
				}
				String APKAndZipSignerPath = System.getProperty("user.dir") + "/tools/APKAndZipSign/";
				try {
					Process p1 = Runtime.getRuntime().exec(APKAndZipSignerPath
							+ "java -jar signapk.jar testkey.x509.pem testkey.pk8 " + file + " " + file + "_signed");
					p1.waitFor();
					JOptionPane.showMessageDialog(null, file.getName() + " signed successfully!");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSignAPKsAndZips.setToolTipText("Repack previously unpacked APKs and save to them to disk");
		btnSignAPKsAndZips.setBounds(541, 27, 220, 75);
		panel_10.add(btnSignAPKsAndZips);

		JButton btnDeodexer = new JButton("Deodexer");
		btnDeodexer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = null;
				String filetype = null;
				JFileChooser chooser1 = new JFileChooser();
				chooser1.setDialogTitle("Select an APK or Zip file to sign");
				chooser1.setCurrentDirectory(new java.io.File("."));
				int returnVal = chooser1.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser1.getSelectedFile();
					filetype = FilenameUtils.getExtension(file.getName());
				}
				String DexToolsPath = System.getProperty("user.dir") + "/tools/dex/";
				String ZipAlignPath = System.getProperty("user.dir") + "/tools/zipalign/";
				try {
					Process p1 = Runtime.getRuntime()
							.exec(DexToolsPath
									+ "java -Xmx1024m -jar baksmali.jar -c :core.jar:bouncycastle.jar:ext.jar:framework.jar:android.policy.jar:services.jar:core-junit.jar -x "
									+ file);
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("java -Xmx1024m -jar smali.jar out -o classes.dex");
					p2.waitFor();
					JOptionPane.showMessageDialog(null,
							"Check the new out folder in\n" + System.getProperty("user.dir"));
					if (filetype.equals("apk")) {
						if (SystemUtils.IS_OS_WINDOWS) {
							Process p3 = Runtime.getRuntime()
									.exec(ZipAlignPath + "zipalign.exe -f -v 4 " + file + " " + file + "_aligned");
							p3.waitFor();
						}
						if (SystemUtils.IS_OS_LINUX) {
							Process p3 = Runtime.getRuntime()
									.exec(ZipAlignPath + "./zipalign -f -v 4 " + file + " " + file + "_aligned");
							p3.waitFor();
						}
					}
					JOptionPane.showMessageDialog(null, file + " deodexed successfully!");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDeodexer.setToolTipText("Deodex APKs and Zips to save ram while their manipulation on device");
		btnDeodexer.setBounds(25, 27, 220, 75);
		panel_10.add(btnDeodexer);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		tabbedPane.addTab("Backup & Restore", null, panel_5, null);
		panel_5.setLayout(null);

		BackupAndRestoreDone = new JLabel("");
		BackupAndRestoreDone.setText("");
		BackupAndRestoreDone.setBounds(758, 70, 300, 220);
		panel_5.add(BackupAndRestoreDone);

		JLabel lblRestoreOperations = new JLabel("Restore Operations");
		lblRestoreOperations.setBounds(541, 12, 142, 36);
		panel_5.add(lblRestoreOperations);

		final JButton btnRestoreFromCustomLocationBackup = new JButton("From Custom Location");
		btnRestoreFromCustomLocationBackup
				.setToolTipText("Restore data to android device from the backup stored somewhere on the computer");
		btnRestoreFromCustomLocationBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Android Backup Files", "ab");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						AppStatus.setText("Restoring may take upto several minutes, please be patient...");
						JOptionPane.showMessageDialog(null,
								"Unlock your device and confirm the restore operation when asked");
						String[] commands = new String[3];
						commands[0] = "adb";
						commands[1] = "restore";
						commands[2] = file.getAbsolutePath();
						AppStatus.setText("Restoring...");
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText("Restore completed successfully!");
						BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnRestoreFromCustomLocationBackup.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnRestoreFromCustomLocationBackup.setBounds(510, 70, 220, 75);
		panel_5.add(btnRestoreFromCustomLocationBackup);

		JLabel lblBackup = new JLabel("Backup Operations");
		lblBackup.setBounds(192, 12, 142, 36);
		panel_5.add(lblBackup);

		final JButton btnBackupInternelStorage = new JButton("Internel Storage");
		btnBackupInternelStorage.setToolTipText("Backup android device internal storage");
		btnBackupInternelStorage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-shared";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupInternelStorage.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupInternelStorage.setBounds(270, 70, 220, 75);
		panel_5.add(btnBackupInternelStorage);

		final JButton btnBackupSingleApp = new JButton("Single App");
		btnBackupSingleApp.setToolTipText("Backup a single app from android device");
		btnBackupSingleApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					String message = JOptionPane.showInputDialog(null, "Please specify a package name to backup");
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = message;
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupSingleApp.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupSingleApp.setBounds(25, 184, 220, 75);
		panel_5.add(btnBackupSingleApp);

		final JButton btnBackupAppAndAppData = new JButton("App and App Data");
		btnBackupAppAndAppData.setToolTipText("Backup app and it's data from android device");
		btnBackupAppAndAppData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-all";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupAppAndAppData.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupAppAndAppData.setBounds(25, 70, 220, 75);
		panel_5.add(btnBackupAppAndAppData);

		final JButton btnBackupWholeDevice = new JButton("Whole Device");
		btnBackupWholeDevice.setToolTipText("Backup whole android device");
		btnBackupWholeDevice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[6];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-all";
					commands[3] = "-apk";
					commands[4] = "-shared";
					commands[5] = "-system";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupWholeDevice.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupWholeDevice.setBounds(25, 303, 220, 75);
		panel_5.add(btnBackupWholeDevice);

		final JButton btnRestorePreviousBackup = new JButton("Previous Backup");
		btnRestorePreviousBackup.setToolTipText("Restore data to android device from the previous backup");
		btnRestorePreviousBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Restoring can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the restore operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "restore";
					commands[2] = "backup.ab";
					AppStatus.setText("Restoring...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Restore completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnRestorePreviousBackup.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRestorePreviousBackup.setBounds(510, 184, 220, 75);
		panel_5.add(btnRestorePreviousBackup);

		final JButton btnBackupSystem = new JButton("System");
		btnBackupSystem.setToolTipText("Backup android device system");
		btnBackupSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackupAndRestoreDone.setText("");
				try {
					AppStatus.setText("Backup can take upto several minutes, please be patient...");
					JOptionPane.showMessageDialog(null,
							"Unlock your device and confirm the backup operation when asked");
					String[] commands = new String[3];
					commands[0] = "adb";
					commands[1] = "backup";
					commands[2] = "-system";
					AppStatus.setText("Performing backup...");
					Process p1 = Runtime.getRuntime().exec(commands, null);
					p1.waitFor();
					AppStatus.setText("Backup completed successfully!");
					BackupAndRestoreDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
					btnBackupSystem.setSelected(false);
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnBackupSystem.setBounds(270, 184, 220, 75);
		panel_5.add(btnBackupSystem);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		tabbedPane.addTab("Rebooter", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblRebootFrom = new JLabel("Reboot from :");
		lblRebootFrom.setBounds(25, 180, 220, 15);
		panel_2.add(lblRebootFrom);

		JLabel lblRebootTo = new JLabel("Reboot to :");
		lblRebootTo.setBounds(25, 12, 220, 15);
		panel_2.add(lblRebootTo);

		JLabel lblNotFor = new JLabel("# Not for Samsung devices");
		lblNotFor.setBounds(514, 359, 238, 19);
		panel_2.add(lblNotFor);

		JLabel lblDeviceMust_1 = new JLabel("Device must be in fastboot mode (Except for Reboot System)");
		lblDeviceMust_1.setBounds(25, 332, 479, 19);
		panel_2.add(lblDeviceMust_1);

		JLabel lblYouMust_1 = new JLabel("* You must have a bootloader that supports fastboot commands");
		lblYouMust_1.setBounds(25, 359, 470, 19);
		panel_2.add(lblYouMust_1);

		JButton btnRebootFromFastboot = new JButton("Fastboot *");
		btnRebootFromFastboot.setToolTipText("Reboot android device from fastboot mode to normal");
		btnRebootFromFastboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("fastboot reboot");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootFromFastboot.setBounds(28, 232, 220, 75);
		panel_2.add(btnRebootFromFastboot);

		JButton btnRebootToBootloaderFromFastboot = new JButton("Fastboot to Bootloader *");
		btnRebootToBootloaderFromFastboot.setToolTipText("Reboot to Bootloader mode while accessing fastboot mode");
		btnRebootToBootloaderFromFastboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("fasboot reboot-bootloader");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToBootloaderFromFastboot.setBounds(281, 232, 220, 75);
		panel_2.add(btnRebootToBootloaderFromFastboot);

		JButton btnRebootToFastboot = new JButton("Fastboot");
		btnRebootToFastboot.setToolTipText("Reboot android device to fastboot mode");
		btnRebootToFastboot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot fastboot");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToFastboot.setBounds(281, 55, 220, 75);
		panel_2.add(btnRebootToFastboot);

		JButton btnRebootToBootloader = new JButton("Bootloader #");
		btnRebootToBootloader.setToolTipText("Reboot android device to bootloader mode");
		btnRebootToBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot bootloader");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToBootloader.setBounds(28, 55, 220, 75);
		panel_2.add(btnRebootToBootloader);

		JButton btnRebootToRecovery = new JButton("Recovery");
		btnRebootToRecovery.setToolTipText("Reboot android device to recovery mode");
		btnRebootToRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot recovery");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootToRecovery.setBounds(532, 55, 220, 75);
		panel_2.add(btnRebootToRecovery);

		JButton btnRebootSystem = new JButton("System");
		btnRebootSystem.setToolTipText("Reboot android device normally");
		btnRebootSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText("Rebooting...");
					Process p1 = Runtime.getRuntime().exec("adb reboot");
					p1.waitFor();
					AppStatus.setText("Done");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnRebootSystem.setBounds(785, 55, 220, 75);
		panel_2.add(btnRebootSystem);

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.WHITE);
		tabbedPane.addTab("Bypass Security", null, panel_9, null);
		panel_9.setLayout(null);

		JLabel lblRootOperationsexperimental = new JLabel("Method #1 : Root Operations (Recommended) [EXPERIMENTAL] :");
		lblRootOperationsexperimental.setBounds(12, 12, 507, 15);
		panel_9.add(lblRootOperationsexperimental);

		JButton btnPattern = new JButton("Pattern #");
		btnPattern.setToolTipText("Remove pattern security from android device (Experimental)");
		btnPattern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Trying to break into security...");
					Process p1 = Runtime.getRuntime().exec("adb shell su -c rm /data/system/gesture.key");
					p1.waitFor();
					AppStatus.setText(
							"Done, now try to unlock the device with a random pattern then change security manually from settings");
				} catch (Exception e1) {
				}
			}
		});

		btnPattern.setBounds(220, 75, 220, 75);
		panel_9.add(btnPattern);

		JButton btnPasswordPin = new JButton("Password/ PIN #");
		btnPasswordPin.setToolTipText("Remove password or pin security from android device (Experimental)");
		btnPasswordPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Trying to break into security...");
					Process p1 = Runtime.getRuntime().exec("adb shell su -c rm /data/system/password.key");
					p1.waitFor();
					AppStatus.setText("Done, check your device...");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnPasswordPin.setBounds(630, 75, 220, 75);
		panel_9.add(btnPasswordPin);

		JLabel lblMayNot = new JLabel("# Works on Android 4.4.x and lower");
		lblMayNot.setBounds(630, 250, 366, 15);
		panel_9.add(lblMayNot);

		JLabel lblNonRoot = new JLabel("Method # 2 : Non - Root/ Root Operations [EXPERIMENTAL] :");
		lblNonRoot.setBounds(12, 191, 507, 15);
		panel_9.add(lblNonRoot);

		JButton btnJellyBeanPatternPinPassword = new JButton("Pattern/ PIN/ Password *");
		btnJellyBeanPatternPinPassword
				.setToolTipText("Remove pattern, pin or password security from android device (Experimental)");
		btnJellyBeanPatternPinPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AppStatus.setText("Trying to break into security...");
					Process p1 = Runtime.getRuntime().exec(
							"adb shell am start -n com.android.settings/com.android.settings.ChooseLockGeneric --ez confirm_credentials false --ei lockscreen.password_type 0 --activity-clear-task");
					p1.waitFor();
					AppStatus.setText("Rebooting...");
					Process p2 = Runtime.getRuntime().exec("adb reboot");
					p2.waitFor();
					AppStatus.setText("Done, check your device...");
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnJellyBeanPatternPinPassword.setBounds(220, 250, 220, 75);
		panel_9.add(btnJellyBeanPatternPinPassword);

		JLabel lblWorksWell = new JLabel("* Works well on Jelly Bean Devices but may or");
		lblWorksWell.setBounds(630, 273, 366, 15);
		panel_9.add(lblWorksWell);

		JLabel lblNewLabel = new JLabel("may not work for older/ newer android versions");
		lblNewLabel.setBounds(640, 293, 356, 15);
		panel_9.add(lblNewLabel);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		tabbedPane.addTab("Logger", null, panel_4, null);
		panel_4.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 72, 1072, 285);
		panel_4.add(scrollPane);

		LogViewer = new JTextArea();
		LogViewer.setEditable(false);
		scrollPane.setViewportView(LogViewer);

		JButton btnSaveAsTextFile = new JButton("Save as a text file");
		btnSaveAsTextFile.setToolTipText("Save printed logcat as a text file on computer");
		btnSaveAsTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (LogViewer.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "No log found, please click view log");
				} else {
					AppStatus.setText("");
					JFrame parentFrame = new JFrame();
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
					fileChooser.setFileFilter(filter);
					fileChooser.setDialogTitle("Save as a text file");
					int userSelection = fileChooser.showSaveDialog(parentFrame);
					if (userSelection == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						FileWriter write = null;
						try {
							write = new FileWriter(fileToSave.getAbsolutePath() + ".txt");
							LogViewer.write(write);
							AppStatus.setText("Logcat saved");
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (write != null)
								try {
									write.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					}
				}
			}
		});

		btnSaveAsTextFile.setBounds(420, 13, 220, 47);
		panel_4.add(btnSaveAsTextFile);

		JButton btnClearLogcat = new JButton("Clear");
		btnClearLogcat.setToolTipText("Clean the printed logcat from the screen");
		btnClearLogcat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LogViewer.setText("");
				File file = new File(".logcat.txt");
				if (file.exists() && !file.isDirectory()) {
					file.delete();
				}
				AppStatus.setText("Logcat cleared");
			}
		});
		btnClearLogcat.setBounds(12, 13, 220, 48);
		panel_4.add(btnClearLogcat);

		JButton btnViewLogcat = new JButton("View Logcat");
		btnViewLogcat.setToolTipText("Print android device logcat on screen");
		btnViewLogcat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppStatus.setText("Generating logcat, please wait a moment...");
				try {
					Process p1 = Runtime.getRuntime().exec("adb logcat -d > /sdcard/.logcat.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb logcat -c");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb pull /sdcard/.logcat.txt");
					p3.waitFor();
					Process p4 = Runtime.getRuntime().exec("adb shell rm /sdcard/.logcat.txt");
					p4.waitFor();
					try {
						Reader reader = new FileReader(new File(".logcat.txt"));
						LogViewer.read(reader, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					File file = new File(".logcat.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
					AppStatus.setText("");
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});

		btnViewLogcat.setBounds(838, 13, 220, 47);
		panel_4.add(btnViewLogcat);

		JLabel lblNoteLogcatG = new JLabel(
				"Note: Logcat generation takes some time, program may not respond for a few moments");
		lblNoteLogcatG.setBounds(12, 364, 1046, 15);
		panel_4.add(lblNoteLogcatG);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		tabbedPane.addTab("Flasher", null, panel, null);
		panel.setLayout(null);

		final JButton btnFlashSystem = new JButton("System");
		btnFlashSystem.setToolTipText("Flash system partition");
		btnFlashSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase system");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "system";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashSystem.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		final JButton btnFlashData = new JButton("Data");
		btnFlashData.setToolTipText("Flash data partition");
		btnFlashData.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase data");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "data";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashData.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		final JButton btnFlashViaRecovery = new JButton("Flash via Recovery");
		btnFlashViaRecovery.setToolTipText("Flash a zip archive using recovery");
		btnFlashViaRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("zip Files", "zip");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						JOptionPane.showMessageDialog(null,
								"Select Update via ADB from recovery menu using physical keys on your device");
						String[] commands = new String[3];
						commands[0] = "adb";
						commands[1] = "sideload";
						commands[2] = file.getAbsolutePath();
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText("Sideloaded...");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashViaRecovery.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		FlasherDone = new JLabel("");
		FlasherDone.setText("");
		FlasherDone.setBounds(760, 29, 300, 220);
		panel.add(FlasherDone);
		btnFlashViaRecovery.setBounds(25, 131, 220, 75);
		panel.add(btnFlashViaRecovery);
		btnFlashData.setBounds(541, 27, 220, 75);
		panel.add(btnFlashData);
		btnFlashSystem.setBounds(282, 236, 220, 75);
		panel.add(btnFlashSystem);

		final JButton btnFlashCache = new JButton("Cache");
		btnFlashCache.setToolTipText("Flash cache partition");
		btnFlashCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase cache");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "cache";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashCache.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashCache.setBounds(282, 27, 220, 75);
		panel.add(btnFlashCache);

		final JButton btnBootImage = new JButton("Boot");
		btnBootImage.setToolTipText("Flash boot partition");
		btnBootImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase boot");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "boot";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnBootImage.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnBootImage.setBounds(25, 27, 220, 75);
		panel.add(btnBootImage);

		final JButton btnFlashZipArchive = new JButton("Zip Archive");
		btnFlashZipArchive.setToolTipText("Flash a zip archive");
		btnFlashZipArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("zip Files", "zip");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						String[] commands = new String[3];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = file.getAbsolutePath();
						Process p1 = Runtime.getRuntime().exec(commands, null);
						p1.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashZipArchive.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashZipArchive.setBounds(541, 236, 220, 75);
		panel.add(btnFlashZipArchive);

		final JButton btnFlashRecovery = new JButton("Recovery");
		btnFlashRecovery.setToolTipText("Flash recovery partition");
		btnFlashRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FlasherDone.setText("");
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase recovery");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "recovery";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashRecovery.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashRecovery.setBounds(541, 131, 220, 75);
		panel.add(btnFlashRecovery);

		JLabel lblYouMust = new JLabel(
				"Note: Your device's bootloader must support fastboot commands and should be in fastboot mode");
		lblYouMust.setBounds(25, 356, 835, 19);
		panel.add(lblYouMust);

		final JButton btnFlashSplash = new JButton("Splash");
		btnFlashSplash.setToolTipText("Flash splash partition");
		btnFlashSplash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase splash");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "splash";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashSplash.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});

		btnFlashSplash.setBounds(25, 236, 220, 75);
		panel.add(btnFlashSplash);

		JButton btnFlashRadio = new JButton("Radio");
		btnFlashRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("IMG Files", "img");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getSelectedFile().getName();
					try {
						AppStatus.setText("Flashing...");
						Process p1 = Runtime.getRuntime().exec("fastboot erase radio");
						p1.waitFor();
						String[] commands = new String[4];
						commands[0] = "fastboot";
						commands[1] = "flash";
						commands[2] = "radio";
						commands[3] = file.getAbsolutePath();
						Process p2 = Runtime.getRuntime().exec(commands, null);
						p2.waitFor();
						AppStatus.setText(filename + "has been successfully flashed on your android device");
						FlasherDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
						btnFlashSplash.setSelected(false);
					} catch (Exception e1) {
						System.err.println(e1);
					}
				}
			}
		});
		btnFlashRadio.setToolTipText("Flash radio partition");
		btnFlashRadio.setBounds(282, 131, 220, 75);
		panel.add(btnFlashRadio);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("Wiper", null, panel_1, null);
		panel_1.setLayout(null);

		WiperDone = new JLabel("");
		WiperDone.setText("");
		WiperDone.setBounds(758, 26, 300, 220);
		panel_1.add(WiperDone);

		JLabel label_13 = new JLabel("** Device must be rooted");
		label_13.setBounds(25, 336, 252, 19);
		panel_1.add(label_13);

		JButton btnWipeRecovery = new JButton("Recovery");
		btnWipeRecovery.setToolTipText("Wipe recovery partition");
		btnWipeRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase cache");
					p1.waitFor();
					AppStatus.setText("Recovery has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeRecovery.setBounds(541, 131, 220, 75);
		panel_1.add(btnWipeRecovery);

		JButton btnWipeBoot = new JButton("Boot");
		btnWipeBoot.setToolTipText("Flash boot partition");
		btnWipeBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase boot");
					p1.waitFor();
					AppStatus.setText("Boot has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeBoot.setBounds(25, 27, 220, 75);
		panel_1.add(btnWipeBoot);

		JButton btnWipeSystem = new JButton("System");
		btnWipeSystem.setToolTipText("Wipe system partition");
		btnWipeSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase system");
					p1.waitFor();
					AppStatus.setText("System has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeSystem.setBounds(282, 236, 220, 75);
		panel_1.add(btnWipeSystem);

		JButton btnWipeSplash = new JButton("Splash");
		btnWipeSplash.setToolTipText("Wipe splash partition");
		btnWipeSplash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase splash");
					p1.waitFor();
					AppStatus.setText("Splash has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeSplash.setBounds(25, 236, 220, 75);
		panel_1.add(btnWipeSplash);

		JButton btnWipeData = new JButton("Data");
		btnWipeData.setToolTipText("Wipe data partition");
		btnWipeData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase data");
					p1.waitFor();
					AppStatus.setText("Data has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeData.setBounds(25, 131, 220, 75);
		panel_1.add(btnWipeData);

		JButton btnFlashDalvikCache = new JButton("Dalvik Cache **");
		btnFlashDalvikCache.setToolTipText("Wipe dalvik cache");
		btnFlashDalvikCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("adb shell su -c rm * /data/dalvik-cache");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb shell su -c rm * /cache/dalvik-cache");
					p2.waitFor();
					AppStatus.setText("Dalvik Cache has been wiped! Now rebooting device...");
					Process p3 = Runtime.getRuntime().exec("adb reboot");
					p3.waitFor();
					AppStatus.setText("Done");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnFlashDalvikCache.setBounds(541, 27, 220, 75);
		panel_1.add(btnFlashDalvikCache);

		JButton btnWipeCache = new JButton("Cache");
		btnWipeCache.setToolTipText("Wipe cache partition");
		btnWipeCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase cache");
					p1.waitFor();
					AppStatus.setText("Cache has been wiped! Now rebooting device...");
					Process p2 = Runtime.getRuntime().exec("adb reboot");
					p2.waitFor();
					AppStatus.setText("Done");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnWipeCache.setBounds(282, 27, 220, 75);
		panel_1.add(btnWipeCache);

		JButton btnWipeRadio = new JButton("Radio");
		btnWipeRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WiperDone.setText("");
				try {
					AppStatus.setText("Wiping...");
					Process p1 = Runtime.getRuntime().exec("fastboot erase radio");
					p1.waitFor();
					AppStatus.setText("Radio has been wiped");
					WiperDone.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Done.png")));
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnWipeRadio.setToolTipText("Wipe radio partition");
		btnWipeRadio.setBounds(282, 131, 220, 75);
		panel_1.add(btnWipeRadio);

		JLabel lblNoteYourDevices = new JLabel(
				"Note: Your device's bootloader must support fastboot commands and should be in fastboot mode");
		lblNoteYourDevices.setBounds(25, 357, 835, 19);
		panel_1.add(lblNoteYourDevices);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		tabbedPane.addTab("Bootloader", null, panel_3, null);
		panel_3.setLayout(null);

		JLabel label_17 = new JLabel("Note: Don't worry if the app says to connect your device while");
		label_17.setBounds(66, 320, 600, 19);
		panel_3.add(label_17);

		JLabel label_18 = new JLabel("android is not booted ex. fastboot, bootloader, booting etc.");
		label_18.setBounds(66, 337, 600, 19);
		panel_3.add(label_18);

		JLabel lblOnlyForNexus = new JLabel(
				"Works only with specific devices ex. Nexus, Android One, FEW MTK devices etc.");
		lblOnlyForNexus.setBounds(66, 351, 600, 24);
		panel_3.add(lblOnlyForNexus);

		JButton btnUnlockBootloader = new JButton("Unlock Bootloader");
		btnUnlockBootloader.setToolTipText("Unlock android device bootloader");
		btnUnlockBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AppStatus.setText(
							"Unlocking bootloader will factory reset your device and may void your device warranty!");
					JOptionPane.showMessageDialog(null,
							"You will need to re-enable USB debugging later as your device will get factory reset");
					Process p1 = Runtime.getRuntime().exec("adb reboot bootloader");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("fastboot oem unlock");
					p2.waitFor();
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnUnlockBootloader.setBounds(282, 27, 220, 75);
		panel_3.add(btnUnlockBootloader);

		JButton btnLockBootloader = new JButton("Lock Bootloader");
		btnLockBootloader.setToolTipText("Lock android device bootloader");
		btnLockBootloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb reboot bootloader");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("fastboot oem lock");
					p2.waitFor();
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});

		btnLockBootloader.setBounds(25, 27, 220, 75);
		panel_3.add(btnLockBootloader);

		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		tabbedPane.addTab("Cryptography", null, panel_6, null);
		panel_6.setLayout(null);

		JButton btnSHA512 = new JButton("SHA-512");
		btnSHA512.setToolTipText("Calculate SHA-512 sum of a file");
		btnSHA512.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha512Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA512.setBounds(541, 131, 220, 75);
		panel_6.add(btnSHA512);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 332, 900, 25);
		panel_6.add(scrollPane_2);

		InputCrypto = new JTextArea();
		InputCrypto.setToolTipText("Input sum to be compared with calculated sum");
		scrollPane_2.setViewportView(InputCrypto);

		JLabel lblLabelCalculatedSum = new JLabel("Calculated Sum :");
		lblLabelCalculatedSum.setBounds(12, 240, 235, 17);
		panel_6.add(lblLabelCalculatedSum);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(12, 268, 900, 28);
		panel_6.add(scrollPane_1);

		CalculatedCrypto = new JTextArea();
		CalculatedCrypto.setToolTipText("Calclated sum");
		scrollPane_1.setViewportView(CalculatedCrypto);
		CalculatedCrypto.setEditable(false);

		JButton btnSHA384 = new JButton("SHA-384");
		btnSHA384.setToolTipText("Calculate SHA-384 sum of a file");
		btnSHA384.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha384Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA384.setBounds(282, 131, 220, 75);
		panel_6.add(btnSHA384);

		JButton btnSHA256 = new JButton("SHA-256");
		btnSHA256.setToolTipText("Calculate SHA-256 sum of a file");
		btnSHA256.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha256Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA256.setBounds(25, 131, 220, 75);
		panel_6.add(btnSHA256);

		JButton btnSHA1 = new JButton("SHA-1");
		btnSHA1.setToolTipText("Calculate SHA-1 sum of a file");
		btnSHA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.sha1Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnSHA1.setBounds(541, 27, 220, 75);
		panel_6.add(btnSHA1);

		JButton btnMD5 = new JButton("MD5");
		btnMD5.setToolTipText("Calculate MD5 sum of a file");
		btnMD5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = new File("");
					AppStatus.setText("Calculating...");
					CalculatedCrypto.setText(DigestUtils.md5Hex(file.getAbsolutePath()));
					AppStatus.setText("");
				}
			}
		});

		btnMD5.setBounds(282, 27, 220, 75);
		panel_6.add(btnMD5);

		JLabel lblInputSumTo = new JLabel("Input Sum to be compared :");
		lblInputSumTo.setBounds(12, 308, 235, 15);
		panel_6.add(lblInputSumTo);

		JButton btnCompare = new JButton("Compare");
		btnCompare.setToolTipText("Click to compare calculated sum and input sum");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (InputCrypto.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select algorithm and a file");
				}
				if (CalculatedCrypto.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please input a sum to be compared");
				} else {
					if (InputCrypto.getText().equalsIgnoreCase(CalculatedCrypto.getText())) {
						JOptionPane.showMessageDialog(null, "Both sums are matched");
					} else {
						JOptionPane.showMessageDialog(null, "Sums are not matched!");
					}
				}
			}
		});
		btnCompare.setBounds(924, 268, 134, 89);
		panel_6.add(btnCompare);

		JButton btnClearCalculatedCrypto = new JButton("Clear");
		btnClearCalculatedCrypto.setToolTipText("Clear the calculated sum");
		btnClearCalculatedCrypto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CalculatedCrypto.setText("");
				InputCrypto.setText("");
			}
		});
		btnClearCalculatedCrypto.setBounds(25, 27, 220, 75);
		panel_6.add(btnClearCalculatedCrypto);

		JLabel label_2 = new JLabel("");
		label_2.setBounds(50, 0, 1038, 256);
		label_2.setIcon(new ImageIcon(Interface.class.getResource("/graphics/Interface_logo.png")));
		contentPane.add(label_2);

		Thread t = new Thread(r); // Background services
		t.start();

		Runtime.getRuntime().addShutdownHook(new Thread() { // Exit sequence
			public void run() {
				try {
					System.out.println("Killing ADB instance...");
					Process p1 = Runtime.getRuntime().exec("adb kill-server");
					p1.waitFor();
					System.out.println("Cleaning cache...");
					File file2 = new File(".CheckADBConnection");
					if (file2.exists() && !file2.isDirectory()) {
						file2.delete();
					}
					File file = new File("su");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
					System.out.println("Droid PC Suite terminated");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
