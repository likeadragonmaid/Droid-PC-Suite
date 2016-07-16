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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Memoryinfo extends JFrame {
	JTextArea MemoryInformationViewer;
	private JPanel contentPane;
	private JButton btnRefresh;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Memoryinfo frame = new Memoryinfo();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Memoryinfo() {
		setTitle("Memory Information");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Memoryinfo.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 525);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 378, 399);
		contentPane.add(scrollPane);

		MemoryInformationViewer = new JTextArea();
		MemoryInformationViewer.setEditable(false);
		scrollPane.setViewportView(MemoryInformationViewer);

		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb pull /proc/meminfo");
					p1.waitFor();
					try {
						Reader reader = new FileReader(new File("meminfo"));
						MemoryInformationViewer.read(reader, "");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					File file = new File("meminfo");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnRefresh.setToolTipText("Refetch memory information from android device");
		btnRefresh.setBounds(22, 420, 120, 47);
		contentPane.add(btnRefresh);

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
						MemoryInformationViewer.write(write);
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
		btnSaveToFile.setToolTipText("Save memory information from screen to a file on the computer");
		btnSaveToFile.setBounds(219, 420, 120, 47);
		contentPane.add(btnSaveToFile);
		try {
			Process p1 = Runtime.getRuntime().exec("adb pull /proc/meminfo");
			p1.waitFor();
			try {
				Reader reader = new FileReader(new File("meminfo"));
				MemoryInformationViewer.read(reader, "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			File file = new File("meminfo");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}