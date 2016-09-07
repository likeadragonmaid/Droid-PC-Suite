package updater;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
public class UpdaterGUI extends JFrame {

	private JPanel contentPane;
	Double AvailableUpdate, AppVersion;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdaterGUI frame = new UpdaterGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("resource")
	public UpdaterGUI() {
		try {
			URL url1 = new URL("https://raw.githubusercontent.com/kvsjxd/Droid-PC-Suite/master/.release-version.txt");
			ReadableByteChannel obj1 = Channels.newChannel(url1.openStream());
			FileOutputStream outputstream1 = new FileOutputStream(".release-version.txt");
			outputstream1.getChannel().transferFrom(obj1, 0, Long.MAX_VALUE);
			URL url2 = new URL("https://raw.githubusercontent.com/kvsjxd/Droid-PC-Suite/master/.release-changelog.txt");
			ReadableByteChannel obj2 = Channels.newChannel(url2.openStream());
			FileOutputStream outputstream2 = new FileOutputStream(".release-changelog.txt");
			outputstream2.getChannel().transferFrom(obj2, 0, Long.MAX_VALUE);
			FileReader file = new FileReader(".release-version.txt");
			BufferedReader reader = new BufferedReader(file);
			String DownloadedString = reader.readLine();
			File file2 = new File(".release-version.txt");
			if (file2.exists() && !file2.isDirectory()) {
				file2.delete();
			}
			AvailableUpdate = Double.parseDouble(DownloadedString);
			InputStreamReader reader2 = new InputStreamReader(
					getClass().getResourceAsStream("/others/app-version.txt"));
			String tmp = IOUtils.toString(reader2);
			AppVersion = Double.parseDouble(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(UpdaterGUI.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Updater");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 415);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAppVersion = new JLabel("App Version: v" + AppVersion);
		lblAppVersion.setBounds(12, 12, 222, 15);
		contentPane.add(lblAppVersion);

		JLabel lblUpdateVersion = new JLabel("Update Version: v" + AvailableUpdate);
		lblUpdateVersion.setBounds(12, 30, 222, 15);
		contentPane.add(lblUpdateVersion);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 51, 422, 281);
		contentPane.add(scrollPane);

		JTextArea UpdateChangelogViewer = new JTextArea();
		scrollPane.setViewportView(UpdateChangelogViewer);

		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip Files", "zip");
				fileChooser.setFileFilter(filter);
				fileChooser.setDialogTitle("Save as");
				int userSelection = fileChooser.showSaveDialog(parentFrame);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					try {
						URL url = new URL("https://github.com/kvsjxd/Droid-PC-Suite/releases/download/"
								+ AvailableUpdate + "/DPCS.v" + AvailableUpdate + ".Stable.zip");
						ReadableByteChannel obj = Channels.newChannel(url.openStream());
						FileOutputStream outputstream = new FileOutputStream(fileToSave.getAbsolutePath() + ".zip");
						outputstream.getChannel().transferFrom(obj, 0, Long.MAX_VALUE);
						JOptionPane.showMessageDialog(null,
								"Download complete!\nPlease delete this version and extract the downloaded zip\nwhich is saved at "
										+ fileToSave.getAbsolutePath() + ".zip");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnDownload.setBounds(140, 344, 117, 25);
		contentPane.add(btnDownload);
		try {
			FileReader reader3 = new FileReader(new File(".release-changelog.txt"));
			UpdateChangelogViewer.read(reader3, "");
			File file3 = new File(".release-changelog.txt");
			if (file3.exists() && !file3.isDirectory()) {
				file3.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}