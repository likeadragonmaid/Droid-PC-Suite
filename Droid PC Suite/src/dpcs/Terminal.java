/*****************************************************************************
 * dpcs/Terminal.java: Experimental ADB and Fastboot Terminal for Droid PC Suite
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

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Terminal extends JFrame {
	JTextArea TerminalEmulatorDisplay;
	private JPanel contentPane;
	private JTextField commandinput;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Terminal frame = new Terminal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Terminal() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Terminal.class.getResource("/graphics/Icon.png")));
		setTitle("ADB Terminal Emulator");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 448, 370);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmADBCommandList = new JMenuItem("ADB command list");
		mntmADBCommandList.setToolTipText("View ADB command list");
		mntmADBCommandList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					InputStreamReader reader = new InputStreamReader(
							getClass().getResourceAsStream("/others/adbhelp.txt"));
					TerminalEmulatorDisplay.read(reader, "");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmADBCommandList);

		JMenuItem mntmAndroidShellCommandList = new JMenuItem("Android shell command list");
		mntmAndroidShellCommandList.setToolTipText("View Android shell command list");
		mntmAndroidShellCommandList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime()
							.exec("adb shell ls /system/bin/ > /sdcard/.androidshellcommands.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.androidshellcommands.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.androidshellcommands.txt");
					p3.waitFor();
					Reader reader = new FileReader(new File(".androidshellcommands.txt"));
					TerminalEmulatorDisplay.read(reader, "");
					File file = new File(".androidshellcommands.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		mntmAndroidShellCommandList.setToolTipText("View list of android shell supported command list");
		mnHelp.add(mntmAndroidShellCommandList);

		JMenuItem mntmFastbootCommandList = new JMenuItem("Fastboot command list");
		mntmFastbootCommandList.setToolTipText("View Fastboot command list");
		mntmFastbootCommandList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					InputStreamReader reader = new InputStreamReader(
							getClass().getResourceAsStream("/others/fastboothelp.txt"));
					TerminalEmulatorDisplay.read(reader, "");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmFastbootCommandList);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 0, 426, 218);
		contentPane.add(scrollPane);

		TerminalEmulatorDisplay = new JTextArea();
		TerminalEmulatorDisplay.setText("ADB terminal emulator is highly experimental! Expect bugs!");
		TerminalEmulatorDisplay.setBackground(Color.BLACK);
		TerminalEmulatorDisplay.setEditable(false);
		TerminalEmulatorDisplay.setForeground(Color.WHITE);
		scrollPane.setViewportView(TerminalEmulatorDisplay);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 227, 357, 34);
		contentPane.add(scrollPane_1);

		commandinput = new JTextField();
		commandinput.setToolTipText("Type the commands here for execution");
		scrollPane_1.setViewportView(commandinput);
		commandinput.setColumns(10);
		commandinput.setText("Start typing command here...");

		JButton Sendcommandbutton = new JButton("Go");
		Sendcommandbutton.setToolTipText("Execute the specified command");
		Sendcommandbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String commandcheck = commandinput.getText();
					if (commandcheck.equalsIgnoreCase("exit")) {
						dispose();
					}
					if (commandcheck.equals("")) {
						JOptionPane.showMessageDialog(null, "Please input a command!");
					}
					if (commandcheck.equals("Start typing command here...")) {
						JOptionPane.showMessageDialog(null, "Please input a command!");
					}
					if (commandcheck.equalsIgnoreCase("adb")) {
						JOptionPane.showMessageDialog(null, "Command incomplete!");
					}
					if (commandcheck.equalsIgnoreCase("fastboot")) {
						JOptionPane.showMessageDialog(null, "Command incomplete!");
					}
					if (commandcheck.equalsIgnoreCase("clear")) {
						TerminalEmulatorDisplay.setText("");
					}
					if (commandcheck.equalsIgnoreCase("cls")) {
						TerminalEmulatorDisplay.setText("ADB terminal emulator is highly experimental! Expect bugs!");
						TerminalEmulatorDisplay.setText("");
					}
					if (commandcheck.equalsIgnoreCase("reset")) {
						TerminalEmulatorDisplay.setText("");
					}
					Process p1 = Runtime.getRuntime().exec(commandinput.getText());
					p1.waitFor();
					commandinput.setText("");
					BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
					String line = reader.readLine();
					while (line != null) {
						Terminal.this.TerminalEmulatorDisplay.append("\n" + line);
						line = reader.readLine();
					}
				} catch (IOException | InterruptedException e1) {
					Terminal.this.TerminalEmulatorDisplay.append("" + e1);
				}
			}
		});
		Sendcommandbutton.setBounds(379, 230, 57, 25);
		contentPane.add(Sendcommandbutton);

		JButton ADBbutton = new JButton("$ adb");
		ADBbutton.setToolTipText("Insert \"adb\" in front of command");
		ADBbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandinput.setText("adb ");
			}
		});
		ADBbutton.setBounds(12, 273, 117, 25);
		contentPane.add(ADBbutton);

		JButton Fastbootbutton = new JButton("$ fastboot");
		Fastbootbutton.setToolTipText("Insert \"fastboot\" in front of command");
		Fastbootbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandinput.setText("fastboot ");
			}
		});
		Fastbootbutton.setBounds(165, 273, 117, 25);
		contentPane.add(Fastbootbutton);

		JButton clearbutton = new JButton("Clear");
		clearbutton.setToolTipText("Clear the terminal");
		clearbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TerminalEmulatorDisplay.setText("");
			}
		});
		clearbutton.setBounds(319, 273, 117, 25);
		contentPane.add(clearbutton);
	}
}