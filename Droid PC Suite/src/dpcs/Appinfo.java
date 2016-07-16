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
public class Appinfo extends JFrame {
	JTextArea AppInformationViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Appinfo frame = new Appinfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Appinfo() {
		setTitle("App Information");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Appinfo.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 415);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 552, 311);
		contentPane.add(scrollPane);

		AppInformationViewer = new JTextArea();
		AppInformationViewer.setEditable(false);
		scrollPane.setViewportView(AppInformationViewer);

		JButton btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
						AppInformationViewer.write(write);
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
		});
		btnSaveToFile.setToolTipText("Save app information information from screen to a file on the computer");
		btnSaveToFile.setBounds(212, 323, 120, 47);
		contentPane.add(btnSaveToFile);

		try {
			JOptionPane.showMessageDialog(null, "You can find an app package name from App Packages List");
			String selectedapp = (JOptionPane.showInputDialog(null, "Enter app's package name:"));
			Process p1 = Runtime.getRuntime()
					.exec("adb shell dumpsys meminfo " + selectedapp + "> /sdcard/.appinfo.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.appinfo.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.appinfo.txt");
			p3.waitFor();
			try {
				Reader reader = new FileReader(new File(".appinfo.txt"));
				AppInformationViewer.read(reader, "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			File file = new File(".appinfo.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}