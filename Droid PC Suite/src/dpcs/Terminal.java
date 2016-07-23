package dpcs;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
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

		JMenuItem mntmADBCommandList = new JMenuItem("ADB Command list");
		mntmADBCommandList.setToolTipText("View ADB command list");
		mntmADBCommandList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					URL obj = Terminal.class.getResource("/others/adbhelp.txt");
					File obj2 = new File(obj.toURI());
					Reader reader = new FileReader(new File(obj2.toURI()));
					TerminalEmulatorDisplay.read(reader, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmADBCommandList);

		JMenuItem mntmAndroidShellCommand = new JMenuItem("Android shell command list");
		mntmAndroidShellCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime()
							.exec("adb shell ls /system/bin/ > /sdcard/.androidshellcommands.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.androidshellcommands.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.androidshellcommands.txt");
					p3.waitFor();
					try {
						Reader reader = new FileReader(new File(".androidshellcommands.txt"));
						TerminalEmulatorDisplay.read(reader, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					File file = new File(".androidshellcommands.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});
		mntmAndroidShellCommand.setToolTipText("View list of android shell supported command list");
		mnHelp.add(mntmAndroidShellCommand);
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
						TerminalEmulatorDisplay.setText("ADB terminal emulator is highly experimental! Expect bugs!"); // Message
																														// for
																														// linux
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
				} catch (Exception e1) {
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