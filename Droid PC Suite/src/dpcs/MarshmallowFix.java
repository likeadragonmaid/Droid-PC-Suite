package dpcs;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MarshmallowFix extends JFrame {
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarshmallowFix frame = new MarshmallowFix();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MarshmallowFix() {
		setResizable(false);
		setTitle("Help for connecting Marshmellow device");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MarshmallowFix.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 479, 451);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnRefresh = new JButton("Download");
		btnRefresh.setToolTipText("Download the file to flash");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop()
							.browse(new URL(
									"http://forum.xda-developers.com/attachment.php?attachmentid=3761901&d=1464187904")
											.toURI());
				} catch (Exception e1) {
					System.err.println(e1);
				}
			}
		});
		btnRefresh.setBounds(125, 357, 220, 47);
		contentPane.add(btnRefresh);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 471, 340);
		contentPane.add(scrollPane);

		JTextArea MarshmallowFixviewer = new JTextArea();
		MarshmallowFixviewer.setEditable(false);
		scrollPane.setViewportView(MarshmallowFixviewer);

		try {
			URL helpobj = ADBHelp.class.getResource("/others/marshmallow-fix.txt");
			File helpobj2 = new File(helpobj.toURI());
			Reader reader = new FileReader(new File(helpobj2.toURI()));
			MarshmallowFixviewer.read(reader, "");
		} catch (Exception e1) {
			System.err.println(e1);
		}
	}
}
