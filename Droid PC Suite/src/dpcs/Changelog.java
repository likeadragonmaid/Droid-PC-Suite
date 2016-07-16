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
	JTextArea ChangelogViewer;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Changelog frame = new Changelog();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Changelog() {
		setTitle("Changelog Tracker");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Changelog.class.getResource("/graphics/Icon.png")));
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

		ChangelogViewer = new JTextArea();
		ChangelogViewer.setEditable(false);
		scrollPane.setViewportView(ChangelogViewer);
		try {
			URL obj = Changelog.class.getResource("/others/changelog.txt");
			File obj2 = new File(obj.toURI());
			Reader reader = new FileReader(new File(obj2.toURI()));
			ChangelogViewer.read(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
