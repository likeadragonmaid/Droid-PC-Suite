package dpcs;

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
import java.awt.Color;

@SuppressWarnings("serial")
public class Buildpropeditor extends JFrame {
	JTextArea Buildpropeditorwindow;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Buildpropeditor frame = new Buildpropeditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Buildpropeditor() {
		setResizable(false);
		setTitle("build.prop Editor");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Buildpropeditor.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 520);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 698, 415);
		contentPane.add(scrollPane);

		Buildpropeditorwindow = new JTextArea();
		scrollPane.setViewportView(Buildpropeditorwindow);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setToolTipText("Refetch build.prop from android device");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Process p1 = Runtime.getRuntime().exec("adb pull /system/build.prop");
					p1.waitFor();
					try {
						Reader reader = new FileReader(new File("build.prop"));
						Buildpropeditorwindow.read(reader, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					File file = new File("build.prop");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		});
		btnRefresh.setBounds(21, 427, 200, 47);
		contentPane.add(btnRefresh);

		JButton btnSaveonpc = new JButton("Save to PC");
		btnSaveonpc.setToolTipText("Save the printed text on sceen as a file");
		btnSaveonpc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("prop Files", "prop");
				fileChooser.setFileFilter(filter);
				fileChooser.setDialogTitle("Save to PC");
				int userSelection = fileChooser.showSaveDialog(parentFrame);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					FileWriter write = null;
					try {
						write = new FileWriter(fileToSave.getAbsolutePath() + ".prop");
						Buildpropeditorwindow.write(write);
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
		btnSaveonpc.setBounds(250, 427, 200, 47);
		contentPane.add(btnSaveonpc);

		JButton btnPushtosdcard = new JButton("Push to sdcard");
		btnPushtosdcard.setToolTipText("Push the text printed on screen to your device's sdcard as a build.prop file");
		btnPushtosdcard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileWriter write = null;
				try {
					write = new FileWriter("build.prop");
					Buildpropeditorwindow.write(write);
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
				try {
					Process p1 = Runtime.getRuntime().exec("adb push build.prop /sdcard/");
					p1.waitFor();
					File file = new File("build.prop");
					if (file.exists() && !file.isDirectory()) {
						file.delete();
					}
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnPushtosdcard.setBounds(478, 427, 200, 47);
		contentPane.add(btnPushtosdcard);

		try {
			Process p1 = Runtime.getRuntime().exec("adb pull /system/build.prop");
			p1.waitFor();
			try {
				Reader reader = new FileReader(new File("build.prop"));
				Buildpropeditorwindow.read(reader, "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			File file = new File("build.prop");
			if (file.exists() && !file.isDirectory()) {
				file.delete();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}