package dpcs;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Workarounds extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Workarounds frame = new Workarounds();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Workarounds() {
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Common Workarounds");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Workarounds.class.getResource("/graphics/Icon.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 565, 270);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblcommonworkaround6 = new JLabel("* WARNING! Connect only one android device at a time!");
		lblcommonworkaround6.setToolTipText("Or Droid PC Suite will not function normally!");
		lblcommonworkaround6.setForeground(Color.RED);
		lblcommonworkaround6.setBounds(25, 186, 504, 24);
		contentPane.add(lblcommonworkaround6);

		JLabel lblcommonworkaround3_2 = new JLabel("and fastboot files with their newer or older versions.");
		lblcommonworkaround3_2.setForeground(Color.BLACK);
		lblcommonworkaround3_2.setBounds(35, 82, 478, 24);
		contentPane.add(lblcommonworkaround3_2);

		JLabel lblcommonworkaround3 = new JLabel("* If some operations do not work on windows, please replace adb");
		lblcommonworkaround3.setForeground(Color.BLACK);
		lblcommonworkaround3.setBounds(25, 65, 506, 24);
		contentPane.add(lblcommonworkaround3);

		JLabel lblcommonworkaround1 = new JLabel(
				"* Use USB 2.0 to connect android device to system instead of USB 3.0.");
		lblcommonworkaround1.setForeground(Color.BLACK);
		lblcommonworkaround1.setBounds(25, 12, 506, 25);
		contentPane.add(lblcommonworkaround1);

		JLabel lblcommonworkaround2 = new JLabel("* You may also need to install your android device usb drivers.");
		lblcommonworkaround2.setForeground(Color.BLACK);
		lblcommonworkaround2.setBounds(25, 32, 506, 38);
		contentPane.add(lblcommonworkaround2);

		JLabel lblcommonworkaround7 = new JLabel("* Click here for help with connectivity of android device on Linux.");
		lblcommonworkaround7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblcommonworkaround7.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblcommonworkaround7.setForeground(Color.BLUE);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Desktop.getDesktop()
							.browse(new URL("https://androidonlinux.wordpress.com/2013/05/12/setting-up-adb-on-linux/")
									.toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		lblcommonworkaround7.setBounds(25, 206, 504, 24);
		contentPane.add(lblcommonworkaround7);

		JLabel lblcommonworkaroud4 = new JLabel("* If File Manager does not work, make sure you are using Launcher");
		lblcommonworkaroud4.setToolTipText("i.e. Launcher for Microsoft Windows.exe or Launcher_for _Linux.sh");
		lblcommonworkaroud4.setForeground(Color.BLACK);
		lblcommonworkaroud4.setBounds(25, 101, 496, 24);
		contentPane.add(lblcommonworkaroud4);

		JLabel lblcommonworkaround4_2 = new JLabel("to Run Droid PC Suite instead of directly running DPCS.jar");
		lblcommonworkaround4_2.setToolTipText("i.e. Launcher for Microsoft Windows.exe or Launcher_for _Linux.sh");
		lblcommonworkaround4_2.setForeground(Color.BLACK);
		lblcommonworkaround4_2.setBounds(35, 119, 486, 24);
		contentPane.add(lblcommonworkaround4_2);

		JLabel lblcommonworkaround5 = new JLabel("* Don't worry if the app says to connect your device while android");
		lblcommonworkaround5.setBounds(25, 145, 486, 19);
		contentPane.add(lblcommonworkaround5);

		JLabel lblcommonworkaround5_2 = new JLabel("is not booted ex. fastboot, bootloader, booting etc.");
		lblcommonworkaround5_2.setBounds(35, 164, 476, 19);
		contentPane.add(lblcommonworkaround5_2);
	}
}
