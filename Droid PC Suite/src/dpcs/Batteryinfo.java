package dpcs;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Batteryinfo extends JFrame {
	JTextArea BatteryInformationViewer;
	private JPanel contentPane;
	private JButton btnRefresh;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Batteryinfo frame = new Batteryinfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Batteryinfo() {
		setTitle("Battery Information");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Batteryinfo.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 343);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 378, 238);
		contentPane.add(scrollPane);

		BatteryInformationViewer = new JTextArea();
		BatteryInformationViewer.setEditable(false);
		scrollPane.setViewportView(BatteryInformationViewer);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb shell dumpsys battery > /sdcard/.batteryinfo.txt");
					p1.waitFor();
					Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.batteryinfo.txt");
					p2.waitFor();
					Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.batteryinfo.txt");
					p3.waitFor();
					try {
						Reader reader = new FileReader(new File(".batteryinfo.txt"));
						BatteryInformationViewer.read(reader, "");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					File file = new File(".batteryinfo.txt");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnRefresh.setToolTipText("Refetch battery information from android device");
		btnRefresh.setBounds(26, 250, 120, 47);
		contentPane.add(btnRefresh);

		JButton btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (BatteryInformationViewer.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Cannot save empty file!");
				} else {
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
							BatteryInformationViewer.write(write);
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
		btnSaveToFile.setToolTipText("Save battery information from screen to a file on the computer");
		btnSaveToFile.setBounds(223, 250, 120, 47);
		contentPane.add(btnSaveToFile);
		try {
			Process p1 = Runtime.getRuntime().exec("adb shell dumpsys battery > /sdcard/.batteryinfo.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.batteryinfo.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.batteryinfo.txt");
			p3.waitFor();
			try {
				Reader reader = new FileReader(new File(".batteryinfo.txt"));
				BatteryInformationViewer.read(reader, "");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			File file = new File(".batteryinfo.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e1) {
			System.err.println(e1);
		}
	}
}