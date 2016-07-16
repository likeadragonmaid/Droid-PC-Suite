package dpcs;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Devicefeatures extends JFrame {
	JTextArea DeviceFeaturesViewer;
	private JPanel contentPane;
	private JButton btnSaveToFile;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Devicefeatures frame = new Devicefeatures();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Devicefeatures() {
		setTitle("Device Features");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Devicefeatures.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 570);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 378, 471);
		contentPane.add(scrollPane);

		DeviceFeaturesViewer = new JTextArea();
		DeviceFeaturesViewer.setEditable(false);
		scrollPane.setViewportView(DeviceFeaturesViewer);

		btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
						DeviceFeaturesViewer.write(write);
					} catch (Exception e1) {
						e1.printStackTrace();
					} finally {
						if (write != null)
							try {
								write.close();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
					}
				}
			}
		});
		btnSaveToFile.setToolTipText("Save battery information from screen to a file on the computer");
		btnSaveToFile.setBounds(122, 478, 120, 47);
		contentPane.add(btnSaveToFile);
		try {
			Process p1 = Runtime.getRuntime().exec("adb shell pm list features > /sdcard/.devicefeatures.txt");
			p1.waitFor();
			Process p2 = Runtime.getRuntime().exec("adb pull /sdcard/.devicefeatures.txt");
			p2.waitFor();
			Process p3 = Runtime.getRuntime().exec("adb shell rm /sdcard/.devicefeatures.txt");
			p3.waitFor();
			try {
				Reader reader = new FileReader(new File(".devicefeatures.txt"));
				DeviceFeaturesViewer.read(reader, "");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			File file = new File(".devicefeatures.txt");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e1) {
			System.err.println(e1);
		}
	}
}
