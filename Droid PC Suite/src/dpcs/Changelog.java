package dpcs;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Changelog extends JFrame {
	JTextArea LicenseViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApacheLicense frame = new ApacheLicense();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Changelog() {
		setTitle("Changelog Tracker");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApacheLicense.class.getResource("/graphics/Icon.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 378, 518);
		contentPane.add(scrollPane);

		LicenseViewer = new JTextArea();
		LicenseViewer.setEditable(false);
		scrollPane.setViewportView(LicenseViewer);
		try {
			URL licenseobj = ApacheLicense.class.getResource("/others/changelog.txt");
			File licenseobj2 = new File(licenseobj.toURI());
			Reader reader = new FileReader(new File(licenseobj2.toURI()));
			LicenseViewer.read(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
